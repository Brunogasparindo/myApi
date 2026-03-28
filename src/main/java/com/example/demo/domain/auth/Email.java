package com.example.demo.domain.auth;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String value) {
    private static final Pattern BASIC_EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Email {
        Objects.requireNonNull(value, "email must not be null");
        if (value.isBlank() || !BASIC_EMAIL.matcher(value).matches()) {
            throw new IllegalArgumentException("invalid email");
        }
    }

    public static Email of(String raw) {
        return new Email(raw.trim());
    }
}
