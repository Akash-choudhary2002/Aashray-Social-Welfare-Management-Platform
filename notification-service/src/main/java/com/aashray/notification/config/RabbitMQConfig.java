package com.aashray.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Notification Service is the sole consumer of the shared
 * "aashray.notification.exchange" topic exchange. Every producer
 * (Auth, Food Donation, Monetary Donation, Education, Volunteer, ...) declares this exchange
 * and its own queues/bindings too — RabbitMQ treats identical
 * declarations as idempotent, so declaring them again here is safe and
 * lets this service start up and bind its listeners independently of
 * producer startup order.
 */
@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATION_EXCHANGE = "aashray.notification.exchange";

    public static final String USER_REGISTERED_QUEUE = "user.registered.queue";
    public static final String DONATION_ACCEPTED_QUEUE = "donation.accepted.queue";
    public static final String DONATION_PICKED_UP_QUEUE = "donation.picked_up.queue";
    public static final String DONATION_DELIVERED_QUEUE = "donation.delivered.queue";
    public static final String MONETARY_DONATION_SUCCESS_QUEUE = "monetary.donation.success.queue";
    public static final String EDUCATOR_ASSIGNED_QUEUE = "educator.assigned.queue";
    public static final String VOLUNTEER_ASSIGNED_QUEUE = "volunteer.assigned.queue";

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE, true, false);
    }

    @Bean
    public Queue userRegisteredQueue() {
        return QueueBuilder.durable(USER_REGISTERED_QUEUE).build();
    }

    @Bean
    public Queue donationAcceptedQueue() {
        return QueueBuilder.durable(DONATION_ACCEPTED_QUEUE).build();
    }

    @Bean
    public Queue donationPickedUpQueue() {
        return QueueBuilder.durable(DONATION_PICKED_UP_QUEUE).build();
    }

    @Bean
    public Queue donationDeliveredQueue() {
        return QueueBuilder.durable(DONATION_DELIVERED_QUEUE).build();
    }

    @Bean
    public Queue monetaryDonationSuccessQueue() {
        return QueueBuilder.durable(MONETARY_DONATION_SUCCESS_QUEUE).build();
    }

    @Bean
    public Queue educatorAssignedQueue() {
        return QueueBuilder.durable(EDUCATOR_ASSIGNED_QUEUE).build();
    }

    @Bean
    public Queue volunteerAssignedQueue() {
        return QueueBuilder.durable(VOLUNTEER_ASSIGNED_QUEUE).build();
    }

    @Bean
    public Binding userRegisteredBinding(TopicExchange notificationExchange) {
        return BindingBuilder.bind(userRegisteredQueue()).to(notificationExchange).with("user.registered");
    }

    @Bean
    public Binding donationAcceptedBinding(TopicExchange notificationExchange) {
        return BindingBuilder.bind(donationAcceptedQueue()).to(notificationExchange).with("donation.accepted");
    }

    @Bean
    public Binding donationPickedUpBinding(TopicExchange notificationExchange) {
        return BindingBuilder.bind(donationPickedUpQueue()).to(notificationExchange).with("donation.picked_up");
    }

    @Bean
    public Binding donationDeliveredBinding(TopicExchange notificationExchange) {
        return BindingBuilder.bind(donationDeliveredQueue()).to(notificationExchange).with("donation.delivered");
    }

    @Bean
    public Binding monetaryDonationSuccessBinding(TopicExchange notificationExchange) {
        return BindingBuilder.bind(monetaryDonationSuccessQueue()).to(notificationExchange).with("monetary.donation.success");
    }

    @Bean
    public Binding educatorAssignedBinding(TopicExchange notificationExchange) {
        return BindingBuilder.bind(educatorAssignedQueue()).to(notificationExchange).with("educator.assigned");
    }

    @Bean
    public Binding volunteerAssignedBinding(TopicExchange notificationExchange) {
        return BindingBuilder.bind(volunteerAssignedQueue()).to(notificationExchange).with("volunteer.assigned");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
