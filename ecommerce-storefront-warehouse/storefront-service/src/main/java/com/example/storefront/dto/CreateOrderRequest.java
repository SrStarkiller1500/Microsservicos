package com.example.storefront.dto;

import java.util.List;

public record CreateOrderRequest(
        String userId,
        List<Item> items
) {
    public record Item(String skuId, String name, int quantity, double price) {}
}
