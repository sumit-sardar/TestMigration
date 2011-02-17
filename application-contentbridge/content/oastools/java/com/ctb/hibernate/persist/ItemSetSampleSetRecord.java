package com.ctb.hibernate.persist;

/**
 * @hibernate.class table="ITEM_SET_SAMPLE_SET" persister="com.ctb.hibernate.CTBEntityPersister"
 *                  dynamic-update="true" dynamic-insert="true"
 */
public class ItemSetSampleSetRecord {
    private Long itemSetId;
    private Long sampleItemSetId;
    private String subtestLevel;
    private String subtestName;
    private String testType;

    /** @hibernate.id generator-class="assigned" column="ITEM_SET_ID" */
    public Long getItemSetId() {
        return itemSetId;
    }

    /**
     * @hibernate.property column="SAMPLE_ITEM_SET_ID" not-null="false"
     */
    public Long getSampleItemSetId() {
        return sampleItemSetId;
    }

    /**
     * @hibernate.property column="SUBTEST_LEVEL" not-null="false"
     */
    public String getSubtestLevel() {
        return subtestLevel;
    }

    /**
     * @hibernate.property column="SUBTEST_NAME" not-null="false"
     */
    public String getSubtestName() {
        return subtestName;
    }

    /**
     * @hibernate.property column="TEST_TYPE" not-null="false"
     */
    public String getTestType() {
        return testType;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public void setSampleItemSetId(Long sampleItemSetId) {
        this.sampleItemSetId = sampleItemSetId;
    }

    public void setSubtestLevel(String subtestLevel) {
        this.subtestLevel = subtestLevel;
    }

    public void setSubtestName(String subtestName) {
        this.subtestName = subtestName;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

}