package io.feoktant.ch12_Object_Relational_Structural_Patterns._3_Association_Table_Mapping;

import lombok.Data;

@Data
public class Skill {
    private Long id;
    private String name;

    public Skill(Long id) {
        this.id = id;
    }
}
