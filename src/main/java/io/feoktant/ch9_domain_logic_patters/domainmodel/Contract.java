package io.feoktant.ch9_domain_logic_patters.domainmodel;

import org.javamoney.moneta.Money;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Contract {
    private final List<RevenueRecognition> revenueRecognitions = new ArrayList<>();
    private final Product product;
    private final Money revenue;
    private final LocalDate whenSigned;
    private Long id;
    private final static Money ZERO_USD = Money.of(0, "USD");

    public Contract(Product product, Money revenue, LocalDate whenSigned) {
        this.product = product;
        this.revenue = revenue;
        this.whenSigned = whenSigned;
    }

    public Money recognizedRevenue(LocalDate asOf) {

        return revenueRecognitions.stream()
                .filter(r -> r.isRecognizableBy(asOf))
                .map(RevenueRecognition::getAmount)
                .reduce(ZERO_USD, Money::add);
    }

    public Money getRevenue() {
        return revenue;
    }

    public LocalDate getWhenSigned() {
        return whenSigned;
    }

    public void addRevenueRecognition(RevenueRecognition rr) {
        revenueRecognitions.add(rr);
    }

    public void calculateRecognitions() {
        product.calculateRevenueRecognitions(this);
    }
}
