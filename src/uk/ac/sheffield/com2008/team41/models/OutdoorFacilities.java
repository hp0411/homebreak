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
public class OutdoorFacilities {
    //private attributes to be accessed by only applicable getters and setters
    public final static List<String> allAmenities = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("On Site Parking");
                add("On Road Parking");
                add("Paid Carpark");
                add("Patio");
                add("Barbeque");
            }});
    private boolean onSiteParking = false;
    private boolean onRoadParking = false;
    private boolean paidCarpark = false;
    private boolean patio = false;
    private boolean barbeque = false;

    private int propertyID;

    /**
     * Creates a OutdoorFacility and uploads it to the database
     * @param con Connection to the MySQL Database
     * @param amenities
     * @param propertyID
     */
    public OutdoorFacilities(Connection con, ArrayList<String> amenities, int propertyID){
        this.propertyID = propertyID;
        for (String amenity : amenities){
            switch (amenity) {
                case "On Site Parking":
                    this.onSiteParking = Boolean.TRUE;
                    break;

                case "On Road Parking":
                    this.onRoadParking = Boolean.TRUE;
                    break;

                case "Paid Carpark":
                    this.paidCarpark = Boolean.TRUE;
                    break;

                case "Patio":
                    this.patio = Boolean.TRUE;
                    break;

                case "Barbeque":
                    this.barbeque = Boolean.TRUE;
                    break;
            }
        }

        try {
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(propertyID, this.onSiteParking, this.onRoadParking, this.paidCarpark, this.patio, this.barbeque));
            if (createOutdoorFacility(con, queryParameters) >= 0) {

            } else {
                throw new SQLException("Could not add outdoor facility to database, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an Outdoor Facility in the database
     * @param con Connection to the MySQL Database
     * @param objects
     * @return
     * @throws SQLException
     */
    private static int createOutdoorFacility(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        //inserts propertyID, onSiteParking (bool), onRoadParking (bool), paidCarpark (bool), patio (bool), barbeque (bool)
        String query = "INSERT INTO OutdoorFacility VALUES (?, ?, ?, ?, ?, ?)";
        return dHelper.execSafeUpdate(query, objects);
    }

    /**
     * Downloads a Outdoor Facility from the database
     * @param con Connection to the MySQL Database
     * @param propertyID
     */
    public OutdoorFacilities(Connection con, int propertyID){
        try {
            DriverHelper dHelper = new DriverHelper(con);

            String query = "SELECT * FROM OutdoorFacility WHERE propertyID = ?";
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(propertyID));
            PreparedStatement preparedStatement = dHelper.safeStatement(query, queryParameters);
            ResultSet rs = dHelper.execSafeQuery(preparedStatement);
            if (rs.next()){
                this.propertyID = propertyID;
                this.onSiteParking = rs.getBoolean("hasOnSiteParking");
                this.onRoadParking = rs.getBoolean("hasOnRoadParking");
                this.paidCarpark = rs.getBoolean("hasPaidCarPark");
                this.patio = rs.getBoolean("hasPatio");
                this.barbeque = rs.getBoolean("hasBarbeque");
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Gets the value of the corresponding Amenity from the database
     * @param element
     * @return
     */
    public Boolean getAmenity(String element) {
        switch (element) {
            case "On Site Parking":
                return onSiteParking;
            case "On Road Parking":
                return onRoadParking;
            case "Paid Carpark":
                return paidCarpark;
            case "Patio":
                return this.patio;
            case "Barbeque":
                return this.barbeque;
        }
        return null;
    }

    /**
     * Sets the value of the corresponding Amenity from the database
     * @param element
     * @param input
     */
    public void setAmenity(String element,Boolean input) {
        switch (element) {
            case "On Site Parking":
                onSiteParking = input;
                break;

            case "On Road Parking":
                onRoadParking = input;
                break;

            case "Paid Carpark":
                paidCarpark = input;
                break;

            case "Patio":
                patio = input;
                break;

            case "Barbeque":
                barbeque = input;
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
    public void updateOutdoorFacilitiesDB(Connection con){
        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("hasOnSiteParking",onSiteParking,
                "hasOnRoadParking",onRoadParking,
                "hasPaidCarPark",paidCarpark,
                "hasPatio",patio,
                "hasBarbeque",barbeque
                ,this.propertyID));
        try {
            if (FunctionHelper.Updater(con,"update OutdoorFacility set",queryParameters, " where propertyID = ?") <= 0) {
                throw new SQLException("Could not update Outdoor Facilities, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
