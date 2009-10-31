package dto; 

/*
 * @author Tata Consultancy Services
 * Deals with customer billing & mailing contact details
 * CustomerContactInformation class is used to contain Customer Contact Information.
*/

import com.ctb.bean.testAdmin.Address;
import java.util.StringTokenizer;

public class CustomerContactInformation implements java.io.Serializable{ 
    static final long serialVersionUID = 1L;
    
    private Integer addressId = new Integer(0);
    private String addressLine1 = "";
    private String addressLine2 = "";
    private String addressLine3 = "";
    private String city = "";
    private String state = "";
    private String stateDesc = "";
    
    private String zipCode = "";
    private String zipCode1 = "";
    private String zipCode2 = "";
    
   
    public CustomerContactInformation() {    
    }
            
    public CustomerContactInformation(Integer addressId, Address customerAddress) {   
        
        if ( addressId == null || customerAddress == null ) {
            
            return;
            
        }

        this.addressId = addressId;         
        this.addressLine1 = customerAddress.getAddressLine1();
        this.addressLine2 = customerAddress.getAddressLine2();
        this.addressLine3 = customerAddress.getAddressLine3();
        this.city = customerAddress.getCity();
        this.state = customerAddress.getStatePr();
        this.stateDesc = customerAddress.getStateDesc();
        
        String zipCode = customerAddress.getZipCode();
        if (zipCode  == null || "".equals(zipCode)) {
            
            this.zipCode1 = "";
            this.zipCode2 = "";
            
        } else {
            StringTokenizer stoke=new StringTokenizer(zipCode," -");
            int nextItem=0;
            while(stoke.hasMoreTokens()){
                if(nextItem == 0){
                    this.zipCode1 = stoke.nextToken();
                }
                else if(nextItem == 1){
                    this.zipCode2 = stoke.nextToken();
                }
                nextItem++;
            }
        }
        
      /*  if ( customerAddress.getZipCodeExt() == null ) {
            
            this.zipCode2 = "";
            
        } else {
                
            this.zipCode2 = customerAddress.getZipCodeExt();
            
        }*/
            
        if ( zipCode2.length() > 0 ) {
            
             this.zipCode = ( this.zipCode1 +  "-"  + this.zipCode2 );
              
        } else {
            
             this.zipCode = this.zipCode1;
             
        }  
                
    }
        
	public Integer getAddressId() {
		return this.addressId;
	}
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
  
	public String getAddressLine1() {
		return this.addressLine1 != null ? this.addressLine1.trim() : "";
	}
	public void setAddressLine1(String addressLine) {
		this.addressLine1 = addressLine;
	}
    
	public String getAddressLine2() {
		return this.addressLine2 != null ? this.addressLine2.trim() : "";
	}
	public void setAddressLine2(String addressLine) {
		this.addressLine2 = addressLine;
	}
    
    public String getAddressLine3() {
		return this.addressLine3 != null ? this.addressLine3.trim() : "";
	}
	public void setAddressLine3(String addressLine) {
		this.addressLine3 = addressLine;
	}
    
	public String getCity() {
		return this.city != null ? this.city.trim() : "";
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return this.state != null ? this.state.trim() : "";
	}
	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return this.zipCode != null ? this.zipCode.trim() : "";
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getZipCode1() {
		return this.zipCode1 != null ? this.zipCode1.trim() : "";
	}
	public void setZipCode1(String zipCode) {
		this.zipCode1 = zipCode;
	}
	public String getZipCode2() {
		return this.zipCode2 != null ? this.zipCode2.trim() : "";
	}
	public void setZipCode2(String zipCode) {
		this.zipCode2 = zipCode;
	}

	/**
	 * @return Returns the stateDesc.
	 */
	public String getStateDesc() {
		return this.stateDesc ;
	}
    /**
	 * @param stateDesc The stateDesc to set.
	 */
	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}
    
    public boolean isEmpty() {
        
        if ( ( addressId == null || addressId.intValue() == 0 )
                    && ( addressLine1 == null || "".equals(addressLine1) )
                    && ( addressLine2 == null || "".equals(addressLine2) )
                    && ( addressLine3 == null || "".equals(addressLine3) )
                    && ( city == null || "".equals(city) )
                    && ( state == null || "".equals(state) 
                            || "Select a state".equalsIgnoreCase(state) )
                    && ( zipCode1 == null || "".equals(zipCode1) ) ) {
                                                
            return true;
            
        }
        
        return false;
    }
    
    
      
} 
