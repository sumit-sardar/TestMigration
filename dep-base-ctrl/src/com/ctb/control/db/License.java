package com.ctb.control.db; 

import com.bea.control.*; 
import com.bea.control.JdbcControl;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.TestProduct; 
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
public interface License extends JdbcControl
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
     * SELECT COUNT(tr.test_roster_id)
    
     * FROM 
     * test_roster tr,
     * org_node_ancestor ona,
     * test_admin adm,
     * product prod
 
     * WHERE
     * tr.org_node_id = ona.org_node_id
     * AND ona.ancestor_org_node_id = {orgId}
     * AND (prod.product_id = {productId} OR prod.parent_product_id = {productId})
     * AND prod.license_enabled = 'T'
     * AND tr.test_admin_id = adm.test_admin_id
     * AND adm.product_id = prod.product_id
     * AND tr.test_completion_status NOT IN ('NT','SC')::
     */
    @JdbcControl.SQL(statement = "SELECT COUNT(tr.test_roster_id)  FROM  test_roster tr, org_node_ancestor ona, test_admin adm, product prod  WHERE tr.org_node_id = ona.org_node_id AND ona.ancestor_org_node_id = {orgId} AND (prod.product_id = {productId} OR prod.parent_product_id = {productId}) AND prod.license_enabled = 'T' AND tr.test_admin_id = adm.test_admin_id AND adm.product_id = prod.product_id AND tr.test_completion_status NOT IN ('NT','SC')")
    Integer getConsumedQuantityForOrgByRoster(Integer orgId, Integer productId) throws SQLException;

    /**
     * @jc:sql statement::
     * SELECT COUNT(tr.test_roster_id)
     * FROM 
     * test_roster tr,
     * org_node_ancestor ona,
     * test_admin adm,
     * product prod
     * WHERE
     * tr.org_node_id = ona.org_node_id
     * AND ona.ancestor_org_node_id = {orgId}
     * AND (prod.product_id = {productId} OR prod.parent_product_id = {productId})
     * AND prod.license_enabled = 'T'
     * AND tr.test_admin_id = adm.test_admin_id
     * AND adm.product_id = prod.product_id
     * AND tr.test_completion_status ='SC'::
     */
    @JdbcControl.SQL(statement = "SELECT COUNT(tr.test_roster_id) FROM  test_roster tr, org_node_ancestor ona, test_admin adm, product prod WHERE tr.org_node_id = ona.org_node_id AND ona.ancestor_org_node_id = {orgId} AND (prod.product_id = {productId} OR prod.parent_product_id = {productId}) AND prod.license_enabled = 'T' AND tr.test_admin_id = adm.test_admin_id AND adm.product_id = prod.product_id AND tr.test_completion_status ='SC'")
    Integer getReservedQuantityForOrgByRoster(Integer orgId, Integer productId) throws SQLException;

    /**
     * @jc:sql statement::
     *     SELECT COUNT(1) FROM( 
     *       SELECT DISTINCT tr.test_roster_id,isp.parent_item_set_id
     *       FROM 
     *       test_roster tr,
     *       org_node_ancestor ona,
     *       test_admin adm,
     *       product prod,
     *       student_item_set_status siss,
     *       item_set iset,
     *       item_set_parent isp
     *       WHERE
     *       tr.org_node_id = ona.org_node_id
     *       AND ona.ancestor_org_node_id = {orgId}
     *       AND (prod.product_id = {productId} OR prod.parent_product_id = {productId})
     *       AND prod.license_enabled = 'T'
     *       AND tr.test_admin_id = adm.test_admin_id
     *       AND adm.product_id = prod.product_id
     *       AND tr.test_completion_status NOT IN ('NT','SC')
     *       AND tr.test_roster_id = siss.test_roster_id
     *       AND siss.completion_status != 'SC'
     *       AND siss.item_set_id = iset.item_set_id
     *       AND iset.item_set_level != 'L'
     *       AND iset.SAMPLE = 'F'
     *       AND iset.item_set_id = isp.item_set_id)::
     */
    @JdbcControl.SQL(statement = "SELECT COUNT(1) FROM(  SELECT DISTINCT tr.test_roster_id,isp.parent_item_set_id  FROM  test_roster tr,  org_node_ancestor ona,  test_admin adm,  product prod,  student_item_set_status siss,  item_set iset,  item_set_parent isp  WHERE  tr.org_node_id = ona.org_node_id  AND ona.ancestor_org_node_id = {orgId}  AND (prod.product_id = {productId} OR prod.parent_product_id = {productId})  AND prod.license_enabled = 'T'  AND tr.test_admin_id = adm.test_admin_id  AND adm.product_id = prod.product_id  AND tr.test_completion_status NOT IN ('NT','SC')  AND tr.test_roster_id = siss.test_roster_id  AND siss.completion_status != 'SC'  AND siss.item_set_id = iset.item_set_id  AND iset.item_set_level != 'L'  AND iset.SAMPLE = 'F'  AND iset.item_set_id = isp.item_set_id)")
    Integer getConsumedQuantityforOrgBySubTest(Integer orgId, Integer productId) throws SQLException;

    /**
     * @jc:sql statement::
     *     SELECT count(1) FROM 
     *    (SELECT DISTINCT isp.parent_item_set_id, tr.test_roster_id
     *     
     *       FROM 
     *       test_roster tr,
     *       org_node_ancestor ona,
     *       test_admin adm,
     *       product prod,
     *       student_item_set_status siss,
     *       item_set iset,
     *       item_set_parent isp
     *     
     *       WHERE
     *       tr.org_node_id = ona.org_node_id
     *       AND ona.ancestor_org_node_id = {orgId}
     *       AND (prod.product_id = {productId} OR prod.parent_product_id = {productId})
     *       AND prod.license_enabled = 'T'
     *       AND tr.test_admin_id = adm.test_admin_id
     *       AND adm.product_id = prod.product_id
     *       AND tr.test_completion_status NOT IN ('NT','IC','CO')
     *       AND tr.test_roster_id = siss.test_roster_id
     *       AND siss.completion_status = 'SC'
     *       AND siss.item_set_id = iset.item_set_id
     *       AND iset.item_set_level != 'L'
     *       AND iset.SAMPLE = 'F'
     *       AND iset.item_set_id = isp.item_set_id)::
     */
    @JdbcControl.SQL(statement = "SELECT count(1) FROM  (SELECT DISTINCT isp.parent_item_set_id, tr.test_roster_id  FROM  test_roster tr,  org_node_ancestor ona,  test_admin adm,  product prod,  student_item_set_status siss,  item_set iset,  item_set_parent isp  WHERE  tr.org_node_id = ona.org_node_id  AND ona.ancestor_org_node_id = {orgId}  AND (prod.product_id = {productId} OR prod.parent_product_id = {productId})  AND prod.license_enabled = 'T'  AND tr.test_admin_id = adm.test_admin_id  AND adm.product_id = prod.product_id  AND tr.test_completion_status NOT IN ('NT','IC','CO')  AND tr.test_roster_id = siss.test_roster_id  AND siss.completion_status = 'SC'  AND siss.item_set_id = iset.item_set_id  AND iset.item_set_level != 'L'  AND iset.SAMPLE = 'F'  AND iset.item_set_id = isp.item_set_id)")
    Integer getReservedQuantityForOrgBySubTest(Integer orgId, Integer productId) throws SQLException;
     
    /**
     * @jc:sql statement::
     * select cplicense.CUSTOMER_ID           as customerId,
     *         cplicense.PRODUCT_ID           as productId,
     *         cplicense.AVAILABLE            as available,
     *         cplicense.RESERVED             as reservedLicense,
     *         cplicense.CONSUMED             as consumedLicense,
     *         cplicense.SUBTEST_MODEL        as subtestModel, 
     *         cplicense.LICENSE_PERIOD_START as licenseperiodStartdate,
     *         cplicense.LICENSE_PERIOD_END as licenseperiodEnd,
     *         cplicense.LICENSE_AFTER_LAST_PURCHASE  AS licenseAfterLastPurchase,
     *         prod.product_description as productName
     *   from customer_product_license cplicense , product prod
     *   where  prod.product_id = cplicense.PRODUCT_ID and 
     *   cplicense.CUSTOMER_ID = {customerId}::
     */
    @JdbcControl.SQL(statement = "select cplicense.CUSTOMER_ID  as customerId,  cplicense.PRODUCT_ID  as productId,  cplicense.AVAILABLE  as available,  cplicense.RESERVED  as reservedLicense,  cplicense.CONSUMED  as consumedLicense,  cplicense.SUBTEST_MODEL  as subtestModel,  cplicense.LICENSE_PERIOD_START as licenseperiodStartdate,  cplicense.LICENSE_PERIOD_END as licenseperiodEnd,  cplicense.LICENSE_AFTER_LAST_PURCHASE  AS licenseAfterLastPurchase,  prod.product_description as productName  from customer_product_license cplicense , product prod  where  prod.product_id = cplicense.PRODUCT_ID and  cplicense.CUSTOMER_ID = {customerId}")
    CustomerLicense[] getCustomerLicenseDetails(Integer customerId) throws SQLException;

    /**
     * @jc:sql statement::
     * SELECT cplicense.CUSTOMER_ID          AS customerId,
     *        cplicense.PRODUCT_ID           AS productId,
     *        cplicense.AVAILABLE            AS available,
     *        cplicense.RESERVED             AS reservedLicense,
     *        cplicense.CONSUMED             AS consumedLicense,
     *        cplicense.SUBTEST_MODEL        AS subtestModel,
     *        cplicense.LICENSE_PERIOD_START AS licenseperiodStartdate,
     *        cplicense.LICENSE_PERIOD_END   AS licenseperiodEnd,
     *        cplicense.LICENSE_AFTER_LAST_PURCHASE  AS licenseAfterLastPurchase,
     *        prod.product_description       AS productName
     *   FROM customer_product_license cplicense,
     *        product                  prod
     *  WHERE prod.product_id = cplicense.PRODUCT_ID
     *    AND cplicense.CUSTOMER_ID = {customerId}
     *    AND cplicense.PRODUCT_ID = {productId}
     *    AND prod.LICENSE_ENABLED='T'
     * 
     * UNION
     * 
     * SELECT cplicense.CUSTOMER_ID          AS customerId,
     *        cplicense.PRODUCT_ID           AS productId,
     *        cplicense.AVAILABLE            AS available,
     *        cplicense.RESERVED             AS reservedLicense,
     *        cplicense.CONSUMED             AS consumedLicense,
     *        cplicense.SUBTEST_MODEL        AS subtestModel,
     *        cplicense.LICENSE_PERIOD_START AS licenseperiodStartdate,
     *        cplicense.LICENSE_PERIOD_END   AS licenseperiodEnd,
     *        cplicense.LICENSE_AFTER_LAST_PURCHASE  AS licenseAfterLastPurchase,
     *        prod.product_description       AS productName
     *   FROM customer_product_license cplicense,
     *        product                  prod
     *  WHERE prod.product_id = cplicense.PRODUCT_ID
     *    AND cplicense.PRODUCT_ID =
     *        (SELECT pr.parent_product_id
     *           FROM product pr
     *          WHERE pr.product_id = {productId}
	 *			AND pr.LICENSE_ENABLED='T')
     *    AND cplicense.CUSTOMER_ID = {customerId}
     *    AND prod.LICENSE_ENABLED='T'
     *    AND NOT EXISTS (SELECT cplicense.CUSTOMER_ID          AS customerId,
     *                           cplicense.PRODUCT_ID           AS productId
     *                    FROM customer_product_license cplicense,
     *                          product                  prod
     *                    WHERE prod.product_id = cplicense.PRODUCT_ID
     *                          AND cplicense.CUSTOMER_ID = {customerId}
     *                          AND cplicense.PRODUCT_ID = {productId})::
     */
    @JdbcControl.SQL(statement = "SELECT cplicense.CUSTOMER_ID  AS customerId,  cplicense.PRODUCT_ID  AS productId,  cplicense.AVAILABLE  AS available,  cplicense.RESERVED  AS reservedLicense,  cplicense.CONSUMED  AS consumedLicense,  cplicense.SUBTEST_MODEL  AS subtestModel,  cplicense.LICENSE_PERIOD_START AS licenseperiodStartdate,  cplicense.LICENSE_PERIOD_END  AS licenseperiodEnd,  cplicense.LICENSE_AFTER_LAST_PURCHASE  AS licenseAfterLastPurchase,  prod.product_description  AS productName  FROM customer_product_license cplicense,  product  prod  WHERE prod.product_id = cplicense.PRODUCT_ID  AND cplicense.CUSTOMER_ID = {customerId}  AND cplicense.PRODUCT_ID = {productId}  AND prod.LICENSE_ENABLED='T'  UNION  SELECT cplicense.CUSTOMER_ID  AS customerId,  cplicense.PRODUCT_ID  AS productId,  cplicense.AVAILABLE  AS available,  cplicense.RESERVED  AS reservedLicense,  cplicense.CONSUMED  AS consumedLicense,  cplicense.SUBTEST_MODEL  AS subtestModel,  cplicense.LICENSE_PERIOD_START AS licenseperiodStartdate,  cplicense.LICENSE_PERIOD_END  AS licenseperiodEnd,  cplicense.LICENSE_AFTER_LAST_PURCHASE  AS licenseAfterLastPurchase,  prod.product_description  AS productName  FROM customer_product_license cplicense,  product  prod  WHERE prod.product_id = cplicense.PRODUCT_ID  AND cplicense.PRODUCT_ID =  (SELECT pr.parent_product_id  FROM product pr  WHERE pr.product_id = {productId} \t\t\tAND pr.LICENSE_ENABLED='T')  AND cplicense.CUSTOMER_ID = {customerId}  AND prod.LICENSE_ENABLED='T'  AND NOT EXISTS (SELECT cplicense.CUSTOMER_ID  AS customerId,  cplicense.PRODUCT_ID  AS productId  FROM customer_product_license cplicense,  product  prod  WHERE prod.product_id = cplicense.PRODUCT_ID  AND cplicense.CUSTOMER_ID = {customerId}  AND cplicense.PRODUCT_ID = {productId})")
    CustomerLicense[] getProductLicenseDetails(Integer customerId, Integer productId) throws SQLException;


    /**
     * @jc:sql statement::
     *  select    
     *  decode( count(cpl.customer_id),0,0,1) 
     *  from customer_product_license cpl
     *  where cpl.customer_id = {customerId}
     *  and cpl.product_id = {productId}::
     */
    @JdbcControl.SQL(statement = "select  decode( count(cpl.customer_id),0,0,1)  from customer_product_license cpl where cpl.customer_id = {customerId} and cpl.product_id = {productId}")
    boolean isCustomerLicenseExist(Integer customerId, Integer productId) throws SQLException;

    /**
     * @jc:sql statement::
     * insert into customer_product_license
     *    (customer_id,
     *    product_id,
     *    available,
     *    reserved,
     *    consumed,
     *    email_notify_flag,
     *    license_after_last_purchase
     *    ) values (
     *    {customerLicense.customerId},
     *    {customerLicense.productId},
     *    {customerLicense.available},
     *    {customerLicense.reservedLicense},
     *    {customerLicense.consumedLicense},
     *    'T',
     *    {customerLicense.available}
     *    )::
     */
    @JdbcControl.SQL(statement = "insert into customer_product_license  (customer_id,  product_id,  available,  reserved,  consumed,  email_notify_flag,  license_after_last_purchase  ) values (  {customerLicense.customerId},  {customerLicense.productId},  {customerLicense.available},  {customerLicense.reservedLicense},  {customerLicense.consumedLicense},  'T',  {customerLicense.available}  )")
    void addCustomerLicense(CustomerLicense customerLicense) throws SQLException;

    /**
     * @jc:sql statement::
     * update customer_product_license
     * set available = {customerLicense.available},
     * consumed = {customerLicense.consumedLicense},
     * email_notify_flag = 'T',
     * license_after_last_purchase = {customerLicense.available}
     * where customer_id = {customerLicense.customerId} and 
     * product_id  = {customerLicense.productId}::
     */
    @JdbcControl.SQL(statement = "update customer_product_license set available = {customerLicense.available}, consumed = {customerLicense.consumedLicense}, email_notify_flag = 'T', license_after_last_purchase = {customerLicense.available} where customer_id = {customerLicense.customerId} and  product_id  = {customerLicense.productId}")
    void updateCustomerLicensewithAvailableChange(CustomerLicense customerLicense) throws SQLException;

    /**
     * @jc:sql statement::
     * update customer_product_license
     * set consumed          = {customerLicense.consumedLicense}
     * where customer_id = {customerLicense.customerId} and 
     * product_id  = {customerLicense.productId}::
     */
    @JdbcControl.SQL(statement = "update customer_product_license set consumed  = {customerLicense.consumedLicense} where customer_id = {customerLicense.customerId} and  product_id  = {customerLicense.productId}")
    void updateCustomerLicensewithoutAvailableChange(CustomerLicense customerLicense) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct
     * parentProduct.product_id as productId,
     * parentProduct.Product_Description as productName,
     * parentProduct.license_enabled as productLicenseEnabled
     * from
     * product prod,
     * test_catalog cat,
     * org_node_test_catalog ontc,
     * product parentProduct
     * where
     * prod.activation_status = 'AC'
     * and prod.product_id = cat.product_id
     * and parentProduct.product_id = prod.parent_product_id
     * and cat.activation_status = 'AC'
     * and cat.test_catalog_id = ontc.test_catalog_id
     * and ontc.activation_status = 'AC'
     * and ontc.customer_id={customerId} 
     * ::
     */
    @JdbcControl.SQL(statement = "select distinct parentProduct.product_id as productId, parentProduct.Product_Description as productName, parentProduct.license_enabled as productLicenseEnabled from product prod, test_catalog cat, org_node_test_catalog ontc, product parentProduct where prod.activation_status = 'AC' and prod.product_id = cat.product_id and parentProduct.product_id = prod.parent_product_id and cat.activation_status = 'AC' and cat.test_catalog_id = ontc.test_catalog_id and ontc.activation_status = 'AC' and ontc.customer_id={customerId} ")
    TestProduct[] getParentProductId (Integer customerId) throws SQLException;
}