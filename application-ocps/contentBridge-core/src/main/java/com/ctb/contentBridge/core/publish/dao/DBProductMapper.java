package com.ctb.contentBridge.core.publish.dao;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.tools.Product;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 7, 2003
 * Time: 8:27:34 AM
 * To change this template use Options | File Templates.
 */
public class DBProductMapper implements DBMapper {

    private List productList = new ArrayList();
    public static final String PRODUCT_SELECT_CLAUSE = "select product.product_id, product.internal_display_name," +
                                 " product.product_type from product";

    public static final String PRODUCT_UPDATE_CLAUSE = "update product set product.internal_display_name = {0}, product.product_type = {1} where product.product_id = {2}";

    public ArgumentsSQLPair[] getArgumentsAndSQLForUpdate(Object persistentObject) {
        Product product = (Product) persistentObject;
        Object[] arguments = {product.getFrameworkCode(),product.getProductType(),new Long(product.getProductID())};
        ArgumentsSQLPair pair = new ArgumentsSQLPair(arguments,PRODUCT_UPDATE_CLAUSE);
        ArrayList list = new ArrayList();
        list.add(pair);
        return (ArgumentsSQLPair[]) list.toArray(new ArgumentsSQLPair[list.size()]);
    }

    public void clear() {
        productList.clear();
    }
    public void assembleObjectFromSQLResult(ResultSet rs) {

        try {
            productList.add(new Product(rs.getLong(1),rs.getString(2),rs.getString(3)));
        } catch (SQLException e) {
            throw new SystemException(e.getMessage());
        }

    }

    public Product[] getProducts() {
        Product[] products = (Product[]) productList.toArray(new Product[productList.size()]);
        clear();
        return products;
    }

    public Object[] getResults() {
        return getProducts();
    }
    public String getSelectClause() {
        return PRODUCT_SELECT_CLAUSE;
    }

    public void assembleObjectFromMultipleRows(ResultSet rs) {
        throw new BusinessException("Product objects are assembled from single rows. Use assmemble" +
                "ObjectFromResultSet method");

    }
       
	public boolean isObjectAssembleFromMultipleRows() {
		return false;
	}
	
	
	public String[] getKeyColumns() {
		return null;
	}
}
