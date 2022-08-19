package io.feoktant.metadatamapping;

import io.feoktant.domain.Person;

import java.sql.Connection;
import java.util.Set;

public class PersonMapper extends Mapper<Person> {

    private Connection conn;

    protected void loadDataMap() {
        dataMap = new DataMap<>("people", Person.class);
        dataMap.addColumn("lastname", "varchar", "lastName");
        dataMap.addColumn("firstname", "varchar", "firstName");
        dataMap.addColumn("number_of_dependents", "int", "numberOfDependents");
    }

    public Set<Person> findLastNamesLike(String pattern) {
        String sql = """
                    SELECT %s
                      FROM %s
                     WHERE UPPER(lastName) like UPPER(?)"""
                .formatted(dataMap.columnList(), dataMap.getTableName());

        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pattern);
            try (var rs = stmt.executeQuery()) {
                return loadAll(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
