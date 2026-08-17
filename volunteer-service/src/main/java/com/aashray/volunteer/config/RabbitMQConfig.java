package com.aashray.volunteer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Reuses the same "aashray.notification.exchange" topic exchange that
 * Auth Service publishes to. Volunteer Service is a producer only
 * here; the Notification Service owns the consuming queue.
 */
@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATION_EXCHANGE = "aashray.notification.exchange";

    public static final String VOLUNTEER_ASSIGNED_KEY = "volunteer.assigned";

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE, true, false);
    }

    @Bean
    public Queue volunteerAssignedQueue() {
        return QueueBuilder.durable("volunteer.assigned.queue").build();
    }

    @Bean
    public Binding volunteerAssignedBinding(Queue volunteerAssignedQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(volunteerAssignedQueue).to(notificationExchange).with(VOLUNTEER_ASSIGNED_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
