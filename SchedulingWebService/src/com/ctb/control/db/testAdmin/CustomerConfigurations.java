package com.ctb.control.db.testAdmin; 

import java.sql.SQLException;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.OrganizationNode;

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
public interface CustomerConfigurations extends JdbcControl
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
     * select distinct customer_configuration_value as customerConfigurationValue,
     *      customer_configuration_id as customerConfigurationId,
     *      sort_order sortOrder
     * from customer_configuration_value
     * where customer_configuration_id in (
     * 	select   
     * 	  customer_configuration_id
     * 	from customer_configuration
     * 	where customer_id = {customerId}
     * 	  and upper(customer_configuration_name) = upper('Grade'))
     * order by sort_order, customer_configuration_value	  ::
     */
    @JdbcControl.SQL(statement = "select distinct customer_configuration_value as customerConfigurationValue,  customer_configuration_id as customerConfigurationId,  sort_order sortOrder from customer_configuration_value where customer_configuration_id in ( \tselect  \t  customer_configuration_id \tfrom customer_configuration \twhere customer_id = {customerId} \t  and upper(customer_configuration_name) = upper('Grade')) order by sort_order, customer_configuration_value\t  ")
    CustomerConfigurationValue [] getCustomerConfigurationValuesForGrades(int customerId) throws SQLException;
 

    /**
     * @jc:sql statement::
     * select   
     *   customer_configuration_id as id,
     *   customer_configuration_name as customerConfigurationName,
     *   customer_id as customerId,
     *   editable as editable,
     *   default_value as defaultValue
     * from customer_configuration
     * where customer_id = {customerId}::
     */
    @JdbcControl.SQL(statement = "select  customer_configuration_id as id,  customer_configuration_name as customerConfigurationName,  customer_id as customerId,  editable as editable,  default_value as defaultValue from customer_configuration where customer_id = {customerId}")
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
    @JdbcControl.SQL(statement = "select  customer_configuration_value as customerConfigurationValue,  customer_configuration_id as customerConfigurationId,  sort_order sortOrder from customer_configuration_value where customer_configuration_id = {customerConfigurationId} order by sort_order, customer_configuration_value")
    CustomerConfigurationValue [] getCustomerConfigurationValues(int customerConfigurationId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select distinct
     *     node.org_node_id as orgNodeId,
     *     node.customer_id as customerId,
     *     node.org_node_category_id as orgNodeCategoryId,
     *     node.org_node_name as orgNodeName,
     *     node.ext_qed_pin as extQedPin,
     *     node.ext_elm_id as extElmId,
     *     node.ext_org_node_type as extOrgNodeType,
     *     node.org_node_description as orgNodeDescription,
     *     node.created_by as createdBy,
     *     node.created_date_time as createdDateTime,
     *     node.updated_by as updatedBy,
     *     node.updated_date_time as updatedDateTime,
     *     node.activation_status as activationStatus,
     *     node.data_import_history_id as dataImportHistoryId,
     *     node.parent_state as parentState,
     *     node.parent_region as parentRegion,
     *     node.parent_county as parentCounty,
     *     node.parent_district as parentDistrict,
     *     node.org_node_code as orgNodeCode,
     *     cat.category_name as orgNodeCategoryName,
     * 	   ona.number_of_levels as numberOfLevels
     * from 
     *      org_node node, org_node_category cat, org_node_ancestor ona
     * where 
     * 	 cat.org_node_category_id = node.org_node_category_id
     * 	 and ona.ancestor_org_node_id = node.org_node_id
     * 	 and ona.org_node_id = {orgNodeId}
     *   and node.org_node_id in 
     * 	 	 (select distinct ona1.org_node_id 
     * 	 	 from org_node_ancestor ona1 where ona1.ANCESTOR_ORG_NODE_ID in ({orgNodeIds})
     * 	 )
     * order by ona.number_of_levels desc::
     */
    @JdbcControl.SQL(statement = "select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName, \t  ona.number_of_levels as numberOfLevels from  org_node node, org_node_category cat, org_node_ancestor ona where  \t cat.org_node_category_id = node.org_node_category_id \t and ona.ancestor_org_node_id = node.org_node_id \t and ona.org_node_id = {orgNodeId}  and node.org_node_id in  \t \t (select distinct ona1.org_node_id  \t \t from org_node_ancestor ona1 where {sql: searchCriteria}) \t ) order by ona.number_of_levels desc")
    OrganizationNode [] getAncestorOrganizationNodesForOrgNodeAtAndBelowTopOrgNodes(Integer orgNodeId, String searchCriteria) throws SQLException;
  
  
    /**
     * @jc:sql statement::
     * select distinct node.org_node_id as orgNodeId
     * from org_node node, user_role role, users
     * where role.org_node_id = node.org_node_id
     * and role.activation_status = 'AC'
     * and node.activation_status = 'AC'
     * and role.user_id = users.user_id
     * and users.user_name = {username}::
     */
    @JdbcControl.SQL(statement = "select distinct node.org_node_id as orgNodeId from org_node node, user_role role, users where role.org_node_id = node.org_node_id and role.activation_status = 'AC' and node.activation_status = 'AC' and role.user_id = users.user_id and users.user_name = {username}")
    Integer [] getTopOrgNodeIdsForUser(String username) throws SQLException;

    /**
     * @jc:sql statement::
     * select cc.default_value
     * from customer_configuration cc 
     * where cc.customer_id = {customerId}
     * and cc.customer_configuration_name = 'Roster_Status_Flag'::
     */
    @JdbcControl.SQL(statement = "select cc.default_value from customer_configuration cc  where cc.customer_id = {customerId} and cc.customer_configuration_name = 'Roster_Status_Flag'")
    String getDefaulCustomerFlagStatus(Integer customerId) throws SQLException;
    
    @JdbcControl.SQL(statement = "select  ccvalue.customer_configuration_value as customerConfigurationValue, ccvalue.customer_configuration_id as customerConfigurationId,  ccvalue.sort_order as sortOrder from customer_configuration cc, customer_configuration_value ccvalue where cc.customer_configuration_id = ccvalue.customer_configuration_id and cc.customer_configuration_name = {configurationName} and cc.customer_id= {customerId}")
    CustomerConfigurationValue [] getCustomerConfigurationValue(int customerId, String configurationName) throws SQLException;
    
    
}