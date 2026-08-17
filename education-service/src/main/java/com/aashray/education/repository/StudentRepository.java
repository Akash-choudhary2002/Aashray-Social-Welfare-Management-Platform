package com.aashray.education.repository;

import com.aashray.education.entity.Student;
import com.aashray.education.entity.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByNgoIdOrderByCreatedAtDesc(Long ngoId);

    long countByStatus(StudentStatus status);
}
