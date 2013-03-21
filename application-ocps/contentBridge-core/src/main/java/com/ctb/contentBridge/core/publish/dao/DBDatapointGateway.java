package com.ctb.contentBridge.core.publish.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.hibernate.persist.ConditionCodeRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.DatapointConditionCodeCompositeId;
import com.ctb.contentBridge.core.publish.hibernate.persist.DatapointConditionCodeRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.DatapointRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetCategoryRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetItemRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemSetRecord;
import com.ctb.contentBridge.core.publish.hibernate.persist.ProductRecord;
import com.ctb.contentBridge.core.publish.tools.Datapoint;
import com.ctb.contentBridge.core.publish.tools.OASConstants;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.LongType;
import net.sf.hibernate.type.StringType;
import net.sf.hibernate.type.Type;


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
            throw new SystemException(e.getMessage());
        }
    }
    
    public Datapoint getFrameworkDatapoint(String itemId, String framework_code )
    {
    	Statement statement = null;
        ResultSet rs = null;
        try 
        {
        	System.out.println("Inside getFrameworkDatapoint");
            /*Query query = session.createQuery(FIND_EXISTING_DATAPOINT);
            query.setString(0, itemId);
            query.setString(1, itemId);
            query.setString(2, framework_code);
            System.out.println("before iterate");
            Iterator datapointIT = query.iterate();
            if ( datapointIT.hasNext() )
            {
            	System.out.println("Inside hasNext");
                DatapointRecord datapointRecord = (DatapointRecord) datapointIT.next();
                System.out.println("datapointRecord.getItemId():" + datapointRecord.getItemId());
                System.out.println("datapointRecord.getItemSetId():" + datapointRecord.getItemSetId());
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
            }*/
			statement = session.connection().createStatement();
			StringBuffer sbuf = new StringBuffer("select dat.item_id,dat.item_set_id,dat.min_points,dat.max_points ");
			sbuf.append(" FROM datapoint dat,item_set_item isi,item_set_category isc,product prd,item_set ist ");
			sbuf.append(" where dat.item_id = '").append(itemId).append("'");
			sbuf.append(" and dat.item_set_id = isi.item_set_id");
			sbuf.append(" and isi.item_id = '").append(itemId).append("'");
			sbuf.append(" and isi.item_set_id= ist.item_set_id");
			sbuf.append(" and ist.item_set_type = 'RE'");
			sbuf.append(" and UPPER(prd.internal_display_name) = '").append(framework_code).append("'");
			sbuf.append(" and prd.product_type = 'CF'");
			sbuf.append(" and isc.framework_product_id = prd.product_id");
			sbuf.append(" and ist.item_set_category_id = isc.item_set_category_id");
			
			String sql = sbuf.toString();
			System.out.println("QUERY:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
			System.out.println(sql);

			rs = statement.executeQuery(sql);
			Datapoint datapoint = null;        
			while (rs.next()) {
				datapoint = new Datapoint(rs.getString("item_id"),
						rs.getLong("item_set_id"), rs.getInt("min_points"),
						rs.getInt("max_points"));
			}
			return datapoint;
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
            return null;
        } finally {
			if (statement != null) {

				try {

					statement.close();

				} catch (SQLException e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

				}

			}

			if (rs != null) {

				try {

					rs.close();

				} catch (SQLException e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

				}

			}


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
            throw new SystemException(e.getMessage());
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
        	System.out.println("Inside insertDatapoint1");
        	System.out.println("itemId: " + itemId);
        	System.out.println("itemSetId" + itemSetID);
            session.flush();
            Long datapointId = (Long) session.save(datapoint);
            System.out.println("before insertDatapointConditionCodes");
            insertDatapointConditionCodes(datapointId, conditionCodes);
            System.out.println("after insertDatapointConditionCodes");
        } catch (HibernateException e) {
        	System.out.println("HibernateException; " + e.getMessage());
            throw new SystemException(e.getMessage());
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
    	Statement statement = null;
        ResultSet rs = null;
        try {
        	System.out.println("Inside updateDataPoint1");
        	System.out.println("itemId: " + itemId);
        	System.out.println("itemSetId" + itemSetId);
        	
        	statement = session.connection().createStatement();
			StringBuffer sbuf = new StringBuffer("UPDATE datapoint dat ");
			sbuf.append(" SET dat.min_points = ").append(minPoints);
			sbuf.append(" , dat.max_points = ").append(maxPoints);
			sbuf.append(" , dat.updated_by = ").append(OASConstants.CREATED_BY);
			sbuf.append(" , dat.updated_date_time = SYSDATE ");
			sbuf.append(" WHERE dat.item_id = '").append(itemId).append("'");
			sbuf.append(" AND dat.item_set_id = '").append(itemSetId).append("'");
			
			String sql = sbuf.toString();
			System.out.println("QUERY:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
			System.out.println(sql);

			statement.executeUpdate(sql);
        	
            /*Query query = session.createQuery(FIND_DATAPOINT_FOR_ITEM_ITEMSET);
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
            }*/
        } catch (Exception e) 
        {
        	e.printStackTrace();
        	throw new SystemException(e.getMessage());
        } finally {
			if (statement != null) {

				try {

					statement.close();

				} catch (SQLException e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

				}

			}

        }/* catch (HibernateException e) {
        	System.out.println("HibernateException: " + e.getMessage());
            throw new SystemException(e.getMessage());
        }*/
    }
    
    public void updateDataPoint(
            String itemId,
            long old_itemSetId,
            long itemSetId,
            String[] conditionCodes,
            int minPoints,
            int maxPoints) {

            try {
            	System.out.println("Inside updateDataPoint2");
            	System.out.println("itemId: " + itemId);
            	System.out.println("itemSetId" + itemSetId);
            	System.out.println("old_itemSetId: " + old_itemSetId);
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
            	System.out.println("HibernateException: " + e.getMessage());
                throw new SystemException(e.getMessage());
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
            throw new SystemException(e.getMessage());
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
            throw new SystemException(e.getMessage());
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
            throw new SystemException(e.getMessage());
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
            throw new SystemException(e.getMessage());
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
            throw new SystemException(e.getMessage());
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
            throw new SystemException(e.getMessage());
        }
    }
}
