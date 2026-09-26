package br.com.zenon.app;

import br.com.zenon.ingestion.EfficientTransactionIngestor;
import br.com.zenon.repository.TransactionSQLRepository;

import java.util.concurrent.atomic.AtomicLong;

public class IngestionMain {
    private static final long TOTAL_LINES_PAYSIM = 6_362_620;

    public static void main(String[] args) {
        var repository = new TransactionSQLRepository(
                "jdbc:mysql://localhost:3306/zenon", "root", "senha123"
        );

        var ingestor = new EfficientTransactionIngestor();
        var saved = new AtomicLong();

        repository.deleteAll();

        /*long start = System.nanoTime();
        ingestor.readAsStream("data/PS_20174392719_1491204439457_log.csv", transaction -> {
            repository.save(transaction);
            saved.incrementAndGet();
        });

        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        double msPerTransaction = (double) elapsedMs / saved.get();
        double estimatedMinutes = msPerTransaction * TOTAL_LINES_PAYSIM / 60_000;

        IO.println("Transações salvas: " + saved.get());
        IO.println("Tempo total (leitura + inserção): " + elapsedMs + " ms");
        IO.println("Média por transação: " + msPerTransaction + " ms");
        IO.println("Estimativa para o arquivo completo: " + Math.round(estimatedMinutes) + " minutos");*/

        long start = System.nanoTime();
        ingestor.readBatch("data/PS_20174392719_1491204439457_log.csv", batch -> {
            repository.saveAll(batch);
            long total = saved.addAndGet(batch.size());
            if (total % 500_000 == 0){
                IO.println("Salvas ate agora: " + total );
            }
        });

        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        IO.println("Transações salvas: " + saved.get());
        IO.println("Tempo total (leitura + inserção): " + elapsedMs + " ms");
        IO.println("Tempo total: " + elapsedMs / 1000 + " s");
    }
}
