package io.feoktant.ch12_Object_Relational_Structural_Patterns._1_Identity_Field.compound_key;

import java.util.ArrayList;
import java.util.List;

public class Order extends DomainObjectWithKey {

    private String customer;
    private final List<LineItem> items = new ArrayList<>();

    public Order(Key ID, String customer) {
        super(ID);
        this.customer = customer;
    }

    public void addLineItem(LineItem result) {
        items.add(result);
    }
}
