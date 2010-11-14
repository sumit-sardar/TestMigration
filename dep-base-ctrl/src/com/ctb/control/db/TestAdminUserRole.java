package com.ctb.control.db; 

import com.bea.control.*;
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import com.ctb.bean.testAdmin.ProctorAssignment; 
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
public interface TestAdminUserRole extends JdbcControl
{ 
    /** 
     * @jc:sql statement::
     * insert into
     *      test_admin_user_role (
     *          TEST_ADMIN_ID,
     *          USER_ID,
     *          ROLE_ID,
     *          CREATED_BY,
     *          CREATED_DATE_TIME
     *      ) values (
     *          {taur.testAdminId},
     *          {taur.userId},
     *          {taur.roleId},
     *          {taur.createdBy},
     *          {taur.createdDateTime}
     *      )::
    */ 
    @JdbcControl.SQL(statement = "insert into  test_admin_user_role (  TEST_ADMIN_ID,  USER_ID,  ROLE_ID,  CREATED_BY,  CREATED_DATE_TIME  ) values (  {taur.testAdminId},  {taur.userId},  {taur.roleId},  {taur.createdBy},  {taur.createdDateTime}  )")
    void createNewTestAdminUserRole(ProctorAssignment taur) throws SQLException;

    /**
     * @jc:sql statement::
     * delete from 
     *      test_admin_user_role
     * where
     *      test_admin_id = {testAdminId}::
     */
    @JdbcControl.SQL(statement = "delete from  test_admin_user_role where  test_admin_id = {testAdminId}")
    void deleteTestAdminUserRolesForAdmin(Integer testAdminId) throws SQLException;

    static final long serialVersionUID = 1L;
}