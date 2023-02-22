package uk.ac.sheffield.com2008.team41.models;

import uk.ac.sheffield.com2008.team41.helper.DriverHelper;
import uk.ac.sheffield.com2008.team41.helper.FunctionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

/**
 * A model containing all the necessary information about a property's living facilities including mutators and accessors.
 * Credit: Team 41 Homebreak submission 2021
 */
public class LivingFacilities {
    //private attributes to be accessed by only applicable getters and setters
    public final static List<String> allAmenities = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("WiFi");
                add("Television");
                add("Satellite");
                add("Streaming");
                add("DVD Player");
                add("Boardgames");
            }});
    private boolean wifi = false;
    private boolean television = false;
    private boolean satellite = false;
    private boolean streaming = false;
    private boolean dvdPlayer = false;
    private boolean boardgames = false;

    private int propertyID;

    /**
     * Adds a LivingFacility to the Database
     * @param con Connection to the MySQL Database
     * @param amenities
     * @param propertyID
     */
    public LivingFacilities(Connection con, ArrayList<String> amenities, int propertyID){
        this.propertyID = propertyID;
        for (String amenity : amenities){
            switch (amenity) {
                case "WiFi":
                    this.wifi = Boolean.TRUE;
                    break;

                case "Television":
                    this.television = Boolean.TRUE;
                    break;

                case "Satellite":
                    this.satellite = Boolean.TRUE;
                    break;

                case "Streaming":
                    this.streaming = Boolean.TRUE;
                    break;

                case "DVD Player":
                    this.dvdPlayer = Boolean.TRUE;
                    break;

                case "Boardgames":
                    this.boardgames = Boolean.TRUE;
                    break;
            }
        }

        try {
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(propertyID, this.wifi, this.television, this.satellite, this.streaming, this.dvdPlayer, this.boardgames));
            if (createLivingFacility(con, queryParameters) >= 0) {

            } else {
                throw new SQLException("Could not add living facility to database, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads a living facility from the database
     * @param con Connection to the MySQL Database
     * @param propertyID
     */
    public LivingFacilities(Connection con, int propertyID){
        try {
            DriverHelper dHelper = new DriverHelper(con);

            String query = "SELECT * FROM LivingFacility WHERE propertyID = ?";
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(propertyID));
            PreparedStatement preparedStatement = dHelper.safeStatement(query, queryParameters);
            ResultSet rs = dHelper.execSafeQuery(preparedStatement);
            if (rs.next()){
                this.propertyID = propertyID;
                this.wifi = rs.getBoolean("hasWifi");
                this.television = rs.getBoolean("hasTelevision");
                this.satellite = rs.getBoolean("hasSatellite");
                this.dvdPlayer = rs.getBoolean("hasDVDPlayer");
                this.boardgames = rs.getBoolean("hasBoardGames");
                this.streaming = rs.getBoolean("hasStreaming");
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Living Facility in the database when a new LivingFacilities object is constructed
     */
    private static int createLivingFacility(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        //inserts propertyID, wifi (bool), television (bool), satellite (bool), streaming (bool), dvdPlayer (bool), boardgames (bool)
        String query = "INSERT INTO LivingFacility VALUES (?, ?, ?, ?, ?, ?, ?)";
        return dHelper.execSafeUpdate(query, objects);
    }

    /**
     * Sets the value of the corresponding Amenity
     * @param element
     * @param input
     */
    public void setAmenity(String element,Boolean input) {
        switch (element) {
            case "WiFi":
                this.wifi = input;
                break;

            case "Television":
                this.television = input;
                break;

            case "Satellite":
                this.satellite = input;
                break;

            case "Streaming":
                this.streaming = input;
                break;

            case "DVD Player":
                this.dvdPlayer = input;
                break;

            case "Boardgames":
                this.boardgames = input;
                break;
            default:
                System.out.println("Failure setting amenity");
                break;
        }
    }

    /**
     * Gets the value of the corresponding Amenity
     * @param element
     * @return
     */
    public Boolean getAmenity(String element) {
        switch (element) {
            case "WiFi":
                return wifi;
            case "Television":
                return television;
            case "Satellite":
                return satellite;
            case "Streaming":
                return streaming;
            case "DVD Player":
                return dvdPlayer;
            case "Boardgames":
                return boardgames;
        }
        return null;
    }

    /*
    @param con Connection to the database
    Updates all given amenity in the database
     */
    public void updateLivingFacilityDB(Connection con){
        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("hasBoardGames",boardgames,
                "hasWifi",wifi,
                "hasTelevision",television,
                "hasSatellite",satellite,
                "hasDVDPlayer",dvdPlayer,
                "hasBoardGames",boardgames,
                "hasStreaming",streaming
                ,this.propertyID));
        try {
            if (FunctionHelper.Updater(con,"update LivingFacility set",queryParameters, " where propertyID = ?") <= 0) {
                throw new SQLException("Could not update Living Facility, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
