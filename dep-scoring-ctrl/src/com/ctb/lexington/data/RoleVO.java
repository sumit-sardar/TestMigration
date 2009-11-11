package com.ctb.lexington.data;

import java.util.Date;


/**
 * @author John M. Shields
 * @version $Id$
 *
 * @created October 9, 2002
 */
public class RoleVO implements java.io.Serializable
{
    /** DOCUMENT ME! */
    public static final String VO_LABEL = "vo.RoleVO";

    /** DOCUMENT ME! */
    public static final String VO_ARRAY_LABEL = "vo.RoleVO.array";

    /** Description of the Field */
    protected Date createdDateTime = null;

    /** Description of the Field */
    protected Date updatedDateTime = null;

    /** Description of the Field */
    protected Integer createdBy = null;

    /** Description of the Field */
    protected Integer roleId = null;

    /** Description of the Field */
    protected Integer roleTypeId = null;

    /** Description of the Field */
    protected Integer updatedBy = null;

    /** Description of the Field */
    protected String activationStatus = null;

    /** Description of the Field */
    protected String roleName = null;

    /**
     * Creates new RoleVO
     */
    public RoleVO() {}

    /**
     * Sets the activationStatus attribute of the RoleVO object
     *
     * @param activationStatus_ The new activationStatus value
     */
    public void setActivationStatus(String activationStatus_)
    {
        this.activationStatus = activationStatus_;
    }

    /**
     * Gets the activationStatus attribute of the RoleVO object
     *
     * @return The activationStatus value
     */
    public String getActivationStatus()
    {
        return this.activationStatus;
    }

    /**
     * Sets the createdBy attribute of the RoleVO object
     *
     * @param createdBy_ The new createdBy value
     */
    public void setCreatedBy(Integer createdBy_)
    {
        this.createdBy = createdBy_;
    }

    /**
     * Gets the createdBy attribute of the RoleVO object
     *
     * @return The createdBy value
     */
    public Integer getCreatedBy()
    {
        return this.createdBy;
    }

    /**
     * Sets the createdDateTime attribute of the RoleVO object
     *
     * @param createdDateTime_ The new createdDateTime value
     */
    public void setCreatedDateTime(Date createdDateTime_)
    {
        this.createdDateTime = createdDateTime_;
    }

    /**
     * Gets the createdDateTime attribute of the RoleVO object
     *
     * @return The createdDateTime value
     */
    public Date getCreatedDateTime()
    {
        return this.createdDateTime;
    }

    /**
     * Sets the roleId attribute of the RoleVO object
     *
     * @param roleId_ The new roleId value
     */
    public void setRoleId(Integer roleId_)
    {
        this.roleId = roleId_;
    }

    /**
     * Gets the roleId attribute of the RoleVO object
     *
     * @return The roleId value
     */
    public Integer getRoleId()
    {
        return this.roleId;
    }

    /**
     * Sets the roleName attribute of the RoleVO object
     *
     * @param roleName_ The new roleName value
     */
    public void setRoleName(String roleName_)
    {
        this.roleName = roleName_;
    }

    /**
     * Gets the roleName attribute of the RoleVO object
     *
     * @return The roleName value
     */
    public String getRoleName()
    {
        return this.roleName;
    }

    /**
     * Sets the roleTypeId attribute of the RoleVO object
     *
     * @param roleTypeId_ The new roleTypeId value
     */
    public void setRoleTypeId(Integer roleTypeId_)
    {
        this.roleTypeId = roleTypeId_;
    }

    /**
     * Gets the roleTypeId attribute of the RoleVO object
     *
     * @return The roleTypeId value
     */
    public Integer getRoleTypeId()
    {
        return this.roleTypeId;
    }

    /**
     * Sets the updatedBy attribute of the RoleVO object
     *
     * @param updatedBy_ The new updatedBy value
     */
    public void setUpdatedBy(Integer updatedBy_)
    {
        this.updatedBy = updatedBy_;
    }

    /**
     * Gets the updatedBy attribute of the RoleVO object
     *
     * @return The updatedBy value
     */
    public Integer getUpdatedBy()
    {
        return this.updatedBy;
    }

    /**
     * Sets the updatedDateTime attribute of the RoleVO object
     *
     * @param updatedDateTime_ The new updatedDateTime value
     */
    public void setUpdatedDateTime(Date updatedDateTime_)
    {
        this.updatedDateTime = updatedDateTime_;
    }

    /**
     * Gets the updatedDateTime attribute of the RoleVO object
     *
     * @return The updatedDateTime value
     */
    public Date getUpdatedDateTime()
    {
        return this.updatedDateTime;
    }
}
