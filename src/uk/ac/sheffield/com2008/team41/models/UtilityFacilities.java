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
public class UtilityFacilities {
    //private attributes to be accessed by only applicable getters and setters
    public final static List<String> allAmenities = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("Central Heating");
                add("Washing Machine");
                add("Drying Machine");
                add("Fire Extinguisher");
                add("Smoke Alarm");
                add("First Aid Kit");
            }});
    private boolean centralHeating = false;
    private boolean washingMachine = false;
    private boolean dryingMachine = false;
    private boolean fireExtinguisher = false;
    private boolean smokeAlarm = false;
    private boolean firstAidKit = false;

    private int propertyID;

    /**
     * Creates a Utility Facility and uploads it to the database.
     * @param con Connection to the MySQL Database
     * @param amenities
     * @param propertyID
     */
    public UtilityFacilities(Connection con, ArrayList<String> amenities, int propertyID){
        this.propertyID = propertyID;
        for (String amenity : amenities){
            switch (amenity) {
                case "Central Heating":
                    this.centralHeating = Boolean.TRUE;
                    break;

                case "Washing Machine":
                    this.washingMachine = Boolean.TRUE;
                    break;

                case "Drying Machine":
                    this.dryingMachine = Boolean.TRUE;
                    break;

                case "Fire Extinguisher":
                    this.fireExtinguisher = Boolean.TRUE;
                    break;

                case "Smoke Alarm":
                    this.smokeAlarm = Boolean.TRUE;
                    break;

                case "First Aid Kit":
                    this.firstAidKit = Boolean.TRUE;
                    break;
            }
        }

        try {
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(propertyID, this.centralHeating, this.washingMachine, this.dryingMachine, this.fireExtinguisher, this.smokeAlarm, this.firstAidKit));
            if (createUtilityFacility(con, queryParameters) >= 0) {

            } else {
                throw new SQLException("Could not add utility facility to database, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Utility Facility in the database when a new UtilityFacilities object is constructed
     */
    private static int createUtilityFacility(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        //inserts propertyID, centralHeating (bool), washingMachine (bool), dryingMachine(bool), fireExtinguisher (bool), smokeAlarm (bool), firstAidKit (bool)
        String query = "INSERT INTO UtilityFacility VALUES (?, ?, ?, ?, ?, ?, ?)";
        return dHelper.execSafeUpdate(query, objects);
    }

    /**
     * Downloads a specific utility facility from the database
     * @param con Connection to the MySQL Database
     * @param propertyID
     */
    public UtilityFacilities(Connection con, int propertyID){
        try {
            DriverHelper dHelper = new DriverHelper(con);

            String query = "SELECT * FROM UtilityFacility WHERE propertyID = ?";
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(propertyID));
            PreparedStatement preparedStatement = dHelper.safeStatement(query, queryParameters);
            ResultSet rs = dHelper.execSafeQuery(preparedStatement);
            if (rs.next()){
                this.propertyID = propertyID;
                this.centralHeating = rs.getBoolean("hasCentralHeating");
                this.washingMachine = rs.getBoolean("hasWashingMachine");
                this.dryingMachine = rs.getBoolean("hasDryingMachine");
                this.fireExtinguisher = rs.getBoolean("hasFireExtinguisher");
                this.smokeAlarm = rs.getBoolean("hasSmokeAlarm");
                this.firstAidKit = rs.getBoolean("hasFirstAidKit");

            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Sets the value of Amenity corresponding to the value of element
     * @param element
     * @param input
     */
    public void setAmenity(String element,Boolean input) {
        switch (element) {
            case "Central Heating":
                this.centralHeating = input;
                break;

            case "Washing Machine":
                this.washingMachine = input;
                break;

            case "Drying Machine":
                this.dryingMachine = input;
                break;

            case "Fire Extinguisher":
                this.fireExtinguisher = input;
                break;

            case "Smoke Alarm":
                this.smokeAlarm = input;
                break;

            case "First Aid Kit":
                this.firstAidKit = input;
                break;
            default:
                System.out.println("Failure setting amenity");
                break;
        }
    }

    /**
     * Gets the value of Amenity corresponding to the value of element
     * @param element
     */
    public Boolean getAmenity(String element) {
        switch (element) {
            case "Central Heating":
                return centralHeating;
            case "Washing Machine":
                return washingMachine;
            case "Drying Machine":
                return dryingMachine;
            case "Fire Extinguisher":
                return fireExtinguisher;
            case "Smoke Alarm":
                return smokeAlarm;
            case "First Aid Kit":
                return firstAidKit;
        }
        return null;
    }

    /*
    @param con Connection to the database
    Updates the given amenity in the database
     */
    public void updateUtilityFacilitiesDB(Connection con){
        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("hasCentralHeating",centralHeating,
                "hasWashingMachine",washingMachine,
                "hasDryingMachine",dryingMachine,
                "hasFireExtinguisher",fireExtinguisher,
                "hasSmokeAlarm",smokeAlarm,
                "hasFirstAidKit",firstAidKit,
                "hasCentralHeating",centralHeating,
                this.propertyID));
        try {
            if (FunctionHelper.Updater(con,"update UtilityFacility set",queryParameters, " where propertyID = ?") <= 0) {
                throw new SQLException("Could not update Utility Facilities, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
