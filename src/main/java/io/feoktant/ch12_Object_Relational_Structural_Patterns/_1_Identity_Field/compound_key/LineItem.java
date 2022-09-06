package io.feoktant.ch12_Object_Relational_Structural_Patterns._1_Identity_Field.compound_key;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LineItem extends DomainObjectWithKey {
    private Key key;
    private int amount;
    private String product;
}
