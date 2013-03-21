package com.ctb.contentBridge.core.publish.hibernate.persist;

/**
 * @hibernate.class table="ITEM_SET_CATEGORY"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ItemSetCategoryRecord {
    private Long createdBy;
    private java.util.Date createdDateTime;
    private Long frameworkProductId;
    private Long itemSetCategoryId;
    private Long itemSetCategoryLevel;
    private String itemSetCategoryName;
    private Long updatedBy;
    private java.util.Date updatedDateTime;

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
     * column="FRAMEWORK_PRODUCT_ID"
     * not-null="true"
     */
    public Long getFrameworkProductId() {
        return frameworkProductId;
    }

    /**
     * @hibernate.id
     * generator-class="sequence"
     * column="ITEM_SET_CATEGORY_ID" 
     * 
     * @hibernate.generator-param
     * name="sequence"
     * value="SEQ_ITEM_SET_CATEGORY_ID"
     */
    public Long getItemSetCategoryId() {
        return itemSetCategoryId;
    }

    /**
     * @hibernate.property
     * column="ITEM_SET_CATEGORY_LEVEL"
     * not-null="true"
     */
    public Long getItemSetCategoryLevel() {
        return itemSetCategoryLevel;
    }

    /**
     * @hibernate.property
     * column="ITEM_SET_CATEGORY_NAME"
     * not-null="false"
     */
    public String getItemSetCategoryName() {
        return itemSetCategoryName;
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

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDateTime(java.util.Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setFrameworkProductId(Long frameworkProductId) {
        this.frameworkProductId = frameworkProductId;
    }

    public void setItemSetCategoryId(Long itemSetCategoryId) {
        this.itemSetCategoryId = itemSetCategoryId;
    }

    public void setItemSetCategoryLevel(Long itemSetCategoryLevel) {
        this.itemSetCategoryLevel = itemSetCategoryLevel;
    }

    public void setItemSetCategoryName(String itemSetCategoryName) {
        this.itemSetCategoryName = itemSetCategoryName;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedDateTime(java.util.Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

}
