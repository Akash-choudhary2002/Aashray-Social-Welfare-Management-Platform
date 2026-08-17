package com.aashray.monetary.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Reuses the same "aashray.notification.exchange" topic exchange that
 * Auth Service and Food Donation Service publish to. Monetary Donation
 * Service is a producer only here; the Notification Service owns the
 * consuming queues.
 */
@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATION_EXCHANGE = "aashray.notification.exchange";

    public static final String MONETARY_DONATION_SUCCESS_KEY = "monetary.donation.success";

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE, true, false);
    }

    @Bean
    public Queue monetaryDonationSuccessQueue() {
        return QueueBuilder.durable("monetary.donation.success.queue").build();
    }

    @Bean
    public Binding monetaryDonationSuccessBinding(Queue monetaryDonationSuccessQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(monetaryDonationSuccessQueue).to(notificationExchange).with(MONETARY_DONATION_SUCCESS_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
