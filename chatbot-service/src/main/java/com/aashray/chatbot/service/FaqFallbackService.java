package com.aashray.chatbot.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * Keyword-matched canned answers about the Aashray platform. Used only
 * when {@code gemini.api-key} isn't configured, so the chatbot endpoint
 * — and the "General FAQs" requirement — still works out of the box in
 * a demo/CDAC environment with zero external setup. Real free-form
 * understanding comes from Gemini once a key is supplied.
 */
@Service
public class FaqFallbackService {

    private record Rule(List<String> keywords, String answer) {}

    private final List<Rule> rules = List.of(
            new Rule(List.of("register", "sign up", "signup", "create account"),
                    "To register on Aashray, go to the Register page and choose your role — Donor, NGO, " +
                            "Educator, Volunteer, or Beneficiary — then fill in your name, email, phone and " +
                            "password. NGOs and Educators may need Admin approval before full access."),
            new Rule(List.of("login", "log in", "sign in", "password"),
                    "Use your registered email and password on the Login page. If you forgot your password, " +
                            "contact an Admin to reset your account for now — self-service reset isn't in scope yet."),
            new Rule(List.of("food donation", "donate food", "food"),
                    "Donors can add a food donation with quantity, food type, pickup address and expiry time. " +
                            "Nearby NGOs see it under 'Available Donations' and can accept it, then update the " +
                            "status through Picked Up and Delivered."),
            new Rule(List.of("monetary", "money donation", "donate money", "payment", "transaction"),
                    "Monetary donations are one-time: enter an amount, submit, and the donation starts as " +
                            "PENDING with a reference number. Once payment is confirmed it moves to SUCCESS and " +
                            "you'll see it in your donation history."),
            new Rule(List.of("education", "student", "educator", "teach"),
                    "NGOs register students needing support and assign an Educator to them. Educators see their " +
                            "assigned students on their dashboard and mark an assignment complete when tutoring wraps up."),
            new Rule(List.of("volunteer"),
                    "NGOs assign tasks to Volunteers. A Volunteer sees assigned tasks on their dashboard, starts " +
                            "one to move it to In Progress, and marks it Completed when done."),
            new Rule(List.of("admin", "dashboard", "stats", "statistics"),
                    "Admins get a platform-wide dashboard with totals for users, NGOs, donors, donations " +
                            "(food and monetary), volunteers, educators and students, plus recent activity feeds."),
            new Rule(List.of("notification", "email"),
                    "Aashray sends event-driven notifications — registration, donation accepted/picked up/" +
                            "delivered, monetary success, educator and volunteer assignment — via the Notification " +
                            "Service. In demo mode these are logged rather than emailed unless email sending is enabled."),
            new Rule(List.of("navigate", "navigation", "menu", "where"),
                    "Use the sidebar after logging in — it's tailored to your role, so Donors see donation " +
                            "options, NGOs see donation/education/volunteer management, and so on.")
    );

    private static final String DEFAULT_ANSWER =
            "I can help with registration, food or monetary donations, the education and volunteer modules, " +
                    "and general navigation around Aashray. Could you tell me a bit more about what you're trying to do?";

    public String answer(String message) {
        String lower = message.toLowerCase(Locale.ROOT);
        for (Rule rule : rules) {
            for (String keyword : rule.keywords()) {
                if (lower.contains(keyword)) {
                    return rule.answer();
                }
            }
        }
        return DEFAULT_ANSWER;
    }
}
