package dto; 


import com.ctb.bean.testAdmin.Address;
import utils.StringUtils;

/**
 * 
 */
public class UserContactInformation implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;
    
    private Integer addressId = new Integer(0);
    private String addressLine1 = "";
    private String addressLine2 = "";
    private String city = "";
    private String state = "";
    private String stateDesc = "";
    
    private String zipCode = "";
    private String zipCode1 = "";
    private String zipCode2 = "";
    
    private String primaryPhone = "";
    private String primaryPhone1 = "";
    private String primaryPhone2 = "";
    private String primaryPhone3 = "";
    private String primaryPhone4 = "";
    
    private String secondaryPhone = "";
    private String secondaryPhone1 = "";
    private String secondaryPhone2 = "";
    private String secondaryPhone3 = "";
    private String secondaryPhone4 = "";
    
    private String faxNumber = "";
    private String faxNumber1 = "";
    private String faxNumber2 = "";
    private String faxNumber3 = "";

    public UserContactInformation() {    
    }
            
    public UserContactInformation(Integer addressId, Address userAddress) {   
        
        if (addressId == null || userAddress == null) {
            return;
        }

        this.addressId = addressId;         
        this.addressLine1 = userAddress.getAddressLine1();
        this.addressLine2 = userAddress.getAddressLine2();
        this.city = userAddress.getCity();
        this.state = userAddress.getStatePr();
        this.stateDesc = userAddress.getStateDesc();
        
        //Split Fax Number for Edit
        if (userAddress.getFaxNumber() != null && !"".equals(userAddress.getFaxNumber())){
            String[] fax = StringUtils.splitPhoneNumberAndFaxNumber(userAddress.getFaxNumber(),false);
            this.faxNumber1 = fax[2];
            this.faxNumber2 = fax[1];
            this.faxNumber3 = fax[0];
        }
        this.faxNumber = userAddress.getFaxNumber();
        
        //Split Primary Phone for Edit
        if (userAddress.getPrimaryPhone() != null && !"".equals(userAddress.getPrimaryPhone())){
            String[] primaryPhone = StringUtils.splitPhoneNumberAndFaxNumber(userAddress.getPrimaryPhone(),true);
            this.primaryPhone1 = primaryPhone[3];
            this.primaryPhone2 = primaryPhone[2];
            this.primaryPhone3 = primaryPhone[1];
            this.primaryPhone4 = primaryPhone[0];
            String primaryPh = userAddress.getPrimaryPhone();
            primaryPh = primaryPh.replaceAll("x"," Ext: ");
            this.primaryPhone = primaryPh;
        }
       
        
        // Split Secondary Phone for Edit
        if(userAddress.getSecondaryPhone() != null && !"".equals(userAddress.getSecondaryPhone())){
            String[] secondaryPhone = StringUtils.splitPhoneNumberAndFaxNumber(userAddress.getSecondaryPhone(),true);
            this.secondaryPhone1 = secondaryPhone[3];
            this.secondaryPhone2 = secondaryPhone[2];
            this.secondaryPhone3 = secondaryPhone[1];
            this.secondaryPhone4 = secondaryPhone[0];
            String secondaryPh = userAddress.getSecondaryPhone();
            secondaryPh = secondaryPh.replaceAll("x"," Ext: ");
            this.secondaryPhone = secondaryPh;
      
        }
        
        if(userAddress.getZipCode() == null)
            this.zipCode1 = "";
        else  
            this.zipCode1 = userAddress.getZipCode();
        if(userAddress.getZipCodeExt() == null)    
            this.zipCode2 = "";
        else    
            this.zipCode2 = userAddress.getZipCodeExt();
            
        if(zipCode2.length() > 0) {
             this.zipCode = (this.zipCode1 +"-"+ this.zipCode2); 
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

	public String getPrimaryPhone() {
		return this.primaryPhone != null ? this.primaryPhone.trim() : "";
	}
	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}
	public String getPrimaryPhone1() {
		return this.primaryPhone1 != null ? this.primaryPhone1.trim() : "";
	}
	public void setPrimaryPhone1(String primaryPhone) {
		this.primaryPhone1 = primaryPhone;
	}
	public String getPrimaryPhone2() {
		return this.primaryPhone2 != null ? this.primaryPhone2.trim() : "";
	}
	public void setPrimaryPhone2(String primaryPhone) {
		this.primaryPhone2 = primaryPhone;
	}
	public String getPrimaryPhone3() {
		return this.primaryPhone3 != null ? this.primaryPhone3.trim() : "";
	}
	public void setPrimaryPhone3(String primaryPhone) {
		this.primaryPhone3 = primaryPhone;
	}
	public String getPrimaryPhone4() {
		return this.primaryPhone4 != null ? this.primaryPhone4.trim() : "";
	}
	public void setPrimaryPhone4(String primaryPhone) {
		this.primaryPhone4 = primaryPhone;
	}

	public String getSecondaryPhone() {
		return this.secondaryPhone != null ? this.secondaryPhone.trim() : "";
	}
	public void setSecondaryPhone(String secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}
	public String getSecondaryPhone1() {
		return this.secondaryPhone1 != null ? this.secondaryPhone1.trim() : "";
	}
	public void setSecondaryPhone1(String secondaryPhone) {
		this.secondaryPhone1 = secondaryPhone;
	}
	public String getSecondaryPhone2() {
		return this.secondaryPhone2 != null ? this.secondaryPhone2.trim() : "";
	}
	public void setSecondaryPhone2(String secondaryPhone) {
		this.secondaryPhone2 = secondaryPhone;
	}
	public String getSecondaryPhone3() {
		return this.secondaryPhone3 != null ? this.secondaryPhone3.trim() : "";
	}
	public void setSecondaryPhone3(String secondaryPhone) {
		this.secondaryPhone3 = secondaryPhone;
	}
	public String getSecondaryPhone4() {
		return this.secondaryPhone4 != null ? this.secondaryPhone4.trim() : "";
	}
	public void setSecondaryPhone4(String secondaryPhone) {
		this.secondaryPhone4 = secondaryPhone;
	}

	public String getFaxNumber() {
		return this.faxNumber != null ? this.faxNumber.trim() : "";
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getFaxNumber1() {
		return this.faxNumber1 != null ? this.faxNumber1.trim() : "";
	}
	public void setFaxNumber1(String faxNumber) {
		this.faxNumber1 = faxNumber;
	}
	public String getFaxNumber2() {
		return this.faxNumber2 != null ? this.faxNumber2.trim() : "";
	}
	public void setFaxNumber2(String faxNumber) {
		this.faxNumber2 = faxNumber;
	}
	public String getFaxNumber3() {
		return this.faxNumber3 != null ? this.faxNumber3.trim() : "";
	}
	public void setFaxNumber3(String faxNumber) {
		this.faxNumber3 = faxNumber;
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
        if ((addressId == null || addressId.intValue() == 0)
                    && (addressLine1 == null || "".equals(addressLine1))
                    && (addressLine2 == null || "".equals(addressLine2))
                    && (city == null || "".equals(city))
                    && (state == null || "".equals(state) 
                            || "Select a state".equalsIgnoreCase(state))
                    && (zipCode1 == null || "".equals(zipCode1))
                    && (primaryPhone == null || "".equals(primaryPhone))
                    && (secondaryPhone == null || "".equals(secondaryPhone))
                    && (faxNumber == null || "".equals(faxNumber))) {
            return true;
        }
        return false;
    }
    
    public void setPrimaryPhoneFromOthers() {
        String phone1 = "";
        String phone2 = "";
        String phone3 = "";
        String phone4 = "";
        if (primaryPhone1 != null && !"".equals(primaryPhone1)){
            phone1 = "(" + primaryPhone1 + ") ";
        }
        if (primaryPhone2 != null && !"".equals(primaryPhone2)){
            phone2 = primaryPhone2;
        }
        if (primaryPhone3 != null && !"".equals(primaryPhone3)){
            phone3 = "-" + primaryPhone3;
        }
        if (primaryPhone4 != null && !"".equals(primaryPhone4)){
            phone4 = " x " + primaryPhone4;
        }
        setPrimaryPhone(phone1+phone2+phone3+phone4);
    }
    
    public void setSecondaryPhoneFromOthers() {
        String phone1 = "";
        String phone2 = "";
        String phone3 = "";
        String phone4 = "";
        if (secondaryPhone1 != null && !"".equals(secondaryPhone1)){
            phone1 = "(" + secondaryPhone1 + ") ";
        }
        if (secondaryPhone2 != null && !"".equals(secondaryPhone2)){
            phone2 = secondaryPhone2;
        }
        if (secondaryPhone3 != null && !"".equals(secondaryPhone3)){
            phone3 = "-" + secondaryPhone3;
        }
        if (secondaryPhone4 != null && !"".equals(secondaryPhone4)){
            phone4 = " x " + secondaryPhone4;
        }
        setSecondaryPhone(phone1+phone2+phone3+phone4);
    }
    
    public void setFaxFromOthers() {
        String fax1 = "";
        String fax2 = "";
        String fax3 = "";
        
        if (faxNumber1 != null && !"".equals(faxNumber1)){
            fax1 = "(" + faxNumber1 + ") ";
        }
         if(faxNumber2 != null && !"".equals(faxNumber2)){
            fax2 = faxNumber2;
        }
         if(faxNumber3 != null && !"".equals(faxNumber3)){
            fax3 = "-" + faxNumber3;
        }
        setFaxNumber(fax1+fax2+fax3);

    }
    
} 
