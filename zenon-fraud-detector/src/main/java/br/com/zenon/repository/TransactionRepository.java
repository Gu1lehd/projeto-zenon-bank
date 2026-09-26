package br.com.zenon.repository;

import br.com.zenon.model.Transaction;

import java.util.List;
import java.util.Optional;


public interface TransactionRepository {

    Optional<Transaction> findByOriginName(String nameOrig);

    void save (Transaction transaction);

    default void saveAll(List<Transaction> transactions) {
        transactions.forEach(this::save);
    }
}