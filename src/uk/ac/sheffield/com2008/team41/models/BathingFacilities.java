package uk.ac.sheffield.com2008.team41.models;

import uk.ac.sheffield.com2008.team41.helper.DriverHelper;
import uk.ac.sheffield.com2008.team41.helper.FunctionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

/**
 * A model containing all the necessary information about a property's bathing facilities including mutators and accessors.
 * Credit: Team 41 Homebreak submission 2021
 */
public class BathingFacilities {
    //private attributes to be accessed by only applicable getters and setters
    public final static List<String> allAmenities = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("Hair Dryer");
                add("Shampoo");
                add("Toilet Paper");
            }});
    private boolean hairDryer = false;
    private boolean shampoo = false;
    private boolean toiletPaper = false;

    private int propertyID;

    /**
     * Creates a bathing facility and adds it to the database
     * @param con
     * @param amenities
     * @param propertyID
     */
    public BathingFacilities(Connection con, ArrayList<String> amenities, int propertyID){
        this.propertyID = propertyID;
        for (String amenity : amenities){
            switch (amenity) {
                case "Hair Dryer":
                    hairDryer = Boolean.TRUE;
                    break;

                case "Shampoo":
                    shampoo = Boolean.TRUE;
                    break;

                case "Toilet Paper":
                    toiletPaper = Boolean.TRUE;
                    break;
            }
        }

        try {
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(propertyID, this.hairDryer, this.shampoo, this.toiletPaper));
            int result = createBathingFacilityGetID(con,queryParameters);
            if (result !=  0) {
                throw new SQLException("Could not add bathing facility to database, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Bathing Facility in the database when a new BathingFacilities object is constructed
     */
    private static int createBathingFacility(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        //inserts propertyID, hairDryer (bool), shampoo (bool), toiletPaper (bool)
        String query = "INSERT INTO BathingFacilities VALUES (?, ?, ?, ?)";
        return dHelper.execSafeUpdate(query, objects);
    }

    /**
     * Gets a bathing facility from the database
     * @param con Connection to the MySQL database
     * @param propertyID
     */
    public BathingFacilities(Connection con, int propertyID){
        try {
            DriverHelper dHelper = new DriverHelper(con);

            String query = "SELECT * FROM BathingFacilities WHERE propertyID = ?";
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(propertyID));
            PreparedStatement preparedStatement = dHelper.safeStatement(query, queryParameters);
            ResultSet rs = dHelper.execSafeQuery(preparedStatement);
            if (rs.next()){
                this.propertyID = propertyID;
                this.hairDryer = rs.getBoolean("hasHairDryer");
                this.shampoo = rs.getBoolean("hasShampoo");
                this.toiletPaper = rs.getBoolean("hasToiletPaper");
            }
            rs.close();
            preparedStatement.close();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Property in the database when a new Property object is constructed and returns the id
     */
    private static int createBathingFacilityGetID(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        String query = "INSERT INTO BathingFacilities VALUES (?, ?, ?, ?)";
        return dHelper.execSafeUpdate(query,objects);
    }

    /**
     * Gets the Amenity corresponding to the string
     * @param element
     * @return
     */
    public Boolean getAmenity(String element) {
        switch (element) {
            case "Hair Dryer":
                return hairDryer;
            case "Shampoo":
                return shampoo;
            case "Toilet Paper":
                return toiletPaper;

        }
        return null;
    }

    public void setAmenity(String element,Boolean input) {
        switch (element) {
            case "Hair Dryer":
                this.hairDryer = input;
                break;
            case "Shampoo":
                this.shampoo = input;
                break;
            case "Toilet Paper":
                this.toiletPaper = input;
                break;
            default:
                System.out.println("Failure setting amenity");
                break;

        }
    }

    /*
    @param con Connection to the database
    Updates the given amenity in the database
     */
    public void updateBathingFacilitiesDB(Connection con){
        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("hasHairDryer",hairDryer,"hasShampoo",shampoo,"hasToiletPaper",toiletPaper,this.propertyID));
        try {
            if (FunctionHelper.Updater(con,"update BathingFacilities set",queryParameters, " where propertyID = ?") <= 0) {
                throw new SQLException("Could not update Bathing Facilities, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
