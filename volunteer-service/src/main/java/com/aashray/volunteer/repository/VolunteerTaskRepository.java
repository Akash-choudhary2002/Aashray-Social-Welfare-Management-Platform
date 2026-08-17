package com.aashray.volunteer.repository;

import com.aashray.volunteer.entity.TaskStatus;
import com.aashray.volunteer.entity.VolunteerTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerTaskRepository extends JpaRepository<VolunteerTask, Long> {

    List<VolunteerTask> findByVolunteerIdOrderByCreatedAtDesc(Long volunteerId);

    List<VolunteerTask> findByVolunteerIdAndStatusOrderByCreatedAtDesc(Long volunteerId, TaskStatus status);

    List<VolunteerTask> findByNgoIdOrderByCreatedAtDesc(Long ngoId);

    long countByStatus(TaskStatus status);
}
