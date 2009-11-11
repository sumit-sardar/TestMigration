package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.Date;


/**
 * RosterVO.java Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 * 
 * @author <a href="mailto:Krys_Gawetski@ctb.com">Krys Gawetski</a>
 * @version $Id$
 */
public class RosterVO implements Serializable {
    public  static final String VO_LABEL       = "com.ctb.lexington.data.RosterVO";
    public  static final String VO_ARRAY_LABEL = VO_LABEL + ".array";
    //private static final long serialVersionUID = 6746352730465450774L;  
    
    private Integer testRosterId = null; //pk
    private Integer studentId    = null; //fk
    private Integer testAdminId  = null; //fk
    private Integer orgNodeId    = null; //fk
    private Integer customerId   = null; //fk
    
    private String  activationStatus     = null;
    private String  captureMethod        = null;
    private Date    completionDateTime   = null;
    private Integer createdBy            = null;    
    private String  overrideTestWindow   = null;
    private String  password             = null;
    private String  scoringStatus        = null;
    private Date    startDateTime        = null;        
    private String  testCompletionStatus = null;
    private String  validationStatus     = null;
    
    //empty ctor
    public RosterVO() {}

    public boolean equals(Object obj) {       
        if (obj instanceof RosterVO) {
            RosterVO that = (RosterVO) obj;
            if( that.getStudentId().equals( this.getStudentId()) ){
                return that.getOrgNodeId().equals( this.getOrgNodeId() );
            }else{
                return that.getStudentId().equals( this.getStudentId());
            }
        }
        return false;
    }
    
    
    //simple setters and getters for properties
    
    public String getActivationStatus() {
        return activationStatus;
    }
    
    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }
    
    public String getCaptureMethod() {
        return captureMethod;
    }
    
    public void setCaptureMethod(String captureMethod) {
        this.captureMethod = captureMethod;
    }
    
    public Date getCompletionDateTime() {
        return completionDateTime;
    }
    
    public void setCompletionDateTime(Date completionDateTime) {
        this.completionDateTime = completionDateTime;
    }
    
    public Integer getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
    
    public Integer getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    
    public Integer getOrgNodeId() {
        return orgNodeId;
    }
    
    public void setOrgNodeId(Integer orgNodeId) {
        this.orgNodeId = orgNodeId;
    }
    
    public String getOverrideTestWindow() {
        return overrideTestWindow;
    }
    
    public void setOverrideTestWindow(String overrideTestWindow) {
        this.overrideTestWindow = overrideTestWindow;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getScoringStatus() {
        return scoringStatus;
    }
    
    public void setScoringStatus(String scoringStatus) {
        this.scoringStatus = scoringStatus;
    }
    
    public Date getStartDateTime() {
        return startDateTime;
    }
    
    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }
    
    public Integer getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
    
    public Integer getTestAdminId() {
        return testAdminId;
    }
    
    public void setTestAdminId(Integer testAdminId) {
        this.testAdminId = testAdminId;
    }
    
    public String getTestCompletionStatus() {
        return testCompletionStatus;
    }
    
    public void setTestCompletionStatus(String testCompletionStatus) {
        this.testCompletionStatus = testCompletionStatus;
    }
    
    public Integer getTestRosterId() {
        return testRosterId;
    }
    
    public void setTestRosterId(Integer testRosterId) {
        this.testRosterId = testRosterId;
    }
    
    public String getValidationStatus() {
        return validationStatus;
    }
    
    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }
    
}
/**
 * $Log$
 * Revision 1.1  2007/01/30 01:31:41  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:47:40  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.2  2005/05/03 21:26:11  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.1.2.1  2005/03/04 20:01:32  gawetski
 * Created to support MultiOrgNode assignment defect.
 *
 */