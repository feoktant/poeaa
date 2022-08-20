package io.feoktant.datamapper;

import io.feoktant.domain.Person;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PersonMapper extends AbstractMapper<Person> {

    private Connection conn;

    public static final String COLUMNS = " id, lastname, firstname, number_of_dependents ";

    protected String findStatement() {
        return """
                SELECT %s
                  FROM people
                 WHERE id = ?""".formatted(COLUMNS);
    }

    @Override
    protected Person doLoad(Long id, ResultSet rs) {
        try {
            String lastNameArg = rs.getString(2);
            String firstNameArg = rs.getString(3);
            int numDependentsArg = rs.getInt(4);
            return new Person(id, lastNameArg, firstNameArg, numDependentsArg);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Person find(Long id) {
        return (Person) abstractFind(id);
    }

    public Person find(long id) {
        return find(Long.valueOf(id));
    }

    private static String findLastNameStatement = """
            SELECT %s
              FROM people
             WHERE UPPER(lastname) like UPPER(?)
             ORDER BY lastname""".formatted(COLUMNS);

    public List<Person> findByLastName(String name) {
        try (var stmt = conn.prepareStatement(findLastNameStatement)) {
            stmt.setString(1, name);
            try (var rs = stmt.executeQuery()) {
                return loadAll(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> findByLastName2(String pattern) {
        return findMany(new FindByLastName(pattern));
    }

    static class FindByLastName implements StatementSource {
        private String lastName;

        public FindByLastName(String lastName) {
            this.lastName = lastName;
        }

        public String sql() {
            return findLastNameStatement;
        }

        public Object[] parameters() {
            return new Object[]{lastName};
        }
    }

    private static final String updateStatementString =
            "UPDATE people " +
                    "  SET lastname = ?, firstname = ?, number_of_dependents = ? " +
                    "  WHERE id = ?";

    public void update(Person subject) {
        try (var updateStatement = conn.prepareStatement(updateStatementString)) {
            updateStatement.setString(1, subject.getLastName());
            updateStatement.setString(2, subject.getFirstName());
            updateStatement.setInt(3, subject.getNumberOfDependents());
            updateStatement.setInt(4, subject.getID().intValue());
            updateStatement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
