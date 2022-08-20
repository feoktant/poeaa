package io.feoktant.ch9_domain_logic_patters.domainmodel;

import static io.feoktant.ch9_domain_logic_patters.domainmodel.RecognitionStrategy.threeWayRecognitionStrategy;

public class Product {

    private final String name;
    private final RecognitionStrategy recognitionStrategy;

    public Product(String name, RecognitionStrategy rs) {
        this.name = name;
        this.recognitionStrategy = rs;
    }

    public static Product newWordProcessor(String name) {
        return new Product(name, contract -> {
            RevenueRecognition rr = new RevenueRecognition(
                    contract.getRevenue(),
                    contract.getWhenSigned());
            contract.addRevenueRecognition(rr);
        });
    }

    public static Product newSpreadsheet(String name) {
        return new Product(name, threeWayRecognitionStrategy(60, 90));
    }

    public static Product newDatabase(String name) {
        return new Product(name, threeWayRecognitionStrategy(30, 60));
    }

    public void calculateRevenueRecognitions(Contract contract) {
        recognitionStrategy.calculateRevenueRecognitions(contract);
    }

}
