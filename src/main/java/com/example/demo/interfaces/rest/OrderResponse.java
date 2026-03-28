package com.example.demo.interfaces.rest;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponse(UUID id, String customerId, BigDecimal amount) {}
