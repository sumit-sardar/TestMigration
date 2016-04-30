/**
 * 
 */
package com.ctb.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * POJO bean for Sales Force license data
 * @author TCS
 *
 */
public class SalesForceLicenseData implements Serializable{
	private static final long serialVersionUID = 1L;

	private String oasImplementationId;
	private String implRecordType;
	private String customerAccountName;
	private String accountState;
	private Integer orgNodeId;
	private String orgNodeName;
	private String contactPhone;
	private String contact;
	private String contactEmail;
	private String categoryName;
	private Integer categoryLevel;
	private String licenseModel;
	private Integer licenseCount;
	private Integer orderQuantity;
	private String licenseDistributedTo;
	private Date createdDate;
	private String intervalName;
	
	private Integer customerId;
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
	 * @return the oasImplementationId
	 */
	public String getOasImplementationId() {
		return oasImplementationId;
	}
	/**
	 * @param oasImplementationId the oasImplementationId to set
	 */
	public void setOasImplementationId(String oasImplementationId) {
		this.oasImplementationId = oasImplementationId;
	}
	/**
	 * @return the implRecordType
	 */
	public String getImplRecordType() {
		return implRecordType;
	}
	/**
	 * @param implRecordType the implRecordType to set
	 */
	public void setImplRecordType(String implRecordType) {
		this.implRecordType = implRecordType;
	}
	/**
	 * @return the customerAccountName
	 */
	public String getCustomerAccountName() {
		return customerAccountName;
	}
	/**
	 * @param customerAccountName the customerAccountName to set
	 */
	public void setCustomerAccountName(String customerAccountName) {
		this.customerAccountName = customerAccountName;
	}
	/**
	 * @return the accountState
	 */
	public String getAccountState() {
		return accountState;
	}
	/**
	 * @param accountState the accountState to set
	 */
	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}
	/**
	 * @return the orgNodeId
	 */
	public Integer getOrgNodeId() {
		return orgNodeId;
	}
	/**
	 * @param orgNodeId the orgNodeId to set
	 */
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	/**
	 * @return the orgNodeName
	 */
	public String getOrgNodeName() {
		return orgNodeName;
	}
	/**
	 * @param orgNodeName the orgNodeName to set
	 */
	public void setOrgNodeName(String orgNodeName) {
		this.orgNodeName = orgNodeName;
	}
	/**
	 * @return the contactPhone
	 */
	public String getContactPhone() {
		return contactPhone;
	}
	/**
	 * @param contactPhone the contactPhone to set
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}
	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
	/**
	 * @return the contactEmail
	 */
	public String getContactEmail() {
		return contactEmail;
	}
	/**
	 * @param contactEmail the contactEmail to set
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	/**
	 * @return the categoryLevel
	 */
	public Integer getCategoryLevel() {
		return categoryLevel;
	}
	/**
	 * @param categoryLevel the categoryLevel to set
	 */
	public void setCategoryLevel(Integer categoryLevel) {
		this.categoryLevel = categoryLevel;
	}
	/**
	 * @return the licenseModel
	 */
	public String getLicenseModel() {
		return licenseModel;
	}
	/**
	 * @param licenseModel the licenseModel to set
	 */
	public void setLicenseModel(String licenseModel) {
		this.licenseModel = licenseModel;
	}
	/**
	 * @return the licenseCount
	 */
	public Integer getLicenseCount() {
		return licenseCount;
	}
	/**
	 * @param licenseCount the licenseCount to set
	 */
	public void setLicenseCount(Integer licenseCount) {
		this.licenseCount = licenseCount;
	}
	/**
	 * @return the orderQuantity
	 */
	public Integer getOrderQuantity() {
		return orderQuantity;
	}
	/**
	 * @param orderQuantity the orderQuantity to set
	 */
	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	/**
	 * @return the licenseDistributedTo
	 */
	public String getLicenseDistributedTo() {
		return licenseDistributedTo;
	}
	/**
	 * @param licenseDistributedTo the licenseDistributedTo to set
	 */
	public void setLicenseDistributedTo(String licenseDistributedTo) {
		this.licenseDistributedTo = licenseDistributedTo;
	}
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the intervalName
	 */
	public String getIntervalName() {
		return intervalName;
	}
	/**
	 * @param intervalName the intervalName to set
	 */
	public void setIntervalName(String intervalName) {
		this.intervalName = intervalName;
	}
	
}
