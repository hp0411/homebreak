package uk.ac.sheffield.com2008.team41.models;

import uk.ac.sheffield.com2008.team41.helper.DriverHelper;
import uk.ac.sheffield.com2008.team41.helper.FunctionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import static sun.misc.MessageUtils.where;

/**
 * A model containing all the necessary information about a given charge band including mutators and accessors.
 * Credit: Team 41 Homebreak submission 2021
 */
public class ChargeBand {
    //private attributes to be accessed by only applicable getters and setters
    private int chargeBandID;
    private int propertyID;
    private Double pricePerNight;
    private Double cleaningCharge;
    private Double serviceCharge;
    private Date startDate;
    private Date endDate;


    /*
    @return Returns the ID of the property that this charge band is associated with
     */
    public int getPropertyID() {
        return propertyID;
    }

    /*
    @return returns the price per night for this charge band
     */
    public Double getPricePerNight() {
        return pricePerNight;
    }

    /*
    @return Returns the cleaning charge for this charge band
     */
    public Double getCleaningCharge() {
        return cleaningCharge;
    }

    /*
    @return Returns the service charge for this charge band
     */
    public Double getServiceCharge() {
        return serviceCharge;
    }

    /*
    @return Returns the start date of the time period when this charge band applies
     */
    public Date getStartDate() {
        return startDate;
    }

    /*
    @return Returns the end date of the time period when this charge band applies
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Creates a Chargeband and adds it to the database
     * @param con Connection to the MySQL Database
     * @param propertyID
     * @param pricePerNight
     * @param cleaningCharge
     * @param serviceCharge
     * @param startDate
     * @param endDate
     */
    public ChargeBand(Connection con, int propertyID, double pricePerNight, double cleaningCharge, double serviceCharge, Date startDate, Date endDate){
        try {
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(null, propertyID, pricePerNight, cleaningCharge, serviceCharge, startDate, endDate));
            String key = createChargebandGetID(con,queryParameters);
            if (key != "") {
                this.chargeBandID = Integer.parseInt(key);
            } else {
                throw new SQLException("Could not add charge band to database, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads a Chargeband from the database
     * @param con Connection to the MySQL Database
     * @param chargeBandID
     */
    public ChargeBand(Connection con, int chargeBandID){
        try{
            DriverHelper dHelper = new DriverHelper(con);
            String query = "Select * FROM ChargeBand WHERE ChargeBandID = ?";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(chargeBandID));
            PreparedStatement preparedStatement = dHelper.safeStatement(query, objects);
            ResultSet rs = dHelper.execSafeQuery(preparedStatement);
            rs.next();
            this.chargeBandID = chargeBandID;
            this.propertyID = rs.getInt("propertyID");
            this.pricePerNight = rs.getDouble("pricePerNight");
            this.cleaningCharge = rs.getDouble("cleaningCharge");
            this.serviceCharge = rs.getDouble("serviceCharge");
            this.startDate = rs.getDate("startDate");
            this.endDate = rs.getDate("endDate");

            rs.close();
            preparedStatement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Chargeband in the database when a new Chargeband object is constructed and returns the id
     */
    private static String createChargebandGetID(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        String query = "INSERT INTO ChargeBand VALUES (?, ?, ?, ?, ?, ?, ?)";
        String key =  (String)dHelper.execSafeUpdateGetKey(query,objects);
        return key;
    }

    /*
    @param con Connection to the database
    @param propertyID ID of the property you are seeking the ChargeBand for
    @param startDate The start date of the period you are seeking the ChargeBand for
    @param endDate The end date of the period you are seeking the ChargeBand for
    @returns chargeBand The correct ChargeBand for the propertyID starting on startDate
    Given a property and dates it returns the correct charge band for that property in that period
     */

    public static ChargeBand findCharge (Connection con, int propertyID, Date startDate, Date endDate){
        DriverHelper driverHelper = new DriverHelper(con);
        ChargeBand chargeBand = null;
        try{
            String query = "SELECT * FROM ChargeBand WHERE propertyID = ? AND startDate <= ? AND endDate > ? ORDER BY startDate LIMIT 1;";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(propertyID, startDate, startDate));
            PreparedStatement preparedStatement = driverHelper.safeStatement(query, objects);
            ResultSet rs = driverHelper.execSafeQuery(preparedStatement);
            if (rs.next()){
                chargeBand = new ChargeBand(con, rs.getInt("chargeBandID"));
            }

            rs.close();
            preparedStatement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeBand;
    }

    public static ArrayList<ChargeBand> getAllChargeBand(Connection con, int propertyID){
        DriverHelper driverHelper = new DriverHelper(con);
        ArrayList<ChargeBand> chargeBands = new ArrayList<>();
        ChargeBand chargeBand = null;
        try{
            String query = "SELECT * FROM ChargeBand WHERE propertyID = ?";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(propertyID));
            PreparedStatement preparedStatement = driverHelper.safeStatement(query, objects);
            ResultSet rs = driverHelper.execSafeQuery(preparedStatement);
            while (rs.next()){
                chargeBand = new ChargeBand(con, rs.getInt("chargeBandID"));
                chargeBands.add(chargeBand);
            }

            rs.close();
            preparedStatement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeBands;
    }

    @Override
    public String toString() {
        return "ChargeBand{" +
                "chargeBandID=" + chargeBandID +
                ", propertyID=" + propertyID +
                ", pricePerNight=" + pricePerNight +
                ", cleaningCharge=" + cleaningCharge +
                ", serviceCharge=" + serviceCharge +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

}
