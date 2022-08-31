package io.feoktant.ch12_Object_Relational_Structural_Patterns._1_Identity_Field.compound_key;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

abstract public class AbstractMapper {
    abstract protected String findStatementString();
    protected Map<Key, DomainObjectWithKey> loadedMap = new HashMap<>();

    private Connection DB;

    public DomainObjectWithKey abstractFind(Key key) {
        var result = loadedMap.get(key);
        if (result  != null) return result;

        try (var findStatement = DB.prepareStatement(findStatementString())) {
            loadFindStatement(key, findStatement);
            try (var rs = findStatement.executeQuery()) {
                rs.next();
                if (rs.isAfterLast()) return null;
                result = load(rs);
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    abstract DomainObjectWithKey load(ResultSet rs);

    // hook method for keys that aren't simple integral
    protected void loadFindStatement(Key key, PreparedStatement finder) throws SQLException {
        finder.setLong(1, key.longValue());
    }
}
