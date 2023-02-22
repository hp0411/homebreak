package uk.ac.sheffield.com2008.team41.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import uk.ac.sheffield.com2008.team41.helper.DriverHelper;
import uk.ac.sheffield.com2008.team41.helper.FunctionHelper;

/**
 * A model containing all the necessary information about a property's kitchen facilities including mutators and accessors.
 * Credit: Team 41 Homebreak submission 2021
 */
public class KitchenFacility{
    //private attributes to be accessed by only applicable getters and setters
	public final static List<String> allAmenities = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("Refrigerator");
                add("Microwave");
                add("Oven");
                add("Stove");
                add("Dishwasher");
                add("Tableware");
                add("Cookware");
                add("Basic Provisions");
            }});
	
	private boolean refrigerator = false;
    private boolean microwave =  false;
    private boolean oven =  false;
    private boolean stove =  false;
    private boolean dishwasher = false;
    private boolean tableware = false;
    private boolean cookware =  false;
    private boolean basicProvisions = false;
    private int propertyID;

    /**
     * Creates a Kitchen Facility in the database
     * @param con Connection to the MySQL Database
     * @param propertyID
     * @param amenities
     */
    public KitchenFacility(Connection con, int propertyID, ArrayList<String> amenities){
    	this.propertyID = propertyID;
    	try {
	        for (String amenity : amenities){
	            switch (amenity) {
	                case "Refrigerator":
	                    refrigerator = true;
	                    break;
	                case "Microwave":
	                    microwave = true;
	                    break;
	                case "Oven":
	                    oven = true;
	                    break;
	                case "Stove":
	                    stove = true;
	                    break;
	                case "Dishwasher":
	                    dishwasher = true;
	                    break;
	                case "Tableware":
	                    tableware = true;
	                    break;
	                case "Cookware":
	                    cookware = true;
	                    break;
	                case "Basic Provisions":
	                    basicProvisions = true;
	                    break;
            	}
	        }
	        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(propertyID, this.refrigerator, this.microwave, this.oven, 
	        		this.stove, this.dishwasher, this.tableware, this.cookware, this.basicProvisions));
	        if (createKitchenFacility(con, queryParameters) >= 0){

            }
            else{
                throw new SQLException("Could not add Kitchen Facility to database, Error occurred");
            }
        } catch (Exception e) {
	            e.printStackTrace();
        }
    }

    /**
     * Downloads a Kitchen Facility from the database
     * @param con Connection to the MySQL Database
     * @param propertyID
     */
    public KitchenFacility (Connection con, int propertyID){
        try {
            DriverHelper dHelper = new DriverHelper(con);

            String query = "SELECT * FROM KitchenFacility WHERE propertyID = ?";
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(propertyID));
            PreparedStatement preparedStatement = dHelper.safeStatement(query, queryParameters);
            ResultSet rs = dHelper.execSafeQuery(preparedStatement);
            if (rs.next()){
                this.propertyID = propertyID;
                this.refrigerator = rs.getBoolean("hasRefrigerator");
                this.microwave = rs.getBoolean("hasMicrowave");
                this.oven = rs.getBoolean("hasOven");
                this.stove = rs.getBoolean(("hasStove"));
                this.dishwasher = rs.getBoolean("hasDishwasher");
                this.tableware = rs.getBoolean("hasTableware");
                this.cookware = rs.getBoolean("hasCookware");
                this.basicProvisions = rs.getBoolean("hasBasicProvisions");
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Gets the value of the corresponding Amenity
     * @param element
     * @return
     */
    public Boolean getAmenity(String element) {
        switch (element) {
            case "Refrigerator":
                return refrigerator;
            case "Microwave":
                return microwave;
            case "Oven":
                return oven;
            case "Stove":
                return stove;
            case "Dishwasher":
                return dishwasher;
            case "Tableware":
                return tableware;
            case "Cookware":
                return cookware;
            case "Basic Provisions":
                return basicProvisions;
        }
        return null;
    }

    /**
     * Sets the value of the corresponding Amenity
     * @param element
     * @param input
     */
    public void setAmenity(String element,Boolean input) {
        switch (element) {
            case "Refrigerator":
                this.refrigerator = input;
                break;
            case "Microwave":
                this.microwave = input;
                break;
            case "Oven":
                this.oven = input;
                break;
            case "Stove":
                this.stove = input;
                break;
            case "Dishwasher":
                this.dishwasher = input;
                break;
            case "Tableware":
                this.tableware = input;
                break;
            case "Cookware":
                this.cookware = input;
                break;
            case "Basic Provisions":
                this.basicProvisions = input;
                break;
            default:
                System.out.println("Failure setting amenity");
                break;

        }
    }

    /**
     * Creates a kitchen facility in the database
     * @param con Connection to the MySQL Database
     * @param objects
     * @return
     * @throws SQLException
     */
    private static int createKitchenFacility(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dhelper = new DriverHelper(con);
    
        String query = "INSERT INTO KitchenFacility VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return dhelper.execSafeUpdate(query, objects);
    }

    /*
    @param con Connection to the database
    Updates the given amenity in the database
     */
    public void updateKitchenFacilityDB(Connection con){
        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("hasRefrigerator",refrigerator,
                "hasMicrowave",microwave,
                "hasOven",oven,
                "hasStove",stove,
                "hasDishwasher",dishwasher,
                "hasTableware",tableware,
                "hasCookware",cookware
                ,this.propertyID));
        try {
            if (FunctionHelper.Updater(con,"update KitchenFacility set",queryParameters, " where propertyID = ?") <= 0) {
                throw new SQLException("Could not update Kitchen Facility, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
