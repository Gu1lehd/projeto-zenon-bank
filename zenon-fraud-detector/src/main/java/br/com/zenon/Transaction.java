package br.com.zenon;

import java.math.BigDecimal;

public record Transaction(int tep,
                          TransactionType type,
                          BigDecimal amount,
                          TransactionCustomer origin,
                          TransactionCustomer recipient,
                          boolean isFraud,
                          boolean isFlaggedFraud) {
}
