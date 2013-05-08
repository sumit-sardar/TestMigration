package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * CustomerLicense class stores the license data of customer
 */
public class CustomerLicense extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer customerId ;         	 
	private Integer productId ;          
	private Integer available  ;          	  
	private Integer reservedLicense;             
	private Integer consumedLicense ;            
	private String subtestModel;        
	private Date licenseperiodStartdate ;
	private Date licenseperiodEnd;
    private Integer licenseAfterLastPurchase;
    private String productName;
    private boolean availableLicenseChange;
    private String customerName;
    private Boolean isLicenseAvailable; 
    private String emailNotify;
    private String orderNumber = null;
    private String purchaseOrder = null;
	private Integer index = null;
	private Integer orderIndex = null;


    /**
	 * @return the orderIndex
	 */
	public Integer getOrderIndex() {
		return orderIndex;
	}

	/**
	 * @param orderIndex the orderIndex to set
	 */
	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	//changes for LM12
    public CustomerLicense() {}
    
    public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
    
    public boolean getAvailableLicenseChange() {
        return availableLicenseChange;
    }
    
    
    public void setAvailableLicenseChange(boolean availableLicenseChange) {
        this.availableLicenseChange = availableLicenseChange;
    }
    
    /**
	 * @return the licenseAfterLastPurchase
	 */
    public Integer getLicenseAfterLastPurchase() {
		return licenseAfterLastPurchase;
	}
	
	/**
	 * @param licenseAfterLastPurchase the licenseAfterLastPurchase to set
	 */
	public void setLicenseAfterLastPurchase(Integer licenseAfterLastPurchase) {
		this.licenseAfterLastPurchase = licenseAfterLastPurchase;
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
	/**
	 * @return the productId
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	/**
	 * @return the available
	 */
	public Integer getAvailable() {
		return available;
	}
	/**
	 * @param available the available to set
	 */
	public void setAvailable(Integer available) {
		this.available = available;
	}
	/**
	 * @return the reservedLicense
	 */
	public Integer getReservedLicense() {
		return reservedLicense;
	}
	/**
	 * @param reservedLicense the reservedLicense to set
	 */
	public void setReservedLicense(Integer reservedLicense) {
		this.reservedLicense = reservedLicense;
	}
	/**
	 * @return the consumedLicense
	 */
	public Integer getConsumedLicense() {
		return consumedLicense;
	}
	/**
	 * @param consumedLicense the consumedLicense to set
	 */
	public void setConsumedLicense(Integer consumedLicense) {
		this.consumedLicense = consumedLicense;
	}
	/**
	 * @return the subtestModel
	 */
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
	 * @return the licenseperiodStartdate
	 */
	public Date getLicenseperiodStartdate() {
		return licenseperiodStartdate;
	}
	/**
	 * @param licenseperiodStartdate the licenseperiodStartdate to set
	 */
	public void setLicenseperiodStartdate(Date licenseperiodStartdate) {
		this.licenseperiodStartdate = licenseperiodStartdate;
	}
	/**
	 * @return the licenseperiodEnd
	 */
	public Date getLicenseperiodEnd() {
		return licenseperiodEnd;
	}
	/**
	 * @param licenseperiodEnd the licenseperiodEnd to set
	 */
	public void setLicenseperiodEnd(Date licenseperiodEnd) {
		this.licenseperiodEnd = licenseperiodEnd;
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

	
	/**
	 * @return the isLicenseAvailable
	 */
	public boolean isLicenseAvailable() {
		return this.isLicenseAvailable != null ? this.isLicenseAvailable : false;
	}

	
	/**
	 * @param isLicenseAvailable the isLicenseAvailable to set
	 */
	public void setIsLicenseAvailable(boolean isLicenseAvailable) {
		this.isLicenseAvailable = isLicenseAvailable;
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

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	
} 
