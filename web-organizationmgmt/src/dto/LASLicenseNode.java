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
	private Integer orderIndex = null;
	private Integer customerId = null;
    private String customerName = null;
    private Integer productId = null;
    private String productName = null;
	private String subtestModel;  
    private String emailNotify;
	
    private String orderNumber = null;
    private String licenseQuantity = null;
    private String purchaseOrder = null;
    private String purchaseDate = null;
    private String expiryDate = null;
    private Integer balanceLicense = null;

    private String expiryStatus = null;
    
    public LASLicenseNode() {    	
    	this.orderNumber = "";
    	this.licenseQuantity = "";
    	this.purchaseOrder = "";
    	this.purchaseDate = "";
    	this.expiryDate = "";
    	this.emailNotify = "T";
    	this.subtestModel = "T";
    }
    public LASLicenseNode(Integer customerId) {
    	this.customerId = customerId;
    	this.orderNumber = "";
    	this.licenseQuantity = "";
    	this.purchaseOrder = "";
    	this.purchaseDate = "";
    	this.expiryDate = "";
    }

    
	public Integer getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
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

	public String getSubtestModel() {
		return subtestModel;
	}
	
	public void setSubtestModel(String subtestModel) {
		this.subtestModel = subtestModel;
	}
	
	
	public String getEmailNotify() {
		return emailNotify;
	}
	public void setEmailNotify(String emailNotify) {
		this.emailNotify = emailNotify;
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

	public Integer getBalanceLicense() {
		return balanceLicense;
	}
	public void setBalanceLicense(Integer balanceLicense) {
		this.balanceLicense = balanceLicense;
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
	    
	
	public CustomerLicense makeCopy() 
	{
         CustomerLicense copied = new CustomerLicense();
       
         copied.setCustomerId(this.customerId);
         copied.setProductId(this.productId);
         copied.setSubtestModel(this.subtestModel);
         copied.setEmailNotify(this.emailNotify);
         
         if (this.licenseQuantity != null && this.licenseQuantity.length() > 0) {
        	 copied.setAvailable(Integer.valueOf(this.licenseQuantity.trim()));
             copied.setLicenseAfterLastPurchase(Integer.valueOf(this.licenseQuantity.trim()));
         }
         copied.setReservedLicense(new Integer(0));  
         copied.setConsumedLicense(new Integer(0));     
         
         copied.setLicenseperiodStartdate(DateUtils.getDateFromDateShortString(this.purchaseDate));
         copied.setLicenseperiodEnd(DateUtils.getDateFromDateShortString(this.expiryDate));

         copied.setOrderIndex(this.orderIndex);
         copied.setOrderNumber(this.orderNumber);
         copied.setPurchaseOrder(this.purchaseOrder);
         
        return copied;       
    }
     
	public void initData(CustomerLicense src) 
	{
         this.customerId = src.getCustomerId();
         this.customerName = src.getCustomerName();
         this.productId = src.getProductId();
         this.productName = src.getProductName();
    }
    
} 
