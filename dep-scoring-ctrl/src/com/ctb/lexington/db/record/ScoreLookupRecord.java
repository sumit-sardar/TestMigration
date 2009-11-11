package com.ctb.lexington.db.record;

import java.math.BigDecimal;

/*
 * Generated class: do NOT format or modify in any way! (unless you want to)
 */
public class ScoreLookupRecord implements Persistent {
    private Long itemSetId;
    private String sourceScoreTypeCode;
    private String destScoreTypeCode;
    private String scoreLookupId;
    private BigDecimal sourceScoreValue;
    private BigDecimal destScoreValue;
    private String testForm;
    private String testLevel;
    private String grade;
    private String contentArea;
    private String normGroup;
    private String normYear;
    private String frameworkCode;
    private String ageCategory;
    private String extendedFlag;

    public ScoreLookupRecord() {
        // empty ctor for ibatis
    }

    public ScoreLookupRecord(Long itemSetId, String sourceScoreTypeCode, String destScoreTypeCode,
            BigDecimal sourceScoreValue, BigDecimal destScoreValue) {
        this.itemSetId = itemSetId;
        this.sourceScoreTypeCode = sourceScoreTypeCode;
        this.destScoreTypeCode = destScoreTypeCode;
        this.sourceScoreValue = sourceScoreValue;
        this.destScoreValue = destScoreValue;
    }

    public String getContentArea() {
        return contentArea;
    }

    public void setContentArea(String contentArea) {
        this.contentArea = contentArea;
    }

    public String getDestScoreTypeCode() {
        return destScoreTypeCode;
    }

    public void setDestScoreTypeCode(String destScoreTypeCode) {
        this.destScoreTypeCode = destScoreTypeCode;
    }

    public BigDecimal getDestScoreValue() {
        return destScoreValue;
    }

    public void setDestScoreValue(BigDecimal destScoreValue) {
        this.destScoreValue = destScoreValue;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Long getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public String getNormGroup() {
        return normGroup;
    }

    public void setNormGroup(String normGroup) {
        this.normGroup = normGroup;
    }

    public String getNormYear() {
        return normYear;
    }

    public void setNormYear(String normYear) {
        this.normYear = normYear;
    }

    public String getScoreLookupId() {
        return scoreLookupId;
    }

    public void setScoreLookupId(String scoreLookupId) {
        this.scoreLookupId = scoreLookupId;
    }

    public String getSourceScoreTypeCode() {
        return sourceScoreTypeCode;
    }

    public void setSourceScoreTypeCode(String sourceScoreTypeCode) {
        this.sourceScoreTypeCode = sourceScoreTypeCode;
    }

    public BigDecimal getSourceScoreValue() {
        return sourceScoreValue;
    }

    public void setSourceScoreValue(BigDecimal sourceScoreValue) {
        this.sourceScoreValue = sourceScoreValue;
    }

    public String getTestForm() {
        return testForm;
    }

    public void setTestForm(String testForm) {
        this.testForm = testForm;
    }

    public String getTestLevel() {
        return testLevel;
    }

    public void setTestLevel(String testLevel) {
        this.testLevel = testLevel;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(String ageCategory) {
        this.ageCategory = ageCategory;
    }

    public String getFrameworkCode() {
        return frameworkCode;
    }

    public void setFrameworkCode(String frameworkCode) {
        this.frameworkCode = frameworkCode;
    }

    public String getExtendedFlag() {
        return extendedFlag;
    }

    public void setExtendedFlag(String extendedFlag) {
        this.extendedFlag = extendedFlag;
    }
}