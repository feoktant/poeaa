package io.feoktant.ch10_Data_Source_Architectural_Patterns._1_Table_Data_Gateway_aka_DAO;

public record PersonDTO(
        Long ID,
        String lastName,
        String firstName,
        int numberOfDependents) {
}
