package com.ctb.lexington.db.irsdata.irstvdata;

import com.ctb.lexington.db.record.Persistent;
import java.util.Date;

/**
 * @author Rama_Rao
 *
 */
public class IrsTVSecObjFactData implements Persistent{

    private Long factid;
    private Long secObjid;
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


    public boolean equals(Object arg0) {
			return 
				secObjid.equals(((IrsTVSecObjFactData)arg0).getSecObjid()) &&
                orgNodeid.equals(((IrsTVSecObjFactData)arg0).getOrgNodeid()) &&
                studentid.equals(((IrsTVSecObjFactData)arg0).getStudentid()) &&
                formid.equals(((IrsTVSecObjFactData)arg0).getFormid()) &&
                sessionid.equals(((IrsTVSecObjFactData)arg0).getSessionid()) &&
                attr1id.equals(((IrsTVSecObjFactData)arg0).getAttr1id()) &&
                attr7id.equals(((IrsTVSecObjFactData)arg0).getAttr7id()) &&
                attr4id.equals(((IrsTVSecObjFactData)arg0).getAttr4id()) &&
                attr2id.equals(((IrsTVSecObjFactData)arg0).getAttr2id()) &&
                attr5id.equals(((IrsTVSecObjFactData)arg0).getAttr5id()) &&
                attr6id.equals(((IrsTVSecObjFactData)arg0).getAttr6id()) &&
                attr3id.equals(((IrsTVSecObjFactData)arg0).getAttr3id()) &&
                attr10id.equals(((IrsTVSecObjFactData)arg0).getAttr10id()) &&
                attr11id.equals(((IrsTVSecObjFactData)arg0).getAttr11id()) &&
                attr12id.equals(((IrsTVSecObjFactData)arg0).getAttr12id()) &&
                attr13id.equals(((IrsTVSecObjFactData)arg0).getAttr13id()) &&
                attr14id.equals(((IrsTVSecObjFactData)arg0).getAttr14id()) &&
                attr15id.equals(((IrsTVSecObjFactData)arg0).getAttr15id()) &&
                attr16id.equals(((IrsTVSecObjFactData)arg0).getAttr16id()) &&
                gradeid.equals(((IrsTVSecObjFactData)arg0).getGradeid()) &&
                attr9id.equals(((IrsTVSecObjFactData)arg0).getAttr9id()) &&
                attr8id.equals(((IrsTVSecObjFactData)arg0).getAttr8id()) &&
                testStartTimestamp.equals(((IrsTVSecObjFactData)arg0).getTestStartTimestamp()) &&
                testCompletionTimestamp.equals(((IrsTVSecObjFactData)arg0).getTestCompletionTimestamp()) &&
                assessmentid.equals(((IrsTVSecObjFactData)arg0).getAssessmentid()) &&
                pointsPossible.equals(((IrsTVSecObjFactData)arg0).getPointsPossible()) &&
                programid.equals(((IrsTVSecObjFactData)arg0).getProgramid()) &&
                currentResultid.equals(((IrsTVSecObjFactData)arg0).getCurrentResultid()) &&
                pointsAttempted.equals(((IrsTVSecObjFactData)arg0).getPointsAttempted()) &&
                pointsObtained.equals(((IrsTVSecObjFactData)arg0).getPointsObtained()) &&
                masteryLevelid.equals(((IrsTVSecObjFactData)arg0).getMasteryLevelid());
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
    public String getSubtestName() {
        return this.subtestName;
    }
    public void setSubtestName(String subtestName) {
        this.subtestName = subtestName;
    }
}
