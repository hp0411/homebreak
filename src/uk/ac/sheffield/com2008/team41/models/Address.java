package uk.ac.sheffield.com2008.team41.models;

import uk.ac.sheffield.com2008.team41.helper.DriverHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A model containing all the necessary information about a property's address including mutators and accessors.
 * Credit: Team 41 Homebreak submission 2021
 */


public class Address {
    //private attribute strings to be accessed by only applicable getters and setters
    private String houseNumber;
    private String streetName;
    private String placeName;
    private String postCode;
    private int addressID;

    public int getAddressID(){
        return this.addressID;
    }

    /*
    @return The address's current houseNumber
     */
    public String getHouseNumber(){
        return this.houseNumber;
    }

    /*
    @return The address's current streetName
     */
    public String getStreetName(){
        return this.streetName;
    }

    /*
    @return The address's current placeName
     */
    public String getPlaceName(){
        return this.placeName;
    }

    /*
    @return The address's current postCode
     */
    public String getPostCode(){
        return this.postCode;
    }

    /**
     * Creates an Address Object and also creates an address item in the database table.
     * @param con //Connection to the MySQL Server
     * @param houseNumber //House Number for New Address
     * @param streetName //Street Name for New Address
     * @param placeName //Place Name for New Address
     * @param postCode //Post Code for New Address
     * @throws SQLException
     */
    public Address(Connection con, String houseNumber, String streetName, String placeName, String postCode) throws SQLException {
        /**
         * Sets local object attributes
         */
        this.houseNumber = houseNumber;
        this.streetName = streetName;
        this.placeName = placeName;
        this.postCode = postCode;

        /**
         * Creates an SQL Query for the Database and then attempts to create an address to said Database using all of the parameters.
         */
        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(null, houseNumber, streetName, placeName, postCode));
        DriverHelper helper = new DriverHelper(con);
        if (createAddress(con, queryParameters) >= 0){
            /**
             * After creating the address it tries to get the addressID via performing a query with exact same information.
             */
            String query = "Select * From Address WHERE houseNumber=? AND postCode=?";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(houseNumber, postCode));
            PreparedStatement preparedStatement = helper.safeStatement(query, objects);

            try (ResultSet rs = findAddress(con, preparedStatement)) {
                rs.next();
                this.addressID = rs.getInt("addressID");
                preparedStatement.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }

        }
        else{
            throw new SQLException("Could not add Host to database, Error occurred");
        }

    }

    /**
     * Downloads a specific address from the server via their addressID
     * @param helper //Helper Object with a Database Connection, For SQL Injection Safe Queries
     * @param addressID //Address ID to find, and download
     * @throws SQLException //Cannot find address or cannot connect to database
     */
    public Address(DriverHelper helper, int addressID) throws SQLException{
        ArrayList<Object> parameters = new ArrayList<Object>(Arrays.asList(addressID));
        ResultSet trueAddressResults  = null;
        try{
            //Creates a sanitised prepared statement for datebase and executes it to download user information.
            String trueAddressQuery  = "select * from Address where addressID = ? limit 1;";
            PreparedStatement preparedStatement = helper.safeStatement(trueAddressQuery, parameters);
            trueAddressResults = helper.execSafeQuery(preparedStatement);
            if (trueAddressResults.next()){
                this.addressID = addressID;
                this.placeName = trueAddressResults.getString("placeName");
                this.streetName = trueAddressResults.getString("streetName");
                this.houseNumber = trueAddressResults.getString("houseNumber");
                this.postCode = trueAddressResults.getString("postCode");
            }
            trueAddressResults.close();
            preparedStatement.close();
        }
        catch (SQLException throwables) {
            if (trueAddressResults != null){
                trueAddressResults.close();

            }
        }
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates an address in the database when a new Address object is constructed
     */
    public static int createAddress(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        
        // addressID (null), houseNumber, streetName, placeName, postCode
        String query = "INSERT INTO Address Values (?, ?, ?, ?, ?)";
        return dHelper.execSafeUpdate(query, objects);
    }

    /*
    @param con Connection to the database
    @param preparedStatement a prepared SQL statement
    @return the ResultSet produced by the prepared SQL statement
     */
    private static ResultSet findAddress(Connection con, PreparedStatement preparedStatement) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        return dHelper.execSafeQuery(preparedStatement);
    }

    @Override
    public String toString() {
        return "Address{" +
                "houseNumber='" + houseNumber + '\'' +
                ", streetName='" + streetName + '\'' +
                ", placeName='" + placeName + '\'' +
                ", postCode='" + postCode + '\'' +
                ", addressID=" + addressID +
                '}';
    }


}
