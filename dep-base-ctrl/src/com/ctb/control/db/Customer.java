package com.ctb.control.db; 

import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerEmail;
import com.ctb.bean.testAdmin.FindCustomer; 
import java.sql.SQLException; 
import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

/** 
 * Defines a new database control. 
 * 
 * The @jc:connection tag indicates which WebLogic data source will be used by 
 * this database control. Please change this to suit your needs. You can see a 
 * list of available data sources by going to the WebLogic console in a browser 
 * (typically http://localhost:7001/console) and clicking Services, JDBC, 
 * Data Sources. 
 * 
 * @author John_Wang
 * 
 * @jc:connection data-source-jndi-name="oasDataSource" 
 */ 
@ControlExtension()
@JdbcControl.ConnectionDataSource(jndiName = "oasDataSource")
public interface Customer extends JdbcControl
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
     * select decode(count(org.customer_id), 0, 'false', 'true') 
     * from user_role ur, users u, org_node org
     * where u.user_id = ur.user_id
     * and u.user_name = {userName}
     * and ur.org_node_id = org.org_node_id
     * and org.customer_id = {customerId}
     * 
     * ::
     */
    @JdbcControl.SQL(statement = "select decode(count(org.customer_id), 0, 'false', 'true')  from user_role ur, users u, org_node org where u.user_id = ur.user_id and u.user_name = {userName} and ur.org_node_id = org.org_node_id and org.customer_id = {customerId} ")
    String checkVisibility(String userName, Integer customerId) throws SQLException;
    
    

    /**
     * @jc:sql statement::
     * insert into customer
     *   (customer_id,
     *    customer_name,
     *    contact_name,
     *    contact_phone,
     *    contact_email,
     *    statepr,
     *    created_date_time,
     *    ext_customer_id,
     *    billing_address_id,
     *    created_by,
     *    updated_by,
     *    updated_date_time,
     *    ctb_contact_name,
     *    activation_status,
     *    mailing_address_id,
     *    ctb_contact_email,
     *    allow_data_upload,
     *    override_hide_accommodations,
     *    import_student_editable,
     *    demographic_visible
     *    
     * ) values (
     *    {customer.customerId},
     *    {customer.customerName},
     *    INITCAP({customer.contactName}),
     *    {customer.contactPhone},
     *    {customer.contactEmail},
     *    {customer.statePr},
     *    {customer.createdDateTime},
     *    {customer.extCustomerId},
     *    {customer.billingAddressId},
     *    {customer.createdBy},
     *    {customer.updatedBy},
     *    {customer.updatedDateTime},
     *    INITCAP({customer.ctbContactName}),
     *    {customer.activationStatus},
     *    {customer.mailingAddressId},
     *    {customer.ctbContactEmail},
     *    {customer.allowDataUpload},
     *    {customer.hideAccommodations},
     *    {customer.importStudentEditable},
     *    {customer.demographicVisible}
     *    )::
     */
    @JdbcControl.SQL(statement = "insert into customer  (customer_id,  customer_name,  contact_name,  contact_phone,  contact_email,  statepr,  created_date_time,  ext_customer_id,  billing_address_id,  created_by,  updated_by,  updated_date_time,  ctb_contact_name,  activation_status,  mailing_address_id,  ctb_contact_email,  allow_data_upload,  override_hide_accommodations,  import_student_editable,  demographic_visible  ) values (  {customer.customerId},  {customer.customerName},  INITCAP({customer.contactName}),  {customer.contactPhone},  {customer.contactEmail},  {customer.statePr},  {customer.createdDateTime},  {customer.extCustomerId},  {customer.billingAddressId},  {customer.createdBy},  {customer.updatedBy},  {customer.updatedDateTime},  INITCAP({customer.ctbContactName}),  {customer.activationStatus},  {customer.mailingAddressId},  {customer.ctbContactEmail},  {customer.allowDataUpload},  {customer.hideAccommodations},  {customer.importStudentEditable},  {customer.demographicVisible}  )")
    void createCustomer(com.ctb.bean.testAdmin.Customer customer) throws SQLException;

    /**
     * @jc:sql statement::
     *  select seq_customer_id.nextval
     *  from dual::
     */
    @JdbcControl.SQL(statement = "select seq_customer_id.nextval from dual")
    Integer getNextPk() throws SQLException;

    /**
     * @jc:sql statement="call setup_tabe_customer({CustomerId})"
     */
    @JdbcControl.SQL(statement = "call setup_tabe_customer({CustomerId})")
    void createTabeCustomerConfiguration(Integer CustomerId) throws SQLException;

    /**
     * @jc:sql statement::
     * select seq_customer_configuration_id.nextval
     * from dual::
     */
    @JdbcControl.SQL(statement = "select seq_customer_configuration_id.nextval from dual")
    Integer getCustConfigNextPk() throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct 
     *                 c.customer_id as customerId,
     *                 c.customer_name as customerName,
     *                 c.contact_name as contactName,
     *                 c.contact_phone as contactPhone,
     *                 c.contact_email as contactEmail,
     *                 c.country as country,
     *                 c.statepr as statePr,
     *                 s.statepr_desc as stateDesc,
     *                 c.created_date_time as createdDateTime,
     *                 c.ext_customer_id as extCustomerId,
     *                 c.billing_address_id as billingAddressId,
     *                 c.created_by as createdBy,
     *                 c.updated_by as updatedBy,
     *                 c.updated_date_time as updatedDateTime,
     *                 c.ctb_contact_name as ctbContactName,
     *                 c.activation_status as activationStatus,
     *                 c.mailing_address_id as mailingAddressId,
     *                 c.ctb_contact_email as ctbContactEmail,
     *                 c.override_hide_accommodations as hideAccommodations,
     *                 c.import_student_editable as importStudentEditable,
     *                 c.demographic_visible as demographicVisible,
     *                 c.allow_data_upload as allowDataUpload,
     *                 decode((select cc.default_value
     *                                from customer_configuration cc
     *                               where cc.customer_id = c.customer_id
     *                                 and cc.customer_configuration_name =
     *                                     'Allow_Subscription'),
     *                              'T',
     *                              'T',
     *                              'F')as allowSubscription
     * 
     *  from customer c, statepr_code s
     *  where c.statePr = s.statePr
     *  and c.activation_status = 'AC'
     *  and  c.customer_id >  {ctbCustomerId} ::
     *   array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  c.customer_id as customerId,  c.customer_name as customerName,  c.contact_name as contactName,  c.contact_phone as contactPhone,  c.contact_email as contactEmail,  c.country as country,  c.statepr as statePr,  s.statepr_desc as stateDesc,  c.created_date_time as createdDateTime,  c.ext_customer_id as extCustomerId,  c.billing_address_id as billingAddressId,  c.created_by as createdBy,  c.updated_by as updatedBy,  c.updated_date_time as updatedDateTime,  c.ctb_contact_name as ctbContactName,  c.activation_status as activationStatus,  c.mailing_address_id as mailingAddressId,  c.ctb_contact_email as ctbContactEmail,  c.override_hide_accommodations as hideAccommodations,  c.import_student_editable as importStudentEditable,  c.demographic_visible as demographicVisible,  c.allow_data_upload as allowDataUpload,  decode((select cc.default_value  from customer_configuration cc  where cc.customer_id = c.customer_id  and cc.customer_configuration_name =  'Allow_Subscription'),  'T',  'T',  'F')as allowSubscription  from customer c, statepr_code s  where c.statePr = s.statePr  and c.activation_status = 'AC'  and  c.customer_id >  {ctbCustomerId} ",
                     arrayMaxLength = 100000)
    FindCustomer[] getCustomers(Integer ctbCustomerId) throws SQLException;

    /**
     * @jc:sql statement::
     *  select c.customer_id                  as customerId,
     *         c.customer_name                as customerName,
     *         c.contact_name                 as contactName,
     *         c.contact_phone                as contactPhone,
     *         c.contact_email                as contactEmail,
     *         c.country                      as country,
     *         c.statepr                      as statePr,
     *         c.created_date_time            as createdDateTime,
     *         c.ext_customer_id              as extCustomerId,
     *         c.billing_address_id           as billingAddressId,
     *         c.created_by                   as createdBy,
     *         c.updated_by                   as updatedBy,
     *         c.updated_date_time            as updatedDateTime,
     *         c.ctb_contact_name             as ctbContactName,
     *         c.activation_status            as activationStatus,
     *         c.mailing_address_id           as mailingAddressId,
     *         c.ctb_contact_email            as ctbContactEmail,
     *         c.override_hide_accommodations as hideAccommodations,
     *         c.import_student_editable      as importStudentEditable,
     *         c.demographic_visible          as demographicVisible,
     *         c.allow_data_upload            as allowDataUpload
     *  
     *    from customer c, statepr_code s
     *   where c.statePr = s.statePr
     *     and c.activation_status = 'AC'
     *     and c.customer_id = {customerId}::
     */
    @JdbcControl.SQL(statement = "select c.customer_id  as customerId,  c.customer_name  as customerName,  c.contact_name  as contactName,  c.contact_phone  as contactPhone,  c.contact_email  as contactEmail,  c.country  as country,  c.statepr  as statePr,  c.created_date_time  as createdDateTime,  c.ext_customer_id  as extCustomerId,  c.billing_address_id  as billingAddressId,  c.created_by  as createdBy,  c.updated_by  as updatedBy,  c.updated_date_time  as updatedDateTime,  c.ctb_contact_name  as ctbContactName,  c.activation_status  as activationStatus,  c.mailing_address_id  as mailingAddressId,  c.ctb_contact_email  as ctbContactEmail,  c.override_hide_accommodations as hideAccommodations,  c.import_student_editable  as importStudentEditable,  c.demographic_visible  as demographicVisible,  c.allow_data_upload  as allowDataUpload  from customer c, statepr_code s  where c.statePr = s.statePr  and c.activation_status = 'AC'  and c.customer_id = {customerId}")
    com.ctb.bean.testAdmin.Customer getCustomerDetails(Integer customerId) throws SQLException;

    /**
     * @jc:sql statement="call setup_terranova_customer({CustomerId})"
     */
    @JdbcControl.SQL(statement = "call setup_terranova_customer({CustomerId})")
    void createTerranovaCustomerConfiguration(Integer CustomerId) throws SQLException;

    /**
     * @jc:sql statement::
     * update customer
     *    set customer_name     = {customer.customerName},
     *        ext_customer_id   = {customer.extCustomerId},
     *        statepr           = {customer.statePr},
     *        contact_name      = INITCAP({customer.contactName}),
     *        contact_email     = {customer.contactEmail},
     *        contact_phone     = {customer.contactPhone},
     *        ctb_contact_name  = INITCAP({customer.ctbContactName}),
     *        ctb_contact_email = {customer.ctbContactEmail},
     * 	   billing_address_id = {customer.billingAddressId},
     * 	   mailing_address_id = {customer.mailingAddressId},		
     *        activation_status = {customer.activationStatus},
     *        updated_by        = {customer.updatedBy},
     *        updated_date_time = {customer.updatedDateTime}
     *  where customer_id = {customer.customerId}::
     */
    @JdbcControl.SQL(statement = "update customer  set customer_name  = {customer.customerName},  ext_customer_id  = {customer.extCustomerId},  statepr  = {customer.statePr},  contact_name  = INITCAP({customer.contactName}),  contact_email  = {customer.contactEmail},  contact_phone  = {customer.contactPhone},  ctb_contact_name  = INITCAP({customer.ctbContactName}),  ctb_contact_email = {customer.ctbContactEmail}, \t  billing_address_id = {customer.billingAddressId}, \t  mailing_address_id = {customer.mailingAddressId},\t\t  activation_status = {customer.activationStatus},  updated_by  = {customer.updatedBy},  updated_date_time = {customer.updatedDateTime}  where customer_id = {customer.customerId}")
    void updateCustomer(com.ctb.bean.testAdmin.Customer customer) throws SQLException;
    
    /**
     * @jc:sql statement::
     * insert into customer_email_config c
     * (c.customer_id, 
     *  c.email_type ,
     *  c.subject ,
     *  c.email_body ,
     *  c.reply_to ,
     *  c.created_date_time,
     *  c.created_by
     *  )
     *  
     * values (
     *          {customerEmail.customerId},
     *          {customerEmail.emailType}, 
     *          {customerEmail.subject},
     *          {customerEmail.emailBodyStr},
     *          {customerEmail.replyTo},
     *          {customerEmail.createdDateTime},                
     *          {customerEmail.createdBy}
     *          
     *         ) ::               
     */
    @JdbcControl.SQL(statement = "insert into customer_email_config c (c.customer_id,  c.email_type ,  c.subject ,  c.email_body ,  c.reply_to ,  c.created_date_time,  c.created_by  )  values (  {customerEmail.customerId},  {customerEmail.emailType},  {customerEmail.subject},  {customerEmail.emailBodyStr},  {customerEmail.replyTo},  {customerEmail.createdDateTime},  {customerEmail.createdBy}  ) ")
    void saveCustomerEmail(CustomerEmail customerEmail) throws SQLException;


    /**
     * @jc:sql statement::
     * select    cc.customer_configuration_id   as customerConfigurationId,
     *           cc.customer_configuration_name as customerConfigurationName,
     *           cc.editable                    as editable,
     *           cc.default_value               as defaultValue,
     *           cc.created_by                  as createdBy,
     *           cc.created_date_time           as createdDateTime
     *      from customer_configuration cc
     *     where cc.customer_id = {customerId}
     *     and cc.customer_configuration_name in ('TABE_Customer','TERRANOVA_Customer')::
     */
     //START - Changes for LASLINK Product
    @JdbcControl.SQL(statement = "select  cc.customer_configuration_id  as customerConfigurationId,  cc.customer_configuration_name as customerConfigurationName,  cc.editable  as editable,  cc.default_value  as defaultValue,  cc.created_by  as createdBy,  cc.created_date_time  as createdDateTime  from customer_configuration cc  where cc.customer_id = {customerId}  and cc.customer_configuration_name in ('TABE_Customer','TERRANOVA_Customer','LASLINK_Customer')")
    com.ctb.bean.testAdmin.CustomerConfig[] getCustomerConfigurations(Integer customerId) throws SQLException;
	//END - Changes for LASLINK Product
    /**
     * @jc:sql statement::
     *  select    decon.email_type as emailType,
     *            decon.subject as subject,
     *            decon.email_body as emailBody,
     *            decon.reply_to as replyTo
     *  from      default_email_config decon
     */
    @JdbcControl.SQL(statement = "select  decon.email_type as emailType,  decon.subject as subject,  decon.email_body as emailBody,  decon.reply_to as replyTo from  default_email_config decon")
    CustomerEmail[] getDefaultEmails() throws SQLException;
	
	
    //START - Changes for LASLINK Product
    @JdbcControl.SQL(statement = "call setup_laslink_customer({CustomerId})")
    void createLasLinkCustomerConfiguration(Integer CustomerId) throws SQLException;
    //END - Changes for LASLINK Product
	
    
    //START - Changes for LASLINK Product
    @JdbcControl.SQL(statement = "update org_node set org_node_mdr_number = {customer.mdrNumber}  where org_node_id = (select org.org_node_id from org_node org, org_node_category onc where onc.category_level = 1  and org.org_node_category_id = onc.org_node_category_id  and onc.customer_id = org.customer_id  and org.customer_id = {customer.customerId})")
    void updateCustomerTopNodeMdrNumber(com.ctb.bean.testAdmin.Customer customer) throws SQLException;
    //END - Changes for LASLINK Product
    
}