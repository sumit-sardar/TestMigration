package com.ctb.control.db; 

import java.sql.SQLException;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.dataExportManagement.ManageJob;
import com.ctb.bean.testAdmin.CustomerConfiguration;

/** 
 * Defines a new database control. 
 * 
 * The @jc:connection tag indicates which WebLogic data source will be used by 
 * this database control. Please change this to suit your needs. You can see a 
 * list of available data sources by going to the WebLogic console in a browser 
 * (typically http://localhost:7001/console) and clicking Services, JDBC, 
 * Data Sources. 
 *
 * @author John_Wang
 * 
 * @jc:connection data-source-jndi-name="oasDataSource" 
 */ 
@ControlExtension()
@JdbcControl.ConnectionDataSource(jndiName = "oasDataSource")
public interface DataExportManagement extends JdbcControl
{ 
    // Sample database function.  Uncomment to use 

    // static public class Customer 
    // { 
    //   public int id; 
    //   public String name; 
    // } 
    // 
    // /** 
    //  * @jc:sql statement="SELECT ID, NAME FROM CUSTOMERS WHERE ID = {id}" 
    //  */ 
    // Customer findCustomer(int id);

    // Add "throws SQLException" to request that SQLExeptions be thrown on errors.

    static final long serialVersionUID = 1L;

 
    @JdbcControl.SQL(statement = "select  customer_configuration_id as id,  customer_configuration_name as customerConfigurationName,  customer_id as customerId,  editable as editable,  default_value as defaultValue from customer_configuration where customer_id = {customerId}", arrayMaxLength = 100000)
    CustomerConfiguration [] getCustomerConfigurations(int customerId) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     *   customer_configuration_value as customerConfigurationValue,
     *   customer_configuration_id as customerConfigurationId,
     *   sort_order sortOrder
     * from customer_configuration_value
     * where customer_configuration_id = {customerConfigurationId}
     * order by sort_order, customer_configuration_value
     * ::
     */
    @JdbcControl.SQL(statement = "   select distinct export.export_id as jobId, export.student_count as studentCount, export.created_date_time as createdDateTime, status.status_name as jobStatus, export.created_by as createdBy, export.last_update_time as lastUpdateTime, export.last_update_status as lastUpdateStatus, export.message from data_export export, job_status status where export.status = status.status_id and export.created_by = {userId}" , arrayMaxLength=0, fetchSize = 50000) 
	ManageJob[] getDataExportJobStatus(Integer userId)throws SQLException;

}