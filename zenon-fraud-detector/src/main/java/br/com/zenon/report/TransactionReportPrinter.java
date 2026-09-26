package br.com.zenon.report;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class TransactionReportPrinter {

    private final Locale locale;
    private final ResourceBundle messages;
    private final NumberFormat currency;
    private final NumberFormat integer;

    public TransactionReportPrinter(Locale locale) {
        this.locale = locale;
        this.messages = ResourceBundle.getBundle("ReportMainBundle", locale);
        this.currency = NumberFormat.getCurrencyInstance(locale);
        this.integer = NumberFormat.getIntegerInstance(locale);
    }

    public void print(TransactionSummary summary, long elapsedMs){
        IO.println(messages.getString("report.title"));
        IO.println(text("total.lines", integer.format(summary.totalLines())));
        IO.println(text("total.frauds", integer.format(summary.totalFrauds())));
        IO.println(text("total.value", currency.format(summary.totalTransactions())));
        IO.println(text("elapsed.time", integer.format(elapsedMs)));
    }

    private String text(String key, String value) {
        return MessageFormat.format(messages.getString(key), value);
    }

}
