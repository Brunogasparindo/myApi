package com.example.demo.application.auth;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException() {
        super("Too many login attempts. Please try again later.");
    }
}
