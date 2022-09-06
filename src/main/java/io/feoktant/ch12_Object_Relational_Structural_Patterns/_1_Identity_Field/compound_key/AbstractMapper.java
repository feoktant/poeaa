package io.feoktant.ch12_Object_Relational_Structural_Patterns._1_Identity_Field.compound_key;

import io.feoktant.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

abstract public class AbstractMapper {
    abstract protected String findStatementString();
    protected Map<Key, DomainObjectWithKey> loadedMap = new HashMap<>();

    public DomainObjectWithKey abstractFind(Key key) {
        var result = loadedMap.get(key);
        if (result  != null) return result;

        try (var findStatement = DB.prepare(findStatementString())) {
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

    // hook method for keys that aren't simple integral
    protected void loadFindStatement(Key key, PreparedStatement finder) throws SQLException {
        finder.setLong(1, key.longValue());
    }

    protected DomainObjectWithKey load(ResultSet rs) throws SQLException {
        Key key = createKey(rs);
        if (loadedMap.containsKey(key)) return loadedMap.get(key);

        DomainObjectWithKey result = doLoad(key, rs);
        loadedMap.put(key, result);
        return result;
    }
    abstract protected DomainObjectWithKey doLoad(Key id, ResultSet rs) throws SQLException;

    // hook method for keys that aren't simple integral
    protected Key createKey(ResultSet rs) throws SQLException {
        return new Key(rs.getLong(1));
    }
}
