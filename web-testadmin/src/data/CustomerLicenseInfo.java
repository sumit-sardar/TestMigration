package data; 

public class CustomerLicenseInfo implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;

    private String userName = null;
    private Integer customerId = null;
    private int purchased = 0;
    private int reserved = 0;
    private int consumed = 0;
    private int inSession = 0;

    public CustomerLicenseInfo(Integer customerId, String userName) 
    {
        this.customerId = customerId;
        this.userName = userName;
        this.purchased = 0;
        this.reserved = 0;
        this.consumed = 0;
        this.inSession = 0;
    }

    public void getCustomerLicense()
    {
        // get licenses for this.customerId
        // for now set it to unlimited
        setPurchased(Integer.MAX_VALUE);
        setReserved(-1);
        setConsumed(-1);
    }
    
    public int getPurchased() {
        return this.purchased;
    }
    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }
    public int getReserved() {
        return this.reserved;
    }
    public void setReserved(int reserved) {
        this.reserved = reserved;
    }
    public int getConsumed() {
        return this.consumed;
    }
    public void setConsumed(int consumed) {
        this.consumed = consumed;
    }
    public int getInSession() {
        return this.inSession;
    }
    public void setInSession(int inSession) {
        this.inSession = inSession;
    }
    public void addInSession(int amount) {
        if (this.purchased == Integer.MAX_VALUE)
            return;
        this.inSession += amount;
    }
    public int getTotalReserved() {
        return (this.reserved + this.inSession);
    }    
    public int getAvailable() {
        if (this.purchased == Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
            
        int totalReserved = getTotalReserved();
        return this.purchased - (totalReserved + this.consumed);
    }
    public int getThresholdLimit() {
        int totalReserved = getTotalReserved();
        int spentAmount = totalReserved + this.consumed;
        int thresholdLimit = 100 - ((spentAmount * 100) / purchased);        
        return thresholdLimit;
    }
    public boolean isEnoughLicenses(int licenses) {
        if (this.purchased == Integer.MAX_VALUE)
            return true;
        int licenseLeft = this.purchased - (this.reserved + this.consumed);
        return (licenseLeft >= licenses);
    }

    public String getLicenseMessageOK() {
        return "The number of available licenses is " + String.valueOf(getAvailable());
    }
    
    public String getPercentThresholdLowMessage() {
        return "Less than 20% of purchased licenses remain available for scheduling. The available license percentage is " +
                                                                                String.valueOf(getThresholdLimit());
    }

    public String getNoLicenseMessage() {
        return "You have no licenses available for scheduling. Your purchased license quantity has been assigned or consumed.";
    }

    public String getNotEnoughLicenseMessage() {
        return "You have no licenses available for scheduling. Your purchased license quantity has been assigned or consumed." +
                                                            "The available license is " + String.valueOf(getAvailable());
    }

} 
