package com.ctb.hibernate.persist;

/**
 * @hibernate.class table="ITEM_SET_ITEM"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ItemSetItemRecord {
    private Long createdBy;
    private java.util.Date createdDateTime;
    private Long itemSortOrder;
    private Long updatedBy;
    private java.util.Date updatedDateTime;
    private String fieldTest;
    private String ibsInvisible = "F";
    private String suppressed;

    private ItemSetItemCompositeId id = new ItemSetItemCompositeId();

    /** @hibernate.id generator-class="assigned" */
    public ItemSetItemCompositeId getId() {
        return id;
    }
    public void setId(ItemSetItemCompositeId id) {
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
     * column="FIELD_TEST"
     * not-null="true"
     */
    public String getFieldTest() {
        return fieldTest;
    }
    
    /**
     * @hibernate.property
     * column="IBS_INVISIBLE"
     * not-null="true"
     */
    public String getIbsInvisible() {
        return ibsInvisible;
    }
    
    /**
     * @hibernate.property
     * column="SUPPRESSED"
     * not-null="true"
     */
    public String getSuppressed() {
        return suppressed;
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
     * column="ITEM_SORT_ORDER"
     * not-null="true"
     */
    public Long getItemSortOrder() {
        return itemSortOrder;
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

    public void setItemSortOrder(Long itemSortOrder) {
        this.itemSortOrder = itemSortOrder;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedDateTime(java.util.Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }
    
    public void setFieldTest(String fieldTest) {
        this.fieldTest = fieldTest;
    }

    public void setIbsInvisible(String ibsInvisible) {
        this.ibsInvisible = ibsInvisible;
    }
    
    public void setSuppressed(String suppressed) {
        this.suppressed = suppressed;
    }

}
