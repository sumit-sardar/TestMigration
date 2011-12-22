package com.ctb.tms.bean.login;

import java.io.Serializable;

public class ReplicationObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean replicate;
	
	public boolean isReplicate() {
		return this.replicate;
	}
	
	public void setReplicate(boolean replicate) {
		this.replicate = replicate;
	}
}
