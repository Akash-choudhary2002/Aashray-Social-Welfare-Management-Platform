package com.aashray.volunteer.service;

import com.aashray.volunteer.dto.*;
import com.aashray.volunteer.entity.TaskStatus;
import com.aashray.volunteer.entity.VolunteerTask;
import com.aashray.volunteer.exception.InvalidTaskStateException;
import com.aashray.volunteer.exception.TaskNotFoundException;
import com.aashray.volunteer.exception.UnauthorizedTaskAccessException;
import com.aashray.volunteer.repository.VolunteerTaskRepository;
import com.aashray.volunteer.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class VolunteerTaskService {

    private final VolunteerTaskRepository repository;
    private final EventPublisherService eventPublisherService;

    public VolunteerTaskService(VolunteerTaskRepository repository, EventPublisherService eventPublisherService) {
        this.repository = repository;
        this.eventPublisherService = eventPublisherService;
    }

    // ---------- NGO operations ----------

    @Transactional
    public TaskResponse assignTask(UserPrincipal ngo, AssignTaskRequest request) {
        VolunteerTask task = VolunteerTask.builder()
                .taskTitle(request.taskTitle())
                .taskDescription(request.taskDescription())
                .assignedDate(request.assignedDate())
                .status(TaskStatus.ASSIGNED)
                .volunteerId(request.volunteerId())
                .volunteerName(request.volunteerName())
                .ngoId(ngo.userId())
                .ngoName(ngo.fullName())
                .build();

        VolunteerTask saved = repository.save(task);

        eventPublisherService.publishVolunteerAssigned(toEvent(saved));
        return toResponse(saved);
    }

    @Transactional
    public TaskResponse cancelTask(UserPrincipal ngo, Long taskId) {
        VolunteerTask task = findOrThrow(taskId);
        assertOwnedByNgo(task, ngo.userId());

        if (task.getStatus() == TaskStatus.COMPLETED || task.getStatus() == TaskStatus.CANCELLED) {
            throw new InvalidTaskStateException("Task is already in a terminal state (" + task.getStatus() + ")");
        }

        task.setStatus(TaskStatus.CANCELLED);
        return toResponse(repository.save(task));
    }

    public List<TaskResponse> getNgoTasks(UserPrincipal ngo) {
        return repository.findByNgoIdOrderByCreatedAtDesc(ngo.userId()).stream()
                .map(this::toResponse)
                .toList();
    }

    // ---------- Volunteer operations ----------

    public List<TaskResponse> getMyTasks(UserPrincipal volunteer) {
        return repository.findByVolunteerIdOrderByCreatedAtDesc(volunteer.userId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TaskResponse> getCompletedTasks(UserPrincipal volunteer) {
        return repository.findByVolunteerIdAndStatusOrderByCreatedAtDesc(volunteer.userId(), TaskStatus.COMPLETED).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public TaskResponse startTask(UserPrincipal volunteer, Long taskId) {
        VolunteerTask task = findOrThrow(taskId);
        assertOwnedByVolunteer(task, volunteer.userId());

        if (task.getStatus() != TaskStatus.ASSIGNED) {
            throw new InvalidTaskStateException("Only ASSIGNED tasks can be started");
        }

        task.setStatus(TaskStatus.IN_PROGRESS);
        return toResponse(repository.save(task));
    }

    @Transactional
    public TaskResponse completeTask(UserPrincipal volunteer, Long taskId) {
        VolunteerTask task = findOrThrow(taskId);
        assertOwnedByVolunteer(task, volunteer.userId());

        if (task.getStatus() != TaskStatus.IN_PROGRESS) {
            throw new InvalidTaskStateException("Only IN_PROGRESS tasks can be marked COMPLETED");
        }

        task.setStatus(TaskStatus.COMPLETED);
        return toResponse(repository.save(task));
    }

    // ---------- Admin operations ----------

    public List<TaskResponse> getAllTasks() {
        return repository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::toResponse)
                .toList();
    }

    public Map<String, Long> getStats() {
        return Map.of(
                "totalTasks", repository.count(),
                "assigned", repository.countByStatus(TaskStatus.ASSIGNED),
                "inProgress", repository.countByStatus(TaskStatus.IN_PROGRESS),
                "completed", repository.countByStatus(TaskStatus.COMPLETED),
                "cancelled", repository.countByStatus(TaskStatus.CANCELLED)
        );
    }

    public List<TaskResponse> getRecentTasks(int limit) {
        return repository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(limit)
                .map(this::toResponse)
                .toList();
    }

    // ---------- helpers ----------

    private VolunteerTask findOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    private void assertOwnedByNgo(VolunteerTask task, Long ngoId) {
        if (!task.getNgoId().equals(ngoId)) {
            throw new UnauthorizedTaskAccessException("You do not have access to this task");
        }
    }

    private void assertOwnedByVolunteer(VolunteerTask task, Long volunteerId) {
        if (!task.getVolunteerId().equals(volunteerId)) {
            throw new UnauthorizedTaskAccessException("You do not have access to this task");
        }
    }

    private VolunteerAssignedEvent toEvent(VolunteerTask t) {
        return VolunteerAssignedEvent.builder()
                .taskId(t.getId())
                .taskTitle(t.getTaskTitle())
                .volunteerId(t.getVolunteerId())
                .volunteerName(t.getVolunteerName())
                .ngoId(t.getNgoId())
                .ngoName(t.getNgoName())
                .build();
    }

    private TaskResponse toResponse(VolunteerTask t) {
        return TaskResponse.builder()
                .id(t.getId())
                .taskTitle(t.getTaskTitle())
                .taskDescription(t.getTaskDescription())
                .assignedDate(t.getAssignedDate())
                .status(t.getStatus())
                .volunteerId(t.getVolunteerId())
                .volunteerName(t.getVolunteerName())
                .ngoId(t.getNgoId())
                .ngoName(t.getNgoName())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }
}
