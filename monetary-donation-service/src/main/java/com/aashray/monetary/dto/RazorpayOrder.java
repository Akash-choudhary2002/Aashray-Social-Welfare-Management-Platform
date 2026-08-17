package com.aashray.monetary.dto;

/**
 * Internal representation of a Razorpay order creation response.
 * amountInSubUnits is in paise (INR's smallest sub-unit), exactly what
 * Razorpay's API expects and returns, and exactly what Razorpay
 * Checkout's `amount` option expects on the frontend.
 */
public record RazorpayOrder(String id, long amountInSubUnits, String currency) {}
