package dto; 

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import utils.DateUtils;

import manageCustomer.ManageCustomerController.ManageCustomerForm;

import com.ctb.bean.testAdmin.CustomerLicense;


public class LASLicenseNode implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
	private Integer index = null;
	private Integer customerId = null;
    private String customerName = null;
    private Integer productId = null;
    private String productName = null;
	
    private String orderNumber = null;
    private String licenseQuantity = null;
    private String purchaseOrder = null;
    private String purchaseDate = null;
    private String expiryDate = null;

    private String expiryStatus = null;
    
    public LASLicenseNode() {    	
    	this.orderNumber = "";
    	this.licenseQuantity = "";
    	this.purchaseOrder = "";
    	this.purchaseDate = "";
    	this.expiryDate = "";
    }
    public LASLicenseNode(Integer customerId) {
    	this.customerId = customerId;
    	this.orderNumber = "";
    	this.licenseQuantity = "";
    	this.purchaseOrder = "";
    	this.purchaseDate = "";
    	this.expiryDate = "";
    }

	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getCustomerId() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getLicenseQuantity() {
		return licenseQuantity;
	}

	public void setLicenseQuantity(String licenseQuantity) {
		this.licenseQuantity = licenseQuantity;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getExpiryStatus() {
        Date expDate = DateUtils.getDateFromDateShortString(this.expiryDate);
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, 30);
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date today60 = cal.getTime();
        
//System.out.println("today60 = " + today60.toString());
        
        if (DateUtils.isBeforeToday(expDate, TimeZone.getDefault().getID())) {
        	this.expiryStatus = "EXPIRED";
        } 
        else {
            if (expDate.compareTo(today60) < 0) {
	        	this.expiryStatus = "ABOUT_EXPIRED";        		        	
	        }
	        else {
	        	this.expiryStatus = "VALID";        	
	        }
        }
		return expiryStatus;
	}
	
	public void setExpiryStatus(String expiryStatus) {
		this.expiryStatus = expiryStatus;
	}
	    
	
	public CustomerLicense makeCopy(Integer customerId, Integer productId, String available) 
	{
         CustomerLicense copied = new CustomerLicense();
       
         copied.setAvailableLicenseChange(true);
         copied.setCustomerId(customerId);
         copied.setProductId(productId);
       
         if (available != null && available.length() > 0)
        	 copied.setAvailable(Integer.valueOf(available.trim()));
         copied.setReservedLicense(new Integer(0));  
         copied.setConsumedLicense(new Integer(0));     
                
        return copied;       
    }
     
    
} 
