package com.ctb.control.db; 

import com.bea.control.*;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.testAdmin.Role; 
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
public interface Roles extends JdbcControl
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
     * SELECT role.role_id as roleId,
     * INITCAP(role.role_name) as roleName,
     * role.activation_status as activationStatus,
     * role.role_type_id as roleTypeId
     * FROM Role role
     * WHERE role.activation_status = 'AC'
     * AND {sql:fn in(role.role_name,{roleNames})}
     * order by role.role_name::
     */
    @JdbcControl.SQL(statement = "SELECT role.role_id as roleId, INITCAP(role.role_name) as roleName, role.activation_status as activationStatus, role.role_type_id as roleTypeId FROM Role role WHERE role.activation_status = 'AC' AND  role.role_name in ('ADMINISTRATOR','ADMINISTRATIVE COORDINATOR','COORDINATOR','PROCTOR') order by role.role_name")
    Role[] getRoles(String roleNames) throws SQLException;

   
    /**
     * @jc:sql statement::
     * SELECT DISTINCT r.role_id as roleId,
     *     INITCAP(r.role_name) as roleName
     * FROM Role r,User_Role ur, Users us
     * WHERE
     *      ur.activation_status = 'AC'
     *  and 
     *      r.activation_status = 'AC'
     *  and 
     *      r.role_id = ur.role_id
     *  and 
     *      ur.user_id = us.user_id
     *  and 
     *      us.user_name = {userName}
     * 
     *      order by r.role_id::
     */
    @JdbcControl.SQL(statement = "SELECT DISTINCT r.role_id as roleId,  INITCAP(r.role_name) as roleName FROM Role r,User_Role ur, Users us WHERE  ur.activation_status = 'AC'  and  r.activation_status = 'AC'  and  r.role_id = ur.role_id  and  ur.user_id = us.user_id  and  us.user_name = {userName}  order by r.role_id")
    Role getActiveRoleForUser(String userName) throws SQLException;
}