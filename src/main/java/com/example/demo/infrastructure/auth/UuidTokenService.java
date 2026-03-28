package com.example.demo.infrastructure.auth;

import com.example.demo.application.auth.TokenService;

import java.util.UUID;

public class UuidTokenService implements TokenService {
    @Override
    public String issueToken(String subject) {
        return UUID.randomUUID().toString();
    }
}
