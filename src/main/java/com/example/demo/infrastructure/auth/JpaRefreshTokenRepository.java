package com.example.demo.infrastructure.auth;

import com.example.demo.domain.auth.RefreshToken;
import com.example.demo.domain.auth.RefreshTokenRepository;

import java.util.Optional;

public class JpaRefreshTokenRepository implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository jpaRepository;

    public JpaRefreshTokenRepository(RefreshTokenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(RefreshToken token) {
        RefreshTokenJpaEntity entity = jpaRepository
                .findById(token.id())
                .orElseGet(() -> new RefreshTokenJpaEntity(
                        token.id(), token.userId(), token.tokenHash(), token.expiresAt(), token.revoked()));
        entity.setRevoked(token.revoked());
        jpaRepository.save(entity);
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return jpaRepository.findByTokenHash(tokenHash).map(this::toDomain);
    }

    private RefreshToken toDomain(RefreshTokenJpaEntity e) {
        return new RefreshToken(e.getId(), e.getUserId(), e.getTokenHash(), e.getExpiresAt(), e.isRevoked());
    }
}
