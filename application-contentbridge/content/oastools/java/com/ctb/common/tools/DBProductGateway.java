package com.ctb.common.tools;

import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 6, 2003
 * Time: 10:17:12 AM
 * To change this template use Options | File Templates.
 */


public class DBProductGateway extends AbstractDBGateway {

    private static final String SUB_PRODUCT_UNIQUE_WHERE_CLAUSE = " where product.parent_product_id = {0} " +
            "and product.internal_display_name = '{1}'";
    private static final String ROOT_PRODUCT_UNIQUE_WHERE_CLAUSE = " where product.internal_display_name = '{0}'";
    private static final String SUBPRODUCTS_BY_PRODUCT_WHERE_CLAUSE = " where product.parent_product_id = {0} " +
            "and product.parent_product_id <> " +
            "product.product_id";

    public DBProductGateway(Connection conn) {
            super(conn);
    }

    /**
     * Finds a root product by framework code.  This should be CTB, CA, KY, etc.
     * @param frameworkCode State or general IBS framework code
     * @return Product, should have product type as CF, null values on subproduct, should be unique
     * todo - mws - subclass product to reflect the product types
     */
    public Product findRootProductByFrameworkCode(String frameworkCode) {
        Object[] arguments = {frameworkCode};
        DBProductMapper mapper = new DBProductMapper();
        Object object = executeMappingForSingleResult(ROOT_PRODUCT_UNIQUE_WHERE_CLAUSE, arguments, mapper);
        return (Product) object;
    }

    public void updateRootProduct(Product product) {
        DBProductMapper mapper = new DBProductMapper();
        //executeUpdateForObject(ROOT_PRODUCT_UNIQUE_WHERE_CLAUSE, product, mapper);
    }


    /**
     * Finds a SubProduct object, like KY Mathematics.
     * todo - mws - does the product type need to also determine uniqueness?
     * @param frameworkCode
     * @param subProduct
     * @return null or a single SubProduct object - should be unique
     *
     */
    public SubProduct findByFrameworkCodeAndSubProduct(String frameworkCode, String subProduct) {
        return findByRootProductAndSubProduct(findRootProductByFrameworkCode(frameworkCode),subProduct);
    }

    public SubProduct findByRootProductAndSubProduct(Product product, String subProduct) {
        Object[] arguments = {new Long(product.getProductID()),product.getFrameworkCode() + " " + subProduct};
        DBSubProductMapper mapper = new DBSubProductMapper();
        Object object = executeMappingForSingleResult(SUB_PRODUCT_UNIQUE_WHERE_CLAUSE, arguments, mapper);
        return (SubProduct) object;
    }
    public SubProduct[] findSubProductsByFrameworkCode(String frameworkCode) {
        return findSubProductsByRootProduct(findRootProductByFrameworkCode(frameworkCode));
    }

    public SubProduct[] findSubProductsByRootProduct(Product product) {
        Object[] arguments = {new Long(product.getProductID())};
        DBSubProductMapper mapper = new DBSubProductMapper();
        Object[] objects = executeMappingForMultipleResults(SUBPRODUCTS_BY_PRODUCT_WHERE_CLAUSE, arguments, mapper);
        return (SubProduct[]) objects;
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
