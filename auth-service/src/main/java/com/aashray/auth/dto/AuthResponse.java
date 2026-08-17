package com.aashray.auth.dto;

import com.aashray.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private Long userId;
    private String fullName;
    private String email;
    private Role role;
    private String accessToken;
    private String tokenType;
    private long expiresInMillis;
}
