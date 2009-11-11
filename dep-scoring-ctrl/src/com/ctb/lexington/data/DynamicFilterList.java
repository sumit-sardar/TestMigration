package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * <p>Title: DynamicFilterList</p>
 * <p>Description: holds a collection of filter keys.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Jon Becker, John Wang
 * @version 1.0
 */

public class DynamicFilterList implements Serializable{
    private DynamicFilterKey[] filterKeys;
    private String name;
    private String type;
    
    public DynamicFilterList(DynamicFilterKey[] filterKeys_, String name_, String type_){
    	this.filterKeys = filterKeys_;
    	this.name =name_;
    	this.type =type_;
    }
    public String getName(){
    	return this.name;
    }
    public String getType(){
    	return this.type;
    }
	public DynamicFilterKey[] getFilterKeys() {
		return filterKeys;
	}
}