package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;
/**
 * CustomerReport.java
 * @author TCS
 *
 */
public class CustomerExportStudentData extends CTBBean {
	static final long serialVersionUID = 1L;
    private Integer customerId;
    private Integer productId;
    private String exportName;
    private String exportURL;
    private String exportDescription;
    
    
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
	 * @return the exportName
	 */
	public String getExportName() {
		return exportName;
	}
	/**
	 * @param exportName the exportName to set
	 */
	public void setExportName(String exportName) {
		this.exportName = exportName;
	}
	/**
	 * @return the exportURL
	 */
	public String getExportURL() {
		return exportURL;
	}
	/**
	 * @param exportURL the exportURL to set
	 */
	public void setExportURL(String exportURL) {
		this.exportURL = exportURL;
	}
	/**
	 * @return the exportDescription
	 */
	public String getExportDescription() {
		return exportDescription;
	}
	/**
	 * @param exportDescription the exportDescription to set
	 */
	public void setExportDescription(String exportDescription) {
		this.exportDescription = exportDescription;
	}
    
    
}
