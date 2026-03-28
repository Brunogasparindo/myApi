package com.example.demo.application.auth;

import com.example.demo.domain.auth.Email;
import com.example.demo.domain.auth.User;
import com.example.demo.domain.auth.UserRepository;

import java.util.Objects;

public class LoginService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;

    public LoginService(UserRepository userRepository, PasswordHasher passwordHasher, TokenService tokenService) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
        this.passwordHasher = Objects.requireNonNull(passwordHasher, "passwordHasher must not be null");
        this.tokenService = Objects.requireNonNull(tokenService, "tokenService must not be null");
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

        String token = tokenService.issueToken(user.id().value().toString());
        return new LoginResult(user.id().value(), user.email().value(), token);
    }
}
