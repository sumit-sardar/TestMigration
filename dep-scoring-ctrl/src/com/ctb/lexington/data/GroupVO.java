package com.ctb.lexington.data;

import java.util.Map;


/*
 * GroupVO.java
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
public class GroupVO extends NodeVO implements java.io.Serializable {

    public static final String BEAN_LABEL = "com.ctb.lexington.bean.GroupVO";
    public static final String BEAN_ARRAY_LABEL = "com.ctb.lexington.bean.GroupVO.array";
    
    private Map criteria;

    /** Creates new GroupVO */
    public GroupVO() {
    }
    
    //-- Get/Set Methods --//
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getGroupID()
    {
        return this.getOrgNodeId();
    }
    
    /**
     * Set the value of this property.
     *
     * @param groupID_ The value to set the property to.
     * @return void
     */
    public void setGroupID(String groupID_)
    {
        if (groupID_ != null) 
        {
            this.setOrgNodeId(groupID_);
        }
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getGroupName()
    {
        return this.getOrgNodeName();
    }
    
    /**
     * Set the value of this property.
     *
     * @param groupName_ The value to set the property to.
     * @return void
     */
    public void setGroupName(String groupName_)
    {
        if (groupName_ != null) 
        {
            this.setOrgNodeName(groupName_);
        }
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getGroupDescription()
    {
        return this.getOrganizationCode();
    }
    
    /**
     * Set the value of this property.
     *
     * @param groupDescription_ The value to set the property to.
     * @return void
     */
    public void setGroupDescription(String groupDescription_)
    {
        if (groupDescription_ != null)
        {
            this.setOrganizationCode(groupDescription_);
        }
        
    }
    /**
     * @return Returns the criteria.
     */
    public Map getCriteria() {
        return criteria;
    }
    /**
     * @param criteria The criteria to set.
     */
    public void setCriteria(Map criteria) {
        this.criteria = criteria;
    }
}