package io.feoktant.ch10_Data_Source_Architectural_Patterns._2_Row_Data_Gateway;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonGateway {
    private Long ID;
    private String lastName;
    private String firstName;
    private int numberOfDependents;

    public Long getID() {return ID;}
    public void setID(Long ID) {this.ID = ID;}

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public int getNumberOfDependents() {return numberOfDependents;}
    public void setNumberOfDependents(int numberOfDependents) {this.numberOfDependents = numberOfDependents;}


    private Connection DB;

    public PersonGateway(Long ID, String lastName, String firstName, int numberOfDependents) {
        this.ID = ID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.numberOfDependents = numberOfDependents;
    }

    private static final String updateStatementString = """
            UPDATE people
               SET lastname = ?, firstname = ?, number_of_dependents = ?
             WHERE id = ?""";

    public void update() {
        try (var updateStatement = DB.prepareStatement(updateStatementString)) {
            updateStatement.setString(1, lastName);
            updateStatement.setString(2, firstName);
            updateStatement.setInt(3, numberOfDependents);
            updateStatement.setInt(4, getID().intValue());
            updateStatement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final String insertStatementString =
            "INSERT INTO people VALUES (?, ?, ?, ?)";
    public Long insert() {
        try (var insertStatement = DB.prepareStatement(insertStatementString)) {
            setID(findNextDatabaseId());
            insertStatement.setInt(1, getID().intValue());
            insertStatement.setString(2, lastName);
            insertStatement.setString(3, firstName);
            insertStatement.setInt(4, numberOfDependents);
            insertStatement.execute();
            Registry.addPerson(this);
            return getID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Long findNextDatabaseId() {
        return -1L; // TODO get it from DB
    }

    public static PersonGateway load(ResultSet rs) throws SQLException {
        Long id = rs.getLong(1);
        PersonGateway result = (PersonGateway) Registry.getPerson(id);
        if (result  != null) return result;

        String lastNameArg = rs.getString(2);
        String firstNameArg = rs.getString(3);
        int numDependentsArg = rs.getInt(4);
        result = new PersonGateway(id, lastNameArg, firstNameArg, numDependentsArg);
        Registry.addPerson(result);
        return result;
    }
}
