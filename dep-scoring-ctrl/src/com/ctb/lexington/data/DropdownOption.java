package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * <p>Title: Option</p>
 * <p>Description: holds information for an Option</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Jon Becker, John Wang
 * @version 1.0
 */

public class DropdownOption implements Serializable{
	private String displayText;
	private String value;
	public DropdownOption(){
	}
	public DropdownOption(String displayText_, String value_){
		this.setDisplayText(displayText_);
		this.setValue(value_);
	}
	private void setValue(String value_) {
		this.value = value_;
	}
	public String getValue() {
		return this.value;
	}
	private void setDisplayText(String displayText_) {
		this.displayText = displayText_;
	}
	public String getDisplayText() {
		return this.displayText;
	}
}