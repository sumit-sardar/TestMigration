package com.ctb.lexington.data;


/*
 * TestletVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author  rmariott
 * @version 
 */
public class TestletVO extends TestVO implements Serializable {

    public static final String VO_LABEL = "com.ctb.lexington.data.TestletVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.data.TestletVO.array";
    
    private List itemIdList = new ArrayList();
    
    /** Creates new TestletVO */
    public TestletVO() {
        
    }
    
    
    //-- Get/Set Methods --//
    
    
    
    /**
     * Get this property from this bean instance.
     *
     * @return List of Integers representing the items in this testlet; NOT a
     * List of actual VOs.
     */
    public List getItemIdList()
    {
        return this.itemIdList;
    }
    
    /**
     * Set the value of this property.
     *
     * @param itemIdList_ List of Integers representing the items in this testlet.
     * @return void
     */
    public void setItemIdList(List itemIdList_)
    {
        this.itemIdList = itemIdList_;
    }
    
}
