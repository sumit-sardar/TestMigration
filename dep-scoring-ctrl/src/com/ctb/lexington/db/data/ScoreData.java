package com.ctb.lexington.db.data;


public class ScoreData {
	private String customerName;
	private String cohortName;
	private String cohortDesc;
	private String cohortGradYear;
	private String adminName;
	private String adminDesc;
	private String adminInstance;
	private String retestFlag;
	private String importName;
	private int importId;
	private int customerId;
	private String importGradYear;
	
	private GenericData genericData;
	
	public String getAdminDesc() {
		return adminDesc;
	}

	public void setAdminDesc(String adminDesc) {
		this.adminDesc = adminDesc;
	}

	public String getAdminInstance() {
		return adminInstance;
	}

	public void setAdminInstance(String adminInstance) {
		this.adminInstance = adminInstance;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getCohortDesc() {
		return cohortDesc;
	}

	public void setCohortDesc(String cohortDesc) {
		this.cohortDesc = cohortDesc;
	}

	public String getCohortGradYear() {
		return cohortGradYear;
	}

	public void setCohortGradYear(String cohortGradYear) {
		this.cohortGradYear = cohortGradYear;
	}

	public String getCohortName() {
		return cohortName;
	}

	public void setCohortName(String cohortName) {
		this.cohortName = cohortName;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getImportId() {
		return importId;
	}

	public void setImportId(int importId) {
		this.importId = importId;
	}

	public String getImportName() {
		return importName;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	public String getRetestFlag() {
		return retestFlag;
	}

	public void setRetestFlag(String retestFlag) {
		this.retestFlag = retestFlag;
	}

	public void setImportGradYear(String importGradYear) {
		this.importGradYear = importGradYear;
	}

	public String getImportGradYear() {
		return importGradYear;
	}

	public GenericData getGenericData() {
		return genericData;
	}

	public void setGenericData(GenericData genericData) {
		this.genericData = genericData;
	}
}
