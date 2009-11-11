package com.ctb.lexington.db.irsdata.irstvdata;

import com.ctb.lexington.db.record.Persistent;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Rama_Rao
 *
 */
public class IrsTVContentAreaFactData implements Persistent{

	private Long factid;
    private Long contentAreaid;
    private Long orgNodeid;
    private Long studentid;
    private Long formid;
    private Long sessionid;
    private Long levelid;
    private Long attr1id;
    private Long attr7id;
    private Long attr4id;
    private Long attr2id;
    private Long attr5id;
    private Long attr6id;
    private Long attr3id;
    private Long attr10id;
    private Long attr11id;
    private Long attr12id;
    private Long attr13id;
    private Long attr14id;
    private Long attr15id;
    private Long attr16id;
    private Long gradeid;
    private Long attr9id;
    private Long attr8id;
    private Long nrsLevelid;
    private Long scaleScore;
    private Float gradeEquivalent;
    private Long normalCurveEquivalent;
    private Long percentageMastery;
    private Long nationalPercentile;
    private Long nationalStanine;
    private Long predictedGed;
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
    
    public boolean equals(Object arg0) {
			return 
				contentAreaid.equals(((IrsTVContentAreaFactData)arg0).getContentAreaid()) &&
                orgNodeid.equals(((IrsTVContentAreaFactData)arg0).getOrgNodeid()) &&
                studentid.equals(((IrsTVContentAreaFactData)arg0).getStudentid()) &&
                formid.equals(((IrsTVContentAreaFactData)arg0).getFormid()) &&
                sessionid.equals(((IrsTVContentAreaFactData)arg0).getSessionid()) &&
                attr1id.equals(((IrsTVContentAreaFactData)arg0).getAttr1id()) &&
                attr7id.equals(((IrsTVContentAreaFactData)arg0).getAttr7id()) &&
                attr4id.equals(((IrsTVContentAreaFactData)arg0).getAttr4id()) &&
                attr2id.equals(((IrsTVContentAreaFactData)arg0).getAttr2id()) &&
                attr5id.equals(((IrsTVContentAreaFactData)arg0).getAttr5id()) &&
                attr6id.equals(((IrsTVContentAreaFactData)arg0).getAttr6id()) &&
                attr3id.equals(((IrsTVContentAreaFactData)arg0).getAttr3id()) &&
                attr10id.equals(((IrsTVContentAreaFactData)arg0).getAttr10id()) &&
                attr11id.equals(((IrsTVContentAreaFactData)arg0).getAttr11id()) &&
                attr12id.equals(((IrsTVContentAreaFactData)arg0).getAttr12id()) &&
                attr13id.equals(((IrsTVContentAreaFactData)arg0).getAttr13id()) &&
                attr14id.equals(((IrsTVContentAreaFactData)arg0).getAttr14id()) &&
                attr15id.equals(((IrsTVContentAreaFactData)arg0).getAttr15id()) &&
                attr16id.equals(((IrsTVContentAreaFactData)arg0).getAttr16id()) &&
                gradeid.equals(((IrsTVContentAreaFactData)arg0).getGradeid()) &&
                attr9id.equals(((IrsTVContentAreaFactData)arg0).getAttr9id()) &&
                attr8id.equals(((IrsTVContentAreaFactData)arg0).getAttr8id()) &&
                nrsLevelid.equals(((IrsTVContentAreaFactData)arg0).getNrsLevelid()) &&
                scaleScore.equals(((IrsTVContentAreaFactData)arg0).getScaleScore()) &&
                gradeEquivalent.equals(((IrsTVContentAreaFactData)arg0).getGradeEquivalent()) &&
                normalCurveEquivalent.equals(((IrsTVContentAreaFactData)arg0).getNormalCurveEquivalent()) &&
                percentageMastery.equals(((IrsTVContentAreaFactData)arg0).getPercentageMastery()) &&
                nationalPercentile.equals(((IrsTVContentAreaFactData)arg0).getNationalPercentile()) &&
                nationalStanine.equals(((IrsTVContentAreaFactData)arg0).getNationalStanine()) &&
                predictedGed.equals(((IrsTVContentAreaFactData)arg0).getPredictedGed()) &&
                testStartTimestamp.equals(((IrsTVContentAreaFactData)arg0).getTestStartTimestamp()) &&
                testCompletionTimestamp.equals(((IrsTVContentAreaFactData)arg0).getTestCompletionTimestamp()) &&
                assessmentid.equals(((IrsTVContentAreaFactData)arg0).getAssessmentid()) &&
                pointsPossible.equals(((IrsTVContentAreaFactData)arg0).getPointsPossible()) &&
                programid.equals(((IrsTVContentAreaFactData)arg0).getProgramid()) &&
                currentResultid.equals(((IrsTVContentAreaFactData)arg0).getCurrentResultid()) &&
                pointsAttempted.equals(((IrsTVContentAreaFactData)arg0).getPointsAttempted()) &&
                pointsObtained.equals(((IrsTVContentAreaFactData)arg0).getPointsObtained()) &&
                recLevelid.equals(((IrsTVContentAreaFactData)arg0).getRecLevelid());
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

    public Long getAttr1id() {
        return attr1id;
    }

    public void setAttr1id(Long attr1id) {
        this.attr1id = attr1id;
    }

    public Long getAttr7id() {
        return attr7id;
    }

    public void setAttr7id(Long attr7id) {
        this.attr7id = attr7id;
    }

    public Long getAttr4id() {
        return attr4id;
    }

    public void setAttr4id(Long attr4id) {
        this.attr4id = attr4id;
    }

    public Long getAttr2id() {
        return attr2id;
    }

    public void setAttr2id(Long attr2id) {
        this.attr2id = attr2id;
    }

    public Long getAttr5id() {
        return attr5id;
    }

    public void setAttr5id(Long attr5id) {
        this.attr5id = attr5id;
    }

    public Long getAttr6id() {
        return attr6id;
    }

    public void setAttr6id(Long attr6id) {
        this.attr6id = attr6id;
    }

    public Long getAttr3id() {
        return attr3id;
    }

    public void setAttr3id(Long attr3id) {
        this.attr3id = attr3id;
    }

    public Long getAttr10id() {
        return attr10id;
    }

    public void setAttr10id(Long attr10id) {
        this.attr10id = attr10id;
    }

    public Long getGradeid() {
        return gradeid;
    }

    public void setGradeid(Long gradeid) {
        this.gradeid = gradeid;
    }

    public Long getAttr9id() {
        return attr9id;
    }

    public void setAttr9id(Long attr9id) {
        this.attr9id = attr9id;
    }

    public Long getAttr8id() {
        return attr8id;
    }

    public void setAttr8id(Long attr8id) {
        this.attr8id = attr8id;
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

    public Float getGradeEquivalent() {
        return gradeEquivalent;
    }

    public void setGradeEquivalent(Float gradeEquivalent) {
        this.gradeEquivalent = gradeEquivalent;
    }

    public Long getNormalCurveEquivalent() {
        return normalCurveEquivalent;
    }

    public void setNormalCurveEquivalent(Long normalCurveEquivalent) {
        this.normalCurveEquivalent = normalCurveEquivalent;
    }

    public Long getPercentageMastery() {
        return percentageMastery;
    }

    public void setPercentageMastery(Long percentageMastery) {
        this.percentageMastery = percentageMastery;
    }

    public Long getNationalPercentile() {
        return nationalPercentile;
    }

    public void setNationalPercentile(Long nationalPercentile) {
        this.nationalPercentile = nationalPercentile;
    }

    public Long getNationalStanine() {
        return nationalStanine;
    }

    public void setNationalStanine(Long nationalStanine) {
        this.nationalStanine = nationalStanine;
    }

    public Long getPredictedGed() {
        return predictedGed;
    }

    public void setPredictedGed(Long predictedGed) {
        this.predictedGed = predictedGed;
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

	public Long getAttr11id() {
		return attr11id;
	}

	public void setAttr11id(Long attr11id) {
		this.attr11id = attr11id;
	}

	public Long getAttr12id() {
		return attr12id;
	}

	public void setAttr12id(Long attr12id) {
		this.attr12id = attr12id;
	}

	public Long getAttr13id() {
		return attr13id;
	}

	public void setAttr13id(Long attr13id) {
		this.attr13id = attr13id;
	}

	public Long getAttr14id() {
		return attr14id;
	}

	public void setAttr14id(Long attr14id) {
		this.attr14id = attr14id;
	}

	public Long getAttr15id() {
		return attr15id;
	}

	public void setAttr15id(Long attr15id) {
		this.attr15id = attr15id;
	}

	public Long getAttr16id() {
		return attr16id;
	}

	public void setAttr16id(Long attr16id) {
		this.attr16id = attr16id;
	}
}