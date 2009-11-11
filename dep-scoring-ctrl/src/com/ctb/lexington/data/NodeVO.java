package com.ctb.lexington.data;


/*
 * NodeVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */
import java.io.Serializable;


/**
 * This class is used as a value object that represents the node entity.
 *
 * @author pdunlave
 * @version $Id$
 */
public class NodeVO implements Serializable
{
    public static final String VO_LABEL = "com.ctb.lexington.data.NodeVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";
    private String categoryName;
    private Integer orgNodeCategoryId;
    private String customerId;
    private String orgNodeId;
    private String orgNodeName;
    private String organizationCode;
    private int indentLevel;
	private String parentOrgNodeId;

    /**
     * Creates new NodeVO
     */
    public NodeVO()
    {
        this.categoryName = "";
        this.orgNodeId = "";
        this.orgNodeName = "";
        this.organizationCode = "";
        this.customerId = "";
        this.orgNodeCategoryId = null;
    }

    /**
     * Set the value of this property.
     *
     * @param categoryName__ The value to set the property to.
     *
     * @return void
     */
    public void setCategoryName(String categoryName_)
    {
        this.categoryName = categoryName_;
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
     * @param customerID_ value to set the property to.
     *
     * @return void
     */
    public void setCustomerId(String customerID_)
    {
        this.customerId = customerID_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getCustomerId()
    {
        return customerId;
    }

    /**
     * Set the value of this property.
     *
     * @param indentLeve_The value to set the property to.
     *
     * @return void
     */
    public void setIndentLevel(int indentLevel_)
    {
        this.indentLevel = indentLevel_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return int The value of the property.
     */
    public int getIndentLevel()
    {
        return this.indentLevel;
    }

    /**
     * Sets the orgNodeCategoryId.
     *
     * @param orgNodeCategoryId The orgNodeCategoryId to set
     */
    public void setOrgNodeCategoryId(Integer orgNodeCategoryId_)
    {
        this.orgNodeCategoryId = orgNodeCategoryId_;
    }

    /**
     * Returns the orgNodeCategoryId.
     *
     * @return Integer
     */
    public Integer getOrgNodeCategoryId()
    {
        return orgNodeCategoryId;
    }

    public void setOrgNodeDescription(String arg1)
    {
        setOrganizationCode(arg1);
    }

    public String getOrgNodeDescription()
    {
        return getOrganizationCode();
    }

    /**
     * Set the value of this property.
     *
     * @param nodeID_ The value to set the property to.
     *
     * @return void
     */
    public void setOrgNodeId(String nodeID_)
    {
        this.orgNodeId = nodeID_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getOrgNodeId()
    {
        return this.orgNodeId;
    }

    /**
     * Set the value of this property.
     *
     * @param nodeName_ The value to set the property to.
     *
     * @return void
     */
    public void setOrgNodeName(String nodeName_)
    {
        this.orgNodeName = nodeName_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getOrgNodeName()
    {
        return this.orgNodeName;
    }

    /**
     * Set the value of this property.
     *
     * @param organizationCode_ The value to set the property to.
     *
     * @return void
     */
    public void setOrganizationCode(String organizationCode_)
    {
        this.organizationCode = organizationCode_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getOrganizationCode()
    {
        return this.organizationCode;
    }

	/**
	 * @return
	 */
	public String getParentOrgNodeId() {
		return this.parentOrgNodeId;
	}

	public void setParentOrgNodeId(String parentOrgNodeId) {
		this.parentOrgNodeId = parentOrgNodeId;
	}
}
