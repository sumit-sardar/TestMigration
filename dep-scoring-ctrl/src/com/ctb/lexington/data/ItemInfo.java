package com.ctb.lexington.data;

/**
* @author Tai Truong
*/

import java.io.Serializable;
import java.util.Date;

import com.ctb.lexington.util.CTBConstants;

public class ItemInfo implements Serializable {
    private String id = "";
    private String name = "";
    private String type = "";
    private String description = "";
    private String status = "";
    private String createdBy = "";
    private Date itemUpdatedDate = null;
    private Date objectiveUpdatedDate = null;
    private Date itemCreatedDate = null;
    private Date objectiveCreatedDate = null;
    private Date displayDate = null;
    private boolean userItem = false;
    private boolean published = false;
    private boolean locked = false;
    private boolean valid = false;
    private boolean onlineCR = false;
    private boolean usedInTest = false;
    private String stimulusTitle = "";
    private String stimulusId = "";
    
    public static final String CTB_ITEM_NOT_EDITABLE = "CTB<br/>Not Editable";
    public static final String UNPUBLISHED = "Not Published";
    public static final String PUBLISHED_USED_IN_TEST = "Published,<br/>used in a test.";
    public static final String PUBLISHED_NOT_USED_IN_TEST = "Published, not<br/>used in a test.";

    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public boolean isLocked() {
        return locked;
    }
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isOnlineCR() {
        return onlineCR;
    }
    public void setOnlineCR(boolean onlineCR) {
        this.onlineCR = onlineCR;
    }
    public boolean isPublished() {
        return published;
    }
    public void setPublished(boolean published) {
        this.published = published;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public boolean isUserItem() {
        return userItem;
    }
    public void setUserItem(boolean userItem) {
        this.userItem = userItem;
    }
    public boolean isValid() {
        return valid;
    }
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isUsedInTest() {
        return usedInTest;
    }
    public void setUsedInTest(boolean usedInTest) {
        this.usedInTest = usedInTest;
    }
    public Date getItemUpdatedDate() {
        return itemUpdatedDate;
    }
    public void setItemUpdatedDate(Date itemUpdatedDate) {
        this.itemUpdatedDate = itemUpdatedDate;
        this.displayDate = null; // force compute new date on display
    }
    public Date getObjectiveUpdatedDate() {
        return objectiveUpdatedDate;
    }
    public void setObjectiveUpdatedDate(Date objectiveUpdatedDate) {
        this.objectiveUpdatedDate = objectiveUpdatedDate;
        this.displayDate = null; // force compute new date on display
    }
    public Date getItemCreatedDate() {
        return itemCreatedDate;
    }
    public void setItemCreatedDate(Date itemCreatedDate) {
        this.itemCreatedDate = itemCreatedDate;
    }
    public Date getObjectiveCreatedDate() {
        return objectiveCreatedDate;
    }
    public void setObjectiveCreatedDate(Date objectiveCreatedDate) {
        this.objectiveCreatedDate = objectiveCreatedDate;
    }
    public String getStatus() {
        String str = null;
        if (! userItem) {
            str = CTB_ITEM_NOT_EDITABLE;
        }
        else {
	        if (! published) {
	            str = UNPUBLISHED;
	        }
	        else {
	            if (usedInTest) {
	                str = PUBLISHED_USED_IN_TEST;
	            }
	            else {
	                str = PUBLISHED_NOT_USED_IN_TEST;
	            }
	        }
        }
        return str;
    }    
    public boolean getStatusForAction(String userType) {
        if (this.userItem && (userType.equals(CTBConstants.ROLE_NAME_ITEM_AUTHOR) || 
                			  userType.equals(CTBConstants.ROLE_NAME_ITEM_ADMINISTRATOR) || 
                			  userType.equals(CTBConstants.ROLE_NAME_ACCOUNT_MANAGER))) {
            if (! this.published)
                return true;	// unpublished = ok to edit/move/delete
            if (this.published && (! this.usedInTest))
                return true;	// published and not used in test = ok to edit/move/delete
            if (this.published && this.usedInTest && (! userType.equals(CTBConstants.ROLE_NAME_ITEM_AUTHOR)))
                return true;	// published and used in test and user is author admin or acount manager = ok to edit/move/delete
        }
        return false;
    }    
    public Date getDisplayDate() {
        if (this.displayDate == null) {
        	if(this.objectiveUpdatedDate == null)
        		this.objectiveUpdatedDate = this.objectiveCreatedDate;
        	if(this.itemUpdatedDate == null)
        		this.itemUpdatedDate = this.itemCreatedDate;
        	
        	if (this.objectiveUpdatedDate.compareTo(this.itemUpdatedDate) > 0)
	            this.displayDate = this.objectiveUpdatedDate;
	        else
	            this.displayDate = this.itemUpdatedDate;    
        }
        return this.displayDate;
    }
    public String getStimulusTitle() {
        return stimulusTitle;
    }
    public void setStimulusTitle(String stimulusTitle) {
        this.stimulusTitle = stimulusTitle;
    }
    public String getStimulusId() {
        return stimulusId;
    }
    public void setStimulusId(String stimulusId) {
        this.stimulusId = stimulusId;
    }
    public boolean equals(Object to_){
		boolean result = false;
    	try{
    		ItemInfo to = (ItemInfo)to_;
	     	boolean createdByEqual = equals(this.getCreatedBy(), to.getCreatedBy());
	    	boolean descriptionEqual = equals(this.getDescription(), to.getDescription());
	     	boolean idEqual = equals(this.getId(), to.getId());
	     	boolean nameEqual = equals(this.getName(), to.getName());
	     	boolean statusEqual = equals(this.getStatus(), to.getStatus());
	     	boolean stimulusTitleEqual = equals(this.getStimulusTitle(), to.getStimulusTitle());
	     	boolean typeEqual = equals(this.getType(), to.getType());
	     	boolean displayDateEqual = equals(this.getDisplayDate(), to.getDisplayDate());
	     	boolean itemCreatedDateEqual = equals(this.getItemCreatedDate(), to.getItemCreatedDate());
	     	boolean itemUpdatedDateEqual = equals(this.getItemUpdatedDate(), to.getItemUpdatedDate());
	     	boolean objectiveCreatedDateEqual = equals(this.getObjectiveCreatedDate(), to.getObjectiveCreatedDate());
	     	boolean objectiveUpdatedDateEqual = equals(this.getObjectiveUpdatedDate(), to.getObjectiveUpdatedDate());
	     	boolean isLockedEqual = this.isLocked() == to.isLocked();
	     	boolean isOnlineCREqual = this.isOnlineCR() == to.isOnlineCR();
	     	boolean isPublishedEqual =  this.isPublished() == to.isPublished();
	     	boolean isUsedInTestEqual = this.isUsedInTest() == to.isUsedInTest();
	     	boolean isUserItemEqual = this.isUserItem() == to.isUserItem();
	     	boolean isValidEqual = this.isValid() == to.isValid();
	   	
			result = createdByEqual && 
			       descriptionEqual && 
				   idEqual &&
				   nameEqual &&
				   statusEqual &&
				   stimulusTitleEqual &&
				   typeEqual &&
				   isLockedEqual &&
				   isOnlineCREqual &&
				   isPublishedEqual &&
				   isUsedInTestEqual &&
				   isUserItemEqual &&
				   isValidEqual;
       	}
    	catch (Exception e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    private boolean equals(Object from_, Object to_){
    	return (((from_ != null && to_ != null) && from_.equals(to_)) ||
    			(from_ == null && to_ == null));
    }
}
