package com.ctb.lexington.db.oracle;

import java.sql.SQLException;


public class StageOracleConnectionFactory extends OracleConnectionFactory {
	private final static String DEFAULT_PASSWORD = "stage";
	private final static String DEFAULT_USERID = "stage";
		
	public StageOracleConnectionFactory(
		String url,
		String username,
		String password)
		throws SQLException {
		super(url, username, password);
	}

}
