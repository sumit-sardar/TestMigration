package dto; 

/**
* Subtest information
* OAS populates the fields
*
* @author Tai_Truong
*/
public class Subject implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Integer subtestId = null;
    private String subtestName = null;
    private Question[] questions = null;
    
    public Subject() {
    	this.subtestId = null;
    	this.subtestName = null;
    	this.questions = null;    	
    }

    public Subject(Integer subtestId, String subtestName, Question[] questions) {
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
