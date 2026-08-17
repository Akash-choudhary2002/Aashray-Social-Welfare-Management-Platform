package com.aashray.notification.listener;

import com.aashray.notification.config.RabbitMQConfig;
import com.aashray.notification.dto.UserRegisteredEvent;
import com.aashray.notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredListener {

    private static final Logger log = LoggerFactory.getLogger(UserRegisteredListener.class);

    private final EmailService emailService;

    public UserRegisteredListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_REGISTERED_QUEUE)
    public void onUserRegistered(UserRegisteredEvent event) {
        log.info("Consumed user.registered event for userId={}", event.getUserId());

        String subject = "Welcome to Aashray, " + event.getFullName() + "!";
        String body = "Hi " + event.getFullName() + ",\n\n"
                + "Your Aashray account has been created successfully as a " + event.getRole() + ".\n"
                + "You can now log in and start using the platform.\n\n"
                + "Thank you for joining Aashray.";

        emailService.send("USER_REGISTERED", event.getEmail(), subject, body);
    }
}
