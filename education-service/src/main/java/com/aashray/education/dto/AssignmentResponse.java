package com.aashray.education.dto;

import com.aashray.education.entity.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AssignmentResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long educatorId;
    private String educatorName;
    private Long ngoId;
    private String ngoName;
    private String subject;
    private LocalDate assignmentDate;
    private AssignmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AssignmentResponse() {}

    public AssignmentResponse(Long id, Long studentId, String studentName, Long educatorId, String educatorName, Long ngoId, String ngoName, String subject, LocalDate assignmentDate, AssignmentStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.educatorId = educatorId;
        this.educatorName = educatorName;
        this.ngoId = ngoId;
        this.ngoName = ngoName;
        this.subject = subject;
        this.assignmentDate = assignmentDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public Long getEducatorId() { return educatorId; }
    public String getEducatorName() { return educatorName; }
    public Long getNgoId() { return ngoId; }
    public String getNgoName() { return ngoName; }
    public String getSubject() { return subject; }
    public LocalDate getAssignmentDate() { return assignmentDate; }
    public AssignmentStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public static AssignmentResponseBuilder builder() {
        return new AssignmentResponseBuilder();
    }

    public static class AssignmentResponseBuilder {
        private Long id;
        private Long studentId;
        private String studentName;
        private Long educatorId;
        private String educatorName;
        private Long ngoId;
        private String ngoName;
        private String subject;
        private LocalDate assignmentDate;
        private AssignmentStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public AssignmentResponseBuilder id(Long id) { this.id = id; return this; }
        public AssignmentResponseBuilder studentId(Long studentId) { this.studentId = studentId; return this; }
        public AssignmentResponseBuilder studentName(String studentName) { this.studentName = studentName; return this; }
        public AssignmentResponseBuilder educatorId(Long educatorId) { this.educatorId = educatorId; return this; }
        public AssignmentResponseBuilder educatorName(String educatorName) { this.educatorName = educatorName; return this; }
        public AssignmentResponseBuilder ngoId(Long ngoId) { this.ngoId = ngoId; return this; }
        public AssignmentResponseBuilder ngoName(String ngoName) { this.ngoName = ngoName; return this; }
        public AssignmentResponseBuilder subject(String subject) { this.subject = subject; return this; }
        public AssignmentResponseBuilder assignmentDate(LocalDate assignmentDate) { this.assignmentDate = assignmentDate; return this; }
        public AssignmentResponseBuilder status(AssignmentStatus status) { this.status = status; return this; }
        public AssignmentResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AssignmentResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public AssignmentResponse build() {
            return new AssignmentResponse(id, studentId, studentName, educatorId, educatorName, ngoId, ngoName, subject, assignmentDate, status, createdAt, updatedAt);
        }
    }
}
