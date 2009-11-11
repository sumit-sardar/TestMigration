package com.ctb.lexington.data;

/*
 * AuthenticationVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

import java.util.Date;

/**
 * @author <a href="mailto:Tai_Truong@ctb.com">Tai Truong</a>
 * @version
 * $Id$
 */
 
/**
this class still needs to be refactored and broken up into AuthenticationDetailVO.
*/ 
public class AuthenticationVO extends Object implements java.io.Serializable
{
    public static final String VO_LABEL       = "com.ctb.lexington.data.AuthenticationVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.data.AuthenticationVO.array";

    private String userId = "";
    private String userName = "";
    private String displayUserName = "";
    private String hintQuestionId = "";
    private String hintQuestion = "";
    private String hintAnswer = "";
    private boolean firstLogin = false;
    private Date passwordExpirationDate = null;
    private String displayNewMessage = "";


    /** Creates new StudentVO */
    public AuthenticationVO() {}

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
	public String getUserId()					
	{ 
		return this.userId; 
	}
	
    /**
     * Set the value of this property.
     *
     * @param userID_ The value to set the property to.
     * @return void
     */
    public void setUserId( String userId_ )	
    {
        if (userId_ != null)
        {
            this.userId = userId_.trim();
        }
    }
	
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getUserName()
    {
        return this.userName;
    }

    /**
     * Set the value of this property.
     *
     * @param userName1_ The value to set the property to.
     * @return void
     */
    public void setUserName(String userName_)
    {
        if (userName_ != null)
        {            
            this.userName = userName_.toLowerCase().trim();
        }
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getDisplayUserName()
    {
        return this.displayUserName;
    }

    /**
     * Set the value of this property.
     *
     * @param displayUserName_ The value to set the property to.
     * @return void
     */
    public void setDisplayUserName(String displayUserName_)
    {
        if (displayUserName_ != null)
        {
            this.displayUserName = displayUserName_.trim();
        }
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getPasswordHintQuestionId()
    {
        return this.hintQuestionId;
    }

    /**
     * Set the value of this property.
     *
     * @param hintQuestionId_ The value to set the property to.
     * @return void
     */
    public void setPasswordHintQuestionId(String hintQuestionId_)
    {
        if (hintQuestionId_ != null)
        {
            this.hintQuestionId = hintQuestionId_.trim();
        }
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getPasswordHintQuestion()
    {
        return this.hintQuestion;
    }

    /**
     * Set the value of this property.
     *
     * @param hintQuestion_ The value to set the property to.
     * @return void
     */
    public void setPasswordHintQuestion(String hintQuestion_)
    {        
        if (hintQuestion_ != null)
        {
            this.hintQuestion = hintQuestion_.trim();
        }
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getPasswordHintAnswer()
    {
        return this.hintAnswer;
    }

     /**
     * Set the value of this property.
     *
     * @param hintAnswer_ The value to set the property to.
     * @return void
     */
    public void setPasswordHintAnswer(String hintAnswer_)
    {
        if (hintAnswer_ != null)
        {
            this.hintAnswer = hintAnswer_.trim();
        }
    }

     /**
     * Get this property from this bean instance.
     *
     * @return boolean The value of the property.
     */
    public boolean getFirstLogin()
    {
        return this.firstLogin;
    }
    
     /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getDisplayNewMessage()
    {
        return this.displayNewMessage;
    }
    
     /**
     * Set the value of this property.
     *
     * @param firstLogin_ The value to set the property to.
     * @return void
     */
    public void setFirstLogin(boolean firstLogin_)
    {
        this.firstLogin=firstLogin_;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return boolean The value of the property.
     */
    public Date getPasswordExpirationDate()
    {
        return this.passwordExpirationDate;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return boolean The value of the property.
     */
    public void setPasswordExpirationDate( Date date_ )
    {
        this.passwordExpirationDate = date_;
    }
    
     /**
     * Set the value of this property.
     *
     * @param passwordExpirationDate_ The value to set the property to.
     * @return void
     */
    public void setFirstLogin(Date passwordExpirationDate_)
    {
        this.passwordExpirationDate=passwordExpirationDate_;
    }
    
     /**
     * Set the value of this property.
     *
     * @param displayNewMessage_ The value to set the property to.
     * @return void
     */
    public void setDisplayNewMessage(String displayNewMessage_)
    {
        this.displayNewMessage = displayNewMessage_;
    }

}
