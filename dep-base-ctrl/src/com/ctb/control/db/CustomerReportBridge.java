package com.ctb.control.db; 

import java.sql.SQLException;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.testAdmin.CustomerReport;
import com.ctb.bean.testAdmin.Program;

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
     *     onc.category_level as categoryLevel
     * from 
     *      customer_report_bridge bridge, users, user_role, org_node, customer, program, org_node_category onc
     * where 
     *         bridge.customer_id = program.customer_id
     *     and bridge.product_id = program.product_id
     *     and program.customer_id = org_node.customer_id
     *     and program.program_id = {programId}
     *     and org_node.org_node_id = {orgNodeId}
     *     and org_node.activation_status = 'AC' 
     *     and org_node.org_node_id = user_role.org_node_id
     *     and user_role.user_id = users.user_id
     *     and user_role.activation_status = 'AC'
     *     and users.user_name = {userName}
     *     and customer.customer_id = bridge.customer_id
     *     and org_node.org_node_category_id = onc.org_node_category_id::
     *     array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  bridge.CUSTOMER_ID as customerId,  bridge.REPORT_NAME as reportName,  bridge.DISPLAY_NAME as displayName,  bridge.DESCRIPTION as description,  bridge.SYSTEM_KEY as systemKey,  bridge.CUSTOMER_KEY as customerKey,  bridge.REPORT_URL as reportUrl,  bridge.PRODUCT_ID as productId,  program.PROGRAM_NAME as programName,  org_node.org_node_id as orgNodeId,  onc.category_level as categoryLevel from  customer_report_bridge bridge, users, user_role, org_node, customer, program, org_node_category onc where  bridge.customer_id = program.customer_id  and bridge.product_id = program.product_id  and program.customer_id = org_node.customer_id  and program.program_id = {programId}  and org_node.org_node_id = {orgNodeId}  and org_node.activation_status = 'AC'  and org_node.org_node_id = user_role.org_node_id  and user_role.user_id = users.user_id  and user_role.activation_status = 'AC'  and users.user_name = {userName}  and customer.customer_id = bridge.customer_id  and org_node.org_node_category_id = onc.org_node_category_id",
                     arrayMaxLength = 100000)
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
     *     onc.category_level as categoryLevel
     * from 
     *      customer_report_bridge bridge, org_node, customer, program, org_node_category onc
     * where 
     *         bridge.customer_id = program.customer_id
     *     and bridge.product_id = program.product_id
     *     and program.customer_id = org_node.customer_id
     *     and program.program_id = {programId}
     *     and org_node.org_node_id = {orgNodeId}
     *     and org_node.activation_status = 'AC' 
     *     and customer.customer_id = bridge.customer_id
     *     and org_node.org_node_category_id = onc.org_node_category_id::
     *     array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  bridge.CUSTOMER_ID as customerId,  bridge.REPORT_NAME as reportName,  bridge.DISPLAY_NAME as displayName,  bridge.DESCRIPTION as description,  bridge.SYSTEM_KEY as systemKey,  bridge.CUSTOMER_KEY as customerKey,  bridge.REPORT_URL as reportUrl,  bridge.PRODUCT_ID as productId,  program.PROGRAM_NAME as programName,  org_node.org_node_id as orgNodeId,  onc.category_level as categoryLevel from  customer_report_bridge bridge, org_node, customer, program, org_node_category onc where  bridge.customer_id = program.customer_id  and bridge.product_id = program.product_id  and program.customer_id = org_node.customer_id  and program.program_id = {programId}  and org_node.org_node_id = {orgNodeId}  and org_node.activation_status = 'AC'  and customer.customer_id = bridge.customer_id  and org_node.org_node_category_id = onc.org_node_category_id",
                     arrayMaxLength = 100000)
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
    @JdbcControl.SQL(statement = "select distinct  pr.PROGRAM_ID as programId,  pr.PRODUCT_ID as productId,  pr.CUSTOMER_ID as customerId,  pr.PROGRAM_NAME as programName,  pr.PROGRAM_START_DATE as programStartDate,  pr.PROGRAM_END_DATE as programEndDate,  pr.NORMS_GROUP as normsGroup,  pr.NORMS_YEAR as normsYear,  pr.CREATED_DATE_TIME as createdDateTime,  pr.UPDATED_DATE_TIME as updatedDateTime from  program pr, users, user_role ur,org_node where  pr.customer_id = org_node.customer_id  and org_node.org_node_id = ur.org_node_id  and ur.user_id = users.user_id  and ur.activation_status = 'AC'  and pr.activation_status = 'AC'  and users.user_name = {userName}",
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
}