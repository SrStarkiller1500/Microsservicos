package com.example.warehouse.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class WarehouseConsumer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    @Value("${app.exchange.name:ecommerce}")
    private String exchangeName;

    @RabbitListener(queues = "order.created.queue")
    public void handleOrderCreated(String payload) throws Exception {
        JsonNode node = objectMapper.readTree(payload);
        String orderId = node.get("order_id").asText();
        System.out.println("Warehouse: received storefront.order.created for " + orderId);

        boolean success = random.nextInt(100) < 80;

        if (success) {
            var event = objectMapper.createObjectNode();
            event.put("order_id", orderId);
            event.put("status", "RESERVED");
            rabbitTemplate.convertAndSend(exchangeName, "warehouse.product.reserved", objectMapper.writeValueAsString(event));
            System.out.println("Warehouse: published warehouse.product.reserved for " + orderId);
        } else {
            var event = objectMapper.createObjectNode();
            event.put("order_id", orderId);
            event.put("status", "RESERVATION_FAILED");
            rabbitTemplate.convertAndSend(exchangeName, "warehouse.product.reservation_failed", objectMapper.writeValueAsString(event));
            System.out.println("Warehouse: published warehouse.product.reservation_failed for " + orderId);
        }
    }
}
