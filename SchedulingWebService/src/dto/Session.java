package dto;

import java.util.Date;

/**
 * This is the second object which passed into scheduling web service to perform TN scheduling session for Acuity application 
 * This object contains information for session, accommodations, sub-tests, and students
 * The values which required from ACUITY through input are: 
 * 				productId, level, sessionName, startTime, endTime, startDate, endDate, hasBreak, timeZone, subtests, students   
 * The values which OAS platform populated in return are:  
 * 				status, sessionId, accessCode
 * 
 * @author Tai_Truong
 */
public class Session implements java.io.Serializable {
    static final long serialVersionUID = 1L;
 
    private Integer sessionId = null;		// null for creating new session - not null for update existing session.
    private Integer testId = null;			// internal to OAS
    private Integer productId; 				// input from Acuity
    private String level ;   				// input from Acuity 
    private String sessionName = null;		// 64 chars
    private String accessCode = null;		// 32 chars
    private String startTime = null;
    private String endTime = null;
    private String startDate = null;
    private String endDate = null;
    private Boolean hasBreak = null;
    private String timeZone = null;			// 255 chars
    private String testLocation = null;		// 64 chars
    
    private Subtest[] subtests = null;
    private Student[] students = null;
    private Accommodation accom = null;
    
    private String status = null;			
    
    
	public Session() {
	}

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}
	
	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}
		
	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Boolean getHasBreak() {
		return hasBreak;
	}

	public void setHasBreak(Boolean hasBreak) {
		this.hasBreak = hasBreak;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getTestLocation() {
		return testLocation;
	}

	public void setTestLocation(String testLocation) {
		this.testLocation = testLocation;
	}

	public Subtest[] getSubtests() {
		return subtests;
	}

	public void setSubtests(Subtest[] subtests) {
		this.subtests = subtests;
	}

	public Student[] getStudents() {
		return students;
	}

	public void setStudents(Student[] students) {
		this.students = students;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Accommodation getAccom() {
		return accom;
	}

	public void setAccom(Accommodation accom) {
		this.accom = accom;
	}

	public void setStatusNonOverwritten(String status) {
		if (this.status == null) 
			this.status = status;
	}
	
}
