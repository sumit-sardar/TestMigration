package com.mhe.ctb.oas.BMTSync.controller;

import java.util.HashMap;
import java.util.Map;

public class HardcodedEndpointSelector implements EndpointSelector {

	private final Map<Integer, String> endpointMap;
	
	public HardcodedEndpointSelector() {
		endpointMap = new HashMap<Integer, String>();
		endpointMap.put(15357,  "http://sync-gain-qa-elb.ec2-ctb.com");
		endpointMap.put(16701,  "http://sync-gain-content-elb.ec2-ctb.com");
	}
	
	@Override
	public String getEndpoint(Integer customerId) {

		return endpointMap.get(customerId);
	}

}
