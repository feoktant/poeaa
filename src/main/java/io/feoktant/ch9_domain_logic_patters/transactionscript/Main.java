package io.feoktant.ch9_domain_logic_patters.transactionscript;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) throws SQLException {
        var url = "jdbc:h2:mem:txscript;INIT=runscript from 'classpath:init.sql'";
        var connection = DriverManager.getConnection(url);
        var gateway = new Gateway(connection);
        var recognitionService = new RecognitionService(gateway);

        recognitionService.calculateRevenueRecognitions(10);
        var revenue = recognitionService.recognizedRevenue(10, LocalDate.parse("2022-02-24"));

        System.out.println("Revenue is: " + revenue);
    }
}
