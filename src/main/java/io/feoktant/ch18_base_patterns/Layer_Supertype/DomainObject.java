package io.feoktant.ch18_base_patterns.Layer_Supertype;

public abstract class DomainObject {
    private static final Long PLACEHOLDER_ID = -1L;
    private Long ID = PLACEHOLDER_ID;

    public DomainObject(Long ID) {
        setID(ID);
    }

    public DomainObject() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        if (ID == null)
            throw new IllegalArgumentException("Cannot set a null ID");
        this.ID = ID;
    }

    public Boolean isNew() {
        return PLACEHOLDER_ID.equals(ID);
    }

}
