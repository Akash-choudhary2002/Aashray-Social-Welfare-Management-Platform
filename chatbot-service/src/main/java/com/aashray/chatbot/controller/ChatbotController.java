package com.aashray.chatbot.controller;

import com.aashray.chatbot.dto.ApiResponse;
import com.aashray.chatbot.dto.ChatRequest;
import com.aashray.chatbot.dto.ChatResponse;
import com.aashray.chatbot.security.UserPrincipal;
import com.aashray.chatbot.service.ChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
@Tag(name = "Chatbot", description = "AI assistant for platform navigation and FAQs")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/chat")
    @Operation(summary = "Ask the Aashray AI assistant a question",
            description = "Public endpoint. An Authorization: Bearer token is optional — when present, " +
                    "the assistant lightly tailors its answer to the caller's role.")
    public ResponseEntity<ApiResponse<ChatResponse>> chat(
            @AuthenticationPrincipal(errorOnInvalidType = false) UserPrincipal principal,
            @Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Response generated", chatbotService.chat(principal, request)));
    }
}
