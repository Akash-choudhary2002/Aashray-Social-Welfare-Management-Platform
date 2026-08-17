package com.aashray.food.dto;

import com.aashray.food.entity.FoodType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record UpdateDonationRequest(
        String foodName,

        @Positive
        Integer quantity,

        String quantityUnit,

        FoodType foodType,

        LocalDateTime preparedTime,

        @Future(message = "Expiry time must be in the future")
        LocalDateTime expiryTime,

        String pickupAddress,

        @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
        Double latitude,

        @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
        Double longitude,

        String city,

        String contactNumber
) {}
