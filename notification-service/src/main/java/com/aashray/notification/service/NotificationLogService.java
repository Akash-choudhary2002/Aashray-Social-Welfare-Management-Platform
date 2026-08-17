package com.aashray.notification.service;

import com.aashray.notification.dto.NotificationLogResponse;
import com.aashray.notification.entity.NotificationLog;
import com.aashray.notification.entity.NotificationStatus;
import com.aashray.notification.repository.NotificationLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NotificationLogService {

    private final NotificationLogRepository repository;

    public NotificationLogService(NotificationLogRepository repository) {
        this.repository = repository;
    }

    public List<NotificationLogResponse> getAllLogs() {
        return repository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<NotificationLogResponse> getLogsByEventType(String eventType) {
        return repository.findByEventTypeOrderByCreatedAtDesc(eventType).stream()
                .map(this::toResponse)
                .toList();
    }

    public Map<String, Long> getStats() {
        return Map.of(
                "total", repository.count(),
                "sent", repository.countByStatus(NotificationStatus.SENT),
                "simulated", repository.countByStatus(NotificationStatus.SIMULATED),
                "failed", repository.countByStatus(NotificationStatus.FAILED)
        );
    }

    private NotificationLogResponse toResponse(NotificationLog n) {
        return NotificationLogResponse.builder()
                .id(n.getId())
                .eventType(n.getEventType())
                .recipientEmail(n.getRecipientEmail())
                .subject(n.getSubject())
                .body(n.getBody())
                .status(n.getStatus())
                .errorMessage(n.getErrorMessage())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
