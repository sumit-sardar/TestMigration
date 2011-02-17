package com.ctb.xmlProcessing.subtest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.ctb.common.tools.OASConstants;
import com.ctb.common.tools.SystemException;


import com.ctb.hibernate.persist.ItemSetItemCompositeId;
import com.ctb.hibernate.persist.ItemSetItemRecord;



import com.ctb.hibernate.persist.ItemSetRecord;

import com.ctb.hibernate.persist.ItemSetParentCompositeId;
import com.ctb.hibernate.persist.ItemSetParentRecord;
import com.ctb.hibernate.persist.ScorableItemCompositeId;
import com.ctb.hibernate.persist.ScorableItemRecord;
import com.ctb.hibernate.persist.ScoreLookupItemSetRecord;
import com.ctb.hibernate.persist.ScoreTypeRecord;
import com.ctb.xmlProcessing.assessment.AssessmentType;
import com.ctb.util.persist.ItemSetRelationship;

/**
 * @author wmli
 */
public class DBSubTestGateway extends AbstractDBSubTestGateway {
    private DBSubTestMediaGateway mediaGateway;

    public DBSubTestGateway(Session session) {
        super(session);
        mediaGateway = new DBSubTestMediaGateway(session);
    }

    public List writeSubTest(SubTestHolder subTest, AssessmentType assessmentType) {

        Long productId = subTest.getProductID();
        if ( productId == null )
            productId = getProductID(subTest.getFrameworkCode(), subTest.getProductDisplayName(),
                assessmentType.getProductType());

        try {
            
            String itemSetType = subTest.isTDType() ? OASConstants.ITEM_SET_TYPE_TEST_DELIVERY : OASConstants.ITEM_SET_TYPE_SCHEDURABLE_UNIT;
            ItemSetRecord itemSetRecordTD = getItemSetRecord(subTest.getExtTstItemSetId(),
                    							itemSetType );

            Long testDeliveryItemsetId = (itemSetRecordTD == null) ? null : itemSetRecordTD
                    .getItemSetId();

            Long ItemsetId = createOrUpdateItemSet(subTest,
                    itemSetType, testDeliveryItemsetId,
                    assessmentType.getMediaPath(), subTest.isSample()).getItemSetId();
            
            if (testDeliveryItemsetId != null) 
            {
                makeUnusedItemsVisible(subTest, testDeliveryItemsetId );
                clearRelationships(productId, testDeliveryItemsetId);
                if ( !subTest.isTDType() )
                {
                    if ( subTest.isAddOn.booleanValue() )
                    {
                        clearTStoTDsByForm( testDeliveryItemsetId, ( Long )subTest.childSubTestIds.get( 0 ));
                    }
                    else
                        clearTStoTDs( testDeliveryItemsetId );
                }
            }
            testDeliveryItemsetId = ItemsetId;

            createRelationships(subTest, testDeliveryItemsetId, productId);
            if ( !subTest.isTDType() )
            {
                createTStoTDs( testDeliveryItemsetId, subTest );
            }
            // write media into the database
            if (subTest.getMedia() != null) {
                SubTestMedia media = subTest.getMedia();
                media.setItemSetId(testDeliveryItemsetId);
                mediaGateway.saveMedia(media, assessmentType.getMediaPath());
            }

            List idList = new ArrayList();
            idList.add(testDeliveryItemsetId);
            return idList;

        } catch (HibernateException he) {
            handleHibernateException(he);
            return null;
        }

    }
    
    protected void createTStoTDs( Long TSItemsetId, SubTestHolder subTest ) throws HibernateException
    {
        for ( int i = 0; i < subTest.childSubTestIds.size(); i++ )
        {
            Long TD_ID = ( Long )subTest.childSubTestIds.get( i );
            connectTC2TD( TSItemsetId, TD_ID, i + 1 );
        }
    }
    
    protected void connectTC2TD( Long TSItemsetId, Long testDeliveryItemsetId, int order )
    				throws HibernateException 
    {
		ItemSetParentCompositeId id = new ItemSetParentCompositeId();
		id.setItemSetId( testDeliveryItemsetId );
		id.setParentItemSetId( TSItemsetId );
		
		ItemSetParentRecord itemSetParent = new ItemSetParentRecord();
		itemSetParent.setId(id);
		itemSetParent.setItemSetType(OASConstants.ITEM_SET_TYPE_TEST_DELIVERY);
		itemSetParent.setParentItemSetType( "TS");
		itemSetParent.setItemSetSortOrder(new Long(order));
		itemSetParent.setCreatedDateTime(new Date());
		itemSetParent.setUpdatedDateTime(new Date());
		itemSetParent.setCreatedBy(this.userID);
		itemSetParent.setUpdatedBy(this.userID);
		
		session.save(itemSetParent);
	}
    
    private void clearTStoTDs( Long TSItemsetId ) throws HibernateException
    {
        session.delete("from " + ItemSetParentRecord.class.getName()
                + " as itemSetParent where itemSetParent.id.parentItemSetId = " + TSItemsetId);
        session.flush();
    }
    
    public void clearTStoTDsByForm( Long TSItemsetId, Long child_item_set_id )throws HibernateException
    {
        String form = getItemSetRecord( child_item_set_id ).getItemSetForm();
        session.delete("from " + ItemSetParentRecord.class.getName()
                + " as itemSetParent where itemSetParent.id.parentItemSetId = " + TSItemsetId
                + " and exists( select itemSet.itemSetId from " + ItemSetRecord.class.getName() 
                + " as itemSet where itemSetParent.id.itemSetId = itemSet.itemSetId and itemSet.itemSetForm = '" 
                + form + "' ) " );
        session.flush();
    }

    /* *********************************************************************** */
    
    private void makeUnusedItemsVisible(SubTestHolder subTest, Long testDeliveryItemsetId)
            throws HibernateException {

        Query query = session.createQuery("from " + ItemSetItemRecord.class.getName()
                + " as itemSetItem " + "where itemSetItem.id.itemSetId = :itemSetId");
        query.setLong("itemSetId", testDeliveryItemsetId.longValue());

        for (Iterator itemSetItemAssoc = query.iterate(); itemSetItemAssoc.hasNext();) {
            ItemSetItemRecord itemSetItemRecord = (ItemSetItemRecord) itemSetItemAssoc.next();

            if (!isAssociatedItem(itemSetItemRecord.getId().getItemId(), subTest.getItems())) {

                if (numberOfActiveTDAssociated(testDeliveryItemsetId, itemSetItemRecord.getId()
                        .getItemId()) == 0) 
                {
                    changeItemState( subTest.getFrameworkCode(), itemSetItemRecord.getId().getItemId(), false );                   
                }
            }
        }
    }

    private int numberOfActiveTDAssociated(Long testDeliveryItemsetId, String itemId)
            throws HibernateException {
        Query query = session.createQuery("select count(*) from "
                + ItemSetItemRecord.class.getName() + " as itemSetItem, "
                + ItemSetRecord.class.getName() + " as itemSet "
                + "where itemSetItem.id.itemId = :itemId "
                + "and itemSetItem.id.itemSetId = itemSet.itemSetId "
                + "and itemSet.itemSetId != :testDeliveryItemsetId "
                + "and itemSet.activationStatus = :activationStatus "
                + "and itemSet.itemSetType = :itemSetType");

        query.setString("itemId", itemId);
        query.setLong("testDeliveryItemsetId", testDeliveryItemsetId.longValue());
        query.setString("activationStatus", OASConstants.ITEM_SET_STATUS_ACTIVE);
        query.setString("itemSetType", OASConstants.ITEM_SET_TYPE_TEST_DELIVERY);

        return ((Integer) query.uniqueResult()).intValue();
    }

    private boolean isAssociatedItem(String itemId, Iterator items) {
        for (Iterator iter = items; iter.hasNext();) {
            SubTestHolder.TestItem testItem = (SubTestHolder.TestItem) iter.next();
            if (testItem.getItemId().equals(itemId)) {
                return true;
            }
        }

        return false;
    }

    private void clearRelationships(Long productId, Long testDeliveryItemsetId)
            throws HibernateException {

        // ITEM_SET_PRODUCT: delete the existing item_set (TD) / product relationship
        ItemSetRelationship.clearProducts(session,testDeliveryItemsetId,productId);

        // SCORE_LOOKUP_ITEM_SET: delete the existing score_lookup / item_set relationship
       // SM ItemSetRelationship.clearScoreLookups(session,testDeliveryItemsetId);
        //HibernateUtils.safeDelete(session, ScoreLookupItemSetRecord.class, testDeliveryItemsetId);

        // ITEM_SET_ITEM: delete item / item_set relationships
          session.delete("from " + ItemSetItemRecord.class.getName()
                + " as isi where isi.id.itemSetId = " + testDeliveryItemsetId);

        // SCORABLE_ITEM: delete scorable_item / item_set /item relationships
       //SM session.delete("from " + ScorableItemRecord.class.getName()
       //SM         + " as si where si.id.itemSetId = " + testDeliveryItemsetId);

        session.flush();
    }

    /* *********************************************************************** */

    private void createRelationships(SubTestHolder subTest, Long testDeliveryItemsetId,
            Long productId) throws HibernateException {

        // Link Product to TD Item Sets
        connectItemSet2Product(testDeliveryItemsetId, productId);

        // Link the TD to the Score_Lookup in the SCORE_LOOKUP_ITEM_SET table
        //TODO - should we do this if scoreTypeCode is null????

        if (subTest.isTDType() && !subTest.getScoreTypeCode().equals("") && !subTest.getScoreLookupId().equals(""))
            connectTD2ScoreLookup(subTest.getScoreLookupId(), testDeliveryItemsetId);

        // Loop through the items from the Test data structure
        int counter = subTest.getStartItemNumber().intValue();
        
        for (Iterator iter = subTest.getItems(); iter.hasNext();) {
            SubTestHolder.TestItem testItem = (SubTestHolder.TestItem) iter.next();
            String itemId = testItem.getItemId();

            // Link the Items to TD in the ITEM_SET_ITEM table
            connectItems2TD(testDeliveryItemsetId, new Long(counter++), itemId
                    		, testItem.getFieldTest(), testItem.getSuppressed() );

            // Link SCORE_TYPE_CODE to Scorable Items in SCORABLE_ITEM table
            if (testItem.isScorable()) {
                String scoreTypeCode = testItem.getScoreTypeCode();

                createScorableItem(scoreTypeCode, testDeliveryItemsetId, itemId);
            }
        }
    }

    /* *********************************************************************** */

    private void connectTD2ScoreLookup(String scoreLookupID, Long testDeliveryItemsetId)
            throws HibernateException {

        ScoreLookupItemSetRecord scoreLookupItemSet = new ScoreLookupItemSetRecord();
        scoreLookupItemSet.setItemSetId(testDeliveryItemsetId);
        scoreLookupItemSet.setScoreLookupId(scoreLookupID);

        session.save(scoreLookupItemSet);
    }

    private void connectItems2TD(Long testDeliveryItemsetId, Long order, String itemId
            					, String fieldTest_, String suppressed_ )
            throws HibernateException {

        ItemSetItemCompositeId id = new ItemSetItemCompositeId();
        id.setItemId(itemId);
        id.setItemSetId(testDeliveryItemsetId);

        ItemSetItemRecord itemSetItem = new ItemSetItemRecord();
        itemSetItem.setId(id);
        itemSetItem.setCreatedDateTime(new Date());
        itemSetItem.setCreatedBy(this.userID);
        itemSetItem.setUpdatedDateTime(new Date());
        itemSetItem.setItemSortOrder(order);
        itemSetItem.setFieldTest( fieldTest_ );
        itemSetItem.setSuppressed( suppressed_ );

        session.save(itemSetItem);
    }

    private void createScorableItem(String scoreTypeCode, Long testDeliveryItemsetId, String itemId)
            throws HibernateException {

        ScorableItemCompositeId id = new ScorableItemCompositeId();
        id.setItemSetId(testDeliveryItemsetId);
        id.setItemId(itemId);
        id.setScoreTypeCode(scoreTypeCode);

        ScorableItemRecord scorableItem = new ScorableItemRecord();
        scorableItem.setId(id);

        session.save(scorableItem);
    }

    public boolean subTestExistsActiveOrInactive(Long subTestItemSetId) {
        try {
            ItemSetRecord itemSetRecordTD = (ItemSetRecord) session.get(ItemSetRecord.class,
                    subTestItemSetId);
            return (itemSetRecordTD != null);
        } catch (HibernateException e) {
            return false;
        }
    }

    public boolean activeSubTestExists(Long subTestItemSetId) {
        try {
            ItemSetRecord itemSetRecordTD = (ItemSetRecord) session.get(ItemSetRecord.class,
                    subTestItemSetId);
            return ( (itemSetRecordTD != null) && itemSetRecordTD
                    .equals(OASConstants.ITEM_SET_STATUS_ACTIVE));
        } catch (HibernateException e) {
            return false;
        }
    }

    public List getScoreTypeCodes() {
        List scoreTypeCodes = new ArrayList();

        try {
            Query query = session.createQuery("select scoreType from "
                    + ScoreTypeRecord.class.getName() + " as scoreType");

            for (Iterator iter = query.iterate(); iter.hasNext();) {
                ScoreTypeRecord scoreType = (ScoreTypeRecord) iter.next();
                scoreTypeCodes.add(scoreType.getScoreTypeCode());
            }

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }

        return scoreTypeCodes;
    }

}