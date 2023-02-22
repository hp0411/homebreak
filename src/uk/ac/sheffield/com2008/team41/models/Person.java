package uk.ac.sheffield.com2008.team41.models;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import uk.ac.sheffield.com2008.team41.helper.DriverHelper;

import static uk.ac.sheffield.com2008.team41.helper.FunctionHelper.ShaHashFunction;

/**
 * A model containing all the necessary information about a person including mutators and accessors.
 * Credit: Team 41 Homebreak submission 2021
 */
public class Person {
    //Email is static so only has a getter
    private String email;

    //private attribute strings to be accessed by only applicable getters and setters
    private String title;
    private String forename;
    private String surname;
    private String phoneNumber;
    private Address address;

    public ArrayList<Booking> bookings = new ArrayList<Booking>();
    /*
    @return The person's current email
     */
    public String getEmail(){
        return this.email;
    }

    /*
    @return The person's current title
     */
    public String getTitle(){
        return this.title;
    }

    /*
    @return The person's bookings depending on if extending host or guest
     */
    public ArrayList<Booking> getBookings(){
        return this.bookings;
    }
    /*
    @param con Connection to the database
    @param title Variable to replace current person's title
    Replaces person's title in object and also in the database
     */
    public void setTitle(Connection con, String title){
        this.title = title;

        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("title", title, this.email));
        try {
            if (updateInformation(con, queryParameters) >= 0) {

            } else {
                throw new SQLException("Could not update information in Person, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @return The person's current forename
     */
    public String getForename(){
        return this.forename;
    }

    /*
    @return The person's current surname
     */
    public String getSurname(){
        return this.surname;
    }

    /*
    @param con Connection to the database
    @param surname Variable to replace current person's surname
    Replaces person's surname in object and also in the database
    */
    public void setSurname(Connection con, String surname){
        this.surname = surname;

        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("surname", surname, this.email));
        try {
            if (updateInformation(con, queryParameters) >= 0) {

            } else {
                throw new SQLException("Could not update information in Person, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFullName(){
        return getForename() + " " + getSurname();
    }

    /*
    @return The person's current phone number
     */
    public String getPhoneNumber(){

        return this.phoneNumber;
    }


    /*
    @return The person's current address
     */
    public Address getAddress(){
        return this.address;
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Updates the given information in the database
     */
    private static int updateInformation(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        String query = "update Person set " + objects.get(0) + " = ? where email = ?";
        objects.remove(0);
        return dHelper.execSafeUpdate(query, objects);
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Person in the database when a new Person object is constructed
     */
    public static int createPerson (Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);

        // title, forename, surname, email, password, phoneNumber, addressID
        String query = "INSERT INTO Person VALUES (?, ?, ?, ?, ?, ?, ?)";
        return dHelper.execSafeUpdate(query, objects);
    }

    /**
     * Empty constructor for a person, to allow for easy deep copying
     */
    public Person(){

    }

    /**
     * Creates a person in the database
     * @param con Connection to the MySQL Database
     * @param title
     * @param email
     * @param forename
     * @param surname
     * @param password
     * @param phoneNumber
     * @param address
     */
    public Person(Connection con, String title, String email, String forename, String surname, String password, String phoneNumber, Address address){
        try{
            this.title = title;
            this.email = email;
            this.forename = forename;
            this.surname = surname;
            this.phoneNumber = phoneNumber;
            this.address = address;
            int addressIndex = address.getAddressID();
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(title,forename,surname,email,ShaHashFunction(password,email),phoneNumber,addressIndex));
            if (createPerson(con, queryParameters) >= 0){

            }
            else{
                throw new SQLException("Could not add user to database, Error occurred");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Creates a Person Object from a Downloaded Result Set
     * @param helper
     * @param personData
     * @throws SQLException
     */
    public Person(DriverHelper helper, ResultSet personData) throws SQLException{
        this.title = personData.getString("title");
        this.email = personData.getString("email");
        this.forename = personData.getString("forename");
        this.surname = personData.getString("surname");
        this.phoneNumber = personData.getString("phoneNumber");
        this.address = new Address(helper,personData.getInt("addressID"));
    }

    public Person(Connection con, String email) {
        try{
            DriverHelper dHelper = new DriverHelper(con);
            String query = "SELECT * FROM Person WHERE email = ?";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(email));
            PreparedStatement preparedStatement = dHelper.safeStatement(query, objects);
            ResultSet rs = dHelper.execSafeQuery(preparedStatement);

            if (rs.next()){
                this.title = rs.getString("title");
                this.email = rs.getString("email");
                this.forename = rs.getString("forename");
                this.surname = rs.getString("surname");
                this.phoneNumber = rs.getString("phoneNumber");
                this.address = new Address(dHelper, rs.getInt("addressID"));
            }
            rs.close();
            preparedStatement.close();
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /*
    @param email Email address inputted by user
    @param password Password inputted by user
    @param helper A DriverHelper created to assist with SQL in java models
    @return guestResult
    When given an email address and password it seeks and returns the person's account which matches those credentials
     */
    public static Person userWithCredentials(String email, String password, DriverHelper helper) {
        ArrayList<Object> parameters = new ArrayList<Object>(Arrays.asList(email,ShaHashFunction(password,email)));
        ResultSet trueUserResults  = null;
        Person result = null;
        try{
            String trueUserQuery  = "select * from Person where email = ? and password= ? limit 1";
            PreparedStatement preparedStatement = helper.safeStatement(trueUserQuery, parameters);
            trueUserResults = helper.execSafeQuery(preparedStatement);
            if (trueUserResults.next()){
                result = new Person(helper,trueUserResults);
            }
            trueUserResults.close();
            preparedStatement.close();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(throwables.getSQLState());
            if (trueUserResults != null){
                try{
                    trueUserResults.close();
                }
                catch (SQLException exception) {

                }
            }
        }
        return result;
    }

    /**
     * Checks if a specific user email exists in the database
     * @param con
     * @param email
     * @return
     */
    public static Boolean findUser(Connection con, String email){
        boolean result = false;
        DriverHelper driverHelper = new DriverHelper(con);
        try {
            String query = "SELECT * FROM Person where email = ?";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(email));

            PreparedStatement preparedStatement = driverHelper.safeStatement(query, objects);
            ResultSet rs = driverHelper.execSafeQuery(preparedStatement);
            if (rs.next()){
                result = true;
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    /**
     * Performs a deepCopy of a person from one object to another
     * @param result copyTo
     * @return output
     */
    public Person copyTo(Person result){
        result.address = this.address;
        result.phoneNumber = this.phoneNumber;
        result.title = this.title;
        result.email = this.email;
        result.surname = this.surname;
        result.forename = this.forename;
        return result;
    }


}
