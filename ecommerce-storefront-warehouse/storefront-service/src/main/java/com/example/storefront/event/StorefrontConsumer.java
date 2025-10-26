package com.example.storefront.event;

import com.example.storefront.service.OrderService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorefrontConsumer {

    private final OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "product.reserved.queue")
    public void handleProductReserved(String payload) throws Exception {
        JsonNode node = objectMapper.readTree(payload);
        String orderId = node.get("order_id").asText();
        orderService.updateStatus(java.util.UUID.fromString(orderId), "RESERVED");
        System.out.println("Storefront: received warehouse.product.reserved for " + orderId);
    }

    @RabbitListener(queues = "product.reservation_failed.queue")
    public void handleReservationFailed(String payload) throws Exception {
        JsonNode node = objectMapper.readTree(payload);
        String orderId = node.get("order_id").asText();
        orderService.updateStatus(java.util.UUID.fromString(orderId), "RESERVATION_FAILED");
        System.out.println("Storefront: received warehouse.product.reservation_failed for " + orderId);
    }
}
