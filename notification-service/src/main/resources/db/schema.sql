-- ============================================================
-- Aashray :: Notification Service :: Reference schema
-- Hibernate (ddl-auto=update) creates this automatically on boot.
-- ============================================================

CREATE DATABASE IF NOT EXISTS aashray_notification_db;
USE aashray_notification_db;

CREATE TABLE IF NOT EXISTS notification_logs (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_type        VARCHAR(50)   NOT NULL,
    recipient_email   VARCHAR(150)  NOT NULL,
    subject           VARCHAR(255)  NOT NULL,
    body              TEXT          NOT NULL,
    status            VARCHAR(20)   NOT NULL DEFAULT 'SIMULATED',
    error_message      VARCHAR(500),

    created_at        DATETIME,

    CONSTRAINT chk_notification_status CHECK (
        status IN ('SENT', 'FAILED', 'SIMULATED')
    )
);

CREATE INDEX idx_notification_event  ON notification_logs (event_type);
CREATE INDEX idx_notification_status ON notification_logs (status);
