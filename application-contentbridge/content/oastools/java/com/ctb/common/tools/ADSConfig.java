package com.ctb.common.tools;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.ctb.util.NoCommitConnection;
import com.ctb.util.SafeConnection;

public class ADSConfig extends Config {
    private String ftpHost;
    private String ftpUser;
    private String ftpPassword;
    private String wsClient;
    public String adsDbUser;
    public String adsDbPassword;
    public String adsDbSid;
    public String adsDbHost;
    private boolean isSftp;
    private int port;
    
    
    

   public ADSConfig(){};
   
    public ADSConfig(
        String ftpHost,
        String ftpUser,
        String ftpPassword,
        String wsClient, boolean isSftp, int port) {
        this.ftpHost = ftpHost;
        this.ftpUser = ftpUser;
        this.ftpPassword = ftpPassword;
        this.wsClient = wsClient;
        this.isSftp= isSftp;
        this.port =port;
     }

    public ADSConfig(File file) {
        load(file);
    }

    protected void readProperties() {
        ftpHost = getRequiredProperty(properties, "ws.ftphost", file);
        ftpUser = getRequiredProperty(properties, "ws.ftpuser", file);
        ftpPassword = getRequiredProperty(properties, "ws.ftppassword", file);
        wsClient = getRequiredProperty(properties, "ws.client", file);
        adsDbUser = getRequiredProperty(properties, "adsdb.user", file);
        adsDbPassword = getRequiredProperty(properties, "adsdb.password", file);
        adsDbSid = getRequiredProperty(properties, "adsdb.sid", file);
        adsDbHost = getRequiredProperty(properties, "adsdb.host", file);
        try{
        	String transferProtocol = getRequiredProperty(properties, "ws.file.transfer.protocol", file);
        	if(transferProtocol == null || !transferProtocol.trim().equalsIgnoreCase("ftp") ){
        		isSftp = true;
        	} else {
        		isSftp = false;
        	}
        } catch (Exception e) {
        	isSftp = true;
        }
        
        try{
        	String sport = getRequiredProperty(properties, "ws.file.transfer.protocol.port", file);
        	port = Integer.parseInt(sport.trim());
        } catch (Exception e) {
        	if(isSftp){
        		port = 22;
        	} else {
        		port = 21;
        	}
        	
        }
        
    }

    public String getFtpHost() {
        return ftpHost;
    }
    
    public String getFtpUser() {
        return ftpUser;
    }
    
    public String getFtpPassword() {
        return ftpPassword;
    }
    public String getWsClient() {
        return wsClient;
    }

	/**
	 * @return the isSftp
	 */
	public boolean isSftp() {
		return isSftp;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

}
