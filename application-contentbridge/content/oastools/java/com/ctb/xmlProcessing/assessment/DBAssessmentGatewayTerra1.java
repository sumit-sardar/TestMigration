/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.ctb.xmlProcessing.assessment;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.ctb.common.tools.OASConstants;
import com.ctb.common.tools.SystemException;
import com.ctb.hibernate.persist.ItemSetParentCompositeId;
import com.ctb.hibernate.persist.ItemSetParentRecord;
import com.ctb.hibernate.persist.ItemSetRecord;
import com.ctb.hibernate.persist.ItemSetSampleSetRecord;
import com.ctb.hibernate.persist.OrgNodeTestCatalogCompositeId;
import com.ctb.hibernate.persist.OrgNodeTestCatalogRecord;
import com.ctb.hibernate.persist.TestCatalogRecord;
import com.ctb.xmlProcessing.OrgNode;

/**
 * @author wmli TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public class DBAssessmentGatewayTerra1 extends AbstractDBAssessmentGateway {
    public DBAssessmentGatewayTerra1(Session session) {
        super(session);
    }

    public void writeAssessment(AssessmentHolder assessment, AssessmentType assessmentType) {
        Long productId = null;
        if ( assessmentType.getName().equals( "AAS" ))
        {
            productId = getProductID( assessment.getFrameworkCode(), assessment.getProductDisplayName() );
        }
        else
        {
        	productId = getProductID(assessment.getFrameworkCode(), assessment
                .getProductDisplayName(), assessmentType.getProductType());
        }

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
        String subject = assessment.getTestName().substring(assessment.getTestName().indexOf(" ") + 1);
        testCatalog.setSubject(subject);
        testCatalog.setTestGrade(assessment.getGrade());
        testCatalog.setTestForm(assessment.getItemSetForm());
        testCatalog.setCreatedDateTime(new Date());
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

        // order of the TC to TD should be zero
        for (Iterator iter = subTestIds.iterator(); iter.hasNext(); order++) {
            Long testDeliveryItemsetId = (Long) iter.next();
            connectTC2TD(testCatalogItemsetId, testDeliveryItemsetId, 0);
        }
    }

    protected void connectTC2TD(Long testCatalogItemsetId, Long testDeliveryItemsetId, int order)
            throws HibernateException {
        ItemSetParentCompositeId id = new ItemSetParentCompositeId();
        id.setItemSetId(testDeliveryItemsetId);
        id.setParentItemSetId(testCatalogItemsetId);

        ItemSetParentRecord itemSetParent = (ItemSetParentRecord) session.get(
                ItemSetParentRecord.class, id);

        if (itemSetParent != null) {
            return;
        }

        itemSetParent = new ItemSetParentRecord();
        itemSetParent.setId(id);
        itemSetParent.setItemSetType(OASConstants.ITEM_SET_TYPE_TEST_DELIVERY);
        itemSetParent.setParentItemSetType(OASConstants.ITEM_SET_TYPE_TEST_CATALOG);
        itemSetParent.setItemSetSortOrder(new Long(order));
        itemSetParent.setCreatedDateTime(new Date());
        itemSetParent.setUpdatedDateTime(new Date());
        itemSetParent.setCreatedBy(this.userID);
        itemSetParent.setUpdatedBy(this.userID);

        session.save(itemSetParent);
    }

    protected void connectOrgNode2TestCatalog(Long testCatalogId, Long testCatalogItemsetId,
            Long productId, Long orgNodeId, Long customerId) throws HibernateException {

        OrgNodeTestCatalogCompositeId id = new OrgNodeTestCatalogCompositeId();
        id.setItemSetId(testCatalogItemsetId);
        id.setOrgNodeId(orgNodeId);
        id.setTestCatalogId(testCatalogId);

        OrgNodeTestCatalogRecord orgNodeTestCatalog = (OrgNodeTestCatalogRecord) session.get(
                OrgNodeTestCatalogRecord.class, id);

        if (orgNodeTestCatalog != null) {
            return;
        }

        orgNodeTestCatalog = new OrgNodeTestCatalogRecord();
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

        createItemSetSampleSetRelationship(assessment);
    }

    private void createItemSetSampleSetRelationship(AssessmentHolder assessment)
            throws HibernateException {
        for (Iterator iter = assessment.getItemSampleSets().iterator(); iter.hasNext();) {
            ItemSetSampleSetRecord itemSetSample = (ItemSetSampleSetRecord) iter.next();

            ItemSetSampleSetRecord tmp = (ItemSetSampleSetRecord) session.get(
                    ItemSetSampleSetRecord.class, itemSetSample.getItemSetId());

            if (tmp != null) {
                tmp.setSampleItemSetId(itemSetSample.getSampleItemSetId());
                tmp.setTestType(itemSetSample.getTestType());
                tmp.setSubtestLevel(itemSetSample.getSubtestLevel());
                tmp.setSubtestName(itemSetSample.getSubtestName());
                session.update(tmp);
            } else {
                session.save(itemSetSample);
            }
        }
    }
}