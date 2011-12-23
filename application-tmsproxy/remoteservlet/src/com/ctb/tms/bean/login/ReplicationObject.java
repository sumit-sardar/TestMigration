package com.ctb.tms.bean.login;

import java.io.Serializable;

public class ReplicationObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean replicate;
	private long cacheTime;
	
	public long getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(long cacheTime) {
		this.cacheTime = cacheTime;
	}

	public boolean isReplicate() {
		return this.replicate;
	}
	
	public void setReplicate(boolean replicate) {
		this.replicate = replicate;
	}
}
