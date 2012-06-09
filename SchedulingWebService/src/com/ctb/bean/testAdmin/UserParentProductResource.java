package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;

/**
 * @author TCS
 */
public class UserParentProductResource extends CTBBean{
	
	static final long serialVersionUID = 1L;
	private Integer productId;
	private String productDescription;
	private String resourceTypeCode;
	private String resourceURI;
	
	
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
	 * @return Returns the productDescription.
	 */
	public String getProductDescription() {
		return productDescription;
	}
	/**
	 * @param productDescription The productDescription to set.
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	 /**
	 * @return Returns the resourceTypeCode.
	 */
	public String getResourceTypeCode() {
		return resourceTypeCode;
	}
	/**
	 * @param resourceTypeCode The resourceTypeCode to set.
	 */
	public void setResourceTypeCode(String resourceTypeCode) {
		this.resourceTypeCode = resourceTypeCode;
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
	
}
