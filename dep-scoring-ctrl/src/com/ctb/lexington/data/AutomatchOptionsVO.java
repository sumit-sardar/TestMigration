package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * <p>Title: AutomatchOptionsVO</p>
 * <p>Description: holds automatch options.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Jon Becker
 * @version 1.0
 */

public class AutomatchOptionsVO implements Serializable{

    private Boolean automatchOverwrite = null;
    private Integer automatchCriteriaId = null;
    
    public AutomatchOptionsVO() {
    }
    public Boolean getAutomatchOverwrite() {
		return automatchOverwrite;
	}
	public void setAutomatchOverwrite(Boolean automatchOverwrite_) {
		this.automatchOverwrite = automatchOverwrite_;
	}
    public Integer getAutomatchCriteriaId(){
    	return this.automatchCriteriaId;
    }
    public void setAutomatchCriteriaId(Integer automatchCriteriaId_){
    	this.automatchCriteriaId = automatchCriteriaId_;
    }
	public boolean equals(Object object){
		boolean isEqual = false;
		try{
			AutomatchOptionsVO vo = (AutomatchOptionsVO) object;
			if( (vo.automatchOverwrite == null && this.automatchOverwrite == null) ||
				(vo.automatchOverwrite.equals(this.automatchOverwrite)) &&
			   ((vo.automatchCriteriaId == null && this.automatchCriteriaId == null) ||
				 vo.automatchCriteriaId.equals(this.automatchCriteriaId)))
			{
				isEqual = true;
			}
		}
		catch(Exception e){
		}
		return isEqual;
	}
}