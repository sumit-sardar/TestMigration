package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * Data bean representing the Address of the User.
 * 
 * @author Tata Consultency Services
 */
public class Address extends CTBBean
{ 
    private Integer addressId;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;
    private String statePr;
    private String stateDesc;
    private String country;
    private String zipCode;
    
    private String primaryPhone;
    private String secondaryPhone;
    private String faxNumber;
       
    private String zipCodeExt;
    private String primaryPhoneExt;
    
    private Integer createdBy;
    private Date CreatedDateTime;
    private Integer updatedBy;
    private Date updatedDateTime;
    
    
    
     /**
	 * @return Returns the addressId.
	 */
	public Integer getAddressId() {
		return addressId;
	}
	/**
	 * @param addressId The addressId to set.
	 */
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
     /**
	 * @return Returns the addressLine1.
	 */
    public String getAddressLine1() {
		return this.addressLine1;
	}
    /**
	 * @param addressLine1 The addressLine1 to set.
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
     /**
	 * @return Returns the addressLine2.
	 */
	public String getAddressLine2() {
		return this.addressLine2;
	}
    /**
	 * @param addressLine2 The addressLine2 to set.
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
     /**
	 * @return Returns the addressLine3.
	 */
    public String getAddressLine3() {
		return this.addressLine3;
	}
    /**
	 * @param addressLine3 The addressLine3 to set.
	 */
	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}
     /**
	 * @return Returns the city.
	 */
	public String getCity() {
		return this.city ;
	}
    /**
	 * @param city The city to set.
	 */
	public void setCity(String city) {
		this.city = city;
	}
     /**
	 * @return Returns the statePr.
	 */
	public String getStatePr() {
		return this.statePr ;
	}
    /**
	 * @param statePr The statePr to set.
	 */
	public void setStatePr(String statePr) {
		this.statePr = statePr;
	}
     /**
	 * @return Returns the stateDesc.
	 */
	public String getStateDesc() {
		return this.stateDesc ;
	}
    /**
	 * @param stateDesc The stateDesc to set.
	 */
	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}
     /**
	 * @return Returns the zipCodeExt.
	 */
    public String getCountry() {
		return this.country ;
	}
    /**
	 * @param country The country to set.
	 */
	public void setCountry(String country) {
		this.country = country;
	}
     /**
	 * @return Returns the zipCode.
	 */
	public String getZipCode() {
		return this.zipCode ;
	}
    /**
	 * @param zipCode The zipCode to set.
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
    
     /**
	 * @return Returns the primaryPhone.
	 */
	public String getPrimaryPhone() {
		return this.primaryPhone;
	}
    /**
	 * @param primaryPhone The primaryPhone to set.
	 */
	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}
    
     /**
	 * @return Returns the secondaryPhone.
	 */
	public String getSecondaryPhone() {
		return this.secondaryPhone;
	}
    /**
	 * @param secondaryPhone The secondaryPhone to set.
	 */
	public void setSecondaryPhone(String secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}
    /**
	 * @return Returns the faxNumber.
	 */
	public String getFaxNumber() {
		return this.faxNumber ;
	}
    /**
	 * @param faxNumber The faxNumber to set.
	 */
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
    
     /**
	 * @return Returns the zipCodeExt.
	 */
    public String getZipCodeExt() {
		return this.zipCodeExt ;
	}
    /**
	 * @param zipCodeExt The zipCodeExt to set.
	 */
	public void setZipCodeExt(String zipCodeExt) {
		this.zipCodeExt = zipCodeExt;
	}
    /**
	 * @return Returns the primaryPhoneExt.
	 */
    public String getPrimaryPhoneExt() {
		return this.primaryPhoneExt ;
	}
    /**
	 * @param primaryPhoneExt The primaryPhoneExt to set.
	 */
	public void setPrimaryPhoneExt(String primaryPhoneExt) {
		this.primaryPhoneExt = primaryPhoneExt;
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
		return CreatedDateTime;
	}
	/**
	 * @param createdDateTime The createdDateTime to set.
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		CreatedDateTime = createdDateTime;
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
} 
