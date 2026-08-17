package com.aashray.monetary.exception;

/**
 * Thrown when a Razorpay payment signature does not verify against our
 * key secret. This is the one check the frontend can never bypass —
 * whatever status the browser claims, only a valid signature ever moves
 * a donation to SUCCESS.
 */
public class PaymentVerificationException extends RuntimeException {
    public PaymentVerificationException(String message) {
        super(message);
    }
}
