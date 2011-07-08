package com.ctb.control.db; 

import com.bea.control.*;
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import com.ctb.bean.testAdmin.StudentAccommodations; 
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
public interface StudentAccommodation extends JdbcControl
{ 
    /**
     * @jc:sql statement::
     * select 
     *      distinct ona.org_node_id as orgNodeId, 
	 *      stu.STUDENT_ID as studentId,
	 *      stu.grade as studentGrade,
	 *      accom.SCREEN_MAGNIFIER as screenMagnifier,
	 *      accom.SCREEN_READER as screenReader,
	 *      accom.CALCULATOR as calculator,
	 *      accom.TEST_PAUSE as testPause,
	 *      accom.UNTIMED_TEST as untimedTest,
     *      accom.HIGHLIGHTER as highlighter,
	 *      accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor,
 	 *      accom.QUESTION_FONT_COLOR as questionFontColor,
	 *      accom.QUESTION_FONT_SIZE as questionFontSize,
	 *      accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor,
	 *      accom.ANSWER_FONT_COLOR as answerFontColor,
	 *      accom.ANSWER_FONT_SIZE as answerFontSize,
     * from
	 *      student_accommodation accom,
	 *      student stu,
	 *      org_node_ancestor ona,
     *      org_node_ancestor ona2,
	 *      org_node_student ons
     * where
	 *      accom.student_id (+) = stu.student_id
	 *      and stu.student_id = ons.student_id
     *      and stu.activation_status = 'AC'
     *      and ons.activation_status = 'AC'
	 *      and ons.org_node_id = ona2.org_node_id
	 *      and ona2.ancestor_org_node_id = ona.org_node_id
     *      and ona.ancestor_org_node_id = {orgNodeId}::
     * array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  distinct ona.org_node_id as orgNodeId,  stu.STUDENT_ID as studentId,  stu.grade as studentGrade,  accom.SCREEN_MAGNIFIER as screenMagnifier,  accom.SCREEN_READER as screenReader,  accom.CALCULATOR as calculator,  accom.TEST_PAUSE as testPause,  accom.UNTIMED_TEST as untimedTest,  accom.HIGHLIGHTER as highlighter,  accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor,  accom.QUESTION_FONT_COLOR as questionFontColor,  accom.QUESTION_FONT_SIZE as questionFontSize,  accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor,  accom.ANSWER_FONT_COLOR as answerFontColor,  accom.ANSWER_FONT_SIZE as answerFontSize from  student_accommodation accom,  student stu,  org_node_ancestor ona,  org_node_ancestor ona2,  org_node_student ons where  accom.student_id (+) = stu.student_id  and stu.student_id = ons.student_id  and stu.activation_status = 'AC'  and ons.activation_status = 'AC'  and ons.org_node_id = ona2.org_node_id  and ona2.ancestor_org_node_id = ona.org_node_id  and ona.ancestor_org_node_id = {orgNodeId}",
                     arrayMaxLength = 100000)
    StudentAccommodations [] getStudentAccommodationsByAncestorNode(Integer orgNodeId) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct 
	 *      stu.grade as studentGrade,
	 *      accom.SCREEN_MAGNIFIER as screenMagnifier,
	 *      accom.SCREEN_READER as screenReader,
	 *      accom.CALCULATOR as calculator,
	 *      accom.TEST_PAUSE as testPause,
	 *      accom.UNTIMED_TEST as untimedTest,
     *      accom.HIGHLIGHTER as highlighter,
	 *      accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor,
 	 *      accom.QUESTION_FONT_COLOR as questionFontColor,
	 *      accom.QUESTION_FONT_SIZE as questionFontSize,
	 *      accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor,
	 *      accom.ANSWER_FONT_COLOR as answerFontColor,
	 *      accom.ANSWER_FONT_SIZE as answerFontSize,
     * from
	 *      student_accommodation accom,
	 *      org_node_ancestor ona,
	 *      org_node_student ons,
     *      user_role urole,
     *      users,
     *      student stu
     * where
     *      urole.user_id = users.user_id
     *      and accom.student_id (+) = stu.student_id
     *      and users.user_name = {userName}
	 *      and stu.student_id = ons.student_id
     *      and stu.activation_status = 'AC'
     *      and ons.activation_status = 'AC'
	 *      and ons.org_node_id = ona.org_node_id
	 *      and ona.ancestor_org_node_id = urole.org_node_id::
     * array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  stu.grade as studentGrade,  accom.SCREEN_MAGNIFIER as screenMagnifier,  accom.SCREEN_READER as screenReader,  accom.CALCULATOR as calculator,  accom.TEST_PAUSE as testPause,  accom.UNTIMED_TEST as untimedTest,  accom.HIGHLIGHTER as highlighter,  accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor,  accom.QUESTION_FONT_COLOR as questionFontColor,  accom.QUESTION_FONT_SIZE as questionFontSize,  accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor,  accom.ANSWER_FONT_COLOR as answerFontColor,  accom.ANSWER_FONT_SIZE as answerFontSize from  student_accommodation accom,  org_node_ancestor ona,  org_node_student ons,  user_role urole,  users,  student stu where  urole.user_id = users.user_id  and accom.student_id (+) = stu.student_id  and users.user_name = {userName}  and stu.student_id = ons.student_id  and stu.activation_status = 'AC'  and ons.activation_status = 'AC'  and ons.org_node_id = ona.org_node_id  and ona.ancestor_org_node_id = urole.org_node_id",
                     arrayMaxLength = 100000)
    StudentAccommodations [] getUniqueStudentAccommodationsForUser(String userName) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     *      distinct stu.STUDENT_ID as studentId,
     *      stu.grade as studentGrade,
     *      accom.SCREEN_MAGNIFIER as screenMagnifier,
     *      accom.SCREEN_READER as screenReader,
     *      accom.CALCULATOR as calculator,
     *      accom.TEST_PAUSE as testPause,
     *      accom.UNTIMED_TEST as untimedTest,
     *      accom.HIGHLIGHTER as highlighter,
     *      accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor,
     *      accom.QUESTION_FONT_COLOR as questionFontColor,
     *      accom.QUESTION_FONT_SIZE as questionFontSize,
     *      accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor,
     *      accom.ANSWER_FONT_COLOR as answerFontColor,
     *      accom.ANSWER_FONT_SIZE as answerFontSize,
     *      accom.MASKING_RULER as maskingRuler, 
     *      accom.MUSIC_FILE_ID as musicFile,
     *      accom.MAGNIFYING_GLASS as magnifyingGlass,
     *      accom.EXTENDED_TIME as extendedTime
     * from
     *      student_accommodation accom,
     *      student stu
     * where
     *      accom.student_id (+) = stu.student_id
     *      and stu.activation_status = 'AC'
     * 	 and stu.student_id = {studentId}::
     */
    @JdbcControl.SQL(statement = "select  distinct stu.STUDENT_ID as studentId,  stu.grade as studentGrade,  accom.SCREEN_MAGNIFIER as screenMagnifier,  accom.SCREEN_READER as screenReader,  accom.CALCULATOR as calculator,  accom.TEST_PAUSE as testPause,  accom.UNTIMED_TEST as untimedTest,  accom.HIGHLIGHTER as highlighter,  accom.QUESTION_BACKGROUND_COLOR as questionBackgroundColor,  accom.QUESTION_FONT_COLOR as questionFontColor,  accom.QUESTION_FONT_SIZE as questionFontSize,  accom.ANSWER_BACKGROUND_COLOR as answerBackgroundColor,  accom.ANSWER_FONT_COLOR as answerFontColor,  accom.ANSWER_FONT_SIZE as answerFontSize, accom.MASKING_RULER as maskingRuler, accom.MUSIC_FILE_ID as musicFile, accom.MAGNIFYING_GLASS as magnifyingGlass, accom.EXTENDED_TIME as extendedTime from  student_accommodation accom,  student stu where  accom.student_id (+) = stu.student_id  and stu.activation_status = 'AC' \t and stu.student_id = {studentId}")
    StudentAccommodations getStudentAccommodations(Integer studentId) throws SQLException;

    /**
     * @jc:sql statement::
     * insert into 
     * 	    student_accommodation (
     * 		STUDENT_ID,
     * 		SCREEN_MAGNIFIER,
     * 		SCREEN_READER,
     * 		CALCULATOR,
     * 		TEST_PAUSE,
     * 		UNTIMED_TEST,
     * 		HIGHLIGHTER,
     * 		QUESTION_BACKGROUND_COLOR,
     * 		QUESTION_FONT_COLOR,
     * 		QUESTION_FONT_SIZE,
     * 		ANSWER_BACKGROUND_COLOR,
     * 		ANSWER_FONT_COLOR,
     * 		ANSWER_FONT_SIZE ,
     * 		MASKING_RULER,
     * 		MUSIC_FILE_ID,
     * 		MAGNIFYING_GLASS,
     * 		EXTENDED_TIME
     * 	   )
     * 	   values (
     *  	{accom.studentId},
     * 		{accom.screenMagnifier},
     * 		{accom.screenReader},
     * 		{accom.calculator},
     * 		{accom.testPause},
     * 		{accom.untimedTest},
     * 		{accom.highlighter},
     * 		{accom.questionBackgroundColor},
     * 		{accom.questionFontColor},
     * 		{accom.questionFontSize},
     * 		{accom.answerBackgroundColor},
     * 		{accom.answerFontColor},
     * 		{accom.answerFontSize}, 
     * 		{accom.maskingRuler},
     * 		{accom.musicFile},
     * 		{accom.magnifyingGlass},
     * 		{accom.extendedTime}
     * 		)
     * ::
     */
    @JdbcControl.SQL(statement = "insert into  \t  student_accommodation ( \t\tSTUDENT_ID, \t\tSCREEN_MAGNIFIER, \t\tSCREEN_READER, \t\tCALCULATOR, \t\tTEST_PAUSE, \t\tUNTIMED_TEST, \t\tHIGHLIGHTER, \t\tQUESTION_BACKGROUND_COLOR, \t\tQUESTION_FONT_COLOR, \t\tQUESTION_FONT_SIZE, \t\tANSWER_BACKGROUND_COLOR, \t\tANSWER_FONT_COLOR, \t\tANSWER_FONT_SIZE, MASKING_RULER, MUSIC_FILE_ID, MAGNIFYING_GLASS, EXTENDED_TIME ) \t  values (  \t{accom.studentId}, \t\t{accom.screenMagnifier}, \t\t{accom.screenReader}, \t\t{accom.calculator}, \t\t{accom.testPause}, \t\t{accom.untimedTest}, \t\t{accom.highlighter}, \t\t{accom.questionBackgroundColor}, \t\t{accom.questionFontColor}, \t\t{accom.questionFontSize}, \t\t{accom.answerBackgroundColor}, \t\t{accom.answerFontColor}, \t\t{accom.answerFontSize}, {accom.maskingRuler}, {accom.musicFile}, {accom.magnifyingGlass}, {accom.extendedTime})")
    void createStudentAccommodations(StudentAccommodations accom) throws SQLException;

    /**
     * @jc:sql statement::
     * update 
     * 	    student_accommodation 
     * set 		
     * 		SCREEN_MAGNIFIER={accom.screenMagnifier},
     * 		SCREEN_READER={accom.screenReader},
     * 		CALCULATOR={accom.calculator},
     * 		TEST_PAUSE={accom.testPause},
     * 		UNTIMED_TEST={accom.untimedTest},
     * 		HIGHLIGHTER={accom.highlighter},
     * 		QUESTION_BACKGROUND_COLOR={accom.questionBackgroundColor},
     * 		QUESTION_FONT_COLOR={accom.questionFontColor},
     * 		QUESTION_FONT_SIZE={accom.questionFontSize},
     * 		ANSWER_BACKGROUND_COLOR={accom.answerBackgroundColor},
     * 		ANSWER_FONT_COLOR={accom.answerFontColor},
     * 		ANSWER_FONT_SIZE={accom.answerFontSize}, 
     * 		MASKING_RULER = {accom.maskingRuler}, 
     * 		MUSIC_FILE_ID = {accom.musicFile},
     * 		MAGNIFYING_GLASS = {accom.magnifyingGlass} ,
     * 		EXTENDED_TIME = {accom.extendedTime}
     * where STUDENT_ID = {accom.studentId}::
     */
    @JdbcControl.SQL(statement = "update  \t  student_accommodation  set \t\t \t\tSCREEN_MAGNIFIER={accom.screenMagnifier}, \t\tSCREEN_READER={accom.screenReader}, \t\tCALCULATOR={accom.calculator}, \t\tTEST_PAUSE={accom.testPause}, \t\tUNTIMED_TEST={accom.untimedTest}, \t\tHIGHLIGHTER={accom.highlighter}, \t\tQUESTION_BACKGROUND_COLOR={accom.questionBackgroundColor}, \t\tQUESTION_FONT_COLOR={accom.questionFontColor}, \t\tQUESTION_FONT_SIZE={accom.questionFontSize}, \t\tANSWER_BACKGROUND_COLOR={accom.answerBackgroundColor}, \t\tANSWER_FONT_COLOR={accom.answerFontColor}, \t\tANSWER_FONT_SIZE={accom.answerFontSize}, MASKING_RULER = {accom.maskingRuler}, MUSIC_FILE_ID = {accom.musicFile}, MAGNIFYING_GLASS = {accom.magnifyingGlass}, EXTENDED_TIME = {accom.extendedTime}  where STUDENT_ID = {accom.studentId}")
    void updateStudentAccommodations(StudentAccommodations accom) throws SQLException;

    /**
     * @jc:sql statement::
     * delete from 
     * 	student_accommodation 
     * where STUDENT_ID = {studentId}::
     */
    @JdbcControl.SQL(statement = "delete from  \tstudent_accommodation  where STUDENT_ID = {studentId}")
    void deleteStudentAccommodations(Integer studentId) throws SQLException;

    
    @JdbcControl.SQL(statement = "update student_accommodation set {sql: updateParameters} where {sql: updateByStudetnId} ")
    void updateBulkStudentAccommodations(String updateByStudetnId, String updateParameters) throws SQLException;


    static final long serialVersionUID = 1L;
}