package com.aashray.food.service;

import com.aashray.food.config.RabbitMQConfig;
import com.aashray.food.dto.DonationStatusEvent;
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

    public void publish(String routingKey, DonationStatusEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_EXCHANGE, routingKey, event);
            log.info("Published {} event for donationId={}", routingKey, event.getDonationId());
        } catch (Exception e) {
            log.error("Failed to publish {} event for donationId={}: {}",
                    routingKey, event.getDonationId(), e.getMessage());
        }
    }
}
