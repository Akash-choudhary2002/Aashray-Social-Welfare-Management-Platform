package com.aashray.notification.security;

public record UserPrincipal(Long userId, String email, String fullName, String role) {}
