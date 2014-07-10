package com.ctb.bean; 

/**
 * Data bean representing the contents of the OAS.CUSTOMER_DEMOGRAPHIC_VALUE table 
 * and student selection on demographic values
 * 
 * @author Tata Consultency Services
 */



public class StudentDemographicValue extends CustomerDemographicValue
{ 
    static final long serialVersionUID = 1L;
    private String selectedFlag;

     public StudentDemographicValue() {
        super();
     }
    
	/**
	 * @return the selectedFlag
	 */
	public String getSelectedFlag() {
		return selectedFlag;
	}
	/**
	 * @param selectedFlag the selectedFlag to set
	 */
	public void setSelectedFlag(String selectedFlag) {
		this.selectedFlag = selectedFlag;
	}
    
    
    
} 
