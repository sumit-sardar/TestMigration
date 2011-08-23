package com.ctb.control.db; 

import java.sql.SQLException;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.dataExportManagement.CustomerConfiguration;
import com.ctb.bean.dataExportManagement.CustomerConfigurationValue;
import com.ctb.bean.dataExportManagement.ManageJob;
import com.ctb.bean.dataExportManagement.ManageStudent;
import com.ctb.bean.testAdmin.ManageTestSession;

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
    @JdbcControl.SQL(statement = "select  customer_configuration_value as customerConfigurationValue,  customer_configuration_id as customerConfigurationId,  sort_order sortOrder from customer_configuration_value where customer_configuration_id = {customerConfigurationId} order by sort_order, customer_configuration_value", arrayMaxLength = 100000)
    CustomerConfigurationValue [] getCustomerConfigurationValues(int customerConfigurationId) throws SQLException;
     
   /* @JdbcControl.SQL(statement = " select distinct roster.test_roster_id as rosterId, stu.user_name as studentUserName, stu.student_id as id, stu.user_name as loginId, stu.first_name as firstName, stu.middle_name as middleName, stu.last_name as lastName, concat(concat(stu.last_name, ', '), concat(stu.first_name, concat(' ', stu.MIDDLE_NAME))) as studentName, stu.gender as gender, stu.birthdate as birthDate, stu.grade as grade, stu.ext_pin1 as studentIdNumber, stu.ext_pin2 as studentIdNumber2, stu.created_by as createdBy, tadmin.test_admin_name as testSessionName, tadmin.item_set_id as itemSetIDTC from student stu, test_roster roster, student_item_set_status sis, test_admin tadmin where stu.student_id = roster.student_id and sis.test_roster_id = roster.test_roster_id and roster.customer_id is not null and roster.customer_id = {customerId} and nvl(roster.student_exported,'F') = 'F' and roster.activation_status = 'AC' and sis.completion_status = 'CO' and roster.test_admin_id = tadmin.test_admin_id " , arrayMaxLength=0, fetchSize = 50000) 
	ManageStudent[] getCompletedSubtestUnexportedStudents(Integer customerId)throws SQLException;
   */
    @JdbcControl.SQL(statement = "select roster.test_roster_id as rosterId, stu.user_name as studentUserName, stu.student_id as id, stu.user_name as loginId, stu.first_name as firstName, stu.middle_name as middleName, stu.last_name as lastName, concat(concat(stu.last_name, ', '), concat(stu.first_name, concat(' ', stu.MIDDLE_NAME))) as studentName, stu.gender as gender, stu.birthdate as birthDate, stu.grade as grade, stu.ext_pin1 as studentIdNumber, stu.ext_pin2 as studentIdNumber2, stu.created_by as createdBy, tadmin.test_admin_name as testSessionName, roster.test_completion_status as testCompletionStatus, (select decode ( count(*), 0, 'No', 'Yes')from  student_item_set_status sis where sis.test_roster_id = roster.test_roster_id and sis.completion_status = 'CO') as exportStatus from student stu, test_roster roster, test_admin tadmin  where stu.student_id = roster.student_id  and roster.customer_id is not null and roster.customer_id = {customerId} and nvl(roster.student_exported, 'F') = 'F' and roster.activation_status = 'AC' and roster.test_completion_status <> 'CO' and roster.test_admin_id = tadmin.test_admin_id " , arrayMaxLength=0, fetchSize = 50000)
    ManageStudent[] getIncompleteRosterUnexportedStudents(Integer customerId)throws SQLException;
    
    @JdbcControl.SQL(statement = " select * from (select distinct roster.test_roster_id as rosterId, stu.student_id as id, stu.user_name as loginId, stu.first_name as firstName,  stu.middle_name as middleName, stu.last_name as lastName, concat(concat(stu.last_name, ', '), concat(stu.first_name, concat(' ', stu.MIDDLE_NAME))) as studentName, stu.gender as gender, stu.birthdate as birthDate, stu.grade as grade, stu.ext_pin1 as studentIdNumber, stu.ext_pin2 as studentIdNumber2, stu.created_by as createdBy, tadmin.test_admin_name as testSessionName, tadmin.access_code as accessCode,                        tadmin.test_admin_id as testAdminId, tadmin.item_set_id as itemSetIDTC from student stu, test_admin tadmin, test_roster roster, student_item_set_status sis where stu.student_id = roster.student_id  and roster.test_admin_id = tadmin.test_admin_id  and sis.test_roster_id = roster.test_roster_id  and roster.customer_id is not null and roster.customer_id = {customerId} and nvl(roster.student_exported, 'F') = 'F' and roster.activation_status = 'AC' and sis.completion_status = 'CO') stu where  OAS_UTILS.GET_SCORING_STATUS_BY_ROSTER(rosterId) = 'IN'" , arrayMaxLength=0, fetchSize = 50000) 
	ManageStudent[] getAllUnscoredUnexportedStudents(Integer customerId)throws SQLException;
    
    @JdbcControl.SQL(statement = " select distinct export.export_id as jobId, export.student_count as studentCount, to_char(export.created_date_time, 'MM/DD/YYYY') as createdDateTime, status.status_name as jobStatus, export.created_by as createdBy, export.last_update_time as lastUpdateTime, export.last_update_status as lastUpdateStatus, export.message from data_export export, job_status status where export.status = status.status_id and export.created_by = {userId}" , arrayMaxLength=0, fetchSize = 50000) 
	ManageJob[] getDataExportJobStatus(Integer userId)throws SQLException;
    
    @JdbcControl.SQL(statement = " select DISTINCT tadmin.test_admin_id as testAdminId,tadmin.test_admin_name as testSessionName,tadmin.login_start_date as startDate,tadmin.login_end_date as endDate,tadmin.test_admin_status as status, tadmin.daily_login_start_time as dailyLoginStartTime, tadmin.daily_login_end_time as dailyLoginEndTime, tadmin.time_zone as timeZone from  test_admin tadmin, program prog where tadmin.login_start_date BETWEEN prog.program_start_date and prog.program_end_date and tadmin.login_end_date BETWEEN prog.program_start_date and prog.program_end_date and tadmin.program_id = prog.program_id and prog.activation_status = 'AC' and prog.customer_id = {customerId}" , arrayMaxLength=0, fetchSize = 50000) 
    ManageTestSession[] getTestSessionForExportWithStudents(Integer customerId)throws SQLException;
    
    @JdbcControl.SQL(statement = " select distinct roster.test_roster_id as rosterId from test_roster  roster where roster.customer_id = {customerId} and nvl(roster.student_exported, 'F') = 'F' and roster.activation_status = 'AC' and roster.test_completion_status = 'CO'and roster.test_admin_id = {testAdminId}" , arrayMaxLength=0, fetchSize = 50000) 
	Integer[] getCompletedSubtestUnexportedStudentsForTestSession(Integer customerId,Integer testAdminId)throws SQLException;

    @JdbcControl.SQL(statement = " select count (distinct roster.test_roster_id) as rosterId from test_roster  roster where roster.customer_id = {customerId} and nvl(roster.student_exported, 'F') = 'F' and roster.activation_status = 'AC' and roster.test_completion_status = 'SC'and roster.test_admin_id = {testAdminId}" , arrayMaxLength=0, fetchSize = 50000)
	Integer getScheduledSubtestUnexportedStudentsForTestSession(Integer customerId,Integer testAdminId)throws SQLException;

    @JdbcControl.SQL(statement = " select count (distinct roster.test_roster_id) as rosterId from test_roster  roster where roster.customer_id = {customerId} and nvl(roster.student_exported, 'F') = 'F' and roster.activation_status = 'AC' and roster.test_completion_status = 'NT'and roster.test_admin_id = {testAdminId}" , arrayMaxLength=0, fetchSize = 50000) 
	Integer getNotTakenSubtestUnexportedStudentsForTestSession(Integer customerId,Integer testAdminId)throws SQLException;

    @JdbcControl.SQL(statement = " SELECT DISTINCT ROSTER.TEST_ROSTER_ID AS ROSTERID FROM TEST_ROSTER ROSTER, STUDENT_ITEM_SET_STATUS SIS WHERE ROSTER.CUSTOMER_ID = {customerId} AND NVL(ROSTER.STUDENT_EXPORTED, 'F') = 'F' AND ROSTER.ACTIVATION_STATUS = 'AC' AND ROSTER.TEST_COMPLETION_STATUS = 'IC' AND ROSTER.TEST_ADMIN_ID = {testAdminId} AND SIS.TEST_ROSTER_ID = ROSTER.TEST_ROSTER_ID   AND EXISTS (SELECT 1  FROM STUDENT_ITEM_SET_STATUS SISS WHERE SISS.COMPLETION_STATUS IN ('CO', 'IS') AND SISS.TEST_ROSTER_ID = ROSTER.TEST_ROSTER_ID) AND NOT EXISTS (SELECT 1 FROM STUDENT_ITEM_SET_STATUS SI WHERE SI.COMPLETION_STATUS = 'IN' AND SI.TEST_ROSTER_ID = ROSTER.TEST_ROSTER_ID)" , arrayMaxLength=0, fetchSize = 50000) 
    Integer[] getInCompleteSubtestUnexportedStudentsForTestSession(Integer customerId,Integer testAdminId)throws SQLException;

    @JdbcControl.SQL(statement = " select count (distinct roster.test_roster_id)as rosterId from test_roster  roster where roster.customer_id = {customerId} and nvl(roster.student_exported, 'F') = 'F' and roster.activation_status = 'AC' and roster.test_completion_status = 'IS'and roster.test_admin_id = {testAdminId}" , arrayMaxLength=0, fetchSize = 50000) 
	Integer getStudentStopSubtestUnexportedStudentsForTestSession(Integer customerId,Integer testAdminId)throws SQLException;
    
    @JdbcControl.SQL(statement = " SELECT DISTINCT ROSTER.TEST_ROSTER_ID AS ROSTERID FROM TEST_ROSTER ROSTER, Student_Item_Set_Status  siss WHERE ROSTER.CUSTOMER_ID = {customerId} AND NVL(ROSTER.STUDENT_EXPORTED, 'F') = 'F' AND ROSTER.ACTIVATION_STATUS = 'AC' AND ROSTER.TEST_COMPLETION_STATUS = 'IS' AND siss.test_roster_id = ROSTER.test_roster_id  AND siss.completion_status  = 'CO' AND ROSTER.TEST_ADMIN_ID = {testAdminId}" , arrayMaxLength=0, fetchSize = 50000) 
	Integer[] getStudentStopToBeExportedSubtestUnexportedStudentsForTestSession(Integer customerId,Integer testAdminId)throws SQLException;
    
    
    @JdbcControl.SQL(statement = " select count (distinct roster.test_roster_id) as rosterId from test_roster  roster where roster.customer_id = {customerId} and nvl(roster.student_exported, 'F') = 'F' and roster.activation_status = 'AC' and roster.test_completion_status = 'IN'and roster.test_admin_id = {testAdminId}" , arrayMaxLength=0, fetchSize = 50000) 
	Integer getSystemStopSubtestUnexportedStudentsForTestSession(Integer customerId,Integer testAdminId)throws SQLException;

    @JdbcControl.SQL(statement = " select count (distinct roster.test_roster_id) as rosterId from test_roster roster, student_item_set_status sis where roster.customer_id = {customerId} and roster.test_admin_id = {testAdminId} and roster.test_completion_status = 'IC' and sis.test_roster_id = roster.test_roster_id and sis.completion_status = 'IN'" , arrayMaxLength=0, fetchSize = 50000) 
    Integer getSystemStopCountFromInCompleteForTestSession(Integer customerId,Integer testAdminId)throws SQLException;
    
    @JdbcControl.SQL(statement = " SELECT SEQ_EXPORT_ID.NEXTVAL as job_id from DUAL") 
	Integer getJobId()throws SQLException;
    
    @JdbcControl.SQL(statement = " insert into data_export(export_id,    student_count, created_date_time, status, created_by, last_update_time, last_update_status,  message) values  ({jobId}, {studentCount}, sysdate, 1, {userId}, sysdate, 1, 'Job submitted')")
	void addJob(Integer jobId, Integer studentCount, Integer userId)throws SQLException;

    @JdbcControl.SQL(statement = " select OAS_UTILS.GET_SCORING_STATUS_BY_ROSTER({rosterId}) as scoringStatus from dual ")
    String getScoringStatusFromRoster(Integer rosterId)throws SQLException;

    //Modified for student id blank defect
    @JdbcControl.SQL(statement = "select  ros.test_roster_id as rosterId, stu.student_id as id, stu.user_name as loginId, concat(concat(stu.last_name, ', '),   concat(stu.first_name,          concat(' ', stu.MIDDLE_NAME))) as studentName,  stu.grade as grade,STU.EXT_PIN1 AS studentIdNumber, tadmin.test_admin_name as testSessionName, tadmin.item_set_id as itemSetIdTC ,  tadmin.test_admin_id as testAdminId  from test_roster ros,student stu , test_admin tadmin where ros.test_roster_id = {rosterId} and ros.activation_status = 'AC' and ros.student_id = stu.student_id and ros.test_admin_id = tadmin.test_admin_id ")
    ManageStudent getAllUnscoredUnexportedStudentsDetail(Integer rosterId)throws SQLException;

    
}