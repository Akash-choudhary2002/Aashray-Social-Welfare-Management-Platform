package com.aashray.notification.listener;

import com.aashray.notification.config.RabbitMQConfig;
import com.aashray.notification.dto.EducatorAssignedEvent;
import com.aashray.notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes education-service's educator.assigned event. Same scope
 * limitation as FoodDonationEventListener: education-service carries
 * educator/NGO names and ids but not email addresses (that data lives
 * only in Auth Service), so the notification is always logged with an
 * "unresolved" recipient rather than dispatched.
 */
@Component
public class EducationEventListener {

    private static final Logger log = LoggerFactory.getLogger(EducationEventListener.class);

    private final EmailService emailService;

    public EducationEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.EDUCATOR_ASSIGNED_QUEUE)
    public void onEducatorAssigned(EducatorAssignedEvent event) {
        log.info("Consumed educator.assigned event for assignmentId={}", event.getAssignmentId());

        String subject = "An educator has been assigned";
        String body = "Hi " + event.getEducatorName() + ",\n\n"
                + event.getNgoName() + " has assigned you to teach \"" + event.getSubject()
                + "\" to " + event.getStudentName() + ".\n\n"
                + "Thank you for supporting their education.";

        emailService.send("EDUCATOR_ASSIGNED", unresolvedEmail(event.getEducatorId(), event.getEducatorName()), subject, body);
    }

    private String unresolvedEmail(Long userId, String fullName) {
        return "unresolved:" + userId + ":" + fullName;
    }
}
