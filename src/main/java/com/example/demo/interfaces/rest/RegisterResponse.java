package com.example.demo.interfaces.rest;

import java.util.UUID;

public record RegisterResponse(UUID userId, String email) {}
