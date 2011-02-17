package com.ctb.common.tools;

import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 21, 2003
 * Time: 9:57:24 AM
 * To change this template use Options | File Templates.
 */
public class Pruner {
    private Connection connection;

    public static final String INACTIVE_ALL_BY_PRODUCT_ID =
        "update item_set set activation_status = 'IN' "
            + "where activation_status <> 'IN' and "
            + "item_set_type = 'RE' and "
            + "item_set_id not in (select item_set_id from item_set_item where ibs_invisible = 'F' and item_id in (select item_id from item where activation_status = 'AC' "
            + ")) and "
            + "item_set.item_set_category_id in (select item_set.item_set_category_id from item_set, item_set_category "
            + "where item_set_category.item_set_category_id = item_set.item_set_category_id and framework_product_id = {0})";

    public static final String ACTIVATE_OBJECTIVES_BY_PRODUCT_ID =
        "update item_set set activation_status = 'AC' "
            + "where activation_status <> 'AC' "
            + "and item_set_type = 'RE' "
            + "and item_set_id in ("
            + "select item_set_id from item_set_item "
            + "where ibs_invisible = 'F' and item_id in ("
            + "select item_id from item "
            + "where activation_status = 'AC' "
            + ")) "
            + "and item_set.item_set_category_id in ("
            + "select item_set.item_set_category_id from item_set, item_set_category "
            + "where item_set_category.item_set_category_id = item_set.item_set_category_id and framework_product_id = {0})";

    public static final String ACTIVATE_ANCESTORS_BY_PRODUCT_ID =
        "update item_set set activation_status = 'AC' "
            + "where item_set_type = 'RE' "
            + "and activation_status <> 'AC'"
            + "and item_set_id in ("
            + "select ancestor_item_set_id from item_set_ancestor "
            + "where item_set_id in ( "
            + "select item_set_id from item_set "
            + "where item_set_type = 'RE' and activation_status = 'AC')) "
            + "and item_set.item_set_category_id in ("
            + "select item_set.item_set_category_id from item_set, "
            + "item_set_category "
            + "where item_set_category.item_set_category_id = item_set.item_set_category_id and framework_product_id = {0})";

    public Pruner(Connection connection) {
        this.connection = connection;

    }

    public void prune(Product product) {
        Object[] arguments = { new Long(product.getProductID())};
        DBConnection conn = new DBConnection(connection);
        System.out.println(
            "Pruned: "
                + conn.executeUpdate(INACTIVE_ALL_BY_PRODUCT_ID, arguments));
        System.out.println(
            "Activated: "
                + conn.executeUpdate(
                    ACTIVATE_OBJECTIVES_BY_PRODUCT_ID,
                    arguments));
        System.out.println(
            "Ancestors Activated: "
                + conn.executeUpdate(
                    ACTIVATE_ANCESTORS_BY_PRODUCT_ID,
                    arguments));
    }
}
