package com.ctb.contentBridge.core.publish.tools;


import java.sql.*;


public class ReportObjective {
    public Integer Id;
    public String Name;
    public String CmsId;
    public Integer CategoryId;
    public String ActivationStatus;

    public ReportObjective(Integer Id, String Name, String CmsId, Integer CategoryId, String ActivationStatus) {
        this.Id = Id;
        this.Name = Name;
        this.CmsId = CmsId;
        this.CategoryId = CategoryId;
        this.ActivationStatus = ActivationStatus;
    }

    public ReportObjective(ResultSet result) throws SQLException {
        Id = new Integer(result.getInt("item_set_id"));
        Name = result.getString("item_set_name");
        CmsId = result.getString("ext_cms_item_set_id");
        CategoryId = new Integer(result.getInt("item_set_category_id"));
        ActivationStatus = result.getString("activation_status");
    }
}
