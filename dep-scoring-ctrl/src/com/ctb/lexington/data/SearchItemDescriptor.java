package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.List;

import com.ctb.lexington.util.CTBConstants;

/**
 * @author John Wang
 * @version 1.0
 */

public class SearchItemDescriptor implements Serializable 
{
	public static final String ALL = CTBConstants.ALL;
    
    private List levelNames;
    
    private List grades;
    private List strands;
    private List standards;
    
    private String selectedGrade = ALL;
    private String selectedStrand = ALL;
    private String selectedStandard = ALL;
    
    private String itemName;
    private String stimulusName;
    private String itemType = ALL;
    private String itemStatus = ALL;
    private String firstName;
    private String lastName;
    
    

    public SearchItemDescriptor() 
    {
        
    }


    /**
     * @param levelNames The levelNames to set.
     */
    public void setLevelNames(List levelNames) {
        this.levelNames = levelNames;
    }


    /**
     * @return Returns the levelNames.
     */
    public List getLevelNames() {
        return levelNames;
    }


    /**
     * @param grades The grades to set.
     */
    public void setGrades(List grades) {
        this.grades = grades;
    }


    /**
     * @return Returns the grades.
     */
    public List getGrades() {
        return grades;
    }


    /**
     * @param strands The strands to set.
     */
    public void setStrands(List strands) {
        this.strands = strands;
    }


    /**
     * @return Returns the strands.
     */
    public List getStrands() {
        return strands;
    }


    /**
     * @param stadards The stadards to set.
     */
    public void setStandards(List standards) {
        this.standards = standards;
    }


    /**
     * @return Returns the stadards.
     */
    public List getStandards() {
        return standards;
    }


    /**
     * @param selectedGrade The selectedGrade to set.
     */
    public void setSelectedGrade(String selectedGrade) {
        this.selectedGrade = selectedGrade;
    }


    /**
     * @return Returns the selectedGrade.
     */
    public String getSelectedGrade() {
        return selectedGrade;
    }


    /**
     * @param selectedStrand The selectedStrand to set.
     */
    public void setSelectedStrand(String selectedStrand) {
        this.selectedStrand = selectedStrand;
    }


    /**
     * @return Returns the selectedStrand.
     */
    public String getSelectedStrand() {
        return selectedStrand;
    }


    /**
     * @param selectedStandard The selectedStandard to set.
     */
    public void setSelectedStandard(String selectedStandard) {
        this.selectedStandard = selectedStandard;
    }


    /**
     * @return Returns the selectedStandard.
     */
    public String getSelectedStandard() {
        return selectedStandard;
    }


	/**
	 * @param itemName The itemName to set.
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	/**
	 * @return Returns the itemName.
	 */
	public String getItemName() {
		return itemName;
	}


	/**
	 * @param stimulusName The stimulusName to set.
	 */
	public void setStimulusName(String stimulusName) {
		this.stimulusName = stimulusName;
	}


	/**
	 * @return Returns the stimulusName.
	 */
	public String getStimulusName() {
		return stimulusName;
	}


	/**
	 * @param itemType The itemType to set.
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}


	/**
	 * @return Returns the itemType.
	 */
	public String getItemType() {
		if (itemType == null)
			return ALL;
		else
			return itemType;
	}


	/**
	 * @param itemStatus The itemStatus to set.
	 */
	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}


	/**
	 * @return Returns the itemStatus.
	 */
	public String getItemStatus() {
		if (itemStatus == null)
			return ALL;
		else
			return itemStatus;
	}


	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}


	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}
    
}