package io.feoktant.ch10_Data_Source_Architectural_Patterns._2_Row_Data_Gateway;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonFinder {

    private Connection DB;

    private final static String findStatementString = """
            SELECT id, lastname, firstname, number_of_dependents
              FROM people
             WHERE id = ?""";

    public PersonGateway find(Long id) {
        PersonGateway result = (PersonGateway) Registry.getPerson(id);
        if (result != null) return result;
        try (var findStatement = DB.prepareStatement(findStatementString)) {
            findStatement.setLong(1, id);
            try (var rs = findStatement.executeQuery()) {
                rs.next();
                result = PersonGateway.load(rs);
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PersonGateway find(long id) {
        return find(Long.valueOf(id));
    }

    private static final String findResponsibleStatement = """
            SELECT id, lastname, firstname, number_of_dependents
              FROM people
             WHERE number_of_dependents > 0""";

    public List<PersonGateway> findResponsibles() {
        List<PersonGateway> result = new ArrayList<>();
        try (var stmt = DB.prepareStatement(findResponsibleStatement);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.add(PersonGateway.load(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
