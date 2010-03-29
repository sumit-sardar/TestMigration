package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * Data bean representing the contents of the OAS.STUDENT_ITEM_SET_STATUS table
 * 
 * @author Nate_Cohen
 */
public class StudentSessionStatus extends CTBBean
{
    static final long serialVersionUID = 1L;
    private Integer testRosterId;
    private Integer itemSetId;
    private String completionStatus;
    private Date startDateTime;
    private Date completionDateTime;
    private String validationStatus;
    private Integer validationUpdatedBy;
    private Date validationUpdatedDateTime;
    private String validationUpdatedNote;
    private String timeExpired;
    private Integer itemSetOrder;
    private Integer rawScore;	
    private Integer maxScore;	
    private Integer unscored;	
    private String recommendedLevel;
    private String customerFlagStatus;
    private String itemSetName;
    private String totalItem;
    private String itemAnswered;
    private String timeSpent;
    private String studentName;
    private String studentLoginName;
    private String studentId;
    private String org_name;
    
    
    /**
	 * @return Returns the maxScore.
	 */  
	public Integer getMaxScore() {
		return maxScore;
	}
	/**
	 * @param maxScore The maxScore to set.
	 */
	public void setMaxScore(Integer maxScore) {
		this.maxScore = maxScore;
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
	 * @return Returns the itemAnswered.
	 */  
	public String getTotalItem() {
		return totalItem;
	}
	/**
	 * @param itemAnswered The itemAnswered to set.
	 */
	public void setTotalItem(String totalItem) {
		this.totalItem = totalItem;
	}
	/**
	 * @return Returns the itemAnswered.
	 */  
	public String getItemAnswered() {
		return itemAnswered;
	}
	/**
	 * @param itemAnswered The itemAnswered to set.
	 */
	public void setItemAnswered(String itemAnswered) {
		this.itemAnswered = itemAnswered;
	}
	/**
	 * @return Returns the itemAnswered.
	 */  
	public String getTimeSpent() {
		return timeSpent;
	}
	/**
	 * @param timeSpent The timeSpent to set.
	 */
	public void setTimeSpent(String timeSpent) {
		this.timeSpent = timeSpent;
	}
	/**
	 * @return Returns the rawScore.
	 */
	public Integer getRawScore() {
		return rawScore;
	}
	/**
	 * @param rawScore The rawScore to set.
	 */
	public void setRawScore(Integer rawScore) {
		this.rawScore = rawScore;
	}
	/**
	 * @return Returns the recommendedLevel.
	 */
	public String getRecommendedLevel() {
		return recommendedLevel;
	}
	/**
	 * @param recommendedLevel The recommendedLevel to set.
	 */
	public void setRecommendedLevel(String recommendedLevel) {
		this.recommendedLevel = recommendedLevel;
	}
	/**
	 * @return Returns unscored.
	 */
	public Integer getUnscored() {
		return unscored;
	}
	/**
	 * @param unscored The unscored to set.
	 */
	public void setUnscored(Integer unscored) {
		this.unscored = unscored;
	}
	/**
	 * @return Returns the completionDateTime.
	 */
	public Date getCompletionDateTime() {
		return completionDateTime;
	}
	/**
	 * @param completionDateTime The completionDateTime to set.
	 */
	public void setCompletionDateTime(Date completionDateTime) {
		this.completionDateTime = completionDateTime;
	}
	/**
	 * @return Returns the completionStatus.
	 */
	public String getCompletionStatus() {
		return completionStatus;
	}
	/**
	 * @param completionStatus The completionStatus to set.
	 */
	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
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
	 * @return Returns the startDateTime.
	 */
	public Date getStartDateTime() {
		return startDateTime;
	}
	/**
	 * @param startDateTime The startDateTime to set.
	 */
	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}
	/**
	 * @return Returns the testRosterId.
	 */
	public Integer getTestRosterId() {
		return testRosterId;
	}
	/**
	 * @param testRosterId The testRosterId to set.
	 */
	public void setTestRosterId(Integer testRosterId) {
		this.testRosterId = testRosterId;
	}
	/**
	 * @return Returns the timeExpired.
	 */
	public String getTimeExpired() {
		return timeExpired;
	}
	/**
	 * @param timeExpired The timeExpired to set.
	 */
	public void setTimeExpired(String timeExpired) {
		this.timeExpired = timeExpired;
	}
	/**
	 * @return Returns the validationStatus.
	 */
	public String getValidationStatus() {
		return validationStatus;
	}
	/**
	 * @param validationStatus The validationStatus to set.
	 */
	public void setValidationStatus(String validationStatus) {
		this.validationStatus = validationStatus;
	}
	/**
	 * @return Returns the validationUpdatedBy.
	 */
	public Integer getValidationUpdatedBy() {
		return validationUpdatedBy;
	}
	/**
	 * @param validationUpdatedBy The validationUpdatedBy to set.
	 */
	public void setValidationUpdatedBy(Integer validationUpdatedBy) {
		this.validationUpdatedBy = validationUpdatedBy;
	}
	/**
	 * @return Returns the validationUpdatedDateTime.
	 */
	public Date getValidationUpdatedDateTime() {
		return validationUpdatedDateTime;
	}
	/**
	 * @param validationUpdatedDateTime The validationUpdatedDateTime to set.
	 */
	public void setValidationUpdatedDateTime(Date validationUpdatedDateTime) {
		this.validationUpdatedDateTime = validationUpdatedDateTime;
	}
	/**
	 * @return Returns the validationUpdatedNote.
	 */
	public String getValidationUpdatedNote() {
		return validationUpdatedNote;
	}
	/**
	 * @param validationUpdatedNote The validationUpdatedNote to set.
	 */
	public void setValidationUpdatedNote(String validationUpdatedNote) {
		this.validationUpdatedNote = validationUpdatedNote;
	}
     /**
     * @return Returns The customerFlagStatus .
     */
    public String getCustomerFlagStatus() {
            return customerFlagStatus;
    }
     /**
     * @param customerFlagStatus The customerFlagStatus to set.
     */
    public void setCustomerFlagStatus(String customerFlagStatus) {
            this.customerFlagStatus = customerFlagStatus;
    }

} 
