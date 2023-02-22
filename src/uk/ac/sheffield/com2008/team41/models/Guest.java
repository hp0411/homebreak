package uk.ac.sheffield.com2008.team41.models;

import uk.ac.sheffield.com2008.team41.helper.DriverHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A model containing all the necessary information about a guest including mutators and accessors.
 * Credit: Team 41 Homebreak submission 2021
 */
public class Guest extends Person{
    private String guestName;
    private int guestID;
    private String email;

    /*
    @param guestName Variable to replace current person's guestName
    Replaces Guest's guestName in object only
     */
    public void setGuestName(String guestName){
        this.guestName = guestName;
    }


    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Updates the given information in the database
     */
    private static int updateInformation(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        String query = "update Guest set " + objects.get(0) + " = ? where guestID = ?";
        objects.remove(0);
        return dHelper.execSafeUpdate(query, objects);
    }

    public int getGuestID() {
        return guestID;
    }

    /*
        @return The Guest's current guestName
         */
    public String getGuestName(){
        return this.guestName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    /**
     * Creates a guest and adds them to the database
     * @param con Connection to the MySQL Database
     * @param email
     * @param guestName
     */
    public Guest(Connection con, String email, String guestName) {
        // Super Class?
        try {
            this.guestName = guestName;
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(email, null, guestName));
            if (createGuest(con, queryParameters) >= 0){

            }
            else{
                throw new SQLException("Could not add Guest to database, Error occurred");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads a guest from the database
     * @param con Connection to the MySQL Database
     * @param guestID
     */
    public Guest(Connection con, int guestID){
        DriverHelper dHelper = new DriverHelper(con);
        String query = "SELECT * FROM Guest where guestID = ?";
        ArrayList<Object> objects = new ArrayList<>(Arrays.asList(guestID));
        try {
            PreparedStatement preparedStatement = dHelper.safeStatement(query, objects);
            ResultSet rs = dHelper.execSafeQuery(preparedStatement);

            if (rs.next()){
                this.guestID = rs.getInt("guestID");
                this.guestName = rs.getString("guestName");
                this.email = rs.getString("email");
                this.bookings = Booking.getBookingsGuest(con,guestID);
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Guest(){

    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Guest in the database when a new Guest object is constructed
     */
    private static int createGuest(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dhelper = new DriverHelper(con);
        // email, guestID, hostname
        String query = "INSERT INTO Guest VALUES (?, ?, ?)";
        return dhelper.execSafeUpdate(query, objects);
    }

    /*
    @param email Email address inputted by user
    @param password Password inputted by user
    @param helper A DriverHelper created to assist with SQL in java models
    @return guestResult
    When given an email address and password it seeks and returns the guest account which matches those credentials
     */
    public static Guest userWithCredentials(String email, String password, Connection con) {
        DriverHelper helper = new DriverHelper(con);
        Person result = Person.userWithCredentials(email, password, helper);
        if (result == null) {
            return null;
        } else {
            Guest guestResult = null;
            String trueGuestQuery = "select * from Guest where email = ? limit 1;";
            ResultSet trueGuestResults = null;
            try {
                ArrayList<Object> parameters = new ArrayList<>(Arrays.asList(email));
                PreparedStatement preparedStatement = helper.safeStatement(trueGuestQuery, parameters);
                trueGuestResults = helper.execSafeQuery(preparedStatement);
                if (trueGuestResults.next()) {
                    guestResult = (Guest) result.copyTo(new Guest());
                    guestResult.setGuestName(trueGuestResults.getString("guestName"));
                    guestResult.guestID = trueGuestResults.getInt("guestID");
                    guestResult.bookings = Booking.getBookingsGuest(con,guestResult.guestID);
                }
                trueGuestResults.close();
                preparedStatement.close();
            } catch (SQLException throwables) {
                if (trueGuestResults != null) {
                    try {
                        trueGuestResults.close();
                    } catch (SQLException exception) {

                    }
                }
            }
            return guestResult;
        }
    }
}