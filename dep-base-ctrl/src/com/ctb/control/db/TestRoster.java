package com.ctb.control.db; 

import java.sql.SQLException;
import java.util.List;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.testAdmin.ProgramStatusSession;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.StudentSessionStatus;

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
public interface TestRoster extends JdbcControl
{ 
    
    /**
     * @jc:sql statement::
     * select 
     *      decode(max(count), 0, 'false', 'true') as visible from
     * (select count(ros.test_roster_id) as count
     *      from
     *       	test_admin_user_role taur,
     *       	test_roster ros,
     *       	users
     *       where
     *       	 users.user_name = {userName}
     *       	 and taur.user_id = users.user_id
     *       	 and ros.test_admin_id = taur.test_admin_id
     *       	 and ros.test_roster_id = {testRosterId}
     * union
     * select count(ros.test_roster_id) as count
     *       from
     *       	users,
     * 		user_role urole,
     * 		org_node_ancestor ona,
     * 		test_admin admin,
     * 		test_roster ros
     *       where
     *       	 users.user_name = {userName}
     * 		 and users.user_id = urole.user_id
     * 		 and urole.org_node_id = ona.ancestor_org_node_id
     *       	 and ona.org_node_id = admin.creator_org_node_id
     *       	 and ros.test_admin_id = admin.test_admin_id
     *       	 and ros.test_roster_id = {testRosterId})::
     */
    @JdbcControl.SQL(statement = "select  decode(max(count), 0, 'false', 'true') as visible from (select count(ros.test_roster_id) as count  from  \ttest_admin_user_role taur,  \ttest_roster ros,  \tusers  where  \t users.user_name = {userName}  \t and taur.user_id = users.user_id  \t and ros.test_admin_id = taur.test_admin_id  \t and ros.test_roster_id = {testRosterId} union select count(ros.test_roster_id) as count  from  \tusers, \t\tuser_role urole, \t\torg_node_ancestor ona, \t\ttest_admin admin, \t\ttest_roster ros  where  \t users.user_name = {userName} \t\t and users.user_id = urole.user_id \t\t and urole.org_node_id = ona.ancestor_org_node_id  \t and ona.org_node_id = admin.creator_org_node_id  \t and ros.test_admin_id = admin.test_admin_id  \t and ros.test_roster_id = {testRosterId})")
    String checkVisibility(String userName, Integer testRosterId) throws SQLException;

    /** 
     * @jc:sql statement::
     * insert into
     *      test_roster (
     *          TEST_ROSTER_ID,
     *          TEST_ADMIN_ID,
     *          CREATED_DATE_TIME,
     *          START_DATE_TIME,
     *          COMPLETION_DATE_TIME,
     *          TEST_COMPLETION_STATUS,
     *          VALIDATION_STATUS,
     *          VALIDATION_UPDATED_BY,
     *          VALIDATION_UPDATED_DATE_TIME,
     *          VALIDATION_UPDATED_NOTE,
     *          OVERRIDE_TEST_WINDOW,
     *          PASSWORD,
     *          STUDENT_ID,
     *          CREATED_BY,
     *          UPDATED_BY,
     *          ACTIVATION_STATUS,
     *          UPDATED_DATE_TIME,
     *          CUSTOMER_ID,
     *          TUTORIAL_TAKEN_DATE_TIME,
     *          CAPTURE_METHOD,
     *          SCORING_STATUS,
     *          ORG_NODE_ID,
     *          FORM_ASSIGNMENT,
     *          CUSTOMER_FLAG_STATUS,
     *          EXTENDED_TIME
     *      ) values (
     *          SEQ_TEST_ROSTER_ID.NEXTVAL,
     *          {roster.testAdminId},
     *          {roster.createdDateTime},
     *          {roster.startDateTime},
     *          {roster.completionDateTime},
     *          {roster.testCompletionStatus},
     *          {roster.validationStatus},
     *          {roster.validationUpdatedBy},
     *          {roster.validationUpdatedDateTime},
     *          {roster.validationUpdatedNote},
     *          {roster.overrideTestWindow},
     *          {roster.password},
     *          {roster.studentId},
     *          {roster.createdBy},
     *          {roster.updatedBy},
     *          {roster.activationStatus},
     *          {roster.updatedDateTime},
     *          {roster.customerId},
     *          {roster.tutorialTakenDateTime},
     *          {roster.captureMethod},
     *          {roster.scoringStatus},
     *          {roster.orgNodeId},
     *          {roster.formAssignment},
     *          {roster.customerFlagStatus},
     *          {roster.extendedTime}
     *         
     *      )::
    */ 
    @JdbcControl.SQL(statement = "insert into  test_roster (  TEST_ROSTER_ID,  TEST_ADMIN_ID,  CREATED_DATE_TIME,  START_DATE_TIME,  COMPLETION_DATE_TIME,  TEST_COMPLETION_STATUS,  VALIDATION_STATUS,  VALIDATION_UPDATED_BY,  VALIDATION_UPDATED_DATE_TIME,  VALIDATION_UPDATED_NOTE,  OVERRIDE_TEST_WINDOW,  PASSWORD,  STUDENT_ID,  CREATED_BY,  UPDATED_BY,  ACTIVATION_STATUS,  UPDATED_DATE_TIME,  CUSTOMER_ID,  TUTORIAL_TAKEN_DATE_TIME,  CAPTURE_METHOD,  SCORING_STATUS,  ORG_NODE_ID,  FORM_ASSIGNMENT,  CUSTOMER_FLAG_STATUS, EXTENDED_TIME  ) values (  SEQ_TEST_ROSTER_ID.NEXTVAL,  {roster.testAdminId},  {roster.createdDateTime},  {roster.startDateTime},  {roster.completionDateTime},  {roster.testCompletionStatus},  {roster.validationStatus},  {roster.validationUpdatedBy},  {roster.validationUpdatedDateTime},  {roster.validationUpdatedNote},  {roster.overrideTestWindow},  {roster.password},  {roster.studentId},  {roster.createdBy},  {roster.updatedBy},  {roster.activationStatus},  {roster.updatedDateTime},  {roster.customerId},  {roster.tutorialTakenDateTime},  {roster.captureMethod},  {roster.scoringStatus},  {roster.orgNodeId},  {roster.formAssignment},  {roster.customerFlagStatus}, {roster.extendedTime}  )")
    void createNewTestRoster(RosterElement roster) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select SEQ_TEST_ROSTER_ID.NEXTVAL from dual
     */
    @JdbcControl.SQL(statement = "select SEQ_TEST_ROSTER_ID.NEXTVAL from dual")
    Integer getNextPK() throws SQLException;
    
    /**
     * @jc:sql statement::
     * update
     *      test_roster
     * set
     *      validation_status = 
     *          decode(
     *              (select 
     *                  validation_status 
     *               from 
     *                  test_roster 
     *               where 
     *                  test_roster_id = {testRosterId}
     *               ),'VA','IN','VA')
     * where
     *      test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "update  test_roster set  validation_status =  decode(  (select  validation_status  from  test_roster  where  test_roster_id = {testRosterId}  ),'VA','IN','VA') where  test_roster_id = {testRosterId}")
    void toggleRosterValidationStatus(Integer testRosterId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select count(*) from test_roster where upper(password) = upper({password})::
     */
    @JdbcControl.SQL(statement = "select count(*) from test_roster where upper(password) = upper({password})")
    Integer getRosterCountForPassword(String password) throws SQLException;


    /**
     * @jc:sql statement::
     * select count(*) 
     * from test_roster
     * where student_id = {studentId}::
     */
    @JdbcControl.SQL(statement = "select count(*)  from test_roster where student_id = {studentId}")
    Integer getRosterCountForStudent(Integer studentId) throws SQLException;

    /**
     * @jc:sql statement::
     * select count(*) 
     * from test_roster
     * where student_id = {studentId}
     * and org_node_id = {orgNodeId}::
     */
    @JdbcControl.SQL(statement = "select count(*)  from test_roster where student_id = {studentId} and org_node_id = {orgNodeId}")
    Integer getRosterCountForStudentAndOrgNode(Integer studentId, Integer orgNodeId) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *      ros.TEST_ROSTER_ID as testRosterId,
     *      ros.TEST_ADMIN_ID as testAdminId,
     *      ros.CREATED_DATE_TIME as createdDateTime,
     *      ros.START_DATE_TIME as startDateTime,
     *      ros.COMPLETION_DATE_TIME as completionDateTime,
     *      ros.TEST_COMPLETION_STATUS as testCompletionStatus,
     *      ros.VALIDATION_STATUS as validationStatus,
     *      ros.VALIDATION_UPDATED_BY as validationUpdatedBy,
     *      ros.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,
     *      ros.VALIDATION_UPDATED_NOTE as validationUpdatedNote,
     *      ros.OVERRIDE_TEST_WINDOW as overrideTestWindow,
     *      ros.PASSWORD as password,
     *      ros.STUDENT_ID as studentId,
     *      ros.CREATED_BY as createdBy,
     *      ros.UPDATED_BY as updatedBy,
     *      ros.ACTIVATION_STATUS as activationStatus,
     *      ros.UPDATED_DATE_TIME as updatedDateTime,
     *      ros.CUSTOMER_ID as customerId,
     *      ros.TUTORIAL_TAKEN_DATE_TIME as tutorialTakenDateTime,
     *      ros.CAPTURE_METHOD as captureMethod,
     *      ros.SCORING_STATUS as scoringStatus,
     *      ros.ORG_NODE_ID as orgNodeId,
     *      ros.FORM_ASSIGNMENT as formAssignment,
     *      decode(ros.CUSTOMER_FLAG_STATUS , null, (select cc.default_value 
     *                                        from customer_configuration cc 
     *                                        where cc.customer_id = stu.customer_id 
     *                                        and cc.customer_configuration_name = 'Roster_Status_Flag')
     *                                       , ros.CUSTOMER_FLAG_STATUS
     *                                      ) as customerFlagStatus,
     *      stu.first_name as firstName,
     *      stu.middle_name as middleName,
     *      stu.last_name as lastName,
     *      stu.ext_pin1 as extPin1,
     *      stu.user_name as userName,
     *      ros.dns_status as dnsStatus
     * from
     *      test_roster ros, student stu
     * where
     *      ros.test_admin_id = {testAdminId}
     *      and stu.student_id = ros.student_id
     *      and (stu.activation_Status = 'AC' or decode(decode(ros.test_completion_status, 'NT', 'SC', ros.test_completion_status), 'SC', 'F', 'T') = 'T')::
     *      array-max-length="all"
    */ 
    @JdbcControl.SQL(statement = "select  ros.TEST_ROSTER_ID as testRosterId,  ros.TEST_ADMIN_ID as testAdminId,  ros.CREATED_DATE_TIME as createdDateTime,  ros.START_DATE_TIME as startDateTime,  ros.COMPLETION_DATE_TIME as completionDateTime,  ros.TEST_COMPLETION_STATUS as testCompletionStatus,  ros.VALIDATION_STATUS as validationStatus,  ros.VALIDATION_UPDATED_BY as validationUpdatedBy,  ros.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,  ros.VALIDATION_UPDATED_NOTE as validationUpdatedNote,  ros.OVERRIDE_TEST_WINDOW as overrideTestWindow,  ros.PASSWORD as password,  ros.STUDENT_ID as studentId,  ros.CREATED_BY as createdBy,  ros.UPDATED_BY as updatedBy,  ros.ACTIVATION_STATUS as activationStatus,  ros.UPDATED_DATE_TIME as updatedDateTime,  ros.CUSTOMER_ID as customerId,  ros.TUTORIAL_TAKEN_DATE_TIME as tutorialTakenDateTime,  ros.CAPTURE_METHOD as captureMethod,  ros.SCORING_STATUS as scoringStatus,  ros.ORG_NODE_ID as orgNodeId,  ros.FORM_ASSIGNMENT as formAssignment,  decode(ros.CUSTOMER_FLAG_STATUS , null, (select cc.default_value  from customer_configuration cc  where cc.customer_id = stu.customer_id  and cc.customer_configuration_name = 'Roster_Status_Flag')  , ros.CUSTOMER_FLAG_STATUS  ) as customerFlagStatus,  stu.first_name as firstName,  stu.middle_name as middleName,  stu.last_name as lastName,  stu.ext_pin1 as extPin1,  stu.user_name as userName, ros.dns_status as dnsStatus from  test_roster ros, student stu where  ros.test_admin_id = {testAdminId}  and stu.student_id = ros.student_id  and (stu.activation_Status = 'AC' or decode(decode(ros.test_completion_status, 'NT', 'SC', ros.test_completion_status), 'SC', 'F', 'T') = 'T')",
                     arrayMaxLength = 100000)
    RosterElement [] getRosterForTestSession(Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *      ros.TEST_ROSTER_ID as testRosterId,
     *      ros.TEST_ADMIN_ID as testAdminId,
     *      ros.CREATED_DATE_TIME as createdDateTime,
     *      ros.START_DATE_TIME as startDateTime,
     *      ros.COMPLETION_DATE_TIME as completionDateTime,
     *      ros.TEST_COMPLETION_STATUS as testCompletionStatus,
     *      ros.VALIDATION_STATUS as validationStatus,
     *      ros.VALIDATION_UPDATED_BY as validationUpdatedBy,
     *      ros.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,
     *      ros.VALIDATION_UPDATED_NOTE as validationUpdatedNote,
     *      ros.OVERRIDE_TEST_WINDOW as overrideTestWindow,
     *      ros.PASSWORD as password,
     *      ros.STUDENT_ID as studentId,
     *      ros.CREATED_BY as createdBy,
     *      ros.UPDATED_BY as updatedBy,
     *      ros.ACTIVATION_STATUS as activationStatus,
     *      ros.UPDATED_DATE_TIME as updatedDateTime,
     *      ros.CUSTOMER_ID as customerId,
     *      ros.TUTORIAL_TAKEN_DATE_TIME as tutorialTakenDateTime,
     *      ros.CAPTURE_METHOD as captureMethod,
     *      ros.SCORING_STATUS as scoringStatus,
     *      ros.ORG_NODE_ID as orgNodeId,
     *      ros.FORM_ASSIGNMENT as formAssignment,
     *      stu.first_name as firstName,
     *      stu.middle_name as middleName,
     *      stu.last_name as lastName,
     *      stu.ext_pin1 as extPin1,
     *      stu.user_name as userName
     * from
     *      test_roster ros, student stu
     * where
     *      ros.test_admin_id = {testAdminId}
     *      and stu.student_id = ros.student_id
     *      and stu.activation_Status = 'AC'
     *      and ros.test_completion_status in ('CO', 'IN', 'IS', 'IC', 'SP')::
     *      array-max-length="all"
    */ 
    @JdbcControl.SQL(statement = "select  ros.TEST_ROSTER_ID as testRosterId,  ros.TEST_ADMIN_ID as testAdminId,  ros.CREATED_DATE_TIME as createdDateTime,  ros.START_DATE_TIME as startDateTime,  ros.COMPLETION_DATE_TIME as completionDateTime,  ros.TEST_COMPLETION_STATUS as testCompletionStatus,  ros.VALIDATION_STATUS as validationStatus,  ros.VALIDATION_UPDATED_BY as validationUpdatedBy,  ros.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,  ros.VALIDATION_UPDATED_NOTE as validationUpdatedNote,  ros.OVERRIDE_TEST_WINDOW as overrideTestWindow,  ros.PASSWORD as password,  ros.STUDENT_ID as studentId,  ros.CREATED_BY as createdBy,  ros.UPDATED_BY as updatedBy,  ros.ACTIVATION_STATUS as activationStatus,  ros.UPDATED_DATE_TIME as updatedDateTime,  ros.CUSTOMER_ID as customerId,  ros.TUTORIAL_TAKEN_DATE_TIME as tutorialTakenDateTime,  ros.CAPTURE_METHOD as captureMethod,  ros.SCORING_STATUS as scoringStatus,  ros.ORG_NODE_ID as orgNodeId,  ros.FORM_ASSIGNMENT as formAssignment,  decode(ros.CUSTOMER_FLAG_STATUS , null, (select cc.default_value  from customer_configuration cc  where cc.customer_id = stu.customer_id  and cc.customer_configuration_name = 'Roster_Status_Flag')  , ros.CUSTOMER_FLAG_STATUS  ) as customerFlagStatus,  stu.first_name as firstName,  stu.middle_name as middleName,  stu.last_name as lastName,  stu.ext_pin1 as extPin1,  stu.user_name as userName from  test_roster ros, student stu where  ros.test_admin_id = {testAdminId}  and stu.student_id = ros.student_id  and stu.activation_Status = 'AC' and ros.test_completion_status in ('CO', 'IN', 'IS', 'IC', 'SP')",
                     arrayMaxLength = 100000)
    RosterElement [] getReportableRosterForTestSession(Integer testAdminId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select
     *      ros.TEST_ROSTER_ID as testRosterId,
     *      ros.TEST_ADMIN_ID as testAdminId,
     *      ros.CREATED_DATE_TIME as createdDateTime,
     *      ros.START_DATE_TIME as startDateTime,
     *      ros.COMPLETION_DATE_TIME as completionDateTime,
     *      ros.TEST_COMPLETION_STATUS as testCompletionStatus,
     *      ros.VALIDATION_STATUS as validationStatus,
     *      ros.VALIDATION_UPDATED_BY as validationUpdatedBy,
     *      ros.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,
     *      ros.VALIDATION_UPDATED_NOTE as validationUpdatedNote,
     *      ros.OVERRIDE_TEST_WINDOW as overrideTestWindow,
     *      ros.PASSWORD as password,
     *      ros.STUDENT_ID as studentId,
     *      ros.CREATED_BY as createdBy,
     *      ros.UPDATED_BY as updatedBy,
     *      ros.ACTIVATION_STATUS as activationStatus,
     *      ros.UPDATED_DATE_TIME as updatedDateTime,
     *      ros.CUSTOMER_ID as customerId,
     *      ros.TUTORIAL_TAKEN_DATE_TIME as tutorialTakenDateTime,
     *      ros.CAPTURE_METHOD as captureMethod,
     *      ros.SCORING_STATUS as scoringStatus,
     *      ros.ORG_NODE_ID as orgNodeId,
     *      ros.FORM_ASSIGNMENT as formAssignment,
     *      stu.first_name as firstName,
     *      stu.middle_name as middleName,
     *      stu.last_name as lastName,
     *      stu.ext_pin1 as extPin1,
     *      stu.user_name as userName,
     *      ros.CUSTOMER_FLAG_STATUS as customerFlagStatus
     * from
     *      test_roster ros, student stu
     * where
     *      ros.test_admin_id = {testAdminId}
     *      and ros.student_Id = {studentId}
     *      and stu.student_id = ros.student_id::
    */ 
    @JdbcControl.SQL(statement = "select  ros.TEST_ROSTER_ID as testRosterId,  ros.TEST_ADMIN_ID as testAdminId,  ros.CREATED_DATE_TIME as createdDateTime,  ros.START_DATE_TIME as startDateTime,  ros.COMPLETION_DATE_TIME as completionDateTime,  ros.TEST_COMPLETION_STATUS as testCompletionStatus,  ros.VALIDATION_STATUS as validationStatus,  ros.VALIDATION_UPDATED_BY as validationUpdatedBy,  ros.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,  ros.VALIDATION_UPDATED_NOTE as validationUpdatedNote,  ros.OVERRIDE_TEST_WINDOW as overrideTestWindow,  ros.PASSWORD as password,  ros.STUDENT_ID as studentId,  ros.CREATED_BY as createdBy,  ros.UPDATED_BY as updatedBy,  ros.ACTIVATION_STATUS as activationStatus,  ros.UPDATED_DATE_TIME as updatedDateTime,  ros.CUSTOMER_ID as customerId,  ros.TUTORIAL_TAKEN_DATE_TIME as tutorialTakenDateTime,  ros.CAPTURE_METHOD as captureMethod,  ros.SCORING_STATUS as scoringStatus,  ros.ORG_NODE_ID as orgNodeId,  ros.FORM_ASSIGNMENT as formAssignment,  stu.first_name as firstName,  stu.middle_name as middleName,  stu.last_name as lastName,  stu.ext_pin1 as extPin1,  stu.user_name as userName,  ros.CUSTOMER_FLAG_STATUS as customerFlagStatus from  test_roster ros, student stu where  ros.test_admin_id = {testAdminId}  and ros.student_Id = {studentId}  and stu.student_id = ros.student_id")
    RosterElement getRosterElementForStudentAndAdmin(Integer studentId, Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *      ros.TEST_ROSTER_ID as testRosterId,
     *      ros.TEST_ADMIN_ID as testAdminId,
     *      ros.CREATED_DATE_TIME as createdDateTime,
     *      ros.START_DATE_TIME as startDateTime,
     *      ros.COMPLETION_DATE_TIME as completionDateTime,
     *      ros.TEST_COMPLETION_STATUS as testCompletionStatus,
     *      ros.VALIDATION_STATUS as validationStatus,
     *      ros.VALIDATION_UPDATED_BY as validationUpdatedBy,
     *      ros.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,
     *      ros.VALIDATION_UPDATED_NOTE as validationUpdatedNote,
     *      ros.OVERRIDE_TEST_WINDOW as overrideTestWindow,
     *      ros.PASSWORD as password,
     *      ros.STUDENT_ID as studentId,
     *      ros.CREATED_BY as createdBy,
     *      ros.UPDATED_BY as updatedBy,
     *      ros.ACTIVATION_STATUS as activationStatus,
     *      ros.UPDATED_DATE_TIME as updatedDateTime,
     *      ros.CUSTOMER_ID as customerId,
     *      ros.TUTORIAL_TAKEN_DATE_TIME as tutorialTakenDateTime,
     *      ros.CAPTURE_METHOD as captureMethod,
     *      ros.SCORING_STATUS as scoringStatus,
     *      ros.ORG_NODE_ID as orgNodeId,
     *      ros.FORM_ASSIGNMENT as formAssignment,
     *      stu.first_name as firstName,
     *      stu.middle_name as middleName,
     *      stu.last_name as lastName,
     *      stu.ext_pin1 as extPin1,
     *      stu.user_name as userName,
     *      ros.CUSTOMER_FLAG_STATUS as customerFlagStatus
     * from
     *      test_roster ros, student stu
     * where
     *      ros.student_Id = {studentId}
     *      and stu.student_id = ros.student_id::
    */ 
    @JdbcControl.SQL(statement = "select  ros.TEST_ROSTER_ID as testRosterId,  ros.TEST_ADMIN_ID as testAdminId,  ros.CREATED_DATE_TIME as createdDateTime,  ros.START_DATE_TIME as startDateTime,  ros.COMPLETION_DATE_TIME as completionDateTime,  ros.TEST_COMPLETION_STATUS as testCompletionStatus,  ros.VALIDATION_STATUS as validationStatus,  ros.VALIDATION_UPDATED_BY as validationUpdatedBy,  ros.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,  ros.VALIDATION_UPDATED_NOTE as validationUpdatedNote,  ros.OVERRIDE_TEST_WINDOW as overrideTestWindow,  ros.PASSWORD as password,  ros.STUDENT_ID as studentId,  ros.CREATED_BY as createdBy,  ros.UPDATED_BY as updatedBy,  ros.ACTIVATION_STATUS as activationStatus,  ros.UPDATED_DATE_TIME as updatedDateTime,  ros.CUSTOMER_ID as customerId,  ros.TUTORIAL_TAKEN_DATE_TIME as tutorialTakenDateTime,  ros.CAPTURE_METHOD as captureMethod,  ros.SCORING_STATUS as scoringStatus,  ros.ORG_NODE_ID as orgNodeId,  ros.FORM_ASSIGNMENT as formAssignment,  stu.first_name as firstName,  stu.middle_name as middleName,  stu.last_name as lastName,  stu.ext_pin1 as extPin1,  stu.user_name as userName,  ros.CUSTOMER_FLAG_STATUS as customerFlagStatus from  test_roster ros, student stu where  ros.student_Id = {studentId}  and stu.student_id = ros.student_id")
    RosterElement getRosterElementForStudent(Integer studentId) throws SQLException;


    /**
     * @jc:sql statement::
     * select
     *      ros.TEST_ROSTER_ID as testRosterId,
     *      ros.TEST_ADMIN_ID as testAdminId,
     *      ros.CREATED_DATE_TIME as createdDateTime,
     *      ros.START_DATE_TIME as startDateTime,
     *      ros.COMPLETION_DATE_TIME as completionDateTime,
     *      ros.TEST_COMPLETION_STATUS as testCompletionStatus,
     *      ros.VALIDATION_STATUS as validationStatus,
     *      ros.VALIDATION_UPDATED_BY as validationUpdatedBy,
     *      ros.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,
     *      ros.VALIDATION_UPDATED_NOTE as validationUpdatedNote,
     *      ros.OVERRIDE_TEST_WINDOW as overrideTestWindow,
     *      ros.PASSWORD as password,
     *      ros.STUDENT_ID as studentId,
     *      ros.CREATED_BY as createdBy,
     *      ros.UPDATED_BY as updatedBy,
     *      ros.ACTIVATION_STATUS as activationStatus,
     *      ros.UPDATED_DATE_TIME as updatedDateTime,
     *      ros.CUSTOMER_ID as customerId,
     *      ros.TUTORIAL_TAKEN_DATE_TIME as tutorialTakenDateTime,
     *      ros.CAPTURE_METHOD as captureMethod,
     *      ros.SCORING_STATUS as scoringStatus,
     *      ros.ORG_NODE_ID as orgNodeId,
     *      ros.FORM_ASSIGNMENT as formAssignment,
     *      ros.CUSTOMER_FLAG_STATUS as customerFlagStatus,
     *      stu.first_name as firstName,
     *      stu.middle_name as middleName,
     *      stu.last_name as lastName,
     *      stu.ext_pin1 as extPin1,
     *      stu.user_name as userName
     * from
     *      test_roster ros, student stu
     * where
     *      ros.test_roster_id = {testRosterId}
     *      and stu.student_id = ros.student_id::
    */ 
    @JdbcControl.SQL(statement = "select  ros.TEST_ROSTER_ID as testRosterId,  ros.TEST_ADMIN_ID as testAdminId,  ros.CREATED_DATE_TIME as createdDateTime,  ros.START_DATE_TIME as startDateTime,  ros.COMPLETION_DATE_TIME as completionDateTime,  ros.TEST_COMPLETION_STATUS as testCompletionStatus,  ros.VALIDATION_STATUS as validationStatus,  ros.VALIDATION_UPDATED_BY as validationUpdatedBy,  ros.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,  ros.VALIDATION_UPDATED_NOTE as validationUpdatedNote,  ros.OVERRIDE_TEST_WINDOW as overrideTestWindow,  ros.PASSWORD as password,  ros.STUDENT_ID as studentId,  ros.CREATED_BY as createdBy,  ros.UPDATED_BY as updatedBy,  ros.ACTIVATION_STATUS as activationStatus,  ros.UPDATED_DATE_TIME as updatedDateTime,  ros.CUSTOMER_ID as customerId,  ros.TUTORIAL_TAKEN_DATE_TIME as tutorialTakenDateTime,  ros.CAPTURE_METHOD as captureMethod,  ros.SCORING_STATUS as scoringStatus,  ros.ORG_NODE_ID as orgNodeId,  ros.FORM_ASSIGNMENT as formAssignment,  ros.CUSTOMER_FLAG_STATUS as customerFlagStatus,  stu.first_name as firstName,  stu.middle_name as middleName,  stu.last_name as lastName,  stu.ext_pin1 as extPin1,  stu.user_name as userName from  test_roster ros, student stu where  ros.test_roster_id = {testRosterId}  and stu.student_id = ros.student_id")
    RosterElement getRosterElement(Integer testRosterId) throws SQLException;

    /**
     * @jc:sql statement::
     * delete from 
     *     test_roster ros 
     * where 
     *     ros.test_completion_status in ('SC', 'NT')
     *     and ros.test_Admin_id = {testAdminId}::
     */
    @JdbcControl.SQL(statement = "delete from  test_roster ros  where  ros.test_completion_status in ('SC', 'NT')  and ros.test_Admin_id = {testAdminId}")
    void deleteTestRostersForAdmin(Integer testAdminId) throws SQLException;
 
    /**
     * @jc:sql statement::
     * delete from 
     *     test_roster ros 
     * where 
     *     ros.test_completion_status in ('SC', 'NT')
     *     and ros.test_roster_id = {re.testRosterId}::
     */
    @JdbcControl.SQL(statement = "delete from  test_roster ros  where  ros.test_completion_status in ('SC', 'NT')  and ros.test_roster_id = {re.testRosterId}")
    void deleteTestRoster(RosterElement re) throws SQLException;
    
    /**
     * @jc:sql statement::
     * update 
     *     test_roster
     * set
     *      form_assignment = {re.formAssignment},
     *      org_node_id = {re.orgNodeId},
     *      test_completion_status = {re.testCompletionStatus},
     *      customer_flag_status = {re.customerFlagStatus},
     *      validation_status = {re.validationStatus}
     * where 
     *     test_admin_id = {re.testAdminId}
     *     and student_id = {re.studentId}::
     */
    @JdbcControl.SQL(statement = "update  test_roster set  form_assignment = {re.formAssignment},  org_node_id = {re.orgNodeId},  test_completion_status = {re.testCompletionStatus},  customer_flag_status = {re.customerFlagStatus},  validation_status = {re.validationStatus} where  test_admin_id = {re.testAdminId}  and student_id = {re.studentId}")
    void updateTestRoster(RosterElement re) throws SQLException;
    
    /**
     * @jc:sql statement::
     * update 
     *     test_roster
     * set
     *      form_assignment = null
     * where 
     *     test_admin_id = {testAdminId}::
     */
    @JdbcControl.SQL(statement = "update  test_roster set  form_assignment = null where  test_admin_id = {testAdminId}")
    void clearFormAssignmentsForAdmin(Integer testAdminId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select
     *      ros.TEST_ROSTER_ID as testRosterId,
     *      ros.TEST_ADMIN_ID as testAdminId,
     *      ros.CREATED_DATE_TIME as createdDateTime,
     *      ros.START_DATE_TIME as startDateTime,
     *      ros.COMPLETION_DATE_TIME as completionDateTime,
     *      ros.TEST_COMPLETION_STATUS as testCompletionStatus,
     *      ros.VALIDATION_STATUS as validationStatus,
     *      ros.VALIDATION_UPDATED_BY as validationUpdatedBy,
     *      ros.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,
     *      ros.VALIDATION_UPDATED_NOTE as validationUpdatedNote,
     *      ros.OVERRIDE_TEST_WINDOW as overrideTestWindow,
     *      ros.PASSWORD as password,
     *      ros.STUDENT_ID as studentId,
     *      ros.CREATED_BY as createdBy,
     *      ros.UPDATED_BY as updatedBy,
     *      ros.ACTIVATION_STATUS as activationStatus,
     *      ros.UPDATED_DATE_TIME as updatedDateTime,
     *      ros.CUSTOMER_ID as customerId,
     *      ros.TUTORIAL_TAKEN_DATE_TIME as tutorialTakenDateTime,
     *      ros.CAPTURE_METHOD as captureMethod,
     *      ros.SCORING_STATUS as scoringStatus,
     *      ros.ORG_NODE_ID as orgNodeId,
     *      ros.FORM_ASSIGNMENT as formAssignment,
     *      stu.first_name as firstName,
     *      stu.middle_name as middleName,
     *      stu.last_name as lastName,
     *      stu.ext_pin1 as extPin1,
     *      stu.user_name as userName,
     *      ros.CUSTOMER_FLAG_STATUS as customerFlagStatus
     * from
     *      test_roster ros, student stu, org_node_ancestor anc
     * where
     *      ros.test_admin_id = {testAdminId}
     *      and anc.ancestor_org_node_id = {orgNodeId}
     *      and anc.org_node_id = ros.org_node_id
     *      and stu.student_id = ros.student_id
     *      and (stu.activation_Status = 'AC' or decode(decode(ros.test_completion_status, 'NT', 'SC', ros.test_completion_status), 'SC', 'F', 'T') = 'T')::
     *      array-max-length="all"
     */ 

    @JdbcControl.SQL(statement = "select  ros.TEST_ROSTER_ID as testRosterId,  ros.TEST_ADMIN_ID as testAdminId,  ros.CREATED_DATE_TIME as createdDateTime,  ros.START_DATE_TIME as startDateTime,  ros.COMPLETION_DATE_TIME as completionDateTime,  ros.TEST_COMPLETION_STATUS as testCompletionStatus,  ros.VALIDATION_STATUS as validationStatus,  ros.VALIDATION_UPDATED_BY as validationUpdatedBy,  ros.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,  ros.VALIDATION_UPDATED_NOTE as validationUpdatedNote,  ros.OVERRIDE_TEST_WINDOW as overrideTestWindow,  ros.PASSWORD as password,  ros.STUDENT_ID as studentId,  ros.CREATED_BY as createdBy,  ros.UPDATED_BY as updatedBy,  ros.ACTIVATION_STATUS as activationStatus,  ros.UPDATED_DATE_TIME as updatedDateTime,  ros.CUSTOMER_ID as customerId,  ros.TUTORIAL_TAKEN_DATE_TIME as tutorialTakenDateTime,  ros.CAPTURE_METHOD as captureMethod,  ros.SCORING_STATUS as scoringStatus,  ros.ORG_NODE_ID as orgNodeId,  ros.FORM_ASSIGNMENT as formAssignment,  stu.first_name as firstName,  stu.middle_name as middleName,  stu.last_name as lastName,  stu.ext_pin1 as extPin1,  stu.user_name as userName,  ros.CUSTOMER_FLAG_STATUS as customerFlagStatus, org.org_node_name as className from  test_roster ros, student stu, org_node_ancestor anc, org_node org where  ros.test_admin_id = {testAdminId}  and anc.ancestor_org_node_id = {orgNodeId}  and anc.org_node_id = ros.org_node_id  and stu.student_id = ros.student_id and ros.org_node_id = org.org_node_id  and (stu.activation_Status = 'AC' or decode(decode(ros.test_completion_status, 'NT', 'SC', ros.test_completion_status), 'SC', 'F', 'T') = 'T')",
                     arrayMaxLength = 100000)
    RosterElement [] getRosterForTestSessionAndOrgNode(Integer testAdminId, Integer orgNodeId) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *     tr.test_roster_id as rosterId
     * from
     *      test_roster tr,
     *      student 
     * where 
     *     tr.test_admin_id = {testAdminId}
     *     and tr.student_id = {studentId}
     *     and tr.student_id = student.student_id::
     */
    @JdbcControl.SQL(statement = "select  tr.test_roster_id as rosterId from  test_roster tr,  student  where  tr.test_admin_id = {testAdminId}  and tr.student_id = {studentId}  and tr.student_id = student.student_id")
    Integer getRosterIdForStudentAndTestAdmin(Integer studentId, Integer testAdminId) throws SQLException;
    
    
    /**
     * @jc:sql statement::
     * select ta.test_admin_id as testAdminId, 
     *        ta.test_admin_name as sessionName, 
     *        ta.session_number as sessionNumber, 
     *        ros.test_roster_id as testRosterId, 
     *        stu.user_name as loginId, 
     *        ros.password as password, 
     *        tais.access_code as accessCode
     * from 
     * ( select itemSetId, rosterId, 
     * (case
     * when count(*) = 1 then max(status)
     * else 'Started'
     * end) as status from
     *   (
     *   select distinct 
     *     ros.test_roster_id as rosterId,
     *     isp.parent_item_set_id as itemSetId,
     *     decode(siss.completion_status, 'SC', 'Scheduled', 'CO', 'Completed', 'Started') as status,
     *     count(*) as counter 
     * from
     *     test_roster ros,
     *     test_admin adm,
     *     org_node_ancestor ona,
     *     item_set_ancestor isa,
     *     item_set iset,
     *     item_set_parent isp,
     *     student_item_Set_status siss
     * where
     *     siss.test_roster_id = ros.test_roster_id
     *     and ros.test_admin_id = adm.test_admin_id
     *     and adm.program_id = {programId}
     *     and adm.item_set_id = {itemSetIdTC}
     *     and ona.ancestor_org_node_id = {orgNodeId}
     *     and ros.org_node_id = ona.org_node_id
     *     and ros.activation_status = 'AC'
     *     and adm.activation_status = 'AC'
     *     and iset.item_set_id = isa.item_set_id
     *     and isa.ancestor_item_set_id = {itemSetIdTS}
     *     and iset.item_set_type = 'TD'
     *     and isp.item_set_id = isa.item_set_id
     *     and siss.item_set_id = isa.item_set_id
     *     and isp.parent_item_set_id = {itemSetIdTS} 
     * group by
     *     ros.test_roster_id,
     *     isp.parent_item_set_id,
     *     decode(siss.completion_status, 'SC', 'Scheduled', 'CO', 'Completed', 'Started')
     *     )
     *     group by itemSetId, rosterId
     * ) sc, test_admin ta, test_roster ros, student stu, test_admin_item_set tais
     * where sc.rosterId = ros.test_roster_id
     * and ta.test_admin_id = ros.test_admin_id
     * and stu.student_id = ros.student_id
     * and tais.item_set_id = sc.itemSetId
     * and tais.test_admin_id = ta.test_admin_id
     * and sc.status = {status}::
     *      array-max-length="all"
     */ 

    @JdbcControl.SQL(statement = "select ta.test_admin_id as testAdminId,  ta.test_admin_name as sessionName,  ta.session_number as sessionNumber,  ros.test_roster_id as testRosterId,  stu.user_name as loginId,  ros.password as password,  tais.access_code as accessCode from  ( select itemSetId, rosterId,  (case when count(*) = 1 then max(status) else 'Started' end) as status from  (  select distinct  ros.test_roster_id as rosterId,  isp.parent_item_set_id as itemSetId,  decode(siss.completion_status, 'SC', 'Scheduled', 'CO', 'Completed', 'Started') as status,  count(*) as counter  from  test_roster ros,  test_admin adm,  org_node_ancestor ona,  item_set_ancestor isa,  item_set iset,  item_set_parent isp,  student_item_Set_status siss where  siss.test_roster_id = ros.test_roster_id  and ros.test_admin_id = adm.test_admin_id  and adm.program_id = {programId}  and adm.item_set_id = {itemSetIdTC}  and ona.ancestor_org_node_id = {orgNodeId}  and ros.org_node_id = ona.org_node_id  and ros.activation_status = 'AC'  and adm.activation_status = 'AC'  and iset.item_set_id = isa.item_set_id  and isa.ancestor_item_set_id = {itemSetIdTS}  and iset.item_set_type = 'TD'  and isp.item_set_id = isa.item_set_id  and siss.item_set_id = isa.item_set_id  and isp.parent_item_set_id = {itemSetIdTS}  group by  ros.test_roster_id,  isp.parent_item_set_id,  decode(siss.completion_status, 'SC', 'Scheduled', 'CO', 'Completed', 'Started')  )  group by itemSetId, rosterId ) sc, test_admin ta, test_roster ros, student stu, test_admin_item_set tais where sc.rosterId = ros.test_roster_id and ta.test_admin_id = ros.test_admin_id and stu.student_id = ros.student_id and tais.item_set_id = sc.itemSetId and tais.test_admin_id = ta.test_admin_id and sc.status = {status}", arrayMaxLength = 100000)
    ProgramStatusSession [] getProgramStatusSessions(Integer programId, Integer orgNodeId, Integer itemSetIdTC, Integer itemSetIdTS, String status) throws SQLException;
    
    
    @JdbcControl.SQL(statement = "update test_roster set test_completion_status = 'IN' , updated_by = {updatedBy} , updated_date_time = {completedDateTime}   where test_roster_id = {testRosterId}")
    void updateTestRosterForReopen(java.lang.Integer testRosterId, java.util.Date completedDateTime, java.lang.Integer updatedBy) throws SQLException;
    
    @JdbcControl.SQL(statement = "insert into  student_item_set_status (  TEST_ROSTER_ID,  ITEM_SET_ID,  COMPLETION_STATUS,  START_DATE_TIME,  COMPLETION_DATE_TIME,  VALIDATION_STATUS,  VALIDATION_UPDATED_BY,  VALIDATION_UPDATED_DATE_TIME,  VALIDATION_UPDATED_NOTE,  TIME_EXPIRED,  ITEM_SET_ORDER,  CUSTOMER_FLAG_STATUS  ) values (  {testRosterId},  {sss.itemSetId},  {sss.completionStatus},  {sss.startDateTime},  {sss.completionDateTime},  {sss.validationStatus},  {sss.validationUpdatedBy},  {sss.validationUpdatedDateTime},  {sss.validationUpdatedNote},  {sss.timeExpired},  {sss.itemSetOrder},  (select  cc.default_value  from  customer_configuration cc  where  cc.customer_id = {customerId}  and cc.customer_configuration_name = 'Roster_Status_Flag')  )")
    void createNewStudentItemSetStatusForRoster(Integer customerId, StudentSessionStatus sss, Integer testRosterId) throws SQLException;
    
    @JdbcControl.SQL(statement = "SELECT ROS.TEST_ROSTER_ID  AS TESTROSTERID,  ROS.TEST_ADMIN_ID  AS TESTADMINID, ROS.VALIDATION_STATUS AS VALIDATIONSTATUS,  ROS.STUDENT_ID  AS STUDENTID FROM TEST_ROSTER ROS, STUDENT STU, TEST_ADMIN TADMIN  WHERE ROS.STUDENT_ID = {studentId} AND ROS.ACTIVATION_STATUS = 'AC' AND STU.STUDENT_ID = ROS.STUDENT_ID  AND STU.ACTIVATION_STATUS = 'AC'  AND TADMIN.TEST_ADMIN_ID = ROS.TEST_ADMIN_ID  AND TADMIN.ACTIVATION_STATUS = 'AC'")
    RosterElement[] getTestRosterForStudentIdAndOrgNode(Integer studentId) throws SQLException;

    // added for student pacing
    @JdbcControl.SQL(statement = "select extended_time as extendedTime from student_accommodation where student_id = {studentId}")
    String getExtendedTimeAccomForStudent(Integer studentId) throws SQLException;
    
    @JdbcControl.SQL(statement = "UPDATE TEST_ROSTER SET DNS_STATUS = {dnsStatus}, DNS_UPDATED_BY = {userName}, DNS_UPDATED_DATETIME = SYSDATE WHERE TEST_ROSTER_ID = {testRosterId}")
    int updateDonotScore(Integer testRosterId, String dnsStatus, Integer userName) throws SQLException;
    
    @JdbcControl.SQL(statement = "select count(*) from test_roster where org_node_id = {selectedOrgNodeId} and activation_status = 'AC'")
    int rosterCountAssociatedWithOrg(Integer selectedOrgNodeId) throws SQLException;
    
    @JdbcControl.SQL(statement = "select test_roster_id from test_roster where student_id = {studentId} and test_admin_id = {testAdminId} and activation_status = 'AC'")
    Integer findTestRosterId(Long studentId, Long testAdminId) throws SQLException;
    
    @JdbcControl.SQL(statement = "SELECT ISET.ITEM_SET_ID FROM ITEM_SET ISET,  ITEM_SET ISETTS, ITEM_SET_PARENT ISP, TEST_ADMIN_ITEM_SET TAIS, TEST_ROSTER ROS WHERE ROS.TEST_ROSTER_ID = {testRosterId} AND ROS.TEST_ADMIN_ID = {sessionId} AND ROS.TEST_ADMIN_ID = TAIS.TEST_ADMIN_ID AND TAIS.ITEM_SET_ID = ISP.PARENT_ITEM_SET_ID AND ISP.PARENT_ITEM_SET_ID = ISETTS.ITEM_SET_ID AND ISETTS.ITEM_SET_NAME = {itemSetName} AND ISP.ITEM_SET_ID = ISET.ITEM_SET_ID")
    Integer[] findSubtestIdFromTestRoster(Integer testRosterId, String itemSetName, Integer sessionId) throws SQLException;
    
    @JdbcControl.SQL(statement = " SELECT DISTINCT ISET.item_Set_name FROM STUDENT_ITEM_SET_STATUS SISS, ITEM_SET ISET, item_Set_parent isp WHERE SISS.TEST_ROSTER_ID = {testRosterId} AND SISS.ITEM_SET_ID = isp.ITEM_SET_ID AND iset.item_Set_id = isp.parent_item_set_id AND ISET.ITEM_SET_NAME = {itemSetName}")
    String verifySubtestPresence(Integer testRosterId, String itemSetName) throws SQLException;

    @JdbcControl.SQL(statement = "SELECT ROS.TEST_ROSTER_ID AS testRosterId, ROS.TEST_ADMIN_ID AS testAdminId, ROS.STUDENT_ID AS studentId, ROS.TEST_COMPLETION_STATUS as testCompletionStatus, ROS.START_DATE_TIME startDateTime, ROS.COMPLETION_DATE_TIME completionDateTime FROM TEST_ROSTER ROS WHERE ROS.TEST_ADMIN_ID = {testAdminId} AND ROS.ACTIVATION_STATUS = 'AC'")     
    RosterElement[] getTestRosterFromTestAdmin(Integer testAdminId) throws SQLException;
    
    static final long serialVersionUID = 1L;
    
    @JdbcControl.SQL(statement ="select IRC.INVALIDATION_REASON_ID||'_'||IRC.INVALIDATION_REASON_DETAILS as invalidReason from invalidation_reason_code IRC order by INVALIDATION_REASON_ID" )
	String[] getInvalidReasonCodeList() throws SQLException;
}