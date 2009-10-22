package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;

/**
 * CustomerResourceData.java
 * @author Tai_Truong
 *
 * Data bean representing the contents of the OAS.CUSTOMER_RESOURCE table
 */
public class CustomerResourceData extends CTBBean
{
    static final long serialVersionUID = 1L;
    private Integer customerId;
    private String resourceTypeCode;
    private String resourceURI;

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
