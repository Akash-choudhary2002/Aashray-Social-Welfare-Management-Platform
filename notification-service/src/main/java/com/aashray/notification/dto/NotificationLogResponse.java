package com.aashray.notification.dto;

import com.aashray.notification.entity.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLogResponse {
    private Long id;
    private String eventType;
    private String recipientEmail;
    private String subject;
    private String body;
    private NotificationStatus status;
    private String errorMessage;
    private LocalDateTime createdAt;
}
