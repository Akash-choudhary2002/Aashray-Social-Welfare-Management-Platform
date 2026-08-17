package com.aashray.notification.listener;

import com.aashray.notification.config.RabbitMQConfig;
import com.aashray.notification.dto.DonationStatusEvent;
import com.aashray.notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes food-donation-service's lifecycle events. The DonationStatusEvent
 * payload carries donor/NGO names and ids but not their email addresses
 * (food-donation-service intentionally never stores emails — that's
 * Auth Service's data). In a full production build this listener would
 * resolve the email via an internal Auth Service lookup; here the
 * notification is always logged (email marked "unresolved") so the
 * event-driven flow is still fully visible and auditable end-to-end.
 */
@Component
public class FoodDonationEventListener {

    private static final Logger log = LoggerFactory.getLogger(FoodDonationEventListener.class);

    private final EmailService emailService;

    public FoodDonationEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.DONATION_ACCEPTED_QUEUE)
    public void onDonationAccepted(DonationStatusEvent event) {
        log.info("Consumed donation.accepted event for donationId={}", event.getDonationId());

        String subject = "Your food donation was accepted";
        String body = "Hi " + event.getDonorName() + ",\n\n"
                + "Your donation of \"" + event.getFoodName() + "\" has been accepted by "
                + event.getNgoName() + ".\n\n"
                + "Thank you for your generosity.";

        emailService.send("DONATION_ACCEPTED", unresolvedEmail(event.getDonorId(), event.getDonorName()), subject, body);
    }

    @RabbitListener(queues = RabbitMQConfig.DONATION_PICKED_UP_QUEUE)
    public void onDonationPickedUp(DonationStatusEvent event) {
        log.info("Consumed donation.picked_up event for donationId={}", event.getDonationId());

        String subject = "Your food donation was picked up";
        String body = "Hi " + event.getDonorName() + ",\n\n"
                + event.getNgoName() + " has picked up your donation of \"" + event.getFoodName() + "\".";

        emailService.send("DONATION_PICKED_UP", unresolvedEmail(event.getDonorId(), event.getDonorName()), subject, body);
    }

    @RabbitListener(queues = RabbitMQConfig.DONATION_DELIVERED_QUEUE)
    public void onDonationDelivered(DonationStatusEvent event) {
        log.info("Consumed donation.delivered event for donationId={}", event.getDonationId());

        String subject = "Your food donation has been delivered";
        String body = "Hi " + event.getDonorName() + ",\n\n"
                + "Great news! Your donation of \"" + event.getFoodName() + "\" has been delivered to beneficiaries by "
                + event.getNgoName() + ".\n\n"
                + "Thank you for making a difference.";

        emailService.send("DONATION_DELIVERED", unresolvedEmail(event.getDonorId(), event.getDonorName()), subject, body);
    }

    private String unresolvedEmail(Long userId, String fullName) {
        return "unresolved:" + userId + ":" + fullName;
    }
}
