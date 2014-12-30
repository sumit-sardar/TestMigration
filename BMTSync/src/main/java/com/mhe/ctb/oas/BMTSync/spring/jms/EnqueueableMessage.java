package com.mhe.ctb.oas.BMTSync.spring.jms;

public interface EnqueueableMessage {
	Integer getCustomerId();
	
	String getPrimaryKeyName();
	
	String getPrimaryKeyValue();
	
	String getSecondaryKeyName();
	
	String getSecondaryKeyValue();
	
	String getLogDetails();
}
