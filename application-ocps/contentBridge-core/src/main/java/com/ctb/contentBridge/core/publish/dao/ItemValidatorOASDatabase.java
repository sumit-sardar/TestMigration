package com.ctb.contentBridge.core.publish.dao;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.xml.item.Item;
import com.ctb.contentBridge.core.util.ObjectiveUtil;


public class ItemValidatorOASDatabase {
    protected Item item;
    DBItemGateway igw;
    DBObjectivesGateway ogw;
    DBDatapointGateway dpgw;

    public ItemValidatorOASDatabase(Item item, DBItemGateway igw, DBObjectivesGateway ogw,
            DBDatapointGateway dpgw) {
        this.item = item;
        this.igw = igw;
        this.ogw = ogw;
        this.dpgw = dpgw;
    }

    /**
     *  SPRINT 10: TO SUPPORT MAPPING AN ITEM TO MULTIPLE OBJECTIVE
     *  This method validated all objective associated with a item.
     * @throws Exception
     */
    public void validateItemForAllObjectveForInsert() throws Exception {

    	final String originalObjective = item.getObjectiveId();
    	if (originalObjective.indexOf(ObjectiveUtil.ObjectiveSeperatore)>1){
    		String [] objectiveArray = ObjectiveUtil.getArrayFromString(originalObjective,ObjectiveUtil.ObjectiveSeperatore);
    		try{
    			for (int i=0; i<objectiveArray.length; i++) {
        			item.setObjectiveId(objectiveArray[i]);
        			validateItemreadyForInsert();
           		 }
    			item.setObjectiveId(originalObjective);
    		} catch (Exception e) {
    			item.setObjectiveId(originalObjective);
    			throw e;
			}
    	} else {
    		validateItemreadyForInsert();
    	}

    }
    
    public void validateItemreadyForInsert() {


        if (!item.isSample()) {
            validateItemSetId();


            if ( (igw.activeItemExists(item.getId()))) {
                validateChangesAllowed();
            }
        }

    }

    public boolean validateItemInDB() throws Exception {
        checkItemInDB();

        // only preform checking for regular item.
        // sample item won't have item set and datapoint entry.
        if (!item.isSample() &&  !Item.NOT_AN_ITEM.equals(item.getType())) {
        	//START SPRINT 10: TO SUPPORT MULTIPLE OBJECTIVE
        
        	final String OriginalObjectiveId =  item.getObjectiveId();
 	    	if (OriginalObjectiveId.indexOf(ObjectiveUtil.ObjectiveSeperatore)>1){
 	    		String [] objectiveIdArray = ObjectiveUtil.getArrayFromString(OriginalObjectiveId,ObjectiveUtil.ObjectiveSeperatore);
 	    		for (int i=0; i<objectiveIdArray.length; i++) {
 	    			item.setObjectiveId(objectiveIdArray[i]);
 	    			try{
 	    			validateItemInDB(OriginalObjectiveId);
 	    			} catch (Exception e){
 	    	    		item.setObjectiveId(OriginalObjectiveId);
 	    	    		throw e;
 	    	    		
 	    	    	}
 	       		 }
 	    		item.setObjectiveId(OriginalObjectiveId);

 	    	} else {
 	    		validateItemInDB(OriginalObjectiveId);
 	    	}
 	    	//END SPRINT 10: TO SUPPORT MULTIPLE OBJECTIVE
        }
        return true;
    }

    /**
     * SPRINT 10: TO SUPPORT MULTIPLE OBJECTIVE
     * Method validated objective, datapoint and datapoint condition code in database
     * @param OriginalObjectiveId
     */
    public void validateItemInDB( String OriginalObjectiveId ) {
    		checkItemSetItemInDB();
            checkDatapointInDB();
            checkDatapointConditionCodeInDB();


    }

    public void validateChangesAllowed() {
        // don't need to check if the item is sample
        if (!item.isSample()) {
            verifyItemNotMoved();
        }
        verifyItemTypeNotChanged();
        verifyCorrectAnswerNotChanged();
    }

    public void validateItemSetId() {
        long itemSetId = ogw.getItemSetIdFromObjective(item.getObjectiveId(), item
                .getFrameworkCode());
    }

    private void verifyItemNotMoved() 
    {

        /*String currentObjective = ( (item.getHistory() != null) && (!item.getHistory().equals(""))) ? ogw
                .getCurrentObjectiveIDForItem(item.getHistory(), item.getFrameworkCode())
                : ogw.getCurrentObjectiveIDForItem(item.getId());

        if (currentObjective == null) {
            throw new SystemException("Existing item not linked to any objective");
        }

        if (!currentObjective.equals(item.getObjectiveId())) {
            throw new RuntimeException(
                    "The hierarchy location of an item can not be updated. (old = '"
                            + currentObjective + "' new = '" + item.getObjectiveId() + "')");
        }*/ 
    }

    private void verifyItemTypeNotChanged() {
        String dbItemType = igw.getItem(item.getId()).getType();

        if (dbItemType != null && !dbItemType.equals(item.getType())) {
            throw new SystemException("An update to an existing item cannot change the item type "
                    + item.getId());
        }
    }

    private void verifyCorrectAnswerNotChanged() {
        String dbAnswer = igw.getItem(item.getId()).getCorrectAnswer();

        if (dbAnswer != null && !dbAnswer.trim().equalsIgnoreCase(item.getCorrectAnswer().trim())) {
            throw new SystemException(
                    "An update to an existing item cannot change the answer choice: "
                            + item.getId());
        }
    }

    private void checkItemInDB() {
        if (!igw.activeItemExists(item.getId())) {
            throw new RuntimeException("Item '" + item.getId() + " not in database!");
        }
    }

    private void checkItemSetItemInDB() {
        if (!igw.isItemProperlyLinked(item.getId(), item.getObjectiveId(), item.getFrameworkCode())) {
            throw new RuntimeException("The item " + item.getId()
                    + " is not properly linked to any item set");
        }
    }

    private void checkItemMediaInDB() {
        int count = igw.getNumberOfMediaRecordsForItem(item.getId());

        if (count == 0) {
            throw new RuntimeException("ITEM_MEDIA table does not have any record for item :"
                    + item.getId());
        }

        if ( ( (item.isSR() || item.isSA()) && count != 3) || (item.isCR() && count != 4)) {
            throw new RuntimeException("ITEM_MEDIA table does not have correct number of "
                    + " records for item : " + item.getId() + " with item type : " + item.getType());
        }
    }

    private void checkDatapointInDB() {
        long itemSetId = ogw.getItemSetIdFromObjective(item.getObjectiveId(), item
                .getFrameworkCode());
        int count;

        count = dpgw.testGetNumberOfDataPoints(item.getId(), itemSetId);
        if (count == 0) {
            throw new RuntimeException("DATAPOINT table does not have record for item : "
                    + item.getId());
        }

    }

    private void checkDatapointConditionCodeInDB() {
        long itemSetId = ogw.getItemSetIdFromObjective(item.getObjectiveId(), item
                .getFrameworkCode());
        int count;

        count = dpgw.testGetNumberOfConditionCodes(item.getId(), itemSetId);
        if (count == 0) {
            throw new RuntimeException(
                    "DATAPOINT_CONDITION_CODE table does not have records for item : "
                            + item.getId());
        }

        if ( ( (item.isSR() || item.isSA()) && count != 2) || (item.isCR() && count != 3)) {
            throw new RuntimeException(
                    "DATAPOINT_CONDITION_CODE table does not have correct number of "
                            + " records for item : " + item.getId() + " with item type : "
                            + item.getType());
        }

    }
}