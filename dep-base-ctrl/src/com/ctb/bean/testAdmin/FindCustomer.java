package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * Data bean representing the contents of the OAS.CUSTOMER table
 * 
 * @author Tata consultency services
 */
public class FindCustomer extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer customerId;
    private String customerName;
    private String hideAccommodations;
    private String importStudentEditable;
    private String demographicVisible;
    private String contactName;
	private String contactPhone;	
	private String contactEmail;
	private String country ;
	private String statePr;
    private String stateDesc;
	private Date createdDateTime;
	private String extCustomerId;
	private Integer billingAddressId;
	private Integer createdBy;
	private Integer updatedBy;
	private Date updatedDateTime;
	private String ctbContactName;
	private String activationStatus;
	private Integer mailingAddressId;
	private String ctbContactEmail;
	private String allowDataUpload;
	private String eissOrg;
	private String topOrgName;
	private String seasonYear;
	private Integer activeProgramId;
	private Date activeProgramStartDate;
	private Date activeProgramEndDate;
	private String activeProgramName;
    private Address billingAddress;
    private Address mailingAddress;
    private Integer customerConfigurationId;
    private String customerConfigurationName;
    private String editable;
    private String allowSubscription;
 
 
    public String getAllowSubscription() {
		return allowSubscription;
	}
	public void setAllowSubscription(String allowSubscription) {
		this.allowSubscription = allowSubscription;
	}
    /**
	 * @return Returns the customerId.
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId The customerId to set.
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
    /**
	 * @return Returns the customerName.
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName The customerName to set.
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
     /**
	 * @return Returns the hideAccommodations.
	 */
	public String getHideAccommodations() {
		return hideAccommodations;
	}
	/**
	 * @param hideAccommodations The hideAccommodations to set.
	 */
	public void setHideAccommodations(String hideAccommodations) {
		this.hideAccommodations = hideAccommodations;
	}
    
     /**
	 * @return Returns the importStudentEditable.
	 */
	public String getImportStudentEditable() {
		return importStudentEditable;
	}
	/**
	 * @param importStudentEditable The importStudentEditable to set.
	 */
	public void setImportStudentEditable(String importStudentEditable) {
		this.importStudentEditable = importStudentEditable;
	}

     /**
	 * @return Returns the demographicVisible.
	 */
	public String getDemographicVisible() {
		return demographicVisible;
	}
	/**
	 * @param demographicVisible The demographicVisible to set.
	 */
	public void setDemographicVisible(String demographicVisible) {
		this.demographicVisible = demographicVisible;
	}
    /**
	 * @return Returns the activationStatus.
	 */
	public String getActivationStatus() {
		return activationStatus;
	}
	/**
	 * @param activationStatus The activationStatus to set.
	 */
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	/**
	 * @return Returns the activeProgramEndDate.
	 */
	public Date getActiveProgramEndDate() {
		return activeProgramEndDate;
	}
	/**
	 * @param activeProgramEndDate The activeProgramEndDate to set.
	 */
	public void setActiveProgramEndDate(Date activeProgramEndDate) {
		this.activeProgramEndDate = activeProgramEndDate;
	}
	/**
	 * @return Returns the activeProgramId.
	 */
	public Integer getActiveProgramId() {
		return activeProgramId;
	}
	/**
	 * @param activeProgramId The activeProgramId to set.
	 */
	public void setActiveProgramId(Integer activeProgramId) {
		this.activeProgramId = activeProgramId;
	}
	/**
	 * @return Returns the activeProgramName.
	 */
	public String getActiveProgramName() {
		return activeProgramName;
	}
	/**
	 * @param activeProgramName The activeProgramName to set.
	 */
	public void setActiveProgramName(String activeProgramName) {
		this.activeProgramName = activeProgramName;
	}
	/**
	 * @return Returns the activeProgramStartDate.
	 */
	public Date getActiveProgramStartDate() {
		return activeProgramStartDate;
	}
	/**
	 * @param activeProgramStartDate The activeProgramStartDate to set.
	 */
	public void setActiveProgramStartDate(Date activeProgramStartDate) {
		this.activeProgramStartDate = activeProgramStartDate;
	}
	/**
	 * @return Returns the allowDataUpload.
	 */
	public String getAllowDataUpload() {
		return allowDataUpload;
	}
	/**
	 * @param allowDataUpload The allowDataUpload to set.
	 */
	public void setAllowDataUpload(String allowDataUpload) {
		this.allowDataUpload = allowDataUpload;
	}
	/**
	 * @return Returns the billingAddressId.
	 */
	public Integer getBillingAddressId() {
		return billingAddressId;
	}
	/**
	 * @param billingAddressId The billingAddressId to set.
	 */
	public void setBillingAddressId(Integer billingAddressId) {
		this.billingAddressId = billingAddressId;
	}
	/**
	 * @return Returns the contactEmail.
	 */
	public String getContactEmail() {
		return contactEmail;
	}
	/**
	 * @param contactEmail The contactEmail to set.
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	/**
	 * @return Returns the contactName.
	 */
	public String getContactName() {
		return contactName;
	}
	/**
	 * @param contactName The contactName to set.
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	/**
	 * @return Returns the contactPhone.
	 */
	public String getContactPhone() {
		return contactPhone;
	}
	/**
	 * @param contactPhone The contactPhone to set.
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	/**
	 * @return Returns the country.
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country The country to set.
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return Returns the createdBy.
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return Returns the createdDateTime.
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime The createdDateTime to set.
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	/**
	 * @return Returns the ctbContactEmail.
	 */
	public String getCtbContactEmail() {
		return ctbContactEmail;
	}
	/**
	 * @param ctbContactEmail The ctbContactEmail to set.
	 */
	public void setCtbContactEmail(String ctbContactEmail) {
		this.ctbContactEmail = ctbContactEmail;
	}
	/**
	 * @return Returns the ctbContactName.
	 */
	public String getCtbContactName() {
		return ctbContactName;
	}
	/**
	 * @param ctbContactName The ctbContactName to set.
	 */
	public void setCtbContactName(String ctbContactName) {
		this.ctbContactName = ctbContactName;
	}
	/**
	 * @return Returns the eissOrg.
	 */
	public String getEissOrg() {
		return eissOrg;
	}
	/**
	 * @param eissOrg The eissOrg to set.
	 */
	public void setEissOrg(String eissOrg) {
		this.eissOrg = eissOrg;
	}
	/**
	 * @return Returns the extCustomerId.
	 */
	public String getExtCustomerId() {
		return extCustomerId;
	}
	/**
	 * @param extCustomerId The extCustomerId to set.
	 */
	public void setExtCustomerId(String extCustomerId) {
		this.extCustomerId = extCustomerId;
	}
	/**
	 * @return Returns the mailingAddressId.
	 */
	public Integer getMailingAddressId() {
		return mailingAddressId;
	}
	/**
	 * @param mailingAddressId The mailingAddressId to set.
	 */
	public void setMailingAddressId(Integer mailingAddressId) {
		this.mailingAddressId = mailingAddressId;
	}
	/**
	 * @return Returns the seasonYear.
	 */
	public String getSeasonYear() {
		return seasonYear;
	}
	/**
	 * @param seasonYear The seasonYear to set.
	 */
	public void setSeasonYear(String seasonYear) {
		this.seasonYear = seasonYear;
	}
	/**
	 * @return Returns the statePr.
	 */
	public String getStatePr() {
		return statePr;
	}
	/**
	 * @param Desc The Desc to set.
	 */
	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}
    /**
	 * @return Returns the Desc.
	 */
	public String getStateDesc() {
		return stateDesc;
	}
	/**
	 * @param statePr The statePr to set.
	 */
	public void setStatePr(String statePr) {
		this.statePr = statePr;
	}
	/**
	 * @return Returns the topOrgName.
	 */
	public String getTopOrgName() {
		return topOrgName;
	}
	/**
	 * @param topOrgName The topOrgName to set.
	 */
	public void setTopOrgName(String topOrgName) {
		this.topOrgName = topOrgName;
	}
	/**
	 * @return Returns the updatedBy.
	 */
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy The updatedBy to set.
	 */
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @return Returns the updatedDateTime.
	 */
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	/**
	 * @param updatedDateTime The updatedDateTime to set.
	 */
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
    
    /**
	 * @return Returns the billingAddress.
	 */
	public Address getBillingAddress() {
	    return billingAddress;
	}
	/**
	 * @param billingAddress The billingAddress to set.
	 */
	public void setBillingAddress(Address billingAddress) {
	    this.billingAddress = billingAddress;
	}
	/**
	 * @return Returns the mailingAddress.
	 */
	public Address getMailingAddress() {
	    return mailingAddress;
	}
	/**
	 * @param mailingAddress The mailingAddress to set.
	 */
	public void setMailingAddress(Address mailingAddress) {
	    this.mailingAddress = mailingAddress;
	}
    
    /**
	 * @return Returns the customerConfigurationId.
	 */
	public Integer getCustomerConfigurationId() {
		return customerConfigurationId;
	}
	/**
	 * @param customerConfigurationId The customerConfigurationId to set.
	 */
	public void setCustomerConfigurationId(Integer customerConfigurationId) {
		this.customerConfigurationId = customerConfigurationId;
	}
	/**
	 * @return Returns the customerConfigurationName.
	 */
	public String getCustomerConfigurationName() {
		return customerConfigurationName;
	}
	/**
	 * @param customerConfigurationName The customerConfigurationName to set.
	 */
	public void setCustomerConfigurationName(String customerConfigurationName) {
		this.customerConfigurationName = customerConfigurationName;
	}
     /**
	 * @return Returns the editable.
	 */
	public String getEditable() {
		return editable;
	}
	/**
	 * @param editable The editable to set.
	 */
	public void setEditable(String editable) {
		this.editable = editable;
	}

   
} 
