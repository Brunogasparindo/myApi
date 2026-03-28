package com.example.demo.interfaces.rest;

import com.example.demo.application.auth.AuthenticationFailedException;
import com.example.demo.application.auth.LoginCommand;
import com.example.demo.application.auth.LoginResult;
import com.example.demo.application.auth.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final LoginService loginService;

    public AuthController(LoginService loginService) {
        this.loginService = Objects.requireNonNull(loginService, "loginService must not be null");
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        System.out.println("request.email()");
        System.out.println(request.email());
        System.out.println("request.password()");
        System.out.println(request.password());
        LoginResult result = loginService.login(new LoginCommand(request.email(), request.password()));
        return new LoginResponse(result.userId(), result.email(), result.token());
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiError> handleAuthFailure(AuthenticationFailedException ex) {
        ApiError body = new ApiError("AUTH_INVALID", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
}
