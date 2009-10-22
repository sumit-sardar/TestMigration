package com.ctb.widgets.bean; 

import java.io.Serializable;

public class ColumnSortEntry implements Serializable
{ 
    public static final String ASCENDING = "asc";
    public static final String DESCENDING = "desc";
    
    private String label;
    private String value;
    private String orderBy;
    
    public static final long serialVersionUID = 1L;
    
    public ColumnSortEntry() {
        this.label = null;
        this.value = null;
    }
    
    
    public void setLabel( String label ) {
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setValue( String value ) {
        this.value = value;
    }
    
    public String getValue( ) {
        return this.value;
    }

    public void setOrderBy( String orderBy ) {
        this.orderBy = orderBy;
    }
    
    public String getOrderBy() {
        return this.orderBy;
    }

} 
