package io.feoktant.ch9_domain_logic_patters.transactionscript;

import org.javamoney.moneta.Money;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Gateway {

    private final Connection db;

    public Gateway(Connection con) {
        this.db = con;
    }

    public ResultSet findRecognitionsFor(long contractID, LocalDate asOf) throws SQLException {
        PreparedStatement stmt = db.prepareStatement("""
                SELECT amount
                  FROM revenueRecognitions
                 WHERE contract = ? AND recognizedOn <= ?""");
        stmt.setLong(1, contractID);
        stmt.setDate(2, Date.valueOf(asOf));
        return stmt.executeQuery();
    }


    public ResultSet findContract(long contractID) throws SQLException {
        PreparedStatement stmt = db.prepareStatement("""
                SELECT *
                  FROM contracts c, products p
                 WHERE c.ID = ? AND c.product = p.ID""");
        stmt.setLong(1, contractID);
        return stmt.executeQuery();
    }

    public void insertRecognition(long contractID,
                                  Money amount,
                                  LocalDate asOf) throws SQLException {
        PreparedStatement stmt = db.prepareStatement(
                "INSERT INTO revenueRecognitions VALUES (?, ?, ?)");
        stmt.setLong(1, contractID);
        stmt.setBigDecimal(2, amount.getNumberStripped());
        stmt.setDate(3, new Date(asOf.toEpochDay()));
        stmt.executeUpdate();
    }

}
