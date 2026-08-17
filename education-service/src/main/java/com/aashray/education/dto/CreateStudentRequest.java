package com.aashray.education.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateStudentRequest(

        @NotBlank(message = "Full name is required")
        @Size(max = 100)
        String fullName,

        @Min(value = 1, message = "Age must be positive")
        Integer age,

        String gender,

        @NotBlank(message = "City is required")
        String city
) {}
