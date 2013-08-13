package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the partial contents of the OAS.statepr_code table 
 *  
 * @author Tata Consultency Services
 */

public class USState extends CTBBean { 
    static final long serialVersionUID = 1L;
    private String statePr;
    private String statePrDesc;
    
    /**
	* @return Returns the statePr.
	*/
    public String getStatePr() {
		return this.statePr;
	}
    /**
	 * @param statePr The statePr to set.
	 */
	public void setStatePr(String statePr) {
		this.statePr = statePr;
	}
    /**
	* @return Returns the statePrDesc.
	*/
    public String getStatePrDesc() {
		return this.statePrDesc;
	}
    /**
	 * @param statePrDesc The statePrDesc to set.
	 */
	public void setStatePrDesc(String statePrDesc) {
		this.statePrDesc = statePrDesc;
	}
} 
