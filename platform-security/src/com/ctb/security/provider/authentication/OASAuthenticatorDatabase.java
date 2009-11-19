package com.ctb.security.provider.authentication;

/**
 *
 * @author John Wang
 *
 */

import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.security.MessageDigest;
import weblogic.logging.NonCatalogLogger;
import weblogic.management.utils.NotFoundException;

public final class OASAuthenticatorDatabase {

	private String databaseURL = "jdbc:oracle:thin:@168.116.31.32:1521:toast";

	private String databaseUser = "oas";

	private String databaseUserPassword = "oas";

	private String databaseJDBCDriver = "oracle.jdbc.driver.OracleDriver";

	/**
	 * Constructor for the OASAuthenticatorDatabase object.
	 */
	public OASAuthenticatorDatabase(String jdbcDriver, String databaseURL,
			String dbUser, String dbPassword) {
		this.databaseJDBCDriver = jdbcDriver;
		this.databaseURL = databaseURL;
		this.databaseUser = dbUser;
		this.databaseUserPassword = dbPassword;
	}

	// Return a JDBC connection
	/**
	 * Returns the Connection property of the OASAuthenticatorDatabase.
	 *
	 * @return The Connection value.
	 * @exception SQLException
	 *                if the <description>.
	 */
	protected Connection getConnection() throws Exception {

		Connection newConn = null;

		try {
			Properties props = new Properties();
			props.put("user", databaseUser);
			props.put("password", databaseUserPassword);
			Driver driver = (Driver) Class.forName(databaseJDBCDriver)
					.newInstance();
			newConn = driver.connect(databaseURL, props);
		}

		catch (Exception e) {
			OASAuthLogger.getLogger().error("Failed to get connection: " + e, e);
			throw new Exception();
		}
		return newConn;
	}

	/**
	 * Close the connection
	 *
	 * @param con
	 *            The connection to be closed
	 */
	protected void returnConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
				con = null;
			}
		} catch (SQLException e) {
			OASAuthLogger.getLogger().error("Error while closing connection: " + e, e);
		}
	}

	/**
	 * Moved the authentication here to make it easier to accomodate encrypted
	 * passwords. Encrypting of passwords is an implementation issue at the
	 * storage level, so a code change in the login module class should not be
	 * required.
	 *
	 * @param user
	 *            Description of Parameter.
	 * @param password
	 *            Description of Parameter.
	 * @return True, if the user was successfully authenticated
	 * @exception NotFoundException
	 *                if the <description>.
	 */
	public synchronized boolean authenticate(String user, String password)
			throws NotFoundException {

		boolean same = false;
		Connection con = null;

		try {

			// Get a connection
			con = getConnection();

			String stored = getStoredPassword(con, user);

			String plain = password;

			String encoded = encodePassword(password);

			OASAuthLogger.getLogger().debug("\tplain password: " + plain);
			OASAuthLogger.getLogger().debug("\tencoded password: " + encoded);
			OASAuthLogger.getLogger().debug("\tstored password: " + stored);

			// Compare the stored password with the supplied password
			same =
					stored.equals(encoded) ||
					stored.equals(plain);

			// Return the connection
			returnConnection(con);

		} catch (Exception e) {
			OASAuthLogger.getLogger().error("Error authenticating user: " + e, e);

		} finally {
			returnConnection(con);
		}

		OASAuthLogger.getLogger().debug("Authenticating user '" + user + "' => "
				+ (same ? "success" : "failure"));

		return same;
	}

	// used for populating the subject at runtime
	// returns an empty list if the user doesn't exist.
	/**
	 * Returns the UserGroups property of the OASAuthenticatorDatabase.
	 *
	 * @param user
	 *            Description of Parameter.
	 * @return The UserGroups value.
	 */
	public synchronized Enumeration getUserGroups(String user) {
		checkVal(user);
		Vector v = new Vector();
		try {
			// Get a database connection
			Connection con = getConnection();

			// Retrieve a listing of all parent groups for the user
			PreparedStatement stmt = con
					.prepareStatement("SELECT r.role_name "
							+ "FROM USERS u, USER_ROLE ur, ROLE r " 
							+ "WHERE u.user_id = ur.user_id "
							+ "AND ur.role_id = r.role_id "
							+ "AND u.activation_status = 'AC' "
							+ "AND ur.activation_status = 'AC' "
							+ "AND u.user_name = ? ");
			stmt.setString(1, user);
			ResultSet rs = stmt.executeQuery();

			// Add all these parent groups to a vector
			while (rs.next()) {
				v.add(rs.getString(1));
			}
			if (rs != null) {
				rs.close();
			}
			stmt.close();
			// Return the connection
			returnConnection(con);

		} catch (SQLException e) {
			OASAuthLogger.getLogger().error("Error accessing database: " + e);
		} catch (Exception ex) {
			OASAuthLogger.getLogger().error("Error accessing database: " + ex);
		}

		// Return an enumeration for the vector of groups
		return v.elements();
	}

	/**
	 * Determine if a specific user exists in the database
	 *
	 * @param user
	 *            The name of the user to check for
	 * @return True, if the user exists
	 */
	public synchronized boolean userExists(String user) {
		OASAuthLogger.getLogger().debug("Checking for existance of user '" + user + "'.");

		Connection con = null;
		boolean exists = false;

		try {

			// Get a database connection
			con = getConnection();

			// Prepare the neccessary statements in order to check if the user
			// exists
			PreparedStatement usersStmt = con
					.prepareStatement("SELECT PASSWORD FROM USERS WHERE user_name = ? AND activation_status = 'AC' ");

			// Query for a user by the specified name
			usersStmt.setString(1, user);
			ResultSet rs = usersStmt.executeQuery();

			// Store the result of the record set (whether we found users or
			// not)

			exists = rs.next();

			// Clean everything up
			rs.close();
			usersStmt.close();

		} catch (SQLException e) {
			OASAuthLogger.getLogger().error("Error checking for user existance: " + e, e);

		} catch (Exception ex) {
			OASAuthLogger.getLogger().error("Error checking for user existance: " + ex, ex);

		} finally {
			// Return the database
			returnConnection(con);
		}

		// Return the saved result
		return exists;
	}

	/**
	 * Returns the password stored in the database for the specified user. This
	 * may be an encrypted value, dependent on your implementation.
	 *
	 * @param user
	 *            The user to retrieve a password for.
	 * @param con
	 *            Description of Parameter.
	 * @return The user's password
	 * @exception NotFoundException
	 *                if the user was not found
	 * @exception SQLException
	 *                if the <description>.
	 */
	protected synchronized String getStoredPassword(Connection con, String user)
			throws NotFoundException, SQLException {
		// Prepare the neccessary statements in order to check if the user
		// exists
		PreparedStatement usersStmt = con
				.prepareStatement("SELECT PASSWORD FROM USERS WHERE user_name = ? AND activation_status = 'AC' ");

		// Query for a user by the specified name
		usersStmt.setString(1, user);
		ResultSet rs = usersStmt.executeQuery();

		// Move the record set to the first element and check whether we found
		// the user
		boolean found = rs.next();

		if (!found) {
			// Throw an exception, as we couldn't find the user
			throw new NotFoundException("User '" + user + "' not found.");
		}

		String password = null;

		try {
			// Retrieve the user's password
			password = rs.getString(1);
		} catch (SQLException e) {
			OASAuthLogger.getLogger().error("Unable to access database: " + e, e);
		} catch (Exception ex) {
			OASAuthLogger.getLogger().error("Unable to access database: " + ex, ex);
		}

		// Clean everything up
		rs.close();
		usersStmt.close();

		// Return the saved result
		return password;
	}

	/**
	 * Check that the specified value is valid.
	 *
	 * @param val
	 *            The value to be checked
	 */
	private void checkVal(String val) {
		if (val == null) {
			throw new IllegalArgumentException("value must not be null");
		}
		if (val.indexOf(",") >= 0) {
			throw new IllegalArgumentException(
					"value must not contain \",\":\"" + val + "\"");
		}
		if (val.length() != val.trim().length()) {
			throw new IllegalArgumentException(
					"value must not begin or end with whitespace:\"" + val
							+ "\"");
		}
		if (val.length() == 0) {
			throw new IllegalArgumentException("value must not be empty");
		}
	}

	/**
	 * Used to encode passwords using the SHA-1 message digest algorithm.
	 *
	 * @param password
	 *            The <code>String</code> to get the hash value of.
	 * @return A <code>String</code> that is the SHA-1 message digest of the
	 *         password parameter.
	 */
	public String encodePassword(String password) {
		MessageDigest md;
		StringBuffer retval = new StringBuffer("");
		byte[] hash = new byte[] {};
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			hash = md.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < hash.length; ++i) {
			// retval.append( Integer.toHexString( hash[i] ) );
			if (((int) hash[i] & 0xff) < 0x10) {
				retval.append("0");
			}
			retval.append(Long.toString((int) hash[i] & 0xff, 16));

		}

		return retval.toString();
	}

}
