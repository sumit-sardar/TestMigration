package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * @author Tai Truong
 * @version 1.0
 */

public class ItemsForStimulusPagerDescriptor implements Serializable 
{
	String stimulusId = null;
	String pagerName = null;
	String pagerType = null;
	Integer pagerSize = null;
	String pagerMemberName = null;
	Object[] params = null;
	SortColumnDescriptor descriptor = null;
    String conversation = null;
    String command = null;
    
    public ItemsForStimulusPagerDescriptor(String stimulusId, String pagerName, String pagerType, Integer pagerSize, 
            					           SortSelectionVO sortVO, String conversation, String command) 
    {
        this.stimulusId = stimulusId;
		this.pagerName = pagerName;
		this.pagerType = pagerType;
		this.pagerSize = pagerSize;
		this.pagerMemberName = this.pagerName + "pageMember";
		this.params = new Object[1];
		this.params[0] = this.stimulusId;
		this.descriptor = new SortColumnDescriptor(this.pagerName, sortVO);     
		this.conversation = conversation;
		this.command = command;
    }
     
    public SortColumnDescriptor getDescriptor() {
        return descriptor;
    }
    public void setDescriptor(SortColumnDescriptor descriptor) {
        this.descriptor = descriptor;
    }
    public String getStimulusId() {
        return stimulusId;
    }
    public void setStimulusId(String stimulusId_) {
        this.stimulusId = stimulusId_;
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