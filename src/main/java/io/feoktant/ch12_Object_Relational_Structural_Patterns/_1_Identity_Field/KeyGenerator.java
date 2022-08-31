package io.feoktant.ch12_Object_Relational_Structural_Patterns._1_Identity_Field;

import java.sql.Connection;
import java.sql.SQLException;

public class KeyGenerator {
    private final Connection conn;
    private final String keyName;
    private long nextId = 0;
    private long maxId = 0;
    private final int incrementBy;

    public KeyGenerator(Connection conn, String keyName, int incrementBy) {
        this.conn = conn;
        this.keyName = keyName;
        this.incrementBy = incrementBy;
        try {
            conn.setAutoCommit(false);
        } catch (SQLException exc) {
            throw new RuntimeException("Unable to turn off autocommit", exc);
        }
    }

    public synchronized Long nextKey() {
        if (nextId == maxId) {
            reserveIds();
        }
        return nextId++;
    }

    private void reserveIds() {
        long newNextId;
        try (var stmt = conn.prepareStatement(
                "SELECT nextID FROM keys WHERE name = ? FOR UPDATE")) {
            stmt.setString(1, keyName);
            try (var rs = stmt.executeQuery()) {
                rs.next();
                newNextId = rs.getLong(1);
            }
        } catch (SQLException exc) {
            throw new RuntimeException("Unable to generate ids", exc);
        }

        long newMaxId = newNextId + incrementBy;
        try (var stmt = conn.prepareStatement(
                "UPDATE keys SET nextID = ? WHERE name = ?")) {
            stmt.setLong(1, newMaxId);
            stmt.setString(2, keyName);
            stmt.executeUpdate();
            conn.commit();
            nextId = newNextId;
            maxId = newMaxId;
        } catch (SQLException exc) {
            throw new RuntimeException("Unable to generate ids", exc);
        }
    }

}
