package com.ctb.control.db; 
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import org.apache.beehive.controls.api.bean.ControlExtension;

//import com.bea.control.*;
//import com.bea.control.JdbcControl;
import com.ctb.bean.testAdmin.Address; 
import com.ctb.bean.testAdmin.USState;

import java.sql.SQLException; 
import java.util.Date;


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
public interface Addresses extends JdbcControl
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
     * select 
     *     s.statepr as statePr,
     *  	 s.statepr_desc as statePrDesc
     * from statepr_code s 
     * order by
     *     s.statepr_desc ::
     */
    @JdbcControl.SQL(statement = "select  s.statepr as statePr,  \t s.statepr_desc as statePrDesc from statepr_code s  order by  s.statepr_desc ")
    USState[] getStates() throws SQLException;

    /**
     * @jc:sql statement::
     * insert into
     *           address (
     *       		address_id,
     *              street_line1,
     *       		street_line2,
     *       		street_line3,
     *       		city,
     *       		statepr,
     *       		country,
     *       		zipcode,
     *       		zipcode_ext,
     *              primary_phone,
     *              primary_phone_ext,
     *              created_by,
     *              created_date_time,
     *              fax,
     *              secondary_phone
     *            ) values (
     *       	 {addressData.addressId},
     *           {addressData.addressLine1},
     *           {addressData.addressLine2},
     *           {addressData.addressLine3},
     *           {addressData.city},
     *           {addressData.statePr},
     *           {addressData.country},
     *           {addressData.zipCode},
     *           {addressData.zipCodeExt},
     *           {addressData.primaryPhone},
     *           {addressData.primaryPhoneExt},
     *           {addressData.createdBy},
     *           {addressData.createdDateTime},
     *           {addressData.faxNumber},
     *           {addressData.secondaryPhone}
     *          )::
     */
    @JdbcControl.SQL(statement = "insert into  address (  \t\taddress_id,  street_line1,  \t\tstreet_line2,  \t\tstreet_line3,  \t\tcity,  \t\tstatepr,  \t\tcountry,  \t\tzipcode,  \t\tzipcode_ext,  primary_phone,  primary_phone_ext,  created_by,  created_date_time,  fax,  secondary_phone  ) values (  \t {addressData.addressId},  {addressData.addressLine1},  {addressData.addressLine2},  {addressData.addressLine3},  {addressData.city},  {addressData.statePr},  {addressData.country},  {addressData.zipCode},  {addressData.zipCodeExt},  {addressData.primaryPhone},  {addressData.primaryPhoneExt},  {addressData.createdBy},  {addressData.createdDateTime},  {addressData.faxNumber},  {addressData.secondaryPhone}  )")
    void createAddress(Address addressData) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     * SEQ_ADDRESS_ID.NEXTVAL 
     * from 
     * dual::
     */
    @JdbcControl.SQL(statement = "select  SEQ_ADDRESS_ID.NEXTVAL  from  dual")
    Integer getNextPK() throws SQLException;

    /**
     * @jc:sql statement::
     * update address
     * set street_line1 = {address.addressLine1},
     * 	street_line2 = {address.addressLine2},
     * 	street_line3 = {address.addressLine3},
     * 	city = {address.city},
     * 	statepr = {address.statePr},
     * 	zipcode = {address.zipCode},
     * 	zipcode_ext = {address.zipCodeExt},
     * 	primary_phone = {address.primaryPhone},
     * 	primary_phone_ext = {address.primaryPhoneExt},
     * 	secondary_phone = {address.secondaryPhone},
     * 	fax = {address.faxNumber},
     * 	updated_by = {address.updatedBy},
     * 	updated_date_time = {address.updatedDateTime}
     * where
     * 	address_id = {address.addressId} ::
     */
    @JdbcControl.SQL(statement = "update address set street_line1 = {address.addressLine1}, \tstreet_line2 = {address.addressLine2}, \tstreet_line3 = {address.addressLine3}, \tcity = {address.city}, \tstatepr = {address.statePr}, \tzipcode = {address.zipCode}, \tzipcode_ext = {address.zipCodeExt}, \tprimary_phone = {address.primaryPhone}, \tprimary_phone_ext = {address.primaryPhoneExt}, \tsecondary_phone = {address.secondaryPhone}, \tfax = {address.faxNumber}, \tupdated_by = {address.updatedBy}, \tupdated_date_time = {address.updatedDateTime} where \taddress_id = {address.addressId} ")
    void updateAddress(Address address) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     * ad.address_id as addressId,
     * ad.street_line1 as addressLine1,
     * ad.street_line2 as addressLine2,
     * ad.street_line3 as addressLine3,
     * ad.city as city,
     * st.statepr as statePr,
     * st.statepr_desc as stateDesc, 
     * cc.country_desc as country,
     * ad.zipcode as zipCode,
     * ad.primary_phone as primaryPhone,
     * ad.secondary_phone as secondaryPhone,
     * ad.fax as faxNumber,
     * ad.zipcode_ext as zipCodeExt,
     * ad.primary_phone_ext as primaryPhoneExt
     * from
     * address ad,statepr_code st,country_code cc
     * where
     * ad.statepr = st.statepr(+)   
     * and
     * ad.country = cc.country(+)    
     * and  
     * ad.address_id = {addressId}::
     */
    @JdbcControl.SQL(statement = "select  ad.address_id as addressId, ad.street_line1 as addressLine1, ad.street_line2 as addressLine2, ad.street_line3 as addressLine3, ad.city as city, st.statepr as statePr, st.statepr_desc as stateDesc,  cc.country_desc as country, ad.zipcode as zipCode, ad.primary_phone as primaryPhone, ad.secondary_phone as secondaryPhone, ad.fax as faxNumber, ad.zipcode_ext as zipCodeExt, ad.primary_phone_ext as primaryPhoneExt from address ad,statepr_code st,country_code cc where ad.statepr = st.statepr(+)  and ad.country = cc.country(+)  and  ad.address_id = {addressId}")
    Address getAddress(Integer addressId) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     *     s.statepr as statePr,
     *  	 s.statepr_desc as statePrDesc
     * from statepr_code s 
     * where s.statepr = {statePr}::
     */
    @JdbcControl.SQL(statement = "select  s.statepr as statePr,  \t s.statepr_desc as statePrDesc from statepr_code s  where s.statepr = {statePr}")
    USState getState(String statePr) throws SQLException;
}