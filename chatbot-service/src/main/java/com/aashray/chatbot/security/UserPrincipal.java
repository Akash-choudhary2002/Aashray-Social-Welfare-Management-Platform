package com.aashray.chatbot.security;

public record UserPrincipal(Long userId, String email, String fullName, String role) {}
