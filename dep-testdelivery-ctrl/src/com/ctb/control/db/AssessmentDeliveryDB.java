package com.ctb.control.db; 

import org.apache.beehive.controls.system.jdbc.JdbcControl;
import com.ctb.bean.testDelivery.assessmentDeliveryData.ItemData;
import com.ctb.bean.testDelivery.assessmentDeliveryData.SubtestData; 
import com.ctb.bean.testDelivery.assessmentDeliveryData.ItemIdEidMap;
import java.sql.Blob; 
import java.sql.SQLException; 
import org.apache.beehive.controls.api.bean.ControlExtension;

/**
 * @author John_Wang
 */

/** 
 * Defines a new database control. 
 * 
 * The @jc:connection tag indicates which WebLogic data source will be used by 
 * this database control. Please change this to suit your needs. You can see a 
 * list of available data sources by going to the WebLogic console in a browser 
 * (typically http://localhost:7001/console) and clicking Services, JDBC, 
 * Data Sources. 
 * 
 * @jc:connection data-source-jndi-name="adsDataSource" 
 */ 
@ControlExtension()
@JdbcControl.ConnectionDataSource(jndiName = "adsDataSource")
public interface AssessmentDeliveryDB extends JdbcControl
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
     * SELECT ob_asmt_id AS subtestId, 
     * 	asmt_hash as hash
     * FROM OB_ASMT 
     * WHERE ob_asmt_id = {subtestId}::
     */
    @JdbcControl.SQL(statement = "SELECT ob_asmt_id AS subtestId,  \tasmt_hash as hash FROM OB_ASMT  WHERE ob_asmt_id = {subtestId}")
    SubtestData getSubtest(int subtestId);

    /**
     * @jc:sql statement::
     * SELECT 
     * asmt_manifest_encr AS subtestBlob
     * FROM OB_ASMT 
     * WHERE ob_asmt_id = {subtestId}::
     */
    @JdbcControl.SQL(statement = "SELECT  asmt_manifest_encr AS subtestBlob FROM OB_ASMT  WHERE ob_asmt_id = {subtestId}")
    Blob getSubtestBlob(int subtestId);

    /**
     * @jc:sql statement::
     * SELECT item_rendition_xml_encr as itemBlob 
     * FROM OB_ITEM_PKG 
     * WHERE ob_item_pkg_id = {itemId}::
     */
    @JdbcControl.SQL(statement = "SELECT item_rendition_xml_encr as itemBlob  FROM OB_ITEM_PKG  WHERE ob_item_pkg_id = {itemId}")
    Blob getItemBlob(int itemId);
    
    /**
     * @jc:sql statement::
     * SELECT ob_item_pkg_id AS itemId,
     * hash
     * FROM OB_ITEM_PKG 
     * WHERE ob_item_pkg_id = {itemId}::
     */
    @JdbcControl.SQL(statement = "SELECT ob_item_pkg_id AS itemId, hash FROM OB_ITEM_PKG  WHERE ob_item_pkg_id = {itemId}")
    ItemData getItem(int itemId);

    /**
     * @jc:sql statement::
     * select oip.aa_item_id as itemId, 
     * oip.ob_item_pkg_id as eid
     * from ob_asmt_item_map aim, 
     * ob_item_pkg oip
     * where aim.ob_item_pkg_id = oip.ob_item_pkg_id
     * and aim.ob_asmt_id = {assessmentId}::
     */
    @JdbcControl.SQL(statement = "select oip.aa_item_id as itemId,  oip.ob_item_pkg_id as eid from ob_asmt_item_map aim,  ob_item_pkg oip where aim.ob_item_pkg_id = oip.ob_item_pkg_id and aim.ob_asmt_id = {assessmentId}")
    ItemIdEidMap [] getItemIdEidMap(int assessmentId);
}