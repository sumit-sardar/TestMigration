package com.ctb.util.persist;

import net.sf.hibernate.Session;
import net.sf.hibernate.Query;
import net.sf.hibernate.HibernateException;
import com.ctb.hibernate.persist.ItemSetProductRecord;
import com.ctb.hibernate.persist.ScoreLookupItemSetRecord;
import com.ctb.common.tools.SystemException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Aug 26, 2004
 * Time: 9:19:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class ItemSetRelationship {
    public static void clearProducts(Session session,Long itemSetId, Long productID) {
        String hql = "select rec from "+ ItemSetProductRecord.class.getName() + " as rec where rec.id.itemSetId = :myId";
        try {
            Query query = session.createQuery(hql);
            query.setParameter("myId",itemSetId);
            List itemSetProducts = query.list();
            for (Iterator iterator = itemSetProducts.iterator();iterator.hasNext();) {
                ItemSetProductRecord rec  =  (ItemSetProductRecord) iterator.next();
                if (rec.getId().getProductId().equals(productID)) {
                    session.delete(rec);
                } else {
                    throw new SystemException("Assessment or SubTest currently " +
                            "associated to another product. ID:" + rec.getId().getProductId());
                }
            }
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(),e);
        }
    }

    public static void clearScoreLookups(Session session, Long itemSetId) {
        String hql = "select rec from ScoreLookupItemSetRecord as rec where rec.id.itemSetId = :myId";
        try {
            Query query = session.createQuery(hql);
            query.setParameter("myId",itemSetId);
            for (Iterator iterator = query.list().iterator();iterator.hasNext();) {
                ScoreLookupItemSetRecord rec  =  (ScoreLookupItemSetRecord) iterator.next();
                session.delete(rec);
            }
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(),e);
        }
    }
}
