package br.com.zenon.analysis;

import br.com.zenon.model.Transaction;
import br.com.zenon.model.TransactionType;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    private static final int TOP_FRAUDS_LIMIT = 3;
    private static final int TOP_SUSPECTS_LIMIT = 5;

    private final List<Transaction> frauds;

    public FraudAnalyzer(List<Transaction> transactions) {
        this.frauds = transactions.stream()
                .filter(Transaction::isFraud)
                .toList();
    }

    public void analyze() {
        IO.println("Total de Fraudes: " + totalFrauds());

        IO.println("Top " + TOP_FRAUDS_LIMIT + " fraudes de maior valor:");
        topFraudsByAmount(TOP_FRAUDS_LIMIT).forEach(IO::println);

        IO.println("Top " + TOP_SUSPECTS_LIMIT + " suspeitos de fraudes de maior valor: "
                + topSuspectsByAmount(TOP_SUSPECTS_LIMIT));

        IO.println("Total de Fraudes por tipo: " + countByType());
    }

    public int totalFrauds() {
        return frauds.size();
    }

    public List<Transaction> topFraudsByAmount(int limit) {
        return frauds.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(limit)
                .toList();
    }

    public List<String> topSuspectsByAmount(int limit) {
        return frauds.stream()
                .collect(Collectors.groupingBy(
                        t -> t.origin().name(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::amount, BigDecimal::add)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }

    public Map<TransactionType, Long> countByType() {
        return frauds.stream()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));
    }
}
