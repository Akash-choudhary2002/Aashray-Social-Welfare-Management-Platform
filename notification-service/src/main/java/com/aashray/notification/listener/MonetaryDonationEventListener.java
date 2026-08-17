package com.aashray.notification.listener;

import com.aashray.notification.config.RabbitMQConfig;
import com.aashray.notification.dto.MonetaryDonationEvent;
import com.aashray.notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MonetaryDonationEventListener {

    private static final Logger log = LoggerFactory.getLogger(MonetaryDonationEventListener.class);

    private final EmailService emailService;

    public MonetaryDonationEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.MONETARY_DONATION_SUCCESS_QUEUE)
    public void onMonetaryDonationSuccess(MonetaryDonationEvent event) {
        log.info("Consumed monetary.donation.success event for donationId={}", event.getDonationId());

        String subject = "Thank you for your donation — Receipt " + event.getReferenceNumber();
        String body = "Hi " + event.getDonorName() + ",\n\n"
                + "We've received your donation of INR " + event.getAmount() + ".\n"
                + "Reference number: " + event.getReferenceNumber() + "\n\n"
                + "Thank you for supporting Aashray.";

        emailService.send("MONETARY_DONATION_SUCCESS", event.getDonorEmail(), subject, body);
    }
}
