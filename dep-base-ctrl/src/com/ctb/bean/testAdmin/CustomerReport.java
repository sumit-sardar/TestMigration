package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

/**
 * CustomerReport.java
 * @author Nate_Cohen, John_Wang
 *
 * Data bean representing the contents of the OAS.CUSTOMER_REPORT_BRIDGE table
 */
public class CustomerReport extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer customerId;
    private String reportName;
    private String displayName;
    private String description;
    private String systemKey;
    private String customerKey;
    private String reportUrl;
//    private Integer activeProgramId;
    private Integer programId;
    private Integer orgNodeId;
    private Integer categoryLevel;
    private Integer productId;
    private String programName;
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
	 * @return Returns the reportName.
	 */
	public String getReportName() {
		return reportName;
	}
	/**
	 * @param reportName The reportName to set.
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
    /**
	 * @return Returns the displayName.
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
    /**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the customerKey.
	 */
	public String getCustomerKey() {
		return customerKey;
	}
	/**
	 * @param customerKey The customerKey to set.
	 */
	public void setCustomerKey(String customerKey) {
		this.customerKey = customerKey;
	}
	/**
	 * @return Returns the reportUrl.
	 */
	public String getReportUrl() {
		return reportUrl;
	}
	/**
	 * @param reportUrl The reportUrl to set.
	 */
	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}
	/**
	 * @return Returns the systemKey.
	 */
	public String getSystemKey() {
		return systemKey;
	}
	/**
	 * @param systemKey The systemKey to set.
	 */
	public void setSystemKey(String systemKey) {
		this.systemKey = systemKey;
	}
	/**
	 * @return the activeProgramId
	 */
//	public Integer getActiveProgramId() {
//		return activeProgramId;
//	}
	/**
	 * @param activeProgramId the activeProgramId to set
	 */
//	public void setActiveProgramId(Integer activeProgramId) {
//		this.activeProgramId = activeProgramId;
//	}
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
	 * @return the productId
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	/**
	 * @return the programName
	 */
	public String getProgramName() {
		return programName;
	}
	/**
	 * @param programName the programName to set
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}
} 
