package com.aashray.chatbot.dto.gemini;

import java.util.List;

public record GeminiGenerateRequest(
        List<GeminiContent> contents,
        GeminiContent systemInstruction,
        GeminiGenerationConfig generationConfig
) {}
