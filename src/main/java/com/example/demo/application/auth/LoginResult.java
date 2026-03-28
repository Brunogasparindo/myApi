package com.example.demo.application.auth;

import java.util.UUID;

public record LoginResult(UUID userId, String email, String token) {
}
