package dto;

import java.util.Date;

public class Session {
    static final long serialVersionUID = 1L;

    private String assignmentId = null;		// publicId
    private String testId = null;	
    private Integer id = null;
    private String name = null;
    private String accessCode = null;
    private String startTime = null;
    private String endTime = null;
    private Date startDate = null;
    private Date endDate = null;
    private Boolean hasBreak = null;
    private String timeZone = null;
    private String testLocation = null;
    
    private Accommodation[] accommodations = null;
    private Subtest[] subtests = null;
    private Student[] students = null;
    
    private String status = "OK";
    
	public Session() {
	}

	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
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
