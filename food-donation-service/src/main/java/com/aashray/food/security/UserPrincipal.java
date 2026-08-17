package com.aashray.food.security;

public record UserPrincipal(Long userId, String email, String fullName, String role) {}
