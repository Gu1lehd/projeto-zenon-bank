package br.com.zenon;

import java.math.BigDecimal;
import java.util.Objects;

public record TransactionCustomer (String name,
                                   BigDecimal oldBalance,
                                   BigDecimal newBalance ) {

    public TransactionCustomer{
        Objects.requireNonNull(name, "Name não pode ser nulo");
        Objects.requireNonNull(oldBalance, "Old Balance não pode ser nulo");
        Objects.requireNonNull(newBalance, "New Balance não pode ser nulo");



        if (oldBalance.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Old Balance não pode ser negativo");
        }

        if (newBalance.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("New Balance não pode ser negativo");
        }
    }


}
