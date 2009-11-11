/*
 * Created on Aug 3, 2004
 *
 */
package com.ctb.lexington.domain.teststructure;

import java.io.Serializable;

/**
 * @author arathore
 *
 */
public class SubtestInfo implements Serializable{
    private String id;
	private String subtestName;
	private String level;
	private int itemOrder;

	public SubtestInfo(String subtestName, String id, String level) {
	    this.id = id;
		this.subtestName = subtestName;
		this.level = level;
	}
	
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getSubtestName() {
		return subtestName;
	}

	public void setSubtestName(String subtestArea) {
		this.subtestName = subtestArea;
	}
	
	public String toString() {
		return "Subtest: " + subtestName + " level " + level + " id " + id;
	}
	
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public boolean equals(Object oth) {
        if (this == oth) 
            return true;
        if (oth == null) 
            return false;
        if (oth.getClass() != getClass()) 
            return false;
        
        SubtestInfo other = (SubtestInfo) oth;
        if (this.id == null) {
            if (other.id != null) 
                return false;
        } else {
            if (!this.id.equals(other.id)) 
                return false;
        }
        return true;
    }

	public void setItemOrder(int order) {
		this.itemOrder = order;
	}
	public int getItemOrder() {
		return this.itemOrder;
	}

	public String guiOptionValue() {
		return getId() + "_" + getLevel();
	}


}
