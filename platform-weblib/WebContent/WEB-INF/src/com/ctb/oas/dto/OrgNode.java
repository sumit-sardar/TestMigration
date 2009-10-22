package com.ctb.oas.dto; 

import java.io.Serializable;

public class OrgNode implements Serializable
{ 
    private Integer orgNodeId;
    private String orgNodeName;
    private Integer totalStudentsWithin;
    private Integer totalStudentsAssigned;
    
    public Integer getOrgNodeId() {
        return this.orgNodeId;
    }
    
    public void setOrgNodeId(Integer orgNodeId) {
        this.orgNodeId = orgNodeId;
    }
    
    public String getOrgNodeName() {
        return this.orgNodeName;
    }
    
    public void setOrgNodeName(String orgNodeName) {
        this.orgNodeName = orgNodeName;
    }
    
    public Integer getTotalStudentsWithin() {
        return this.totalStudentsWithin;
    }
    
    public void setTotalStudentsWithin(Integer totalStudentsWithin) {
        this.totalStudentsWithin = totalStudentsWithin;
    }
    
    public Integer getTotalStudentsAssigned() {
        return this.totalStudentsAssigned;
    }
    
    public void setTotalStudentsAssigned(Integer totalStudentsAssigned) {
        this.totalStudentsAssigned = totalStudentsAssigned;
    }
    
} 
