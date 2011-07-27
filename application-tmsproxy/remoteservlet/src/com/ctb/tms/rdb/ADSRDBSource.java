package com.ctb.tms.rdb;

import java.sql.Connection;
import java.sql.SQLException;

public interface ADSRDBSource {
	public Connection getADSConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException;
	
	public String getSubtest(Connection conn, int subtestId, String hash);
	
	public String getItem(Connection conn, int itemId, String hash);
	
	public void shutdown();
}
