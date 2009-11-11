package com.ctb.lexington.db.data;

import java.math.BigDecimal;

import com.ctb.lexington.db.record.Persistent;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class StudentPredictedScoresData implements Persistent, ValidatedScoreRecord {
    private Long studentDimId;
    private Long studentDimVersionId;
    private Long adminDimId;
    private BigDecimal expectedGedReading;
    private BigDecimal expectedGedWriting;
    private BigDecimal expectedGedMath;
    private BigDecimal expectedGedSocialStudies;
    private BigDecimal expectedGedScience;
    private BigDecimal expectedGedAverage;
    private String validScore;

    public StudentPredictedScoresData() {
    }

    public StudentPredictedScoresData(Long studentDimId, Long studentDimVersionId, Long admindDimId) {
        this.studentDimId = studentDimId;
        this.studentDimVersionId = studentDimVersionId;
        this.adminDimId = admindDimId;
    }

    public Long getStudentDimId() {
        return studentDimId;
    }

    public void setStudentDimId(Long studentDimId) {
        this.studentDimId = studentDimId;
    }

    public Long getStudentDimVersionId() {
        return studentDimVersionId;
    }

    public void setStudentDimVersionId(Long studentDimVersionId) {
        this.studentDimVersionId = studentDimVersionId;
    }

    public Long getAdminDimId() {
        return adminDimId;
    }

    public void setAdminDimId(Long adminDimId) {
        this.adminDimId = adminDimId;
    }

    public BigDecimal getExpectedGedReading() {
        return expectedGedReading;
    }

    public void setExpectedGedReading(BigDecimal expectedGedReading) {
        this.expectedGedReading = expectedGedReading;
    }

    public BigDecimal getExpectedGedWriting() {
        return expectedGedWriting;
    }

    public void setExpectedGedWriting(BigDecimal expectedGedWriting) {
        this.expectedGedWriting = expectedGedWriting;
    }

    public BigDecimal getExpectedGedMath() {
        return expectedGedMath;
    }

    public void setExpectedGedMath(BigDecimal expectedGedMath) {
        this.expectedGedMath = expectedGedMath;
    }

    public BigDecimal getExpectedGedSocialStudies() {
        return expectedGedSocialStudies;
    }

    public void setExpectedGedSocialStudies(BigDecimal expectedGedSocialStudies) {
        this.expectedGedSocialStudies = expectedGedSocialStudies;
    }

    public BigDecimal getExpectedGedScience() {
        return expectedGedScience;
    }

    public void setExpectedGedScience(BigDecimal expectedGedScience) {
        this.expectedGedScience = expectedGedScience;
    }

    public BigDecimal getExpectedGedAverage() {
        return expectedGedAverage;
    }

    public void setExpectedGedAverage(BigDecimal expectedGedAverage) {
        this.expectedGedAverage = expectedGedAverage;
    }

    public String getValidScore() {
        return validScore;
    }

    public void setValidScore(String validScore) {
        this.validScore = validScore;
    }
}