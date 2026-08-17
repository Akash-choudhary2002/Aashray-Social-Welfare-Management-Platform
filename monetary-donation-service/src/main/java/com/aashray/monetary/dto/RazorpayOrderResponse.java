package com.aashray.monetary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Everything the React Checkout widget needs to open Razorpay's modal.
 * razorpayKeyId is the PUBLIC key — safe to send to the browser. The key
 * secret never leaves the backend.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RazorpayOrderResponse {
    private Long donationId;
    private String razorpayOrderId;
    private String razorpayKeyId;
    private Long amount; // paise
    private String currency;
    private String referenceNumber;
    private String donorName;
    private String donorEmail;
}
