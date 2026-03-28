package com.example.demo.interfaces.rest;

import java.util.UUID;

public record LoginResponse(UUID userId, String email, String token, String refreshToken) {
}
