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
    private String subtestScoringStatus;
    private Long attr17id;
    private Long attr18id;
    private Long attr19id;
    private Long attr20id;
    private Long attr21id;
    private Long attr22id;
    private Long attr24id;
    private Long attr25id;
    private Long attr26id;
    private Long attr27id;
    private Long attr28id;
    private Long attr29id;
    private Long attr30id;
    private Long attr36id;
    private Long attr37id;
    
    public boolean equals(Object arg0) {
			return 
				contentAreaid.equals(((IrsTASCContentAreaFactData)arg0).getContentAreaid()) &&
                orgNodeid.equals(((IrsTASCContentAreaFactData)arg0).getOrgNodeid()) &&
                studentid.equals(((IrsTASCContentAreaFactData)arg0).getStudentid()) &&
                formid.equals(((IrsTASCContentAreaFactData)arg0).getFormid()) &&
                sessionid.equals(((IrsTASCContentAreaFactData)arg0).getSessionid()) &&
                attr1id.equals(((IrsTASCContentAreaFactData)arg0).getAttr1id()) &&
                attr7id.equals(((IrsTASCContentAreaFactData)arg0).getAttr7id()) &&
                attr4id.equals(((IrsTASCContentAreaFactData)arg0).getAttr4id()) &&
                attr2id.equals(((IrsTASCContentAreaFactData)arg0).getAttr2id()) &&
                attr5id.equals(((IrsTASCContentAreaFactData)arg0).getAttr5id()) &&
                attr6id.equals(((IrsTASCContentAreaFactData)arg0).getAttr6id()) &&
                attr3id.equals(((IrsTASCContentAreaFactData)arg0).getAttr3id()) &&
                attr10id.equals(((IrsTASCContentAreaFactData)arg0).getAttr10id()) &&
                attr11id.equals(((IrsTASCContentAreaFactData)arg0).getAttr11id()) &&
                attr12id.equals(((IrsTASCContentAreaFactData)arg0).getAttr12id()) &&
                attr13id.equals(((IrsTASCContentAreaFactData)arg0).getAttr13id()) &&
                attr14id.equals(((IrsTASCContentAreaFactData)arg0).getAttr14id()) &&
                attr15id.equals(((IrsTASCContentAreaFactData)arg0).getAttr15id()) &&
                attr16id.equals(((IrsTASCContentAreaFactData)arg0).getAttr16id()) &&
                gradeid.equals(((IrsTASCContentAreaFactData)arg0).getGradeid()) &&
                attr9id.equals(((IrsTASCContentAreaFactData)arg0).getAttr9id()) &&
                attr8id.equals(((IrsTASCContentAreaFactData)arg0).getAttr8id()) &&
                nrsLevelid.equals(((IrsTASCContentAreaFactData)arg0).getNrsLevelid()) &&
                scaleScore.equals(((IrsTASCContentAreaFactData)arg0).getScaleScore()) &&
                gradeEquivalent.equals(((IrsTASCContentAreaFactData)arg0).getGradeEquivalent()) &&
                normalCurveEquivalent.equals(((IrsTASCContentAreaFactData)arg0).getNormalCurveEquivalent()) &&
                percentageMastery.equals(((IrsTASCContentAreaFactData)arg0).getPercentageMastery()) &&
                nationalPercentile.equals(((IrsTASCContentAreaFactData)arg0).getNationalPercentile()) &&
                nationalStanine.equals(((IrsTASCContentAreaFactData)arg0).getNationalStanine()) &&
                predictedGed.equals(((IrsTASCContentAreaFactData)arg0).getPredictedGed()) &&
                testStartTimestamp.equals(((IrsTASCContentAreaFactData)arg0).getTestStartTimestamp()) &&
                testCompletionTimestamp.equals(((IrsTASCContentAreaFactData)arg0).getTestCompletionTimestamp()) &&
                assessmentid.equals(((IrsTASCContentAreaFactData)arg0).getAssessmentid()) &&
                pointsPossible.equals(((IrsTASCContentAreaFactData)arg0).getPointsPossible()) &&
                programid.equals(((IrsTASCContentAreaFactData)arg0).getProgramid()) &&
                currentResultid.equals(((IrsTASCContentAreaFactData)arg0).getCurrentResultid()) &&
                pointsAttempted.equals(((IrsTASCContentAreaFactData)arg0).getPointsAttempted()) &&
                pointsObtained.equals(((IrsTASCContentAreaFactData)arg0).getPointsObtained()) &&
                recLevelid.equals(((IrsTASCContentAreaFactData)arg0).getRecLevelid()) && 
                
                
                subtestScoringStatus.equals(((IrsTASCContentAreaFactData)arg0).getSubtestScoringStatus()) &&
                attr17id.equals(((IrsTASCContentAreaFactData)arg0).getAttr17id()) &&
                attr18id.equals(((IrsTASCContentAreaFactData)arg0).getAttr18id()) &&
                attr19id.equals(((IrsTASCContentAreaFactData)arg0).getAttr19id()) &&
                attr20id.equals(((IrsTASCContentAreaFactData)arg0).getAttr20id()) &&
                attr21id.equals(((IrsTASCContentAreaFactData)arg0).getAttr21id()) &&
                attr22id.equals(((IrsTASCContentAreaFactData)arg0).getAttr22id()) &&
                attr24id.equals(((IrsTASCContentAreaFactData)arg0).getAttr24id()) &&
                attr25id.equals(((IrsTASCContentAreaFactData)arg0).getAttr25id()) &&
                attr26id.equals(((IrsTASCContentAreaFactData)arg0).getAttr26id()) &&
                attr27id.equals(((IrsTASCContentAreaFactData)arg0).getAttr27id()) &&
                attr28id.equals(((IrsTASCContentAreaFactData)arg0).getAttr28id()) &&
                attr29id.equals(((IrsTASCContentAreaFactData)arg0).getAttr29id()) &&
                attr30id.equals(((IrsTASCContentAreaFactData)arg0).getAttr30id()) &&
                attr36id.equals(((IrsTASCContentAreaFactData)arg0).getAttr36id()) &&
                attr37id.equals(((IrsTASCContentAreaFactData)arg0).getAttr37id());
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
	
	public String getSubtestScoringStatus() {
		return subtestScoringStatus;
	}
	
	public void setSubtestScoringStatus(String subtestScoringStatus) {
		this.subtestScoringStatus = subtestScoringStatus;
	}

	public Long getAttr17id() {
		return attr17id;
	}

	public void setAttr17id(Long attr17id) {
		this.attr17id = attr17id;
	}

	public Long getAttr18id() {
		return attr18id;
	}

	public void setAttr18id(Long attr18id) {
		this.attr18id = attr18id;
	}

	public Long getAttr19id() {
		return attr19id;
	}

	public void setAttr19id(Long attr19id) {
		this.attr19id = attr19id;
	}

	public Long getAttr20id() {
		return attr20id;
	}

	public void setAttr20id(Long attr20id) {
		this.attr20id = attr20id;
	}

	public Long getAttr21id() {
		return attr21id;
	}

	public void setAttr21id(Long attr21id) {
		this.attr21id = attr21id;
	}

	public Long getAttr22id() {
		return attr22id;
	}

	public void setAttr22id(Long attr22id) {
		this.attr22id = attr22id;
	}

	public Long getAttr24id() {
		return attr24id;
	}

	public void setAttr24id(Long attr24id) {
		this.attr24id = attr24id;
	}

	public Long getAttr25id() {
		return attr25id;
	}

	public void setAttr25id(Long attr25id) {
		this.attr25id = attr25id;
	}

	public Long getAttr26id() {
		return attr26id;
	}

	public void setAttr26id(Long attr26id) {
		this.attr26id = attr26id;
	}

	public Long getAttr27id() {
		return attr27id;
	}

	public void setAttr27id(Long attr27id) {
		this.attr27id = attr27id;
	}

	public Long getAttr28id() {
		return attr28id;
	}

	public void setAttr28id(Long attr28id) {
		this.attr28id = attr28id;
	}

	public Long getAttr29id() {
		return attr29id;
	}

	public void setAttr29id(Long attr29id) {
		this.attr29id = attr29id;
	}

	public Long getAttr30id() {
		return attr30id;
	}

	public void setAttr30id(Long attr30id) {
		this.attr30id = attr30id;
	}

	public Long getAttr36id() {
		return attr36id;
	}

	public void setAttr36id(Long attr36id) {
		this.attr36id = attr36id;
	}

	public Long getAttr37id() {
		return attr37id;
	}

	public void setAttr37id(Long attr37id) {
		this.attr37id = attr37id;
	}
}