package com.example.demo.domain.auth;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(RefreshToken token);
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
