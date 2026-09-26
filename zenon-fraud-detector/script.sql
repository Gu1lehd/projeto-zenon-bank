CREATE DATABASE IF NOT EXISTS zenon;
USE zenon;

CREATE TABLE IF NOT EXISTS TRANSACTIONS (
                                            id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            step              INT            NOT NULL,
                                            type              VARCHAR(10)    NOT NULL,
    amount            DECIMAL(15, 2) NOT NULL,
    name_orig         VARCHAR(20)    NOT NULL,
    old_balance_orig  DECIMAL(15, 2) NOT NULL,
    new_balance_orig  DECIMAL(15, 2) NOT NULL,
    name_dest         VARCHAR(20)    NOT NULL,
    old_balance_dest  DECIMAL(15, 2) NOT NULL,
    new_balance_dest  DECIMAL(15, 2) NOT NULL,
    is_fraud          BOOLEAN        NOT NULL,
    is_flagged_fraud  BOOLEAN        NOT NULL,

    CONSTRAINT chk_step   CHECK (step >= 1),
    CONSTRAINT chk_type   CHECK (type IN ('CASH_IN', 'CASH_OUT', 'DEBIT', 'PAYMENT', 'TRANSFER')),
    CONSTRAINT chk_amount CHECK (amount >= 0),
    CONSTRAINT chk_balances CHECK (
                                      old_balance_orig >= 0 AND new_balance_orig >= 0 AND
                                      old_balance_dest >= 0 AND new_balance_dest >= 0
                                  )
    );