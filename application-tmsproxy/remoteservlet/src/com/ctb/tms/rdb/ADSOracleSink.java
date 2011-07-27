package com.ctb.tms.rdb;

import java.sql.Connection;
import java.sql.SQLException;

public class ADSOracleSink implements ADSRDBSink {
	public Connection getADSConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// do nothing
		return null;
	}
	
	public void putSubtest(Connection conn, int subtestId, String subtest) {
		// do nothing
	}
	
	public void putItem(Connection conn, int itemId, String item) {
		// do nothing
	}

	public void shutdown() {
		// do nothing
	}
}
