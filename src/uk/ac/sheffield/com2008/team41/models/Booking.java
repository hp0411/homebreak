package uk.ac.sheffield.com2008.team41.models;

import uk.ac.sheffield.com2008.team41.helper.DriverHelper;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * A model containing all the necessary information about a booking including mutators and accessors.
 * Credit: Team 41 Homebreak submission 2021
 */
public class Booking {
    //private attributes to be accessed by only applicable getters and setters
    private int bookingID;
    private int guestID;
    private int propertyID;
    private Date startDate;
    private Date endDate;
    private boolean pending;
    private boolean hostAgree;

    /*
    @return returns the ID for this booking
     */
    public int getBookingID() {
        return bookingID;
    }

    /*
    @param bookingID Variable to replace current bookingID
    Replaces bookingID just in the object, not the database, because it is a primary key
     */
    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    /*
    @return Returns the ID of the guest making the booking
     */
    public int getGuestID() {
        return guestID;
    }

    /*
    @return Returns the ID of the property being booked
     */
    public int getPropertyID() {
        return propertyID;
    }

    /*
    @return Returns the date of the start of the booking
     */
    public Date getStartDate() {
        return startDate;
    }

    /*
    @return Returns the date on which the booking will end
     */
    public Date getEndDate() {
        return endDate;
    }

    public double getPrice(Connection con){
        ChargeBand band = ChargeBand.findCharge(con,propertyID,startDate,endDate);
        long Days = DAYS.between(startDate.toLocalDate(),endDate.toLocalDate());
        double price = band.getPricePerNight()*Days;
        price += band.getCleaningCharge();
        price += band.getServiceCharge();
        return price;
    }


    /*
    @return Returns whether or not the booking is pending
     */
    public boolean pending() {
        return pending;
    }

    /*
    @param con Connection to the database
    @param pending Variable to replace current booking's pending state
    Replaces booking's pending state in object and also in the database
     */
    public void setPending(Connection con, boolean pending) {
        this.pending = pending;

        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("pending", pending, this.bookingID));
        try {
            if (updateInformation(con, queryParameters) >= 0) {

            } else {
                throw new SQLException("Could not update information in booking, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @return Returns whether or not the host has agreed to the booking
     */
    public boolean hostAgree() {
        return hostAgree;
    }

    /*
    @param con Connection to the database
    @param hostAgree Variable to replace current booking's hostAgree state
    Replaces booking's hostAgree state in object and also in the database
     */
    public void setHostAgree(Connection con, boolean hostAgree) {
        this.hostAgree = hostAgree;

        ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList("hostAgree", hostAgree, this.bookingID));
        try {
            if (updateInformation(con, queryParameters) >= 0) {

            } else {
                throw new SQLException("Could not update information in booking, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a booking and then uploads it to the database
     * @param con Connection to the MySQL Database
     * @param guestID
     * @param propertyID
     * @param startDate
     * @param endDate
     * @param pending
     * @param hostAgree
     */
    public Booking(Connection con, int guestID, int propertyID, Date startDate, Date endDate, boolean pending, boolean hostAgree) {
        this.guestID = guestID;
        this.propertyID = propertyID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pending = pending;
        this.hostAgree = hostAgree;
        try {
            ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(null, guestID, propertyID, startDate, endDate, pending, hostAgree));
            if (createBooking(con, queryParameters) >= 0) {

            } else {
                throw new SQLException("Could not add this booking to database, Error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads a booking from the database
     * @param con Connection to the MySQL Database
     * @param bookingID
     */
    public Booking(Connection con, int bookingID) {
        try {
            DriverHelper dHelper = new DriverHelper(con);
            String query = "Select * FROM Booking WHERE bookingID = ?";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(bookingID));
            PreparedStatement preparedStatement = dHelper.safeStatement(query, objects);
            ResultSet rs = dHelper.execSafeQuery(preparedStatement);
            rs.next();
            this.bookingID = bookingID;
            this.guestID = rs.getInt("guestID");
            this.propertyID = rs.getInt("propertyID");
            this.startDate = rs.getDate("startDate");
            this.endDate = rs.getDate("endDate");
            this.pending = rs.getBoolean("pending");
            this.hostAgree = rs.getBoolean("hostAgree");

            rs.close();
            preparedStatement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Gets all bookings from specific host
     * @param con Connection to the MySQL Database
     * @param hostID
     * @return
     */
    public static ArrayList<Booking> getBookingsHost(Connection con, int hostID) {
        ArrayList<Booking> bookings = new ArrayList<>();
        try {
            DriverHelper dHelper = new DriverHelper(con);
            String query = "SELECT * " +
                    "    From Booking " +
                    "    INNER JOIN Property P on Booking.propertyID = P.propertyID " +
                    "    INNER JOIN Host H on P.hostID = H.hostID " +
                    "    WHERE H.hostID = ? AND pending != 0 ORDER BY hostAgree DESC";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(hostID));

            PreparedStatement preparedStatement = dHelper.safeStatement(query, objects);
            ResultSet rs = dHelper.execSafeQuery(preparedStatement);

            while (rs.next()) {
                bookings.add(new Booking(con, rs.getInt("bookingID")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return bookings;
    }

    /**
     * Checks if a booking is valid and doesn;'t collide with any others
     * @param con Connection to the MySQL Database
     * @return
     */
    public boolean validBooking(Connection con) {
        boolean valid = true;
        try {
            DriverHelper dHelper = new DriverHelper(con);
            String query1 = "Select * FROM Booking WHERE hostAgree = 1 AND bookingID != ? AND propertyID = ? AND " +
                    " startDate < ? AND endDate > ? ";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(getBookingID(), getPropertyID(), getStartDate(), getStartDate()));
            PreparedStatement preparedStatement = dHelper.safeStatement(query1, objects);
            ResultSet rs = dHelper.execSafeQuery(preparedStatement);

            if (rs.next()) {
                valid = false;
            }


            String query2 = "Select * FROM Booking WHERE hostAgree = 1 AND bookingID != ? AND propertyID = ? AND " +
                    "  startDate < ? AND endDate > ? ";

            objects = new ArrayList<>(Arrays.asList(getBookingID(), getPropertyID(), getEndDate(), getEndDate()));
            preparedStatement = dHelper.safeStatement(query2, objects);

            rs = dHelper.execSafeQuery(preparedStatement);

            if (rs.next()) {
                valid = false;
            }

            rs.close();
            preparedStatement.close();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return valid;
    }

    /**
     * Gets all bookings from a specific guest
     * @param con Connection to the MySQL Database
     * @param guestID
     * @return
     */
    public static ArrayList<Booking> getBookingsGuest(Connection con, int guestID) {
        ArrayList<Booking> bookings = new ArrayList<>();
        try {
            DriverHelper dHelper = new DriverHelper(con);
            String query = "SELECT * FROM Booking WHERE guestID = ?";
            ArrayList<Object> objects = new ArrayList<>(Arrays.asList(guestID));

            PreparedStatement preparedStatement = dHelper.safeStatement(query, objects);
            ResultSet rs = dHelper.execSafeQuery(preparedStatement);

            while (rs.next()) {
                bookings.add(new Booking(con, rs.getInt("bookingID")));
            }

            rs.close();
            preparedStatement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return bookings;
    }

    /*
    @param con Connection to the database
    @param queryParameters ArrayList containing parameters to be inserted into an SQL query
    Creates a Booking in the database when a new Booking object is constructed
     */
    private int createBooking(Connection con, ArrayList<Object> queryParameters) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        String query = "INSERT INTO Booking VALUES (?, ?, ?, ?, ?, ?, ?)";
        return dHelper.execSafeUpdate(query, queryParameters);
    }

    /*
   @param con Connection to the database
   @param objects ArrayList containing parameters to be inserted into an SQL query
   Updates the given variable in the database
    */
    private static int updateInformation(Connection con, ArrayList<Object> objects) throws SQLException {
        DriverHelper dHelper = new DriverHelper(con);
        String query = "update Booking set " + objects.get(0) + " = ? where bookingID = ?";
        objects.remove(0);
        return dHelper.execSafeUpdate(query, objects);
    }

    @Override
    public String toString() {
        return "Booking {" +
                "bookingID = " + bookingID +
                ", guestID = " + guestID +
                ", propertyID = " + propertyID +
                ", startDate = " + startDate +
                ", endDate = " + endDate +
                ", pending = " + pending +
                ", hostAgree = " + hostAgree +
                '}';
    }
}
