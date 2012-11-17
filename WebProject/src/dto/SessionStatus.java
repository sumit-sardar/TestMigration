package dto;

import java.util.Date;

/**
 * This is the second object which passed into test roster status web service
 * @author Tai_Truong
 */
public class SessionStatus implements java.io.Serializable {
    static final long serialVersionUID = 1L;
 
    private Integer sessionId = null;			// required		
    private StudentStatus[] students = null;	// required		
    private String status = null;			
    
    
	public SessionStatus() {
	}

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}
	
	public StudentStatus[] getStudents() {
		return students;
	}

	public void setStudents(StudentStatus[] students) {
		this.students = students;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
