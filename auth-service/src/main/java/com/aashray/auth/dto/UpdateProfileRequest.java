package com.aashray.auth.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(min = 2, max = 100)
        String fullName,

        @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
        String phoneNumber,

        String address,

        String city
) {}
