package br.com.zenon;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TransactionReport {

    public TransactionSummary generate (Path file){
        long totalLines = 0;
        long totalFrauds = 0;
        BigDecimal totalTransactions = BigDecimal.ZERO;

        try (Stream<String> lines = Files.lines(file)) {
            var interator = lines.skip(1).iterator();

            while (interator.hasNext()) {
                String[] c = interator.next().split(",");

                totalLines++;

                if (c[9].equals("1")) {
                    totalFrauds++;
                }

                BigDecimal amount = new BigDecimal(c[2].trim());
                totalTransactions = totalTransactions.add(amount);

            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return new TransactionSummary(totalLines, totalFrauds, totalTransactions);

    }


}
