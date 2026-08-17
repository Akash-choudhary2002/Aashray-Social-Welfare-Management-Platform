package com.aashray.monetary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PaymentFailedRequest(
        @NotBlank(message = "razorpayOrderId is required") String razorpayOrderId,
        @Size(max = 255, message = "Reason must be under 255 characters") String reason
) {}
