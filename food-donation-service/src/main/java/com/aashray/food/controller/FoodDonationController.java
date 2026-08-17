package com.aashray.food.controller;

import com.aashray.food.dto.*;
import com.aashray.food.security.UserPrincipal;
import com.aashray.food.service.FoodDonationService;
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
@RequestMapping("/api/food-donations")
@Tag(name = "Food Donations", description = "Donor, NGO and Admin food donation flows")
public class FoodDonationController {

    private final FoodDonationService service;

    public FoodDonationController(FoodDonationService service) {
        this.service = service;
    }

    // ---------- Donor ----------

    @PostMapping
    @PreAuthorize("hasRole('DONOR')")
    @Operation(summary = "Donor: create a new food donation")
    public ResponseEntity<ApiResponse<DonationResponse>> create(@AuthenticationPrincipal UserPrincipal donor,
                                                                  @Valid @RequestBody CreateDonationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Donation created", service.createDonation(donor, request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DONOR')")
    @Operation(summary = "Donor: edit own PENDING donation")
    public ApiResponse<DonationResponse> update(@AuthenticationPrincipal UserPrincipal donor,
                                                  @PathVariable Long id,
                                                  @Valid @RequestBody UpdateDonationRequest request) {
        return ApiResponse.success("Donation updated", service.updateDonation(donor, id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DONOR')")
    @Operation(summary = "Donor: delete own PENDING donation")
    public ApiResponse<Void> delete(@AuthenticationPrincipal UserPrincipal donor, @PathVariable Long id) {
        service.deleteDonation(donor, id);
        return ApiResponse.success("Donation deleted", null);
    }

    @GetMapping("/my-donations")
    @PreAuthorize("hasRole('DONOR')")
    @Operation(summary = "Donor: view own donation history")
    public ApiResponse<List<DonationResponse>> myDonations(@AuthenticationPrincipal UserPrincipal donor) {
        return ApiResponse.success(service.getMyDonations(donor));
    }

    // ---------- NGO ----------

    @GetMapping("/available")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: view all PENDING donations available for pickup")
    public ApiResponse<List<DonationResponse>> available() {
        return ApiResponse.success(service.getAvailableDonations());
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: accept a pending donation")
    public ApiResponse<DonationResponse> accept(@AuthenticationPrincipal UserPrincipal ngo, @PathVariable Long id) {
        return ApiResponse.success("Donation accepted", service.acceptDonation(ngo, id));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: reject a pending donation")
    public ApiResponse<DonationResponse> reject(@AuthenticationPrincipal UserPrincipal ngo, @PathVariable Long id) {
        return ApiResponse.success("Donation rejected", service.rejectDonation(ngo, id));
    }

    @PatchMapping("/{id}/pickup")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: mark an accepted donation as picked up")
    public ApiResponse<DonationResponse> markPickedUp(@AuthenticationPrincipal UserPrincipal ngo, @PathVariable Long id) {
        return ApiResponse.success("Marked as picked up", service.updatePickupStatus(ngo, id));
    }

    @PatchMapping("/{id}/delivered")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: mark a picked-up donation as delivered")
    public ApiResponse<DonationResponse> markDelivered(@AuthenticationPrincipal UserPrincipal ngo, @PathVariable Long id) {
        return ApiResponse.success("Marked as delivered", service.updateDeliveredStatus(ngo, id));
    }

    @GetMapping("/ngo/history")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: view donations this NGO has handled")
    public ApiResponse<List<DonationResponse>> ngoHistory(@AuthenticationPrincipal UserPrincipal ngo) {
        return ApiResponse.success(service.getNgoDonations(ngo));
    }

    // ---------- Admin ----------

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: view all donations across the platform")
    public ApiResponse<List<DonationResponse>> allDonations() {
        return ApiResponse.success(service.getAllDonations());
    }

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: donation counts by status for the Admin Dashboard")
    public ApiResponse<Map<String, Long>> stats() {
        return ApiResponse.success(service.getDonationStats());
    }

    @GetMapping("/admin/recent")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: most recent donations for the Admin Dashboard")
    public ApiResponse<List<DonationResponse>> recent(@RequestParam(defaultValue = "5") int limit) {
        return ApiResponse.success(service.getRecentDonations(limit));
    }
}
