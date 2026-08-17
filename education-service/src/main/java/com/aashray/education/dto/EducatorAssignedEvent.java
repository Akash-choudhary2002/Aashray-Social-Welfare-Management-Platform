package com.aashray.education.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

public class EducatorAssignedEvent implements Serializable {
    private Long assignmentId;
    private Long studentId;
    private String studentName;
    private Long educatorId;
    private String educatorName;
    private Long ngoId;
    private String ngoName;
    private String subject;

    public EducatorAssignedEvent() {}

    public EducatorAssignedEvent(Long assignmentId, Long studentId, String studentName, Long educatorId, String educatorName, Long ngoId, String ngoName, String subject) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.educatorId = educatorId;
        this.educatorName = educatorName;
        this.ngoId = ngoId;
        this.ngoName = ngoName;
        this.subject = subject;
    }

    public static EducatorAssignedEventBuilder builder() {
        return new EducatorAssignedEventBuilder();
    }

    public static class EducatorAssignedEventBuilder {
        private Long assignmentId;
        private Long studentId;
        private String studentName;
        private Long educatorId;
        private String educatorName;
        private Long ngoId;
        private String ngoName;
        private String subject;

        public EducatorAssignedEventBuilder assignmentId(Long assignmentId) { this.assignmentId = assignmentId; return this; }
        public EducatorAssignedEventBuilder studentId(Long studentId) { this.studentId = studentId; return this; }
        public EducatorAssignedEventBuilder studentName(String studentName) { this.studentName = studentName; return this; }
        public EducatorAssignedEventBuilder educatorId(Long educatorId) { this.educatorId = educatorId; return this; }
        public EducatorAssignedEventBuilder educatorName(String educatorName) { this.educatorName = educatorName; return this; }
        public EducatorAssignedEventBuilder ngoId(Long ngoId) { this.ngoId = ngoId; return this; }
        public EducatorAssignedEventBuilder ngoName(String ngoName) { this.ngoName = ngoName; return this; }
        public EducatorAssignedEventBuilder subject(String subject) { this.subject = subject; return this; }

        public EducatorAssignedEvent build() {
            return new EducatorAssignedEvent(assignmentId, studentId, studentName, educatorId, educatorName, ngoId, ngoName, subject);
        }
    }

    public Long getAssignmentId() { return assignmentId; }
    public Long getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public Long getEducatorId() { return educatorId; }
    public String getEducatorName() { return educatorName; }
    public Long getNgoId() { return ngoId; }
    public String getNgoName() { return ngoName; }
    public String getSubject() { return subject; }
}
