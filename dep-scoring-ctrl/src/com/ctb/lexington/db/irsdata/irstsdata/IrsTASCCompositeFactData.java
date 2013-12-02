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
	//private String gradeEquivalent;
	private Long normalCurveEquivalent;
	//private Long percentageMastery;
	private Long nationalPercentile;
	//private Long nationalStanine;
	//private Long predictedGed;
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
	
	/*private Long attr1id;
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
    private Long attr9id;
	private Long attr8id;
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
    private Long attr37id;*/
    
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
                //gradeEquivalent.equals(((IrsTASCCompositeFactData)arg0).getGradeEquivalent()) &&
                normalCurveEquivalent.equals(((IrsTASCCompositeFactData)arg0).getNormalCurveEquivalent()) &&
                //percentageMastery.equals(((IrsTASCCompositeFactData)arg0).getPercentageMastery()) &&
                nationalPercentile.equals(((IrsTASCCompositeFactData)arg0).getNationalPercentile()) &&
                //nationalStanine.equals(((IrsTASCCompositeFactData)arg0).getNationalStanine()) &&
                //predictedGed.equals(((IrsTASCCompositeFactData)arg0).getPredictedGed()) &&
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
                
                /*attr1id.equals(((IrsTASCCompositeFactData)arg0).getAttr1id()) &&
                attr7id.equals(((IrsTASCCompositeFactData)arg0).getAttr7id()) &&
                attr4id.equals(((IrsTASCCompositeFactData)arg0).getAttr4id()) &&
                attr2id.equals(((IrsTASCCompositeFactData)arg0).getAttr2id()) &&
                attr5id.equals(((IrsTASCCompositeFactData)arg0).getAttr5id()) &&
                attr6id.equals(((IrsTASCCompositeFactData)arg0).getAttr6id()) &&
                attr3id.equals(((IrsTASCCompositeFactData)arg0).getAttr3id()) &&
                attr10id.equals(((IrsTASCCompositeFactData)arg0).getAttr10id()) &&
                attr11id.equals(((IrsTASCCompositeFactData)arg0).getAttr11id()) &&
                attr12id.equals(((IrsTASCCompositeFactData)arg0).getAttr12id()) &&
                attr13id.equals(((IrsTASCCompositeFactData)arg0).getAttr13id()) &&
                attr14id.equals(((IrsTASCCompositeFactData)arg0).getAttr14id()) &&
                attr15id.equals(((IrsTASCCompositeFactData)arg0).getAttr15id()) &&
                attr16id.equals(((IrsTASCCompositeFactData)arg0).getAttr16id()) &&
                attr9id.equals(((IrsTASCCompositeFactData)arg0).getAttr9id()) &&
                attr8id.equals(((IrsTASCCompositeFactData)arg0).getAttr8id()) &&
                attr17id.equals(((IrsTASCCompositeFactData)arg0).getAttr17id()) &&
                attr18id.equals(((IrsTASCCompositeFactData)arg0).getAttr18id()) &&
                attr19id.equals(((IrsTASCCompositeFactData)arg0).getAttr19id()) &&
                attr20id.equals(((IrsTASCCompositeFactData)arg0).getAttr20id()) &&
                attr21id.equals(((IrsTASCCompositeFactData)arg0).getAttr21id()) &&
                attr22id.equals(((IrsTASCCompositeFactData)arg0).getAttr22id()) &&
                attr24id.equals(((IrsTASCCompositeFactData)arg0).getAttr24id()) &&
                attr25id.equals(((IrsTASCCompositeFactData)arg0).getAttr25id()) &&
                attr26id.equals(((IrsTASCCompositeFactData)arg0).getAttr26id()) &&
                attr27id.equals(((IrsTASCCompositeFactData)arg0).getAttr27id()) &&
                attr28id.equals(((IrsTASCCompositeFactData)arg0).getAttr28id()) &&
                attr29id.equals(((IrsTASCCompositeFactData)arg0).getAttr29id()) &&
                attr30id.equals(((IrsTASCCompositeFactData)arg0).getAttr30id()) &&
                attr36id.equals(((IrsTASCCompositeFactData)arg0).getAttr36id()) &&
                attr37id.equals(((IrsTASCCompositeFactData)arg0).getAttr37id())*/
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

	/*public Long getAttr1id() {
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
	}*/

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

	/*public String getGradeEquivalent() {
		return gradeEquivalent;
	}

	public void setGradeEquivalent(String gradeEquivalent) {
		this.gradeEquivalent = gradeEquivalent;
	}*/

	public Long getNormalCurveEquivalent() {
		return normalCurveEquivalent;
	}

	public void setNormalCurveEquivalent(Long normalCurveEquivalent) {
		this.normalCurveEquivalent = normalCurveEquivalent;
	}

	/*public Long getPercentageMastery() {
		return percentageMastery;
	}

	public void setPercentageMastery(Long percentageMastery) {
		this.percentageMastery = percentageMastery;
	}*/

	public Long getNationalPercentile() {
		return nationalPercentile;
	}

	public void setNationalPercentile(Long nationalPercentile) {
		this.nationalPercentile = nationalPercentile;
	}

	/*public Long getNationalStanine() {
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
	}*/

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

	/*public Long getAttr11id() {
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
	}*/
}
