package com.aashray.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducatorAssignedEvent implements Serializable {
    private Long assignmentId;
    private Long studentId;
    private String studentName;
    private Long educatorId;
    private String educatorName;
    private Long ngoId;
    private String ngoName;
    private String subject;
}
