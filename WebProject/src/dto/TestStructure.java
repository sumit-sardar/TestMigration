package dto; 

/**
* Structure of the test
* Subtests is the list of subtests in the test
* OAS populates the fields
* status stores error message otherwise set to 'OK'
*
* @author Tai_Truong
*/
public class TestStructure implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Integer testId = null;
    private String testName = null;
    private Subject[] subjects = null; 
    private String status = null;		
    
    public TestStructure() {
    	this.testId = null;
    	this.testName = null;
    	this.subjects = null;
    	this.status = null;
    }
    public TestStructure(Integer testId, String testName, Subject[] subjects) {
    	this.testId = testId;
    	this.testName = testName;
    	this.subjects = subjects;
    	this.status = "OK";
    }

    public TestStructure(String error) {
    	this.status = error;
    }
    
	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public Subject[] getSubjects() {
		return subjects;
	}

	public void setSubjects(Subject[] subjects) {
		this.subjects = subjects;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

        
} 
