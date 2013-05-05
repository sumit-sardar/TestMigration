package dto; 

/**
* Student response from vendor
* Vendor needs to populate response field in Question object
*
* @author Tai_Truong
*/
public class StudentResponse implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Integer testId;
    private Session session;
    
    public StudentResponse() {}

    public StudentResponse(Integer testId, Session session) {
    	this.testId = testId;
    	this.session = session;
    }
    
	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
    
    
} 
