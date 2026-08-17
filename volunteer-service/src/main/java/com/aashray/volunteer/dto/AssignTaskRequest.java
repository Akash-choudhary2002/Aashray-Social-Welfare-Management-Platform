package com.aashray.volunteer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AssignTaskRequest(

        @NotNull(message = "Volunteer id is required")
        @Positive(message = "Volunteer id must be a valid positive id")
        Long volunteerId,

        @NotBlank(message = "Volunteer name is required")
        String volunteerName,

        @NotBlank(message = "Task title is required")
        @Size(max = 150)
        String taskTitle,

        @Size(max = 500)
        String taskDescription,

        @NotNull(message = "Assigned date is required")
        LocalDate assignedDate
) {}
