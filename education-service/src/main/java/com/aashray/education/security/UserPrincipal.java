package com.aashray.education.security;

public record UserPrincipal(Long userId, String email, String fullName, String role) {}
