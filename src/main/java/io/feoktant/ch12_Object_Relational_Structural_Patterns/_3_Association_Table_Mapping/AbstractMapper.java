package io.feoktant.ch12_Object_Relational_Structural_Patterns._3_Association_Table_Mapping;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <E> entity
 */
public abstract class AbstractMapper<E> {

    protected Connection DB;
    protected Map<Long, E> loadedMap = new HashMap<>();

    abstract protected String findStatement();
    abstract protected E doLoad(Long id, ResultSet rs) throws SQLException;

    protected E abstractFind(Long id) {
        if (loadedMap.containsKey(id)) return loadedMap.get(id);

        try (var stmt = DB.prepareStatement(findStatement())) {
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            rs.next();
            return load(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected E load(ResultSet rs) throws SQLException {
        Long id = rs.getLong(1);
        return load(id, rs);
    }

    public E load(Long id, ResultSet rs) throws SQLException {
        if (loadedMap.containsKey(id)) return loadedMap.get(id);

        var result = doLoad(id, rs);
        loadedMap.put(id, result);
        return result;
    }

    protected E loadRow(Long id, ResultSet rs) throws SQLException {
        return load(id, rs);
    }

    protected List<E> findAll(String sql) {
        try (var stmt = DB.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            var result = new ArrayList<E>();
            while (rs.next()) result.add(load(rs));
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
