package io.feoktant.ch12_Object_Relational_Structural_Patterns._1_Identity_Field.integral_key;

public class CricketerMapper extends Mapper<Cricketer> {

    public Cricketer find(long id) {
        return abstractFind(id);
    }

    @Override
    protected Cricketer createDomainObject() {
        return null;
    }
}
