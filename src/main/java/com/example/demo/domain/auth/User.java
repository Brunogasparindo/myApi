package com.example.demo.domain.auth;

import java.util.Objects;

public class User {
    private final UserId id;
    private final Email email;
    private final PasswordHash passwordHash;

    public User(UserId id, Email email, PasswordHash passwordHash) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash must not be null");
    }

    public UserId id() {
        return id;
    }

    public Email email() {
        return email;
    }

    public PasswordHash passwordHash() {
        return passwordHash;
    }
}
