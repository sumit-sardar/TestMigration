package com.ctb.lexington.domain.score.collector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ctb.lexington.db.ConnectionFactory;
import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.util.SQLUtil;

public class AdminCollector{
    private final Connection conn;
     
    public AdminCollector(Connection conn){
        this.conn = conn;
    }

    public AdminData collectAdminData(Long oasRosterId) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{	    
        	final String sql = "SELECT distinct count(distinct ros.student_id) as roster_count, " + 
        								"a.test_admin_id, " + 
        								"a.LOGIN_START_DATE - 0.5, " + 
        								"a.LOGIN_END_DATE, " + 
        								"prog.PRODUCT_ID, " +
        								"pp.product_name, " + 
        								"p.product_type, " + 
        								"tc.test_catalog_id, " + 
        								"tc.test_name, " + 
        								"prog.CUSTOMER_ID, " + 
        								"customer.customer_name, " + 
        								"crb.customer_key, " + 
        								"prog.program_id, " + 
        								"prog.program_name, " + 
        								"prog.program_start_date, " + 
        								"prog.program_end_date, " +
                                        "prog.norms_group, " + 
        								"prog.norms_year, " + 
        								"us.user_id, " + 
        								"us.user_name, " + 
        								"us.last_name, " + 
        								"us.first_name, " + 
        								"a.time_zone " + 
        						"FROM " + 
        								"test_admin a, " + 
        								"test_roster b, " + 
        								"item_set c, " + 
        								"customer, " + 
        								"customer_report_bridge crb, " + 
        								"test_catalog tc, " + 
        								"users us, " + 
        								"product p, " + 
        								"test_roster ros, " +
        								"program prog, " +
                                        "product pp " + 
        					   "WHERE " + 
        					   			"crb.customer_id (+) = customer.customer_id " + 
        					   		"AND b.test_admin_id = a.test_admin_id " + 
        					   		"AND prog.customer_id = b.customer_id " +
                                    "AND prog.product_id = p.parent_product_id " +
                                    "AND prog.program_id = a.program_id " +
        					   		"AND customer.customer_id = prog.customer_id " + 
        					   		"AND a.item_set_id = tc.item_set_id " + 
        					   		"AND a.item_set_id = c.item_set_id " + 
        					   		"AND a.created_by = us.user_id " +  
        					   		"AND a.product_id = p.product_id " + 
        					   		"AND b.test_roster_id=? " + 
        					   		"AND ros.test_admin_id = a.test_admin_id " +
                                    "AND pp.product_id = prog.product_id " + 
        					   "GROUP BY " + 
        					   			"a.test_admin_id, " + 
        					   			"a.LOGIN_START_DATE, " + 
        					   			"a.LOGIN_END_DATE, " +  
        					   			"prog.PRODUCT_ID, " + 
        					   			"pp.product_name, " + 
        					   			"p.product_type, " + 
        					   			"tc.test_catalog_id, " + 
        					   			"tc.test_name, " + 
        					   			"prog.CUSTOMER_ID, " +
        					   			"customer.customer_name, " + 
        					   			"crb.customer_key, " + 
        					   			"prog.program_id, " + 
        					   			"prog.program_name, " +
        					   			"prog.program_start_date, " + 
        					   			"prog.program_end_date, " + 
                                        "prog.norms_group, " + 
        					   			"prog.norms_year, " + 
        					   			"us.user_id, " + 
        					   			"us.user_name, " + 
        					   			"us.last_name, " + 
        					   			"us.first_name, " + 
        					   			"a.time_zone";
            
        	final AdminData details = new AdminData();
	        
            ps = conn.prepareStatement(sql);
	        ps.setLong(1, oasRosterId.longValue());           
	        rs = ps.executeQuery();
	        if(rs.next()){
	        	details.setAssessmentId(SQLUtil.getLong(rs, "test_catalog_id"));
                details.setAssessmentName(SQLUtil.getString(rs, "test_name"));
	        	details.setAssessmentType(SQLUtil.getString(rs, "product_type"));
	        	
	        	details.setCustomerId(SQLUtil.getLong(rs, "customer_id"));
	        	details.setCustomerName(SQLUtil.getString(rs, "customer_name"));
                details.setCustomerKey(SQLUtil.getString(rs, "customer_key"));
	        	
	        	details.setSessionId(SQLUtil.getLong(rs, "test_admin_id"));
	        	details.setNumberOfStudents(SQLUtil.getLong(rs, "roster_count"));
	        	details.setWindowStartDate(SQLUtil.getTimestamp(rs,"login_start_date"));
	        	details.setWindowEndDate(SQLUtil.getTimestamp(rs,"login_end_date"));
	        	
	        	details.setProductId(SQLUtil.getLong(rs, "product_id"));
	        	details.setproductName(SQLUtil.getString(rs, "product_name"));
	        	
	        	details.setProgramId(SQLUtil.getLong(rs, "program_id"));
	        	details.setProgramName(SQLUtil.getString(rs, "program_name"));
	        	details.setProgStartDate(SQLUtil.getTimestamp(rs,"program_start_date"));
	        	details.setProgEndDate(SQLUtil.getTimestamp(rs,"program_end_date"));
                details.setNormsGroup(SQLUtil.getString(rs, "norms_group"));
                details.setNormsYear(SQLUtil.getString(rs, "norms_year"));
	        		        	
	        	details.setSchedulerId(SQLUtil.getLong(rs, "user_id"));
                String lastName = SQLUtil.getString(rs, "last_name");
                String firstName = SQLUtil.getString(rs, "first_name");
                String schedulerName = SQLUtil.getString(rs, "user_name");
                if(lastName != null && lastName.length() > 0) {
                    schedulerName = lastName;
                    if(firstName != null && firstName.length() > 0) {
                        schedulerName = schedulerName + ", " + firstName;
                    }
                }
	        	details.setSchedulerName(schedulerName);
	        
                details.setTimeZone(SQLUtil.getString(rs, "time_zone"));
            } 
            return details;
        	}finally{
        			SQLUtil.close(rs);
        			ConnectionFactory.getInstance().release(ps);
        	}
    }
}