package com.ctb.lexington.data;

import java.io.Serializable;


/*
 * AlphaNavigatorVO.java
 *
 * Copyright CTB/McGraw-Hill, 2003
 * 
 * CONFIDENTIAL
 *
 */


/**
 * This class holds information needed by the AlphaNavigatorTag.
 * 
 * @author  Jon Becker
 */
public class AlphaNavigatorVO implements Serializable
{
    private String name;                // the name of this VO
    private boolean[] hasItems;         // are there items for this selection
    private RowObject[] rowObjects;     // the row rowObjects that contain the data sorted by sort
    private int expandedIndex;          // the array index of the expanded node
	private int selectedIndex;          // the array index of the selected node
    private String filter;              // the current filter
    private String sort;                // the current sort order
    private int startIndex;             // the index of the first row matching filter
    private int endIndex;               // the index of the last row matching filter
    private boolean [] selected;        // is row selected?
    private String conversation;        // conversation
    private String command;             // command
    private String viewType;			// which node (district/school) in which tab (pending/completed) 
     
    /**
     * Creates new AlphaNavigatorVO.
     */
    public AlphaNavigatorVO(RowObject[] objects_)
    {
    	this.rowObjects = objects_;
    	this.expandedIndex = -1;
    }

    public void setName(String name_)
	{
		this.name = name_;
	}
	
	public void setHasItems(boolean[] hasItems_){
	    this.hasItems = hasItems_;
	}
	
	public void setRowObjects(RowObject[] objects_){
		this.rowObjects = objects_;
	}
	
	public void setExpandedIndex(int expandedIndex_){
		this.expandedIndex = expandedIndex_;
	}

	public void setSelectedIndex(int selectedIndex_){
		this.selectedIndex = selectedIndex_;
	}
	
	public void setFilter(String filter_){
		this.filter = filter_;
	}
	
	public void setSort(String sort_){
		this.sort = sort_;
	}
	
	public void setStartIndex(int startIndex_){
		this.startIndex = startIndex_;
	}
	
	public void setEndIndex(int endIndex_){
		this.endIndex = endIndex_;
	}
	
	public void setSelected(boolean[] selected_){
		this.selected = selected_;
	}
	
	public void setConversation(String conversation_){
		this.conversation = conversation_;
	}
	
	public void setCommand(String command_){
		this.command = command_;
	}

	public void setViewType(String viewType_){
		this.viewType = viewType_;
	}

	public String getName()
	{
		return this.name;
	}
	
	public boolean[] getHasItems(){
		return this.hasItems;
	}
	
	public RowObject[] getRowObjects(){
		return this.rowObjects;
	}
	
	public int getExpandedIndex(){
		return this.expandedIndex;
	}

	public int getSelectedIndex(){
		return this.selectedIndex;
	}

	public String getFilter(){
		return this.filter;
	}
	
	public String getSort(){
		return this.sort;
	}
	
	public int getStartIndex(){
		return this.startIndex;
	}
	
	public int getEndIndex(){
		return this.endIndex;
	}
	
	public boolean[] getSelected(){
		return this.selected;
	}

	public String getConversation(){
		return this.conversation;
	}
	
	public String getCommand(){
		return this.command;
	}

	public String getViewType(){
		return this.viewType;
	}
}