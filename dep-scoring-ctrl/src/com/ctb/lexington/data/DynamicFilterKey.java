package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * <p>Title: DynamicFilterKey</p>
 * <p>Description: holds a collection of operators.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Jon Becker, John Wang
 * @version 1.0
 */

public class DynamicFilterKey implements Serializable{

	// upload types
    private Operator[] operators; 
    private String displayText;
    private String name;
    
    public DynamicFilterKey(Operator[] operators_, String displayText_, String name_) {
    	this.operators = operators_;
    	this.displayText = displayText_;
    	this.name = name_;
    }
	public Operator[] getOperators() {
		return this.operators;
	}
	public String getDisplayText() {
		return this.displayText;
	}
	public String getName() {
		return this.name;
	}	
}