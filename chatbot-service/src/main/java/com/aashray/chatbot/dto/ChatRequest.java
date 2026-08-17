package com.aashray.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(

        @NotBlank(message = "message is required")
        @Size(max = 2000, message = "message must not exceed 2000 characters")
        String message,

        @Size(max = 100, message = "sessionId must not exceed 100 characters")
        String sessionId
) {}
