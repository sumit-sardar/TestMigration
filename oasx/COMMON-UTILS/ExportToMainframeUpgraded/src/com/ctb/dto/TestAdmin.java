package com.ctb.dto;

import java.io.Serializable;


public class TestAdmin implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer testAdminId;
	private Integer testCatalogId;
	private String preferedForm;
	private Integer productId;
	private String testLevel;
	private String testDate;
	private String timezone;
	private String programStartDate;
	private String dateTestingCompleted;
	
	/**
	 * @return the testAdminId
	 */
	public Integer getTestAdminId() {
		return testAdminId;
	}
	/**
	 * @param testAdminId the testAdminId to set
	 */
	public void setTestAdminId(Integer testAdminId) {
		this.testAdminId = testAdminId;
	}
	/**
	 * @return the testCatalogId
	 */
	public Integer getTestCatalogId() {
		return testCatalogId;
	}
	/**
	 * @param testCatalogId the testCatalogId to set
	 */
	public void setTestCatalogId(Integer testCatalogId) {
		this.testCatalogId = testCatalogId;
	}
	/**
	 * @return the preferedForm
	 */
	public String getPreferedForm() {
		return preferedForm;
	}
	/**
	 * @param preferedForm the preferedForm to set
	 */
	public void setPreferedForm(String preferedForm) {
		this.preferedForm = preferedForm;
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
	 * @return the testLevel
	 */
	public String getTestLevel() {
		return testLevel;
	}
	/**
	 * @param testLevel the testLevel to set
	 */
	public void setTestLevel(String testLevel) {
		this.testLevel = testLevel;
	}
	/**
	 * @return the testDate
	 */
	public String getTestDate() {
		return testDate;
	}
	/**
	 * @param testDate the testDate to set
	 */
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}
	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	/**
	 * @return the programStartDate
	 */
	public String getProgramStartDate() {
		return programStartDate;
	}
	/**
	 * @param programStartDate the programStartDate to set
	 */
	public void setProgramStartDate(String programStartDate) {
		this.programStartDate = programStartDate;
	}
	/**
	 * @return the dateTestingCompleted
	 */
	public String getDateTestingCompleted() {
		return dateTestingCompleted;
	}
	/**
	 * @param dateTestingCompleted the dateTestingCompleted to set
	 */
	public void setDateTestingCompleted(String dateTestingCompleted) {
		this.dateTestingCompleted = dateTestingCompleted;
	}
	
}
