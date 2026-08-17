package com.aashray.volunteer.controller;

import com.aashray.volunteer.dto.*;
import com.aashray.volunteer.security.UserPrincipal;
import com.aashray.volunteer.service.VolunteerTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/volunteers")
@Tag(name = "Volunteers", description = "NGO, Volunteer and Admin volunteer task flows")
public class VolunteerTaskController {

    private final VolunteerTaskService service;

    public VolunteerTaskController(VolunteerTaskService service) {
        this.service = service;
    }

    // ---------- NGO ----------

    @PostMapping("/tasks")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: assign a task to a volunteer")
    public ResponseEntity<ApiResponse<TaskResponse>> assignTask(@AuthenticationPrincipal UserPrincipal ngo,
                                                                   @Valid @RequestBody AssignTaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Task assigned", service.assignTask(ngo, request)));
    }

    @PatchMapping("/tasks/{id}/cancel")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: cancel a task before it's completed")
    public ApiResponse<TaskResponse> cancelTask(@AuthenticationPrincipal UserPrincipal ngo, @PathVariable Long id) {
        return ApiResponse.success("Task cancelled", service.cancelTask(ngo, id));
    }

    @GetMapping("/tasks/ngo/history")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: view tasks assigned by this NGO")
    public ApiResponse<List<TaskResponse>> ngoTasks(@AuthenticationPrincipal UserPrincipal ngo) {
        return ApiResponse.success(service.getNgoTasks(ngo));
    }

    // ---------- Volunteer ----------

    @GetMapping("/tasks/my-tasks")
    @PreAuthorize("hasRole('VOLUNTEER')")
    @Operation(summary = "Volunteer: view own assigned tasks (dashboard)")
    public ApiResponse<List<TaskResponse>> myTasks(@AuthenticationPrincipal UserPrincipal volunteer) {
        return ApiResponse.success(service.getMyTasks(volunteer));
    }

    @GetMapping("/tasks/completed")
    @PreAuthorize("hasRole('VOLUNTEER')")
    @Operation(summary = "Volunteer: view own completed task history")
    public ApiResponse<List<TaskResponse>> completedTasks(@AuthenticationPrincipal UserPrincipal volunteer) {
        return ApiResponse.success(service.getCompletedTasks(volunteer));
    }

    @PatchMapping("/tasks/{id}/start")
    @PreAuthorize("hasRole('VOLUNTEER')")
    @Operation(summary = "Volunteer: mark an assigned task as in progress")
    public ApiResponse<TaskResponse> startTask(@AuthenticationPrincipal UserPrincipal volunteer, @PathVariable Long id) {
        return ApiResponse.success("Task started", service.startTask(volunteer, id));
    }

    @PatchMapping("/tasks/{id}/complete")
    @PreAuthorize("hasRole('VOLUNTEER')")
    @Operation(summary = "Volunteer: mark an in-progress task as completed")
    public ApiResponse<TaskResponse> completeTask(@AuthenticationPrincipal UserPrincipal volunteer, @PathVariable Long id) {
        return ApiResponse.success("Task completed", service.completeTask(volunteer, id));
    }

    // ---------- Admin ----------

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: view all volunteer tasks across the platform")
    public ApiResponse<List<TaskResponse>> allTasks() {
        return ApiResponse.success(service.getAllTasks());
    }

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: task counts by status for the Admin Dashboard")
    public ApiResponse<Map<String, Long>> stats() {
        return ApiResponse.success(service.getStats());
    }

    @GetMapping("/admin/recent")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: most recent volunteer tasks for the Admin Dashboard")
    public ApiResponse<List<TaskResponse>> recent(@RequestParam(defaultValue = "5") int limit) {
        return ApiResponse.success(service.getRecentTasks(limit));
    }
}
