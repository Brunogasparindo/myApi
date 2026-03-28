package com.example.demo.interfaces.rest;

import com.example.demo.application.auth.AuthenticationFailedException;
import com.example.demo.application.auth.EmailAlreadyTakenException;
import com.example.demo.application.auth.InvalidRefreshTokenException;
import com.example.demo.application.auth.LoginCommand;
import com.example.demo.application.auth.LoginResult;
import com.example.demo.application.auth.LoginService;
import com.example.demo.application.auth.RefreshCommand;
import com.example.demo.application.auth.RefreshResult;
import com.example.demo.application.auth.RefreshService;
import com.example.demo.application.auth.RegisterCommand;
import com.example.demo.application.auth.RegisterResult;
import com.example.demo.application.auth.RegisterService;
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
    private final RegisterService registerService;
    private final RefreshService refreshService;

    public AuthController(LoginService loginService, RegisterService registerService, RefreshService refreshService) {
        this.loginService = Objects.requireNonNull(loginService, "loginService must not be null");
        this.registerService = Objects.requireNonNull(registerService, "registerService must not be null");
        this.refreshService = Objects.requireNonNull(refreshService, "refreshService must not be null");
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        LoginResult result = loginService.login(new LoginCommand(request.email(), request.password()));
        return new LoginResponse(result.userId(), result.email(), result.token(), result.refreshToken());
    }

    @PostMapping("/refresh")
    public RefreshResponse refresh(@Valid @RequestBody RefreshRequest request) {
        RefreshResult result = refreshService.refresh(new RefreshCommand(request.refreshToken()));
        return new RefreshResponse(result.accessToken(), result.refreshToken());
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResult result = registerService.register(new RegisterCommand(request.email(), request.password()));
        RegisterResponse body = new RegisterResponse(result.userId(), result.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiError> handleAuthFailure(AuthenticationFailedException ex) {
        ApiError body = new ApiError("AUTH_INVALID", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<ApiError> handleEmailTaken(EmailAlreadyTakenException ex) {
        ApiError body = new ApiError("EMAIL_TAKEN", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiError> handleInvalidRefreshToken(InvalidRefreshTokenException ex) {
        ApiError body = new ApiError("INVALID_REFRESH_TOKEN", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadInput(IllegalArgumentException ex) {
        ApiError body = new ApiError("INVALID_INPUT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
