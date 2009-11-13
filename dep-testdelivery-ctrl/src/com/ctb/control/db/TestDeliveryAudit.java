package com.ctb.control.db; 

import com.bea.control.*;
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import com.ctb.bean.testDelivery.studentTestData.AuditFile;
import java.sql.Clob; 
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
public interface TestDeliveryAudit extends JdbcControl
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
     * @jc:sql statement::
     * insert into test_delivery_audit (
     * 	test_delivery_audit_id,
     *     	test_roster_id, 
     *      	item_set_id, 
     * 	audit_file)
     * values (
     * 	SEQ_TEST_DELIVERY_AUDIT_ID.NEXTVAL,
     *      	{testRosterId},
     *      	{itemSetId},
     *      	{auditFile})::
     */
    @JdbcControl.SQL(statement = "insert into test_delivery_audit ( \ttest_delivery_audit_id,  \ttest_roster_id,  \titem_set_id,  \taudit_file) values ( \tSEQ_TEST_DELIVERY_AUDIT_ID.NEXTVAL,  \t{testRosterId},  \t{itemSetId},  \t{auditFile})")
    void storeTestDeliveryAuditFile(int testRosterId, int itemSetId, String auditFile) throws SQLException;

    /**
     * @jc:sql statement::
     * select count(*) 
     * from test_delivery_audit 
     * where test_roster_id = {testRosterId}
     * and item_set_id = {itemSetId}
     * ::
     */
    @JdbcControl.SQL(statement = "select count(*)  from test_delivery_audit  where test_roster_id = {testRosterId} and item_set_id = {itemSetId}")
    int getCountTestDeliveryAuditFile(int testRosterId, int itemSetId);
    
    /**
     * @jc:sql statement::
     * select 
     *     test_delivery_audit_id as testDeliveryAuditId, 
     *     audit_file as auditFile
     * from 
     *     test_delivery_audit 
     * where 
     *     test_roster_id = {testRosterId}
     *     and item_set_id = {itemSetId}
     *     and test_delivery_audit_id = (
     *         select max(test_delivery_audit_id)
     *         from test_delivery_audit
     *         where test_roster_id = {testRosterId}
     *         and item_set_id = {itemSetId}
     *      )::
     */
    @JdbcControl.SQL(statement = "select  test_delivery_audit_id as testDeliveryAuditId,  audit_file as auditFile from  test_delivery_audit  where  test_roster_id = {testRosterId}  and item_set_id = {itemSetId}  and test_delivery_audit_id = (  select max(test_delivery_audit_id)  from test_delivery_audit  where test_roster_id = {testRosterId}  and item_set_id = {itemSetId}  )")
    AuditFile getTestDeliveryAuditFile(int testRosterId, int itemSetId);
    
    /**
     * @jc:sql statement::
     * update test_delivery_audit
     * 	set auditFile = {auditFile}
     * 	where test_delivery_audit_id = {testDeliveryAuditId}::
     */
    @JdbcControl.SQL(statement = "update test_delivery_audit \tset auditFile = {auditFile} \twhere test_delivery_audit_id = {testDeliveryAuditId}")
    void updateTestDeliveryAuditFile(int testDeliveryAuditId, Clob auditFile) throws SQLException;

}