package com.ctb.xmlProcessing;

import java.util.Date;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.ctb.common.tools.DBObjectivesGateway;
import com.ctb.common.tools.OASConstants;
import com.ctb.common.tools.SystemException;
import com.ctb.hibernate.persist.ItemSetProductCompositeId;
import com.ctb.hibernate.persist.ItemSetProductRecord;
import com.ctb.hibernate.persist.ItemSetRecord;

/**
 * @author wmli
 */
public class AbstractItemSetMapper {
    protected static ItemSetRecord getActiveItemSetRecord(
        Session session,
        String extTstsItemSetId,
        String itemSetType) {
        try {
            Query query =
                session.createQuery(
                    "from "
                        + ItemSetRecord.class.getName()
                        + " as itemSet "
                        + "where itemSet.activationStatus = :activationStatus "
                        + "and itemSet.itemSetType = :itemSetType "
                        + "and itemSet.extTstItemSetId = :extTstItemSetId");

            query.setString("itemSetType", itemSetType);
            query.setString("extTstItemSetId", extTstsItemSetId);
            query.setString(
                "activationStatus",
                OASConstants.ITEM_SET_STATUS_ACTIVE);

            return (ItemSetRecord) query.uniqueResult();
        } catch (HibernateException he) {
            throw new SystemException(he.getMessage(), he);
        }

    }

    public static void saveItemSetProductAssociation(
        Session session,
        long tdItemSetId,
        long productId) {
        try {
            session.delete(
                "from "
                    + ItemSetProductRecord.class.getName()
                    + " as itemSetProduct where itemSetProduct.id.itemSetId = "
                    + tdItemSetId);

            ItemSetProductCompositeId id = new ItemSetProductCompositeId();
            id.setItemSetId(new Long(tdItemSetId));
            id.setProductId(new Long(productId));

            ItemSetProductRecord itemSetProductRecord =
                new ItemSetProductRecord();
            itemSetProductRecord.setId(id);
            itemSetProductRecord.setCreatedBy(
                new Long(OASConstants.CREATED_BY));
            itemSetProductRecord.setCreatedDateTime(new Date());

            session.save(itemSetProductRecord);

        } catch (HibernateException he) {
            throw new SystemException(he.getMessage(), he);
        }
    }

    protected static Long getProductID(Session session, String frameworkCode, String productName) {
        int productId =
            new DBObjectivesGateway(session).getProductID(
                frameworkCode,
                productName,
                OASConstants.SCALED_FORMATIVE_PRODUCT_TYPE_CODE);
    
        return new Long(productId);
    }
}
