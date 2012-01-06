package com.ctb.control.db; 

import com.bea.control.*;
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import com.ctb.bean.testAdmin.ActiveTest;
import com.ctb.bean.testAdmin.CustomerTestResource;
import com.ctb.bean.testAdmin.ScheduleElement;
import com.ctb.bean.testAdmin.TestElement; 
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
public interface ItemSet extends JdbcControl
{ 
    /**
     * @jc:sql statement::
     * select 
     * 	   decode(count(iset.item_set_id), 0, 'false', 'true') as visible
     * from
     * 	item_set iset,
     * 	item_set_ancestor isa,
     *  org_node_test_catalog ontc,
     * 	org_node_ancestor ona,
     * 	user_role urole,
     * 	users
     * where
     * 	 users.user_name = {userName}
     * 	 and urole.user_id = users.user_id
     * 	 and ona.ancestor_org_node_id = urole.org_node_id
     * 	 and ontc.org_node_id = ona.org_node_id
     * 	 and ontc.activation_status = 'AC'
     * 	 and urole.activation_status = 'AC'
     * 	 and isa.ancestor_item_set_id = ontc.item_set_id
     * 	 and iset.item_set_id = isa.item_set_id
     * 	 and iset.item_set_id = {itemSetId}::
     */
    @JdbcControl.SQL(statement = "select  \t  decode(count(iset.item_set_id), 0, 'false', 'true') as visible from \titem_set iset, \titem_set_ancestor isa,  org_node_test_catalog ontc, \torg_node_ancestor ona, \tuser_role urole, \tusers where \t users.user_name = {userName} \t and urole.user_id = users.user_id \t and ona.ancestor_org_node_id = urole.org_node_id \t and ontc.org_node_id = ona.org_node_id \t and ontc.activation_status = 'AC' \t and urole.activation_status = 'AC' \t and isa.ancestor_item_set_id = ontc.item_set_id \t and iset.item_set_id = isa.item_set_id \t and iset.item_set_id = {itemSetId}")
    String checkVisibility(String userName, Integer itemSetId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select distinct
	 *      iset.ITEM_SET_ID as itemSetId,
	 *      iset.ITEM_SET_TYPE as itemSetType,
	 *      iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,
	 *      iset.ITEM_SET_NAME as itemSetName,
	 *      iset.ITEM_SET_LEVEL as itemSetLevel,
	 *      iset.ITEM_SET_FORM as itemSetForm,
	 *      iset.MIN_GRADE as minGrade,
	 *      iset.MAX_GRADE as maxGrade,
	 *      iset.SAMPLE as sample,
	 *      iset.TIME_LIMIT as timeLimit,
	 *      iset.BREAK_TIME as breakTime,
	 *      iset.MEDIA_PATH as mediaPath,
	 *      iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,
	 *      iset.ITEM_SET_DESCRIPTION as itemSetDescription,
	 *      iset.ITEM_SET_RULE_ID as itemSetRuleId,
	 *      iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,
	 *      iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,
	 *      iset.CREATED_BY as createdBy,
	 *      iset.CREATED_DATE_TIME as createdDateTime,
	 *      iset.UPDATED_BY as updatedBy,
	 *      iset.UPDATED_DATE_TIME as updatedDateTime,
	 *      iset.ACTIVATION_STATUS as activationStatus,
	 *      iset.VERSION as version,
	 *      iset.SUBJECT as subject,
	 *      iset.GRADE as grade,
	 *      iset.OWNER_CUSTOMER_ID as ownerCustomerId,
	 *      iset.PUBLISH_STATUS as publishStatus,
	 *      iset.ORIGINAL_CREATED_BY as originalCreatedBy,
	 *      iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,
     *      max(ontc.RANDOM_DISTRACTOR_ALLOWABLE) as isRandomize,
     *      max(ontc.override_form_assignment) as overrideFormAssignmentMethod,
     *      max(ontc.override_login_start_date) as overrideLoginStartDate
     * from
	 *      product prod,
     *      item_set_product isp,
	 *      item_set iset,
	 *	    org_node_test_catalog ontc,
	 *	    user_role urole,
	 *	    users
     * where
     *      prod.activation_status = 'AC'
     *      and iset.activation_status = 'AC'
     *      and isp.product_id = prod.product_id
     *      and iset.item_set_id = isp.item_set_id
     *      and iset.item_set_type = 'TC'
	 *      and prod.product_id = {productId}
     *      and ontc.item_set_id = iset.item_set_id
     *      and ontc.activation_status = 'AC'
	 *	    and urole.org_node_id = ontc.org_node_id
     *	    and urole.activation_status = 'AC'
	 *	    and users.user_id = urole.user_id
	 *	    and users.user_name = {userName}
     *	group by
     *      iset.ITEM_SET_ID,
     *      iset.ITEM_SET_TYPE,
     *      iset.ITEM_SET_CATEGORY_ID,
     *      iset.ITEM_SET_NAME,
     *      iset.ITEM_SET_LEVEL,
     *      iset.ITEM_SET_FORM,
     *      iset.MIN_GRADE,
     *      iset.MAX_GRADE,
     *      iset.SAMPLE,
     *      iset.TIME_LIMIT,
     *      iset.BREAK_TIME,
     *      iset.MEDIA_PATH,
     *      iset.ITEM_SET_DISPLAY_NAME,
     *      iset.ITEM_SET_DESCRIPTION,
     *      iset.ITEM_SET_RULE_ID,
     *      iset.EXT_EMS_ITEM_SET_ID,
     *      iset.EXT_CMS_ITEM_SET_ID,
     *      iset.CREATED_BY,
     *      iset.CREATED_DATE_TIME,
     *      iset.UPDATED_BY,
     *      iset.UPDATED_DATE_TIME,
     *      iset.ACTIVATION_STATUS,
     *      iset.VERSION,
     *      iset.SUBJECT,
     *      iset.GRADE,
     *      iset.OWNER_CUSTOMER_ID,
     *      iset.PUBLISH_STATUS,
     *      iset.ORIGINAL_CREATED_BY,
     *      iset.EXT_TST_ITEM_SET_ID::
     *	    array-max-length="100000"
     */
    @JdbcControl.SQL(statement = "select distinct  iset.ITEM_SET_ID as itemSetId,  iset.ITEM_SET_TYPE as itemSetType,  iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,  iset.ITEM_SET_NAME as itemSetName,  iset.ITEM_SET_LEVEL as itemSetLevel,  iset.ITEM_SET_FORM as itemSetForm,  iset.MIN_GRADE as minGrade,  iset.MAX_GRADE as maxGrade,  iset.SAMPLE as sample,  iset.TIME_LIMIT as timeLimit,  iset.BREAK_TIME as breakTime,  iset.MEDIA_PATH as mediaPath,  iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,  iset.ITEM_SET_DESCRIPTION as itemSetDescription,  iset.ITEM_SET_RULE_ID as itemSetRuleId,  iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,  iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,  iset.CREATED_BY as createdBy,  iset.CREATED_DATE_TIME as createdDateTime,  iset.UPDATED_BY as updatedBy,  iset.UPDATED_DATE_TIME as updatedDateTime,  iset.ACTIVATION_STATUS as activationStatus,  iset.VERSION as version,  iset.SUBJECT as subject,  iset.GRADE as grade,  iset.OWNER_CUSTOMER_ID as ownerCustomerId,  iset.PUBLISH_STATUS as publishStatus,  iset.ORIGINAL_CREATED_BY as originalCreatedBy,  iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,  max(ontc.RANDOM_DISTRACTOR_ALLOWABLE) as isRandomize,  max(ontc.override_form_assignment) as overrideFormAssignmentMethod,  max(ontc.override_login_start_date) as overrideLoginStartDate from  product prod,  item_set_product isp,  item_set iset, \t  org_node_test_catalog ontc, \t  user_role urole, \t  users where  prod.activation_status = 'AC'  and iset.activation_status = 'AC'  and isp.product_id = prod.product_id  and iset.item_set_id = isp.item_set_id  and iset.item_set_type = 'TC'  and prod.product_id = {productId}  and ontc.item_set_id = iset.item_set_id  and ontc.activation_status = 'AC' \t  and urole.org_node_id = ontc.org_node_id \t  and urole.activation_status = 'AC' \t  and users.user_id = urole.user_id \t  and users.user_name = {userName} \tgroup by  iset.ITEM_SET_ID,  iset.ITEM_SET_TYPE,  iset.ITEM_SET_CATEGORY_ID,  iset.ITEM_SET_NAME,  iset.ITEM_SET_LEVEL,  iset.ITEM_SET_FORM,  iset.MIN_GRADE,  iset.MAX_GRADE,  iset.SAMPLE,  iset.TIME_LIMIT,  iset.BREAK_TIME,  iset.MEDIA_PATH,  iset.ITEM_SET_DISPLAY_NAME,  iset.ITEM_SET_DESCRIPTION,  iset.ITEM_SET_RULE_ID,  iset.EXT_EMS_ITEM_SET_ID,  iset.EXT_CMS_ITEM_SET_ID,  iset.CREATED_BY,  iset.CREATED_DATE_TIME,  iset.UPDATED_BY,  iset.UPDATED_DATE_TIME,  iset.ACTIVATION_STATUS,  iset.VERSION,  iset.SUBJECT,  iset.GRADE,  iset.OWNER_CUSTOMER_ID,  iset.PUBLISH_STATUS,  iset.ORIGINAL_CREATED_BY,  iset.EXT_TST_ITEM_SET_ID",
                     arrayMaxLength = 0, fetchSize=500)
    TestElement [] getTestsForProduct(String userName, Integer productId) throws SQLException;
  
  
    /**
     * @jc:sql statement::
     * select distinct
     *      iset.ITEM_SET_ID as itemSetId,
     *      iset.ITEM_SET_TYPE as itemSetType,
     *      iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,
     *      iset.ITEM_SET_NAME as itemSetName,
     *      iset.ITEM_SET_LEVEL as itemSetLevel,
     *      iset.ITEM_SET_FORM as itemSetForm,
     *      iset.MIN_GRADE as minGrade,
     *      iset.MAX_GRADE as maxGrade,
     *      iset.SAMPLE as sample,
     *      iset.TIME_LIMIT as timeLimit,
     *      iset.BREAK_TIME as breakTime,
     *      iset.MEDIA_PATH as mediaPath,
     *      iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,
     *      iset.ITEM_SET_DESCRIPTION as itemSetDescription,
     *      iset.ITEM_SET_RULE_ID as itemSetRuleId,
     *      iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,
     *      iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,
     *      iset.CREATED_BY as createdBy,
     *      iset.CREATED_DATE_TIME as createdDateTime,
     *      iset.UPDATED_BY as updatedBy,
     *      iset.UPDATED_DATE_TIME as updatedDateTime,
     *      iset.ACTIVATION_STATUS as activationStatus,
     *      iset.VERSION as version,
     *      iset.SUBJECT as subject,
     *      iset.GRADE as grade,
     *      iset.OWNER_CUSTOMER_ID as ownerCustomerId,
     *      iset.PUBLISH_STATUS as publishStatus,
     *      iset.ORIGINAL_CREATED_BY as originalCreatedBy,
     *      iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,
     *      max(ontc.override_form_assignment) as overrideFormAssignmentMethod,
     *      max(ontc.override_login_start_date) as overrideLoginStartDate
     * from
     *      product prod,
     *      item_set_product isp,
     *      item_set iset,
     *      org_node_test_catalog ontc,
     *      user_role urole,
     *      users,
     *      program prog,
     *      product prodfamily
     * where
     *      prod.activation_status = 'AC' 
     *      and iset.activation_status = 'AC'
     *      and isp.product_id = prod.product_id
     *      and iset.item_set_id = isp.item_set_id
     *      and iset.item_set_type = 'TC'
     *      and prod.product_id = prodfamily.product_id
     *      and prog.product_id = prodfamily.parent_product_id
     *      and prog.program_id = {programId}
     *      and ontc.item_set_id = iset.item_set_id
     *      and ontc.activation_status = 'AC'
     *      and urole.org_node_id = ontc.org_node_id
     *      and urole.activation_status = 'AC'
     *      and users.user_id = urole.user_id
     *      and users.user_name = {userName}
     *      and prog.customer_id = ontc.customer_id
     * group by
     *      iset.ITEM_SET_ID,
     *      iset.ITEM_SET_TYPE,
     *      iset.ITEM_SET_CATEGORY_ID,
     *      iset.ITEM_SET_NAME,
     *      iset.ITEM_SET_LEVEL,
     *      iset.ITEM_SET_FORM,
     *      iset.MIN_GRADE,
     *      iset.MAX_GRADE,
     *      iset.SAMPLE,
     *      iset.TIME_LIMIT,
     *      iset.BREAK_TIME,
     *      iset.MEDIA_PATH,
     *      iset.ITEM_SET_DISPLAY_NAME,
     *      iset.ITEM_SET_DESCRIPTION,
     *      iset.ITEM_SET_RULE_ID,
     *      iset.EXT_EMS_ITEM_SET_ID,
     *      iset.EXT_CMS_ITEM_SET_ID,
     *      iset.CREATED_BY,
     *      iset.CREATED_DATE_TIME,
     *      iset.UPDATED_BY,
     *      iset.UPDATED_DATE_TIME,
     *      iset.ACTIVATION_STATUS,
     *      iset.VERSION,
     *      iset.SUBJECT,
     *      iset.GRADE,
     *      iset.OWNER_CUSTOMER_ID,
     *      iset.PUBLISH_STATUS,
     *      iset.ORIGINAL_CREATED_BY,
     *      iset.EXT_TST_ITEM_SET_ID::
     *	    array-max-length="100000"
     */
    @JdbcControl.SQL(statement = "select distinct  iset.ITEM_SET_ID as itemSetId,  iset.ITEM_SET_TYPE as itemSetType,  iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,  iset.ITEM_SET_NAME as itemSetName,  iset.ITEM_SET_LEVEL as itemSetLevel,  iset.ITEM_SET_FORM as itemSetForm,  iset.MIN_GRADE as minGrade,  iset.MAX_GRADE as maxGrade,  iset.SAMPLE as sample,  iset.TIME_LIMIT as timeLimit,  iset.BREAK_TIME as breakTime,  iset.MEDIA_PATH as mediaPath,  iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,  iset.ITEM_SET_DESCRIPTION as itemSetDescription,  iset.ITEM_SET_RULE_ID as itemSetRuleId,  iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,  iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,  iset.CREATED_BY as createdBy,  iset.CREATED_DATE_TIME as createdDateTime,  iset.UPDATED_BY as updatedBy,  iset.UPDATED_DATE_TIME as updatedDateTime,  iset.ACTIVATION_STATUS as activationStatus,  iset.VERSION as version,  iset.SUBJECT as subject,  iset.GRADE as grade,  iset.OWNER_CUSTOMER_ID as ownerCustomerId,  iset.PUBLISH_STATUS as publishStatus,  iset.ORIGINAL_CREATED_BY as originalCreatedBy,  iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,  max(ontc.override_form_assignment) as overrideFormAssignmentMethod,  max(ontc.override_login_start_date) as overrideLoginStartDate from  product prod,  item_set_product isp,  item_set iset,  org_node_test_catalog ontc,  user_role urole,  users,  program prog,  product prodfamily where  prod.activation_status = 'AC'  and iset.activation_status = 'AC'  and isp.product_id = prod.product_id  and iset.item_set_id = isp.item_set_id  and iset.item_set_type = 'TC'  and prod.product_id = prodfamily.product_id  and prog.product_id = prodfamily.parent_product_id  and prog.program_id = {programId}  and ontc.item_set_id = iset.item_set_id  and ontc.activation_status = 'AC'  and urole.org_node_id = ontc.org_node_id  and urole.activation_status = 'AC'  and users.user_id = urole.user_id  and users.user_name = {userName}  and prog.customer_id = ontc.customer_id group by  iset.ITEM_SET_ID,  iset.ITEM_SET_TYPE,  iset.ITEM_SET_CATEGORY_ID,  iset.ITEM_SET_NAME,  iset.ITEM_SET_LEVEL,  iset.ITEM_SET_FORM,  iset.MIN_GRADE,  iset.MAX_GRADE,  iset.SAMPLE,  iset.TIME_LIMIT,  iset.BREAK_TIME,  iset.MEDIA_PATH,  iset.ITEM_SET_DISPLAY_NAME,  iset.ITEM_SET_DESCRIPTION,  iset.ITEM_SET_RULE_ID,  iset.EXT_EMS_ITEM_SET_ID,  iset.EXT_CMS_ITEM_SET_ID,  iset.CREATED_BY,  iset.CREATED_DATE_TIME,  iset.UPDATED_BY,  iset.UPDATED_DATE_TIME,  iset.ACTIVATION_STATUS,  iset.VERSION,  iset.SUBJECT,  iset.GRADE,  iset.OWNER_CUSTOMER_ID,  iset.PUBLISH_STATUS,  iset.ORIGINAL_CREATED_BY,  iset.EXT_TST_ITEM_SET_ID",
                     arrayMaxLength = 100000)
    TestElement [] getTestsForProgram(String userName, Integer programId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select distinct
     *      iset.ITEM_SET_LEVEL as itemSetLevel
     * from
     *      product prod,
     *      item_set_product isp,
     *      item_set iset
     * where
     *      prod.activation_status = 'AC'
     *      and iset.activation_status = 'AC'
     *      and isp.product_id = prod.product_id
     *      and iset.item_set_id = isp.item_set_id
     *      and iset.item_set_type = 'TC'
     *      and prod.product_id = {productId}::
     *	    array-max-length="100000"
     */
    @JdbcControl.SQL(statement = "select distinct  iset.ITEM_SET_LEVEL as itemSetLevel from  product prod,  item_set_product isp,  item_set iset where  prod.activation_status = 'AC'  and iset.activation_status = 'AC'  and isp.product_id = prod.product_id  and iset.item_set_id = isp.item_set_id  and iset.item_set_type = 'TC'  and prod.product_id = {productId}",
                     arrayMaxLength = 0, fetchSize=100)
    String [] getLevelsForProduct(Integer productId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select distinct
     *      iset.GRADE as grade
     * from
     *      product prod,
     *      item_set_product isp,
     *      item_set iset
     * where
     *      prod.activation_status = 'AC'
     *      and iset.activation_status = 'AC'
     *      and isp.product_id = prod.product_id
     *      and iset.item_set_id = isp.item_set_id
     *      and iset.item_set_type = 'TC'
     *      and prod.product_id = {productId}::
     *	    array-max-length="100000"
     */
    @JdbcControl.SQL(statement = "select distinct  iset.GRADE as grade from  product prod,  item_set_product isp,  item_set iset where  prod.activation_status = 'AC'  and iset.activation_status = 'AC'  and isp.product_id = prod.product_id  and iset.item_set_id = isp.item_set_id  and iset.item_set_type = 'TC'  and prod.product_id = {productId}",
                     arrayMaxLength = 100000)
    String [] getGradesForProduct(Integer productId) throws SQLException;


    /**
     * @jc:sql statement::
     * select distinct
	 *      iset.ITEM_SET_LEVEL as itemSetLevel
     * from
	 *      product prod,
     *      item_set_product isp,
	 *      item_set iset,
	 *	    org_node_test_catalog ontc,
	 *	    user_role urole,
	 *	    users
     * where
     *      prod.activation_status = 'AC'
     *      and iset.activation_status = 'AC'
     *      and isp.product_id = prod.product_id
     *      and iset.item_set_id = isp.item_set_id
     *      and iset.item_set_type = 'TC'
	 *      and prod.product_id = {productId}
     *      and ontc.item_set_id = iset.item_set_id
     *      and ontc.activation_status = 'AC'
	 *	    and urole.org_node_id = ontc.org_node_id
	 *	    and users.user_id = urole.user_id
	 *	    and users.user_name = {userName}::
     *	    array-max-length="100000"
     */
    @JdbcControl.SQL(statement = "select distinct  iset.ITEM_SET_LEVEL as itemSetLevel from  product prod,  item_set_product isp,  item_set iset, \t  org_node_test_catalog ontc, \t  user_role urole, \t  users where  prod.activation_status = 'AC'  and iset.activation_status = 'AC'  and isp.product_id = prod.product_id  and iset.item_set_id = isp.item_set_id  and iset.item_set_type = 'TC'  and prod.product_id = {productId}  and ontc.item_set_id = iset.item_set_id  and ontc.activation_status = 'AC' \t  and urole.org_node_id = ontc.org_node_id \t  and users.user_id = urole.user_id \t  and users.user_name = {userName}",
                     arrayMaxLength = 100000)
    String [] getLevelsForProductForUser(String userName, Integer productId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select distinct
	 *      iset.GRADE as grade
     * from
	 *      product prod,
     *      item_set_product isp,
	 *      item_set iset,
	 *	    org_node_test_catalog ontc,
	 *	    user_role urole,
	 *	    users
     * where
     *      prod.activation_status = 'AC'
     *      and iset.activation_status = 'AC'
     *      and isp.product_id = prod.product_id
     *      and iset.item_set_id = isp.item_set_id
     *      and iset.item_set_type = 'TC'
	 *      and prod.product_id = {productId}
     *      and ontc.item_set_id = iset.item_set_id
     *      and ontc.activation_status = 'AC'
	 *	    and urole.org_node_id = ontc.org_node_id
	 *	    and users.user_id = urole.user_id
	 *	    and users.user_name = {userName}::
     *	    array-max-length="100000"
     */
    @JdbcControl.SQL(statement = "select distinct  iset.GRADE as grade from  product prod,  item_set_product isp,  item_set iset, \t  org_node_test_catalog ontc, \t  user_role urole, \t  users where  prod.activation_status = 'AC'  and iset.activation_status = 'AC'  and isp.product_id = prod.product_id  and iset.item_set_id = isp.item_set_id  and iset.item_set_type = 'TC'  and prod.product_id = {productId}  and ontc.item_set_id = iset.item_set_id  and ontc.activation_status = 'AC' \t  and urole.org_node_id = ontc.org_node_id \t  and users.user_id = urole.user_id \t  and users.user_name = {userName}",
                     arrayMaxLength = 0, fetchSize=100)
    String [] getGradesForProductForUser(String userName, Integer productId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select 
	 *      iset.ITEM_SET_ID as itemSetId,
	 *      iset.ITEM_SET_TYPE as itemSetType,
	 *      iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,
	 *      iset.ITEM_SET_NAME as itemSetName,
	 *      iset.ITEM_SET_LEVEL as itemSetLevel,
	 *      iset.ITEM_SET_FORM as itemSetForm,
	 *      iset.MIN_GRADE as minGrade,
	 *      iset.MAX_GRADE as maxGrade,
	 *      iset.SAMPLE as sample,
	 *      iset.TIME_LIMIT as timeLimit,
	 *      iset.BREAK_TIME as breakTime,
	 *      iset.MEDIA_PATH as mediaPath,
	 *      iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,
	 *      iset.ITEM_SET_DESCRIPTION as itemSetDescription,
	 *      iset.ITEM_SET_RULE_ID as itemSetRuleId,
	 *      iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,
	 *      iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,
	 *      iset.CREATED_BY as createdBy,
	 *      iset.CREATED_DATE_TIME as createdDateTime,
	 *      iset.UPDATED_BY as updatedBy,
	 *      iset.UPDATED_DATE_TIME as updatedDateTime,
	 *      iset.ACTIVATION_STATUS as activationStatus,
	 *      iset.VERSION as version,
	 *      iset.SUBJECT as subject,
	 *      iset.GRADE as grade,
	 *      iset.OWNER_CUSTOMER_ID as ownerCustomerId,
	 *      iset.PUBLISH_STATUS as publishStatus,
	 *      iset.ORIGINAL_CREATED_BY as originalCreatedBy,
	 *      iset.EXT_TST_ITEM_SET_ID as extTstItemSetId
     * from
	 *      item_set iset,
     *      item_set_ancestor isa
     * where
     *      iset.activation_status = 'AC'
     *      and iset.item_set_id = isa.item_set_id
     *      and iset.item_set_type = {itemSetType}
	 *      and isa.ancestor_item_set_id = {ancestorItemSetId}::
     *      array-max-length="100000"
     */
    @JdbcControl.SQL(statement = "select  iset.ITEM_SET_ID as itemSetId,  iset.ITEM_SET_TYPE as itemSetType,  iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,  iset.ITEM_SET_NAME as itemSetName,  iset.ITEM_SET_LEVEL as itemSetLevel,  iset.ITEM_SET_FORM as itemSetForm,  iset.MIN_GRADE as minGrade,  iset.MAX_GRADE as maxGrade,  iset.SAMPLE as sample,  iset.TIME_LIMIT as timeLimit,  iset.BREAK_TIME as breakTime,  iset.MEDIA_PATH as mediaPath,  iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,  iset.ITEM_SET_DESCRIPTION as itemSetDescription,  iset.ITEM_SET_RULE_ID as itemSetRuleId,  iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,  iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,  iset.CREATED_BY as createdBy,  iset.CREATED_DATE_TIME as createdDateTime,  iset.UPDATED_BY as updatedBy,  iset.UPDATED_DATE_TIME as updatedDateTime,  iset.ACTIVATION_STATUS as activationStatus,  iset.VERSION as version,  iset.SUBJECT as subject,  iset.GRADE as grade,  iset.OWNER_CUSTOMER_ID as ownerCustomerId,  iset.PUBLISH_STATUS as publishStatus,  iset.ORIGINAL_CREATED_BY as originalCreatedBy,  iset.EXT_TST_ITEM_SET_ID as extTstItemSetId from  item_set iset,  item_set_ancestor isa where  iset.activation_status = 'AC'  and iset.item_set_id = isa.item_set_id  and iset.item_set_type = {itemSetType}  and isa.ancestor_item_set_id = {ancestorItemSetId}",
                     arrayMaxLength = 100000)
    TestElement [] getTestElementsForAncestor(Integer ancestorItemSetId, String itemSetType) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select 
	 *      iset.ITEM_SET_ID as itemSetId,
	 *      iset.ITEM_SET_TYPE as itemSetType,
	 *      iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,
	 *      iset.ITEM_SET_NAME as itemSetName,
	 *      iset.ITEM_SET_LEVEL as itemSetLevel,
	 *      iset.ITEM_SET_FORM as itemSetForm,
	 *      iset.MIN_GRADE as minGrade,
	 *      iset.MAX_GRADE as maxGrade,
	 *      iset.SAMPLE as sample,
	 *      iset.TIME_LIMIT as timeLimit,
	 *      iset.BREAK_TIME as breakTime,
	 *      iset.MEDIA_PATH as mediaPath,
	 *      iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,
	 *      iset.ITEM_SET_DESCRIPTION as itemSetDescription,
	 *      iset.ITEM_SET_RULE_ID as itemSetRuleId,
	 *      iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,
	 *      iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,
	 *      iset.CREATED_BY as createdBy,
	 *      iset.CREATED_DATE_TIME as createdDateTime,
	 *      iset.UPDATED_BY as updatedBy,
	 *      iset.UPDATED_DATE_TIME as updatedDateTime,
	 *      iset.ACTIVATION_STATUS as activationStatus,
	 *      iset.VERSION as version,
	 *      iset.SUBJECT as subject,
	 *      iset.GRADE as grade,
	 *      iset.OWNER_CUSTOMER_ID as ownerCustomerId,
	 *      iset.PUBLISH_STATUS as publishStatus,
	 *      iset.ORIGINAL_CREATED_BY as originalCreatedBy,
	 *      iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,
     *      isp.item_set_group as itemSetGroup
     * from
	 *      item_set iset,
     *      item_set_parent isp
     * where
     *      iset.activation_status = 'AC'
     *      and iset.item_set_id = isp.item_set_id
     *      and iset.item_set_type = {itemSetType}
	 *      and isp.parent_item_set_id = {parentItemSetId}
     * order by
     *      isp.item_set_sort_order::
     *      array-max-length="100000"
     */
    @JdbcControl.SQL(statement = "select  iset.ITEM_SET_ID as itemSetId,  iset.ITEM_SET_TYPE as itemSetType,  iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,  iset.ITEM_SET_NAME as itemSetName,  iset.ITEM_SET_LEVEL as itemSetLevel,  iset.ITEM_SET_FORM as itemSetForm,  iset.MIN_GRADE as minGrade,  iset.MAX_GRADE as maxGrade,  iset.SAMPLE as sample,  iset.TIME_LIMIT as timeLimit,  iset.BREAK_TIME as breakTime,  iset.MEDIA_PATH as mediaPath,  iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,  iset.ITEM_SET_DESCRIPTION as itemSetDescription,  iset.ITEM_SET_RULE_ID as itemSetRuleId,  iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,  iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,  iset.CREATED_BY as createdBy,  iset.CREATED_DATE_TIME as createdDateTime,  iset.UPDATED_BY as updatedBy,  iset.UPDATED_DATE_TIME as updatedDateTime,  iset.ACTIVATION_STATUS as activationStatus,  iset.VERSION as version,  iset.SUBJECT as subject,  iset.GRADE as grade,  iset.OWNER_CUSTOMER_ID as ownerCustomerId,  iset.PUBLISH_STATUS as publishStatus,  iset.ORIGINAL_CREATED_BY as originalCreatedBy,  iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,  isp.item_set_group as itemSetGroup from  item_set iset,  item_set_parent isp where  iset.activation_status = 'AC'  and iset.item_set_id = isp.item_set_id  and iset.item_set_type = {itemSetType}  and isp.parent_item_set_id = {parentItemSetId} order by  isp.item_set_sort_order",
                     arrayMaxLength = 0, fetchSize=500)
    TestElement [] getTestElementsForParent(Integer parentItemSetId, String itemSetType) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     *      iset.ITEM_SET_ID as itemSetId,
     *      iset.ITEM_SET_TYPE as itemSetType,
     *      iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,
     *      iset.ITEM_SET_NAME as itemSetName,
     *      iset.ITEM_SET_LEVEL as itemSetLevel,
     *      iset.ITEM_SET_FORM as itemSetForm,
     *      iset.MIN_GRADE as minGrade,
     *      iset.MAX_GRADE as maxGrade,
     *      iset.SAMPLE as sample,
     *      iset.TIME_LIMIT as timeLimit,
     *      iset.BREAK_TIME as breakTime,
     *      iset.MEDIA_PATH as mediaPath,
     *      iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,
     *      iset.ITEM_SET_DESCRIPTION as itemSetDescription,
     *      iset.ITEM_SET_RULE_ID as itemSetRuleId,
     *      iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,
     *      iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,
     *      iset.CREATED_BY as createdBy,
     *      iset.CREATED_DATE_TIME as createdDateTime,
     *      iset.UPDATED_BY as updatedBy,
     *      iset.UPDATED_DATE_TIME as updatedDateTime,
     *      iset.ACTIVATION_STATUS as activationStatus,
     *      iset.VERSION as version,
     *      iset.SUBJECT as subject,
     *      iset.GRADE as grade,
     *      iset.OWNER_CUSTOMER_ID as ownerCustomerId,
     *      iset.PUBLISH_STATUS as publishStatus,
     *      iset.ORIGINAL_CREATED_BY as originalCreatedBy,
     *      iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,
     *      isp.item_set_group as itemSetGroup
     * from
     *      item_set iset,
     *      item_set_parent isp
     * where
     *      iset.activation_status = 'AC'
     *      and iset.item_set_id = isp.item_set_id
     *      and iset.item_set_type = {itemSetType}
     *      and isp.parent_item_set_id = {parentItemSetId}
     *      and iset.ITEM_SET_FORM = {itemSetForm}
     * order by
     *      isp.item_set_sort_order::
     *      array-max-length="100000"
     */
    @JdbcControl.SQL(statement = "select  iset.ITEM_SET_ID as itemSetId,  iset.ITEM_SET_TYPE as itemSetType,  iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,  iset.ITEM_SET_NAME as itemSetName,  iset.ITEM_SET_LEVEL as itemSetLevel,  iset.ITEM_SET_FORM as itemSetForm,  iset.MIN_GRADE as minGrade,  iset.MAX_GRADE as maxGrade,  iset.SAMPLE as sample,  iset.TIME_LIMIT as timeLimit,  iset.BREAK_TIME as breakTime,  iset.MEDIA_PATH as mediaPath,  iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,  iset.ITEM_SET_DESCRIPTION as itemSetDescription,  iset.ITEM_SET_RULE_ID as itemSetRuleId,  iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,  iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,  iset.CREATED_BY as createdBy,  iset.CREATED_DATE_TIME as createdDateTime,  iset.UPDATED_BY as updatedBy,  iset.UPDATED_DATE_TIME as updatedDateTime,  iset.ACTIVATION_STATUS as activationStatus,  iset.VERSION as version,  iset.SUBJECT as subject,  iset.GRADE as grade,  iset.OWNER_CUSTOMER_ID as ownerCustomerId,  iset.PUBLISH_STATUS as publishStatus,  iset.ORIGINAL_CREATED_BY as originalCreatedBy,  iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,  isp.item_set_group as itemSetGroup from  item_set iset,  item_set_parent isp where  iset.activation_status = 'AC'  and iset.item_set_id = isp.item_set_id  and iset.item_set_type = {itemSetType}  and isp.parent_item_set_id = {parentItemSetId}  and iset.ITEM_SET_FORM = {itemSetForm} order by  isp.item_set_sort_order",
                     arrayMaxLength = 100000)
    TestElement [] getTestElementsForParentByForm(Integer parentItemSetId, String itemSetType, String itemSetForm) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     *      iset.ITEM_SET_ID as itemSetId,
     *      iset.ITEM_SET_TYPE as itemSetType,
     *      iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,
     *      iset.ITEM_SET_NAME as itemSetName,
     *      iset.ITEM_SET_LEVEL as itemSetLevel,
     *      tais.ITEM_SET_FORM as itemSetForm,
     *      iset.MIN_GRADE as minGrade,
     *      iset.MAX_GRADE as maxGrade,
     *      iset.SAMPLE as sample,
     *      iset.TIME_LIMIT as timeLimit,
     *      iset.BREAK_TIME as breakTime,
     *      iset.MEDIA_PATH as mediaPath,
     *      iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,
     *      iset.ITEM_SET_DESCRIPTION as itemSetDescription,
     *      iset.ITEM_SET_RULE_ID as itemSetRuleId,
     *      iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,
     *      iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,
     *      iset.CREATED_BY as createdBy,
     *      iset.CREATED_DATE_TIME as createdDateTime,
     *      iset.UPDATED_BY as updatedBy,
     *      iset.UPDATED_DATE_TIME as updatedDateTime,
     *      iset.ACTIVATION_STATUS as activationStatus,
     *      iset.VERSION as version,
     *      iset.SUBJECT as subject,
     *      iset.GRADE as grade,
     *      iset.OWNER_CUSTOMER_ID as ownerCustomerId,
     *      iset.PUBLISH_STATUS as publishStatus,
     *      iset.ORIGINAL_CREATED_BY as originalCreatedBy,
     *      iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,
     *      tais.access_code as accessCode,
     *      isp.item_set_group as itemSetGroup,
     *      tais.session_default as sessionDefault
     * from
     *      item_set iset,
     *      item_set_parent isp,
     *      test_admin ta,
     *      test_admin_item_set tais
     * where
     *      iset.activation_status = 'AC'
     *      and iset.item_set_id = isp.item_set_id
     *      and isp.parent_item_set_id = ta.item_set_id
     *      and tais.item_set_id = iset.item_set_id
     *      and tais.test_Admin_id = ta.test_admin_id
     *      and ta.test_admin_id = {testAdminId}
     * order by
     *      tais.item_set_order::
     *      array-max-length="100000"
     */
    @JdbcControl.SQL(statement = "select  iset.ITEM_SET_ID as itemSetId,  iset.ITEM_SET_TYPE as itemSetType,  iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,  iset.ITEM_SET_NAME as itemSetName,  iset.ITEM_SET_LEVEL as itemSetLevel,  tais.ITEM_SET_FORM as itemSetForm,  iset.MIN_GRADE as minGrade,  iset.MAX_GRADE as maxGrade,  iset.SAMPLE as sample,  iset.TIME_LIMIT as timeLimit,  iset.BREAK_TIME as breakTime,  iset.MEDIA_PATH as mediaPath,  iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,  iset.ITEM_SET_DESCRIPTION as itemSetDescription,  iset.ITEM_SET_RULE_ID as itemSetRuleId,  iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,  iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,  iset.CREATED_BY as createdBy,  iset.CREATED_DATE_TIME as createdDateTime,  iset.UPDATED_BY as updatedBy,  iset.UPDATED_DATE_TIME as updatedDateTime,  iset.ACTIVATION_STATUS as activationStatus,  iset.VERSION as version,  iset.SUBJECT as subject,  iset.GRADE as grade,  iset.OWNER_CUSTOMER_ID as ownerCustomerId,  iset.PUBLISH_STATUS as publishStatus,  iset.ORIGINAL_CREATED_BY as originalCreatedBy,  iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,  tais.access_code as accessCode,  isp.item_set_group as itemSetGroup,  tais.session_default as sessionDefault from  item_set iset,  item_set_parent isp,  test_admin ta,  test_admin_item_set tais where  iset.activation_status = 'AC'  and iset.item_set_id = isp.item_set_id  and isp.parent_item_set_id = ta.item_set_id  and tais.item_set_id = iset.item_set_id  and tais.test_Admin_id = ta.test_admin_id  and ta.test_admin_id = {testAdminId} order by  tais.item_set_order",
                     arrayMaxLength = 100000)
    TestElement [] getTestElementsForSession(Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     *      iset.ITEM_SET_ID as itemSetId,
     *      iset.ITEM_SET_TYPE as itemSetType,
     *      iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,
     *      iset.ITEM_SET_NAME as itemSetName,
     *      iset.ITEM_SET_LEVEL as itemSetLevel,
     *      tais.ITEM_SET_FORM as itemSetForm,
     *      iset.MIN_GRADE as minGrade,
     *      iset.MAX_GRADE as maxGrade,
     *      iset.SAMPLE as sample,
     *      iset.TIME_LIMIT as timeLimit,
     *      iset.BREAK_TIME as breakTime,
     *      iset.MEDIA_PATH as mediaPath,
     *      iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,
     *      iset.ITEM_SET_DESCRIPTION as itemSetDescription,
     *      iset.ITEM_SET_RULE_ID as itemSetRuleId,
     *      iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,
     *      iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,
     *      iset.CREATED_BY as createdBy,
     *      iset.CREATED_DATE_TIME as createdDateTime,
     *      iset.UPDATED_BY as updatedBy,
     *      iset.UPDATED_DATE_TIME as updatedDateTime,
     *      iset.ACTIVATION_STATUS as activationStatus,
     *      iset.VERSION as version,
     *      iset.SUBJECT as subject,
     *      iset.GRADE as grade,
     *      iset.OWNER_CUSTOMER_ID as ownerCustomerId,
     *      iset.PUBLISH_STATUS as publishStatus,
     *      iset.ORIGINAL_CREATED_BY as originalCreatedBy,
     *      iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,
     *      tais.access_code as accessCode,
     *      isp.item_set_group as itemSetGroup,
     *      tais.session_default as sessionDefault
     * from
     *      item_set iset,
     *      item_set_parent isp,
     *      test_admin ta,
     *      test_admin_item_set tais
     * where
     *      iset.activation_status = 'AC'
     *      and iset.item_set_id = isp.item_set_id
     *      and isp.parent_item_set_id = ta.item_set_id
     *      and tais.item_set_id = iset.item_set_id
     *      and tais.test_Admin_id = ta.test_admin_id
     *      and tais.session_default = 'T'
     *      and ta.test_admin_id = {testAdminId}
     * order by
     *      tais.item_set_order::
     *      array-max-length="100000"
     */
    @JdbcControl.SQL(statement = "select  iset.ITEM_SET_ID as itemSetId,  iset.ITEM_SET_TYPE as itemSetType,  iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,  iset.ITEM_SET_NAME as itemSetName,  iset.ITEM_SET_LEVEL as itemSetLevel,  tais.ITEM_SET_FORM as itemSetForm,  iset.MIN_GRADE as minGrade,  iset.MAX_GRADE as maxGrade,  iset.SAMPLE as sample,  iset.TIME_LIMIT as timeLimit,  iset.BREAK_TIME as breakTime,  iset.MEDIA_PATH as mediaPath,  iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,  iset.ITEM_SET_DESCRIPTION as itemSetDescription,  iset.ITEM_SET_RULE_ID as itemSetRuleId,  iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,  iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,  iset.CREATED_BY as createdBy,  iset.CREATED_DATE_TIME as createdDateTime,  iset.UPDATED_BY as updatedBy,  iset.UPDATED_DATE_TIME as updatedDateTime,  iset.ACTIVATION_STATUS as activationStatus,  iset.VERSION as version,  iset.SUBJECT as subject,  iset.GRADE as grade,  iset.OWNER_CUSTOMER_ID as ownerCustomerId,  iset.PUBLISH_STATUS as publishStatus,  iset.ORIGINAL_CREATED_BY as originalCreatedBy,  iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,  tais.access_code as accessCode,  isp.item_set_group as itemSetGroup,  tais.session_default as sessionDefault from  item_set iset,  item_set_parent isp,  test_admin ta,  test_admin_item_set tais where  iset.activation_status = 'AC'  and iset.item_set_id = isp.item_set_id  and isp.parent_item_set_id = ta.item_set_id  and tais.item_set_id = iset.item_set_id  and tais.test_Admin_id = ta.test_admin_id  and tais.session_default = 'T'  and ta.test_admin_id = {testAdminId} order by  tais.item_set_order",
                     arrayMaxLength = 100000)
    TestElement [] getDefaultTestElementsForSession(Integer testAdminId) throws SQLException;

    
    /** 
     * @jc:sql statement::
     * select distinct
	 *      iset.item_set_form as form
     * from 
	 *      item_set iset,
     *      item_set_ancestor isa 
     * where 
     *      iset.item_set_id = isa.item_set_id
     *      and iset.item_set_type = 'TD'
     *      and isa.ancestor_item_set_id = {itemSetId}
     * order by
     *      iset.item_set_form:: 
     *      array-max-length="100000"
    */ 
    @JdbcControl.SQL(statement = "select distinct  iset.item_set_form as form from  item_set iset,  item_set_ancestor isa  where  iset.item_set_id = isa.item_set_id  and iset.item_set_type = 'TD'  and isa.ancestor_item_set_id = {itemSetId} order by  iset.item_set_form",
                     arrayMaxLength = 0, fetchSize=500 )
    String [] getFormsForTest(Integer itemSetId) throws SQLException;
    
    /** 
     * @jc:sql statement::
     * select
     *    itemSetId,
     * 	  itemSetName,
     * 	  itemSetLevel,
     * 	  itemSetGrade,
     * 	  min(loginStartDate) as loginStartDate
     * from (
     * select distinct
     *  iset.item_set_id as itemSetId,
     * 	iset.item_set_name as itemSetName,
     * 	iset.item_set_level as itemSetLevel,
     * 	iset.grade as itemSetGrade,
     * 	admin.login_start_date as loginStartDate
     * from
     * 	item_set iset,
     * 	item_set tdiset,
     * 	item_set_ancestor isa,
     * 	test_admin admin,
     * 	org_node_ancestor ona,
     * 	user_role urole,
     * 	role,
     * 	users
     * where
     * 	 isa.ancestor_item_set_id = iset.item_set_id
     * 	 and tdiset.item_set_id = isa.item_set_id
     * 	 and tdiset.item_set_type = 'TD'
     * 	 and admin.item_set_id = iset.item_set_id
     * 	 and ona.org_node_id = admin.creator_org_node_id
     * 	 and urole.org_node_id = ona.ancestor_org_node_id
     * 	 and urole.role_id = ROLE.role_id
     * 	 and role.role_name <> 'PROCTOR'
     * 	 and users.user_id = urole.user_id
     * 	 and users.user_name = {userName}
     * 	 and admin.login_start_date < sysdate + 60 
     * 	 and to_date(concat(to_char(admin.login_end_date, 'MON-DD-YYYY'), to_char(admin.daily_login_end_time, ' HH24:MI:SS')), 'MON-DD-YYYY HH24:MI:SS') > sysdate
     * union
     * select distinct
     *  iset.item_set_id as itemSetId,
     * 	iset.item_set_name as itemSetName,
     * 	iset.item_set_level as itemSetLevel,
     * 	iset.grade as itemSetGrade,
     * 	admin.login_start_date as loginStartDate
     * from
     * 	item_set iset,
     * 	item_set tdiset,
     * 	item_set_ancestor isa,
     * 	test_admin admin,
     * 	test_admin_user_role taur,
     * 	users
     * where
     * 	 isa.ancestor_item_set_id = iset.item_set_id
     * 	 and tdiset.item_set_id = isa.item_set_id
     * 	 and tdiset.item_set_type = 'TD'
     * 	 and admin.item_set_id = iset.item_set_id
     * 	 and taur.test_Admin_id = admin.test_Admin_id
     * 	 and users.user_id = taur.user_id
     * 	 and users.user_name = {userName}
     * 	 and admin.login_start_date < sysdate + 60 
     * 	 and to_date(concat(to_char(admin.login_end_date, 'MON-DD-YYYY'), to_char(admin.daily_login_end_time, ' HH24:MI:SS')), 'MON-DD-YYYY HH24:MI:SS') > sysdate
     * ) group by
     *      itemSetId,
     *      itemSetName,
     * 		itemSetLevel,
     * 		itemSetGrade
     *   order by itemSetName, loginStartDate::
     *   array-max-length="100000"
    */ 
    @JdbcControl.SQL(statement = "select  itemSetId, \t  itemSetName, \t  itemSetLevel, \t  itemSetGrade, \t  min(loginStartDate) as loginStartDate from ( select distinct  iset.item_set_id as itemSetId, \tiset.item_set_name as itemSetName, \tiset.item_set_level as itemSetLevel, \tiset.grade as itemSetGrade, \tadmin.login_start_date as loginStartDate from \titem_set iset, \titem_set tdiset, \titem_set_ancestor isa, \ttest_admin admin, \torg_node_ancestor ona, \tuser_role urole, \trole, \tusers where \t isa.ancestor_item_set_id = iset.item_set_id \t and tdiset.item_set_id = isa.item_set_id \t and tdiset.item_set_type = 'TD' \t and admin.item_set_id = iset.item_set_id \t and ona.org_node_id = admin.creator_org_node_id \t and urole.org_node_id = ona.ancestor_org_node_id \t and urole.role_id = ROLE.role_id \t and role.role_name <> 'PROCTOR' \t and users.user_id = urole.user_id \t and users.user_name = {userName} \t and admin.login_start_date < sysdate + 60  \t and to_date(concat(to_char(admin.login_end_date, 'MON-DD-YYYY'), to_char(admin.daily_login_end_time, ' HH24:MI:SS')), 'MON-DD-YYYY HH24:MI:SS') > sysdate union select distinct  iset.item_set_id as itemSetId, \tiset.item_set_name as itemSetName, \tiset.item_set_level as itemSetLevel, \tiset.grade as itemSetGrade, \tadmin.login_start_date as loginStartDate from \titem_set iset, \titem_set tdiset, \titem_set_ancestor isa, \ttest_admin admin, \ttest_admin_user_role taur, \tusers where \t isa.ancestor_item_set_id = iset.item_set_id \t and tdiset.item_set_id = isa.item_set_id \t and tdiset.item_set_type = 'TD' \t and admin.item_set_id = iset.item_set_id \t and taur.test_Admin_id = admin.test_Admin_id \t and users.user_id = taur.user_id \t and users.user_name = {userName} \t and admin.login_start_date < sysdate + 60  \t and to_date(concat(to_char(admin.login_end_date, 'MON-DD-YYYY'), to_char(admin.daily_login_end_time, ' HH24:MI:SS')), 'MON-DD-YYYY HH24:MI:SS') > sysdate ) group by  itemSetId,  itemSetName, \t\titemSetLevel, \t\titemSetGrade  order by itemSetName, loginStartDate",
                     arrayMaxLength = 100000)
    ActiveTest [] getActiveTestsForUser(String userName) throws SQLException;
    
    /** 
     * @jc:sql statement::
     * select
     *      sum(tdiset.content_size)
     * from
     *      item_set tdiset,
     *      item_set_ancestor isa
     * where
     *      tdiset.item_Set_id = isa.item_set_id
     *      and tdiset.item_Set_type = 'TS'
     *      and isa.ancestor_item_Set_id = {itemSetId}::
     * array-max-length="100000"
    */
    @JdbcControl.SQL(statement = "select  sum(tdiset.content_size) from  item_set tdiset,  item_set_ancestor isa where  tdiset.item_Set_id = isa.item_set_id  and tdiset.item_Set_type = 'TS'  and isa.ancestor_item_Set_id = {itemSetId}",
                     arrayMaxLength = 100000)
    Integer getContentSizeForTest(Integer itemSetId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select 
     *      iset.ITEM_SET_ID as itemSetId,
     *      iset.ITEM_SET_TYPE as itemSetType,
     *      iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,
     *      iset.ITEM_SET_NAME as itemSetName,
     *      iset.ITEM_SET_LEVEL as itemSetLevel,
     *      iset.ITEM_SET_FORM as itemSetForm,
     *      iset.MIN_GRADE as minGrade,
     *      iset.MAX_GRADE as maxGrade,
     *      iset.SAMPLE as sample,
     *      iset.TIME_LIMIT as timeLimit,
     *      iset.BREAK_TIME as breakTime,
     *      iset.MEDIA_PATH as mediaPath,
     *      iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,
     *      iset.ITEM_SET_DESCRIPTION as itemSetDescription,
     *      iset.ITEM_SET_RULE_ID as itemSetRuleId,
     *      iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,
     *      iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,
     *      iset.CREATED_BY as createdBy,
     *      iset.CREATED_DATE_TIME as createdDateTime,
     *      iset.UPDATED_BY as updatedBy,
     *      iset.UPDATED_DATE_TIME as updatedDateTime,
     *      iset.ACTIVATION_STATUS as activationStatus,
     *      iset.VERSION as version,
     *      iset.SUBJECT as subject,
     *      iset.GRADE as grade,
     *      iset.OWNER_CUSTOMER_ID as ownerCustomerId,
     *      iset.PUBLISH_STATUS as publishStatus,
     *      iset.ORIGINAL_CREATED_BY as originalCreatedBy,
     *      iset.EXT_TST_ITEM_SET_ID as extTstItemSetId
     * from
     *      item_set iset
     * where
     *      iset.item_set_id = {itemSetId}
     * ::
     */
    @JdbcControl.SQL(statement = "select  iset.ITEM_SET_ID as itemSetId,  iset.ITEM_SET_TYPE as itemSetType,  iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,  iset.ITEM_SET_NAME as itemSetName,  iset.ITEM_SET_LEVEL as itemSetLevel,  iset.ITEM_SET_FORM as itemSetForm,  iset.MIN_GRADE as minGrade,  iset.MAX_GRADE as maxGrade,  iset.SAMPLE as sample,  iset.TIME_LIMIT as timeLimit,  iset.BREAK_TIME as breakTime,  iset.MEDIA_PATH as mediaPath,  iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,  iset.ITEM_SET_DESCRIPTION as itemSetDescription,  iset.ITEM_SET_RULE_ID as itemSetRuleId,  iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,  iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,  iset.CREATED_BY as createdBy,  iset.CREATED_DATE_TIME as createdDateTime,  iset.UPDATED_BY as updatedBy,  iset.UPDATED_DATE_TIME as updatedDateTime,  iset.ACTIVATION_STATUS as activationStatus,  iset.VERSION as version,  iset.SUBJECT as subject,  iset.GRADE as grade,  iset.OWNER_CUSTOMER_ID as ownerCustomerId,  iset.PUBLISH_STATUS as publishStatus,  iset.ORIGINAL_CREATED_BY as originalCreatedBy,  iset.EXT_TST_ITEM_SET_ID as extTstItemSetId from  item_set iset where  iset.item_set_id = {itemSetId}")
    TestElement getTestElementById(Integer itemSetId) throws SQLException;
    
    @JdbcControl.SQL(statement = " select iset.ITEM_SET_ID as itemSetId, iset.ITEM_SET_TYPE as itemSetType, iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId, iset.ITEM_SET_NAME as itemSetName, iset.ITEM_SET_LEVEL as itemSetLevel, iset.ITEM_SET_FORM as itemSetForm, iset.MIN_GRADE as minGrade, iset.MAX_GRADE as maxGrade, iset.SAMPLE as sample, iset.TIME_LIMIT as timeLimit, iset.BREAK_TIME as breakTime, iset.MEDIA_PATH as mediaPath, iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName, iset.ITEM_SET_DESCRIPTION as itemSetDescription, iset.ITEM_SET_RULE_ID as itemSetRuleId, iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId, iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId, iset.CREATED_BY as createdBy, iset.CREATED_DATE_TIME as createdDateTime, iset.UPDATED_BY as updatedBy, iset.UPDATED_DATE_TIME as updatedDateTime, iset.ACTIVATION_STATUS as activationStatus, iset.VERSION as version, iset.SUBJECT as subject, iset.GRADE as grade, iset.OWNER_CUSTOMER_ID as ownerCustomerId, iset.PUBLISH_STATUS as publishStatus, iset.ORIGINAL_CREATED_BY as originalCreatedBy, iset.EXT_TST_ITEM_SET_ID as extTstItemSetId from item_set iset, item_set_parent itp where iset.item_set_id = itp.parent_item_set_id and itp.item_set_id = {itemSetId}")
    TestElement getParentItemset(Integer itemSetId) throws SQLException;

    /**
     * ISTEP CR032:Download Test
     * @param customerId
     * @return
     * @throws SQLException
     */
    @JdbcControl.SQL(statement = "select cdtr.customer_id  customerId, cdtr.product_id productId, cdtr.product_type productType, cdtr.product_name productName, cdtr.resource_uri resourceURI, cdtr.content_size contentSize from  customer_product_test_resource cdtr where cdtr.customer_id = {customerId}",
            arrayMaxLength = 100000)
    CustomerTestResource [] getCustomerTestResources(Integer customerId) throws SQLException;
       
    @JdbcControl.SQL(statement = "select iset.ITEM_SET_ID  as itemSetId, iset.ITEM_SET_NAME as itemSetName, max(ontc.RANDOM_DISTRACTOR_ALLOWABLE) as isRandomize, max(ontc.override_form_assignment) as overrideFormAssignmentMethod,  max(ontc.override_login_start_date) as overrideLoginStartDate  from item_set iset , org_node_test_catalog ontc  where  ontc.item_set_id = iset.item_set_id  and ontc.activation_status = 'AC'  and iset.activation_status = 'AC'  and iset.item_set_id = {itemSetId}  group by iset.ITEM_SET_ID , iset.ITEM_SET_NAME")
    TestElement getTestElementMinInfoById(Integer itemSetId) throws SQLException;
    static final long serialVersionUID = 1L;
}