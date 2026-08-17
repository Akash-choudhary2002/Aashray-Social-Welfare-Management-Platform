package com.aashray.food.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "food_donations", indexes = {
        @Index(name = "idx_donation_status", columnList = "status"),
        @Index(name = "idx_donation_donor", columnList = "donor_id"),
        @Index(name = "idx_donation_ngo", columnList = "ngo_id"),
        @Index(name = "idx_donation_city", columnList = "city")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String foodName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 30)
    private String quantityUnit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FoodType foodType;

    @Column(nullable = false)
    private LocalDateTime preparedTime;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @Column(nullable = false, length = 255)
    private String pickupAddress;

    // Set by the donor's Leaflet location picker (click on the map, or
    // drag the marker, then confirm) - not reverse-geocoded, since the
    // picker returns coordinates only, no address lookup. Nullable -
    // donations created before this feature, or where the donor never
    // opens the picker, simply have no coordinates, and the UI falls
    // back to showing pickupAddress as plain text.
    private Double latitude;

    private Double longitude;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 15)
    private String contactNumber;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 20)
    private DonationStatus status = DonationStatus.PENDING;

    // Donor info denormalized from Auth Service (no cross-service join in a microservices world)
    @Column(nullable = false)
    private Long donorId;

    @Column(nullable = false, length = 100)
    private String donorName;

    // NGO info populated once a donation is accepted
    private Long ngoId;

    @Column(length = 100)
    private String ngoName;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
