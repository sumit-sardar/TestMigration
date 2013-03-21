package com.ctb.contentBridge.core.publish.hibernate.persist;

/**
 * @hibernate.class table="TEST_CATALOG"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class TestCatalogRecord {
    private String activationStatus;
    private Long createdBy;
    private java.util.Date createdDateTime;
    private String extCatalogId;
    private Long itemSetId;
    private Long productId;
    private String subject;
    private Long testCatalogId;
    private String testDisplayName;
    private String commodityCode;
    private String testForm;
    private String testGrade;
    private String testLevel;
    private String testName;
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
     * column="EXT_CATALOG_ID"
     * not-null="false"
     */
    public String getExtCatalogId() {
        return extCatalogId;
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
     * column="PRODUCT_ID"
     * not-null="true"
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @hibernate.property
     * column="SUBJECT"
     * not-null="true"
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @hibernate.id
     * generator-class="sequence"
     * column="TEST_CATALOG_ID" 
     * 
     * @hibernate.generator-param
     * name="sequence"
     * value="SEQ_TEST_CATALOG_ID"
     */
    public Long getTestCatalogId() {
        return testCatalogId;
    }

    /**
     * @hibernate.property
     * column="TEST_DISPLAY_NAME"
     * not-null="false"
     */
    public String getTestDisplayName() {
        return testDisplayName;
    }

    /**
     * @hibernate.property
     * column="TEST_FORM"
     * not-null="false"
     */
    public String getTestForm() {
        return testForm;
    }

    /**
     * @hibernate.property
     * column="TEST_GRADE"
     * not-null="false"
     */
    public String getTestGrade() {
        return testGrade;
    }

    /**
     * @hibernate.property
     * column="TEST_LEVEL"
     * not-null="false"
     */
    public String getTestLevel() {
        return testLevel;
    }

    /**
     * @hibernate.property
     * column="TEST_NAME"
     * not-null="true"
     */
    public String getTestName() {
        return testName;
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

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDateTime(java.util.Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setExtCatalogId(String extCatalogId) {
        this.extCatalogId = extCatalogId;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTestCatalogId(Long testCatalogId) {
        this.testCatalogId = testCatalogId;
    }

    public void setTestDisplayName(String testDisplayName) {
        this.testDisplayName = testDisplayName;
    }

    public void setTestForm(String testForm) {
        this.testForm = testForm;
    }

    public void setTestGrade(String testGrade) {
        this.testGrade = testGrade;
    }

    public void setTestLevel(String testLevel) {
        this.testLevel = testLevel;
    }

    public void setTestName(String testName) {
        this.testName = testName;
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
    
    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
    	this.commodityCode = commodityCode;
    }
}
