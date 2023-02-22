package uk.ac.sheffield.com2008.team41.models;

import uk.ac.sheffield.com2008.team41.helper.DriverHelper;
import uk.ac.sheffield.com2008.team41.helper.FunctionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A model containing all the necessary information about a property's bathing facilities including mutators and accessors.
 * Credit: Team 41 Homebreak submission 2021
 */
public class Property {
    //private attributes to be accessed by only applicable getters and setters
    private Address address;
    private Host host;
    private String shortName;
    private String description;
    private String generalLocation;
    private Boolean hasBreakfast;

    private int propertyID;
    public SleepingFacilities sleepingFacilities;
    public KitchenFacility kitchenFacility;
    public  LivingFacilities livingFacilities;
    public  BathingFacilities bathingFacilities;
    public UtilityFacilities utilityFacilities;
    public OutdoorFacilities outdoorFacilities;
    public ArrayList<Bedroom> bedrooms;
    public ArrayList<Bathroom> bathrooms;
    public ArrayList<ChargeBand> chargeBands;

    public SleepingFacilities getSleepingFacilities(){
        return this.sleepingFacilities;
    }
    public KitchenFacility getKitchenFacility(){
        return this.kitchenFacility;
    }
    public LivingFacilities getLivingFacilities(){
        return this.livingFacilities;
    }
    public BathingFacilities getBathingFacilities(){
        return this.bathingFacilities;
    }
    public UtilityFacilities getUtilityFacilities(){
        return this.utilityFacilities;
    }
    public OutdoorFacilities getOutdoorFacilities(){
        return this.outdoorFacilities;
    }

    /*
    @return The property's address
     */
    public Address getAddress(){
        return this.address;
    }

    /*
    @param con Connection to the database
    @param address Variable to replace current property's address
    Replaces property's address in object and also in the database
     */
    public void setAddress(Connection con, Address address){
        this.address = address;

        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("address", address, this.propertyID));
        try {
            if (updateInformation(con, queryParameters) >= 0) {

            } else {
                throw new SQLException("Could not update property, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @return The property's short name
     */
    public String getShortName() {
        return shortName;
    }

    /*
    @param con Connection to the database
    @param shortName Variable to replace current Property's shortName
    Replaces property's shortName in object and also in the database
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    /*
        @return The property's description
         */
    public String getDescription() {
        return description;
    }

    /*
    @param description Variable to replace current Property's description
    Replaces property's description in object
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /*
    @return The property's general location
     */
    public String getGeneralLocation() {
        return generalLocation;
    }

    /*
    @param con Connection to the database
    @param generalLocation Variable to replace current Property's generalLocation
    Replaces property's generalLocation in object and also in the database
     */
    public void setGeneralLocation(String generalLocation) {
        this.generalLocation = generalLocation;
    }

    /*
    @return Whether the property provides breakfast
     */
    public Boolean getHasBreakfast() {
        return hasBreakfast;
    }

    /*
    @param con Connection to the database
    @param hasBreakfast Variable to replace current Property's hasBreakfast
    Replaces property's hasBreakfast in object and also in the database
     */
    public void setHasBreakfast(Boolean hasBreakfast) {
        this.hasBreakfast = hasBreakfast;
    }

    /*
    @return The property ID
     */
    public int getPropertyID() {
        return propertyID;
    }

    /*
    @param propertyID Variable to replace the current propertyID
    Replaces propertyID in the object only as it is a primary key
     */
    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }

    /*
    @param con Connection to the database
    Updates the given amenity in the database
     */
    public void updatePropertyDB(Connection con){
        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("Property.description",description,
                "Property.shortName",shortName,
                "Property.hasBreakfast",hasBreakfast,
                this.propertyID));
        try {
            if (FunctionHelper.Updater(con,"UPDATE Property SET",queryParameters, " WHERE propertyID = ?;") <= 0) {
                throw new SQLException("Could not update Property Info, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Updates the given information in the database
     */
    private static int updateInformation(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        String query = "update Property set " + objects.get(0) + " = ? where propertyID = ?";
        objects.remove(0);
        return dHelper.execSafeUpdate(query, objects);
    }

    /**
     * Creates a Property and uploads it to the MySQL Database
     * @param con Connection to the MySQL Database
     * @param address
     * @param host
     * @param shortName
     * @param description
     * @param generalLocation
     * @param hasBreakfast
     */
    public Property(Connection con, Address address, Host host, String shortName, String description, String generalLocation, Boolean hasBreakfast ){
        try {
            this.address = address;
            this.host = host;
            this.shortName = shortName;
            this.description = description;
            this.generalLocation = generalLocation;
            this.hasBreakfast = hasBreakfast;

            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(address.getAddressID(), host.getHostID(), shortName, description, generalLocation, hasBreakfast));
            String key = createPropertyGetID(con, queryParameters);
            if (key != "") {
                this.propertyID = Integer.parseInt(key);
            }
            else {
                throw new SQLException("Could not add property to database, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads a property with a specific ID from the database
     * @param con Connection to the MySQL Database
     * @param propertyID
     */
    public Property(Connection con, int propertyID){
        DriverHelper dHelper = new DriverHelper(con);
        String query = "SELECT * FROM Property where propertyID=?";
        ArrayList<Object> objects = new ArrayList<>(Arrays.asList(propertyID));
        try {
            PreparedStatement preparedStatement = dHelper.safeStatement(query, objects);
            ResultSet rs = findProperty(con, preparedStatement);

            if (rs.next()){
                this.propertyID = propertyID;
                this.shortName = rs.getString("shortName");
                this.description = rs.getString("description");
                this.generalLocation = rs.getString("generalLocation");
                this.hasBreakfast = rs.getBoolean("hasBreakfast");
                this.address = new Address(dHelper, rs.getInt("addressID"));
                this.host = new Host(con, rs.getInt("hostID"));
                this.bathingFacilities = new BathingFacilities(con,propertyID);
                this.utilityFacilities = new UtilityFacilities(con,propertyID);
                this.sleepingFacilities = new SleepingFacilities(con,propertyID);
                this.kitchenFacility = new KitchenFacility(con,propertyID);
                this.livingFacilities = new LivingFacilities(con,propertyID);
                this.outdoorFacilities = new OutdoorFacilities(con,propertyID);
                this.sleepingFacilities.bedrooms = Bedroom.getAllBedrooms(con, propertyID);
                this.bathrooms = Bathroom.getAllBathrooms(con, propertyID);
                this.chargeBands = ChargeBand.getAllChargeBand(con,propertyID);
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a property from a pre-existing downloaded result and a pre-existing downloaded Host
     * @param con Connection to the MySQL Database
     * @param result
     * @param host
     * @throws SQLException
     */
    public Property(Connection con, ResultSet result,Host host) throws SQLException {
        DriverHelper helper = new DriverHelper(con);
        this.propertyID = result.getInt("propertyID");
        this.generalLocation = result.getString("generalLocation");
        this.shortName = result.getString("shortName");
        this.description = result.getString("description");
        this.hasBreakfast = result.getBoolean("hasBreakfast");
        this.bathingFacilities = new BathingFacilities(helper.getCon(),propertyID);
        this.utilityFacilities = new UtilityFacilities(helper.getCon(),propertyID);
        this.sleepingFacilities = new SleepingFacilities(helper.getCon(),propertyID);
        this.kitchenFacility = new KitchenFacility(helper.getCon(),propertyID);
        this.livingFacilities = new LivingFacilities(helper.getCon(),propertyID);
        this.outdoorFacilities = new OutdoorFacilities(helper.getCon(),propertyID);
        this.chargeBands = ChargeBand.getAllChargeBand(helper.getCon(),propertyID);
        this.sleepingFacilities.bedrooms = Bedroom.getAllBedrooms(helper.getCon(), propertyID);
        this.bathrooms = Bathroom.getAllBathrooms(helper.getCon(), propertyID);
        this.address = new Address(helper, result.getInt("addressID"));
        this.hasBreakfast = result.getBoolean("hasBreakFast");
        this.host = host;
        this.bathrooms = Bathroom.getAllBathrooms(con,propertyID);
        this.bathingFacilities = new BathingFacilities(con,propertyID);
        this.utilityFacilities = new UtilityFacilities(con,propertyID);
        this.sleepingFacilities = new SleepingFacilities(con,propertyID);
        this.kitchenFacility = new KitchenFacility(con,propertyID);
        this.livingFacilities = new LivingFacilities(con,propertyID);
        this.outdoorFacilities = new OutdoorFacilities(con,propertyID);
        this.chargeBands = ChargeBand.getAllChargeBand(con,propertyID);
    }

    /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Property in the database when a new Property object is constructed and returns the id
     */
    private static String createPropertyGetID(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        String query = "INSERT INTO Property (addressID, hostID, shortName, description, generalLocation, hasBreakfast) VALUES (?, ?, ?, ?, ?, ?)";
        String key =  (String)dHelper.execSafeUpdateGetKey(query,objects);
        return key;
    }

        /*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Property in the database when a new Property object is constructed
     */
    private static int createProperty(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        String query = "INSERT INTO Property (addressID, hostID, shortName, description, generalLocation, hasBreakfast) VALUES (?, ?, ?, ?, ?, ?)";
        return dHelper.execSafeUpdate(query, objects);
    }

    /**
     * Returns a result set for a Prepared Statement
     * @param con Connection to the MySQL Database
     * @param preparedStatement
     * @return
     * @throws SQLException
     */
    private static ResultSet findProperty (Connection con, PreparedStatement preparedStatement) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
//        String query = "SELECT * FROM Property where propertyID=?";
//        ArrayList<Object> objects = new ArrayList<>(Arrays.asList(propertyID));
        return dHelper.execSafeQuery(preparedStatement);
    }

    /**
     * Creates a PreparedStatement ready for searching for a specific types of property in database
     * @param con Connection to the MySQL Database
     * @param prop
     * @param sleepF
     * @param bathingF
     * @param kitchenF
     * @param livingF
     * @param utilityF
     * @param outdoorF
     * @return
     */
    public static PreparedStatement searchProperty (Connection con, ArrayList<Object> prop, ArrayList<Object> sleepF, ArrayList<Object> bathingF, ArrayList<Object> kitchenF, ArrayList<Object> livingF, ArrayList<Object> utilityF, ArrayList<Object> outdoorF) {
        try {
            DriverHelper dHelper = new DriverHelper(con);
            String startString = "SELECT * " +
                    " FROM Property " +
                    " INNER JOIN SleepingFacilities SF ON Property.propertyID = SF.propertyID " +
                    " INNER JOIN BathingFacilities BF ON Property.propertyID = BF.propertyID " +
                    " INNER JOIN KitchenFacility KF ON Property.propertyID = KF.propertyID " +
                    " INNER JOIN LivingFacility LF on Property.propertyID = LF.propertyID " +
                    " INNER JOIN UtilityFacility UF on Property.propertyID = UF.propertyID " +
                    " INNER JOIN OutdoorFacility O on Property.propertyID = O.propertyID " +
                    " WHERE (shortName LIKE ? OR generalLocation LIKE ?) AND ( hasBreakfast = ? " ;
            if (!sleepF.isEmpty()) {
                String stringFS = "" ;
                for (Object amenity : sleepF) {
                    switch ((String) amenity) {
                        case "Bed Linen":
                            stringFS += " AND hasBedlinen = 1 " ;
                            break;

                        case "Towels":
                            stringFS += " AND hasTowels = 1 " ;
                            break;
                    }
                }
                startString += stringFS;
            }

            if (!bathingF.isEmpty()) {
                String stringFS = "" ;
                for (Object amenity : bathingF) {
                    switch ((String) amenity) {
                        case "Hair Dryer":
                            stringFS += " AND hasHairDryer = 1 " ;
                            break;
                        case "Shampoo":
                            stringFS += " AND hasShampoo = 1 " ;
                            break;
                        case "Toilet Paper":
                            stringFS += " AND hasToiletPaper = 1 " ;
                    }
                }
                startString += stringFS;
            }

            if (!kitchenF.isEmpty()) {
                String stringFS = "" ;
                for (Object amenity : kitchenF) {
                    switch ((String) amenity) {
                        case "Refrigerator":
                            stringFS += " AND hasRefrigerator = 1 " ;
                            break;
                        case "Microwave":
                            stringFS += " AND hasMicrowave = 1 " ;
                            break;
                        case "Oven":
                            stringFS += " AND hasOven = 1 " ;
                            break;
                        case "Stove":
                            stringFS += " AND hasStove = 1 " ;
                            break;
                        case "Dishwasher":
                            stringFS += " AND hasDishwasher = 1 " ;
                            break;
                        case "Tableware":
                            stringFS += " AND hasTableware = 1 " ;
                            break;
                        case "Cookware":
                            stringFS += " AND hasCookware = 1 " ;
                            break;
                        case "Basic Provisions":
                            stringFS += " AND hasBasicProvisions = 1 " ;
                            break;
                    }
                }
                startString += stringFS;
            }

            if (!livingF.isEmpty()) {
                String stringFS = "" ;
                for (Object amenity : livingF) {
                    switch ((String) amenity) {
                        case "WiFi":
                            stringFS += " AND hasWifi = 1 " ;
                            break;
                        case "Television":
                            stringFS += " AND hasTelevision = 1 " ;
                            break;
                        case "Satellite":
                            stringFS += " AND hasSatellite = 1 " ;
                            break;
                        case "Streaming":
                            stringFS += " AND hasStreaming = 1 " ;
                            break;
                        case "DVD Player":
                            stringFS += " AND hasDVDPlayer = 1 " ;
                            break;
                        case "Boardgames":
                            stringFS += " AND hasBoardGames = 1 " ;
                            break;
                    }
                }
                startString += stringFS;
            }

            if (!utilityF.isEmpty()) {
                String stringFS = "" ;
                for (Object amenity : utilityF) {
                    switch ((String) amenity) {
                        case "Central Heating":
                            stringFS += " AND hasCentralHeating = 1 " ;
                            break;
                        case "Washing Machine":
                            stringFS += " AND hasWashingMachine = 1 " ;
                            break;
                        case "Drying Machine":
                            stringFS += " AND hasDryingMachine = 1 " ;
                            break;
                        case "Fire Extinguisher":
                            stringFS += " AND hasFireExtinguisher = 1 " ;
                            break;
                        case "Smoke Alarm":
                            stringFS += " AND hasSmokeAlarm = 1 " ;
                            break;
                        case "First Aid Kit":
                            stringFS += " AND hasFirstAidKit = 1 " ;
                            break;
                    }
                }
                startString += stringFS;
            }

            if (!outdoorF.isEmpty()) {
                String stringFS = "" ;
                for (Object amenity : outdoorF) {
                    switch ((String) amenity) {
                        case "On Site Parking":
                            stringFS += " AND hasOnSiteParking = 1 " ;
                            break;
                        case "On Road Parking":
                            stringFS += " AND hasOnRoadParking = 1 " ;
                            break;
                        case "Paid Carpark":
                            stringFS += " AND hasPaidCarPark = 1 " ;
                            break;
                        case "Patio":
                            stringFS += " AND hasPatio = 1 " ;
                            break;
                        case "Barbeque":
                            stringFS += " AND hasBarbeque = 1 " ;
                            break;
                    }
                }
                startString += stringFS;

            }
            startString += " ) LIMIT 10";
            PreparedStatement preparedStatement = dHelper.safeStatement(startString, prop);
            return preparedStatement;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Downloads all reviews for a specific property
     * @param con Connection to the MySQL Database
     * @return
     */
    public ArrayList<Review> getAllReviews (Connection con){
        ArrayList<Review> reviewArrayList = new ArrayList<>();
        try {
            DriverHelper driverHelper = new DriverHelper(con);
            String query = "SELECT * FROM Booking, Review WHERE Booking.bookingID = Review.bookingID "
                    + "AND propertyID = ? AND hostAgree = 1;";
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(getPropertyID()));
            PreparedStatement preparedStatement = driverHelper.safeStatement(query, queryParameters);
            ResultSet rs = driverHelper.execSafeQuery(preparedStatement);
            while (rs.next()){
                reviewArrayList.add(new Review(con, rs.getInt("bookingID")));
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return reviewArrayList;
    }








}
