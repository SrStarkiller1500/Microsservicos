package com.example.storefront.event;

import com.example.storefront.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.exchange.name:ecommerce}")
    private String exchangeName;

    public void publishOrderCreated(Order order) throws Exception {
        Map<String, Object> event = new HashMap<>();
        event.put("order_id", order.getId().toString());
        event.put("user_id", order.getUserId());
        event.put("total_amount", order.getTotalAmount());
        event.put("items", order.getItems());
        rabbitTemplate.convertAndSend(exchangeName, "storefront.order.created", objectMapper.writeValueAsString(event));
        System.out.println("📦 Storefront published: storefront.order.created");
    }
}
