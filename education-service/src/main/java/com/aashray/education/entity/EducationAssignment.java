package com.aashray.education.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "education_assignments", indexes = {
        @Index(name = "idx_assignment_educator", columnList = "educator_id"),
        @Index(name = "idx_assignment_ngo", columnList = "ngo_id"),
        @Index(name = "idx_assignment_student", columnList = "student_id"),
        @Index(name = "idx_assignment_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
public class EducationAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false, length = 100)
    private String studentName;

    // Educator assigned by the NGO, denormalized from Auth Service
    @Column(nullable = false)
    private Long educatorId;

    @Column(nullable = false, length = 100)
    private String educatorName;

    @Column(nullable = false)
    private Long ngoId;

    @Column(nullable = false, length = 100)
    private String ngoName;

    @Column(nullable = false, length = 100)
    private String subject;

    @Column(nullable = false)
    private LocalDate assignmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AssignmentStatus status = AssignmentStatus.ACTIVE;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public EducationAssignment() {}

    public EducationAssignment(Long id, Long studentId, String studentName, Long educatorId, String educatorName, Long ngoId, String ngoName, String subject, LocalDate assignmentDate, AssignmentStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.educatorId = educatorId;
        this.educatorName = educatorName;
        this.ngoId = ngoId;
        this.ngoName = ngoName;
        this.subject = subject;
        this.assignmentDate = assignmentDate;
        this.status = status != null ? status : AssignmentStatus.ACTIVE;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static EducationAssignmentBuilder builder() {
        return new EducationAssignmentBuilder();
    }

    public static class EducationAssignmentBuilder {
        private Long id;
        private Long studentId;
        private String studentName;
        private Long educatorId;
        private String educatorName;
        private Long ngoId;
        private String ngoName;
        private String subject;
        private LocalDate assignmentDate;
        private AssignmentStatus status = AssignmentStatus.ACTIVE;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public EducationAssignmentBuilder id(Long id) { this.id = id; return this; }
        public EducationAssignmentBuilder studentId(Long studentId) { this.studentId = studentId; return this; }
        public EducationAssignmentBuilder studentName(String studentName) { this.studentName = studentName; return this; }
        public EducationAssignmentBuilder educatorId(Long educatorId) { this.educatorId = educatorId; return this; }
        public EducationAssignmentBuilder educatorName(String educatorName) { this.educatorName = educatorName; return this; }
        public EducationAssignmentBuilder ngoId(Long ngoId) { this.ngoId = ngoId; return this; }
        public EducationAssignmentBuilder ngoName(String ngoName) { this.ngoName = ngoName; return this; }
        public EducationAssignmentBuilder subject(String subject) { this.subject = subject; return this; }
        public EducationAssignmentBuilder assignmentDate(LocalDate assignmentDate) { this.assignmentDate = assignmentDate; return this; }
        public EducationAssignmentBuilder status(AssignmentStatus status) { this.status = status; return this; }
        public EducationAssignmentBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public EducationAssignmentBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public EducationAssignment build() {
            return new EducationAssignment(id, studentId, studentName, educatorId, educatorName, ngoId, ngoName, subject, assignmentDate, status, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Long getEducatorId() { return educatorId; }
    public void setEducatorId(Long educatorId) { this.educatorId = educatorId; }

    public String getEducatorName() { return educatorName; }
    public void setEducatorName(String educatorName) { this.educatorName = educatorName; }

    public Long getNgoId() { return ngoId; }
    public void setNgoId(Long ngoId) { this.ngoId = ngoId; }

    public String getNgoName() { return ngoName; }
    public void setNgoName(String ngoName) { this.ngoName = ngoName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public LocalDate getAssignmentDate() { return assignmentDate; }
    public void setAssignmentDate(LocalDate assignmentDate) { this.assignmentDate = assignmentDate; }

    public AssignmentStatus getStatus() { return status; }
    public void setStatus(AssignmentStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
