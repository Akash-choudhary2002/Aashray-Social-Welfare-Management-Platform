package com.aashray.volunteer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerAssignedEvent implements Serializable {
    private Long taskId;
    private String taskTitle;
    private Long volunteerId;
    private String volunteerName;
    private Long ngoId;
    private String ngoName;
}
