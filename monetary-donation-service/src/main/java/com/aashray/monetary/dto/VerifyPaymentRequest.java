package com.aashray.monetary.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * The three values Razorpay Checkout's success handler hands back to the
 * frontend. Note there is deliberately no "status" or "success" field
 * here — the backend decides success purely by verifying the signature
 * against these three values, never by trusting a flag from the client.
 */
public record VerifyPaymentRequest(
        @NotBlank(message = "razorpayOrderId is required") String razorpayOrderId,
        @NotBlank(message = "razorpayPaymentId is required") String razorpayPaymentId,
        @NotBlank(message = "razorpaySignature is required") String razorpaySignature
) {}
