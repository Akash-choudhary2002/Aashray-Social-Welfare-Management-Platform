package com.aashray.notification.controller;

import com.aashray.notification.dto.ApiResponse;
import com.aashray.notification.dto.NotificationLogResponse;
import com.aashray.notification.service.NotificationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications/admin")
@Tag(name = "Notifications - Admin", description = "Admin-only audit log of every notification event consumed")
@PreAuthorize("hasRole('ADMIN')")
public class NotificationAdminController {

    private final NotificationLogService service;

    public NotificationAdminController(NotificationLogService service) {
        this.service = service;
    }

    @GetMapping("/logs")
    @Operation(summary = "Admin: view all notification logs")
    public ApiResponse<List<NotificationLogResponse>> allLogs() {
        return ApiResponse.success(service.getAllLogs());
    }

    @GetMapping("/logs/event/{eventType}")
    @Operation(summary = "Admin: view notification logs filtered by event type")
    public ApiResponse<List<NotificationLogResponse>> logsByEvent(@PathVariable String eventType) {
        return ApiResponse.success(service.getLogsByEventType(eventType));
    }

    @GetMapping("/stats")
    @Operation(summary = "Admin: notification counts by status for the Admin Dashboard")
    public ApiResponse<Map<String, Long>> stats() {
        return ApiResponse.success(service.getStats());
    }
}
