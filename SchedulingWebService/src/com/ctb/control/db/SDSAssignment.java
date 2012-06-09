package com.ctb.control.db; 

import com.bea.control.*;
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerEmail;
import com.ctb.bean.testAdmin.FindUser;
import com.ctb.bean.testAdmin.PasswordHintQuestion;
import com.ctb.bean.testAdmin.PasswordHistory;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.Role; 
import java.sql.SQLException; 
import com.ctb.bean.testAdmin.TimeZones;
import java.util.Date;
import org.apache.beehive.controls.api.bean.ControlExtension;

/** 
 * Defines a new database control. 
 * 
 * The @jc:connection tag indicates which WebLogic data source will be used by 
 * this database control. Please change this to suit your needs. You can see a 
 * list of available data sources by going to the WebLogic console in a browser 
 * (typically http://localhost:7001/console) and clicking Services, JDBC, 
 * Data Sources. 
 * 
 * @jc:connection data-source-jndi-name="adsDataSource" 
 */ 
@ControlExtension()
@JdbcControl.ConnectionDataSource(jndiName = "adsDataSource")
public interface SDSAssignment extends JdbcControl
{ 
    /**
     * @jc:sql statement::
     * Insert into TCM_CUSTOMER
     *      (CUSTOMER_ACCOUNT_ID, DATE_ACTIVATE, DATE_TERMINATE, CUSTOMER_NAME)
     *       Values
     *       ({customerId}, 
     *       NULL, 
     *       NULL, 
     *       substr({customerName}, 1, 30))::
     */
    @JdbcControl.SQL(statement = "Insert into TCM_CUSTOMER  (CUSTOMER_ACCOUNT_ID, DATE_ACTIVATE, DATE_TERMINATE, CUSTOMER_NAME)  Values  ({customerId},  NULL,  NULL,  substr({customerName}, 1, 30))")
    void addCustomerToADS(Integer customerId, String customerName) throws SQLException;
    
    /**
     * @jc:sql statement::
     * Insert into TCM_CUSTOMER_SDS
     *   (CUSTOMER_ACCOUNT_ID, SITE_SERVER_ID)
     *      Values
     *      ({customerId}, 2)::
     */
    @JdbcControl.SQL(statement = "Insert into TCM_CUSTOMER_SDS  (CUSTOMER_ACCOUNT_ID, SITE_SERVER_ID)  Values  ({customerId}, 2)")
    void configureSDSForCustomer(Integer customerId) throws SQLException;
            
}