package com.aashray.auth.controller;

import com.aashray.auth.dto.ApiResponse;
import com.aashray.auth.dto.UserProfileResponse;
import com.aashray.auth.entity.Role;
import com.aashray.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/admin")
@Tag(name = "Admin - User Management", description = "Admin-only user management, feeds the Admin Dashboard")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @Operation(summary = "List all users")
    public ApiResponse<List<UserProfileResponse>> getAllUsers() {
        return ApiResponse.success(userService.getAllUsers());
    }

    @GetMapping("/users/role/{role}")
    @Operation(summary = "List users by role")
    public ApiResponse<List<UserProfileResponse>> getUsersByRole(@PathVariable Role role) {
        return ApiResponse.success(userService.getUsersByRole(role));
    }

    @PatchMapping("/users/{userId}/status")
    @Operation(summary = "Enable or disable a user account")
    public ApiResponse<Void> setUserEnabled(@PathVariable Long userId, @RequestParam boolean enabled) {
        userService.setUserEnabled(userId, enabled);
        return ApiResponse.success(enabled ? "User enabled" : "User disabled", null);
    }

    @GetMapping("/dashboard/counts")
    @Operation(summary = "User counts by role for the Admin Dashboard")
    public ApiResponse<Map<String, Long>> getDashboardCounts() {
        return ApiResponse.success(userService.getUserCountsByRole());
    }
}
