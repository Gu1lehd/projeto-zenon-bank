package br.com.zenon;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    void main() {
        /*Transaction t1 = new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9839.64"),
                new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
                new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
                false,
                false

        );

        Transaction t2 = new Transaction(743, TransactionType.CASH_IN, new BigDecimal("850002.52"),
                new TransactionCustomer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0")),
                new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
                true,
                false
        );

        System.out.println(t1);
        System.out.println(t2);

        IO.println("----------------------------------------------------------------------------------");

        var transactionIngestor = new TransactionIngestor();
        List<Transaction> transactions = transactionIngestor.read("data/PS_20174392719_1491204439457_log.csv");

        transactions.stream().limit(10).forEach(System.out::println);

        IO.println("----------------------------------------------------------------------------------");

        var transactionIngestorEr = new TransactionIngestor();
        List<Transaction> transactionsEr = transactionIngestorEr.read("data/paysim_with_bad_data.csv");

        transactionsEr.stream().limit(10).forEach(System.out::println);*/

        System.out.println("----------------------------------------------------------------------------------");

        var ingestor = new TransactionIngestor();
        List<Transaction> list = ingestor.read("data/PS_20174392719_1491204439457_log.csv");

        IO.println("Linhas carregadas: " + list.size());

        FraudAnalyzer analyzer = new FraudAnalyzer(list);
        analyzer.analyze();





    }
}

