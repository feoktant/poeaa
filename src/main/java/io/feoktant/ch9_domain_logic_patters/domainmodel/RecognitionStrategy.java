package io.feoktant.ch9_domain_logic_patters.domainmodel;

import org.javamoney.moneta.Money;

@FunctionalInterface
public interface RecognitionStrategy {

    void calculateRevenueRecognitions(Contract contract);

    public static RecognitionStrategy threeWayRecognitionStrategy(
            int firstRecognitionOffset,
            int secondRecognitionOffset
    ) {
        return contract -> {
            Money[] allocation = allocate(contract.getRevenue(), 3);
            contract.addRevenueRecognition(new RevenueRecognition(
                    allocation[0],
                    contract.getWhenSigned()));
            contract.addRevenueRecognition(new RevenueRecognition(
                    allocation[1],
                    contract.getWhenSigned().plusDays(firstRecognitionOffset)));
            contract.addRevenueRecognition(new RevenueRecognition(
                    allocation[2],
                    contract.getWhenSigned().plusDays(secondRecognitionOffset)));
        };
    }

    private static Money[] allocate(Money m, int n) {
        var amount = m.getNumber().longValue();
        Money lowResult = Money.of(amount / n, m.getCurrency());
        Money highResult = Money.of(lowResult.getNumber().longValue() + 1, m.getCurrency());
        Money[] results = new Money[n];
        int remainder = (int) amount % n;
        for (int i = 0; i < remainder; i++) results[i] = highResult;
        for (int i = remainder; i < n; i++) results[i] = lowResult;
        return results;
    }
}
