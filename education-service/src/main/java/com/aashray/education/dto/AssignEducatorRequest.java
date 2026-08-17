package com.aashray.education.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record AssignEducatorRequest(

        @NotNull(message = "Student id is required")
        @Positive(message = "Student id must be a valid positive id")
        Long studentId,

        @NotNull(message = "Educator id is required")
        @Positive(message = "Educator id must be a valid positive id")
        Long educatorId,

        @NotBlank(message = "Educator name is required")
        String educatorName,

        @NotBlank(message = "Subject is required")
        String subject,

        @NotNull(message = "Assignment date is required")
        LocalDate assignmentDate
) {}
