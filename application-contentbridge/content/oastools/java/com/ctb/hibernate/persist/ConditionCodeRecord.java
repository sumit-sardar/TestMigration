package com.ctb.hibernate.persist;

/**
 * @hibernate.class table="CONDITION_CODE"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ConditionCodeRecord {
    private String attempted;
    private String conditionCode;
    private String conditionCodeDesc;
    private Long conditionCodeId;
    private String conditionCodeLabel;

    /**
     * @hibernate.property
     * column="ATTEMPTED"
     * not-null="true"
     */
    public String getAttempted() {
        return attempted;
    }

    /**
     * @hibernate.property
     * column="CONDITION_CODE"
     * not-null="true"
     */
    public String getConditionCode() {
        return conditionCode;
    }

    /**
     * @hibernate.property
     * column="CONDITION_CODE_DESC"
     * not-null="true"
     */
    public String getConditionCodeDesc() {
        return conditionCodeDesc;
    }

    /**
     * @hibernate.id
     * generator-class="sequence"
     * column="CONDITION_CODE_ID" 
     * 
     * @hibernate.generator-param
     * name="sequence"
     * value="SEQ_CONDITION_CODE_ID"
     */
    public Long getConditionCodeId() {
        return conditionCodeId;
    }

    /**
     * @hibernate.property
     * column="CONDITION_CODE_LABEL"
     * not-null="true"
     */
    public String getConditionCodeLabel() {
        return conditionCodeLabel;
    }

    public void setAttempted(String attempted) {
        this.attempted = attempted;
    }

    public void setConditionCode(String conditionCode) {
        this.conditionCode = conditionCode;
    }

    public void setConditionCodeDesc(String conditionCodeDesc) {
        this.conditionCodeDesc = conditionCodeDesc;
    }

    public void setConditionCodeId(Long conditionCodeId) {
        this.conditionCodeId = conditionCodeId;
    }

    public void setConditionCodeLabel(String conditionCodeLabel) {
        this.conditionCodeLabel = conditionCodeLabel;
    }

}
