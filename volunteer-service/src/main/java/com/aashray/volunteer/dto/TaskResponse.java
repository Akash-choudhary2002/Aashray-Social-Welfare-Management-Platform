package com.aashray.volunteer.dto;

import com.aashray.volunteer.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String taskTitle;
    private String taskDescription;
    private LocalDate assignedDate;
    private TaskStatus status;
    private Long volunteerId;
    private String volunteerName;
    private Long ngoId;
    private String ngoName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
