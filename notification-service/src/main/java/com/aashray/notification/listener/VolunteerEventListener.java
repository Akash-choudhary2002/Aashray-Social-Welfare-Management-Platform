package com.aashray.notification.listener;

import com.aashray.notification.config.RabbitMQConfig;
import com.aashray.notification.dto.VolunteerAssignedEvent;
import com.aashray.notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes volunteer-service's volunteer.assigned event. Same scope
 * limitation as FoodDonationEventListener: volunteer-service carries
 * volunteer/NGO names and ids but not email addresses (that data lives
 * only in Auth Service), so the notification is always logged with an
 * "unresolved" recipient rather than dispatched.
 */
@Component
public class VolunteerEventListener {

    private static final Logger log = LoggerFactory.getLogger(VolunteerEventListener.class);

    private final EmailService emailService;

    public VolunteerEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.VOLUNTEER_ASSIGNED_QUEUE)
    public void onVolunteerAssigned(VolunteerAssignedEvent event) {
        log.info("Consumed volunteer.assigned event for taskId={}", event.getTaskId());

        String subject = "You've been assigned a new task";
        String body = "Hi " + event.getVolunteerName() + ",\n\n"
                + event.getNgoName() + " has assigned you a new task: \"" + event.getTaskTitle() + "\".\n\n"
                + "Thank you for volunteering your time.";

        emailService.send("VOLUNTEER_ASSIGNED", unresolvedEmail(event.getVolunteerId(), event.getVolunteerName()), subject, body);
    }

    private String unresolvedEmail(Long userId, String fullName) {
        return "unresolved:" + userId + ":" + fullName;
    }
}
