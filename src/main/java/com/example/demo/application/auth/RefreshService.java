package com.example.demo.application.auth;

import com.example.demo.domain.auth.RefreshToken;
import com.example.demo.domain.auth.RefreshTokenRepository;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class RefreshService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;
    private final long refreshTokenExpirationMs;

    public RefreshService(RefreshTokenRepository refreshTokenRepository, TokenService tokenService,
                          long refreshTokenExpirationMs) {
        this.refreshTokenRepository = Objects.requireNonNull(refreshTokenRepository, "refreshTokenRepository must not be null");
        this.tokenService = Objects.requireNonNull(tokenService, "tokenService must not be null");
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public RefreshResult refresh(RefreshCommand command) {
        Objects.requireNonNull(command, "command must not be null");
        if (command.refreshToken() == null || command.refreshToken().isBlank()) {
            throw new InvalidRefreshTokenException();
        }

        String tokenHash = LoginService.hashToken(command.refreshToken());
        RefreshToken existing = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (!existing.isValid()) {
            throw new InvalidRefreshTokenException();
        }

        // Revoke old token
        refreshTokenRepository.save(existing.revoke());

        // Issue new access token and rotate refresh token
        String newAccessToken = tokenService.issueToken(existing.userId().toString());
        String newRawRefreshToken = LoginService.generateRawToken();
        RefreshToken newRefreshToken = new RefreshToken(
                UUID.randomUUID(),
                existing.userId(),
                LoginService.hashToken(newRawRefreshToken),
                Instant.now().plusMillis(refreshTokenExpirationMs),
                false
        );
        refreshTokenRepository.save(newRefreshToken);

        return new RefreshResult(newAccessToken, newRawRefreshToken);
    }
}
