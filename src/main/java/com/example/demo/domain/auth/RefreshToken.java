package com.example.demo.domain.auth;

import java.time.Instant;
import java.util.UUID;

public record RefreshToken(
        UUID id,
        UUID userId,
        String tokenHash,
        Instant expiresAt,
        boolean revoked
) {
    public boolean isValid() {
        return !revoked && Instant.now().isBefore(expiresAt);
    }

    public RefreshToken revoke() {
        return new RefreshToken(id, userId, tokenHash, expiresAt, true);
    }
}
