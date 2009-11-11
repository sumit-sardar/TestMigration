package com.ctb.lexington.data;

/*
 * SubscriptionVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

import java.util.Date;

/**
 *
 * @author  KBletzer
 * @version
 * $Id$
 */

public class SubscriptionVO  implements java.io.Serializable
{
    public static final String VO_LABEL = "com.ctb.lexington.data.SubscriptionVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    //DATA fields
    private String subscriptionId;
    private String customerId;
    private String productId;
    private String productName;
    private String purchaseQuantity;
    private Date   startDate;
    private Date   endDate;
    private String internalDisplayName;

    //BUSINESS fields --> to be moved to DetailVO, eventually
    private String organizationName;
    private String usedQuantity;
    private String rootOrganizationNodeId;
    private String rootOrganizationNodeName;

    /** Creates new SubscriptionVO */
    public SubscriptionVO() {
    }


    //----------------------------------------------------------------------------
    // DATA fields Set/Get methods.
    //----------------------------------------------------------------------------

    public String getSubscriptionId(){ return this.subscriptionId;  }
    public void setSubscriptionId(String subscriptionId_){
        this.subscriptionId = subscriptionId_;
    }

    public String getCustomerId(){ return this.customerId; }
    public void setCustomerId(String customerId_){
        this.customerId = customerId_;
    }

    public String getProductId( ){ return this.productId; }
    public void setProductId(String productID_){
        this.productId = productID_;
    }

    public String getProductName(){ return this.productName; }
    public void setProductName(String productName_){
        this.productName=productName_;
    }

    public String getPurchaseQuantity(){ return this.purchaseQuantity; }
    public void setPurchaseQuantity(String purchaseQuantity_){
        this.purchaseQuantity = purchaseQuantity_;
    }
    
    public String getPurchasedQty(){ return this.purchaseQuantity; }
    public void setPurchasedQty(String purchaseQuantity_){
        this.purchaseQuantity = purchaseQuantity_;
    }
    
    public Date getStartDate(){ return this.startDate; }
    public void setStartDate(Date startDate_){
        this.startDate=startDate_;
    }

    public Date getEndDate(){ return this.endDate; }
    public void setEndDate(Date endDate_){
        this.endDate=endDate_;
    }


    //--------------------------------------------------------------------------
    // BUSINESS Set/Get methods
    //--------------------------------------------------------------------------

    public String getOrganizationName(){ return this.organizationName; }
    public void setOrganizationName(String organizationName_){
        this.organizationName = organizationName_;
    }

    public String getRootOrganizationNodeId(){ return this.rootOrganizationNodeId; }
    public void setRootOrganizationNodeId(String rootId){
        this.rootOrganizationNodeId=rootId;
    }

    public String getRootOrganizationNodeName(){ return this.rootOrganizationNodeName; }
    public void setRootOrganizationNodeName(String rootName){
        this.rootOrganizationNodeName=rootName;
    }

    public String getUsedQuantity(){ return this.usedQuantity; }
    public void setUsedQuantity(String usedQuantity_){
        this.usedQuantity=usedQuantity_;
    }

	/**
	 * Returns the internalDisplayName.
	 * @return String
	 */
	public String getInternalDisplayName()
	{
		return internalDisplayName;
	}

	/**
	 * Sets the internalDisplayName.
	 * @param internalDisplayName The internalDisplayName to set
	 */
	public void setInternalDisplayName(String internalDisplayName)
	{
		this.internalDisplayName = internalDisplayName;
	}

}
/*
 * $Log$
 * Revision 1.1  2007/01/30 01:31:41  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:47:39  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.4  2003/04/07 22:49:16  oasuser
 * iknowR2v5 to trunk
 *
 * Revision 1.3.2.2  2003/04/02 22:37:12  wchang
 * add purchaseQty
 *
 * Revision 1.3.2.1  2003/04/02 04:00:57  sprakash
 * added internalDisplayName and getters and setters in SUbscription EJB
 *
 * Revision 1.3  2003/02/05 19:28:05  oasuser
 * merged from OASR3tmp to trunk
 *
 * Revision 1.2  2003/01/31 04:03:31  oasuser
 * merged from OASR3tmp to trunk.  Code not compiling, so java will be re-merged at a later date.
 *
 * Revision 1.1.2.3  2003/01/31 20:32:12  jshields
 * Updates for AddressVO, UserSessionVO, UserVO, and UserDetailVO.
 * Minor update to RoleBean.
 * Minor updates to AuthenticationVO.
 *
 * Revision 1.1.2.2  2003/01/29 05:13:56  kgawetsk
 * Refactoring - SubscriptionEJB related changes, and code reformatting.
 *
 * Revision 1.1.2.1  2003/01/29 04:09:48  kgawetsk
 * refactoring for SubscriptionVO.
 *
 */
