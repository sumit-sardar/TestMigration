package dto; 

/**
* Question information
* Vendor populates response
*
* @author Tai_Truong
*/
public class Question implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private String questionId = null;
    private String correctAnswer = null;
    private String response = null; 

    public Question() {
        this.questionId = null;
        this.correctAnswer = null;
        this.response = null;     	
    }
    
    public Question(String questionId, String correctAnswer, String response) {
        this.questionId = questionId;
        this.correctAnswer = correctAnswer;
        this.response = response;     	
    }

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
    
    
} 
