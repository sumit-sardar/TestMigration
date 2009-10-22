package com.ctb.oas.dto; 

import java.io.Serializable;

public class NotSerializable
{ 
    private String something;
    
    public void setSomething( String something ) {
        this.something = something;
    }
    
    public String getSomething() {
        return this.something;
    }
} 
