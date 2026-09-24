package br.com.zenon;

import java.util.List;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {

    private final List<Transaction> transactions;

    public TransactionListRepository(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override

    public Optional<Transaction> findByOriginName (String nameOrig) {
        return transactions.stream()
                .filter(t -> t.origin().name().equals(nameOrig))
                .findFirst();
    }


}
