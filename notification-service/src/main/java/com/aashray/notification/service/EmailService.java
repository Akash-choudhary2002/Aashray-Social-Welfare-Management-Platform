package com.aashray.notification.service;

import com.aashray.notification.entity.NotificationLog;
import com.aashray.notification.entity.NotificationStatus;
import com.aashray.notification.repository.NotificationLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Central email dispatcher for every notification event.
 *
 * When notification.email.enabled=false (the default — no SMTP
 * credentials required), the email is not actually dispatched: it is
 * logged and persisted with status SIMULATED so the whole event-driven
 * flow (RabbitMQ publish -> consume -> "email") can be demoed and
 * verified end-to-end without a real mailbox. Set EMAIL_ENABLED=true
 * plus MAIL_USERNAME/MAIL_PASSWORD to send real emails via JavaMailSender.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final NotificationLogRepository logRepository;

    @Value("${notification.email.enabled:false}")
    private boolean emailEnabled;

    @Value("${notification.email.from}")
    private String fromAddress;

    public EmailService(JavaMailSender mailSender, NotificationLogRepository logRepository) {
        this.mailSender = mailSender;
        this.logRepository = logRepository;
    }

    public void send(String eventType, String recipientEmail, String subject, String body) {
        NotificationLog.NotificationLogBuilder logBuilder = NotificationLog.builder()
                .eventType(eventType)
                .recipientEmail(recipientEmail)
                .subject(subject)
                .body(body);

        boolean canDispatch = emailEnabled && recipientEmail != null && !recipientEmail.startsWith("unresolved:");

        if (!canDispatch) {
            log.info("[SIMULATED EMAIL] to={} subject='{}' -> {}", recipientEmail, subject, body);
            logRepository.save(logBuilder.status(NotificationStatus.SIMULATED).build());
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(recipientEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);

            log.info("Email sent to={} subject='{}'", recipientEmail, subject);
            logRepository.save(logBuilder.status(NotificationStatus.SENT).build());
        } catch (Exception e) {
            log.error("Failed to send email to={} subject='{}': {}", recipientEmail, subject, e.getMessage());
            logRepository.save(logBuilder.status(NotificationStatus.FAILED).errorMessage(e.getMessage()).build());
        }
    }
}
