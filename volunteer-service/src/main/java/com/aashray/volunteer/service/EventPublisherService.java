package com.aashray.volunteer.service;

import com.aashray.volunteer.config.RabbitMQConfig;
import com.aashray.volunteer.dto.VolunteerAssignedEvent;
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

    public void publishVolunteerAssigned(VolunteerAssignedEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_EXCHANGE, RabbitMQConfig.VOLUNTEER_ASSIGNED_KEY, event);
            log.info("Published volunteer.assigned event for taskId={}", event.getTaskId());
        } catch (Exception e) {
            log.error("Failed to publish volunteer.assigned event for taskId={}: {}", event.getTaskId(), e.getMessage());
        }
    }
}
