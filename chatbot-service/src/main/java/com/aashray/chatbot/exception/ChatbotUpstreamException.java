package com.aashray.chatbot.exception;

/**
 * Thrown when the Gemini API call fails (network error, non-2xx
 * response, malformed/empty response body) while an API key IS
 * configured. Mapped to 502 Bad Gateway — this service is healthy,
 * the upstream AI provider is not.
 */
public class ChatbotUpstreamException extends RuntimeException {
    public ChatbotUpstreamException(String message) {
        super(message);
    }

    public ChatbotUpstreamException(String message, Throwable cause) {
        super(message, cause);
    }
}
