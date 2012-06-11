package dto;

import java.util.Date;

/**
 * This is the second object which passed into scheduling web service to perform TN scheduling session for Acuity application 
 * This object contains information for session, accommodations, sub-tests, and students
 * The values which required from ACUITY through input are: 
 * 				assignmentId, testId, name, startTime, endTime, startDate, endDate, hasBreak, timeZone, subtests, students   
 * The values which OAS platform populated in return are:  
 * 				status, accessCode
 * 
 * @author Tai_Truong
 */
public class Session implements java.io.Serializable {
    static final long serialVersionUID = 1L;

    private String assignmentId = null;		// publicId - 32 chars
    private Integer testId = null;	
    private Integer id = null;
    private String name = null;				// 64 chars
    private String accessCode = null;		// 32 chars
    private String startTime = null;
    private String endTime = null;
    private Date startDate = null;
    private Date endDate = null;
    private Boolean hasBreak = null;
    private String timeZone = null;			// 255 chars
    private String testLocation = null;		// 64 chars
    
    private Accommodation[] accommodations = null;
    private Subtest[] subtests = null;
    private Student[] students = null;
    
    private String status = "OK";			// 32 chars
    
	public Session() {
	}

	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
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

	public Accommodation[] getAccommodations() {
		return accommodations;
	}

	public void setAccommodations(Accommodation[] accommodations) {
		this.accommodations = accommodations;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
	
}
