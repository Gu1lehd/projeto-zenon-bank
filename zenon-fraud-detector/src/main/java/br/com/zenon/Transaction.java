package br.com.zenon;

import java.math.BigDecimal;
import java.util.Objects;

public record Transaction(int step,
                          TransactionType type,
                          BigDecimal amount,
                          TransactionCustomer origin,
                          TransactionCustomer recipient,
                          boolean isFraud,
                          boolean isFlaggedFraud) {


    public Transaction {
        Objects.requireNonNull(type, "Type pode ser nulo");
        Objects.requireNonNull(amount, "Amount pode ser nulo");
        Objects.requireNonNull(origin,"Name Origin não pode ser nulo");
        Objects.requireNonNull(recipient,"Old Balance pode ser nulo");

        if (step < 1){
            throw new IllegalArgumentException("Step deve ser >= 1");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Amount não pode ser negativo");
        }


    }
}
