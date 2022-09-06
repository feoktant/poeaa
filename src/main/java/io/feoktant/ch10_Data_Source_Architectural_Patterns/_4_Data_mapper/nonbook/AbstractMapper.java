package io.feoktant.ch10_Data_Source_Architectural_Patterns._4_Data_mapper.nonbook;

import io.feoktant.ch10_Data_Source_Architectural_Patterns._4_Data_mapper.StatementSource;
import io.feoktant.ch18_base_patterns.Layer_Supertype.DomainObject;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Just a mapper, no <b>identity map</b>
 * @param <E> Entity type
 */
public abstract class AbstractMapper<E extends DomainObject> {

    private final DataSource dataSource;

    public AbstractMapper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    abstract protected String findStatement();

    abstract protected String insertStatement();

    abstract protected E doLoad(Long id, ResultSet rs) throws SQLException;

    abstract protected void doInsert(E subject, PreparedStatement insertStatement)
            throws SQLException;

    /**
     * Fowler says, add abstractFind. We can omit this, and use normal find,
     * thanks for generics, was:
     * <code>protected DomainObject abstractFind(Long id)</code>
     */
    public Optional<E> find(Long id) {
        try (var conn = dataSource.getConnection()) {
            var findStatement = conn.prepareStatement(findStatement());
            findStatement.setLong(1, id);
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
        Long id = rs.getLong(1);

        return doLoad(id, rs);
    }

    protected List<E> loadAll(ResultSet rs) throws SQLException {
        var result = new ArrayList<E>();
        while (rs.next()) result.add(load(rs));
        return result;
    }

    public List<E> findMany(StatementSource source) {
        try (var conn = dataSource.getConnection()) {
            var stmt = conn.prepareStatement(source.sql());
            for (int i = 0; i < source.parameters().length; i++)
                stmt.setObject(i + 1, source.parameters()[i]);
            var rs = stmt.executeQuery();
            return loadAll(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long insert(E subject) {
        try (var conn = dataSource.getConnection()) {
            var insertStatement = conn.prepareStatement(insertStatement());
            subject.setID(findNextDatabaseId());
            insertStatement.setInt(1, subject.getID().intValue());
            doInsert(subject, insertStatement);
            insertStatement.execute();
            return subject.getID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Long findNextDatabaseId() {
        throw new RuntimeException("Not yet implemented"); // TODO
    }

}
