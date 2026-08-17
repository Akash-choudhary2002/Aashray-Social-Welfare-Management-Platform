package com.aashray.auth.service;

import com.aashray.auth.dto.AuthResponse;
import com.aashray.auth.dto.LoginRequest;
import com.aashray.auth.dto.RegisterRequest;
import com.aashray.auth.dto.UserRegisteredEvent;
import com.aashray.auth.entity.User;
import com.aashray.auth.exception.DuplicateEmailException;
import com.aashray.auth.exception.InvalidCredentialsException;
import com.aashray.auth.repository.UserRepository;
import com.aashray.auth.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EventPublisherService eventPublisherService;

    public AuthService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService,
                        EventPublisherService eventPublisherService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.eventPublisherService = eventPublisherService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(request.email());
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email().toLowerCase())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .address(request.address())
                .city(request.city())
                .role(request.role())
                .enabled(true)
                .build();

        User saved = userRepository.save(user);

        eventPublisherService.publishUserRegistered(
                UserRegisteredEvent.builder()
                        .userId(saved.getId())
                        .fullName(saved.getFullName())
                        .email(saved.getEmail())
                        .role(saved.getRole())
                        .build()
        );

        return buildAuthResponse(saved);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        if (!user.isEnabled()) {
            throw new InvalidCredentialsException();
        }

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .accessToken(token)
                .tokenType("Bearer")
                .expiresInMillis(jwtService.getExpirationMs())
                .build();
    }
}
