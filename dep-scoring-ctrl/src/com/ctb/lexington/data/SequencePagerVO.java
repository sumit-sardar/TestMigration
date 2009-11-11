package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/*
 * SequencePagerVO.java
 *
 * Copyright CTB/McGraw-Hill, 2003
 * 
 * CONFIDENTIAL
 *
 */


/**
 * This class holds information needed by the SequencePager.
 * 
 * @author  Tai Truong
 */
public class SequencePagerVO implements Serializable
{
    private Collection objectList;            
    private int currentIndex;        
    private int doneCount;        
    private String commandName;
    
    public SequencePagerVO(Collection objectList, String commandName)
    {
    	this.objectList = objectList;
    	this.commandName = commandName;
    	this.currentIndex = 0;
    	this.doneCount = 0;
    }
    
	/**
	 * @return Returns the currentIndex.
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}
	/**
	 * @param currentIndex The currentIndex to set.
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	/**
	 * @return Returns the objectList.
	 */
	public Collection getObjectList() {
		return objectList;
	}
	/**
	 * @param objectList The objectList to set.
	 */
	public void setObjectList(List objectList) {
		this.objectList = objectList;
	}
	/**
	 * @return Returns the doneCount.
	 */
	public int getDoneCount() {
		return doneCount;
	}
	/**
	 * @param doneCount The doneCount to set.
	 */
	public void setDoneCount(int doneCount) {
		this.doneCount = doneCount;
	}
	/**
	 * @return Returns the commandName.
	 */
	public String getCommandName() {
		return commandName;
	}
	/**
	 * @param commandName The commandName to set.
	 */
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
	
	public Object getCurrentObject() {
	    if (this.objectList != null) {
	        Object temp[] = objectList.toArray();
	        if ((temp != null) && (temp.length > currentIndex)) 
	            return temp[currentIndex];
	    }
	    return null;
	}
}