package com.example.storefront.service;

import com.example.storefront.dto.CreateOrderRequest;
import com.example.storefront.entity.Order;
import com.example.storefront.event.RabbitMQProducer;
import com.example.storefront.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final RabbitMQProducer producer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Order createOrder(CreateOrderRequest request) throws Exception {
        BigDecimal total = request.items().stream()
                .map(i -> BigDecimal.valueOf(i.price() * i.quantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String itemsJson = objectMapper.writeValueAsString(request.items());

        Order order = Order.builder()
                .userId(UUID.fromString(request.userId()))
                .items(itemsJson)
                .totalAmount(total)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        Order saved = repository.save(order);

        producer.publishOrderCreated(saved);

        return saved;
    }

    public Order getOrder(UUID id) {
        return repository.findById(id).orElse(null);
    }

    public Order updateStatus(UUID id, String status) {
        var opt = repository.findById(id);
        if (opt.isEmpty()) return null;
        var o = opt.get();
        o.setStatus(status);
        return repository.save(o);
    }
}
