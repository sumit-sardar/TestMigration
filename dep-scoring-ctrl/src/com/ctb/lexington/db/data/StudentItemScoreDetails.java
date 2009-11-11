package com.ctb.lexington.db.data;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ctb.lexington.db.record.Persistent;

public class StudentItemScoreDetails implements Persistent, Comparable {
    private Long studentTestHistoryId;
    private String itemId;
    private Integer itemResponseId;
    private Long testItemSetId;
    private String testItemSetName;
    private Long testItemNum;
    private Long reportItemSetId;
    private String reportItemSetName;
    private String response;
    private Long itemElapsedTime;
    private Timestamp createdDateTime;
    private String correctAnswer;
    private String correctAnswerId;
    private Long detailReportItemSetId;
    private String detailReportItemSetName;
    private Integer points;
    private String itemType;
    private Integer minPoints;
    private Integer maxPoints;
    private Long conditionCodeId;
    private String comments;
    private String atsArchive;
    
    public String getAtsArchive() {
        return this.atsArchive;
    }

    public void setAtsArchive(String atsArchive) {
        this.atsArchive = atsArchive;
    }

	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
    public StudentItemScoreDetails() {
        this.atsArchive = "T";    
    }

    public StudentItemScoreDetails(String itemId) {
        this.itemId = itemId;
    }
    /**
     * @return Returns the conditionCodeId.
     */
    public Long getConditionCodeId() {
        return conditionCodeId;
    }

    /**
     * @param conditionCodeId The conditionCodeId to set.
     */
    public void setConditionCodeId(Long conditionCodeId) {
        this.conditionCodeId = conditionCodeId;
    }

    /**
     * @return Returns the correctAnswer.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * @param correctAnswer The correctAnswer to set.
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * @return Returns the correctAnswerId.
     */
    public String getCorrectAnswerId() {
        return correctAnswerId;
    }

    /**
     * @param correctAnswerId The correctAnswerId to set.
     */
    public void setCorrectAnswerId(String correctAnswerId) {
        this.correctAnswerId = correctAnswerId;
    }

    /**
     * @return Returns the createdDateTime.
     */
    public Timestamp getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * @param createdDateTime The createdDateTime to set.
     */
    public void setCreatedDateTime(Timestamp createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    /**
     * @return Returns the detailReportItemSetId.
     */
    public Long getDetailReportItemSetId() {
        return detailReportItemSetId;
    }

    /**
     * @param detailReportItemSetId The detailReportItemSetId to set.
     */
    public void setDetailReportItemSetId(Long detailReportItemSetId) {
        this.detailReportItemSetId = detailReportItemSetId;
    }

    /**
     * @return Returns the detailReportItemSetName.
     */
    public String getDetailReportItemSetName() {
        return detailReportItemSetName;
    }

    /**
     * @param detailReportItemSetName The detailReportItemSetName to set.
     */
    public void setDetailReportItemSetName(String detailReportItemSetName) {
        this.detailReportItemSetName = detailReportItemSetName;
    }

    /**
     * @return Returns the itemElapsedTime.
     */
    public Long getItemElapsedTime() {
        return itemElapsedTime;
    }

    /**
     * @param itemElapsedTime The itemElapsedTime to set.
     */
    public void setItemElapsedTime(Long itemElapsedTime) {
        this.itemElapsedTime = itemElapsedTime;
    }

    /**
     * @return Returns the itemId.
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId The itemId to set.
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * @return Returns the itemResponseId.
     */
    public Integer getItemResponseId() {
        return itemResponseId;
    }

    /**
     * @param itemResponseId The itemResponseId to set.
     */
    public void setItemResponseId(Integer itemResponseId) {
        this.itemResponseId = itemResponseId;
    }

    /**
     * @return Returns the itemType.
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * @param itemType The itemType to set.
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * @return Returns the maxPoints.
     */
    public Integer getMaxPoints() {
        return maxPoints;
    }

    /**
     * @param maxPoints The maxPoints to set.
     */
    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }

    /**
     * @return Returns the minPoints.
     */
    public Integer getMinPoints() {
        return minPoints;
    }

    /**
     * @param minPoints The minPoints to set.
     */
    public void setMinPoints(Integer minPoints) {
        this.minPoints = minPoints;
    }

    /**
     * @return Returns the points.
     */
    public Integer getPoints() {
        return points;
    }

    /**
     * @param points The points to set.
     */
    public void setPoints(Integer points) {
        this.points = points;
    }

    /**
     * @return Returns the reportItemSetId.
     */
    public Long getReportItemSetId() {
        return reportItemSetId;
    }

    /**
     * @param reportItemSetId The reportItemSetId to set.
     */
    public void setReportItemSetId(Long reportItemSetId) {
        this.reportItemSetId = reportItemSetId;
    }

    /**
     * @return Returns the reportItemSetName.
     */
    public String getReportItemSetName() {
        return reportItemSetName;
    }

    /**
     * @param reportItemSetName The reportItemSetName to set.
     */
    public void setReportItemSetName(String reportItemSetName) {
        this.reportItemSetName = reportItemSetName;
    }

    /**
     * @return Returns the response.
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response The response to set.
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * @return Returns the studentTestHistoryId.
     */
    public Long getStudentTestHistoryId() {
        return studentTestHistoryId;
    }

    /**
     * @param studentTestHistoryId The studentTestHistoryId to set.
     */
    public void setStudentTestHistoryId(Long studentTestHistoryId) {
        this.studentTestHistoryId = studentTestHistoryId;
    }

    /**
     * @return Returns the testItemNum.
     */
    public Long getTestItemNum() {
        return testItemNum;
    }

    /**
     * @param testItemNum The testItemNum to set.
     */
    public void setTestItemNum(Long testItemNum) {
        this.testItemNum = testItemNum;
    }

    /**
     * @return Returns the testItemSetId.
     */
    public Long getTestItemSetId() {
        return testItemSetId;
    }

    /**
     * @param testItemSetId The testItemSetId to set.
     */
    public void setTestItemSetId(Long testItemSetId) {
        this.testItemSetId = testItemSetId;
    }

    /**
     * @return Returns the testItemSetName.
     */
    public String getTestItemSetName() {
        return testItemSetName;
    }

    /**
     * @param testItemSetName The testItemSetName to set.
     */
    public void setTestItemSetName(String testItemSetName) {
        this.testItemSetName = testItemSetName;
    }

    // Object

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentItemScoreDetails)) return false;

        final StudentItemScoreDetails studentItemScoreDetails = (StudentItemScoreDetails) o;

        if (itemId != null
            ? !itemId.equals(studentItemScoreDetails.itemId)
            : studentItemScoreDetails.itemId != null) return false;
        if (studentTestHistoryId != null
            ? !studentTestHistoryId.equals(
                    studentItemScoreDetails.studentTestHistoryId)
            : studentItemScoreDetails.studentTestHistoryId != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (studentTestHistoryId != null
                  ? studentTestHistoryId.hashCode() : 0);
        result = 29 * result + (itemId != null ? itemId.hashCode() : 0);
        return result;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    // Comparable

    /** {@inheritDoc} */
    public int compareTo(final Object o) {
        StudentItemScoreDetails that = (StudentItemScoreDetails) o;
        int result = studentTestHistoryId.compareTo(that.studentTestHistoryId);
        if(result != 0) return result;
        return testItemNum.compareTo(that.testItemNum);
    }
}