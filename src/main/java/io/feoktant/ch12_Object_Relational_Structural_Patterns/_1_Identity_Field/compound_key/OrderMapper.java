package io.feoktant.ch12_Object_Relational_Structural_Patterns._1_Identity_Field.compound_key;

import java.sql.ResultSet;

public class OrderMapper extends AbstractMapper {
    public Order find(Key key) {
        return (Order) abstractFind(key);
    }

    public Order find(Long id) {
        return find(new Key(id));
    }

    protected String findStatementString() {
        return "SELECT id, customer from orders WHERE id = ?";
    }

    @Override
    DomainObjectWithKey load(ResultSet rs) {
        throw new RuntimeException("Not yet implemented");
    }
}
