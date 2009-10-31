package dto; 

public class PasswordInformation implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;
    
    private String oldPassword = "";
    private String newPassword = "";
    private String confirmPassword = "";
    private String hintQuestion = "";
    private String hintQuestionId = "";
    private String hintAnswer = "";
    
    public PasswordInformation() {        
        this.hintQuestion = "Select a hint question";
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    public String getOldPassword() {
        return this.oldPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    public String getNewPassword() {
        return this.newPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String getConfirmPassword() {
        return this.confirmPassword;
    }
    
    public void setHintQuestion(String hintQuestion) {
        this.hintQuestion = hintQuestion;
    }
    
    public String getHintQuestion() {
        return this.hintQuestion;
    }
    
    public void setHintQuestionId(String hintQuestionId) {
        this.hintQuestionId = hintQuestionId;
    }
    
    public String getHintQuestionId() {
        return this.hintQuestionId;
    }
    
    public void setHintAnswer(String hintAnswer) {
        this.hintAnswer = hintAnswer;
    }
    
    public String getHintAnswer() {
        return this.hintAnswer;
    }
} 
