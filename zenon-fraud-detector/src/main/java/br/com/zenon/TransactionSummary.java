package br.com.zenon;

import java.math.BigDecimal;

public record TransactionSummary(long totalLines, long totalFrauds, BigDecimal totalTransactions) {

}
