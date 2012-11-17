package dto;

import java.util.Date;

/**
 * This object contains test roster status for student
 * @author Tai_Truong
 */
public class StudentStatus implements java.io.Serializable {
    static final long serialVersionUID = 1L;

    private Integer studentId = null; 		// required
    private long acuityQueueKey = 0L;
    private String status = null;			   
    private String rosterStatus = null;
    
	public StudentStatus() {
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public long getAcuityQueueKey() {
		return acuityQueueKey;
	}

	public void setAcuityQueueKey(long acuityQueueKey) {
		this.acuityQueueKey = acuityQueueKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getRosterStatus() {
		return rosterStatus;
	}

	public void setRosterStatus(String status) {
		this.rosterStatus = status;
	}
	
}
