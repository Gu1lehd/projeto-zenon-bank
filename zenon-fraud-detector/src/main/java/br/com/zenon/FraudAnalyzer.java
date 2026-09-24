package br.com.zenon;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    private final List<Transaction> transactions;

    public FraudAnalyzer(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void analyze() {
        List<Transaction> frauds = findFrauds();

        printTop3ByAmount(frauds);

        printTop5Suspects(frauds);

        printFraudsByType(frauds);
    }

    private List<Transaction> findFrauds() {
        List<Transaction> frauds = transactions.stream()
                .filter(Transaction::isFraud)
                .toList();

        IO.println("Total de Fraudes: " + frauds.size());
        return frauds;
    }

    private void printTop3ByAmount(List<Transaction> frauds) {
        List<Transaction> top3 = frauds.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(3)
                .toList();

        IO.println("Top 3 fraudes de maior valor:");
        top3.forEach(System.out::println);
    }

    private void printTop5Suspects(List<Transaction> frauds) {
        List<String> top5Suspects = frauds.stream()
                .collect(Collectors.groupingBy(
                        t -> t.origin().name(),
                        Collectors.mapping(
                                Transaction::amount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();

        IO.println("Top 5 suspeitos de fraudes de maior valor: " + top5Suspects);
    }

    private void printFraudsByType(List<Transaction> frauds) {
        Map<TransactionType, Long> byType = frauds.stream()
                .collect(Collectors.groupingBy(
                        Transaction::type,
                        Collectors.counting()
                ));

        IO.println("Total de Fraudes por tipo: " + byType);
    }
}
