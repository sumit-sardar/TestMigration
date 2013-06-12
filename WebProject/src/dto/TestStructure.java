package dto; 

/**
* Structure of the test
* contentAreas is a list of content areas in the test
* status stores error message otherwise set to 'OK'
*
* @author Tai_Truong
*/
public class TestStructure implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Integer testId = null;
    private String testName = null;
    private ContentArea[] contentAreas = null; 
    private String status = null;		
    
    public TestStructure() {
    	this.testId = null;
    	this.testName = null;
    	this.contentAreas = null;
    	this.status = null;
    }
    public TestStructure(Integer testId, String testName, ContentArea[] contentAreas) {
    	this.testId = testId;
    	this.testName = testName;
    	this.contentAreas = contentAreas;
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
	
	public ContentArea[] getContentAreas() {
		return contentAreas;
	}
	
	public void setContentAreas(ContentArea[] contentAreas) {
		this.contentAreas = contentAreas;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

        
} 
