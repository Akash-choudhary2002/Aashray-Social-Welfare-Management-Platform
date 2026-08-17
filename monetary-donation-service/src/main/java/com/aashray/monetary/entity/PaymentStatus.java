package com.aashray.monetary.entity;

/**
 * PENDING  -> Razorpay order created, awaiting payment completion
 * SUCCESS  -> payment signature verified server-side, terminal state
 * FAILED   -> payment declined/cancelled/signature mismatch, terminal state
 * REFUNDED -> a SUCCESS donation was later refunded (reserved for future
 *             refund-processing support; not yet transitioned into automatically)
 */
public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED,
    REFUNDED
}
