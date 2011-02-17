package com.ctb.common.tools;

import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 6, 2003
 * Time: 10:17:12 AM
 * To change this template use Options | File Templates.
 */


public class DBItemInfoGateway extends AbstractDBGateway {

    private static final String ITEM_INFO_BY_FRAMEWORK_CODE = ",item_set_item,item_set_category," +
            "product where item_set_item.item_id = item.item_id " +
            "and item_set.item_set_id = item_set_item.item_set_id and " +
            "item_set.item_set_category_id = item_set_category.item_set_category_id and " +
            "item_set_category.framework_product_id = product.product_id and " +
            "product.internal_display_name = '{0}' and " +
            "product.product_type = 'CF' and " +
            "item_set.item_set_type = 'RE' " +
            "and item.item_id = datapoint.item_id and " +
            "item_set.item_set_id = datapoint.item_set_id";

    public DBItemInfoGateway(Connection conn) {
            super(conn);
    }

    public ItemInfo[] findItemInfoByFrameworkCode(String frameworkCode) {
        Object[] arguments = {frameworkCode};
        DBItemInfoMapper mapper = new DBItemInfoMapper();
        Object[] objects = executeMappingForMultipleResults(ITEM_INFO_BY_FRAMEWORK_CODE, arguments, mapper);

        return (ItemInfo[]) objects;
    }

}

//delete item_set_ancestor where item_set_id
//in(select item_set_id from item_set where item_set_category_id
//in(select item_set_category_id from item_set_category where framework_product_id=2100));
//
//delete item_set_parent where item_set_id
//in (select item_set_id from item_set where item_set_category_id
//in (select item_set_category_id from item_set_category where framework_product_id=2100));
//
//delete item_set_product where product_id=ANY(2100,2101,2102,2111,2112)
//
//delete item_set where item_set_category_id
//in(select item_set_category_id from item_set_category where framework_product_id=2100)
