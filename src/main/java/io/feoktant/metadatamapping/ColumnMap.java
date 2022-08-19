package io.feoktant.metadatamapping;

import java.lang.reflect.Field;

/**
 *  This isn’t a terribly sophisticated mapping.
 *  I’m just using the default Java type mappings, which means there’s no type
 *  conversion between fields and columns.
 *  I’m also forcing a one-to-one relationship between tables and classes.
 * @param <T>
 */
public class ColumnMap<T> {
    private final String columnName;
    private final String fieldName;
    private Field field;
    private final Class<T> domainClass;

    public ColumnMap(String columnName, String fieldName, Class<T> domainClass) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.domainClass = domainClass;
        initField();
    }

    private void initField() {
        try {
            field = domainClass.getDeclaredField(getFieldName());
            field.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("unable to set up field: " + fieldName, e);
        }
    }

    public void setField(Object result, Object columnValue) {
        try {
            field.set(result, columnValue);
        } catch (Exception e) {
            throw new RuntimeException("Error in setting " + fieldName, e);
        }
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
