package com.ctb.tms.rdb; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;

public interface OASRDBSource
{ 
	public StudentCredentials [] getActiveRosters(Connection con);
	
    public RosterData getRosterData(Connection conn, StudentCredentials creds)  throws Exception;
    
    public RosterData getRosterData(Connection conn, String key) throws Exception;

    public Connection getOASConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException;
	
    public void shutdown();
} 
