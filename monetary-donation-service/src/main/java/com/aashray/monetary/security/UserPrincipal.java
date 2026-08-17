package com.aashray.monetary.security;

public record UserPrincipal(Long userId, String email, String fullName, String role) {}
