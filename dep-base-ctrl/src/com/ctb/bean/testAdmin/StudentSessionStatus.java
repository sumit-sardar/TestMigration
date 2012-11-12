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
    private String itemSetLevel;    //Added for defect - 64622 
    private String totalItem;
    private String itemAnswered;
    private String timeSpent;
    private String studentName;
    private String studentLoginName;
    private String externalStudentId;
    private Integer studentId;
    private String org_name;
	private String testAccessCode;
	private Integer orgNodeId;
	private String completionDate;
    
	
	//START- ADDED for LLO -109
	private String testExemptions;
	private String absent;
	private String invalidationReason;
	
	//START - TABE BAUM 020 Form Recommendation 
	private String testSessionName;
	private String productName;
	private String itemSetType;
	private Integer productId;
	private Integer recommendedProductId;
	private String recommendedProductName;
	//END - TABE BAUM 020 Form Recommendation 
	
    
    /**
	 * @return the testExemptions
	 */
	public String getTestExemptions() {
		return testExemptions;
	}
	/**
	 * @param testExemptions the testExemptions to set
	 */
	public void setTestExemptions(String testExemptions) {
		this.testExemptions = testExemptions;
	}
	/**
	 * @return the absent
	 */
	public String getAbsent() {
		return absent;
	}
	/**
	 * @param absent the absent to set
	 */
	public void setAbsent(String absent) {
		this.absent = absent;
	}
	
	//END- ADDED for LLO -109
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
	/**
	 * @return the studentName
	 */
	public String getStudentName() {
		return studentName;
	}
	/**
	 * @param studentName the studentName to set
	 */
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	/**
	 * @return the studentLoginName
	 */
	public String getStudentLoginName() {
		return studentLoginName;
	}
	/**
	 * @param studentLoginName the studentLoginName to set
	 */
	public void setStudentLoginName(String studentLoginName) {
		this.studentLoginName = studentLoginName;
	}
	/**
	 * @return the org_name
	 */
	public String getOrg_name() {
		return org_name;
	}
	/**
	 * @param org_name the org_name to set
	 */
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	
	/**
	 * @return the studentId
	 */
	public Integer getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return the externalStudentId
	 */
	public String getExternalStudentId() {
		return externalStudentId;
	}
	/**
	 * @param externalStudentId the externalStudentId to set
	 */
	public void setExternalStudentId(String externalStudentId) {
		this.externalStudentId = externalStudentId;
	} 
	/**
     * @return the testAccessCode
	 */
	public String getTestAccessCode() {
		return testAccessCode;
	}
	/**
	 * @param testAccessCode the testAccessCode to set
	 */
	public void setTestAccessCode(String testAccessCode) {
		this.testAccessCode = testAccessCode;
	}
	/**
	 * @return the orgNodeId
	 */
	public Integer getOrgNodeId() {
		return orgNodeId;
	}
	/**
	 * @param orgNodeId the orgNodeId to set
	 */
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	/**
	 * @return the itemSetLevel
	 */
	public String getItemSetLevel() {
		return itemSetLevel;
	}
	/**
	 * @param itemSetLevel the itemSetLevel to set
	 */
	public void setItemSetLevel(String itemSetLevel) {
		this.itemSetLevel = itemSetLevel;
	}
	/**
	 * @return the testSessionName
	 */
	public String getTestSessionName() {
		return testSessionName;
	}
	/**
	 * @param testSessionName the testSessionName to set
	 */
	public void setTestSessionName(String testSessionName) {
		this.testSessionName = testSessionName;
	}
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return the itemSetType
	 */
	public String getItemSetType() {
		return itemSetType;
	}
	/**
	 * @param itemSetType the itemSetType to set
	 */
	public void setItemSetType(String itemSetType) {
		this.itemSetType = itemSetType;
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
	 * @return the recommendedProductId
	 */
	public Integer getRecommendedProductId() {
		return recommendedProductId;
	}
	/**
	 * @param recommendedProductId the recommendedProductId to set
	 */
	public void setRecommendedProductId(Integer recommendedProductId) {
		this.recommendedProductId = recommendedProductId;
	}
	/**
	 * @return the recommendedProductName
	 */
	public String getRecommendedProductName() {
		return recommendedProductName;
	}
	/**
	 * @param recommendedProductName the recommendedProductName to set
	 */
	public void setRecommendedProductName(String recommendedProductName) {
		this.recommendedProductName = recommendedProductName;
	}
	/**
	 * @return the completionDate
	 */
	public String getCompletionDate() {
		return completionDate;
	}
	/**
	 * @param completionDate the completionDate to set
	 */
	public void setCompletionDate(String completionDate) {
		this.completionDate = completionDate;
	}
	public String getInvalidationReason() {
		return invalidationReason;
	}
	public void setInvalidationReason(String invalidationReason) {
		this.invalidationReason = invalidationReason;
	}
	
} 
