package io.feoktant.domain;

public class Person extends DomainObject {
    private String lastName;
    private String firstName;
    private int numberOfDependents;

    public Person(Long id,
                  String lastName,
                  String firstName,
                  int numberOfDependents) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.numberOfDependents = numberOfDependents;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getNumberOfDependents() {
        return numberOfDependents;
    }

    public void setNumberOfDependents(int numberOfDependents) {
        this.numberOfDependents = numberOfDependents;
    }
}
