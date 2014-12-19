package com.ctb.lexington.db.irsdata.irstsdata;

import com.ctb.lexington.db.record.Persistent;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Rama_Rao
 *
 */
public class IrsTASCCompositeFactData implements Persistent {

	private Long factid;
	private Long compositeid;
	private Long orgNodeid;
	private Long studentid;
	private Long formid;
	private Long sessionid;
	private Long gradeid;
	private Long nrsLevelid;
	private Long scaleScore;
	private Long normalCurveEquivalent;
	private Long nationalPercentile;
	private Long proficiencyLevel;
	private Date testCompletionTimestamp;
    private Date testStartTimestamp;
	private Long assessmentid;
	private Long pointsPossible;
	private Long programid;
	private Long currentResultid;
	private Long pointsAttempted;
	private Long pointsObtained;
	private Long recLevelid;
	private String scaleScoreRangeForProficiency; //Added for TASC
    
    public String getScaleScoreRangeForProficiency() {
		return scaleScoreRangeForProficiency;
	}

	public void setScaleScoreRangeForProficiency(
			String scaleScoreRangeForProficiency) {
		this.scaleScoreRangeForProficiency = scaleScoreRangeForProficiency;
	}

	public boolean equals(Object arg0) {
			return 
				compositeid.equals(((IrsTASCCompositeFactData)arg0).getCompositeid()) &&
                orgNodeid.equals(((IrsTASCCompositeFactData)arg0).getOrgNodeid()) &&
                studentid.equals(((IrsTASCCompositeFactData)arg0).getStudentid()) &&
                formid.equals(((IrsTASCCompositeFactData)arg0).getFormid()) &&
                sessionid.equals(((IrsTASCCompositeFactData)arg0).getSessionid()) &&
                gradeid.equals(((IrsTASCCompositeFactData)arg0).getGradeid()) &&
                nrsLevelid.equals(((IrsTASCCompositeFactData)arg0).getNrsLevelid()) &&
                scaleScore.equals(((IrsTASCCompositeFactData)arg0).getScaleScore()) &&
                normalCurveEquivalent.equals(((IrsTASCCompositeFactData)arg0).getNormalCurveEquivalent()) &&
                nationalPercentile.equals(((IrsTASCCompositeFactData)arg0).getNationalPercentile()) &&
                testStartTimestamp.equals(((IrsTASCCompositeFactData)arg0).getTestStartTimestamp()) &&
                testCompletionTimestamp.equals(((IrsTASCCompositeFactData)arg0).getTestCompletionTimestamp()) &&
                assessmentid.equals(((IrsTASCCompositeFactData)arg0).getAssessmentid()) &&
                pointsPossible.equals(((IrsTASCCompositeFactData)arg0).getPointsPossible()) &&
                programid.equals(((IrsTASCCompositeFactData)arg0).getProgramid()) &&
                currentResultid.equals(((IrsTASCCompositeFactData)arg0).getCurrentResultid()) &&
                pointsAttempted.equals(((IrsTASCCompositeFactData)arg0).getPointsAttempted()) &&
                pointsObtained.equals(((IrsTASCCompositeFactData)arg0).getPointsObtained()) &&
                proficiencyLevel.equals(((IrsTASCCompositeFactData)arg0).getProficiencyLevel()) &&
                recLevelid.equals(((IrsTASCCompositeFactData)arg0).getRecLevelid());
			
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

	public Long getCompositeid() {
		return compositeid;
	}

	public void setCompositeid(Long compositeid) {
		this.compositeid = compositeid;
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

	public Long getGradeid() {
		return gradeid;
	}

	public void setGradeid(Long gradeid) {
		this.gradeid = gradeid;
	}

	public Long getNrsLevelid() {
		return nrsLevelid;
	}

	public void setNrsLevelid(Long nrsLevelid) {
		this.nrsLevelid = nrsLevelid;
	}

	public Long getScaleScore() {
		return scaleScore;
	}

	public void setScaleScore(Long scaleScore) {
		this.scaleScore = scaleScore;
	}
	public Long getNormalCurveEquivalent() {
		return normalCurveEquivalent;
	}

	public void setNormalCurveEquivalent(Long normalCurveEquivalent) {
		this.normalCurveEquivalent = normalCurveEquivalent;
	}

	public Long getNationalPercentile() {
		return nationalPercentile;
	}

	public void setNationalPercentile(Long nationalPercentile) {
		this.nationalPercentile = nationalPercentile;
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

	public Long getPointsAttempted() {
		return pointsAttempted;
	}

	public void setPointsAttempted(Long pointsAttempted) {
		this.pointsAttempted = pointsAttempted;
	}

	public Long getPointsObtained() {
		return pointsObtained;
	}

	public void setPointsObtained(Long pointsObtained) {
		this.pointsObtained = pointsObtained;
	}

	public Long getRecLevelid() {
		return recLevelid;
	}

	public void setRecLevelid(Long recLevelid) {
		this.recLevelid = recLevelid;
	}

	public Long getProficiencyLevel() {
		return proficiencyLevel;
	}

	public void setProficiencyLevel(Long proficiencyLevel) {
		this.proficiencyLevel = proficiencyLevel;
	}
}
