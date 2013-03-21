package com.ctb.contentBridge.core.publish.xml.subtest;

import java.util.*;

import com.ctb.contentBridge.core.publish.dao.AbstractDBSubTestGateway;
import com.ctb.contentBridge.core.publish.dao.ContentAreaInfo;
import com.ctb.contentBridge.core.publish.dao.DBSubTestMediaGateway;
import com.ctb.contentBridge.core.publish.dao.ItemSetRelationship;
import com.ctb.contentBridge.core.publish.dao.ProductConfig;
import com.ctb.contentBridge.core.publish.dao.ProductTypeInfo;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetItemCompositeId;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetItemRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ScorableItemCompositeId;
import com.ctb.contentBridge.core.publish.hibernate.persist.ScorableItemRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ScoreLookupItemSetRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ScoreLookupRecord;
import com.ctb.contentBridge.core.publish.tools.OASConstants;
import com.ctb.contentBridge.core.publish.xml.assessment.AssessmentType;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;


public class DBSubTestGatewayTerra1 extends AbstractDBSubTestGateway {
    private DBSubTestMediaGateway mediaGateway;
    private ProductConfig productConfig;

    public DBSubTestGatewayTerra1(Session session, ProductConfig productConfig) {
        super(session);
        mediaGateway = new DBSubTestMediaGateway(session);
        this.productConfig = productConfig;
    }

    public List writeSubTest(SubTestHolder subTest, AssessmentType assessmentType) {
        Long itemSetId = writeSubTest(subTest, assessmentType, subTest.isSample(), subTest
                .getMedia());

        List idList = new ArrayList();
        idList.add(itemSetId);
        return idList;
    }

    public Long writeSubTest(SubTestHolder subTest, AssessmentType assessmentType,
            boolean isSample, SubTestMedia media) {

        Long productId = null;
        if ( assessmentType.getName().equals( "AAS" ))
        {
            productId = getProductID(subTest.getFrameworkCode(), subTest.getProductDisplayName() );
        }
        else
        {
	        productId = getProductID(subTest.getFrameworkCode(), subTest.getProductDisplayName(),
	                assessmentType.getProductType());
        }
        
        try {

            ItemSetRecord itemSetRecordTD = getItemSetRecord(subTest.getExtTstItemSetId(),
                    OASConstants.ITEM_SET_TYPE_TEST_DELIVERY, isSample);

            Long testDeliveryItemsetId = (itemSetRecordTD == null) ? null : itemSetRecordTD
                    .getItemSetId();

            if (testDeliveryItemsetId != null) {
                makeUnusedItemsVisible(subTest, testDeliveryItemsetId);
                clearRelationships(productId, testDeliveryItemsetId);
            }

            testDeliveryItemsetId = createOrUpdateItemSet(subTest,
                    OASConstants.ITEM_SET_TYPE_TEST_DELIVERY, testDeliveryItemsetId,
                    assessmentType.getMediaPath(), isSample).getItemSetId();

            createRelationships(subTest, testDeliveryItemsetId, productId, isSample);

            // write media into the database
            if (media != null) {
                media.setItemSetId(testDeliveryItemsetId);
                mediaGateway.saveMedia(media, assessmentType.getMediaPath());
            }

            return testDeliveryItemsetId;

        } catch (HibernateException he) {
            handleHibernateException(he);
            return null;
        }

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
                        .getItemId()) == 0) {

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

        // SCORE_LOOKUP_ITEM_SET: delete all existing score_lookup / item_set relationship
        ItemSetRelationship.clearScoreLookups(session,testDeliveryItemsetId);

        // ITEM_SET_ITEM: delete item / item_set relationships
        session.delete("from " + ItemSetItemRecord.class.getName()
                + " as isi where isi.id.itemSetId = " + testDeliveryItemsetId);

        // SCORABLE_ITEM: delete scorable_item / item_set /item relationships
        session.delete("from " + ScorableItemRecord.class.getName()
                + " as si where si.id.itemSetId = " + testDeliveryItemsetId);

        session.flush();
    }

    /* *********************************************************************** */

    private void createRelationships(SubTestHolder subTest, Long testDeliveryItemsetId,
            Long productId, boolean isSample) throws HibernateException {

        // Link Product to TD Item Sets
        connectItemSet2Product(testDeliveryItemsetId, productId);

        // Link the TD to the Score_Lookup in the SCORE_LOOKUP_ITEM_SET table
        connectTD2ScoreLookup(subTest, testDeliveryItemsetId);

        // Loop through the items from the Test data structure
        int counter = 1;

        for (Iterator iter = subTest.getItems(); iter.hasNext();) {
            SubTestHolder.TestItem testItem = (SubTestHolder.TestItem) iter.next();
            String itemId = testItem.getItemId();

            if (!isSample) {
                // Link the Items to TD in the ITEM_SET_ITEM table
                connectItems2TD(testDeliveryItemsetId, new Long(counter++), itemId
                        , testItem.getFieldTest(), testItem.getSuppressed());

                // Link SCORE_TYPE_CODE to Scorable Items in SCORABLE_ITEM table
                if (testItem.isScorable()) {
                    String scoreTypeCode = testItem.getScoreTypeCode();
                    createScorableItem(subTest, testDeliveryItemsetId, itemId);
                }
            }
        }
    }

    /* *********************************************************************** */

    private void connectItems2TD(Long testDeliveryItemsetId, Long order, String itemId
            , String fieldTest_, String suppressed_)
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

    private void connectTD2ScoreLookup(SubTestHolder subTest, Long testDeliveryItemsetId)
            throws HibernateException {

        // get the possible
        ContentAreaInfo contentArea = productConfig.findProductType(subTest.getFrameworkCode(),
                subTest.getProductDisplayName()).findContentArea(subTest.getContentArea());
        Collection codes = contentArea.getScoreLookupCodes(session,subTest.getFrameworkCode(),
                subTest.getItemSetLevel(),subTest.getItemSetForm(),
                subTest.getFrameworkCode() + " " + subTest.getProductDisplayName());
        for (Iterator iter = codes.iterator(); iter.hasNext();) {
            ScoreLookupRecord scoreLookupRecord = (ScoreLookupRecord) iter.next();
            ScoreLookupItemSetRecord scoreLookupItemSet = new ScoreLookupItemSetRecord();
            scoreLookupItemSet.setItemSetId(testDeliveryItemsetId);
            scoreLookupItemSet.setScoreLookupId(scoreLookupRecord.getScoreLookupId());
            session.save(scoreLookupItemSet);
        }

    }

    private void createScorableItem(SubTestHolder subTest, Long testDeliveryItemsetId, String itemId)
            throws HibernateException {

        ProductTypeInfo productType = productConfig.findProductType(subTest.getFrameworkCode(),
                subTest.getProductDisplayName());
        List scoreList = productType.getScoreTypes();
        if ( scoreList != null )
        {
	        for (Iterator iter = scoreList.iterator(); iter.hasNext();) {
	            String scoreTypeCode = (String) iter.next();
	
	            ScorableItemCompositeId id = new ScorableItemCompositeId();
	            id.setItemSetId(testDeliveryItemsetId);
	            id.setItemId(itemId);
	            id.setScoreTypeCode(scoreTypeCode);
	
	            ScorableItemRecord scorableItem = new ScorableItemRecord();
	            scorableItem.setId(id);
	
	            session.save(scorableItem);
	        }
        }
    }
}