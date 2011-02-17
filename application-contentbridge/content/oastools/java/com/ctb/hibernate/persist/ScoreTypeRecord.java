package com.ctb.hibernate.persist;

/**
 * @hibernate.class table="SCORE_TYPE"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ScoreTypeRecord {
    private String scoreTypeCode;
    private String scoreTypeDescription;
    private String scoreTypeName;

    /** @hibernate.id generator-class="assigned" column="SCORE_TYPE_CODE" */
    public String getScoreTypeCode() {
        return scoreTypeCode;
    }

    /**
     * @hibernate.property
     * column="SCORE_TYPE_DESCRIPTION"
     * not-null="true"
     */
    public String getScoreTypeDescription() {
        return scoreTypeDescription;
    }

    /**
     * @hibernate.property
     * column="SCORE_TYPE_NAME"
     * not-null="true"
     */
    public String getScoreTypeName() {
        return scoreTypeName;
    }

    public void setScoreTypeCode(String scoreTypeCode) {
        this.scoreTypeCode = scoreTypeCode;
    }

    public void setScoreTypeDescription(String scoreTypeDescription) {
        this.scoreTypeDescription = scoreTypeDescription;
    }

    public void setScoreTypeName(String scoreTypeName) {
        this.scoreTypeName = scoreTypeName;
    }

}
