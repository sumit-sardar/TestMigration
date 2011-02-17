package com.ctb.common.tools;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.LongType;
import net.sf.hibernate.type.StringType;
import net.sf.hibernate.type.Type;

import com.ctb.hibernate.persist.ConditionCodeRecord;
import com.ctb.hibernate.persist.DatapointConditionCodeCompositeId;
import com.ctb.hibernate.persist.DatapointConditionCodeRecord;
import com.ctb.hibernate.persist.DatapointRecord;
import com.ctb.hibernate.persist.ItemSetCategoryRecord;
import com.ctb.hibernate.persist.ItemSetItemRecord;
import com.ctb.hibernate.persist.ItemSetRecord;
import com.ctb.hibernate.persist.ProductRecord;

public class DBDatapointGateway {
    public static final String[] SR_CONDITION_CODES = { "-", "*" };
    public static final String[] CR_CONDITION_CODES = { "A", "B", "C" };

    private Session session;

    private final String FIND_DATAPOINT_FOR_ITEM =
        "from "
            + DatapointRecord.class.getName()
            + " as dataPoint where dataPoint.itemId = ?";

    private final String FIND_DATAPOINT_FOR_ITEM_ITEMSET =
        "from "
            + DatapointRecord.class.getName()
            + " as datapoint where datapoint.itemId = ? and datapoint.itemSetId = ?";

    private final String FIND_DATAPOINT_CONDITION_CODE_FOR_ITEM =
        "select datapointConditionCode from "
            + DatapointConditionCodeRecord.class.getName()
            + " as datapointConditionCode, "
            + DatapointRecord.class.getName()
            + " as datapoint "
            + "where datapointConditionCode.id.datapointId = datapoint.datapointId "
            + "and datapoint.itemId = ? ";

    private final String FIND_DATAPOINT_CONDITION_CODE_FOR_ITEM_ITEMSET =
        "select datapointConditionCode from "
            + DatapointConditionCodeRecord.class.getName()
            + " as datapointConditionCode, "
            + DatapointRecord.class.getName()
            + " as datapoint "
            + "where datapointConditionCode.id.datapointId = datapoint.datapointId "
            + "and datapoint.itemId = ? "
            + "and datapoint.itemSetId = ?";

    private final String FIND_EXISTING_DATAPOINT =
        "select datapoint from "
        	+ DatapointRecord.class.getName()
            + " as datapoint, "
            + ItemSetItemRecord.class.getName()
            + " as itemSetItem, "
            + ItemSetCategoryRecord.class.getName()
            + " as itemSetCategory, "
            + ProductRecord.class.getName()
            + " as product, "
            + ItemSetRecord.class.getName()
            + " as itemSet "
            + "where datapoint.itemId = ? "
            + "and datapoint.itemSetId = itemSetItem.id.itemSetId "
            + "and itemSetItem.id.itemId = ? "
            + "and itemSetItem.id.itemSetId = itemSet.itemSetId "
            + "and itemSet.itemSetType = 'RE' "
    		+ "and UPPER(product.internalDisplayName) = ? "
    		+ "and product.productType = 'CF' "
    		+ "and itemSetCategory.frameworkProductId = product.productId "
    		+ "and itemSet.itemSetCategoryId = itemSetCategory.itemSetCategoryId ";
    
    public DBDatapointGateway(Session session) {
        this.session = session;
    }

    /**
     * WARNING!!  This method will be INVALID if OAS ever supports a single item
     * belonging to more than one objective within a framework.  CMS currently
     * supports this concept.  It is preferable to use getDatapoint(itemId, itemSetId)
     */
    public Datapoint getAnyDatapoint(String itemId) {
        try {
            Query query = session.createQuery(FIND_DATAPOINT_FOR_ITEM);
            query.setString(0, itemId);
            List datapointList = query.list();
            Iterator iterator = datapointList.iterator();
            DatapointRecord datapointRecord =
                (DatapointRecord) iterator.next();

            if (datapointRecord != null) {
                Datapoint datapoint =
                    new Datapoint(
                        datapointRecord.getItemId(),
                        datapointRecord.getItemSetId().longValue(),
                        datapointRecord.getMinPoints().intValue(),
                        datapointRecord.getMaxPoints().intValue());

                session.evict(datapointRecord);

                return datapoint;
            } else {
                throw new SystemException("no datapoint for item_id=" + itemId);
            }

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }
    
    public Datapoint getFrameworkDatapoint(String itemId, String framework_code )
    {
        try 
        {
            Query query = session.createQuery(FIND_EXISTING_DATAPOINT);
            query.setString(0, itemId);
            query.setString(1, itemId);
            query.setString(2, framework_code);
            Iterator datapointIT = query.iterate();
            if ( datapointIT.hasNext() )
            {
                DatapointRecord datapointRecord = (DatapointRecord) datapointIT.next();
                Datapoint datapoint =
                    new Datapoint(
                        datapointRecord.getItemId(),
                        datapointRecord.getItemSetId().longValue(),
                        datapointRecord.getMinPoints().intValue(),
                        datapointRecord.getMaxPoints().intValue());
                return datapoint;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e) 
        {
            return null;
        }
    }

    public Datapoint getDatapoint(String itemId, long itemSetId) {
        try {
            Query query = session.createQuery(FIND_DATAPOINT_FOR_ITEM_ITEMSET);
            query.setString(0, itemId);
            query.setLong(1, itemSetId);

            DatapointRecord datapointRecord =
                (DatapointRecord) query.uniqueResult();

            if (datapointRecord != null) {
                Datapoint datapoint =
                    new Datapoint(
                        datapointRecord.getItemId(),
                        datapointRecord.getItemSetId().longValue(),
                        datapointRecord.getMinPoints().intValue(),
                        datapointRecord.getMaxPoints().intValue());

                session.evict(datapointRecord);

                return datapoint;
            } else {
                throw new SystemException("no datapoint for item_id=" + itemId);
            }

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public boolean datapointExists(String itemId, long itemSetId) 
    {
        boolean result = false;
        try
        {
            result = getDatapoint(itemId, itemSetId) != null;
        }
        catch ( Exception e) {
        }
        return result;
    }

    public void insertDatapoint(
        String itemId,
        long itemSetID,
        String[] conditionCodes,
        int minPoints,
        int maxPoints) {

        DatapointRecord datapoint = new DatapointRecord();
        datapoint.setItemId(itemId);
        datapoint.setItemSetId(new Long(itemSetID));
        datapoint.setMinPoints(new Long(minPoints));
        datapoint.setMaxPoints(new Long(maxPoints));
        datapoint.setCreatedBy(new Long(OASConstants.CREATED_BY));
        datapoint.setCreatedDateTime(new Date());

        try {
            session.flush();
            Long datapointId = (Long) session.save(datapoint);
            insertDatapointConditionCodes(datapointId, conditionCodes);
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }catch (Exception e)
        {
        
        }
    }

    private void insertDatapointConditionCodes(
        Long datapointID,
        String[] conditionCodes)
        throws HibernateException {
        for (int i = 0; i < conditionCodes.length; i++) {
            long conditionCodeId = getConditionCodeID(conditionCodes[i]);
            DatapointConditionCodeCompositeId id =
                new DatapointConditionCodeCompositeId();
            id.setConditionCodeId(new Long(conditionCodeId));
            id.setDatapointId(datapointID);

            DatapointConditionCodeRecord datapointCondition =
                new DatapointConditionCodeRecord();
            datapointCondition.setId(id);

            session.save(datapointCondition);
        }
        session.flush();
    }

    /*
     * because an item cannot change types (e.g. from SR to CR), we do not need to update condition codes
     * Also, items cannot move so item_set_id cannot be updated
     */
    public void updateDataPoint(
        String itemId,
        long itemSetId,
        String[] conditionCodes,
        int minPoints,
        int maxPoints) {

        try {
            Query query = session.createQuery(FIND_DATAPOINT_FOR_ITEM_ITEMSET);
            query.setString(0, itemId);
            query.setLong(1, itemSetId);

            DatapointRecord datapointRecord =
                (DatapointRecord) query.uniqueResult();

            if (datapointRecord != null) {
                datapointRecord.setMinPoints(new Long(minPoints));
                datapointRecord.setMaxPoints(new Long(maxPoints));
                datapointRecord.setUpdatedBy(new Long(OASConstants.CREATED_BY));
                datapointRecord.setUpdatedDateTime(new Date());

                session.update(datapointRecord);
                session.flush();
            }
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }
    
    public void updateDataPoint(
            String itemId,
            long old_itemSetId,
            long itemSetId,
            String[] conditionCodes,
            int minPoints,
            int maxPoints) {

            try {
                Query query = session.createQuery(FIND_DATAPOINT_FOR_ITEM_ITEMSET);
                query.setString(0, itemId);
                query.setLong(1, old_itemSetId);

                DatapointRecord datapointRecord =
                    (DatapointRecord) query.uniqueResult();

                if (datapointRecord != null) {
                    datapointRecord.setItemSetId( new Long(itemSetId) );
                    datapointRecord.setMinPoints(new Long(minPoints));
                    datapointRecord.setMaxPoints(new Long(maxPoints));
                    datapointRecord.setUpdatedBy(new Long(OASConstants.CREATED_BY));
                    datapointRecord.setUpdatedDateTime(new Date());

                    session.update(datapointRecord);
                    session.flush();
                }
            } catch (HibernateException e) {
                throw new SystemException(e.getMessage(), e);
            }

        }

    private long getConditionCodeID(String conditionCode) {
        final String FIND_CONDITIONCODE_BY_CODE =
            "from "
                + ConditionCodeRecord.class.getName()
                + " as conditionCode where conditionCode.conditionCode = :conditionCode";

        try {
            Query query = session.createQuery(FIND_CONDITIONCODE_BY_CODE);
            query.setString("conditionCode", conditionCode);

            ConditionCodeRecord condition =
                (ConditionCodeRecord) query.uniqueResult();

            if (condition != null) {
                session.evict(condition);
                return condition.getConditionCodeId().longValue();
            } else {
                throw new SystemException(
                    "Invalid Condition Code: " + conditionCode);
            }

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public void deleteItemDatapoints(String itemId, long itemSetId) {
        try {

            session.delete(
                FIND_DATAPOINT_CONDITION_CODE_FOR_ITEM_ITEMSET,
                new Object[] { itemId, new Long(itemSetId)},
                new Type[] { new StringType(), new LongType()});

            session.delete(
                FIND_DATAPOINT_FOR_ITEM_ITEMSET,
                new Object[] { itemId, new Long(itemSetId)},
                new Type[] { new StringType(), new LongType()});

            session.flush();
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    /* === The following functions are included for testing purposes ===*/

    public void testDeleteAllItemDatapoints(String itemId) {
        try {

            session.delete(
                FIND_DATAPOINT_CONDITION_CODE_FOR_ITEM,
                itemId,
                new StringType());

            session.delete(FIND_DATAPOINT_FOR_ITEM, itemId, new StringType());

            session.flush();
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public int testGetNumberOfDataPoints(String itemId, long itemSetId) {
        final String COUNT_DATAPOINT =
            "select count(*) "
                + "from "
                + DatapointRecord.class.getName()
                + " as datapoint "
                + "where datapoint.itemId = :itemId "
                + "and datapoint.itemSetId = :itemSetId";

        try {
            Query query = session.createQuery(COUNT_DATAPOINT);
            query.setString("itemId", itemId);
            query.setLong("itemSetId", itemSetId);

            return ((Integer) query.uniqueResult()).intValue();
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public int testGetMaxScore(String itemId, long itemSetId) {
        final String FIND_MAXPOINTS_FOR_DATAPOINT =
            "select datapoint.maxPoints "
                + "from "
                + DatapointRecord.class.getName()
                + " as datapoint "
                + "where datapoint.itemId = :itemId "
                + "and datapoint.itemSetId = :itemSetId";

        try {
            Query query = session.createQuery(FIND_MAXPOINTS_FOR_DATAPOINT);
            query.setString("itemId", itemId);
            query.setLong("itemSetId", itemSetId);

            return ((Long) query.uniqueResult()).intValue();
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }

    }

    public int testGetNumberOfConditionCodes(String itemId, long itemSetId) {
        final String COUNT_CONDITION_CODES =
            "SELECT count(*) from "
                + DatapointConditionCodeRecord.class.getName()
                + " as datapointConditionCode, "
                + DatapointRecord.class.getName()
                + " as datapoint "
                + "where datapointConditionCode.id.datapointId = datapoint.datapointId "
                + "and datapoint.itemId = :itemId "
                + "and datapoint.itemSetId = :itemSetId";

        try {
            Query query = session.createQuery(COUNT_CONDITION_CODES);
            query.setString("itemId", itemId);
            query.setLong("itemSetId", itemSetId);

            return ((Integer) query.uniqueResult()).intValue();
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }
}
