package com.example.demo.application.auth;

import com.example.demo.domain.auth.Email;
import com.example.demo.domain.auth.PasswordHash;
import com.example.demo.domain.auth.User;
import com.example.demo.domain.auth.UserId;
import com.example.demo.domain.auth.UserRepository;

import java.util.Objects;

public class RegisterService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
        this.passwordHasher = Objects.requireNonNull(passwordHasher, "passwordHasher must not be null");
    }

    public RegisterResult register(RegisterCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        Email email = Email.of(command.email());

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyTakenException();
        }

        String hash = passwordHasher.hash(command.password());
        User user = new User(UserId.newId(), email, PasswordHash.of(hash));
        userRepository.save(user);

        return new RegisterResult(user.id().value(), user.email().value());
    }
}
