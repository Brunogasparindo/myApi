package com.example.demo.infrastructure.auth;

import com.example.demo.domain.auth.Email;
import com.example.demo.domain.auth.PasswordHash;
import com.example.demo.domain.auth.User;
import com.example.demo.domain.auth.UserId;
import com.example.demo.domain.auth.UserRepository;

import java.util.Optional;

public class JpaUserRepository implements UserRepository {
    private final UserJpaRepository jpaRepository;

    public JpaUserRepository(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.value().toLowerCase())
                .map(this::toDomain);
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = new UserJpaEntity(
                user.id().value(),
                user.email().value().toLowerCase(),
                user.passwordHash().value()
        );
        jpaRepository.save(entity);
        return user;
    }

    private User toDomain(UserJpaEntity entity) {
        return new User(
                new UserId(entity.getId()),
                Email.of(entity.getEmail()),
                PasswordHash.of(entity.getPasswordHash())
        );
    }
}
