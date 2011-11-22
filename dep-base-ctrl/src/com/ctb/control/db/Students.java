package com.ctb.control.db; 

import com.bea.control.*;
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import com.ctb.bean.testAdmin.SchedulingStudent;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.Student; 
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
public interface Students extends JdbcControl
{ 
    /**
     * @jc:sql statement::
     * select 
     *      stu.student_id as studentId,
     *      stu.user_Name as userName,
     *      stu.password as password,
     *      stu.first_Name as firstName,
     *      stu.middle_Name as middleName,
     *      stu.last_Name as lastName,
     *      stu.preferred_Name as preferredName,
     *      stu.prefix as prefix,
     *      stu.suffix as suffix,
     *      stu.birthdate as birthdate,
     *      stu.gender as gender,
     *      stu.ethnicity as ethnicity,
     *      stu.email as email,
     *      stu.grade as grade,
     *      stu.ext_Elm_Id as extElmId,
     *      stu.ext_Pin1 as extPin1,
     *      stu.ext_Pin2 as extPin2,
     *      stu.ext_Pin3 as extPin3,
     *      stu.ext_School_Id as extSchoolId,
     *      stu.active_Session as activeSession,
     *      stu.potential_Duplicated_Student as potentialDuplicatedStudent,
     *      stu.created_By as createdBy,
     *      stu.created_Date_Time as createdDateTime,
     *      stu.updated_By as updatedBy,
     *      stu.updated_Date_Time as updatedDateTime,
     *      stu.activation_Status as activationStatus,
     *      stu.data_import_history_id as dataImportHistoryId
     * from
	 *      org_node_student ons,
     *      student stu
     * where
     *      ons.student_id = stu.student_id
     *      and ons.org_node_id = {orgNodeId}
     *      and ons.activation_status = 'AC'
     *      and stu.activation_status = 'AC'::
     * array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  stu.student_id as studentId,  stu.user_Name as userName,  stu.password as password,  stu.first_Name as firstName,  stu.middle_Name as middleName,  stu.last_Name as lastName,  stu.preferred_Name as preferredName,  stu.prefix as prefix,  stu.suffix as suffix,  stu.birthdate as birthdate,  stu.gender as gender,  stu.ethnicity as ethnicity,  stu.email as email,  stu.grade as grade,  stu.ext_Elm_Id as extElmId,  stu.ext_Pin1 as extPin1,  stu.ext_Pin2 as extPin2,  stu.ext_Pin3 as extPin3,  stu.ext_School_Id as extSchoolId,  stu.active_Session as activeSession,  stu.potential_Duplicated_Student as potentialDuplicatedStudent,  stu.created_By as createdBy,  stu.created_Date_Time as createdDateTime,  stu.updated_By as updatedBy,  stu.updated_Date_Time as updatedDateTime,  stu.activation_Status as activationStatus,  stu.data_import_history_id as dataImportHistoryId from  org_node_student ons,  student stu where  ons.student_id = stu.student_id  and ons.org_node_id = {orgNodeId}  and ons.activation_status = 'AC'  and stu.activation_status = 'AC'",
                     arrayMaxLength = 100000)
    Student [] getStudentsForOrgNode(Integer orgNodeId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select 
     *      count(distinct stu.student_id)
     * from
     *      student stu,
     *      org_node_student ons,
     *      org_node_ancestor ona,
     *      user_role urole,
     *      users
     * where
     *      stu.student_id = {studentId}
     *      and ons.student_id = stu.student_id
     *      and ona.org_node_id = ons.org_node_id
     *      and urole.org_node_id = ona.ancestor_org_node_id
     *      and users.user_id = urole.user_id
     *      and urole.activation_status = 'AC'
     *      and users.user_name = {userName}::
     */
    @JdbcControl.SQL(statement = "select  count(distinct stu.student_id) from  student stu,  org_node_student ons,  org_node_ancestor ona,  user_role urole,  users where  stu.student_id = {studentId}  and ons.student_id = stu.student_id  and ona.org_node_id = ons.org_node_id  and urole.org_node_id = ona.ancestor_org_node_id  and users.user_id = urole.user_id  and urole.activation_status = 'AC'  and users.user_name = {userName}")
    Integer isStudentEditableByUser(String userName, Integer studentId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select 
     *      count(distinct stu.student_id)
     * from
     *      student stu,
     *      org_node_student ons,
     *      org_node_ancestor ona,
     *      user_role urole,
     *      users
     * where
     *      stu.student_id = {studentId}
     *      and ons.student_id = stu.student_id
     *      and ons.org_node_id = {orgNodeId}
     *      and ona.org_node_id = ons.org_node_id
     *      and urole.org_node_id = ona.ancestor_org_node_id
     *      and users.user_id = urole.user_id
     *      and stu.activation_status = 'AC'
     *      and ons.activation_status = 'AC'
     *      and urole.activation_status = 'AC'
     *      and users.user_name = {userName}::
     */
    @JdbcControl.SQL(statement = "select  count(distinct stu.student_id) from  student stu,  org_node_student ons,  org_node_ancestor ona,  user_role urole,  users where  stu.student_id = {studentId}  and ons.student_id = stu.student_id  and ons.org_node_id = {orgNodeId}  and ona.org_node_id = ons.org_node_id  and urole.org_node_id = ona.ancestor_org_node_id  and users.user_id = urole.user_id  and stu.activation_status = 'AC'  and ons.activation_status = 'AC'  and urole.activation_status = 'AC'  and users.user_name = {userName}")
    Integer isStudentEditableByUserForOrg(String userName, Integer studentId, Integer orgNodeId) throws SQLException;

    
    /**
     * @jc:sql statement::
     * select max(count) from (select 
     *      count(distinct stu.student_id) as count
     * from
     *      student stu,
     *      org_node_student ons,
     *      org_node_ancestor ona,
     *      user_role urole,
     *      users,
     *      test_roster roster
     * where
     *      stu.student_id = {studentId}
     *      and ons.student_id = stu.student_id
     *      and ona.org_node_id = ons.org_node_id
     *      and urole.org_node_id = ona.ancestor_org_node_id
     *      and users.user_id = urole.user_id
     *      and users.user_name = {userName}
     *      and roster.test_admin_id = {testAdminId}
     *      and roster.student_id = stu.student_id
     *      and roster.test_completion_status = 'SC'
     *      and roster.scoring_status = 'NA'
     *      and urole.activation_status = 'AC'
     * union
	 * select 
	 *     count(distinct stu.student_id) as count
	 * from
	 *     student stu,
	 *     org_node_student ons,
	 *     org_node_ancestor ona,
	 *     user_role urole,
	 *     users
	 * where
	 *     stu.student_id = {studentId}
     *     and ons.student_id = stu.student_id
	 *     and ona.org_node_id = ons.org_node_id
	 *     and urole.org_node_id = ona.ancestor_org_node_id
	 *     and users.user_id = urole.user_id
	 *     and users.user_name = {userName}
     *     and urole.activation_status = 'AC'
	 *    and 0 = (select count(*) from test_roster ros where
	 *     	   ros.test_admin_id = {testAdminId}
	 *    	   and ros.student_id = stu.student_id))::
     */
    @JdbcControl.SQL(statement = "select max(count) from (select  count(distinct stu.student_id) as count from  student stu,  org_node_student ons,  org_node_ancestor ona,  user_role urole,  users,  test_roster roster where  stu.student_id = {studentId}  and ons.student_id = stu.student_id  and ona.org_node_id = ons.org_node_id  and urole.org_node_id = ona.ancestor_org_node_id  and users.user_id = urole.user_id  and users.user_name = {userName}  and roster.test_admin_id = {testAdminId}  and roster.student_id = stu.student_id  and roster.test_completion_status = 'SC'  and roster.scoring_status = 'NA'  and urole.activation_status = 'AC' union select  count(distinct stu.student_id) as count from  student stu,  org_node_student ons,  org_node_ancestor ona,  user_role urole,  users where  stu.student_id = {studentId}  and ons.student_id = stu.student_id  and ona.org_node_id = ons.org_node_id  and urole.org_node_id = ona.ancestor_org_node_id  and users.user_id = urole.user_id  and users.user_name = {userName}  and urole.activation_status = 'AC'  and 0 = (select count(*) from test_roster ros where  \t  ros.test_admin_id = {testAdminId}  \t  and ros.student_id = stu.student_id))")
    Integer isStudentEditableByUserForAdmin(String userName, Integer studentId, Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * select max(count) from (select 
     *      count(distinct stu.student_id) as count
     * from
     *      student stu,
     *      org_node_student ons,
     *      org_node_ancestor ona,
     *      user_role urole,
     *      users,
     *      test_roster roster
     * where
     *      stu.student_id = {studentId}
     *      and ons.student_id = stu.student_id
     *      and ons.org_node_id = {orgNodeId}
     *      and ona.org_node_id = ons.org_node_id
     *      and urole.org_node_id = ona.ancestor_org_node_id
     *      and users.user_id = urole.user_id
     *      and users.user_name = {userName}
     *      and roster.test_admin_id = {testAdminId}
     *      and roster.student_id = stu.student_id
     *      and roster.test_completion_status in ('SC', 'NT')
     *      and urole.activation_status = 'AC'
     * union
	 * select 
	 *     count(distinct stu.student_id) as count
	 * from
	 *     student stu,
	 *     org_node_student ons,
	 *     org_node_ancestor ona,
	 *     user_role urole,
	 *     users
	 * where
	 *     stu.student_id = {studentId}
     *     and ons.student_id = stu.student_id
     *     and ons.org_node_id = {orgNodeId}
	 *     and ona.org_node_id = ons.org_node_id
	 *     and urole.org_node_id = ona.ancestor_org_node_id
	 *     and users.user_id = urole.user_id
	 *     and users.user_name = {userName}
     *     and urole.activation_status = 'AC'
	 *    and 0 = (select count(*) from test_roster ros where
	 *     	   ros.test_admin_id = {testAdminId}
	 *    	   and ros.student_id = stu.student_id))::
     */
    @JdbcControl.SQL(statement = "select max(count) from (select  count(distinct stu.student_id) as count from  student stu,  org_node_student ons,  org_node_ancestor ona,  user_role urole,  users,  test_roster roster where  stu.student_id = {studentId}  and ons.student_id = stu.student_id  and ons.org_node_id = {orgNodeId}  and ona.org_node_id = ons.org_node_id  and urole.org_node_id = ona.ancestor_org_node_id  and users.user_id = urole.user_id  and users.user_name = {userName}  and roster.test_admin_id = {testAdminId}  and roster.student_id = stu.student_id  and roster.test_completion_status in ('SC', 'NT')  and urole.activation_status = 'AC' union select  count(distinct stu.student_id) as count from  student stu,  org_node_student ons,  org_node_ancestor ona,  user_role urole,  users where  stu.student_id = {studentId}  and ons.student_id = stu.student_id  and ons.org_node_id = {orgNodeId}  and ona.org_node_id = ons.org_node_id  and urole.org_node_id = ona.ancestor_org_node_id  and users.user_id = urole.user_id  and users.user_name = {userName}  and urole.activation_status = 'AC'  and 0 = (select count(*) from test_roster ros where  \t  ros.test_admin_id = {testAdminId}  \t  and ros.student_id = stu.student_id))")
    Integer isStudentEditableByUserForAdminAndOrg(String userName, Integer studentId, Integer testAdminId, Integer orgNodeId) throws SQLException;


    /**
     * @jc:sql statement::
     * select distinct 
     *            stu.student_id as studentId, 
     *            stu.user_Name as userName, 
     *            stu.password as password, 
     *            stu.first_Name as firstName, 
     *            stu.middle_Name as middleName, 
     *            stu.last_Name as lastName, 
     *            stu.preferred_Name as preferredName, 
     *            stu.prefix as prefix, 
     *            stu.suffix as suffix, 
     *            stu.birthdate as birthdate, 
     *            stu.gender as gender, 
     *            stu.ethnicity as ethnicity, 
     *            stu.email as email, 
     *            stu.grade as grade, 
     *            stu.ext_Elm_Id as extElmId, 
     *            stu.ext_Pin1 as extPin1, 
     *            stu.ext_Pin2 as extPin2, 
     *            stu.ext_Pin3 as extPin3, 
     *            stu.ext_School_Id as extSchoolId, 
     *            stu.active_Session as activeSession, 
     *            stu.potential_Duplicated_Student as potentialDuplicatedStudent, 
     *            stu.created_By as createdBy, 
     *            stu.created_Date_Time as createdDateTime, 
     *            stu.updated_By as updatedBy, 
     *            stu.updated_Date_Time as updatedDateTime, 
     *            stu.activation_Status as activationStatus, 
     *            stu.data_import_history_id as dataImportHistoryId, 
     *            stu.grade as studentGrade, 
     *            node.org_node_name as orgNodeName, 
     *            node.org_node_id as orgNodeId, 
     * 	       accom.SCREEN_MAGNIFIER as screenMagnifier, 
     * 	       accom.SCREEN_READER as screenReader, 
     * 	       accom.CALCULATOR as calculator, 
     * 	       accom.TEST_PAUSE as testPause, 
     * 	       accom.UNTIMED_TEST as untimedTest, 
     * 	       accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor, 
     *  	       accom.QUESTION_FONT_COLOR as questionFontColor, 
     * 	       accom.QUESTION_FONT_SIZE as questionFontSize, 
     * 	       accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor, 
     * 	       accom.ANSWER_FONT_COLOR as answerFontColor, 
     * 	       accom.ANSWER_FONT_SIZE as answerFontSize, 
     * 		   accom.EXTENDED_TIME as extendedTimeAccom, 
     *            onc.category_name as orgNodeCategoryName, 
     * 		   NVL(priors.test_admin_id, -1) as priorAdmin
     *       from 
     * 	       org_node_student ons, 
     *            student stu, 
     *            student_accommodation accom, 
     *            org_node node, 
     *            org_node_category onc,
     * 		   (select 
     * 		   		   distinct ros.student_id, ros.test_admin_id
     * 		     from
     * 			 	 test_roster ros,
     * 				 student stu,
     * 				 test_admin adm,
     * 				 org_node_test_catalog ontc,
     * 				 user_role urole,
     * 				 users
     * 			 where
     * 			 	   ros.student_id = stu.student_id 
     * 	      	 	   and adm.test_admin_id = ros.test_admin_id 
     * 	      	 	   and adm.item_set_id = ontc.item_set_id 
     * 				   and urole.org_node_id = ontc.org_node_id
     *      			   and ontc.activation_status = 'AC'
     *      			   and urole.activation_status = 'AC'
     *      			   and users.user_id = urole.user_id
     *      			   and users.user_name = {userName}
     * 	      	 	   and ontc.item_set_id = {itemSetId} 
     * 			 	   and adm.test_admin_id != {testAdminId}
     * 				   and ontc.override_no_retake = 'T') priors
     *       where 
     *            ons.student_id = stu.student_id 
     *            and ons.org_node_id = {orgNodeId} 
     *            and ons.activation_status = 'AC' 
     *            and node.org_node_id = ons.org_node_id 
     *            and stu.activation_status = 'AC' 
     *            and accom.student_id (+) = stu.student_id
     *            and onc.org_node_category_id = node.org_node_category_id
     * 		   and priors.student_id (+) = stu.student_id::
     *  array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  stu.student_id as studentId,  stu.user_Name as userName,  stu.password as password,  stu.first_Name as firstName,  stu.middle_Name as middleName,  stu.last_Name as lastName,  stu.preferred_Name as preferredName,  stu.prefix as prefix,  stu.suffix as suffix,  stu.birthdate as birthdate,  stu.gender as gender,  stu.ethnicity as ethnicity,  stu.email as email,  stu.grade as grade,  stu.ext_Elm_Id as extElmId,  stu.ext_Pin1 as extPin1,  stu.ext_Pin2 as extPin2,  stu.ext_Pin3 as extPin3,  stu.ext_School_Id as extSchoolId,  stu.active_Session as activeSession,  stu.potential_Duplicated_Student as potentialDuplicatedStudent,  stu.created_By as createdBy,  stu.created_Date_Time as createdDateTime,  stu.updated_By as updatedBy,  stu.updated_Date_Time as updatedDateTime,  stu.activation_Status as activationStatus,  stu.data_import_history_id as dataImportHistoryId,  stu.grade as studentGrade,  node.org_node_name as orgNodeName,  node.org_node_id as orgNodeId,  \t  accom.SCREEN_MAGNIFIER as screenMagnifier,  \t  accom.SCREEN_READER as screenReader,  \t  accom.CALCULATOR as calculator,  \t  accom.TEST_PAUSE as testPause,  \t  accom.UNTIMED_TEST as untimedTest,  \t  accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor,  \t  accom.QUESTION_FONT_COLOR as questionFontColor,  \t  accom.QUESTION_FONT_SIZE as questionFontSize,  \t  accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor,  \t  accom.ANSWER_FONT_COLOR as answerFontColor,  \t  accom.ANSWER_FONT_SIZE as answerFontSize, accom.EXTENDED_TIME as extendedTimeAccom, onc.category_name as orgNodeCategoryName,  \t\t  NVL(priors.test_admin_id, -1) as priorAdmin  from  \t  org_node_student ons,  student stu,  student_accommodation accom,  org_node node,  org_node_category onc, \t\t  (select  \t\t  \t\t  distinct ros.student_id, ros.test_admin_id \t\t  from \t\t\t \t test_roster ros, \t\t\t\t student stu, \t\t\t\t test_admin adm, \t\t\t\t org_node_test_catalog ontc, \t\t\t\t user_role urole, \t\t\t\t users \t\t\t where \t\t\t \t  ros.student_id = stu.student_id  \t  \t \t  and adm.test_admin_id = ros.test_admin_id  \t  \t \t  and adm.item_set_id = ontc.item_set_id  \t\t\t\t  and urole.org_node_id = ontc.org_node_id  \t\t\t  and ontc.activation_status = 'AC'  \t\t\t  and urole.activation_status = 'AC'  \t\t\t  and users.user_id = urole.user_id  \t\t\t  and users.user_name = {userName} \t  \t \t  and ontc.item_set_id = {itemSetId}  \t\t\t \t  and adm.test_admin_id != {testAdminId} \t\t\t\t  and ontc.override_no_retake = 'T') priors  where  ons.student_id = stu.student_id  and ons.org_node_id = {orgNodeId}  and ons.activation_status = 'AC'  and node.org_node_id = ons.org_node_id  and stu.activation_status = 'AC'  and accom.student_id (+) = stu.student_id  and onc.org_node_category_id = node.org_node_category_id \t\t  and priors.student_id (+) = stu.student_id",
                     arrayMaxLength = 100000)
    SessionStudent [] getSchedulingStudentsForOrgNode(Integer orgNodeId, String userName, Integer itemSetId, Integer testAdminId) throws SQLException;

    @JdbcControl.SQL(statement = "select studentId, userName, password, firstName,  middleName, lastName, preferredName, prefix, suffix, birthdate, gender, ethnicity, email, grade, extElmId, extPin1, extPin2, extPin3, extSchoolId, activeSession, potentialDuplicatedStudent, createdBy, createdDateTime, updatedBy, updatedDateTime, activationStatus, dataImportHistoryId, studentGrade, orgNodeName, orgNodeId, screenMagnifier,  screenReader, calculator, testPause, untimedTest, questionBackgroundColor, questionFontColor, questionFontSize, answerBackgroundColor, answerFontColor, answerFontSize, extendedTimeAccom, orgNodeCategoryName,  priorAdmin from ( select distinct  stu.student_id as studentId,  stu.user_Name as userName,  stu.password as password,  stu.first_Name as firstName,  stu.middle_Name as middleName,  stu.last_Name as lastName,  stu.preferred_Name as preferredName,  stu.prefix as prefix,  stu.suffix as suffix,  stu.birthdate as birthdate,  stu.gender as gender,  stu.ethnicity as ethnicity,  stu.email as email,  stu.grade as grade,  stu.ext_Elm_Id as extElmId,  stu.ext_Pin1 as extPin1,  stu.ext_Pin2 as extPin2,  stu.ext_Pin3 as extPin3,  stu.ext_School_Id as extSchoolId,  stu.active_Session as activeSession,  stu.potential_Duplicated_Student as potentialDuplicatedStudent,  stu.created_By as createdBy,  stu.created_Date_Time as createdDateTime,  stu.updated_By as updatedBy,  stu.updated_Date_Time as updatedDateTime,  stu.activation_Status as activationStatus,  stu.data_import_history_id as dataImportHistoryId,  stu.grade as studentGrade,  node.org_node_name as orgNodeName,  node.org_node_id as orgNodeId,    accom.SCREEN_MAGNIFIER as screenMagnifier,    accom.SCREEN_READER as screenReader,    accom.CALCULATOR as calculator,    accom.TEST_PAUSE as testPause,    accom.UNTIMED_TEST as untimedTest,    accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor,    accom.QUESTION_FONT_COLOR as questionFontColor,    accom.QUESTION_FONT_SIZE as questionFontSize,    accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor,    accom.ANSWER_FONT_COLOR as answerFontColor,    accom.ANSWER_FONT_SIZE as answerFontSize, accom.EXTENDED_TIME as extendedTimeAccom, onc.category_name as orgNodeCategoryName,    NVL(priors.test_admin_id, -1) as priorAdmin, decode ( accom.QUESTION_BACKGROUND_COLOR , null, 'f', decode(accom.QUESTION_FONT_COLOR , null, 'f', decode(accom.QUESTION_FONT_SIZE , null, 'f', decode( accom.ANSWER_BACKGROUND_COLOR , null, 'f', decode( accom.ANSWER_FONT_COLOR , null, 'f', decode(  accom.ANSWER_FONT_SIZE , null, 'f', 't' )  ) ))) ) hasColorFontAccommodations  from    org_node_student ons,  student stu,  student_accommodation accom,  org_node node,  org_node_category onc,   (select      distinct ros.student_id, ros.test_admin_id   from   test_roster ros,  student stu,  test_admin adm,  org_node_test_catalog ontc,  user_role urole,  users  where    ros.student_id = stu.student_id       and adm.test_admin_id = ros.test_admin_id       and adm.item_set_id = ontc.item_set_id    and urole.org_node_id = ontc.org_node_id    and ontc.activation_status = 'AC'    and urole.activation_status = 'AC'    and users.user_id = urole.user_id    and users.user_name = {userName}      and ontc.item_set_id = {itemSetId}     and adm.test_admin_id != {testAdminId}   and ontc.override_no_retake = 'T') priors  where  ons.student_id = stu.student_id  and ons.org_node_id = {orgNodeId}  and ons.activation_status = 'AC'  and node.org_node_id = ons.org_node_id  and stu.activation_status = 'AC'  and accom.student_id (+) = stu.student_id  and onc.org_node_category_id = node.org_node_category_id   and priors.student_id (+) = stu.student_id)  {sql: searchOrder}",
    		arrayMaxLength = 100000)
    SessionStudent [] getSchedulingStudentsForOrgNodeByOrder(Integer orgNodeId, String userName, Integer itemSetId, Integer testAdminId,  String searchOrder) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct
     *      stu.student_id as studentId,
     *      stu.user_Name as userName,
     *      stu.password as password,
     *      stu.first_Name as firstName,
     *      stu.middle_Name as middleName,
     *      stu.last_Name as lastName,
     *      stu.preferred_Name as preferredName,
     *      stu.prefix as prefix,
     *      stu.suffix as suffix,
     *      stu.birthdate as birthdate,
     *      stu.gender as gender,
     *      stu.ethnicity as ethnicity,
     *      stu.email as email,
     *      stu.grade as grade,
     *      stu.ext_Elm_Id as extElmId,
     *      stu.ext_Pin1 as extPin1,
     *      stu.ext_Pin2 as extPin2,
     *      stu.ext_Pin3 as extPin3,
     *      stu.ext_School_Id as extSchoolId,
     *      stu.active_Session as activeSession,
     *      stu.potential_Duplicated_Student as potentialDuplicatedStudent,
     *      stu.created_By as createdBy,
     *      stu.created_Date_Time as createdDateTime,
     *      stu.updated_By as updatedBy,
     *      stu.updated_Date_Time as updatedDateTime,
     *      stu.activation_Status as activationStatus,
     *      stu.data_import_history_id as dataImportHistoryId,
     *      stu.grade as studentGrade,
	 *      accom.SCREEN_MAGNIFIER as screenMagnifier,
	 *      accom.SCREEN_READER as screenReader,
	 *      accom.CALCULATOR as calculator,
	 *      accom.TEST_PAUSE as testPause,
	 *      accom.UNTIMED_TEST as untimedTest,
	 *      accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor,
 	 *      accom.QUESTION_FONT_COLOR as questionFontColor,
	 *      accom.QUESTION_FONT_SIZE as questionFontSize,
	 *      accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor,
	 *      accom.ANSWER_FONT_COLOR as answerFontColor,
	 *      accom.ANSWER_FONT_SIZE as answerFontSize,
     *      node.org_node_name as orgNodeName,
     *      node.org_node_id as orgNodeId,
     *      ros.form_assignment as itemSetForm,
     *      decode(ros.test_completion_status, 'SC', 'F', 'T') as tested,
     *      onc.category_name as orgNodeCategoryName
     * from
	 *      org_node_student ons,
     *      student stu,
     *      student_accommodation accom,
     *      test_roster ros,
     *      org_node node,
     *      org_node_category onc
     * where
     *      ons.student_id = stu.student_id
     *      and ons.org_node_id = {orgNodeId}
     *      and accom.student_id (+) = stu.student_id
     *      and ros.student_id = stu.student_id
     *      and ros.test_Admin_id = {testAdminId}
     *      and node.org_node_id = ros.org_node_id
     *      and onc.org_node_category_id = node.org_node_category_id::
     * array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  stu.student_id as studentId,  stu.user_Name as userName,  stu.password as password,  stu.first_Name as firstName,  stu.middle_Name as middleName,  stu.last_Name as lastName,  stu.preferred_Name as preferredName,  stu.prefix as prefix,  stu.suffix as suffix,  stu.birthdate as birthdate,  stu.gender as gender,  stu.ethnicity as ethnicity,  stu.email as email,  stu.grade as grade,  stu.ext_Elm_Id as extElmId,  stu.ext_Pin1 as extPin1,  stu.ext_Pin2 as extPin2,  stu.ext_Pin3 as extPin3,  stu.ext_School_Id as extSchoolId,  stu.active_Session as activeSession,  stu.potential_Duplicated_Student as potentialDuplicatedStudent,  stu.created_By as createdBy,  stu.created_Date_Time as createdDateTime,  stu.updated_By as updatedBy,  stu.updated_Date_Time as updatedDateTime,  stu.activation_Status as activationStatus,  stu.data_import_history_id as dataImportHistoryId,  stu.grade as studentGrade,  accom.SCREEN_MAGNIFIER as screenMagnifier,  accom.SCREEN_READER as screenReader,  accom.CALCULATOR as calculator,  accom.TEST_PAUSE as testPause,  accom.UNTIMED_TEST as untimedTest,  accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor,  accom.QUESTION_FONT_COLOR as questionFontColor,  accom.QUESTION_FONT_SIZE as questionFontSize,  accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor,  accom.ANSWER_FONT_COLOR as answerFontColor,  accom.ANSWER_FONT_SIZE as answerFontSize,  node.org_node_name as orgNodeName,  node.org_node_id as orgNodeId,  ros.form_assignment as itemSetForm,  decode(ros.test_completion_status, 'SC', 'F', 'T') as tested,  onc.category_name as orgNodeCategoryName from  org_node_student ons,  student stu,  student_accommodation accom,  test_roster ros,  org_node node,  org_node_category onc where  ons.student_id = stu.student_id  and ons.org_node_id = {orgNodeId}  and accom.student_id (+) = stu.student_id  and ros.student_id = stu.student_id  and ros.test_Admin_id = {testAdminId}  and node.org_node_id = ros.org_node_id  and onc.org_node_category_id = node.org_node_category_id",
                     arrayMaxLength = 100000)
    SessionStudent [] getSessionStudentsForOrgNode(Integer orgNodeId, Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct
     *      stu.student_id as studentId,
     *      stu.user_Name as userName,
     *      ros.password as password,
     *      stu.first_Name as firstName,
     *      stu.middle_Name as middleName,
     *      stu.last_Name as lastName,
     *      stu.preferred_Name as preferredName,
     *      stu.prefix as prefix,
     *      stu.suffix as suffix,
     *      stu.birthdate as birthdate,
     *      stu.gender as gender,
     *      stu.ethnicity as ethnicity,
     *      stu.email as email,
     *      stu.grade as grade,
     *      stu.ext_Elm_Id as extElmId,
     *      stu.ext_Pin1 as extPin1,
     *      stu.ext_Pin2 as extPin2,
     *      stu.ext_Pin3 as extPin3,
     *      stu.ext_School_Id as extSchoolId,
     *      stu.active_Session as activeSession,
     *      stu.potential_Duplicated_Student as potentialDuplicatedStudent,
     *      stu.created_By as createdBy,
     *      stu.created_Date_Time as createdDateTime,
     *      stu.updated_By as updatedBy,
     *      stu.updated_Date_Time as updatedDateTime,
     *      stu.activation_Status as activationStatus,
     *      stu.data_import_history_id as dataImportHistoryId,
     *      stu.grade as studentGrade,
     *      accom.SCREEN_MAGNIFIER as screenMagnifier,
     *      accom.SCREEN_READER as screenReader,
     *      accom.CALCULATOR as calculator,
     *      accom.TEST_PAUSE as testPause,
     *      accom.UNTIMED_TEST as untimedTest,
     *      accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor,
     *      accom.QUESTION_FONT_COLOR as questionFontColor,
     *      accom.QUESTION_FONT_SIZE as questionFontSize,
     *      accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor,
     *      accom.ANSWER_FONT_COLOR as answerFontColor,
     *      accom.ANSWER_FONT_SIZE as answerFontSize,
     *      accom.HIGHLIGHTER as highLighter,
     *      node.org_node_name as orgNodeName,
     *      node.org_node_id as orgNodeId,
     *      ros.form_assignment as itemSetForm,
     *      ros.test_completion_status as testCompletionStatus,
     *      ros.scoring_status as scoringStatus,
     *      decode(decode(ros.test_completion_status, 'NT', 'SC', ros.test_completion_status), 'SC', 'F', 'T') as tested,
     *      onc.category_name as orgNodeCategoryName
     * from
     *      student stu,
     *      student_accommodation accom,
     *      test_roster ros,
     *      org_node node,
     *      org_node_category onc
     * where
     *      accom.student_id (+) = stu.student_id
     *      and ros.student_id = stu.student_id
     *      and ros.test_Admin_id = {testAdminId}
     *      and node.org_node_id = ros.org_node_id
     *      and onc.org_node_category_id = node.org_node_category_id
     *      and (stu.activation_Status = 'AC' or decode(decode(ros.test_completion_status, 'NT', 'SC', ros.test_completion_status), 'SC', 'F', 'T') = 'T')::
     * array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  stu.student_id as studentId,  stu.user_Name as userName,  ros.password as password,  stu.first_Name as firstName,  stu.middle_Name as middleName,  stu.last_Name as lastName,  stu.preferred_Name as preferredName,  stu.prefix as prefix,  stu.suffix as suffix,  stu.birthdate as birthdate,  stu.gender as gender,  stu.ethnicity as ethnicity,  stu.email as email,  stu.grade as grade,  stu.ext_Elm_Id as extElmId,  stu.ext_Pin1 as extPin1,  stu.ext_Pin2 as extPin2,  stu.ext_Pin3 as extPin3,  stu.ext_School_Id as extSchoolId,  stu.active_Session as activeSession,  stu.potential_Duplicated_Student as potentialDuplicatedStudent,  stu.created_By as createdBy,  stu.created_Date_Time as createdDateTime,  stu.updated_By as updatedBy,  stu.updated_Date_Time as updatedDateTime,  stu.activation_Status as activationStatus,  stu.data_import_history_id as dataImportHistoryId,  stu.grade as studentGrade,  accom.SCREEN_MAGNIFIER as screenMagnifier,  accom.SCREEN_READER as screenReader,  accom.CALCULATOR as calculator,  accom.TEST_PAUSE as testPause,  accom.UNTIMED_TEST as untimedTest,  accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor,  accom.QUESTION_FONT_COLOR as questionFontColor,  accom.QUESTION_FONT_SIZE as questionFontSize,  accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor,  accom.ANSWER_FONT_COLOR as answerFontColor,  accom.ANSWER_FONT_SIZE as answerFontSize,  accom.HIGHLIGHTER as highLighter, accom.masking_ruler as maskingRular,   accom.masking_tool as maskingTool, accom.magnifying_glass as magnifyingGlass,  accom.music_file_id as musicFileId, ros.EXTENDED_TIME as extendedTimeAccom, node.org_node_name as orgNodeName,  node.org_node_id as orgNodeId,  ros.form_assignment as itemSetForm,  ros.test_completion_status as testCompletionStatus,  ros.scoring_status as scoringStatus,  decode(decode(ros.test_completion_status, 'NT', 'SC', ros.test_completion_status), 'SC', 'F', 'T') as tested,  onc.category_name as orgNodeCategoryName from  student stu,  student_accommodation accom,  test_roster ros,  org_node node,  org_node_category onc where  accom.student_id (+) = stu.student_id  and ros.student_id = stu.student_id  and ros.test_Admin_id = {testAdminId}  and node.org_node_id = ros.org_node_id  and onc.org_node_category_id = node.org_node_category_id  and (stu.activation_Status = 'AC' or decode(decode(ros.test_completion_status, 'NT', 'SC', ros.test_completion_status), 'SC', 'F', 'T') = 'T')",
                     arrayMaxLength = 100000)
    SessionStudent [] getSessionStudentsForAdmin(Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct
     * 	  decode (ontc.override_no_retake, 'T' , adm.test_admin_id, -1)
     * from
     * 	  users,
     * 	  user_role urole,
     * 	  org_node_test_catalog ontc,
     * 	  test_admin adm,
     * 	  test_roster ros
     * where
     * 	 ros.student_id = {studentId}
     * 	 and adm.test_admin_id = ros.test_admin_id
     * 	 and adm.item_set_id = ontc.item_set_id
     * 	 and urole.org_node_id = ontc.org_node_id
     * 	 and ontc.activation_status = 'AC'
     * 	 and urole.activation_status = 'AC'
     * 	 and users.user_id = urole.user_id
     * 	 and users.user_name = {userName}
     * 	 and ontc.item_set_id = {testItemSetId}::
     * array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct \t  decode (ontc.override_no_retake, 'T' , adm.test_admin_id, -1) from \t  users, \t  user_role urole, \t  org_node_test_catalog ontc, \t  test_admin adm, \t  test_roster ros where \t ros.student_id = {studentId} \t and adm.test_admin_id = ros.test_admin_id \t and adm.item_set_id = ontc.item_set_id \t and urole.org_node_id = ontc.org_node_id \t and ontc.activation_status = 'AC' \t and urole.activation_status = 'AC' \t and users.user_id = urole.user_id \t and users.user_name = {userName} \t and ontc.item_set_id = {testItemSetId}",
                     arrayMaxLength = 100000)
    Integer isTestRestrictedForStudent(String userName, Integer studentId, Integer testItemSetId) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     * 	  decode (ontc.override_no_retake, 'T' , adm.test_Admin_id, -1)
     * from
     * 	  users,
     * 	  user_role urole,
     * 	  org_node_test_catalog ontc,
     * 	  test_admin adm,
     * 	  test_roster ros
     * where
     * 	 ros.student_id = {studentId}
     * 	 and adm.test_admin_id = ros.test_admin_id
     * 	 and adm.item_set_id = ontc.item_set_id
     * 	 and urole.org_node_id = ontc.org_node_id
     * 	 and ontc.activation_status = 'AC'
     * 	 and urole.activation_status = 'AC'
     * 	 and users.user_id = urole.user_id
     * 	 and users.user_name = {userName}
     * 	 and ontc.item_set_id = {testItemSetId}
     * 	 and adm.test_admin_id != {testAdminId}::
     * array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select \t  decode (ontc.override_no_retake, 'T' , adm.test_Admin_id, -1) from \t  users, \t  user_role urole, \t  org_node_test_catalog ontc, \t  test_admin adm, \t  test_roster ros where \t ros.student_id = {studentId} \t and adm.test_admin_id = ros.test_admin_id \t and adm.item_set_id = ontc.item_set_id \t and urole.org_node_id = ontc.org_node_id \t and ontc.activation_status = 'AC' \t and urole.activation_status = 'AC' \t and users.user_id = urole.user_id \t and users.user_name = {userName} \t and ontc.item_set_id = {testItemSetId} \t and adm.test_admin_id != {testAdminId}",
                     arrayMaxLength = 100000)
    Integer isTestRestrictedForStudentAndAdmin(String userName, Integer studentId, Integer testItemSetId, Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     * 	decode(count(ona.org_node_id), 0, 'false', 'true') as visible
     * from
     * 	org_node_ancestor ona,
     * 	user_role urole,
     * 	users,
     * 	org_node_student ons,
     * 	student stu
     * where
     * 	 users.user_name = {userName}
     * 	 and urole.user_id = users.user_id
     * 	 and ona.ancestor_org_node_id = urole.org_node_id
     * 	 and ona.org_node_id = ons.org_node_id
     * 	 and ons.student_id = stu.student_id
     * 	 and ons.activation_status = 'AC'
     * 	 and stu.activation_status = 'AC'
     * 	 and ons.student_id = {studentId}
     * 	 
     * 	 ::
     */
    @JdbcControl.SQL(statement = "select  \tdecode(count(ona.org_node_id), 0, 'false', 'true') as visible from \torg_node_ancestor ona, \tuser_role urole, \tusers, \torg_node_student ons, \tstudent stu where \t users.user_name = {userName} \t and urole.user_id = users.user_id \t and ona.ancestor_org_node_id = urole.org_node_id \t and ona.org_node_id = ons.org_node_id \t and ons.student_id = stu.student_id \t and ons.activation_status = 'AC' \t and stu.activation_status = 'AC' \t and ons.student_id = {studentId} \t ")
    String checkVisibility(String userName, Integer studentId) throws SQLException;

    /**
     * @jc:sql statement::
     * select seq_student_id.nextval from dual
     */
    @JdbcControl.SQL(statement = "select seq_student_id.nextval from dual")
    Integer getNextPK() throws SQLException;
    
    /**
     * @jc:sql statement::
     * insert into
     *     student (
     * 		STUDENT_ID,
     * 		USER_NAME,
     * 		PASSWORD,
     * 		FIRST_NAME,
     * 		MIDDLE_NAME,
     * 		PREFERRED_NAME,
     * 		LAST_NAME,
     * 		BIRTHDATE,
     * 		ETHNICITY,
     * 		GENDER,
     * 		EMAIL,
     * 		GRADE,
     * 		EXT_PIN1,
     * 		EXT_ELM_ID,
     * 		EXT_PIN2,
     * 		EXT_PIN3,
     * 		CREATED_DATE_TIME,
     * 		ACTIVE_SESSION,
     * 		ACTIVATION_STATUS,
     * 		POTENTIAL_DUPLICATED_STUDENT,
     * 		CREATED_BY,
     * 		UPDATED_BY,
     * 		UPDATED_DATE_TIME,
     * 		EXT_SCHOOL_ID,
     * 		PREFIX,
     * 		SUFFIX,
     * 		DATA_IMPORT_HISTORY_ID,
     * 		BARCODE,
     * 		CUSTOMER_ID,
     * 		PRECODE_ID,
     * 		UDF,
     * 		UDF_1,
     * 		UDF_2
     *      ) values (
     * 		{student.studentId},
     * 		{student.userName},
     * 		{student.password},
     * 		{student.firstName},
     * 		{student.middleName},
     * 		{student.preferredName},
     * 		{student.lastName},
     * 		{student.birthdate},
     * 		{student.ethnicity},
     * 		{student.gender},
     * 		{student.email},
     * 		{student.grade},
     * 		{student.extPin1},
     * 		{student.extElmId},
     * 		{student.extPin2},
     * 		{student.extPin3},
     * 		{student.createdDateTime},
     * 		{student.activeSession},
     * 		{student.activationStatus},
     * 		{student.potentialDuplicatedStudent},
     * 		{student.createdBy},
     * 		{student.updatedBy},
     * 		{student.updatedDateTime},
     * 		{student.extSchoolId},
     * 		{student.prefix},
     * 		{student.suffix},
     * 		{student.dataImportHistoryId},
     * 		{student.barcode},
     * 		{student.customerId},
     * 		{student.precodeId},
     * 		{student.udf},
     * 		{student.udf1},
     * 		{student.udf2}
     * 	 
     *      )::
     */
    @JdbcControl.SQL(statement = "insert into  student ( \t\tSTUDENT_ID, \t\tUSER_NAME, \t\tPASSWORD, \t\tFIRST_NAME, \t\tMIDDLE_NAME, \t\tPREFERRED_NAME, \t\tLAST_NAME, \t\tBIRTHDATE, \t\tETHNICITY, \t\tGENDER, \t\tEMAIL, \t\tGRADE, \t\tEXT_PIN1, \t\tEXT_ELM_ID, \t\tEXT_PIN2, \t\tEXT_PIN3, \t\tCREATED_DATE_TIME, \t\tACTIVE_SESSION, \t\tACTIVATION_STATUS, \t\tPOTENTIAL_DUPLICATED_STUDENT, \t\tCREATED_BY, \t\tUPDATED_BY, \t\tUPDATED_DATE_TIME, \t\tEXT_SCHOOL_ID, \t\tPREFIX, \t\tSUFFIX, \t\tDATA_IMPORT_HISTORY_ID, \t\tBARCODE, \t\tCUSTOMER_ID, \t\tPRECODE_ID, \t\tUDF, \t\tUDF_1, \t\tUDF_2, TEST_PURPOSE) values ( \t\t{student.studentId}, \t\t{student.userName}, \t\t{student.password}, \t\t{student.firstName}, \t\t{student.middleName}, \t\t{student.preferredName}, \t\t{student.lastName}, \t\t{student.birthdate}, \t\t{student.ethnicity}, \t\t{student.gender}, \t\t{student.email}, \t\t{student.grade}, \t\t{student.extPin1}, \t\t{student.extElmId}, \t\t{student.extPin2}, \t\t{student.extPin3}, \t\t{student.createdDateTime}, \t\t{student.activeSession}, \t\t{student.activationStatus}, \t\t{student.potentialDuplicatedStudent}, \t\t{student.createdBy}, \t\t{student.updatedBy}, \t\t{student.updatedDateTime}, \t\t{student.extSchoolId}, \t\t{student.prefix}, \t\t{student.suffix}, \t\t{student.dataImportHistoryId}, \t\t{student.barcode}, \t\t{student.customerId}, \t\t{student.precodeId}, \t\t{student.udf}, \t\t{student.udf1}, \t\t{student.udf2}, {student.testPurpose} )")
    void createNewStudent(Student student) throws SQLException;
   
    /**
     * @jc:sql statement::
     * delete from 
     *      student 
     * where 
     *      student_id = {studentId}
     *      and not exists (
     *          select * 
     *          from 
     *              test_roster 
     *          where 
     *              student_id = {studentId})
     * ::
     */
    @JdbcControl.SQL(statement = "delete from  student  where  student_id = {studentId}  and not exists (  select *  from  test_roster  where  student_id = {studentId})")
    void deleteStudent(Integer studentId) throws SQLException;

    /**
     * @jc:sql statement::
     * update student
     *      set activation_status = 'IN' 
     * where 
     *      student_id = {studentId}::
     */
	@JdbcControl.SQL(statement = "update student  set activation_status = 'IN'  where  student_id = {studentId}")
    void deactivateStudent(Integer studentId) throws SQLException;
    
	/**
     * @jc:sql statement::
     * select 
     * 		seq_student_login_id.nextval
     * from 
     * 		dual
     */
	@JdbcControl.SQL(statement = "select seq_student_login_id.nextval from dual")
    String getStudentLoginIdSequence() throws SQLException;
    
	/**
     * @jc:sql statement::
	 * select distinct  stu.student_id as studentId,
	 * stu.user_name as userName,  
	 * stu.first_name as firstName, 
	 * stu.middle_name as middleName,  
	 * stu.last_name as lastName,   
	 * concat(concat(stu.last_name, ', '), 
	 * concat(stu.first_name, concat(' ', stu.MIDDLE_NAME))) as preferredName,  
	 * stu.gender as gender, stu.birthdate as birthdate,  
	 * stu.grade as grade,  
	 * stu.ext_pin1 as extPin1,  
	 * stu.ext_pin2 as extPin2,
	 * stu.created_by as createdBy, 
	 * stu.customer_id as customerId 
	 * 
	 * from org_node_student ons, 
	 * student stu, org_node_ancestor ona, 
	 * user_role urole, 
	 * users usr 
	 * 
	 * where upper(stu.user_name) = upper({studentUserName}) and 
	 * stu.activation_status = 'AC' and 
	 * stu.student_id = ons.student_id and 
	 * ons.activation_status = 'AC' and 
	 * ons.org_node_id = ona.org_node_id and 
	 * ona.ANCESTOR_ORG_NODE_ID = urole.org_node_id and 
	 * urole.activation_Status = 'AC' and 
	 * urole.user_id = usr.user_id and 
	 * usr.user_name = {loginUserName} 
	 * 
	 * */   
	
	@JdbcControl.SQL(statement= " select distinct  stu.student_id as studentId, stu.user_name as userName,  stu.first_name as firstName, stu.middle_name as middleName,  stu.last_name as lastName,   concat(concat(stu.last_name, ', '), concat(stu.first_name, concat(' ', stu.MIDDLE_NAME))) as preferredName,  stu.gender as gender, stu.birthdate as birthdate,  stu.grade as grade,  stu.ext_pin1 as extPin1,  stu.ext_pin2 as extPin2, stu.created_by as createdBy, stu.customer_id as customerId from org_node_student ons, student stu, org_node_ancestor ona, user_role urole, users usr where upper(stu.user_name) = upper({studentUserName}) and stu.activation_status = 'AC' and stu.student_id = ons.student_id and ons.activation_status = 'AC' and ons.org_node_id = ona.org_node_id and ona.ANCESTOR_ORG_NODE_ID = urole.org_node_id and urole.activation_Status = 'AC' and urole.user_id = usr.user_id and usr.user_name = {loginUserName} ")
	Student getStudentDetail(String loginUserName,String studentUserName) throws SQLException;
	
	/**
     * @jc:sql statement::
     * update student
     *      set active_session = 'F' 
     * where 
     *      student_id = {studentId}::
     */
	@JdbcControl.SQL(statement = "update student set active_session = 'F' ,updated_by = {updatedBy} , updated_date_time = {completedDateTime} where student_id = {studentId}")
    void updateStudentActiveSessionFlag ( java.lang.Integer studentId,java.util.Date completedDateTime, java.lang.Integer updatedBy) throws SQLException;
   
	
	/*
     * start changes for CR - ISTEP-33
     * 
    */
   
	/**
     * @jc:sql statement::
	 * select 
	 * stu.customer_id as customerId 
	 * 
	 * from 
	 * student stu
	 * 
	 * where upper(stu.user_name) = upper({UserName}) 
	 * 
	 * */   
	
	@JdbcControl.SQL(statement= " select stu.customer_id as customerId from student stu where upper(stu.user_name) = upper({UserName}) ")
	Student getStudentCustomer(String UserName) throws SQLException;
	
	
	/*
     * end changes for CR - ISTEP-33
     * 
    */
    
    /**
     * @jc:sql statement::
	 * SELECT
	 * DECODE(COUNT(1), 0, 'T', 'F') AS UNIQUESTUDENTID
	 * FROM STUDENT STU 
	 * WHERE trim(STU.EXT_PIN1) = {studentId} 
	 * AND STU.CUSTOMER_ID = {customerId} AND STU.ACTIVATION_STATUS = 'AC'" 
	 *  
	 * */   
	
	//Changes for TABE BAUM - Unique Student Id
	@JdbcControl.SQL(statement = "SELECT DECODE(COUNT(1), 0, 'T', 'F') AS UNIQUESTUDENTID  FROM STUDENT STU WHERE trim(STU.EXT_PIN1) = {studentId} AND STU.CUSTOMER_ID = {customerId} AND STU.ACTIVATION_STATUS = 'AC'") // change for defect - 66238
    String checkUniqueStudentId(String studentId, Integer customerId) throws SQLException;
	
	static final long serialVersionUID = 1L;


}