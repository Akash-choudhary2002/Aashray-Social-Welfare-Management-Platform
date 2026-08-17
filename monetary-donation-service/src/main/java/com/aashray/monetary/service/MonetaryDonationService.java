package com.aashray.monetary.service;

import com.aashray.monetary.config.RazorpayConfig;
import com.aashray.monetary.dto.CreateDonationRequest;
import com.aashray.monetary.dto.DonationResponse;
import com.aashray.monetary.dto.MonetaryDonationEvent;
import com.aashray.monetary.dto.PaymentFailedRequest;
import com.aashray.monetary.dto.RazorpayOrder;
import com.aashray.monetary.dto.RazorpayOrderResponse;
import com.aashray.monetary.dto.VerifyPaymentRequest;
import com.aashray.monetary.entity.DonationType;
import com.aashray.monetary.entity.MonetaryDonation;
import com.aashray.monetary.entity.PaymentStatus;
import com.aashray.monetary.exception.DonationNotFoundException;
import com.aashray.monetary.exception.PaymentVerificationException;
import com.aashray.monetary.exception.UnauthorizedDonationAccessException;
import com.aashray.monetary.repository.MonetaryDonationRepository;
import com.aashray.monetary.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MonetaryDonationService {

    private static final Logger log = LoggerFactory.getLogger(MonetaryDonationService.class);
    private static final String DEFAULT_CURRENCY = "INR";

    private final MonetaryDonationRepository repository;
    private final EventPublisherService eventPublisherService;
    private final RazorpayService razorpayService;
    private final RazorpayConfig razorpayConfig;

    public MonetaryDonationService(MonetaryDonationRepository repository,
                                    EventPublisherService eventPublisherService,
                                    RazorpayService razorpayService,
                                    RazorpayConfig razorpayConfig) {
        this.repository = repository;
        this.eventPublisherService = eventPublisherService;
        this.razorpayService = razorpayService;
        this.razorpayConfig = razorpayConfig;
    }

    // ---------- Donor operations ----------

    /**
     * Step 1 of the payment flow. Creates the order with Razorpay FIRST;
     * only if that succeeds do we persist a local PENDING donation row,
     * so we never end up with a donation that has no corresponding
     * Razorpay order to actually be paid against.
     */
    @Transactional
    public RazorpayOrderResponse createOrder(UserPrincipal donor, CreateDonationRequest request) {
        String referenceNumber = generateReferenceNumber();

        RazorpayOrder razorpayOrder = razorpayService.createOrder(request.amount(), DEFAULT_CURRENCY, referenceNumber);

        MonetaryDonation donation = MonetaryDonation.builder()
                .amount(request.amount())
                .currency(DEFAULT_CURRENCY)
                .donationType(DonationType.ONE_TIME)
                .donationDate(LocalDateTime.now())
                .paymentStatus(PaymentStatus.PENDING)
                .referenceNumber(referenceNumber)
                .purposeNote(request.purposeNote())
                .razorpayOrderId(razorpayOrder.id())
                .donorId(donor.userId())
                .donorName(donor.fullName())
                .donorEmail(donor.email())
                .build();

        MonetaryDonation saved = repository.save(donation);
        log.info("Created Razorpay order {} for donationId={} amount={}", razorpayOrder.id(), saved.getId(), request.amount());

        return RazorpayOrderResponse.builder()
                .donationId(saved.getId())
                .razorpayOrderId(saved.getRazorpayOrderId())
                .razorpayKeyId(razorpayConfig.getKeyId())
                .amount(razorpayOrder.amountInSubUnits())
                .currency(saved.getCurrency())
                .referenceNumber(saved.getReferenceNumber())
                .donorName(saved.getDonorName())
                .donorEmail(saved.getDonorEmail())
                .build();
    }

    /**
     * Step 2 of the payment flow, called after Razorpay Checkout's
     * success handler fires. This is the ONLY place a donation is ever
     * moved to SUCCESS, and it happens purely because the HMAC signature
     * verifies — nothing the frontend claims about the outcome is
     * trusted. Idempotent: if the donation has already left PENDING
     * (e.g. a duplicate handler call), we just return its current state
     * rather than re-processing or erroring.
     */
    @Transactional
    public DonationResponse verifyPayment(UserPrincipal donor, VerifyPaymentRequest request) {
        MonetaryDonation donation = repository.findByRazorpayOrderId(request.razorpayOrderId())
                .orElseThrow(() -> new DonationNotFoundException(
                        "No donation found for Razorpay order " + request.razorpayOrderId()));

        assertOwnedByDonor(donation, donor.userId());

        if (donation.getPaymentStatus() != PaymentStatus.PENDING) {
            return toResponse(donation);
        }

        boolean verified = razorpayService.verifySignature(
                request.razorpayOrderId(), request.razorpayPaymentId(), request.razorpaySignature());

        if (!verified) {
            donation.setPaymentStatus(PaymentStatus.FAILED);
            donation.setRazorpayPaymentId(request.razorpayPaymentId());
            repository.save(donation);
            log.warn("Signature verification FAILED for donationId={} orderId={}", donation.getId(), request.razorpayOrderId());
            throw new PaymentVerificationException("Payment could not be verified. If money was deducted, it will be refunded automatically.");
        }

        donation.setPaymentStatus(PaymentStatus.SUCCESS);
        donation.setRazorpayPaymentId(request.razorpayPaymentId());
        donation.setRazorpaySignature(request.razorpaySignature());
        donation.setPaymentMethod(resolvePaymentMethod(request.razorpayPaymentId()));

        MonetaryDonation saved = repository.save(donation);
        log.info("Payment verified for donationId={} paymentId={}", saved.getId(), request.razorpayPaymentId());

        eventPublisherService.publishDonationSuccess(toEvent(saved));

        return toResponse(saved);
    }

    /**
     * Called when Razorpay Checkout reports a failure or the donor
     * closes the modal before paying. Only ever moves PENDING -> FAILED;
     * never downgrades an already-SUCCESS donation, in case this fires
     * late after a success has already been verified.
     */
    @Transactional
    public DonationResponse markPaymentFailed(UserPrincipal donor, PaymentFailedRequest request) {
        MonetaryDonation donation = repository.findByRazorpayOrderId(request.razorpayOrderId())
                .orElseThrow(() -> new DonationNotFoundException(
                        "No donation found for Razorpay order " + request.razorpayOrderId()));

        assertOwnedByDonor(donation, donor.userId());

        if (donation.getPaymentStatus() == PaymentStatus.PENDING) {
            donation.setPaymentStatus(PaymentStatus.FAILED);
            repository.save(donation);
            log.info("Marked donationId={} FAILED (reason: {})", donation.getId(), request.reason());
        }

        return toResponse(donation);
    }

    public List<DonationResponse> getMyDonations(UserPrincipal donor) {
        return repository.findByDonorIdOrderByCreatedAtDesc(donor.userId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public DonationResponse getMyDonationById(UserPrincipal donor, Long donationId) {
        MonetaryDonation donation = findOrThrow(donationId);
        assertOwnedByDonor(donation, donor.userId());
        return toResponse(donation);
    }

    // ---------- Admin operations ----------

    public List<DonationResponse> getAllDonations() {
        return repository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::toResponse)
                .toList();
    }

    public Map<String, Object> getDonationStats() {
        return Map.of(
                "totalDonations", repository.count(),
                "pending", repository.countByPaymentStatus(PaymentStatus.PENDING),
                "success", repository.countByPaymentStatus(PaymentStatus.SUCCESS),
                "failed", repository.countByPaymentStatus(PaymentStatus.FAILED),
                "refunded", repository.countByPaymentStatus(PaymentStatus.REFUNDED),
                "totalAmountRaised", repository.sumSuccessfulDonations()
        );
    }

    public List<DonationResponse> getRecentDonations(int limit) {
        return repository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(limit)
                .map(this::toResponse)
                .toList();
    }

    // ---------- helpers ----------

    private String resolvePaymentMethod(String paymentId) {
        Object method = razorpayService.fetchPayment(paymentId).get("method");
        return method != null ? method.toString().toUpperCase() : null;
    }

    private String generateReferenceNumber() {
        return "MD-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }

    private MonetaryDonation findOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new DonationNotFoundException(id));
    }

    private void assertOwnedByDonor(MonetaryDonation donation, Long donorId) {
        if (!donation.getDonorId().equals(donorId)) {
            throw new UnauthorizedDonationAccessException("You do not have access to this donation");
        }
    }

    private MonetaryDonationEvent toEvent(MonetaryDonation d) {
        return MonetaryDonationEvent.builder()
                .donationId(d.getId())
                .amount(d.getAmount())
                .referenceNumber(d.getReferenceNumber())
                .donorId(d.getDonorId())
                .donorName(d.getDonorName())
                .donorEmail(d.getDonorEmail())
                .build();
    }

    private DonationResponse toResponse(MonetaryDonation d) {
        return DonationResponse.builder()
                .id(d.getId())
                .amount(d.getAmount())
                .currency(d.getCurrency())
                .donationType(d.getDonationType())
                .donationDate(d.getDonationDate())
                .paymentStatus(d.getPaymentStatus())
                .referenceNumber(d.getReferenceNumber())
                .paymentMethod(d.getPaymentMethod())
                .purposeNote(d.getPurposeNote())
                .razorpayOrderId(d.getRazorpayOrderId())
                .razorpayPaymentId(d.getRazorpayPaymentId())
                .donorId(d.getDonorId())
                .donorName(d.getDonorName())
                .donorEmail(d.getDonorEmail())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}
