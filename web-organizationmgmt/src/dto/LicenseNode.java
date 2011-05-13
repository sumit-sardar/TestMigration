package dto; 

import manageCustomer.ManageCustomerController.ManageCustomerForm;

import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.LicenseNodeData;

public class LicenseNode implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    private Integer id = null;
    private Integer productId = null;
    private String productName = null;
    private String name = null;
    private Integer childrenNodeCount = null;
    private String categoryName = null;
    private String fullPathName = null;
    //Change for Defect 59260 and 59262
    private String reserved = null;
    private String consumed = null;
    private String available = null;
    private String licenseAfterLastPurchase = null;
	private String subtestModel;  
	private Integer customerId = null;
    
    public LicenseNode() {
    	this.id = new Integer(0);
    	this.name = "Top";
    	this.reserved = "0";
    	this.consumed = "0";
    	this.available = "0";   
    	this.subtestModel = "";
    	this.customerId =  new Integer(0);
    	this.productId = new Integer(0);
    }

    public LicenseNode(LicenseNode node) {
    	this.id = node.getId();
    	this.name = node.getName();
    	this.subtestModel = node.getSubtestModel();
    	this.productId = node.getProductId();
    	this.reserved = node.getReserved();
    	this.consumed = node.getConsumed();
    	this.available = node.getAvailable();
    	this.subtestModel = node.getSubtestModel();
    	this.customerId = node.getCustomerId();
    	this.productId = node.getProductId();
    }
    
    /**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

    
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getProductId() {
        return this.productId;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getReserved() {
        return this.reserved;
    }
    public void setReserved(String reserved) {
        this.reserved = reserved;
    }
    public String getConsumed() {
        return this.consumed;
    }
    public void setConsumed(String consumed) {
        this.consumed = consumed;
    }
    public String getAvailable() {
        return this.available;
    }
    public void setAvailable(String available) {
        this.available = available;
    }
    public Integer getChildrenNodeCount() {
        return this.childrenNodeCount;
    }
    public void setChildrenNodeCount(Integer childrenNodeCount) {
        this.childrenNodeCount = childrenNodeCount;
    }
    public String getHasChildren() {
        
        if ( ( this.childrenNodeCount != null) 
                && ( this.childrenNodeCount.intValue() > 0 ) ) {
             
            return "true";
            
        }
            
        else {
            
            return "false";
            
        }
    }
    public String getCategoryName() {
        return this.categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getFullPathName() {
        return this.fullPathName;
    }
    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }
    
    public String getLicenseAfterLastPurchase() {
		return licenseAfterLastPurchase;
	}

	public void setLicenseAfterLastPurchase(String licenseAfterLastPurchase) {
		this.licenseAfterLastPurchase = licenseAfterLastPurchase;
	}

     public String getSubtestModel() {
		return subtestModel;
	}

	public void setSubtestModel(String subtestModel) {	
		if ("T".equalsIgnoreCase(subtestModel))
			this.subtestModel = "Subtest";
		else
			this.subtestModel = "Session";
	}

	/**
     * makeCopy
     */
   public CustomerLicense makeCopy(ManageCustomerForm form) 
   {
        CustomerLicense copied = new CustomerLicense();
       
         //customer Information\
        if (this.licenseAfterLastPurchase == null 
            || (this.licenseAfterLastPurchase.equals(this.available))) {
            
             copied.setAvailableLicenseChange(false);
            
        } else {
            
           copied.setAvailableLicenseChange(true);
            
         }
           
         copied.setCustomerId(form.getSelectedCustomerId());
         copied.setProductId(this.productId);
         //license Information
         copied.setAvailable(ConvertStringToInteger(this.available.trim()));
         String reservedT = (this.reserved != null && !"".equals(this.reserved.
                trim()))? this.reserved : new String ("0");
         copied.setReservedLicense(ConvertStringToInteger(reservedT.trim()));  
         //Changed for Defect 59260 and 59262
         String consumedT = this.consumed != null && !("".equals(this.consumed.
                trim())) ? this.consumed :  new String ("0");
         copied.setConsumedLicense(ConvertStringToInteger(consumedT.trim()));     
                
        return copied;       
    }
    
    //Added for Defect 59260 and 59262
   
   
   
   
   /**
    * makeCopy
    */
  public LicenseNodeData[] makelicenseNodeCopy(LicenseNode [] licenseNode) 
  {
	  	LicenseNodeData [] copied = new LicenseNodeData[licenseNode.length];
	  	
        for(int i= 0; i < licenseNode.length ; i++) {
        	LicenseNodeData  copiedData = new LicenseNodeData();
        	copiedData.setAvailable(licenseNode[i].getAvailable());
        	copiedData.setConsumed(licenseNode[i].getConsumed());
        	copiedData.setCustomerId(licenseNode[i].getCustomerId());
        	copiedData.setOrgNodeId(licenseNode[i].getId());
        	copiedData.setProductId(licenseNode[i].getProductId());
        	copiedData.setReserved(licenseNode[i].getReserved());
        	copiedData.setSubtestModel(licenseNode[i].getSubtestModel());
        	copied[i] = copiedData;
        	
        }     
       return copied;       
   }
   
     /**
     * This method is responsible for converting String to Integer
     * @param value
     * @return Integer
     */
    
    public Integer ConvertStringToInteger (String value) {
        
        
        return Integer.valueOf(value);
        
    } 
    
    /**
     * This method is responsible for converting Integer to String
     */
    
    public String ConvertIntegerToString (Integer value) {
        
        if (value != null) {
            
            return value.toString();
            
        } else {
          
            return null;   
        }
        
    }
    
    public void updateNode(LicenseNode node) {
    	this.reserved = node.getReserved();
    	this.consumed = node.getConsumed();
    	this.available = node.getAvailable();
    }

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
    
    
} 
