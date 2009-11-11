package com.ctb.control.db; 

import com.bea.control.*; 
import com.bea.control.JdbcControl;
import java.sql.SQLException; 
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
 * @jc:connection data-source-jndi-name="oasDataSource" 
 */ 
@ControlExtension()
@JdbcControl.ConnectionDataSource(jndiName = "oasDataSource")
public interface OASDB extends JdbcControl
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

    /** 
     * @jc:sql statement="SELECT distinct test_roster_id from test_roster where test_Admin_id = {testAdminId} and test_completion_status not in ('SC', 'NT')" 
     */
    @JdbcControl.SQL(statement = "SELECT distinct test_roster_id from test_roster where test_Admin_id = {testAdminId} and test_completion_status not in ('SC', 'NT')")
    int [] getCompletedRostersForAdmin(int testAdminId) throws SQLException;

    /** 
     * @jc:sql statement="SELECT distinct ros.test_roster_id from test_roster ros, test_Admin adm where ros.test_Admin_id = adm.test_Admin_id and adm.product_id = {productId} and ros.test_completion_status not in ('SC', 'NT')" 
     */
    @JdbcControl.SQL(statement = "SELECT distinct ros.test_roster_id from test_roster ros, test_Admin adm where ros.test_Admin_id = adm.test_Admin_id and adm.product_id = {productId} and ros.test_completion_status not in ('SC', 'NT')")
    int [] getCompletedRostersForProduct(int productId) throws SQLException;

    /** 
     * @jc:sql statement="SELECT distinct ros.test_roster_id from test_roster ros, test_Admin adm where ros.test_Admin_id = adm.test_Admin_id and adm.customer_id = {customerId} and ros.test_completion_status not in ('SC', 'NT')" 
     */
    @JdbcControl.SQL(statement = "SELECT distinct ros.test_roster_id from test_roster ros, test_Admin adm where ros.test_Admin_id = adm.test_Admin_id and adm.customer_id = {customerId} and ros.test_completion_status not in ('SC', 'NT')")
    int [] getCompletedRostersForCustomer(int customerId) throws SQLException;

}