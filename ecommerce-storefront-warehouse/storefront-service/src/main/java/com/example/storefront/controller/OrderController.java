package com.example.storefront.controller;

import com.example.storefront.dto.CreateOrderRequest;
import com.example.storefront.entity.Order;
import com.example.storefront.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody CreateOrderRequest req) throws Exception {
        return ResponseEntity.ok(orderService.createOrder(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable UUID id) {
        Order o = orderService.getOrder(id);
        return (o != null) ? ResponseEntity.ok(o) : ResponseEntity.notFound().build();
    }
}
