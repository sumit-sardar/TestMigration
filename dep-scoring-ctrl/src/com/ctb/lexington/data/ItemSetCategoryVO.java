package com.ctb.lexington.data;

/*
 * ItemSetCategoryVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 * @author Adrienne J. Dimayuga
 * @version
 *
 */


public class ItemSetCategoryVO implements java.io.Serializable {

    public static final String VO_LABEL = "com.ctb.lexington.valueobject.ItemSetCategoryVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.valueobject.ItemSetCategoryVO.array";

    private Integer itemSetCategoryId;
    private String itemSetCategoryName;
    private Integer itemSetCategoryLevel;
    
    /** Creates new ItemSetCategoryVO */
    public ItemSetCategoryVO() 
    {
        super();
    }
    
    
    //-- Get/Set Methods --//
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public Integer getItemSetCategoryId()
    {
        return this.itemSetCategoryId;
    }
    
    /**
     * Set the value of this property.
     *
     * @param itemSetCategoryId_ The value to set the property to.
     * @return void
     */
    public void setItemSetCategoryId(Integer itemSetCategoryId_)
    {
        this.itemSetCategoryId = itemSetCategoryId_;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getItemSetCategoryName()
    {
        return this.itemSetCategoryName;
    }
    
    /**
     * Set the value of this property.
     *
     * @param itemSetCategoryName_ The value to set the property to.
     * @return void
     */
    public void setItemSetCategoryName(String itemSetCategoryName_)
    {
        this.itemSetCategoryName = itemSetCategoryName_ ;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public Integer getItemSetCategoryLevel()
    {
        return this.itemSetCategoryLevel;
    }
    
    /**
     * Set the value of this property.
     *
     * @param itemSetCategoryName_ The value to set the property to.
     * @return void
     */
    public void setItemSetCategoryLevel(Integer itemSetCategoryLevel_)
    {
        this.itemSetCategoryLevel = itemSetCategoryLevel_ ;
    }
    
}