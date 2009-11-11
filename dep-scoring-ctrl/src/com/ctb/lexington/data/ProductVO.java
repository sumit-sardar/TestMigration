package com.ctb.lexington.data;

/*
 * ProductVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */



/**
 *
 * @author  rmariott
 * @version 
 */
public class ProductVO extends Object implements java.io.Serializable 
{

    public static final String VO_LABEL = "com.ctb.lexington.data.ProductVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.data.ProductVO.array";
    
    /** Product Type Constants */
    public static final String PRODUCT_TYPE_RESEARCH_STUDY = "RS";
        
    public static final String PRODUCT_TYPE_TERRANOVA = "TN";
        
    /** DOCUMENT ME! */
    public static final String PRODUCT_TYPE_STANDARD_TYPE = "ST";
        
    public static final String PRODUCT_TYPE_TERRANOVA_TEST = "TV";
        
    public static final String PRODUCT_TYPE_TABE_TEST = "TB";
        
        
    private Integer productId;
    private Integer parentProductId;
    private String productName;
    private String productType;
    private String brandingTypeCode;
    private Integer scoringItemSetLevel;
    private String internalDisplayName;

    /** Creates new ProductVO */
    public ProductVO() {
        
    }
    
    
    //-- Get/Set Methods --//
    
    
    
    /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public Integer getProductId()
    {
        return this.productId;
    }
    
    /**
     * Set the value of this property.
     *
     * @param productId_ The value to set the property to.
     * @return void
     */
    public void setParentProductId(Integer productId_)
    {
        this.parentProductId = productId_;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public Integer getParentProductId()
    {
        return this.parentProductId;
    }
    
    /**
     * Set the value of this property.
     *
     * @param productId_ The value to set the property to.
     * @return void
     */
    public void setProductId(Integer productId_)
    {
        this.productId = productId_;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getProductName()
    {
        return this.productName;
    }
    
    /**
     * Set the value of this property.
     *
     * @param productName_ The value to set the property to.
     * @return void
     */
    public void setProductType(String productType_)
    {
        this.productType = productType_;
    }
    
        /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getProductType()
    {
        return this.productType;
    }
    
    /**
     * Set the value of this property.
     *
     * @param brandingTypeCode_ The value to set the property to.
     * @return void
     */
    public void setBrandingTypeCode(String brandingTypeCode_)
    {
        this.brandingTypeCode = brandingTypeCode_;
    }
    
        /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getBrandingTypeCode()
    {
        return this.brandingTypeCode;
    }
    
    /**
     * Set the value of this property.
     *
     * @param productName_ The value to set the property to.
     * @return void
     */
    public void setProductName(String productName_)
    {
        this.productName = productName_;
    }
    /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public Integer getScoringItemSetLevel()
    {
        return this.scoringItemSetLevel;
    }
    
    /**
     * Set the value of this property.
     *
     * @param level__ The value to set the property to.
     * @return void
     */
    public void setScoringItemSetLevel(Integer level_)
    {
        this.scoringItemSetLevel = level_;
    }
    
	/**
	 * Returns the internalDisplayName.
	 * @return String
	 */
	public String getInternalDisplayName()
	{
		return internalDisplayName;
	}

	/**
	 * Sets the internalDisplayName.
	 * @param internalDisplayName The internalDisplayName to set
	 */
	public void setInternalDisplayName(String internalDisplayName)
	{
		this.internalDisplayName = internalDisplayName;
	}

}
