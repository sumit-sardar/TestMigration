package com.ctb.util.jmsutils;

import java.io.Serializable;

public class ExportDataVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer CustomerId ;

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return CustomerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		CustomerId = customerId;
	}

	

}
