package com.ctb.lexington.data;


/*
 * ProductDetailVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */
import java.util.List;


/**
 * @author sprakash
 * @version
 */
public class ProductDetailVO extends ProductVO implements java.io.Serializable
{
    /** DOCUMENT ME! */
    public static final String VO_LABEL =
        "com.ctb.lexington.valueobject.ProductDetailVO";

    /** DOCUMENT ME! */
    public static final String VO_ARRAY_LABEL =
        "com.ctb.lexington.valueobject.ProductDetailVO.array";
    private List tests;

    /**
     * Creates new ProductVO
     */
    public ProductDetailVO()
    {
        super();
    }

    /**
     * Creates new ProductVO
     */
    public ProductDetailVO(ProductVO productVO)
    {
        super();
        setProductId(productVO.getProductId());
        setProductName(productVO.getProductName());
        setProductType(productVO.getProductType());
        setBrandingTypeCode(productVO.getBrandingTypeCode());
    }

    /**
     * Set the value of this property.
     *
     * @param productId_ The value to set the property to.
     *
     * @return void
     */
    public void setTests(List tests_)
    {
        this.tests = tests_;
    }

    //-- Get/Set Methods --//

    /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public List getTests()
    {
        return this.tests;
    }
}
