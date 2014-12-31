package com.ctb.control.db; 

import java.sql.SQLException;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.testAdmin.ActiveTest;
import com.ctb.bean.testAdmin.CustomerTestResource;
import com.ctb.bean.testAdmin.Item;
import com.ctb.bean.testAdmin.ItemResponseAndScore;
import com.ctb.bean.testAdmin.ScoreDetails;
import com.ctb.bean.testAdmin.StudentManifest;
import com.ctb.bean.testAdmin.TestElement;

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
    @JdbcControl.SQL(statement = "select distinct  iset.ITEM_SET_ID as itemSetId,  iset.ITEM_SET_TYPE as itemSetType,  iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,  iset.ITEM_SET_NAME as itemSetName,  iset.ITEM_SET_LEVEL as itemSetLevel,  iset.ITEM_SET_FORM as itemSetForm,  iset.MIN_GRADE as minGrade,  iset.MAX_GRADE as maxGrade,  iset.SAMPLE as sample,  iset.TIME_LIMIT as timeLimit,  iset.BREAK_TIME as breakTime,  iset.MEDIA_PATH as mediaPath,  iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,  iset.ITEM_SET_DESCRIPTION as itemSetDescription,  iset.ITEM_SET_RULE_ID as itemSetRuleId,  iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,  iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,  iset.CREATED_BY as createdBy,  iset.CREATED_DATE_TIME as createdDateTime,  iset.UPDATED_BY as updatedBy,  iset.UPDATED_DATE_TIME as updatedDateTime,  iset.ACTIVATION_STATUS as activationStatus,  iset.VERSION as version,  iset.SUBJECT as subject,  iset.GRADE as grade,  iset.OWNER_CUSTOMER_ID as ownerCustomerId,  iset.PUBLISH_STATUS as publishStatus,  iset.ORIGINAL_CREATED_BY as originalCreatedBy,  iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,  max(ontc.RANDOM_DISTRACTOR_ALLOWABLE) as isRandomize,  max(ontc.override_form_assignment) as overrideFormAssignmentMethod,  max(ontc.override_login_start_date) as overrideLoginStartDate, min(ontc.OVERRIDE_LOGIN_END_DATE) as overrideLoginEndDate from  product prod,  item_set_product isp,  item_set iset, \t  org_node_test_catalog ontc, \t  user_role urole, \t  users where  prod.activation_status = 'AC'  and iset.activation_status = 'AC'  and isp.product_id = prod.product_id  and iset.item_set_id = isp.item_set_id  and iset.item_set_type = 'TC'  and prod.product_id = {productId}  and ontc.item_set_id = iset.item_set_id  and ontc.activation_status = 'AC' \t  and urole.org_node_id = ontc.org_node_id \t  and urole.activation_status = 'AC' \t  and users.user_id = urole.user_id \t  and users.user_name = {userName} \tgroup by  iset.ITEM_SET_ID,  iset.ITEM_SET_TYPE,  iset.ITEM_SET_CATEGORY_ID,  iset.ITEM_SET_NAME,  iset.ITEM_SET_LEVEL,  iset.ITEM_SET_FORM,  iset.MIN_GRADE,  iset.MAX_GRADE,  iset.SAMPLE,  iset.TIME_LIMIT,  iset.BREAK_TIME,  iset.MEDIA_PATH,  iset.ITEM_SET_DISPLAY_NAME,  iset.ITEM_SET_DESCRIPTION,  iset.ITEM_SET_RULE_ID,  iset.EXT_EMS_ITEM_SET_ID,  iset.EXT_CMS_ITEM_SET_ID,  iset.CREATED_BY,  iset.CREATED_DATE_TIME,  iset.UPDATED_BY,  iset.UPDATED_DATE_TIME,  iset.ACTIVATION_STATUS,  iset.VERSION,  iset.SUBJECT,  iset.GRADE,  iset.OWNER_CUSTOMER_ID,  iset.PUBLISH_STATUS,  iset.ORIGINAL_CREATED_BY,  iset.EXT_TST_ITEM_SET_ID",
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
    @JdbcControl.SQL(statement = "select distinct  iset.ITEM_SET_ID as itemSetId,  iset.ITEM_SET_TYPE as itemSetType,  iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,  iset.ITEM_SET_NAME as itemSetName,  iset.ITEM_SET_LEVEL as itemSetLevel,  iset.ITEM_SET_FORM as itemSetForm,  iset.MIN_GRADE as minGrade,  iset.MAX_GRADE as maxGrade,  iset.SAMPLE as sample,  iset.TIME_LIMIT as timeLimit,  iset.BREAK_TIME as breakTime,  iset.MEDIA_PATH as mediaPath,  iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,  iset.ITEM_SET_DESCRIPTION as itemSetDescription,  iset.ITEM_SET_RULE_ID as itemSetRuleId,  iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,  iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,  iset.CREATED_BY as createdBy,  iset.CREATED_DATE_TIME as createdDateTime,  iset.UPDATED_BY as updatedBy,  iset.UPDATED_DATE_TIME as updatedDateTime,  iset.ACTIVATION_STATUS as activationStatus,  iset.VERSION as version,  iset.SUBJECT as subject,  iset.GRADE as grade,  iset.OWNER_CUSTOMER_ID as ownerCustomerId,  iset.PUBLISH_STATUS as publishStatus,  iset.ORIGINAL_CREATED_BY as originalCreatedBy,  iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,  max(ontc.override_form_assignment) as overrideFormAssignmentMethod,  max(ontc.override_login_start_date) as overrideLoginStartDate from  product prod,  item_set_product isp,  item_set iset,  org_node_test_catalog ontc,  user_role urole,  users,  program prog,  product prodfamily where  prod.activation_status = 'AC'  and iset.activation_status = 'AC'  and isp.product_id = prod.product_id  and iset.item_set_id = isp.item_set_id  and iset.item_set_type = 'TC'  and prod.product_id = prodfamily.product_id  and prog.product_id = prodfamily.parent_product_id  and prog.program_id = {programId}  and ontc.item_set_id = iset.item_set_id  and ontc.activation_status = 'AC'  and urole.org_node_id = ontc.org_node_id  and urole.activation_status = 'AC'  and prog.activation_status = 'AC'  and users.user_id = urole.user_id  and users.user_name = {userName}  and prog.customer_id = ontc.customer_id group by  iset.ITEM_SET_ID,  iset.ITEM_SET_TYPE,  iset.ITEM_SET_CATEGORY_ID,  iset.ITEM_SET_NAME,  iset.ITEM_SET_LEVEL,  iset.ITEM_SET_FORM,  iset.MIN_GRADE,  iset.MAX_GRADE,  iset.SAMPLE,  iset.TIME_LIMIT,  iset.BREAK_TIME,  iset.MEDIA_PATH,  iset.ITEM_SET_DISPLAY_NAME,  iset.ITEM_SET_DESCRIPTION,  iset.ITEM_SET_RULE_ID,  iset.EXT_EMS_ITEM_SET_ID,  iset.EXT_CMS_ITEM_SET_ID,  iset.CREATED_BY,  iset.CREATED_DATE_TIME,  iset.UPDATED_BY,  iset.UPDATED_DATE_TIME,  iset.ACTIVATION_STATUS,  iset.VERSION,  iset.SUBJECT,  iset.GRADE,  iset.OWNER_CUSTOMER_ID,  iset.PUBLISH_STATUS,  iset.ORIGINAL_CREATED_BY,  iset.EXT_TST_ITEM_SET_ID",
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
     *      tais.locator_subtest as islocatorChecked
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
    @JdbcControl.SQL(statement = "select  iset.ITEM_SET_ID as itemSetId,  iset.ITEM_SET_TYPE as itemSetType,  iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,  iset.ITEM_SET_NAME as itemSetName,  iset.ITEM_SET_LEVEL as itemSetLevel,  tais.ITEM_SET_FORM as itemSetForm,  iset.MIN_GRADE as minGrade,  iset.MAX_GRADE as maxGrade,  iset.SAMPLE as sample,  iset.TIME_LIMIT as timeLimit,  iset.BREAK_TIME as breakTime,  iset.MEDIA_PATH as mediaPath,  iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,  iset.ITEM_SET_DESCRIPTION as itemSetDescription,  iset.ITEM_SET_RULE_ID as itemSetRuleId,  iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,  iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,  iset.CREATED_BY as createdBy,  iset.CREATED_DATE_TIME as createdDateTime,  iset.UPDATED_BY as updatedBy,  iset.UPDATED_DATE_TIME as updatedDateTime,  iset.ACTIVATION_STATUS as activationStatus,  iset.VERSION as version,  iset.SUBJECT as subject,  iset.GRADE as grade,  iset.OWNER_CUSTOMER_ID as ownerCustomerId,  iset.PUBLISH_STATUS as publishStatus,  iset.ORIGINAL_CREATED_BY as originalCreatedBy,  iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,  tais.access_code as accessCode, tais.locator_subtest as islocatorChecked, isp.item_set_group as itemSetGroup,  tais.session_default as sessionDefault from  item_set iset,  item_set_parent isp,  test_admin ta,  test_admin_item_set tais where  iset.activation_status = 'AC'  and iset.item_set_id = isp.item_set_id  and isp.parent_item_set_id = ta.item_set_id  and tais.item_set_id = iset.item_set_id  and tais.test_Admin_id = ta.test_admin_id  and ta.test_admin_id = {testAdminId} order by  tais.item_set_order",
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
     *      tais.locator_subtest as islocatorChecked
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
    @JdbcControl.SQL(statement = "select  iset.ITEM_SET_ID as itemSetId,  iset.ITEM_SET_TYPE as itemSetType,  iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,  iset.ITEM_SET_NAME as itemSetName,  iset.ITEM_SET_LEVEL as itemSetLevel,  tais.ITEM_SET_FORM as itemSetForm,  iset.MIN_GRADE as minGrade,  iset.MAX_GRADE as maxGrade,  iset.SAMPLE as sample,  iset.TIME_LIMIT as timeLimit,  iset.BREAK_TIME as breakTime,  iset.MEDIA_PATH as mediaPath,  iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,  iset.ITEM_SET_DESCRIPTION as itemSetDescription,  iset.ITEM_SET_RULE_ID as itemSetRuleId,  iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,  iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,  iset.CREATED_BY as createdBy,  iset.CREATED_DATE_TIME as createdDateTime,  iset.UPDATED_BY as updatedBy,  iset.UPDATED_DATE_TIME as updatedDateTime,  iset.ACTIVATION_STATUS as activationStatus,  iset.VERSION as version,  iset.SUBJECT as subject,  iset.GRADE as grade,  iset.OWNER_CUSTOMER_ID as ownerCustomerId,  iset.PUBLISH_STATUS as publishStatus,  iset.ORIGINAL_CREATED_BY as originalCreatedBy,  iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,  tais.access_code as accessCode, tais.locator_subtest as islocatorChecked,  isp.item_set_group as itemSetGroup,  tais.session_default as sessionDefault from  item_set iset,  item_set_parent isp,  test_admin ta,  test_admin_item_set tais where  iset.activation_status = 'AC'  and iset.item_set_id = isp.item_set_id  and isp.parent_item_set_id = ta.item_set_id  and tais.item_set_id = iset.item_set_id  and tais.test_Admin_id = ta.test_admin_id  and tais.session_default = 'T'  and ta.test_admin_id = {testAdminId} order by  tais.item_set_order",
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
    @JdbcControl.SQL(statement = "select distinct  iset.item_set_form as form from  item_set iset,  item_set_ancestor isa  where  iset.item_set_id = isa.item_set_id  and iset.item_set_type = 'TD' and iset.item_set_level <> 'L'  and isa.ancestor_item_set_id = {itemSetId} order by  iset.item_set_form",
                     arrayMaxLength = 0, fetchSize=500 )
    String [] getFormsForTABETests(Integer itemSetId) throws SQLException;
    
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
       
    @JdbcControl.SQL(statement = "select iset.ITEM_SET_ID  as itemSetId, iset.ITEM_SET_NAME as itemSetName, max(ontc.RANDOM_DISTRACTOR_ALLOWABLE) as isRandomize, max(ontc.override_form_assignment) as overrideFormAssignmentMethod,  max(ontc.override_login_start_date) as overrideLoginStartDate,  min(ontc.OVERRIDE_LOGIN_END_DATE) as overrideLoginEndDate  from item_set iset , org_node_test_catalog ontc  where  ontc.item_set_id = iset.item_set_id  and ontc.activation_status = 'AC'  and iset.activation_status = 'AC'  and iset.item_set_id = {itemSetId} and ontc.customer_id = {customerId} group by iset.ITEM_SET_ID , iset.ITEM_SET_NAME")
    TestElement getTestElementMinInfoById(Integer customerId, Integer itemSetId) throws SQLException;
    
    @JdbcControl.SQL(statement = "select iset.ITEM_SET_ID  as itemSetId, iset.ITEM_SET_NAME as itemSetName, max(ontc.RANDOM_DISTRACTOR_ALLOWABLE) as isRandomize, max(ontc.override_form_assignment) as overrideFormAssignmentMethod,  max(ontc.override_login_start_date) as overrideLoginStartDate,  min(ontc.OVERRIDE_LOGIN_END_DATE) as overrideLoginEndDate  from item_set iset , org_node_test_catalog ontc  where  ontc.item_set_id = iset.item_set_id  and ontc.activation_status = 'AC'  and iset.activation_status = 'AC'  and iset.item_set_id = {itemSetId} and ontc.customer_id = {customerId} and ontc.org_node_id = {orgNodeId} group by iset.ITEM_SET_ID , iset.ITEM_SET_NAME")
    TestElement getTestElementMinInfoByIds(Integer customerId, Integer itemSetId, Integer orgNodeId) throws SQLException;
     
    @JdbcControl.SQL(statement = "select distinct iset.ITEM_SET_ID  as itemSetId, iset.ITEM_SET_NAME as itemSetName, max(ontc.RANDOM_DISTRACTOR_ALLOWABLE) as isRandomize, max(ontc.override_form_assignment) as overrideFormAssignmentMethod,  max(ontc.override_login_start_date) as overrideLoginStartDate,  min(ontc.OVERRIDE_LOGIN_END_DATE) as overrideLoginEndDate  from item_set iset , org_node_test_catalog ontc , USERS USR, USER_ROLE UROLE  where  ontc.item_set_id = iset.item_set_id  and ontc.activation_status = 'AC'  and iset.activation_status = 'AC'  and iset.item_set_id = {itemSetId} and ontc.customer_id = {customerId} AND USR.USER_ID =  UROLE.USER_ID   AND UROLE.ORG_NODE_ID = ONTC.ORG_NODE_ID   AND USR.USER_NAME ={userName} group by iset.ITEM_SET_ID , iset.ITEM_SET_NAME")
    TestElement getTestElementMinInfoByIdsAndUserName(Integer customerId, Integer itemSetId, String userName) throws SQLException;
    
    @JdbcControl.SQL(statement = "SELECT ISET.ITEM_SET_ID  AS ITEMSETID,  ISET.ITEM_SET_NAME   AS ITEMSETNAME, ISET.ITEM_SET_LEVEL  AS ITEMSETLEVEL,  ISET.ITEM_SET_FORM AS ITEMSETFORM, ISET.ITEM_SET_DISPLAY_NAME AS ITEMSETDISPLAYNAME, ISET.GRADE   AS GRADE,  ISET.TIME_LIMIT as timeLimit FROM TEST_ADMIN    ADMIN,  TEST_CATALOG    CATALOG,  ITEM_SET_PARENT ISP,  ITEM_SET  ISET  WHERE CATALOG.TEST_CATALOG_ID = ADMIN.TEST_CATALOG_ID  AND CATALOG.ITEM_SET_ID = ISP.PARENT_ITEM_SET_ID AND ISP.ITEM_SET_ID = ISET.ITEM_SET_ID    AND ISET.ITEM_SET_TYPE = 'TS'    AND ISET.ACTIVATION_STATUS = 'AC'    AND ADMIN.TEST_ADMIN_ID = {testAdminId}  ORDER BY ISP.ITEM_SET_SORT_ORDER")
    TestElement[] getTestElementByTestAdmin(Integer testAdminId) throws SQLException;
   
    @JdbcControl.SQL(statement = " SELECT ISETB.ITEM_SET_ID AS ITEMSETID, ISETB.ITEM_SET_NAME AS ITEMSETNAME FROM ITEM_SET_PARENT ISP, ITEM_SET ISETA, ITEM_SET ISETB WHERE ISETA.ITEM_SET_ID = {itemSetIdTC} AND ISETA.ITEM_SET_TYPE = 'TC' AND ISP.PARENT_ITEM_SET_ID = ISETA.ITEM_SET_ID AND ISP.ITEM_SET_ID = ISETB.ITEM_SET_ID AND ISETB.ITEM_SET_TYPE = 'TS' AND ISETB.ACTIVATION_STATUS = 'AC' ORDER BY ISP.ITEM_SET_SORT_ORDER ")
    TestElement[] getAllItemSetIdTSbyItemSetIdTC(Integer itemSetIdTC) throws SQLException;
    
    @JdbcControl.SQL(statement = " SELECT ISETB.ITEM_SET_ID AS ITEMSETID, ISETB.ITEM_SET_NAME AS ITEMSETNAME FROM ITEM_SET_PARENT ISP, ITEM_SET ISETA, ITEM_SET ISETB WHERE ISETA.ITEM_SET_ID = {itemSetIdTC} AND ISETA.ITEM_SET_TYPE = 'TC' AND ISP.PARENT_ITEM_SET_ID = ISETA.ITEM_SET_ID AND ISP.ITEM_SET_ID = ISETB.ITEM_SET_ID AND ISETB.ITEM_SET_TYPE = 'TS' AND ISETB.ACTIVATION_STATUS = 'AC' AND UPPER(ISETB.ITEM_SET_NAME) = UPPER({itemSetName}) ")
    TestElement getItemSetIdTSbyItemSetIdTCandItemSetName(Integer itemSetIdTC, String itemSetName) throws SQLException;
    
    @JdbcControl.SQL(statement = "select  iset.ITEM_SET_ID as itemSetId,  iset.ITEM_SET_TYPE as itemSetType,  iset.ITEM_SET_CATEGORY_ID as itemSetCategoryId,  iset.ITEM_SET_NAME as itemSetName,  iset.ITEM_SET_LEVEL as itemSetLevel,  iset.ITEM_SET_FORM as itemSetForm,  iset.MIN_GRADE as minGrade,  iset.MAX_GRADE as maxGrade,  iset.SAMPLE as sample,  iset.TIME_LIMIT as timeLimit,  iset.BREAK_TIME as breakTime,  iset.MEDIA_PATH as mediaPath,  iset.ITEM_SET_DISPLAY_NAME as itemSetDisplayName,  iset.ITEM_SET_DESCRIPTION as itemSetDescription,  iset.ITEM_SET_RULE_ID as itemSetRuleId,  iset.EXT_EMS_ITEM_SET_ID as extEmsItemSetId,  iset.EXT_CMS_ITEM_SET_ID as extCmsItemSetId,  iset.CREATED_BY as createdBy,  iset.CREATED_DATE_TIME as createdDateTime,  iset.UPDATED_BY as updatedBy,  iset.UPDATED_DATE_TIME as updatedDateTime,  iset.ACTIVATION_STATUS as activationStatus,  iset.VERSION as version,  iset.SUBJECT as subject,  iset.GRADE as grade,  iset.OWNER_CUSTOMER_ID as ownerCustomerId,  iset.PUBLISH_STATUS as publishStatus,  iset.ORIGINAL_CREATED_BY as originalCreatedBy,  iset.EXT_TST_ITEM_SET_ID as extTstItemSetId,  isp.item_set_group as itemSetGroup from  item_set iset,  item_set_parent isp where  iset.activation_status = 'AC'  and iset.item_set_id = isp.item_set_id  and iset.item_set_type = {itemSetType}  and isp.parent_item_set_id = {parentItemSetId}  order by  isp.item_set_sort_order",
            arrayMaxLength = 0, fetchSize=500)
    TestElement [] getTestElementsForParentForTD(Integer parentItemSetId, String itemSetType) throws SQLException;
    static final long serialVersionUID = 1L;
    
    @JdbcControl.SQL(statement = "SELECT DISTINCT ISET.ITEM_SET_ID   AS ITEMSETID, ISET.ITEM_SET_TYPE         AS ITEMSETTYPE, ISET.ITEM_SET_CATEGORY_ID  AS ITEMSETCATEGORYID, ISET.ITEM_SET_NAME         AS ITEMSETNAME, ISET.ITEM_SET_LEVEL        AS ITEMSETLEVEL, TAIS.ITEM_SET_FORM         AS ITEMSETFORM, ISET.MIN_GRADE             AS MINGRADE, ISET.MAX_GRADE             AS MAXGRADE, ISET.SAMPLE                AS SAMPLE, ISET.TIME_LIMIT            AS TIMELIMIT, ISET.BREAK_TIME            AS BREAKTIME, ISET.MEDIA_PATH            AS MEDIAPATH, ISET.ITEM_SET_DISPLAY_NAME AS ITEMSETDISPLAYNAME, ISET.ITEM_SET_DESCRIPTION  AS ITEMSETDESCRIPTION, ISET.ITEM_SET_RULE_ID      AS ITEMSETRULEID, ISET.EXT_EMS_ITEM_SET_ID   AS EXTEMSITEMSETID, ISET.EXT_CMS_ITEM_SET_ID   AS EXTCMSITEMSETID, ISET.CREATED_BY            AS CREATEDBY, ISET.CREATED_DATE_TIME     AS CREATEDDATETIME, ISET.UPDATED_BY            AS UPDATEDBY, ISET.UPDATED_DATE_TIME     AS UPDATEDDATETIME, ISET.ACTIVATION_STATUS     AS ACTIVATIONSTATUS, ISET.VERSION               AS VERSION, ISET.SUBJECT               AS SUBJECT, ISET.GRADE                 AS GRADE, ISET.OWNER_CUSTOMER_ID     AS OWNERCUSTOMERID, ISET.PUBLISH_STATUS        AS PUBLISHSTATUS, ISET.ORIGINAL_CREATED_BY   AS ORIGINALCREATEDBY, ISET.EXT_TST_ITEM_SET_ID   AS EXTTSTITEMSETID, TAIS.ACCESS_CODE           AS ACCESSCODE, ISP.ITEM_SET_GROUP         AS ITEMSETGROUP, TAIS.SESSION_DEFAULT       AS SESSIONDEFAULT FROM ITEM_SET                ISET, ITEM_SET_PARENT         ISP, TEST_ADMIN              TA, TEST_ADMIN_ITEM_SET     TAIS, STUDENT_ITEM_SET_STATUS SISS, TEST_ROSTER             TR WHERE TA.TEST_ADMIN_ID = {testAdminId} AND TAIS.TEST_ADMIN_ID = TA.TEST_ADMIN_ID AND TAIS.ITEM_SET_ID = ISP.PARENT_ITEM_SET_ID AND ISET.ITEM_SET_ID = ISP.ITEM_SET_ID AND ISET.ITEM_SET_LEVEL = 'L' AND SISS.ITEM_SET_ID = ISET.ITEM_SET_ID AND SISS.TEST_ROSTER_ID = TR.TEST_ROSTER_ID AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID ORDER BY ISET.ITEM_SET_ID",
                     arrayMaxLength = 100000)
    TestElement [] getTestElementsTDForLocatorSession(Integer testAdminId) throws SQLException;
    
    @JdbcControl.SQL(statement = "SELECT ISET.ITEM_SET_ID AS itemSetId, ISET.ITEM_SET_NAME AS itemSetName FROM ITEM_SET ISET {sql: itemSetIds}",
            arrayMaxLength = 100000)
    StudentManifest [] getSubtestName(String itemSetIds) throws SQLException;

    @JdbcControl.SQL(statement = "select item_id as itemId, item_display_name itemName, item_type as itemType, correct_answer correctAnswer from item where item_id in (select item_id from item_set_item where item_set_id = {itemSetId})",
            arrayMaxLength = 100000)
    Item [] getItems(Integer itemSetId) throws SQLException;
    
    @JdbcControl.SQL(statement = "SELECT DISTINCT PROD.PRODUCT_ID || CONTENTAREA.ITEM_SET_ID as extCmsItemSetId , CONTENTAREA.ITEM_SET_NAME AS itemSetName FROM ITEM_SET CONTENTAREA, ITEM_SET_CATEGORY ISC, TEST_ADMIN TA, PRODUCT PROD, ITEM_SET_ITEM ISI1, ITEM_SET_ITEM ISI2, ITEM_SET_ANCESTOR ISA, ITEM_SET ISET, ITEM WHERE TA.TEST_ADMIN_ID = {testAdminId} AND TA.PRODUCT_ID = PROD.PRODUCT_ID AND ISC.FRAMEWORK_PRODUCT_ID = PROD.PARENT_PRODUCT_ID AND ISC.ITEM_SET_CATEGORY_LEVEL = PROD.CONTENT_AREA_LEVEL AND CONTENTAREA.ITEM_SET_CATEGORY_ID = ISC.ITEM_SET_CATEGORY_ID AND ISI1.ITEM_SET_ID = {itemSetId} AND ISI1.ITEM_ID = ITEM.ITEM_ID AND ITEM.ITEM_ID = ISI2.ITEM_ID AND ISI2.ITEM_SET_ID = ISET.ITEM_SET_ID AND ISET.ITEM_SET_TYPE = 'RE' AND ISA.ITEM_SET_ID  = ISET.ITEM_SET_ID AND ISA.ANCESTOR_ITEM_SET_ID = CONTENTAREA.ITEM_SET_ID AND UPPER(CONTENTAREA.ITEM_SET_NAME) IN ('SPEAKING','READING','WRITING','LISTENING') ", arrayMaxLength = 1)
    TestElement getContentAreaId (Integer testAdminId, Integer itemSetId) throws SQLException;
    
    @JdbcControl.SQL(statement = "SELECT DISTINCT PROD.PRODUCT_ID || CONTENTAREA.ITEM_SET_ID as extCmsItemSetId, CONTENTAREA.ITEM_SET_NAME AS itemSetName FROM ITEM_SET CONTENTAREA, ITEM_SET_CATEGORY ISC, TEST_ADMIN TA, PRODUCT PROD, ITEM_SET_ITEM ISI1, ITEM_SET_ITEM ISI2, ITEM_SET_ANCESTOR ISA, ITEM_SET ISET, ITEM, ITEM_SET ISETTD WHERE TA.TEST_ADMIN_ID = {testAdminId} AND TA.PRODUCT_ID = PROD.PRODUCT_ID AND ISC.FRAMEWORK_PRODUCT_ID = PROD.PARENT_PRODUCT_ID AND ISC.ITEM_SET_CATEGORY_LEVEL = PROD.CONTENT_AREA_LEVEL AND CONTENTAREA.ITEM_SET_CATEGORY_ID = ISC.ITEM_SET_CATEGORY_ID AND ISETTD.ITEM_SET_ID = {itemSetId} AND ISETTD.SAMPLE = 'F' AND ISETTD.ITEM_SET_ID = ISI1.ITEM_SET_ID AND ISI1.ITEM_ID = ITEM.ITEM_ID AND ITEM.ITEM_ID = ISI2.ITEM_ID AND ISI2.ITEM_SET_ID = ISET.ITEM_SET_ID AND ISET.ITEM_SET_TYPE = 'RE' AND ISA.ITEM_SET_ID = ISET.ITEM_SET_ID AND ISA.ANCESTOR_ITEM_SET_ID = CONTENTAREA.ITEM_SET_ID", arrayMaxLength = 1)
    TestElement getContentAreaIdForTASC (Integer testAdminId, Integer itemSetId) throws SQLException;
    
    @JdbcControl.SQL(statement = "SELECT ACTIVATION_STATUS  as activationStatus FROM ORG_NODE_TEST_CATALOG WHERE CUSTOMER_ID= {customerId} AND ITEM_SET_ID = {itemsetIdTC} AND ORG_NODE_ID={orgNodeId}" ,arrayMaxLength = 1)
    String getSelectedTestActivationStatus(Integer customerId,Integer itemsetIdTC,Integer orgNodeId) throws SQLException;
    
    //@JdbcControl.SQL(statement = "SELECT DECODE(UPPER(DD.ANSWER_AREA), 'GRID', 'GR', DD.ITEM_TYPE) AS itemType, DD.ANSWER_AREA AS itemAnswerArea, DD.ITEM_ID AS itemId, DD.ITEM_SORT_ORDER AS itemOrder, DD.ITEM_SET_ID AS itemSetIdTD, DD.ITEM_SET_NAME AS itemSetNameTD, DD.TDCOMPLETIONSTATUS AS completionStatusTD, DD.PARENT_SET_ID AS itemSetIdTS, DD.RESPONSE AS response, CASE DD.ITEM_TYPE WHEN 'CR' THEN (SELECT CR.CONSTRUCTED_RESPONSE FROM ITEM_RESPONSE_CR CR WHERE CR.ITEM_ID = DD.ITEM_ID AND CR.TEST_ROSTER_ID = DD.TEST_ROSTER_ID) ELSE NULL END AS crResponse FROM (SELECT DISTINCT DERIVED.TEST_ROSTER_ID, DERIVED.ITEM_TYPE, DERIVED.ANSWER_AREA, DERIVED.ITEM_ID, DERIVED.ITEM_SORT_ORDER, DERIVED.ITEM_SET_ID, DERIVED.ITEM_SET_NAME, DERIVED.TDCOMPLETIONSTATUS, DERIVED.PARENT_SET_ID, DECODE(DERIVED.ITEM_TYPE, 'SR', (SELECT IR.RESPONSE FROM ITEM_RESPONSE IR WHERE IR.TEST_ROSTER_ID = DERIVED.TEST_ROSTER_ID AND IR.ITEM_SET_ID = DERIVED.ITEM_SET_ID AND IR.ITEM_ID = DERIVED.ITEM_ID AND IR.RESPONSE_SEQ_NUM = (SELECT MAX(RESPONSE_SEQ_NUM) FROM ITEM_RESPONSE WHERE TEST_ROSTER_ID = IR.TEST_ROSTER_ID AND ITEM_ID = IR.ITEM_ID))) AS RESPONSE FROM (SELECT TR.TEST_ROSTER_ID, ISET.ITEM_SET_ID, ISET.ITEM_SET_NAME, ITEM.ITEM_TYPE, ITEM.ITEM_ID, ITEM.ANSWER_AREA, ISI.ITEM_SORT_ORDER, ISP.PARENT_ITEM_SET_ID AS PARENT_SET_ID, SISS.COMPLETION_STATUS AS TDCOMPLETIONSTATUS FROM ITEM_SET_ITEM           ISI, ITEM                    ITEM, TEST_ROSTER             TR, ITEM_SET_PARENT         ISP, ITEM_SET                ISET, STUDENT_ITEM_SET_STATUS SISS WHERE TR.TEST_ROSTER_ID = {testRosterId} AND ISP.PARENT_ITEM_SET_ID IN ({sql: itemSetIdsTS}) AND ISP.ITEM_SET_ID = ISET.ITEM_SET_ID AND ISET.SAMPLE <> 'T' AND ISI.ITEM_SET_ID = ISET.ITEM_SET_ID AND (ISI.FIELD_TEST <> 'T' OR ISI.SUPPRESSED = 'F') AND ITEM.ITEM_ID = ISI.ITEM_ID AND ITEM.ITEM_TYPE IN ('SR', 'CR') AND (SISS.ITEM_SET_ID, SISS.TEST_ROSTER_ID) IN ((ISET.ITEM_SET_ID, TR.TEST_ROSTER_ID)) {sql: customerClause}) DERIVED, ITEM_RESPONSE_CR IRC WHERE DERIVED.TEST_ROSTER_ID = IRC.TEST_ROSTER_ID(+) AND DERIVED.ITEM_SET_ID = IRC.ITEM_SET_ID(+) AND DERIVED.ITEM_ID = IRC.ITEM_ID(+)) DD", arrayMaxLength = 0, fetchSize=50)
    @JdbcControl.SQL(statement = "SELECT DECODE(UPPER(DD.ANSWER_AREA), 'GRID', 'GR', DD.ITEM_TYPE) AS itemType, DD.ANSWER_AREA AS itemAnswerArea, DD.ITEM_ID AS itemId, DD.ITEM_SORT_ORDER AS itemOrder, DD.ITEM_SET_ID AS itemSetIdTD, DD.ITEM_SET_NAME AS itemSetNameTD, DD.TDCOMPLETIONSTATUS AS completionStatusTD, DD.PARENT_SET_ID AS itemSetIdTS, DD.OBJECTIVENAME AS contentDomain, DD.MAX_POINTS AS possibleScore, DD.RESPONSE AS response, CASE DD.ITEM_TYPE WHEN 'CR' THEN (SELECT CR.CONSTRUCTED_RESPONSE FROM ITEM_RESPONSE_CR CR WHERE CR.ITEM_ID = DD.ITEM_ID AND CR.TEST_ROSTER_ID = DD.TEST_ROSTER_ID) ELSE NULL END AS crResponse FROM (SELECT DISTINCT DERIVED.TEST_ROSTER_ID, DERIVED.ITEM_TYPE, DERIVED.ANSWER_AREA, DERIVED.ITEM_ID, DERIVED.ITEM_SORT_ORDER, DERIVED.ITEM_SET_ID, DERIVED.ITEM_SET_NAME, DERIVED.TDCOMPLETIONSTATUS, DERIVED.PARENT_SET_ID, DERIVED.OBJECTIVENAME, DERIVED.MAX_POINTS, DECODE(DERIVED.ITEM_TYPE, 'SR', (SELECT IR.RESPONSE FROM ITEM_RESPONSE IR WHERE IR.TEST_ROSTER_ID = DERIVED.TEST_ROSTER_ID AND IR.ITEM_SET_ID = DERIVED.ITEM_SET_ID AND IR.ITEM_ID = DERIVED.ITEM_ID AND IR.RESPONSE_SEQ_NUM = (SELECT MAX(RESPONSE_SEQ_NUM) FROM ITEM_RESPONSE WHERE TEST_ROSTER_ID = IR.TEST_ROSTER_ID AND ITEM_ID = IR.ITEM_ID))) AS RESPONSE FROM (SELECT TR.TEST_ROSTER_ID, ISET.ITEM_SET_ID, ISET.ITEM_SET_NAME, ITEM.ITEM_TYPE, ITEM.ITEM_ID, ITEM.ANSWER_AREA, ISI.ITEM_SORT_ORDER, ISP.PARENT_ITEM_SET_ID AS PARENT_SET_ID, SISS.COMPLETION_STATUS AS TDCOMPLETIONSTATUS, DP.MAX_POINTS, LISTAGG(OBJ.ITEM_SET_NAME, ':') WITHIN GROUP( ORDER BY TR.TEST_ROSTER_ID, ISET.ITEM_SET_ID, ISET.ITEM_SET_NAME, ITEM.ITEM_TYPE, ITEM.ITEM_ID, ITEM.ANSWER_AREA, ISI.ITEM_SORT_ORDER, ISP.PARENT_ITEM_SET_ID, SISS.COMPLETION_STATUS, DP.MAX_POINTS) AS OBJECTIVENAME FROM ITEM_SET_ITEM ISI, ITEM ITEM, TEST_ADMIN TA, TEST_ROSTER TR, ITEM_SET_PARENT ISP, ITEM_SET ISET, STUDENT_ITEM_SET_STATUS SISS, ITEM_SET_ITEM OBJSET, ITEM_SET OBJ, ITEM_SET_ANCESTOR ISA, ITEM_SET_CATEGORY ISC, PRODUCT PROD, DATAPOINT DP WHERE TR.TEST_ROSTER_ID = {testRosterId} AND ISP.PARENT_ITEM_SET_ID IN ({SQL: itemSetIdsTS}) AND ISP.ITEM_SET_ID = ISET.ITEM_SET_ID AND ISET.SAMPLE <> 'T' AND ISI.ITEM_SET_ID = ISET.ITEM_SET_ID AND (ISI.FIELD_TEST <> 'T' OR ISI.SUPPRESSED = 'F') AND ITEM.ITEM_ID = ISI.ITEM_ID AND ITEM.ITEM_TYPE IN ('SR', 'CR') AND (SISS.ITEM_SET_ID, SISS.TEST_ROSTER_ID) IN ((ISET.ITEM_SET_ID, TR.TEST_ROSTER_ID)) AND TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID AND TA.PRODUCT_ID = PROD.PRODUCT_ID AND OBJSET.ITEM_ID = ITEM.ITEM_ID AND OBJSET.ITEM_SET_ID = ISA.ITEM_SET_ID AND ISA.ANCESTOR_ITEM_SET_ID = OBJ.ITEM_SET_ID AND OBJ.ITEM_SET_CATEGORY_ID = ISC.ITEM_SET_CATEGORY_ID AND ISC.ITEM_SET_CATEGORY_LEVEL = PROD.SEC_SCORING_ITEM_SET_LEVEL AND ISC.FRAMEWORK_PRODUCT_ID = PROD.PARENT_PRODUCT_ID {SQL: customerClause} GROUP BY TR.TEST_ROSTER_ID, ISET.ITEM_SET_ID, ISET.ITEM_SET_NAME, ITEM.ITEM_TYPE, ITEM.ITEM_ID, ITEM.ANSWER_AREA, ISI.ITEM_SORT_ORDER, ISP.PARENT_ITEM_SET_ID, SISS.COMPLETION_STATUS, DP.MAX_POINTS) DERIVED, ITEM_RESPONSE_CR IRC WHERE DERIVED.TEST_ROSTER_ID = IRC.TEST_ROSTER_ID(+) AND DERIVED.ITEM_SET_ID = IRC.ITEM_SET_ID(+) AND DERIVED.ITEM_ID = IRC.ITEM_ID(+)) DD", arrayMaxLength = 0, fetchSize=50)
    ItemResponseAndScore[] getScoreElementsForTS(String itemSetIdsTS, Integer testRosterId, String customerClause) throws SQLException;
       
    //@JdbcControl.SQL(statement = "SELECT DISTINCT IPP.ITEM_SET_ID AS itemSetId, TAIS.ITEM_SET_ORDER AS itemSetOrder, IPP.ITEM_SET_NAME AS itemSetName, DECODE(INSTR(UPPER(IPP.ITEM_SET_NAME), 'LOCATOR'), 0, NULL, SISS.COMPLETION_STATUS) AS completionStatus, DECODE(PROD.PRODUCT_TYPE, 'TB', NULL, ISET.ITEM_SET_LEVEL) AS itemSetLevel, ISET.ITEM_SET_TYPE AS itemSetType, TAIS.ACCESS_CODE AS accessCode, TA.TEST_ADMIN_NAME AS testSessionName, TC.TEST_NAME AS testName, PROD.PRODUCT_TYPE AS productType FROM STUDENT_ITEM_SET_STATUS SISS, ITEM_SET ISET, ITEM_SET_PARENT ISP, ITEM_SET  IPP, TEST_ROSTER  TR, TEST_ADMIN TA, TEST_ADMIN_ITEM_SET TAIS, TEST_CATALOG TC, PRODUCT  PROD WHERE TR.TEST_ROSTER_ID = {testRosterId} AND SISS.TEST_ROSTER_ID = TR.TEST_ROSTER_ID AND SISS.ITEM_SET_ID = ISET.ITEM_SET_ID AND ISET.SAMPLE <> 'T' AND ISP.ITEM_SET_ID = ISET.ITEM_SET_ID AND IPP.ITEM_SET_ID = ISP.PARENT_ITEM_SET_ID AND TAIS.ITEM_SET_ID = IPP.ITEM_SET_ID AND TAIS.TEST_ADMIN_ID = TA.TEST_ADMIN_ID AND TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID AND TC.TEST_CATALOG_ID = TA.TEST_CATALOG_ID AND PROD.PRODUCT_ID = TC.PRODUCT_ID",   arrayMaxLength = 0, fetchSize=500) 
    @JdbcControl.SQL(statement = "SELECT DISTINCT IPP.ITEM_SET_ID AS itemSetId, TAIS.ITEM_SET_ORDER AS itemSetOrder, IPP.ITEM_SET_NAME AS itemSetName, ISET.ITEM_SET_ID AS itemSetIdTD, ISET.ITEM_SET_NAME AS itemSetNameTD, SISS.COMPLETION_STATUS AS completionStatusTD, DECODE(PROD.PRODUCT_TYPE, 'TB', NULL, ISET.ITEM_SET_LEVEL) AS itemSetLevel, ISET.ITEM_SET_TYPE AS itemSetType, TAIS.ACCESS_CODE AS accessCode, TA.TEST_ADMIN_NAME AS testSessionName, TC.TEST_NAME AS testName, PROD.PRODUCT_TYPE AS productType FROM STUDENT_ITEM_SET_STATUS SISS, ITEM_SET ISET, ITEM_SET_PARENT ISP, ITEM_SET  IPP, TEST_ROSTER TR, TEST_ADMIN TA, TEST_ADMIN_ITEM_SET TAIS, TEST_CATALOG TC, PRODUCT PROD WHERE TR.TEST_ROSTER_ID = {testRosterId} AND SISS.TEST_ROSTER_ID = TR.TEST_ROSTER_ID AND SISS.ITEM_SET_ID = ISET.ITEM_SET_ID AND ISET.SAMPLE <> 'T' AND ISP.ITEM_SET_ID = ISET.ITEM_SET_ID AND IPP.ITEM_SET_ID = ISP.PARENT_ITEM_SET_ID AND TAIS.ITEM_SET_ID = IPP.ITEM_SET_ID AND TAIS.TEST_ADMIN_ID = TA.TEST_ADMIN_ID AND TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID AND TC.TEST_CATALOG_ID = TA.TEST_CATALOG_ID AND PROD.PRODUCT_ID = TC.PRODUCT_ID",   arrayMaxLength = 0, fetchSize=500)
    ScoreDetails[] getAllItemSetForRoster(Integer testRosterId) throws SQLException;
    
}