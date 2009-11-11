package com.ctb.lexington.valueobject;

/*
 * FilterVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */


import java.util.List;

/**
 *
 * @author  rmariott
 * @version
 */
public class FilterVO extends Object implements java.io.Serializable
{

    public static final String VO_LABEL = "com.ctb.lexington.valueobject.FilterVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.valueobject.FilterVO.array";

    private Integer filterId;
    private String filterName;


    //shkdjalh
    private List filterIdList;


    /** Creates new FilterVO */
    public FilterVO() {

    }


    //-- Get/Set Methods --//



    /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public Integer getFilterId()
    {
        return this.filterId;
    }

    /**
     * deprecated
     *
     */
    public void setFilterId(Integer filterId_)
    {
        this.filterId = filterId_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getFilterName()
    {
        return this.filterName;
    }

    /**
     * Set the value of this property.
     *
     * @param filterName_ The value to set the property to.
     * @return void
     */
    public void setFilterName(String filterName_)
    {
        this.filterName = filterName_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return List The value of the property.
     */
    public List getFilterIdList()
    {
        return this.filterIdList;
    }

    /**
     * Set the value of this property.
     *
     * @param filterIdList_ The value to set the property to.
     * @return void
     */
    public void setFilterIdList(List filterIdList_)
    {
        this.filterIdList = filterIdList_;
    }
}
