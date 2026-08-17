package com.aashray.food.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Reuses the same "aashray.notification.exchange" topic exchange that
 * Auth Service publishes to. Food Donation Service is a producer only
 * here; the Notification Service (Phase 3) owns the consuming queues.
 */
@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATION_EXCHANGE = "aashray.notification.exchange";

    public static final String DONATION_ACCEPTED_KEY = "donation.accepted";
    public static final String DONATION_PICKED_UP_KEY = "donation.picked_up";
    public static final String DONATION_DELIVERED_KEY = "donation.delivered";

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE, true, false);
    }

    @Bean
    public Queue donationAcceptedQueue() {
        return QueueBuilder.durable("donation.accepted.queue").build();
    }

    @Bean
    public Queue donationPickedUpQueue() {
        return QueueBuilder.durable("donation.picked_up.queue").build();
    }

    @Bean
    public Queue donationDeliveredQueue() {
        return QueueBuilder.durable("donation.delivered.queue").build();
    }

    @Bean
    public Binding donationAcceptedBinding(@Qualifier("donationAcceptedQueue") Queue donationAcceptedQueue,
                                            TopicExchange notificationExchange) {
        return BindingBuilder.bind(donationAcceptedQueue).to(notificationExchange).with(DONATION_ACCEPTED_KEY);
    }

    @Bean
    public Binding donationPickedUpBinding(@Qualifier("donationPickedUpQueue") Queue donationPickedUpQueue,
                                            TopicExchange notificationExchange) {
        return BindingBuilder.bind(donationPickedUpQueue).to(notificationExchange).with(DONATION_PICKED_UP_KEY);
    }

    @Bean
    public Binding donationDeliveredBinding(@Qualifier("donationDeliveredQueue") Queue donationDeliveredQueue,
                                             TopicExchange notificationExchange) {
        return BindingBuilder.bind(donationDeliveredQueue).to(notificationExchange).with(DONATION_DELIVERED_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
