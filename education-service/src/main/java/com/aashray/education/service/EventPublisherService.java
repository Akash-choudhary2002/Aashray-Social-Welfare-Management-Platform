package com.aashray.education.service;

import com.aashray.education.config.RabbitMQConfig;
import com.aashray.education.dto.EducatorAssignedEvent;
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

    public void publishEducatorAssigned(EducatorAssignedEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_EXCHANGE, RabbitMQConfig.EDUCATOR_ASSIGNED_KEY, event);
            log.info("Published educator.assigned event for assignmentId={}", event.getAssignmentId());
        } catch (Exception e) {
            log.error("Failed to publish educator.assigned event for assignmentId={}: {}",
                    event.getAssignmentId(), e.getMessage());
        }
    }
}
