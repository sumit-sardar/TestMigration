package com.ctb.control.db; 

import java.sql.SQLException;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.testAdmin.CustomerReport;
import com.ctb.bean.testAdmin.Program;
import com.ctb.bean.testAdmin.TestElement;

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
public interface CustomerReportBridge extends JdbcControl
{ 
    static final long serialVersionUID = 1L;
    
    /**
     * @jc:sql statement::
	*select distinct bridge.CUSTOMER_ID   as customerId,
	*                bridge.REPORT_NAME   as reportName,
	*                bridge.DISPLAY_NAME  as displayName,
	*                bridge.DESCRIPTION   as description,
	*                bridge.SYSTEM_KEY    as systemKey,
	*                bridge.CUSTOMER_KEY  as customerKey,
	*                bridge.REPORT_URL    as reportUrl,
	*                bridge.PRODUCT_ID    as productId,
	*                program.PROGRAM_NAME as programName,
	*                org_node.org_node_id as orgNodeId,
	*                count(ona.org_node_id ) as categoryLevel
	*  from customer_report_bridge bridge,
	*       users,
	*       user_role,
	*       org_node,
	*       customer,
	*       program,
	*       org_node_category onc,
	*       org_node_ancestor ona
	* where bridge.customer_id = program.customer_id
	*   and bridge.product_id =  case when(bridge.product_id = 4000
	*               or bridge.product_id = 4200) then program.product_id else bridge.product_id end
	*   and program.customer_id = org_node.customer_id
	*   and program.program_id = {programId}
	*   and org_node.org_node_id = {orgNodeId}
	*   and org_node.activation_status = 'AC'
	*   and org_node.org_node_id = user_role.org_node_id
	*   and ona.org_node_id = org_node.org_node_id
	*   and ona.ancestor_org_node_id not in (1,2)
	*   and user_role.user_id = users.user_id
	*   and user_role.activation_status = 'AC'
	*   and users.user_name = {userName}
	*   and customer.customer_id = bridge.customer_id
	*   and org_node.org_node_category_id = onc.org_node_category_id
	*   group by 
	*   bridge.CUSTOMER_ID   ,
	*                bridge.REPORT_NAME   ,
	*                bridge.DISPLAY_NAME ,
	*                bridge.DESCRIPTION ,
	*                bridge.SYSTEM_KEY ,
	*                bridge.CUSTOMER_KEY,
	*                bridge.REPORT_URL ,
	*                bridge.PRODUCT_ID  ,
	*                program.PROGRAM_NAME ,
	*                org_node.org_node_id 
	* order by bridge.product_id desc, bridge.DISPLAY_NAME asc
	*
     *     array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct bridge.CUSTOMER_ID   as customerId, bridge.REPORT_NAME   as reportName, bridge.DISPLAY_NAME  as displayName, bridge.DESCRIPTION   as description, bridge.SYSTEM_KEY    as systemKey, bridge.CUSTOMER_KEY  as customerKey, bridge.REPORT_URL    as reportUrl, bridge.PRODUCT_ID    as productId, program.PROGRAM_NAME as programName, org_node.org_node_id as orgNodeId, count(ona.org_node_id ) as categoryLevel from customer_report_bridge bridge, users, user_role, org_node, customer, program, org_node_category onc, org_node_ancestor ona where bridge.customer_id = program.customer_id and bridge.product_id =  case when(bridge.product_id = 4000 or bridge.product_id = 4200) then program.product_id else bridge.product_id end and program.customer_id = org_node.customer_id and program.program_id = {programId} and org_node.org_node_id = {orgNodeId} and org_node.activation_status = 'AC' and org_node.org_node_id = user_role.org_node_id and ona.org_node_id = org_node.org_node_id and ona.ancestor_org_node_id not in (1,2) and user_role.user_id = users.user_id and user_role.activation_status = 'AC' and users.user_name = {userName} and customer.customer_id = bridge.customer_id and org_node.org_node_category_id = onc.org_node_category_id group by bridge.CUSTOMER_ID   , bridge.REPORT_NAME   , bridge.DISPLAY_NAME , bridge.DESCRIPTION , bridge.SYSTEM_KEY , bridge.CUSTOMER_KEY, bridge.REPORT_URL , bridge.PRODUCT_ID  , program.PROGRAM_NAME , org_node.org_node_id order by bridge.product_id desc, bridge.DISPLAY_NAME asc",
    		arrayMaxLength = 0, fetchSize = 100)
    CustomerReport [] getReportAssignmentsForUser(String userName, Integer programId, Integer orgNodeId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select distinct
     *     bridge.CUSTOMER_ID as customerId,
     *     bridge.REPORT_NAME as reportName,
     *     bridge.DISPLAY_NAME as displayName,
     *     bridge.DESCRIPTION as description,
     *     bridge.SYSTEM_KEY as systemKey,
     *     bridge.CUSTOMER_KEY as customerKey,
     *     bridge.REPORT_URL as reportUrl,
     *     bridge.PRODUCT_ID as productId,
     *     program.PROGRAM_NAME as programName,
     *     org_node.org_node_id as orgNodeId,
     *     count(ona.ORG_NODE_ID) AS categoryLevel
     * from 
     *      customer_report_bridge bridge, org_node, customer, program, org_node_category onc, org_node_ancestor ona
     * where 
     *         bridge.customer_id = program.customer_id
     *     and bridge.product_id = program.product_id
     *     and program.customer_id = org_node.customer_id
     *     and ona.org_node_id = org_node.org_node_id
     *     and ona.ancestor_org_node_id NOT IN (1, 2)
     *     and program.program_id = {programId}
     *     and org_node.org_node_id = {orgNodeId}
     *     and org_node.activation_status = 'AC' 
     *     and customer.customer_id = bridge.customer_id
     *     and org_node.org_node_category_id = onc.org_node_category_id
     *     group by bridge.CUSTOMER_ID,
     *			    bridge.REPORT_NAME,
     *				bridge.DISPLAY_NAME,
     *				bridge.DESCRIPTION,
     *				bridge.SYSTEM_KEY,
     *				bridge.CUSTOMER_KEY,
     *				bridge.REPORT_URL,
     *				bridge.PRODUCT_ID,
     *				program.PROGRAM_NAME,
     *				org_node.ORG_NODE_ID"
     */
    @JdbcControl.SQL(statement = "select distinct  bridge.CUSTOMER_ID as customerId,  bridge.REPORT_NAME as reportName,  bridge.DISPLAY_NAME as displayName,  bridge.DESCRIPTION as description,  bridge.SYSTEM_KEY as systemKey,  bridge.CUSTOMER_KEY as customerKey,  bridge.REPORT_URL as reportUrl,  bridge.PRODUCT_ID as productId,  program.PROGRAM_NAME as programName,  org_node.org_node_id as orgNodeId, COUNT(ona.ORG_NODE_ID) as categoryLevel from  customer_report_bridge bridge, org_node, customer, program, org_node_category onc where  bridge.customer_id = program.customer_id  and bridge.product_id = program.product_id  and program.customer_id = org_node.customer_id  and program.program_id = {programId}  and org_node.org_node_id = {orgNodeId}  and org_node.activation_status = 'AC' and ona.org_node_id = org_node.org_node_id and ona.ancestor_org_node_id NOT IN (1, 2) and customer.customer_id = bridge.customer_id  and org_node.org_node_category_id = onc.org_node_category_id group by bridge.CUSTOMER_ID, bridge.REPORT_NAME, bridge.DISPLAY_NAME, bridge.DESCRIPTION, bridge.SYSTEM_KEY, bridge.CUSTOMER_KEY, bridge.REPORT_URL, bridge.PRODUCT_ID, program.PROGRAM_NAME, org_node.ORG_NODE_ID",
    		arrayMaxLength = 0, fetchSize = 100)
    CustomerReport [] getReportAssignmentsForProgram(Integer programId, Integer orgNodeId) throws SQLException;

   
    /**
     * @jc:sql statement::
     * select 
     *     bridge.CUSTOMER_ID as customerId,
     *     bridge.REPORT_NAME as reportName,
     *     bridge.DISPLAY_NAME as displayName,
     *     bridge.DESCRIPTION as description,
     *     bridge.SYSTEM_KEY as systemKey,
     *     bridge.CUSTOMER_KEY as customerKey,
     *     bridge.REPORT_URL as reportUrl,
     *     bridge.PRODUCT_ID as productId,
     * from 
     *      customer_report_bridge bridge
     * where 
     *     bridge.report_name = {reportName}::
     *     array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select bridge.CUSTOMER_ID as customerId,  bridge.REPORT_NAME as reportName,  bridge.DISPLAY_NAME as displayName,  bridge.DESCRIPTION as description,  bridge.SYSTEM_KEY as systemKey,  bridge.CUSTOMER_KEY as customerKey,  bridge.REPORT_URL as reportUrl,  bridge.PRODUCT_ID as productId  from  customer_report_bridge bridge where bridge.report_name = {reportName}",
                     arrayMaxLength = 100000)
    CustomerReport getReportOpenAPI(String reportName) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select distinct
     *     pr.PROGRAM_ID as programId, 
     *     pr.PRODUCT_ID as productId,
     *     pr.CUSTOMER_ID as customerId,
     *     pr.PROGRAM_NAME as programName,
     *     pr.PROGRAM_START_DATE as programStartDate,
     *     pr.PROGRAM_END_DATE as programEndDate,
     *     pr.NORMS_GROUP as normsGroup,
     *     pr.NORMS_YEAR as normsYear,
     *     pr.CREATED_DATE_TIME as createdDateTime,
     *     pr.UPDATED_DATE_TIME as updatedDateTime
     * from 
     *     program pr, users, user_role ur,org_node
     * where
     *         pr.customer_id = org_node.customer_id
     *     and org_node.org_node_id = ur.org_node_id
     *     and ur.user_id = users.user_id
     *     and ur.activation_status = 'AC'
     *     and users.user_name = {userName}::
     *     array-max-length="all"
     */     
    @JdbcControl.SQL(statement = "select distinct  pr.PROGRAM_ID as programId,  pr.PRODUCT_ID as productId,  pr.CUSTOMER_ID as customerId,  pr.PROGRAM_NAME as programName,  pr.PROGRAM_START_DATE as programStartDate,  pr.PROGRAM_END_DATE as programEndDate,  pr.NORMS_GROUP as normsGroup,  pr.NORMS_YEAR as normsYear,  pr.CREATED_DATE_TIME as createdDateTime,  pr.UPDATED_DATE_TIME as updatedDateTime from  program pr, users, user_role ur,org_node where  pr.customer_id = org_node.customer_id  and org_node.org_node_id = ur.org_node_id  and ur.user_id = users.user_id  and ur.activation_status = 'AC'  and users.user_name = {userName}",
                     arrayMaxLength = 100000)
    Program [] getProgramsForUser(String userName) throws SQLException;   

    /**
     * @jc:sql statement::
     * select distinct
     *     pr.PROGRAM_ID as programId, 
     *     pr.PRODUCT_ID as productId,
     *     pr.CUSTOMER_ID as customerId,
     *     pr.PROGRAM_NAME as programName,
     *     pr.PROGRAM_START_DATE as programStartDate,
     *     pr.PROGRAM_END_DATE as programEndDate,
     *     pr.NORMS_GROUP as normsGroup,
     *     pr.NORMS_YEAR as normsYear,
     *     pr.CREATED_DATE_TIME as createdDateTime,
     *     pr.UPDATED_DATE_TIME as updatedDateTime
     * from 
     *     program pr, users, user_role ur,org_node
     * where
     *         pr.customer_id = org_node.customer_id
     *     and org_node.org_node_id = ur.org_node_id
     *     and ur.user_id = users.user_id
     *     and ur.activation_status = 'AC'
     *     and pr.activation_status = 'AC'
     *     and users.user_name = {userName}::
     *     array-max-length="all"
     */     
    @JdbcControl.SQL(statement = "select distinct  pr.PROGRAM_ID as programId,  pr.PRODUCT_ID as productId,  pr.CUSTOMER_ID as customerId,  pr.PROGRAM_NAME as programName,  pr.PROGRAM_START_DATE as programStartDate,  pr.PROGRAM_END_DATE as programEndDate,  pr.NORMS_GROUP as normsGroup,  pr.NORMS_YEAR as normsYear,  pr.CREATED_DATE_TIME as createdDateTime,  pr.UPDATED_DATE_TIME as updatedDateTime from  program pr, users, user_role ur,org_node where  pr.customer_id = org_node.customer_id  and org_node.org_node_id = ur.org_node_id  and ur.user_id = users.user_id  and ur.activation_status = 'AC'  and pr.activation_status = 'AC'  and users.user_name = {userName} order by pr.PROGRAM_ID ",
                     arrayMaxLength = 100000)
    Program [] getActiveProgramsForUser(String userName) throws SQLException;   
    
    /**
     * @jc:sql statement::
     * select 
     *      count(crb.customer_id)
     * from 
     *     customer_report_bridge crb
     * where
     *    crb.customer_id = {customerId}::    
     */     
    @JdbcControl.SQL(statement = "select  count(crb.customer_id) from  customer_report_bridge crb where  crb.customer_id = {customerId}")
    Integer  getCustomerReports(Integer customerId) throws SQLException; 
    
    
    /**
     * @jc:sql statement::
     * select distinct 	
     * 		iset.ITEM_SET_ID   as itemSetId,
     *  	iset.ITEM_SET_NAME as itemSetName,
     *   	prog.program_id    as programId
     * from 
     * 		product prod,
     *     	item_set_product isp,
     *      item_set iset,
     *      org_node_test_catalog ontc, 
     *      user_role urole, users, 
     *      program prog 
     * where 
     * 		users.user_name = {userName} 
     * 	and users.user_id = urole.user_id 
     * 	and urole.org_node_id = ontc.org_node_id 
     * 	and ontc.item_set_id = iset.item_set_id 
     * 	and iset.item_set_id = isp.item_set_id 
     * 	and isp.product_id = prod.product_id 
     * 	and prog.product_id = prod.parent_product_id 
     * 	and prog.customer_id = ontc.customer_id 
     * 	and iset.item_set_type = 'TC' 
     * 	and prod.activation_status = 'AC' 
     * 	and iset.activation_status = 'AC' 
     * 	and prog.activation_status = 'AC' 
     * 	and urole.activation_status = 'AC' 
     * 	and ontc.activation_status = 'AC' 
     * order by 
     * 	prog.program_id
     * 
     */
       
    @JdbcControl.SQL(statement = " select distinct iset.ITEM_SET_ID   as itemSetId, iset.ITEM_SET_NAME as itemSetName, prog.program_id    as programId from product prod, item_set_product isp, item_set iset, org_node_test_catalog ontc, user_role urole, users, program prog where users.user_name = {userName} and users.user_id = urole.user_id and urole.org_node_id = ontc.org_node_id and ontc.item_set_id = iset.item_set_id and iset.item_set_id = isp.item_set_id and isp.product_id = prod.product_id and prog.product_id = prod.parent_product_id and prog.customer_id = ontc.customer_id and iset.item_set_type = 'TC' and prod.activation_status = 'AC' and iset.activation_status = 'AC' and prog.activation_status = 'AC' and urole.activation_status = 'AC' and ontc.activation_status = 'AC' order by prog.program_id " ,
    				 arrayMaxLength = 1024)
    TestElement[]  getProgramTestDetails(String userName) throws SQLException; 
    
    
    
}