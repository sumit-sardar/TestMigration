package dto; 

/*
 * @author Tata Consultancy Services
 * Deals with customer profile details
 * CustomerProfileInformation class is used to contain Customer profile Details.
*/

import utils.PermissionsUtils;
import utils.StringUtils;

import com.ctb.bean.testAdmin.Address;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfig;
import com.ctb.bean.testAdmin.OrgNodeCategory;
import com.ctb.bean.testAdmin.USState;
import com.ctb.util.web.sanitizer.SanitizedFormField;


public class CustomerProfileInformation extends SanitizedFormField
{ 
    static final long serialVersionUID = 1L;

    private String id = "";
    private String customerType = "";
    private String customerTypeId = "";
    private String name = "";
    private String code = "";
    private String state = "";
    private String stateId = "";
    private Integer billingAddressId = new Integer(0);
    private Integer mailingAddressId = new Integer(0); 
    private CustomerContactInformation billingContact = new CustomerContactInformation();
    private CustomerContactInformation mailingContact = new CustomerContactInformation();
    private Level level = new Level();
    private Level[] categoryList = null;
    private String ctbContact = "";
    private String ctbContactEmail = "";
    private String customerContact = "";
    private String customerContactEmail = "";
    
    private String conatctPhone = "";
    private String conatctPhone1 = "";
    private String conatctPhone2 = "";
    private String conatctPhone3 = "";
    private String conatctPhone4 = ""; 
    private String actionPermission = PermissionsUtils.ENABLE_ALL_PERMISSION_TOKEN;
    /*
     * default construnctor
    */
    public CustomerProfileInformation() {
        
    }
    
     /**
     * constructor
     */
    public CustomerProfileInformation(Customer customer) {
        this.customerType = null;
        CustomerConfig cConfig = new CustomerConfig(); 
        
        if ( customer.getCustomerConfiguration() != null
                && customer.getCustomerConfiguration().length > 0 ) {
                
                 cConfig = customer.getCustomerConfiguration()[0];
                 
                 if ( cConfig.getCustomerConfigurationName() != null ) {
                    
                    this.customerType = cConfig.getCustomerConfigurationName();
                    
                 }
                 
            this.customerTypeId = null; 
                       
            if ( cConfig.getCustomerConfigurationId() != null ) {
                
                this.customerTypeId = cConfig.getCustomerConfigurationId().toString();
                
            }
            
        }
        this.name = customer.getCustomerName();
        this.id = customer.getCustomerId().toString();
        this.code = customer.getExtCustomerId();
        this.state = customer.getState().getStatePrDesc();
        this.stateId = customer.getState().getStatePr();
        this.billingAddressId = customer.getBillingAddressId();
        this.billingContact = new CustomerContactInformation(
                                customer.getBillingAddressId(), 
                                customer.getBillingAddress());
                                
        this.mailingAddressId = customer.getMailingAddressId();
        this.mailingContact = new CustomerContactInformation(
                                customer.getMailingAddressId(), 
                                customer.getMailingAddress());    
                                
        this.ctbContact = customer.getCtbContactName();
        this.ctbContactEmail = customer.getCtbContactEmail();
        this.customerContact = customer.getContactName();
        this.customerContactEmail = customer.getContactEmail();
        this.actionPermission = customer.getEditable();
         //Split Contact Phone for Edit
        if ( customer.getContactPhone() != null 
                && !"".equals(customer.getContactPhone()) ) {
            
            String[] contactPhone = StringUtils.splitPhoneNumberAndFaxNumber(
                                                customer.getContactPhone(),true);
            this.conatctPhone1 = contactPhone[3];
            this.conatctPhone2 = contactPhone[2];
            this.conatctPhone3 = contactPhone[1];
            this.conatctPhone4 = contactPhone[0];
            String contactPh= customer.getContactPhone();
            contactPh = contactPh.replaceAll("x"," Ext: ");
            this.conatctPhone = contactPh;
            
        }
        
        if ( customer.getOrgNodeCategory() != null 
                && customer.getOrgNodeCategory().length > 0 ) {
            
             OrgNodeCategory[] orgNodeCategory = customer.getOrgNodeCategory();
             categoryList = new Level[orgNodeCategory.length];
             for( int i = 0 ; i < orgNodeCategory.length ; i++ ) {
                this.level = new Level(customer.getOrgNodeCategory()[i].getOrgNodeCategoryId(),
                              customer.getOrgNodeCategory()[i].getCategoryName(),
                              customer.getOrgNodeCategory()[i].getCategoryLevel(),
                              customer.getOrgNodeCategory()[i].getDeletable(),
                              customer.getOrgNodeCategory()[i].getBeforeInsertable(),
                              customer.getOrgNodeCategory()[i].getAfterInsertable()); 
                categoryList[i] = this.level;
             }
             
        }                                                          
    }

    
    public CustomerProfileInformation(Integer id, String name, String code, String state) {
        this.id = id.toString();
        this.name = name;
        this.code = code;
        this.state = state;
    }
    
    /**
     * createClone
     */
    public CustomerProfileInformation createClone() {
        CustomerProfileInformation copied = new CustomerProfileInformation();

        copied.setId(this.id);
        copied.setCustomerType(this.customerType);
        copied.setCustomerTypeId(this.customerTypeId);
        copied.setName(this.name);
        copied.setCode(this.code);
        copied.setState(this.state);
        copied.setStateId(this.stateId);
        copied.setActionPermission(this.actionPermission);    
        copied.setBillingContact(this.billingContact);
        copied.setMailingContact(this.mailingContact);
        copied.setCtbContact(this.ctbContact);
        copied.setCtbContactEmail(this.ctbContactEmail);
        copied.setCustomerContact(this.customerContact);
        copied.setCustomerContactEmail(this.customerContactEmail);
        copied.setConatctPhone(this.conatctPhone);
        copied.setLevel(this.level);
        return copied;       
    }
    
    /**
     * makeCopy
     */
   public Customer makeCopy(String userName, OrgNodeCategory[] orgNodeCategories) 
   {
        Customer copied = new Customer();
        CustomerConfig[] customerConfigs = new CustomerConfig[1];
        CustomerConfig customerConfig = new CustomerConfig();
        USState usState = new USState();
             
        
        //customer Information
        if ( !"".equals(this.id) ) {
            
            copied.setCustomerId(Integer.valueOf(this.id.trim()));
            
        }
        customerConfig.setCustomerConfigurationName(this.customerTypeId);
        customerConfigs[0] = customerConfig;
        copied.setCustomerConfiguration(customerConfigs);
        copied.setCustomerName(this.name.trim());
        if ( this.code != null ) {
            
            copied.setExtCustomerId(this.code.trim());
        
        }
        usState.setStatePrDesc(this.state);
        copied.setState(usState);
        
        if ( !"".equals(this.stateId) ) {
            
            usState.setStatePr(this.stateId);
            copied.getState().setStatePr(this.stateId);
            copied.setStatePr(this.stateId);
            
        }
        
        copied.setOrgNodeCategory(orgNodeCategories);
        
        copied.setCtbContactName(this.ctbContact.trim());
        copied.setCtbContactEmail(this.ctbContactEmail.trim());
        copied.setContactName(this.customerContact.trim());
        copied.setContactEmail(this.customerContactEmail.trim());
        
        copied.setContactPhone(this.joinPhoneFromOthers().trim());
        
        //Contact Information
                
        if ( ( this.getBillingAddressId() != null 
                && this.billingContact.getAddressId().intValue() != 0 ) 
                || !billingContact.isEmpty() ) {
                    
            Address address = new Address();
            copied.setBillingAddressId(this.billingAddressId);   
            address.setAddressId(this.billingAddressId);
            address.setAddressLine1(this.billingContact.getAddressLine1().trim());
            address.setAddressLine2(this.billingContact.getAddressLine2().trim());
            address.setAddressLine3(this.billingContact.getAddressLine3().trim());
            address.setCity(this.billingContact.getCity().trim());
            address.setStatePr(this.billingContact.getState());
           
            String zipCode = this.billingContact.getZipCode1().trim();
            String zipCode2 = this.billingContact.getZipCode2();
            if( zipCode2 != null && !"".equals(zipCode2.trim())){
                zipCode += "-" + zipCode2.trim();
            }
            address.setZipCode(zipCode);
           
            copied.setBillingAddress(address); 
                   
        }
        
        if ( ( this.getMailingAddressId() != null 
                && this.mailingContact.getAddressId().intValue() != 0 ) 
                || !mailingContact.isEmpty() ) {
                    
            Address address = new Address();
            copied.setMailingAddressId(this.mailingAddressId);   
            address.setAddressId(this.mailingAddressId);
            address.setAddressLine1(this.mailingContact.getAddressLine1().trim());
            address.setAddressLine2(this.mailingContact.getAddressLine2().trim());
            address.setAddressLine3(this.mailingContact.getAddressLine3().trim());
            address.setCity(this.mailingContact.getCity().trim());
            address.setStatePr(this.mailingContact.getState());
            
            String zipCode = this.mailingContact.getZipCode1().trim();
            String zipCode2 = this.mailingContact.getZipCode2();
            if( zipCode2 != null && !"".equals(zipCode2.trim())){
                zipCode += "-" + zipCode2.trim();
            }
            address.setZipCode(zipCode);
           
            copied.setMailingAddress(address);    
                
        }
                 
        return copied;       
    }
    
    
	public String getId() {
		return this.id != null ? this.id : "";
	}
	public void setId(String id) {
		this.id = id;
	}
    public String getCustomerType() {
		return this.customerType != null ? this.customerType : "";
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
    public String getCustomerTypeId() {
		return this.customerTypeId != null ? this.customerTypeId : "";
	}
	public void setCustomerTypeId(String customerTypeId) {
		this.customerTypeId = customerTypeId;
	}
	public String getName() {
		return this.name != null ? this.name : "";
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return this.code != null ? this.code : "";
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getState() {
		return this.state != null ? this.state : "";
	}
	public void setState(String state) {
		this.state = state;
	}
    public String getStateId() {
		return this.stateId != null ? this.stateId : "";
	}
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}
      
    public CustomerContactInformation getBillingContact() {
		return this.billingContact != null ? 
                    this.billingContact : new CustomerContactInformation();
	}
	public void setBillingContact(CustomerContactInformation billingContact) {
		this.billingContact = billingContact;
	}
    public CustomerContactInformation getMailingContact() {
		return this.mailingContact != null ?
                    this.mailingContact : new CustomerContactInformation();
	}
	public void setMailingContact(CustomerContactInformation mailingContact) {
		this.mailingContact = mailingContact;
	}
    public String getCtbContact() {
		return this.ctbContact != null ? this.ctbContact : "";
	}
	public void setCtbContact(String ctbContact) {
		this.ctbContact = ctbContact;
	}
    public String getCtbContactEmail() {
		return this.ctbContactEmail != null ? this.ctbContactEmail : "";
	}
	public void setCtbContactEmail(String ctbContactEmail) {
		this.ctbContactEmail = ctbContactEmail;
	}
    public String getCustomerContact() {
		return this.customerContact != null ? this.customerContact : "";
	}
	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}
    public String getCustomerContactEmail() {
		return this.customerContactEmail != null ? this.customerContactEmail : "";
	}
	public void setCustomerContactEmail(String customerContactEmail) {
		this.customerContactEmail = customerContactEmail;
	}
    public String getConatctPhone() {
		return this.conatctPhone != null ? this.conatctPhone.trim() : "";
	}
	public void setConatctPhone(String conatctPhone) {
		this.conatctPhone = conatctPhone;
	}
    public String getConatctPhone1() {
		return this.conatctPhone1 != null ? this.conatctPhone1.trim() : "";
	}
	public void setConatctPhone1(String conatctPhone1) {
		this.conatctPhone1 = conatctPhone1;
	}
    public String getConatctPhone2() {
		return this.conatctPhone2 != null ? this.conatctPhone2.trim() : "";
	}
	public void setConatctPhone2(String conatctPhone2) {
		this.conatctPhone2 = conatctPhone2;
	}
    public String getConatctPhone3() {
		return this.conatctPhone3 != null ? this.conatctPhone3.trim() : "";
	}
	public void setConatctPhone3(String conatctPhone3) {
		this.conatctPhone3 = conatctPhone3;
	}
     public String getConatctPhone4() {
		return this.conatctPhone4 != null ? this.conatctPhone4.trim() : "";
	}
	public void setConatctPhone4(String conatctPhone4) {
		this.conatctPhone4 = conatctPhone4;
	}
     /**
	 * @return Returns the billingAddressId.
	 */
	public Integer getBillingAddressId() {
		return this.billingAddressId ;
	}
	/**
	 * @param billingAddressId The billingAddressId to set.
	 */
	public void setBillingAddressId(Integer billingAddressId) {
		this.billingAddressId = billingAddressId;
	}
    /**
	 * @return Returns the mailingAddressId.
	 */
	public Integer getMailingAddressId() {
		return this.mailingAddressId ;
	}
	/**
	 * @param mailingAddressId The mailingAddressId to set.
	 */
	public void setMailingAddressId(Integer mailingAddressId) {
		this.mailingAddressId = mailingAddressId;
	}
    
    /**
	 * @return Returns the actionPermission.
	 */
	public String getActionPermission() {
		return this.actionPermission != null ?
                     this.actionPermission : PermissionsUtils.ENABLE_ALL_PERMISSION_TOKEN;
	}
	/**
	 * @param actionPermission 
	 */
	public void setActionPermission(String actionPermission) {
		this.actionPermission = actionPermission;
	}
    public Level getLevel() {
		return this.level != null ? this.level : new Level();
	}
	public void setLevel(Level level) {
		this.level = level;
	}
    public Level[] getCategoryList() {
		return this.categoryList ;
	}
	public void setCategoryList(Level[] categoryList) {
		this.categoryList = categoryList;
	}
   
    public String joinPhoneFromOthers() {
        String phone1 = "";
        String phone2 = "";
        String phone3 = "";
        String phone4 = "";
        
        if ( conatctPhone1 != null && !"".equals(conatctPhone1) ) {
            
            // changed for defect # 50842
            phone1 = "(" + conatctPhone1 + ")";
            
        }
        
        if ( conatctPhone2 != null && !"".equals(conatctPhone2) ) {
            
            phone2 = conatctPhone2;
            
        }
        
        if ( conatctPhone3 != null && !"".equals(conatctPhone3) ) {
            
            phone3 = "-" + conatctPhone3;
            
        }
        
        if ( conatctPhone4 != null && !"".equals(conatctPhone4) ) {
            
            phone4 = "x" + conatctPhone4;
            
        }
        
        return ( phone1+phone2+phone3+phone4 );
    }
    
} 