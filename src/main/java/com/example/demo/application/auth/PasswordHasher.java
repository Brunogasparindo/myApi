package com.example.demo.application.auth;

public interface PasswordHasher {
    boolean matches(String rawPassword, String hashedPassword);
}
