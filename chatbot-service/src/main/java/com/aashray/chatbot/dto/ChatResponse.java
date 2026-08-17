package com.aashray.chatbot.dto;

import java.time.LocalDateTime;

public record ChatResponse(
        String reply,
        String source,
        LocalDateTime respondedAt
) {
    public static ChatResponse of(String reply, String source) {
        return new ChatResponse(reply, source, LocalDateTime.now());
    }
}
