package io.feoktant.ch10_Data_Source_Architectural_Patterns._4_Data_mapper;

public interface StatementSource {
    String sql();

    Object[] parameters();
}
