package com.ctb.lexington.data;

/*
 * CurriculumNodeVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 * @author Ryan A. Mariotti
 * @version
 *
 */

import java.util.List;

public class CurriculumNodeVO extends ItemSetVO implements java.io.Serializable {

    public static final String VO_LABEL = "com.ctb.lexington.valueobject.CurriculumNodeVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.valueobject.CurriculumNodeVO.array";

    private Integer categoryId;
    private String categoryName;
    private String curriculumFrameworkName;
    private List children;
    private Integer productId;
    private Integer curriculumFrameworkProductId;   
    
    /** Creates new CurriculumNodeVO */
    public CurriculumNodeVO() 
    {
        super();
    }
    
    
    //-- Get/Set Methods --//
    
    /**
     * Get this property from this bean instance. The Id of the category level 
     * represented by this CurriculumNodeVO.
     *
     * @return Integer The value of the property.
     */
    public Integer getCategoryId()
    {
        return this.categoryId;
    }
    
    /**
     * Set the value of this property. The Id of the category level represented 
     * by this CurriculumNodeVO.
     *
     * @param categoryId_ The value to set the property to.
     * @return void
     */
    public void setCategoryId(Integer categoryId_)
    {
        this.categoryId = categoryId_;
    }
    
    /**
     * Get this property from this bean instance. The name of the category level 
     * represented by this CurriculumNodeVO.
     *
     * @return String The value of the property.
     */
    public String getCategoryName()
    {
        return this.categoryName;
    }
    
    /**
     * Set the value of this property.
     *
     * @param categoryName_ The value to set the property to. The name of the 
     * category level represented by this CurriculumNodeVO.
     * @return void
     */
    public void setCategoryName(String categoryName_)
    {
        this.categoryName = categoryName_;
    }
    
    
    /**
     * Get this property from this bean instance. The name of the curriculum 
     * framework to which this node belongs.
     *
     * @return String The value of the property.
     */
    public String getCurriculumFrameworkName()
    {
        return this.curriculumFrameworkName;
    }
    
    /**
     * Set the value of this property. The name of the curriculum framework to 
     * which this node belongs.
     *
     * @param curriculumFrameworkName_ The value to set the property to.
     * @return void
     */
    public void setCurriculumFrameworkName(String curriculumFrameworkName_)
    {
        this.curriculumFrameworkName = curriculumFrameworkName_;
    }
    
    /**
     * Get this property from this bean instance. Represents the child filters 
     * of this curriculum node; this will be a soft reference in order to save 
     * memory.
     *
     * @return List The value of the property.
     */
    public List getChildren()
    {
        return this.children;
    }
    
    /**
     * Set the value of this property. Represents the child filters of this 
     * curriculum node; this will be a soft reference in order to save memory.
     *
     * @param children_ The value to set the property to.
     * @return void
     */
    public void setChildren(List children_)
    {
        this.children = children_;
    }
    
    public void setProductId( Integer productId_)
    {
        this.productId = productId_;
    }
    
    public Integer getProductId()
    {
        return this.productId;
    }
    
    public void setCurriculumFrameworkProductId( Integer curriculumFrameworkProductId_ )
    {
        this.curriculumFrameworkProductId = curriculumFrameworkProductId_;
    }
    
    public Integer getCurriculumFrameworkProductId()
    {
        return this.curriculumFrameworkProductId;
    }
    
    
    
}