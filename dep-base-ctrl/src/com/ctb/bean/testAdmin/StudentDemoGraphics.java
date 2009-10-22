package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;
import java.util.Date;


public class StudentDemoGraphics extends CTBBean
{ 
    static final long serialVersionUID = 1L;
		
    private Integer customerDemographicId;
    private Integer customerId;
    private String labelName;
    private String labelCode;
    private String valueCardinality;
    private Integer sortOrder;
    private String importEditable;
    private String visible;
    private Date createdDateTime;
    private Date updatedDateTime;
    
    
	/**
	 * @return the customerDemographicId
	 */
	public Integer getCustomerDemographicId() {
		return customerDemographicId;
	}
	/**
	 * @param customerDemographicId the customerDemographicId to set
	 */
	public void setCustomerDemographicId(Integer customerDemographicId) {
		this.customerDemographicId = customerDemographicId;
	}
	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the labelName
	 */
	public String getLabelName() {
		return labelName;
	}
	/**
	 * @param labelName the labelName to set
	 */
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	/**
	 * @return the labelCode
	 */
	public String getLabelCode() {
		return labelCode;
	}
	/**
	 * @param labelCode the labelCode to set
	 */
	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}
	/**
	 * @return the valueCardinality
	 */
	public String getValueCardinality() {
		return valueCardinality;
	}
	/**
	 * @param valueCardinality the valueCardinality to set
	 */
	public void setValueCardinality(String valueCardinality) {
		this.valueCardinality = valueCardinality;
	}
	/**
	 * @return the sortOrder
	 */
	public Integer getSortOrder() {
		return sortOrder;
	}
	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	/**
	 * @return the importEditable
	 */
	public String getImportEditable() {
		return importEditable;
	}
	/**
	 * @param importEditable the importEditable to set
	 */
	public void setImportEditable(String importEditable) {
		this.importEditable = importEditable;
	}
	/**
	 * @return the visible
	 */
	public String getVisible() {
		return visible;
	}
	/**
	 * @param visible the visible to set
	 */
	public void setVisible(String visible) {
		this.visible = visible;
	}
	/**
	 * @return the createdDateTime
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime the createdDateTime to set
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	/**
	 * @return the updatedDateTime
	 */
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	/**
	 * @param updatedDateTime the updatedDateTime to set
	 */
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	
}
