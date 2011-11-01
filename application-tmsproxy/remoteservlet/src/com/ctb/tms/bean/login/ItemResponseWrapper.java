package com.ctb.tms.bean.login;

import java.io.Serializable;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

public class ItemResponseWrapper implements Serializable, CachePreLoadObject{

	private static final long serialVersionUID = 1L;
	private boolean replicate = true;
	private Tsd tsd;

	public String getLsid() {
		return this.tsd.getLsid();
	}

	public Tsd getTsd() {
		return tsd;
	}

	public void setTsd(Tsd tsd) {
		this.tsd = tsd;
	}

	public boolean doReplicate() {
		return replicate;
	}

	public void setReplicate(boolean replicate) {
		this.replicate = replicate;
	}

}
