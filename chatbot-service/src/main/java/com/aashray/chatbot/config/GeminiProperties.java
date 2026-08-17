package com.aashray.chatbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds the `gemini.*` block from application.yml (backed by env vars —
 * see README). {@code apiKey} is intentionally allowed to be blank: when
 * it is, {@code ChatbotService} falls back to a rule-based FAQ responder
 * instead of calling the Gemini API, so the module still works out of
 * the box in a demo/CDAC environment without a real key.
 */
@ConfigurationProperties(prefix = "gemini")
public record GeminiProperties(
        String apiKey,
        String baseUrl,
        String model,
        long connectTimeoutMs,
        long readTimeoutMs,
        double temperature,
        int maxOutputTokens
) {
    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }
}
