package com.ctb.contentBridge.core.publish.dao;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.xml.item.Item;


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

    public void validateItemreadyForInsert() {


        if (!item.isSample()) {
            validateItemSetId();

            if ( (igw.activeItemExists(item.getId()))) {
                validateChangesAllowed();
            }
        }

    }

    public boolean validateItemInDB() {
        checkItemInDB();

        // only preform checking for regular item.
        // sample item won't have item set and datapoint entry.
        if (!item.isSample() &&  !Item.NOT_AN_ITEM.equals(item.getType())) {
            checkItemSetItemInDB();
            // TODO: reactivate
            // checkItemMediaInDB();
            checkDatapointInDB();
            checkDatapointConditionCodeInDB();
        }
        return true;
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
/*
        String currentObjective = ( (item.getHistory() != null) && (!item.getHistory().equals(""))) ? ogw
                .getCurrentObjectiveIDForItem(item.getHistory(), item.getFrameworkCode())
                : ogw.getCurrentObjectiveIDForItem(item.getId());

        if (currentObjective == null) {
            throw new BusinessException("Existing item not linked to any objective");
        }

        if (!currentObjective.equals(item.getObjectiveId())) {
            throw new RuntimeException(
                    "The hierarchy location of an item can not be updated. (old = '"
                            + currentObjective + "' new = '" + item.getObjectiveId() + "')");
        } */
    }

    private void verifyItemTypeNotChanged() {
        String dbItemType = igw.getItem(item.getId()).getType();

        if (dbItemType != null && !dbItemType.equals(item.getType())) {
            throw new BusinessException("An update to an existing item cannot change the item type "
                    + item.getId());
        }
    }

    private void verifyCorrectAnswerNotChanged() {
        String dbAnswer = igw.getItem(item.getId()).getCorrectAnswer();

        if (dbAnswer != null && !dbAnswer.trim().equalsIgnoreCase(item.getCorrectAnswer().trim())) {
            throw new BusinessException(
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