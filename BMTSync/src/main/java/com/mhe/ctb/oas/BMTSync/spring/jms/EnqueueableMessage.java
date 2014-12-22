package com.mhe.ctb.oas.BMTSync.spring.jms;

public interface EnqueueableMessage {
	public Integer getCustomerId();
	
	public String getPrimaryKeyName();
	
	public String getPrimaryKeyValue();
	
	public String getSecondaryKeyName();
	
	public String getSecondaryKeyValue();
	
	public String getErrorDetails();
}
