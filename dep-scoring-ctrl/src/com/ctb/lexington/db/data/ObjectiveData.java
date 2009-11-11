package com.ctb.lexington.db.data;

import com.ctb.lexington.db.record.Persistent;

/*
 * Generated class: do NOT format or modify in any way!
 */
public class ObjectiveData implements Persistent {
    public static final String TABLE_NAME = "ITEM_SET";

    public static final String ITEM_SET_ID = "ITEM_SET_ID";
    public static final String ITEM_SET_TYPE = "ITEM_SET_TYPE";
    public static final String ITEM_SET_NAME = "ITEM_SET_NAME";
    public static final String MIN_GRADE = "MIN_GRADE";
    public static final String VERSION = "VERSION";
    public static final String MAX_GRADE = "MAX_GRADE";
    public static final String ITEM_SET_LEVEL = "ITEM_SET_LEVEL";
    public static final String SUBJECT = "SUBJECT";
    public static final String GRADE = "GRADE";
    public static final String SAMPLE = "SAMPLE";
    public static final String MEDIA_PATH = "MEDIA_PATH";
    public static final String TIME_LIMIT = "TIME_LIMIT";
    public static final String BREAK_TIME = "BREAK_TIME";
    public static final String EXT_EMS_ITEM_SET_ID = "EXT_EMS_ITEM_SET_ID";
    public static final String EXT_CMS_ITEM_SET_ID = "EXT_CMS_ITEM_SET_ID";
    public static final String ITEM_SET_DISPLAY_NAME = "ITEM_SET_DISPLAY_NAME";
    public static final String ITEM_SET_DESCRIPTION = "ITEM_SET_DESCRIPTION";
    public static final String ITEM_SET_RULE_ID = "ITEM_SET_RULE_ID";
    public static final String CREATED_DATE_TIME = "CREATED_DATE_TIME";
    public static final String CREATED_BY = "CREATED_BY";

	public static final String PRIMARY = "PRIMARY";
    public static final String ACTIVATION_STATUS = "ACTIVATION_STATUS";
    public static final String ITEM_SET_CATEGORY_ID = "ITEM_SET_CATEGORY_ID";

	public static final String SECONDARY = "SECONDARY";
    public static final String OWNER_CUSTOMER_ID = "OWNER_CUSTOMER_ID";
    public static final String UPDATED_BY = "UPDATED_BY";
    public static final String UPDATED_DATE_TIME = "UPDATED_DATE_TIME";
    public static final String ITEM_SET_FORM = "ITEM_SET_FORM";
    public static final String PUBLISH_STATUS = "PUBLISH_STATUS";
    public static final String ORIGINAL_CREATED_BY = "ORIGINAL_CREATED_BY";
    public static final String EXT_TST_ITEM_SET_ID = "EXT_TST_ITEM_SET_ID";

    private String reportingLevel;
    private String itemId;
    private Long itemSetId;
    private String itemSetType;
    private String itemSetName;
    private String minGrade;
    private String version;
    private String maxGrade;
    private String itemSetLevel;
    private String subject;
    private String grade;
    private String sample;
    private String mediaPath;
    private Long timeLimit;
    private Long breakTime;
    private String extEmsItemSetId;
    private String extCmsItemSetId;
    private String itemSetDisplayName;
    private String itemSetDescription;
    private Long itemSetRuleId;
    private java.util.Date createdDateTime;
    private Long createdBy;
    private String activationStatus;
    private Long itemSetCategoryId;
    private Long ownerCustomerId;
    private Long updatedBy;
    private java.util.Date updatedDateTime;
    private String itemSetForm;
    private String publishStatus;
    private Long originalCreatedBy;
    private String extTstItemSetId;

    public ObjectiveData () {
    	this.reportingLevel = PRIMARY;
    }

    public ObjectiveData (Long itemSetId) {
    	this();
    	this.itemSetId = itemSetId;
    }

    public ObjectiveData (Long itemSetId, String reportingLevel) {
    	this(itemSetId);
    	this.reportingLevel = reportingLevel;
    }

    public Long getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public String getItemSetType() {
        return itemSetType;
    }

    public void setItemSetType(String itemSetType) {
        this.itemSetType = itemSetType;
    }

    public String getItemSetName() {
        return itemSetName;
    }

    public void setItemSetName(String itemSetName) {
        this.itemSetName = itemSetName;
    }

    public String getMinGrade() {
        return minGrade;
    }

    public void setMinGrade(String minGrade) {
        this.minGrade = minGrade;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMaxGrade() {
        return maxGrade;
    }

    public void setMaxGrade(String maxGrade) {
        this.maxGrade = maxGrade;
    }

    public String getItemSetLevel() {
        return itemSetLevel;
    }

    public void setItemSetLevel(String itemSetLevel) {
        this.itemSetLevel = itemSetLevel;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public Long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Long getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(Long breakTime) {
        this.breakTime = breakTime;
    }

    public String getExtEmsItemSetId() {
        return extEmsItemSetId;
    }

    public void setExtEmsItemSetId(String extEmsItemSetId) {
        this.extEmsItemSetId = extEmsItemSetId;
    }

    public String getExtCmsItemSetId() {
        return extCmsItemSetId;
    }

    public void setExtCmsItemSetId(String extCmsItemSetId) {
        this.extCmsItemSetId = extCmsItemSetId;
    }

    public String getItemSetDisplayName() {
        return itemSetDisplayName;
    }

    public void setItemSetDisplayName(String itemSetDisplayName) {
        this.itemSetDisplayName = itemSetDisplayName;
    }

    public String getItemSetDescription() {
        return itemSetDescription;
    }

    public void setItemSetDescription(String itemSetDescription) {
        this.itemSetDescription = itemSetDescription;
    }

    public Long getItemSetRuleId() {
        return itemSetRuleId;
    }

    public void setItemSetRuleId(Long itemSetRuleId) {
        this.itemSetRuleId = itemSetRuleId;
    }

    public java.util.Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(java.util.Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }

    public Long getItemSetCategoryId() {
        return itemSetCategoryId;
    }

    public void setItemSetCategoryId(Long itemSetCategoryId) {
        this.itemSetCategoryId = itemSetCategoryId;
    }

    public Long getOwnerCustomerId() {
        return ownerCustomerId;
    }

    public void setOwnerCustomerId(Long ownerCustomerId) {
        this.ownerCustomerId = ownerCustomerId;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public java.util.Date getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(java.util.Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getItemSetForm() {
        return itemSetForm;
    }

    public void setItemSetForm(String itemSetForm) {
        this.itemSetForm = itemSetForm;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Long getOriginalCreatedBy() {
        return originalCreatedBy;
    }

    public void setOriginalCreatedBy(Long originalCreatedBy) {
        this.originalCreatedBy = originalCreatedBy;
    }

    public String getExtTstItemSetId() {
        return extTstItemSetId;
    }

    public void setExtTstItemSetId(String extTstItemSetId) {
        this.extTstItemSetId = extTstItemSetId;
    }
	/**
	 * @return Returns the item_id.
	 */
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return Returns the reportingLevel.
	 */
	public String getReportingLevel() {
		return reportingLevel;
	}
	/**
	 * @param reportingLevel The reportingLevel to set.
	 */
	public void setReportingLevel(String reportingLevel) {
		this.reportingLevel = reportingLevel;
	}
}