package com.aashray.food.dto;

import com.aashray.food.entity.DonationStatus;
import com.aashray.food.entity.FoodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationResponse {
    private Long id;
    private String foodName;
    private Integer quantity;
    private String quantityUnit;
    private FoodType foodType;
    private LocalDateTime preparedTime;
    private LocalDateTime expiryTime;
    private String pickupAddress;
    private Double latitude;
    private Double longitude;
    private String city;
    private String contactNumber;
    private DonationStatus status;
    private Long donorId;
    private String donorName;
    private Long ngoId;
    private String ngoName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
