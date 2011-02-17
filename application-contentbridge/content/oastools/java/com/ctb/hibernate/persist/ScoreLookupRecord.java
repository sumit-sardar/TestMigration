package com.ctb.hibernate.persist;

/**
 * @hibernate.class table="SCORE_LOOKUP"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ScoreLookupRecord {

    private String sourceScoreTypeCode;//NOT NULL VARCHAR2(3)
    private String destScoreTypeCode;//NOT NULL VARCHAR2(3)
    private String scoreLookupId;//NOT NULL VARCHAR2(32)
    private Long sourceScoreValue;//NOT NULL NUMBER
    private Long destScoreValue;//NOT NULL NUMBER
    private String testForm;//VARCHAR2(16)
    private String testLevel;//VARCHAR2(16)
    private String grade;//VARCHAR2(16)
    private String contentArea;//VARCHAR2(16)
    private String normGroup;//VARCHAR2(16)
    private Long quarterMonth;//NUMBER
    private String normYear;//VARCHAR2(16)
    private String frameworkCode;//VARCHAR2(30)
    private String productInternalDisplayName;

    /**
     * @hibernate.property
     * column="SOURCE_SCORE_TYPE_CODE"
     * not-null="true"
     */
    public String getSourceScoreTypeCode() {
        return sourceScoreTypeCode;
    }

    public void setSourceScoreTypeCode(String sourceScoreTypeCode) {
        this.sourceScoreTypeCode = sourceScoreTypeCode;
    }

    /**
     * @hibernate.property
     * column="DEST_SCORE_TYPE_CODE"
     * not-null="true"
     */
    public String getDestScoreTypeCode() {
        return destScoreTypeCode;
    }

    public void setDestScoreTypeCode(String destScoreTypeCode) {
        this.destScoreTypeCode = destScoreTypeCode;
    }

    /** @hibernate.id generator-class="assigned" column="SCORE_LOOKUP_ID" */
    public String getScoreLookupId() {
        return scoreLookupId;
    }

    public void setScoreLookupId(String scoreLookupId) {
        this.scoreLookupId = scoreLookupId;
    }
    /**
     * @hibernate.property
     * column="SOURCE_SCORE_VALUE"
     * not-null="true"
     */
    public Long getSourceScoreValue() {
        return sourceScoreValue;
    }

    public void setSourceScoreValue(Long sourceScoreValue) {
        this.sourceScoreValue = sourceScoreValue;
    }
    /**
     * @hibernate.property
     * column="DEST_SCORE_VALUE"
     * not-null="true"
     */
    public Long getDestScoreValue() {
        return destScoreValue;
    }

    public void setDestScoreValue(Long destScoreValue) {
        this.destScoreValue = destScoreValue;
    }

    /**
     * @hibernate.property
     * column="TEST_FORM"
     * not-null="false"
     */
    public String getTestForm() {
        return testForm;
    }

    public void setTestForm(String testForm) {
        this.testForm = testForm;
    }

    /**
     * @hibernate.property
     * column="TEST_LEVEL"
     * not-null="false"
     */
    public String getTestLevel() {
        return testLevel;
    }

    public void setTestLevel(String testLevel) {
        this.testLevel = testLevel;
    }

    /**
     * @hibernate.property
     * column="GRADE"
     * not-null="false"
     */
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * @hibernate.property
     * column="CONTENT_AREA"
     * not-null="false"
     */
    public String getContentArea() {
        return contentArea;
    }

    public void setContentArea(String contentArea) {
        this.contentArea = contentArea;
    }

    /**
     * @hibernate.property
     * column="NORM_GROUP"
     * not-null="false"
     */
    public String getNormGroup() {
        return normGroup;
    }

    public void setNormGroup(String normGroup) {
        this.normGroup = normGroup;
    }

    /**
     * @hibernate.property
     * column="QUARTER_MONTH"
     * not-null="false"
     */
    public Long getQuarterMonth() {
        return quarterMonth;
    }

    public void setQuarterMonth(Long quarterMonth) {
        this.quarterMonth = quarterMonth;
    }

    /**
     * @hibernate.property
     * column="NORM_YEAR"
     * not-null="false"
     */
    public String getNormYear() {
        return normYear;
    }

    public void setNormYear(String normYear) {
        this.normYear = normYear;
    }

    /**
     * @hibernate.property
     * column="FRAMEWORK_CODE"
     * not-null="false"
     */
    public String getFrameworkCode() {
        return frameworkCode;
    }

    public void setFrameworkCode(String frameworkCode) {
        this.frameworkCode = frameworkCode;
    }

    /**
     * @hibernate.property
     * column="PRODUCT_INTERNAL_DISPLAY_NAME"
     * not-null="false"
     */
    public String getProductInternalDisplayName() {
        return productInternalDisplayName;
    }

    public void setProductInternalDisplayName(String productInternalDisplayName) {
        this.productInternalDisplayName = productInternalDisplayName;
    }
    //make these equal based on score_lookup_id only
    public int hashCode() {
        return this.scoreLookupId.hashCode();//To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean equals(Object obj) {
        ScoreLookupRecord slr = (ScoreLookupRecord) obj;
        return this.scoreLookupId.equals(slr.scoreLookupId);
    }
}
