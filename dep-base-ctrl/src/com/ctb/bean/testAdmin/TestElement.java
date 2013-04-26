package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * Data bean representing the contents of the OAS.ITEM_SET table
 * 
 * @author Nate_Cohen
 */
public class TestElement extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer itemSetId;
    private String itemSetType;
    private Integer itemSetCategoryId;
    private String itemSetName; 
    private String itemSetLevel; 
    private String itemSetForm;
    private String minGrade;   
    private String maxGrade; 
    private String sample;
    private Integer timeLimit;
    private Integer breakTime;
    private String mediaPath;
    private String itemSetDisplayName; 
    private String itemSetDescription;
    private Integer itemSetRuleId;
    private String extEmsItemSetId;
    private String extCmsItemSetId;
    private Integer createdBy; 
    private Date createdDateTime;
    private Integer updatedBy;
    private Date updatedDateTime;
    private String activationStatus;
    private String version;
    private String subject;
    private String grade;
    private Integer ownerCustomerId;
    private String publishStatus;
    private Integer originalCreatedBy;
    private String extTstItemSetId;
    private String accessCode;
    private String [] forms;
    private String overrideFormAssignmentMethod;
    private Date overrideLoginStartDate;
    private String itemSetGroup;
    private String sessionDefault = "T";
    private Date overrideLoginEndDate;
    private String islocatorChecked = "";
    
    
    // Random Distractor 
    private String isRandomize;
    
    
    /**
	 * @return Returns the IsRandomize.
	 */
    
    public String getIsRandomize() {
		return isRandomize;
	}
    
    /**
	 * @param IsRandomize The IsRandomize to set.
	 */
	public void setIsRandomize(String isRandomize) {
		this.isRandomize = isRandomize;
	}
    
    /**
	 * @return Returns the overrideLoginStartDate.
	 */
	public Date getOverrideLoginStartDate() {
		return overrideLoginStartDate;
	}
	/**
	 * @param overrideLoginStartDate The overrideLoginStartDate to set.
	 */
	public void setOverrideLoginStartDate(Date overrideLoginStartDate) {
		this.overrideLoginStartDate = overrideLoginStartDate;
	}
    /**
	 * @return Returns the overrideFormAssignmentMethod.
	 */
	public String getOverrideFormAssignmentMethod() {
		return overrideFormAssignmentMethod;
	}
	/**
	 * @param overrideFormAssignmentMethod The overrideFormAssignmentMethod to set.
	 */
	public void setOverrideFormAssignmentMethod(String overrideFormAssignmentMethod) {
		this.overrideFormAssignmentMethod = overrideFormAssignmentMethod;
	}
    /**
	 * @return Returns the forms.
	 */
	public String[] getForms() {
		return forms;
	}
	/**
	 * @param forms The forms to set.
	 */
	public void setForms(String[] forms) {
		this.forms = forms;
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
	 * @return Returns the activationStatus.
	 */
	public String getActivationStatus() {
		return activationStatus;
	}
	/**
	 * @param activationStatus The activationStatus to set.
	 */
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	/**
	 * @return Returns the breakTime.
	 */
	public Integer getBreakTime() {
		return breakTime;
	}
	/**
	 * @param breakTime The breakTime to set.
	 */
	public void setBreakTime(Integer breakTime) {
		this.breakTime = breakTime;
	}
	/**
	 * @return Returns the createdBy.
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return Returns the createdDateTime.
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime The createdDateTime to set.
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	/**
	 * @return Returns the extCmsItemSetId.
	 */
	public String getExtCmsItemSetId() {
		return extCmsItemSetId;
	}
	/**
	 * @param extCmsItemSetId The extCmsItemSetId to set.
	 */
	public void setExtCmsItemSetId(String extCmsItemSetId) {
		this.extCmsItemSetId = extCmsItemSetId;
	}
	/**
	 * @return Returns the extEmsItemSetId.
	 */
	public String getExtEmsItemSetId() {
		return extEmsItemSetId;
	}
	/**
	 * @param extEmsItemSetId The extEmsItemSetId to set.
	 */
	public void setExtEmsItemSetId(String extEmsItemSetId) {
		this.extEmsItemSetId = extEmsItemSetId;
	}
	/**
	 * @return Returns the extTstItemSetId.
	 */
	public String getExtTstItemSetId() {
		return extTstItemSetId;
	}
	/**
	 * @param extTstItemSetId The extTstItemSetId to set.
	 */
	public void setExtTstItemSetId(String extTstItemSetId) {
		this.extTstItemSetId = extTstItemSetId;
	}
	/**
	 * @return Returns the grade.
	 */
	public String getGrade() {
		return grade;
	}
	/**
	 * @param grade The grade to set.
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	/**
	 * @return Returns the itemSetCategoryId.
	 */
	public Integer getItemSetCategoryId() {
		return itemSetCategoryId;
	}
	/**
	 * @param itemSetCategoryId The itemSetCategoryId to set.
	 */
	public void setItemSetCategoryId(Integer itemSetCategoryId) {
		this.itemSetCategoryId = itemSetCategoryId;
	}
	/**
	 * @return Returns the itemSetDescription.
	 */
	public String getItemSetDescription() {
		return itemSetDescription;
	}
	/**
	 * @param itemSetDescription The itemSetDescription to set.
	 */
	public void setItemSetDescription(String itemSetDescription) {
		this.itemSetDescription = itemSetDescription;
	}
	/**
	 * @return Returns the itemSetDisplayName.
	 */
	public String getItemSetDisplayName() {
		return itemSetDisplayName;
	}
	/**
	 * @param itemSetDisplayName The itemSetDisplayName to set.
	 */
	public void setItemSetDisplayName(String itemSetDisplayName) {
		this.itemSetDisplayName = itemSetDisplayName;
	}
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
	 * @return Returns the itemSetLevel.
	 */
	public String getItemSetLevel() {
		return itemSetLevel;
	}
	/**
	 * @param itemSetLevel The itemSetLevel to set.
	 */
	public void setItemSetLevel(String itemSetLevel) {
		this.itemSetLevel = itemSetLevel;
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
	 * @return Returns the itemSetRuleId.
	 */
	public Integer getItemSetRuleId() {
		return itemSetRuleId;
	}
	/**
	 * @param itemSetRuleId The itemSetRuleId to set.
	 */
	public void setItemSetRuleId(Integer itemSetRuleId) {
		this.itemSetRuleId = itemSetRuleId;
	}
	/**
	 * @return Returns the itemSetType.
	 */
	public String getItemSetType() {
		return itemSetType;
	}
	/**
	 * @param itemSetType The itemSetType to set.
	 */
	public void setItemSetType(String itemSetType) {
		this.itemSetType = itemSetType;
	}
	/**
	 * @return Returns the maxGrade.
	 */
	public String getMaxGrade() {
		return maxGrade;
	}
	/**
	 * @param maxGrade The maxGrade to set.
	 */
	public void setMaxGrade(String maxGrade) {
		this.maxGrade = maxGrade;
	}
	/**
	 * @return Returns the mediaPath.
	 */
	public String getMediaPath() {
		return mediaPath;
	}
	/**
	 * @param mediaPath The mediaPath to set.
	 */
	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}
	/**
	 * @return Returns the minGrade.
	 */
	public String getMinGrade() {
		return minGrade;
	}
	/**
	 * @param minGrade The minGrade to set.
	 */
	public void setMinGrade(String minGrade) {
		this.minGrade = minGrade;
	}
	/**
	 * @return Returns the originalCreatedBy.
	 */
	public Integer getOriginalCreatedBy() {
		return originalCreatedBy;
	}
	/**
	 * @param originalCreatedBy The originalCreatedBy to set.
	 */
	public void setOriginalCreatedBy(Integer originalCreatedBy) {
		this.originalCreatedBy = originalCreatedBy;
	}
	/**
	 * @return Returns the ownerCustomerId.
	 */
	public Integer getOwnerCustomerId() {
		return ownerCustomerId;
	}
	/**
	 * @param ownerCustomerId The ownerCustomerId to set.
	 */
	public void setOwnerCustomerId(Integer ownerCustomerId) {
		this.ownerCustomerId = ownerCustomerId;
	}
	/**
	 * @return Returns the publishStatus.
	 */
	public String getPublishStatus() {
		return publishStatus;
	}
	/**
	 * @param publishStatus The publishStatus to set.
	 */
	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}
	/**
	 * @return Returns the sample.
	 */
	public String getSample() {
		return sample;
	}
	/**
	 * @param sample The sample to set.
	 */
	public void setSample(String sample) {
		this.sample = sample;
	}
	/**
	 * @return Returns the subject.
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return Returns the timeLimit.
	 */
	public Integer getTimeLimit() {
		return timeLimit;
	}
	/**
	 * @param timeLimit The timeLimit to set.
	 */
	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}
	/**
	 * @return Returns the updatedBy.
	 */
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy The updatedBy to set.
	 */
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @return Returns the updatedDateTime.
	 */
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	/**
	 * @param updatedDateTime The updatedDateTime to set.
	 */
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the itemSetGroup
	 */
	public String getItemSetGroup() {
		return itemSetGroup;
	}
	/**
	 * @param itemSetGroup the itemSetGroup to set
	 */
	public void setItemSetGroup(String itemSetGroup) {
		this.itemSetGroup = itemSetGroup;
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

	
	/**
	 * @return the overrideLoginEndDate
	 */
	public Date getOverrideLoginEndDate() {
		return overrideLoginEndDate;
	}

	
	/**
	 * @param overrideLoginEndDate the overrideLoginEndDate to set
	 */
	public void setOverrideLoginEndDate(Date overrideLoginEndDate) {
		this.overrideLoginEndDate = overrideLoginEndDate;
	}

	/**
	 * @return the islocatorChecked
	 */
	public String getIslocatorChecked() {
		return islocatorChecked;
	}

	/**
	 * @param islocatorChecked the islocatorChecked to set
	 */
	public void setIslocatorChecked(String islocatorChecked) {
		this.islocatorChecked = islocatorChecked;
	}

	
} 
