package com.ctb.control.db; 

import com.bea.control.*; 
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserRole;
import com.ctb.bean.testAdmin.Role;
import java.sql.SQLException;
import java.util.Date; 
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
public interface UserRoles extends JdbcControl
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
     *         user_role (
     *     		user_id,
     *          role_id,
     *     		org_node_id,
     *     		activation_status,
     *     		created_by,
     *     		created_date_time
     *           ) values (
     *     		{userId},
     *          {role.roleId},
     *     		{orgNodeId},
     *     		'AC',
     *     		{role.createdBy},
     *     		{role.createdDateTime}
     *     	   )::
     */
    @JdbcControl.SQL(statement = "insert into  user_role (  \t\tuser_id,  role_id,  \t\torg_node_id,  \t\tactivation_status,  \t\tcreated_by,  \t\tcreated_date_time  ) values (  \t\t{userId},  {role.roleId},  \t\t{orgNodeId},  \t\t'AC',  \t\t{role.createdBy},  \t\t{role.createdDateTime}  \t  )")
    void createUserRole(Long userId, Role role, Long orgNodeId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * update user_role
     * set activation_status = 'IN',
     * updated_by = {updatedBy},
     * updated_date_time = {updatedDateTime}
     * where user_id = {userId}
     * ::
     */
    @JdbcControl.SQL(statement = "update user_role set activation_status = 'IN', updated_by = {updatedBy}, updated_date_time = {updatedDateTime} where user_id = {userId}")
    void inactivateUserRoles(Integer userId, Integer updatedBy, Date updatedDateTime) throws SQLException;


    /**
     * @jc:sql statement::
     * select ur.user_id as userId,
     * ur.role_id as roleId,
     * ur.org_node_id as orgNodeId
     * from user_role ur
     * where ur.user_id = (select u.user_id from users u where u.user_name = {userName}) ::
     */
    @JdbcControl.SQL(statement = "select ur.user_id as userId, ur.role_id as roleId, ur.org_node_id as orgNodeId from user_role ur where ur.user_id = (select u.user_id from users u where u.user_name = {userName}) ")
    UserRole[] getUserRoles(String userName) throws SQLException;

   
}