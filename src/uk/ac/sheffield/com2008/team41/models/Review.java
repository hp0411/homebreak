package uk.ac.sheffield.com2008.team41.models;

import uk.ac.sheffield.com2008.team41.helper.DriverHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * A model containing all the necessary information about a booking's review including mutators and accessors.
 * Credit: Team 41 Homebreak submission 2021
 */
public class Review {
	//private attributes to be accessed by only applicable getters
	private int bookingID;
	private String description;
	private int cleanlinessScore;
	private int communicationScore;
	private int checkInScore;
	private int accuracyScore;
	private int locationScore;
	private int valueScore;

	/*
	@return The ID of the booking the review is for
	 */
	public int getBookingID() {
		return bookingID;
	}

	/*
	@return The description given in the review
	 */
	public String getDescription() {
		return description;
	}

	/*
	@return The score for cleanliness
	 */
	public int getCleanlinessScore() {
		return cleanlinessScore;
	}

	/*
	@return The score for communication
	 */
	public int getCommunicationScore() {
		return communicationScore;
	}

	/*
	@return The score for check in
	 */
	public int getCheckInScore() {
		return checkInScore;
	}

	/*
	@return The score for accuracy
	 */
	public int getAccuracyScore() {
		return accuracyScore;
	}

	/*
	@return The score for location
	 */
	public int getLocationScore() {
		return locationScore;
	}

	/*
	@return The score for value
	 */
	public int getValueScore() {
		return valueScore;
	}

	/**
	 * Creates a review and uploads it to the database
	 * @param con Connection to the MySQL Database
	 * @param bookingID
	 * @param description
	 * @param cleanlinessScore
	 * @param communicationScore
	 * @param checkInScore
	 * @param accuracyScore
	 * @param locationScore
	 * @param valueScore
	 */
	public Review (Connection con, int bookingID, String description, int cleanlinessScore, int communicationScore, 
			int checkInScore, int accuracyScore, int locationScore, int valueScore) {
		try {
			this.description = description;
			this.cleanlinessScore = cleanlinessScore;
			this.communicationScore = communicationScore;
			this.checkInScore = checkInScore;
			this.accuracyScore = accuracyScore;
			this.locationScore = locationScore;
			this.valueScore = valueScore;
		    ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(bookingID, description, cleanlinessScore, communicationScore, 
		    		checkInScore, accuracyScore, locationScore, valueScore));
            if (createReview(con, queryParameters) >= 0){

            }
            else{
                throw new SQLException("Could not add Review to database, Error occurred");
            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		
	}

	/*
    @param con Connection to the database
    @param objects ArrayList containing parameters to be inserted into an SQL query
    Creates a Review in the database when a new Review object is constructed
     */
	private static int createReview(Connection con, ArrayList<Object> objects) throws SQLException {
	        DriverHelper dhelper = new DriverHelper(con);
	        String query = "INSERT INTO Review VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	        return dhelper.execSafeUpdate(query, objects);
    }

	/**
	 * Downloads a specific review from the database
	 * @param con Connection to the MySQL Database
	 * @param bookingID
	 */
	public Review (Connection con, int bookingID){
		try {
			DriverHelper driverHelper = new DriverHelper(con);
			String query = "SELECT * FROM Booking, Review WHERE Booking.bookingID = Review.bookingID "
					+ "AND Booking.bookingID = ? AND hostAgree = 1;";
			ArrayList<Object> objects = new ArrayList<>(Arrays.asList(bookingID));

			PreparedStatement preparedStatement = driverHelper.safeStatement(query, objects);
			ResultSet rs = driverHelper.execSafeQuery(preparedStatement);

			if (rs.next()){
				this.bookingID = bookingID;
				this.description = rs.getString("description");
				this.cleanlinessScore = rs.getInt("cleanlinessScore");
				this.communicationScore = rs.getInt("communicationScore");
				this.checkInScore = rs.getInt("checkInScore");
				this.accuracyScore = rs.getInt("accuracyScore");
				this.locationScore = rs.getInt("locationScore");
				this.valueScore = rs.getInt("valueScore");
			}
			rs.close();
			preparedStatement.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

	}
	
	/**
	 * Calculate the average scores for each category
	 * @param con Connection to the database
	 * @param propertyID The property to calculate average scores
	 * @return a list of average scores for each category for a property
	 */
	public static HashMap <String, Double> calAvgScores(Connection con, int propertyID) {
		HashMap <String, Double> avgScores = new LinkedHashMap<String, Double>();
		try {
			DriverHelper driverHelper = new DriverHelper(con);
			String query = "SELECT AVG(cleanlinessScore), AVG(communicationScore), "
					+ "AVG(checkInScore), AVG(accuracyScore), AVG(locationScore), "
					+ "AVG(valueScore) FROM Booking, Review WHERE Booking.bookingID = Review.bookingID "
					+ "AND propertyID = ? AND hostAgree = 1;";
			ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(propertyID));
			PreparedStatement preparedStatement = driverHelper.safeStatement(query, queryParameters);
			ResultSet res = driverHelper.execSafeQuery(preparedStatement);
			if (res.next()) {
				double avg1 = res.getDouble(1);
				avgScores.put("Cleaniness", avg1);
				double avg2 = res.getDouble(2);
				avgScores.put("Communication", avg2);
				double avg3 = res.getDouble(3);
				avgScores.put("Check-in", avg3);
				double avg4 = res.getDouble(4);
				avgScores.put("Accuracy of public information", avg4);
				double avg5 = res.getDouble(5);
				avgScores.put("Location", avg5);
				double avg6 = res.getDouble(6);
				avgScores.put("Value for money", avg6);
			}
			res.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return avgScores;
	}
	
	
	public static double calcPropRating (Connection con, int propertyID) {
		double propRating;
		double sum = 0;
		HashMap <String, Double> scores = calAvgScores(con, propertyID);
		for (double score: scores.values()) {
		     sum += score;     
		}
		propRating = sum/6;
		if (scores.size() == 0){
			return 10;
		}
		return Math.round(propRating*10.0)/10.0;
	}
	
	/**
	 * Calculate the host rating from reviews of all properties
	 * @param con Connection to the database
	 * @param hostID The host to calculate rating for
	 * @return the average rating of host's properties
	 */
    public static double calcHostRating (Connection con, int hostID) {
		double avgRating = 0;
		try {
			DriverHelper driverHelper = new DriverHelper(con);
			String query = "SELECT AVG(cleanlinessScore), AVG(communicationScore), AVG(checkInScore), AVG(accuracyScore), "
					+ "AVG(locationScore), AVG(valueScore) "
					+ "FROM ((Booking INNER JOIN Property ON Booking.propertyID = Property.propertyID and hostID = ? and hostAgree = TRUE) "
					+ "INNER JOIN Review ON Review.bookingID = Booking.bookingID);";
			ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(hostID));
			PreparedStatement preparedStatement = driverHelper.safeStatement(query, queryParameters);
			ResultSet res = driverHelper.execSafeQuery(preparedStatement);
			if (res.next()) {
				double avg1 = res.getDouble(1);
				double avg2 = res.getDouble(2);
				double avg3 = res.getDouble(3);
				double avg4 = res.getDouble(4);
				double avg5 = res.getDouble(5);
				double avg6 = res.getDouble(6);
				avgRating = (avg1 + avg2 + avg3 + avg4 + avg5 + avg6)/6;
				return Math.round(avgRating*10.0)/10.0;
				}
			res.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();;
		}
		return avgRating;
	}

	/**
	 * Checks if there is a review for a specific booking in the database
	 * @param con Connection to the MySQL Database
	 * @param bookingID
	 * @return
	 */
	public static boolean findReview(Connection con, int bookingID){
		boolean findReview = false;
		try {
			DriverHelper driverHelper = new DriverHelper(con);
			String query = "Select * FROM Review WHERE bookingID = ?";
			ArrayList<Object> queryParameters = new ArrayList<>(Arrays.asList(bookingID));

			PreparedStatement preparedStatement = driverHelper.safeStatement(query, queryParameters);
			ResultSet rs = driverHelper.execSafeQuery(preparedStatement);

			if (rs.next()){
				findReview = true;
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return findReview;
	}
	
	@Override
	 public String toString() {
		 return "Review {" +
				 "bookingID = " + bookingID +
				 ", description = " + description +
				 ", cleanlinessScore = " + cleanlinessScore +
				 ", communicationScore = " + communicationScore +
				 ", checkInScore = " + checkInScore +
				 ", accuracyScore = " + accuracyScore +
				 ", locationScore = " + locationScore +
				  ", valueScore = " + valueScore +
				 '}';
	 	}
}
