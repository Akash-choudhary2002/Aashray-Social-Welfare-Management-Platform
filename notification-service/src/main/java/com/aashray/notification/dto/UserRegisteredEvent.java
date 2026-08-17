package com.aashray.notification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Mirrors auth-service's UserRegisteredEvent payload. Kept as a
 * separate class per Notification Service's own package (microservices
 * don't share DTO jars) — field names must match the JSON produced by
 * the publisher exactly.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegisteredEvent implements Serializable {
    private Long userId;
    private String fullName;
    private String email;
    private String role;
}
