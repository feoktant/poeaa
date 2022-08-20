package io.feoktant.ch9_domain_logic_patters.transactionscript;

import org.javamoney.moneta.Money;

import java.sql.SQLException;
import java.time.LocalDate;

public class RecognitionService {

    private final Gateway db;

    public RecognitionService(Gateway db) {
        this.db = db;
    }

    public Money recognizedRevenue(long contractNumber, LocalDate asOf) {
        Money result = Money.of(0, "USD");
        try (var rs = db.findRecognitionsFor(contractNumber, asOf)) {
            while (rs.next()) {
                var amount = rs.getBigDecimal("amount");
                result = result.add(Money.of(amount, "USD"));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void calculateRevenueRecognitions(long contractNumber) {
        try (var contracts = db.findContract(contractNumber)) {
            contracts.next();
            var totalRevenue = Money.of(contracts.getBigDecimal("revenue"), "USD");
            var recognitionDate = contracts.getDate("dateSigned").toLocalDate();
            var type = contracts.getString("type");
            switch (type) {
                case "S" -> {
                    var allocation = allocate(totalRevenue, 3);
                    db.insertRecognition(contractNumber, allocation[0], recognitionDate);
                    db.insertRecognition(contractNumber, allocation[1], recognitionDate.plusDays(60));
                    db.insertRecognition(contractNumber, allocation[2], recognitionDate.plusDays(90));
                }
                case "W" -> db.insertRecognition(contractNumber, totalRevenue, recognitionDate);
                case "D" -> {
                    var allocation = allocate(totalRevenue, 3);
                    db.insertRecognition(contractNumber, allocation[0], recognitionDate);
                    db.insertRecognition(contractNumber, allocation[1], recognitionDate.plusDays(30));
                    db.insertRecognition(contractNumber, allocation[2], recognitionDate.plusDays(60));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Money[] allocate(Money m, int n) {
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
