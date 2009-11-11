package com.ctb.lexington.domain.score.event.common;

import com.ctb.lexington.exception.CTBSystemException;

public interface EventRecipient {
	
	public void onEvent(Event event) throws CTBSystemException;
}
