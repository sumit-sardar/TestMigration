package com.ctb.control.db.testAdmin; 

import com.bea.control.*;
import com.bea.control.JdbcControl;
import com.ctb.bean.request.testAdmin.FormAssignmentCount;
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
public interface FormAssignment extends JdbcControl
{
    static final long serialVersionUID = 1L;
    
    /** 
     * @jc:sql statement::
     * select 
	 *      iset.item_set_form as form, 
     *      count(distinct ros.test_roster_id) as count 
     * from 
	 *      test_admin adm, item_set iset, item_set_ancestor isa,
     *      (select distinct test_roster_id, form_assignment from test_roster where test_admin_id = {testAdminId}) ros
     * where
     *      isa.ancestor_item_set_id = adm.item_set_id
     *      and iset.item_set_id = isa.item_set_id
     *      and ros.form_assignment (+) = iset.item_set_form
     *      and iset.item_set_type = 'TD'
     *      and adm.test_admin_id = {testAdminId}
     * group by 
     *      iset.item_set_form::
     *      array-max-length="all" 
    */ 
    @JdbcControl.SQL(statement = "select  iset.item_set_form as form,  count(distinct ros.test_roster_id) as count  from  test_admin adm, item_set iset, item_set_ancestor isa,  (select distinct test_roster_id, form_assignment from test_roster where test_admin_id = {testAdminId}) ros where  isa.ancestor_item_set_id = adm.item_set_id  and iset.item_set_id = isa.item_set_id  and ros.form_assignment (+) = iset.item_set_form  and iset.item_set_type = 'TD'  and adm.test_admin_id = {testAdminId} group by  iset.item_set_form",
                     arrayMaxLength = 100000)
    FormAssignmentCount [] getFormAssignmentCounts(Integer testAdminId) throws SQLException;

}