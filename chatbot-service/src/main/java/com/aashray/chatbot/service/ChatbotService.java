package com.aashray.chatbot.service;

import com.aashray.chatbot.config.GeminiProperties;
import com.aashray.chatbot.dto.ChatRequest;
import com.aashray.chatbot.dto.ChatResponse;
import com.aashray.chatbot.security.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    private static final String BASE_SYSTEM_PROMPT = """
            You are the Aashray Assistant, the official AI chatbot for Aashray — an AI-enabled, \
            microservices-based social welfare platform connecting Donors, NGOs, Educators, \
            Volunteers, Beneficiaries and Admins.

            You help users with:
            - Registration and login (roles: Donor, NGO, Educator, Volunteer, Beneficiary, Admin)
            - Food donation flow (Donor creates -> NGO accepts -> pickup -> delivered)
            - Monetary donation flow (one-time donation, reference number, payment status)
            - Education support (NGO registers students, assigns Educators)
            - Volunteer task assignment and tracking
            - General platform navigation and FAQs

            Answer only questions related to the Aashray platform and general goodwill/volunteering \
            topics. Keep answers concise and friendly — 2 to 4 sentences unless the user asks for detail. \
            If a question is unrelated to Aashray, politely say so and redirect to what you can help with.""";

    private final GeminiProperties geminiProperties;
    private final GeminiClientService geminiClientService;
    private final FaqFallbackService faqFallbackService;

    public ChatbotService(GeminiProperties geminiProperties,
                           GeminiClientService geminiClientService,
                           FaqFallbackService faqFallbackService) {
        this.geminiProperties = geminiProperties;
        this.geminiClientService = geminiClientService;
        this.faqFallbackService = faqFallbackService;
    }

    public ChatResponse chat(UserPrincipal principal, ChatRequest request) {
        if (!geminiProperties.isConfigured()) {
            return ChatResponse.of(faqFallbackService.answer(request.message()), "faq-fallback");
        }

        String systemPrompt = buildSystemPrompt(principal);
        String reply = geminiClientService.generateReply(systemPrompt, request.message());
        return ChatResponse.of(reply, "gemini");
    }

    private String buildSystemPrompt(UserPrincipal principal) {
        if (principal == null) {
            return BASE_SYSTEM_PROMPT + "\n\nThe current visitor is not logged in.";
        }
        return BASE_SYSTEM_PROMPT + "\n\nThe current user is logged in with role " + principal.role() +
                ". Tailor guidance to what that role can do where relevant.";
    }
}
