package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

public class License extends CTBBean
{ 
    private Integer customerId;
    private Integer productId;
    private String addressLine1;
    
    private Integer createdBy;
    private Date CreatedDateTime;
    private Integer updatedBy;
    private Date updatedDateTime;
    
    
    
     /**
	 * @return Returns the addressId.
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param addressId The addressId to set.
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
    
    
     /**
	 * @return Returns the addressId.
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * @param addressId The addressId to set.
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
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
