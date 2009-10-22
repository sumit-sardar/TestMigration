package com.ctb.control.db; 

import com.bea.control.*;

import com.ctb.bean.testAdmin.CustomerResourceData;
import java.sql.SQLException; 
import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

/** 
 * Defines a new database control. 
 * 
 * 
 * @jc:connection data-source-jndi-name="oasDataSource" 
 */ 
@ControlExtension()
@JdbcControl.ConnectionDataSource(jndiName = "oasDataSource")
public interface CustomerResource extends JdbcControl
{ 
    static final long serialVersionUID = 1L;
    
    /**
     * @jc:sql statement::
     * select
     *     cr.CUSTOMER_ID as customerId,
     *     cr.RESOURCE_TYPE_CODE as resourceTypeCode,
     *     cr.RESOURCE_URI as resourceURI
     * from 
     *      customer_resource cr
     * where 
     *      cr.customer_id = {customerId}::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  cr.CUSTOMER_ID as customerId,  cr.RESOURCE_TYPE_CODE as resourceTypeCode,  cr.RESOURCE_URI as resourceURI from  customer_resource cr where  cr.customer_id = {customerId}",
                     arrayMaxLength = 100000)
    CustomerResourceData [] getCustomerResource(Integer customerId) throws SQLException;
}