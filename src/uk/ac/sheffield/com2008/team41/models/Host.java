package uk.ac.sheffield.com2008.team41.models;

import uk.ac.sheffield.com2008.team41.helper.DriverHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A model containing all the necessary information about a host including mutators and accessors.
 * Credit: Team 41 Homebreak submission 2021
 */
public class Host extends Person{
    private String hostName;
    private int hostID;
    private String email;
    public ArrayList<Property> properties;

    /*
    @param hostName Variable to replace current person's hostName
    Replaces Host's hostName in object only
     */
    public void setHostName(String hostName){
        this.hostName = hostName;
    }

    /* OVERLOADED
    @param con Connection to the database
    @param hostName Variable to replace current person's hostName
    Replaces Host's hostName in object and also in the database
     */
    public void setHostName(Connection con, String hostName){
        this.hostName = hostName;

        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("hostName", hostName, this.hostID));
        try {
            if (updateInformation(con, queryParameters) >= 0) {

            } else {
                throw new SQLException("Could not update information in Host, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Updates the given information in the database
     */
    private static int updateInformation(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        String query = "update Host set " + objects.get(0) + " = ? where hostID = ?";
        objects.remove(0);
        return dHelper.execSafeUpdate(query, objects);
    }

    /*
    @return The Host's current hostName
     */
    public String getHostName(){
        return this.hostName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHostID() {
        return hostID;
    }

    public void setHostID(int hostID) {
        this.hostID = hostID;
    }

    public Host(Connection con, String email, String hostName){
        // Super Class?
        try {
            this.hostName = hostName;
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(email, null, hostName));
            if (createHost(con, queryParameters) >= 0){

            }
            else{
                throw new SQLException("Could not add Host to database, Error occurred");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Host(){

    }
    public Host(Connection con, int hostID){
        DriverHelper driverHelper = new DriverHelper(con);
        ArrayList<Object> parameters = new ArrayList<Object>(Arrays.asList(hostID));
        try{
            String trueAddressQuery  = "select * from Host where hostID = ? limit 1;";
            PreparedStatement preparedStatement = driverHelper.safeStatement(trueAddressQuery, parameters);
            ResultSet rs = driverHelper.execSafeQuery(preparedStatement);
            while (rs.next()){
                this.hostID = hostID;
                this.hostName = rs.getString("hostName");
                this.email = rs.getString("email");
                this.properties = getProperties(con);
                this.bookings = Booking.getBookingsHost(con,hostID);
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Host in the database when a new Host object is constructed
    */
    private static int createHost(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        // email, hostID, hostname
        String query = "INSERT INTO Host VALUES (?, ?, ?)";
        return dHelper.execSafeUpdate(query, objects);
        //
    }

    // TODO: Decide
//    private static ResultSet findHost (Connection con, String email) throws SQLException {
//        DriverHelper dHelper = new DriverHelper(con);
//
//        String query = "SELECT * FROM Host WHERE email=?";
//        ArrayList<Object> objects = new ArrayList<>(Arrays.asList(email));
//
//        return dHelper.execSafeQuery(query, objects);
//    }


    /*
    @param email Email address inputted by user
    @param password Password inputted by user
    @param helper A DriverHelper created to assist with SQL in java models
    @return hostResult
    When given an email address and password it seeks and returns the host account which matches those credentials
     */
    public static Host userWithCredentials(String email, String password, Connection con) {
        DriverHelper helper = new DriverHelper(con);
        Person result = Person.userWithCredentials(email,password,helper);
        if (result == null){
            return null;
        }
        else{
            Host hostResult = null;
            String trueHostQuery  = "select * from Host where email = ? limit 1;";
            ResultSet trueHostResults = null;
            try{
                ArrayList<Object> parameters = new ArrayList<>(Arrays.asList(email));
                PreparedStatement preparedStatement = helper.safeStatement(trueHostQuery, parameters);
                trueHostResults = helper.execSafeQuery(preparedStatement);
                if (trueHostResults.next()){
                    hostResult = (Host)result.copyTo(new Host());
                    hostResult.setHostName(trueHostResults.getString("hostName"));
                    hostResult.hostID = trueHostResults.getInt("hostID");
                    hostResult.properties = hostResult.getProperties(con);
                    hostResult.bookings = Booking.getBookingsHost(con,hostResult.hostID);
                }
                trueHostResults.close();
                preparedStatement.close();
            }
            catch (SQLException throwables){
                if (trueHostResults != null){
                    try{
                        trueHostResults.close();
                    }
                    catch(SQLException exception) {

                    }
                }
            }
            return hostResult;
        }
    }

    /*
    @param helper A driver made to assist with safely executing SQL in a java model
    @return properties A list of all properties the host has registered
    Searches the database for any properties associated with this user's hostID and returns them in an ArrayList
     */
    public ArrayList<Property> getProperties(Connection con){
        DriverHelper helper = new DriverHelper(con);
        String truePropertiesQ  = "SELECT * FROM Property where HostID = ?";
        ResultSet truePropertiesR = null;
        ArrayList<Property> properties = new ArrayList<Property>();
        try{
            ArrayList<Object> parameters = new ArrayList<>(Arrays.asList(hostID));
            PreparedStatement preparedStatement = helper.safeStatement(truePropertiesQ, parameters);
            truePropertiesR = helper.execSafeQuery(preparedStatement);
            while(truePropertiesR.next()){
                Property newProp = new Property(con,truePropertiesR,this);
                properties.add(newProp);
            }
            truePropertiesR.close();
            preparedStatement.close();
        }
        catch (SQLException throwables){
            if (truePropertiesR != null){
                try{
                    truePropertiesR.close();
                }
                catch(SQLException exception) {

                }
            }
        }
        return properties;
    }

    /*
    @return properties Returns the list of properties associated with this Host object
     */
    public ArrayList<Property> getProperties(){
        return properties;
    }

  
    /*
    @param con Connection to the database
    @return boolean A true/false value corresponding to whether the host can be classed as a superhost
    Checks if the host has a high enough average rating to be considered a Super Host
     */
	public boolean isSuperhost(Connection con) {
		if (Review.calcHostRating(con, this.hostID) >= 4.7) 
    	 return true;
		return false;
    }
}
