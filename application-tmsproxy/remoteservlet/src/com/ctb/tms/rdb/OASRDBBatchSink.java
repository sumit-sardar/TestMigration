package com.ctb.tms.rdb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;

public interface OASRDBBatchSink {
	public Connection getOASConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException;
	
	public PreparedStatement getStoreResponseStatement(Connection conn) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException;

	public PreparedStatement getDeleteCRResponseStatement(Connection conn) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException;
		
	public PreparedStatement getStoreCRResponseStatement(Connection conn) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException;

	public void putItemResponse(Connection conn, PreparedStatement storeResponseStatement, PreparedStatement deleteCRResponseStatement, PreparedStatement storeCRResponseStatement, ItemResponseData ird) throws NumberFormatException, SQLException, IOException, ClassNotFoundException;

	public void putManifest(Connection conn, String testRosterId, Manifest[] manifest) throws Exception;
	
	public void putActiveRosters(Connection con, StudentCredentials [] credsA);
	
    public void putRosterData(Connection conn, StudentCredentials creds, RosterData rosterData) throws Exception;

    public void shutdown();
}
