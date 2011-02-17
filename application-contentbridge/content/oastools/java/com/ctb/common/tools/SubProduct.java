package com.ctb.common.tools;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 6, 2003
 * Time: 10:02:48 AM
 * To change this template use Options | File Templates.
 */
public class SubProduct extends Product {

    private String subProduct;
    private long parentProductID;

    public SubProduct() {

    }

    public SubProduct(long id, long parentId, String frameworkCode, String productType, String subProduct) {
        super(id,frameworkCode,productType);
        this.parentProductID = parentId;
        this.subProduct = subProduct;
    }
    public long getParentProductID() {
        return parentProductID;
    }

    public void setSubProductID(long subProductID) {
        this.parentProductID = subProductID;
    }

    public String getSubProductName() {
        return subProduct;
    }

    public void setSubProductName(String subProduct) {
        this.subProduct = subProduct;
    }
}
