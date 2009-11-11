package com.ctb.lexington.data;

/**
 *  UserSessionVO.java
 *
 *  Copyright CTB/McGraw-Hill, 2002
 *  CONFIDENTIAL
 *
 *  @author mclemens
 */


/**
 * @author <a href="mailto:Tai_Truong@ctb.com">Tai Truong</a>
 * @version
 * $Id$
 */

/**
this class still needs to be refactored and broken up into UserSessionDetailVO.
*/ 
public class UserSessionVO extends Object implements java.io.Serializable
{
    public static final String VO_LABEL = "com.ctb.lexington.data.UserSessionVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    private int userId = -1;
    private String userName = "";
    private String firstName = "";
    private String lastName  = "";
    private String preferredName = "";
    private String timezoneCode = "";
    private int customerId = -1;
    private String customerName = "";
    private String role = "";
    private int roleId = -1;
    private String displayUserName = "";
    /**
     *  Initialize any variable needed by this bean
     */
    public UserSessionVO()
    {
    }

    /**
     *  @return If the user id has&nbsp;
     *          not been set, this method will return a <CODE>-1</CODE>
     */
    public int getUserId()
    {
        return userId;
    }

    /**
     *  @param id_ Set the user id with parameter value.
     */
    public void setUserId(int id_)
    {
            userId = id_;

    }

    /**
     *  @return The value a user enters to log into the site.&nbsp; If the&nbsp;
     *          the user name has not be set, this method will return a blank string.
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     *  @param name_ The value a user enters to log into the site.
     */
    public void setUserName(String name_)
    {
        if(name_!=null)
        {
            userName = name_;
        }
    }
    
    /**
     *  @return If the first name has not be set, this method will return a blank string.
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     *  @param name_ The user's first name.
     */
    public void setFirstName(String name_)
    {
        if(name_!=null)
        {
            firstName = name_;
        }
    }
    
    /**
     *  @return If the preferred name has not be set, this method will return a blank string.
     */
    public String getPreferredName()
    {
        return preferredName;
    }

    /**
     *  @param name_ The user's preferred name.
     */
    public void setPreferredName(String name_)
    {
        if (name_!= null)
        {
            this.preferredName = name_.trim();
        }
    }
    
    /**
     *  @return If the last name has not be set, this method will return a blank string.
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     *  @param name_ The user's last name.
     */
    public void setLastName(String name_)
    {
        if(name_!=null)
        {
            lastName = name_;
        }
    }

    /**
     *  @return If the timezone code has not be set, this method will return a blank string.
     */
    public String getTimezoneCode()
    {
        return timezoneCode;
    }

    /**
     *  @param code_ The user's timezone code.
     */
    public void setTimezoneCode(String code_)
    {
        if(code_!=null)
        {
            timezoneCode = code_;
        }
    }
    

    /**
     *  @return If the customer id has&nbsp;
     *          not been set, this method will return a <CODE>-1</CODE>
     */
    public int getCustomerId()
    {
        return customerId;
    }

    /**
     *  @param id_ Set the customer id attribute with parameter value.
     */
    public void setCustomerId(int id_)
    {
            customerId = id_;
    }

    /**
     *  @return If the customer name has not be set, this method will return a blank string.
     */
    public String getCustomerName()
    {
        return customerName;
    }

    /**
     *  @param name_ Set the customer name attribute with parameter value.
     */
    public void setCustomerName(String name_)
    {
        if(name_!=null)
        {
            customerName = name_;
        }
    }
    
    /**
     *  @return If the customer name has not be set, this method will return a blank string.
     */
    public String getRoleName()
    {
        return role;
    }

    /**
     *  @param name_ Set the customer name attribute with parameter value.
     */
    public void setRoleName(String role_)
    {
        if(role_!=null)
        {    
            role = role_;
        }
    }
    
    /**
     *  @return If the customer name has not be set, this method will return a blank string.
     */
    public int getRoleId()
    {
        return roleId;
    }

    /**
     *  @param name_ Set the customer name attribute with parameter value.
     */
    public void setRoleId(int roleId_)
    {
            roleId = roleId_;
    }
    
    public void setDisplayUsername(String displayUsername)
    {
        if(displayUsername!=null)
        {
            this.displayUserName = displayUsername;
        }
    }
    
    public String getDisplayUsername()
    {
        return displayUserName;
    }
}
