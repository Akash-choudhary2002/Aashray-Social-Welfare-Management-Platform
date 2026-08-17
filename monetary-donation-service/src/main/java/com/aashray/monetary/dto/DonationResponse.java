package com.aashray.monetary.dto;

import com.aashray.monetary.entity.DonationType;
import com.aashray.monetary.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationResponse {
    private Long id;
    private BigDecimal amount;
    private String currency;
    private DonationType donationType;
    private LocalDateTime donationDate;
    private PaymentStatus paymentStatus;
    private String referenceNumber;
    private String paymentMethod;
    private String purposeNote;
    // Safe to expose — these identify the transaction but reveal nothing
    // that could be used to forge one. razorpaySignature is intentionally
    // never included here.
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private Long donorId;
    private String donorName;
    private String donorEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
