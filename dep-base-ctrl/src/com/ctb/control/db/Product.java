package com.ctb.control.db; 

import com.bea.control.*;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.testAdmin.TestProduct; 
import com.ctb.bean.testAdmin.UserParentProductResource;

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
public interface Product extends JdbcControl
{ 
    /**
     * @jc:sql statement::
     * select 
     * 	   decode(count(ontc.product_id), 0, 'false', 'true') as visible
     * from
     * 	org_node_test_catalog ontc,
     * 	org_node_ancestor ona,
     * 	user_role urole,
     * 	users
     * where
     * 	 users.user_name = {userName}
     * 	 and urole.user_id = users.user_id
     * 	 and ona.ancestor_org_node_id = urole.org_node_id
     * 	 and ontc.org_node_id = ona.org_node_id
     * 	 and ontc.product_id = {productId}::
     */
    @JdbcControl.SQL(statement = "select  \t  decode(count(ontc.product_id), 0, 'false', 'true') as visible from \torg_node_test_catalog ontc, \torg_node_ancestor ona, \tuser_role urole, \tusers where \t users.user_name = {userName} \t and urole.user_id = users.user_id \t and ona.ancestor_org_node_id = urole.org_node_id \t and ontc.org_node_id = ona.org_node_id \t and ontc.product_id = {productId}")
    String checkVisibility(String userName, Integer productId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select distinct
     *      prod.product_id as productId,
     *      prod.product_name as productName,
     *      prod.version as version,
     *      prod.product_description as productDescription,
     *      prod.created_by as createdBy,
     *      prod.created_date_time as createdDateTime,
     *      prod.updated_by as updatedBy,
     *      prod.updated_date_time as updatedDateTime,
     *      prod.activation_status as activationStatus,
     *      prod.product_type as productType,
     *      prod.scoring_item_set_level as scoringItemSetLevel,
     *      prod.preview_item_set_level as previewItemSetLevel,
     *      prod.parent_product_id as parentProductId,
     *      prod.ext_product_id as extProductId,
     *      prod.content_area_level as contentAreaLevel,
     *      prod.internal_display_name as internalDisplayName,
     *      prod.sec_scoring_item_set_level as secScoringItemSetLevel,
     *      prod.ibs_show_cms_id as ibsShowCmsId,
     *      prod.printable as printable,
     *      prod.scannable as scannable,
     *      prod.keyenterable as keyenterable,
     *      prod.branding_type_code as brandingTypeCode,
     *      prod.acknowledgments_url as acknowledgmentsURL,
     *      prod.show_student_feedback as showStudentFeedback,
     *      prod.static_manifest as staticManifest,
     *      prod.session_manifest as sessionManifest,
     *      prod.subtests_selectable as subtestsSelectable,
     *      prod.subtests_orderable as subtestsOrderable,
     *      prod.subtests_levels_vary as subtestsLevelsVary,
     *      prod.off_grade_testing_disabled as offGradeTestingDisabled,
	 *		prod.license_enabled as productLicenseEnabled
     * from
     *      product prod,
     *      test_catalog cat,
     *      org_node_test_catalog ontc,
     *      user_role urole,
     *      users
     * where
     *      prod.activation_status = 'AC'
     *      and prod.product_id = cat.product_id
     *      and cat.activation_status = 'AC'
     *      and cat.test_catalog_id = ontc.test_catalog_id
     *      and ontc.activation_status = 'AC'
     *      and urole.org_node_id = ontc.org_node_id
     *      and urole.activation_status = 'AC'
     *      and users.user_id = urole.user_id
     *      and users.user_name = {userName}::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  prod.product_id as productId,  prod.product_name as productName,  prod.version as version,  prod.product_description as productDescription,  prod.created_by as createdBy,  prod.created_date_time as createdDateTime,  prod.updated_by as updatedBy,  prod.updated_date_time as updatedDateTime,  prod.activation_status as activationStatus,  prod.product_type as productType,  prod.scoring_item_set_level as scoringItemSetLevel,  prod.preview_item_set_level as previewItemSetLevel,  prod.parent_product_id as parentProductId,  prod.ext_product_id as extProductId,  prod.content_area_level as contentAreaLevel,  prod.internal_display_name as internalDisplayName,  prod.sec_scoring_item_set_level as secScoringItemSetLevel,  prod.ibs_show_cms_id as ibsShowCmsId,  prod.printable as printable,  prod.scannable as scannable,  prod.keyenterable as keyenterable,  prod.branding_type_code as brandingTypeCode,  prod.acknowledgments_url as acknowledgmentsURL,  prod.show_student_feedback as showStudentFeedback,  prod.static_manifest as staticManifest,  prod.session_manifest as sessionManifest,  prod.subtests_selectable as subtestsSelectable,  prod.subtests_orderable as subtestsOrderable,  prod.subtests_levels_vary as subtestsLevelsVary,  prod.off_grade_testing_disabled as offGradeTestingDisabled, \t\tprod.license_enabled as productLicenseEnabled from  product prod,  test_catalog cat,  org_node_test_catalog ontc,  user_role urole,  users where  prod.activation_status = 'AC'  and prod.product_id = cat.product_id  and cat.activation_status = 'AC'  and cat.test_catalog_id = ontc.test_catalog_id  and ontc.activation_status = 'AC'  and urole.org_node_id = ontc.org_node_id  and urole.activation_status = 'AC'  and users.user_id = urole.user_id  and users.user_name = {userName}",
                     arrayMaxLength = 100000)
    TestProduct [] getProductsForUser(String userName) throws SQLException;
    
    
    @JdbcControl.SQL(statement = "select distinct prod.product_id as productId, prod.product_name  as productName, cat.test_catalog_id  as catalogId, cat.test_name as testCatalogName from product prod,  test_catalog cat,  org_node_test_catalog ontc,  user_role urole,  users where prod.activation_status = 'AC' and prod.product_id = cat.product_id  and cat.activation_status = 'AC' and cat.test_catalog_id = ontc.test_catalog_id  and ontc.activation_status = 'AC' and urole.org_node_id = ontc.org_node_id and urole.activation_status = 'AC' and users.user_id = urole.user_id  and users.user_name = {userName} ",
            arrayMaxLength = 100000)
TestProduct [] getTestCatalogForUser(String userName) throws SQLException;


    /**
     * @jc:sql statement::
     * select distinct
     *      prod.product_id as productId,
     *      prod.product_name as productName,
     *      prod.version as version,
     *      prod.product_description as productDescription,
     *      prod.created_by as createdBy,
     *      prod.created_date_time as createdDateTime,
     *      prod.updated_by as updatedBy,
     *      prod.updated_date_time as updatedDateTime,
     *      prod.activation_status as activationStatus,
     *      prod.product_type as productType,
     *      prod.scoring_item_set_level as scoringItemSetLevel,
     *      prod.preview_item_set_level as previewItemSetLevel,
     *      prod.parent_product_id as parentProductId,
     *      prod.ext_product_id as extProductId,
     *      prod.content_area_level as contentAreaLevel,
     *      prod.internal_display_name as internalDisplayName,
     *      prod.sec_scoring_item_set_level as secScoringItemSetLevel,
     *      prod.ibs_show_cms_id as ibsShowCmsId,
     *      prod.printable as printable,
     *      prod.scannable as scannable,
     *      prod.keyenterable as keyenterable,
     *      prod.branding_type_code as brandingTypeCode,
     *      prod.acknowledgments_url as acknowledgmentsURL,
     *      prod.show_student_feedback as showStudentFeedback,
     *      prod.static_manifest as staticManifest,
     *      prod.session_manifest as sessionManifest,
     *      prod.subtests_selectable as subtestsSelectable,
     *      prod.subtests_orderable as subtestsOrderable,
     *      prod.subtests_levels_vary as subtestsLevelsVary,
     *      cec.support_phone_number as supportPhoneNumber,
     *      prod.off_grade_testing_disabled as offGradeTestingDisabled
     * from
     *      product prod, test_admin adm, customer_email_config cec
     * where
     *      prod.product_id = adm.product_id
     *      and cec.customer_id (+) = adm.customer_id 
     *      and adm.test_admin_id = {testAdminId}::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  prod.product_id as productId,  prod.product_name as productName,  prod.version as version,  prod.product_description as productDescription,  prod.created_by as createdBy,  prod.created_date_time as createdDateTime,  prod.updated_by as updatedBy,  prod.updated_date_time as updatedDateTime,  prod.activation_status as activationStatus,  prod.product_type as productType,  prod.scoring_item_set_level as scoringItemSetLevel,  prod.preview_item_set_level as previewItemSetLevel,  prod.parent_product_id as parentProductId,  prod.ext_product_id as extProductId,  prod.content_area_level as contentAreaLevel,  prod.internal_display_name as internalDisplayName,  prod.sec_scoring_item_set_level as secScoringItemSetLevel,  prod.ibs_show_cms_id as ibsShowCmsId,  prod.printable as printable,  prod.scannable as scannable,  prod.keyenterable as keyenterable,  prod.branding_type_code as brandingTypeCode,  prod.acknowledgments_url as acknowledgmentsURL,  prod.show_student_feedback as showStudentFeedback,  prod.static_manifest as staticManifest,  prod.session_manifest as sessionManifest,  prod.subtests_selectable as subtestsSelectable,  prod.subtests_orderable as subtestsOrderable,  prod.subtests_levels_vary as subtestsLevelsVary,  cec.support_phone_number as supportPhoneNumber,  prod.off_grade_testing_disabled as offGradeTestingDisabled from  product prod, test_admin adm, customer_email_config cec where  prod.product_id = adm.product_id  and cec.customer_id (+) = adm.customer_id  and adm.test_admin_id = {testAdminId}",
                     arrayMaxLength = 100000)
    TestProduct getProductForTestAdmin(Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct
     *      prod.product_id as productId,
     *      prod.product_name as productName,
     *      prod.version as version,
     *      prod.product_description as productDescription,
     *      prod.created_by as createdBy,
     *      prod.created_date_time as createdDateTime,
     *      prod.updated_by as updatedBy,
     *      prod.updated_date_time as updatedDateTime,
     *      prod.activation_status as activationStatus,
     *      prod.product_type as productType,
     *      prod.scoring_item_set_level as scoringItemSetLevel,
     *      prod.preview_item_set_level as previewItemSetLevel,
     *      prod.parent_product_id as parentProductId,
     *      prod.ext_product_id as extProductId,
     *      prod.content_area_level as contentAreaLevel,
     *      prod.internal_display_name as internalDisplayName,
     *      prod.sec_scoring_item_set_level as secScoringItemSetLevel,
     *      prod.ibs_show_cms_id as ibsShowCmsId,
     *      prod.printable as printable,
     *      prod.scannable as scannable,
     *      prod.keyenterable as keyenterable,
     *      prod.branding_type_code as brandingTypeCode,
     *      prod.acknowledgments_url as acknowledgmentsURL,
     *      prod.show_student_feedback as showStudentFeedback,
     *      prod.static_manifest as staticManifest,
     *      prod.session_manifest as sessionManifest,
     *      prod.subtests_selectable as subtestsSelectable,
     *      prod.subtests_orderable as subtestsOrderable,
     *      prod.subtests_levels_vary as subtestsLevelsVary,
     *      prod.off_grade_testing_disabled as offGradeTestingDisabled
     * from
     *      product prod
     * where
     *      prod.product_id  = {testProductId}::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  prod.product_id as productId,  prod.product_name as productName,  prod.version as version,  prod.product_description as productDescription,  prod.created_by as createdBy,  prod.created_date_time as createdDateTime,  prod.updated_by as updatedBy,  prod.updated_date_time as updatedDateTime,  prod.activation_status as activationStatus,  prod.product_type as productType,  prod.scoring_item_set_level as scoringItemSetLevel,  prod.preview_item_set_level as previewItemSetLevel,  prod.parent_product_id as parentProductId,  prod.ext_product_id as extProductId,  prod.content_area_level as contentAreaLevel,  prod.internal_display_name as internalDisplayName,  prod.sec_scoring_item_set_level as secScoringItemSetLevel,  prod.ibs_show_cms_id as ibsShowCmsId,  prod.printable as printable,  prod.scannable as scannable,  prod.keyenterable as keyenterable,  prod.branding_type_code as brandingTypeCode,  prod.acknowledgments_url as acknowledgmentsURL,  prod.show_student_feedback as showStudentFeedback,  prod.static_manifest as staticManifest,  prod.session_manifest as sessionManifest,  prod.subtests_selectable as subtestsSelectable,  prod.subtests_orderable as subtestsOrderable,  prod.subtests_levels_vary as subtestsLevelsVary,  prod.off_grade_testing_disabled as offGradeTestingDisabled from  product prod where  prod.product_id  = {testProductId}", arrayMaxLength = 100000)
    TestProduct getProduct(Integer testProductId) throws SQLException;
    
    
    /**
     * @jc:sql statement::
     * select distinct 
     * 		product.product_id as productId, 
     * 		product.product_description as productDescription, 
     * 		pr.resource_type_code as resourceTypeCode, 
     * 		pr.resource_uri as resourceURI 
     * from 
     * 		product, 
     * 		product_resource pr 
     * where 
     * 		product.product_id = pr.product_id 
     * 		and pr.resource_type_code = {resourceTypeCode} 
     * 		and product.product_id in ( 
     * 		select distinct 
     * 			parent_product_id 
     * 		from 
     * 			product 
     * 		where product_id in (
     * 			select distinct 
     * 				prod.product_id as productId 
     * 			from 
     * 				product prod, 
     * 				test_catalog cat, 
     * 				org_node_test_catalog ontc, 
     * 				user_role urole, 
     * 				users 
     * 			where 
     * 				prod.activation_status = 'AC' 
     * 				and prod.product_id = cat.product_id 
     * 				and cat.activation_status = 'AC' 
     * 				and cat.test_catalog_id = ontc.test_catalog_id 
     * 				and ontc.activation_status = 'AC' 
     * 				and urole.org_node_id = ontc.org_node_id 
     * 				and urole.activation_status = 'AC' 
     * 				and users.user_id = urole.user_id 
     * 				and users.user_name = {userName}))
     */
    @JdbcControl.SQL(statement = "select distinct product.product_id as productId, product.product_description as productDescription, pr.resource_type_code as resourceTypeCode, pr.resource_uri as resourceURI from product, product_resource pr where product.product_id = pr.product_id and pr.resource_type_code = {resourceTypeCode} and product.product_id in ( select distinct parent_product_id from product where product_id in (select distinct prod.product_id as productId from product prod, test_catalog cat, org_node_test_catalog ontc, user_role urole, users where prod.activation_status = 'AC' and prod.product_id = cat.product_id and cat.activation_status = 'AC' and cat.test_catalog_id = ontc.test_catalog_id and ontc.activation_status = 'AC' and urole.org_node_id = ontc.org_node_id and urole.activation_status = 'AC' and users.user_id = urole.user_id and users.user_name = {userName}))")
    UserParentProductResource[] getParentProductListForUser(String userName, String resourceTypeCode) throws SQLException;

    
    /**
     * @jc:sql statement::
     * select 
     * 		resource_uri
     * from
     * 		product_resource
     * where
     * 		product_id = {productId}
     * 		and resource_type_code = {resourceTypeCode}
     */
    @JdbcControl.SQL(statement = "select resource_uri from product_resource where product_id = {productId} and resource_type_code = {resourceTypeCode}")
    String getDemoInstallerUri(String resourceTypeCode, Integer productId) throws SQLException;
    
    @JdbcControl.SQL(statement = "select distinct cat.test_name as testCatalogName from product prod,  test_catalog cat,  org_node_test_catalog ontc,  user_role urole,  users where prod.activation_status = 'AC' and prod.product_id = cat.product_id  and cat.activation_status = 'AC' and cat.test_catalog_id = ontc.test_catalog_id  and ontc.activation_status = 'AC' and urole.org_node_id = ontc.org_node_id and urole.activation_status = 'AC' and users.user_id = urole.user_id  and users.user_name = {userName} order by cat.test_name",
            arrayMaxLength = 100000)
            String [] getTestCatalogForUserForScoring(String userName) throws SQLException;
    
    @JdbcControl.SQL(statement = "select distinct cat.test_name as testCatalogName from product prod,  test_catalog cat,  org_node_test_catalog ontc,  user_role urole,  users, product parentprod  where parentprod.product_id = prod.product_id and parentprod.parent_product_id = {productId} and prod.activation_status = 'AC' and prod.product_id = cat.product_id  and cat.activation_status = 'AC' and cat.test_catalog_id = ontc.test_catalog_id  and ontc.activation_status = 'AC' and urole.org_node_id = ontc.org_node_id and urole.activation_status = 'AC' and users.user_id = urole.user_id  and users.user_name = {userName} order by cat.test_name",
            arrayMaxLength = 100000)
            String [] getTestCatalogForUserForReporting(String userName, Integer productId) throws SQLException;
    
    static final long serialVersionUID = 1L;

    @JdbcControl.SQL(statement = "SELECT DISTINCT INITCAP( ISET.ITEM_SET_NAME) FROM USERS  USRS, USER_ROLE    UROLE, ORG_NODE  NODE, ORG_NODE_ANCESTOR   ONA, ORG_NODE_TEST_CATALOG ONTC, TEST_CATALOG   CATALOG, ITEM_SET_ANCESTOR     ISA, ITEM_SET              ISET WHERE ISET.ITEM_SET_TYPE = 'TD' AND NODE.ACTIVATION_STATUS ='AC' AND ISET.ACTIVATION_STATUS ='AC' AND CATALOG.ACTIVATION_STATUS ='AC' AND ONTC.ACTIVATION_STATUS ='AC' AND ISA.ITEM_SET_ID = ISET.ITEM_SET_ID AND CATALOG.ITEM_SET_ID = ISA.ANCESTOR_ITEM_SET_ID AND ONTC.TEST_CATALOG_ID = CATALOG.TEST_CATALOG_ID AND NODE.ORG_NODE_ID = ONTC.ORG_NODE_ID AND ONA.ORG_NODE_ID = ONTC.ORG_NODE_ID AND UROLE.ORG_NODE_ID = ONA.ANCESTOR_ORG_NODE_ID AND USRS.USER_ID = UROLE.USER_ID AND USRS.USER_NAME = {userName}",
            arrayMaxLength = 100)
	String[] getAllContentAreaOptionsForUser(String userName) throws SQLException;
    
    @JdbcControl.SQL(statement = "SELECT DISTINCT ISET.ITEM_SET_FORM FROM USERS USR, USER_ROLE UROLE, ORG_NODE_ANCESTOR ONA, ORG_NODE ORG, ORG_NODE_TEST_CATALOG ONTC, TEST_CATALOG TC, ITEM_SET_ANCESTOR ISA, ITEM_SET ISET WHERE USR.USER_NAME = {userName} AND USR.USER_ID = UROLE.USER_ID AND UROLE.ORG_NODE_ID = ONA.ANCESTOR_ORG_NODE_ID AND ONA.ORG_NODE_ID = ORG.ORG_NODE_ID AND ORG.ACTIVATION_STATUS = 'AC' AND ORG.ORG_NODE_ID = ONTC.ORG_NODE_ID AND ONTC.ACTIVATION_STATUS = 'AC' AND ONTC.TEST_CATALOG_ID = TC.TEST_CATALOG_ID AND TC.ACTIVATION_STATUS = 'AC' AND TC.ITEM_SET_ID = ISA.ANCESTOR_ITEM_SET_ID AND ISA.ITEM_SET_ID = ISET.ITEM_SET_ID AND ISET.ACTIVATION_STATUS = 'AC' AND ISET.ITEM_SET_TYPE = 'TD'",
            arrayMaxLength = 100)
	String[] getAllFormOptionsForUser(String userName) throws SQLException;
	
    @JdbcControl.SQL(statement = "SELECT DISTINCT CATALOG.ITEM_SET_ID FROM PRODUCT PROD, TEST_CATALOG CATALOG, USERS, USER_ROLE UROLE, ORG_NODE_ANCESTOR ONA, ORG_NODE_TEST_CATALOG ONTC ,   ITEM_SET ISET WHERE PROD.PRODUCT_ID = {productId} AND PROD.PRODUCT_ID = CATALOG.PRODUCT_ID AND CATALOG.TEST_LEVEL = {level} AND USERS.USER_NAME = {userName} AND UROLE.USER_ID = USERS.USER_ID AND ONA.ANCESTOR_ORG_NODE_ID = UROLE.ORG_NODE_ID AND ONTC.ORG_NODE_ID = ONA.ORG_NODE_ID AND ONTC.PRODUCT_ID = PROD.PRODUCT_ID AND CATALOG.ITEM_SET_ID = ISET.ITEM_SET_ID   AND ISET.ACTIVATION_STATUS = 'AC'   AND ONTC.ACTIVATION_STATUS = 'AC'   AND PROD.ACTIVATION_STATUS = 'AC'   AND CATALOG.ACTIVATION_STATUS = 'AC'   AND USERS.ACTIVATION_STATUS = 'AC'   AND UROLE.ACTIVATION_STATUS = 'AC'",
            arrayMaxLength = 1)
    Integer getItemSetIdTCByProductAndLevel(String userName, Integer productId, String level) throws SQLException;
}