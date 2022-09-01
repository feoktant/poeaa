package io.feoktant.ch10_Data_Source_Architectural_Patterns._4_Data_mapper;

import io.feoktant.domain.Person;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PersonMapper extends AbstractMapper<Person> {
    public static final String COLUMNS = " id, lastname, firstname, number_of_dependents ";
    private static final String updateStatementString = """
            UPDATE people
               SET lastname = ?, firstname = ?, number_of_dependents = ?
             WHERE id = ?""";
    private static String findLastNameStatement = """
            SELECT %s
              FROM people
             WHERE UPPER(lastname) like UPPER(?)
             ORDER BY lastname""".formatted(COLUMNS);

    protected String findStatement() {
        return """
                SELECT %s
                  FROM people
                 WHERE id = ?""".formatted(COLUMNS);
    }

    public Person find(Long id) {
        return abstractFind(id);
    }

    public Person find(long id) {
        return find(Long.valueOf(id));
    }

    @Override
    protected Person doLoad(Long id, ResultSet rs) throws SQLException {
        String lastNameArg = rs.getString(2);
        String firstNameArg = rs.getString(3);
        int numDependentsArg = rs.getInt(4);
        return new Person(id, lastNameArg, firstNameArg, numDependentsArg);
    }

    public List<Person> findByLastName(String name) {
        try (var stmt = DB.prepareStatement(findLastNameStatement)) {
            stmt.setString(1, name);
            var rs = stmt.executeQuery();
            return loadAll(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> findByLastName2(String pattern) {
        return findMany(new FindByLastName(pattern));
    }

    public void update(Person subject) {
        try (var updateStatement = DB.prepareStatement(updateStatementString)) {
            updateStatement.setString(1, subject.getLastName());
            updateStatement.setString(2, subject.getFirstName());
            updateStatement.setInt(3, subject.getNumberOfDependents());
            updateStatement.setInt(4, subject.getID().intValue());
            updateStatement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String insertStatement() {
        return "INSERT INTO people VALUES (?, ?, ?, ?)";
    }

    protected void doInsert(Person subject, PreparedStatement stmt) throws SQLException {
        stmt.setString(2, subject.getLastName());
        stmt.setString(3, subject.getFirstName());
        stmt.setInt(4, subject.getNumberOfDependents());
    }

    static class FindByLastName implements StatementSource {
        private final String lastName;

        public FindByLastName(String lastName) {
            this.lastName = lastName;
        }

        public String sql() {
            return """
                    SELECT %s
                      FROM people
                     WHERE UPPER(lastname) like UPPER(?)
                     ORDER BY lastname""".formatted(COLUMNS);
        }

        public Object[] parameters() {
            return new Object[]{lastName};
        }
    }

}
