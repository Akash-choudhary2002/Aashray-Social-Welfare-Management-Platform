package com.aashray.education.dto;

import com.aashray.education.entity.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class StudentResponse {
    private Long id;
    private String fullName;
    private Integer age;
    private String gender;
    private String city;
    private StudentStatus status;
    private Long ngoId;
    private String ngoName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StudentResponse() {}

    public StudentResponse(Long id, String fullName, Integer age, String gender, String city, StudentStatus status, Long ngoId, String ngoName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.city = city;
        this.status = status;
        this.ngoId = ngoId;
        this.ngoName = ngoName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public Integer getAge() { return age; }
    public String getGender() { return gender; }
    public String getCity() { return city; }
    public StudentStatus getStatus() { return status; }
    public Long getNgoId() { return ngoId; }
    public String getNgoName() { return ngoName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public static StudentResponseBuilder builder() {
        return new StudentResponseBuilder();
    }

    public static class StudentResponseBuilder {
        private Long id;
        private String fullName;
        private Integer age;
        private String gender;
        private String city;
        private StudentStatus status;
        private Long ngoId;
        private String ngoName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public StudentResponseBuilder id(Long id) { this.id = id; return this; }
        public StudentResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public StudentResponseBuilder age(Integer age) { this.age = age; return this; }
        public StudentResponseBuilder gender(String gender) { this.gender = gender; return this; }
        public StudentResponseBuilder city(String city) { this.city = city; return this; }
        public StudentResponseBuilder status(StudentStatus status) { this.status = status; return this; }
        public StudentResponseBuilder ngoId(Long ngoId) { this.ngoId = ngoId; return this; }
        public StudentResponseBuilder ngoName(String ngoName) { this.ngoName = ngoName; return this; }
        public StudentResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public StudentResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public StudentResponse build() {
            return new StudentResponse(id, fullName, age, gender, city, status, ngoId, ngoName, createdAt, updatedAt);
        }
    }
}
