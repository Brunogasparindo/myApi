package com.example.demo.domain.auth;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(Email email);
    Optional<User> findById(UserId id);
    User save(User user);
}
