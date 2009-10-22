package com.ctb.util.studentManagement; 

/**
 * Data representing status returned to caller
 * 
 * @author John_Wang
 */

public class Status 
{ 
    private String statusCode;
    
    
    public Status(String statusCode) {
        this.statusCode = statusCode;    
    }

    public String getStatusCode() {
        return this.statusCode;
    }
    
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode; 
    }
    
    public boolean equals(Status s) {
        if (s!=null && s.getStatusCode()!= null && s.getStatusCode().equals(this.statusCode))
            return true;
        else
            return false;
    }
        
} 
