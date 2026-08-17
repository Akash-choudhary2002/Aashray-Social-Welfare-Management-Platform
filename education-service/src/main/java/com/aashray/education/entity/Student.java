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

import java.time.LocalDateTime;

@Entity
@Table(name = "students", indexes = {
        @Index(name = "idx_student_ngo", columnList = "ngo_id"),
        @Index(name = "idx_student_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    private Integer age;

    @Column(length = 20)
    private String gender;

    @Column(length = 100)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StudentStatus status = StudentStatus.UNASSIGNED;

    // NGO that registered the student, denormalized from Auth Service
    @Column(nullable = false)
    private Long ngoId;

    @Column(nullable = false, length = 100)
    private String ngoName;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Student() {}

    public Student(Long id, String fullName, Integer age, String gender, String city, StudentStatus status, Long ngoId, String ngoName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.city = city;
        this.status = status != null ? status : StudentStatus.UNASSIGNED;
        this.ngoId = ngoId;
        this.ngoName = ngoName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public static class StudentBuilder {
        private Long id;
        private String fullName;
        private Integer age;
        private String gender;
        private String city;
        private StudentStatus status = StudentStatus.UNASSIGNED;
        private Long ngoId;
        private String ngoName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public StudentBuilder id(Long id) { this.id = id; return this; }
        public StudentBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public StudentBuilder age(Integer age) { this.age = age; return this; }
        public StudentBuilder gender(String gender) { this.gender = gender; return this; }
        public StudentBuilder city(String city) { this.city = city; return this; }
        public StudentBuilder status(StudentStatus status) { this.status = status; return this; }
        public StudentBuilder ngoId(Long ngoId) { this.ngoId = ngoId; return this; }
        public StudentBuilder ngoName(String ngoName) { this.ngoName = ngoName; return this; }
        public StudentBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public StudentBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Student build() {
            return new Student(id, fullName, age, gender, city, status, ngoId, ngoName, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public StudentStatus getStatus() { return status; }
    public void setStatus(StudentStatus status) { this.status = status; }

    public Long getNgoId() { return ngoId; }
    public void setNgoId(Long ngoId) { this.ngoId = ngoId; }

    public String getNgoName() { return ngoName; }
    public void setNgoName(String ngoName) { this.ngoName = ngoName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
