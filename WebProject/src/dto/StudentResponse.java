package dto; 

/**
* Student response from vendor
* Vendor needs to populate all necessary fields in the StudentResponse object (i.e, student responses...)
*
* @author Tai_Truong
*/
public class StudentResponse implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Assignment assignment = null;
    private String status = null;		
    
    public StudentResponse() {
    	this.assignment = null;    	
    	this.status = null;
    }

    public StudentResponse(Assignment assignment) {
    	this.assignment = assignment;
    	this.status = "OK";
    }
    
	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    
} 
