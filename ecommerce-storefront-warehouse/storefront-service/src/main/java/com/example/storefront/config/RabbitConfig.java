package com.example.storefront.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${app.exchange.name:ecommerce}")
    private String exchangeName;

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
    }

    @Bean
    public Queue orderCreatedQueue() {
        return QueueBuilder.durable("order.created.queue").build();
    }

    @Bean
    public Queue productReservedQueue() {
        return QueueBuilder.durable("product.reserved.queue").build();
    }

    @Bean
    public Queue productReservationFailedQueue() {
        return QueueBuilder.durable("product.reservation_failed.queue").build();
    }

    @Bean
    public Binding bindOrderCreated() {
        return BindingBuilder.bind(orderCreatedQueue()).to((TopicExchange) exchange()).with("storefront.order.created").noargs();
    }

    @Bean
    public Binding bindProductReserved() {
        return BindingBuilder.bind(productReservedQueue()).to((TopicExchange) exchange()).with("warehouse.product.reserved").noargs();
    }

    @Bean
    public Binding bindProductReservationFailed() {
        return BindingBuilder.bind(productReservationFailedQueue()).to((TopicExchange) exchange()).with("warehouse.product.reservation_failed").noargs();
    }
}
