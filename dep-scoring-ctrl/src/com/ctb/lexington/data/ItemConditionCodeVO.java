package com.ctb.lexington.data;

// java imports

/**
 * <p>
 * ItemConditionCodeVO
 * </p>
 * <p>
 * Copyright CTB/McGraw-Hill, 2003
 * CONFIDENTIAL
 * </p>
 *
 * @author  <a href="mailto:dvu@ctb.com">Dat Vu</a>
 */

public class ItemConditionCodeVO extends Object implements java.io.Serializable, java.lang.Cloneable  
{

    public static final String VO_LABEL = "com.ctb.lexington.data.ItemConditionCodeVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";


    //properties below should match 100% the entity bean ejb
    private String itemId;
    private String conditionCode;
    private String description;


    /** Creates new ItemConditionCodeVO */
    public ItemConditionCodeVO() {
    }

    public Object clone(){
        try
        {
            return super.clone();
        }
        catch(CloneNotSupportedException e)
        {
            return null;
        }
    }


    //-- Get/Set Methods --//
    
    public String getItemId()
    {
        return this.itemId;
    }

    public void setItemId(String itemId_)
    {
        this.itemId = itemId_;
    }

    public String getConditionCode()
    {
        return this.conditionCode;
    }

    public void setConditionCode(String conditionCode_)
    {
        this.conditionCode = conditionCode_;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description_)
    {
        this.description = description_;
    }


}
