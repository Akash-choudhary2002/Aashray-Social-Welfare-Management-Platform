package com.aashray.food.dto;

import com.aashray.food.entity.FoodType;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CreateDonationRequest(

        @NotBlank(message = "Food name is required")
        @Size(max = 150)
        String foodName,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity,

        String quantityUnit,

        @NotNull(message = "Food type is required")
        FoodType foodType,

        @NotNull(message = "Prepared time is required")
        LocalDateTime preparedTime,

        @NotNull(message = "Expiry time is required")
        @Future(message = "Expiry time must be in the future")
        LocalDateTime expiryTime,

        @NotBlank(message = "Pickup address is required")
        String pickupAddress,

        // Optional — set by the Leaflet location picker on the frontend.
        // Bean Validation skips range checks when the value is null, so a
        // donor who never opens the map can still submit with just a
        // typed address only, same as before this feature existed.
        @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
        Double latitude,

        @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
        Double longitude,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "Contact number is required")
        @Pattern(regexp = "^\\d{10}$", message = "Contact number must be 10 digits")
        String contactNumber
) {}
