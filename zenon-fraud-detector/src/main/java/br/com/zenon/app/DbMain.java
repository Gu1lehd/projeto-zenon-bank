package br.com.zenon.app;

import br.com.zenon.model.Transaction;
import br.com.zenon.model.TransactionCustomer;
import br.com.zenon.model.TransactionType;
import br.com.zenon.repository.TransactionRepository;
import br.com.zenon.repository.TransactionSQLRepository;

import java.math.BigDecimal;

public class DbMain {
    public static void main(String[] args) {
        TransactionRepository repository = new TransactionSQLRepository(
                "jdbc:mysql://localhost:3306/zenon", "root", "senha123"
        );

        TransactionCustomer origin = new TransactionCustomer(
                "C1231006815", new BigDecimal("170136.00"), new BigDecimal("160296.36")
        );

        TransactionCustomer recipient = new TransactionCustomer(
                "M1979787155", BigDecimal.ZERO, BigDecimal.ZERO
        );

        repository.save(new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9839.64"),
                origin, recipient, false, false
        ));

        IO.println(repository.findByOriginName("C1231006815"));
        IO.println(repository.findByOriginName("C12345"));
    }
}
