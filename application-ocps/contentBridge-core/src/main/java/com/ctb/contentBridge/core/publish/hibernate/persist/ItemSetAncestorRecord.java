package com.ctb.contentBridge.core.publish.hibernate.persist;
/**
 * @hibernate.class table="ITEM_SET_ANCESTOR"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ItemSetAncestorRecord {
    private String ancestorItemSetType;
    private Long createdBy;
    private java.util.Date createdDateTime;
    private Long degrees;
    private Long itemSetSortOrder;
    private String itemSetType;
    private Long updatedBy;
    private java.util.Date updatedDateTime;

    private ItemSetAncestorCompositeId id = new ItemSetAncestorCompositeId();

    /** @hibernate.id generator-class="assigned" */
    public ItemSetAncestorCompositeId getId() {
        return id;
    }
    public void setId(ItemSetAncestorCompositeId id) {
        this.id = id;
    }

    /**
     * @hibernate.property
     * column="ANCESTOR_ITEM_SET_TYPE"
     * not-null="true"
     */
    public String getAncestorItemSetType() {
        return ancestorItemSetType;
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
     * not-null="false"
     */
    public java.util.Date getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * @hibernate.property
     * column="DEGREES"
     * not-null="true"
     */
    public Long getDegrees() {
        return degrees;
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

    public void setAncestorItemSetType(String ancestorItemSetType) {
        this.ancestorItemSetType = ancestorItemSetType;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDateTime(java.util.Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setDegrees(Long degrees) {
        this.degrees = degrees;
    }

    public void setItemSetSortOrder(Long itemSetSortOrder) {
        this.itemSetSortOrder = itemSetSortOrder;
    }

    public void setItemSetType(String itemSetType) {
        this.itemSetType = itemSetType;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedDateTime(java.util.Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

}
