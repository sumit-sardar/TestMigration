package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * TABERecommendedLevel 
 * 
 * @author John_Wang
 */


public class TABERecommendedLevel extends CTBBean {
	static final long serialVersionUID = 1L;
    
    private Integer itemSetId;
    private String itemSetName;
    private Date completedDate;
    private Integer testAdminId;
    private String testAdminName;
    private String recommendedLevel;
	/**
	 * @return the completedDate
	 */
	public Date getCompletedDate() {
		return completedDate;
	}
	/**
	 * @param completedDate the completedDate to set
	 */
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	/**
	 * @return the itemSetId
	 */
	public Integer getItemSetId() {
		return itemSetId;
	}
	/**
	 * @param itemSetId the itemSetId to set
	 */
	public void setItemSetId(Integer itemSetId) {
		this.itemSetId = itemSetId;
	}
	/**
	 * @return the itemSetName
	 */
	public String getItemSetName() {
		return itemSetName;
	}
	/**
	 * @param itemSetName the itemSetName to set
	 */
	public void setItemSetName(String itemSetName) {
		this.itemSetName = itemSetName;
	}
	/**
	 * @return the recommendedLevel
	 */
	public String getRecommendedLevel() {
		return recommendedLevel;
	}
	/**
	 * @param recommendedLevel the recommendedLevel to set
	 */
	public void setRecommendedLevel(String recommendedLevel) {
		this.recommendedLevel = recommendedLevel;
	}
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
	 * @return the testAdminName
	 */
	public String getTestAdminName() {
		return testAdminName;
	}
	/**
	 * @param testAdminName the testAdminName to set
	 */
	public void setTestAdminName(String testAdminName) {
		this.testAdminName = testAdminName;
	}
}

    
