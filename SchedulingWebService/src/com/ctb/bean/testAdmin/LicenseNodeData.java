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
    	this.productId = new Integer(0);
    	this.customerId = new Integer(0);
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

	

	
    
   
   

	

	/**
 * @param subtestModel the subtestModel to set
 */
public void setSubtestModel(String subtestModel) {
	this.subtestModel = subtestModel;
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
