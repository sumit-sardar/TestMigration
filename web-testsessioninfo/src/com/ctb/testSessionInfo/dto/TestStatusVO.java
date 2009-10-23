package com.ctb.testSessionInfo.dto; 

import com.ctb.bean.testAdmin.ProgramStatusSession;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestStatus;
import java.util.Date;

public class TestStatusVO implements java.io.Serializable
{     
    static final long serialVersionUID = 1L;

    private Integer testAdminId = null;
    private String sessionName = null;
    private String sessionNumber = null;
    private String loginId = null;
    private String password = null;
    private String accessCode = null;
    private Boolean canViewSession = null;
    
    public TestStatusVO() 
    {
    }    
    public TestStatusVO(ProgramStatusSession status){
        this.testAdminId = status.getTestAdminId();
        this.sessionName = status.getSessionName();
        this.sessionNumber = status.getSessionNumber();
        this.loginId = status.getLoginId();
        this.password = status.getPassword();
        this.accessCode = status.getAccessCode();
        this.canViewSession = new Boolean(status.getVisible());
    }    
    public Integer getTestAdminId() {
        return this.testAdminId;
    }
    public void setTestAdminId(Integer testAdminId) {
        this.testAdminId = testAdminId;
    }
    public String getSessionName() {
        return this.sessionName;
    }
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
    public String getSessionNumber() {
        return this.sessionNumber;
    }
    public void setSessionNumber(String sessionNumber) {
        this.sessionNumber = sessionNumber;
    }
    public String getLoginId() {
        return this.loginId;
    }
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAccessCode() {
        return this.accessCode;
    }
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
    public Boolean getCanViewSession() {
        return this.canViewSession;
    }
    public void setCanViewSession(Boolean canViewSession) {
        this.canViewSession = canViewSession;
    }
} 
