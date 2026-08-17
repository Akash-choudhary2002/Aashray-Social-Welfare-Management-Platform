package com.aashray.volunteer.entity;

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
@Table(name = "volunteer_tasks", indexes = {
        @Index(name = "idx_task_volunteer", columnList = "volunteer_id"),
        @Index(name = "idx_task_ngo", columnList = "ngo_id"),
        @Index(name = "idx_task_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String taskTitle;

    @Column(length = 500)
    private String taskDescription;

    @Column(nullable = false)
    private LocalDate assignedDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 20)
    private TaskStatus status = TaskStatus.ASSIGNED;

    // Volunteer assigned by the NGO, denormalized from Auth Service
    @Column(nullable = false)
    private Long volunteerId;

    @Column(nullable = false, length = 100)
    private String volunteerName;

    @Column(nullable = false)
    private Long ngoId;

    @Column(nullable = false, length = 100)
    private String ngoName;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
