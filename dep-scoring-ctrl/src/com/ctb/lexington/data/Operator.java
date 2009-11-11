package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * <p>Title: Operator</p>
 * <p>Description: holds information for an operator</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Jon Becker, John Wang
 * @version 1.0
 */

public class Operator implements Serializable{

    private Operands operands; 
    private String displayText;
    private String name;
    
    public Operator(Operands operands_, String displayText_, String name_) {
    	this.operands = operands_;
    	this.displayText = displayText_;
    	this.name=name_;
    }
	public Operands getOperands() {
		return this.operands;
	}
	public String getDisplayText() {
		return this.displayText;
	}
	public String getName() {
		return this.name;
	}
}