package com.ctb.lexington.data;
import java.io.Serializable;

/**
 * @author    cthomas
 * @created   December 10, 2002
 * @version
 */
public class OrganizationVO implements Serializable
{

    /**
     * Description of the Field
     */
    public final static String BEAN_LABEL = "com.ctb.lexington.data.OrganizationVO";
    
    /**
     * Description of the Field
     */
    public final static String BEAN_ARRAY_LABEL = BEAN_LABEL + ".array";

    private String customerId;

    private String customerName;

    private String customerState;

    private boolean hasFramework;

    /**
     * Creates new OrganizationVO
     */
    public OrganizationVO() { }

    /**
     * Gets the customerId attribute of the OrganizationVO object
     *
     * @return   The customerId value
     */
    public String getCustomerId()
    {
        return this.customerId;
    }


    /**
     * Gets the customerName attribute of the OrganizationVO object
     *
     * @return   The customerName value
     */
    public String getCustomerName()
    {
        return this.customerName;
    }


    /**
     * Gets the customerState attribute of the OrganizationVO object
     *
     * @return   The customerState value
     */
    public String getCustomerState()
    {
        return this.customerState;
    }


    /**
     * Gets the hasFramework attribute of the OrganizationVO object
     *
     * @return   The hasFramework value
     */
    public boolean getHasFramework()
    {
        return this.hasFramework;
    }


    /**
     * Sets the customerId attribute of the OrganizationVO object
     *
     * @param customerID_  The new customerId value
     */
    public void setCustomerId(String customerID_)
    {
        this.customerId = customerID_;
    }


    /**
     * Sets the customerName attribute of the OrganizationVO object
     *
     * @param customerName_  The new customerName value
     */
    public void setCustomerName(String customerName_)
    {
        this.customerName = customerName_;
    }


    /**
     * Sets the customerState attribute of the OrganizationVO object
     *
     * @param customerState_  The new customerState value
     */
    public void setCustomerState(String customerState_)
    {
        this.customerState = customerState_;
    }


    /**
     * Sets the hasFramework attribute of the OrganizationVO object
     *
     * @param hasFramework_  The new hasFramework value
     */
    public void setHasFramework(boolean hasFramework_)
    {
        this.hasFramework = hasFramework_;
    }
}

