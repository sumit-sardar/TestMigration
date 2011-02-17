package com.ctb.common.tools;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.*;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import net.sf.hibernate.type.LongType;
import net.sf.hibernate.type.StringType;
import net.sf.hibernate.type.Type;



import com.ctb.hibernate.persist.*;
import com.ctb.reporting.ItemProcessorReport;
import com.ctb.xmlProcessing.item.*;

public class DBItemGateway {

    protected Session session;

    public DBItemGateway(Session session) {
        this.session = session;
    }

    /* *************************************************************************** */

    /**
     * @param itemID The Unique ID of the Item that is being searched for
     * @return ItemRecord if found. Does not return null
     * @throws <code>ItemNotFoundException</code> if the Item does not exist in the database
     */
    private ItemRecord getUniqueItemRecord(String itemId) {
        try {
            ItemRecord item = (ItemRecord) session.get(ItemRecord.class, itemId);
            if (item == null)
                throw new ItemNotFoundException("Could not find Item " + itemId);
            return item;
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public String getLastCreatedItem(long productID, String itemID) {
        String item = null;

        try {
            List list = getItemRecords(new Long(productID), itemID);
            ItemRecord itemRecord = getLastCreatedRecordFromList(list);
            if (itemRecord != null)
                item = itemRecord.getItemId();

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
        return item;
    }

    private ItemRecord getLastCreatedRecordFromList(List list) {

        ItemRecord item = null;

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            ItemRecord currentItem = (ItemRecord) iter.next();
            if (item == null || item.getCreatedDateTime().before(currentItem.getCreatedDateTime())) {
                item = currentItem;
            }
        }
        return item;
    }

    /**
     * @param originalItemID The Original Item ID of the Item that is being searched
     * @param frameworkCode The framework in which it is being searched
     * @return ItemRecord if found. Does not return null
     * @throws <code>ItemNotFoundException</code> if the Item does not exist in the database
     */

    /*private List getItemRecords(String originalItemID, String frameworkCode) {
        try {
            Long productID = new Long(new DBObjectivesGateway(session)
                    .getFrameWorkID(frameworkCode));
            return getItemRecords(productID, originalItemID);
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }*/

    private List getItemRecords(Long productID, String originalItemID) throws HibernateException {
        return session.find("from " + ItemRecord.class.getName()
                + " as item, " + ItemSetRecord.class.getName()
                + " as itemSet, " + ItemSetItemRecord.class.getName()
                + " as itemSetItem, " + ItemSetCategoryRecord.class.getName()
                + " as itemSetCategory where itemSetCategory.frameworkProductId = ? "
                + "and itemSet.itemSetCategoryId = itemSetCategory.itemSetCategoryId "
                + "and itemSet.itemSetType = 'RE' "
                + "and itemSet.activationStatus = 'AC' "
                + "and itemSetItem.id.itemSetId = itemSet.itemSetId "
                + "and itemSetItem.id.item_id = item.itemId "
                + "item.itemId = ?", new Object[] {productID,
                originalItemID}, new Type[] {new LongType(), new StringType()});
    }

    private ItemRecord getUniqueActiveItemRecord(String originalItemID ) {
 //       List itemRecords = getItemRecords(originalItemID, frameworkCode);
        return getUniqueItemRecord(originalItemID);
 /*       ItemRecord returnedItemRecord = null;
        int count = 0;
        for (Iterator iter = itemRecords.iterator(); iter.hasNext();) {
            ItemRecord itemRecord = (ItemRecord) iter.next();
            if (itemRecord.getActivationStatus().equals(OASConstants.ITEM_STATUS_ACTIVE)) {
                returnedItemRecord = itemRecord;
                count++;
            }
        }
        if (count > 1)
            throw new SystemException("Inconsistent data - " + count
                    + " active Items found with Original Item ID [" + originalItemID
                    + "] in framework [" + frameworkCode + "]");

        if (count == 0)
            throw new ItemNotFoundException("Could not find Item with Original Item ID ["
                    + originalItemID + "] in framework [" + frameworkCode + "]");
        return returnedItemRecord; */
    }

    /* *************************************************************************** */
    /* === The following public functions query the database for the existance of items=== */

    public boolean activeItemExists(String itemID) {
        try {
            ItemRecord itemRecord = getUniqueItemRecord(itemID);
            return itemRecord.getActivationStatus().equals(OASConstants.ITEM_STATUS_ACTIVE);
        } catch (ItemNotFoundException e) {
            return false;
        }
    }

    public boolean itemExistsActiveOrInactive(String itemID) {
        try {
            ItemRecord itemRecord = getUniqueItemRecord(itemID);
            if (itemRecord == null)
                return false;
            return true;
        } catch (ItemNotFoundException e) {
            return false;
        }
    }
    
    public boolean isItemXMLMediaExist( String itemID )
    {
        try
        {
            List itemRecordList = session.find( "from " + ItemRecord.class.getName()
                    + " as item, " + ItemMediaRecord.class.getName()
                    + " as ItemMedia where ItemMedia.id.mediaType = 'IBXML' and "
                    + "ItemMedia.id.itemId = ? and ItemMedia.id.itemId = item.itemId"
                    , new Object[] { itemID }, new Type[] { new StringType()} );
            if ( itemRecordList == null || itemRecordList.size() != 1 )
                return false;
            return true;
        }
        catch (HibernateException e) 
        {
            return false;
        }
    }


    /* *************************************************************************** */
    /* === The following public functions do not modify the database === */

    public String getItemId(String originalItemId ) {
        return getUniqueActiveItemRecord( originalItemId ).getItemId();
    }

/*    public boolean isInvisible(String itemId) {
        String ibsInvisible = getUniqueItemRecord(itemId).getIbsInvisible();
        return (ibsInvisible == null) ? false : OASConstants.ITEM_IBS_INVISIBLE
                .equals(ibsInvisible);
    }
*/
    public String getCorrectAnswer(String itemId) {
        String answer = getUniqueItemRecord(itemId).getCorrectAnswer();
        return (answer == null || answer.equals("null")) ? null : answer;
    }

    public String getItemTypeCode(String itemId) {
        return getUniqueItemRecord(itemId).getItemType();
    }

    private String getTruncatedField(String text, int limit) {
        if (text == null) {
            return null;
        }
        if (text.length() > limit) {
            return text.substring(0, limit);
        }
        return text;
    }

    public Item getItem(String itemId) {
        Item item = new Item(itemId);
        ItemRecord itemRecord = getUniqueItemRecord(itemId);

        item.setId(itemId);
        item.setActivationStatus(itemRecord.getActivationStatus());
        item.setCorrectAnswer(itemRecord.getCorrectAnswer());
//        item.setFieldTest(itemRecord.getFieldTest());
//        item.setSuppressed(itemRecord.getSuppressed());
        item.setType(itemRecord.getItemType());
        item.setTemplateId(itemRecord.getTemplateId());
        item.setVersion(itemRecord.getVersion());
        item.setDescription(itemRecord.getDescription());
        item.setExtStimulusId(itemRecord.getExtStimulusId());
        item.setExtStimulusTitle(itemRecord.getExtStimulusTitle());
        item.setHistory(itemRecord.getItemId());
 //       item.setFrameworkId(itemRecord.getFrameworkProductId().intValue());
        item.setDisplayId(itemRecord.getItemDisplayName());
//        item.setInvisible(OASConstants.ITEM_IBS_INVISIBLE.equals(itemRecord.getIbsInvisible()));
        //TODO - remove the conditional retrieval when THINK_CODE is activated in ItemRecord
        item.setThinkID( (itemRecord.getThinkID() == null) ? "" : itemRecord.getThinkID());
        return item;
    }

    public boolean isTemplateInDB(String templateID) {
        try {
            TemplateCodeRecord templateCodeRecord = (TemplateCodeRecord) session.get(
                    TemplateCodeRecord.class, templateID);
            if (templateCodeRecord == null)
                return false;
            return true;
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public boolean isItemProperlyLinked(String itemId, String cmsId, String frameworkCode) {
        try {
            List itemSetIds = session.find("select item_set_item.id.itemSetId from "
                    + ItemRecord.class.getName() + " as item, " + ItemSetItemRecord.class.getName()
                    + " as item_set_item, " + ItemSetRecord.class.getName() + " as item_set, "
                    + ItemSetCategoryRecord.class.getName() + " as item_set_category, "
                    + ProductRecord.class.getName() + " as product " + "where item.itemId = ? "
                    + "and item_set_item.id.itemId = item.itemId "
                    + "and item_set.itemSetId = item_set_item.id.itemSetId "
                    + "and item_set.itemSetType = ? " + "and item_set.extCmsItemSetId = ? "
                    + "and item_set_category.itemSetCategoryId = item_set.itemSetCategoryId "
                    + "and product.productId = item_set_category.frameworkProductId "
                    + "and product.internalDisplayName = ?", new Object[] {itemId,
                    OASConstants.ITEM_SET_TYPE_REPORTING, cmsId, frameworkCode}, new Type[] {
                    new StringType(), new StringType(), new StringType(), new StringType()});
            return (itemSetIds.size() == 1);
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public int getNumberOfMediaRecordsForItem(String itemId) {
        try {
            Query query = session.createQuery("count(*) from " + ItemMediaRecord.class.getName()
                    + " as itemMedia where itemMedia.itemId = :itemId");
            query.setString("itemId", itemId);
            return ((Integer) query.uniqueResult()).intValue();
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    /* *************************************************************************** */
    /* === The following public functions modify the database === */

    public void inactivateItem(String itemId) {
        try {
            ItemRecord item = getUniqueItemRecord(itemId);
            item.setActivationStatus(OASConstants.ITEM_SET_STATUS_INACTIVE);
            session.update(item);
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
        // TODO: this needs to cascade and inactivate item sets that no longer have items
    }
/*
    public void makeItemInvisible(String itemId) {
        try {
            ItemRecord item = getUniqueItemRecord(itemId);
            item.setIbsInvisible(OASConstants.ITEM_IBS_INVISIBLE);
            session.update(item);
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }
*/
    public void makeItemInactive(String itemId) {
        try {
            ItemRecord item = getUniqueItemRecord(itemId);
            item.setActivationStatus(OASConstants.ITEM_STATUS_INACTIVE);
            session.update(item);
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    /**
     * This method processes the Item Element, Datapoint and Datapoint_condition_code by inserting
     * the item, datapont_id and condition_code_id setting up its media (where applicable) ??
     */

    public void writeItemIntoDatabase(Item item) {
        writeItemIntoDatabase(item, true);
    }

    /* *************************************************************************** */
    /* === The following public functions modify the database tables === */

    private void writeItemIntoDatabase(Item item, boolean activateObjectives) {

        long itemSetId = 0;
        DBObjectivesGateway ogw = new DBObjectivesGateway(session);

        // create data point entry and connect to curriculum if the item type is not NI
        if (! Item.NOT_AN_ITEM.equals(item.getType())) {
        
	        item.setFrameworkId(ogw.getFrameWorkID(item.getFrameworkCode()));
	
	
	        itemSetId = ogw.getItemSetIdFromObjective(item.getObjectiveId(), item
	                .getFrameworkCode());
        }
        if (itemExistsActiveOrInactive(item.getId())) {
            updateExistingItem(item, itemSetId, item.isSample());
        } else {
            insertNewItem(item, itemSetId, item.isSample());
        }
        if (item.getChoiceCount() > 2 && item.getChoiceCount() < 6)
        {	// handle item_answer_choice
	        CallableStatement stmt = null;
	        try
	        {
	            this.session.flush();
	//            this.session.connection().commit();
	            stmt = this.session.connection().prepareCall( "{call SP_POP_ANS_CHOICE_SINGLE_ITEM (?, ?)}");
		        stmt.setString( 1, item.getId());
		        stmt.setInt(2, item.getChoiceCount());
		        stmt.execute();
		        stmt.close();
	        }
	        catch (SQLException sqlEx) {
	            throw new SystemException(sqlEx.getMessage(), sqlEx);
	        }
	        catch (HibernateException e) {
	            throw new SystemException(e.getMessage(), e);
	        }
        }
        //TODO - mws - prune logic should be part of the else
        if (!item.isSample() && activateObjectives  && ! Item.NOT_AN_ITEM.equals(item.getType())) {
            ogw.activateAllParentObjectives(itemSetId);
        }
    }

    private void insertNewItem(Item item, long itemSetId, boolean isSampleItem) {
        ItemRecord itemRecord = new ItemRecord();
        itemRecord.setCreatedBy(new Long(OASConstants.CREATED_BY));
        itemRecord.setCreatedDateTime(new Date());
        saveOrUpdateItem(item, itemRecord);

        // create data point entry and connect to curriculum if the item type is not NI
        if (! Item.NOT_AN_ITEM.equals(item.getType())) {
        
			String[] conditionCodes = item.isCR() ? DBDatapointGateway.CR_CONDITION_CODES
			        : DBDatapointGateway.SR_CONDITION_CODES;
			
		    new DBDatapointGateway(session).insertDatapoint(item.getId(), itemSetId,
		            conditionCodes, item.getMinPoints(), item.getMaxPoints());
		
		    new DBObjectivesGateway(session).linkItemToObjective(item.getId(), itemSetId
			            						, item.getFrameworkCode()
			            						, !item.isInvisible());

        }
    }

    private void updateExistingItem(Item item, long itemSetId, boolean isSampleItem) {
        DBDatapointGateway dpgw = new DBDatapointGateway(session);

        saveOrUpdateItem(item, getUniqueItemRecord(item.getId()));

        // update data point if the item type is not NI
        if (! Item.NOT_AN_ITEM.equals(item.getType())) {
        
	        String[] conditionCodes = item.isCR() ? DBDatapointGateway.CR_CONDITION_CODES
	                : DBDatapointGateway.SR_CONDITION_CODES;
	
	        // update data point if the item is sample
	    //    if (!isSampleItem) {
	 //          dpgw.deleteItemDatapoints( item.getId(), itemSetId );
	            Datapoint theDatapoint = dpgw.getFrameworkDatapoint( item.getId(), item.getFrameworkCode() );
	            if ( theDatapoint != null && theDatapoint.getItemSetId() == itemSetId )
	            {
	                dpgw.updateDataPoint(item.getId(), itemSetId, conditionCodes, item.getMinPoints(), item
	                        .getMaxPoints());
	            }
	            else if ( theDatapoint != null )
	            {
	                try 
	                {
	                    ItemProcessorReport r = ItemProcessorReport.getCurrentReport();
		                ItemSetRecord originalItemSet = (ItemSetRecord) session.get(ItemSetRecord.class, new Long( theDatapoint.getItemSetId()));
		                ItemSetRecord moveToItemSet = (ItemSetRecord) session.get(ItemSetRecord.class, new Long( itemSetId ) );
		                r.setWarning( "Item moved from \"" + originalItemSet.getExtCmsItemSetId() + ":" +
		                        		originalItemSet.getItemSetDisplayName() + "\" to \""
		                        		+ moveToItemSet.getExtCmsItemSetId() + ":"
		                        		+ moveToItemSet.getItemSetDisplayName() + "\"." );
	                }
	                catch (Exception e) 
	                {
	                }
	                dpgw.updateDataPoint(item.getId(), theDatapoint.getItemSetId(), itemSetId, conditionCodes, item.getMinPoints(), item
	                        .getMaxPoints());
	            }
	            else
	            {
		            dpgw.insertDatapoint(item.getId(), itemSetId,
		                    conditionCodes, item.getMinPoints(), item.getMaxPoints());
	            }
	            new DBObjectivesGateway(session).linkItemToObjective(item.getId(), itemSetId
						, item.getFrameworkCode(), !item.isInvisible());
        }
      }

    private void saveOrUpdateItem(Item item, ItemRecord itemRecord) {
        // TODO: *all* the item fields arguments (not just these)
        itemRecord.setItemId(item.getId());
        itemRecord.setCorrectAnswer(item.getCorrectAnswer());
        itemRecord.setItemType(item.getType());
//        itemRecord.setFieldTest(item.getFieldTest());
//        itemRecord.setSuppressed(item.getSuppressed());
        itemRecord.setActivationStatus(OASConstants.ITEM_STATUS_ACTIVE);
        itemRecord.setGriddedColumns(item.getGriddedColumns());
        itemRecord.setAnswerArea(item.getAnswerArea());
        itemRecord.setUpdatedBy(new Long(OASConstants.CREATED_BY));
        itemRecord.setUpdatedDateTime(new Date());
        itemRecord.setTemplateId(item.getTemplateId());
        itemRecord.setOnlineCR(item.getOnlineCR());
        itemRecord.setExternalID( item.getExternalID() );
        itemRecord.setExternalSystem( item.getExternalSystem() );
        itemRecord.setVersion(item.getVersion());
        itemRecord.setDescription(getTruncatedField(item.getDescription(), OASConstants.MAX_DESC));
        itemRecord.setExtStimulusId(item.getExtStimulusId());
        itemRecord.setExtStimulusTitle(item.getExtStimulusTitle());
//        itemRecord.setFrameworkProductId(new Long(item.getFrameworkId()));
        itemRecord.setItemDisplayName(item.getDisplayId());
        itemRecord.setThinkID(item.getThinkID());

//        if (item.isInvisible())
//            itemRecord.setIbsInvisible(OASConstants.ITEM_IBS_INVISIBLE);
        try {
            session.saveOrUpdate(itemRecord);
            //      session.flush();
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    /* *************************************************************************** */
    /* === The following private functions do not modify the database === */

    /*private String getTrueFalseString(boolean flag) {
        return flag ? OASConstants.DB_TRUE : OASConstants.DB_FALSE;
    }*/

    /* *************************************************************************** */

    /* === The following functions are included for testing purposes only === */

    public void testDeleteItemWithAssociatedTables(String itemId) {

        DBDatapointGateway dpgw = new DBDatapointGateway(session);

        dpgw.testDeleteAllItemDatapoints(itemId);

        String[] tablesCompositeId = {ItemMediaRecord.class.getName(),
                ItemSetItemRecord.class.getName()};

        String[] tablesSimpleId = {AuxItemResponseRecord.class.getName(),
                DatapointRecord.class.getName(), ItemResponseRecord.class.getName(),
                ItemRecord.class.getName()};

        for (int i = 0; i < tablesCompositeId.length; i++) {
            String table = tablesCompositeId[i];
            try {
                session.delete("from " + table + " as table where table.id.itemId = ?", itemId,
                        new StringType());
            } catch (HibernateException e1) {
                throw new SystemException(e1.getMessage(), e1);
            }
        }

        for (int i = 0; i < tablesSimpleId.length; i++) {
            String table = tablesSimpleId[i];
            try {
                session.delete("from " + table + " as table where table.itemId = ?", itemId,
                        new StringType());
            } catch (HibernateException e1) {
                throw new SystemException(e1.getMessage(), e1);
            }
        }
    }

    public Item testReadItemFromDB(String itemId) {
        return getItem(itemId);
    }

    public void getAllItemIDs(Collection allIDs, Collection inactiveIDs) {
        try {
            List items = session.find("from " + ItemRecord.class.getName() + " as item");
            for (Iterator iter = items.iterator(); iter.hasNext();) {
                ItemRecord itemRecord = (ItemRecord) iter.next();
                String itemID = itemRecord.getItemId();
                allIDs.add(itemID);
                if (itemRecord.getActivationStatus().compareToIgnoreCase(
                        OASConstants.ITEM_STATUS_ACTIVE) != 0) {
                    inactiveIDs.add(itemID);
                }

            }
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }
    
    public Datapoint getAnyDataPointForCRItem( String itemID )
    {
        DBDatapointGateway dpgw = new DBDatapointGateway(session);
        return dpgw.getAnyDatapoint( itemID );
    }

    /* *************************************************************************** */

}