package com.example.demo.application.auth;

public class EmailAlreadyTakenException extends RuntimeException {
    public EmailAlreadyTakenException() {
        super("Email is already registered");
    }
}
