package com.aashray.chatbot.dto.gemini;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Mirrors Gemini's {@code Content} shape, reused for the request's
 * {@code contents}/{@code systemInstruction} blocks and for the
 * response's {@code candidates[].content} block. {@code role} is
 * omitted from the JSON (via NON_NULL) for systemInstruction, which
 * Gemini expects without a role field.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GeminiContent(String role, List<GeminiPart> parts) {

    public static GeminiContent userText(String text) {
        return new GeminiContent("user", List.of(new GeminiPart(text)));
    }

    public static GeminiContent systemText(String text) {
        return new GeminiContent(null, List.of(new GeminiPart(text)));
    }
}
