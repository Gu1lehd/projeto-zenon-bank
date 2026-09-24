package br.com.zenon.ingestion;


import br.com.zenon.model.Transaction;
import br.com.zenon.model.TransactionCustomer;
import br.com.zenon.model.TransactionType;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class TransactionIngestor {

    public List<Transaction> read(String filename) {
        Path path = Path.of(filename);

        try {
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
                    //.limit(100000)
                    .map(this::tryParseTransaction)
                    .flatMap(t -> t == null ? Stream.empty() : Stream.of(t))
                    .toList();

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler o arquivo: " + filename, ex);
        }
    }

    private Transaction tryParseTransaction(String line) {
        try {
            return parseTransaction(line);
        } catch (Exception ex) {
            System.err.println("Linha inválida ignorada: " + line + " (" + ex.getMessage() + ")");
            return null;
        }
    }

    private Transaction parseTransaction(String line) {
        String[] chunks = line.split(",", -1);

        int step = Integer.parseInt(chunks[0]);
        TransactionType type = TransactionType.valueOf(chunks[1]);
        BigDecimal amount = new BigDecimal(chunks[2]);

        var origin = new TransactionCustomer(chunks[3], new BigDecimal(chunks[4]), new BigDecimal(chunks[5]));
        var recipient = new TransactionCustomer(chunks[6], new BigDecimal(chunks[7]), new BigDecimal(chunks[8]));

        boolean isFraud = "1".equals(chunks[9]);
        boolean isFlaggedFraud = "1".equals(chunks[10]);

        return new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud);
    }
}
