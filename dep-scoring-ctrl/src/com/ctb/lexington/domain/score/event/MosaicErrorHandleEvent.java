package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.score.event.common.Event;

public class MosaicErrorHandleEvent extends Event {

	private Long studentId;
	private Long sessionId;
	private final Long invokeKey;
	private boolean isRetryFTProcess;

	public MosaicErrorHandleEvent(Long testRosterId, final Long invokeKey,
			final boolean isRetryFTProcess) {
		super(testRosterId);
		this.invokeKey = invokeKey;
		this.isRetryFTProcess = isRetryFTProcess;
	}

	public boolean isRetryFTProcess() {
		return isRetryFTProcess;
	}

	public Long getInvokeKey() {
		return invokeKey;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
}
