package com.ctb.tms.rdb;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;

public interface OASRDBSink {
	public Connection getOASConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException;
	
	public void putItemResponse(Connection conn, ItemResponseData ird) throws NumberFormatException, SQLException;

	public void putManifest(Connection conn, String testRosterId, Manifest[] manifest) throws Exception;
	
	public void putActiveRosters(Connection con, StudentCredentials [] credsA);
	
    public void putRosterData(Connection conn, StudentCredentials creds, RosterData rosterData) throws Exception;

    public void shutdown();
}
