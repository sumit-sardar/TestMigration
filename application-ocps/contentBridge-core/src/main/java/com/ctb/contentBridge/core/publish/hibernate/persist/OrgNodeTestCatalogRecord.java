package com.ctb.contentBridge.core.publish.hibernate.persist;

/**
 * @hibernate.class table="ORG_NODE_TEST_CATALOG"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class OrgNodeTestCatalogRecord {
    private String activationStatus;
    private Long createdBy;
    private java.util.Date createdDateTime;
    private Long customerId;
    private Long productId;
    private Long updatedBy;
    private java.util.Date updatedDateTime;

    private OrgNodeTestCatalogCompositeId id = new OrgNodeTestCatalogCompositeId();

    /** @hibernate.id generator-class="assigned" */
    public OrgNodeTestCatalogCompositeId getId() {
        return id;
    }
    public void setId(OrgNodeTestCatalogCompositeId id) {
        this.id = id;
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
     * column="CUSTOMER_ID"
     * not-null="true"
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @hibernate.property
     * column="PRODUCT_ID"
     * not-null="false"
     */
    public Long getProductId() {
        return productId;
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

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDateTime(java.util.Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedDateTime(java.util.Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

}
