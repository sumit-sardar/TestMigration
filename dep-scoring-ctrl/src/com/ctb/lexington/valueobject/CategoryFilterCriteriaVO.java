package com.ctb.lexington.valueobject;

/*
 * CategoryFilterCriteriaVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

import java.io.Serializable;
import java.util.List;

import com.ctb.lexington.data.*;

/**
 *
 * @author  rmariott
 * @version 
 */
public class CategoryFilterCriteriaVO extends Object implements Serializable {

    public static final String VO_LABEL = "com.ctb.lexington.valueobject.CategoryFilterCriteriaVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.valueobject.CategoryFilterCriteriaVO.array";
    
    private Integer categoryId;
    private String categoryName;
    private List filterList;
    private ItemSetCategoryVO category;

    /** Creates new CategoryFilterCriteriaVO */
    public CategoryFilterCriteriaVO() {
        
    }
    
    
    //-- Get/Set Methods --//
    
    /**
     * Get this property from this bean instance.
     *
     * @return ItemSetCategoryVO The value of the property.
     */
    public ItemSetCategoryVO getCategory()
    {
        return this.category;
    }
    
    /**
     * Set the value of this property.
     *
     * @param category_ The value to set the property to.
     * @return void
     */
    public void setCategory(ItemSetCategoryVO category_)
    {
        this.category = category_;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public Integer getCategoryId()
    {
        return this.categoryId;
    }
    
    /**
     * Set the value of this property.
     *
     * @param categoryId_ The value to set the property to.
     * @return void
     */
    public void setCategoryId(Integer categoryId_)
    {
        this.categoryId = categoryId_;
    }
    
    /**
     * Get this property from this bean instance.
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
     * @param categoryName_ The value to set the property to.
     * @return void
     */
    public void setCategoryName(String categoryName_)
    {
        this.categoryName = categoryName_;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return List List of CurriculumNodeVOs
     */
    public List getFilterList()
    {
        return this.filterList;
    }
    
    /**
     * Set the value of this property.
     *
     * @param filterList_ List of CurriculumNodeVOs
     * @return void
     */
    public void setFilterList(List filterList_)
    {
        this.filterList = filterList_;
    }
    

}
