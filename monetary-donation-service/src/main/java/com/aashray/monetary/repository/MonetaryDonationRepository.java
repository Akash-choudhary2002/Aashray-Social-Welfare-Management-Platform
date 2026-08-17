package com.aashray.monetary.repository;

import com.aashray.monetary.entity.MonetaryDonation;
import com.aashray.monetary.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MonetaryDonationRepository extends JpaRepository<MonetaryDonation, Long> {

    List<MonetaryDonation> findByDonorIdOrderByCreatedAtDesc(Long donorId);

    List<MonetaryDonation> findByPaymentStatusOrderByCreatedAtDesc(PaymentStatus status);

    Optional<MonetaryDonation> findByReferenceNumber(String referenceNumber);

    Optional<MonetaryDonation> findByRazorpayOrderId(String razorpayOrderId);

    long countByPaymentStatus(PaymentStatus status);

    @org.springframework.data.jpa.repository.Query(
            "SELECT COALESCE(SUM(m.amount), 0) FROM MonetaryDonation m WHERE m.paymentStatus = 'SUCCESS'")
    BigDecimal sumSuccessfulDonations();
}
