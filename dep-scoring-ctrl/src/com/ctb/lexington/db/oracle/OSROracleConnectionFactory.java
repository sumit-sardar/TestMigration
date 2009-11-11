package com.ctb.lexington.db.oracle;

import java.sql.SQLException;


public class OSROracleConnectionFactory extends OracleConnectionFactory{

	private final static String DEFAULT_PASSWORD = "osr";
	private final static String DEFAULT_USERID = "osr";
	

	public OSROracleConnectionFactory(String dbUrl, String userId, String password) throws SQLException {
		super(dbUrl, userId, password);
	}
	
}
