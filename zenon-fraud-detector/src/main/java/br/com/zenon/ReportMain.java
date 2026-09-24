package br.com.zenon;

import java.nio.file.Path;

public class ReportMain {
 void main() {

     long startTime = System.nanoTime();

     TransactionSummary tSummary = new TransactionReport().generate(Path.of("data/PS_20174392719_1491204439457_log.csv"));

     long elapsedMs = (System.nanoTime() - startTime) / 1_000_000;

     IO.println("Total de linhas: " + tSummary.totalLines());
     IO.println("Total de frauds: " + tSummary.totalFrauds());
     IO.println("Total de transactions: " + tSummary.totalTransactions());
     IO.println("Tempo: " + elapsedMs + " ms");

 }
}
