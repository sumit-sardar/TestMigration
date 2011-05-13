package com.ctb.bean.testAdmin;


import com.ctb.bean.CTBBean;
import java.util.Date;


public class LicenseNodeData extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer orgNodeId = null;
    private Integer productId = null;
    private String reserved = null;
    private String consumed = null;
    private String available = null;
    private String subtestModel;
    private Integer customerId = null;
    
    public LicenseNodeData() {
    	this.orgNodeId = new Integer(0);
    	this.reserved = "0";
    	this.consumed = "0";
    	this.available = "0";   
    	this.subtestModel = "";
    }

    public LicenseNodeData(LicenseNodeData node) {
    	this.orgNodeId = node.getOrgNodeId();
    	this.reserved = node.getReserved();
    	this.consumed = node.getConsumed();
    	this.available = node.getAvailable();
    	this.subtestModel = node.getSubtestModel();
    }
    
   
   
    public Integer getProductId() {
        return this.productId;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
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
    
 public String getSubtestModel() {
		return subtestModel;
	}

	public void setSubtestModel(String subtestModel) {	
		if ("T".equalsIgnoreCase(subtestModel))
			this.subtestModel = "Subtest";
		else
			this.subtestModel = "Session";
	}

	
    //Added for Defect 59260 and 59262
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
    
    public void updateNode(LicenseNodeData node) {
    	this.reserved = node.getReserved();
    	this.consumed = node.getConsumed();
    	this.available = node.getAvailable();
    }

	

	/**
	 * @return the orgNodeId
	 */
	public Integer getOrgNodeId() {
		return orgNodeId;
	}

	/**
	 * @param orgNodeId the orgNodeId to set
	 */
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
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
