package br.com.zenon.app;


import br.com.zenon.report.TransactionReport;
import br.com.zenon.report.TransactionReportPrinter;
import br.com.zenon.report.TransactionSummary;

import java.nio.file.Path;
import java.util.Locale;

public class ReportMain {
    public static void main(String[] args) {

        Locale locale = parseLocale(args);

        long start = System.nanoTime();
        TransactionSummary summary = new TransactionReport()
                .generate(Path.of("data/PS_20174392719_1491204439457_log.csv"));

        long elapsed = (System.nanoTime() - start) / 1_000_000;

        new TransactionReportPrinter(locale).print(summary, elapsed);
 }

 private static Locale parseLocale(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--lang=")){
                String lang = arg.substring("--lang=".length());
                if (lang.equalsIgnoreCase("en")){
                    return Locale.US;
                }
                if (lang.equalsIgnoreCase("pt")){
                    return Locale.forLanguageTag("pt-BR");
                }

                IO.println("Unsupported language / Idioma não suportado: " + lang);

            }
        }

        return Locale.forLanguageTag("pt-BR");
 }
}
