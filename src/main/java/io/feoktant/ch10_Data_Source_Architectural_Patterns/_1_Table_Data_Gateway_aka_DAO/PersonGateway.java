package io.feoktant.ch10_Data_Source_Architectural_Patterns._1_Table_Data_Gateway_aka_DAO;

import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/* TODO Ok, here should be not ResultSet, but RowSet, since it is the closest
    to C# IDataReader */
public class PersonGateway {

    private Connection DB;

    public ResultSet findAll() {
        var sql = "select * from person";
        try (var findStatement = DB.prepareStatement(sql)) {
            return findStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error during findAll", e);
        }
    }

    public ResultSet findWithLastName(String lastName) {
        var sql = "SELECT * FROM person WHERE lastname = ?";
        try (var findStatement = DB.prepareStatement(sql)) {
            findStatement.setString(1, lastName);
            return findStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error during findWithLastName", e);
        }
    }

    public ResultSet findWhere(String whereClause) {
        String sql = "select * from person where %s".formatted(whereClause);
        try (var findStatement = DB.prepareStatement(sql)) {
            return findStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error during findWhere", e);
        }
    }

    public Optional<PersonDTO> findRow(long key) {
        var sql = "SELECT * FROM person WHERE id = ?";
        try (var findStatement = DB.prepareStatement(sql)) {
            findStatement.setLong(1, key);
            var rs = findStatement.executeQuery();
            return rs.next() ? Optional.of(
                    new PersonDTO(
                            rs.getLong("ID"),
                            rs.getString("lastname"),
                            rs.getString("firstname"),
                            rs.getInt("number_of_dependents"))) :
                    Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error during findRow", e);
        }
    }

    public Long insert(String lastName, String firstName, int numberOfDependents) {
        try (var ist = DB.prepareStatement("INSERT INTO person VALUES (?,?,?,?)")) {
            Long key = getNextID();
            ist.setLong(1, key);
            ist.setString(2, lastName);
            ist.setString(3, firstName);
            ist.setLong(4, numberOfDependents);
            ist.execute();
            return key;
        } catch (SQLException e) {
            throw new RuntimeException("Error during insert", e);
        }
    }

    public void delete(long key) {
        try (var stm = DB.prepareStatement("DELETE from person WHERE id = ?")) {
            stm.setLong(1, key);
            stm.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error during delete", e);
        }
    }

    private Long getNextID() {
        throw new RuntimeException("Not yet implemented"); // TODO
    }

}
