package com.ctb.xmlProcessing.assessment;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.ctb.common.tools.OASConstants;
import com.ctb.common.tools.SystemException;
import com.ctb.hibernate.HibernateUtils;
import com.ctb.hibernate.persist.ItemSetParentCompositeId;
import com.ctb.hibernate.persist.ItemSetParentRecord;


import com.ctb.hibernate.persist.ItemSetRecord;
import com.ctb.hibernate.persist.OrgNodeTestCatalogCompositeId;
import com.ctb.hibernate.persist.OrgNodeTestCatalogRecord;
import com.ctb.hibernate.persist.TestCatalogRecord;
import com.ctb.xmlProcessing.OrgNode;
import com.ctb.util.persist.ItemSetRelationship;

/**
 * @author wmli
 */
public class DBAssessmentGateway extends AbstractDBAssessmentGateway {

    public DBAssessmentGateway(Session session) {
        super(session);
    }

    public void writeAssessment(AssessmentHolder assessment, AssessmentType assessmentType) {

        Long productId = assessment.getProductID();
        if ( productId == null )
        	productId = getProductID(assessment.getFrameworkCode(), assessment
                .getProductDisplayName(), assessmentType.getProductType());

        try {

            ItemSetRecord itemSetRecordTC = getItemSetRecord(assessment.getExtTstItemSetId(),
                    OASConstants.ITEM_SET_TYPE_TEST_CATALOG);

            Long testCatalogItemSetId = (itemSetRecordTC == null) ? null : itemSetRecordTC
                    .getItemSetId();

            Long testCatalogId = null;

            // if item set for assessment existed. Deactivate unused subtest item set and clear the
            // relation to the assessment itemset.
            if (testCatalogItemSetId != null) {
                TestCatalogRecord testCatalogRecord = getTestCatalogRecord(testCatalogItemSetId);

                if (testCatalogRecord == null)
                    throw new SystemException(
                            "Cannot retrieve associate test catalog for TC item set.");

                testCatalogId = (testCatalogRecord == null) ? null : testCatalogRecord
                        .getTestCatalogId();
                if ( !assessment.isAddOn.booleanValue() )
                    deactivateUnusedSubTests(testCatalogItemSetId, assessment.getSubTestIds());

                clearRelationships(productId, testCatalogItemSetId, assessment.getSubTestIds(),
                        testCatalogId, assessment.isAddOn );
            }

            // create/update the item set record for the assessment.
            testCatalogItemSetId = createOrUpdateItemSet(assessment,
                    OASConstants.ITEM_SET_TYPE_TEST_CATALOG, testCatalogItemSetId,
                    assessmentType.getMediaPath(), false).getItemSetId();

            // create/update the test catalog
            testCatalogId = createOrUpdateTestCatalog(assessment, testCatalogItemSetId, productId,
                    testCatalogId).getTestCatalogId();

            // reestablisb the relationship to the assessment.
            createRelationships(assessment, testCatalogId, testCatalogItemSetId, assessment
                    .getSubTestIds(), productId);

        } catch (HibernateException he) {
            handleHibernateException(he);
        }
    }

    private void deactivateUnusedSubTests(Long testCatalogItemSetId, List subTests)
            throws HibernateException {

        // select all the subtests under the assessment in the database.
        Query query = session.createQuery("from " + ItemSetParentRecord.class.getName()
                + " as itemSetParent "
                + "where itemSetParent.id.parentItemSetId = :parentItemSetId");
        query.setLong("parentItemSetId", testCatalogItemSetId.longValue());

        // if the subtest is not in the new subtest list
        for (Iterator iter = query.iterate(); iter.hasNext();) {
            ItemSetParentRecord itemSetParentRecord = (ItemSetParentRecord) iter.next();

            deactivateUnusedSubTests(subTests, itemSetParentRecord);
        }
    }

    /**
     * Set item set to inactive if the item set not associated with the current TC and not
     * associated with other assessment
     */
    private void deactivateUnusedSubTests(List subTests, ItemSetParentRecord itemSetParentRecord)
            throws HibernateException {

        // if subtest still assocated with the current assessment. Stop deactivation.
        if (subTests.contains(itemSetParentRecord.getId().getItemSetId()))
            return;

        // check if the subtest associated with other active assessments.
        Query countParentQuery = session.createQuery("select count(*) from "
                + ItemSetParentRecord.class.getName()
                + " as itemSetParent, ItemSetRecord as itemSet "
                + "where itemSetParent.id.itemSetId = :itemSetId "
                + "and itemSetParent.id.parentItemSetId=itemSet.itemSetId "
                + "and itemSet.activationStatus=:activationStatus");
        countParentQuery.setLong("itemSetId", itemSetParentRecord.getId().getItemSetId()
                .longValue());
        countParentQuery.setString("activationStatus", OASConstants.ITEM_SET_STATUS_ACTIVE);

        int numOfParents = ((Integer) countParentQuery.uniqueResult()).intValue();

        // if the item set used in only one assessment. Deactivate the subtest
        if (numOfParents == 1) {
            deactiveSubTest(itemSetParentRecord.getId().getItemSetId());
        }

    }

    private void deactiveSubTest(Long itemSetId) throws HibernateException {
        ItemSetRecord itemSetRecord = (ItemSetRecord) session.get(ItemSetRecord.class, itemSetId);

        if (itemSetRecord != null) {
            itemSetRecord.setActivationStatus(OASConstants.ITEM_STATUS_INACTIVE);
            itemSetRecord.setUpdatedBy(new Long(OASConstants.CREATED_BY));
            itemSetRecord.setUpdatedDateTime(new Date());
            session.update(itemSetRecord);
        }
    }

    private TestCatalogRecord getTestCatalogRecord(Long testCatalogItemsetId)
            throws HibernateException {
        Query query = session.createQuery("from " + TestCatalogRecord.class.getName()
                + " as tc where tc.itemSetId = :testCatalogItemSetId");
        query.setLong("testCatalogItemSetId", testCatalogItemsetId.longValue());
        TestCatalogRecord testCatalogRecord = (TestCatalogRecord) query.uniqueResult();
        return testCatalogRecord;
    }

    protected TestCatalogRecord createOrUpdateTestCatalog(AssessmentHolder assessment,
            Long testCatalogItemSetId, Long productId, Long testCatalogId)
            throws HibernateException {
        TestCatalogRecord testCatalog = new TestCatalogRecord();
        testCatalog.setTestName(assessment.getTestName());
        testCatalog.setTestDisplayName(assessment.getItemSetDisplayName());
        String subject = assessment.getProductDisplayName();
        if ( subject == null || subject.length() == 0 )
            testCatalog.setSubject( " ");
        else
            testCatalog.setSubject(assessment.getProductDisplayName()); //???
        testCatalog.setTestGrade(assessment.getGrade());
        testCatalog.setTestForm(assessment.getItemSetForm());
        testCatalog.setCreatedDateTime(new Date());
        testCatalog.setCommodityCode(assessment.getCommodityCode());
        testCatalog.setTestLevel(assessment.getItemSetLevel());
        testCatalog.setVersion(assessment.getVersion());
        testCatalog.setCreatedBy(this.userID);
        testCatalog.setUpdatedDateTime(new Date());
        testCatalog.setActivationStatus(OASConstants.ITEM_SET_STATUS_ACTIVE);

        testCatalog.setProductId(productId);
        testCatalog.setItemSetId(testCatalogItemSetId);
        testCatalog.setTestCatalogId(testCatalogId);
        return (TestCatalogRecord) session.saveOrUpdateCopy(testCatalog);
    }

    protected void connectTC2TDs(Long testCatalogItemsetId, List subTestIds)
            throws HibernateException {
        int order = 1;
        for (Iterator iter = subTestIds.iterator(); iter.hasNext(); order++) {
            Long testDeliveryItemsetId = (Long) iter.next();
            ItemSetParentRecord theRecord = getItemSetParentRecord( testCatalogItemsetId, testDeliveryItemsetId );
            if ( theRecord == null )
                connectTC2TD(testCatalogItemsetId, testDeliveryItemsetId, order);
            else
            {
                theRecord.setItemSetSortOrder( new Long(order) );
                session.update( theRecord );
            }
         }
    }
    
    protected ItemSetParentRecord getItemSetParentRecord(Long testCatalogItemsetId
            											, Long TSItemsetId )
    									throws HibernateException 
    {
		Query query = session
		        .createQuery("from "
		                + ItemSetParentRecord.class.getName()
		                + " as itemSetParent where itemSetParent.id.parentItemSetId = ? and itemSetParent.id.itemSetId = ?");
		query.setLong( 0, testCatalogItemsetId.longValue());
		query.setLong( 1, TSItemsetId.longValue());
		return (ItemSetParentRecord) query.uniqueResult();
    }

    protected void connectTC2TD(Long testCatalogItemsetId, Long testDeliveryItemsetId, int order)
            throws HibernateException {
        ItemSetParentCompositeId id = new ItemSetParentCompositeId();
        id.setItemSetId(testDeliveryItemsetId);
        id.setParentItemSetId(testCatalogItemsetId);

        ItemSetParentRecord itemSetParent = new ItemSetParentRecord();
        itemSetParent.setId(id);
        itemSetParent.setItemSetType(OASConstants.ITEM_SET_TYPE_SCHEDURABLE_UNIT);
        itemSetParent.setParentItemSetType(OASConstants.ITEM_SET_TYPE_TEST_CATALOG);
        itemSetParent.setItemSetSortOrder(new Long(order));
        itemSetParent.setCreatedDateTime(new Date());
        itemSetParent.setUpdatedDateTime(new Date());
        itemSetParent.setCreatedBy(this.userID);
        itemSetParent.setUpdatedBy(this.userID);
        // session.saveOrUpdateCopy(itemSetParent);
         session.save(itemSetParent);
    }

    protected void connectOrgNode2TestCatalog(Long testCatalogId, Long testCatalogItemsetId,
            Long productId, Long orgNodeId, Long customerId) throws HibernateException {

        OrgNodeTestCatalogCompositeId id = new OrgNodeTestCatalogCompositeId();
        id.setItemSetId(testCatalogItemsetId);
        id.setOrgNodeId(orgNodeId);
        id.setTestCatalogId(testCatalogId);

        OrgNodeTestCatalogRecord orgNodeTestCatalog = new OrgNodeTestCatalogRecord();
        orgNodeTestCatalog.setId(id);
        orgNodeTestCatalog.setCreatedDateTime(new Date());
        orgNodeTestCatalog.setCreatedBy(this.userID);
        orgNodeTestCatalog.setUpdatedDateTime(new Date());
        orgNodeTestCatalog.setUpdatedBy(this.userID);
        orgNodeTestCatalog.setCustomerId(customerId);
        orgNodeTestCatalog.setProductId(productId);
        orgNodeTestCatalog.setActivationStatus(OASConstants.ITEM_STATUS_ACTIVE);

        session.save(orgNodeTestCatalog);
    }

    private void clearRelationships(Long productId, Long testCatalogItemsetId, List subTestIds,
            Long testCatalogId, Boolean isAddOn ) throws HibernateException {

        // delete subtest (TD) / assessment (TC) association.
        if ( !isAddOn.booleanValue() )
        {
	        session.delete("from " + ItemSetParentRecord.class.getName()
	                + " as itemSetParent where itemSetParent.id.parentItemSetId = "
	                + testCatalogItemsetId);
        }

        // ITEM_SET_PRODUCT: delete the existing assessment (TC) / product relationship
        ItemSetRelationship.clearProducts(session,testCatalogItemsetId,productId);

        // ORG_NODE_TEST_CATALOG: delete the existing org_node / test_catalog relationships
        for (Iterator iter = OrgNode.getAllOrgNodes(); iter.hasNext();) {
            OrgNode orgNode = (OrgNode) iter.next();
            OrgNodeTestCatalogCompositeId id3 = new OrgNodeTestCatalogCompositeId();
            id3.setItemSetId(testCatalogItemsetId);
            id3.setTestCatalogId(testCatalogId);
            id3.setOrgNodeId((Long) orgNode.getOrgNodeId());
            HibernateUtils.safeDelete(session, OrgNodeTestCatalogRecord.class, id3);
        }
        
        session.flush();
    }

    private void createRelationships(AssessmentHolder assessment, Long testCatalogId,
            Long testCatalogItemsetId, List subTestIds, Long productId) throws HibernateException {

        // Link TD to TC to create the hierarchy
        connectTC2TDs(testCatalogItemsetId, subTestIds);

        // Link Product to TD and TC Item Sets
        connectItemSet2Product(testCatalogItemsetId, productId);

        for (Iterator iter = OrgNode.getAllOrgNodes(); iter.hasNext();) {
            OrgNode orgNode = (OrgNode) iter.next();
            connectOrgNode2TestCatalog(testCatalogId, testCatalogItemsetId, productId, orgNode
                    .getOrgNodeId(), orgNode.getCustomerId());
        }
    }
}