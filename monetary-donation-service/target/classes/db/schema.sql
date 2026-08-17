-- ============================================================
-- Aashray :: Monetary Donation Service :: Reference schema
-- Hibernate (ddl-auto=update) creates this automatically on boot.
-- ============================================================

CREATE DATABASE IF NOT EXISTS aashray_monetary_db;
USE aashray_monetary_db;

CREATE TABLE IF NOT EXISTS monetary_donations (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount              DECIMAL(12,2) NOT NULL,
    currency            VARCHAR(10)   NOT NULL DEFAULT 'INR',
    donation_type       VARCHAR(20)   NOT NULL DEFAULT 'ONE_TIME',
    donation_date       DATETIME      NOT NULL,
    payment_status      VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    reference_number    VARCHAR(64)   NOT NULL UNIQUE,
    payment_method      VARCHAR(30),
    purpose_note        VARCHAR(255),

    razorpay_order_id   VARCHAR(64)   UNIQUE,
    razorpay_payment_id VARCHAR(64),
    razorpay_signature  VARCHAR(255),

    donor_id            BIGINT        NOT NULL,
    donor_name          VARCHAR(100)  NOT NULL,
    donor_email         VARCHAR(150)  NOT NULL,

    created_at          DATETIME,
    updated_at          DATETIME,

    CONSTRAINT chk_payment_status CHECK (
        payment_status IN ('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED')
    ),
    CONSTRAINT chk_donation_type CHECK (
        donation_type IN ('ONE_TIME', 'RECURRING')
    )
);

CREATE INDEX idx_monetary_status        ON monetary_donations (payment_status);
CREATE INDEX idx_monetary_donor         ON monetary_donations (donor_id);
CREATE INDEX idx_monetary_date          ON monetary_donations (donation_date);
CREATE INDEX idx_monetary_razorpay_order ON monetary_donations (razorpay_order_id);

-- donor_id is a logical foreign key to users.id in aashray_auth_db.
-- No physical FK across databases in a microservices architecture —
-- referential integrity is enforced at the application layer instead.

-- ============================================================
-- Migration for an EXISTING database (Hibernate ddl-auto=update
-- will add the new columns automatically on next boot, but if you
-- run with ddl-auto=validate/none in production, or just want to
-- apply it by hand first, run this):
-- ============================================================
-- ALTER TABLE monetary_donations
--     ADD COLUMN currency VARCHAR(10) NOT NULL DEFAULT 'INR',
--     ADD COLUMN donation_type VARCHAR(20) NOT NULL DEFAULT 'ONE_TIME',
--     ADD COLUMN razorpay_order_id VARCHAR(64) UNIQUE,
--     ADD COLUMN razorpay_payment_id VARCHAR(64),
--     ADD COLUMN razorpay_signature VARCHAR(255);
--
-- ALTER TABLE monetary_donations
--     DROP CHECK chk_payment_status,
--     ADD CONSTRAINT chk_payment_status CHECK (payment_status IN ('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED'));
--
-- CREATE INDEX idx_monetary_razorpay_order ON monetary_donations (razorpay_order_id);
--
-- Existing rows will have razorpay_order_id = NULL, which is fine — the
-- UNIQUE constraint allows multiple NULLs in MySQL/InnoDB. Old PENDING
-- rows created under the previous simulated-confirm flow will simply
-- never be reachable via the new verify-payment endpoint (there's no
-- Razorpay order behind them); they're safe to leave as historical
-- records or clean up manually.
