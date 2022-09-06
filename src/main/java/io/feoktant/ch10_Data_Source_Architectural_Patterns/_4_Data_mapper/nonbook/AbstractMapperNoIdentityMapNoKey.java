package io.feoktant.ch10_Data_Source_Architectural_Patterns._4_Data_mapper.nonbook;

import io.feoktant.ch10_Data_Source_Architectural_Patterns._4_Data_mapper.StatementSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @param <E> Entity type
 * @param <K> Key type
 */
public abstract class AbstractMapperNoIdentityMapNoKey<E extends DomainObjectWithK<K>, K> {

    protected Connection DB;

    abstract protected String findStatement();

    abstract protected String insertStatement();

    abstract protected E doLoad(K id, ResultSet rs) throws SQLException;

    private Class<K> keyClass;

    abstract protected void doInsert(E subject, PreparedStatement insertStatement)
            throws SQLException;

    /**
     * Fowler says, add abstractFind. We can omit this, and use normal find,
     * thanks for generics, was:
     * <code>protected DomainObject abstractFind(Long id)</code>
     */
    public Optional<E> find(K id) {
        try (var findStatement = DB.prepareStatement(findStatement())) {
            setID(findStatement, id);
            var rs = findStatement.executeQuery();
            var maybeEntity = rs.next() ?
                    Optional.of(load(rs)) :
                    Optional.<E>empty();
            if (rs.next()) throw new SQLException("Get by id returned >1 entity");
            else return maybeEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected E load(ResultSet rs) throws SQLException {
        K id = rs.getObject(1, keyClass);
        return doLoad(id, rs);
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

    public K insert(E subject) {
        try (var insertStatement = DB.prepareStatement(insertStatement())) {
            var nextId = findNextDatabaseId();
            subject.setID(nextId);
            setID(insertStatement, nextId);
            doInsert(subject, insertStatement);
            insertStatement.execute();
            return nextId;
        } catch (SQLException e) {
            subject.setID(null); // unset id
            throw new RuntimeException(e);
        }
    }

    private void setID(PreparedStatement ps, K key) throws SQLException {
        switch (key) {
            case Integer i -> ps.setInt(1, i);
            case UUID u -> ps.setObject(1, u, JDBCType.BINARY);
            case Long l -> ps.setLong(1, l);
            case String s -> ps.setString(1, s);
            case Double d -> ps.setDouble(1, d);
            default -> throw new RuntimeException("Unknown id type");
        }
    }

    K findNextDatabaseId() {
        throw new RuntimeException("Not yet implemented"); // TODO
    }

}

interface DomainObjectWithK<K> {
    void setID(K id);
    K getID();
}
