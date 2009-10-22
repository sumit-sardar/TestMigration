package com.ctb.widgets.bean; 

import java.io.Serializable;

public class PathListEntry implements Serializable
{ 
    private String label;
    private Integer value;
    
    
    
    public PathListEntry() {
        this.label = null;
        this.value = null;
    }
    
    
    public void setLabel( String label ) {
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setValue( Integer value ) {
        this.value = value;
    }
    
    public Integer getValue( ) {
        return this.value;
    }

} 
