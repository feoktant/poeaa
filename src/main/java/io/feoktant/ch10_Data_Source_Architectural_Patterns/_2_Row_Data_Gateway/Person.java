package io.feoktant.ch10_Data_Source_Architectural_Patterns._2_Row_Data_Gateway;

public class Person {
    private final PersonGateway data;

    public Person(PersonGateway data) {
        this.data = data;
    }

    public int getNumberOfDependents() {
        return data.getNumberOfDependents();
    }

//    public Money getExemption() {
//        Money baseExemption = Money.dollars(1500);
//        Money dependentExemption = Money.dollars(750);
//        return baseExemption.add(dependentExemption.multiply(this.getNumberOfDependents()));
//    }
}
