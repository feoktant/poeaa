package io.feoktant.ch13_Object_Relational_Metadata_Mapping_Patterns._1_metadata_mapping;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

/**
 * This isn’t a terribly sophisticated mapping.
 * I’m just using the default Java type mappings, which means there’s no type
 * conversion between fields and columns.
 * I’m also forcing a one-to-one relationship between tables and classes.
 *
 * @param <T>
 */
public class ColumnMap<T> {
    private final String columnName;
    private String fieldName;
    private Field field;
    private final BiConsumer<T, Object> setter;

    public ColumnMap(String columnName, String fieldName, Class<T> domainClass) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        initField(domainClass);
        this.setter = (T t, Object value) -> {
            try {
                field.set(t, value);
            } catch (Exception e) {
                throw new RuntimeException("Error in setting " + fieldName, e);
            }
        };
    }

    public ColumnMap(String columnName, BiConsumer<T, Object> setter) {
        this.columnName = columnName;
        this.setter = setter;
    }

    private void initField(Class<T> domainClass) {
        try {
            field = domainClass.getDeclaredField(getFieldName());
            field.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("unable to set up field: " + fieldName, e);
        }
    }

    public void setField(T result, Object columnValue) {
        setter.accept(result, columnValue);
    }

    public String getColumnName() {
        return columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getValue(Object subject) {
        try {
            return field.get(subject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
