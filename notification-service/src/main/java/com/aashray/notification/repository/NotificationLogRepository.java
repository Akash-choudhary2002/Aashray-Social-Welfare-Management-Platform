package com.aashray.notification.repository;

import com.aashray.notification.entity.NotificationLog;
import com.aashray.notification.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    List<NotificationLog> findAllByOrderByCreatedAtDesc();

    List<NotificationLog> findByEventTypeOrderByCreatedAtDesc(String eventType);

    List<NotificationLog> findByStatusOrderByCreatedAtDesc(NotificationStatus status);

    long countByStatus(NotificationStatus status);
}
