package com.ctb.lexington.db.data;

import java.math.BigDecimal;

import com.ctb.lexington.db.record.Persistent;


public class StudentSubtestScoresDetails implements Persistent, Comparable {
    
    private Long itemSetId;
    private Long studentTestHistoryId;
    private String itemSetName;
    private String scoreTypeCode;
    private BigDecimal scoreValue;
    
    

    public Long getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public String getItemSetName() {
        return itemSetName;
    }

    public void setItemSetName(String itemSetName) {
        this.itemSetName = itemSetName;
    }

    public String getScoreTypeCode() {
        return scoreTypeCode;
    }

    public void setScoreTypeCode(String scoreTypeCode) {
        this.scoreTypeCode = scoreTypeCode;
    }

    public BigDecimal getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(BigDecimal scoreValue) {
        this.scoreValue = scoreValue;
    }

    public Long getStudentTestHistoryId() {
        return studentTestHistoryId;
    }

    public void setStudentTestHistoryId(Long studentTestHistoryId) {
        this.studentTestHistoryId = studentTestHistoryId;
    }

    public int compareTo(final Object o) {
        final StudentSubtestScoresDetails that = (StudentSubtestScoresDetails)o;
        final int result = studentTestHistoryId.compareTo(that.studentTestHistoryId);
        if(result != 0) return result;
        return itemSetId.compareTo(that.itemSetId);
    }
}
