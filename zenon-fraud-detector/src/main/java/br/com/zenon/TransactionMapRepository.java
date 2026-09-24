package br.com.zenon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionMapRepository implements TransactionRepository {

    private final Map<String, Transaction> byOriginName;

    public TransactionMapRepository(List<Transaction> transactions) {
        this.byOriginName = new HashMap<>();
        for (Transaction t : transactions) {

            byOriginName.put(t.origin().name(), t);
        }
    }

    @Override
    public Optional<Transaction> findByOriginName(String nameOrig) {
        return Optional.ofNullable(byOriginName.get(nameOrig));
    }
}