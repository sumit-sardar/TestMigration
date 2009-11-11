package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tai Truong
 * @version 1.0
 */

public class TestItemPagerDescriptor implements Serializable 
{
	String customerId = null;
	String objectiveId = null;
	String objectiveName = null;
	String pagerName = null;
	String pagerType = null;
	Integer pagerSize = null;
	String pagerMemberName = null;
	Object[] params = null;
	SortColumnDescriptor descriptor = null;
    String conversation = null;
    String command = null;
    
    private List objectiveIds = null;
     
    public TestItemPagerDescriptor(String customerId, String objectiveId, String objectiveName, String pagerName, String pagerType, Integer pagerSize, 
            					   SortSelectionVO sortVO, String conversation, String command) 
    {
        this.customerId = customerId;
        this.objectiveId = objectiveId;
        this.objectiveName = objectiveName;
		this.pagerName = pagerName;
		this.pagerType = pagerType;
		this.pagerSize = pagerSize;
		this.pagerMemberName = this.pagerName + "pageMember";
		this.params = new Object[2];
		this.params[0] = new Integer(this.objectiveId);
		this.params[1] = new Integer(this.customerId);
		this.descriptor = new SortColumnDescriptor(this.pagerName, sortVO);     
		this.conversation = conversation;
		this.command = command;
    }

    public TestItemPagerDescriptor(String customerId, List objectiveIds, String objectiveName, String pagerName, String pagerType, Integer pagerSize, 
			   SortSelectionVO sortVO, String conversation, String command, 
			   String itemName, String itemType, String itemStatus, String stimulusName, 
	    		String firstName, String lastName ) 
	{
		this.customerId = customerId;
		this.objectiveIds = objectiveIds;
		this.objectiveName = objectiveName;
		this.pagerName = pagerName;
		this.pagerType = pagerType;
		this.pagerSize = pagerSize;
		this.pagerMemberName = this.pagerName + "pageMember";
		
		this.params = new Object[8];
		this.params[0] = this.objectiveIds;
		this.params[1] = new Integer(this.customerId);
		this.params[2] = itemName;
		this.params[3] = itemType;
		this.params[4] = itemStatus;
		this.params[5] = stimulusName;
		this.params[6] = firstName;
		this.params[7] = lastName;
		
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
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getObjectiveId() {
        return objectiveId;
    }
    public void setObjectiveId(String objectiveId) {
        this.objectiveId = objectiveId;
    }
    public String getObjectiveName() {
        return objectiveName;
    }
    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
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

	/**
	 * @param objectiveIds The objectiveIds to set.
	 */
	public void setObjectiveIds(List objectiveIds) {
		this.objectiveIds = objectiveIds;
	}

	/**
	 * @return Returns the objectiveIds.
	 */
	public List getObjectiveIds() {
		return objectiveIds;
	}
}