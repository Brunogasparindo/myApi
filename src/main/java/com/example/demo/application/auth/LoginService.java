package com.example.demo.application.auth;

import com.example.demo.domain.auth.Email;
import com.example.demo.domain.auth.RefreshToken;
import com.example.demo.domain.auth.RefreshTokenRepository;
import com.example.demo.domain.auth.User;
import com.example.demo.domain.auth.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Objects;
import java.util.UUID;

public class LoginService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenExpirationMs;

    public LoginService(UserRepository userRepository, PasswordHasher passwordHasher,
                        TokenService tokenService, RefreshTokenRepository refreshTokenRepository,
                        long refreshTokenExpirationMs) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
        this.passwordHasher = Objects.requireNonNull(passwordHasher, "passwordHasher must not be null");
        this.tokenService = Objects.requireNonNull(tokenService, "tokenService must not be null");
        this.refreshTokenRepository = Objects.requireNonNull(refreshTokenRepository, "refreshTokenRepository must not be null");
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public LoginResult login(LoginCommand command) {
        Objects.requireNonNull(command, "command must not be null");
        if (command.email() == null || command.email().isBlank()) {
            throw new AuthenticationFailedException();
        }
        if (command.password() == null || command.password().isBlank()) {
            throw new AuthenticationFailedException();
        }

        User user = userRepository.findByEmail(Email.of(command.email()))
                .orElseThrow(AuthenticationFailedException::new);

        if (!passwordHasher.matches(command.password(), user.passwordHash().value())) {
            throw new AuthenticationFailedException();
        }

        String accessToken = tokenService.issueToken(user.id().value().toString());
        String rawRefreshToken = generateRawToken();
        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID(),
                user.id().value(),
                hashToken(rawRefreshToken),
                Instant.now().plusMillis(refreshTokenExpirationMs),
                false
        );
        refreshTokenRepository.save(refreshToken);

        return new LoginResult(user.id().value(), user.email().value(), accessToken, rawRefreshToken);
    }

    static String generateRawToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    static String hashToken(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
