package com.ctb.tms.bean.login;

import java.io.Serializable;

public class ReplicationObject implements Serializable {

	private static final long serialVersionUID = 1L;
	//private Boolean replicate;
	private long cacheTime;
	
	public long getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(long cacheTime) {
		this.cacheTime = cacheTime;
	}

	/*public Boolean isReplicate() {
		return this.replicate;
	}
	
	public Boolean doReplicate() {
		return this.replicate;
	}
	
	public Boolean getReplicate() {
		return this.replicate;
	}
	
	public void setReplicate(Boolean replicate) {
		this.replicate = replicate;
	}*/
}
