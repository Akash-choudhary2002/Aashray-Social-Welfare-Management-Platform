package com.aashray.chatbot.dto.gemini;

import java.util.List;

public record GeminiGenerateResponse(List<GeminiCandidate> candidates) {}
