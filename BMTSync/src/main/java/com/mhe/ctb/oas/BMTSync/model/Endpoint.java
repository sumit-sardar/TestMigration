package com.mhe.ctb.oas.BMTSync.model;

/** A nested class describing an endpoint, only used inside the DAO to map customer ID to URI. */
public class Endpoint {
	private Integer customerId;
	private String endpoint;
	
	public void setCustomerId(final Integer customerId) {
		this.customerId = customerId;
	}
	
	public Integer getCustomerId() {
		return customerId;
	}
	
	public void setEndpoint(final String endpoint) {
		this.endpoint = endpoint;
	}
	
	public String getEndpoint() {
		return endpoint;
	}
}