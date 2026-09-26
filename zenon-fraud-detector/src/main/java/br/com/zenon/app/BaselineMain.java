package br.com.zenon.app;

import br.com.zenon.ingestion.TransactionIngestor;
import br.com.zenon.model.Transaction;
import br.com.zenon.repository.TransactionSQLRepository;

import java.util.List;

public class BaselineMain {
    public static void main(String[] args) {

        var repository = new TransactionSQLRepository(
                "jdbc:mysql://localhost:3306/zenon", "root", "senha123"
        );

        repository.deleteAll();

        long startRead = System.nanoTime();

        List<Transaction> transactions = new TransactionIngestor()
                .read("data/PS_20174392719_1491204439457_log.csv");
        long readMs = (System.nanoTime() - startRead) / 1_000_000;

        long startInsert = System.nanoTime();
        transactions.forEach(repository::save);
        long insertMs = (System.nanoTime() - startInsert) / 1_000_000;

        IO.println("Transações: " + transactions.size());
        IO.println("Tempo de leitura: " + readMs + "ms");
        IO.println("Tempo de inserção: " + insertMs + "ms");
        IO.println("Media por inserção: " + (double) insertMs / transactions.size());

        long startConn = System.nanoTime();
        for (int i = 0; i < 1_000; i++) {
            try (var c = java.sql.DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/zenon", "root", "senha123")) {

            } catch (java.sql.SQLException e) {
                throw new RuntimeException(e);
            }

        }

        IO.println("1000 conexões: " + (System.nanoTime() - startConn) / 1_000_000 + "ms");
    }
}
