-- ============================================================
-- Aashray :: Auth Service :: Reference schema
-- Hibernate (ddl-auto=update) creates this automatically on boot.
-- Kept here for documentation / manual DB review purposes.
-- ============================================================

CREATE DATABASE IF NOT EXISTS aashray_auth_db;
USE aashray_auth_db;

CREATE TABLE IF NOT EXISTS users (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name       VARCHAR(100)  NOT NULL,
    email           VARCHAR(150)  NOT NULL,
    password        VARCHAR(255)  NOT NULL,
    phone_number    VARCHAR(15),
    address         VARCHAR(255),
    city            VARCHAR(100),
    role            VARCHAR(20)   NOT NULL,
    enabled         BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at      DATETIME,
    updated_at      DATETIME,

    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_role CHECK (
        role IN ('ADMIN', 'DONOR', 'NGO', 'EDUCATOR', 'VOLUNTEER', 'BENEFICIARY')
    )
);

CREATE INDEX idx_user_email ON users (email);
CREATE INDEX idx_user_role  ON users (role);

-- Seed an initial admin account.
-- Password below is the BCrypt hash of "Admin@12345" — change immediately after first login.
INSERT INTO users (full_name, email, password, role, enabled, created_at, updated_at)
VALUES (
    'Platform Admin',
    'admin@aashray.org',
    '$2a$12$8Zt1E6Yl4b1oXWKlYQXqUOZ3qzKjE9zGxU5xW1o0lE9x0k7Yq0O5S',
    'ADMIN',
    TRUE,
    NOW(),
    NOW()
);
