package com.aashray.education.dto;

import jakarta.validation.constraints.Min;

public record UpdateStudentRequest(
        String fullName,

        @Min(value = 1, message = "Age must be positive")
        Integer age,

        String gender,

        String city
) {}
