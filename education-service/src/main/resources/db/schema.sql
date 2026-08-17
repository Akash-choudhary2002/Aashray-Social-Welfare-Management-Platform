-- ============================================================
-- Aashray :: Education Support Service :: Reference schema
-- Hibernate (ddl-auto=update) creates this automatically on boot.
-- ============================================================

CREATE DATABASE IF NOT EXISTS aashray_education_db;
USE aashray_education_db;

CREATE TABLE IF NOT EXISTS students (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name       VARCHAR(100)  NOT NULL,
    age             INT,
    gender          VARCHAR(20),
    city            VARCHAR(100),
    status          VARCHAR(20)   NOT NULL DEFAULT 'UNASSIGNED',

    ngo_id          BIGINT        NOT NULL,
    ngo_name        VARCHAR(100)  NOT NULL,

    created_at      DATETIME,
    updated_at      DATETIME,

    CONSTRAINT chk_student_status CHECK (status IN ('UNASSIGNED', 'ASSIGNED'))
);

CREATE TABLE IF NOT EXISTS education_assignments (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id        BIGINT        NOT NULL,
    student_name      VARCHAR(100)  NOT NULL,
    educator_id       BIGINT        NOT NULL,
    educator_name     VARCHAR(100)  NOT NULL,
    ngo_id            BIGINT        NOT NULL,
    ngo_name          VARCHAR(100)  NOT NULL,
    subject           VARCHAR(100)  NOT NULL,
    assignment_date   DATE          NOT NULL,
    status            VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',

    created_at        DATETIME,
    updated_at        DATETIME,

    CONSTRAINT fk_assignment_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT chk_assignment_status CHECK (status IN ('ACTIVE', 'COMPLETED', 'CANCELLED'))
);

CREATE INDEX idx_student_ngo        ON students (ngo_id);
CREATE INDEX idx_student_status     ON students (status);
CREATE INDEX idx_assignment_educator ON education_assignments (educator_id);
CREATE INDEX idx_assignment_ngo      ON education_assignments (ngo_id);
CREATE INDEX idx_assignment_student  ON education_assignments (student_id);
CREATE INDEX idx_assignment_status   ON education_assignments (status);

-- ngo_id / educator_id are logical foreign keys to users.id in aashray_auth_db.
-- No physical FK across databases in a microservices architecture —
-- referential integrity is enforced at the application layer instead.
