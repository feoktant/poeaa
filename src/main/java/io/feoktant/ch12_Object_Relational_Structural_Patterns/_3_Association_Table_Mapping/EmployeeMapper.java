package io.feoktant.ch12_Object_Relational_Structural_Patterns._3_Association_Table_Mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeMapper extends AbstractMapper<Employee> {

    public static final String COLUMN_LIST = " ID, lastname, firstname  ";
    private static final String findAllStatement = """
            SELECT %s
              FROM employees employee
             ORDER BY employee.lastname"""
            .formatted(COLUMN_LIST);

    private static final String findSkillsStatement = """
            SELECT skill.ID, %s
              FROM skills skill, employeeSkills es
             WHERE es.employeeID = ? AND skill.ID = es.skillID"""
            .formatted(SkillMapper.COLUMN_LIST);


    public Employee find(Long key) {
        return abstractFind(key);
    }

    public List<Employee> findAll() {
        return findAll(findAllStatement);
    }

    @Override
    protected String findStatement() {
        return """
                SELECT %s
                  FROM employees
                 WHERE  ID = ?""".formatted(COLUMN_LIST);
    }

    protected Employee doLoad(Long id, ResultSet rs) throws SQLException {
        Employee result = new Employee(id);
        result.setFirstName(rs.getString("firstname"));
        result.setLastName(rs.getString("lastname"));
        result.setSkills(loadSkills(id));
        return result;
    }

    private List<Skill> loadSkills(Long employeeID) {
        try (var stmt = DB.prepareStatement(findSkillsStatement)) {
            stmt.setObject(1, employeeID);
            var rs = stmt.executeQuery();
            var result = new ArrayList<Skill>();
            while (rs.next()) {
                Long skillId = rs.getLong(1);
                result.add(MapperRegistry.skill().loadRow(skillId, rs));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
