package br.com.zenon.ingestion;

import br.com.zenon.model.Transaction;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EfficientTransactionIngestor {

    private static final long LIMIT = 10_000;
    private static final int BATCH_SIZE = 10_000;

    private final TransactionParser parser = new TransactionParser();

    public void readAsStream(String filename, Consumer<Transaction> consumer) {
        try (Stream<String> lines = Files.lines(Path.of(filename))) {
            lines.skip(1)
                    .limit(LIMIT)
                    .map(parser::tryParse)
                    .flatMap(Optional::stream)
                    .forEach(consumer);

        }catch (IOException e){
            throw new UncheckedIOException("Erro ao ler o arquivo: "+ filename, e);
        }

    }

    public void readBatch(String filename, Consumer< List<Transaction>> consumer){
        try (Stream<String> lines = Files.lines(Path.of(filename))){
            Iterator<Transaction> transactions = lines.skip(1)
                    .map(parser::tryParse)
                    .flatMap(Optional::stream)
                    .iterator();

            List<Transaction> batch = new ArrayList<>(BATCH_SIZE);

            while (transactions.hasNext()) {
                batch.add(transactions.next());

                if (batch.size() == BATCH_SIZE) {
                    consumer.accept(batch);
                    batch =  new ArrayList<>(BATCH_SIZE);
                }
            }

            if (!batch.isEmpty()) {
                consumer.accept(batch);
            }
        }catch (IOException e){
            throw new UncheckedIOException("Erro ao ler o arquivo: "+ filename, e);
        }
    }
}
