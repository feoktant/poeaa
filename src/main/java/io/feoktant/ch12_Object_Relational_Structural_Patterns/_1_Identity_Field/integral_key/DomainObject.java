package io.feoktant.ch12_Object_Relational_Structural_Patterns._1_Identity_Field.integral_key;

abstract public class DomainObject<E extends DomainObject<E>> {
    public static final long PLACEHOLDER_ID = -1;
    private long id = PLACEHOLDER_ID;

    public Boolean isNew() {
        return id == PLACEHOLDER_ID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
