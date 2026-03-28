package com.example.demo.infrastructure.auth;

import com.example.demo.domain.auth.Email;
import com.example.demo.domain.auth.PasswordHash;
import com.example.demo.domain.auth.User;
import com.example.demo.domain.auth.UserId;
import com.example.demo.domain.auth.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();

    public InMemoryUserRepository(PasswordEncoder passwordEncoder) {
        String defaultEmail = "user@example.com";
        String defaultPasswordHash = passwordEncoder.encode("Password123!");
        User user = new User(UserId.newId(), Email.of(defaultEmail), PasswordHash.of(defaultPasswordHash));
        usersByEmail.put(normalize(defaultEmail), user);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return Optional.ofNullable(usersByEmail.get(normalize(email.value())));
    }

    private String normalize(String email) {
        return email.toLowerCase();
    }
}
