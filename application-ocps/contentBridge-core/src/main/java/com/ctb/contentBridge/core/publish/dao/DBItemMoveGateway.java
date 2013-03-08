package com.ctb.contentBridge.core.publish.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.hibernate.persist.DatapointRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetItemCompositeId;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetItemRecord;
import com.ctb.contentBridge.core.publish.tools.OASConstants;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.LongType;
import net.sf.hibernate.type.StringType;
import net.sf.hibernate.type.Type;


/**
 * contains SQL used to move items.  since item moves should
 * only happen in exceptional circumstances these methods are
 * not placed in DBItemGateway and DBDatapointGateway.
 */
public class DBItemMoveGateway {
    Session session;
    public DBItemMoveGateway(Session session) {
        this.session = session;
    }

    /**
     * when moving an item between objectives update the datapoint
     * to link to the new objective
     * @return number of datapoint rows updated, only possible values are 0 and 1
     * @exception SystemException thrown if more than one datapopint row updated
     */
    public int updateDatapointIfExists(
        String itemId,
        long sourceItemSetId,
        long targetItemSetId,
        String frameworkCode) {
        int rows =
            executeDatapointUpdate(sourceItemSetId, targetItemSetId, itemId);

        if (rows > 1) {
            throw new SystemException(
                "datapoint update failed for item "
                    + itemId
                    + ".  row count was unexpectedly "
                    + rows);
        }
        return rows;
    }

    /**
     * when moving an item between objectives update the datapoint
     * to link to the new objective
     */
    public void updateDatapoint(
        String itemId,
        long sourceItemSetId,
        long targetItemSetId,
        String frameworkCode) {
        int rows =
            executeDatapointUpdate(sourceItemSetId, targetItemSetId, itemId);

        if (rows != 1) {
            throw new SystemException(
                "datapoint update failed for item "
                    + itemId
                    + ".  row count was unexpectedly "
                    + rows);
        }
    }

    int executeDatapointUpdate(
        long sourceItemSetId,
        long targetItemSetId,
        String itemId) {
        try {
            List datapointRecords =
                session.find(
                    "from "
                        + DatapointRecord.class.getName()
                        + " as datapoint where datapoint.itemId = ? and datapoint.itemSetId = ?",
                    new Object[] { itemId, new Long(sourceItemSetId)},
                    new Type[] { new StringType(), new LongType()});
                    
            
			
            for (Iterator iter = datapointRecords.iterator();
                iter.hasNext();
                ) {
                DatapointRecord datapointRecord = (DatapointRecord) iter.next();
                datapointRecord.setItemSetId(new Long(targetItemSetId));
                datapointRecord.setUpdatedBy(new Long(OASConstants.CREATED_BY));
                datapointRecord.setUpdatedDateTime(new Date());

                session.update(datapointRecord);
            }
            return (datapointRecords == null) ? 0 : datapointRecords.size();

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage());
        }
    }

    /**
     * when moving an item between objectives update the item_set_item
     * entry to link to the new objective
     * @exception SystemException thrown if one and only one item_set_item row now updated
     */
    public void updateItemSetItem(
        String itemId,
        long sourceItemSetId,
        long targetItemSetId,
        String frameworkCode) {
        try {
            ItemSetItemCompositeId id = new ItemSetItemCompositeId();
            id.setItemId(itemId);
            id.setItemSetId(new Long(sourceItemSetId));
            ItemSetItemRecord itemSetItem =
                (ItemSetItemRecord) session.load(ItemSetItemRecord.class, id);
            session.delete(itemSetItem);
            session.flush();

			ItemSetItemCompositeId id2 = itemSetItem.getId();
			id2.setItemSetId(new Long(targetItemSetId));
			itemSetItem.setId(id2);
            itemSetItem.setUpdatedBy(new Long(OASConstants.CREATED_BY));
            itemSetItem.setUpdatedDateTime(new Date());
            session.save(itemSetItem);
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage());
        }
    }
}
