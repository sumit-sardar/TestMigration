package com.ctb.lexington.domain.score.collector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ctb.lexington.db.ConnectionFactory;
import com.ctb.lexington.db.data.StudentTestDetails;
import com.ctb.lexington.db.data.UrlData;
import com.ctb.lexington.exception.DataException;
import com.ctb.lexington.util.SQLUtil;

public class UrlDataCollector {
	private final Connection conn;

    public UrlDataCollector(Connection conn) {
        this.conn = conn;
    }
    
    public UrlData collectAquityUrlData(Long testAdminId)
    throws SQLException, DataException {
    	final String sql = "SELECT cr.resource_uri as URL, cr.resource_type_code as TYPE " +
    			"FROM customer_resource cr, test_admin adm " +
    			"WHERE adm.test_Admin_id = ? AND adm.LOCATION = cr.resource_type_code";

    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	final UrlData data = new UrlData();
    	try {
    		ps = conn.prepareStatement(sql);
    		ps.setLong(1, testAdminId.longValue());
    		rs = ps.executeQuery();
    		while (rs.next()) {
		        data.setUserType(rs.getString("TYPE"));
		        data.setWebserviceUrl(rs.getString("URL"));
    		}
    		if (data == null || data.getWebserviceUrl() == null) {
    			data.setWebserviceUrl("http://192.168.14.136:8080/host/services/ScoringService");
    		}
    		return data;
    	} finally {
    		SQLUtil.close(rs);
    		ConnectionFactory.getInstance().release(ps);
    	}
    }

}
