package dto; 

/**
* Structure of the test
* Subtests is the list of subtests in the test
* OAS populates the fields
*
* @author Tai_Truong
*/
public class TestStructure implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Integer testId;
    private String testName;
    private Subject[] subjects; 
    
    public TestStructure(Integer testId, String testName, Subject[] subjects) {
    	this.testId = testId;
    	this.testName = testName;
    	this.subjects = subjects;
    }

    public TestStructure() {}
    
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

        
} 
