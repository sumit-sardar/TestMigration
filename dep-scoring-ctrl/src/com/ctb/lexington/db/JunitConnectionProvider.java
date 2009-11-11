package com.ctb.lexington.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.PropertyResourceBundle;

public class JunitConnectionProvider extends AbstractConnectionProvider {
    private static final String KEY_OSR_DATABASE_URL = "OSRDatabaseURL";
    private static final String KEY_OSR_USER_NAME = "OSRUserName";
    private static final String KEY_OSR_PASSWORD = "OSRPassword";
    private static final String KEY_IRS_DATABASE_URL = "IRSDatabaseURL";
    private static final String KEY_IRS_USER_NAME = "IRSUserName";
    private static final String KEY_IRS_PASSWORD = "IRSPassword";

    private Connection osrConnection;
    private Connection irsConnection;

    protected void initProperties() {
        bundle = PropertyResourceBundle.getBundle("testDb");
    }

    public Connection getOSRConnection() throws SQLException {
        if (osrConnection == null) {
            initProperties();
            String url = bundle.getString(KEY_OSR_DATABASE_URL);
            String user = bundle.getString(KEY_OSR_USER_NAME);
            String password = bundle.getString(KEY_OSR_PASSWORD);
            osrConnection = getConnection(url, user, password);
        }
        return osrConnection;
    }
    
    
   public Connection getIRSConnection() throws SQLException{
    	if(irsConnection == null){
    		initProperties();
    		String irsurl = bundle.getString(KEY_IRS_DATABASE_URL);
    		String irsuser = bundle.getString(KEY_IRS_USER_NAME);
    		String irspassword = bundle.getString(KEY_IRS_PASSWORD);
    		irsConnection = getConnection(irsurl,irsuser,irspassword);
    	}
    	return irsConnection;
    }
    
    public String getOSRSchemaName() {
        initProperties();
        return bundle.getString(KEY_OSR_USER_NAME).toUpperCase();
    }
   
    
    public String getIRSSchemaName(){
    	initProperties();
    	return bundle.getString(KEY_IRS_USER_NAME).toUpperCase();
    }
}