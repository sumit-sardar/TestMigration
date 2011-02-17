package com.ctb.hibernate.persist;

/**
 * @hibernate.class table="ITEM_SET_PARENT"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ItemSetParentRecord {
    private Long createdBy;
    private java.util.Date createdDateTime;
    private Long itemSetSortOrder;
    private String itemSetType;
    private String parentItemSetType;
    private Long updatedBy;
    private java.util.Date updatedDateTime;

    private ItemSetParentCompositeId id = new ItemSetParentCompositeId();

    /** @hibernate.id generator-class="assigned" */
    public ItemSetParentCompositeId getId() {
        return id;
    }
    public void setId(ItemSetParentCompositeId id) {
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
     * column="ITEM_SET_SORT_ORDER"
     * not-null="false"
     */
    public Long getItemSetSortOrder() {
        return itemSetSortOrder;
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
     * column="PARENT_ITEM_SET_TYPE"
     * not-null="true"
     */
    public String getParentItemSetType() {
        return parentItemSetType;
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

    public void setItemSetSortOrder(Long itemSetSortOrder) {
        this.itemSetSortOrder = itemSetSortOrder;
    }

    public void setItemSetType(String itemSetType) {
        this.itemSetType = itemSetType;
    }

    public void setParentItemSetType(String parentItemSetType) {
        this.parentItemSetType = parentItemSetType;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedDateTime(java.util.Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

}
