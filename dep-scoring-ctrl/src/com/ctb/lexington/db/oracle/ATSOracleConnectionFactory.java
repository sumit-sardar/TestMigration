package com.ctb.lexington.db.oracle;

import java.sql.SQLException;



public class ATSOracleConnectionFactory	extends OracleConnectionFactory{
	private final static String DEFAULT_PASSWORD = "ats";
	private final static String DEFAULT_USERID = "ats";
	

	public ATSOracleConnectionFactory(String dbUrl, String userId, String password) throws SQLException {
		super(dbUrl, userId, password);
	}
	
}
