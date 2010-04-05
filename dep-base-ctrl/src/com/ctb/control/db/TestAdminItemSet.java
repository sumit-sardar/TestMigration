package com.ctb.control.db; 

import com.bea.control.*;
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import com.ctb.bean.testAdmin.ScheduleElement; 
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
public interface TestAdminItemSet extends JdbcControl
{ 
    /** 
     * @jc:sql statement::
     * insert into
     *      test_admin_item_set (
     *          ITEM_SET_ID,
     *          TEST_ADMIN_ID,
     *          ITEM_SET_ORDER,
     *          ACCESS_CODE,
     *			ITEM_SET_FORM,
     *			SESSION_DEFAULT
     *      ) values (
     *          {se.itemSetId},
     *          {se.testAdminId},
     *          {se.itemSetOrder},
     *          {se.accessCode},
     *			{se.itemSetForm},
     *			{se.sessionDefault}
     *      )::
    */ 
    @JdbcControl.SQL(statement = "insert into  test_admin_item_set (  ITEM_SET_ID,  TEST_ADMIN_ID,  ITEM_SET_ORDER,  ACCESS_CODE, \t\t\tITEM_SET_FORM, \t\t\tSESSION_DEFAULT  ) values (  {se.itemSetId},  {se.testAdminId},  {se.itemSetOrder},  {se.accessCode}, \t\t\t{se.itemSetForm}, \t\t\t{se.sessionDefault}  )")
    void createNewTestAdminItemSet(ScheduleElement se) throws SQLException;

    /** 
     * @jc:sql statement::
     * update
     *      test_admin_item_set set
     *          ITEM_SET_ORDER = {se.itemSetOrder},
     *          ACCESS_CODE = {se.accessCode},
     *          ITEM_SET_FORM = {se.itemSetForm},
     *          SESSION_DEFAULT = {se.sessionDefault}
     * where
     *      test_admin_id = {se.testAdminId}
     *      and item_set_id = {se.itemSetId}::
    */ 
    @JdbcControl.SQL(statement = "update  test_admin_item_set set  ITEM_SET_ORDER = {se.itemSetOrder},  ACCESS_CODE = {se.accessCode},  ITEM_SET_FORM = {se.itemSetForm},  SESSION_DEFAULT = {se.sessionDefault} where  test_admin_id = {se.testAdminId}  and item_set_id = {se.itemSetId}")
    void updateTestAdminItemSet(ScheduleElement se) throws SQLException;


    /**
     * @jc:sql statement::
     * select distinct
     *     tais.ITEM_SET_ID as itemSetId,
     *     tais.TEST_ADMIN_ID as testAdminId,
     *     tais.ITEM_SET_ORDER as itemSetOrder,
     *     tais.ACCESS_CODE as accessCode,    
     *     max(decode(status.item_Set_id, null, 'F', 'T')) as tested,
     *	   tais.ITEM_SET_FORM as itemSetForm,
     *	   tais.SESSION_DEFAULT as sessionDefault
     * from
     *     test_admin_item_set tais,
     *     item_Set_parent isp,
     *     (select distinct 
     *          siss.item_set_id 
     *      from 
     *          student_item_set_status siss, 
     *          test_roster ros 
     *      where 
     *          siss.completion_status != 'SC' 
     *          and siss.test_roster_id = ros.test_roster_id 
     *          and ros.test_admin_id = {testAdminId}) status
     * where
     *     tais.test_admin_id = {testAdminId}
     *     and isp.parent_item_set_id = tais.item_set_id
     *     and status.item_Set_id (+) = isp.item_Set_id
     * group by
     *     tais.ITEM_SET_ID,
     *     tais.TEST_ADMIN_ID,
     *     tais.ITEM_SET_ORDER,
     *     tais.ACCESS_CODE,    
     *     tais.ITEM_SET_FORM,
     *     tais.SESSION_DEFAULT
     * order by tais.ITEM_SET_ORDER::
     *     array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  tais.ITEM_SET_ID as itemSetId,  tais.TEST_ADMIN_ID as testAdminId,  tais.ITEM_SET_ORDER as itemSetOrder,  tais.ACCESS_CODE as accessCode,  max(decode(status.item_Set_id, null, 'F', 'T')) as tested, \t  tais.ITEM_SET_FORM as itemSetForm, \t  tais.SESSION_DEFAULT as sessionDefault from  test_admin_item_set tais,  item_Set_parent isp,  (select distinct  siss.item_set_id  from  student_item_set_status siss,  test_roster ros  where  siss.completion_status != 'SC'  and siss.test_roster_id = ros.test_roster_id  and ros.test_admin_id = {testAdminId}) status where  tais.test_admin_id = {testAdminId}  and isp.parent_item_set_id = tais.item_set_id  and status.item_Set_id (+) = isp.item_Set_id group by  tais.ITEM_SET_ID,  tais.TEST_ADMIN_ID,  tais.ITEM_SET_ORDER,  tais.ACCESS_CODE,  tais.ITEM_SET_FORM,  tais.SESSION_DEFAULT order by tais.ITEM_SET_ORDER",
                     arrayMaxLength = 100000)
    ScheduleElement [] getTestAdminItemSetsForAdmin(Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * delete from 
     *     test_admin_item_set tais 
     * where 
     *     test_Admin_id = {testAdminId}
     *     and not exists (
     *         select 
     *              siss.test_roster_id 
     *         from 
     *              student_item_set_status siss, 
     *              item_set_parent isp,
     *              test_roster ros
     *          where 
     *              ros.test_roster_id = siss.test_roster_id
     *              and ros.test_admin_id = {testAdminId}
     *              and siss.item_Set_id = isp.item_set_id
     *              and tais.item_set_id = isp.parent_item_set_id
     *              and siss.completion_status != 'SC')::
     */
    @JdbcControl.SQL(statement = "delete from  test_admin_item_set tais  where  test_Admin_id = {testAdminId}  and not exists (  select  siss.test_roster_id  from  student_item_set_status siss,  item_set_parent isp,  test_roster ros  where  ros.test_roster_id = siss.test_roster_id  and ros.test_admin_id = {testAdminId}  and siss.item_Set_id = isp.item_set_id  and tais.item_set_id = isp.parent_item_set_id  and siss.completion_status != 'SC')")
    void deleteTestAdminItemSetsForAdmin(Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * delete from 
     *     test_admin_item_set
     * where 
     *     test_Admin_id = {se.testAdminId}
     *     and item_set_id = {se.itemSetId}::
     */
    @JdbcControl.SQL(statement = "delete from  test_admin_item_set where  test_Admin_id = {se.testAdminId}  and item_set_id = {se.itemSetId}")
    void deleteTestAdminItemSet(ScheduleElement se) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct
     *     tais.ITEM_SET_ID as itemSetId,
     *     tais.TEST_ADMIN_ID as testAdminId,
     *     tais.ITEM_SET_ORDER as itemSetOrder,
     *     tais.ACCESS_CODE as accessCode,
     *	   tais.ITEM_SET_FORM as itemSetForm,
     *	   tais.SESSION_DEFAULT as sessionDefault	
     * from
     *     test_admin_item_set tais     
     * where
     *     tais.test_admin_id = {testAdminId}
     * order by tais.ITEM_SET_ORDER::
     *     array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  tais.ITEM_SET_ID as itemSetId,  tais.TEST_ADMIN_ID as testAdminId,  tais.ITEM_SET_ORDER as itemSetOrder,  tais.ACCESS_CODE as accessCode, \t  tais.ITEM_SET_FORM as itemSetForm, \t  tais.SESSION_DEFAULT as sessionDefault\t from  test_admin_item_set tais  where  tais.test_admin_id = {testAdminId} order by tais.ITEM_SET_ORDER",
                     arrayMaxLength = 100000)
    ScheduleElement [] getTestAdminItemSets(Integer testAdminId) throws SQLException;
    
    /** 
     * @jc:sql statement::
     * update
     *      test_admin_item_set set
     *          ITEM_SET_ORDER = {se.itemSetOrder}
     *                 
     * where
     *      test_admin_id = {se.testAdminId}
     *      and item_set_id = {se.itemSetId}::
    */ 
    @JdbcControl.SQL(statement = "update  test_admin_item_set set  ITEM_SET_ORDER = {se.itemSetOrder}  where  test_admin_id = {se.testAdminId}  and item_set_id = {se.itemSetId}")
    void updateTestAdminItemSetOrder(ScheduleElement se) throws SQLException;

    @JdbcControl.SQL(statement = " select ta.test_admin_id as testAdminId, tais.item_set_id as itemSetId, ta.customer_id as customerId , ta.creator_org_node_id as creatorOrgNodeId,  its.item_set_name as itemSetName, tais.item_set_order as itemSetOrder, tais.access_code as accessCode, tais.item_set_form as itemSetForm from test_admin ta, test_admin_item_set tais, item_set its, customer_configuration cc where ta.activation_status = 'AC' and ta.test_admin_status = 'CU' and ta.test_admin_id = tais.test_admin_id and tais.item_set_id = its.item_set_id and ta.customer_id = cc.customer_id and cc.customer_configuration_name = 'Allow_Reopen_Subtest' and cc.default_value = 'T' and ta.test_admin_id in (select distinct tais.test_admin_id from test_admin_item_set tais where upper(tais.access_code) = upper({accessCode}))",
            arrayMaxLength = 100000)
    ScheduleElement [] getSubTestListForTestSession(String accessCode) throws SQLException;
       

    static final long serialVersionUID = 1L;
}