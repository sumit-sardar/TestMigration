package com.ctb.testSessionInfo.dto; 

import com.ctb.bean.testAdmin.TestStatus;

/**
 *@author Tata Consultancy Services 
 */

public class SubtestStatus implements java.io.Serializable 
{
    static final long serialVersionUID = 1L;

    private String id = null;
    private String name = null;
    private String scheduled = null;
    private String attempted = null;
    private String completed = null;
    private Boolean isParent = null;
    private Boolean hasLink = null;
        
    public SubtestStatus() {
        this.name = "";
        this.scheduled = "0";
        this.attempted = "0";
        this.completed = "0";
        this.isParent = Boolean.FALSE;
        this.hasLink = Boolean.FALSE;
    }   
    public SubtestStatus(String id, String name, String scheduled, 
                        String attempted, String completed, 
                        Boolean isParent, Boolean hasLink) {
        this.id = id;
        this.name = name;
        this.scheduled = scheduled;
        this.attempted = attempted;
        this.completed = completed;
        this.isParent = isParent;
        this.hasLink = hasLink;
    }   
     
     public SubtestStatus(TestStatus testStatus, Boolean hasLink) {
        this.id = testStatus.getItemSetId().toString();
        this.name = testStatus.getItemSetName().toString();
        this.scheduled = testStatus.getScheduledCount().toString();
        this.attempted = testStatus.getStartedCount().toString();
        this.completed = testStatus.getCompletedCount().toString();
        this.isParent = Boolean.FALSE;
        this.hasLink = hasLink;
    }   
   public String getId() {
        return this.id != null ? this.id : "" ;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name != null ? this.name : "" ;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getScheduled() {
        return this.scheduled != null ? this.scheduled : "0" ;
    }
    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }
    public String getAttempted() {
        return this.attempted != null ? this.attempted : "0" ;
    }
    public void setAttempted(String attempted) {
        this.attempted = attempted;
    }
    public String getCompleted() {
        return this.completed != null ? this.completed : "0" ;
    }
    public void setCompleted(String completed) {
        this.completed = completed;
    }
    public Boolean getIsParent() {
        return this.isParent;
    }
    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }
    public Boolean getHasLink() {
        return this.hasLink;
    }
    public void setHasLink(Boolean hasLink) {
        this.hasLink = hasLink;
    }
    public Boolean getHasScheduledLink() {
        return new Boolean(this.hasLink.booleanValue() && 
                           new Integer(this.scheduled).intValue() > 0);
    }
     public Boolean getHasAttemptedLink() {
         return new Boolean(this.hasLink.booleanValue() && 
                           new Integer(this.attempted).intValue() > 0);
    }
    public Boolean getHasCompletedLink() {
        return new Boolean(this.hasLink.booleanValue() && 
                           new Integer(this.completed).intValue() > 0);
    }
   
} 
