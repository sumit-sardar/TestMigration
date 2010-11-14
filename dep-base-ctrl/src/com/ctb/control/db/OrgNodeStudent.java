package com.ctb.control.db; 

import com.bea.control.*; 
import org.apache.beehive.controls.system.jdbc.JdbcControl;
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
 * @author John_Wang
 * 
 * @jc:connection data-source-jndi-name="oasDataSource" 
 */ 
@ControlExtension()
@JdbcControl.ConnectionDataSource(jndiName = "oasDataSource")
public interface OrgNodeStudent extends JdbcControl
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
     * insert into
     *     org_node_student (
     * 		student_id,
     * 		org_node_id,
     * 		created_date_time,
     * 		created_by,
     * 		customer_id,
     * 		updated_by,
     * 		updated_date_time,
     * 		activation_status,
     * 		data_import_history_id
     *        ) values (
     * 		{ons.studentId},
     * 		{ons.orgNodeId},
     * 		{ons.createdDateTime},
     * 		{ons.createdBy},
     * 		{ons.customerId},
     * 		{ons.updatedBy},
     * 		{ons.updatedDateTime},
     * 		{ons.activationStatus},
     * 		{ons.dataImportHistoryId}
     * 	   )::
    */ 
    @JdbcControl.SQL(statement = "insert into  org_node_student ( \t\tstudent_id, \t\torg_node_id, \t\tcreated_date_time, \t\tcreated_by, \t\tcustomer_id, \t\tupdated_by, \t\tupdated_date_time, \t\tactivation_status, \t\tdata_import_history_id  ) values ( \t\t{ons.studentId}, \t\t{ons.orgNodeId}, \t\t{ons.createdDateTime}, \t\t{ons.createdBy}, \t\t{ons.customerId}, \t\t{ons.updatedBy}, \t\t{ons.updatedDateTime}, \t\t{ons.activationStatus}, \t\t{ons.dataImportHistoryId} \t  )")
    void createOrgNodeStudent(com.ctb.bean.testAdmin.OrgNodeStudent ons) throws SQLException;

    /** 
     * @jc:sql statement::
     * update 
     *     org_node_student
     * set 	
     * 		customer_id={ons.customerId},
     * 		updated_by={ons.updatedBy},
     * 		updated_date_time={ons.updatedDateTime},
     * 		activation_status={ons.activationStatus},
     * 		data_import_history_id={ons.dataImportHistoryId}
     * where
     * 		student_id={ons.studentId} 
     * 		and org_node_id={ons.orgNodeId}::
    */ 
    @JdbcControl.SQL(statement = "update  org_node_student set \t \t\tcustomer_id={ons.customerId}, \t\tupdated_by={ons.updatedBy}, \t\tupdated_date_time={ons.updatedDateTime}, \t\tactivation_status={ons.activationStatus}, \t\tdata_import_history_id={ons.dataImportHistoryId} where \t\tstudent_id={ons.studentId}  \t\tand org_node_id={ons.orgNodeId}")
    void updateOrgNodeStudent(com.ctb.bean.testAdmin.OrgNodeStudent ons) throws SQLException;


    /** 
     * @jc:sql statement::
     * select
     * 	student_id as studentId,
     * 	org_node_id as orgNodeId,
     * 	created_date_time as createdDateTime,
     * 	created_by as createdBy,
     * 	customer_id as customerId,
     * 	updated_by as updatedBy, 
     * 	updated_date_time as updatedDateTime,
     * 	activation_status as activationStatus, 
     * 	data_import_history_id as dataImportHistoryId
     * from 
     *     org_node_student
     * where student_id = {studentId}
     * ::
    */ 
    @JdbcControl.SQL(statement = "select \tstudent_id as studentId, \torg_node_id as orgNodeId, \tcreated_date_time as createdDateTime, \tcreated_by as createdBy, \tcustomer_id as customerId, \tupdated_by as updatedBy,  \tupdated_date_time as updatedDateTime, \tactivation_status as activationStatus,  \tdata_import_history_id as dataImportHistoryId from  org_node_student where student_id = {studentId}")
    com.ctb.bean.testAdmin.OrgNodeStudent [] getOrgNodeStudentForStudent(Integer studentId) throws SQLException;

    /** 
     * @jc:sql statement::
     * select
     * 	student_id as studentId,
     * 	org_node_id as orgNodeId,
     * 	created_date_time as createdDateTime,
     * 	created_by as createdBy,
     * 	customer_id as customerId,
     * 	updated_by as updatedBy, 
     * 	updated_date_time as updatedDateTime,
     * 	activation_status as activationStatus, 
     * 	data_import_history_id as dataImportHistoryId
     * from 
     *     org_node_student
     * where student_id = {studentId}
     *      and org_node_id in (select distinct ona.org_node_id from org_node_ancestor ona 
     *      where {sql: searchCriteria} 
     * 	 )
     * ::
    */ 
    @JdbcControl.SQL(statement = "select \tstudent_id as studentId, \torg_node_id as orgNodeId, \tcreated_date_time as createdDateTime, \tcreated_by as createdBy, \tcustomer_id as customerId, \tupdated_by as updatedBy,  \tupdated_date_time as updatedDateTime, \tactivation_status as activationStatus,  \tdata_import_history_id as dataImportHistoryId from  org_node_student where student_id = {studentId}  and org_node_id in (select distinct ona.org_node_id from org_node_ancestor ona  where {sql: searchCriteria}  \t )")
    com.ctb.bean.testAdmin.OrgNodeStudent [] getOrgNodeStudentForStudentAtAndBelowOrgNodes(Integer studentId, String searchCriteria) throws SQLException;

    /**
     * @jc:sql statement::
     * delete from 
     *      org_node_student 
     * where 
     *      student_id = {studentId}
     * ::
     */
    @JdbcControl.SQL(statement = "delete from  org_node_student  where  student_id = {studentId}")
    void deleteOrgNodeStudentForStudent(Integer studentId) throws SQLException;

    /**
     * @jc:sql statement::
     * delete from 
     *      org_node_student 
     * where 
     *      student_id = {studentId}
     *      and org_node_id = {orgNodeId}
     * ::
     */
    @JdbcControl.SQL(statement = "delete from  org_node_student  where  student_id = {studentId}  and org_node_id = {orgNodeId}")
    void deleteOrgNodeStudentForStudentAndOrgNode(Integer studentId, Integer orgNodeId) throws SQLException;

    /**
     * @jc:sql statement::
     * update org_node_student
     *      set activation_status = 'IN' 
     * where 
     *      student_id = {studentId}::
     */
    @JdbcControl.SQL(statement = "update org_node_student  set activation_status = 'IN'  where  student_id = {studentId}")
    void deactivateOrgNodeStudentForStudent(Integer studentId) throws SQLException;

    /**
     * @jc:sql statement::
     * update org_node_student
     *      set activation_status = 'IN' 
     * where 
     *      student_id = {studentId}
     *      and org_node_id = {orgNodeId}
     * ::
     */
    @JdbcControl.SQL(statement = "update org_node_student  set activation_status = 'IN'  where  student_id = {studentId}  and org_node_id = {orgNodeId}")
    void deactivateOrgNodeStudentForStudentAndOrgNode(Integer studentId, Integer orgNodeId) throws SQLException;

    /**
     * @jc:sql statement::
     * update org_node_student
     *      set activation_status = 'AC' 
     * where 
     *      student_id = {studentId}::
     */
    @JdbcControl.SQL(statement = "update org_node_student  set activation_status = 'AC'  where  student_id = {studentId}")
    void activateOrgNodeStudentForStudent(Integer studentId) throws SQLException;

    /**
     * @jc:sql statement::
     * update org_node_student
     *      set activation_status = 'AC' 
     * where 
     *      student_id = {studentId}
     *      and org_node_id = {orgNodeId}
     * ::
     */
    @JdbcControl.SQL(statement = "update org_node_student  set activation_status = 'AC'  where  student_id = {studentId}  and org_node_id = {orgNodeId}")
    void activateOrgNodeStudentForStudentAndOrgNode(Integer studentId, Integer orgNodeId) throws SQLException;
    
    /**
     * @jc:sql statement::
     *  select
     *      ons.student_id as studentId,
     *      ons.org_node_id as orgNodeId,
     *      ons.created_date_time as createdDateTime,
     *      ons.created_by as createdBy,
     *      ons.customer_id as customerId,
     *      ons.updated_by as updatedBy, 
     *      ons.updated_date_time as updatedDateTime,
     *      ons.activation_status as activationStatus, 
     *      ons.data_import_history_id as dataImportHistoryId
     *  from
     *     org_node_student ons
     *  where 
     *      ons.student_id = {studentId}
     *      and ons.org_node_id = {orgNodeId}
     *      and ons.activation_status = 'AC'::
     */
    @JdbcControl.SQL(statement = "select  ons.student_id as studentId,  ons.org_node_id as orgNodeId,  ons.created_date_time as createdDateTime,  ons.created_by as createdBy,  ons.customer_id as customerId,  ons.updated_by as updatedBy,  ons.updated_date_time as updatedDateTime,  ons.activation_status as activationStatus,  ons.data_import_history_id as dataImportHistoryId from  org_node_student ons where  ons.student_id = {studentId}  and ons.org_node_id = {orgNodeId}  and ons.activation_status = 'AC'")
    com.ctb.bean.testAdmin.OrgNodeStudent getValidStudentOrgNode(Integer studentId, Integer orgNodeId) throws SQLException;

}