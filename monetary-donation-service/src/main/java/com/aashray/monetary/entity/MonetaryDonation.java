package com.aashray.monetary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "monetary_donations", indexes = {
        @Index(name = "idx_monetary_status", columnList = "payment_status"),
        @Index(name = "idx_monetary_donor", columnList = "donor_id"),
        @Index(name = "idx_monetary_date", columnList = "donation_date")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonetaryDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "donation_date", nullable = false)
    private LocalDateTime donationDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(nullable = false, unique = true, length = 64)
    private String referenceNumber;

    @Column(length = 30)
    private String paymentMethod;

    @Column(length = 255)
    private String purposeNote;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "donation_type", nullable = false, length = 20)
    private DonationType donationType = DonationType.ONE_TIME;

    @Builder.Default
    @Column(nullable = false, length = 10)
    private String currency = "INR";

    // ---- Razorpay tracking ----
    // Set the moment the order is created with Razorpay, before the donor
    // ever sees a checkout modal. Used as the lookup key when verifying.
    @Column(name = "razorpay_order_id", unique = true, length = 64)
    private String razorpayOrderId;

    // Set only after the donor completes checkout (success or failure).
    @Column(name = "razorpay_payment_id", length = 64)
    private String razorpayPaymentId;

    // Set only after a signature has actually been verified. Never
    // returned to the frontend — kept purely as a server-side audit trail.
    @Column(name = "razorpay_signature", length = 255)
    private String razorpaySignature;

    // Donor info denormalized from Auth Service (no cross-service join in a microservices world)
    @Column(nullable = false)
    private Long donorId;

    @Column(nullable = false, length = 100)
    private String donorName;

    @Column(nullable = false, length = 150)
    private String donorEmail;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
