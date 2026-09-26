package br.com.zenon.ingestion;

import br.com.zenon.model.Transaction;
import br.com.zenon.model.TransactionCustomer;
import br.com.zenon.model.TransactionType;

import java.math.BigDecimal;
import java.util.Optional;

public class TransactionParser {

    public Optional<Transaction> tryParse(String line) {
        try {
            return Optional.of(parse(line));
        } catch (Exception ex) {
            System.err.println("Linha inválida ignorada: " + line + " (" + ex.getMessage() + ")");
            return Optional.empty();
        }
    }

    private Transaction parse(String line) {
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