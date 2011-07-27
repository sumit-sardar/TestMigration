package com.ctb.tms.rdb;

import java.sql.Connection;
import java.sql.SQLException;

public interface ADSRDBSink {
	public Connection getADSConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException;
	
	public void putSubtest(Connection conn, int subtestId, String subtest);
	
	public void putItem(Connection conn, int itemId, String item);

	public void shutdown();
}
