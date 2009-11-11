package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * <p>Title: StimulusFilterVO</p>
 * <p>Description: holds stimulus filter options.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Jon Becker
 * @version 1.0
 */

public class StimulusFilterVO implements Serializable{

    private String stimulusName;
    private String contentArea;
    private String grade;
    private String stimulusFormat;
    
    public StimulusFilterVO(String stimulusName_, String contentArea_, 
    		                String grade_, String stimulusFormat_) {
    	this.stimulusName = stimulusName_;
    	this.contentArea=contentArea_;
    	this.grade=grade_;
    	this.stimulusFormat = stimulusFormat_;
    }
	public String getStimulusName() {
		return stimulusName;
	}
	public String getContentArea() {
		return contentArea;
	}
	public String getGrade() {
		return grade;
	}
	public String getStimulusFormat() {
		return stimulusFormat;
	}
}