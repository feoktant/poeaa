package io.feoktant.domain;

public abstract class DomainObject {
    protected Long id;
    public Long getID() { return id; }
    public void setID(Long key) { this.id = key; }
}
