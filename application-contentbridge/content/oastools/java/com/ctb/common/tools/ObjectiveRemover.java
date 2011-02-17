package com.ctb.common.tools;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 7, 2003
 * Time: 9:20:28 AM
 * todo - mws - refactor this to use a set of DBgateways and objects for tables
 */
public class ObjectiveRemover extends AbstractDBGateway {
    private static final String DELETE_PARENTS = "delete item_set_parent where item_set_id" +
                        " in (select item_set_id from item_set where item_set_category_id" +
                        " in (select item_set_category_id from item_set_category where framework_product_id = {0}))";

    private Connection conn;

    public ObjectiveRemover(Connection conn) {
        super(conn);
    }
    public void removeItemSetsForProduct(Product product) {


        //first delete against parents
        deleteItemSetParentsForProduct(product);
        //delete ancestors
        //delete item set product associations
        //delete item sets
    }

    private Object[] getArgumentsFromProduct(Product product) {
        Object[] arguments = {new Long(product.getProductID())};
        return arguments;
    }

    public void deleteItemSetParentsForProduct(Product product) {
        Object[] arguments = getArgumentsFromProduct(product);
        String sql = DELETE_PARENTS;
        try {
            executeDelete(sql,arguments);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        }
    }
}
