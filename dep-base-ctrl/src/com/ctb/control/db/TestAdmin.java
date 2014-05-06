package com.ctb.control.db; 

import com.bea.control.*;
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import com.ctb.bean.testAdmin.ActiveSession;
import com.ctb.bean.testAdmin.LASLicenseNode;
import com.ctb.bean.testAdmin.Program;
import com.ctb.bean.testAdmin.SubtestAccessCodeDetail;
import com.ctb.bean.testAdmin.TestSession; 
import java.sql.SQLException;
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
 * @jc:connection data-source-jndi-name="oasDataSource" 
 */ 
@ControlExtension()
@JdbcControl.ConnectionDataSource(jndiName = "oasDataSource")
public interface TestAdmin extends JdbcControl
{ 

    static final long serialVersionUID = 1L;

    /**
     * @jc:sql statement::
     * select distinct
     * 	  ontc.override_no_retake
     * from
     * 	  org_node_test_catalog ontc
     * where
     * 	 ontc.item_set_id = {testItemSetId}::
     * array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct \t  ontc.override_no_retake from \t  org_node_test_catalog ontc where \t ontc.item_set_id = {testItemSetId}",
                     arrayMaxLength = 100000)
    String isTestRestricted(Integer testItemSetId) throws SQLException;


    /**
     * @jc:sql statement::
     * select decode(count(*), 0, 'false', 'true') from (
     * 	  select 
     *       	count(taur.test_admin_id) as admins
     *       from
     *       	test_admin_user_role taur,
     *       	users
     *      where
     *       	 users.user_name = {userName}
     *       	 and taur.user_id = users.user_id
     *      	 and taur.test_admin_id = {testAdminId}
     * union
     * 	  select
     * 	    count(admin.test_admin_id) as admins
     * 	  from
     * 	    test_admin admin,
     * 		org_node_ancestor ona,
     * 		user_role urole,
     * 		users
     * 	  where
     * 	  	   admin.test_Admin_id = {testAdminId}
     * 		   and ona.org_node_id = admin.creator_org_node_id
     * 		   and urole.org_node_id = ona.ancestor_org_node_id
     * 		   and users.user_id = urole.user_id
     * 		   and users.user_name = {userName})
     * 	 where admins > 0::
     */
    @JdbcControl.SQL(statement = "select decode(count(*), 0, 'false', 'true') from ( \t  select  \tcount(taur.test_admin_id) as admins  from  \ttest_admin_user_role taur,  \tusers  where  \t users.user_name = {userName}  \t and taur.user_id = users.user_id  \t and taur.test_admin_id = {testAdminId} union \t  select \t  count(admin.test_admin_id) as admins \t  from \t  test_admin admin, \t\torg_node_ancestor ona, \t\tuser_role urole, \t\tusers \t  where \t  \t  admin.test_Admin_id = {testAdminId} \t\t  and ona.org_node_id = admin.creator_org_node_id \t\t  and urole.org_node_id = ona.ancestor_org_node_id \t\t  and users.user_id = urole.user_id \t\t  and users.user_name = {userName}) \t where admins > 0")
    String checkVisibility(String userName, Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *     test_admin.TEST_ADMIN_ID as testAdminId,
     *     test_admin.CUSTOMER_ID as customerId,
     *     test_admin.TEST_ADMIN_NAME as testAdminName,
     *     test_admin.PRODUCT_ID as productId,
     *     product.parent_product_id as parentProductId,
     *     product.license_enabled as licenseEnabled,
     *     test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,
     *     node.org_node_name as creatorOrgNodeName,
     *     cat.category_name as creatorOrgNodeCategoryName,
     *     test_admin.ACCESS_CODE as accessCode,
     *     test_admin.LOCATION as location,
     *     test_admin.LOGIN_START_DATE as loginStartDate,
     *     test_admin.LOGIN_END_DATE as loginEndDate,
     *     test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,
     *     test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,
     *     test_admin.PREFERRED_FORM as preferredForm,
     *     test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,
     *     test_admin.TIME_ZONE as timeZone,
     *     test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,
     *     test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,
     *     test_admin.ENFORCE_BREAK as enforceBreak,
     *     users.USER_NAME as createdBy,
     *     test_admin.CREATED_DATE_TIME as createdDateTime,
     *     test_admin.ACTIVATION_STATUS as activationStatus,
     *     test_admin.ITEM_SET_ID as itemSetId,
     *     test_admin.TEST_ADMIN_STATUS as testAdminStatus,
     *     test_admin.SESSION_NUMBER as sessionNumber,
     *     test_admin.TEST_ADMIN_TYPE as testAdminType,
     *     test.item_Set_name as testName,
     *     nvl(report.completed, 'F') as reportable
     * from 
     *      test_admin, 
     * 	   users, 
     * 	   item_set test,
     * 	   org_node node,
     * 	   org_node_category cat, 
     *      product,
     * 	   (select 
     * 	   		   ros.test_admin_id, 
     * 			   decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed 
     * 	   	from 
     * 			   test_roster ros, 
     * 			   test_admin, 
     * 			   users
     * 	   	where 
     * 			   ros.test_admin_id = test_admin.test_admin_id
     * 			   and test_Admin.created_by = users.user_id 
     * 			   and ros.test_completion_Status = 'CO'
     * 			   and ros.validation_status = 'VA'
     * 			   and users.user_name = {userName}
     * 			   and test_admin.activation_status = 'AC'
     * 		group by 
     * 			   ros.test_admin_id) report
     * where 
     * 		test.item_set_id = test_admin.item_set_id
     * 		and node.org_node_id = test_Admin.creator_org_node_id
     * 		and test_Admin.test_admin_id = report.test_admin_id (+)
     *      and test_admin.activation_status = 'AC'
     *      and test_admin.created_by = users.user_id
     *      and users.user_name = {userName}
     *      and cat.org_node_category_id = node.org_node_category_id
     *      and product.product_id = test_admin.PRODUCT_ID
     * order by
     *      test_admin.login_end_date,
     *      test_admin.test_admin_name,
     *      test_admin.login_start_date::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  test_admin.TEST_ADMIN_ID as testAdminId,  test_admin.CUSTOMER_ID as customerId,  test_admin.TEST_ADMIN_NAME as testAdminName,  test_admin.PRODUCT_ID as productId,  product.parent_product_id as parentProductId,  product.license_enabled as licenseEnabled,  test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,  node.org_node_name as creatorOrgNodeName,  cat.category_name as creatorOrgNodeCategoryName,  test_admin.ACCESS_CODE as accessCode,  test_admin.LOCATION as location,  test_admin.LOGIN_START_DATE as loginStartDate,  test_admin.LOGIN_END_DATE as loginEndDate,  test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,  test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,  test_admin.PREFERRED_FORM as preferredForm,  test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,  test_admin.TIME_ZONE as timeZone,  test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,  test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,  test_admin.ENFORCE_BREAK as enforceBreak,  users.USER_NAME as createdBy,  test_admin.CREATED_DATE_TIME as createdDateTime,  test_admin.ACTIVATION_STATUS as activationStatus,  test_admin.ITEM_SET_ID as itemSetId,  test_admin.TEST_ADMIN_STATUS as testAdminStatus,  test_admin.SESSION_NUMBER as sessionNumber,  test_admin.TEST_ADMIN_TYPE as testAdminType,  test.item_Set_name as testName,  nvl(report.completed, 'F') as reportable from  test_admin,  \t  users,  \t  item_set test, \t  org_node node, \t  org_node_category cat,  product, \t  (select  \t  \t\t  ros.test_admin_id,  \t\t\t  decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed  \t  \tfrom  \t\t\t  test_roster ros,  \t\t\t  test_admin,  \t\t\t  users \t  \twhere  \t\t\t  ros.test_admin_id = test_admin.test_admin_id \t\t\t  and test_Admin.created_by = users.user_id  \t\t\t  and ros.test_completion_Status = 'CO' \t\t\t  and ros.validation_status = 'VA' \t\t\t  and users.user_name = {userName} \t\t\t  and test_admin.activation_status = 'AC' \t\tgroup by  \t\t\t  ros.test_admin_id) report where  \t\ttest.item_set_id = test_admin.item_set_id \t\tand node.org_node_id = test_Admin.creator_org_node_id \t\tand test_Admin.test_admin_id = report.test_admin_id (+)  and test_admin.activation_status = 'AC'  and test_admin.created_by = users.user_id  and users.user_name = {userName}  and cat.org_node_category_id = node.org_node_category_id  and product.product_id = test_admin.PRODUCT_ID order by  test_admin.login_end_date,  test_admin.test_admin_name,  test_admin.login_start_date",
                     arrayMaxLength = 100000)
    TestSession [] getTestAdminsForUser(String userName) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select
     *     test_admin.TEST_ADMIN_ID as testAdminId,
     *     test_admin.CUSTOMER_ID as customerId,
     *     test_admin.TEST_ADMIN_NAME as testAdminName,
     *     test_admin.PRODUCT_ID as productId,
     *     test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,
     *     node.org_node_name as creatorOrgNodeName,
     *     test_admin.ACCESS_CODE as accessCode,
     *     test_admin.LOCATION as location,
     *     test_admin.LOGIN_START_DATE as loginStartDate,
     *     test_admin.LOGIN_END_DATE as loginEndDate,
     *     test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,
     *     test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,
     *     users.USER_NAME as createdBy,
     *     test_admin.CREATED_DATE_TIME as createdDateTime,
     *     test_admin.ACTIVATION_STATUS as activationStatus,
     *     test_admin.ITEM_SET_ID as itemSetId,
     *     test_admin.TEST_ADMIN_STATUS as testAdminStatus,
     *     test_admin.SESSION_NUMBER as sessionNumber,
     *     test_admin.TEST_ADMIN_TYPE as testAdminType,
     *     test_admin.PREFERRED_FORM as preferredForm,
     *     test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,
     *     test_admin.TIME_ZONE as timeZone,
     *     test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,
     *     test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,
     *     test_admin.ENFORCE_BREAK as enforceBreak,
     *     test.item_Set_name as testName,
     *     nvl(report.completed, 'F') as reportable
     * from 
     *      test_admin, users, item_set test, test_admin_item_set tais,
     *      org_node node,
     *      (select 
	 *	   		   ros.test_admin_id, 
	 *			   decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed 
	 *	   	from 
	 *			   test_roster ros, 
	 *			   test_admin,
     *			   test_admin_item_set tais
	 *	   	where 
	 *			   ros.test_admin_id = test_admin.test_admin_id
     *			   and tais.test_admin_id = test_admin.test_admin_id
	 *			   and ros.test_completion_Status = 'CO'
     *			   and ros.validation_status = 'VA'
	 *			   and test_admin.activation_status = 'AC'
     *             and upper(tais.access_code) = upper({accessCode})
	 *		group by 
	 *			   ros.test_admin_id) report
     * where 
     *      test_admin.activation_status = 'AC'
     *      and node.org_node_id = test_Admin.creator_org_node_id
     *      and tais.test_admin_id = test_admin.test_admin_id
     *      and test.item_Set_id = test_admin.item_set_id
	 *		and test_Admin.test_admin_id = report.test_admin_id (+)
     *      and test_admin.created_by = users.user_id
     *      and upper(tais.access_code) = upper({accessCode})::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  test_admin.TEST_ADMIN_ID as testAdminId,  test_admin.CUSTOMER_ID as customerId,  test_admin.TEST_ADMIN_NAME as testAdminName,  test_admin.PRODUCT_ID as productId,  test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,  node.org_node_name as creatorOrgNodeName,  test_admin.ACCESS_CODE as accessCode,  test_admin.LOCATION as location,  test_admin.LOGIN_START_DATE as loginStartDate,  test_admin.LOGIN_END_DATE as loginEndDate,  test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,  test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,  users.USER_NAME as createdBy,  test_admin.CREATED_DATE_TIME as createdDateTime,  test_admin.ACTIVATION_STATUS as activationStatus,  test_admin.ITEM_SET_ID as itemSetId,  test_admin.TEST_ADMIN_STATUS as testAdminStatus,  test_admin.SESSION_NUMBER as sessionNumber,  test_admin.TEST_ADMIN_TYPE as testAdminType,  test_admin.PREFERRED_FORM as preferredForm,  test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,  test_admin.TIME_ZONE as timeZone,  test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,  test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,  test_admin.ENFORCE_BREAK as enforceBreak,  test.item_Set_name as testName,  nvl(report.completed, 'F') as reportable from  test_admin, users, item_set test, test_admin_item_set tais,  org_node node,  (select  \t  \t\t/*+ index(ros XIF121TEST_ROSTER) */   ros.test_admin_id,  \t\t\t  decode(count(1), 0, 'F', 'T') as completed  \t  \tfrom  \t\t\t  test_roster ros,  \t\t\t  test_admin, \t\t\t  test_admin_item_set tais \t  \twhere  \t\t\t  ros.test_admin_id = test_admin.test_admin_id \t\t\t  and tais.test_admin_id = test_admin.test_admin_id \t\t\t  and ros.test_completion_Status = 'CO' \t\t\t  and ros.validation_status = 'VA' \t\t\t  and test_admin.activation_status = 'AC'  and upper(tais.access_code) = upper({accessCode}) \t\tgroup by  \t\t\t  ros.test_admin_id) report where  test_admin.activation_status = 'AC'  and node.org_node_id = test_Admin.creator_org_node_id  and tais.test_admin_id = test_admin.test_admin_id  and test.item_Set_id = test_admin.item_set_id \t\tand test_Admin.test_admin_id = report.test_admin_id (+)  and test_admin.created_by = users.user_id  and upper(tais.access_code) = upper({accessCode})",
                     arrayMaxLength = 0, fetchSize = 100)
    TestSession [] getTestAdminsByAccessCode(String accessCode) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select
     *     test_admin.TEST_ADMIN_ID as testAdminId,
     *     test_admin.CUSTOMER_ID as customerId,
     *     test_admin.TEST_ADMIN_NAME as testAdminName,
     *     test_admin.PRODUCT_ID as productId,
     *     test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,
     *     node.org_node_name as creatorOrgNodeName,
     *     test_admin.ACCESS_CODE as accessCode,
     *     test_admin.LOCATION as location,
     *     test_admin.LOGIN_START_DATE as loginStartDate,
     *     test_admin.LOGIN_END_DATE as loginEndDate,
     *     test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,
     *     test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,
     *     users.USER_NAME as createdBy,
     *     test_admin.CREATED_DATE_TIME as createdDateTime,
     *     test_admin.ACTIVATION_STATUS as activationStatus,
     *     test_admin.ITEM_SET_ID as itemSetId,
     *     test_admin.TEST_ADMIN_STATUS as testAdminStatus,
     *     test_admin.SESSION_NUMBER as sessionNumber,
     *     test_admin.TEST_ADMIN_TYPE as testAdminType,
     *     test_admin.PREFERRED_FORM as preferredForm,
     *     test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,
     *     test_admin.TIME_ZONE as timeZone,
     *     test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,
     *     test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,
     *     test_admin.ENFORCE_BREAK as enforceBreak,
     *     test.item_Set_name as testName,
     *     nvl(report.completed, 'F') as reportable
     * from 
     *      test_admin, users, item_set test, test_admin_item_set tais,
     *      org_node node,
     *      (select 
	 *	   		   ros.test_admin_id, 
	 *			   decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed 
	 *	   	from 
	 *			   test_roster ros, 
	 *			   test_admin,
     *			   test_admin_item_set tais
	 *	   	where 
	 *			   ros.test_admin_id = test_admin.test_admin_id
     *			   and tais.test_admin_id = test_admin.test_admin_id
	 *			   and ros.test_completion_Status = 'CO'
     *			   and ros.validation_status = 'VA'
	 *			   and test_admin.activation_status = 'AC'
     *             and upper(tais.access_code) = upper({accessCode})
	 *		group by 
	 *			   ros.test_admin_id) report
     * where 
     *      test_admin.activation_status = 'AC'
     *      and test_admin.test_admin_id != {testAdminId}
     *      and node.org_node_id = test_Admin.creator_org_node_id
     *      and tais.test_admin_id = test_admin.test_admin_id
     *      and test.item_Set_id = test_admin.item_set_id
	 *		and test_Admin.test_admin_id = report.test_admin_id (+)
     *      and test_admin.created_by = users.user_id
     *      and upper(tais.access_code) = upper({accessCode})::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  test_admin.TEST_ADMIN_ID as testAdminId,  test_admin.CUSTOMER_ID as customerId,  test_admin.TEST_ADMIN_NAME as testAdminName,  test_admin.PRODUCT_ID as productId,  test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,  node.org_node_name as creatorOrgNodeName,  test_admin.ACCESS_CODE as accessCode,  test_admin.LOCATION as location,  test_admin.LOGIN_START_DATE as loginStartDate,  test_admin.LOGIN_END_DATE as loginEndDate,  test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,  test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,  users.USER_NAME as createdBy,  test_admin.CREATED_DATE_TIME as createdDateTime,  test_admin.ACTIVATION_STATUS as activationStatus,  test_admin.ITEM_SET_ID as itemSetId,  test_admin.TEST_ADMIN_STATUS as testAdminStatus,  test_admin.SESSION_NUMBER as sessionNumber,  test_admin.TEST_ADMIN_TYPE as testAdminType,  test_admin.PREFERRED_FORM as preferredForm,  test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,  test_admin.TIME_ZONE as timeZone,  test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,  test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,  test_admin.ENFORCE_BREAK as enforceBreak,  test.item_Set_name as testName,  nvl(report.completed, 'F') as reportable from  test_admin, users, item_set test, test_admin_item_set tais,  org_node node,  (select  \t  \t\t /*+ index(ros XIF121TEST_ROSTER) */  ros.test_admin_id,  \t\t\t  decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed  \t  \tfrom  \t\t\t  test_roster ros,  \t\t\t  test_admin, \t\t\t  test_admin_item_set tais \t  \twhere  \t\t\t  ros.test_admin_id = test_admin.test_admin_id \t\t\t  and tais.test_admin_id = test_admin.test_admin_id \t\t\t  and ros.test_completion_Status = 'CO' \t\t\t  and ros.validation_status = 'VA' \t\t\t  and test_admin.activation_status = 'AC'  and upper(tais.access_code) = upper({accessCode}) \t\tgroup by  \t\t\t  ros.test_admin_id) report where  test_admin.activation_status = 'AC'  and test_admin.test_admin_id != {testAdminId}  and node.org_node_id = test_Admin.creator_org_node_id  and tais.test_admin_id = test_admin.test_admin_id  and test.item_Set_id = test_admin.item_set_id \t\tand test_Admin.test_admin_id = report.test_admin_id (+)  and test_admin.created_by = users.user_id  and upper(tais.access_code) = upper({accessCode})",
                     arrayMaxLength = 0, fetchSize=10)
    TestSession [] getTestAdminsByAccessCodeIgnoreAdmin(String accessCode, Integer testAdminId) throws SQLException;

    
    /**
     * @jc:sql statement::
     * select distinct
     *     test_admin.TEST_ADMIN_ID as testAdminId,
     *     test_admin.TEST_ADMIN_NAME as testAdminName,
     *     test_admin.LOCATION as location,
     *     test_admin.LOGIN_START_DATE as loginStartDate,
     *     test_admin.LOGIN_END_DATE as loginEndDate,
     *     test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,
     *     test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,
     *     node.org_node_id as orgNodeId,
     *     node.org_node_name as orgNodeName,
     *     test_admin.time_zone as timeZone
     * from 
     *      test_admin,
     *      org_node node
     * where 
     *      node.org_node_id = test_admin.creator_org_node_id
     *      and test_admin.activation_status = 'AC'
     *      and (to_date(concat(to_char(test_Admin.login_start_date, 'MON-DD-YYYY'), to_char(test_Admin.daily_login_start_time, ' HH24:MI:SS')), 'MON-DD-YYYY HH24:MI:SS') >= SYSDATE 
     *           or to_date(concat(to_char(test_admin.login_end_date, 'MON-DD-YYYY'), to_char(test_admin.daily_login_end_time, ' HH24:MI:SS')), 'MON-DD-YYYY HH24:MI:SS') >= SYSDATE)
     *      and test_admin.item_set_id = {itemSetId}
     * order by
     *      test_admin.login_start_date,
     *      test_admin.login_end_date::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  test_admin.TEST_ADMIN_ID as testAdminId,  test_admin.TEST_ADMIN_NAME as testAdminName,  test_admin.LOCATION as location,  test_admin.LOGIN_START_DATE as loginStartDate,  test_admin.LOGIN_END_DATE as loginEndDate,  test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,  test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,  node.org_node_id as orgNodeId,  node.org_node_name as orgNodeName,  test_admin.time_zone as timeZone from  test_admin,  org_node node where  node.org_node_id = test_admin.creator_org_node_id  and test_admin.activation_status = 'AC'  and (to_date(concat(to_char(test_Admin.login_start_date, 'MON-DD-YYYY'), to_char(test_Admin.daily_login_start_time, ' HH24:MI:SS')), 'MON-DD-YYYY HH24:MI:SS') >= SYSDATE  or to_date(concat(to_char(test_admin.login_end_date, 'MON-DD-YYYY'), to_char(test_admin.daily_login_end_time, ' HH24:MI:SS')), 'MON-DD-YYYY HH24:MI:SS') >= SYSDATE)  and test_admin.item_set_id = {itemSetId} order by  test_admin.login_start_date,  test_admin.login_end_date",
                     arrayMaxLength = 100000)
    ActiveSession [] getActiveSessionsForTest(Integer itemSetId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select
     *     test_admin.TEST_ADMIN_ID as testAdminId,
     *     test_admin.CUSTOMER_ID as customerId,
     *     test_admin.TEST_ADMIN_NAME as testAdminName,
     *     test_admin.PRODUCT_ID as productId,
     *     product.parent_product_id as parentProductId,
     *     product.license_enabled as licenseEnabled,
     *     test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,
     *     node.org_node_name as creatorOrgNodeName,
     *     cat.category_name as creatorOrgNodeCategoryName,
     *     test_admin.ACCESS_CODE as accessCode,
     *     test_admin.LOCATION as location,
     *     test_admin.LOGIN_START_DATE as loginStartDate,
     *     test_admin.LOGIN_END_DATE as loginEndDate,
     *     test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,
     *     test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,
     *     users.USER_NAME as createdBy,
     *     test_admin.CREATED_DATE_TIME as createdDateTime,
     *     test_admin.ACTIVATION_STATUS as activationStatus,
     *     test_admin.ITEM_SET_ID as itemSetId,
     *     test_admin.TEST_ADMIN_STATUS as testAdminStatus,
     *     test_admin.SESSION_NUMBER as sessionNumber,
     *     test_admin.TEST_ADMIN_TYPE as testAdminType,
     *     test_admin.PREFERRED_FORM as preferredForm,
     *     test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,
     *     test_admin.TIME_ZONE as timeZone,
     *     test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,
     *     test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,
     *     test_admin.ENFORCE_BREAK as enforceBreak,
     *     test.item_Set_name as testName,
     *     nvl(report.completed, 'F') as reportable
     * from 
     *      test_admin, users,
     *      item_set test, 
     *      org_node node,
     *      org_node_category cat,
     *      product,
	 *	   (select 
	 *	   		   ros.test_admin_id, 
	 *			   decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed 
	 *	   	from 
	 *			   test_roster ros, 
	 *			   test_admin
	 *	   	where 
	 *			   ros.test_admin_id = test_admin.test_admin_id
	 *			   and ros.test_completion_Status = 'CO'
     *			   and ros.validation_status = 'VA'
	 *			   and test_admin.activation_status = 'AC'
     *             and test_admin.creator_org_node_id = {orgNodeId}
	 *		group by 
	 *			   ros.test_admin_id) report
     * where 
     *      users.user_id = test_admin.created_by
     *      and node.org_node_id = test_Admin.creator_org_node_id
     *      and test.item_set_id = test_admin.item_set_id
	 *		and test_Admin.test_admin_id = report.test_admin_id (+)
     *      and test_admin.activation_status = 'AC'
     *      and test_admin.creator_org_node_id = {orgNodeId}
     *      and cat.org_node_category_id = node.org_node_category_id
     *      and product.product_id = test_admin.PRODUCT_ID
     * order by
     *      test_admin.login_end_date,
     *      test_admin.test_admin_name,
     *      test_admin.login_start_date::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  test_admin.TEST_ADMIN_ID as testAdminId,  test_admin.CUSTOMER_ID as customerId,  test_admin.TEST_ADMIN_NAME as testAdminName,  test_admin.PRODUCT_ID as productId,  product.parent_product_id as parentProductId,  product.license_enabled as licenseEnabled,  test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,  node.org_node_name as creatorOrgNodeName,  cat.category_name as creatorOrgNodeCategoryName,  test_admin.ACCESS_CODE as accessCode,  test_admin.LOCATION as location,  test_admin.LOGIN_START_DATE as loginStartDate,  test_admin.LOGIN_END_DATE as loginEndDate,  test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,  test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,  users.USER_NAME as createdBy,  test_admin.CREATED_DATE_TIME as createdDateTime,  test_admin.ACTIVATION_STATUS as activationStatus,  test_admin.ITEM_SET_ID as itemSetId,  test_admin.TEST_ADMIN_STATUS as testAdminStatus,  test_admin.SESSION_NUMBER as sessionNumber,  test_admin.TEST_ADMIN_TYPE as testAdminType,  test_admin.PREFERRED_FORM as preferredForm,  test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,  test_admin.TIME_ZONE as timeZone,  test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,  test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,  test_admin.ENFORCE_BREAK as enforceBreak,  test.item_Set_name as testName,  nvl(report.completed, 'F') as reportable from  test_admin, users,  item_set test,  org_node node,  org_node_category cat,  product, \t  (select  \t  \t\t  ros.test_admin_id,  \t\t\t  decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed  \t  \tfrom  \t\t\t  test_roster ros,  \t\t\t  test_admin \t  \twhere  \t\t\t  ros.test_admin_id = test_admin.test_admin_id \t\t\t  and ros.test_completion_Status = 'CO' \t\t\t  and ros.validation_status = 'VA' \t\t\t  and test_admin.activation_status = 'AC'  and test_admin.creator_org_node_id = {orgNodeId} \t\tgroup by  \t\t\t  ros.test_admin_id) report where  users.user_id = test_admin.created_by  and node.org_node_id = test_Admin.creator_org_node_id  and test.item_set_id = test_admin.item_set_id \t\tand test_Admin.test_admin_id = report.test_admin_id (+)  and test_admin.activation_status = 'AC'  and test_admin.creator_org_node_id = {orgNodeId}  and cat.org_node_category_id = node.org_node_category_id  and product.product_id = test_admin.PRODUCT_ID order by  test_admin.login_end_date,  test_admin.test_admin_name,  test_admin.login_start_date",
                     arrayMaxLength = 100000)
    TestSession [] getTestAdminsForOrgNode(Integer orgNodeId) throws SQLException;
    
    @JdbcControl.SQL(statement = "select test_admin.TEST_ADMIN_ID as testAdminId,        test_admin.CUSTOMER_ID as customerId,        test_admin.TEST_ADMIN_NAME as testAdminName,        test_admin.PRODUCT_ID as productId,        product.parent_product_id as parentProductId,        product.license_enabled as licenseEnabled,        test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,        node.org_node_name as creatorOrgNodeName,        cat.category_name as creatorOrgNodeCategoryName,        test_admin.ACCESS_CODE as accessCode,        test_admin.LOCATION as location,        test_admin.LOGIN_START_DATE as loginStartDate,        test_admin.LOGIN_END_DATE as loginEndDate,        test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,        test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,        users.USER_NAME as createdBy,        test_admin.CREATED_DATE_TIME as createdDateTime,        test_admin.ACTIVATION_STATUS as activationStatus,        test_admin.ITEM_SET_ID as itemSetId,        test_admin.TEST_ADMIN_STATUS as testAdminStatus,        test_admin.SESSION_NUMBER as sessionNumber,        test_admin.TEST_ADMIN_TYPE as testAdminType,        test_admin.PREFERRED_FORM as preferredForm,        test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,        test_admin.TIME_ZONE as timeZone,        test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,        test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,        test_admin.ENFORCE_BREAK as enforceBreak,        test.item_Set_name as testName,        nvl(report.completed, 'F') as reportable   from test_admin,        users,        item_set test,        org_node node,        org_node_category cat,        product,         (select ros.test_admin_id,                  decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed from  test_roster ros,                  test_admin  where ros.test_admin_id = test_admin.test_admin_id  and ros.test_completion_Status = 'CO'  and ros.validation_status = 'VA' and test_admin.activation_status = 'AC' and test_admin.creator_org_node_id ={orgNodeId} group by ros.test_admin_id) report  where users.user_id = test_admin.created_by    and node.org_node_id = test_Admin.creator_org_node_id    and test.item_set_id = test_admin.item_set_id    and test_Admin.test_admin_id = report.test_admin_id(+)    and test_admin.activation_status = 'AC'    and test_admin.creator_org_node_id = {orgNodeId}   AND TEST_ADMIN.TEST_ADMIN_STATUS IN ('CU','FU') and cat.org_node_category_id = node.org_node_category_id    and product.product_id = test_admin.PRODUCT_ID    and test_admin.PRODUCT_ID = {selectedProductId}  order by test_admin.login_end_date,           test_admin.test_admin_name,           test_admin.login_start_date",
            arrayMaxLength = 1000000)
    TestSession [] getRecommendedTestSessionsForOrgNode(Integer orgNodeId, Integer selectedProductId) throws SQLException;
    
    @JdbcControl.SQL(statement = "SELECT TEST_ADMIN.TEST_ADMIN_ID AS TESTADMINID,        TEST_ADMIN.CUSTOMER_ID AS CUSTOMERID,        TEST_ADMIN.TEST_ADMIN_NAME AS TESTADMINNAME,        TEST_ADMIN.PRODUCT_ID AS PRODUCTID,        PRODUCT.PARENT_PRODUCT_ID AS PARENTPRODUCTID,        PRODUCT.LICENSE_ENABLED AS LICENSEENABLED,        TEST_ADMIN.CREATOR_ORG_NODE_ID AS CREATORORGNODEID,        NODE.ORG_NODE_NAME AS CREATORORGNODENAME,        CAT.CATEGORY_NAME AS CREATORORGNODECATEGORYNAME,        TEST_ADMIN.ACCESS_CODE AS ACCESSCODE,        TEST_ADMIN.LOCATION AS LOCATION,        TEST_ADMIN.LOGIN_START_DATE AS LOGINSTARTDATE,        TEST_ADMIN.LOGIN_END_DATE AS LOGINENDDATE,        TEST_ADMIN.DAILY_LOGIN_START_TIME AS DAILYLOGINSTARTTIME,        TEST_ADMIN.DAILY_LOGIN_END_TIME AS DAILYLOGINENDTIME,        USERS.USER_NAME AS CREATEDBY,        TEST_ADMIN.CREATED_DATE_TIME AS CREATEDDATETIME,        TEST_ADMIN.ACTIVATION_STATUS AS ACTIVATIONSTATUS,        TEST_ADMIN.ITEM_SET_ID AS ITEMSETID,        TEST_ADMIN.TEST_ADMIN_STATUS AS TESTADMINSTATUS,        TEST_ADMIN.SESSION_NUMBER AS SESSIONNUMBER,        TEST_ADMIN.TEST_ADMIN_TYPE AS TESTADMINTYPE,        TEST_ADMIN.PREFERRED_FORM AS PREFERREDFORM,        TEST_ADMIN.FORM_ASSIGNMENT_METHOD AS FORMASSIGNMENTMETHOD,        TEST_ADMIN.TIME_ZONE AS TIMEZONE,        TEST_ADMIN.SHOW_STUDENT_FEEDBACK AS SHOWSTUDENTFEEDBACK,        TEST_ADMIN.ENFORCE_TIME_LIMIT AS ENFORCETIMELIMIT,        TEST_ADMIN.ENFORCE_BREAK AS ENFORCEBREAK,        TEST.ITEM_SET_NAME AS TESTNAME,        NVL(REPORT.COMPLETED, 'F') AS REPORTABLE   FROM TEST_ADMIN,        USERS,        ITEM_SET TEST,        ORG_NODE NODE,        ORG_NODE_CATEGORY CAT,        PRODUCT,        (SELECT   ROS.TEST_ADMIN_ID,                    DECODE(COUNT(DISTINCT ROS.TEST_ROSTER_ID), 0, 'F', 'T') AS COMPLETED FROM  TEST_ROSTER ROS,                    TEST_ADMIN WHERE   ROS.TEST_ADMIN_ID = TEST_ADMIN.TEST_ADMIN_ID   AND ROS.TEST_COMPLETION_STATUS = 'CO'   AND ROS.VALIDATION_STATUS = 'VA'   AND TEST_ADMIN.ACTIVATION_STATUS = 'AC' AND TEST_ADMIN.CREATOR_ORG_NODE_ID = {orgNodeId} GROUP BY   ROS.TEST_ADMIN_ID) REPORT  WHERE USERS.USER_ID = TEST_ADMIN.CREATED_BY    AND NODE.ORG_NODE_ID = TEST_ADMIN.CREATOR_ORG_NODE_ID    AND TEST.ITEM_SET_ID = TEST_ADMIN.ITEM_SET_ID AND  TEST_ADMIN.TEST_ADMIN_ID = REPORT.TEST_ADMIN_ID(+)    AND TEST_ADMIN.ACTIVATION_STATUS = 'AC'    AND TEST_ADMIN.CREATOR_ORG_NODE_ID = {orgNodeId}    AND TEST_ADMIN.TEST_ADMIN_STATUS IN ('CU','FU')    AND CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID    AND PRODUCT.PRODUCT_ID = TEST_ADMIN.PRODUCT_ID  ORDER BY TEST_ADMIN.LOGIN_END_DATE,           TEST_ADMIN.TEST_ADMIN_NAME,           TEST_ADMIN.LOGIN_START_DATE",
            arrayMaxLength = 1000000)
    TestSession [] getAllTestSessionsForOrgNode(Integer orgNodeId) throws SQLException;


    /**
     * @jc:sql statement::
     * select
     *     test_admin.TEST_ADMIN_ID as testAdminId,
     *     test_admin.CUSTOMER_ID as customerId,
     *     test_admin.TEST_ADMIN_NAME as testAdminName,
     *     test_admin.PRODUCT_ID as productId,
     *     test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,
     *     node.org_node_name as creatorOrgNodeName,
     *     test_admin.ACCESS_CODE as accessCode,
     *     test_admin.LOCATION as location,
     *     test_admin.LOGIN_START_DATE as loginStartDate,
     *     test_admin.LOGIN_END_DATE as loginEndDate,
     *     test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,
     *     test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,
     *     users.USER_NAME as createdBy,
     *     test_admin.CREATED_DATE_TIME as createdDateTime,
     *     test_admin.ACTIVATION_STATUS as activationStatus,
     *     test_admin.ITEM_SET_ID as itemSetId,
     *     test_admin.TEST_ADMIN_STATUS as testAdminStatus,
     *     test_admin.SESSION_NUMBER as sessionNumber,
     *     test_admin.TEST_ADMIN_TYPE as testAdminType,
     *     test_admin.PREFERRED_FORM as preferredForm,
     *     test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,
     *     test_admin.TIME_ZONE as timeZone,
     *     test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,
     *     test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,
     *     test_admin.ENFORCE_BREAK as enforceBreak,
     *     test_admin.LEXINGTON_VERSION as lexingtonVersion,
     *     test.item_Set_name as testName,
     *     nvl(report.completed, 'F') as reportable,
     *     ontc.override_form_assignment as overrideFormAssignmentMethod,
     *     ontc.override_login_start_date as overrideLoginStartDate,
     *     test_admin.test_catalog_id as testCatalogId,
     *     test_admin.program_id as programId,
     *     test_admin.random_distractor_status as isRandomize,
     *     test_admin.test_session_data_exported as isTestSessionDataExported
     * from 
     *      test_admin, users, item_set test,
     *      org_node node,
     *      org_node_test_catalog ontc,
     *      (select 
	 *	   		   ros.test_admin_id, 
	 *			   decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed 
	 *	   	from 
	 *			   test_roster ros, 
	 *			   test_admin
	 *	   	where 
	 *			   ros.test_admin_id = test_admin.test_admin_id
	 *			   and ros.test_completion_Status = 'CO'
     *			   and ros.validation_status = 'VA'
	 *			   and test_admin.activation_status = 'AC'
     *             and test_admin.test_admin_id = {testAdminId}
	 *		group by 
	 *			   ros.test_admin_id) report
     * where 
     *      test_admin.activation_status = 'AC'
     *      and ontc.item_set_id (+) = test_admin.item_set_id
     *      and ontc.org_node_id (+) = test_admin.creator_org_node_id
     *      and node.org_node_id = test_Admin.creator_org_node_id
     *      and test.item_Set_id = test_admin.item_set_id
	 *		and test_Admin.test_admin_id = report.test_admin_id (+)
     *      and test_admin.created_by = users.user_id
     *      and test_admin.test_admin_id = {testAdminId}::
     */
    @JdbcControl.SQL(statement = "select  test_admin.TEST_ADMIN_ID as testAdminId,  test_admin.CUSTOMER_ID as customerId,  test_admin.TEST_ADMIN_NAME as testAdminName,  test_admin.PRODUCT_ID as productId,  test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,  node.org_node_name as creatorOrgNodeName, DECODE( TEST_ADMIN.CREATED_BY, USERS.USER_ID,'Owner', 'Proctor') AS AssignedRole, test_admin.ACCESS_CODE as accessCode,  test_admin.LOCATION as location,  test_admin.LOGIN_START_DATE as loginStartDate,  test_admin.LOGIN_END_DATE as loginEndDate,  test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,  test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,  users.USER_NAME as createdBy,  test_admin.CREATED_DATE_TIME as createdDateTime,  test_admin.ACTIVATION_STATUS as activationStatus,  test_admin.ITEM_SET_ID as itemSetId,  test_admin.TEST_ADMIN_STATUS as testAdminStatus,  test_admin.SESSION_NUMBER as sessionNumber,  test_admin.TEST_ADMIN_TYPE as testAdminType,  test_admin.PREFERRED_FORM as preferredForm,  test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,  test_admin.TIME_ZONE as timeZone,  test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,  test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,  test_admin.ENFORCE_BREAK as enforceBreak, test_admin.LEXINGTON_VERSION as lexingtonVersion, test.item_Set_name as testName,  nvl(report.completed, 'F') as reportable,  ontc.override_form_assignment as overrideFormAssignmentMethod,  ontc.override_login_start_date as overrideLoginStartDate,  test_admin.test_catalog_id as testCatalogId,  test_admin.program_id as programId,  test_admin.random_distractor_status as isRandomize,  test_admin.test_session_data_exported as isTestSessionDataExported , cat.test_level as testLevel, product.product_type as productType from  test_admin, users, item_set test,  org_node node,  org_node_test_catalog ontc,  test_catalog cat, product, (select  \t  \t\t  ros.test_admin_id,  \t\t\t  decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed  \t  \tfrom  \t\t\t  test_roster ros,  \t\t\t  test_admin \t  \twhere  \t\t\t  ros.test_admin_id = test_admin.test_admin_id \t\t\t  and ros.test_completion_Status = 'CO' \t\t\t  and ros.validation_status = 'VA' \t\t\t  and test_admin.activation_status = 'AC' and test_admin.test_admin_id = {testAdminId} \t\tgroup by  \t\t\t  ros.test_admin_id) report where  test_admin.activation_status = 'AC'  and ontc.item_set_id (+) = test_admin.item_set_id  and ontc.org_node_id (+) = test_admin.creator_org_node_id  and node.org_node_id = test_Admin.creator_org_node_id  and test.item_Set_id = test_admin.item_set_id \t\tand test_Admin.test_admin_id = report.test_admin_id (+)  and test_admin.created_by = users.user_id and ontc.test_catalog_id = cat.test_catalog_id(+) and product.product_id = test_admin.product_id and test_admin.test_admin_id = {testAdminId}")
    TestSession getTestAdminDetails(Integer testAdminId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select
     *     test_admin.TEST_ADMIN_ID as testAdminId,
     *     test_admin.CUSTOMER_ID as customerId,
     *     test_admin.TEST_ADMIN_NAME as testAdminName,
     *     test_admin.PRODUCT_ID as productId,
     *     test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,
     *     node.org_node_name as creatorOrgNodeName,
     *     test_admin.ACCESS_CODE as accessCode,
     *     test_admin.LOCATION as location,
     *     test_admin.LOGIN_START_DATE as loginStartDate,
     *     test_admin.LOGIN_END_DATE as loginEndDate,
     *     test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,
     *     test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,
     *     users.USER_NAME as createdBy,
     *     test_admin.CREATED_DATE_TIME as createdDateTime,
     *     test_admin.ACTIVATION_STATUS as activationStatus,
     *     test_admin.ITEM_SET_ID as itemSetId,
     *     test_admin.TEST_ADMIN_STATUS as testAdminStatus,
     *     test_admin.SESSION_NUMBER as sessionNumber,
     *     test_admin.TEST_ADMIN_TYPE as testAdminType,
     *     test_admin.PREFERRED_FORM as preferredForm,
     *     test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,
     *     test_admin.TIME_ZONE as timeZone,
     *     test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,
     *     test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,
     *     test_admin.ENFORCE_BREAK as enforceBreak,
     *     test_admin.LEXINGTON_VERSION as lexingtonVersion,
     *     test.item_Set_name as testName,
     *     nvl(report.completed, 'F') as reportable,
     *     ontc.override_form_assignment as overrideFormAssignmentMethod,
     *     ontc.override_login_start_date as overrideLoginStartDate,
     *     test_admin.test_catalog_id as testCatalogId
     * from 
     *      test_admin, users, item_set test,
     *      org_node node,
     *      org_node_test_catalog ontc,
     *      test_roster,
     *      (select 
	 *	   		   ros.test_admin_id, 
	 *			   decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed 
	 *	   	from 
	 *			   test_roster ros, 
	 *			   test_admin
	 *	   	where 
	 *			   ros.test_admin_id = test_admin.test_admin_id
	 *			   and ros.test_completion_Status = 'CO'
     *			   and ros.validation_status = 'VA'
	 *			   and test_admin.activation_status = 'AC'
     *             and test_admin.test_admin_id = {testAdminId}
	 *		group by 
	 *			   ros.test_admin_id) report
     * where 
     *      test_admin.activation_status = 'AC'
     *      and ontc.item_set_id (+) = test_admin.item_set_id
     *      and ontc.org_node_id (+) = test_admin.creator_org_node_id
     *      and node.org_node_id = test_Admin.creator_org_node_id
     *      and test.item_Set_id = test_admin.item_set_id
	 *		and test_Admin.test_admin_id = report.test_admin_id (+)
     *      and test_admin.created_by = users.user_id
     *      and test_admin.test_admin_id = {testAdminId}
     *      and test_roster.test_admin_id test_admin.test_admin_id
     *      and test_roster.test_Roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "select  test_admin.TEST_ADMIN_ID as testAdminId,  test_admin.CUSTOMER_ID as customerId,  test_admin.TEST_ADMIN_NAME as testAdminName,  test_admin.PRODUCT_ID as productId,  test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,  node.org_node_name as creatorOrgNodeName,  test_admin.ACCESS_CODE as accessCode,  test_admin.LOCATION as location,  test_admin.LOGIN_START_DATE as loginStartDate,  test_admin.LOGIN_END_DATE as loginEndDate,  test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,  test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,  users.USER_NAME as createdBy,  test_admin.CREATED_DATE_TIME as createdDateTime,  test_admin.ACTIVATION_STATUS as activationStatus,  test_admin.ITEM_SET_ID as itemSetId,  test_admin.TEST_ADMIN_STATUS as testAdminStatus,  test_admin.SESSION_NUMBER as sessionNumber,  test_admin.TEST_ADMIN_TYPE as testAdminType,  test_admin.PREFERRED_FORM as preferredForm,  test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,  test_admin.TIME_ZONE as timeZone,  test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,  test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,  test_admin.ENFORCE_BREAK as enforceBreak, test_admin.LEXINGTON_VERSION as lexingtonVersion, test.item_Set_name as testName,  nvl(report.completed, 'F') as reportable,  ontc.override_form_assignment as overrideFormAssignmentMethod,  ontc.override_login_start_date as overrideLoginStartDate,  test_admin.test_catalog_id as testCatalogId from  test_admin, users, item_set test,  org_node node,  org_node_test_catalog ontc,  test_roster,  (select  \t  \t\t  ros.test_admin_id,  \t\t\t  decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed  \t  \tfrom  \t\t\t  test_roster ros,  \t\t\t  test_admin \t  \twhere  \t\t\t  ros.test_admin_id = test_admin.test_admin_id \t\t\t  and ros.test_completion_Status = 'CO' \t\t\t  and ros.validation_status = 'VA' \t\t\t  and test_admin.activation_status = 'AC'  and test_admin.test_admin_id = {testAdminId} \t\tgroup by  \t\t\t  ros.test_admin_id) report where  test_admin.activation_status = 'AC'  and ontc.item_set_id (+) = test_admin.item_set_id  and ontc.org_node_id (+) = test_admin.creator_org_node_id  and node.org_node_id = test_Admin.creator_org_node_id  and test.item_Set_id = test_admin.item_set_id \t\tand test_Admin.test_admin_id = report.test_admin_id (+)  and test_admin.created_by = users.user_id  and test_admin.test_admin_id = {testAdminId}  and test_roster.test_admin_id test_admin.test_admin_id  and test_roster.test_Roster_id = {testRosterId}")
    TestSession getTestAdminDetailsForRoster(Integer testRosterId, Integer testAdminId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select
     *     ontc.override_form_assignment as overrideFormAssignmentMethod
     * from 
     *      org_node_test_catalog ontc
     * where 
     *      ontc.item_set_id = {testItemSetId}
     *      and ontc.org_node_id = {orgNodeId}::
     */
    @JdbcControl.SQL(statement = "select  ontc.override_form_assignment as overrideFormAssignmentMethod from  org_node_test_catalog ontc where  ontc.item_set_id = {testItemSetId}  and ontc.org_node_id = {orgNodeId}")
    String getTestCatalogFormAssignmentOverride(Integer testItemSetId, Integer orgNodeId) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *     ontc.override_login_start_date as overrideLoginStartDate
     * from 
     *      org_node_test_catalog ontc
     * where 
     *      ontc.item_set_id = {testItemSetId}
     *      and ontc.org_node_id = {orgNodeId}::
     */
    @JdbcControl.SQL(statement = "select  ontc.override_login_start_date as overrideLoginStartDate from  org_node_test_catalog ontc where  ontc.item_set_id = {testItemSetId}  and ontc.org_node_id = {orgNodeId}")
    Date getTestCatalogLoginStartDateOverride(Integer testItemSetId, Integer orgNodeId) throws SQLException;


    /**
     * @jc:sql statement::
     * select
     *     test_admin.TEST_ADMIN_ID as testAdminId,
     *     test_admin.CUSTOMER_ID as customerId,
     *     test_admin.TEST_ADMIN_NAME as testAdminName,
     *     test_admin.PRODUCT_ID as productId,
     *     product.parent_product_id as parentProductId,
     *     product.license_enabled as licenseEnabled,
     *     test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,
     *     node.org_node_name as creatorOrgNodeName,
     *     cat.category_name as creatorOrgNodeCategoryName,
     *     test_admin.ACCESS_CODE as accessCode,
     *     test_admin.LOCATION as location,
     *     test_admin.LOGIN_START_DATE as loginStartDate,
     *     test_admin.LOGIN_END_DATE as loginEndDate,
     *     test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,
     *     test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,
     *     users.USER_NAME as createdBy,
     *     test_admin.CREATED_DATE_TIME as createdDateTime,
     *     test_admin.ACTIVATION_STATUS as activationStatus,
     *     test_admin.ITEM_SET_ID as itemSetId,
     *     test_admin.TEST_ADMIN_STATUS as testAdminStatus,
     *     test_admin.SESSION_NUMBER as sessionNumber,
     *     test_admin.TEST_ADMIN_TYPE as testAdminType,
     *     test_admin.PREFERRED_FORM as preferredForm,
     *     test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,
     *     test_admin.TIME_ZONE as timeZone,
     *     test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,
     *     test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,
     *     test_admin.ENFORCE_BREAK as enforceBreak,
     *     test.item_Set_name as testName,
     *     nvl(report.completed, 'F') as reportable
     * from 
     *      test_admin, users, test_admin_user_role, item_set test,
     *      org_node node,
     *      org_node_category cat,
     *      product,
     *      (select 
	 *	   		   ros.test_admin_id, 
	 *			   decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed 
	 *	   	from 
	 *			   test_roster ros, 
	 *			   test_admin,
     *			   test_admin_user_role,
     *			   users
	 *	   	where 
	 *			   ros.test_admin_id = test_admin.test_admin_id
	 *			   and ros.test_completion_Status = 'CO'
     *			   and ros.validation_status = 'VA'
	 *			   and test_admin.activation_status = 'AC'
     *             and test_admin_user_role.user_id = users.user_id
     *             and test_admin.test_admin_id = test_admin_user_role.test_admin_id
     *             and users.user_name = {userName}
	 *		group by 
	 *			   ros.test_admin_id) report
     * where 
     *      test_admin.activation_status = 'AC'
     *      and node.org_node_id = test_admin.creator_org_node_id
     *      and report.test_admin_id (+) = test_admin.test_admin_id
     *      and test.item_Set_id = test_admin.item_set_id
     *      and test_admin_user_role.user_id = users.user_id
     *      and test_admin.created_by != users.user_id
     *      and test_admin.test_admin_id = test_admin_user_role.test_admin_id
     *      and users.user_name = {userName}
     *      and cat.org_node_category_id = node.org_node_category_id
     *      and product.product_id = test_admin.PRODUCT_ID
     * order by
     *      test_admin.login_end_date,
     *      test_admin.test_admin_name,
     *      test_admin.login_start_date::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  test_admin.TEST_ADMIN_ID as testAdminId,  test_admin.CUSTOMER_ID as customerId,  test_admin.TEST_ADMIN_NAME as testAdminName,  test_admin.PRODUCT_ID as productId,  product.parent_product_id as parentProductId,  product.license_enabled as licenseEnabled,  test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,  node.org_node_name as creatorOrgNodeName,  cat.category_name as creatorOrgNodeCategoryName,  test_admin.ACCESS_CODE as accessCode,  test_admin.LOCATION as location,  test_admin.LOGIN_START_DATE as loginStartDate,  test_admin.LOGIN_END_DATE as loginEndDate,  test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,  test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,  users.USER_NAME as createdBy,  test_admin.CREATED_DATE_TIME as createdDateTime,  test_admin.ACTIVATION_STATUS as activationStatus,  test_admin.ITEM_SET_ID as itemSetId,  test_admin.TEST_ADMIN_STATUS as testAdminStatus,  test_admin.SESSION_NUMBER as sessionNumber,  test_admin.TEST_ADMIN_TYPE as testAdminType,  test_admin.PREFERRED_FORM as preferredForm,  test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,  test_admin.TIME_ZONE as timeZone,  test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,  test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,  test_admin.ENFORCE_BREAK as enforceBreak,  test.item_Set_name as testName,  nvl(report.completed, 'F') as reportable from  test_admin, users, test_admin_user_role, item_set test,  org_node node,  org_node_category cat,  product,  (select  \t  \t\t  ros.test_admin_id,  \t\t\t  decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed  \t  \tfrom  \t\t\t  test_roster ros,  \t\t\t  test_admin, \t\t\t  test_admin_user_role, \t\t\t  users \t  \twhere  \t\t\t  ros.test_admin_id = test_admin.test_admin_id \t\t\t  and ros.test_completion_Status = 'CO' \t\t\t  and ros.validation_status = 'VA' \t\t\t  and test_admin.activation_status = 'AC'  and test_admin_user_role.user_id = users.user_id  and test_admin.test_admin_id = test_admin_user_role.test_admin_id  and users.user_name = {userName} \t\tgroup by  \t\t\t  ros.test_admin_id) report where  test_admin.activation_status = 'AC'  and node.org_node_id = test_admin.creator_org_node_id  and report.test_admin_id (+) = test_admin.test_admin_id  and test.item_Set_id = test_admin.item_set_id  and test_admin_user_role.user_id = users.user_id  and test_admin.created_by != users.user_id  and test_admin.test_admin_id = test_admin_user_role.test_admin_id  and users.user_name = {userName}  and cat.org_node_category_id = node.org_node_category_id  and product.product_id = test_admin.PRODUCT_ID order by  test_admin.login_end_date,  test_admin.test_admin_name,  test_admin.login_start_date",
                     arrayMaxLength = 100000)
    TestSession [] getProctorAssignmentsForUser(String userName) throws SQLException;
    
    /**
     * @jc:sql statement::
     * insert into
     *     test_admin (
     *          TEST_ADMIN_ID,
     *          CUSTOMER_ID,
     *          TEST_ADMIN_NAME,
     *          PRODUCT_ID,
     *          CREATOR_ORG_NODE_ID,
     *          ACCESS_CODE,
     *          LOCATION,
     *          LOGIN_START_DATE,
     *          LOGIN_END_DATE,
     *          DAILY_LOGIN_START_TIME,
     *          DAILY_LOGIN_END_TIME,
     *          CREATED_BY,
     *          CREATED_DATE_TIME,
     *          ACTIVATION_STATUS,
     *          ITEM_SET_ID,
     *          TEST_ADMIN_STATUS,
     *          SESSION_NUMBER,
     *          TEST_ADMIN_TYPE,
     *          ENFORCE_TIME_LIMIT,
     *          ENFORCE_BREAK,
     *          TIME_ZONE,
     *          TEST_CATALOG_ID,
     *          PREFERRED_FORM,
     *          FORM_ASSIGNMENT_METHOD,
     *          SHOW_STUDENT_FEEDBACK,
     *          PROGRAM_ID,
     *          LEXINGTON_VERSION,
     *          RANDOM_DISTRACTOR_STATUS
     *      ) values (
     *          {session.testAdminId},
     *          {session.customerId},
     *          {session.testAdminName},
     *          {session.productId},
     *          {session.creatorOrgNodeId},
     *          {session.accessCode},
     *          {session.location},
     *          {session.loginStartDate},
     *          {session.loginEndDate},
     *          {session.dailyLoginStartTime},
     *          {session.dailyLoginEndTime},
     *          {session.createdBy},
     *          {session.createdDateTime},
     *          {session.activationStatus},
     *          {session.itemSetId},
     *          {session.testAdminStatus},
     *          {session.sessionNumber},
     *          {session.testAdminType},
     *          {session.enforceTimeLimit},
     *          {session.enforceBreak},
     *          {session.timeZone},
     *          (select distinct test_catalog_id from test_catalog where activation_status = 'AC' and item_set_id = {session.itemSetId}),
     *          {session.preferredForm},
     *          {session.formAssignmentMethod},
     *          {session.showStudentFeedback},
     *          {session.programId},
     *          {session.lexingtonVersion},
     *          {session.isRandomize}
     *      )::
     */
    @JdbcControl.SQL(statement = "insert into  test_admin (  TEST_ADMIN_ID,  CUSTOMER_ID,  TEST_ADMIN_NAME,  PRODUCT_ID,  CREATOR_ORG_NODE_ID,  ACCESS_CODE,  LOCATION,  LOGIN_START_DATE,  LOGIN_END_DATE,  DAILY_LOGIN_START_TIME,  DAILY_LOGIN_END_TIME,  CREATED_BY,  CREATED_DATE_TIME,  ACTIVATION_STATUS,  ITEM_SET_ID,  TEST_ADMIN_STATUS,  SESSION_NUMBER,  TEST_ADMIN_TYPE,  ENFORCE_TIME_LIMIT,  ENFORCE_BREAK,  TIME_ZONE,  TEST_CATALOG_ID,  PREFERRED_FORM,  FORM_ASSIGNMENT_METHOD,  SHOW_STUDENT_FEEDBACK,  PROGRAM_ID, LEXINGTON_VERSION, RANDOM_DISTRACTOR_STATUS  ) values (  {session.testAdminId},  {session.customerId},  {session.testAdminName},  {session.productId},  {session.creatorOrgNodeId},  {session.accessCode},  {session.location},  {session.loginStartDate},  {session.loginEndDate},  {session.dailyLoginStartTime},  {session.dailyLoginEndTime},  {session.createdBy},  {session.createdDateTime},  {session.activationStatus},  {session.itemSetId},  {session.testAdminStatus},  {session.sessionNumber},  {session.testAdminType},  {session.enforceTimeLimit},  {session.enforceBreak},  {session.timeZone},  (select distinct test_catalog_id from test_catalog where activation_status = 'AC' and item_set_id = {session.itemSetId}),  {session.preferredForm},  {session.formAssignmentMethod},  {session.showStudentFeedback},  {session.programId}, {session.lexingtonVersion}, {session.isRandomize}  )")
    void createNewTestAdmin(TestSession session) throws SQLException;
    
    /**
     * @jc:sql statement::
     * update
     *     test_admin set
     *          TEST_ADMIN_NAME = {session.testAdminName},
     *          PRODUCT_ID = {session.productId},
     *          ACCESS_CODE = {session.accessCode},
     *          LOCATION = {session.location},
     *          LOGIN_START_DATE = {session.loginStartDate},
     *          LOGIN_END_DATE = {session.loginEndDate},
     *          DAILY_LOGIN_START_TIME = {session.dailyLoginStartTime},
     *          DAILY_LOGIN_END_TIME = {session.dailyLoginEndTime},
     *          UPDATED_BY = {session.updatedBy},
     *          UPDATED_DATE_TIME = {session.updatedDateTime},
     *          ITEM_SET_ID = {session.itemSetId},
     *          TEST_ADMIN_STATUS = {session.testAdminStatus},
     *          SESSION_NUMBER = {session.sessionNumber},
     *          TEST_ADMIN_TYPE = {session.testAdminType},
     *          ENFORCE_TIME_LIMIT = {session.enforceTimeLimit},
     *          ENFORCE_BREAK = {session.enforceBreak},
     *          TIME_ZONE = {session.timeZone},
     *          TEST_CATALOG_ID = (select distinct test_catalog_id from test_catalog where activation_status = 'AC' and item_set_id = {session.itemSetId}),
     *          PREFERRED_FORM = {session.preferredForm},
     *          FORM_ASSIGNMENT_METHOD = {session.formAssignmentMethod},
     *          SHOW_STUDENT_FEEDBACK = {session.showStudentFeedback},
     *          CREATOR_ORG_NODE_ID = {session.creatorOrgNodeId},
     *          PROGRAM_ID = {session.programId},
     * 		    LEXINGTON_VERSION = {session.lexingtonVersion},
     * 		    RANDOM_DISTRACTOR_STATUS = {session.isRandomize}
     *          
     * where
     *      test_admin_id = {session.testAdminId}::
     */
    @JdbcControl.SQL(statement = "update  test_admin set  TEST_ADMIN_NAME = {session.testAdminName},  PRODUCT_ID = {session.productId},  ACCESS_CODE = {session.accessCode},  LOCATION = {session.location},  LOGIN_START_DATE = {session.loginStartDate},  LOGIN_END_DATE = {session.loginEndDate},  DAILY_LOGIN_START_TIME = {session.dailyLoginStartTime},  DAILY_LOGIN_END_TIME = {session.dailyLoginEndTime},  UPDATED_BY = {session.updatedBy},  UPDATED_DATE_TIME = {session.updatedDateTime},  ITEM_SET_ID = {session.itemSetId},  TEST_ADMIN_STATUS = {session.testAdminStatus},  SESSION_NUMBER = {session.sessionNumber},  TEST_ADMIN_TYPE = {session.testAdminType},  ENFORCE_TIME_LIMIT = {session.enforceTimeLimit},  ENFORCE_BREAK = {session.enforceBreak},  TIME_ZONE = {session.timeZone},  TEST_CATALOG_ID = (select distinct test_catalog_id from test_catalog where activation_status = 'AC' and item_set_id = {session.itemSetId}),  PREFERRED_FORM = {session.preferredForm},  FORM_ASSIGNMENT_METHOD = {session.formAssignmentMethod},  SHOW_STUDENT_FEEDBACK = {session.showStudentFeedback},  CREATOR_ORG_NODE_ID = {session.creatorOrgNodeId},  PROGRAM_ID = {session.programId}, LEXINGTON_VERSION = {session.lexingtonVersion}, \t\t  RANDOM_DISTRACTOR_STATUS = {session.isRandomize}  where  test_admin_id = {session.testAdminId}")
    void updateTestAdmin(TestSession session) throws SQLException;

    
    /**
     * @jc:sql statement::
     * select SEQ_TEST_ADMIN_ID.NEXTVAL from dual
     */
    @JdbcControl.SQL(statement = "select SEQ_TEST_ADMIN_ID.NEXTVAL from dual")
    Integer getNextPK() throws SQLException;
                   
    /**
     * @jc:sql statement::	
     * delete from 
     *      test_admin 
     * where 
     *      test_admin_id = {testAdminId}
     *      and not exists (
     *          select * 
     *          from 
     *              test_roster 
     *          where 
     *              test_admin_id = {testAdminId})
     *      and not exists (
     *          select * 
     *          from 
     *              test_Admin_item_set 
     *          where 
     *              test_admin_id = {testAdminId})::
     */
    @JdbcControl.SQL(statement = "delete from  test_admin  where  test_admin_id = {testAdminId}  and not exists (  select *  from  test_roster  where  test_admin_id = {testAdminId})  and not exists (  select *  from  test_Admin_item_set  where  test_admin_id = {testAdminId})")
    void deleteTestAdmin(Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * select decode(count(*), 0, 'F', 'T') as copyable
     * from
     * 	test_Admin admin,
     * 	users,
     * 	user_role,
     * 	org_node_ancestor ona,
     * 	org_node_test_catalog ontc,
     * 	item_set iset
     * where
     * 	 ontc.activation_status = 'AC'
     * 	 and iset.activation_status = 'AC'
     * 	 and ontc.item_set_id = iset.item_set_id
     * 	 and ontc.item_set_id = admin.item_set_id
     * 	 and ontc.org_node_id = user_role.org_node_id
     * 	 and nvl(ontc.override_no_retake, 'F') != 'T'
     * 	 and user_role.org_node_id = ona.ancestor_org_node_id
     * 	 and ona.org_node_id = admin.creator_org_node_id
     * 	 and user_role.user_id = users.user_id
     * 	 and user_role.role_id not in (select role_id from role where role_name = 'PROCTOR')
     * 	 and user_role.activation_status = 'AC'
     * 	 and users.user_name = {userName}
     * 	 and admin.test_admin_id = {testAdminId}::
     */
    @JdbcControl.SQL(statement = "select decode(count(*), 0, 'F', 'T') as copyable from \ttest_Admin admin, \tusers, \tuser_role, \torg_node_ancestor ona, \torg_node_test_catalog ontc, \titem_set iset where \t ontc.activation_status = 'AC' \t and iset.activation_status = 'AC' \t and ontc.item_set_id = iset.item_set_id \t and ontc.item_set_id = admin.item_set_id \t and ontc.org_node_id = user_role.org_node_id \t and nvl(ontc.override_no_retake, 'F') != 'T' \t and user_role.org_node_id = ona.ancestor_org_node_id \t and ona.org_node_id = admin.creator_org_node_id \t and user_role.user_id = users.user_id \t and user_role.role_id not in (select role_id from role where role_name = 'PROCTOR') \t and user_role.activation_status = 'AC' \t and users.user_name = {userName} \t and admin.test_admin_id = {testAdminId}")
    String checkCopyable(String userName, Integer testAdminId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select
     *     ta.ENFORCE_BREAK as enforceBrake
     * from 
     *      test_admin ta
     * where 
     *      ta.test_admin_id = {testAdminId}::
     */
    @JdbcControl.SQL(statement = "select  ta.ENFORCE_BREAK as enforceBrake from  test_admin ta where  ta.test_admin_id = {testAdminId}")
    String getTestBrakesFlag(Integer testAdminId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select
     *     min(pr.PROGRAM_ID) as programId  
     * from 
     *      program pr, product p
     * where 
     *      pr.CUSTOMER_ID = {customerId}
     *  and pr.PRODUCT_ID  = p.PARENT_PRODUCT_ID
     *  and trunc(pr.program_start_date) <= trunc({startDate}) and trunc(pr.program_end_date) >= trunc({startDate})
     *  and p.product_id = {productId}::
     *  array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  min(pr.PROGRAM_ID) as programId  from  program pr, product p where  pr.CUSTOMER_ID = {customerId}  and pr.PRODUCT_ID  = p.PARENT_PRODUCT_ID  and trunc(pr.program_start_date) <= trunc({startDate}) and trunc(pr.program_end_date) >= trunc({startDate})  and p.product_id = {productId}",
                     arrayMaxLength = 100000)
    Integer [] getProgramIdForCustomerAndProduct(Integer customerId, Integer productId, Date startDate) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select decode(count(admin.test_admin_id), 0, 'false', 'true')
     * 
     *   from test_admin admin
     * 
     *  where admin.test_Admin_id = {testAdminId}::
     */
	@JdbcControl.SQL(statement = "select decode(count(admin.test_admin_id), 0, 'false', 'true')  from test_admin admin  where admin.test_Admin_id = {testAdminId}")
    String isTestAdminExists(Integer testAdminId) throws SQLException;
    
	
	/**
	 * 
	 * For ISTEP CR003
	 */
	
	@JdbcControl.SQL(statement =" select ta.test_admin_id as testAdminId, ta.customer_id as customerId, ta.creator_org_node_id as creatorOrgNodeId, ta.test_admin_name as testAdminName, tr.test_roster_id as testRosterId,  ta.item_set_id as itemSetId,  tc.test_catalog_id as testCatalogId , tc.test_name as testName, ta.access_code as accessCode, ta.created_by as createdBy, ta.session_number as sessionNumber, concat(concat(usr.last_name, ', '), concat(usr.first_name, concat(' ', usr.MIDDLE_NAME))) as scheduler from test_admin ta, test_roster tr, test_catalog tc, users usr, customer_configuration cc where ta.test_admin_id = tr.test_admin_id and tc.test_catalog_id = ta.test_catalog_id and usr.user_id = ta.created_by and ta.activation_status = 'AC' and ta.test_admin_status = 'CU' and ta.customer_id = cc.customer_id and cc.customer_configuration_name = 'Allow_Reopen_Subtest' and cc.default_value = 'T' {sql: searchCriteria}")
	TestSession[] getTestSessionData(String searchCriteria) throws SQLException;
	 
	@JdbcControl.SQL(statement = "SELECT DISTINCT TEST_ADMIN.TEST_ADMIN_ID AS TESTADMINID,                 TEST_ADMIN.CUSTOMER_ID AS CUSTOMERID,                 TEST_ADMIN.TEST_ADMIN_NAME AS TESTADMINNAME,                 TEST_ADMIN.PRODUCT_ID AS PRODUCTID,                 PRODUCT.PARENT_PRODUCT_ID AS PARENTPRODUCTID,                 PRODUCT.LICENSE_ENABLED AS LICENSEENABLED,                 TEST_ADMIN.CREATOR_ORG_NODE_ID AS CREATORORGNODEID,                 NODE.ORG_NODE_NAME AS CREATORORGNODENAME,                 CAT.CATEGORY_NAME AS CREATORORGNODECATEGORYNAME,                 TEST_ADMIN.ACCESS_CODE AS ACCESSCODE,                 TEST_ADMIN.LOCATION AS LOCATION,                 TEST_ADMIN.LOGIN_START_DATE AS LOGINSTARTDATE,                 TEST_ADMIN.LOGIN_END_DATE AS LOGINENDDATE,                 TEST_ADMIN.DAILY_LOGIN_START_TIME AS DAILYLOGINSTARTTIME,                 TEST_ADMIN.DAILY_LOGIN_END_TIME AS DAILYLOGINENDTIME,                 USERS.USER_NAME AS CREATEDBY,                 TEST_ADMIN.CREATED_DATE_TIME AS CREATEDDATETIME,                 TEST_ADMIN.ACTIVATION_STATUS AS ACTIVATIONSTATUS,                 TEST_ADMIN.ITEM_SET_ID AS ITEMSETID,                 TEST_ADMIN.TEST_ADMIN_STATUS AS TESTADMINSTATUS,                 TEST_ADMIN.SESSION_NUMBER AS SESSIONNUMBER,                 TEST_ADMIN.TEST_ADMIN_TYPE AS TESTADMINTYPE,                 TEST_ADMIN.PREFERRED_FORM AS PREFERREDFORM,                 TEST_ADMIN.FORM_ASSIGNMENT_METHOD AS FORMASSIGNMENTMETHOD,                 TEST_ADMIN.TIME_ZONE AS TIMEZONE,                 TEST_ADMIN.SHOW_STUDENT_FEEDBACK AS SHOWSTUDENTFEEDBACK,                 TEST_ADMIN.ENFORCE_TIME_LIMIT AS ENFORCETIMELIMIT,                 TEST_ADMIN.ENFORCE_BREAK AS ENFORCEBREAK,                 TEST.ITEM_SET_NAME AS TESTNAME,                 NVL(REPORT.COMPLETED, 'F') AS REPORTABLE ,                 DECODE( TEST_ADMIN.CREATED_BY, USERS.USER_ID,'Owner', 'Proctor') AS AssignedRole  ,PRODUCT.PRODUCT_TYPE  PRODUCTTYPE  FROM TEST_ADMIN,        USERS,        TEST_ADMIN_USER_ROLE,        ITEM_SET TEST,        ORG_NODE NODE,        ORG_NODE_CATEGORY CAT,        PRODUCT,        (SELECT ROS.TEST_ADMIN_ID,                DECODE(COUNT(DISTINCT ROS.TEST_ROSTER_ID), 0, 'F', 'T') AS                               COMPLETED           FROM TEST_ROSTER ROS, TEST_ADMIN, TEST_ADMIN_USER_ROLE, USERS          WHERE ROS.TEST_ADMIN_ID = TEST_ADMIN.TEST_ADMIN_ID                          AND ROS.TEST_COMPLETION_STATUS = 'CO'            AND ROS.VALIDATION_STATUS = 'VA'            AND                              TEST_ADMIN.ACTIVATION_STATUS = 'AC'            AND TEST_ADMIN_USER_ROLE.USER_ID =                              USERS.USER_ID            AND TEST_ADMIN.TEST_ADMIN_ID = TEST_ADMIN_USER_ROLE.TEST_ADMIN_ID                          AND USERS.USER_NAME = {userName}          GROUP BY ROS.TEST_ADMIN_ID) REPORT  WHERE TEST_ADMIN.ACTIVATION_STATUS = 'AC'    AND NODE.ORG_NODE_ID = TEST_ADMIN.CREATOR_ORG_NODE_ID    AND REPORT.TEST_ADMIN_ID(+) = TEST_ADMIN.TEST_ADMIN_ID    AND TEST.ITEM_SET_ID = TEST_ADMIN.ITEM_SET_ID    AND TEST_ADMIN_USER_ROLE.USER_ID = USERS.USER_ID    AND TEST_ADMIN.TEST_ADMIN_ID = TEST_ADMIN_USER_ROLE.TEST_ADMIN_ID    AND USERS.USER_NAME = {userName}    AND CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID    AND PRODUCT.PRODUCT_ID = TEST_ADMIN.PRODUCT_ID  ORDER BY TEST_ADMIN.LOGIN_END_DATE,           TEST_ADMIN.TEST_ADMIN_NAME,           TEST_ADMIN.LOGIN_START_DATE",
            arrayMaxLength = 100000)
    TestSession [] getTestSessionsForUserHome(String userName) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT DISTINCT TEST_ADMIN.TEST_ADMIN_ID AS TESTADMINID,                 TEST_ADMIN.TEST_ADMIN_NAME AS TESTADMINNAME,                 TEST_ADMIN.PRODUCT_ID AS PRODUCTID,                 PRODUCT.PARENT_PRODUCT_ID AS PARENTPRODUCTID,                 PRODUCT.LICENSE_ENABLED AS LICENSEENABLED,                 TEST_ADMIN.CREATOR_ORG_NODE_ID AS CREATORORGNODEID,                 NODE.ORG_NODE_NAME AS CREATORORGNODENAME,                 CAT.CATEGORY_NAME AS CREATORORGNODECATEGORYNAME,                 TEST_ADMIN.LOGIN_START_DATE AS LOGINSTARTDATE,                 TEST_ADMIN.LOGIN_END_DATE AS LOGINENDDATE,                 TEST_ADMIN.DAILY_LOGIN_START_TIME AS DAILYLOGINSTARTTIME,                 TEST_ADMIN.DAILY_LOGIN_END_TIME AS DAILYLOGINENDTIME,                 USERS.USER_NAME AS CREATEDBY,                 TEST_ADMIN.CREATED_DATE_TIME AS CREATEDDATETIME,                 TEST_ADMIN.ACTIVATION_STATUS AS ACTIVATIONSTATUS,                 TEST_ADMIN.TEST_ADMIN_STATUS AS TESTADMINSTATUS,                                TEST_ADMIN.TEST_ADMIN_TYPE AS TESTADMINTYPE,                 TEST_ADMIN.PREFERRED_FORM AS PREFERREDFORM,                                 TEST_ADMIN.TIME_ZONE AS TIMEZONE,                              TEST.ITEM_SET_NAME AS TESTNAME, DECODE(TEST_ADMIN.CREATED_BY, {userId}, 'Owner', 'Proctor') AS ASSIGNEDROLE, PRODUCT.PRODUCT_TYPE PRODUCTTYPE   FROM TEST_ADMIN,        USERS,        ITEM_SET TEST,        ORG_NODE NODE,        ORG_NODE_CATEGORY CAT,        TEST_ADMIN_USER_ROLE,        org_node_ancestor ONA,        PRODUCT,        (SELECT ROS.TEST_ADMIN_ID,                DECODE(COUNT(DISTINCT ROS.TEST_ROSTER_ID), 0, 'F', 'T') AS COMPLETED           FROM TEST_ROSTER ROS, org_node_ancestor ONA, TEST_ADMIN          WHERE ROS.TEST_ADMIN_ID = TEST_ADMIN.TEST_ADMIN_ID            AND ROS.TEST_COMPLETION_STATUS = 'CO'            AND ROS.VALIDATION_STATUS = 'VA'            AND TEST_ADMIN.ACTIVATION_STATUS = 'AC'            AND ONA.ANCESTOR_ORG_NODE_ID = {orgNodeId}            AND TEST_ADMIN.CREATOR_ORG_NODE_ID = ONA.Org_Node_Id          GROUP BY ROS.TEST_ADMIN_ID) REPORT  WHERE USERS.USER_ID = TEST_ADMIN.CREATED_BY    AND NODE.ORG_NODE_ID = TEST_ADMIN.CREATOR_ORG_NODE_ID    AND TEST.ITEM_SET_ID = TEST_ADMIN.ITEM_SET_ID    AND TEST_ADMIN.TEST_ADMIN_ID = REPORT.TEST_ADMIN_ID(+)    AND TEST_ADMIN.ACTIVATION_STATUS = 'AC'    AND ONA.ANCESTOR_ORG_NODE_ID = {orgNodeId}    AND TEST_ADMIN.CREATOR_ORG_NODE_ID = ONA.Org_Node_Id    AND TEST_ADMIN.TEST_ADMIN_ID = TEST_ADMIN_USER_ROLE.TEST_ADMIN_ID    AND CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID    AND PRODUCT.PRODUCT_ID = TEST_ADMIN.PRODUCT_ID  ORDER BY TEST_ADMIN.LOGIN_END_DATE,           TEST_ADMIN.TEST_ADMIN_NAME,           TEST_ADMIN.LOGIN_START_DATE",
            arrayMaxLength = 100000)
    TestSession [] getTestAdminsForOrgNode(Integer orgNodeId, Integer userId) throws SQLException;
	
	@JdbcControl.SQL(statement = "select  ontc.OVERRIDE_LOGIN_END_DATE as overrideLoginStartDate from  org_node_test_catalog ontc where  ontc.item_set_id = {testItemSetId}  and ontc.org_node_id = {orgNodeId}")
    Date getTestCatalogLoginEndDateOverride(Integer testItemSetId, Integer orgNodeId) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT DISTINCT TEST_ADMIN.TEST_ADMIN_ID AS TESTADMINID, TEST_ADMIN.TEST_ADMIN_NAME AS TESTADMINNAME, NODE.ORG_NODE_NAME AS CREATORORGNODENAME, TEST_ADMIN.LOGIN_START_DATE AS LOGINSTARTDATE, TEST_ADMIN.LOGIN_END_DATE AS LOGINENDDATE, TEST_ADMIN.DAILY_LOGIN_START_TIME AS DAILYLOGINSTARTTIME, TEST_ADMIN.DAILY_LOGIN_END_TIME AS DAILYLOGINENDTIME, TEST_ADMIN.TIME_ZONE AS TIMEZONE, TEST_ADMIN.TEST_ADMIN_STATUS AS TESTADMINSTATUS, TEST.ITEM_SET_NAME AS TESTNAME, DECODE(TEST_ADMIN.CREATED_BY, {userId}, 'Owner', 'Proctor') AS ASSIGNEDROLE, TEST_ADMIN.ACCESS_CODE AS ACCESSCODE FROM TEST_ADMIN, USERS, ITEM_SET TEST, ORG_NODE NODE, ORG_NODE_CATEGORY CAT, TEST_ADMIN_USER_ROLE, ORG_NODE_ANCESTOR ONA, PRODUCT WHERE USERS.USER_ID = TEST_ADMIN.CREATED_BY AND NODE.ORG_NODE_ID = TEST_ADMIN.CREATOR_ORG_NODE_ID AND TEST.ITEM_SET_ID = TEST_ADMIN.ITEM_SET_ID AND TEST_ADMIN.ACTIVATION_STATUS = 'AC' AND ONA.ANCESTOR_ORG_NODE_ID = {orgNodeId} AND TEST_ADMIN.CREATOR_ORG_NODE_ID = ONA.ORG_NODE_ID AND TEST_ADMIN.TEST_ADMIN_ID = TEST_ADMIN_USER_ROLE.TEST_ADMIN_ID AND CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID AND PRODUCT.PRODUCT_ID = TEST_ADMIN.PRODUCT_ID AND TEST_ADMIN.TEST_ADMIN_STATUS IN ('CU','PA') ORDER BY TEST_ADMIN.TEST_ADMIN_NAME",
            arrayMaxLength = 100000)
    TestSession [] getTestAdminsForStudentScoring(Integer userId, Integer orgNodeId) throws SQLException;
   

	
	@JdbcControl.SQL(statement = "SELECT DISTINCT TEST_ADMIN.TEST_ADMIN_ID AS TESTADMINID, TEST_ADMIN.ITEM_SET_ID   AS itemSetId,             TEST_ADMIN.TEST_ADMIN_NAME AS TESTADMINNAME,                 TEST_ADMIN.PRODUCT_ID AS PRODUCTID,                 PRODUCT.PARENT_PRODUCT_ID AS PARENTPRODUCTID,                 PRODUCT.LICENSE_ENABLED AS LICENSEENABLED,                 TEST_ADMIN.CREATOR_ORG_NODE_ID AS CREATORORGNODEID,                 NODE.ORG_NODE_NAME AS CREATORORGNODENAME,                 CAT.CATEGORY_NAME AS CREATORORGNODECATEGORYNAME,                 TEST_ADMIN.LOGIN_START_DATE AS LOGINSTARTDATE,                 TEST_ADMIN.LOGIN_END_DATE AS LOGINENDDATE,                 TEST_ADMIN.DAILY_LOGIN_START_TIME AS DAILYLOGINSTARTTIME,                 TEST_ADMIN.DAILY_LOGIN_END_TIME AS DAILYLOGINENDTIME,                 USERS.USER_NAME AS CREATEDBY,                 TEST_ADMIN.CREATED_DATE_TIME AS CREATEDDATETIME,                 TEST_ADMIN.ACTIVATION_STATUS AS ACTIVATIONSTATUS,                 TEST_ADMIN.TEST_ADMIN_STATUS AS TESTADMINSTATUS,                                TEST_ADMIN.TEST_ADMIN_TYPE AS TESTADMINTYPE,                 TEST_ADMIN.PREFERRED_FORM AS PREFERREDFORM,                                 TEST_ADMIN.TIME_ZONE AS TIMEZONE,                              TEST.ITEM_SET_NAME AS TESTNAME, DECODE(TEST_ADMIN.CREATED_BY, {userId}, 'Owner', 'Proctor') AS ASSIGNEDROLE, PRODUCT.PRODUCT_TYPE PRODUCTTYPE   FROM TEST_ADMIN,        USERS,        ITEM_SET TEST,        ORG_NODE NODE,        ORG_NODE_CATEGORY CAT,        TEST_ADMIN_USER_ROLE,        org_node_ancestor ONA,        PRODUCT,        (SELECT ROS.TEST_ADMIN_ID,                DECODE(COUNT(DISTINCT ROS.TEST_ROSTER_ID), 0, 'F', 'T') AS COMPLETED           FROM TEST_ROSTER ROS, org_node_ancestor ONA, TEST_ADMIN          WHERE ROS.TEST_ADMIN_ID = TEST_ADMIN.TEST_ADMIN_ID            AND ROS.TEST_COMPLETION_STATUS = 'CO'            AND ROS.VALIDATION_STATUS = 'VA'            AND TEST_ADMIN.ACTIVATION_STATUS = 'AC'            AND ONA.ANCESTOR_ORG_NODE_ID = {orgNodeId}            AND TEST_ADMIN.CREATOR_ORG_NODE_ID = ONA.Org_Node_Id          GROUP BY ROS.TEST_ADMIN_ID) REPORT  WHERE USERS.USER_ID = TEST_ADMIN.CREATED_BY    AND NODE.ORG_NODE_ID = TEST_ADMIN.CREATOR_ORG_NODE_ID    AND TEST.ITEM_SET_ID = TEST_ADMIN.ITEM_SET_ID    AND TEST_ADMIN.TEST_ADMIN_ID = REPORT.TEST_ADMIN_ID(+)    AND TEST_ADMIN.ACTIVATION_STATUS = 'AC'    AND ONA.ANCESTOR_ORG_NODE_ID = {orgNodeId}    AND TEST_ADMIN.CREATOR_ORG_NODE_ID = ONA.Org_Node_Id    AND TEST_ADMIN.TEST_ADMIN_ID = TEST_ADMIN_USER_ROLE.TEST_ADMIN_ID    AND CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID    AND PRODUCT.PRODUCT_ID = TEST_ADMIN.PRODUCT_ID   AND TEST_ADMIN.TEST_ADMIN_STATUS IN ('CU', 'FU') ORDER BY TEST_ADMIN.TEST_ADMIN_NAME",
            arrayMaxLength = 100000)
    TestSession [] getCurrentFutureTestAdminsForOrgNode(Integer orgNodeId, Integer userId) throws SQLException;
	
	@JdbcControl.SQL(statement = "select test_admin.TEST_ADMIN_ID as testAdminId,  DECODE(TEST_ADMIN.CREATED_BY, {userId}, 'Owner', 'Proctor') AS ASSIGNEDROLE,  PRODUCT.PRODUCT_TYPE PRODUCTTYPE,     test_admin.CUSTOMER_ID as customerId,        test_admin.TEST_ADMIN_NAME as testAdminName,        test_admin.PRODUCT_ID as productId,        product.parent_product_id as parentProductId,        product.license_enabled as licenseEnabled,        test_admin.CREATOR_ORG_NODE_ID as creatorOrgNodeId,        node.org_node_name as creatorOrgNodeName,        cat.category_name as creatorOrgNodeCategoryName,        test_admin.ACCESS_CODE as accessCode,        test_admin.LOCATION as location,        test_admin.LOGIN_START_DATE as loginStartDate,        test_admin.LOGIN_END_DATE as loginEndDate,        test_admin.DAILY_LOGIN_START_TIME as dailyLoginStartTime,        test_admin.DAILY_LOGIN_END_TIME as dailyLoginEndTime,        users.USER_NAME as createdBy,        test_admin.CREATED_DATE_TIME as createdDateTime,        test_admin.ACTIVATION_STATUS as activationStatus,        test_admin.ITEM_SET_ID as itemSetId,        test_admin.TEST_ADMIN_STATUS as testAdminStatus,        test_admin.SESSION_NUMBER as sessionNumber,        test_admin.TEST_ADMIN_TYPE as testAdminType,        test_admin.PREFERRED_FORM as preferredForm,        test_admin.FORM_ASSIGNMENT_METHOD as formAssignmentMethod,        test_admin.TIME_ZONE as timeZone,        test_admin.SHOW_STUDENT_FEEDBACK as showStudentFeedback,        test_admin.ENFORCE_TIME_LIMIT as enforceTimeLimit,        test_admin.ENFORCE_BREAK as enforceBreak,        test.item_Set_name as testName,        nvl(report.completed, 'F') as reportable  , DECODE(ROSTER.TEST_ADMIN_ID, NULL, 'F', 'T') ISSTUDENTSSESSION from test_admin,        users,        item_set test,        org_node node,        org_node_category cat,        product,     ORG_NODE_ANCESTOR ONA,  (SELECT TEST_ADMIN_ID FROM TEST_ROSTER WHERE STUDENT_ID = {studentId}) ROSTER,  (select ros.test_admin_id,                  decode(count(distinct ros.test_roster_id), 0, 'F', 'T') as completed from  test_roster ros,                  test_admin  where ros.test_admin_id = test_admin.test_admin_id  and ros.test_completion_Status = 'CO'  and ros.validation_status = 'VA' and test_admin.activation_status = 'AC' and test_admin.creator_org_node_id ={orgNodeId} group by ros.test_admin_id) report  where users.user_id = test_admin.created_by    and node.org_node_id = test_Admin.creator_org_node_id    and test.item_set_id = test_admin.item_set_id    and test_Admin.test_admin_id = report.test_admin_id(+)    and test_admin.activation_status = 'AC'  AND ONA.ANCESTOR_ORG_NODE_ID = {orgNodeId}  and test_admin.creator_org_node_id = ONA.ORG_NODE_ID   AND TEST_ADMIN.TEST_ADMIN_STATUS IN ('CU','FU') and cat.org_node_category_id = node.org_node_category_id    and product.product_id = test_admin.PRODUCT_ID    and test_admin.PRODUCT_ID = {selectedProductId} AND TEST_ADMIN.TEST_ADMIN_ID = ROSTER.TEST_ADMIN_ID(+) order by test_admin.test_admin_name",
            arrayMaxLength = 1000000)
    TestSession [] getRecommendedTestSessionsForOrgNodeWithStudentStatus(Integer orgNodeId, Integer userId, Integer selectedProductId, Integer studentId) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT DISTINCT TEST_ADMIN.TEST_ADMIN_ID AS TESTADMINID, TEST_ADMIN.ITEM_SET_ID   AS itemSetId,             TEST_ADMIN.TEST_ADMIN_NAME AS TESTADMINNAME,                 TEST_ADMIN.PRODUCT_ID AS PRODUCTID,                 PRODUCT.PARENT_PRODUCT_ID AS PARENTPRODUCTID,                 PRODUCT.LICENSE_ENABLED AS LICENSEENABLED,                 TEST_ADMIN.CREATOR_ORG_NODE_ID AS CREATORORGNODEID,                 NODE.ORG_NODE_NAME AS CREATORORGNODENAME,                 CAT.CATEGORY_NAME AS CREATORORGNODECATEGORYNAME,                 TEST_ADMIN.LOGIN_START_DATE AS LOGINSTARTDATE,                 TEST_ADMIN.LOGIN_END_DATE AS LOGINENDDATE,                 TEST_ADMIN.DAILY_LOGIN_START_TIME AS DAILYLOGINSTARTTIME,                 TEST_ADMIN.DAILY_LOGIN_END_TIME AS DAILYLOGINENDTIME,                 USERS.USER_NAME AS CREATEDBY,                 TEST_ADMIN.CREATED_DATE_TIME AS CREATEDDATETIME,                 TEST_ADMIN.ACTIVATION_STATUS AS ACTIVATIONSTATUS,                 TEST_ADMIN.TEST_ADMIN_STATUS AS TESTADMINSTATUS,                                TEST_ADMIN.TEST_ADMIN_TYPE AS TESTADMINTYPE,                 TEST_ADMIN.PREFERRED_FORM AS PREFERREDFORM,                                 TEST_ADMIN.TIME_ZONE AS TIMEZONE,                              TEST.ITEM_SET_NAME AS TESTNAME, DECODE(TEST_ADMIN.CREATED_BY, {userId}, 'Owner', 'Proctor') AS ASSIGNEDROLE, PRODUCT.PRODUCT_TYPE PRODUCTTYPE , decode(roster.test_admin_id, null, 'F', 'T') isStudentsSession  FROM TEST_ADMIN,        USERS,        ITEM_SET TEST,        ORG_NODE NODE,        ORG_NODE_CATEGORY CAT,        TEST_ADMIN_USER_ROLE,        org_node_ancestor ONA,        PRODUCT,  (select test_admin_id from Test_Roster where student_id = {studentId} ) roster,      (SELECT ROS.TEST_ADMIN_ID,                DECODE(COUNT(DISTINCT ROS.TEST_ROSTER_ID), 0, 'F', 'T') AS COMPLETED           FROM TEST_ROSTER ROS, org_node_ancestor ONA, TEST_ADMIN          WHERE ROS.TEST_ADMIN_ID = TEST_ADMIN.TEST_ADMIN_ID            AND ROS.TEST_COMPLETION_STATUS = 'CO'            AND ROS.VALIDATION_STATUS = 'VA'            AND TEST_ADMIN.ACTIVATION_STATUS = 'AC'            AND ONA.ANCESTOR_ORG_NODE_ID = {orgNodeId}            AND TEST_ADMIN.CREATOR_ORG_NODE_ID = ONA.Org_Node_Id          GROUP BY ROS.TEST_ADMIN_ID) REPORT  WHERE USERS.USER_ID = TEST_ADMIN.CREATED_BY    AND NODE.ORG_NODE_ID = TEST_ADMIN.CREATOR_ORG_NODE_ID    AND TEST.ITEM_SET_ID = TEST_ADMIN.ITEM_SET_ID    AND TEST_ADMIN.TEST_ADMIN_ID = REPORT.TEST_ADMIN_ID(+)    AND TEST_ADMIN.ACTIVATION_STATUS = 'AC'    AND ONA.ANCESTOR_ORG_NODE_ID = {orgNodeId}    AND TEST_ADMIN.CREATOR_ORG_NODE_ID = ONA.Org_Node_Id    AND TEST_ADMIN.TEST_ADMIN_ID = TEST_ADMIN_USER_ROLE.TEST_ADMIN_ID    AND CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID    AND PRODUCT.PRODUCT_ID = TEST_ADMIN.PRODUCT_ID   AND TEST_ADMIN.TEST_ADMIN_STATUS IN ('CU', 'FU')  and test_admin.test_admin_id = roster.test_admin_id(+) ORDER BY TEST_ADMIN.TEST_ADMIN_NAME",
            arrayMaxLength = 100000)
    TestSession [] getCurrentFutureTestAdminsForOrgNodeWithStudentStatus(Integer orgNodeId, Integer userId,	Integer studentId) throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT CAT.TEST_LEVEL AS TESTLEVEL  FROM TEST_CATALOG CAT , TEST_ADMIN ADMIN WHERE ADMIN.TEST_ADMIN_ID = {testAdminId}   AND ADMIN.TEST_CATALOG_ID = CAT.TEST_CATALOG_ID",
            arrayMaxLength = 1)
	String getTestCataLogLevelByTestAdmin(Integer testAdminId)throws SQLException;
    
	@JdbcControl.SQL(statement = " SELECT LICENSEQUANTITY, CUSTOMERID, PRODUCTID, ORDERINDEX, ORDERNUMBER,  EXPIRYDATE, PURCHASEDATE  FROM ( SELECT SUM(OOL.AVAILABLE) AS LICENSEQUANTITY, CPL.CUSTOMER_ID AS CUSTOMERID, CPL.PRODUCT_ID AS PRODUCTID,  CPL.ORDER_INDEX AS ORDERINDEX,  CPL.ORDER_NUMBER AS ORDERNUMBER, TO_CHAR(CPL.LICENSE_PERIOD_END, 'mm/dd/yyyy') AS EXPIRYDATE, TO_CHAR(CPL.LICENSE_PERIOD_START, 'mm/dd/yyyy') AS PURCHASEDATE,  CPL.PO_TEXT AS PURCHASEORDER  FROM CUSTOMER_PRODUCT_LICENSE CPL, ORGNODE_ORDER_LICENSE    OOL, USERS  U, USER_ROLE   UR, ORG_NODE   ORG  WHERE U.USER_ID = UR.USER_ID  AND UR.ORG_NODE_ID = ORG.ORG_NODE_ID  AND ORG.CUSTOMER_ID = CPL.CUSTOMER_ID  AND CPL.ORDER_INDEX = OOL.ORDER_INDEX  AND U.USER_NAME = {userName} AND U.ACTIVATION_STATUS = 'AC' AND ORG.ACTIVATION_STATUS = 'AC' GROUP BY CPL.CUSTOMER_ID, CPL.PRODUCT_ID, CPL.ORDER_INDEX, CPL.ORDER_NUMBER, CPL.LICENSE_PERIOD_END, CPL.LICENSE_PERIOD_START, CPL.PO_TEXT ORDER BY CPL.LICENSE_PERIOD_END ASC, CPL.LICENSE_PERIOD_START ASC, SUM(OOL.AVAILABLE) ASC ) DERIVED WHERE DERIVED.LICENSEQUANTITY > 0  AND ROWNUM = 1", 
			arrayMaxLength = 1)
	LASLicenseNode getOldestNonZeroActivePOForCustomer(String userName)throws SQLException;
	
	@JdbcControl.SQL(statement = " SELECT LICENSEQUANTITY, CUSTOMERID, PRODUCTID, ORDERINDEX, ORDERNUMBER, EXPIRYDATE, PURCHASEDATE FROM (SELECT SUM(OOL.AVAILABLE) AS LICENSEQUANTITY, CPL.CUSTOMER_ID AS CUSTOMERID, CPL.PRODUCT_ID AS PRODUCTID, CPL.ORDER_INDEX AS ORDERINDEX, CPL.ORDER_NUMBER AS ORDERNUMBER, TO_CHAR(CPL.LICENSE_PERIOD_END, 'mm/dd/yyyy') AS EXPIRYDATE, TO_CHAR(CPL.LICENSE_PERIOD_START, 'mm/dd/yyyy') AS PURCHASEDATE, CPL.PO_TEXT AS PURCHASEORDER FROM CUSTOMER_PRODUCT_LICENSE CPL, ORGNODE_ORDER_LICENSE OOL WHERE CPL.CUSTOMER_ID = {customerId} AND CPL.ORDER_INDEX = OOL.ORDER_INDEX AND {sql: OrgIds} GROUP BY CPL.CUSTOMER_ID, CPL.PRODUCT_ID, CPL.ORDER_INDEX, CPL.ORDER_NUMBER, CPL.LICENSE_PERIOD_END, CPL.LICENSE_PERIOD_START, CPL.PO_TEXT ORDER BY CPL.LICENSE_PERIOD_END ASC, CPL.LICENSE_PERIOD_START ASC, SUM(OOL.AVAILABLE) ASC) DERIVED WHERE DERIVED.LICENSEQUANTITY > 0 AND ROWNUM = 1", 
			arrayMaxLength = 1)
	LASLicenseNode getOldestNonZeroActivePOForOrgIds(Integer customerId, String OrgIds)throws SQLException;

	@JdbcControl.SQL(statement = " SELECT LICENSEQUANTITY, CUSTOMERID, PRODUCTID, ORDERINDEX, ORDERNUMBER, EXPIRYDATE, PURCHASEDATE FROM (SELECT SUM(OOL.AVAILABLE) AS LICENSEQUANTITY, CPL.CUSTOMER_ID AS CUSTOMERID, CPL.PRODUCT_ID AS PRODUCTID, CPL.ORDER_INDEX AS ORDERINDEX,CPL.ORDER_NUMBER AS ORDERNUMBER, TO_CHAR(CPL.LICENSE_PERIOD_END, 'mm/dd/yyyy') AS EXPIRYDATE, TO_CHAR(CPL.LICENSE_PERIOD_START, 'mm/dd/yyyy') AS PURCHASEDATE, CPL.PO_TEXT AS PURCHASEORDER FROM CUSTOMER_PRODUCT_LICENSE CPL, ORGNODE_ORDER_LICENSE OOL, TEST_ROSTER TR WHERE CPL.CUSTOMER_ID = TR.CUSTOMER_ID AND CPL.ORDER_INDEX = OOL.ORDER_INDEX  AND OOL.ORG_NODE_ID = TR.ORG_NODE_ID AND TR.TEST_ADMIN_ID= {testAdminId} GROUP BY CPL.CUSTOMER_ID, CPL.PRODUCT_ID, CPL.ORDER_INDEX, CPL.ORDER_NUMBER, CPL.LICENSE_PERIOD_END, CPL.LICENSE_PERIOD_START, CPL.PO_TEXT ORDER BY CPL.LICENSE_PERIOD_END ASC, CPL.LICENSE_PERIOD_START ASC, SUM(OOL.AVAILABLE) ASC) DERIVED WHERE DERIVED.LICENSEQUANTITY > 0 AND ROWNUM = 1 ", 
			arrayMaxLength = 1)
	LASLicenseNode getOldestNonZeroActivePOForTestSelected(Integer testAdminId)throws SQLException;
	
	@JdbcControl.SQL(statement = "SELECT SUM(COL.AVAILABLE) AS LICENSEQUANTITY , COL.SUBTEST_MODEL as subtestModel FROM CUSTOMER_ORGNODE_LICENSE COL WHERE COL.CUSTOMER_ID = {customerId} AND {sql: OrgIds} GROUP BY COL.SUBTEST_MODEL ", 
			arrayMaxLength = 10)
	LASLicenseNode getLicenseInfo(Integer customerId,String OrgIds)throws SQLException;
	
	/**
     * @jc:sql statement::
     * select
     *     pr.PROGRAM_ID as programId  
     * from 
     *      program pr
     * where 
     *      pr.CUSTOMER_ID = {customerId}
     *  and activation_status='AC'
     *  and trunc(pr.program_end_date) >= trunc({startDate})::
     *  array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  pr.PROGRAM_ID as programId  from  program pr where  pr.CUSTOMER_ID = {customerId} and activation_status='AC' and trunc(pr.program_end_date) >= trunc({startDate}) ",
                     arrayMaxLength = 100000)
    Integer [] getActiveProgramIdForCustomer(Integer customerId, Date startDate) throws SQLException;

	/**
     * @jc:sql statement::
     * select
     *     customer_id as customerId,product_id as productId, program_id as programId,program_name as programName,program_start_date as programStartDate,program_end_date as programEndDate, norms_group as normsGroup, norms_year as normsYear, created_date_time as createdDateTime, updated_date_time as updatedDateTime 
     * from 
     *      program pr
     * where 
     *      pr.CUSTOMER_ID = {customerId}
     *  and activation_status='AC'
     *  and trunc(pr.program_end_date) >= trunc({startDate})::
     *  array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select customer_id as customerId,product_id as productId, program_id as programId,program_name as programName,program_start_date as programStartDate,program_end_date as programEndDate, norms_group as normsGroup, norms_year as normsYear, created_date_time as createdDateTime, updated_date_time as updatedDateTime from program pr where  pr.CUSTOMER_ID = {customerId} and (activation_status='IN' or trunc(pr.program_end_date) < trunc({startDate})) ",
                     arrayMaxLength = 100000)
    Program [] getExpiredProgramsForCustomer(Integer customerId, Date startDate) throws SQLException;
    
    
    
    /**
     * @jc:sql statement::
     * select
     *     TAIS.ACCESS_CODE AS accessCode, ISET.ITEM_SET_NAME AS subtestName 
     * from 
     *      TEST_ADMIN_ITEM_SET TAIS, ITEM_SET ISET
     * where 
     *      TAIS.TEST_ADMIN_ID = {sessionId}
     *  AND TAIS.ITEM_SET_ID = ISET.ITEM_SET_ID
     *  ORDER BY TAIS.ITEM_SET_ORDER::
     *  array-max-length="all"
     */
    @JdbcControl.SQL(statement = " SELECT TAIS.ACCESS_CODE AS accessCode, ISET.ITEM_SET_NAME AS subtestName  FROM TEST_ADMIN_ITEM_SET TAIS, ITEM_SET ISET WHERE TAIS.TEST_ADMIN_ID = {sessionId} AND TAIS.ITEM_SET_ID = ISET.ITEM_SET_ID ORDER BY TAIS.ITEM_SET_ORDER ",
                     arrayMaxLength = 100)
    SubtestAccessCodeDetail [] getSessionAccessCodeDetails(Integer sessionId) throws SQLException;
  

}