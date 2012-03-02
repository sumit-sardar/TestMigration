package com.ctb.control.db;

import java.sql.SQLException;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;


/** 
 * Defines a new database control. 
 * 
 * The @jc:connection tag indicates which WebLogic data source will be used by 
 * this database control. Please change this to suit your needs. You can see a 
 * list of available data sources by going to the WebLogic console in a browser 
 * (typically http://localhost:7001/console) and clicking Services, JDBC, 
 * Data Sources. 
 *
 * @author TCS
 * 
 * @jc:connection data-source-jndi-name="irsDataSource" 
 */ 

@ControlExtension()
@JdbcControl.ConnectionDataSource(jndiName = "irsDataSource")
public interface ImmediateReportingIrs extends JdbcControl{
	
	static final long serialVersionUID = 1L;
	
	@JdbcControl.SQL(statement = "select proficency_level from laslink_composite_fact where studentid = {studentId} and sessionid = {testAdminId}")
            String getProficiencyLevel(Integer studentId, Integer testAdminId) throws SQLException;


}
