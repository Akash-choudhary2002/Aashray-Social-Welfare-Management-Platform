package com.aashray.monetary.exception;

/**
 * Thrown when Razorpay's API itself is unreachable or returns an error
 * (network issue, bad credentials, rate limit, Razorpay outage, etc).
 * Kept distinct from PaymentVerificationException so the frontend can
 * tell "your payment failed" apart from "we couldn't even reach the
 * payment gateway, try again".
 */
public class PaymentGatewayException extends RuntimeException {
    public PaymentGatewayException(String message) {
        super(message);
    }
}
