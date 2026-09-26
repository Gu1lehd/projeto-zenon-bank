package br.com.zenon.model;

import java.math.BigDecimal;


public record TransactionCustomer (String name,
                                   BigDecimal oldBalance,
                                   BigDecimal newBalance ) {

    public TransactionCustomer{

        if (oldBalance.signum() < 0) throw new IllegalArgumentException("Old Balance não pode ser negativo");

        if (newBalance.signum() < 0) throw new IllegalArgumentException("New Balance não pode ser negativo");

        if (name.trim().isEmpty()) throw new IllegalArgumentException("O nome não pode ser vazio");
    }


}
