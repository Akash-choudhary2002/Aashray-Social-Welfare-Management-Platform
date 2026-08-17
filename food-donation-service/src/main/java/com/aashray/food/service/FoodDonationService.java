package com.aashray.food.service;

import com.aashray.food.config.RabbitMQConfig;
import com.aashray.food.dto.*;
import com.aashray.food.entity.DonationStatus;
import com.aashray.food.entity.FoodDonation;
import com.aashray.food.exception.DonationNotFoundException;
import com.aashray.food.exception.InvalidDonationStateException;
import com.aashray.food.exception.UnauthorizedDonationAccessException;
import com.aashray.food.repository.FoodDonationRepository;
import com.aashray.food.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FoodDonationService {

    private final FoodDonationRepository repository;
    private final EventPublisherService eventPublisherService;

    public FoodDonationService(FoodDonationRepository repository, EventPublisherService eventPublisherService) {
        this.repository = repository;
        this.eventPublisherService = eventPublisherService;
    }

    // ---------- Donor operations ----------

    @Transactional
    public DonationResponse createDonation(UserPrincipal donor, CreateDonationRequest request) {
        FoodDonation donation = FoodDonation.builder()
                .foodName(request.foodName())
                .quantity(request.quantity())
                .quantityUnit(request.quantityUnit())
                .foodType(request.foodType())
                .preparedTime(request.preparedTime())
                .expiryTime(request.expiryTime())
                .pickupAddress(request.pickupAddress())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .city(request.city())
                .contactNumber(request.contactNumber())
                .status(DonationStatus.PENDING)
                .donorId(donor.userId())
                .donorName(donor.fullName())
                .build();

        return toResponse(repository.save(donation));
    }

    @Transactional
    public DonationResponse updateDonation(UserPrincipal donor, Long donationId, UpdateDonationRequest request) {
        FoodDonation donation = findOrThrow(donationId);
        assertOwnedByDonor(donation, donor.userId());

        if (donation.getStatus() != DonationStatus.PENDING) {
            throw new InvalidDonationStateException("Only PENDING donations can be edited");
        }

        if (request.foodName() != null) donation.setFoodName(request.foodName());
        if (request.quantity() != null) donation.setQuantity(request.quantity());
        if (request.quantityUnit() != null) donation.setQuantityUnit(request.quantityUnit());
        if (request.foodType() != null) donation.setFoodType(request.foodType());
        if (request.preparedTime() != null) donation.setPreparedTime(request.preparedTime());
        if (request.expiryTime() != null) donation.setExpiryTime(request.expiryTime());
        if (request.pickupAddress() != null) donation.setPickupAddress(request.pickupAddress());
        if (request.latitude() != null) donation.setLatitude(request.latitude());
        if (request.longitude() != null) donation.setLongitude(request.longitude());
        if (request.city() != null) donation.setCity(request.city());
        if (request.contactNumber() != null) donation.setContactNumber(request.contactNumber());

        return toResponse(repository.save(donation));
    }

    @Transactional
    public void deleteDonation(UserPrincipal donor, Long donationId) {
        FoodDonation donation = findOrThrow(donationId);
        assertOwnedByDonor(donation, donor.userId());

        if (donation.getStatus() != DonationStatus.PENDING) {
            throw new InvalidDonationStateException("Only PENDING donations can be deleted");
        }

        repository.delete(donation);
    }

    public List<DonationResponse> getMyDonations(UserPrincipal donor) {
        return repository.findByDonorIdOrderByCreatedAtDesc(donor.userId()).stream()
                .map(this::toResponse)
                .toList();
    }

    // ---------- NGO operations ----------

    public List<DonationResponse> getAvailableDonations() {
        return repository.findByStatusOrderByCreatedAtDesc(DonationStatus.PENDING).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public DonationResponse acceptDonation(UserPrincipal ngo, Long donationId) {
        FoodDonation donation = findOrThrow(donationId);

        if (donation.getStatus() != DonationStatus.PENDING) {
            throw new InvalidDonationStateException("Donation is no longer available (current status: " + donation.getStatus() + ")");
        }

        donation.setStatus(DonationStatus.ACCEPTED);
        donation.setNgoId(ngo.userId());
        donation.setNgoName(ngo.fullName());
        FoodDonation saved = repository.save(donation);

        eventPublisherService.publish(RabbitMQConfig.DONATION_ACCEPTED_KEY, toEvent(saved));
        return toResponse(saved);
    }

    @Transactional
    public DonationResponse rejectDonation(UserPrincipal ngo, Long donationId) {
        FoodDonation donation = findOrThrow(donationId);

        if (donation.getStatus() != DonationStatus.PENDING) {
            throw new InvalidDonationStateException("Only PENDING donations can be rejected");
        }

        // Rejecting simply leaves it PENDING so other NGOs can still pick it up;
        // we don't hard-fail the donor's donation over one NGO's rejection.
        return toResponse(donation);
    }

    @Transactional
    public DonationResponse updatePickupStatus(UserPrincipal ngo, Long donationId) {
        FoodDonation donation = findOrThrow(donationId);
        assertOwnedByNgo(donation, ngo.userId());

        if (donation.getStatus() != DonationStatus.ACCEPTED) {
            throw new InvalidDonationStateException("Donation must be ACCEPTED before it can be marked PICKED_UP");
        }

        donation.setStatus(DonationStatus.PICKED_UP);
        FoodDonation saved = repository.save(donation);

        eventPublisherService.publish(RabbitMQConfig.DONATION_PICKED_UP_KEY, toEvent(saved));
        return toResponse(saved);
    }

    @Transactional
    public DonationResponse updateDeliveredStatus(UserPrincipal ngo, Long donationId) {
        FoodDonation donation = findOrThrow(donationId);
        assertOwnedByNgo(donation, ngo.userId());

        if (donation.getStatus() != DonationStatus.PICKED_UP) {
            throw new InvalidDonationStateException("Donation must be PICKED_UP before it can be marked DELIVERED");
        }

        donation.setStatus(DonationStatus.DELIVERED);
        FoodDonation saved = repository.save(donation);

        eventPublisherService.publish(RabbitMQConfig.DONATION_DELIVERED_KEY, toEvent(saved));
        return toResponse(saved);
    }

    public List<DonationResponse> getNgoDonations(UserPrincipal ngo) {
        return repository.findByNgoIdOrderByCreatedAtDesc(ngo.userId()).stream()
                .map(this::toResponse)
                .toList();
    }

    // ---------- Admin operations ----------

    public List<DonationResponse> getAllDonations() {
        return repository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::toResponse)
                .toList();
    }

    public Map<String, Long> getDonationStats() {
        return Map.of(
                "totalDonations", repository.count(),
                "pending", repository.countByStatus(DonationStatus.PENDING),
                "accepted", repository.countByStatus(DonationStatus.ACCEPTED),
                "pickedUp", repository.countByStatus(DonationStatus.PICKED_UP),
                "delivered", repository.countByStatus(DonationStatus.DELIVERED)
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

    private FoodDonation findOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new DonationNotFoundException(id));
    }

    private void assertOwnedByDonor(FoodDonation donation, Long donorId) {
        if (!donation.getDonorId().equals(donorId)) {
            throw new UnauthorizedDonationAccessException("You do not have access to this donation");
        }
    }

    private void assertOwnedByNgo(FoodDonation donation, Long ngoId) {
        if (donation.getNgoId() == null || !donation.getNgoId().equals(ngoId)) {
            throw new UnauthorizedDonationAccessException("You do not have access to this donation");
        }
    }

    private DonationStatusEvent toEvent(FoodDonation d) {
        return DonationStatusEvent.builder()
                .donationId(d.getId())
                .foodName(d.getFoodName())
                .donorId(d.getDonorId())
                .donorName(d.getDonorName())
                .ngoId(d.getNgoId())
                .ngoName(d.getNgoName())
                .status(d.getStatus().name())
                .build();
    }

    private DonationResponse toResponse(FoodDonation d) {
        return DonationResponse.builder()
                .id(d.getId())
                .foodName(d.getFoodName())
                .quantity(d.getQuantity())
                .quantityUnit(d.getQuantityUnit())
                .foodType(d.getFoodType())
                .preparedTime(d.getPreparedTime())
                .expiryTime(d.getExpiryTime())
                .pickupAddress(d.getPickupAddress())
                .latitude(d.getLatitude())
                .longitude(d.getLongitude())
                .city(d.getCity())
                .contactNumber(d.getContactNumber())
                .status(d.getStatus())
                .donorId(d.getDonorId())
                .donorName(d.getDonorName())
                .ngoId(d.getNgoId())
                .ngoName(d.getNgoName())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}
