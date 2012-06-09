package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;

/*
*ISTEP CR032:Download Test
*/
public class CustomerTestResource extends CTBBean {
	static final long serialVersionUID = 1L;
	private Integer customerId;
	private Integer productId;
	private String productType;
	private String productName;
	private String resourceURI;
	private String contentSize;
	
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
	 * @return Returns the productId.
	 */
	public Integer getProductId() {
		return productId;
	}
	
	/**
	 * @param productId The productId to set.
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	/**
	 * @return  Returns the productType.
	 */
	public String getProductType() {
		return productType;
	}
	
	/**
	 * @param productType The productType to set.
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	/**
	 * @return Returns the productName.
	 */
	public String getProductName() {
		return productName;
	}
	
	/**
	 * @param productName The productName to set.
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	/**
	 * @return Returns the resourceURI.
	 */
	public String getResourceURI() {
		return resourceURI;
	}
	
	/**
	 * @param resourceURI The resourceURI to set. 
	 */
	public void setResourceURI(String resourceURI) {
		this.resourceURI = resourceURI;
	}
	
	/**
	 * @return  Returns the contentSize.
	 */
	public String getContentSize() {
		return contentSize;
	}
	
	/**
	 * @param contentSize  The contentSize to set. 
	 */
	public void setContentSize(String contentSize) {
		this.contentSize = contentSize;
	}

	
}
