package com.aashray.monetary.service;

import com.aashray.monetary.config.RabbitMQConfig;
import com.aashray.monetary.dto.MonetaryDonationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisherService {

    private static final Logger log = LoggerFactory.getLogger(EventPublisherService.class);

    private final RabbitTemplate rabbitTemplate;

    public EventPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishDonationSuccess(MonetaryDonationEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    RabbitMQConfig.MONETARY_DONATION_SUCCESS_KEY,
                    event
            );
            log.info("Published monetary.donation.success event for donationId={}", event.getDonationId());
        } catch (Exception e) {
            log.error("Failed to publish monetary.donation.success event for donationId={}: {}",
                    event.getDonationId(), e.getMessage());
        }
    }
}
