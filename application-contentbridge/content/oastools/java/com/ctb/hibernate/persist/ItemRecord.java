package com.ctb.hibernate.persist;

import com.ctb.hibernate.Persistent;

/**
 * @hibernate.class table="ITEM"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ItemRecord extends Persistent{
    private String activationStatus;
    private String correctAnswer;
    private Long createdBy;
    private java.util.Date createdDateTime;
    private String description;
    private String extStimulusId;
    private String extStimulusTitle;
    private String itemDisplayName;
    private String itemId;
    private String itemType;
    private String name;
    private String templateId;
	private String thinkID;
    private Long updatedBy;
    private java.util.Date updatedDateTime;
    private String version;
    private String onlineCR = "F";
	private String ExternalID;
	private String ExternalSystem;
	private String answerArea;
	private Long griddedColumns;

	/**
     * @hibernate.property
     * column="ANSWER_AREA"
     * not-null="false"
     */
    public String getAnswerArea() {
        return answerArea;
    }
    
    /**
     * @hibernate.property
     * column="GRIDDED_COLUMNS"
     * not-null="false"
     */
    public Long getGriddedColumns() {
        return griddedColumns;
    }
    
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
     * column="CORRECT_ANSWER"
     * not-null="false"
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * @hibernate.property
     * column="CREATED_BY"
     * not-null="false"
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
     * column="DESCRIPTION"
     * not-null="false"
     */
    public String getDescription() {
        return description;
    }

    /**
     * @hibernate.property
     * column="EXT_STIMULUS_ID"
     * not-null="false"
     */
    public String getExtStimulusId() {
        return extStimulusId;
    }

    /**
     * @hibernate.property
     * column="EXT_STIMULUS_TITLE"
     * not-null="false"
     */
    public String getExtStimulusTitle() {
        return extStimulusTitle;
    }

    /**
     * @hibernate.property
     * column="ITEM_DISPLAY_NAME"
     * not-null="false"
     */
    public String getItemDisplayName() {
        return itemDisplayName;
    }

    /** @hibernate.id generator-class="assigned" column="ITEM_ID" */
    public String getItemId() {
        return itemId;
    }

    /**
     * @hibernate.property
     * column="ITEM_TYPE"
     * not-null="true"
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * @hibernate.property
     * column="NAME"
     * not-null="false"
     */
    public String getName() {
        return name;
    }

    /**
     * @hibernate.property
     * column="TEMPLATE_ID"
     * not-null="false"
     */
    public String getTemplateId() {
        return templateId;
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
	 * column="THINK_CODE"
	 * not-null="false"
	 */
	public String getThinkID() {
		return this.thinkID;
	}

	/**
     * @hibernate.property
     * column="External_ID"
     * not-null="false"
     */
    public String getExternalID() {
        return ExternalID;
    }

	/**
     * @hibernate.property
     * column="External_System"
     * not-null="false"
     */
    public String getExternalSystem() {
        return ExternalSystem;
    }

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDateTime(java.util.Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExtStimulusId(String extStimulusId) {
        this.extStimulusId = extStimulusId;
    }

    public void setExtStimulusTitle(String extStimulusTitle) {
        this.extStimulusTitle = extStimulusTitle;
    }

    public void setItemDisplayName(String itemDisplayName) {
        this.itemDisplayName = itemDisplayName;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public void setThinkID(String thinkID) {
		this.thinkID = thinkID;
    }

    public void setGriddedColumns(Long griddedColumns) {
		this.griddedColumns = griddedColumns;
    }
    
    public void setAnswerArea(String answerArea) {
		this.answerArea = answerArea;
    }
    
    /**
     * @hibernate.property
     * column="ONLINE_CR"
     * not-null="true"
     */
    public String getOnlineCR() {
        return onlineCR;
    }

    public void setOnlineCR(String onlineCR) {
        this.onlineCR = onlineCR;
    }

	public void setExternalSystem( String ExternalSystem_) {
		if ( !"na".equals( ExternalSystem_ ))
			this.ExternalSystem = ExternalSystem_;
    }

	public void setExternalID( String ExternalID_ ) {
        this.ExternalID = ExternalID_;
    }
}
