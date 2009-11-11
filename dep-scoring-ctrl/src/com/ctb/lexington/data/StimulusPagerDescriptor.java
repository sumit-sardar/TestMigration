package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Jon Becker
 * @version 1.0
 */

public class StimulusPagerDescriptor implements Serializable 
{
	Collection itemSetIds = null;
	String stimulusName = null;
	String stimulusFormat = null;
	String pagerName = null;
	String pagerType = null;
	Integer pagerSize = null;
	String pagerMemberName = null;
	Object[] params = null;
	SortColumnDescriptor descriptor = null;
    String conversation = null;
    String command = null;
    
    public StimulusPagerDescriptor(Collection itemSetIds_, String stimulusName_, String stimulusFormat_, 
    		                       String pagerName_, String pagerType_, Integer pagerSize_, 
            					   SortSelectionVO sortVO_, String conversation_, String command_) 
    {
        this.itemSetIds = itemSetIds_;
        this.stimulusName = stimulusName_;
        this.stimulusFormat = stimulusFormat_;
		this.pagerName = pagerName_;
		this.pagerType = pagerType_;
		this.pagerSize = pagerSize_;
		this.pagerMemberName = this.pagerName + "pageMember";
		this.descriptor = new SortColumnDescriptor(this.pagerName, sortVO_);     
		this.conversation = conversation_;
		this.command = command_;
		this.params = new Object[3];
		this.params[0] = this.itemSetIds;
		this.params[1] = this.stimulusName;
		this.params[2] = this.stimulusFormat;
    }
    
    public SortColumnDescriptor getDescriptor() {
        return descriptor;
    }
    public void setDescriptor(SortColumnDescriptor descriptor) {
        this.descriptor = descriptor;
    }
    public Collection getItemSetIds() {
        return this.itemSetIds;
    }
    public void setItemSetIds(Collection itemSetIds_) {
        this.itemSetIds = itemSetIds_;
    }
    public String getStimulusName() {
        return this.stimulusName;
    }
    public void setStimulusName(String stimulusName_) {
        this.stimulusName = stimulusName_;
    }
    public String getStimulusFormat() {
        return this.stimulusFormat;
    }
    public void setStimulusFormat(String stimulusFormat_) {
        this.stimulusFormat = stimulusFormat_;
    }
    public String getPagerMemberName() {
        return pagerMemberName;
    }
    public void setPagerMemberName(String pagerMemberName) {
        this.pagerMemberName = pagerMemberName;
    }
    public String getPagerName() {
        return pagerName;
    }
    public void setPagerName(String pagerName) {
        this.pagerName = pagerName;
    }
    public String getPagerType() {
        return pagerType;
    }
    public void setPagerType(String pagerType) {
        this.pagerType = pagerType;
    }
    public Integer getPagerSize() {
        return pagerSize;
    }
    public void setPagerSize(Integer pagerSize) {
        this.pagerSize = pagerSize;
    }
    public Object[] getParams() {
        return params;
    }
    public void setParams(Object[] params) {
        this.params = params;
    }
    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public String getConversation() {
        return conversation;
    }
    public void setConversation(String conversation) {
        this.conversation = conversation;
    }
}