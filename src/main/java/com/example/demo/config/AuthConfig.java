package com.example.demo.config;

import com.example.demo.application.auth.LoginService;
import com.example.demo.application.auth.PasswordHasher;
import com.example.demo.application.auth.TokenService;
import com.example.demo.domain.auth.UserRepository;
import com.example.demo.infrastructure.auth.BCryptPasswordHasher;
import com.example.demo.infrastructure.auth.InMemoryUserRepository;
import com.example.demo.infrastructure.auth.JwtAuthenticationFilter;
import com.example.demo.infrastructure.auth.JwtTokenService;
import org.springframework.beans.factory.annotation.Value;
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
    public JwtTokenService jwtTokenService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs) {
        return new JwtTokenService(secret, expirationMs);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenService jwtTokenService) {
        return new JwtAuthenticationFilter(jwtTokenService);
    }

    @Bean
    public LoginService loginService(UserRepository userRepository, PasswordHasher passwordHasher, TokenService tokenService) {
        return new LoginService(userRepository, passwordHasher, tokenService);
    }
}
