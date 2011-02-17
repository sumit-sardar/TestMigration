package com.ctb.common.tools;

import com.ctb.util.ListUtils;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

/**
 * User: mwshort
 * Date: Dec 9, 2003
 * Time: 3:49:16 PM
 * 
 *
 */
public class FrameworkRemover {
//todo - mws - handle these tables
//---GGENNARO.ORG_NODE_TEST_CATALOG (Product_Id)  SYS_C00546945
//---GGENNARO.PRODUCT_TUTORIAL (Product_Id)  SYS_C00546978
//GGENNARO.TEST_CATALOG (Product_Id)  SYS_C00546993

    private String DELETE_ITEM_SET_PARENT = "delete item_set_parent where item_set_id" +
                    " in (select item_set_id from item_set where item_set_category_id" +
                    " in (select item_set_category_id from item_set_category where framework_product_id={0}))";
    private String DELETE_ITEM_SET_ANCESTOR = "delete item_set_ancestor where item_set_id" +
                                " in (select item_set_id from item_set where item_set_category_id" +
                                " in (select item_set_category_id from item_set_category where framework_product_id={0}))";
    private String DELETE_ITEM_SET_PRODUCT = "delete item_set_product where product_id ";
    private String DELETE_ITEM_SET = "delete item_set where item_set_category_id" +
            " in (select item_set_category_id from item_set_category where framework_product_id={0})";
    private String DELETE_ITEM_SET_CATEGORY = "delete item_set_category where item_set_category.framework_product_id = {0}";
    private String DELETE_PRODUCT = "delete product where parent_product_id = {0} or product_id = {0}";
    private String DELETE_SUBSCRIPTION = "delete subscription where product_id ";
    private String DELETE_TEST_ROSTER= "delete test_roster where test_admin_id in (select test_admin_id from test_admin where product_id ";
    private String DELETE_TEST_ADMIN= "delete test_admin where product_id ";
    private String DELETE_DATAPOINTS = "delete datapoint where item_set_id in " +
            "(select item_set_id from item_set where item_set_category_id in " +
            "(select item_set_category_id from item_set_category where framework_product_id={0}))";
    private String DELETE_ITEM_SET_ITEM = "delete item_set_item where item_set_id in (select item_set_id from " +
            "item_set where item_set_category_id in (select item_set_category_id " +
            "from item_set_category where framework_product_id={0}))";
    private String DELETE_DATAPOINT_CONDITION_CODE = "delete datapoint_condition_code where datapoint_id in " +
            "(select datapoint_id from datapoint where item_set_id in " +
            "(select item_set_id from item_set where item_set_category_id in " +
            "(select item_set_category_id from item_set_category where framework_product_id={0})))";
    private Connection connection;

    public FrameworkRemover(Connection connection) {
        this.connection = connection;
    }

    public int removeFramework(String frameworkCode) {
        DBProductGateway gateway = new DBProductGateway(connection);
        Product rootProduct = gateway.findRootProductByFrameworkCode(frameworkCode);
        return removeFramework(rootProduct);
    }
    public int removeFramework(Product rootProduct) {

        int i = 0;
        DBProductGateway gateway = new DBProductGateway(connection);
        List subproducts = Arrays.asList(gateway.findSubProductsByRootProduct(rootProduct));
        i += deleteItemSetParent(rootProduct);
        i += deleteItemSetAncestor(rootProduct);
        i += deleteItemSetProduct(rootProduct,subproducts);
        i += deleteDatapointConditionCode(rootProduct);
        i += deleteDatapoints(rootProduct);
        i += deleteItemSetItem(rootProduct);
        i += deleteItemSet(rootProduct);
        i += deleteItemSetCategory(rootProduct);
        i += deleteSubscription(rootProduct,subproducts);
        i += deleteTestRoster(rootProduct,subproducts);
        i += deleteTestAdmin(rootProduct,subproducts);
        i += deleteProduct(rootProduct);
        return i;
    }

    private int deleteTestRoster(Product rootProduct, List subproducts) {
        List productIds = new ArrayList();
        productIds.add(new Long(rootProduct.getProductID()));

        for (Iterator iter = subproducts.iterator();iter.hasNext();) {
            SubProduct product = (SubProduct)iter.next();
            productIds.add(new Long(product.getProductID()));
        }


        String inClause = ListUtils.arrayToInClause((Long[])productIds.toArray(new Long[productIds.size()]));

        String SQL = DELETE_TEST_ROSTER + inClause + ")";
        System.out.println(SQL);
        Statement stmt = null;
        int i = 0;
        try {
            stmt = connection.createStatement();
            i = stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        } finally {
            DBConnection.safeClose(stmt,null);
        }
        return i;
    }
    private int deleteTestAdmin(Product rootProduct, List subproducts) {
        List productIds = new ArrayList();
        productIds.add(new Long(rootProduct.getProductID()));

        for (Iterator iter = subproducts.iterator();iter.hasNext();) {
            SubProduct product = (SubProduct)iter.next();
            productIds.add(new Long(product.getProductID()));
        }


        String inClause = ListUtils.arrayToInClause((Long[])productIds.toArray(new Long[productIds.size()]));

        String SQL = DELETE_TEST_ADMIN + inClause;
        System.out.println(SQL);
        Statement stmt = null;
        int i = 0;
        try {
            stmt = connection.createStatement();
            i = stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        } finally {
            DBConnection.safeClose(stmt,null);
        }
        return i;
    }

    public int deleteItemSetParent(Product rootProduct) {

        Object[] arguments = {new Long(rootProduct.getProductID())};
        String SQL = DBConnection.formatSql(DELETE_ITEM_SET_PARENT,arguments);
        Statement stmt = null;
        int i = 0;
        try {
            stmt = connection.createStatement();
            i = stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        } finally {
            DBConnection.safeClose(stmt,null);
        }
        return i;
    }

    public int deleteItemSetAncestor(Product rootProduct) {
        Object[] arguments = {new Long(rootProduct.getProductID())};
        String SQL = DBConnection.formatSql(DELETE_ITEM_SET_ANCESTOR,arguments);
        Statement stmt = null;
        int i = 0;
        try {
            stmt = connection.createStatement();
            i = stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        } finally {
            DBConnection.safeClose(stmt,null);
        }
        return i;

    }


    public int deleteItemSetProduct(Product rootProduct, List products) {
        List productIds = new ArrayList();
        productIds.add(new Long(rootProduct.getProductID()));

        for (Iterator iter = products.iterator();iter.hasNext();) {
            SubProduct product = (SubProduct)iter.next();
            productIds.add(new Long(product.getProductID()));
        }


        String inClause = ListUtils.arrayToInClause((Long[])productIds.toArray(new Long[productIds.size()]));

        String SQL = DELETE_ITEM_SET_PRODUCT + inClause;
        System.out.println(SQL);
        Statement stmt = null;
        int i = 0;
        try {
            stmt = connection.createStatement();
            i = stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        } finally {
            DBConnection.safeClose(stmt,null);
        }
        return i;

    }
    public int deleteSubscription(Product rootProduct, List products) {
        List productIds = new ArrayList();
        productIds.add(new Long(rootProduct.getProductID()));

        for (Iterator iter = products.iterator();iter.hasNext();) {
            SubProduct product = (SubProduct)iter.next();
            productIds.add(new Long(product.getProductID()));
        }


        String inClause = ListUtils.arrayToInClause((Long[])productIds.toArray(new Long[productIds.size()]));

        String SQL = DELETE_SUBSCRIPTION + inClause;
        System.out.println(SQL);
        Statement stmt = null;
        int i = 0;
        try {
            stmt = connection.createStatement();
            i = stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        } finally {
            DBConnection.safeClose(stmt,null);
        }
        return i;

    }

    public int deleteItemSet(Product rootProduct) {
        Object[] arguments = {new Long(rootProduct.getProductID())};
        String SQL = DBConnection.formatSql(DELETE_ITEM_SET,arguments);
        Statement stmt = null;
        int i = 0;
        try {
            stmt = connection.createStatement();
            i = stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        } finally {
            DBConnection.safeClose(stmt,null);
        }
        return i;

    }

    public int deleteItemSetCategory(Product rootProduct) {

        Object[] arguments = {new Long(rootProduct.getProductID())};
        String SQL = DBConnection.formatSql(DELETE_ITEM_SET_CATEGORY,arguments);
        Statement stmt = null;
        int i = 0;
        try {
            stmt = connection.createStatement();
            i = stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        } finally {
            DBConnection.safeClose(stmt,null);
        }
        return i;


    }
    public int deleteProduct(Product rootProduct) {

        Object[] arguments = {new Long(rootProduct.getProductID())};
        String SQL = DBConnection.formatSql(DELETE_PRODUCT,arguments);
        System.out.println(SQL);
        Statement stmt = null;
        int i = 0;
        try {
            stmt = connection.createStatement();
            i = stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        } finally {
            DBConnection.safeClose(stmt,null);
        }
        return i;


    }
    public int deleteDatapoints(Product rootProduct) {

        Object[] arguments = {new Long(rootProduct.getProductID())};
        String SQL = DBConnection.formatSql(DELETE_DATAPOINTS,arguments);
        Statement stmt = null;
        int i = 0;
        try {
            stmt = connection.createStatement();
            i = stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        } finally {
            DBConnection.safeClose(stmt,null);
        }
        return i;

    }

    public int deleteItemSetItem(Product rootProduct) {

        Object[] arguments = {new Long(rootProduct.getProductID())};
        String SQL = DBConnection.formatSql(DELETE_ITEM_SET_ITEM,arguments);
        Statement stmt = null;
        int i = 0;
        try {
            stmt = connection.createStatement();
            i = stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        } finally {
            DBConnection.safeClose(stmt,null);
        }
        return i;

    }

    public int deleteDatapointConditionCode(Product rootProduct) {

        Object[] arguments = {new Long(rootProduct.getProductID())};
        String SQL = DBConnection.formatSql(DELETE_DATAPOINT_CONDITION_CODE,arguments);
        Statement stmt = null;
        int i = 0;
        try {
            stmt = connection.createStatement();
            i = stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        } finally {
            DBConnection.safeClose(stmt,null);
        }
        return i;

    }

}
