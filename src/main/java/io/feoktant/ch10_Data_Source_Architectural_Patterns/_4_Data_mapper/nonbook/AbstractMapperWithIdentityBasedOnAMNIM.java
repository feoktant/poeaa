package io.feoktant.ch10_Data_Source_Architectural_Patterns._4_Data_mapper.nonbook;

import io.feoktant.ch18_base_patterns.Layer_Supertype.DomainObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @param <E> Entity type
 */
public abstract class AbstractMapperWithIdentityBasedOnAMNIM<E extends DomainObject> {
    private final AbstractMapper<E> mapper;
    private final Map<Long, E> loadedMap = new HashMap<>();

    public AbstractMapperWithIdentityBasedOnAMNIM(AbstractMapper<E> mapper) {
        this.mapper = mapper;
    }

    public Optional<E> find(Long id) {
        return Optional
                .of(loadedMap.get(id))
                .or(() -> mapper.find(id));
    }

    protected E load(ResultSet rs) throws SQLException {
        Long id = rs.getLong(1);
        if (loadedMap.containsKey(id)) return loadedMap.get(id);

        var result = mapper.load(rs);
        loadedMap.put(id, result);
        return result;
    }

}
