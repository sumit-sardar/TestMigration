package com.ctb.control.db.testAdmin; 


import java.sql.SQLException;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.testAdmin.CustomerSDS;
import com.ctb.bean.testAdmin.ScorableItem;

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
public interface ADS extends JdbcControl
{ 
    static final long serialVersionUID = 1L;
    
    /**
     * @jc:sql statement::
     * select tcm_sds.common_name as name, tcm_sds_params.enc_token as token
     * from tcm_customer_sds, tcm_sds, tcm_sds_params
     * where tcm_customer_sds.customer_account_id = {customerId}
     * and tcm_customer_sds.site_server_id = tcm_sds.site_server_id
     * and tcm_sds.common_name = tcm_sds_params.common_name::
     */
    @JdbcControl.SQL(statement = "select tcm_sds.common_name as name, tcm_sds_params.enc_token as token from tcm_customer_sds, tcm_sds, tcm_sds_params where tcm_customer_sds.customer_account_id = {customerId} and tcm_customer_sds.site_server_id = tcm_sds.site_server_id and tcm_sds.common_name = tcm_sds_params.common_name")
    CustomerSDS [] getCustomerSDSList(Integer customerId) throws SQLException;
    
    /**
     * Changes for Las Links to get Decrypted Item XML for Item player
     * 
     * 
     * 
     */
  @JdbcControl.SQL(statement = "select ITEM_RENDITION_XML as itemXml, CREATED_DATE_TIME as createdDateTime from AA_ITEM_DECRYPTED where AA_ITEM_ID= {itemId}")
  	ScorableItem [] getDecryptedItemXml(String itemId) throws SQLException;
}