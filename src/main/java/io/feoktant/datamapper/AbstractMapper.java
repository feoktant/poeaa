package io.feoktant.datamapper;

import io.feoktant.domain.DomainObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapper<T extends DomainObject> {
    private Connection conn;

    protected Map<Long, T> loadedMap = new HashMap<>();

    abstract protected String findStatement();
    abstract protected T doLoad(Long id, ResultSet rs);

    protected T abstractFind(Long id) {
        T result = loadedMap.get(id);
        if (result != null) return result;

        try (var findStatement = conn.prepareStatement(findStatement())) {
            findStatement.setLong(1, id);
            try (var rs = findStatement.executeQuery()) {
                rs.next();
                return load(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected T load(ResultSet rs) throws SQLException {
        return loadedMap.computeIfAbsent(
                rs.getLong(1),
                id -> doLoad(id, rs));
    }

    protected List<T> loadAll(ResultSet rs) throws SQLException {
        var result = new ArrayList<T>();
        while (rs.next()) {
            result.add(load(rs));
        }
        return result;
    }

    public List<T> findMany(StatementSource source) {
        try (var stmt = conn.prepareStatement(source.sql())) {
            for (int i = 0; i < source.parameters().length; i++) {
                stmt.setObject(i + 1, source.parameters()[i]);
            }
            try (var rs = stmt.executeQuery()) {
                return loadAll(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
