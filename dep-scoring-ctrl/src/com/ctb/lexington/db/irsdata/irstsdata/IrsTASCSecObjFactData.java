package com.ctb.lexington.db.irsdata.irstsdata;

import com.ctb.lexington.db.record.Persistent;
import java.util.Date;

/**
 * @author Rama_Rao
 *
 */
public class IrsTASCSecObjFactData implements Persistent{

    private Long factid;
    private Long secObjid;
    private Long orgNodeid;
    private Long studentid;
    private Long formid;
    private Long sessionid;
    private Long levelid;
    private Long gradeid;
    private Long pointsAttempted;
    private Long percentObtained;
    private Long masteryLevelid;
    private Date testStartTimestamp;
    private Date testCompletionTimestamp;
    private Long assessmentid;
    private Long pointsObtained;
    private Long pointsPossible;
    private Long programid;
    private Long currentResultid;
    private String subtestName;
    private Long scaleScore; //Added for TASC
    private String objectiveScoringStatus; //Added for TASC
    private String conditionCode; //Added for TASC
    private String scaleScoreRangeForMasteryLevel; //Added for TASC

	/**
	 * @return the objectiveScoringStatus
	 */
	public String getObjectiveScoringStatus() {
		return objectiveScoringStatus;
	}

	/**
	 * @param objectiveScoringStatus the objectiveScoringStatus to set
	 */
	public void setObjectiveScoringStatus(String objectiveScoringStatus) {
		this.objectiveScoringStatus = objectiveScoringStatus;
	}

	public Long getScaleScore() {
		return scaleScore;
	}

	public void setScaleScore(Long scaleScore) {
		this.scaleScore = scaleScore;
	}

	public boolean equals(Object arg0) {
			return 
				secObjid.equals(((IrsTASCSecObjFactData)arg0).getSecObjid()) &&
                orgNodeid.equals(((IrsTASCSecObjFactData)arg0).getOrgNodeid()) &&
                studentid.equals(((IrsTASCSecObjFactData)arg0).getStudentid()) &&
                formid.equals(((IrsTASCSecObjFactData)arg0).getFormid()) &&
                sessionid.equals(((IrsTASCSecObjFactData)arg0).getSessionid()) &&
                gradeid.equals(((IrsTASCSecObjFactData)arg0).getGradeid()) &&
                testStartTimestamp.equals(((IrsTASCSecObjFactData)arg0).getTestStartTimestamp()) &&
                testCompletionTimestamp.equals(((IrsTASCSecObjFactData)arg0).getTestCompletionTimestamp()) &&
                assessmentid.equals(((IrsTASCSecObjFactData)arg0).getAssessmentid()) &&
                pointsPossible.equals(((IrsTASCSecObjFactData)arg0).getPointsPossible()) &&
                programid.equals(((IrsTASCSecObjFactData)arg0).getProgramid()) &&
                currentResultid.equals(((IrsTASCSecObjFactData)arg0).getCurrentResultid()) &&
                pointsAttempted.equals(((IrsTASCSecObjFactData)arg0).getPointsAttempted()) &&
                pointsObtained.equals(((IrsTASCSecObjFactData)arg0).getPointsObtained()) &&
                scaleScore.equals(((IrsTASCSecObjFactData)arg0).getScaleScore()) &&
                masteryLevelid.equals(((IrsTASCSecObjFactData)arg0).getMasteryLevelid());
			
    }
    
    public Date getTestStartTimestamp() {
        return testStartTimestamp;
    }

    public void setTestStartTimestamp(Date testStartTimestamp) {
        this.testStartTimestamp = testStartTimestamp;
    }

    public int hashCode() {
        return (int) factid.longValue();
    }

    public Long getFactid() {
        return factid;
    }

    public void setFactid(Long factid) {
        this.factid = factid;
    }

    public Long getSecObjid() {
        return secObjid;
    }

    public void setSecObjid(Long secObjid) {
        this.secObjid = secObjid;
    }

    public Long getOrgNodeid() {
        return orgNodeid;
    }

    public void setOrgNodeid(Long orgNodeid) {
        this.orgNodeid = orgNodeid;
    }

    public Long getStudentid() {
        return studentid;
    }

    public void setStudentid(Long studentid) {
        this.studentid = studentid;
    }

    public Long getFormid() {
        return formid;
    }

    public void setFormid(Long formid) {
        this.formid = formid;
    }

    public Long getSessionid() {
        return sessionid;
    }

    public void setSessionid(Long sessionid) {
        this.sessionid = sessionid;
    }

    public Long getLevelid() {
        return levelid;
    }

    public void setLevelid(Long levelid) {
        this.levelid = levelid;
    }

    public Long getGradeid() {
        return gradeid;
    }

    public void setGradeid(Long gradeid) {
        this.gradeid = gradeid;
    }

    public Long getPointsAttempted() {
        return pointsAttempted;
    }

    public void setPointsAttempted(Long pointsAttempted) {
        this.pointsAttempted = pointsAttempted;
    }

    public Long getPercentObtained() {
        return percentObtained;
    }

    public void setPercentObtained(Long percentObtained) {
        this.percentObtained = percentObtained;
    }

    public Long getMasteryLevelid() {
        return masteryLevelid;
    }

    public void setMasteryLevelid(Long masteryLevelid) {
        this.masteryLevelid = masteryLevelid;
    }

    public Date getTestCompletionTimestamp() {
        return testCompletionTimestamp;
    }

    public void setTestCompletionTimestamp(Date testCompletionTimestamp) {
        this.testCompletionTimestamp = testCompletionTimestamp;
    }

    public Long getAssessmentid() {
        return assessmentid;
    }

    public void setAssessmentid(Long assessmentid) {
        this.assessmentid = assessmentid;
    }

    public Long getPointsObtained() {
        return pointsObtained;
    }

    public void setPointsObtained(Long pointsObtained) {
        this.pointsObtained = pointsObtained;
    }

    public Long getPointsPossible() {
        return pointsPossible;
    }

    public void setPointsPossible(Long pointsPossible) {
        this.pointsPossible = pointsPossible;
    }

    public Long getProgramid() {
        return programid;
    }

    public void setProgramid(Long programid) {
        this.programid = programid;
    }

    public Long getCurrentResultid() {
        return currentResultid;
    }

    public void setCurrentResultid(Long currentResultid) {
        this.currentResultid = currentResultid;
    }

    public String getSubtestName() {
        return this.subtestName;
    }
    
    public void setSubtestName(String subtestName) {
        this.subtestName = subtestName;
    }

	/**
	 * @return the conditionCode
	 */
	public String getConditionCode() {
		return conditionCode;
	}

	/**
	 * @param conditionCode the conditionCode to set
	 */
	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}

	/**
	 * @return the scaleScoreRangeForMasteryLevel
	 */
	public String getScaleScoreRangeForMasteryLevel() {
		return scaleScoreRangeForMasteryLevel;
	}

	/**
	 * @param scaleScoreRangeForMasteryLevel the scaleScoreRangeForMasteryLevel to set
	 */
	public void setScaleScoreRangeForMasteryLevel(
			String scaleScoreRangeForMasteryLevel) {
		this.scaleScoreRangeForMasteryLevel = scaleScoreRangeForMasteryLevel;
	}
}
