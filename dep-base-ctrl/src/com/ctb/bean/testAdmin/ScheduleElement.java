package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the contents of the OAS.TEST_ADMIN_ITEM_SET table
 * 
 * @author Nate_Cohen
 */
public class ScheduleElement extends CTBBean
{ 
     static final long serialVersionUID = 1L;
     private Integer itemSetId;
     private Integer testAdminId;
     private Integer itemSetOrder;
     private String accessCode;
     private String tested;
     private String itemSetForm;
     private String sessionDefault;
     private String itemSetName;
    
     /**
 	 * @return Returns the itemSetForm.
 	 */
    public String getItemSetForm() {
		return itemSetForm;
	}
    /**
	 * @param itemSetForm The itemSetForm to set.
	 */
	public void setItemSetForm(String itemSetForm) {
		this.itemSetForm = itemSetForm;
	}
	
	/**
 	 * @return Returns the itemSetForm.
 	 */
    public String getItemSetName() {
		return itemSetName;
	}
    /**
	 * @param itemSetForm The itemSetForm to set.
	 */
	public void setItemSetName(String itemSetName) {
		this.itemSetName = itemSetName;
	}
	
	/**
	 * @return Returns the tested.
	 */
	public String getTested() {
		return tested;
	}
	/**
	 * @param tested The tested to set.
	 */
	public void setTested(String tested) {
		this.tested = tested;
	}
	/**
	 * @return Returns the accessCode.
	 */
	public String getAccessCode() {
		return accessCode;
	}
	/**
	 * @param accessCode The accessCode to set.
	 */
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	/**
	 * @return Returns the itemSetId.
	 */
	public Integer getItemSetId() {
		return itemSetId;
	}
	/**
	 * @param itemSetId The itemSetId to set.
	 */
	public void setItemSetId(Integer itemSetId) {
		this.itemSetId = itemSetId;
	}
	/**
	 * @return Returns the itemSetOrder.
	 */
	public Integer getItemSetOrder() {
		return itemSetOrder;
	}
	/**
	 * @param itemSetOrder The itemSetOrder to set.
	 */
	public void setItemSetOrder(Integer itemSetOrder) {
		this.itemSetOrder = itemSetOrder;
	}
	/**
	 * @return Returns the testAdminId.
	 */
	public Integer getTestAdminId() {
		return testAdminId;
	}
	/**
	 * @param testAdminId The testAdminId to set.
	 */
	public void setTestAdminId(Integer testAdminId) {
		this.testAdminId = testAdminId;
	}
	/**
	 * @return the sessionDefault
	 */
	public String getSessionDefault() {
		return sessionDefault;
	}
	/**
	 * @param sessionDefault the sessionDefault to set
	 */
	public void setSessionDefault(String sessionDefault) {
		this.sessionDefault = sessionDefault;
	}
} 
