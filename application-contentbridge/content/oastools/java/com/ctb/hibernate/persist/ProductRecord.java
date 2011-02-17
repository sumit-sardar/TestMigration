package com.ctb.hibernate.persist;

/**
 * @hibernate.class table="PRODUCT"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ProductRecord {
    private String activationStatus;
    private Long contentAreaLevel;
    private Long createdBy;
    private java.util.Date createdDateTime;
    private String extProductId;
    private String internalDisplayName;
    private Long parentProductId;
    private Long previewItemSetLevel;
    private String productDescription;
    private Long productId;
    private String productName;
    private String productType;
    private Long scoringItemSetLevel;
    private Long secScoringItemSetLevel;
    private Long updatedBy;
    private java.util.Date updatedDateTime;
    private String version;

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
     * column="CONTENT_AREA_LEVEL"
     * not-null="false"
     */
    public Long getContentAreaLevel() {
        return contentAreaLevel;
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
     * column="EXT_PRODUCT_ID"
     * not-null="false"
     */
    public String getExtProductId() {
        return extProductId;
    }

    /**
     * @hibernate.property
     * column="INTERNAL_DISPLAY_NAME"
     * not-null="false"
     */
    public String getInternalDisplayName() {
        return internalDisplayName;
    }

    /**
     * @hibernate.property
     * column="PARENT_PRODUCT_ID"
     * not-null="false"
     */
    public Long getParentProductId() {
        return parentProductId;
    }

    /**
     * @hibernate.property
     * column="PREVIEW_ITEM_SET_LEVEL"
     * not-null="false"
     */
    public Long getPreviewItemSetLevel() {
        return previewItemSetLevel;
    }

    /**
     * @hibernate.property
     * column="PRODUCT_DESCRIPTION"
     * not-null="false"
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * @hibernate.id
     * generator-class="sequence"
     * column="PRODUCT_ID" 
     * 
     * @hibernate.generator-param
     * name="sequence"
     * value="SEQ_PRODUCT_ID"
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @hibernate.property
     * column="PRODUCT_NAME"
     * not-null="true"
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @hibernate.property
     * column="PRODUCT_TYPE"
     * not-null="true"
     */
    public String getProductType() {
        return productType;
    }

    /**
     * @hibernate.property
     * column="SCORING_ITEM_SET_LEVEL"
     * not-null="false"
     */
    public Long getScoringItemSetLevel() {
        return scoringItemSetLevel;
    }

    /**
     * @hibernate.property
     * column="SEC_SCORING_ITEM_SET_LEVEL"
     * not-null="false"
     */
    public Long getSecScoringItemSetLevel() {
        return secScoringItemSetLevel;
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

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }

    public void setContentAreaLevel(Long contentAreaLevel) {
        this.contentAreaLevel = contentAreaLevel;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDateTime(java.util.Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setExtProductId(String extProductId) {
        this.extProductId = extProductId;
    }

    public void setInternalDisplayName(String internalDisplayName) {
        this.internalDisplayName = internalDisplayName;
    }

    public void setParentProductId(Long parentProductId) {
        this.parentProductId = parentProductId;
    }

    public void setPreviewItemSetLevel(Long previewItemSetLevel) {
        this.previewItemSetLevel = previewItemSetLevel;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setScoringItemSetLevel(Long scoringItemSetLevel) {
        this.scoringItemSetLevel = scoringItemSetLevel;
    }

    public void setSecScoringItemSetLevel(Long secScoringItemSetLevel) {
        this.secScoringItemSetLevel = secScoringItemSetLevel;
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
