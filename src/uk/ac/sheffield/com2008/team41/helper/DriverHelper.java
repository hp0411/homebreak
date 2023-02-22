package uk.ac.sheffield.com2008.team41.helper;
//For Generic Database Features
import java.sql.*;
import java.sql.Date;
//For Advanced Database Features
import java.util.*;

/**
 * A Class which Handles SQL Injection Protection and has extra methods
 * Credit: Team 41 Homebreak submission 2021
 */
public class DriverHelper{
	private Connection con;

	/***
	 * Returns the MySQL Connection
	 * @return
	 */
	public Connection getCon() {
		return con;
	}

	/**
	 * Creates a DriverHelper with a Connection to the Database
	 * @param con
	 */
	public DriverHelper(Connection con){
		this.con = con;
	}

	//@SuppressWarning("all")

	/**
	 * Executes a prepared statement query
	 * @param safeStatement
	 * @return
	 * @throws SQLException
	 */
	public ResultSet execSafeQuery(PreparedStatement safeStatement) throws SQLException{
		try {
			return safeStatement.executeQuery();
		}
		catch(SQLException e){
			e.printStackTrace();
			throw new SQLException("SQL Parameter Conversion Error for query: " + e.getMessage());
		}
	}

	/**
	 * Executes a prepared statement update
	 * @param query
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public int execSafeUpdate(String query, ArrayList<Object> parameters) throws SQLException{
		try (PreparedStatement safeStatement = safeStatement(query, parameters)){
			return safeStatement.executeUpdate();
		}
		catch(SQLException e){
			throw new SQLException("SQL Parameter Conversion Error for query: " + e.getMessage());
		}
	}


	/**
	 * Executes a prepared statement update amd returns the key created/updated
	 * @param query
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public Object execSafeUpdateGetKey(String query, ArrayList<Object> parameters) throws SQLException{
		try {
			PreparedStatement safeStatement = safeStatement(con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS), parameters);
			safeStatement.execute();
			ResultSet rs = safeStatement.getGeneratedKeys();
			String key = rs.next() ? rs.getString(1) : null;
			rs.close();
			safeStatement.close();
			return key;
		}
		catch(SQLException e){
			throw new SQLException("SQL Parameter Conversion Error for query: " + e.getMessage());
		}
	}

	/**
	 * Creates a safe statement, using a prior existing prepared statement.
	 * Typically, used in getting generated keys.
	 * @param pstmt
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement safeStatement(PreparedStatement pstmt,ArrayList<Object> parameters) throws SQLException {
		try {
			for (int i = 1; i < (parameters.size() + 1); i++){
				Object parameter = parameters.get(i-1);
				if (parameter instanceof String){
					pstmt.setString(i, (String) parameter);
				}
				else if (parameter instanceof Boolean){
					pstmt.setBoolean(i,(Boolean) parameter);
				}
				else if (parameter instanceof Integer){
					pstmt.setInt(i, (Integer)( parameter));
				}
				else if (parameter instanceof Float){
					pstmt.setFloat(i, (Float) (parameter));
				}
				else if (parameter instanceof Double){
					pstmt.setDouble(i, (Double) parameter);
				}
				else if (parameter instanceof Timestamp){
					pstmt.setTimestamp(i, (Timestamp) parameter);
				}
				else if (parameter instanceof Time){
					pstmt.setTime(i, (Time) parameter);
				}
				else if (parameter instanceof Date){
					pstmt.setDate(i, (Date) parameter);
				}
				else{
					pstmt.setObject(i, parameter);
				}
			}
			return pstmt;
		}
		catch(SQLException e){
			e.printStackTrace();
			throw new SQLException("SQL Parameter Conversion Error for query: " + e.getMessage());
		}
	}
	/**
	 * Creates a safe statement
	 * @param query
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement safeStatement(String query, ArrayList<Object> parameters) throws SQLException {
		PreparedStatement pstmt = this.con.prepareStatement(query);
		return safeStatement(pstmt,parameters);
	}
}