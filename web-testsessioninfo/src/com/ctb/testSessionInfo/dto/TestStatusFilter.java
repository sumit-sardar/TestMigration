package com.ctb.testSessionInfo.dto; 

import com.ctb.testSessionInfo.utils.FilterSortPageUtils;
import com.ctb.util.web.sanitizer.SanitizedFormField;

public class TestStatusFilter extends SanitizedFormField  
{ 
    static final long serialVersionUID = 1L;

    private String sessionName = null;
    private String sessionNameFilterType = null;
    private String sessionNumber = null;
    private String sessionNumberFilterType = null;
    private String loginId = null;
    private String loginIdFilterType = null;
    private String password = null;
    private String passwordFilterType = null;
    private String accessCode = null;
    private String accessCodeFilterType = null;

    public TestStatusFilter() 
    {
        this.sessionName = "";
        this.sessionNameFilterType = FilterSortPageUtils.FILTERTYPE_CONTAINS;
        this.sessionNumber = "";
        this.sessionNumberFilterType = FilterSortPageUtils.FILTERTYPE_CONTAINS;
        this.loginId = "";
        this.loginIdFilterType = FilterSortPageUtils.FILTERTYPE_CONTAINS;
        this.password = "";
        this.passwordFilterType = FilterSortPageUtils.FILTERTYPE_CONTAINS;
        this.accessCode = "";
        this.accessCodeFilterType = FilterSortPageUtils.FILTERTYPE_CONTAINS;
    }

    public void copyValues(TestStatusFilter src)
    {
        this.sessionName = src.getSessionName();
        this.sessionNameFilterType = src.getSessionNameFilterType();
        this.sessionNumber = src.getSessionNumber();
        this.sessionNumberFilterType = src.getSessionNumberFilterType();
        this.loginId = src.getLoginId();
        this.loginIdFilterType = src.getLoginIdFilterType();
        this.password = src.getPassword();
        this.passwordFilterType = src.getPasswordFilterType();
        this.accessCode = src.getAccessCode();
        this.accessCodeFilterType = src.getAccessCodeFilterType();
    }

    public void setSessionName(String sessionName)
    {
        this.sessionName = sessionName;
    }
    public String getSessionName()
    {
        return this.sessionName;
    }    
    public void setSessionNameFilterType(String sessionNameFilterType)
    {
        this.sessionNameFilterType = sessionNameFilterType;
    }
    public String getSessionNameFilterType()
    {
        return this.sessionNameFilterType;
    }    
    public void setSessionNumber(String sessionNumber)
    {
        this.sessionNumber = sessionNumber;
    }
    public String getSessionNumber()
    {
        return this.sessionNumber;
    }    
    public void setSessionNumberFilterType(String sessionNumberFilterType)
    {
        this.sessionNumberFilterType = sessionNumberFilterType;
    }
    public String getSessionNumberFilterType()
    {
        return this.sessionNumberFilterType;
    }    
    public void setLoginId(String loginId)
    {
        this.loginId = loginId;
    }
    public String getLoginId()
    {
        return this.loginId;
    }    
    public void setLoginIdFilterType(String loginIdFilterType)
    {
        this.loginIdFilterType = loginIdFilterType;
    }
    public String getLoginIdFilterType()
    {
        return this.loginIdFilterType;
    }    
    public void setPassword(String password)
    {
        this.password = password;
    }
    public String getPassword()
    {
        return this.password;
    }    
    public void setPasswordFilterType(String passwordFilterType)
    {
        this.passwordFilterType = passwordFilterType;
    }
    public String getPasswordFilterType()
    {
        return this.passwordFilterType;
    }    
     public void setAccessCode(String accessCode)
    {
        this.accessCode = accessCode;
    }
    public String getAccessCode()
    {
        return this.accessCode;
    }    
    public void setAccessCodeFilterType(String accessCodeFilterType)
    {
        this.accessCodeFilterType = accessCodeFilterType;
    }
    public String getAccessCodeFilterType()
    {
        return this.accessCodeFilterType;
    }    
    
} 
