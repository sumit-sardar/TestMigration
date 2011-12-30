package com.ctb.tms.rdb; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;

public interface OASRDBSource
{ 
	public void markActiveRosters(Connection con, String clusterName, int nodeid);
	
	public StudentCredentials[] getActiveRosters(Connection con, String clusterName, int nodeid);
	
	public void sweepActiveRosters(Connection con, String clusterName, int nodeid);
    
    public RosterData getRosterData(Connection conn, String key) throws Exception;

    public Manifest[] getManifest(Connection conn, String testRosterId) throws Exception;
    
    public Connection getOASConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException;
    
    public void shutdown();
} 
