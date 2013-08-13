package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the partial contents of the OAS.Time_Zone_Code table 
 *  
 * @author Tata Consultency Services
 */

public class TimeZones extends CTBBean { 
    
    static final long serialVersionUID = 1L;
    private String timeZone;
    private String timeZoneDesc;
    
    /**
	* @return Returns the timeZone.
	*/
    public String getTimeZone() {
		return this.timeZone;
	}
    /**
	 * @param timeZone The timeZone to set.
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
    /**
	* @return Returns the timeZoneDesc.
	*/
    public String getTimeZoneDesc() {
		return this.timeZoneDesc;
	}
    /**
	 * @param timeZoneDesc The timeZoneDesc to set.
	 */
	public void setTimeZoneDesc(String timeZoneDesc) {
		this.timeZoneDesc = timeZoneDesc;
	}
} 
