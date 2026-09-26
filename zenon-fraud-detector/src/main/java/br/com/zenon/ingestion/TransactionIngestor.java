package br.com.zenon.ingestion;


import br.com.zenon.model.Transaction;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;


public class TransactionIngestor {

    private final TransactionParser parser = new TransactionParser();

    public List<Transaction> read(String filename) {
        Path path = Path.of(filename);

        try {
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
                    .limit(10_000)
                    .map(parser::tryParse)
                    .flatMap(Optional::stream)
                    .toList();
        } catch (Exception ex) {
            throw new RuntimeException("Error ao ler o arquivo: " + filename, ex);
        }
    }


}
