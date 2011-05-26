package com.ctb.tms.bean.login.response.testingSessionData; 

import com.ctb.tms.bean.login.response.testingSessionData.accomodations.Accomodations;
import com.ctb.tms.bean.login.response.testingSessionData.core.Core;

public class TestingSessionData 
{ 
    private Core core;
    private Accomodations accomodations;
    
    /**
	 * @return Returns the accomodations.
	 */
	public Accomodations getAccomodations() {
		return accomodations;
	}
	/**
	 * @param accomodations The accomodations to set.
	 */
	public void setAccomodations(Accomodations accomodations) {
		this.accomodations = accomodations;
	}
	/**
	 * @return Returns the core.
	 */
	public Core getCore() {
		return core;
	}
	/**
	 * @param core The core to set.
	 */
	public void setCore(Core core) {
		this.core = core;
	}
} 
