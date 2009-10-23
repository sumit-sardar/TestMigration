package com.ctb.testSessionInfo.dto; 

//import com.ctb.bean.testAdmin.ActiveTest;
import com.ctb.bean.testAdmin.ActiveSession;
import com.ctb.bean.testAdmin.ActiveTest;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;
import java.util.Date;

public class ActiveSessionVO implements java.io.Serializable
{     
    static final long serialVersionUID = 1L;
    private String orgNodeName = null;
    private Date loginEndDate = null;
    private Date loginStartDate = null;
    private Integer testAdminId = null;
    private String testAdminName = null;
    private boolean selected = false;
    
    public ActiveSessionVO() 
    {
    }    
    public ActiveSessionVO(ActiveSession as) 
    {
        this.loginEndDate   = as.getLoginEndDate();
        this.loginStartDate = as.getLoginStartDate();
        this.testAdminId    = as.getTestAdminId();
        this.testAdminName  = as.getTestAdminName();
        this.orgNodeName   =  as.getOrgNodeName();
    }     
    public void setOrgNodeName(String orgNodeName){
        this.orgNodeName = orgNodeName;
    }
    public String getOrgNodeName(){
        return this.orgNodeName;
    }
    public void setLoginEndDate(Date loginEndDate){
        this.loginEndDate = loginEndDate;
    }
    public Date getLoginEndDate(){
        return this.loginEndDate;
    }
    public void setLoginStartDate(Date loginStartDate){
        this.loginStartDate = loginStartDate;
    }
    public Date getLoginStartDate(){
        return this.loginStartDate;
    }
    public void setTestAdminId(Integer testAdminId){
        this.testAdminId = testAdminId;
    }
    public Integer getTestAdminId(){
        return this.testAdminId;
    }
    public void setTestAdminName(String testAdminName){
        this.testAdminName = testAdminName;
    }
    public String getTestAdminName(){
        return this.testAdminName;
    }
    public void setSelected(boolean selected){
        this.selected = selected;
    }
    public boolean getSelected(){
        return this.selected;
    }

} 
