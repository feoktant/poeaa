package io.feoktant.ch12_Object_Relational_Structural_Patterns._3_Association_Table_Mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillMapper extends AbstractMapper<Skill> {
    public static final String COLUMN_LIST = " skill.name skillName  ";

    @Override
    protected String findStatement() {
        return """
            SELECT %s
              FROM skills
             WHERE  ID = ?""".formatted(COLUMN_LIST);
    }

    @Override
    protected Skill doLoad(Long id, ResultSet rs) throws SQLException {
        var result = new Skill(id);
        result.setName(rs.getString("skillName"));
        return result;
    }

}
