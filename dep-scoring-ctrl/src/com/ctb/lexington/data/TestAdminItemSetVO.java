package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * Copyright CTB/McGraw-Hill, 2004 CONFIDENTIAL
 *
 * @author Tai Truong
 * @created July 22, 2004
 */

public class TestAdminItemSetVO implements Serializable{

    public  static final String VO_LABEL       = "com.ctb.lexington.data.TestAdminItemSetVO";
    public  static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    private Integer testAdminId;
    private Integer itemSetId;
    private Integer itemSetOrder;

    public TestAdminItemSetVO() {
    }

    public Integer getTestAdminId()
    {
        return this.testAdminId;
    }
    public void setTestAdminId(Integer testAdminId_)
    {
        this.testAdminId = testAdminId_;
    }
    
    public Integer getItemSetId()
    {
        return this.itemSetId;
    }
    public void setItemSetId(Integer itemSetId_)
    {
        this.itemSetId = itemSetId_;
    }

    public Integer getItemSetOrder()
    {
        return this.itemSetOrder;
    }
    public void setItemSetOrder(Integer itemSetOrder_) 
    {
        this.itemSetOrder = itemSetOrder_;
    }
}