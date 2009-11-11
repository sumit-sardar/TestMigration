/*
 * PasswordHintQuestionBean.java
 *
 * Created on April 10, 2002, 7:50 PM
 */

package com.ctb.lexington.data;

/**
 *
 * @author  KBletzer
 * @version 
 */
public class PasswordHintQuestionVO  implements java.io.Serializable
{

    private String passwordHintQuestion;
    
    private int passwordHintQuestionID;
    
    /** Creates new PasswordHintQuestionVO */
    public PasswordHintQuestionVO() 
    {
    }

    public int getPasswordHintQuestionID() 
    {
        return this.passwordHintQuestionID;
    }
    
    public void setPasswordHintQuestionID(int passwordHintQuestionID_)
    {
        this.passwordHintQuestionID = passwordHintQuestionID_;
    }
    
    public void setPasswordHintQuestion(String passwordHintQuestion_)
    {
        this.passwordHintQuestion = passwordHintQuestion_;
    }
    
    public String getPasswordHintQuestion()
    {
        return this.passwordHintQuestion;
    }
    
}
