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
 * @jc:connection data-source-jndi-name="oasDataSource" 
 */ 
@ControlExtension()
@JdbcControl.ConnectionDataSource(jndiName = "oasDataSource")
public interface OrgNodeCategory extends JdbcControl
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
     *   insert into org_node_category
     *     (org_node_category_id,
     *      customer_id,
     *      category_name,
     *      category_level,
     *      is_group,
     *      created_by,
     *      created_date_time,
     *      updated_by,
     *      updated_date_time,
     *      activation_status)
     *   values
     *     (seq_org_node_category_id.nextval,
     *      {orgNodeCategory.customerId},
     *      {orgNodeCategory.categoryName},
     *      {orgNodeCategory.categoryLevel},
     *      {orgNodeCategory.isGroup},
     *      {orgNodeCategory.createdBy},
     *      {orgNodeCategory.createdDateTime},
     *      {orgNodeCategory.createdBy},
     *      {orgNodeCategory.createdDateTime},
     *      {orgNodeCategory.activationStatus}
     *    )::
     */
    @JdbcControl.SQL(statement = "insert into org_node_category  (org_node_category_id,  customer_id,  category_name,  category_level,  is_group,  created_by,  created_date_time,  updated_by,  updated_date_time,  activation_status) values  (seq_org_node_category_id.nextval,  {orgNodeCategory.customerId},  {orgNodeCategory.categoryName},  {orgNodeCategory.categoryLevel},  {orgNodeCategory.isGroup},  {orgNodeCategory.createdBy},  {orgNodeCategory.createdDateTime},  {orgNodeCategory.createdBy},  {orgNodeCategory.createdDateTime},  {orgNodeCategory.activationStatus}  )")
    void createOrgNodeCategory(com.ctb.bean.testAdmin.OrgNodeCategory orgNodeCategory) throws SQLException;

    /**
     * @jc:sql statement::
     * update org_node_category
     *  set activation_Status = 'IN',
     *       updated_by = {orgNodeCategory.updatedBy}  
     * where org_node_category_id = {orgNodeCategory.orgNodeCategoryId}
     * and activation_Status = 'AC'::
     */
    @JdbcControl.SQL(statement = "update org_node_category  set activation_Status = 'IN',  updated_by = {orgNodeCategory.updatedBy}  where org_node_category_id = {orgNodeCategory.orgNodeCategoryId} and activation_Status = 'AC'")
    void deleteOrgNodeCategory(com.ctb.bean.testAdmin.OrgNodeCategory orgNodeCategory) throws SQLException;

    /**
     * @jc:sql statement::
     * update org_node_category
     *    set
     *    category_name = {orgNodeCategory.categoryName},
     *    category_level = {orgNodeCategory.categoryLevel},
     *    updated_by = {orgNodeCategory.updatedBy},
     *    updated_date_time ={orgNodeCategory.updatedDateTime},
     *    activation_status = {orgNodeCategory.activationStatus}
     *    where org_node_category_id = {orgNodeCategory.OrgNodeCategoryId}::
     */
    @JdbcControl.SQL(statement = "update org_node_category  set  category_name = {orgNodeCategory.categoryName},  category_level = {orgNodeCategory.categoryLevel},  updated_by = {orgNodeCategory.updatedBy},  updated_date_time ={orgNodeCategory.updatedDateTime},  activation_status = {orgNodeCategory.activationStatus}  where org_node_category_id = {orgNodeCategory.OrgNodeCategoryId}")
    void editOrgNodeCategory(com.ctb.bean.testAdmin.OrgNodeCategory orgNodeCategory) throws SQLException;

    /**
     * @jc:sql statement::
     * select orgNodeCat.org_node_category_id as orgNodeCategoryId,
     *    orgNodeCat.customer_id as customerId,
     *    orgNodeCat.is_group as isGroup,
     *    orgNodeCat.category_name as categoryName,
     *    orgNodeCat.category_level as categoryLevel,
     *    orgNodeCat.activation_status as activationStatus,    
     *    orgNodeCat.created_by as createdBy,
     *    orgNodeCat.created_date_time as createdDateTime,
     *    orgNodeCat.updated_by as updatedBy,
     *    orgNodeCat.updated_date_time as updatedDateTime
     *    from org_node_category orgNodeCat
     *    where orgNodeCat.customer_id = {customerId}
     *    and orgNodeCat.Activation_Status = 'AC'
     *    and orgNodeCat.category_level > 0
     *    order by orgNodeCat.category_level::
     */
    @JdbcControl.SQL(statement = "select orgNodeCat.org_node_category_id as orgNodeCategoryId,  orgNodeCat.customer_id as customerId,  orgNodeCat.is_group as isGroup,  orgNodeCat.category_name as categoryName,  orgNodeCat.category_level as categoryLevel,  orgNodeCat.activation_status as activationStatus,  orgNodeCat.created_by as createdBy,  orgNodeCat.created_date_time as createdDateTime,  orgNodeCat.updated_by as updatedBy,  orgNodeCat.updated_date_time as updatedDateTime  from org_node_category orgNodeCat  where orgNodeCat.customer_id = {customerId}  and orgNodeCat.Activation_Status = 'AC'  and orgNodeCat.category_level > 0  order by orgNodeCat.category_level")
    com.ctb.bean.testAdmin.OrgNodeCategory[] getOrgNodeCategories(Integer customerId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select orn.org_node_id as orgNodeId
     *    from org_node orn
     *    where orn.org_node_category_id = {orgNodeCategoryId}
     *    and orn.activation_status = 'AC'
	 *    Changed For LASLINK Product	
     */
    @JdbcControl.SQL(statement = "select orn.org_node_id as orgNodeId, orn.org_node_mdr_number as mdrNumber from org_node orn  where orn.org_node_category_id = {orgNodeCategoryId}  and orn.activation_status = 'AC'")
    com.ctb.bean.testAdmin.Node[] getOrgNodeForCategory(Integer orgNodeCategoryId) throws SQLException;
   
   
    /**
     * @jc:sql statement::
     * select stu.student_id as studentId
     *    from org_node_student stu, org_node orn
     *    where stu.org_node_id = orn.org_node_id
     *    and orn.org_node_category_id = {orgNodeCategoryId}
     *    and orn.activation_status = 'AC'
     */
    @JdbcControl.SQL(statement = "select stu.student_id as studentId  from org_node_student stu, org_node orn  where stu.org_node_id = orn.org_node_id  and orn.org_node_category_id = {orgNodeCategoryId}  and orn.activation_status = 'AC'")
    com.ctb.bean.testAdmin.Student[] getStudentForCategory(Integer orgNodeCategoryId) throws SQLException;
    
}