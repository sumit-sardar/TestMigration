package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * ActiveTest.java
 * @author Nate_Cohen
 *
 * Data bean representing tests with active sessions for a user.
 * Contains a list of all subtests (TD item sets) belonging to
 * the test.
 */
public class ActiveTest extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer itemSetId;
    private String itemSetName;
    private String itemSetLevel;
    private String itemSetGrade;
    private Integer contentSize;
    private Date loginStartDate;
    private Date loginEndDate;
    private TestElement [] subtests;
    /**
	 * @return Returns the itemSetLevel.
	 */
	public String getItemSetLevel() {
		return this.itemSetLevel;
	}
	/**
	 * @param itemSetLevel The itemSetLevel to set.
	 */
	public void setItemSetLevel(String itemSetLevel) {
		this.itemSetLevel = itemSetLevel;
	}
	/**
	 * @return Returns the itemSetGrade.
	 */
	public String getItemSetGrade() {
		return this.itemSetGrade;
	}
	/**
	 * @param itemSetGrade The itemSetGrade to set.
	 */
	public void setItemSetGrade(String itemSetGrade) {
		this.itemSetGrade = itemSetGrade;
	}
    /**
	 * @return Returns the subtests.
	 */
	public TestElement [] getSubtests() {
		return this.subtests;
	}
	/**
	 * @param subtests The subtests to set.
	 */
	public void setSubtests(TestElement [] subtests) {
		this.subtests = subtests;
	}
	/**
	 * @return Returns the contentSize.
	 */
	public Integer getContentSize() {
		return contentSize;
	}
	/**
	 * @param contentSize The contentSize to set.
	 */
	public void setContentSize(Integer contentSize) {
		this.contentSize = contentSize;
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
	 * @return Returns the itemSetName.
	 */
	public String getItemSetName() {
		return itemSetName;
	}
	/**
	 * @param itemSetName The itemSetName to set.
	 */
	public void setItemSetName(String itemSetName) {
		this.itemSetName = itemSetName;
	}
	/**
	 * @return Returns the loginEndDate.
	 */
	public Date getLoginEndDate() {
		return loginEndDate;
	}
	/**
	 * @param loginEndDate The loginEndDate to set.
	 */
	public void setLoginEndDate(Date loginEndDate) {
		this.loginEndDate = loginEndDate;
	}
	/**
	 * @return Returns the loginStartDate.
	 */
	public Date getLoginStartDate() {
		return loginStartDate;
	}
	/**
	 * @param loginStartDate The loginStartDate to set.
	 */
	public void setLoginStartDate(Date loginStartDate) {
		this.loginStartDate = loginStartDate;
	}
    
    
} 
