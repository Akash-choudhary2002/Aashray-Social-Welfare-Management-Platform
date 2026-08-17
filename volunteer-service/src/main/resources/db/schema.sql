-- ============================================================
-- Aashray :: Volunteer Service :: Reference schema
-- Hibernate (ddl-auto=update) creates this automatically on boot.
-- ============================================================

CREATE DATABASE IF NOT EXISTS aashray_volunteer_db;
USE aashray_volunteer_db;

CREATE TABLE IF NOT EXISTS volunteer_tasks (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_title        VARCHAR(150)  NOT NULL,
    task_description  VARCHAR(500),
    assigned_date     DATE          NOT NULL,
    status            VARCHAR(20)   NOT NULL DEFAULT 'ASSIGNED',

    volunteer_id      BIGINT        NOT NULL,
    volunteer_name    VARCHAR(100)  NOT NULL,
    ngo_id            BIGINT        NOT NULL,
    ngo_name          VARCHAR(100)  NOT NULL,

    created_at        DATETIME,
    updated_at        DATETIME,

    CONSTRAINT chk_task_status CHECK (
        status IN ('ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')
    )
);

CREATE INDEX idx_task_volunteer ON volunteer_tasks (volunteer_id);
CREATE INDEX idx_task_ngo       ON volunteer_tasks (ngo_id);
CREATE INDEX idx_task_status    ON volunteer_tasks (status);

-- volunteer_id / ngo_id are logical foreign keys to users.id in aashray_auth_db.
-- No physical FK across databases in a microservices architecture —
-- referential integrity is enforced at the application layer instead.
