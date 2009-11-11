package com.ctb.lexington.data;

import java.util.Date;

/**
 * @author    John M. Shields
 * @created   October 9, 2002
 * @version   $Id$
 */
public class PermissionVO implements java.io.Serializable
{

    /**
     * Description of the Field
     */
    public static final String VO_LABEL = "vo.PermissionVO";
    /**
     * Description of the Field
     */
    public static final String VO_ARRAY_LABEL = "vo.PermissionVO.array";

    /**
     * Description of the Field
     */
    protected Integer permissionId = null;
    /**
     * Description of the Field
     */
    protected String permissionName = null;
    /**
     * Description of the Field
     */
    //protected String permissionActions = null;
    protected String actions = null;
    /**
     * Description of the Field
     */
    protected String permissionDisplayName = null;
    /**
     * Description of the Field
     */
    //protected String permissionType = null;
    protected String type = null;
    
    /**
     * Description of the Field
     */
    protected Integer createdBy = null;
    /**
     * Description of the Field
     */
    protected Date createdDateTime = null;
    /**
     * Description of the Field
     */
    protected Integer updatedBy = null;
    /**
     * Description of the Field
     */
    protected Date updatedDateTime = null;
    /**
     * Description of the Field
     */
    protected String activationStatus = null;

    /**
     * Creates new RoleVO
     */
    public PermissionVO() { }

    /**
     * Uses the permission id, name, type, actions, and activation status to
     * determine if two <code>PermissionVO</code>s are equal.
     *
     * @param o_  Object to compare
     * @return    true if equal, false otherwise
     */
    public boolean equals(Object o_)
    {
        boolean result = false;

        if (o_ instanceof PermissionVO)
        {
            PermissionVO p = (PermissionVO)o_;

            // perm id
            if (this.permissionId != null)
            {
                result = (this.permissionId.equals(p.getPermissionId()));
            }
            else
            {
                result = (p.getPermissionId() == null);
            }

            if (result == false)
            {
                return result;
            }

            // perm name
            if (this.permissionName != null)
            {
                result = (this.permissionName.equals(p.getPermissionName()));
            }
            else
            {
                result = (p.getPermissionName() == null);
            }

            if (result == false)
            {
                return result;
            }

            // activation status
            if (this.activationStatus != null)
            {
                result = (this.activationStatus.equals(p.getActivationStatus()));
            }
            else
            {
                result = (p.getActivationStatus() == null);
            }

            if (result == false)
            {
                return result;
            }

            // perm action
            if (this.actions != null)
            {
                result = (this.actions.equals(p.getPermissionActions()));
            }
            else
            {
                result = (p.getPermissionActions() == null);
            }

            if (result == false)
            {
                return result;
            }

            // perm type
            if (this.type != null)
            {
                result = (this.type.equals(p.getPermissionType()));
            }
            else
            {
                result = (p.getPermissionType() == null);
            }

            if (result == false)
            {
                return result;
            }

        }

        return result;
    }

    /**
     * Sets the permissionId attribute of the RoleVO object
     *
     * @param permissionId_  The new permissionId value
     */
    public void setPermissionId(Integer permissionId_)
    {
        this.permissionId = permissionId_;
    }

    /**
     * Gets the permissionId attribute of the RoleVO object
     *
     * @return   The permissionId value
     */
    public Integer getPermissionId()
    {
        return this.permissionId;
    }

    /**
     * Sets the permissionName attribute of the RoleVO object
     *
     * @param permissionName_  The new permissionName value
     */
    public void setPermissionName(String permissionName_)
    {
        this.permissionName = permissionName_;
    }

    /**
     * Gets the permissionName attribute of the RoleVO object
     *
     * @return   The permissionName value
     */
    public String getPermissionName()
    {
        return this.permissionName;
    }

    /**
     * Sets the permissionName attribute of the RoleVO object
     *
     * @param permissionActions_  The new permissionActions value
     */
    public void setPermissionActions(String permissionActions_)
    {
        setPermissionAction(permissionActions_);
    }

    /**
     * Gets the permissionName attribute of the RoleVO object
     *
     * @return   The permissionName value
     */
    public String getPermissionActions()
    {
        return getPermissionAction();
    }
    
    /**
     * Sets the permissionName attribute of the RoleVO object
     *
     * @param permissionActions_  The new permissionActions value
     */
    public void setPermissionAction(String actions_)
    {
        this.actions = actions_;
    }

    /**
     * Gets the permissionName attribute of the RoleVO object
     *
     * @return   The permissionName value
     */
    public String getPermissionAction()
    {
        return this.actions;
    }

    /**
     * Sets the permissionName attribute of the RoleVO object
     *
     * @param permissionDisplayName_  The new permissionDisplayName value
     */
    public void setPermissionDisplayName(String permissionDisplayName_)
    {
        this.permissionDisplayName = permissionDisplayName_;
    }

    /**
     * Gets the permissionName attribute of the RoleVO object
     *
     * @return   The permissionName value
     */
    public String getPermissionDisplayName()
    {
        return this.permissionDisplayName;
    }

    /**
     * Sets the permissionName attribute of the RoleVO object
     *
     * @param permissionType_  The new permissionType value
     */
    public void setPermissionType(String permissionType_)
    {
        setType(permissionType_);
    }

    /**
     * Gets the permissionName attribute of the RoleVO object
     *
     * @return   The permissionName value
     */
    public String getPermissionType()
    {
        return getType();
    }
    
    /**
     * Sets the permissionName attribute of the RoleVO object
     *
     * @param permissionType_  The new permissionType value
     */
    public void setType(String type_)
    {
        this.type = type_;
    }

    /**
     * Gets the permissionName attribute of the RoleVO object
     *
     * @return   The permissionName value
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * Sets the createdBy attribute of the RoleVO object
     *
     * @param createdBy_  The new createdBy value
     */
    public void setCreatedBy(Integer createdBy_)
    {
        this.createdBy = createdBy_;
    }

    /**
     * Gets the createdBy attribute of the RoleVO object
     *
     * @return   The createdBy value
     */
    public Integer getCreatedBy()
    {
        return this.createdBy;
    }

    /**
     * Sets the createdDateTime attribute of the RoleVO object
     *
     * @param createdDateTime_  The new createdDateTime value
     */
    public void setCreatedDateTime(Date createdDateTime_)
    {
        this.createdDateTime = createdDateTime_;
    }

    /**
     * Gets the createdDateTime attribute of the RoleVO object
     *
     * @return   The createdDateTime value
     */
    public Date getCreatedDateTime()
    {
        return this.createdDateTime;
    }

    /**
     * Sets the updatedBy attribute of the RoleVO object
     *
     * @param updatedBy_  The new updatedBy value
     */
    public void setUpdatedBy(Integer updatedBy_)
    {
        this.updatedBy = updatedBy_;
    }

    /**
     * Gets the updatedBy attribute of the RoleVO object
     *
     * @return   The updatedBy value
     */
    public Integer getUpdatedBy()
    {
        return this.updatedBy;
    }

    /**
     * Sets the updatedDateTime attribute of the RoleVO object
     *
     * @param updatedDateTime_  The new updatedDateTime value
     */
    public void setUpdatedDateTime(Date updatedDateTime_)
    {
        this.updatedDateTime = updatedDateTime_;
    }

    /**
     * Gets the updatedDateTime attribute of the RoleVO object
     *
     * @return   The updatedDateTime value
     */
    public Date getUpdatedDateTime()
    {
        return this.updatedDateTime;
    }

    /**
     * Sets the activationStatus attribute of the RoleVO object
     *
     * @param activationStatus_  The new activationStatus value
     */
    public void setActivationStatus(String activationStatus_)
    {
        this.activationStatus = activationStatus_;
    }

    /**
     * Gets the activationStatus attribute of the RoleVO object
     *
     * @return   The activationStatus value
     */
    public String getActivationStatus()
    {
        return this.activationStatus;
    }

}
