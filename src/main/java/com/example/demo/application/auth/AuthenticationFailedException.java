package com.example.demo.application.auth;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException() {
        super("invalid credentials");
    }
}
