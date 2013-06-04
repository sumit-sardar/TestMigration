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
    
    private Integer questionId = null;
    private char correctAnswer = '-';
    private char response = '-'; 

    public Question() {
        this.questionId = null;
        this.correctAnswer = '-';
        this.response = '-';     	
    }
    public Question(Integer questionId, char correctAnswer, char response) {
        this.questionId = questionId;
        this.correctAnswer = correctAnswer;
        this.response = response;     	
    }

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
