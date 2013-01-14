package com.ctb.control.db; 


import org.apache.beehive.controls.system.jdbc.JdbcControl;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.OrgNodeCategory;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.OrgNodeLicenseInfo;
import com.ctb.bean.testAdmin.StudentNode; 
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
public interface OrgNode extends JdbcControl
{ 
    static final long serialVersionUID = 1L;
    
    /**
     * @jc:sql statement::
     * select org_node_category_id 
     * from org_node_category 
     * where customer_id = {customerId}
     * order by category_level desc::
     */
    @JdbcControl.SQL(statement = "select org_node_category_id  from org_node_category  where customer_id = {customerId} order by category_level desc")
    Integer [] getOrgNodeCategoryIdsForCustomerFromBottomUp(Integer customerId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select 
     * 	   decode(count(ona.org_node_id), 0, 'false', 'true') as visible
     * from
     * 	org_node_ancestor ona,
     * 	user_role urole,
     * 	users
     * where
     * 	 users.user_name = {userName}
     * 	 and urole.user_id = users.user_id
     * 	 and ona.ancestor_org_node_id = urole.org_node_id
     * 	 and ona.org_node_id = {orgNodeId}::
     */
    @JdbcControl.SQL(statement = "select  \t  decode(count(ona.org_node_id), 0, 'false', 'true') as visible from \torg_node_ancestor ona, \tuser_role urole, \tusers where \t users.user_name = {userName} \t and urole.user_id = users.user_id \t and ona.ancestor_org_node_id = urole.org_node_id \t and ona.org_node_id = {orgNodeId}")
    String checkVisibility(String userName, Integer orgNodeId) throws SQLException;
    
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
     *     count(distinct child.org_node_id) as childNodeCount
     * from 
     *      org_node node, org_node_parent parent, org_node_category cat,
     *      org_node_parent child, org_node chnode
     * where 
     *      parent.org_node_id = node.org_node_id
     *      and cat.org_node_category_id = node.org_node_category_id
     *      and parent.parent_org_node_id = {parentOrgNodeId}
     *      and child.parent_org_node_id (+) = node.org_node_id
     *      and chnode.org_node_id (+) = child.org_node_id   
     *      and NVL(chnode.activation_status, 'AC') = 'AC'
     *      and node.activation_status = 'AC'
     *      and cat.activation_status = 'AC'
     * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  count(distinct child.org_node_id) as childNodeCount from  org_node node, org_node_parent parent, org_node_category cat,  org_node_parent child, org_node chnode where  parent.org_node_id = node.org_node_id  and cat.org_node_category_id = node.org_node_category_id  and parent.parent_org_node_id = {parentOrgNodeId}  and child.parent_org_node_id (+) = node.org_node_id  and chnode.org_node_id (+) = child.org_node_id  and NVL(chnode.activation_status, 'AC') = 'AC'  and node.activation_status = 'AC'  and cat.activation_status = 'AC' group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name",
                     arrayMaxLength = 0, fetchSize=100)
    Node [] getOrgNodesByParent(Integer parentOrgNodeId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select
     *     count(distinct node.org_node_id) as count
     * from 
     *      org_node node, org_node_parent parent
     * where 
     *      parent.org_node_id = node.org_node_id
     *      and parent.parent_org_node_id = {parentOrgNodeId}::
     */
    @JdbcControl.SQL(statement = "select  count(distinct node.org_node_id) as count from  org_node node, org_node_parent parent where  parent.org_node_id = node.org_node_id  and parent.parent_org_node_id = {parentOrgNodeId}")
    Integer getOrgNodeCountByParent(Integer parentOrgNodeId) throws SQLException;
        
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
     *     count(distinct child.org_node_id) as childNodeCount
     * from 
     *      org_node node, user_role role, users, org_node_category cat,
     *      org_node_parent child, org_node chnode
     * where 
     *      role.org_node_id = node.org_node_id
     *      and cat.org_node_category_id = node.org_node_category_id
     *      and node.activation_status = 'AC'
     *      and role.activation_status = 'AC'
     *      and users.activation_status = 'AC'
     *      and cat.activation_status = 'AC'
     *      and role.user_id = users.user_id
     *      and users.user_name = {userName}
     *      and child.parent_org_node_id (+) = node.org_node_id
     *      and chnode.org_node_id (+) = child.org_node_id   
     *      and NVL(chnode.activation_status, 'AC') = 'AC'
     * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  count(distinct child.org_node_id) as childNodeCount from  org_node node, user_role role, users, org_node_category cat,  org_node_parent child, org_node chnode where  role.org_node_id = node.org_node_id  and cat.org_node_category_id = node.org_node_category_id  and node.activation_status = 'AC'  and role.activation_status = 'AC'  and users.activation_status = 'AC'  and cat.activation_status = 'AC'  and role.user_id = users.user_id  and users.user_name = {userName}  and child.parent_org_node_id (+) = node.org_node_id  and chnode.org_node_id (+) = child.org_node_id  and NVL(chnode.activation_status, 'AC') = 'AC' group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name",
                     arrayMaxLength = 100000)
    Node [] getTopNodesForUser(String userName) throws SQLException;


    /**
     * @jc:sql statement::
     * select 
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
     *     node.org_node_code as orgNodeCode
     * from 
     *      org_node node
     * where 
     *      node.org_node_id = {orgNodeId}
     *    and node.activation_status = 'AC'::
     *      array-max-length="all"
	 * Changes For LASLINK Customer
     */
    @JdbcControl.SQL(statement = "select  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode, node.org_node_mdr_number as mdrNumber from  org_node node where  node.org_node_id = {orgNodeId}  and node.activation_status = 'AC'",
                     arrayMaxLength = 100000)
    Node getOrgNodeById(Integer orgNodeId) throws SQLException;


    /**
     * @jc:sql statement::
     * select distinct
     *     orgNodeId,
     *     customerId,
     *     orgNodeCategoryId,
     *     orgNodeName,
     *     extQedPin,
     *     extElmId,
     *     extOrgNodeType,
     *     orgNodeDescription,
     *     createdBy,
     *     createdDateTime,
     *     updatedBy,
     *     updatedDateTime,
     *     activationStatus,
     *     dataImportHistoryId,
     *     parentState,
     *     parentRegion,
     *     parentCounty,
     *     parentDistrict,
     *     orgNodeCode,
     *     orgNodeCategoryName,
     *     childNodeCount
     * from
     * (select distinct
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
     *     count(distinct child.org_node_id) as childNodeCount
     * from 
     *      org_node node, user_role role, users, org_node_category cat,
     *      org_node_parent child
     * where 
     *      role.org_node_id = node.org_node_id
     *      and cat.org_node_category_id = node.org_node_category_id
     *      and role.activation_status = 'AC'
     *      and role.user_id = users.user_id
     *      and users.user_name = {userName}
     *      and child.parent_org_node_id (+) = node.org_node_id
	 * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name
     * union
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
     *     count(distinct child.org_node_id) as childNodeCount
     * from 
     *      org_node node, 
     *      org_node_category cat,
     *      org_node_parent child,
     *      test_admin admin
     * where 
     *      admin.creator_org_node_id = node.org_node_id
     *      and cat.org_node_category_id = node.org_node_category_id
     *      and admin.test_admin_id = {testAdminId}
     *      and child.parent_org_node_id (+) = node.org_node_id
	 * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name)::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  orgNodeId,  customerId,  orgNodeCategoryId,  orgNodeName,  extQedPin,  extElmId,  extOrgNodeType,  orgNodeDescription,  createdBy,  createdDateTime,  updatedBy,  updatedDateTime,  activationStatus,  dataImportHistoryId,  parentState,  parentRegion,  parentCounty,  parentDistrict,  orgNodeCode,  orgNodeCategoryName,  childNodeCount from (select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  count(distinct child.org_node_id) as childNodeCount from  org_node node, user_role role, users, org_node_category cat,  org_node_parent child where  role.org_node_id = node.org_node_id  and cat.org_node_category_id = node.org_node_category_id  and role.activation_status = 'AC'  and role.user_id = users.user_id  and users.user_name = {userName}  and child.parent_org_node_id (+) = node.org_node_id group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name union select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  count(distinct child.org_node_id) as childNodeCount from  org_node node,  org_node_category cat,  org_node_parent child,  test_admin admin where  admin.creator_org_node_id = node.org_node_id  and cat.org_node_category_id = node.org_node_category_id  and admin.test_admin_id = {testAdminId}  and child.parent_org_node_id (+) = node.org_node_id group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name)",
                     arrayMaxLength = 100000)
    Node [] getTopNodesForUserAndAdmin(String userName, Integer testAdminId) throws SQLException;


    /**
     * @jc:sql statement::
     * select 
     *      count (distinct ros.test_roster_id) as rosterCount
     * from
     *      org_node_ancestor ona,
     *      org_node_student ons,
     *      test_roster ros,
     *      student stu
     * where
     *      ros.student_id = ons.student_id
     *      and ons.org_node_id = ona.org_node_id
     *      and ona.ancestor_org_node_id = {orgNodeId}
     *      and ros.test_admin_id = {testAdminId}
     *      and ros.org_node_id = ons.org_node_id
     *      and stu.student_id = ros.student_id
     *      and (stu.activation_status = 'AC' or ros.test_completion_status not in ('SC', 'NT'))::
     */
    @JdbcControl.SQL(statement = "select  count (distinct ros.test_roster_id) as rosterCount from  org_node_ancestor ona,  org_node_student ons,  test_roster ros,  student stu where  ros.student_id = ons.student_id  and ons.org_node_id = ona.org_node_id  and ona.ancestor_org_node_id = {orgNodeId}  and ros.test_admin_id = {testAdminId}  and ros.org_node_id = ons.org_node_id  and stu.student_id = ros.student_id  and (stu.activation_status = 'AC' or ros.test_completion_status not in ('SC', 'NT'))")
    Integer getRosterCountForAncestorNode(Integer orgNodeId, Integer testAdminId) throws SQLException;      

    /**
     * @jc:sql statement::
     * select 
     *      count (distinct ros.test_roster_id) as rosterCount
     * from
     *      org_node_ancestor ona,
     *      test_roster ros,
     *      student stu
     * where
     *      ona.ancestor_org_node_id = {orgNodeId}
     *      and ros.test_admin_id = {testAdminId}
     *      and ros.org_node_id = ona.org_node_id
     *      and stu.student_id = ros.student_id
     *      and stu.activation_status = 'AC'::
     */
    @JdbcControl.SQL(statement = "select  count (distinct ros.test_roster_id) as rosterCount from  org_node_ancestor ona,  test_roster ros,  student stu where  ona.ancestor_org_node_id = {orgNodeId}  and ros.test_admin_id = {testAdminId}  and ros.org_node_id = ona.org_node_id  and stu.student_id = ros.student_id  and stu.activation_status = 'AC'")
    Integer getTestTicketRosterCountForAncestorNode(Integer orgNodeId, Integer testAdminId) throws SQLException;  
    
    /**
     * @jc:sql statement::
     * select 
     *      count (distinct adm.test_admin_id) as sessionCount
     * from
     *      org_node_ancestor ona,
     *      test_admin adm
     * where
     *      adm.creator_org_node_id = ona.org_node_id
     *      and adm.activation_status = 'AC'
     *      and ona.ancestor_org_node_id = {orgNodeId}::
     */
    @JdbcControl.SQL(statement = "select  count (distinct adm.test_admin_id) as sessionCount from  org_node_ancestor ona,  test_admin adm where  adm.creator_org_node_id = ona.org_node_id  and adm.activation_status = 'AC'  and ona.ancestor_org_node_id = {orgNodeId}")
    Integer getSessionCountForAncestorNode(Integer orgNodeId) throws SQLException;      
    
    //START - TABE BAUM 020 Form Recommendation 
	
    @JdbcControl.SQL(statement = "select  count (distinct adm.test_admin_id) as sessionCount from  org_node_ancestor ona,  test_admin adm where  adm.creator_org_node_id = ona.org_node_id  and adm.activation_status = 'AC'  and ona.ancestor_org_node_id = {orgNodeId} and adm.test_admin_status in ('CU','FU')")
    Integer getRecommendedSessionCountForAncestorNode(Integer orgNodeId) throws SQLException;      
    
    @JdbcControl.SQL(statement = "select  count (distinct adm.test_admin_id) as sessionCount from  org_node_ancestor ona,  test_admin adm where  adm.creator_org_node_id = ona.org_node_id  and adm.activation_status = 'AC'  and ona.ancestor_org_node_id = {orgNodeId} and adm.test_admin_status in ('CU','FU') and  adm.product_Id = {productId}")
    Integer getRecommendedSessionCountForProductAncestorNode(Integer orgNodeId, Integer productId) throws SQLException;      
	//END - TABE BAUM 020 Form Recommendation 
	
    /**
     * @jc:sql statement::
     * select 
     *      count (distinct urole.user_id) as userCount
     * from
     *      org_node_ancestor ona,
     *      user_role urole
     * where
     *      urole.org_node_id = ona.org_node_id
     *      and urole.activation_status = 'AC'
     *      and ona.ancestor_org_node_id = {orgNodeId}::
     */
    @JdbcControl.SQL(statement = "select  count (distinct urole.user_id) as userCount from  org_node_ancestor ona,  user_role urole where  urole.org_node_id = ona.org_node_id  and urole.activation_status = 'AC'  and ona.ancestor_org_node_id = {orgNodeId}")
    Integer getUserCountForAncestorNode(Integer orgNodeId) throws SQLException;      

    /**
     * @jc:sql statement::
     * select 
     *      count (distinct urole.user_id) as userCount
     * from
     *      org_node_ancestor ona,
     *      user_role urole,
     *      test_admin_user_role taur
     * where
     *      urole.org_node_id = ona.org_node_id
     *      and urole.activation_status = 'AC'
     *      and taur.user_id = urole.user_id
     *      and taur.test_admin_id = {testAdminId}
     *      and ona.ancestor_org_node_id = {orgNodeId}::
     */
    @JdbcControl.SQL(statement = "select  count (distinct urole.user_id) as userCount from  org_node_ancestor ona,  user_role urole,  test_admin_user_role taur where  urole.org_node_id = ona.org_node_id  and urole.activation_status = 'AC'  and taur.user_id = urole.user_id  and taur.test_admin_id = {testAdminId}  and ona.ancestor_org_node_id = {orgNodeId}")
    Integer getProctorCountForAncestorNodeAndAdmin(Integer orgNodeId, Integer testAdminId) throws SQLException;      
        
    /**
     * @jc:sql statement::
     * select  
     *          ontc.org_node_id as orgNodeId,
     *          ona.number_of_levels orgNodeLevel,
     *          ontc.lic_purchased as licPurchased,
     *          ontc.lic_reserved as licReserved,
     *          ontc.lic_used as licUsed
     * from  
     *          org_node_test_catalog ontc,
     *          org_node_ancestor ona,
     *          user_role ur
     * where  
     *          ontc.customer_id = {customerId}
     *          and ontc.product_id = {productId}
     *          and ontc.org_node_id = ona.ancestor_org_node_id
     *          and ona.org_node_id = ur.org_node_id
     *          and ur.user_id = {userId} 
     * group by
     *          ontc.org_node_id,
     *          ona.number_of_levels,
     *          ontc.lic_purchased,
     *          ontc.lic_reserved,
     *          ontc.lic_used
     * order by number_of_levels::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  ontc.org_node_id as orgNodeId,  ona.number_of_levels orgNodeLevel,  ontc.lic_purchased as licPurchased,  ontc.lic_reserved as licReserved,  ontc.lic_used as licUsed from  org_node_test_catalog ontc,  org_node_ancestor ona,  user_role ur where  ontc.customer_id = {customerId}  and ontc.product_id = {productId}  and ontc.org_node_id = ona.ancestor_org_node_id  and ona.org_node_id = ur.org_node_id  and ur.user_id = {userId}  group by  ontc.org_node_id,  ona.number_of_levels,  ontc.lic_purchased,  ontc.lic_reserved,  ontc.lic_used order by number_of_levels",
                     arrayMaxLength = 100000)
    OrgNodeLicenseInfo [] getLicenseInfoForUserOrgNode(Integer customerId, Integer productId, Integer userId) throws SQLException; 
        
    /**
     * @jc:sql statement::
     * update  
     *          org_node_test_catalog ontc
     * set  
     *          ontc.lic_purchased = {licPurchased},
     *          ontc.lic_reserved = {licReserved}
     * where  
     *          ontc.org_node_id = {orgNodeId}
     *          and ontc.customer_id = {customerId}
     *          and ontc.product_id = {productId}::
     */
    @JdbcControl.SQL(statement = "update  org_node_test_catalog ontc set  ontc.lic_purchased = {licPurchased},  ontc.lic_reserved = {licReserved} where  ontc.org_node_id = {orgNodeId}  and ontc.customer_id = {customerId}  and ontc.product_id = {productId}")
    void updateLicenseInfoForUserOrgNode(Integer orgNodeId, Integer customerId, Integer productId, Integer licPurchased, Integer licReserved) throws SQLException; 
    
    
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
     * select onc.org_node_category_id 
     * from org_node_category onc
     * where onc.customer_id = {customerId} 
     * 	  and onc.category_level = 
     * 	  (select max(onc1.category_level) 
     * 	  from org_node_category onc1 
     * 	  where onc.customer_id = onc1.customer_id)::
     */
    @JdbcControl.SQL(statement = "select onc.org_node_category_id  from org_node_category onc where onc.customer_id = {customerId}  \t  and onc.category_level =  \t  (select max(onc1.category_level)  \t  from org_node_category onc1  \t  where onc.customer_id = onc1.customer_id)")
    Integer getBottomOrgNodeCategoryIdForCustomer(Integer customerId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select count(distinct u.user_id) 
     * from
     *   user_role ur,
     *   users u,
     *   org_node node,
     *   org_node_category onc
     * where
     *   u.user_id = ur.user_id
     * and 
     *   ur.org_node_id = node.org_node_id
     * and 
     *   ur.activation_status= 'AC'
     * and 
     *   u.activation_status= 'AC'
     * and 
     *   ur.org_node_id in  (select distinct ona.org_node_id 
     * from org_node_ancestor ona where {sql:fn in(ona.ANCESTOR_ORG_NODE_ID,{orgNodeIds})}
     * ) 
     * and onc.org_node_category_id = node.org_node_category_id
     * ::
     */
    @JdbcControl.SQL(statement = "select count(distinct u.user_id)  from  user_role ur,  users u,  org_node node,  org_node_category onc where  u.user_id = ur.user_id and  ur.org_node_id = node.org_node_id and  ur.activation_status= 'AC' and  u.activation_status= 'AC' and  ur.org_node_id in  (select distinct ona.org_node_id  from org_node_ancestor ona where {sql:fn in(ona.ANCESTOR_ORG_NODE_ID,{orgNodeIds})} )  and onc.org_node_category_id = node.org_node_category_id")
    Integer getUserCountAtAndBelowOrgNodes(String username, Integer[] orgNodeIds) throws SQLException;
   

    /**
     * This query brings down details of all top nodes for the user.
     * @jc:sql statement::
     * select distinct
     *     orgNodeId,
     *     customerId,
     *     orgNodeCategoryId,
     *     orgNodeName,
     *     extQedPin,
     *     extElmId,
     *     extOrgNodeType,
     *     orgNodeDescription,
     *     createdBy,
     *     createdDateTime,
     *     updatedBy,
     *     updatedDateTime,
     *     activationStatus,
     *     dataImportHistoryId,
     *     parentState,
     *     parentRegion,
     *     parentCounty,
     *     parentDistrict,
     *     orgNodeCode,
     *     orgNodeCategoryName,
     *     max(userCount) as userCount,
     *     max(childNodeCount) as childNodeCount,
     *     parentOrgNodeId
     * from (select distinct
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
     *     count(distinct uor.user_id) as userCount,
     *     0 as childNodeCount,
     *     parent.parent_org_node_id as parentOrgNodeId
     * from 
     *      org_node node,
     *      org_node_parent parent, 
     *      org_node_category cat,
     *      org_node_ancestor descendants,
     *      org_node descendant_node,
     *      user_role uor,
     *      users,
     *      user_role tur,
     *      users tu
     * where 
     *      tu.user_name = {userName}
     *      and tur.user_id = tu.user_id
     *      and tur.activation_status = 'AC'
     *      and node.org_node_id = tur.org_node_id
     *      and parent.org_node_id = node.org_node_id
     *      and cat.org_node_category_id = node.org_node_category_id
     *      and cat.activation_status = 'AC'
     *      and node.org_node_id = descendants.ancestor_org_node_id
     *      and descendants.org_node_id = descendant_node.org_node_id
     *      and descendants.number_of_levels >= 0
     *      and descendant_node.activation_status = 'AC'
     *      and node.activation_status = 'AC'
     *      and uor.org_node_id (+) = descendant_node.org_node_id
     *      and NVL(uor.activation_status, 'AC') = 'AC'
     *      and users.user_id (+) = uor.user_id
     *      and NVL(users.activation_status, 'AC') = 'AC'
     * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name,
     *      parent.parent_org_node_id
     * union
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
     *     0 as userCount,
     *     count(distinct descendant_node.org_node_id) - 1 as childNodeCount,
     *     parent.parent_org_node_id as parentOrgNodeId
     * from 
     *      org_node node,
     *      org_node_parent parent, 
     *      org_node_category cat,
     *      org_node_ancestor descendants,
     *      org_node descendant_node,
     *      user_role tur,
     *      users tu
     * where 
     *      tu.user_name = {userName}
     *      and tur.user_id = tu.user_id
     *      and tur.activation_status = 'AC'
     *      and node.org_node_id = tur.org_node_id
     *      and parent.org_node_id = node.org_node_id
     *      and cat.org_node_category_id = node.org_node_category_id
     *      and cat.activation_status = 'AC'
     *      and node.org_node_id = descendants.ancestor_org_node_id
     *      and descendants.org_node_id = descendant_node.org_node_id
     *      and descendants.number_of_levels >= 0
     *      and descendant_node.activation_status = 'AC'
     *      and node.activation_status = 'AC'    
     * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name,
     *      parent.parent_org_node_id)
     * group by
     *     orgNodeId,
     *     customerId,
     *     orgNodeCategoryId,
     *     orgNodeName,
     *     extQedPin,
     *     extElmId,
     *     extOrgNodeType,
     *     orgNodeDescription,
     *     createdBy,
     *     createdDateTime,
     *     updatedBy,
     *     updatedDateTime,
     *     activationStatus,
     *     dataImportHistoryId,
     *     parentState,
     *     parentRegion,
     *     parentCounty,
     *     parentDistrict,
     *     orgNodeCode,
     *     orgNodeCategoryName,
     *     parentOrgNodeId::
     */
    @JdbcControl.SQL(statement = "select distinct  orgNodeId,  customerId,  orgNodeCategoryId, categoryLevel,  orgNodeName,  extQedPin,  extElmId,  extOrgNodeType,  orgNodeDescription,  createdBy,  createdDateTime,  updatedBy,  updatedDateTime,  activationStatus,  dataImportHistoryId,  parentState,  parentRegion,  parentCounty,  parentDistrict,  orgNodeCode,  orgNodeCategoryName,  max(userCount) as userCount,  max(childNodeCount) as childNodeCount,  parentOrgNodeId from (select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  cat.category_level as categoryLevel,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  count(distinct uor.user_id) as userCount,  0 as childNodeCount,  parent.parent_org_node_id as parentOrgNodeId from  org_node node,  org_node_parent parent,  org_node_category cat,  org_node_ancestor descendants,  org_node descendant_node,  user_role uor,  users,  user_role tur,  users tu where  tu.user_name = {userName}  and tur.user_id = tu.user_id  and tur.activation_status = 'AC'  and node.org_node_id = tur.org_node_id  and parent.org_node_id = node.org_node_id  and cat.org_node_category_id = node.org_node_category_id  and cat.activation_status = 'AC'  and node.org_node_id = descendants.ancestor_org_node_id  and descendants.org_node_id = descendant_node.org_node_id  and descendants.number_of_levels >= 0  and descendant_node.activation_status = 'AC'  and node.activation_status = 'AC'  and uor.org_node_id (+) = descendant_node.org_node_id  and NVL(uor.activation_status, 'AC') = 'AC'  and users.user_id (+) = uor.user_id  and NVL(users.activation_status, 'AC') = 'AC' group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  cat.category_level,	node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name,  parent.parent_org_node_id union select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  cat.category_level as categoryLevel,	node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  0 as userCount,  count(distinct descendant_node.org_node_id) - 1 as childNodeCount,  parent.parent_org_node_id as parentOrgNodeId from  org_node node,  org_node_parent parent,  org_node_category cat,  org_node_ancestor descendants,  org_node descendant_node,  user_role tur,  users tu where  tu.user_name = {userName}  and tur.user_id = tu.user_id  and tur.activation_status = 'AC'  and node.org_node_id = tur.org_node_id  and parent.org_node_id = node.org_node_id  and cat.org_node_category_id = node.org_node_category_id  and cat.activation_status = 'AC'  and node.org_node_id = descendants.ancestor_org_node_id  and descendants.org_node_id = descendant_node.org_node_id  and descendants.number_of_levels >= 0  and descendant_node.activation_status = 'AC'  and node.activation_status = 'AC'  group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  cat.category_level,	node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name,  parent.parent_org_node_id) group by  orgNodeId,  customerId,  orgNodeCategoryId,  categoryLevel,	orgNodeName,  extQedPin,  extElmId,  extOrgNodeType,  orgNodeDescription,  createdBy,  createdDateTime,  updatedBy,  updatedDateTime,  activationStatus,  dataImportHistoryId,  parentState,  parentRegion,  parentCounty,  parentDistrict,  orgNodeCode,  orgNodeCategoryName,  parentOrgNodeId")
    UserNode[] getTopUserNodesForUser(String userName) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct
     *     orgNodeId,
     *     customerId,
     *     orgNodeCategoryId,
     *     orgNodeName,
     *     extQedPin,
     *     extElmId,
     *     extOrgNodeType,
     *     orgNodeDescription,
     *     createdBy,
     *     createdDateTime,
     *     updatedBy,
     *     updatedDateTime,
     *     activationStatus,
     *     dataImportHistoryId,
     *     parentState,
     *     parentRegion,
     *     parentCounty,
     *     parentDistrict,
     *     orgNodeCode,
     *     orgNodeCategoryName,
     *     max(userCount) as userCount,
     *     max(childNodeCount) as childNodeCount,
     *     parentOrgNodeId
     * from (select distinct
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
     *     count(distinct uor.user_id) as userCount,
     *     0 as childNodeCount,
     *     parent.parent_org_node_id as parentOrgNodeId
     * from 
     *      org_node node,
     *      org_node_parent parent, 
     *      org_node_category cat,
     *      org_node_ancestor descendants,
     *      org_node descendant_node,
     *      user_role uor,
     *      users
     * where 
     *      parent.org_node_id = node.org_node_id
     *      and cat.org_node_category_id = node.org_node_category_id
     *      and cat.activation_status = 'AC'
     *      and parent.parent_org_node_id = {parentOrgNodeId}
     *      and node.org_node_id = descendants.ancestor_org_node_id
     *      and descendants.org_node_id = descendant_node.org_node_id
     *      and descendants.number_of_levels >= 0
     *      and descendant_node.activation_status = 'AC'
     *      and node.activation_status = 'AC'
     *      and uor.org_node_id (+) = descendant_node.org_node_id
     *      and NVL(uor.activation_status, 'AC') = 'AC'
     *      and users.user_id (+) = uor.user_id
     *      and NVL(users.activation_status, 'AC') = 'AC'
     * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name,
     *      parent.parent_org_node_id
     * union
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
     *     0 as userCount,
     *     count(distinct descendant_node.org_node_id) - 1 as childNodeCount,
     *     parent.parent_org_node_id as parentOrgNodeId
     * from 
     *      org_node node,
     *      org_node_parent parent, 
     *      org_node_category cat,
     *      org_node_ancestor descendants,
     *      org_node descendant_node
     * where 
     *      parent.org_node_id = node.org_node_id
     *      and cat.org_node_category_id = node.org_node_category_id
     *      and cat.activation_status = 'AC'
     *      and parent.parent_org_node_id = {parentOrgNodeId}
     *      and node.org_node_id = descendants.ancestor_org_node_id
     *      and descendants.org_node_id = descendant_node.org_node_id
     *      and descendants.number_of_levels >= 0
     *      and descendant_node.activation_status = 'AC'
     *      and node.activation_status = 'AC'    
     * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name,
     *      parent.parent_org_node_id)
     * group by
     *     orgNodeId,
     *     customerId,
     *     orgNodeCategoryId,
     *     orgNodeName,
     *     extQedPin,
     *     extElmId,
     *     extOrgNodeType,
     *     orgNodeDescription,
     *     createdBy,
     *     createdDateTime,
     *     updatedBy,
     *     updatedDateTime,
     *     activationStatus,
     *     dataImportHistoryId,
     *     parentState,
     *     parentRegion,
     *     parentCounty,
     *     parentDistrict,
     *     orgNodeCode,
     *     orgNodeCategoryName,
     *     parentOrgNodeId::
     *     array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  orgNodeId,  customerId,  orgNodeCategoryId,  orgNodeName,  extQedPin,  extElmId,  extOrgNodeType,  orgNodeDescription,  createdBy,  createdDateTime,  updatedBy,  updatedDateTime,  activationStatus,  dataImportHistoryId,  parentState,  parentRegion,  parentCounty,  parentDistrict,  orgNodeCode,  orgNodeCategoryName,  max(userCount) as userCount,  max(childNodeCount) as childNodeCount,  parentOrgNodeId from (select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  count(distinct uor.user_id) as userCount,  0 as childNodeCount,  parent.parent_org_node_id as parentOrgNodeId from  org_node node,  org_node_parent parent,  org_node_category cat,  org_node_ancestor descendants,  org_node descendant_node,  user_role uor,  users where  parent.org_node_id = node.org_node_id  and cat.org_node_category_id = node.org_node_category_id  and cat.activation_status = 'AC'  and parent.parent_org_node_id = {parentOrgNodeId}  and node.org_node_id = descendants.ancestor_org_node_id  and descendants.org_node_id = descendant_node.org_node_id  and descendants.number_of_levels >= 0  and descendant_node.activation_status = 'AC'  and node.activation_status = 'AC'  and uor.org_node_id (+) = descendant_node.org_node_id  and NVL(uor.activation_status, 'AC') = 'AC'  and users.user_id (+) = uor.user_id  and NVL(users.activation_status, 'AC') = 'AC' group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name,  parent.parent_org_node_id union select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  0 as userCount,  count(distinct descendant_node.org_node_id) - 1 as childNodeCount,  parent.parent_org_node_id as parentOrgNodeId from  org_node node,  org_node_parent parent,  org_node_category cat,  org_node_ancestor descendants,  org_node descendant_node where  parent.org_node_id = node.org_node_id  and cat.org_node_category_id = node.org_node_category_id  and cat.activation_status = 'AC'  and parent.parent_org_node_id = {parentOrgNodeId}  and node.org_node_id = descendants.ancestor_org_node_id  and descendants.org_node_id = descendant_node.org_node_id  and descendants.number_of_levels >= 0  and descendant_node.activation_status = 'AC'  and node.activation_status = 'AC'  group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name,  parent.parent_org_node_id) group by  orgNodeId,  customerId,  orgNodeCategoryId,  orgNodeName,  extQedPin,  extElmId,  extOrgNodeType,  orgNodeDescription,  createdBy,  createdDateTime,  updatedBy,  updatedDateTime,  activationStatus,  dataImportHistoryId,  parentState,  parentRegion,  parentCounty,  parentDistrict,  orgNodeCode,  orgNodeCategoryName,  parentOrgNodeId",
                     arrayMaxLength = 100000)
    UserNode[] getUserNodesForParent(Integer parentOrgNodeId) throws SQLException;

    /**
     * This query brings down details of all top nodes for the user.
     * @jc:sql statement::
     * select distinct
     *     orgNodeId,
     *     customerId,
     *     orgNodeCategoryId,
     *     orgNodeName,
     *     extQedPin,
     *     extElmId,
     *     extOrgNodeType,
     *     orgNodeDescription,
     *     createdBy,
     *     createdDateTime,
     *     updatedBy,
     *     updatedDateTime,
     *     activationStatus,
     *     dataImportHistoryId,
     *     parentState,
     *     parentRegion,
     *     parentCounty,
     *     parentDistrict,
     *     orgNodeCode,
     *     orgNodeCategoryName,
     *     max(studentCount) as studentCount,
     *     max(childNodeCount) as childNodeCount
     * from (select distinct
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
     *     count(distinct ons.student_id) as studentCount,
     *     0 as childNodeCount
     * from 
     *      org_node node,
     *      org_node_category cat,
     *      org_node_ancestor descendants,
     *      org_node descendant_node,
     *      org_node_student ons,
     *      student,
     *      user_role tur,
     *      users tu
     * where 
     *      cat.org_node_category_id = node.org_node_category_id
     *      and node.org_node_id = tur.org_node_id
     *      and tur.activation_status = 'AC'
     *      and tur.user_id = tu.user_id
     *      and tu.user_name = {userName}
     *      and node.org_node_id = descendants.ancestor_org_node_id
     *      and descendants.org_node_id = descendant_node.org_node_id
     *      and descendant_node.activation_status = 'AC'
     *      and ons.org_node_id (+) = descendant_node.org_node_id
     *      and NVL(ons.activation_status, 'AC') = 'AC'
     *      and student.student_id (+) = ons.student_id
     *      and NVL(student.activation_status, 'AC') = 'AC'
     * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name
     * union
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
     *     0 as studentCount,
     *     count(distinct descendant_node.org_node_id) - 1 as childNodeCount
     * from 
     *      org_node node,
     *      org_node_category cat,
     *      org_node_ancestor descendants,
     *      org_node descendant_node,
     *      user_role tur,
     *      users tu
     * where 
     *      cat.org_node_category_id = node.org_node_category_id
     *      and node.org_node_id = tur.org_node_id
     *      and tur.activation_status = 'AC'
     *      and tur.user_id = tu.user_id
     *      and tu.user_name = {userName}
     *      and node.org_node_id = descendants.ancestor_org_node_id
     *      and descendants.org_node_id = descendant_node.org_node_id
     *      and descendant_node.activation_status = 'AC'
     * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name)
     * group by
     *     orgNodeId,
     *     customerId,
     *     orgNodeCategoryId,
     *     orgNodeName,
     *     extQedPin,
     *     extElmId,
     *     extOrgNodeType,
     *     orgNodeDescription,
     *     createdBy,
     *     createdDateTime,
     *     updatedBy,
     *     updatedDateTime,
     *     activationStatus,
     *     dataImportHistoryId,
     *     parentState,
     *     parentRegion,
     *     parentCounty,
     *     parentDistrict,
     *     orgNodeCode,
     *     orgNodeCategoryName::
     */
    @JdbcControl.SQL(statement = "select distinct  orgNodeId,  customerId,  orgNodeCategoryId,  orgNodeName,  extQedPin,  extElmId,  extOrgNodeType,  orgNodeDescription,  createdBy,  createdDateTime,  updatedBy,  updatedDateTime,  activationStatus,  dataImportHistoryId,  parentState,  parentRegion,  parentCounty,  parentDistrict,  orgNodeCode,  orgNodeCategoryName,  max(studentCount) as studentCount,  max(childNodeCount) as childNodeCount from (select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  count(distinct ons.student_id) as studentCount,  0 as childNodeCount from  org_node node,  org_node_category cat,  org_node_ancestor descendants,  org_node descendant_node,  org_node_student ons,  student,  user_role tur,  users tu where  cat.org_node_category_id = node.org_node_category_id  and node.org_node_id = tur.org_node_id  and tur.activation_status = 'AC'  and tur.user_id = tu.user_id  and tu.user_name = {userName}  and node.org_node_id = descendants.ancestor_org_node_id  and descendants.org_node_id = descendant_node.org_node_id  and descendant_node.activation_status = 'AC'  and ons.org_node_id (+) = descendant_node.org_node_id  and NVL(ons.activation_status, 'AC') = 'AC'  and student.student_id (+) = ons.student_id  and NVL(student.activation_status, 'AC') = 'AC' group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name union select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  0 as studentCount,  count(distinct descendant_node.org_node_id) - 1 as childNodeCount from  org_node node,  org_node_category cat,  org_node_ancestor descendants,  org_node descendant_node,  user_role tur,  users tu where  cat.org_node_category_id = node.org_node_category_id  and node.org_node_id = tur.org_node_id  and tur.activation_status = 'AC'  and tur.user_id = tu.user_id  and tu.user_name = {userName}  and node.org_node_id = descendants.ancestor_org_node_id  and descendants.org_node_id = descendant_node.org_node_id  and descendant_node.activation_status = 'AC' group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name) group by  orgNodeId,  customerId,  orgNodeCategoryId,  orgNodeName,  extQedPin,  extElmId,  extOrgNodeType,  orgNodeDescription,  createdBy,  createdDateTime,  updatedBy,  updatedDateTime,  activationStatus,  dataImportHistoryId,  parentState,  parentRegion,  parentCounty,  parentDistrict,  orgNodeCode,  orgNodeCategoryName")
    StudentNode[] getTopStudentNodesForUser(String userName) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct
     *     orgNodeId,
     *     customerId,
     *     orgNodeCategoryId,
     *     orgNodeName,
     *     extQedPin,
     *     extElmId,
     *     extOrgNodeType,
     *     orgNodeDescription,
     *     createdBy,
     *     createdDateTime,
     *     updatedBy,
     *     updatedDateTime,
     *     activationStatus,
     *     dataImportHistoryId,
     *     parentState,
     *     parentRegion,
     *     parentCounty,
     *     parentDistrict,
     *     orgNodeCode,
     *     orgNodeCategoryName,
     *     max(studentCount) as studentCount,
     *     max(childNodeCount) as childNodeCount
     * from (select distinct
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
     *     count(distinct ons.student_id) as studentCount,
     *     0 as childNodeCount
     * from 
     *      org_node node,
     *      org_node_parent parent, 
     *      org_node_category cat,
     *      org_node_ancestor descendants,
     *      org_node descendant_node,
     *      org_node_student ons,
     *      student
     * where 
     *      parent.org_node_id = node.org_node_id
     *      and cat.org_node_category_id = node.org_node_category_id
     *      and parent.parent_org_node_id = {parentOrgNodeId}
     *      and node.org_node_id = descendants.ancestor_org_node_id
     *      and descendants.org_node_id = descendant_node.org_node_id
     *      and descendant_node.activation_status = 'AC'
     *      and ons.org_node_id (+) = descendant_node.org_node_id
     *      and NVL(ons.activation_status, 'AC') = 'AC'
     *      and student.student_id (+) = ons.student_id
     *      and NVL(student.activation_status, 'AC') = 'AC'
     * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name
     * union
     * select distinct
     *    node.org_node_id as orgNodeId,
     *    node.customer_id as customerId,
     *    node.org_node_category_id as orgNodeCategoryId,
     *    node.org_node_name as orgNodeName,
     *    node.ext_qed_pin as extQedPin,
     *    node.ext_elm_id as extElmId,
     *    node.ext_org_node_type as extOrgNodeType,
     *    node.org_node_description as orgNodeDescription,
     *    node.created_by as createdBy,
     *    node.created_date_time as createdDateTime,
     *    node.updated_by as updatedBy,
     *    node.updated_date_time as updatedDateTime,
     *    node.activation_status as activationStatus,
     *    node.data_import_history_id as dataImportHistoryId,
     *    node.parent_state as parentState,
     *    node.parent_region as parentRegion,
     *    node.parent_county as parentCounty,
     *    node.parent_district as parentDistrict,
     *    node.org_node_code as orgNodeCode,
     *    cat.category_name as orgNodeCategoryName,
     *    0 as studentCount,
     *    count(distinct descendant_node.org_node_id) - 1 as childNodeCount
     * from 
     *     org_node node,
     *     org_node_parent parent, 
     *     org_node_category cat,
     *     org_node_ancestor descendants,
     *     org_node descendant_node
     * where 
     *     parent.org_node_id = node.org_node_id
     *     and cat.org_node_category_id = node.org_node_category_id
     *     and parent.parent_org_node_id = {parentOrgNodeId}
     *     and node.org_node_id = descendants.ancestor_org_node_id
     *     and descendants.org_node_id = descendant_node.org_node_id
     *     and descendant_node.activation_status = 'AC'
     * group by
     *     node.org_node_id,
     *     node.customer_id,
     *     node.org_node_category_id,
     *     node.org_node_name,
     *     node.ext_qed_pin,
     *     node.ext_elm_id,
     *     node.ext_org_node_type,
     *     node.org_node_description,
     *     node.created_by,
     *     node.created_date_time,
     *     node.updated_by,
     *     node.updated_date_time,
     *     node.activation_status,
     *     node.data_import_history_id,
     *     node.parent_state,
     *     node.parent_region,
     *     node.parent_county,
     *     node.parent_district,
     *     node.org_node_code,
     *     cat.category_name)
     * group by
     *     orgNodeId,
     *     customerId,
     *     orgNodeCategoryId,
     *     orgNodeName,
     *     extQedPin,
     *     extElmId,
     *     extOrgNodeType,
     *     orgNodeDescription,
     *     createdBy,
     *     createdDateTime,
     *     updatedBy,
     *     updatedDateTime,
     *     activationStatus,
     *     dataImportHistoryId,
     *     parentState,
     *     parentRegion,
     *     parentCounty,
     *     parentDistrict,
     *     orgNodeCode,
     *     orgNodeCategoryName::
     */
    @JdbcControl.SQL(statement = "select distinct  orgNodeId,  customerId,  orgNodeCategoryId,  orgNodeName,  extQedPin,  extElmId,  extOrgNodeType,  orgNodeDescription,  createdBy,  createdDateTime,  updatedBy,  updatedDateTime,  activationStatus,  dataImportHistoryId,  parentState,  parentRegion,  parentCounty,  parentDistrict,  orgNodeCode,  orgNodeCategoryName,  max(studentCount) as studentCount,  max(childNodeCount) as childNodeCount from (select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  count(distinct ons.student_id) as studentCount,  0 as childNodeCount from  org_node node,  org_node_parent parent,  org_node_category cat,  org_node_ancestor descendants,  org_node descendant_node,  org_node_student ons,  student where  parent.org_node_id = node.org_node_id  and cat.org_node_category_id = node.org_node_category_id  and parent.parent_org_node_id = {parentOrgNodeId}  and node.org_node_id = descendants.ancestor_org_node_id  and descendants.org_node_id = descendant_node.org_node_id  and descendant_node.activation_status = 'AC'  and ons.org_node_id (+) = descendant_node.org_node_id  and NVL(ons.activation_status, 'AC') = 'AC'  and student.student_id (+) = ons.student_id  and NVL(student.activation_status, 'AC') = 'AC' group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name union select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  0 as studentCount,  count(distinct descendant_node.org_node_id) - 1 as childNodeCount from  org_node node,  org_node_parent parent,  org_node_category cat,  org_node_ancestor descendants,  org_node descendant_node where  parent.org_node_id = node.org_node_id  and cat.org_node_category_id = node.org_node_category_id  and parent.parent_org_node_id = {parentOrgNodeId}  and node.org_node_id = descendants.ancestor_org_node_id  and descendants.org_node_id = descendant_node.org_node_id  and descendant_node.activation_status = 'AC' group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name) group by  orgNodeId,  customerId,  orgNodeCategoryId,  orgNodeName,  extQedPin,  extElmId,  extOrgNodeType,  orgNodeDescription,  createdBy,  createdDateTime,  updatedBy,  updatedDateTime,  activationStatus,  dataImportHistoryId,  parentState,  parentRegion,  parentCounty,  parentDistrict,  orgNodeCode,  orgNodeCategoryName")
    StudentNode[] getStudentNodesForParent(Integer parentOrgNodeId) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     *          node.org_node_id as orgNodeId,
     *          node.customer_id as customerId,
     *          node.org_node_category_id as orgNodeCategoryId,
     *          node.org_node_name as orgNodeName,
     *          node.ext_qed_pin as extQedPin,
     *          node.ext_elm_id as extElmId,
     *          node.ext_org_node_type as extOrgNodeType,
     *          node.org_node_description as orgNodeDescription,
     *          node.created_by as createdBy,
     *          node.created_date_time as createdDateTime,
     *          node.updated_by as updatedBy,
     *          node.updated_date_time as updatedDateTime,
     *          node.activation_status as activationStatus,
     *          node.data_import_history_id as dataImportHistoryId,
     *          node.parent_state as parentState,
     *          node.parent_region as parentRegion,
     *          node.parent_county as parentCounty,
     *          node.parent_district as parentDistrict,
     *          node.org_node_code as orgNodeCode
     *      from
     *           org_node node,
     *           org_node_category onc
     *      where
     *           
     *      	 node.activation_status = 'AC'
     *           and {sql:fn in(node.org_node_id,{orgNodeIds})}
     *           and onc.org_node_category_id = node.org_node_category_id
     *           order by node.org_node_name asc::
     */
    @JdbcControl.SQL(statement = "select  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode  from  org_node node,  org_node_category onc  where  \t node.activation_status = 'AC'  and {sql:fn in(node.org_node_id,{orgNodeIds})}  and onc.org_node_category_id = node.org_node_category_id  order by node.org_node_name asc")
    Node[] getAssignedNodeForUser(Long []orgNodeIds) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct
     *          node.org_node_id as orgNodeId,
     *          node.customer_id as customerId,
     *          node.org_node_category_id as orgNodeCategoryId,
     *          node.org_node_name as orgNodeName,
     *          node.ext_qed_pin as extQedPin,
     *          node.ext_elm_id as extElmId,
     *          node.ext_org_node_type as extOrgNodeType,
     *          node.org_node_description as orgNodeDescription,
     *          node.created_by as createdBy,
     *          node.created_date_time as createdDateTime,
     *          node.updated_by as updatedBy,
     *          node.updated_date_time as updatedDateTime,
     *          node.activation_status as activationStatus,
     *          node.data_import_history_id as dataImportHistoryId,
     *          node.parent_state as parentState,
     *          node.parent_region as parentRegion,
     *          node.parent_county as parentCounty,
     *          node.parent_district as parentDistrict,
     *          node.org_node_code as orgNodeCode
     *      from
     *           user_role ur,
     *           org_node node,
     *           org_node_category onc
     *      where
     *           ur.user_id = {userId} 
     *      	 and ur.org_node_id = node.org_node_id
     *      	 and ur.activation_status = 'AC'
     *           and ur.org_node_id in (select distinct ona.org_node_id from org_node_ancestor ona 
     *           where {sql: searchCriteria})
     *     	 )
     *           and onc.org_node_category_id = node.org_node_category_id
     *           order by node.org_node_name asc::
     */
    @JdbcControl.SQL(statement = "select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode, getOrghierarchy(node.org_node_id) as leafNodePath    from  user_role ur,  org_node node,  org_node_category onc  where  ur.user_id = {userId}  \t and ur.org_node_id = node.org_node_id  \t and ur.activation_status = 'AC'  and ur.org_node_id in (select distinct ona.org_node_id from org_node_ancestor ona  where {sql: searchCriteria}  \t )  and onc.org_node_category_id = node.org_node_category_id  order by node.org_node_name asc")
    Node[] getAssignedBelowOrgNodes(Integer userId, String  searchCriteria) throws SQLException;
    
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
     * 	 and node.activation_status = 'AC'
     * 	 and cat.activation_status = 'AC'
     * order by ona.number_of_levels desc::
     */
    @JdbcControl.SQL(statement = "select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName, \t  ona.number_of_levels as numberOfLevels, getOrghierarchy(node.org_node_id) as leafNodePath from  org_node node, org_node_category cat, org_node_ancestor ona where  \t cat.org_node_category_id = node.org_node_category_id \t and ona.ancestor_org_node_id = node.org_node_id \t and ona.org_node_id = {orgNodeId}  and node.org_node_id in  \t \t (select distinct ona1.org_node_id  \t \t from org_node_ancestor ona1 where {sql: searchCriteria} \t ) \t and node.activation_status = 'AC' \t and cat.activation_status = 'AC' order by ona.number_of_levels desc")
    UserNode[] getAncestorOrganizationNodesForOrgNodeAtAndBelowTopOrgNodes(Integer orgNodeId, String searchCriteria);


    @JdbcControl.SQL(statement = "select distinct  ona1.ancestor_org_node_id from  org_node_ancestor ona1 where  ona1.ancestor_org_node_id = {orgNodeIds}  minus  select distinct  ona2.org_node_id  from  org_node_ancestor ona2  where ona2.org_node_id = {orgNodeIds}  and  ona2.ancestor_org_node_id = {orgNodeIds}  and ona2.org_node_id != ona2.ancestor_org_node_id  ")
    Integer [] getTopOrgNodesInList(Integer orgNodeIds);

    /**
     * @jc:sql statement::
     * SELECT org.org_node_category_id as orgNodeCategoryId,
     *            org.customer_id          as customerId,
     *            org.category_level       as categoryLevel,
     *            org.category_name        as categoryName,
     *            org.is_group             as isGroup,
     *            org.created_by           as createdBy,
     *            org.created_date_time    as createdDateTime,
     *            org.updated_by           as updatedBy,
     *            org.updated_date_time    as updatedDateTime,
     *            org.activation_status    as activationStatus
     *       FROM org_Node_Category org, org_node onode
     *      WHERE onode.org_node_category_id = org.org_node_category_id
     *        AND onode.org_node_id = {orgNodeId}
     *        AND org.is_group = 'F'
     *        AND org.activation_status = 'AC'
     *        AND onode.activation_status = 'AC'::
     */
    @JdbcControl.SQL(statement = "SELECT org.org_node_category_id as orgNodeCategoryId,  org.customer_id  as customerId,  org.category_level  as categoryLevel,  org.category_name  as categoryName,  org.is_group  as isGroup,  org.created_by  as createdBy,  org.created_date_time  as createdDateTime,  org.updated_by  as updatedBy,  org.updated_date_time  as updatedDateTime,  org.activation_status  as activationStatus  FROM org_Node_Category org, org_node onode  WHERE onode.org_node_category_id = org.org_node_category_id  AND onode.org_node_id = {orgNodeId}  AND org.is_group = 'F'  AND org.activation_status = 'AC'  AND onode.activation_status = 'AC'")
    OrgNodeCategory getOrgNodeCategories(Integer orgNodeId) throws SQLException;

    /**
     * @jc:sql statement::
     * SELECT DISTINCT n1.org_Node_Id            as orgNodeId,
     *                  n1.customer_Id            as customerId,
     *                  n1.org_Node_Category_Id   as orgNodeCategoryId,
     *                  n1.org_Node_Name          as orgNodeName,
     *                  n1.ext_Qed_Pin            as extQedPin,
     *                  n1.ext_Elm_Id             as extElmId,
     *                  n1.ext_Org_Node_Type      as extOrgNodeType,
     *                  n1.org_Node_Description   as orgNodeDescription,
     *                  n1.created_By             as createdBy,
     *                  n1.created_Date_Time      as createdDateTime,
     *                  n1.updated_By             as updatedBy,
     *                  n1.updated_Date_Time      as updatedDateTime,
     *                  n1.activation_Status      as activationStatus,
     *                  n1.data_Import_History_Id as dataImportHistoryId,
     *                  n1.parent_State           as parentState,
     *                  n1.parent_Region          as parentRegion,
     *                  n1.parent_County          as parentCounty,
     *                  n1.parent_district        as parentDistrict,
     *                  n1.org_Node_Code          as orgNodeCode
     *     FROM org_node n1, Org_Node_Parent p, org_node n2
     *   WHERE n1.customer_Id = {customerId}
     *     AND n1.org_Node_Id = p.org_Node_Id
     *     AND p.parent_Org_Node_Id = n2.org_Node_Id
     *     AND n2.customer_Id <> n1.customer_Id::
     */
    @JdbcControl.SQL(statement = "SELECT DISTINCT n1.org_Node_Id  as orgNodeId,  n1.customer_Id  as customerId,  n1.org_Node_Category_Id  as orgNodeCategoryId,  n1.org_Node_Name  as orgNodeName,  n1.ext_Qed_Pin  as extQedPin,  n1.ext_Elm_Id  as extElmId,  n1.ext_Org_Node_Type  as extOrgNodeType,  n1.org_Node_Description  as orgNodeDescription,  n1.created_By  as createdBy,  n1.created_Date_Time  as createdDateTime,  n1.updated_By  as updatedBy,  n1.updated_Date_Time  as updatedDateTime,  n1.activation_Status  as activationStatus,  n1.data_Import_History_Id as dataImportHistoryId,  n1.parent_State  as parentState,  n1.parent_Region  as parentRegion,  n1.parent_County  as parentCounty,  n1.parent_district  as parentDistrict,  n1.org_Node_Code  as orgNodeCode  FROM org_node n1, Org_Node_Parent p, org_node n2  WHERE n1.customer_Id = {customerId}  AND n1.org_Node_Id = p.org_Node_Id  AND p.parent_Org_Node_Id = n2.org_Node_Id  AND n2.customer_Id <> n1.customer_Id")
    Node getTopOrgNodeForCustomer(Integer customerId) throws SQLException;
    
    
    /**
     * @jc:sql statement::
     * SELECT DISTINCT n2.org_Node_Id            as orgNodeId,
     *                  n2.customer_Id            as customerId,
     *                  n2.org_Node_Category_Id   as orgNodeCategoryId,
     *                  n2.org_Node_Name          as orgNodeName,
     *                  n2.ext_Qed_Pin            as extQedPin,
     *                  n2.ext_Elm_Id             as extElmId,
     *                  n2.ext_Org_Node_Type      as extOrgNodeType,
     *                  n2.org_Node_Description   as orgNodeDescription,
     *                  n2.created_By             as createdBy,
     *                  n2.created_Date_Time      as createdDateTime,
     *                  n2.updated_By             as updatedBy,
     *                  n2.updated_Date_Time      as updatedDateTime,
     *                  n2.activation_Status      as activationStatus,
     *                  n2.data_Import_History_Id as dataImportHistoryId,
     *                  n2.parent_State           as parentState,
     *                  n2.parent_Region          as parentRegion,
     *                  n2.parent_County          as parentCounty,
     *                  n2.parent_district        as parentDistrict,
     *                  n2.org_Node_Code          as orgNodeCode
     *     FROM org_node n1, Org_Node_Parent p, org_node n2
     *   WHERE n1.customer_Id = {customerId}
     *     AND n1.org_Node_Id = p.org_Node_Id
     *     AND p.parent_Org_Node_Id = n2.org_Node_Id
     *     AND n2.customer_Id <> n1.customer_Id::
     */
    @JdbcControl.SQL(statement = "SELECT DISTINCT n2.org_Node_Id  as orgNodeId,  n2.customer_Id  as customerId,  n2.org_Node_Category_Id  as orgNodeCategoryId,  n2.org_Node_Name  as orgNodeName,  n2.ext_Qed_Pin  as extQedPin,  n2.ext_Elm_Id  as extElmId,  n2.ext_Org_Node_Type  as extOrgNodeType,  n2.org_Node_Description  as orgNodeDescription,  n2.created_By  as createdBy,  n2.created_Date_Time  as createdDateTime,  n2.updated_By  as updatedBy,  n2.updated_Date_Time  as updatedDateTime,  n2.activation_Status  as activationStatus,  n2.data_Import_History_Id as dataImportHistoryId,  n2.parent_State  as parentState,  n2.parent_Region  as parentRegion,  n2.parent_County  as parentCounty,  n2.parent_district  as parentDistrict,  n2.org_Node_Code  as orgNodeCode  FROM org_node n1, Org_Node_Parent p, org_node n2  WHERE n1.customer_Id = {customerId}  AND n1.org_Node_Id = p.org_Node_Id  AND p.parent_Org_Node_Id = n2.org_Node_Id  AND n2.customer_Id <> n1.customer_Id")
    Node getParentOrgNodeForCustomer(Integer customerId) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct ona.org_node_id          as orgNodeId,
     *                 o.customer_id            as customerId,
     *                 o.org_node_category_id   as orgNodeCategoryId,
     *                 o.org_node_name          as orgNodeName,
     *                 o.ext_qed_pin            as extQedPin,
     *                 o.ext_elm_id             as extElmId,
     *                 o.ext_org_node_type      as extOrgNodeType,
     *                 o.org_node_description   as orgNodeDescription,
     *                 o.created_by             as createdBy,
     *                 o.created_date_time      as createdDateTime,
     *                 o.updated_by             as updatedBy,
     *                 o.updated_date_time      as updatedDateTime,
     *                 o.activation_status      as activationStatus,
     *                 o.data_import_history_id as dataImportHistoryId,
     *                 o.parent_state           as parentState,
     *                 o.parent_region          as parentRegion,
     *                 o.parent_county          as parentCounty,
     *                 o.parent_district        as parentDistrict,
     *                 o.org_node_code          as orgNodeCode
     *   from user_role ur, org_node o, org_node_ancestor ona
     *  where ur.user_id = {userId}
     *    and ur.activation_status = 'AC'
     *    and ur.org_node_id = ona.org_node_id
     *    and o.org_node_id = ur.org_node_id
     *    and ona.org_node_id not in
     *        (select ona.org_node_id
     *           from user_role ur, org_node_ancestor ona
     *          where ur.user_id = {userId}
     *            and ur.activation_status = 'AC'
     *            and ur.org_node_id = ona.ancestor_org_node_id
     *            and ona.number_of_levels > 0)
     *  order by o.org_node_category_id asc, o.org_node_name::
     */
    @JdbcControl.SQL(statement = "select distinct ona.org_node_id  as orgNodeId,  o.customer_id  as customerId,  o.org_node_category_id  as orgNodeCategoryId,  o.org_node_name  as orgNodeName,  o.ext_qed_pin  as extQedPin,  o.ext_elm_id  as extElmId,  o.ext_org_node_type  as extOrgNodeType,  o.org_node_description  as orgNodeDescription,  o.created_by  as createdBy,  o.created_date_time  as createdDateTime,  o.updated_by  as updatedBy,  o.updated_date_time  as updatedDateTime,  o.activation_status  as activationStatus,  o.data_import_history_id as dataImportHistoryId,  o.parent_state  as parentState,  o.parent_region  as parentRegion,  o.parent_county  as parentCounty,  o.parent_district  as parentDistrict,  o.org_node_code  as orgNodeCode  from user_role ur, org_node o, org_node_ancestor ona  where ur.user_id = {userId}  and ur.activation_status = 'AC'  and ur.org_node_id = ona.org_node_id  and o.org_node_id = ur.org_node_id  and ona.org_node_id not in  (select ona.org_node_id  from user_role ur, org_node_ancestor ona  where ur.user_id = {userId}  and ur.activation_status = 'AC'  and ur.org_node_id = ona.ancestor_org_node_id  and ona.number_of_levels > 0)  order by o.org_node_category_id asc, o.org_node_name")
    Node getTopUserNodeForUser(Integer userId) throws SQLException ;

    /**
     * @jc:sql statement::
     * Insert into 
     *    org_node(
     *     org_node_id,
     *     org_node_category_id,
     *     org_node_name,
     *     customer_id,
     *     created_by,
     *     org_node_code,
     *     activation_status 
     *    )         
     *   values (
     *    {orgNode.orgNodeId},
     *    {orgNode.orgNodeCategoryId},
     *    {orgNode.orgNodeName},
     *    {orgNode.customerId},
     *    {orgNode.createdBy},   
     *    {orgNode.orgNodeCode},
     *    {orgNode.activationStatus} 
     *   )::
	 * Changes For LASLINK Customer
     */
    @JdbcControl.SQL(statement = "Insert into  org_node(  org_node_id,  org_node_category_id,  org_node_name,  customer_id,  created_by,  org_node_code,  activation_status, org_node_mdr_number)  values (  {orgNode.orgNodeId},  {orgNode.orgNodeCategoryId},  {orgNode.orgNodeName},  {orgNode.customerId},  {orgNode.createdBy},  {orgNode.orgNodeCode},  {orgNode.activationStatus}, {orgNode.mdrNumber} )")
    void createOrganization(Node orgNode) throws SQLException ;
    
    /**
     * @jc:sql statement::
     * select 
     * SEQ_ORG_NODE_ID.NEXTVAL 
     * from 
     * dual::
     */
    @JdbcControl.SQL(statement = "select  SEQ_ORG_NODE_ID.NEXTVAL  from  dual")
    Integer getNextPK() throws SQLException;

    /**
     * @jc:sql statement::
     * Insert into 
     *       org_node_parent (
     *        parent_org_node_id,
     *        org_node_id,
     *        customer_id,
     *        created_by
     *       )
     *       values (
     *         {orgNode.parentOrgNodeId},
     *         {orgNode.orgNodeId},
     *         {orgNode.customerId},
     *         {orgNode.createdBy}  
     *       )::
     */
    @JdbcControl.SQL(statement = "Insert into  org_node_parent (  parent_org_node_id,  org_node_id,  customer_id,  created_by  )  values (  {orgNode.parentOrgNodeId},  {orgNode.orgNodeId},  {orgNode.customerId},  {orgNode.createdBy}  )")
    void insertOrgNodeForParent(Node orgNode) throws SQLException;

    /**
     * @jc:sql statement::
     * delete from org_node_parent
     * where org_node_id = {node.orgNodeId}::
     * 
     */
    @JdbcControl.SQL(statement = "delete from org_node_parent where org_node_id = {node.orgNodeId}")
    void deleteOrgNodeParentForOrgNode(Node node) throws SQLException;

    /**
     * @jc:sql statement::
     * update org_node
     * set org_node_category_id = {node.orgNodeCategoryId},
     * org_node_name = {node.orgNodeName},
     * org_node_code = {node.orgNodeCode},
     * updated_By = {node.updatedBy},
     * updated_Date_Time = {node.updatedDateTime}
     * where org_node_id = {node.orgNodeId}::
	 * Changes For LASLINK Customer
     */
    @JdbcControl.SQL(statement = "update org_node set org_node_category_id = {node.orgNodeCategoryId}, org_node_name = {node.orgNodeName}, org_node_code = {node.orgNodeCode}, org_node_mdr_number = {node.mdrNumber}, updated_By = {node.updatedBy}, updated_Date_Time = {node.updatedDateTime} where org_node_id = {node.orgNodeId}")
    void updateOrganization(Node node) throws SQLException;

    /**
     * @jc:sql statement::
     * UPDATE ORG_NODE_TEST_CATALOG o  
     *   set o.activation_Status = 'IN',
     *       o.updated_by = {userId}  
     *   WHERE o.org_Node_Id = {orgNodeId} AND 
     *   o.activation_Status = 'AC'
     *      
     *      ::
     */
    @JdbcControl.SQL(statement = "UPDATE ORG_NODE_TEST_CATALOG o  set o.activation_Status = 'IN',  o.updated_by = {userId}  WHERE o.org_Node_Id = {orgNodeId} AND  o.activation_Status = 'AC'  ")
    void inActivateTestCatalogForOrgId(Integer orgNodeId, Integer userId) throws SQLException;
    
    
    /**
     * @jc:sql statement::
     * update org_node orgnode
     * set orgnode.activation_status = 'IN'
     * , orgnode.updated_by = {loginUserId}
     * , orgnode.updated_Date_Time = {node.updatedDateTime}
     * where orgnode.org_node_id = {node.orgNodeId}::
     */
    @JdbcControl.SQL(statement = "update org_node orgnode set orgnode.activation_status = 'IN' , orgnode.updated_by = {loginUserId} , orgnode.updated_Date_Time = {node.updatedDateTime} where orgnode.org_node_id = {node.orgNodeId}")
    void inActivateOrganization(Node node, Integer loginUserId) throws SQLException;

    /**
     * @jc:sql statement::
     * select onc1.org_node_category_id as orgNodeCategoryId,
     *        onc1.category_name        as categoryName
     * from org_node_category onc1, org_node_category onc2, org_node ond
     * where ond.org_node_id = {orgNodeId}
     *    and ond.org_node_category_id = onc2.org_node_category_id
     *    and onc1.category_level > onc2.category_level
     *    and onc1.activation_status = 'AC'
     *    and onc2.activation_status = 'AC'
     *    and onc1.customer_id =
     *        (select customer_id from org_node where org_node_id = {orgNodeId})
     * order by onc1.category_level::
     */
    @JdbcControl.SQL(statement = "select onc1.org_node_category_id as orgNodeCategoryId,  onc1.category_name  as categoryName from org_node_category onc1, org_node_category onc2, org_node ond where ond.org_node_id = {orgNodeId}  and ond.org_node_category_id = onc2.org_node_category_id  and onc1.category_level > onc2.category_level  and onc1.activation_status = 'AC'  and onc2.activation_status = 'AC'  and onc1.customer_id =  (select customer_id from org_node where org_node_id = {orgNodeId}) order by onc1.category_level")
    OrgNodeCategory[] getLevelForAddOrganization(Integer orgNodeId) throws SQLException ;

    /**
     * @jc:sql statement::
     * (select onc1.org_node_category_id as orgNodeCategoryId,
     *        onc1.category_name        as categoryName,
     *        onc1.category_level as categoryLevel
     * from org_node_category onc1,
     *        org_node_category onc2,
     *        org_node          ond,
     *        org_node_parent   onp
     * where onp.org_node_id = {orgNodeId}
     *    and ond.org_node_id = onp.parent_org_node_id
     *    and ond.org_node_category_id = onc2.org_node_category_id
     *    and onc1.activation_status = 'AC'
     *    and onc2.activation_status = 'AC'
     *    and ond.activation_status = 'AC'
     *    and onc1.category_level > onc2.category_level
     *    and onc1.customer_id =
     *        (select customer_id from org_node where org_node_id = {orgNodeId})
     * 
     * minus
     * 
     * select distinct onc1.org_node_category_id as orgNodeCategoryId,
     *        onc1.category_name        as categoryName,
     *        onc1.category_level as categoryLevel
     * from org_node_category onc1,
     *        org_node_category onc2,
     *        org_node          ond,
     *        org_node_parent   onp
     * where onp.parent_org_node_id = {orgNodeId}
     *    and ond.org_node_id = onp.org_node_id
     *    and ond.org_node_category_id = onc2.org_node_category_id
     *    and onc1.activation_status = 'AC'
     *    and onc2.activation_status = 'AC'
     *    and ond.activation_status = 'AC'
     *    and onc1.category_level >= onc2.category_level
     *    and onc1.customer_id =
     *        (select customer_id from org_node where org_node_id = {orgNodeId}))
     * order by categoryLevel::
     */
    @JdbcControl.SQL(statement = "(select onc1.org_node_category_id as orgNodeCategoryId,  onc1.category_name  as categoryName,  onc1.category_level as categoryLevel from org_node_category onc1,  org_node_category onc2,  org_node  ond,  org_node_parent  onp where onp.org_node_id = {orgNodeId}  and ond.org_node_id = onp.parent_org_node_id  and ond.org_node_category_id = onc2.org_node_category_id  and onc1.activation_status = 'AC'  and onc2.activation_status = 'AC'  and ond.activation_status = 'AC'  and onc1.category_level > onc2.category_level  and onc1.customer_id =  (select customer_id from org_node where org_node_id = {orgNodeId})  minus  select distinct onc1.org_node_category_id as orgNodeCategoryId,  onc1.category_name  as categoryName,  onc1.category_level as categoryLevel from org_node_category onc1,  org_node_category onc2,  org_node  ond,  org_node_parent  onp where onp.parent_org_node_id = {orgNodeId}  and ond.org_node_id = onp.org_node_id  and ond.org_node_category_id = onc2.org_node_category_id  and onc1.activation_status = 'AC'  and onc2.activation_status = 'AC'  and ond.activation_status = 'AC'  and onc1.category_level >= onc2.category_level  and onc1.customer_id =  (select customer_id from org_node where org_node_id = {orgNodeId})) order by categoryLevel")
    OrgNodeCategory[] getLevelForEditOrganization(Integer orgNodeId) throws SQLException;

    /**
     * @jc:sql statement::
     * (select onc1.org_node_category_id as orgNodeCategoryId,
     *        onc1.category_name        as categoryName,
     *        onc1.category_level as categoryLevel
     * from org_node_category onc1, org_node_category onc2, org_node ond
     * where ond.org_node_id = {selectedParentId}
     *    and ond.org_node_category_id = onc2.org_node_category_id
     *    and onc1.activation_status = 'AC'
     *    and onc2.activation_status = 'AC'
     *    and ond.activation_status = 'AC'
     *    and onc1.category_level > onc2.category_level
     *    and onc1.customer_id =
     *        (select customer_id from org_node where org_node_id = {selectedParentId})
     * 
     * minus
     * 
     * select distinct onc1.org_node_category_id as orgNodeCategoryId,
     *        onc1.category_name        as categoryName,
     *        onc1.category_level as categoryLevel
     * from org_node_category onc1,
     *        org_node_category onc2,
     *        org_node          ond,
     *        org_node_parent   onp
     * where onp.parent_org_node_id = {orgNodeId}
     *    and ond.org_node_id = onp.org_node_id
     *    and ond.org_node_category_id = onc2.org_node_category_id
     *    and onc1.activation_status = 'AC'
     *    and onc2.activation_status = 'AC'
     *    and ond.activation_status = 'AC'
     *    and onc1.category_level >= onc2.category_level
     *    and onc1.customer_id =
     *        (select customer_id from org_node where org_node_id = {orgNodeId}))
     * order by categoryLevel::
     */
    @JdbcControl.SQL(statement = "(select onc1.org_node_category_id as orgNodeCategoryId,  onc1.category_name  as categoryName,  onc1.category_level as categoryLevel from org_node_category onc1, org_node_category onc2, org_node ond where ond.org_node_id = {selectedParentId}  and ond.org_node_category_id = onc2.org_node_category_id  and onc1.activation_status = 'AC'  and onc2.activation_status = 'AC'  and ond.activation_status = 'AC'  and onc1.category_level > onc2.category_level  and onc1.customer_id =  (select customer_id from org_node where org_node_id = {selectedParentId})  minus  select distinct onc1.org_node_category_id as orgNodeCategoryId,  onc1.category_name  as categoryName,  onc1.category_level as categoryLevel from org_node_category onc1,  org_node_category onc2,  org_node  ond,  org_node_parent  onp where onp.parent_org_node_id = {orgNodeId}  and ond.org_node_id = onp.org_node_id  and ond.org_node_category_id = onc2.org_node_category_id  and onc1.activation_status = 'AC'  and onc2.activation_status = 'AC'  and ond.activation_status = 'AC'  and onc1.category_level >= onc2.category_level  and onc1.customer_id =  (select customer_id from org_node where org_node_id = {orgNodeId})) order by categoryLevel")
    OrgNodeCategory[] getLevelForEditOrganizationWithParent(Integer orgNodeId, Integer selectedParentId) throws SQLException;

    
    /**
     * @jc:sql statement::
     * (select onc1.org_node_category_id as orgNodeCategoryId,
     *       onc1.category_name        as categoryName,
     *       onc1.category_level as categoryLevel
     *  from org_node_category onc1,  org_node ond
     *  where onc1.category_level > 0
     *  and onc1.activation_status = 'AC'
     *  and ond.activation_status = 'AC'
     *  and onc1.customer_id =
     *      (select customer_id from org_node where org_node_id =  {orgNodeId})
     * 
     * minus
     * 
     * select distinct onc1.org_node_category_id as orgNodeCategoryId,
     *        onc1.category_name        as categoryName,
     *        onc1.category_level as categoryLevel
     * from org_node_category onc1,
     *        org_node_category onc2,
     *        org_node          ond,
     *        org_node_parent   onp
     * where onp.parent_org_node_id = {orgNodeId}
     *    and ond.org_node_id = onp.org_node_id
     *    and ond.org_node_category_id = onc2.org_node_category_id
     *    and onc1.activation_status = 'AC'
     *    and onc2.activation_status = 'AC'
     *    and ond.activation_status = 'AC'
     *    and onc1.category_level >= onc2.category_level
     *    and onc1.customer_id =
     *        (select customer_id from org_node where org_node_id = {orgNodeId}))
     * order by categoryLevel::
     */
    @JdbcControl.SQL(statement = "(select onc1.org_node_category_id as orgNodeCategoryId,  onc1.category_name  as categoryName,  onc1.category_level as categoryLevel  from org_node_category onc1,  org_node ond  where onc1.category_level > 0  and onc1.activation_status = 'AC'  and ond.activation_status = 'AC'  and onc1.customer_id =  (select customer_id from org_node where org_node_id =  {orgNodeId})  minus  select distinct onc1.org_node_category_id as orgNodeCategoryId,  onc1.category_name  as categoryName,  onc1.category_level as categoryLevel from org_node_category onc1,  org_node_category onc2,  org_node  ond,  org_node_parent  onp where onp.parent_org_node_id = {orgNodeId}  and ond.org_node_id = onp.org_node_id  and ond.org_node_category_id = onc2.org_node_category_id  and onc1.activation_status = 'AC'  and onc2.activation_status = 'AC'  and ond.activation_status = 'AC'  and onc1.category_level >= onc2.category_level  and onc1.customer_id =  (select customer_id from org_node where org_node_id = {orgNodeId})) order by categoryLevel")
    OrgNodeCategory[] getLevelForEditTopOrganization(Integer orgNodeId) throws SQLException;

    
    /**
     * @jc:sql statement::
     * select 
     *     onp.parent_org_node_id as orgNodeId,
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
     *     node.org_node_code as orgNodeCode
     *    
     *    from org_node_parent onp, org_node node
     *       where onp.org_node_id = {childOrgNodeId}
     *         and onp.parent_org_node_id = node.org_node_id::
     */
    @JdbcControl.SQL(statement = "select  onp.parent_org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode  from org_node_parent onp, org_node node  where onp.org_node_id = {childOrgNodeId}  and onp.parent_org_node_id = node.org_node_id")
    Node getParentOrgNode(Integer childOrgNodeId) throws SQLException;
    
    /**
     * @jc:sql statement::
     *  select 
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
     *     
     *      
     *     (select count(org_node_id) from org_node where org_node_id in (select org_node_id from org_node_parent pt 
     *           start with pt.parent_org_node_id in 
     *                 (select org_node_id from user_role uor where uor.activation_status='AC' and uor.user_id=(select user_id from users where user_name={userName}))
     *          connect by prior pt.org_node_id = pt.parent_org_node_id)
     *          and  activation_status = 'AC') as
     *    childNodeCount
     *     
     *     from org_node node, org_node_category cat where cat.org_node_category_id = node.org_node_category_id
     *     
     *  
     *   and node.activation_status = 'AC'
     *   and org_node_id in (select org_node_id from user_role uor where uor.activation_status='AC' and uor.user_id=(select user_id from users where user_name={userName}))
     *   
     *   group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name::
     */
    @JdbcControl.SQL(statement = "select  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  (select count(org_node_id) from org_node where org_node_id in (select org_node_id from org_node_parent pt  start with pt.parent_org_node_id in  (select org_node_id from user_role uor where uor.activation_status='AC' and uor.user_id=(select user_id from users where user_name={userName}))  connect by prior pt.org_node_id = pt.parent_org_node_id)  and  activation_status = 'AC') as  childNodeCount  from org_node node, org_node_category cat where cat.org_node_category_id = node.org_node_category_id  and node.activation_status = 'AC'  and org_node_id in (select org_node_id from user_role uor where uor.activation_status='AC' and uor.user_id=(select user_id from users where user_name={userName}))  group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name")
    UserNode[] getTopOrgNodesForUser(String userName) throws SQLException;
   
    

    /**
     * @jc:sql statement::
     * select onc.category_level
     *   from org_node_category onc
     *  where onc.org_node_category_id =
     *        (SELECT DISTINCT n1.org_Node_Category_Id as orgNodeCategoryId
     *           FROM org_node n1, Org_Node_Parent p, org_node n2
     *          WHERE n1.customer_Id =
     *                (select customer_id from org_node where org_node_id = {orgNodeId}) 
     *            AND n1.org_Node_Id = p.org_Node_Id
     *            AND p.parent_Org_Node_Id = n2.org_Node_Id
     *            AND n2.customer_Id <> n1.customer_Id)::
     */
    @JdbcControl.SQL(statement = "select onc.category_level  from org_node_category onc  where onc.org_node_category_id =  (SELECT DISTINCT n1.org_Node_Category_Id as orgNodeCategoryId  FROM org_node n1, Org_Node_Parent p, org_node n2  WHERE n1.customer_Id =  (select customer_id from org_node where org_node_id = {orgNodeId})  AND n1.org_Node_Id = p.org_Node_Id  AND p.parent_Org_Node_Id = n2.org_Node_Id  AND n2.customer_Id <> n1.customer_Id)")
    Integer getCategoryLevelForTopOrganization(Integer orgNodeId) throws SQLException;

    /**
     * @jc:sql statement::
     * select customer_id from org_node 
     * where org_node_id = {orgNodeId}::
     */
	@JdbcControl.SQL(statement = "select customer_id from org_node  where org_node_id = {orgNodeId}")
    Integer getCustomerIdbyOrgNode(Integer orgNodeId) throws SQLException;
	
	//START - Changes for LASLINK Customer
	@JdbcControl.SQL(statement = "select decode(count(1),0,'F','T') as isLasLinkCustomer from org_node org, customer_configuration config where org.org_node_id ={selectedOrgNodeId} and config.customer_Id = org.customer_Id   and config.customer_configuration_name = {ConfigName}  and config.default_value ='T'")
    String getlasLinkConfigForOrgNodes(Integer selectedOrgNodeId, String ConfigName) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT DECODE(COUNT(1), 0, 'T', 'F') AS UNIQUEMDRNUMBER  FROM ORG_NODE ORG WHERE ORG.ORG_NODE_MDR_NUMBER = {selectedMdrNumber} AND ORG.ACTIVATION_STATUS = 'AC'") // change for defect - 66238
    String checkUniqueMdrNumberForOrgNodes(String selectedMdrNumber) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT DECODE(COUNT(1), 0, 'T', 'F') AS UNIQUEMDRNUMBER  FROM ORG_NODE ORG WHERE ORG.ORG_NODE_MDR_NUMBER = {selectedMdrNumber} AND ORG.ACTIVATION_STATUS = 'AC' AND ORG.ORG_NODE_ID != {orgNodeId}") // change for defect - 66238
    String checkUniqueMdrNumberForOrgNodes(String selectedMdrNumber,int orgNodeId) throws SQLException;
	
	//END - Changes for LASLINK Customer
	
	//START - changes for TABE BAUM to delete organization node and assign remaining license to top node
    
	@JdbcControl.SQL(statement = "select count(*) from customer_orgnode_license where org_node_id = {selectedOrgNodeId} and customer_id= {customerId}")
	Boolean getOrgNodeLiceseEntryPresent(Integer selectedOrgNodeId, Integer customerId) throws SQLException;

	@JdbcControl.SQL(statement = "select available from customer_orgnode_license where org_node_id = {selectedOrgNodeId} and customer_id= {customerId}  and product_id= {productId}")
	Integer getAvailableLicenseQuantityForOrgNode(Integer selectedOrgNodeId, Integer customerId, Integer productId) throws SQLException;
	
	@JdbcControl.SQL(statement = " update customer_orgnode_license con set con.available = ({availableLicense} + con.available) where con.org_node_id ={customerTopNodeId} and product_id= {productId}")
	void addDeletedNodeLicenseToTopNode(Integer customerTopNodeId, Integer availableLicense, Integer productId) throws SQLException;
	
	@JdbcControl.SQL(statement = " delete from customer_orgnode_license where org_node_id = {selectedOrgNodeId} and customer_id= {customerId} ")
	void deleteOrgNodeDetails(Integer selectedOrgNodeId, Integer customerId) throws SQLException;
	
	@JdbcControl.SQL(statement = "  select distinct product_id from customer_orgnode_license  where customer_id = {customerId} and org_node_id = {selectedOrgNodeId}")
	Integer[] getProductIdList(Integer selectedOrgNodeId, Integer customerId) throws SQLException;
    //END - changes for TABE BAUM to delete organization node and assign remaining license to top node
    
    // LLO- 118 - Change for Ematrix UI
	@JdbcControl.SQL(statement = "select count(*) from users u,user_role urole,org_node orgnode,org_node_category onc where u.user_id = urole.user_id and urole.org_node_id = orgnode.org_node_id and orgnode.org_node_category_id = onc.org_node_category_id and onc.category_level = 1 and u.user_name = {userName}")
	Boolean checkTopOrgNodeUser(String userName) throws SQLException;

	/*@JdbcControl.SQL(statement = " SELECT DISTINCT NODE.ORG_NODE_ID AS orgNodeId,                  ONP.PARENT_ORG_NODE_ID AS parentOrgNodeId,                  NODE.ORG_NODE_CATEGORY_ID AS orgNodeCategoryId,                  NODE.ORG_NODE_NAME AS orgNodeName,                  CAT.CATEGORY_NAME AS orgNodeCategoryName,                  CAT.CATEGORY_LEVEL AS categoryLevel    FROM ORG_NODE          NODE,         ORG_NODE_CATEGORY CAT,         ORG_NODE_CATEGORY CUSTOMERCAT,         ORG_NODE_ANCESTOR DESCENDANTS,         ORG_NODE          DESCENDANT_NODE,         ORG_NODE_ANCESTOR DESCENDANTSCHILD,         ORG_NODE_PARENT   ONP   WHERE DESCENDANTS.ORG_NODE_ID = NODE.ORG_NODE_ID     AND CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID     AND CAT.ACTIVATION_STATUS = 'AC'     AND NODE.ORG_NODE_ID = ONP.ORG_NODE_ID     AND DESCENDANTS.ANCESTOR_ORG_NODE_ID = {associatedNodeId}     AND NODE.ORG_NODE_ID = DESCENDANTSCHILD.ANCESTOR_ORG_NODE_ID     AND DESCENDANTSCHILD.ORG_NODE_ID = DESCENDANT_NODE.ORG_NODE_ID     AND DESCENDANTSCHILD.NUMBER_OF_LEVELS >= 0   ORDER BY CATEGORYLEVEL")
	UserNode[] OrgNodehierarchy(Integer associatedNodeId) throws SQLException;
	*/
	

	@JdbcControl.SQL(statement = "  SELECT DISTINCT NODE.ORG_NODE_ID AS orgNodeId,                  ONP.PARENT_ORG_NODE_ID AS parentOrgNodeId,                  NODE.ORG_NODE_CATEGORY_ID AS orgNodeCategoryId,                  NODE.ORG_NODE_NAME AS orgNodeName,                  CAT.CATEGORY_NAME AS orgNodeCategoryName,                  CAT.CATEGORY_LEVEL AS categoryLevel, NODE.CUSTOMER_ID             AS customerId    FROM ORG_NODE          NODE,         ORG_NODE_CATEGORY CAT,		ORG_NODE_ANCESTOR DESCENDANTS,         ORG_NODE          DESCENDANT_NODE,         ORG_NODE_ANCESTOR DESCENDANTSCHILD,         ORG_NODE_PARENT   ONP   WHERE DESCENDANTS.ORG_NODE_ID = NODE.ORG_NODE_ID     AND CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID     AND CAT.ACTIVATION_STATUS = 'AC'     AND NODE.ORG_NODE_ID = ONP.ORG_NODE_ID     AND DESCENDANTS.ANCESTOR_ORG_NODE_ID = {associatedNodeId}     AND NODE.ORG_NODE_ID = DESCENDANTSCHILD.ANCESTOR_ORG_NODE_ID     AND DESCENDANTSCHILD.ORG_NODE_ID = DESCENDANT_NODE.ORG_NODE_ID     AND DESCENDANTSCHILD.NUMBER_OF_LEVELS >= 0   ORDER BY CATEGORYLEVEL,UPPER(ORGNODENAME)", arrayMaxLength = 100000)
	UserNode[] OrgNodehierarchy(Integer associatedNodeId) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT DISTINCT NODE.ORG_NODE_ID AS orgNodeId,                 ONP.PARENT_ORG_NODE_ID AS parentOrgNodeId,                 NODE.ORG_NODE_CATEGORY_ID AS orgNodeCategoryId,                 NODE.ORG_NODE_NAME AS orgNodeName,                 CAT.CATEGORY_NAME AS orgNodeCategoryName,                 CAT.CATEGORY_LEVEL AS categoryLevel   FROM ORG_NODE          NODE,        ORG_NODE_CATEGORY CAT,        ORG_NODE_CATEGORY CUSTOMERCAT,        ORG_NODE_ANCESTOR DESCENDANTS,        ORG_NODE          DESCENDANT_NODE,        ORG_NODE_ANCESTOR DESCENDANTSCHILD,        ORG_NODE_PARENT   ONP  WHERE DESCENDANTS.ORG_NODE_ID = NODE.ORG_NODE_ID    AND CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID    AND CAT.ACTIVATION_STATUS = 'AC'    AND NODE.ORG_NODE_ID = ONP.ORG_NODE_ID    AND DESCENDANTS.ANCESTOR_ORG_NODE_ID =        (SELECT DISTINCT ORN.ORG_NODE_ID           FROM ORG_NODE_CATEGORY ONC,                USERS             USERS,                USER_ROLE         UROLE,                ORG_NODE          NODE,                ORG_NODE          ORN          WHERE USERS.USER_NAME = {userName}           AND USERS.USER_ID = UROLE.USER_ID            AND UROLE.ORG_NODE_ID = NODE.ORG_NODE_ID            AND ONC.CUSTOMER_ID = NODE.CUSTOMER_ID            AND ORN.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID            AND ONC.CATEGORY_LEVEL =                (SELECT MIN(ONC1.CATEGORY_LEVEL)                   FROM ORG_NODE_CATEGORY ONC1                  WHERE ONC.CUSTOMER_ID = ONC1.CUSTOMER_ID                    AND ONC1.ACTIVATION_STATUS = 'AC')            AND ONC.ACTIVATION_STATUS = 'AC')    AND NODE.ORG_NODE_ID = DESCENDANTSCHILD.ANCESTOR_ORG_NODE_ID    AND DESCENDANTSCHILD.ORG_NODE_ID = DESCENDANT_NODE.ORG_NODE_ID    AND DESCENDANTSCHILD.NUMBER_OF_LEVELS >= 0  ORDER BY CATEGORYLEVEL")
	UserNode[] OrgNodehierarchyForParent(String userName) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT MAX(ONC.CATEGORY_LEVEL) FROM ORG_NODE_CATEGORY ONC WHERE ONC.CUSTOMER_ID = {customerId} AND ONC.ACTIVATION_STATUS = 'AC'")
	Integer getLeafNodeCategoryId(Integer customerId) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT ORG_NODE_ID FROM ORG_NODE_CATEGORY ONC, ORG_NODE ORG WHERE ONC.CUSTOMER_ID = {customerId} AND ONC.ACTIVATION_STATUS = 'AC' AND ORG.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID AND ONC.CATEGORY_LEVEL IN (SELECT MIN(ONGC.CATEGORY_LEVEL) FROM ORG_NODE_CATEGORY ONGC WHERE ONGC.CUSTOMER_ID = {customerId} AND ONGC.ACTIVATION_STATUS = 'AC') AND ORG.ACTIVATION_STATUS = 'AC'")
	Integer[] getStateLevelNodeId(Integer customerId) throws SQLException;
	//Changes for new UI
	
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
     *     count(distinct child.org_node_id) as childNodeCount,
     *     pnode.org_node_name as parentOrgNodeName
     * from 
     *      org_node node, org_node_parent parent, org_node_category cat,
     *      org_node_parent child, org_node chnode, org_node pnode
     * where 
     *      parent.org_node_id = node.org_node_id
     *      and cat.org_node_category_id = node.org_node_category_id
     *      and parent.parent_org_node_id = {parentOrgNodeId}
     *      and child.parent_org_node_id (+) = node.org_node_id
     *      and chnode.org_node_id (+) = child.org_node_id   
     *      and NVL(chnode.activation_status, 'AC') = 'AC'
     *      and node.activation_status = 'AC'
     *      and cat.activation_status = 'AC'
     *      and pnode.org_node_id = parent.parent_org_node_id
     * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name,
     *      pnode.org_node_name
     * order by 
     * 		UPPER(node.org_node_name)::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  count(distinct child.org_node_id) as childNodeCount, pnode.org_node_name as parentOrgNodeName from  org_node node, org_node_parent parent, org_node_category cat,  org_node_parent child, org_node chnode, org_node pnode where  parent.org_node_id = node.org_node_id  and cat.org_node_category_id = node.org_node_category_id  and parent.parent_org_node_id = {parentOrgNodeId}  and child.parent_org_node_id (+) = node.org_node_id  and chnode.org_node_id (+) = child.org_node_id  and NVL(chnode.activation_status, 'AC') = 'AC'  and node.activation_status = 'AC'  and cat.activation_status = 'AC' and pnode.org_node_id = parent.parent_org_node_id group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name, pnode.org_node_name order by UPPER(node.org_node_name)",
                     arrayMaxLength = 0, fetchSize=100)
    Node [] getOrgNodesByParentIncludingParentName(Integer parentOrgNodeId) throws SQLException;
    
    @JdbcControl.SQL(statement = "SELECT ONC.CATEGORY_LEVEL AS categoryLevel, ONC.CATEGORY_NAME AS categoryName   FROM ORG_NODE_CATEGORY ONC  WHERE ONC.CUSTOMER_ID = {customerId}    AND ONC.ACTIVATION_STATUS = 'AC'    AND ONC.CATEGORY_LEVEL =        (SELECT MAX(ONC.CATEGORY_LEVEL)           FROM ORG_NODE_CATEGORY ONC          WHERE ONC.CUSTOMER_ID = {customerId}            AND ONC.ACTIVATION_STATUS = 'AC')")
	OrgNodeCategory getCustomerLeafNodeDetail(Integer customerId) throws SQLException;
    
    
    @JdbcControl.SQL(statement = "SELECT DISTINCT NODE.ORG_NODE_ID          AS orgNodeId,                ONP.PARENT_ORG_NODE_ID    AS parentOrgNodeId,                NODE.ORG_NODE_CATEGORY_ID AS orgNodeCategoryId,                NODE.ORG_NODE_NAME        AS orgNodeName,                CAT.CATEGORY_NAME         AS orgNodeCategoryName,                CAT.CATEGORY_LEVEL        AS categoryLevel,                NODE.CUSTOMER_ID          AS customerId  FROM ORG_NODE          NODE,       ORG_NODE_CATEGORY CAT,       ORG_NODE_ANCESTOR DESCENDANTS,       ORG_NODE          DESCENDANT_NODE,       ORG_NODE_ANCESTOR DESCENDANTSCHILD,       ORG_NODE_PARENT   ONP WHERE DESCENDANTS.ORG_NODE_ID = NODE.ORG_NODE_ID   AND CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID   AND NVL(DESCENDANT_NODE.ACTIVATION_STATUS, 'AC') = 'AC'   AND NODE.ACTIVATION_STATUS = 'AC'   AND CAT.ACTIVATION_STATUS = 'AC'   AND NODE.ORG_NODE_ID = ONP.ORG_NODE_ID   AND DESCENDANTS.ANCESTOR_ORG_NODE_ID = {orgNodeId}   AND NODE.ORG_NODE_ID = DESCENDANTSCHILD.ANCESTOR_ORG_NODE_ID   AND DESCENDANTSCHILD.ORG_NODE_ID = DESCENDANT_NODE.ORG_NODE_ID   AND DESCENDANTSCHILD.NUMBER_OF_LEVELS >= 0 ORDER BY CATEGORYLEVEL, UPPER(ORGNODENAME)  ",
                     arrayMaxLength = 0, fetchSize=100)
    Node [] getOrgNodesByParentAncestor(Integer orgNodeId) throws SQLException;
    
    
    @JdbcControl.SQL(statement = "SELECT DISTINCT NODE.ORG_NODE_ID          AS orgNodeId,                ONP.PARENT_ORG_NODE_ID    AS parentOrgNodeId,                NODE.ORG_NODE_CATEGORY_ID AS orgNodeCategoryId,                NODE.ORG_NODE_NAME        AS orgNodeName,                CAT.CATEGORY_NAME         AS orgNodeCategoryName,                CAT.CATEGORY_LEVEL        AS categoryLevel,                NODE.CUSTOMER_ID          AS customerId  FROM ORG_NODE          NODE,       ORG_NODE_CATEGORY CAT,       ORG_NODE_ANCESTOR DESCENDANTS,       ORG_NODE          DESCENDANT_NODE,       ORG_NODE_ANCESTOR DESCENDANTSCHILD,       ORG_NODE_PARENT   ONP,   ORG_NODE_ANCESTOR ONA, TEST_ROSTER  ROSTER,    STUDENT  STU WHERE DESCENDANTS.ORG_NODE_ID = NODE.ORG_NODE_ID   AND CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID   AND NVL(DESCENDANT_NODE.ACTIVATION_STATUS, 'AC') = 'AC'   AND NODE.ACTIVATION_STATUS = 'AC'   AND CAT.ACTIVATION_STATUS = 'AC'   AND NODE.ORG_NODE_ID = ONP.ORG_NODE_ID   AND DESCENDANTS.ANCESTOR_ORG_NODE_ID = {orgNodeId}   AND NODE.ORG_NODE_ID = DESCENDANTSCHILD.ANCESTOR_ORG_NODE_ID   AND DESCENDANTSCHILD.ORG_NODE_ID = DESCENDANT_NODE.ORG_NODE_ID   AND DESCENDANTSCHILD.NUMBER_OF_LEVELS >= 0  AND ONA.ANCESTOR_ORG_NODE_ID = NODE.ORG_NODE_ID   AND ROSTER.ORG_NODE_ID = ONA.ORG_NODE_ID   AND STU.STUDENT_ID = ROSTER.STUDENT_ID   AND ROSTER.TEST_ADMIN_ID = {testAdminId} ORDER BY CATEGORYLEVEL, UPPER(ORGNODENAME)  ",
            arrayMaxLength = 0, fetchSize=100)
    StudentNode [] getOrgNodesHaveStudentByParentAncestor(Integer orgNodeId, Integer testAdminId) throws SQLException;
    
    @JdbcControl.SQL(statement = "select distinct  orgNodeId,  customerId,  orgNodeCategoryId,  orgNodeName,  extQedPin,  extElmId,  extOrgNodeType,  orgNodeDescription,  createdBy,  createdDateTime,  updatedBy,  updatedDateTime,  activationStatus,  dataImportHistoryId,  parentState,  parentRegion,  parentCounty,  parentDistrict,  orgNodeCode,  orgNodeCategoryName, categoryLevel,  childNodeCount from (select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName, cat.category_level as categoryLevel,  count(distinct child.org_node_id) as childNodeCount from  org_node node, user_role role, users, org_node_category cat,  org_node_parent child where  role.org_node_id = node.org_node_id  and cat.org_node_category_id = node.org_node_category_id  and role.activation_status = 'AC'  and role.user_id = users.user_id  and users.user_name = {userName}  and child.parent_org_node_id (+) = node.org_node_id group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name, cat.category_level union select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName, cat.category_level as categoryLevel,  count(distinct child.org_node_id) as childNodeCount from  org_node node,  org_node_category cat,  org_node_parent child,  test_admin admin where  admin.creator_org_node_id = node.org_node_id  and cat.org_node_category_id = node.org_node_category_id  and admin.test_admin_id = {testAdminId}  and child.parent_org_node_id (+) = node.org_node_id group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name, cat.category_level) order by  categoryLevel, upper(orgNodeName)",
            arrayMaxLength = 100000)
    Node [] getTopNodesForUserAndAdminForPrintTT(String userName, Integer testAdminId) throws SQLException;
    
}