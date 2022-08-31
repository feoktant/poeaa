package io.feoktant.ch12_Object_Relational_Structural_Patterns._1_Identity_Field.compound_key;

public class DomainObjectWithKey {
    private Key key;

    protected DomainObjectWithKey(Key ID) {
        this.key = ID;
    }

    protected DomainObjectWithKey() {
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }
}
