package com.ctb.common.tools;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 7, 2003
 * Time: 8:27:34 AM
 * To change this template use Options | File Templates.
 */
public class DBSubProductMapper implements DBMapper {

    private List subProductList = new ArrayList();
    public static final String SUBPRODUCT_SELECT_CLAUSE = "select product.product_id, product.internal_display_name," +
                                        " product.product_type, product.parent_product_id from product";


    public void clear() {
        subProductList.clear();
    }

    public ArgumentsSQLPair[] getArgumentsAndSQLForUpdate(Object persistentObject) {
        return new ArgumentsSQLPair[0];
    }

    public void assembleObjectFromSQLResult(ResultSet rs) {

        try {
            StringTokenizer tokenizer = new StringTokenizer(rs.getString(2));
            String fwkCode = tokenizer.nextToken();
            String subproduct = tokenizer.nextToken();
            subProductList.add(new SubProduct(rs.getLong(1),rs.getLong(4),fwkCode,rs.getString(3),subproduct));
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        }

    }
    public Object[] getResults() {
        return getSubProducts();
    }

    public SubProduct[] getSubProducts() {
        SubProduct[] subProducts = (SubProduct[]) subProductList.toArray(new SubProduct[subProductList.size()]);
        clear();
        return subProducts;
    }

    public String getSelectClause() {
        return SUBPRODUCT_SELECT_CLAUSE;
    }


    public void assembleObjectFromMultipleRows(ResultSet rs) {
        throw new SystemException("SubProduct objects are assembled from single rows. Use assmemble" +
                "ObjectFromResultSet method");

    }
    
	public boolean isObjectAssembleFromMultipleRows() {
		return false;
	}
	
	
	public String[] getKeyColumns() {
		return null;
	}
}
