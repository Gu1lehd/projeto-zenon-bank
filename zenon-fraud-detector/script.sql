CREATE DATABASE IF NOT EXISTS zenon
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE zenon;

CREATE TABLE IF NOT EXISTS TRANSACTIONS (
    id                BIGINT         NOT NULL AUTO_INCREMENT,
    step              INT            NOT NULL,
    type              VARCHAR(10)    NOT NULL,
    amount            DECIMAL(15, 2) NOT NULL,
    name_orig         VARCHAR(20)    NOT NULL,
    old_balance_orig  DECIMAL(15, 2) NOT NULL,
    new_balance_orig  DECIMAL(15, 2) NOT NULL,
    name_dest         VARCHAR(20)    NOT NULL,
    old_balance_dest  DECIMAL(15, 2) NOT NULL,
    new_balance_dest  DECIMAL(15, 2) NOT NULL,
    is_fraud          BOOLEAN        NOT NULL DEFAULT FALSE,
    is_flagged_fraud  BOOLEAN        NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_transactions PRIMARY KEY (id),

    CONSTRAINT chk_step             CHECK (step >= 1),
    CONSTRAINT chk_type             CHECK (type IN ('CASH_IN', 'CASH_OUT', 'DEBIT', 'PAYMENT', 'TRANSFER')),
    CONSTRAINT chk_amount           CHECK (amount >= 0),
    CONSTRAINT chk_old_balance_orig CHECK (old_balance_orig >= 0),
    CONSTRAINT chk_new_balance_orig CHECK (new_balance_orig >= 0),
    CONSTRAINT chk_old_balance_dest CHECK (old_balance_dest >= 0),
    CONSTRAINT chk_new_balance_dest CHECK (new_balance_dest >= 0),

    INDEX idx_transactions_name_orig (name_orig)
) ENGINE = InnoDB;
