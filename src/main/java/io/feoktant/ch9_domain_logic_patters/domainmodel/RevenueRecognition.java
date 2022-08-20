package io.feoktant.ch9_domain_logic_patters.domainmodel;

import org.javamoney.moneta.Money;

import java.time.LocalDate;

public class RevenueRecognition {
    private Money amount;
    private LocalDate date;

    public RevenueRecognition(Money amount, LocalDate date) {
        this.amount = amount;
        this.date = date;
    }

    public Money getAmount() {
        return amount;
    }

    boolean isRecognizableBy(LocalDate asOf) {
        return asOf.isAfter(date) || asOf.equals(date);
    }
}
