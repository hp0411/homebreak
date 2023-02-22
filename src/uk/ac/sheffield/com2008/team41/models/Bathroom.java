package uk.ac.sheffield.com2008.team41.models;

import uk.ac.sheffield.com2008.team41.helper.DriverHelper;
import uk.ac.sheffield.com2008.team41.helper.FunctionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A model containing all the necessary information about a bathroom in BathingFacilities including mutators and accessors.
 * Credit: Team 41 Homebreak submission 2021
 */
public class Bathroom {
    //private attributes to be accessed by only applicable getters and setters
    public final static List<String> allAmenities = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("Toilet");
                add("Bath");
                add("Shower");
                add("Shared");
            }});
    private boolean toilet = false;
    private boolean bath = false;
    private boolean shower = false;
    private boolean isShared = false;

    private int bathroomID;
    private int propertyID;

    /**
     * Creates a bathroom and its contents to the database
     * @param con Connection to the MySQL Database
     * @param amenities
     * @param propertyID
     */
    public Bathroom(Connection con, ArrayList<String> amenities, int propertyID){
        this.propertyID = propertyID;
        for (String amenity : amenities){
            switch (amenity) {
                case "Toilet":
                    toilet = Boolean.TRUE;
                    break;

                case "Bath":
                    bath = Boolean.TRUE;
                    break;

                case "Shower":
                    shower = Boolean.TRUE;
                    break;

                case "Shared":
                    isShared = Boolean.TRUE;
                    break;
            }
        }
        try {
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(null, propertyID, this.toilet, this.bath, this.shower, this.isShared));
            String key = createBathroomGetID(con,queryParameters);
            if (key != "") {
                this.bathroomID = Integer.parseInt(key);
            } else {
                throw new SQLException("Could not add sleeping facility to database, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads a Bathroom from the database
     * @param con Connection to the MySQL Database
     * @param bathroomID Bathroom to download
     */
    public Bathroom(Connection con, int bathroomID){
        try {
            DriverHelper driverHelper = new DriverHelper(con);
            String query = "SELECT * FROM Bathroom WHERE bathRoomID = ?";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(bathroomID));

            PreparedStatement preparedStatement = driverHelper.safeStatement(query, objects);
            ResultSet rs = driverHelper.execSafeQuery(preparedStatement);
            if (rs.next()){
                this.bathroomID = bathroomID;
                this.propertyID = rs.getInt("propertyID");
                this.toilet = rs.getBoolean("hasToilet");
                this.bath = rs.getBoolean("hasBath");
                this.shower = rs.getBoolean("hasShower");
                this.isShared = rs.getBoolean("isShared");
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a bathroom in the database when a new bathroom object is constructed
     */
    private static int createBathroom(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        //inserts bathroomID (null), propertyID, toilet, bath, shower, isShared
        String query = "INSERT INTO Bathroom VALUES (?, ?, ?, ?, ?, ?)";
        return dHelper.execSafeUpdate(query, objects);
    }

    /**
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Bathroom in the database when a new Bathroom object is constructed and returns the id
     */
    private static String createBathroomGetID(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        String query = "INSERT INTO Bathroom VALUES (?, ?, ?, ?, ?, ?)";
        String key =  (String)dHelper.execSafeUpdateGetKey(query,objects);
        return key;
    }

    /**
     * Gets all bathrooms relating to a property
     * @param con Connection to the MySQL Database
     * @param propertyID
     * @return
     */
    public static ArrayList<Bathroom> getAllBathrooms (Connection con, int propertyID){
        ArrayList<Bathroom> bathroomArrayList = new ArrayList<>();
        try{
            DriverHelper driverHelper = new DriverHelper(con);
            String query = "SELECT * FROM Bathroom WHERE propertyID = ?";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(propertyID));

            PreparedStatement preparedStatement = driverHelper.safeStatement(query, objects);
            ResultSet rs = driverHelper.execSafeQuery(preparedStatement);

            while (rs.next()){
                Bathroom tempBathroom = new Bathroom(con, rs.getInt("bathRoomID"));
                bathroomArrayList.add(tempBathroom);
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bathroomArrayList;
    }

    /*
    @return returns whether or not the Bathroom has a toilet
     */
    public boolean getToilet(){
        return this.toilet;
    }

    /*
    @return returns whether or not the Bathroom has a bath
     */
    public boolean getBath(){
        return this.bath;
    }

    /*
    @return returns whether or not the Bathroom has a shower
     */
    public boolean getShower(){
        return this.shower;
    }

    /*
    @return returns whether or not the Bathroom is shared
     */
    public boolean getIsShared(){
        return this.isShared;
    }

    /*
    @return returns the propertyID of the property which the Bathroom belongs to
     */
    public int getPropertyID(){
        return this.propertyID;
    }

    /*
    @return returns the ID of this Bathroom
     */
    public int getBathroomID(){
        return this.bathroomID;
    }

    /*
    @param con Connection to the database
    @param toilet Variable to replace current bathroom's toilet
    Replaces bathroom's toilet in object and also in the database
     */
    public void setToilet(boolean toilet) {
        this.toilet = toilet;
    }

    /*
    @param con Connection to the database
    @param toilet Variable to replace current bathroom's bath
    Replaces bathroom's bath in object and also in the database
     */
    public void setBath(boolean bath) {
        this.bath = bath;
    }

    /*
    @param con Connection to the database
    @param toilet Variable to replace current bathroom's shower
    Replaces bathroom's shower in object and also in the database
     */
    public void setShower(boolean shower) {
        this.shower = shower;
    }

    /*
    @param con Connection to the database
    @param toilet Variable to replace whether the bathroom is currently shared
    Replaces whether the bathroom is shared in object and also in the database
     */
    public void setIsShared(boolean isShared) {
        this.isShared = isShared;
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Updates the given amenity in the database
     */
    public void updateBathroomDB(Connection con){
        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("hasToilet", toilet,"hasBath",bath,"hasShower",shower, "isShared",isShared,this.bathroomID));
        try {
            if (FunctionHelper.Updater(con,"update Bathroom set ",queryParameters, " where bathroomID = ?") <= 0) {
                throw new SQLException("Could not update Bathroom, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
