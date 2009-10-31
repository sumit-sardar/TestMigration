package dto; 

/**
 *@author Tata Consultancy Services 
 */

import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;
import java.util.Date;

public class TestSessionVO implements java.io.Serializable
{     
    static final long serialVersionUID = 1L;

    private Integer testAdminId = null;
    private String testAdminName = null;
    private String testName = null;
    private String sessionNumber = null;
    private Date loginStartDate = null;
    private Date loginEndDate = null;
    private String reportable = null;
    private String accessCode = null;
    private String creatorOrgNodeName = null;
    private String creatorOrgNodeCategoryName = null;
    private String testAdminStatus = null;
    private String showStudentFeedback = null;
    
    public TestSessionVO() {
    }    
    public TestSessionVO(TestSession ts) {
        this.testAdminId =  ts.getTestAdminId();
        this.testAdminName = ts.getTestAdminName();
        this.testName = ts.getTestName();
        this.sessionNumber = ts.getSessionNumber();        
        this.loginStartDate = ts.getLoginStartDate();
        this.loginEndDate = ts.getLoginEndDate();
        this.reportable = ts.getReportable();   
        this.accessCode = ts.getAccessCode();
        this.creatorOrgNodeName = ts.getCreatorOrgNodeName();
        this.creatorOrgNodeCategoryName = ts.getCreatorOrgNodeCategoryName();
        this.testAdminStatus = ts.getTestAdminStatus();
        this.showStudentFeedback = ts.getShowStudentFeedback();
    }     
    public String getAccessCode() {
        return this.accessCode;
    }
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
    public Date getLoginEndDate() {
        return this.loginEndDate;
    }
    public void setLoginEndDate(Date loginEndDate) {
        this.loginEndDate = loginEndDate;
    }
    public Integer getTestAdminId() {
        return this.testAdminId;
    }
    public void setId(Integer testAdminId) {
        this.testAdminId = testAdminId;
    }
    public String getSessionNumber() {
        return this.sessionNumber;
    }
    public void setSessionNumber(String sessionNumber) {
        this.sessionNumber = sessionNumber;
    }
    public String getTestAdminName() {
        return this.testAdminName;
    }
    public void setTestAdminName(String testAdminName) {
        this.testAdminName = testAdminName;
    }
    public String getTestAdminStatus() {
        return this.testAdminStatus;
    }
    public void setTestAdminStatus(String testAdminStatus) {
        this.testAdminStatus = testAdminStatus;
    }
    public Date getLoginStartDate() {
        return this.loginStartDate;
    }
    public void setLoginStartDate(Date loginStartDate) {
        this.loginStartDate = loginStartDate;
    }
    public String getTestName() {
        return this.testName;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }                
    public String getReportable() {
        
        if ( ( this.reportable != null) && this.reportable.equals("T") ) {
            
            return "true";
        }
        
        return "false";
    }
    public void setReportable(String reportable) {
        this.reportable = reportable;
    }                
    public String getCreatorOrgNodeName() {
        return this.creatorOrgNodeName;
    }
    public void setCreatorOrgNodeName(String creatorOrgNodeName) {
        this.creatorOrgNodeName = creatorOrgNodeName;
    }                
    public String getCreatorOrgNodeCategoryName() {
        return this.creatorOrgNodeCategoryName;
    }
    public void setCreatorOrgNodeCategoryName(String creatorOrgNodeCategoryName) {
        this.creatorOrgNodeCategoryName = creatorOrgNodeCategoryName;
    }                
    public String getShowStudentFeedback() {
        return this.showStudentFeedback;
    }
    public void setShowStudentFeedback(String showStudentFeedback) {
        this.showStudentFeedback = showStudentFeedback;
    }                
} 
