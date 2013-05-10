package com.ctb.control.db; 

import com.bea.control.*;
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import com.ctb.bean.testAdmin.StudentManifest;
import com.ctb.bean.testAdmin.StudentSessionStatus;
import com.ctb.bean.testAdmin.SubtestStatusCount;
import com.ctb.bean.testAdmin.TABERecommendedLevel; 
import java.sql.SQLException; 
import org.apache.beehive.controls.api.bean.ControlExtension;
import com.ctb.bean.testAdmin.AuditFileReopenSubtest;

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
public interface StudentItemSetStatus extends JdbcControl
{ 
    /** 
     * @jc:sql statement::
     * insert into student_item_set_status (
     *      select distinct 
     *          test_roster_id, {itemSetId}, {completionStatus}, null, null, 
     *          {validationStatus}, null, null, null, 
     *          {timeExpired}, {order}, null, null, null, null, null, max(default_value) as default_value 
     *      from (
     *              select distinct
     *                  test_roster_id, cc.default_value
     *              from 
     *                  test_roster,
     *                  customer_configuration cc 
     *              where 
     *                  cc.customer_id = {customerId}
     *                  and cc.customer_configuration_name = 'Roster_Status_Flag'
     *                  and test_admin_id in ({sql: adminList})
     *                  and student_id in ({sql: studentList})
     *              union
     *              select distinct
     *                  test_roster_id, null as default_value
     *              from 
     *                  test_roster 
     *              where 
     *                  test_admin_id in ({sql: adminList})
     *                  and student_id in ({sql: studentList}))
     *      group by test_roster_id)::
     *          
    */ 
    @JdbcControl.SQL(statement = "insert into student_item_set_status(TEST_ROSTER_ID,ITEM_SET_ID,COMPLETION_STATUS,START_DATE_TIME,COMPLETION_DATE_TIME,VALIDATION_STATUS,VALIDATION_UPDATED_BY,VALIDATION_UPDATED_DATE_TIME,VALIDATION_UPDATED_NOTE,TIME_EXPIRED,ITEM_SET_ORDER,RAW_SCORE,MAX_SCORE,UNSCORED,RECOMMENDED_LEVEL,SCRATCHPAD_CONTENT,CUSTOMER_FLAG_STATUS,EXEMPTIONS,ABSENT)  (  select distinct  test_roster_id, {itemSetId}, {completionStatus}, null, null,  {validationStatus}, null, null, null,  {timeExpired}, {order}, null, null, null, null, null,max(default_value) as default_value, {exemption}, {absent}  from (  select distinct  test_roster_id, cc.default_value  from  test_roster,  customer_configuration cc  where  cc.customer_id = {customerId}  and cc.customer_configuration_name = 'Roster_Status_Flag'  and test_admin_id in ({sql: adminList})  and student_id in ({sql: studentList})  union  select distinct  test_roster_id, null as default_value  from  test_roster  where  test_admin_id in ({sql: adminList})  and student_id in ({sql: studentList}))  group by test_roster_id)")
    void createNewStudentItemSetStatus(Integer customerId, String adminList, String studentList, Integer itemSetId, Integer order, String timeExpired, String validationStatus, String completionStatus,String exemption, String absent) throws SQLException;

    /** 
     * @jc:sql statement::
     * insert into
     *      student_item_set_status (
     *          TEST_ROSTER_ID,
     *          ITEM_SET_ID,
     *          COMPLETION_STATUS,
     *          START_DATE_TIME,
     *          COMPLETION_DATE_TIME,
     *          VALIDATION_STATUS,
     *          VALIDATION_UPDATED_BY,
     *          VALIDATION_UPDATED_DATE_TIME,
     *          VALIDATION_UPDATED_NOTE,
     *          TIME_EXPIRED,
     *          ITEM_SET_ORDER,
     *          CUSTOMER_FLAG_STATUS
     *      ) values (
     *          {testRosterId},
     *          {sss.itemSetId},
     *          {sss.completionStatus},
     *          {sss.startDateTime},
     *          {sss.completionDateTime},
     *          {sss.validationStatus},
     *          {sss.validationUpdatedBy},
     *          {sss.validationUpdatedDateTime},
     *          {sss.validationUpdatedNote},
     *          {sss.timeExpired},
     *          {sss.itemSetOrder},
     *          (select 
     *                  cc.default_value
     *              from 
     *                  customer_configuration cc 
     *              where 
     *                  cc.customer_id = {customerId}
     *                  and cc.customer_configuration_name = 'Roster_Status_Flag')
     *      )::
    */ 
    @JdbcControl.SQL(statement = "insert into  student_item_set_status (  TEST_ROSTER_ID,  ITEM_SET_ID,  COMPLETION_STATUS,  START_DATE_TIME,  COMPLETION_DATE_TIME,  VALIDATION_STATUS,  VALIDATION_UPDATED_BY,  VALIDATION_UPDATED_DATE_TIME,  VALIDATION_UPDATED_NOTE,  TIME_EXPIRED,  ITEM_SET_ORDER,  CUSTOMER_FLAG_STATUS  ) values (  {testRosterId},  {sss.itemSetId},  {sss.completionStatus},  {sss.startDateTime},  {sss.completionDateTime},  {sss.validationStatus},  {sss.validationUpdatedBy},  {sss.validationUpdatedDateTime},  {sss.validationUpdatedNote},  {sss.timeExpired},  {sss.itemSetOrder},  (select  cc.default_value  from  customer_configuration cc  where  cc.customer_id = {customerId}  and cc.customer_configuration_name = 'Roster_Status_Flag')  )")
    void createNewStudentItemSetStatusForRoster(Integer customerId, StudentSessionStatus sss, Integer testRosterId) throws SQLException;

    /** 
     * @jc:sql statement::
     * insert into
     *      student_item_set_status (
     *          TEST_ROSTER_ID,
     *          ITEM_SET_ID,
     *          COMPLETION_STATUS,
     *          START_DATE_TIME,
     *          COMPLETION_DATE_TIME,
     *          VALIDATION_STATUS,
     *          VALIDATION_UPDATED_BY,
     *          VALIDATION_UPDATED_DATE_TIME,
     *          VALIDATION_UPDATED_NOTE,
     *          TIME_EXPIRED,
     *          ITEM_SET_ORDER,
     *          CUSTOMER_FLAG_STATUS,
     *          EXEMPTIONS,
     *          ABSENT
     *      ) values (
     *          {testRosterId},
     *          {sss.itemSetId},
     *          {sss.completionStatus},
     *          {sss.startDateTime},
     *          {sss.completionDateTime},
     *          {sss.validationStatus},
     *          {sss.validationUpdatedBy},
     *          {sss.validationUpdatedDateTime},
     *          {sss.validationUpdatedNote},
     *          {sss.timeExpired},
     *          {sss.itemSetOrder},
     *          (select 
     *                  cc.default_value
     *              from 
     *                  customer_configuration cc 
     *              where 
     *                  cc.customer_id = {customerId}
     *                  and cc.customer_configuration_name = 'Roster_Status_Flag'),
     *          {sss.testExemptions}, 
     *          {sss.absent}        
     *      )::
    */ 
    @JdbcControl.SQL(statement = "insert into  student_item_set_status (  TEST_ROSTER_ID,  ITEM_SET_ID,  COMPLETION_STATUS,  START_DATE_TIME,  COMPLETION_DATE_TIME,  VALIDATION_STATUS,  VALIDATION_UPDATED_BY,  VALIDATION_UPDATED_DATE_TIME,  VALIDATION_UPDATED_NOTE,  TIME_EXPIRED,  ITEM_SET_ORDER,  CUSTOMER_FLAG_STATUS, EXEMPTIONS, ABSENT  ) values (  {testRosterId},  {sss.itemSetId},  {sss.completionStatus},  {sss.startDateTime},  {sss.completionDateTime},  {sss.validationStatus},  {sss.validationUpdatedBy},  {sss.validationUpdatedDateTime},  {sss.validationUpdatedNote},  {sss.timeExpired},  {sss.itemSetOrder},  (select  cc.default_value  from  customer_configuration cc  where  cc.customer_id = {customerId}  and cc.customer_configuration_name = 'Roster_Status_Flag'), {sss.testExemptions}, {sss.absent} )")
    void createStudentItemSetStatusForRosterOnFormChange(Integer customerId, StudentSessionStatus sss, Integer testRosterId) throws SQLException;

    /**
     * @jc:sql statement::
     * delete from 
     *      student_item_set_status 
     * where 
     *      test_roster_id in (
     *          select 
     *              test_roster_id 
     *          from 
     *              test_roster 
     *          where 
     *              test_completion_status in ('SC', 'NT') 
     *              and test_Admin_id = {testAdminId})::
     */
    @JdbcControl.SQL(statement = "delete from  student_item_set_status  where  test_roster_id in (  select  test_roster_id  from  test_roster  where  test_completion_status in ('SC', 'NT')  and test_Admin_id = {testAdminId})")
    void deleteStudentItemSetStatusesForAdmin(Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * delete from 
     *      student_item_set_status 
     * where 
     *      test_roster_id = {testRosterId}::
     */
    @JdbcControl.SQL(statement = "delete from  student_item_set_status  where  test_roster_id = {testRosterId}")
    void deleteStudentItemSetStatusesForRoster(Integer testRosterId) throws SQLException;


    /**
     * @jc:sql statement::
     * select distinct
     *          siss.TEST_ROSTER_ID as testRosterId,
     *          siss.ITEM_SET_ID as itemSetId,
     *          siss.COMPLETION_STATUS as completionStatus,
     *          siss.START_DATE_TIME as startDateTime,
     *          siss.COMPLETION_DATE_TIME as completionDateTime,
     *          siss.VALIDATION_STATUS as validationStatus,
     *          siss.VALIDATION_UPDATED_BY as validationUpdatedBy,
     *          siss.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,
     *          siss.VALIDATION_UPDATED_NOTE as validationUpdatedNote,
     *          siss.TIME_EXPIRED as timeExpired,
     *          siss.ITEM_SET_ORDER as itemSetOrder,
     *          siss.RAW_SCORE as rawScore,
     *          siss.MAX_SCORE as maxScore,
     *          siss.UNSCORED as unscored,
     *          siss.RECOMMENDED_LEVEL as recommendedLevel,
     *          siss.CUSTOMER_FLAG_STATUS as customerFlagStatus
     *          siss.TEST_EXEMPTIONS  as testExemption,
                siss.ABSENT as absent
                siss.INVALIDATION_REASON as  invalidationReason
     *          
     * from 
     *      student_item_set_status siss,
     *      test_roster ros 
     * where 
     *      siss.test_roster_id = ros.test_roster_id
     *      and ros.student_id = {studentId}
     *      and ros.test_admin_id = {testAdminId}::
     *      array-max-length="all"
     */
     
    @JdbcControl.SQL(statement = "select distinct  siss.TEST_ROSTER_ID as testRosterId,  siss.ITEM_SET_ID as itemSetId,  siss.COMPLETION_STATUS as completionStatus,  siss.START_DATE_TIME as startDateTime,  siss.COMPLETION_DATE_TIME as completionDateTime,  siss.VALIDATION_STATUS as validationStatus,  siss.VALIDATION_UPDATED_BY as validationUpdatedBy,  siss.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,  siss.VALIDATION_UPDATED_NOTE as validationUpdatedNote,  siss.TIME_EXPIRED as timeExpired,  siss.ITEM_SET_ORDER as itemSetOrder,  siss.RAW_SCORE as rawScore,  siss.MAX_SCORE as maxScore,  siss.UNSCORED as unscored,  siss.RECOMMENDED_LEVEL as recommendedLevel,  siss.CUSTOMER_FLAG_STATUS as customerFlagStatus ,siss.EXEMPTIONS as testExemptions,siss.ABSENT as absent, siss.INVALIDATION_REASON_ID as  invalidationReason from  student_item_set_status siss,  test_roster ros  where  siss.test_roster_id = ros.test_roster_id  and ros.student_id = {studentId}  and ros.test_admin_id ={testAdminId}",
                     arrayMaxLength = 100000)
    StudentSessionStatus [] getStudentItemSetStatusesForRoster(Integer studentId, Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     *          siss.TEST_ROSTER_ID as testRosterId,
     *          siss.ITEM_SET_ID as itemSetId,
     *          siss.COMPLETION_STATUS as completionStatus,
     *          siss.START_DATE_TIME as startDateTime,
     *          siss.COMPLETION_DATE_TIME as completionDateTime,
     *          siss.VALIDATION_STATUS as validationStatus,
     *          siss.VALIDATION_UPDATED_BY as validationUpdatedBy,
     *          siss.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,
     *          siss.VALIDATION_UPDATED_NOTE as validationUpdatedNote,
     *          siss.TIME_EXPIRED as timeExpired,
     *          siss.ITEM_SET_ORDER as itemSetOrder,
     *          siss.RAW_SCORE as rawScore,
     *          siss.MAX_SCORE as maxScore,
     *          siss.UNSCORED as unscored,
     *          siss.RECOMMENDED_LEVEL as recommendedLevel,
     *          siss.CUSTOMER_FLAG_STATUS as customerFlagStatus
     * from 
     *      student_item_set_status siss
     * where 
     *      siss.test_roster_id = {testRosterId}::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  siss.TEST_ROSTER_ID as testRosterId,  siss.ITEM_SET_ID as itemSetId,  siss.COMPLETION_STATUS as completionStatus,  siss.START_DATE_TIME as startDateTime,  siss.COMPLETION_DATE_TIME as completionDateTime,  siss.VALIDATION_STATUS as validationStatus,  siss.VALIDATION_UPDATED_BY as validationUpdatedBy,  siss.VALIDATION_UPDATED_DATE_TIME as validationUpdatedDateTime,  siss.VALIDATION_UPDATED_NOTE as validationUpdatedNote,  siss.TIME_EXPIRED as timeExpired,  siss.ITEM_SET_ORDER as itemSetOrder,  siss.RAW_SCORE as rawScore,  siss.MAX_SCORE as maxScore,  siss.UNSCORED as unscored,  siss.RECOMMENDED_LEVEL as recommendedLevel,  siss.CUSTOMER_FLAG_STATUS as customerFlagStatus from  student_item_set_status siss where  siss.test_roster_id = {testRosterId}",
                     arrayMaxLength = 100000)
    StudentSessionStatus [] getStudentItemSetStatuses(Integer testRosterId) throws SQLException;


    /**
     * @jc:sql statement::
     * update 
     *     student_item_set_status
     * set
     *      COMPLETION_STATUS = {sss.completionStatus},
     *      START_DATE_TIME = {sss.startDateTime},
     *      COMPLETION_DATE_TIME = {sss.completionDateTime},
     *      VALIDATION_STATUS = {sss.validationStatus},
     *      VALIDATION_UPDATED_BY = {sss.validationUpdatedBy},
     *      VALIDATION_UPDATED_DATE_TIME = {sss.validationUpdatedDateTime},
     *      VALIDATION_UPDATED_NOTE = {sss.validationUpdatedNote},
     *      TIME_EXPIRED = {sss.timeExpired},
     *      ITEM_SET_ORDER = {sss.itemSetOrder},
     *      RAW_SCORE = {sss.rawScore},
     *      MAX_SCORE = {sss.maxScore},
     *      UNSCORED = {sss.unscored},
     *      RECOMMENDED_LEVEL = {sss.recommendedLevel},
     *      CUSTOMER_FLAG_STATUS = {sss.customerFlagStatus}
     * where 
     *     test_roster_id = {sss.testRosterId}
     *     and item_set_id = {sss.itemSetId}::
     */
    @JdbcControl.SQL(statement = "update  student_item_set_status set  COMPLETION_STATUS = {sss.completionStatus},  START_DATE_TIME = {sss.startDateTime},  COMPLETION_DATE_TIME = {sss.completionDateTime},  VALIDATION_STATUS = {sss.validationStatus},  VALIDATION_UPDATED_BY = {sss.validationUpdatedBy},  VALIDATION_UPDATED_DATE_TIME = {sss.validationUpdatedDateTime},  VALIDATION_UPDATED_NOTE = {sss.validationUpdatedNote},  TIME_EXPIRED = {sss.timeExpired},  ITEM_SET_ORDER = {sss.itemSetOrder},  RAW_SCORE = {sss.rawScore},  MAX_SCORE = {sss.maxScore},  UNSCORED = {sss.unscored},  RECOMMENDED_LEVEL = {sss.recommendedLevel},  CUSTOMER_FLAG_STATUS = {sss.customerFlagStatus} where  test_roster_id = {sss.testRosterId}  and item_set_id = {sss.itemSetId}")
    void updateStudentItemSetStatus(StudentSessionStatus sss) throws SQLException;


    /**
     * @jc:sql statement::
     * select 
     * 	  tsiset.item_Set_id as itemSetId,
     * 	  tsiset.item_set_name as itemSetName,
     * 	  tdiset.item_set_form as itemSetForm,
     *    isp1.item_set_group as itemSetGroup,
     *    min(siss.ITEM_SET_ORDER) as itemSetOrder
     * from 
     * 	 student_item_set_status siss, 
     * 	 item_set tdiset, 
     * 	 item_Set_parent isp1, 
     * 	 item_set_parent isp2,
     * 	 item_Set tsiset,
     *   test_roster tr,
     *	 test_admin ta
     * where
     * 	 isp1.item_set_id = siss.item_set_id
     * 	 and tdiset.item_Set_id = siss.item_Set_id
     * 	 and tsiset.item_set_id = isp1.parent_item_set_id 
     *   and tsiset.item_set_type = 'TS'
     * 	 and isp1.parent_item_set_id = isp2.item_set_id
     *   and isp2.parent_item_set_id = ta.item_set_id    
     *   and ta.test_admin_id = tr.test_admin_id  
     *   and tr.test_roster_id = siss.test_roster_id 
     *   and tr.test_admin_id = {testAdminId}
     *   and tr.student_id = {studentId}
     * group by
     * 	  tsiset.item_Set_id,
     * 	  tsiset.item_set_name,
     * 	  tdiset.item_set_form,
     *     isp1.item_set_group
     * order by 				  
     * 	  min(siss.ITEM_SET_ORDER)::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  \t  tsiset.item_Set_id as itemSetId, \t  tsiset.item_set_name as itemSetName, \t  tdiset.item_set_form as itemSetForm,  isp1.item_set_group as itemSetGroup,  min(siss.ITEM_SET_ORDER) as itemSetOrder from  \t student_item_set_status siss,  \t item_set tdiset,  \t item_Set_parent isp1,  \t item_set_parent isp2, \t item_Set tsiset,  test_roster tr, \t test_admin ta where \t isp1.item_set_id = siss.item_set_id \t and tdiset.item_Set_id = siss.item_Set_id \t and tsiset.item_set_id = isp1.parent_item_set_id  and tsiset.item_set_type = 'TS' \t and isp1.parent_item_set_id = isp2.item_set_id  and isp2.parent_item_set_id = ta.item_set_id  and ta.test_admin_id = tr.test_admin_id  and tr.test_roster_id = siss.test_roster_id  and tr.test_admin_id = {testAdminId}  and tr.student_id = {studentId} group by \t  tsiset.item_Set_id, \t  tsiset.item_set_name, \t  tdiset.item_set_form,  isp1.item_set_group order by \t\t\t\t  \t  min(siss.ITEM_SET_ORDER)",
                     arrayMaxLength = 100000)
    StudentManifest [] getStudentManifestsForRoster(Integer studentId, Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct
     *          min(siss.ITEM_SET_ORDER) as itemSetOrder,
	 *          tsiset.item_set_id as itemSetId,
	 *          tsiset.item_set_name as itemSetName,
	 *          tdiset.item_set_form as itemSetForm,
     *          isp.item_set_group as itemSetGroup       
     * from 
	 *          student_item_set_status siss, 
	 *          item_set tdiset, 
	 *          item_Set_parent isp, 
     *          item_Set tsiset
     * where
	 *          siss.test_roster_id ={rosterId}
     *          and isp.item_set_id = siss.item_set_id
	 *          and tdiset.item_Set_id = siss.item_Set_id
	 *          and tsiset.item_set_id = isp.parent_item_set_id 
     * group by
	 *          tsiset.item_Set_id,
     *          tsiset.item_set_name,
	 *          tdiset.item_set_form,
     *          isp.item_set_group   
     * order by 				  
     *          min(siss.ITEM_SET_ORDER)::
     *          array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  min(siss.ITEM_SET_ORDER) as itemSetOrder,  tsiset.item_set_id as itemSetId,  tsiset.item_set_name as itemSetName,  tdiset.item_set_form as itemSetForm,  isp.item_set_group as itemSetGroup  from  student_item_set_status siss,  item_set tdiset,  item_Set_parent isp,  item_Set tsiset where  siss.test_roster_id ={rosterId}  and isp.item_set_id = siss.item_set_id  and tdiset.item_Set_id = siss.item_Set_id  and tsiset.item_set_id = isp.parent_item_set_id  group by  tsiset.item_Set_id,  tsiset.item_set_name,  tdiset.item_set_form,  isp.item_set_group  order by \t\t\t\t  min(siss.ITEM_SET_ORDER)",
                     arrayMaxLength = 100000)
    StudentManifest [] getManfestForRoster(Integer rosterId) throws SQLException;   
    
    /**
     * @jc:sql statement::
     * select 
     *         ros.TEST_COMPLETION_STATUS as tesCompletionStatus
     * from 
     *      test_roster ros
     * where   
     *      ros.student_id = {studentId}
     *      and ros.test_admin_id = {testAdminId}::
     */
    @JdbcControl.SQL(statement = "select  ros.TEST_COMPLETION_STATUS as tesCompletionStatus from  test_roster ros where  ros.student_id = {studentId}  and ros.test_admin_id = {testAdminId}")
    String geCompletionStatusForRoster(Integer studentId, Integer testAdminId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select 
     *         	iset.ITEM_SET_ID as itemSetId   
     * from 
     *         	item_set iset,
     *			item_set_parent isp
     * where  
     *			iset.item_set_id = isp.item_set_id
     *          and isp.parent_item_set_id = {parentItemSetId}
     *          and iset.item_set_form like {form}
     *  order by
     *          iset.item_set_form,
     *          isp.item_set_sort_order::
     * 			array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  \tiset.ITEM_SET_ID as itemSetId  from  \titem_set iset, \t\t\titem_set_parent isp where  \t\t\tiset.item_set_id = isp.item_set_id  and isp.parent_item_set_id = {parentItemSetId}  and iset.item_set_form like {form}  order by  iset.item_set_form,  isp.item_set_sort_order",
                     arrayMaxLength = 100000)
    Integer [] getItemSetIdsForFormForParent(Integer parentItemSetId, String form) throws SQLException;
   
   
    /**
     * @jc:sql statement::
     * update
     *      org_node_test_catalog ontc,
     * set       
     *      ontc.         
     *         org_node_ancestor ona
     * where  
     *			iset.item_set_id in	(
     *              select 
     *                  isp.item_set_id 
     *              from 
     *                  item_set_parent isp 
     *               where 
     *                  isp.parent_item_set_id ={parentItemSetId})
     * and 		iset.item_set_form = {form}::
     */
   
    @JdbcControl.SQL(statement = "update  org_node_test_catalog ontc, set  ontc.  org_node_ancestor ona where  \t\t\tiset.item_set_id in\t(  select  isp.item_set_id  from  item_set_parent isp  where  isp.parent_item_set_id ={parentItemSetId}) and \t\tiset.item_set_form = {form}")
    Integer getReservedLicForUser(Integer customerId, Integer productId, Integer userId,Integer parentItemSetId, String form);
    
    
    /**
     * @jc:sql statement::
     * select siss.item_set_id as itemSetId, 
     * 	ist.item_set_name as itemSetName, 
     * 	ta.test_admin_id as testAdminId, 
     * 	ta.test_admin_name as testAdminName, 
     * 	siss.completion_date_time as completedDate, 
     * 	siss.recommended_level as recommendedLevel
     * from student_item_set_status siss, test_roster ros,
     *      item_set ist, test_admin ta
     * where siss.test_roster_id = ros.test_roster_id
     * and ros.student_id = {studentId}
     * and ros.validation_status = 'VA'
     * and siss.completion_status = 'CO' 
     * and siss.recommended_level is not null
     * and siss.item_set_id = ist.item_set_id
     * and ros.test_admin_id = ta.test_admin_id
     * and siss.item_set_id in (
     * SELECT item_set_id FROM ITEM_SET_PARENT isp WHERE isp.parent_item_set_id = {locatorTSItemSetId} )::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select siss.item_set_id as itemSetId,  \tist.item_set_name as itemSetName,  \tta.test_admin_id as testAdminId,  \tta.test_admin_name as testAdminName,  \tsiss.completion_date_time as completedDate,  \tsiss.recommended_level as recommendedLevel from student_item_set_status siss, test_roster ros,  item_set ist, test_admin ta where siss.test_roster_id = ros.test_roster_id and ros.student_id = {studentId} and ros.validation_status = 'VA' and siss.completion_status = 'CO'  and siss.recommended_level is not null and siss.item_set_id = ist.item_set_id and ros.test_admin_id = ta.test_admin_id and siss.item_set_id in ( SELECT item_set_id FROM ITEM_SET_PARENT isp WHERE isp.parent_item_set_id = {locatorTSItemSetId} )",
                     arrayMaxLength = 100000)
    TABERecommendedLevel [] getTABERecommendedLevelForStudent(Integer studentId, Integer locatorTSItemSetId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select distinct 
     *      ts.item_set_id as itemSetId,
     *      ts.item_set_name as itemSetName     
     * from 
     *      item_set ts,
     *      item_set_parent isp,
     *      student_item_set_status siss,
     *      test_roster ros,
     *      test_admin ta   
     * where     
     *      siss.item_set_id = isp.item_set_id
     *      and ts.item_set_id = isp.parent_item_set_id
     *      and siss.test_roster_id = ros.test_roster_id
     *      and ros.test_admin_id = ta.test_admin_id
     *      and ta.test_admin_id ={testAdminId}::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  ts.item_set_id as itemSetId,  ts.item_set_name as itemSetName  from  item_set ts,  item_set_parent isp,  student_item_set_status siss,  test_roster ros,  test_admin ta  where  siss.item_set_id = isp.item_set_id  and ts.item_set_id = isp.parent_item_set_id  and siss.test_roster_id = ros.test_roster_id  and ros.test_admin_id = ta.test_admin_id  and ta.test_admin_id ={testAdminId}",
                     arrayMaxLength = 100000)
    StudentManifest [] getTotalStudentManifest(Integer testAdminId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select itemSetId, status, count (distinct rosterId) as rosterCount
     * from (
     * select itemSetId, rosterId, 
     * (case
     * when count(*) = 1 then max(status)
     * else 'Started'
     * end) as status from
     * (
     * select distinct 
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
     *     student_item_Set_status siss,
     *     student stu
     * where
     *     siss.test_roster_id = ros.test_roster_id
     *     and ros.test_admin_id = adm.test_admin_id
     *     and ros.student_id = stu.student_id
     *     and stu.activation_status = 'AC'
     *     and adm.program_id = {programId}
     *     and adm.item_set_id = {itemSetId}
     *     and ona.ancestor_org_node_id = {orgNodeId}
     *     and ros.org_node_id = ona.org_node_id
     *     and ros.activation_status = 'AC'
     *     and adm.activation_status = 'AC'
     *     and iset.item_set_id = isa.item_set_id
     *     and isa.ancestor_item_set_id = {itemSetId}
     *     and iset.item_set_type = 'TD'
     *     and isp.item_set_id = isa.item_set_id
     *     and siss.item_set_id = isa.item_set_id 
     *     group by
     *     ros.test_roster_id,
     *     isp.parent_item_set_id,
     *     decode(siss.completion_status, 'SC', 'Scheduled', 'CO', 'Completed', 'Started')
     *     )
     *     group by itemSetId, rosterId
     * )
     * group by itemSetId, status
     * order by itemSetId, status::
     */
    @JdbcControl.SQL(statement = "select itemSetId, status, count (distinct rosterId) as rosterCount from ( select itemSetId, rosterId,  (case when count(*) = 1 then max(status) else 'Started' end) as status from ( select distinct  ros.test_roster_id as rosterId,  isp.parent_item_set_id as itemSetId,  decode(siss.completion_status, 'SC', 'Scheduled', 'CO', 'Completed', 'Started') as status,  count(*) as counter  from  test_roster ros,  test_admin adm,  org_node_ancestor ona,  item_set_ancestor isa,  item_set iset,  item_set_parent isp,  student_item_Set_status siss  where  siss.test_roster_id = ros.test_roster_id  and ros.test_admin_id = adm.test_admin_id  and adm.program_id = {programId}  and adm.item_set_id = {itemSetId}  and ona.ancestor_org_node_id = {orgNodeId}  and ros.org_node_id = ona.org_node_id  and ros.activation_status = 'AC'  and adm.activation_status = 'AC'  and iset.item_set_id = isa.item_set_id  and isa.ancestor_item_set_id = {itemSetId}  and iset.item_set_type = 'TD'  and isp.item_set_id = isa.item_set_id  and siss.item_set_id = isa.item_set_id  group by  ros.test_roster_id,  isp.parent_item_set_id,  decode(siss.completion_status, 'SC', 'Scheduled', 'CO', 'Completed', 'Started')  )  group by itemSetId, rosterId ) group by itemSetId, status order by itemSetId, status")
    SubtestStatusCount [] getSubtestStatusCount(Integer programId, Integer orgNodeId, Integer itemSetId) throws SQLException;

    /**
     * @jc:sql statement::
     * select itemSetId, status, count (distinct rosterId) as rosterCount
     * from (
     * select distinct 
     *     ros.test_roster_id as rosterId,
     *     adm.item_set_id as itemSetId,
     *     decode(ros.test_completion_status, 'SC', 'Scheduled', 'NT', 'Scheduled','CO', 'Completed', 'Started') as status,
     *     count(*) as counter 
     * from
     *     test_roster ros,
     *     test_admin adm,
     *     org_node_ancestor ona
     * where
     *     ros.test_admin_id = adm.test_admin_id
     *     and adm.program_id = {programId}
     *     and adm.item_set_id = {itemSetId}
     *     and ona.ancestor_org_node_id = {orgNodeId}
     *     and ros.org_node_id = ona.org_node_id
     *     and ros.activation_status = 'AC'
     *     and adm.activation_status = 'AC'
     *     group by
     *     ros.test_roster_id,
     *     adm.item_set_id,
     *     decode(ros.test_completion_status, 'SC', 'Scheduled', 'NT', 'Scheduled','CO', 'Completed', 'Started')
     * )
     * group by itemSetId, status
     * order by itemSetId, status
     * ::
     */
    @JdbcControl.SQL(statement = "select itemSetId, status, count (distinct rosterId) as rosterCount from ( select distinct  ros.test_roster_id as rosterId,  adm.item_set_id as itemSetId,  decode(ros.test_completion_status, 'SC', 'Scheduled', 'NT', 'Scheduled','CO', 'Completed', 'Started') as status,  count(*) as counter  from  test_roster ros,  test_admin adm,  org_node_ancestor ona where  ros.test_admin_id = adm.test_admin_id  and adm.program_id = {programId}  and adm.item_set_id = {itemSetId}  and ona.ancestor_org_node_id = {orgNodeId}  and ros.org_node_id = ona.org_node_id  and ros.activation_status = 'AC'  and adm.activation_status = 'AC'  group by  ros.test_roster_id,  adm.item_set_id,  decode(ros.test_completion_status, 'SC', 'Scheduled', 'NT', 'Scheduled','CO', 'Completed', 'Started') ) group by itemSetId, status order by itemSetId, status")
    SubtestStatusCount [] getTestStatusCount(Integer programId, Integer orgNodeId, Integer itemSetId) throws SQLException;

    /**
     * @jc:sql statement::
     * update
     *      student_item_set_status
     * set
     *      validation_status = 
     *          decode(
     *              (select 
     *                  validation_status 
     *               from 
     *                  student_item_set_status 
     *               where 
     *                  test_roster_id = {testRosterId}
     *                  and item_set_id = {itemSetId}
     *               ),'VA','IN','VA')
     * where
     *      test_roster_id = {testRosterId}
     *      and item_set_id = {itemSetId} ::
     */
    @JdbcControl.SQL(statement = "update student_item_set_status set validation_status =  decode((select validation_status from student_item_set_status where test_roster_id = {testRosterId} and item_set_id = {itemSetId} ),'VA','IN','VA') where test_roster_id = {testRosterId} and item_set_id = {itemSetId}")
    void toggleSubtestValidationStatus(Integer testRosterId, Integer itemSetId) throws SQLException;
    
    @JdbcControl.SQL(statement = "update  student_item_set_status set  validation_status =  decode(  (select  validation_status  from  student_item_set_status  where  test_roster_id = {testRosterId}  and item_set_id = {itemSetId}  ),'VA','IN','VA'),  invalidation_reason_id = decode((select validation_status from student_item_set_status where test_roster_id = {testRosterId} and item_set_id = {itemSetId}), 'VA', {reason}, '') where  test_roster_id = {testRosterId}  and item_set_id = {itemSetId} ")
    void toggleSubtestValidationStatus(Integer testRosterId, Integer itemSetId,String reason) throws SQLException;

//START -ADDED for toggling values of exemption and absent
    @JdbcControl.SQL(statement = " update student_item_set_status siss  set siss.exemptions = decode((select exemptions from student_item_set_status where test_roster_id = {testRosterId} and item_set_id = {itemSetId}), 'Y', 'N', 'Y') where siss.test_roster_id = {testRosterId}  and siss.item_set_id = {itemSetId} ")
    void toggleSubtestExemptionStatus(Integer testRosterId, Integer itemSetId) throws SQLException;
    
    @JdbcControl.SQL(statement = " update student_item_set_status siss  set siss.absent = decode((select absent from student_item_set_status where test_roster_id = {testRosterId} and item_set_id = {itemSetId}), 'Y', 'N', 'Y') where siss.test_roster_id = {testRosterId}  and siss.item_set_id = {itemSetId} ")
    void toggleSubtestAbsentStatus(Integer testRosterId, Integer itemSetId) throws SQLException;

//END -ADDED for for toggling values of exemption and absent

    /**
     * @jc:sql statement::
     * select 
     * (case
     * when count(*) = 1 then max(validation_status)
     * else 'PI'
     * end) as status from
     * (select validation_status, count(*) as statusCount 
     * from student_item_set_status siss 
     * where siss.test_roster_id = {testRosterId}
     * group by validation_status)::
     */
    @JdbcControl.SQL(statement = "select  (case when count(*) = 1 then max(validation_status) else 'PI' end) as status from (select validation_status, count(*) as statusCount  from student_item_set_status siss  where siss.test_roster_id = {testRosterId} group by validation_status)")
    String getRosterValidationStatusFromSubtests(Integer testRosterId) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     * (case
     * when count(*) = 1 then max(customer_flag_status)
     * else 'PI'
     * end) as status from
     * (select customer_flag_status, count(*) as statusCount 
     * from student_item_set_status siss 
     * where siss.test_roster_id = {testRosterId}
     * group by customer_flag_status)
     * ::
     */
    @JdbcControl.SQL(statement = "select  (case when count(*) = 1 then max(customer_flag_status) else 'PI' end) as status from (select customer_flag_status, count(*) as statusCount  from student_item_set_status siss  where siss.test_roster_id = {testRosterId} group by customer_flag_status)")
	String  getRosterCustomerFlagStatusFromSubtests(Integer testRosterId) throws SQLException;
    
    /**
	 * 
	 * For ISTEP CR003
	 */
    @JdbcControl.SQL(statement = " select distinct siss.TEST_ROSTER_ID as testRosterId, siss.ITEM_SET_ID as itemSetId, tcsc.test_completion_status_desc as completionStatus, siss.START_DATE_TIME as startDateTime, siss.COMPLETION_DATE_TIME as completionDateTime, siss.TIME_EXPIRED as timeExpired, siss.ITEM_SET_ORDER as itemSetOrder, siss.RAW_SCORE as rawScore, siss.MAX_SCORE as maxScore, siss.UNSCORED as unscored, its.item_set_level as itemSetLevel, its.item_set_name as itemSetName, (select nvl(sum(max(resp.response_elapsed_time)), 0) from item_response resp where resp.test_roster_id = ros.test_roster_id and resp.item_set_id = its.item_set_id group by resp.test_roster_id, resp.item_set_id, resp.item_id) as timeSpent, (select  count (isi.item_id) from item_set_item isi, ITEM IT where isi.item_set_id = its.item_set_id  AND IT.ITEM_ID = ISI.ITEM_ID  AND IT.ITEM_TYPE != 'NI' group by isi.item_set_id) totalItem,  (NVL( (select  count(distinct irs.item_id) from item_response irs , ITEM IT where irs.item_set_id = its.item_set_id and irs.test_roster_id = ros.test_roster_id and irs.response is not null AND IRS.ITEM_ID = IT.ITEM_ID AND IT.ITEM_TYPE != 'NI' group by irs.test_roster_id, irs.item_set_id) , 0) + NVL((SELECT COUNT(1) CR_RSP_CNT  FROM ITEM_RESPONSE_CR IRC , ITEM IT WHERE IRC.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID           AND IRC.ITEM_SET_ID = ITS.ITEM_SET_ID  AND IRC.CONSTRUCTED_RESPONSE IS NOT NULL AND IRC.ITEM_ID = IT.ITEM_ID AND IT.ITEM_TYPE != 'NI' AND  IT.Answer_Area IS NULL AND INSTR(CONSTRUCTED_RESPONSE, 'CDATA') > 0),  0) + + NVL((SELECT COUNT(1) CR_RSP_CNT  FROM ITEM_RESPONSE_CR IRC , ITEM IT WHERE IRC.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID           AND IRC.ITEM_SET_ID = ITS.ITEM_SET_ID  AND IRC.CONSTRUCTED_RESPONSE IS NOT NULL AND IRC.ITEM_ID = IT.ITEM_ID AND IT.ITEM_TYPE != 'NI' AND  IT.Answer_Area IS NOT null ),  0)) as itemAnswered , tais.access_code as testAccessCode  from student_item_set_status siss, item_set its, test_roster ros, test_completion_status_code tcsc , test_admin_item_set tais,item_set_parent isp where   tais.item_set_id = isp.parent_item_set_id and isp.item_set_id= siss.item_set_id and tais.test_admin_id = ros.test_admin_id and siss.test_roster_id = ros.test_roster_id and ros.test_roster_id = {testRosterId} and  siss.item_set_id = its.item_set_id and siss.completion_status = tcsc.test_completion_status {sql: searchCriteria} order by itemSetName, siss.item_set_order ")
	StudentSessionStatus [] getSubtestListForRoster(Integer testRosterId, String searchCriteria) throws SQLException;
         
	/**
	 * 
	 * For ISTEP CR003 
	 * to get student subtestDetails
	 */
	@JdbcControl.SQL(statement =" select distinct siss.TEST_ROSTER_ID as testRosterId, siss.ITEM_SET_ID as itemSetId, tcsc.test_completion_status_desc as completionStatus, siss.START_DATE_TIME as startDateTime, siss.COMPLETION_DATE_TIME as completionDateTime, siss.ITEM_SET_ORDER as itemSetOrder, siss.RAW_SCORE as rawScore, siss.MAX_SCORE as maxScore, siss.UNSCORED as unscored, its.item_set_level as itemSetLevel, its.item_set_name as itemSetName, concat(concat(std.last_name, ', '), concat(std.first_name, concat(' ', std.MIDDLE_NAME))) as studentName, std.user_name as studentLoginName, std.ext_pin1 as externalStudentId, std.student_id as studentId , ong.org_node_name as org_name,  (select nvl(sum(max(resp.response_elapsed_time)), 0) from item_response resp where resp.test_roster_id = ros.test_roster_id and resp.item_set_id = its.item_set_id group by resp.test_roster_id, resp.item_set_id, resp.item_id) as timeSpent, (select count(isi.item_id) from item_set_item isi  , item it where isi.item_set_id = its.item_set_id  and it.item_id = isi.item_id and it.item_type != 'NI' group by isi.item_set_id) totalItem, (NVL(( select count(distinct irs.item_id) from item_response irs , ITEM IT where irs.item_set_id = its.item_set_id and irs.test_roster_id = ros.test_roster_id and irs.response is not null AND IRS.ITEM_ID = IT.ITEM_ID AND IT.ITEM_TYPE != 'NI' group by irs.test_roster_id, irs.item_set_id),0)  + NVL( ( SELECT COUNT(1) cr_rsp_cnt FROM ITEM_RESPONSE_CR IRC , ITEM IT  WHERE IRC.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID    AND IRC.ITEM_SET_ID = ITS.ITEM_SET_ID    AND IRC.CONSTRUCTED_RESPONSE IS NOT NULL  AND IRC.ITEM_ID = IT.ITEM_ID   AND IT.ITEM_TYPE != 'NI' AND IT.Answer_Area IS NULL AND INSTR(CONSTRUCTED_RESPONSE,  'CDATA') > 0 ) ,0)   + NVL( ( SELECT COUNT(1) cr_rsp_cnt FROM ITEM_RESPONSE_CR IRC , ITEM IT  WHERE IRC.TEST_ROSTER_ID = ROS.TEST_ROSTER_ID    AND IRC.ITEM_SET_ID = ITS.ITEM_SET_ID    AND IRC.CONSTRUCTED_RESPONSE IS NOT NULL  AND IRC.ITEM_ID = IT.ITEM_ID   AND IT.ITEM_TYPE != 'NI' AND IT.Answer_Area IS NOT NULL ) ,0) ) as itemAnswered from student_item_set_status siss, item_set its, test_roster ros, test_completion_status_code tcsc, student std, item_set_parent itsp, org_node ong where ros.test_admin_id = {testAdminId} and ros.student_id = std.student_id and std.activation_status = 'AC' and ros.org_node_id = ong.org_node_id and ros.test_roster_id = siss.test_roster_id and siss.item_set_id = itsp.item_set_id and itsp.parent_item_set_id = {itemSetId} and siss.item_set_id = its.item_set_id and siss.completion_status = tcsc.test_completion_status order by CONCAT(CONCAT(STD.LAST_NAME, ', '), CONCAT(STD.FIRST_NAME, CONCAT(' ', STD.MIDDLE_NAME))) ")
    StudentSessionStatus [] getRosterListForSubTest(Integer testAdminId, Integer itemSetId) throws SQLException;
   	
	/**
	 * 
	 * For ISTEP CR003
	 */
	@JdbcControl.SQL(statement ="update student_item_set_status set completion_status = 'IN' where test_roster_id = {testRosterId} and item_set_id = {itemSetId} ")
    void updateStudentItemSetStatus(Integer testRosterId,Integer itemSetId) throws SQLException;
   	
	/**
	 * 
	 * For ISTEP CR003
	 */
	@JdbcControl.SQL(statement =" insert into audit_file_reopen_subtest ( AUDIT_ID, CUSTOMER_ID, ORG_NODE_ID, TEST_ADMIN_ID, STUDENT_ID, TEST_ROSTER_ID, ITEM_SET_TS_ID, ITEM_SET_TD_ID, OLD_ROSTER_COMPLETION_STATUS, NEW_ROSTER_COMPLETION_STATUS, OLD_SUBTEST_COMPLETION_STATUS, NEW_SUBTEST_COMPLETION_STATUS, TICKET_ID, REQUESTOR_NAME, REASON_FOR_REQUEST, CREATED_BY, CREATED_DATE_TIME, COMPLETION_DATE_TIME, START_DATE_TIME, ITEM_ANSWERED, TIME_SPENT ) values ( seq_audit_id.nextval, {auditFileReopenSubtest.customerId}, {auditFileReopenSubtest.orgNodeId}, {auditFileReopenSubtest.testAdminId}, {auditFileReopenSubtest.studentId}, {auditFileReopenSubtest.testRosterId}, {auditFileReopenSubtest.itemSetTSId}, {auditFileReopenSubtest.itemSetTDId}, {auditFileReopenSubtest.oldSRosterCompStatus}, {auditFileReopenSubtest.newRosterCompStatus}, {auditFileReopenSubtest.oldSubtestCompStatus}, {auditFileReopenSubtest.newSubtestCompStatus}, {auditFileReopenSubtest.ticketId}, {auditFileReopenSubtest.requestorName}, {auditFileReopenSubtest.reasonForRequest}, {auditFileReopenSubtest.createdBy}, {auditFileReopenSubtest.createdDateTime}, {auditFileReopenSubtest.completionDateTime}, {auditFileReopenSubtest.startDateTime}, {auditFileReopenSubtest.itemAnswered}, {auditFileReopenSubtest.timeSpent} )")
    void insertAuditRecordForReopenSubtestData(AuditFileReopenSubtest auditFileReopenSubtest) throws SQLException;
   	
	@JdbcControl.SQL(statement = "select  \t  tsiset.item_Set_id as itemSetId, \t  tsiset.item_set_name as itemSetName, \t  tdiset.item_set_form as itemSetForm,  isp1.item_set_group as itemSetGroup,  min(siss.ITEM_SET_ORDER) as itemSetOrder from  \t student_item_set_status siss,  \t item_set tdiset,  \t item_Set_parent isp1,  \t item_set_parent isp2, \t item_Set tsiset,  test_roster tr, \t test_admin ta where \t isp1.item_set_id = siss.item_set_id \t and tdiset.item_Set_id = siss.item_Set_id \t and tsiset.item_set_id = isp1.parent_item_set_id  and tsiset.item_set_type = 'TD' and tsiset.item_set_level = 'L' \t and isp1.parent_item_set_id = isp2.item_set_id  and isp2.parent_item_set_id = ta.item_set_id  and ta.test_admin_id = tr.test_admin_id  and tr.test_roster_id = siss.test_roster_id  and tr.test_admin_id = {testAdminId}  and tr.student_id = {studentId} group by \t  tsiset.item_Set_id, \t  tsiset.item_set_name, \t  tdiset.item_set_form,  isp1.item_set_group order by \t\t\t\t  \t  min(siss.ITEM_SET_ORDER)",
            arrayMaxLength = 100000)
     StudentManifest [] getStudentLocatorManifestsForRoster(Integer studentId, Integer testAdminId) throws SQLException;

	@JdbcControl.SQL(statement= "SELECT ISET.ITEM_SET_ID    AS itemSetId, ISET.ITEM_SET_NAME  AS itemSetName ,ISET.ITEM_SET_FORM  AS itemSetForm, SISS.ITEM_SET_ORDER AS itemSetOrder FROM ITEM_SET     ISET, ITEM_SET_PARENT    ISP,  STUDENT_ITEM_SET_STATUS    SISS,  TEST_ROSTER             TR WHERE ISP.PARENT_ITEM_SET_ID = {itemSetIdTS} AND ISET.ITEM_SET_ID = ISP.ITEM_SET_ID AND SISS.ITEM_SET_ID = ISET.ITEM_SET_ID AND SISS.TEST_ROSTER_ID = TR.TEST_ROSTER_ID AND TR.TEST_ADMIN_ID = {testAdminId} ", arrayMaxLength = 100000)
	StudentManifest [] getLocatorTD(Integer testAdminId, Integer itemSetIdTS) throws SQLException;
	
    static final long serialVersionUID = 1L;

}   
 