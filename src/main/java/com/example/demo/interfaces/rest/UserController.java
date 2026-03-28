package com.example.demo.interfaces.rest;

import com.example.demo.domain.auth.User;
import com.example.demo.domain.auth.UserId;
import com.example.demo.domain.auth.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(new UserId(UUID.fromString(userId)))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserResponse(
                user.id().value(),
                user.email().value(),
                user.email().value(),
                List.of("ROLE_USER"),
                List.of("ROLE_USER"),
                List.of(),
                "",
                ""
        );
    }

    @GetMapping("/permissions/{userId}")
    public List<String> getUserPermissions(@PathVariable UUID userId) {
        return List.of();
    }

    @GetMapping("/roles/{userId}")
    public List<String> getUserRoles(@PathVariable UUID userId) {
        return List.of("ROLE_USER");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadInput(IllegalArgumentException ex) {
        ApiError body = new ApiError("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
