package io.feoktant.ch10_Data_Source_Architectural_Patterns._4_Data_mapper;

import io.feoktant.ch18_base_patterns.Layer_Supertype.DomainObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <E> Entity type
 */
public abstract class AbstractMapper<E extends DomainObject> {
    protected Map<Long, E> loadedMap = new HashMap<>();

    protected Connection DB;

    abstract protected String findStatement();

    abstract protected E doLoad(Long id, ResultSet rs) throws SQLException;

    protected E abstractFind(Long id) {
        var result = loadedMap.get(id);
        if (result != null) return result;

        try (var findStatement = DB.prepareStatement(findStatement())) {
            findStatement.setLong(1, id);
            ResultSet rs = findStatement.executeQuery();
            rs.next();
            return load(rs); // TODO this will fail on non-existing?
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected E load(ResultSet rs) throws SQLException {
        Long id = rs.getLong(1);
        if (loadedMap.containsKey(id)) return loadedMap.get(id);

        var result = doLoad(id, rs);
        loadedMap.put(id, result);
        return result;
    }

    protected List<E> loadAll(ResultSet rs) throws SQLException {
        List<E> result = new ArrayList<>();
        while (rs.next()) result.add(load(rs));
        return result;
    }

    public List<E> findMany(StatementSource source) {
        try (var stmt = DB.prepareStatement(source.sql())) {
            for (int i = 0; i < source.parameters().length; i++)
                stmt.setObject(i + 1, source.parameters()[i]);
            var rs = stmt.executeQuery();
            return loadAll(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long insert(E subject) {
        try (var insertStatement = DB.prepareStatement(insertStatement())) {
            subject.setID(findNextDatabaseId());
            insertStatement.setInt(1, subject.getID().intValue());
            doInsert(subject, insertStatement);
            insertStatement.execute();
            loadedMap.put(subject.getID(), subject);
            return subject.getID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    abstract protected String insertStatement();

    abstract protected void doInsert(E subject, PreparedStatement insertStatement)
            throws SQLException;

    Long findNextDatabaseId() {
        throw new RuntimeException("Not yet implemented"); // TODO
    }

}
