package com.aashray.auth.service;

import com.aashray.auth.config.RabbitMQConfig;
import com.aashray.auth.dto.UserRegisteredEvent;
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

    public void publishUserRegistered(UserRegisteredEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    RabbitMQConfig.USER_REGISTERED_ROUTING_KEY,
                    event
            );
            log.info("Published user.registered event for userId={}", event.getUserId());
        } catch (Exception e) {
            // Never let a notification failure break registration
            log.error("Failed to publish user.registered event for userId={}: {}",
                    event.getUserId(), e.getMessage());
        }
    }
}
