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
public class SleepingFacilities {
    //private attributes to be accessed by only applicable getters and setters
    public final static List<String> allAmenities = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("Bed Linen");
                add("Towels");
            }});
    private boolean bedLinen = false;
    private boolean towels = false;
    public ArrayList<Bedroom> bedrooms;

    private int propertyID;

    /**
     * Creates a sleeping facility and upload it to the database
     * @param con Connection to the MySQL Database
     * @param amenities
     * @param bedrooms
     * @param propertyID
     */
    public SleepingFacilities(Connection con, ArrayList<String> amenities, ArrayList<Bedroom> bedrooms, int propertyID) {
        this.propertyID = propertyID;
        this.bedrooms = bedrooms;
        for (String amenity : amenities) {
            switch (amenity) {
                case "Bed Linen":
                    this.bedLinen = Boolean.TRUE;
                    break;

                case "Towels":
                    this.towels = Boolean.TRUE;
                    break;
            }
        }

        try {
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(propertyID, this.bedLinen, this.towels));
            if (createSleepingFacility(con, queryParameters) >= 0) {

            } else {
                throw new SQLException("Could not add sleeping facility to database, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Sleeping Facility in the database when a new SleepingFacilities object is constructed
     */
    private static int createSleepingFacility(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        //inserts propertyID, bedLinen (bool), towels (bool)
        String query = "INSERT INTO SleepingFacilities VALUES (?, ?, ?)";
        return dHelper.execSafeUpdate(query, objects);
    }

    /**
     * Downloads a specific sleeping facility from the database
     * @param con Connection to the MySQL Database
     * @param propertyID
     */
    public SleepingFacilities(Connection con, int propertyID) {
        try {
            DriverHelper dHelper = new DriverHelper(con);

            String query = "SELECT * FROM SleepingFacilities WHERE propertyID = ?";
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(propertyID));
            PreparedStatement preparedStatement = dHelper.safeStatement(query, queryParameters);
            ResultSet rs = dHelper.execSafeQuery(preparedStatement);
            if (rs.next()) {
                this.propertyID = propertyID;
                this.bedLinen = rs.getBoolean("hasBedLinen");
                this.towels = rs.getBoolean("hasTowels");
                this.bedrooms = Bedroom.getAllBedrooms(con, propertyID);

            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Returns the attribute corresponding to the value of the element.
     * @param element
     * @return
     */
    public Boolean getAmenity(String element) {
        switch (element) {
            case "Bed Linen":
                return bedLinen;
            case "Towels":
                return towels;
        }
        return null;
    }

    /**
     * Sets the Amenity corresponding to the element value
     * @param element
     * @param input
     */
    public void setAmenity(String element,Boolean input) {
        switch (element) {
            case "Bed Linen":
                this.bedLinen = input;
                break;

            case "Towels":
                this.towels = input;
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
    public void updateSleepingFacilitiesDB(Connection con){
        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("hasBedLinen",bedLinen,
                "hasTowels",towels,
                this.propertyID));
        try {
            if (FunctionHelper.Updater(con,"update SleepingFacilities set",queryParameters, " where propertyID = ?") <= 0) {
                throw new SQLException("Could not update Sleeping Facilities, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the amount of bedrooms in a sleeping facility
     * @return
     */
    public int calcBedrooms() {
        return bedrooms.size();
    }

    /**
     * Calculates the amount of beds in a sleeping facility
     * @return
     */
    public int calcBeds() {
        int counter = 0;
        for (int i = 0; i < bedrooms.size(); i++) {
            counter += 0;
            if (bedrooms.get(i) != null) {
                counter += 1;
            }
        }
        return counter;
    }

    /**
     * Calculates the amount of sleepers per sleeping facility
     * @return
     */
    public int calcSleeper() {
        int counter = 0;
        for (int i = 0; i < bedrooms.size(); i++) {
            switch (bedrooms.get(i).getBed1()) {
                case ("Single"):
                    counter += 1;
                    break;
                default:
                    counter += 2;
            }
            if (bedrooms.get(i).getBed2() != null) {
                switch (bedrooms.get(i).getBed2()) {
                    case ("Single"):
                        counter += 1;
                        break;
                    default:
                        counter += 2;

                }
            }
        }
        return counter;
    }
}
