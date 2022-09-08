package io.feoktant.ch13_Object_Relational_Metadata_Mapping_Patterns._1_metadata_mapping;

import io.feoktant.ch18_base_patterns.Layer_Supertype.DomainObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Mapper<T extends DomainObject> {

    private UnitOfWork uow;
    protected DataMap<T> dataMap;

    private Connection conn;

    public T findObject(Long key) {
        if (uow.isLoaded(key)) return (T) uow.getObject(key);

        String sql = """
                SELECT %s
                  FROM %s
                 WHERE ID = ?"""
                .formatted(dataMap.columnList(), dataMap.getTableName());
        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, key);
            try (var rs = stmt.executeQuery()) {
                rs.next();
                return load(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T load(ResultSet rs) throws InstantiationException,
            IllegalAccessException, SQLException {
        Long key = rs.getLong("ID");
        if (uow.isLoaded(key)) return (T) uow.getObject(key);

        T result = dataMap.getDomainClass().newInstance();
        result.setID(key);
        uow.registerClean(result);
        loadFields(rs, result);
        return result;
    }

    private void loadFields(final ResultSet rs, T result) throws SQLException {
        for (var columnMap : dataMap.getColumns()) {
            Object columnValue = rs.getObject(columnMap.getColumnName());
            columnMap.setField(result, columnValue);
        }
    }

    public void update(T obj) {
        String sql = """
                UPDATE %s %s
                 WHERE ID = ?"""
                .formatted(dataMap.getTableName(), dataMap.updateList());
        try (var stmt = conn.prepareStatement(sql)) {
            int argCount = 1;
            for (var col : dataMap.getColumns()) {
                stmt.setObject(argCount++, col.getValue(obj));
            }
            stmt.setLong(argCount, obj.getID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long insert(T obj) {
        String sql = """
                INSERT INTO %s
                VALUES (?%s)"""
                .formatted(dataMap.getTableName(), dataMap.insertList());
        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, obj.getID());
            int argCount = 2;
            for (var col: dataMap.getColumns()) {
                stmt.setObject(argCount++, col.getValue(obj));
            }
            stmt.executeUpdate();
            return obj.getID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<T> findObjectsWhere(String whereClause) {
        String sql = """
                SELECT %s
                  FROM %s
                 WHERE %s"""
                .formatted(dataMap.columnList(), dataMap.getTableName(), whereClause);
        try (var stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            return loadAll(rs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<T> loadAll(ResultSet rs) throws SQLException, InstantiationException,
            IllegalAccessException {
        Set<T> result = new HashSet<>();
        while (rs.next()) {
            T newObj = dataMap.getDomainClass().newInstance();
            newObj = load(rs);
            result.add(newObj);
        }
        return result;
    }

}

class UnitOfWork {
    boolean isLoaded(Long key) {
        return false;
    }

    DomainObject getObject(Long key) {
        return null;
    }

    void registerClean(DomainObject _do) {
    }
}
