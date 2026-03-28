package com.example.demo.config;

import com.example.demo.application.auth.LoginService;
import com.example.demo.application.auth.PasswordHasher;
import com.example.demo.application.auth.TokenService;
import com.example.demo.domain.auth.UserRepository;
import com.example.demo.infrastructure.auth.BCryptPasswordHasher;
import com.example.demo.infrastructure.auth.InMemoryUserRepository;
import com.example.demo.infrastructure.auth.UuidTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserRepository userRepository(PasswordEncoder passwordEncoder) {
        return new InMemoryUserRepository(passwordEncoder);
    }

    @Bean
    public PasswordHasher passwordHasher(PasswordEncoder passwordEncoder) {
        return new BCryptPasswordHasher(passwordEncoder);
    }

    @Bean
    public TokenService tokenService() {
        return new UuidTokenService();
    }

    @Bean
    public LoginService loginService(UserRepository userRepository, PasswordHasher passwordHasher, TokenService tokenService) {
        return new LoginService(userRepository, passwordHasher, tokenService);
    }
}
