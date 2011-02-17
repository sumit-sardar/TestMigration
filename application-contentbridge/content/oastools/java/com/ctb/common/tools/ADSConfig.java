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
    

   public ADSConfig(){};
   
    public ADSConfig(
        String ftpHost,
        String ftpUser,
        String ftpPassword,
        String wsClient) {
        this.ftpHost = ftpHost;
        this.ftpUser = ftpUser;
        this.ftpPassword = ftpPassword;
        this.wsClient = wsClient;
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

}
