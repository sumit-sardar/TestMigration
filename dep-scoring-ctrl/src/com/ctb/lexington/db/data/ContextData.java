package com.ctb.lexington.db.data; 

import com.ctb.lexington.db.irsdata.IrsDemographicData;
import java.sql.Timestamp;

public class ContextData 
{
    private Long assessmentId;
    private Long currentResultId;
    private Long gradeId;
    private Long orgNodeId;
    private Long programId;
    private Long sessionId;
    private Long studentId;
    private Timestamp testCompletionTimestamp;
    private Timestamp testStartTimestamp;
    private IrsDemographicData demographicData;
    private String assessmentType;
    
    public String getAssessmentType() {
        return this.assessmentType;
    }
    
    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }
    
	public IrsDemographicData getDemographicData() {
		return demographicData;
	}
	
	public void setDemographicData(IrsDemographicData demographicData) {
		this.demographicData = demographicData;
	}
	/**
	 * @return Returns the assessmentId.
	 */
	public Long getAssessmentId() {
		return assessmentId;
	}
	/**
	 * @param assessmentId The assessmentId to set.
	 */
	public void setAssessmentId(Long assessmentId) {
		this.assessmentId = assessmentId;
	}
	/**
	 * @return Returns the currentResultId.
	 */
	public Long getCurrentResultId() {
		return currentResultId;
	}
	/**
	 * @param currentResultId The currentResultId to set.
	 */
	public void setCurrentResultId(Long currentResultId) {
		this.currentResultId = currentResultId;
	}
	/**
	 * @return Returns the gradeId.
	 */
	public Long getGradeId() {
		return gradeId;
	}
	/**
	 * @param gradeId The gradeId to set.
	 */
	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}
	/**
	 * @return Returns the orgNodeId.
	 */
	public Long getOrgNodeId() {
		return orgNodeId;
	}
	/**
	 * @param orgNodeId The orgNodeId to set.
	 */
	public void setOrgNodeId(Long orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	/**
	 * @return Returns the programId.
	 */
	public Long getProgramId() {
		return programId;
	}
	/**
	 * @param programId The programId to set.
	 */
	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	/**
	 * @return Returns the sessionId.
	 */
	public Long getSessionId() {
		return sessionId;
	}
	/**
	 * @param sessionId The sessionId to set.
	 */
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * @return Returns the studentId.
	 */
	public Long getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId The studentId to set.
	 */
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return Returns the testCompletionTimestamp.
	 */
	public Timestamp getTestCompletionTimestamp() {
		return testCompletionTimestamp;
	}
	/**
	 * @param testCompletionTimestamp The testCompletionTimestamp to set.
	 */
	public void setTestCompletionTimestamp(Timestamp testCompletionTimestamp) {
		this.testCompletionTimestamp = testCompletionTimestamp;
	}
    /**
	 * @return Returns the testStartTimestamp.
	 */
	public Timestamp getTestStartTimestamp() {
		return testStartTimestamp;
	}
	/**
	 * @param testStartTimestamp The testStartTimestamp to set.
	 */
	public void setTestStartTimestamp(Timestamp testStartTimestamp) {
		this.testStartTimestamp = testStartTimestamp;
	}
} 
