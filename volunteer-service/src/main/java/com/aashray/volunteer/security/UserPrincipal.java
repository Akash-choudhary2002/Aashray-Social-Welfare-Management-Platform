package com.aashray.volunteer.security;

public record UserPrincipal(Long userId, String email, String fullName, String role) {}
