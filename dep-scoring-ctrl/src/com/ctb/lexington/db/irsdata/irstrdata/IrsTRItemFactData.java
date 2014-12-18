package com.ctb.lexington.db.irsdata.irstrdata;

import com.ctb.lexington.db.record.Persistent;
import java.util.Date;

/**
 * @author Somenath_Chakroborti
 *
 */
public class IrsTRItemFactData implements Persistent{
	
    private Long factid;
    private Long studentid;
    private Long formid;
    private Long sessionid;
    private Long gradeid;
    private Long levelid;
    private Long pointsObtained;
    private Date itemResponseTimestamp;
    private Date testStartTimestamp;
    private Date testCompletionTimestamp;
    private Long responseid;
    private Long itemid;
    private Long orgNodeid;
    private Long assessmentid;
    private Long pointsPossible;
    private Long programid;
    private Long currentResultid;
    private String subtestName;
    
    public boolean equals(Object arg0) {
			return 
				itemid.equals(((IrsTRItemFactData)arg0).getItemid()) &&
                orgNodeid.equals(((IrsTRItemFactData)arg0).getOrgNodeid()) &&
                studentid.equals(((IrsTRItemFactData)arg0).getStudentid()) &&
                formid.equals(((IrsTRItemFactData)arg0).getFormid()) &&
                sessionid.equals(((IrsTRItemFactData)arg0).getSessionid()) &&
                gradeid.equals(((IrsTRItemFactData)arg0).getGradeid()) &&
                testStartTimestamp.equals(((IrsTRItemFactData)arg0).getTestStartTimestamp()) &&
                testCompletionTimestamp.equals(((IrsTRItemFactData)arg0).getTestCompletionTimestamp()) &&
                assessmentid.equals(((IrsTRItemFactData)arg0).getAssessmentid()) &&
                pointsPossible.equals(((IrsTRItemFactData)arg0).getPointsPossible()) &&
                programid.equals(((IrsTRItemFactData)arg0).getProgramid()) &&
                currentResultid.equals(((IrsTRItemFactData)arg0).getCurrentResultid()) &&
                pointsObtained.equals(((IrsTRItemFactData)arg0).getPointsObtained()) &&
                responseid.equals(((IrsTRItemFactData)arg0).getResponseid());
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


    public Long getLevelid() {
        return levelid;
    }

    public void setLevelid(Long levelid) {
        this.levelid = levelid;
    }

    public Long getPointsObtained() {
        return pointsObtained;
    }

    public void setPointsObtained(Long pointsObtained) {
        this.pointsObtained = pointsObtained;
    }

    public Date getItemResponseTimestamp() {
        return itemResponseTimestamp;
    }

    public void setItemResponseTimestamp(Date itemResponseTimestamp) {
        this.itemResponseTimestamp = itemResponseTimestamp;
    }

    public Date getTestCompletionTimestamp() {
        return testCompletionTimestamp;
    }

    public void setTestCompletionTimestamp(Date testCompletionTimestamp) {
        this.testCompletionTimestamp = testCompletionTimestamp;
    }

    public Long getResponseid() {
        return responseid;
    }

    public void setResponseid(Long responseid) {
        this.responseid = responseid;
    }

    public Long getItemid() {
        return itemid;
    }

    public void setItemid(Long itemid) {
        this.itemid = itemid;
    }

    public Long getOrgNodeid() {
        return orgNodeid;
    }

    public void setOrgNodeid(Long orgNodeid) {
        this.orgNodeid = orgNodeid;
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

    public String getSubtestName() {
        return this.subtestName;
    }
    public void setSubtestName(String subtestName) {
        this.subtestName = subtestName;
    }
}