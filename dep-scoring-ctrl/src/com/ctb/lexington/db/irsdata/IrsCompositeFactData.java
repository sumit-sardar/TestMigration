package com.ctb.lexington.db.irsdata; 

import com.ctb.lexington.db.record.Persistent;
/**
 * @author Rama_Rao
 *
 */
public class IrsCompositeFactData implements Persistent{

    private Long factid;
    private Long compositeid;
    private Long orgNodeid;
    private Long studentid;
    private Long formid;
    private Long sessionid;
    private Long attr1id;
    private Long attr2id;
    private Long attr3id;
    private Long attr4id;
    private Long attr5id;
    private Long attr6id;
    private Long attr7id;
    private Long attr8id;
    private Long attr9id;
    private Long attr10id;
    private Long gradeid;    
    private Long nrsLevelid;
    private Long scaleScore;
    private Long gradeEquivalent;
    private Long normalCurveEquivalent;
    private Long percentageMastery;
    private Long nationalPercentile;
    private Long nationalStanine;
    private Long predictedGed;
    //private Date testCompletionTimestamp;
    private Long assessmentid;
    private Long pointsPossible;
    private Long programid;
    private Long currentResultid;


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

    public Long getGradeEquivalent() {
        return gradeEquivalent;
    }

    public void setGradeEquivalent(Long gradeEquivalent) {
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
}


/*public class IrsCompositeFactData implements Persistent{

    private Long factid;
    private Long compositeid;
    private Long orgNodeid;
    private Long studentid;
    private Long formid;
    private Long sessionid;
    private Long ellid;
    private Long lepid;
    private Long genderid;
    private Long ethnicityid;
    private Long iepid;
    private Long lfstatid;
    private Long frlunchid;
    private Long sec504id;
    private Long gradeid;
    private Long otaccid;
    private Long migrantid;
    private Long nrsLevel;
    private Long scaleScore;
    private Long gradeEquivalent;
    private Long normalCurveEquivalent;
    private Long percentageMastery;
    private Long nationalPercentile;
    private Long nationalStanine;
    private Long predictedGed;
  //  private Date testCompletionTimestamp;
    private Long assessmentid;
    private Long pointsPossible;

   

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

    public Long getEllid() {
        return ellid;
    }

    public void setEllid(Long ellid) {
        this.ellid = ellid;
    }

    public Long getLepid() {
        return lepid;
    }

    public void setLepid(Long lepid) {
        this.lepid = lepid;
    }

    public Long getGenderid() {
        return genderid;
    }

    public void setGenderid(Long genderid) {
        this.genderid = genderid;
    }

    public Long getEthnicityid() {
        return ethnicityid;
    }

    public void setEthnicityid(Long ethnicityid) {
        this.ethnicityid = ethnicityid;
    }

    public Long getIepid() {
        return iepid;
    }

    public void setIepid(Long iepid) {
        this.iepid = iepid;
    }

    public Long getLfstatid() {
        return lfstatid;
    }

    public void setLfstatid(Long lfstatid) {
        this.lfstatid = lfstatid;
    }

    public Long getFrlunchid() {
        return frlunchid;
    }

    public void setFrlunchid(Long frlunchid) {
        this.frlunchid = frlunchid;
    }

    public Long getSec504id() {
        return sec504id;
    }

    public void setSec504id(Long sec504id) {
        this.sec504id = sec504id;
    }

    public Long getGradeid() {
        return gradeid;
    }

    public void setGradeid(Long gradeid) {
        this.gradeid = gradeid;
    }

    public Long getOtaccid() {
        return otaccid;
    }

    public void setOtaccid(Long otaccid) {
        this.otaccid = otaccid;
    }

    public Long getMigrantid() {
        return migrantid;
    }

    public void setMigrantid(Long migrantid) {
        this.migrantid = migrantid;
    }

    public Long getNrsLevel() {
        return nrsLevel;
    }

    public void setNrsLevel(Long nrsLevel) {
        this.nrsLevel = nrsLevel;
    }

    public Long getScaleScore() {
        return scaleScore;
    }

    public void setScaleScore(Long scaleScore) {
        this.scaleScore = scaleScore;
    }

    public Long getGradeEquivalent() {
        return gradeEquivalent;
    }

    public void setGradeEquivalent(Long gradeEquivalent) {
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

    public Long getPointsPossible() {
        return pointsPossible;
    }

    public void setPointsPossible(Long pointsPossible) {
        this.pointsPossible = pointsPossible;
    }
}
*/