package com.ctb.lexington.db.data;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ctb.lexington.db.record.Persistent;
import com.ctb.lexington.domain.score.scorer.ScorerHelper;

public class StudentScoreSummaryDetails implements Persistent, Comparable {
    private Long reportItemSetId;
    private Long studentTestHistoryId;
    private String reportItemSetName;
    private Long numIncorrect;
    private Long numUnattempted;
    private String mastered;
    private String atsArchive;
    private Long pointsPossible;
    private Long pointsObtained;
    private Long percentObtained;
    private Long pointsAttempted;
    private Long numOfItems;
    private String masteryLevel;
    private BigDecimal nationalScore;
    private Long ATSCurriculumDimId;
    private Long subtestId;
	private Long numCorrect;
	private Long numAttempted;

    public void calculatePercentObtained() {
        setPercentObtained(new Long(ScorerHelper.calculatePercentage(
                getPointsObtained().intValue(),
                pointsPossible.intValue())));
    }
	
	/**
	 * @return Returns the subtestName.
	 */
	public Long getSubtestId() {
		return this.subtestId;
	}
	/**
	 * @param subtestName The subtestName to set.
	 */
	public void setSubtestId(Long subtestId) {
        if(this.subtestId != null && !this.subtestId.equals(subtestId)) {
            // System.out.println("Suspicious! Attempt to change objective subtest id from " + this.subtestId + " to " + subtestId);
        } else {
            this.subtestId = subtestId;
        }
	}
    public String getAtsArchive() {
        return this.atsArchive;
    }

    public void setAtsArchive(String atsArchive) {
        this.atsArchive = atsArchive;
    }

    public String getMastered() {
        return mastered;
    }

    public void setMastered(String mastered) {
        this.mastered = mastered;
    }

    public Long getNumIncorrect() {
        return numIncorrect;
    }

    public void setNumIncorrect(Long numIncorrect) {
        this.numIncorrect = numIncorrect;
    }
    
    public Long getNumCorrect() {
        return numCorrect;
    }

    public void setNumCorrect(Long numCorrect) {
        this.numCorrect = numCorrect;
    }

    public Long getNumOfItems() {
        return numOfItems;
    }

    public void setNumOfItems(Long numOfItems) {
        this.numOfItems = numOfItems;
    }

    public Long getNumUnattempted() {
        return numUnattempted;
    }

    public void setNumUnattempted(Long numUnattempted) {
        this.numUnattempted = numUnattempted;
    }
    
    public Long getNumAttempted() {
        return numAttempted;
    }

    public void setNumAttempted(Long numAttempted) {
        this.numAttempted = numAttempted;
    }

    public Long getReportItemSetId() {
        return reportItemSetId;
    }

    public void setReportItemSetId(Long reportItemSetId) {
        this.reportItemSetId = reportItemSetId;
    }

    public String getReportItemSetName() {
        return reportItemSetName;
    }

    public void setReportItemSetName(String reportItemSetName) {
        this.reportItemSetName = reportItemSetName;
    }

    public Long getStudentTestHistoryId() {
        return studentTestHistoryId;
    }

    public void setStudentTestHistoryId(Long studentTestHistoryId) {
        this.studentTestHistoryId = studentTestHistoryId;
    }

    /**
     * @return Returns the percentObtained.
     */
    public Long getPercentObtained() {
        return percentObtained;
    }

    /**
     * @param percentObtained The percentObtained to set.
     */
    public void setPercentObtained(Long percentObtained) {
        this.percentObtained = percentObtained;
    }

    /**
     * @return Returns the pointsAttempted.
     */
    public Long getPointsAttempted() {
        return pointsAttempted;
    }

    /**
     * @param pointsAttempted The pointsAttempted to set.
     */
    public void setPointsAttempted(Long pointsAttempted) {
        this.pointsAttempted = pointsAttempted;
    }

    /**
     * @return Returns the pointsObtained.
     */
    public Long getPointsObtained() {
        return pointsObtained;
    }

    /**
     * @param pointsObtained The pointsObtained to set.
     */
    public void setPointsObtained(Long pointsObtained) {
        this.pointsObtained = pointsObtained;
    }

    /**
     * @return Returns the pointsPossible.
     */
    public Long getPointsPossible() {
        return pointsPossible;
    }

    /**
     * @param pointsPossible The pointsPossible to set.
     */
    public void setPointsPossible(Long pointsPossible) {
        this.pointsPossible = pointsPossible;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public void setMasteryLevel(final String masteryCode){
        this.masteryLevel = masteryCode;
    }

    public String getMasteryLevel(){
        return masteryLevel;
    }

    public BigDecimal getNationalScore() {
        return nationalScore;
    }

    public void setNationalScore(BigDecimal nationalScore) {
        this.nationalScore = nationalScore;
    }

    public int compareTo(Object o) {
        StudentScoreSummaryDetails that = (StudentScoreSummaryDetails)o;
        int result = studentTestHistoryId.compareTo(that.studentTestHistoryId);
        if(result != 0) return result;
        return reportItemSetId.compareTo(that.reportItemSetId);
    }
	/**
	 * @return Returns the aTSCurriculumDimId.
	 */
	public Long getATSCurriculumDimId() {
		return ATSCurriculumDimId;
	}
	/**
	 * @param curriculumDimId The aTSCurriculumDimId to set.
	 */
	public void setATSCurriculumDimId(Long curriculumDimId) {
		ATSCurriculumDimId = curriculumDimId;
	}
}