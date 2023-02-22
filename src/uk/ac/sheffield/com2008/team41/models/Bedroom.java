package uk.ac.sheffield.com2008.team41.models;

import uk.ac.sheffield.com2008.team41.helper.DriverHelper;
import uk.ac.sheffield.com2008.team41.helper.FunctionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A model containing all the necessary information about a sleeping facility's bedroom including mutators and accessors.
 * Credit: Team 41 Homebreak submission 2021
 */
public class Bedroom {
    //private attributes to be accessed by only applicable getters and setters
    private String bed1;
    private String bed2;

    private int propertyID;
    private int bedRoomID;

    public final static String[] bed1Type = {"Single", "Double", "King", "Bunk"};
    public final static String[] bed2Type = {"Null","Single", "Double", "King", "Bunk"};
    public enum bed1Types  {Single, Double, King, Bunk};
    public enum bed2Types {Null, Single, Double, King, Bunk};

    /**
     * Creates a new Bedroom and uploads it to the database
     * @param con Connection to the MySQL Database
     * @param propertyID
     * @param bed1
     */
    public Bedroom(Connection con, int propertyID, String bed1) {
        try {
            this.propertyID = propertyID;
            this.bed1 = bed1;
            this.bed2 = null;

            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(null, propertyID, bed1, null));
            String key = createBedroomGetID(con,queryParameters);
            if (key != "") {
                this.bedRoomID = Integer.parseInt(key);
            } else {
                throw new SQLException("Could not add bedroom to database, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new Bedroom and uploads it to the database
     * @param con Connection to the MySQL Database
     * @param propertyID
     * @param bed1
     * @param bed2
     */
    public Bedroom(Connection con, int propertyID, String bed1, String bed2) {
        try {
            this.propertyID = propertyID;
            this.bed1 = bed1;
            this.bed2 = bed2;

            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(null, propertyID, bed1, bed2));
            if (createBedroom(con, queryParameters) >= 0) {

            } else {
                throw new SQLException("Could not add bedroom to database, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Bedroom in the database when a new Bedroom object is constructed and returns the id
     */
    private static String createBedroomGetID(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        String query = "INSERT INTO Bedroom VALUES (?, ?, ?, ?)";
        String key =  (String)dHelper.execSafeUpdateGetKey(query,objects);
        return key;
    }


    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Bedroom in the database when a new Bedroom object is constructed
     */
    private static int createBedroom(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        //inserts sleepingFacilityID (null), bed1, bed2
        String query = "INSERT INTO Bedroom VALUES (?, ?, ?, ?)";
        return dHelper.execSafeUpdate(query, objects);
    }

    /**
     * Downloads a bedroom from the database
     * @param con Connection to the MySQL Database
     * @param bedRoomID
     */
    public Bedroom (Connection con, int bedRoomID){
        try {
            DriverHelper driverHelper = new DriverHelper(con);
            String query = "SELECT * FROM Bedroom WHERE bedRoomID = ?";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(bedRoomID));

            PreparedStatement preparedStatement = driverHelper.safeStatement(query, objects);
            ResultSet rs = driverHelper.execSafeQuery(preparedStatement);
            if (rs.next()){
                this.bedRoomID = bedRoomID;
                this.propertyID = rs.getInt("propertyID");
                this.bed1 = rs.getString("bed1Type");
                this.bed2 = rs.getString("bed2Type");
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads all bedrooms from a property
     * @param con Connection to the MySQL Database
     * @param propertyID
     * @return
     */
    public static ArrayList<Bedroom> getAllBedrooms (Connection con, int propertyID){
        ArrayList<Bedroom> bedroomArrayList = new ArrayList<>();

        try {
            DriverHelper driverHelper = new DriverHelper(con);
            String query = "SELECT * FROM Bedroom WHERE propertyID = ?";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(propertyID));

            PreparedStatement preparedStatement = driverHelper.safeStatement(query, objects);
            ResultSet rs = driverHelper.execSafeQuery(preparedStatement);

            while (rs.next()){
                Bedroom tempBedroom = new Bedroom(con, rs.getInt("bedRoomID"));
                bedroomArrayList.add(tempBedroom);
            }

            rs.close();
            preparedStatement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return bedroomArrayList;
    }

    /*
    @return Returns what kind of bed Bed1 is
     */
    public String getBed1() {
        return this.bed1;
    }

    /*
    @return Returns what kind of bed Bed1 is
     */
    public String getBed2() {
        return this.bed2;
    }

    /*
    @return Returns the propertyID of the SleepingFacility the Bedroom belongs to
     */
    public int getPropertyID() {
        return this.propertyID;
    }

    /*
    @param con Connection to the database
    @param bed Variable to replace current bedroom's bed
    Replaces bedroom's bed1 in object and also in the database
     */
    public void setBed1(String bed) {
        this.bed1 = bed;
    }

    /*
    @param con Connection to the database
    @param bed Variable to replace current bedroom's bed
    Replaces bedroom's bed2 in object and also in the database
     */
    public void setBed2(String bed) {
        this.bed2 = bed;
    }

    /*
    @param con Connection to the database
    Updates the given bed in the database
     */
    public void updateBedDB(Connection con){
        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("bed1Type", bed1,"bed2Type",bed2, this.bedRoomID));
        try {
            if (FunctionHelper.Updater(con,"update Bedroom set",queryParameters, " where bedRoomID = ?") <= 0) {
                throw new SQLException("Could not update Bedroom, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

