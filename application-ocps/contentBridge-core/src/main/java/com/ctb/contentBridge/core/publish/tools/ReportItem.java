package com.ctb.contentBridge.core.publish.tools;


import java.sql.*;


public class ReportItem {
    public String Id;
    public String Name;
    public String ActivationStatus;
    public String extraInfo;

    public ReportItem(String Id, String Name, String ActivationStatus) {
        this.Id = Id;
        this.Name = Name;
        this.ActivationStatus = ActivationStatus;
    }
    
    public ReportItem(String Id, String Name, String ActivationStatus, String extraInfo) {
    	this(Id, Name, ActivationStatus);
    	this.extraInfo = extraInfo;
    }

    public ReportItem(ResultSet result) throws SQLException {
        Id = result.getString("item_id");
        Name = result.getString("description");
        if (Name != null && Name.length() > 100) {
            Name = Name.substring(0, 100) + "...";
        }
        ActivationStatus = result.getString("activation_status");

    }
}

