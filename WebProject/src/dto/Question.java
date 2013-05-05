package dto; 

/**
* Question information
* OAS populates correctAnswer
* Vendor populates response
*
* @author Tai_Truong
*/
public class Question implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Integer questionId;
    private char correctAnswer;
    private char response; 

    public Question() {}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public char getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(char correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public char getResponse() {
		return response;
	}

	public void setResponse(char response) {
		this.response = response;
	}
    
    
} 
