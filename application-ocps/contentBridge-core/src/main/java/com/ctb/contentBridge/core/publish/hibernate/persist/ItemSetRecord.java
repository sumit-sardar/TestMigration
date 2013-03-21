package com.ctb.contentBridge.core.publish.hibernate.persist;

/**
 * @hibernate.class table="ITEM_SET"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ItemSetRecord {
    private String activationStatus;
    private Long breakTime;
    private Long createdBy;
    private java.util.Date createdDateTime;
    private String extCmsItemSetId;
    private String extEmsItemSetId;
    private String extTstItemSetId;
    private String grade;
    private Long itemSetCategoryId;
    private String itemSetDescription;
    private String itemSetDisplayName;
    private String itemSetForm;
    private Long itemSetId;
    private String itemSetLevel;
    private String itemSetName;
    private Long itemSetRuleId;
    private String itemSetType;
    private String maxGrade;
    private String mediaPath;
    private String minGrade;
    private Long originalCreatedBy;
    private Long ownerCustomerId;
    private String publishStatus;
    private String sample;
    private String subject;
    private Long timeLimit;
    private Long updatedBy;
    private java.util.Date updatedDateTime;
    private String version;
    private Long contentSize;

    /**
     * @hibernate.property
     * column="ACTIVATION_STATUS"
     * not-null="true"
     */
    public String getActivationStatus() {
        return activationStatus;
    }

    /**
     * @hibernate.property
     * column="BREAK_TIME"
     * not-null="true"
     */
    public Long getBreakTime() {
        return breakTime;
    }

    /**
     * @hibernate.property
     * column="CREATED_BY"
     * not-null="true"
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @hibernate.property
     * update="false"
     * column="CREATED_DATE_TIME"
     * not-null="true"
     */
    public java.util.Date getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * @hibernate.property
     * column="EXT_CMS_ITEM_SET_ID"
     * not-null="false"
     */
    public String getExtCmsItemSetId() {
        return extCmsItemSetId;
    }

    /**
     * @hibernate.property
     * column="EXT_EMS_ITEM_SET_ID"
     * not-null="false"
     */
    public String getExtEmsItemSetId() {
        return extEmsItemSetId;
    }

    /**
     * @hibernate.property
     * column="EXT_TST_ITEM_SET_ID"
     * not-null="false"
     */
    public String getExtTstItemSetId() {
        return extTstItemSetId;
    }

    /**
     * @hibernate.property
     * column="GRADE"
     * not-null="false"
     */
    public String getGrade() {
        return grade;
    }

    /**
     * @hibernate.property
     * column="ITEM_SET_CATEGORY_ID"
     * not-null="false"
     */
    public Long getItemSetCategoryId() {
        return itemSetCategoryId;
    }

    /**
     * @hibernate.property
     * column="ITEM_SET_DESCRIPTION"
     * not-null="false"
     */
    public String getItemSetDescription() {
        return itemSetDescription;
    }

    /**
     * @hibernate.property
     * column="ITEM_SET_DISPLAY_NAME"
     * not-null="true"
     */
    public String getItemSetDisplayName() {
        return itemSetDisplayName;
    }

    /**
     * @hibernate.property
     * column="ITEM_SET_FORM"
     * not-null="false"
     */
    public String getItemSetForm() {
        return itemSetForm;
    }

    /**
     * @hibernate.id
     * generator-class="sequence"
     * column="ITEM_SET_ID" 
     * 
     * @hibernate.generator-param
     * name="sequence"
     * value="SEQ_ITEM_SET_ID"
     */
    public Long getItemSetId() {
        return itemSetId;
    }

    /**
     * @hibernate.property
     * column="ITEM_SET_LEVEL"
     * not-null="false"
     */
    public String getItemSetLevel() {
        return itemSetLevel;
    }

    /**
     * @hibernate.property
     * column="ITEM_SET_NAME"
     * not-null="false"
     */
    public String getItemSetName() {
        return itemSetName;
    }

    /**
     * @hibernate.property
     * column="ITEM_SET_RULE_ID"
     * not-null="false"
     */
    public Long getItemSetRuleId() {
        return itemSetRuleId;
    }

    /**
     * @hibernate.property
     * column="ITEM_SET_TYPE"
     * not-null="true"
     */
    public String getItemSetType() {
        return itemSetType;
    }

    /**
     * @hibernate.property
     * column="MAX_GRADE"
     * not-null="false"
     */
    public String getMaxGrade() {
        return maxGrade;
    }

    /**
     * @hibernate.property
     * column="MEDIA_PATH"
     * not-null="false"
     */
    public String getMediaPath() {
        return mediaPath;
    }

    /**
     * @hibernate.property
     * column="MIN_GRADE"
     * not-null="false"
     */
    public String getMinGrade() {
        return minGrade;
    }

    /**
     * @hibernate.property
     * column="ORIGINAL_CREATED_BY"
     * not-null="false"
     */
    public Long getOriginalCreatedBy() {
        return originalCreatedBy;
    }

    /**
     * @hibernate.property
     * column="OWNER_CUSTOMER_ID"
     * not-null="false"
     */
    public Long getOwnerCustomerId() {
        return ownerCustomerId;
    }

    /**
     * @hibernate.property
     * column="PUBLISH_STATUS"
     * not-null="false"
     */
    public String getPublishStatus() {
        return publishStatus;
    }

    /**
     * @hibernate.property
     * column="SAMPLE"
     * not-null="true"
     */
    public String getSample() {
        return sample;
    }

    /**
     * @hibernate.property
     * column="SUBJECT"
     * not-null="false"
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @hibernate.property
     * column="TIME_LIMIT"
     * not-null="true"
     */
    public Long getTimeLimit() {
        return timeLimit;
    }

    /**
     * @hibernate.property
     * column="UPDATED_BY"
     * not-null="false"
     */
    public Long getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @hibernate.property
     * column="UPDATED_DATE_TIME"
     * not-null="false"
     */
    public java.util.Date getUpdatedDateTime() {
        return updatedDateTime;
    }

    /**
     * @hibernate.property
     * column="VERSION"
     * not-null="false"
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * @hibernate.property
     * column="CONTENT_SIZE"
     * not-null="false"
     */
    public Long getContentSize() {
        return contentSize;
    }
    
    public void setContentSize( Long contentSize_) {
        this.contentSize = contentSize_;
    }

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }

    public void setBreakTime(Long breakTime) {
        this.breakTime = breakTime;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDateTime(java.util.Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setExtCmsItemSetId(String extCmsItemSetId) {
        this.extCmsItemSetId = extCmsItemSetId;
    }

    public void setExtEmsItemSetId(String extEmsItemSetId) {
        this.extEmsItemSetId = extEmsItemSetId;
    }

    public void setExtTstItemSetId(String extTstItemSetId) {
        this.extTstItemSetId = extTstItemSetId;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setItemSetCategoryId(Long itemSetCategoryId) {
        this.itemSetCategoryId = itemSetCategoryId;
    }

    public void setItemSetDescription(String itemSetDescription) {
        this.itemSetDescription = itemSetDescription;
    }

    public void setItemSetDisplayName(String itemSetDisplayName) {
        this.itemSetDisplayName = itemSetDisplayName;
    }

    public void setItemSetForm(String itemSetForm) {
        this.itemSetForm = itemSetForm;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public void setItemSetLevel(String itemSetLevel) {
        this.itemSetLevel = itemSetLevel;
    }

    public void setItemSetName(String itemSetName) {
        this.itemSetName = itemSetName;
    }

    public void setItemSetRuleId(Long itemSetRuleId) {
        this.itemSetRuleId = itemSetRuleId;
    }

    public void setItemSetType(String itemSetType) {
        this.itemSetType = itemSetType;
    }

    public void setMaxGrade(String maxGrade) {
        this.maxGrade = maxGrade;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public void setMinGrade(String minGrade) {
        this.minGrade = minGrade;
    }

    public void setOriginalCreatedBy(Long originalCreatedBy) {
        this.originalCreatedBy = originalCreatedBy;
    }

    public void setOwnerCustomerId(Long ownerCustomerId) {
        this.ownerCustomerId = ownerCustomerId;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTimeLimit(Long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedDateTime(java.util.Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
