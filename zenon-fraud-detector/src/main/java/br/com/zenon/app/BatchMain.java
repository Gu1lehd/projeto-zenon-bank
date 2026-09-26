package br.com.zenon.app;

import br.com.zenon.ingestion.TransactionIngestor;
import br.com.zenon.model.Transaction;
import br.com.zenon.repository.TransactionSQLRepository;

import java.util.List;
import java.util.function.Consumer;

public class BatchMain {

    private static final String URL = "jdbc:mysql://localhost:3306/zenon";
    private static final String URL_REWRITE = URL + "?rewriteBatchedStatements=true";


    public static void main(final String[] args) {
        List<Transaction>  transactions = new TransactionIngestor()
                .read("data/PS_20174392719_1491204439457_log.csv");
    IO.println("Transações lidas: " + transactions.size());

    var plain = new TransactionSQLRepository(URL, "root", "senha123");
    var rewrite = new TransactionSQLRepository(URL_REWRITE, "root", "senha123");

        measure("A - save um por um", plain, list -> list.forEach(plain::save), transactions);
        measure("B - batch", plain, plain::saveAll, transactions);
        measure("C - batch + rewrite", rewrite, rewrite::saveAll, transactions);
    }

    public static void measure(String label,
                               TransactionSQLRepository repository,
                               Consumer<List<Transaction>> action,
                               List<Transaction> transactions){

        repository.deleteAll();

        long start = System.nanoTime();
        action.accept(transactions);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        IO.println(label + ": " + elapsedMs + " ms");


    }
}
