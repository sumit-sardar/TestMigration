package com.ctb.contentBridge.core.publish.dao;

import java.sql.Connection;

import com.ctb.contentBridge.core.publish.dao.tools.AbstractDBGateway;
import com.ctb.contentBridge.core.publish.tools.ObjectiveInfo;
import com.ctb.contentBridge.core.publish.tools.Product;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 6, 2003
 * Time: 10:17:12 AM
 * To change this template use Options | File Templates.
 */


public class DBObjectiveInfoGateway extends AbstractDBGateway {


    private static final String ALL_BY_PRODUCT = " where framework_product_id = {0} and " +
            "item_set_category.item_set_category_id = item_set.item_set_category_id and " +
            "item_set.item_set_type = 'RE'";

    private static final String ACTIVE_BY_PRODUCT = ALL_BY_PRODUCT +
            " and item_set.activation_status = 'AC'";

    public DBObjectiveInfoGateway(Connection conn) {
            super(conn);
    }

    public ObjectiveInfo[] findActiveObjectiveInfosByProduct(Product product) {
        Object[] arguments = {new Long(product.getProductID())};
        DBObjectiveInfoMapper mapper = new DBObjectiveInfoMapper();
        Object[] objects = executeMappingForMultipleResults(ACTIVE_BY_PRODUCT, arguments, mapper);
        return (ObjectiveInfo[]) objects;
    }

    public ObjectiveInfo[] findObjectiveInfosByProduct(Product product) {
        Object[] arguments = {new Long(product.getProductID())};
        DBObjectiveInfoMapper mapper = new DBObjectiveInfoMapper();
        Object[] objects = executeMappingForMultipleResults(ALL_BY_PRODUCT, arguments, mapper);
        return (ObjectiveInfo[]) objects;
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
