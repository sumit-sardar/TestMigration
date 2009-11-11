package com.ctb.lexington.db.oracle;

import java.sql.SQLException;


public class OASOracleConnectionFactory extends OracleConnectionFactory {
	private final static String DEFAULT_PASSWORD = "oas";
	private final static String DEFAULT_USERID = "oas";

	
	public OASOracleConnectionFactory(String url, String username, String password)
	throws SQLException {
		super(url, username, password);
	}
}

