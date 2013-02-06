package com.ctb.contentBridge.core.publish.sofa;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.dao.DBObjectivesGateway;
import com.ctb.contentBridge.core.publish.dao.DBSubTestMediaGateway;
import com.ctb.contentBridge.core.publish.dao.ItemSetRelationship;
import com.ctb.contentBridge.core.publish.hibernate.HibernateUtils;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetItemCompositeId;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetItemRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetParentCompositeId;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetParentRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetProductCompositeId;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetProductRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.OrgNodeTestCatalogCompositeId;
import com.ctb.contentBridge.core.publish.hibernate.persist.OrgNodeTestCatalogRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ScorableItemCompositeId;
import com.ctb.contentBridge.core.publish.hibernate.persist.ScorableItemRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ScoreLookupItemSetRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.TestCatalogRecord;
import com.ctb.contentBridge.core.publish.sofa.PremadeTestHolder;
import com.ctb.contentBridge.core.publish.sofa.PremadeTestHolder.TestItem;
import com.ctb.contentBridge.core.publish.tools.MediaMapper;
import com.ctb.contentBridge.core.publish.tools.OASConstants;
import com.ctb.contentBridge.core.publish.xml.subtest.SubTestMedia;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;


/**
 * @author wmli
 */
public class DBPremadeTestGateway {
    private Long userID = new Long(OASConstants.CREATED_BY);
    private Session session;
    private DBObjectivesGateway ogw;

    public static final Long ROOT_ORG_NODE_ID = new Long(1);
    public static final Long ROOT_CUSTOMER_ID = new Long(1);

    public static final Long CTB_ORG_NODE_ID = new Long(2);
    public static final Long CTB_CUSTOMER_ID = new Long(2);

    private List orgNodes;
    private DBSubTestMediaGateway mediaGateway;

    public DBPremadeTestGateway(Session session) {
        this.session = session;
        mediaGateway = new DBSubTestMediaGateway(session);

        // dbconnection.setDebug(true);
        this.ogw = new DBObjectivesGateway(session);

        // setup default org node information
        orgNodes = new ArrayList();
        orgNodes.add(
            new Object[] {
                DBPremadeTestGateway.ROOT_ORG_NODE_ID,
                DBPremadeTestGateway.ROOT_CUSTOMER_ID });
        orgNodes.add(
            new Object[] {
                DBPremadeTestGateway.CTB_ORG_NODE_ID,
                DBPremadeTestGateway.CTB_CUSTOMER_ID });
    }

    public void writePremadeTestIntoDatabase(
        PremadeTestHolder premadeTest,
        SubTestMedia media)
        throws HibernateException {

        Long testCatalogId = null;
        Long testCatalogItemsetId = null;
        Long testDeliveryItemsetId = null;

        // Retrieve Product ID usingthe objectives Database
        Long productId = getProductID(premadeTest);

        if (isNewPremadeTest(premadeTest)) {
            // INSERT MODE

            //Create TC and TD in ITEM_SET table
            testCatalogItemsetId =
                createItemSet(
                    premadeTest,
                    OASConstants.ITEM_SET_TYPE_TEST_CATALOG);
            testDeliveryItemsetId =
                createItemSet(
                    premadeTest,
                    OASConstants.ITEM_SET_TYPE_TEST_DELIVERY);

            // Create Test Catalog in TEST_CATALOG table
            testCatalogId =
                createTestCatalog(premadeTest, testCatalogItemsetId, productId);
        } else {
            // UPDATE MODE
            String extTestId = premadeTest.getExtTstItemSetId();

            ItemSetRecord itemSetTestCatalog =
                getUniqueItemSetRecord(
                    extTestId,
                    OASConstants.ITEM_SET_TYPE_TEST_CATALOG);
            testCatalogItemsetId = itemSetTestCatalog.getItemSetId();

            ItemSetRecord itemSetTestDelivery =
                getUniqueItemSetRecord(
                    extTestId,
                    OASConstants.ITEM_SET_TYPE_TEST_DELIVERY);
            testDeliveryItemsetId = itemSetTestDelivery.getItemSetId();

            TestCatalogRecord testCatalogRecord =
                getUniqueTestCatalogRecord(testCatalogItemsetId);
            testCatalogId = testCatalogRecord.getTestCatalogId();

            deactivateUnusedItems(premadeTest, testDeliveryItemsetId);

            clearRelationships(
                productId,
                testCatalogItemsetId,
                testDeliveryItemsetId,
                testCatalogId);

            createOrUpdateItemSet(
                premadeTest,
                OASConstants.ITEM_SET_TYPE_TEST_CATALOG,
                testCatalogItemsetId);
            createOrUpdateItemSet(
                premadeTest,
                OASConstants.ITEM_SET_TYPE_TEST_DELIVERY,
                testDeliveryItemsetId);

            createOrUpdateTestCatalog(
                premadeTest,
                testCatalogItemsetId,
                productId,
                testCatalogId);
        }

        createRelationships(
            premadeTest,
            testCatalogId,
            testCatalogItemsetId,
            testDeliveryItemsetId,
            productId);

        // write media into the database
        if (media != null) {
            media.setItemSetId(testDeliveryItemsetId);
            mediaGateway.saveMedia(media, MediaMapper.CAB_MEDIA_PATH);

        }
    }

    /* ************************************************************************/

    private TestCatalogRecord getUniqueTestCatalogRecord(Long testCatalogItemsetId)
        throws HibernateException {
        Query query =
            session.createQuery(
                "from "
                    + TestCatalogRecord.class.getName()
                    + " as tc where tc.itemSetId = :testCatalogItemSetId");
        query.setLong("testCatalogItemSetId", testCatalogItemsetId.longValue());
        TestCatalogRecord testCatalogRecord =
            (TestCatalogRecord) query.uniqueResult();
        if (testCatalogRecord == null)
            throw new BusinessException(
                "No Test Catalog entry found for Item Set "
                    + testCatalogItemsetId);
        return testCatalogRecord;
    }

    private ItemSetRecord getUniqueItemSetRecord(
        String extTstsItemSetId,
        String itemSetType)
        throws HibernateException {
        ItemSetRecord itemSetRecord =
            getItemSetRecord(extTstsItemSetId, itemSetType);
        if (itemSetRecord == null)
            throw new BusinessException(
                "No Item Set entry of Item Set Type ["
                    + itemSetType
                    + "] found for ["
                    + extTstsItemSetId
                    + "]");
        return itemSetRecord;
    }

    private ItemSetRecord getItemSetRecord(
        String extTstsItemSetId,
        String itemSetType)
        throws HibernateException {
        Query query =
            session.createQuery(
                "from "
                    + ItemSetRecord.class.getName()
                    + " as isr where isr.itemSetType = :itemSetType and isr.extTstItemSetId = :extTstItemSetId and isr.activationStatus = 'AC'");
        query.setString("itemSetType", itemSetType);
        query.setString("extTstItemSetId", extTstsItemSetId);
        return (ItemSetRecord) query.uniqueResult();
    }

    private boolean isNewPremadeTest(PremadeTestHolder premadeTest)
        throws HibernateException {
        return (
            getItemSetRecord(
                premadeTest.getExtTstItemSetId(),
                OASConstants.ITEM_SET_TYPE_TEST_CATALOG)
                == null);
    }

    /* ************************************************************************/

    private void deactivateUnusedItems(
        PremadeTestHolder premadeTest,
        Long testDeliveryItemsetId)
        throws HibernateException {
        String commaSeparatedListOfItemIDs = "";
        Iterator iter = premadeTest.getItems();
        while (iter.hasNext()) {
            commaSeparatedListOfItemIDs =
                commaSeparatedListOfItemIDs
                    + "'"
                    + ((TestItem) iter.next()).getItemId()
                    + "'";
            if (iter.hasNext())
                commaSeparatedListOfItemIDs += ", ";
        }
        List unusedPreviousItemsInSubTest =
            session.find(
                "from "
                    + ItemRecord.class.getName()
                    + " as item where item.itemId in ( select isi.id.itemId from "
                    + ItemSetItemRecord.class.getName()
                    + " as isi where isi.id.itemSetId = '"
                    + testDeliveryItemsetId
                    + "') and item.activationStatus = '"
                    + OASConstants.ITEM_STATUS_ACTIVE
                    + "' and item.itemId not in ("
                    + commaSeparatedListOfItemIDs
                    + ")");
        for (Iterator iterator = unusedPreviousItemsInSubTest.iterator();
            iterator.hasNext();
            ) {
            ItemRecord itemRecord = (ItemRecord) iterator.next();
            itemRecord.setActivationStatus(OASConstants.ITEM_STATUS_INACTIVE);
            session.saveOrUpdate(itemRecord);
        }
    }

    Long getProductID(PremadeTestHolder premadeTest) {
        int productId =
            this.ogw.getProductID(
                premadeTest.getFrameworkCode(),
                premadeTest.getProductDisplayName(),
                OASConstants.SCALED_FORMATIVE_PRODUCT_TYPE_CODE);

        return new Long(productId);
    }

    /* ************************************************************************/

    private Long createTestCatalog(
        PremadeTestHolder premadeTest,
        Long testCatalogItemsetId,
        Long productId)
        throws HibernateException {
        return createOrUpdateTestCatalog(
            premadeTest,
            testCatalogItemsetId,
            productId,
            null)
            .getTestCatalogId();
    }

    private TestCatalogRecord createOrUpdateTestCatalog(
        PremadeTestHolder premadeTest,
        Long testCatalogItemSetId,
        Long productId,
        Long testCatalogId)
        throws HibernateException {
        TestCatalogRecord testCatalog = new TestCatalogRecord();
        testCatalog.setTestName(premadeTest.getTestName());
        testCatalog.setTestDisplayName(premadeTest.getItemSetDisplayName());
        testCatalog.setSubject(premadeTest.getProductDisplayName()); //???
        testCatalog.setTestGrade(premadeTest.getGrade());
        testCatalog.setTestForm(premadeTest.getItemSetForm());
        testCatalog.setCreatedDateTime(new Date());
        testCatalog.setTestLevel(premadeTest.getItemSetLevel());
        testCatalog.setVersion(premadeTest.getVersion());
        testCatalog.setCreatedBy(this.userID);
        testCatalog.setUpdatedDateTime(new Date());
        testCatalog.setActivationStatus(OASConstants.ITEM_STATUS_ACTIVE);

        testCatalog.setProductId(productId);
        testCatalog.setItemSetId(testCatalogItemSetId);
        testCatalog.setTestCatalogId(testCatalogId);
        return (TestCatalogRecord) session.saveOrUpdateCopy(testCatalog);
    }

    private Long createItemSet(
        PremadeTestHolder premadeTest,
        String itemSetType)
        throws HibernateException {
        return createOrUpdateItemSet(premadeTest, itemSetType, null)
            .getItemSetId();
    }

    private ItemSetRecord createOrUpdateItemSet(
        PremadeTestHolder premadeTest,
        String itemSetType,
        Long itemSetId)
        throws HibernateException {
        ItemSetRecord itemSet = new ItemSetRecord();
        itemSet.setActivationStatus(OASConstants.ITEM_STATUS_ACTIVE);
        itemSet.setBreakTime(new Long(premadeTest.getBreakTime()));
        itemSet.setCreatedBy(this.userID);
        itemSet.setCreatedDateTime(new Date());
        itemSet.setExtTstItemSetId(premadeTest.getExtTstItemSetId());
        itemSet.setGrade(premadeTest.getGrade());
        itemSet.setItemSetDescription(premadeTest.getItemSetDescription());
        itemSet.setItemSetDisplayName(premadeTest.getItemSetDisplayName());
        itemSet.setItemSetForm(premadeTest.getItemSetForm());
        itemSet.setItemSetLevel(premadeTest.getItemSetLevel());
        itemSet.setItemSetName(premadeTest.getTestName());
        itemSet.setMediaPath(MediaMapper.CAB_MEDIA_PATH);
        itemSet.setSample(OASConstants.DB_FALSE);
        itemSet.setTimeLimit(new Long(premadeTest.getTimeLimit()));
        itemSet.setUpdatedDateTime(new Date());
        itemSet.setVersion(premadeTest.getVersion());

        itemSet.setItemSetType(itemSetType);
        itemSet.setItemSetId(itemSetId);
        return (ItemSetRecord) session.saveOrUpdateCopy(itemSet);
    }

    /* ************************************************************************/

    private void clearRelationships(
        Long productId,
        Long testCatalogItemsetId,
        Long testDeliveryItemsetId,
        Long testCatalogId)
        throws HibernateException {

        // ITEM_SET_PARENT: delete the existing item_set (TC) / item_set (TD) hierarchy
        ItemSetParentCompositeId id = new ItemSetParentCompositeId();
        id.setItemSetId(testDeliveryItemsetId);
        id.setParentItemSetId(testCatalogItemsetId);
        HibernateUtils.safeDelete(session, ItemSetParentRecord.class, id);

        // ITEM_SET_PRODUCT: delete the existing item_set (TC) / product relationship
        ItemSetRelationship.clearProducts(session,testCatalogItemsetId,productId);

        // ITEM_SET_PRODUCT: delete the existing item_set (TD) / product relationship
        ItemSetRelationship.clearProducts(session,testDeliveryItemsetId,productId);

        // ORG_NODE_TEST_CATALOG: delete the existing org_node / test_catalog relationships
        for (Iterator iter = orgNodes.iterator(); iter.hasNext();) {
            Object[] element = (Object[]) iter.next();
            OrgNodeTestCatalogCompositeId id3 =
                new OrgNodeTestCatalogCompositeId();
            id3.setItemSetId(testCatalogItemsetId);
            id3.setTestCatalogId(testCatalogId);
            id3.setOrgNodeId((Long) element[0]); //ORG_NODE_ID
            HibernateUtils.safeDelete(
                session,
                OrgNodeTestCatalogRecord.class,
                id3);
        }

        // SCORE_LOOKUP_ITEM_SET: delete the existing score_lookup / item_set relationship
        ItemSetRelationship.clearScoreLookups(session,testDeliveryItemsetId);
        // ITEM_SET_ITEM: delete item / item_set relationships
        session.delete(
            "from "
                + ItemSetItemRecord.class.getName()
                + " as isi where isi.id.itemSetId = "
                + testDeliveryItemsetId);

        // SCORABLE_ITEM: delete scorable_item / item_set /item relationships
        session.delete(
            "from "
                + ScorableItemRecord.class.getName()
                + " as si where si.id.itemSetId = "
                + testDeliveryItemsetId);
    }

    /* ************************************************************************/

    private void createRelationships(
        PremadeTestHolder premadeTest,
        Long testCatalogId,
        Long testCatalogItemsetId,
        Long testDeliveryItemsetId,
        Long productId)
        throws HibernateException {
        // Link TD to TC to create the hierarchy
        connectTC2TD(testCatalogItemsetId, testDeliveryItemsetId);

        // Link Product to TD and TC Item Sets
        connectItemSet2Product(testCatalogItemsetId, productId);
        connectItemSet2Product(testDeliveryItemsetId, productId);

        // Link TC to Org_Node in ORG_NODE_TEST_CATALOG table
        for (Iterator iter = orgNodes.iterator(); iter.hasNext();) {
            Object[] orgNode = (Object[]) iter.next();

            connectOrgNode2TestCatalog(
                testCatalogId,
                testCatalogItemsetId,
                productId,
                (Long) orgNode[0],
                (Long) orgNode[1]);
        }

        // Link the TD to the Score_Lookup in the SCORE_LOOKUP_ITEM_SET table
        connectTD2ScoreLookup(
            premadeTest.getScoreLookupId(),
            testDeliveryItemsetId);

        // Loop through the items from the Test data structure
        int counter = 1;

        for (Iterator iter = premadeTest.getItems(); iter.hasNext();) {
            TestItem testItem = (TestItem) iter.next();
            String itemId = testItem.getItemId();

            // Link the Items to TD in the ITEM_SET_ITEM table
            connectItems2TD(testDeliveryItemsetId, new Long(counter++), itemId
                    		, testItem.getFieldTest(), testItem.getSuppressed() );

            // Link SCORE_TYPE_CODE to Scorable Items in SCORABLE_ITEM table
            if (testItem.isScorable()) {
                String scoreTypeCode = testItem.getScoreTypeCode();

                createScorableItem(
                    scoreTypeCode,
                    testDeliveryItemsetId,
                    itemId);
            }
        }
    }

    /* ************************************************************************/

    private void connectTC2TD(
        Long testCatalogItemsetId,
        Long testDeliveryItemsetId)
        throws HibernateException {

        ItemSetParentCompositeId id = new ItemSetParentCompositeId();
        id.setItemSetId(testDeliveryItemsetId);
        id.setParentItemSetId(testCatalogItemsetId);

        ItemSetParentRecord itemSetParent = new ItemSetParentRecord();
        itemSetParent.setId(id);
        itemSetParent.setItemSetType(OASConstants.ITEM_SET_TYPE_TEST_DELIVERY);
        itemSetParent.setParentItemSetType(
            OASConstants.ITEM_SET_TYPE_TEST_CATALOG);
        itemSetParent.setCreatedDateTime(new Date());
        itemSetParent.setUpdatedDateTime(new Date());
        itemSetParent.setCreatedBy(this.userID);

        session.save(itemSetParent);
    }

    private void connectItemSet2Product(Long itemSetId, Long productId)
        throws HibernateException {

        ItemSetProductCompositeId id = new ItemSetProductCompositeId();
        id.setItemSetId(itemSetId);
        id.setProductId(productId);

        ItemSetProductRecord itemSetProduct = new ItemSetProductRecord();
        itemSetProduct.setId(id);
        itemSetProduct.setCreatedDateTime(new Date());
        itemSetProduct.setCreatedBy(this.userID);
        itemSetProduct.setUpdatedDateTime(new Date());

        session.save(itemSetProduct);
    }

    private void connectOrgNode2TestCatalog(
        Long testCatalogId,
        Long testCatalogItemsetId,
        Long productId,
        Long orgNodeId,
        Long customerId)
        throws HibernateException {

        OrgNodeTestCatalogCompositeId id = new OrgNodeTestCatalogCompositeId();
        id.setItemSetId(testCatalogItemsetId);
        id.setOrgNodeId(orgNodeId);
        id.setTestCatalogId(testCatalogId);

        OrgNodeTestCatalogRecord orgNodeTestCatalog =
            new OrgNodeTestCatalogRecord();
        orgNodeTestCatalog.setId(id);
        orgNodeTestCatalog.setCreatedDateTime(new Date());
        orgNodeTestCatalog.setCreatedBy(this.userID);
        orgNodeTestCatalog.setUpdatedDateTime(new Date());
        orgNodeTestCatalog.setCustomerId(customerId);
        orgNodeTestCatalog.setProductId(productId);
        orgNodeTestCatalog.setActivationStatus(OASConstants.ITEM_STATUS_ACTIVE);

        session.save(orgNodeTestCatalog);
    }

    private void connectTD2ScoreLookup(
        String scoreLookupID,
        Long testDeliveryItemsetId)
        throws HibernateException {

        ScoreLookupItemSetRecord scoreLookupItemSet =
            new ScoreLookupItemSetRecord();
        scoreLookupItemSet.setItemSetId(testDeliveryItemsetId);
        scoreLookupItemSet.setScoreLookupId(scoreLookupID);

        session.save(scoreLookupItemSet);
    }

    private void connectItems2TD(
        Long testDeliveryItemsetId,
        Long order,
        String itemId, String fieldTest_, String suppressed_)
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

    private void createScorableItem(
        String scoreTypeCode,
        Long testDeliveryItemsetId,
        String itemId)
        throws HibernateException {

        ScorableItemCompositeId id = new ScorableItemCompositeId();
        id.setItemSetId(testDeliveryItemsetId);
        id.setItemId(itemId);
        id.setScoreTypeCode(scoreTypeCode);

        ScorableItemRecord scorableItem = new ScorableItemRecord();
        scorableItem.setId(id);

        session.save(scorableItem);
    }

}