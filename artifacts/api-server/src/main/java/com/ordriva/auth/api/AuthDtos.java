package com.ordriva.auth.api;

import com.ordriva.common.domain.Role;
import jakarta.validation.constraints.*;

import java.time.Instant;

public final class AuthDtos {
    private AuthDtos() {
    }

    public record RegisterRequest(
            @NotBlank @Size(max = 140) String name,
            @NotBlank @Email @Size(max = 180) String email,
            @NotBlank @Size(min = 12, max = 100) String password
    ) {
    }

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password
    ) {
    }

    public record UserResponse(Long id, String name, String email, Role role, Instant createdAt) {
    }

    public record AuthResponse(String accessToken, String tokenType, long expiresInSeconds, UserResponse user) {
    }
}