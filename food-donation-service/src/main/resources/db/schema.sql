-- ============================================================
-- Aashray :: Food Donation Service :: Reference schema
-- Hibernate (ddl-auto=update) creates this automatically on boot.
-- ============================================================

CREATE DATABASE IF NOT EXISTS aashray_food_db;
USE aashray_food_db;

CREATE TABLE IF NOT EXISTS food_donations (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    food_name       VARCHAR(150)  NOT NULL,
    quantity        INT           NOT NULL,
    quantity_unit   VARCHAR(30),
    food_type       VARCHAR(30)   NOT NULL,
    prepared_time   DATETIME      NOT NULL,
    expiry_time     DATETIME      NOT NULL,
    pickup_address  VARCHAR(255)  NOT NULL,
    latitude        DECIMAL(10,7),
    longitude       DECIMAL(10,7),
    city            VARCHAR(100)  NOT NULL,
    contact_number  VARCHAR(15)   NOT NULL,
    status          VARCHAR(20)   NOT NULL DEFAULT 'PENDING',

    donor_id        BIGINT        NOT NULL,
    donor_name      VARCHAR(100)  NOT NULL,
    ngo_id          BIGINT,
    ngo_name        VARCHAR(100),

    created_at      DATETIME,
    updated_at      DATETIME,

    CONSTRAINT chk_food_type CHECK (
        food_type IN ('COOKED', 'RAW', 'PACKAGED', 'BAKERY', 'FRUITS_VEGETABLES', 'OTHER')
    ),
    CONSTRAINT chk_donation_status CHECK (
        status IN ('PENDING', 'ACCEPTED', 'REJECTED', 'PICKED_UP', 'DELIVERED', 'EXPIRED')
    )
);

CREATE INDEX idx_donation_status ON food_donations (status);
CREATE INDEX idx_donation_donor  ON food_donations (donor_id);
CREATE INDEX idx_donation_ngo    ON food_donations (ngo_id);
CREATE INDEX idx_donation_city   ON food_donations (city);

-- donor_id / ngo_id are logical foreign keys to users.id in aashray_auth_db.
-- No physical FK across databases in a microservices architecture —
-- referential integrity is enforced at the application layer instead.

-- ============================================================
-- Migration for an EXISTING database (Hibernate ddl-auto=update
-- will add these automatically on next boot; run by hand only if
-- you're on ddl-auto=validate/none in production):
-- ============================================================
-- ALTER TABLE food_donations
--     ADD COLUMN latitude DECIMAL(10,7),
--     ADD COLUMN longitude DECIMAL(10,7);
--
-- Existing rows get NULL for both — the UI already handles that
-- (no "View Map" action shows up until a donation has coordinates).
