package io.feoktant.ch12_Object_Relational_Structural_Patterns._3_Association_Table_Mapping;

public class MapperRegistry {

    private MapperRegistry() {}

    private static final SkillMapper skillMapper = new SkillMapper();
    private static final EmployeeMapper employeeMapper = new EmployeeMapper();

    public static SkillMapper skill() {
        return skillMapper;
    }

    public static EmployeeMapper employee() {
        return employeeMapper;
    }

}
