package com.aashray.education.controller;

import com.aashray.education.dto.*;
import com.aashray.education.security.UserPrincipal;
import com.aashray.education.service.EducationService;
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
@RequestMapping("/api/education")
@Tag(name = "Education Support", description = "NGO, Educator and Admin education support flows")
public class EducationController {

    private final EducationService service;

    public EducationController(EducationService service) {
        this.service = service;
    }

    // ---------- NGO: Students ----------

    @PostMapping("/students")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: register a new student")
    public ResponseEntity<ApiResponse<StudentResponse>> registerStudent(@AuthenticationPrincipal UserPrincipal ngo,
                                                                          @Valid @RequestBody CreateStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student registered", service.registerStudent(ngo, request)));
    }

    @PutMapping("/students/{id}")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: update own registered student")
    public ApiResponse<StudentResponse> updateStudent(@AuthenticationPrincipal UserPrincipal ngo,
                                                        @PathVariable Long id,
                                                        @Valid @RequestBody UpdateStudentRequest request) {
        return ApiResponse.success("Student updated", service.updateStudent(ngo, id, request));
    }

    @GetMapping("/students/my-ngo")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: view students registered by this NGO")
    public ApiResponse<List<StudentResponse>> myNgoStudents(@AuthenticationPrincipal UserPrincipal ngo) {
        return ApiResponse.success(service.getNgoStudents(ngo));
    }

    // ---------- NGO: Assignments ----------

    @PostMapping("/assignments")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: assign an educator to a registered student")
    public ResponseEntity<ApiResponse<AssignmentResponse>> assignEducator(@AuthenticationPrincipal UserPrincipal ngo,
                                                                            @Valid @RequestBody AssignEducatorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Educator assigned", service.assignEducator(ngo, request)));
    }

    @PatchMapping("/assignments/{id}/cancel")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: cancel an active assignment")
    public ApiResponse<AssignmentResponse> cancelAssignment(@AuthenticationPrincipal UserPrincipal ngo, @PathVariable Long id) {
        return ApiResponse.success("Assignment cancelled", service.cancelAssignment(ngo, id));
    }

    @GetMapping("/assignments/ngo/history")
    @PreAuthorize("hasRole('NGO')")
    @Operation(summary = "NGO: view assignments made by this NGO")
    public ApiResponse<List<AssignmentResponse>> ngoAssignments(@AuthenticationPrincipal UserPrincipal ngo) {
        return ApiResponse.success(service.getNgoAssignments(ngo));
    }

    // ---------- Educator ----------

    @GetMapping("/assignments/my-students")
    @PreAuthorize("hasRole('EDUCATOR')")
    @Operation(summary = "Educator: view assigned students")
    public ApiResponse<List<AssignmentResponse>> myAssignedStudents(@AuthenticationPrincipal UserPrincipal educator) {
        return ApiResponse.success(service.getAssignedStudents(educator));
    }

    @PatchMapping("/assignments/{id}/complete")
    @PreAuthorize("hasRole('EDUCATOR')")
    @Operation(summary = "Educator: mark an assignment as completed")
    public ApiResponse<AssignmentResponse> completeAssignment(@AuthenticationPrincipal UserPrincipal educator, @PathVariable Long id) {
        return ApiResponse.success("Assignment marked completed", service.completeAssignment(educator, id));
    }

    // ---------- Admin ----------

    @GetMapping("/admin/students")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: view all registered students")
    public ApiResponse<List<StudentResponse>> allStudents() {
        return ApiResponse.success(service.getAllStudents());
    }

    @GetMapping("/admin/assignments")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: view all educator assignments")
    public ApiResponse<List<AssignmentResponse>> allAssignments() {
        return ApiResponse.success(service.getAllAssignments());
    }

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: student/assignment counts for the Admin Dashboard")
    public ApiResponse<Map<String, Long>> stats() {
        return ApiResponse.success(service.getStats());
    }

    @GetMapping("/admin/recent")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: most recent assignments for the Admin Dashboard")
    public ApiResponse<List<AssignmentResponse>> recent(@RequestParam(defaultValue = "5") int limit) {
        return ApiResponse.success(service.getRecentAssignments(limit));
    }
}
