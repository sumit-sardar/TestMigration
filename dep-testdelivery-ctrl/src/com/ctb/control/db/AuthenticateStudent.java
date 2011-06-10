package com.ctb.control.db; 

import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.testDelivery.login.AccomodationsData;
import com.ctb.bean.testDelivery.login.AuthenticationData;
import com.ctb.bean.testDelivery.login.ItemResponseData;
import com.ctb.bean.testDelivery.login.ManifestData;
import com.ctb.bean.testDelivery.login.SystemThrottle;

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
public interface AuthenticateStudent extends JdbcControl
{ 
    static final long serialVersionUID = 1L;

    /**
     * @jc:sql statement::
     * select 
     * 		throttle_mode as throttleMode, 
     * 		throttle_threshold as throttleThreshold, 
     * 		check_frequency as checkFrequency 
     * from 
     * 		system_throttle::
     */
    @JdbcControl.SQL(statement = "select throttle_mode as throttleMode, throttle_threshold as throttleThreshold, check_frequency as checkFrequency from system_throttle")
    SystemThrottle getSystemThrottle();

    /**
     * @jc:sql statement::
     * select count(*) as counter from test_roster where test_completion_status = 'IP'::
     */
    @JdbcControl.SQL(statement = "select count(*) as counter from test_roster where test_completion_status = 'IP'")
    int getInProgressRosterCount();
    
    /**
     * @jc:sql statement::
     * update
     *        test_roster
     * set
     *     test_completion_status = {status},
     *     restart_number = {restartNumber},
     *     start_date_time = nvl(start_date_time,{timestamp}),
     *     last_login_date_time = {timestamp},
     *     capture_method = {captureMethod},
     *     updated_date_time = {timestamp},
     *     last_mseq = {mseq},
     *     correlation_id = null
     * where
     *      test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "update  test_roster set  test_completion_status = {status},  restart_number = {restartNumber},  start_date_time = nvl(start_date_time,{timestamp}),  last_login_date_time = {timestamp},  capture_method = {captureMethod},  updated_date_time = {timestamp},  last_mseq = {mseq},  correlation_id = null where  test_roster_id = {testRosterId}")
    void setRosterCompletionStatus(int testRosterId, String status, String captureMethod, int restartNumber, Date timestamp, int mseq);

    /**
     * @jc:sql statement::
     * select
     *        ros.test_roster_id as testRosterId, 
     *        stu.student_id as studentId, 
     *        stu.last_name as studentLastName,
     *        stu.first_name as studentFirstName,
     *        stu.middle_name as studentMiddleName,
     *        ros.test_completion_status as rosterTestCompletionStatus,
     *        adm.login_start_date as windowStartDate,
     *        adm.login_end_date as windowEndDate,
     *        adm.daily_login_start_time as dailyStartTime,
     *        adm.daily_login_end_time as dailyEndTime,
     *        adm.test_admin_status as testAdminStatus,
     *        adm.time_zone AS timeZone,
     *        ros.capture_method as captureMethod,
     *        ros.restart_number as restartNumber,
     *        ros.test_admin_id as testAdminId,
     * 	      ros.random_distractor_seed as randomDistractorSeedNumber,
	 * 	      ros.tts_speed_status as ttsSpeedStatus
     * from
     *     student stu,
     *     test_roster ros,
     *     test_admin adm
     * where
     *      adm.test_admin_id = ros.test_admin_id
     *      and ros.student_id = stu.student_id
     *      and stu.activation_status = 'AC'
     *      and ros.activation_status = 'AC'
     *      and adm.activation_status = 'AC'
     *      and upper(stu.user_name) = upper({username})
     *      and upper(ros.password) = upper({password})::
     */
    @JdbcControl.SQL(statement = "select  ros.test_roster_id as testRosterId,  stu.student_id as studentId,  stu.last_name as studentLastName,  stu.first_name as studentFirstName,  stu.middle_name as studentMiddleName,  ros.test_completion_status as rosterTestCompletionStatus,  adm.login_start_date as windowStartDate,  adm.login_end_date as windowEndDate,  adm.daily_login_start_time as dailyStartTime,  adm.daily_login_end_time as dailyEndTime,  adm.test_admin_status as testAdminStatus,  adm.time_zone AS timeZone,  ros.capture_method as captureMethod,  ros.restart_number as restartNumber,  ros.test_admin_id as testAdminId, \t  ros.random_distractor_seed as randomDistractorSeedNumber, \t  ros.tts_speed_status as ttsSpeedStatus from  student stu,  test_roster ros,  test_admin adm where  adm.test_admin_id = ros.test_admin_id  and ros.student_id = stu.student_id  and stu.activation_status = 'AC'  and ros.activation_status = 'AC'  and adm.activation_status = 'AC'  and upper(stu.user_name) = upper({username})  and upper(ros.password) = upper({password})")
    AuthenticationData [] authenticateStudent(String username, String password);

    /**
     * @jc:sql statement::
     * select
     *        accom.student_id as studentId,
     *        accom.screen_magnifier as screenMagnifier,
     *        accom.screen_reader as screenReader,
     *        accom.calculator as calculator,
     *        accom.test_pause as testPause,
     *        accom.untimed_test as untimedTest,
     *        accom.question_background_color as questionBackgroundColor,
     *        accom.question_font_color as questionFontColor,
     *        accom.question_font_size as questionFontSize,
     *        accom.answer_background_color as answerBackgroundColor,
     *        accom.answer_font_color as answerFontColor,
     *        accom.answer_font_size as answerFontSize,
     *        accom.highlighter as highlighter
     *        
     *  from
     *      test_roster ros,
     *      student_accommodation accom
     *  where
     *       accom.student_id = ros.student_id
     *       and ros.test_roster_id = {testRosterId}
     */
    @JdbcControl.SQL(statement = "select  accom.student_id as studentId,  accom.screen_magnifier as screenMagnifier,  accom.screen_reader as screenReader,  accom.calculator as calculator,  accom.test_pause as testPause,  accom.untimed_test as untimedTest,  accom.question_background_color as questionBackgroundColor,  accom.question_font_color as questionFontColor,  accom.question_font_size as questionFontSize,  accom.answer_background_color as answerBackgroundColor,  accom.answer_font_color as answerFontColor,  accom.answer_font_size as answerFontSize,  accom.highlighter as highlighter from  test_roster ros,  student_accommodation accom  where  accom.student_id = ros.student_id  and ros.test_roster_id = {testRosterId}")
    AccomodationsData getAccomodations(int testRosterId) throws SQLException;

    /**
     * @jc:sql statement::
     * select scoOrder,
     *        scoParentId,
     *        adminForceLogout,
     *        showStudentFeedback,
     *        id,
     *        title,
     *        testTitle,
     *        scoDurationMinutes,
     *        0 as totalTime,
     *        scoUnitType,
     *        scoEntry,
     *        completionStatus,
     *        asmtHash,
     *        asmtEncryptionKey,
     *        itemEncryptionKey,
     *        adsid,
     *        randomDistractorStatus
     *   from (select siss.item_Set_order as scoOrder,
     *                isp.parent_item_Set_id as scoParentId,
     *                ta.force_logout as adminForceLogout,
     *                ta.show_student_feedback as showStudentFeedback,
     *                iset.item_set_id as id,
     *                iset.item_set_name as title,
     *                test.item_set_name as testTitle,
     *                iset.time_limit / 60 as scoDurationMinutes,
     *                'SUBTEST' as scoUnitType,
     *                'ab-initio' as scoEntry,
     *                siss.completion_status as completionStatus,
     *                iset.asmt_hash as asmtHash,
     *                iset.asmt_encryption_key as asmtEncryptionKey,
     *                iset.item_encryption_key as itemEncryptionKey,
     *                iset.ads_ob_asmt_id as adsid,
     *                ta.test_admin_id testid,
     *                ta.random_distractor_status as randomDistractorStatus
     *           from item_set_item           isi,
     *                item_Set                iset,
     *                item_set                test,
     *                student_item_set_status siss,
     *                test_roster             tr,
     *                test_admin              ta,
     *                item_set_parent         isp,
     *                test_admin_item_set     tais
     *          where isi.item_set_id = iset.item_set_id
     *            and iset.item_set_id = siss.item_set_id
     *            and iset.item_set_type = 'TD'
     *            and siss.test_roster_id = tr.test_roster_id
     *            and ta.test_admin_id = tr.test_admin_id
     *            and isp.item_Set_id = iset.item_set_id
     *            and tr.test_roster_id = {testRosterId}
     *            and tais.item_set_id = isp.parent_item_set_id
     *            and test.item_set_id = ta.item_set_id
     *            and upper(tais.access_code) = upper({testAccessCode})
     *            and tais.test_admin_id = ta.test_admin_id
     *          group by siss.item_Set_order,
     *                   isp.parent_item_set_id,
     *                   ta.force_logout,
     *                   ta.show_student_feedback,
     *                   iset.item_Set_id,
     *                   iset.item_set_name,
     *                   test.item_set_name,
     *                   iset.time_limit,
     *                   isi.item_sort_order,
     *                   siss.completion_status,
     *                   iset.asmt_hash,
     *                   iset.asmt_encryption_key,
     *                   iset.item_encryption_key,
     *                   iset.ads_ob_asmt_id,
     *                   iset.item_set_level,
     *                   ta.test_admin_id,
     *                   ta.random_distractor_status)
     *  group by scoOrder,
     *           scoParentId,
     *           adminForceLogout,
     *           showStudentFeedback,
     *           id,
     *           title,
     *           testTitle,
     *           scoDurationMinutes,
     *           scoUnitType,
     *           scoEntry,
     *           completionStatus,
     *           asmtHash,
     *           asmtEncryptionKey,
     *           itemEncryptionKey,
     *           adsid,
     *           randomDistractorStatus
     *  order by scoOrder::
     */
    @JdbcControl.SQL(statement = "select scoOrder,  scoParentId,  adminForceLogout,  showStudentFeedback,  id,  title,  testTitle,  scoDurationMinutes,  0 as totalTime,  scoUnitType,  scoEntry,  completionStatus,  asmtHash,  asmtEncryptionKey,  itemEncryptionKey,  adsid,  randomDistractorStatus  from (select siss.item_Set_order as scoOrder,  isp.parent_item_Set_id as scoParentId,  ta.force_logout as adminForceLogout,  ta.show_student_feedback as showStudentFeedback,  iset.item_set_id as id,  iset.item_set_name as title,  test.item_set_name as testTitle,  iset.time_limit / 60 as scoDurationMinutes,  'SUBTEST' as scoUnitType,  'ab-initio' as scoEntry,  siss.completion_status as completionStatus,  iset.asmt_hash as asmtHash,  iset.asmt_encryption_key as asmtEncryptionKey,  iset.item_encryption_key as itemEncryptionKey,  iset.ads_ob_asmt_id as adsid,  ta.test_admin_id testid,  ta.random_distractor_status as randomDistractorStatus  from item_set_item  isi,  item_Set  iset,  item_set  test,  student_item_set_status siss,  test_roster  tr,  test_admin  ta,  item_set_parent  isp,  test_admin_item_set  tais  where isi.item_set_id = iset.item_set_id  and iset.item_set_id = siss.item_set_id  and iset.item_set_type = 'TD'  and siss.test_roster_id = tr.test_roster_id  and ta.test_admin_id = tr.test_admin_id  and isp.item_Set_id = iset.item_set_id  and tr.test_roster_id = {testRosterId}  and tais.item_set_id = isp.parent_item_set_id  and test.item_set_id = ta.item_set_id  and upper(tais.access_code) = upper({testAccessCode})  and tais.test_admin_id = ta.test_admin_id  group by siss.item_Set_order,  isp.parent_item_set_id,  ta.force_logout,  ta.show_student_feedback,  iset.item_Set_id,  iset.item_set_name,  test.item_set_name,  iset.time_limit,  isi.item_sort_order,  siss.completion_status,  iset.asmt_hash,  iset.asmt_encryption_key,  iset.item_encryption_key,  iset.ads_ob_asmt_id,  iset.item_set_level,  ta.test_admin_id,  ta.random_distractor_status)  group by scoOrder,  scoParentId,  adminForceLogout,  showStudentFeedback,  id,  title,  testTitle,  scoDurationMinutes,  scoUnitType,  scoEntry,  completionStatus,  asmtHash,  asmtEncryptionKey,  itemEncryptionKey,  adsid,  randomDistractorStatus  order by scoOrder")
    ManifestData [] getManifest(int testRosterId, String testAccessCode) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select 
     *      nvl(sum(max(resp.response_elapsed_time)), 0) as totalTime
     * from 
     *      item_response resp
     * where 
     *      resp.test_roster_id = {testRosterId}
     *      and resp.item_set_id = {itemSetId}
     * group by
     *      resp.test_roster_id,
     *      resp.item_set_id,
     *      resp.item_id::
     */
    @JdbcControl.SQL(statement = "select  nvl(sum(max(resp.response_elapsed_time)), 0) as totalTime from  item_response resp where  resp.test_roster_id = {testRosterId}  and resp.item_set_id = {itemSetId} group by  resp.test_roster_id,  resp.item_set_id,  resp.item_id")
    Integer getTotalElapsedTimeForSubtest(Integer testRosterId, Integer itemSetId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select
     *     decode(count(siss.item_set_id), 0, 'T', 'F')
     * from
     * 	   student_item_set_status siss,
     *     test_Admin_item_set tais,
     *     item_set_parent isp
     * where
     *     tais.test_Admin_id = {testAdminId}
     *     and siss.test_roster_id = {testRosterId}
     *     and upper(tais.access_code) != upper({accessCode})
     * 	   and isp.parent_item_set_id = tais.item_set_id
     * 	   and siss.item_set_id = isp.item_set_id
     * 	   and siss.completion_status != 'CO'::
     */
    @JdbcControl.SQL(statement = "select  decode(count(siss.item_set_id), 0, 'T', 'F') from \t  student_item_set_status siss,  test_Admin_item_set tais,  item_set_parent isp where  tais.test_Admin_id = {testAdminId}  and siss.test_roster_id = {testRosterId}  and upper(tais.access_code) != upper({accessCode}) \t  and isp.parent_item_set_id = tais.item_set_id \t  and siss.item_set_id = isp.item_set_id \t  and siss.completion_status != 'CO'")
    String isUltimateAccessCode(Integer testRosterId, Integer testAdminId, String accessCode) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *      ir.item_id as itemId, 
     *      ir.response_seq_num as responseSeqNum, 
     *      ir.student_marked as studentMarked, 
     *      i.item_type as itemType, 
     *      isi.item_sort_order as itemSortOrder,
     *      ir.response as response,
     *      icr.constructed_response as constructedResponse,
     *      ir.response_elapsed_time as responseElapsedTime,
     *      decode(ir.response, i.correct_answer, 1, 0) as score,
     *      i.ads_item_id as eid
     * from 
     *      item_response ir, 
     *      item i, 
     *      item_response_cr icr,
     *      item_set_item isi
     * where 
     *      ir.test_roster_id = {testRosterId}
     *      and ir.item_set_id = {itemSetId}
     *      and ir.item_id = i.item_id
     *      and ir.item_set_id = isi.item_set_id
     *      and ir.item_id = isi.item_id
     *      and ir.response_seq_num = 
     *          (select 
     *              max(ir1.response_seq_num) 
     *           from 
     *              item_response ir1 
     *           where 
     *              ir1.item_set_id = ir.item_set_id 
     *              and ir1.item_id = ir.item_id 
     *              and ir1.test_roster_id = ir.test_roster_id)
     * 	     AND ir.test_roster_id = icr.test_roster_id (+)
     * 	     AND ir.item_set_id = icr.item_set_id (+)
     * 	     AND ir.item_id = icr.item_id (+)
     * order by
     *      isi.item_sort_order::
     */
    @JdbcControl.SQL(statement = "select  ir.item_id as itemId,  ir.response_seq_num as responseSeqNum,  ir.student_marked as studentMarked,  i.item_type as itemType,  isi.item_sort_order as itemSortOrder,  ir.response as response,  icr.constructed_response as constructedResponse,  ir.response_elapsed_time as responseElapsedTime,  decode(ir.response, i.correct_answer, 1, 0) as score,  i.ads_item_id as eid from  item_response ir,  item i,  item_response_cr icr,  item_set_item isi where  ir.test_roster_id = {testRosterId}  and ir.item_set_id = {itemSetId}  and ir.item_id = i.item_id  and ir.item_set_id = isi.item_set_id  and ir.item_id = isi.item_id  and ir.response_seq_num =  (select  max(ir1.response_seq_num)  from  item_response ir1  where  ir1.item_set_id = ir.item_set_id  and ir1.item_id = ir.item_id  and ir1.test_roster_id = ir.test_roster_id) \t  AND ir.test_roster_id = icr.test_roster_id (+) \t  AND ir.item_set_id = icr.item_set_id (+) \t  AND ir.item_id = icr.item_id (+) order by  isi.item_sort_order")
    ItemResponseData [] getRestartItemResponses(int testRosterId, int itemSetId);

    /**
     * @jc:sql statement::
     * select 
     *     siss.item_Set_order as scoOrder,
     * 	  iset.item_set_id as id,
     * 	  iset.item_set_name as title,
     * 	  siss.completion_status as completionStatus,
     * 	  iset.asmt_hash as asmtHash,
     * 	  iset.asmt_encryption_key as asmtEncryptionKey,
     * 	  iset.item_encryption_key as itemEncryptionKey,
     * 	  iset.ads_ob_asmt_id as adsid 
     * from student_item_set_status siss, item_set iset
     * where siss.item_set_id = iset.item_set_id
     * and siss.test_roster_id = {testRosterId}
     * and iset.item_set_level = 'L'::
     */
    @JdbcControl.SQL(statement = "select  siss.item_Set_order as scoOrder, \t  iset.item_set_id as id, \t  iset.item_set_name as title, \t  siss.completion_status as completionStatus, \t  iset.asmt_hash as asmtHash, \t  iset.asmt_encryption_key as asmtEncryptionKey, \t  iset.item_encryption_key as itemEncryptionKey, \t  iset.ads_ob_asmt_id as adsid  from student_item_set_status siss, item_set iset where siss.item_set_id = iset.item_set_id and siss.test_roster_id = {testRosterId} and iset.item_set_level = 'L'")
    ManifestData [] getTABELocatorManifest(int testRosterId) throws SQLException;


    /**
     * @jc:sql statement::
     * select
     * 	  scoOrder,
     * 	  scoParentId,
     * 	  adminForceLogout,
     * 	  showStudentFeedback,
     * 	  id,
     * 	  title,
     * 	  testTitle,
     * 	  scoDurationMinutes,
     * 	  0 as totalTime,
     * 	  scoUnitType,
     * 	  scoEntry,
     * 	  completionStatus,
     * 	  asmtHash,
     * 	  asmtEncryptionKey,
     * 	  itemEncryptionKey,
     * 	  adsid,
     *     accessCode	
     * from (
     *   select
     * 	  siss.item_Set_order as scoOrder,
     * 	  isp.parent_item_Set_id as scoParentId,
     * 	  ta.force_logout as adminForceLogout,
     * 	  ta.show_student_feedback as showStudentFeedback,
     * 	  iset.item_set_id as id,
     * 	  iset.item_set_name as title,
     * 	  test.item_set_name as testTitle,
     * 	  iset.time_limit / 60 as scoDurationMinutes,
     * 	  'SUBTEST' as scoUnitType,
     * 	  'ab-initio' as scoEntry,
     * 	  siss.completion_status as completionStatus,
     * 	  iset.asmt_hash as asmtHash,
     * 	  iset.asmt_encryption_key as asmtEncryptionKey,
     * 	  iset.item_encryption_key as itemEncryptionKey,
     * 	  iset.ads_ob_asmt_id as adsid,
     *     tais.access_code as accessCode
     *   from
     *       item_set_item isi,
     *       item_Set iset,
     *       item_set test,
     *       student_item_set_status siss,
     *       test_roster tr,
     *       test_admin ta,
     *       item_set_parent isp,
     *       test_admin_item_set tais
     *    where
     *        isi.item_set_id = iset.item_set_id
     *        and iset.item_set_id = siss.item_set_id
     *        and iset.item_set_type = 'TD'
     *        and siss.test_roster_id = tr.test_roster_id
     *        and ta.test_admin_id = tr.test_admin_id
     *        and isp.item_Set_id = iset.item_set_id
     *        and tr.test_roster_id = {testRosterId}
     *        and tais.item_set_id = isp.parent_item_set_id
     *        and test.item_set_id = ta.item_set_id
     *        and tais.test_admin_id = ta.test_admin_id
     *   group by 
     *         siss.item_Set_order, 
     *         isp.parent_item_set_id, 
     *         ta.force_logout,
     *         ta.show_student_feedback, 
     *         iset.item_Set_id, 
     *         iset.item_set_name,
     *         test.item_set_name,
     *         iset.time_limit,
     *         isi.item_sort_order,
     *         siss.completion_status,
     *         iset.asmt_hash,
     *         iset.asmt_encryption_key,
     *         iset.item_encryption_key,
     *         iset.ads_ob_asmt_id,
     *         tais.access_code)
     * group by scoOrder,
     * 	  scoParentId,
     * 	  adminForceLogout,
     * 	  showStudentFeedback,
     * 	  id,
     * 	  title,
     * 	  testTitle,
     * 	  scoDurationMinutes,
     * 	  scoUnitType,
     * 	  scoEntry,
     * 	  completionStatus,
     * 	  asmtHash,
     * 	  asmtEncryptionKey,
     * 	  itemEncryptionKey,
     * 	  adsid,
     *     accessCode
     * 	order by
     * 	  scoOrder::
     */
    @JdbcControl.SQL(statement = "select \t  scoOrder, \t  scoParentId, \t  adminForceLogout, \t  showStudentFeedback, \t  id, \t  title, \t  testTitle, \t  scoDurationMinutes, \t  0 as totalTime, \t  scoUnitType, \t  scoEntry, \t  completionStatus, \t  asmtHash, \t  asmtEncryptionKey, \t  itemEncryptionKey, \t  adsid,  accessCode\t from (  select \t  siss.item_Set_order as scoOrder, \t  isp.parent_item_Set_id as scoParentId, \t  ta.force_logout as adminForceLogout, \t  ta.show_student_feedback as showStudentFeedback, \t  iset.item_set_id as id, \t  iset.item_set_name as title, \t  test.item_set_name as testTitle, \t  iset.time_limit / 60 as scoDurationMinutes, \t  'SUBTEST' as scoUnitType, \t  'ab-initio' as scoEntry, \t  siss.completion_status as completionStatus, \t  iset.asmt_hash as asmtHash, \t  iset.asmt_encryption_key as asmtEncryptionKey, \t  iset.item_encryption_key as itemEncryptionKey, \t  iset.ads_ob_asmt_id as adsid,  tais.access_code as accessCode  from  item_set_item isi,  item_Set iset,  item_set test,  student_item_set_status siss,  test_roster tr,  test_admin ta,  item_set_parent isp,  test_admin_item_set tais  where  isi.item_set_id = iset.item_set_id  and iset.item_set_id = siss.item_set_id  and iset.item_set_type = 'TD'  and siss.test_roster_id = tr.test_roster_id  and ta.test_admin_id = tr.test_admin_id  and isp.item_Set_id = iset.item_set_id  and tr.test_roster_id = {testRosterId}  and tais.item_set_id = isp.parent_item_set_id  and test.item_set_id = ta.item_set_id  and tais.test_admin_id = ta.test_admin_id  group by  siss.item_Set_order,  isp.parent_item_set_id,  ta.force_logout,  ta.show_student_feedback,  iset.item_Set_id,  iset.item_set_name,  test.item_set_name,  iset.time_limit,  isi.item_sort_order,  siss.completion_status,  iset.asmt_hash,  iset.asmt_encryption_key,  iset.item_encryption_key,  iset.ads_ob_asmt_id,  tais.access_code) group by scoOrder, \t  scoParentId, \t  adminForceLogout, \t  showStudentFeedback, \t  id, \t  title, \t  testTitle, \t  scoDurationMinutes, \t  scoUnitType, \t  scoEntry, \t  completionStatus, \t  asmtHash, \t  asmtEncryptionKey, \t  itemEncryptionKey, \t  adsid,  accessCode \torder by \t  scoOrder")
    ManifestData [] getManifestByRoster(int testRosterId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select resource_uri 
     * from PRODUCT_RESOURCE 
     * where resource_type_code = 'TDCLOGO' 
     * and product_id = {productId}::
     */
    @JdbcControl.SQL(statement = "select resource_uri  from PRODUCT_RESOURCE  where resource_type_code = 'TDCLOGO'  and product_id = {productId}")
    String getProductLogo(Integer productId) throws SQLException;
    
    
  

    /**
     * @jc:sql statement::
     * select pr.resource_uri from product pp, product cp, product_resource pr
     * where pp.product_id = cp.parent_product_id
     * and pp.product_id = pr.product_id
     * and pr.resource_type_code = 'TUTORIAL'
     * and cp.product_id in (
     * select product_id from test_admin where test_admin_id in 
     * (select test_admin_id from test_roster where test_roster_id = {testRosterId}))::
     */
    @JdbcControl.SQL(statement = "select pr.resource_uri from product pp, product cp, product_resource pr where pp.product_id = cp.parent_product_id and pp.product_id = pr.product_id and pr.resource_type_code = 'TUTORIAL' and cp.product_id in ( select product_id from test_admin where test_admin_id in  (select test_admin_id from test_roster where test_roster_id = {testRosterId}))")
    String getTutorialResource(int testRosterId);

    /**
     * @jc:sql statement::
     * select count(*)
     * from test_roster tr, test_admin ta, student_tutorial_status sts
     * where tr.test_admin_id = ta.test_admin_id
     * and ta.product_id = sts.product_id
     * and tr.student_id = sts.student_id
     * and sts.completion_status = 'CO'
     * and tr.test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "select count(*) from test_roster tr, test_admin ta, student_tutorial_status sts where tr.test_admin_id = ta.test_admin_id and ta.product_id = sts.product_id and tr.student_id = sts.student_id and sts.completion_status = 'CO' and tr.test_roster_id = {testRosterId}")
    boolean wasTutotrialTaken(int testRosterId);

    /**
     * @jc:sql statement::
     * select scratchpad_content
     * from student_item_set_status
     * where test_roster_id = {testRosterId}
     * and item_set_id = {itemSetId}::
     */
    @JdbcControl.SQL(statement = "select scratchpad_content from student_item_set_status where test_roster_id = {testRosterId} and item_set_id = {itemSetId}")
    Clob getScratchpadContent(int testRosterId, int itemSetId) throws SQLException;

    /**
     * @jc:sql statement::
     * update
     *     test_roster
     * set
     *     random_distractor_seed = {rndNumber}
     * where
     *     test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "update  test_roster set  random_distractor_seed = {rndNumber} where  test_roster_id = {testRosterId}")
    void updateTestRosterWithRDSeed(int testRosterId, int rndNumber) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select cconfig.default_value as speechControllerFlag
     *    
     *   from test_roster            ros,
     *        customer               cus,
     *        customer_configuration cconfig,
     *        student_accommodation  accom
     *  where accom.screen_reader = 'T'
     *    and accom.student_id = ros.student_id
     *    and cconfig.customer_configuration_name = 'Allow_Speech_Controller'
     *    and cconfig.customer_id = cus.customer_id
     *    and cus.customer_id = ros.customer_id
     *       
     *    and ros.test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "select cconfig.default_value as speechControllerFlag  from test_roster  ros,  customer  cus,  customer_configuration cconfig,  student_accommodation  accom  where accom.screen_reader = 'T'  and accom.student_id = ros.student_id  and cconfig.customer_configuration_name = 'Allow_Speech_Controller'  and cconfig.customer_id = cus.customer_id  and cus.customer_id = ros.customer_id  and ros.test_roster_id = {testRosterId}")
    String getSpeechControllerAccommodation(int testRosterId) throws SQLException;
}