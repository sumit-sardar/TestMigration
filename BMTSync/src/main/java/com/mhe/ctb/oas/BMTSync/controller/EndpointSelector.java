package com.mhe.ctb.oas.BMTSync.controller;

public interface EndpointSelector {
	/**
	 * get an BMT endpoint for a given customer ID.
	 * @param customerId the customer ID.
	 * @return the BMT endpoint.
	 */
	String getEndpoint(final Integer customerId);
}
