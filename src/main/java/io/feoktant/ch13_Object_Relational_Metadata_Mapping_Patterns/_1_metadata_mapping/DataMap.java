package io.feoktant.ch13_Object_Relational_Metadata_Mapping_Patterns._1_metadata_mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The data map corresponds to the mapping of one class to one table.
 * This is a simple mapping, but it will do for illustration.
 * <br/>
 * The data map contains a collection of column maps that map columns in the
 * table to fields.
 * @param <T> domain class type
 */
public class DataMap<T> {
    private final String tableName;
    private final List<ColumnMap<T>> columnMaps = new ArrayList<>();
    private final Class<T> domainClass;

    public DataMap(String tableName, Class<T> domainClass) {
        this.tableName = tableName;
        this.domainClass = domainClass;
    }

    public String columnList() {
        return Stream.concat(
                Stream.of("  ID"),
                columnMaps.stream().map(ColumnMap::getColumnName))
                .collect(Collectors.joining(", "));
    }

    public String getTableName() { return tableName; }
    public Class<T> getDomainClass() { return domainClass; }

    public String updateList() {
        return columnMaps.stream()
                .map(cm -> cm.getColumnName() + "=?")
                .collect(Collectors.joining(", ", "  SET  ", ""));
    }

    public List<ColumnMap<T>> getColumns() {
        return columnMaps;
    }

    public String insertList() {
        return Collections.nCopies(columnMaps.size(), "?")
                .stream()
                .collect(Collectors.joining(", ", ", ", ""));
    }

    public void addColumn(String columnName, String type, String fieldName) {
        columnMaps.add(new ColumnMap<>(columnName, fieldName, domainClass));
    }
}
