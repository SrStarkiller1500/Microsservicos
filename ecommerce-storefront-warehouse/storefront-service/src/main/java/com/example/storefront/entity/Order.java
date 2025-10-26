package com.example.storefront.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;

    @Column(columnDefinition = "jsonb")
    private String items;

    private BigDecimal totalAmount;

    private String status;

    private LocalDateTime createdAt;
}
