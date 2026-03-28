package com.example.demo.interfaces.rest;

import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        List<String> roles,
        List<String> authorities,
        List<String> permissions,
        String createdAt,
        String updatedAt
) {}
