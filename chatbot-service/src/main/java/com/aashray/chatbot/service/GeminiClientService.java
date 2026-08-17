package com.aashray.chatbot.service;

import com.aashray.chatbot.config.GeminiProperties;
import com.aashray.chatbot.dto.gemini.*;
import com.aashray.chatbot.exception.ChatbotUpstreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

/**
 * Thin wrapper around Gemini's {@code generateContent} REST endpoint.
 * Knows nothing about Aashray's domain — {@link ChatbotService} builds
 * the system prompt and decides when to call this at all.
 */
@Service
public class GeminiClientService {

    private static final Logger log = LoggerFactory.getLogger(GeminiClientService.class);

    private final RestClient geminiRestClient;
    private final GeminiProperties properties;

    public GeminiClientService(RestClient geminiRestClient, GeminiProperties properties) {
        this.geminiRestClient = geminiRestClient;
        this.properties = properties;
    }

    public String generateReply(String systemPrompt, String userMessage) {
        GeminiGenerateRequest request = new GeminiGenerateRequest(
                List.of(GeminiContent.userText(userMessage)),
                GeminiContent.systemText(systemPrompt),
                new GeminiGenerationConfig(properties.temperature(), properties.maxOutputTokens())
        );

        GeminiGenerateResponse response;
        try {
            response = geminiRestClient.post()
                    .uri("/{model}:generateContent?key={key}", properties.model(), properties.apiKey())
                    .body(request)
                    .retrieve()
                    .body(GeminiGenerateResponse.class);
        } catch (RestClientException ex) {
            log.warn("Gemini API call failed: {}", ex.getMessage());
            throw new ChatbotUpstreamException("Gemini API call failed", ex);
        }

        return extractText(response);
    }

    private String extractText(GeminiGenerateResponse response) {
        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            throw new ChatbotUpstreamException("Gemini returned no candidates");
        }

        GeminiContent content = response.candidates().get(0).content();
        if (content == null || content.parts() == null || content.parts().isEmpty()) {
            throw new ChatbotUpstreamException("Gemini returned an empty response");
        }

        String text = content.parts().get(0).text();
        if (text == null || text.isBlank()) {
            throw new ChatbotUpstreamException("Gemini returned an empty response");
        }

        return text.trim();
    }
}
