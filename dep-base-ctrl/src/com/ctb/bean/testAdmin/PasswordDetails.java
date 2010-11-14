package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;
/**
 * Data bean representing the partial contents of the OAS.User and OAS.Password_Hint_Question table 
 * 
 * 
 * @author Tata Consultency Services
 */
public class PasswordDetails extends CTBBean
{ 
    private String resetPassword;

    private Integer passwordHintQuestionId;

    private String passwordHintAnswer;

    private String password;
    
    private Date passwordExpDate;
    
    private String displayNewMesage;
    
    

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPasswordHintAnswer(String passwordHintAnswer)
    {
        this.passwordHintAnswer = passwordHintAnswer;
    }

    public String getPasswordHintAnswer()
    {
        return this.passwordHintAnswer;
    }

    public void setPasswordHintQuestionId(Integer passwordHintQuestionId)
    {
        this.passwordHintQuestionId = passwordHintQuestionId;
    }

    public Integer getPasswordHintQuestionId()
    {
        return this.passwordHintQuestionId;
    }

    public void setResetPassword(String resetPassword)
    {
        this.resetPassword = resetPassword;
    }

    public String getResetPassword()
    {
        return this.resetPassword;
    }
  
    public void setPasswordExpDate(Date passwordExpDate)
    {
        this.passwordExpDate = passwordExpDate;
    }

    public Date getPasswordExpDate()
    {
        return this.passwordExpDate;
    }
    
    public void setDisplayNewMesage(String displayNewMesage)
    {
        this.displayNewMesage = displayNewMesage;
    }

    public String getDisplayNewMesage()
    {
        return this.displayNewMesage;
    }
  
} 
