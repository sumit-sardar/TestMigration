package com.ctb.contentBridge.core.publish.hibernate.persist;

/**
 * @hibernate.class table="DATAPOINT"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class DatapointRecord {
    private Long createdBy;
    private java.util.Date createdDateTime;
    private Long datapointId;
    private String itemId;
    private Long itemSetId;
    private Long maxPoints;
    private Long minPoints;
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
     * @hibernate.id
     * generator-class="sequence"
     * column="DATAPOINT_ID" 
     * 
     * @hibernate.generator-param
     * name="sequence"
     * value="SEQ_DATAPOINT_ID"
     */
    public Long getDatapointId() {
        return datapointId;
    }

    /**
     * @hibernate.property
     * column="ITEM_ID"
     * not-null="true"
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @hibernate.property
     * column="ITEM_SET_ID"
     * not-null="true"
     */
    public Long getItemSetId() {
        return itemSetId;
    }

    /**
     * @hibernate.property
     * column="MAX_POINTS"
     * not-null="false"
     */
    public Long getMaxPoints() {
        return maxPoints;
    }

    /**
     * @hibernate.property
     * column="MIN_POINTS"
     * not-null="false"
     */
    public Long getMinPoints() {
        return minPoints;
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

    public void setDatapointId(Long datapointId) {
        this.datapointId = datapointId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public void setMaxPoints(Long maxPoints) {
        this.maxPoints = maxPoints;
    }

    public void setMinPoints(Long minPoints) {
        this.minPoints = minPoints;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedDateTime(java.util.Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

}
