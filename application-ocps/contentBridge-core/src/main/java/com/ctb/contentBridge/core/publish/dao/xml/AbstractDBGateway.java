package com.ctb.contentBridge.core.publish.dao.xml;

import java.sql.SQLException;
import java.util.Date;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.dao.DBObjectivesGateway;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetProductCompositeId;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetProductRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetRecord;
import com.ctb.contentBridge.core.publish.tools.OASConstants;
import com.ctb.contentBridge.core.publish.xml.ItemSet;
import com.ctb.contentBridge.core.publish.xml.subtest.SubTestHolder;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;


/**
 * @author wmli
 */
public class AbstractDBGateway {
    protected Long userID = new Long(OASConstants.CREATED_BY);
    protected Session session;
    protected DBObjectivesGateway ogw;

    public AbstractDBGateway(Session session) {
        this.session = session;
        this.ogw = new DBObjectivesGateway(session);
    }

    /**
     * @return null if not found, ItemSetRecord if found
     */
    protected ItemSetRecord getItemSetRecord(String extTstsItemSetId, String itemSetType)
            throws HibernateException {
        Query query = session
                .createQuery("from "
                        + ItemSetRecord.class.getName()
                        + " as isr where isr.itemSetType = :itemSetType and isr.extTstItemSetId = :extTstItemSetId and isr.activationStatus = 'AC'");
        query.setString("itemSetType", itemSetType);
        query.setString("extTstItemSetId", extTstsItemSetId);
        return (ItemSetRecord) query.uniqueResult();
    }
    
    protected ItemSetRecord getItemSetRecord( Long ItemSetId ) throws HibernateException
    {
        Query query = session
        .createQuery("from "
                + ItemSetRecord.class.getName()
                + " as isr where isr.itemSetId = :itemSetId");
        query.setLong( "itemSetId", ItemSetId.longValue() );
		return (ItemSetRecord) query.uniqueResult();
    }

    protected ItemSetRecord getItemSetRecord(String extTstsItemSetId, String itemSetType,
            boolean isSample) throws HibernateException {
        String sample = isSample ? "T" : "F";

        Query query = session
                .createQuery("from "
                        + ItemSetRecord.class.getName()
                        + " as isr where isr.itemSetType = :itemSetType and isr.extTstItemSetId = :extTstItemSetId "
                        + "and isr.activationStatus = 'AC' and isr.sample = :sample");
        query.setString("itemSetType", itemSetType);
        query.setString("extTstItemSetId", extTstsItemSetId);
        query.setString("sample", sample);
        return (ItemSetRecord) query.uniqueResult();
    }

    protected Long getProductID(String frameworkCode, String productDisplayName, String productType) {
        int productId = this.ogw.getProductID(frameworkCode, productDisplayName, productType);

        return new Long(productId);
    }
    
    protected Long getProductID(String frameworkCode, String productDisplayName ) {
        int productId = this.ogw.getProductID(frameworkCode, productDisplayName );

        return new Long(productId);
    }

    protected ItemSetRecord createOrUpdateItemSet(ItemSet itemSetHolder, String itemSetType,
            Long itemSetId, String mediaPath, boolean isSample) throws HibernateException {
        // if this is a sample subtest
        // set the the grade = 0
        // set sample to "T"
        // set timeLimit = 0
        ItemSetRecord itemSet = new ItemSetRecord();
        itemSet.setActivationStatus(OASConstants.ITEM_STATUS_ACTIVE);
        itemSet.setBreakTime(new Long(itemSetHolder.getBreakTime()));
        itemSet.setCreatedBy(this.userID);
        itemSet.setCreatedDateTime(new Date());
        itemSet.setExtTstItemSetId(itemSetHolder.getExtTstItemSetId());
        itemSet.setSubject(itemSetHolder.getContentArea());
        itemSet.setGrade(itemSetHolder.getGrade());
        itemSet.setItemSetDescription(itemSetHolder.getItemSetDescription());
        itemSet.setItemSetDisplayName(itemSetHolder.getItemSetDisplayName());
        itemSet.setItemSetForm(itemSetHolder.getItemSetForm());
        itemSet.setItemSetLevel(itemSetHolder.getItemSetLevel());
        itemSet.setItemSetName(itemSetHolder.getTestName());
        itemSet.setMediaPath(mediaPath);
        itemSet.setSample(isSample ? OASConstants.DB_TRUE : OASConstants.DB_FALSE);
        itemSet.setTimeLimit(new Long(itemSetHolder.getTimeLimit()));
        itemSet.setUpdatedDateTime(new Date());
        itemSet.setVersion(itemSetHolder.getVersion());
        itemSet.setItemSetType(itemSetType);
        itemSet.setItemSetId(itemSetId);
        itemSet.setForwardOnly(itemSetHolder.getForwardOnly());
        itemSet.setAdaptive(itemSetHolder.getAdaptive());
        if ( itemSetHolder instanceof SubTestHolder )
        {
            SubTestHolder aSubTestHolder = ( SubTestHolder )itemSetHolder;
            itemSet.setContentSize( aSubTestHolder.content_size );
        }
        return (ItemSetRecord) session.saveOrUpdateCopy(itemSet);
    }

    protected void connectItemSet2Product(Long itemSetId, Long productId) throws HibernateException {

        ItemSetProductCompositeId id = new ItemSetProductCompositeId();
        id.setItemSetId(itemSetId);
        id.setProductId(productId);

        ItemSetProductRecord itemSetProduct = (ItemSetProductRecord) session.get(
                ItemSetProductRecord.class, id);

        if (itemSetProduct != null) {
            return;
        }

        itemSetProduct = new ItemSetProductRecord();
        itemSetProduct.setId(id);
        itemSetProduct.setCreatedDateTime(new Date());
        itemSetProduct.setCreatedBy(this.userID);
        itemSetProduct.setUpdatedDateTime(new Date());

        session.save(itemSetProduct);
    }

    protected void handleHibernateException(HibernateException he) {
        String message = null;

        Throwable[] exceptionList = he.getThrowables();
        for (int i = 0; i < exceptionList.length; i++) {
            if ( (exceptionList[i] != null) && (exceptionList[i] instanceof SQLException)) {
                message = exceptionList[i].getMessage();
            }
        }

        throw new SystemException( (message == null) ? he.getMessage() : message);
    }

}