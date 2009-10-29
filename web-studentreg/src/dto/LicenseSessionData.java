package dto; 

public class LicenseSessionData implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;

    private Integer id = null;
    private Integer reserved = null;
    private Integer consumed = null;
    private Integer available = null;
    private String licenseDisplayBarColour = null;
    private double availableLicPercent;
    private Integer licenseAfterLastPurchase;
   
    public LicenseSessionData() {}
    
     
    public Integer getLicenseAfterLastPurchase() {
		return licenseAfterLastPurchase;
	}
	public void setLicenseAfterLastPurchase(Integer licenseAfterLastPurchase) {
		this.licenseAfterLastPurchase = licenseAfterLastPurchase;
	}
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getReserved() {
        return this.reserved;
    }
    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }
    public Integer getConsumed() {
        return this.consumed;
    }
    public void setConsumed(Integer consumed) {
        this.consumed = consumed;
    }
    public Integer getAvailable() {
        return this.available;
    }
    public void setAvailable(Integer available) {
        this.available = available;
    }
   
    public String getPercentageAvailable() {
       
               
        double totalLicense = licenseAfterLastPurchase.doubleValue();
        this.availableLicPercent = Math.round(((this.available.doubleValue()*100)/ totalLicense ));
        //For defect #59054   
        this.availableLicPercent = this.availableLicPercent > 
                Message.MAX_LICENSE_PERCENT ? Message.MAX_LICENSE_PERCENT : 
                    this.availableLicPercent;
        this.availableLicPercent = this.availableLicPercent <
                Message.MIN_LICENSE_PERCENT ? Message.MIN_LICENSE_PERCENT : 
                    this.availableLicPercent;        
      
        String availableLicensePercent = 
                "Available: "+ this.available + " ("+ new Double(availableLicPercent).intValue() + "%)";      
        return availableLicensePercent;
    }
    
     public String getNoLicenseMessage() {
        return "There are insufficient licenses available to schedule the number of tests requested.";
    }
    
    public String getLicenseDisplayBarColour() {
        
        getPercentageAvailable();
        Integer availableLicensePercentage = new Integer
                (new Double(availableLicPercent).intValue());
        
        if (availableLicensePercentage.intValue() < Message.MIN_LICENSE ) {
             
                    licenseDisplayBarColour = Message.LOW_LICENSE_COLOR;
                    
        } else if (availableLicensePercentage.intValue() < Message.MAX_LICENSE && 
                        availableLicensePercentage.intValue() >= Message.MIN_LICENSE )  {
                       
                       licenseDisplayBarColour =  Message.MEDIUM_LICENSE_COLOR;     
                    
        } else if (availableLicensePercentage.intValue() >= Message.MAX_LICENSE ) {
                    
                    licenseDisplayBarColour = Message.HIGH_LICENSE_COLOR;
        }
                    
              
        return licenseDisplayBarColour;
        
    }
    
   

} 
