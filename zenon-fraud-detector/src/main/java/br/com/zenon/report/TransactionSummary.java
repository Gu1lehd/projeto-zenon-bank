package br.com.zenon.report;

import java.math.BigDecimal;

public record TransactionSummary(long totalLines, long totalFrauds, BigDecimal totalTransactions) {

}
