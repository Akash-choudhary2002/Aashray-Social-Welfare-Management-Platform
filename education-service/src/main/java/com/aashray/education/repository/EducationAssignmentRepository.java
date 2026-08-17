package com.aashray.education.repository;

import com.aashray.education.entity.AssignmentStatus;
import com.aashray.education.entity.EducationAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationAssignmentRepository extends JpaRepository<EducationAssignment, Long> {

    List<EducationAssignment> findByEducatorIdOrderByCreatedAtDesc(Long educatorId);

    List<EducationAssignment> findByNgoIdOrderByCreatedAtDesc(Long ngoId);

    List<EducationAssignment> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    long countByStatus(AssignmentStatus status);
}
