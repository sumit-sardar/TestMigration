package com.ctb.common.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.StringType;
import net.sf.hibernate.type.Type;


import com.ctb.hibernate.persist.ItemSetAncestorRecord;
import com.ctb.hibernate.persist.ItemSetCategoryRecord;
import com.ctb.hibernate.persist.ItemSetItemCompositeId;
import com.ctb.hibernate.persist.ItemSetItemRecord;
import com.ctb.hibernate.persist.ItemSetParentRecord;
import com.ctb.hibernate.persist.ItemSetRecord;
import com.ctb.hibernate.persist.ProductRecord;

/**
 *
 */
public class DBObjectivesGateway {
    private final String FIND_ITEM_SET_CATEGORY_FOR_EXT_CMS_ITEM_SET_ID =
        "select itemSetCategory from "
            + ItemSetCategoryRecord.class.getName()
            + " as itemSetCategory, "
            + ItemSetRecord.class.getName()
            + " as itemSet "
            + "where itemSet.itemSetCategoryId = itemSetCategory.itemSetCategoryId "
            + "and itemSet.extCmsItemSetId = :extCmsItemSetID";

    private final String DELETE_ITEM_SET_ITEM =
        "select itemSetItem from "
            + ItemSetItemRecord.class.getName()
            + " as itemSetItem, "
            + ItemSetCategoryRecord.class.getName()
            + " as itemSetCategory, "
            + ProductRecord.class.getName()
            + " as product, "
            + ItemSetRecord.class.getName()
            + " as itemSet "
            + "where itemSetItem.id.itemId = ? "
            + "and itemSetItem.id.itemSetId = itemSet.itemSetId "
            + "and itemSet.itemSetType = ? "
    		+ "and UPPER(product.internalDisplayName) = ? "
    		+ "and product.productType = 'CF' "
    		+ "and itemSetCategory.frameworkProductId = product.productId "
    		+ "and itemSet.itemSetCategoryId = itemSetCategory.itemSetCategoryId ";

    private final Type[] DELETE_ITEM_SET_ITEM_ARGU_TYPES =
        { new StringType(), new StringType(), new StringType()};

    private final String FIND_OBJECTIVE_FOR_ITEM =
        "select itemSet from "
            + ItemSetRecord.class.getName()
            + " as itemSet, "
            + ItemSetItemRecord.class.getName()
            + " as itemSetItem "
            + "where itemSetItem.id.itemSetId = itemSet.itemSetId "
            + "and itemSetItem.id.itemId = :itemId "
            + "and itemSet.itemSetType = :itemSetType";

    private final String FIND_OBJECTIVE_FOR_MAPPED_ITEM =
        "select itemSet from "
            + ItemSetRecord.class.getName()
            + " as itemSet, "
            + ItemSetItemRecord.class.getName()
            + " as itemSetItem, "
            + ItemSetCategoryRecord.class.getName()
            + " as itemSetCategory "
            + "where itemSetCategory.frameworkProductId = :frameworkProductId "
            + "and itemSet.itemSetCategoryId = itemSetCategory.itemSetCategoryId "
            + "and itemSet.itemSetType = :itemSetType "
            + "and itemSetItem.id.itemSetId = itemSet.itemSetId "
            + "and itemSetItem.id.itemId = :origItemId";

    private final String FIND_CATEGORY_FOR_PRODUCT =
        "from "
            + ItemSetCategoryRecord.class.getName()
            + " as itemSetCategory "
            + "where itemSetCategory.frameworkProductId = :frameworkProductId";

    private final String FIND_PRODUCT_FOR_INTERNAL_DISPLAY_NAME =
        "from "
            + ProductRecord.class.getName()
            + " as product where UPPER(product.internalDisplayName) = :internalDisplayName";

    private final String FIND_PRODUCT_FOR_INTERNAL_DISPLAY_NAME_PRODUCT_TYPE =
        "from "
            + ProductRecord.class.getName()
            + " as product where UPPER(product.internalDisplayName) = :internalDisplayName and product.productType = :productType";

    //	the 999 trick borrowed from SP_IMPORT_FRAMEWORK
    private final String FIND_ITEM_SET_BY_CMSITEMSETID_AND_FRAMEWORKCODE =
        "select itemSet from "
            + ItemSetRecord.class.getName()
            + " as itemSet, "
            + ItemSetCategoryRecord.class.getName()
            + " as itemSetCategory, "
            + ProductRecord.class.getName()
            + " as product "
            + "WHERE itemSet.itemSetCategoryId = itemSetCategory.itemSetCategoryId "
            + "AND itemSetCategory.frameworkProductId = product.productId "
            + "AND itemSet.itemSetType = :itemSetType "
            + "AND itemSet.itemSetId > 999 "
            + "AND itemSet.extCmsItemSetId = :extCmsItemSetId "
            + "AND upper(product.internalDisplayName) = :internalDisplayName";

    private final String FIND_ANCESTOR_ITEM_SET =
        "select itemSet from "
            + ItemSetRecord.class.getName()
            + " as itemSet, "
            + ItemSetAncestorRecord.class.getName()
            + " as itemSetAncestor "
            + "where itemSet.activationStatus = :activationStatus "
            + "and itemSet.itemSetType = :itemSetType "
            + "and itemSetAncestor.id.itemSetId = :itemSetId "
            + "and itemSet.itemSetId = itemSetAncestor.id.ancestorItemSetId";

    private final String FIND_PARENT_ITEM_SET =
        "select itemSet from "
            + ItemSetRecord.class.getName()
            + " as itemSet, "
            + ItemSetParentRecord.class.getName()
            + " as itemSetParent "
            + "where itemSetParent.id.itemSetId = :itemSetId "
            + "and itemSetParent.id.parentItemSetId = itemSet.itemSetId";

    private final String FIND_CHILDREN_ITEM_SET =
        "select itemSet from "
            + ItemSetRecord.class.getName()
            + " as itemSet, "
            + ItemSetParentRecord.class.getName()
            + " as itemSetParent "
            + "where itemSetParent.id.itemSetId = itemSet.itemSetId "
            + "and itemSetParent.id.parentItemSetId = :itemSetId";

    private Session session;

    public DBObjectivesGateway(Session session) {
        this.session = session;
    }

    public boolean objectiveExistsWithinFramework(
        String cmsObjectiveID,
        String frameworkCode) {
        Long field = getItemSetIdValue(cmsObjectiveID, frameworkCode);

        return (field != null);
    }

    public long getItemSetIdFromObjective(
        String cmsObjectiveID,
        String frameworkCode) {
        Long field =
            getItemSetIdValue(cmsObjectiveID, frameworkCode.toUpperCase());

        if (field != null) {
            return field.longValue();
        } else {
            throw new SystemException(
                "Item Set for Objective '"
                    + cmsObjectiveID
                    + "' in framework '"
                    + frameworkCode
                    + "' not found.");
        }
    }

    public int getItemSetLevel(String extCmsItemSetID) {
        try {
            Query query =
                session.createQuery(
                    FIND_ITEM_SET_CATEGORY_FOR_EXT_CMS_ITEM_SET_ID);
            query.setString("extCmsItemSetID", extCmsItemSetID);

            ItemSetCategoryRecord itemSetCategoryRecord =
                (ItemSetCategoryRecord) query.uniqueResult();

            if (itemSetCategoryRecord != null) {
                int itemSetLevel =
                    itemSetCategoryRecord.getItemSetCategoryLevel().intValue();

                session.evict(itemSetCategoryRecord);
                return itemSetLevel;
            } else {
                throw new SystemException(
                    "Item Set for ID " + extCmsItemSetID + " not found.");
            }

        } catch (HibernateException e) {
            throw new SystemException(
                "Hibernate Error Retrieving Item Set Level",
                e);
        }
    }

    /* Warning: This low-level method allows items to be moved from one objectives
     * to another. The current business requirement is not to allow such a move, so
     * the ItemValidator explicitly prevents such a move.
     */
    public void linkItemToObjective(String itemId, long itemSetId
            				, String frameworkCode, boolean setVisible ) {
        final Object[] arguments =
            { itemId, OASConstants.ITEM_SET_TYPE_REPORTING, frameworkCode };

        try {
            // delete current assoication of items to item set.
            session.delete(
                DELETE_ITEM_SET_ITEM,
                arguments,
                DELETE_ITEM_SET_ITEM_ARGU_TYPES);

            // now link the item to the desired item set
            ItemSetItemCompositeId itemSetItemId = new ItemSetItemCompositeId();
            itemSetItemId.setItemId(itemId);
            itemSetItemId.setItemSetId(new Long(itemSetId));

            ItemSetItemRecord itemSetItem = new ItemSetItemRecord();
            itemSetItem.setId(itemSetItemId);
            itemSetItem.setItemSortOrder(new Long(0));
            itemSetItem.setCreatedBy(new Long(OASConstants.CREATED_BY));
            itemSetItem.setCreatedDateTime(new Date());
            itemSetItem.setFieldTest( "F" );
            itemSetItem.setSuppressed( "F" );
            itemSetItem.setIbsInvisible( setVisible ? "F" : "T" );
            session.save(itemSetItem);

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public void linkItemToObjectiveByObjectiveID(
        String ItemID,
        String objectiveID,
        String frameworkCode,
        boolean setVisible) {
        long itemSetID = getItemSetIdFromObjective(objectiveID, frameworkCode);

        linkItemToObjective(ItemID, itemSetID, frameworkCode, setVisible);
    }

    /**
     * This method does not check whether the item set is active or not
     */
    public String getCurrentObjectiveIDForItem(String itemId) {
        try {
            Query query = session.createQuery(FIND_OBJECTIVE_FOR_ITEM);
            query.setString("itemId", itemId);
            query.setString(
                "itemSetType",
                OASConstants.ITEM_SET_TYPE_REPORTING);

            ItemSetRecord itemSet = (ItemSetRecord) query.uniqueResult();

            if (itemSet != null)
                return itemSet.getExtCmsItemSetId();

            return null;
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    /**
     * Finds the Objective ID for a given Item in a specified framework. The Objective ID
     * is retrieved only if the Item is Active.
     * 
     * @return the objective ID for an Active Item in a Framework, null otherwise
     * @param  frameworkCode code corresponding to given Framework
     * @param originalItemId may be the Item ID (if the Item was imported into that Framework) 
     * or Original Item ID (if the Item was mapped into that Framework)
     */

    public String getCurrentObjectiveIDForItem(
        String originalItemId,
        String frameworkCode) {

        try {
            Query query = session.createQuery(FIND_OBJECTIVE_FOR_MAPPED_ITEM);
            query.setString("origItemId", originalItemId);
            query.setLong("frameworkProductId", getFrameWorkID(frameworkCode));
            query.setString(
                "itemSetType",
                OASConstants.ITEM_SET_TYPE_REPORTING);

            ItemSetRecord itemSet = (ItemSetRecord) query.uniqueResult();

            if (itemSet != null)
                return itemSet.getExtCmsItemSetId();

            return null;
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public int getFrameWorkID(String frameworkCode) {
        return getProductID(frameworkCode, "");
    }

    public Set getCategoryLevels(String frameworkCode) {
        try {
            Query query = session.createQuery(FIND_CATEGORY_FOR_PRODUCT);
            query.setLong(
                "frameworkProductId",
                new Long(getFrameWorkID(frameworkCode)).longValue());

            Set categoryLevels = new HashSet();

            for (Iterator iter = query.iterate(); iter.hasNext();) {
                ItemSetCategoryRecord itemSetCategoryRecord =
                    (ItemSetCategoryRecord) iter.next();

                int level =
                    itemSetCategoryRecord.getItemSetCategoryLevel().intValue();

                if (!categoryLevels.add(new Integer(level))) {
                    throw new SystemException(
                        "framework "
                            + frameworkCode
                            + " contains multiple categories at level "
                            + level);
                }
            }

            return categoryLevels;

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public int getProductID(String frameworkCode, String productDisplayName) {
        try {
            String fullDisplayName = frameworkCode;

            if (productDisplayName.length() > 0) {
                fullDisplayName += " " + productDisplayName;
            }

            fullDisplayName = fullDisplayName.toUpperCase();

            Query query =
                session.createQuery(FIND_PRODUCT_FOR_INTERNAL_DISPLAY_NAME);
            query.setString("internalDisplayName", fullDisplayName);

            ProductRecord product = (ProductRecord) query.uniqueResult();

            if (product != null) {
                int productId = product.getProductId().intValue();
                session.evict(product);
                return productId;
            } else {
                throw new SystemException(
                    "Product "
                        + fullDisplayName
                        + " not found in framework "
                        + frameworkCode);
            }

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public int getProductID(
        String frameworkCode,
        String productDisplayName,
        String productType) {
        try {
            String fullDisplayName = frameworkCode;

            if (productDisplayName.length() > 0) {
                fullDisplayName += " " + productDisplayName;
            }

            fullDisplayName = fullDisplayName.toUpperCase();

            Query query =
                session.createQuery(
                    FIND_PRODUCT_FOR_INTERNAL_DISPLAY_NAME_PRODUCT_TYPE);
            query.setString("internalDisplayName", fullDisplayName);
            query.setString("productType", productType);

            ProductRecord product = (ProductRecord) query.uniqueResult();

            if (product != null)
                return product.getProductId().intValue();
            else
                throw new SystemException(
                    "Product "
                        + fullDisplayName
                        + " not found in framework "
                        + frameworkCode + " for product type " + productType);

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    /*
     *  This function finds active and inactive items sets because the importer can attach
     *  items to inactive objectives
     */
    private Long getItemSetIdValue(
        String extCmsItemSetId,
        String frameworkCode) {
        ItemSetRecord itemSet =
            getItemSetByExtCMSItemSetId(extCmsItemSetId, frameworkCode);

        if (itemSet != null) {
            Long itemSetId = itemSet.getItemSetId();
            try {
                session.evict(itemSet);
            } catch (HibernateException e) {
                throw new SystemException(e.getMessage(), e);
            }
            return itemSetId;
        }

        return null;

    }

    public String getItemSetName(
        String extCmsItemSetId,
        String frameworkCode) {
        ItemSetRecord itemSet =
            getItemSetByExtCMSItemSetId(extCmsItemSetId, frameworkCode);

        if (itemSet != null) {
            String itemSetName = itemSet.getItemSetName();
            try {
                session.evict(itemSet);
            } catch (HibernateException e) {
                throw new SystemException(e.getMessage(), e);
            }
            return itemSetName;
        }
        return null;
    }

    public String getItemSetDisplayName(
        String extCmsItemSetId,
        String frameworkCode) {
        ItemSetRecord itemSet =
            getItemSetByExtCMSItemSetId(extCmsItemSetId, frameworkCode);

        if (itemSet != null) {
            String itemSetName = itemSet.getItemSetDisplayName();
            try {
                session.evict(itemSet);
            } catch (HibernateException e) {
                throw new SystemException(e.getMessage(), e);
            }
            return itemSet.getItemSetDisplayName();
        }

        return null;
    }

    private ItemSetRecord getItemSetByExtCMSItemSetId(
        String extCmsItemSetId,
        String frameworkCode) {
        try {
            Query query =
                session.createQuery(
                    FIND_ITEM_SET_BY_CMSITEMSETID_AND_FRAMEWORKCODE);
            query.setString(
                "itemSetType",
                OASConstants.ITEM_SET_TYPE_REPORTING);
            query.setString("extCmsItemSetId", extCmsItemSetId);
            query.setString("internalDisplayName", frameworkCode.toUpperCase());

            return (ItemSetRecord) query.uniqueResult();

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public int activateAllParentObjectives(long itemSetId) {
        // select all ancestorItemSet and set the activation status to active.
        try {
            Query query = session.createQuery(FIND_ANCESTOR_ITEM_SET);
            query.setString(
                "activationStatus",
                OASConstants.ITEM_SET_STATUS_INACTIVE);
            query.setString(
                "itemSetType",
                OASConstants.ITEM_SET_TYPE_REPORTING);
            query.setLong("itemSetId", itemSetId);

            int ancestorCount = 0;
            for (Iterator iter = query.iterate(); iter.hasNext();) {
                ancestorCount++;
                ItemSetRecord itemSet = (ItemSetRecord) iter.next();
                itemSet.setActivationStatus(
                    OASConstants.ITEM_SET_STATUS_ACTIVE);
                session.save(itemSet);
            }

            return ancestorCount;

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public void updateItemSetName(
        String extCmsItemSetId,
        String frameworkCode,
        String itemSetName) {

        ItemSetRecord itemSet =
            getItemSetByExtCMSItemSetId(extCmsItemSetId, frameworkCode);

        if (itemSet != null) {
            itemSet.setItemSetName(itemSetName);
            itemSet.setUpdatedBy(new Long(OASConstants.CREATED_BY));
            itemSet.setUpdatedDateTime(new Date());

            try {
                session.update(itemSet);
            } catch (HibernateException e) {
                throw new SystemException(e.getMessage(), e);
            }

        } else {
            throw new SystemException(
                "Cannot update item set name for item with EXT_CMS_ITEM_SET_ID: ["
                    + extCmsItemSetId
                    + "]");

        }

    }

    public void updateItemSetDisplayName(
        String extCmsItemSetId,
        String frameworkCode,
        String itemSetDisplayName) {
        ItemSetRecord itemSet =
            getItemSetByExtCMSItemSetId(extCmsItemSetId, frameworkCode);

        if (itemSet != null) {
            itemSet.setItemSetDisplayName(itemSetDisplayName);
            itemSet.setUpdatedBy(new Long(OASConstants.CREATED_BY));
            itemSet.setUpdatedDateTime(new Date());

            try {
                session.update(itemSet);
            } catch (HibernateException e) {
                throw new SystemException(e.getMessage(), e);
            }
        } else {
            throw new SystemException(
                "Cannot update item set display name for item with EXT_CMS_ITEM_SET_ID: ["
                    + extCmsItemSetId
                    + "]");

        }

    }

    public String getParentExtCmsItemSetId(
        String childExtCmsItemSetId,
        String frameworkCode) {
        ItemSetRecord itemSet =
            getItemSetByExtCMSItemSetId(childExtCmsItemSetId, frameworkCode);

        if (itemSet != null) {
            try {
                Query query = session.createQuery(FIND_PARENT_ITEM_SET);
                query.setLong("itemSetId", itemSet.getItemSetId().longValue());
                ItemSetRecord parrentItemSet =
                    (ItemSetRecord) query.uniqueResult();

                if (parrentItemSet != null) {
                    String parentExtCmsItemSetId =
                        parrentItemSet.getExtCmsItemSetId();
                    session.evict(parrentItemSet);
                    return parentExtCmsItemSetId;
                }

            } catch (HibernateException e) {
                throw new SystemException(e.getMessage(), e);
            }
        }

        return null;
    }

    public List getChildrenExtCmsItemSetIds(
        String parentExtCmsItemSetId,
        String frameworkCode) 
    {
        List result = new ArrayList();
        ItemSetRecord itemSet =
            getItemSetByExtCMSItemSetId(parentExtCmsItemSetId, frameworkCode);

        if (itemSet != null) {
            try {
                Query query = session.createQuery(FIND_CHILDREN_ITEM_SET);
                query.setLong("itemSetId", itemSet.getItemSetId().longValue());
                for (Iterator iter = query.iterate(); iter.hasNext();) {
                    ItemSetRecord childItemSet = (ItemSetRecord) iter.next();
                    result.add(childItemSet.getExtCmsItemSetId());
                    session.evict(childItemSet);
                }
            } catch (HibernateException e) {
                throw new SystemException(e.getMessage(), e);
            }
        }

        return result;
    }

    /* === The following functions are included for testing purposes only ===*/
    public String testGetItemSetActivationStatus(long itemSetId) {
        try {
            ItemSetRecord itemSet =
                (ItemSetRecord) session.get(
                    ItemSetRecord.class,
                    new Long(itemSetId));

            if (itemSet != null)
                return itemSet.getActivationStatus();
            else
                return null;

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public void testSetItemSetActivationStatus(
        long itemSetId,
        String activationStatus) {
        try {
            ItemSetRecord itemSet =
                (ItemSetRecord) session.get(
                    ItemSetRecord.class,
                    new Long(itemSetId));

            if (itemSet != null) {
                itemSet.setActivationStatus(activationStatus);
                session.update(itemSet);
            }

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }
}
