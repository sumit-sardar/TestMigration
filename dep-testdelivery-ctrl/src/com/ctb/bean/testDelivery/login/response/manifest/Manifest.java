package com.ctb.bean.testDelivery.login.response.manifest; 

import com.ctb.bean.testDelivery.login.response.manifest.terminator.Terminator;
import java.util.List;

public class Manifest 
{ 
    private List scos;
    private Terminator terminator;
    
    /**
	 * @return Returns the scos.
	 */
	public List getScos() {
		return scos;
	}
	/**
	 * @param scos The scos to set.
	 */
	public void setScos(List scos) {
		this.scos = scos;
	}
	/**
	 * @return Returns the terminator.
	 */
	public Terminator getTerminator() {
		return terminator;
	}
	/**
	 * @param terminator The terminator to set.
	 */
	public void setTerminator(Terminator terminator) {
		this.terminator = terminator;
	}
} 
