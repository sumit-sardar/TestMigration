package com.ctb.control.db; 

import com.bea.control.*;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.testDelivery.login.ItemResponseData;
import com.ctb.bean.testDelivery.studentTestData.RosterSubtestFeedback;
import com.ctb.bean.testDelivery.studentTestData.RosterSubtestStatus;
import com.ctb.bean.testDelivery.studentTestData.StudentTutorialStatus;
import java.util.Date; 
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
public interface SaveStudentTestData extends JdbcControl
{ 

    static final long serialVersionUID = 1L;
    
    /**
     * @jc:sql statement::
     *  select
     *         ir.*
     *  from
     *      item_response ir
     *  where
     *       ir.test_roster_id = {testRosterId}
     *       and ir.response_seq_num = {responseSeqNum}::
     */
    @JdbcControl.SQL(statement = "select ir.item_id as itemId, ir.response as response, ir.response_elapsed_time as responseElapsedTime from item_response ir where ir.test_roster_id = {testRosterId} and ir.response_seq_num = {responseSeqNum}")
    ItemResponseData getItemResponseForRosterAndMseq(int testRosterId, int responseSeqNum) throws SQLException;
    
    /**
     * @jc:sql statement::
     *  update
     *         test_roster
     *  set
     *      test_completion_status = {status},
     *      updated_date_time = {updatedDateTime},
     *      last_mseq = {mseq}
     *  where
     *       test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "update  test_roster set  test_completion_status = {status},  updated_date_time = {updatedDateTime},  last_mseq = {mseq} where  test_roster_id = {testRosterId}")
    void setRosterCompletionStatus(int testRosterId, String status, Date updatedDateTime, int mseq) throws SQLException;

    /**
     * @jc:sql statement::
     *  select
     *         test_roster.test_completion_status
     *  from
     *      test_roster
     *  where
     *       test_roster.test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "select  test_roster.test_completion_status from  test_roster where  test_roster.test_roster_id = {testRosterId}")
    String getRosterCompletionStatus(int testRosterId) throws SQLException;

    /**
     * @jc:sql statement::
     *  select
     *      test_admin.test_admin_status
     *  from
     *      test_admin,
     *      test_roster
     *  where
     *      test_admin.test_admin_id = test_roster.test_admin_id
     *      and test_roster.test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "select  test_admin.test_admin_status from  test_admin,  test_roster where  test_admin.test_admin_id = test_roster.test_admin_id  and test_roster.test_roster_id = {testRosterId}")
    String getTestAdminStatus(int testRosterId) throws SQLException;


    /**
     * @jc:sql statement::
     *  select
     *      ros.test_roster_id as testRosterId,
	 *      ros.test_completion_status as testCompletionStatus,
	 *      siss.item_set_id as itemSetId,
     *      siss.completion_status as subtestCompletionStatus,
     *      siss.raw_score as rawScore,
     *      siss.recommended_level as recommendedLevel,
     *      ros.updated_date_time as updatedDateTime,
     *      ros.last_mseq as lastMseq,
     *      pr.scoreable as scoreable
     *  from
	 *      student_item_set_status siss,
     *      test_roster ros,
     *  	test_admin ta,
     *		product pr
     *  where
	 *      ros.test_roster_id = {testRosterId}
	 *      and siss.test_roster_id = ros.test_roster_id
     *  	and ros.test_admin_id = ta.test_admin_id
     *  	and pr.product_id = ta.product_id::
     */
    @JdbcControl.SQL(statement = "select ros.test_roster_id as testRosterId, ros.test_completion_status as testCompletionStatus, siss.item_set_id as itemSetId, siss.completion_status as subtestCompletionStatus, siss.raw_score as rawScore, siss.recommended_level as recommendedLevel, ros.updated_date_time as updatedDateTime, ros.last_mseq as lastMseq, pr.scannable as scoreable from student_item_set_status siss, test_roster ros, test_admin ta, product pr where ros.test_roster_id = {testRosterId} and siss.test_roster_id = ros.test_roster_id and ros.test_admin_id = ta.test_admin_id and pr.product_id = ta.product_id")
    RosterSubtestStatus [] getRosterSubtestStatus(int testRosterId) throws SQLException;

    /**
     * @jc:sql statement::
     *  select
     *      ros.test_roster_id as testRosterId,
	 *      ros.test_completion_status as testCompletionStatus,
	 *      siss.item_set_id as itemSetId,
     *      siss.completion_status as subtestCompletionStatus,
     *      siss.raw_score as rawScore,
     *      siss.recommended_level as recommendedLevel,
     *      ros.updated_date_time as updatedDateTime,
     *      ros.last_mseq as lastMseq,
     *      iset.item_set_name as itemSetName 
     *  from
	 *      student_item_set_status siss,
     *      test_roster ros,
     *      item_set iset
     *  where
	 *      ros.test_roster_id = {testRosterId}
	 *      and siss.test_roster_id = ros.test_roster_id
     *      and siss.item_set_id = iset.item_set_id
     *      and iset.item_set_level = 'L'::
     */
    @JdbcControl.SQL(statement = "select  ros.test_roster_id as testRosterId,  ros.test_completion_status as testCompletionStatus,  siss.item_set_id as itemSetId,  siss.completion_status as subtestCompletionStatus,  siss.raw_score as rawScore,  siss.recommended_level as recommendedLevel,  ros.updated_date_time as updatedDateTime,  ros.last_mseq as lastMseq,  iset.item_set_name as itemSetName  from  student_item_set_status siss,  test_roster ros,  item_set iset where  ros.test_roster_id = {testRosterId}  and siss.test_roster_id = ros.test_roster_id  and siss.item_set_id = iset.item_set_id  and iset.item_set_level = 'L'")
    RosterSubtestStatus [] getLocatorRosterSubtestStatus(int testRosterId) throws SQLException;


    /**
     * @jc:sql statement::
     *  update 
	 *      student_item_set_status
     *  set
     *      completion_status = 'IP',
     *      start_date_time = {timestamp}
     *  where
     *      test_roster_id = {testRosterId} and
     *      item_set_id = {itemSetId}::
     */
    @JdbcControl.SQL(statement = "update  student_item_set_status set  completion_status = 'IP',  start_date_time = {timestamp} where  test_roster_id = {testRosterId} and  item_set_id = {itemSetId}")
    void startSubtest(int testRosterId, int itemSetId, Date timestamp) throws SQLException;

    /**
     * @jc:sql statement::
     *  update 
	 *      student_item_set_status
     *  set
     *      completion_status = 'CO',
     *      completion_date_time = {timestamp},
     *      time_expired = {timeout}
     *  where
     *      test_roster_id = {testRosterId} and
     *      item_set_id = {itemSetId}::
     */
    @JdbcControl.SQL(statement = "update  student_item_set_status set  completion_status = 'CO',  completion_date_time = {timestamp},  time_expired = {timeout} where  test_roster_id = {testRosterId} and  item_set_id = {itemSetId}")
    void stopSubtest(int testRosterId, int itemSetId, Date timestamp, String timeout) throws SQLException;

    /**
     * @jc:sql statement::
     *  update 
	 *      student_item_set_status
     *  set
     *      completion_status = {status}
     *  where
     *      test_roster_id = {testRosterId} and
     *      item_set_id = {itemSetId} and
     *      completion_status != {notStatus}::
     */
    @JdbcControl.SQL(statement = "update  student_item_set_status set  completion_status = {status} where  test_roster_id = {testRosterId} and  item_set_id = {itemSetId} and  completion_status != {notStatus}")
    void updateSubtestCompletionStatus(int testRosterId, int itemSetId, String status, String notStatus) throws SQLException;

    /**
     * @jc:sql statement::
     *  update 
	 *      student_item_set_status
     *  set
     *      recommended_level = {level}
     *  where
     *      test_roster_id = {testRosterId} and
     *      item_set_id = {itemSetId}::
     */
    @JdbcControl.SQL(statement = "update  student_item_set_status set  recommended_level = {level} where  test_roster_id = {testRosterId} and  item_set_id = {itemSetId}")
    void updateRecommendedLevelForSubtestCompletionStatus(int testRosterId, int itemSetId, String level) throws SQLException;

    /**
     * @jc:sql statement::
     *  update
     *         test_roster
     *  set
     *      test_completion_status = {testCompletionStatus},
     *      completion_date_time = {timestamp},
     *      updated_date_time = {timestamp},
     *      last_mseq = {mseq}
     *  where
     *       test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "update  test_roster set  test_completion_status = {testCompletionStatus},  completion_date_time = {timestamp},  updated_date_time = {timestamp},  last_mseq = {mseq} where  test_roster_id = {testRosterId}")
    void stopTest(int testRosterId, Date timestamp, String testCompletionStatus, int mseq) throws SQLException;

    /**
     * @jc:sql statement::
     *  insert into item_response (
     *      item_response_id,
	 *      test_roster_id, 
	 *		item_set_id, 
     *		item_id, 
     *		response, 
     *		response_method, 
     *		response_elapsed_time, 
     *		response_seq_num, 
     *		ext_answer_choice_id, 
     *     	student_marked, 
     *		created_by)
	 *	values
     *      (SEQ_ITEM_RESPONSE_ID.NEXTVAL,
     *      {testRosterId},
     *      {itemSetId},
     *      {itemId},
     *      {response},
     *      'M',
     *      {elapsedTime},
     *      ((select NVL(max(response_seq_num), 0) from item_response where test_roster_id = {testRosterId}) + 1),
     *      {answerChoiceId},
     *      'T',
     *      6)::
     */
    @JdbcControl.SQL(statement = "insert into item_response (  item_response_id,  test_roster_id,  \t\titem_set_id,  \t\titem_id,  \t\tresponse,  \t\tresponse_method,  \t\tresponse_elapsed_time,  \t\tresponse_seq_num,  \t\text_answer_choice_id,  \tstudent_marked,  \t\tcreated_by) \tvalues  (SEQ_ITEM_RESPONSE_ID.NEXTVAL,  {testRosterId},  {itemSetId},  {itemId},  {response},  'M',  {elapsedTime},  ((select NVL(max(response_seq_num), 0) from item_response where test_roster_id = {testRosterId}) + 1),  {answerChoiceId},  'T',  6)")
    void storeResponse(int testRosterId, int itemSetId, String itemId, String response, float elapsedTime, String answerChoiceId) throws SQLException;

    /**
     * @jc:sql statement::
     *  insert into stg_item_response (
     *      item_response_id,
	 *      test_roster_id, 
	 *		item_set_id, 
     *		item_id, 
     *		response, 
     *		response_method, 
     *		response_elapsed_time, 
     *		response_seq_num, 
     *		ext_answer_choice_id, 
     *     	student_marked, 
     *		created_by)
	 *	values
     *      (SEQ_STG_ITEM_RESPONSE_ID.NEXTVAL,
     *      {testRosterId},
     *      {itemSetId},
     *      {itemId},
     *      {response},
     *      'M',
     *      {elapsedTime},
     *      ((select NVL(max(response_seq_num), 0) from stg_item_response where test_roster_id = {testRosterId}) + 1),
     *      {answerChoiceId},
     *      'T',
     *      6)::
     */
    @JdbcControl.SQL(statement = "insert into stg_item_response (  item_response_id,  test_roster_id,  \t\titem_set_id,  \t\titem_id,  \t\tresponse,  \t\tresponse_method,  \t\tresponse_elapsed_time,  \t\tresponse_seq_num,  \t\text_answer_choice_id,  \tstudent_marked,  \t\tcreated_by) \tvalues  (SEQ_STG_ITEM_RESPONSE_ID.NEXTVAL,  {testRosterId},  {itemSetId},  {itemId},  {response},  'M',  {elapsedTime},  ((select NVL(max(response_seq_num), 0) from stg_item_response where test_roster_id = {testRosterId}) + 1),  {answerChoiceId},  'T',  6)")
    void storeResponseSTG(int testRosterId, int itemSetId, String itemId, String response, float elapsedTime, String answerChoiceId) throws SQLException;

    /**
     * @jc:sql statement::
     *  insert into item_response (
     *      item_response_id,
	 *      test_roster_id, 
	 *		item_set_id, 
     *		item_id, 
     *		response, 
     *		response_method, 
     *		response_elapsed_time, 
     *		response_seq_num, 
     *		ext_answer_choice_id, 
     *     	student_marked, 
     *		created_by)
	 *	values
     *      (SEQ_ITEM_RESPONSE_ID.NEXTVAL,
     *      {testRosterId},
     *      {itemSetId},
     *      {itemId},
     *      {response},
     *      'M',
     *      {elapsedTime},
     *      {responseSeqNum},
     *      {answerChoiceId},
     *      {studentMarked},
     *      6)::
     */
    @JdbcControl.SQL(statement = "insert into item_response (  item_response_id,  test_roster_id,  \t\titem_set_id,  \t\titem_id,  \t\tresponse,  \t\tresponse_method,  \t\tresponse_elapsed_time,  \t\tresponse_seq_num,  \t\text_answer_choice_id,  \tstudent_marked,  \t\tcreated_by) \tvalues  (SEQ_ITEM_RESPONSE_ID.NEXTVAL,  {testRosterId},  {itemSetId},  {itemId},  {response},  'M',  {elapsedTime},  {responseSeqNum},  {answerChoiceId},  {studentMarked},  6)")
    void storeResponseWithMseq(int testRosterId, int itemSetId, String itemId, String response, float elapsedTime, String answerChoiceId, int responseSeqNum, String studentMarked) throws SQLException;

    /**
     * @jc:sql statement::
     *  update
     *      student_item_set_status
     *  set
     *      raw_score = {score},
     *      max_score = {max},
     *      unscored = {unscored}
     *  where
     *      test_roster_id = {testRosterId}
     *      and item_set_id = {itemSetId}::
     */
    @JdbcControl.SQL(statement = "update  student_item_set_status set  raw_score = {score},  max_score = {max},  unscored = {unscored} where  test_roster_id = {testRosterId}  and item_set_id = {itemSetId}")
    void storeSubtestRawScore(int testRosterId, int itemSetId, int score, int max, int unscored) throws SQLException;

    /**
     * @jc:sql statement::
     * insert into item_response_cr (
     *      test_roster_id, 
     *      item_set_id, 
     *		item_id, 
     *		constructed_response)
	 *	values (
     *      {testRosterId},
     *      {itemSetId},
     *      {itemId},
     *      {response})::
     */
    @JdbcControl.SQL(statement = "insert into item_response_cr (  test_roster_id,  item_set_id,  \t\titem_id,  \t\tconstructed_response) \tvalues (  {testRosterId},  {itemSetId},  {itemId},  {response})")
    void storeCRResponse(int testRosterId, int itemSetId, String itemId, String response) throws SQLException;

    /**
     * @jc:sql statement::
     * delete from item_response_cr where
     *      test_roster_id = {testRosterId}
     *      and item_set_id = {itemSetId}
     *      and item_id = {itemId}::
     */
    @JdbcControl.SQL(statement = "delete from item_response_cr where  test_roster_id = {testRosterId}  and item_set_id = {itemSetId}  and item_id = {itemId}")
    void deleteCRResponse(int testRosterId, int itemSetId, String itemId) throws SQLException;

    /**
     * @jc:sql statement::
     * insert into stg_item_response_cr (
     *      test_roster_id, 
     *      item_set_id, 
     *		item_id, 
     *		constructed_response)
	 *	values (
     *      {testRosterId},
     *      {itemSetId},
     *      {itemId},
     *      {response})::
     */
    @JdbcControl.SQL(statement = "insert into stg_item_response_cr (  test_roster_id,  item_set_id,  \t\titem_id,  \t\tconstructed_response) \tvalues (  {testRosterId},  {itemSetId},  {itemId},  {response})")
    void storeCRResponseSTG(int testRosterId, int itemSetId, String itemId, String response) throws SQLException;

    /**
     * @jc:sql statement::
     * delete from stg_item_response_cr where
     *      test_roster_id = {testRosterId}
     *      and item_set_id = {itemSetId}
     *      and item_id = {itemId}::
     */
    @JdbcControl.SQL(statement = "delete from stg_item_response_cr where  test_roster_id = {testRosterId}  and item_set_id = {itemSetId}  and item_id = {itemId}")
    void deleteCRResponseSTG(int testRosterId, int itemSetId, String itemId) throws SQLException;


    /**
     * @jc:sql statement::
     * select distinct
     *      siss.raw_score as rawScore,
     *      siss.max_score as maxScore,
     *      siss.unscored as unscored,
     *      siss.item_set_order + 1 as sequence,
     *      subtest.item_set_name as subtestTitle,
     *      subtest.item_set_id as subtestId,
     *      test.item_set_name as testTitle,
     *      test.item_set_id as testId,
     *      stu.first_name || ' ' || stu.last_name as studentName
     * from
     *      test_roster ros, 
     *      student stu,
     *      item_set subtest,
     *      item_set test,
     *      student_item_set_status siss,
     *      test_admin adm
     * where
     *      ros.test_roster_id = {testRosterId}
     *      and stu.student_id = ros.student_id
     *      and siss.test_roster_id = ros.test_roster_id
     *      and subtest.item_set_id = siss.item_set_id
     *      and adm.test_Admin_id = ros.test_admin_id
     *      and test.item_set_id = adm.item_set_id::
    */ 
    @JdbcControl.SQL(statement = "select distinct  siss.raw_score as rawScore,  siss.max_score as maxScore,  siss.unscored as unscored,  siss.item_set_order + 1 as sequence,  subtest.item_set_name as subtestTitle,  subtest.item_set_id as subtestId,  test.item_set_name as testTitle,  test.item_set_id as testId,  stu.first_name || ' ' || stu.last_name as studentName from  test_roster ros,  student stu,  item_set subtest,  item_set test,  student_item_set_status siss,  test_admin adm where  ros.test_roster_id = {testRosterId}  and stu.student_id = ros.student_id  and siss.test_roster_id = ros.test_roster_id  and subtest.item_set_id = siss.item_set_id  and adm.test_Admin_id = ros.test_admin_id  and test.item_set_id = adm.item_set_id")
    RosterSubtestFeedback [] getSubtestFeedbackForRoster(Integer testRosterId) throws SQLException;

    

    /**
     * @jc:sql statement::
     * select correlation_id
     * from test_roster
     * where test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "select correlation_id from test_roster where test_roster_id = {testRosterId}")
    Integer getCorrelationIdForRoster(Integer testRosterId);

    /**
     * @jc:sql statement::
     * update test_roster
     * set correlation_id = {correlationId}
     * where test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "update test_roster set correlation_id = {correlationId} where test_roster_id = {testRosterId}")
    void setCorrelationIdForRoster(Integer testRosterId, Integer correlationId);

    /**
     * @jc:sql statement::
     * update
     *     test_roster
     * set
     *     updated_date_time = {updatedDateTime}
     * where
     *     test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "update  test_roster set  updated_date_time = {updatedDateTime} where  test_roster_id = {testRosterId}")
    void updateTestRosterTimeStamp(int testRosterId, Date updatedDateTime);

    /**
     * @jc:sql statement::
     * update
     *     test_roster
     * set
     *     updated_date_time = {updatedDateTime},
     *     last_mseq = {mseq}
     * where
     *     test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "update  test_roster set  updated_date_time = {updatedDateTime},  last_mseq = {mseq} where  test_roster_id = {testRosterId}")
    void updateTestRosterTimeStampWithMseq(int testRosterId, Date updatedDateTime, int mseq);

    /**
     * @jc:sql statement::
     * delete from student_item_set_status
     * where test_roster_id = {testRosterId}
     * and item_set_id in 
     * (select siss.item_set_id 
     * from student_item_set_status siss, item_set ist
     * where siss.test_roster_id = {testRosterId}
     * and ist.item_set_id = siss.item_set_id
     * and ist.item_set_name like {itemSetNamePattern}
     * and ist.item_set_form <> {form})::
     */
    @JdbcControl.SQL(statement = "delete from student_item_set_status where test_roster_id = {testRosterId} and item_set_id in  (select siss.item_set_id  from student_item_set_status siss, item_set ist where siss.test_roster_id = {testRosterId} and ist.item_set_id = siss.item_set_id and ist.item_set_name like {itemSetNamePattern} and ist.item_set_form <> {form})")
    void deleteUnrecommendedSubtests(int testRosterId, String itemSetNamePattern, String form);


    /**
     * @jc:sql statement::
     * select a.student_id as studentId,
     * a.product_id as productId, 
     * sts.completion_status as completionStatus
     * from student_tutorial_status sts,
     * (select tr.student_id, ta.product_id
     * from test_roster tr, test_admin ta
     * where tr.test_admin_id = ta.test_admin_id
     * and tr.test_roster_id = {testRosterId}) a
     * where a.student_id = sts.student_id (+)
     * and a.product_id = sts.product_id (+)::
     */
    @JdbcControl.SQL(statement = "select a.student_id as studentId, a.product_id as productId,  sts.completion_status as completionStatus from student_tutorial_status sts, (select tr.student_id, ta.product_id from test_roster tr, test_admin ta where tr.test_admin_id = ta.test_admin_id and tr.test_roster_id = {testRosterId}) a where a.student_id = sts.student_id (+) and a.product_id = sts.product_id (+)")
    StudentTutorialStatus getStudentTutorialStatus(int testRosterId);

    /**
     * @jc:sql statement::
     * insert into student_tutorial_status 
     * (product_id, student_id, completion_status)
     * values ({productId}, {studentId}, {status})::
     */
    @JdbcControl.SQL(statement = "insert into student_tutorial_status  (product_id, student_id, completion_status) values ({productId}, {studentId}, {status})")
    void storeStudentTutorialStatus(int productId, int studentId, String status);

    /**
     * @jc:sql statement::
     * update 
     * 	student_item_set_status
     * set 
     * 	scratchpad_content = {text}
     * where
     * 	test_roster_id = {testRosterId}
     * and 
     * 	item_set_id = {itemSetId}::
     */
    @JdbcControl.SQL(statement = "update  \tstudent_item_set_status set  \tscratchpad_content = {text} where \ttest_roster_id = {testRosterId} and  \titem_set_id = {itemSetId}")
    void saveSubtestScratchpadContents(int testRosterId, int itemSetId, String text);
    
     

    /**
     * @jc:sql statement::
     *  update test_roster ros
     *           set ros.tts_speed_status = {ttsSpeedValue}
     *           where ros.test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "update test_roster ros  set ros.tts_speed_status = {ttsSpeedValue}  where ros.test_roster_id = {testRosterId}")
    public void updateStudentTTSspeedValue (Integer testRosterId,String ttsSpeedValue) throws SQLException;

    
    @JdbcControl.SQL(statement = "SELECT decode(i.answer_area, 'AudioItem',1,0) FROM item i WHERE i.item_id = {itemId}")
    int checkAudioItem(String itemId);
    
    @JdbcControl.SQL(statement = "SELECT COUNT(1) FROM item_response_cr WHERE item_id = {itemId} AND test_roster_id = {testRosterId}")
    int checkCRResponseExists(String itemId, int testRosterId);
}