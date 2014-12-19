package com.ctb.lexington.db.irsdata.irstsdata;

import com.ctb.lexington.db.record.Persistent;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Dipak Dutta (381623)
 *
 */
public class IrsTASCContentAreaFactData implements Persistent{

	private Long factid;
    private Long contentAreaid;
    private Long orgNodeid;
    private Long studentid;
    private Long formid;
    private Long sessionid;
    private Long levelid;
    private Long gradeid;
    private Long nrsLevelid;
    private Long scaleScore;
    private Long normalCurveEquivalent;
    private Long nationalPercentile;
    private Date testStartTimestamp;
    private Date testCompletionTimestamp;
    private Long assessmentid;
    private Long pointsAttempted;
    private Long percentObtained;
    private Long recActivityid;
    private Long pointsObtained;
    private Long pointsPossible;
    private Long subjectid;
    private Long recLevelid;
    private Long programid;
    private Long currentResultid;
    private String subtestScoringStatus;
    private Long proficiencyLevel;
    private String proficiencyRange;
    
    public Long getProficiencyLevel() {
		return proficiencyLevel;
	}

	public void setProficiencyLevel(Long proficiencyLevel) {
		this.proficiencyLevel = proficiencyLevel;
	}

	public boolean equals(Object arg0) {
			return 
				contentAreaid.equals(((IrsTASCContentAreaFactData)arg0).getContentAreaid()) &&
                orgNodeid.equals(((IrsTASCContentAreaFactData)arg0).getOrgNodeid()) &&
                studentid.equals(((IrsTASCContentAreaFactData)arg0).getStudentid()) &&
                formid.equals(((IrsTASCContentAreaFactData)arg0).getFormid()) &&
                sessionid.equals(((IrsTASCContentAreaFactData)arg0).getSessionid()) &&
                gradeid.equals(((IrsTASCContentAreaFactData)arg0).getGradeid()) &&
                nrsLevelid.equals(((IrsTASCContentAreaFactData)arg0).getNrsLevelid()) &&
                scaleScore.equals(((IrsTASCContentAreaFactData)arg0).getScaleScore()) &&
                normalCurveEquivalent.equals(((IrsTASCContentAreaFactData)arg0).getNormalCurveEquivalent()) &&
                nationalPercentile.equals(((IrsTASCContentAreaFactData)arg0).getNationalPercentile()) &&
                testStartTimestamp.equals(((IrsTASCContentAreaFactData)arg0).getTestStartTimestamp()) &&
                testCompletionTimestamp.equals(((IrsTASCContentAreaFactData)arg0).getTestCompletionTimestamp()) &&
                assessmentid.equals(((IrsTASCContentAreaFactData)arg0).getAssessmentid()) &&
                pointsPossible.equals(((IrsTASCContentAreaFactData)arg0).getPointsPossible()) &&
                programid.equals(((IrsTASCContentAreaFactData)arg0).getProgramid()) &&
                currentResultid.equals(((IrsTASCContentAreaFactData)arg0).getCurrentResultid()) &&
                pointsAttempted.equals(((IrsTASCContentAreaFactData)arg0).getPointsAttempted()) &&
                pointsObtained.equals(((IrsTASCContentAreaFactData)arg0).getPointsObtained()) &&
                recLevelid.equals(((IrsTASCContentAreaFactData)arg0).getRecLevelid()) && 
                proficiencyLevel.equals(((IrsTASCContentAreaFactData)arg0).getRecLevelid()) && 
                subtestScoringStatus.equals(((IrsTASCContentAreaFactData)arg0).getSubtestScoringStatus());
			
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

    public Long getContentAreaid() {
        return contentAreaid;
    }

    public void setContentAreaid(Long contentAreaid) {
        this.contentAreaid = contentAreaid;
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

    public Long getRecActivityid() {
        return recActivityid;
    }

    public void setRecActivityid(Long recActivityid) {
        this.recActivityid = recActivityid;
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

    public Long getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(Long subjectid) {
        this.subjectid = subjectid;
    }

    public Long getRecLevelid() {
        return recLevelid;
    }

    public void setRecLevelid(Long recLevelid) {
        this.recLevelid = recLevelid;
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
    
	public String getSubtestScoringStatus() {
		return subtestScoringStatus;
	}
	
	public void setSubtestScoringStatus(String subtestScoringStatus) {
		this.subtestScoringStatus = subtestScoringStatus;
	}

	/**
	 * @return the proficiencyRange
	 */
	public String getProficiencyRange() {
		return proficiencyRange;
	}

	/**
	 * @param proficiencyRange the proficiencyRange to set
	 */
	public void setProficiencyRange(String proficiencyRange) {
		this.proficiencyRange = proficiencyRange;
	}
}