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
    private Subtest[] subtests; 
    
    public TestStructure(Integer testId, String testName, Subtest[] subtests) {
    	this.testId = testId;
    	this.testName = testName;
    	this.subtests = subtests;
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

	public Subtest[] getSubtests() {
		return subtests;
	}

	public void setSubtests(Subtest[] subtests) {
		this.subtests = subtests;
	}
        
} 
