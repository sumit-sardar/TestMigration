package com.ctb.widgets.bean; 

import java.io.Serializable;

public class PagerSummary implements Serializable
{ 
    private String objectLabel;
    private Integer currentPage;
    private Integer totalPages;
    private Integer totalObjects;
    private Integer totalFilteredObjects;
    private Integer totalSelectedObjects;//Bulk Accommodation
    
    
    
    
    public PagerSummary() {
        this.objectLabel = null;
        this.currentPage = null;
        this.totalPages = null;
        this.totalObjects = null;
        this.totalFilteredObjects = null;
        this.totalSelectedObjects = null;
    }
    
    
    public void setCurrentPage( Integer currentPage ) {
        this.currentPage = currentPage;
    }
    
    public Integer getCurrentPage() {
        return this.currentPage;
    }
    
    public void setTotalPages( Integer totalPages ) {
        this.totalPages = totalPages;
    }
    
    public Integer getTotalPages() {
        return this.totalPages;
    }
    
    public void setTotalObjects( Integer totalObjects ) {
        this.totalObjects = totalObjects;
    }
    
    public Integer getTotalObjects() {
        return this.totalObjects;
    }
    
    public void setTotalFilteredObjects( Integer totalFilteredObjects ) {
        this.totalFilteredObjects = totalFilteredObjects;
    }
    
    public Integer getTotalFilteredObjects() {
        return this.totalFilteredObjects;
    }

    public void setObjectLabel( String objectLabel ) {
        this.objectLabel = objectLabel;
    }
    
    public String getObjectLabel( ) {
        return this.objectLabel;
    }


	public Integer getTotalSelectedObjects() {
		return totalSelectedObjects;
	}


	public void setTotalSelectedObjects(Integer totalSelectedObjects) {
		this.totalSelectedObjects = totalSelectedObjects;
	}

} 
