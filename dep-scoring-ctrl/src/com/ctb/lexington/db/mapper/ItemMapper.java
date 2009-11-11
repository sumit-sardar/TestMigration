package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.data.ItemVO;
import com.ctb.lexington.data.ItemContentArea;
import com.ctb.lexington.db.utils.DatabaseHelper;

public class ItemMapper extends AbstractDBMapper {
    private static final String FIND_NAME = "findItem";
    private static final String FIND_ITEM_BY_ITEM_SET_ID_NAME = "findItemByItemSetId";
    private static final String FIND_CONDITION_CODE_ID_ITEM_IDS_BY_ITEM_SET_ID_NAME = "findConditionCodeIdItemIdsForItemSet";
    private static final String FIND_ITEM_GROUP_BY_CONTENT_AREA_AND_PRODUCT = "findItemGroupByContentAreaForItemSetAndProduct";

    /**
     * @param conn
     */
    public ItemMapper(Connection conn) {
        super(conn);
    }

    public ItemVO find(String itemId) {
        ItemVO template = new ItemVO();
        template.setItemId(itemId);
        return (ItemVO) find(FIND_NAME, template);
    }

    public List findItemByItemSetId(Long itemSetId) {
        ItemVO template = new ItemVO();
        template.setItemSetId(itemSetId);

        List result = findMany(FIND_ITEM_BY_ITEM_SET_ID_NAME, template);
        addConditionCodeIDs(result, itemSetId);
        return result;
    }

    public List findItemGroupByContentAreaForItemSetAndProduct(Long itemSetId, Long productId) {
        ItemContentArea template = new ItemContentArea();
        template.setItemSetId(itemSetId);
        template.setProductId(productId);

        return findMany(FIND_ITEM_GROUP_BY_CONTENT_AREA_AND_PRODUCT,template);
    }

    /**
     * @param items
     * @param itemSetId
     */
    private void addConditionCodeIDs(Collection items, Long itemSetId) {
        ConditionCodeIdItemId template2 = new ConditionCodeIdItemId();
        template2.setItemSetId(DatabaseHelper.asInteger(itemSetId));
        Collection conditionCodeIDs = findMany(FIND_CONDITION_CODE_ID_ITEM_IDS_BY_ITEM_SET_ID_NAME,
                template2);

        Map index = indexItems(items);
        for (Iterator iter = conditionCodeIDs.iterator(); iter.hasNext();) {
            ConditionCodeIdItemId cciID = (ConditionCodeIdItemId) iter.next();

            ItemVO item = (ItemVO) index.get(cciID.getItemId());
            item.addConditionCodeId(cciID.getConditionCodeId());
        }
    }

    /**
     * @param items
     * @return
     */
    private Map indexItems(Collection items) {
        Map result = new HashMap();
        for (Iterator iter = items.iterator(); iter.hasNext();) {
            ItemVO item = (ItemVO) iter.next();

            result.put(item.getItemId(), item);
        }

        return result;
    }
}