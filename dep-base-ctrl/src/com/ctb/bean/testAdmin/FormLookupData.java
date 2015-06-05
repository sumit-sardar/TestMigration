package com.ctb.bean.testAdmin;

import java.util.Date;
import com.ctb.bean.CTBBean;

public class FormLookupData extends CTBBean {
	static final long serialVersionUID = 1L;
	private Integer testCatalogId;
	private Integer productId;
	private String testName;
	private String screenReaderForms;
	private String nonScreenReaderForms;
	private String activationStatus;
	private Date createdDateTime;
	private Date updatedDateTime;

	public Integer getTestCatalogId() {
		return testCatalogId;
	}

	public void setTestCatalogId(Integer testCatalogId) {
		this.testCatalogId = testCatalogId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getScreenReaderForms() {
		return screenReaderForms;
	}

	public void setScreenReaderForms(String screenReaderForms) {
		this.screenReaderForms = screenReaderForms;
	}

	public String getNonScreenReaderForms() {
		return nonScreenReaderForms;
	}

	public void setNonScreenReaderForms(String nonScreenReaderForms) {
		this.nonScreenReaderForms = nonScreenReaderForms;
	}

	public String getActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public String[] getAllScreenReaderForms() {
		String allForms[] = this.screenReaderForms.split(",");
		return allForms;
	}

	public String[] getAllNonScreenReaderForms() {
		String allForms[] = this.nonScreenReaderForms.split(",");
		return allForms;
	}

}
