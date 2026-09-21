package com.ordriva.auth.service;

import com.ordriva.auth.api.AuthDtos.*;
import com.ordriva.auth.security.JwtService;
import com.ordriva.auth.security.UserPrincipal;
import com.ordriva.common.api.ConflictException;
import com.ordriva.common.api.ResourceNotFoundException;
import com.ordriva.common.domain.Role;
import com.ordriva.users.domain.User;
import com.ordriva.users.repository.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ConflictException("An account with that email already exists");
        }
        User user = userRepository.save(new User(
                request.name().trim(),
                request.email().trim().toLowerCase(),
                passwordEncoder.encode(request.password()),
                Role.VIEWER
        ));
        return response(user);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password())
        );
        User user = ((UserPrincipal) authentication.getPrincipal()).user();
        return response(user);
    }

    @Transactional(readOnly = true)
    public UserResponse me(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user was not found"));
    }

    private AuthResponse response(User user) {
        return new AuthResponse(jwtService.issue(user), "Bearer", jwtService.expirationSeconds(), toResponse(user));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt());
    }
}