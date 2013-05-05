package dto; 

/**
* Subtest information
* OAS populates the fields
*
* @author Tai_Truong
*/
public class Subtest implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Integer subtestId;
    private String subtestName;
    private Question[] questions;
    
    public Subtest() {}

    public Subtest(Integer subtestId, String subtestName, Question[] questions) {
    	this.subtestId = subtestId;
    	this.subtestName = subtestName;
    	this.questions = questions;
    }
    
	public Integer getSubtestId() {
		return subtestId;
	}

	public void setSubtestId(Integer subtestId) {
		this.subtestId = subtestId;
	}

	public String getSubtestName() {
		return subtestName;
	}

	public void setSubtestName(String subtestName) {
		this.subtestName = subtestName;
	}

	public Question[] getQuestions() {
		return questions;
	}

	public void setQuestions(Question[] questions) {
		this.questions = questions;
	}
    
} 
