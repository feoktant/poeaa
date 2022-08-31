package io.feoktant.ch12_Object_Relational_Structural_Patterns._3_Association_Table_Mapping;

import lombok.Data;

import java.util.List;

@Data
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Skill> skills;

    public Employee(Long id) {
        this.id = id;
    }

}
