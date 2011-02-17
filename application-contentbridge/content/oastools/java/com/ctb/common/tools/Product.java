package com.ctb.common.tools;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 6, 2003
 * Time: 10:02:48 AM
 * To change this template use Options | File Templates.
 */
public class Product {

    private String frameworkCode;
    private String productType;
    private long productID;

    public Product() {

    }

    public Product(long id, String frameworkCode, String productType) {
        this.productID = id;
        this.frameworkCode = frameworkCode;
        this.productType = productType;

    }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }


    public String getFrameworkCode() {
        return frameworkCode;
    }

    public void setFrameworkCode(String frameworkCode) {
        this.frameworkCode = frameworkCode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
