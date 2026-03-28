package com.example.demo.infrastructure.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderJpaEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(nullable = false)
    private BigDecimal amount;

    protected OrderJpaEntity() {}

    public OrderJpaEntity(UUID id, String customerId, BigDecimal amount) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
    }

    public UUID getId() { return id; }
    public String getCustomerId() { return customerId; }
    public BigDecimal getAmount() { return amount; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
