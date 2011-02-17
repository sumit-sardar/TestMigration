package com.ctb.hibernate.persist;

/**
 * @hibernate.class table="ITEM_SET_PRODUCT"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ItemSetProductRecord {
    private Long createdBy;
    private java.util.Date createdDateTime;
    private Long updatedBy;
    private java.util.Date updatedDateTime;

    private ItemSetProductCompositeId id = new ItemSetProductCompositeId();

    /** @hibernate.id generator-class="assigned" */
    public ItemSetProductCompositeId getId() {
        return id;
    }
    public void setId(ItemSetProductCompositeId id) {
        this.id = id;
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

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedDateTime(java.util.Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

}
