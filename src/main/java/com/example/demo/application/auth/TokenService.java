package com.example.demo.application.auth;

public interface TokenService {
    String issueToken(String subject);
}
