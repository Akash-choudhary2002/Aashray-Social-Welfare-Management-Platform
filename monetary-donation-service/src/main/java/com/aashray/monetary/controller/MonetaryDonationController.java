package com.aashray.monetary.controller;

import com.aashray.monetary.dto.ApiResponse;
import com.aashray.monetary.dto.CreateDonationRequest;
import com.aashray.monetary.dto.DonationResponse;
import com.aashray.monetary.dto.PaymentFailedRequest;
import com.aashray.monetary.dto.RazorpayOrderResponse;
import com.aashray.monetary.dto.VerifyPaymentRequest;
import com.aashray.monetary.security.UserPrincipal;
import com.aashray.monetary.service.MonetaryDonationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monetary-donations")
@Tag(name = "Monetary Donations", description = "Donor and Admin monetary donation flows, backed by Razorpay")
public class MonetaryDonationController {

    private final MonetaryDonationService service;

    public MonetaryDonationController(MonetaryDonationService service) {
        this.service = service;
    }

    // ---------- Donor: Razorpay payment flow ----------

    @PostMapping("/create-order")
    @PreAuthorize("hasRole('DONOR')")
    @Operation(summary = "Donor: create a Razorpay order for a donation amount (step 1 of checkout)")
    public ResponseEntity<ApiResponse<RazorpayOrderResponse>> createOrder(@AuthenticationPrincipal UserPrincipal donor,
                                                                            @Valid @RequestBody CreateDonationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Razorpay order created", service.createOrder(donor, request)));
    }

    @PostMapping("/verify-payment")
    @PreAuthorize("hasRole('DONOR')")
    @Operation(summary = "Donor: verify a completed Razorpay payment's signature (step 2 of checkout)")
    public ApiResponse<DonationResponse> verifyPayment(@AuthenticationPrincipal UserPrincipal donor,
                                                          @Valid @RequestBody VerifyPaymentRequest request) {
        return ApiResponse.success("Payment verified successfully", service.verifyPayment(donor, request));
    }

    @PostMapping("/payment-failed")
    @PreAuthorize("hasRole('DONOR')")
    @Operation(summary = "Donor: report a failed/abandoned Razorpay checkout so the donation isn't left PENDING forever")
    public ApiResponse<DonationResponse> paymentFailed(@AuthenticationPrincipal UserPrincipal donor,
                                                          @Valid @RequestBody PaymentFailedRequest request) {
        return ApiResponse.success("Payment marked as failed", service.markPaymentFailed(donor, request));
    }

    @GetMapping("/my-donations")
    @PreAuthorize("hasRole('DONOR')")
    @Operation(summary = "Donor: view own donation history")
    public ApiResponse<List<DonationResponse>> myDonations(@AuthenticationPrincipal UserPrincipal donor) {
        return ApiResponse.success(service.getMyDonations(donor));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DONOR')")
    @Operation(summary = "Donor: view a single own transaction")
    public ApiResponse<DonationResponse> myDonation(@AuthenticationPrincipal UserPrincipal donor, @PathVariable Long id) {
        return ApiResponse.success(service.getMyDonationById(donor, id));
    }

    // ---------- Admin ----------

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: view all monetary donations across the platform")
    public ApiResponse<List<DonationResponse>> allDonations() {
        return ApiResponse.success(service.getAllDonations());
    }

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: donation totals/counts for the Admin Dashboard")
    public ApiResponse<Map<String, Object>> stats() {
        return ApiResponse.success(service.getDonationStats());
    }

    @GetMapping("/admin/recent")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: most recent monetary donations for the Admin Dashboard")
    public ApiResponse<List<DonationResponse>> recent(@RequestParam(defaultValue = "5") int limit) {
        return ApiResponse.success(service.getRecentDonations(limit));
    }
}
