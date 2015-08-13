package com.ctb.utils.datamodel;

import java.io.Serializable;

public class PossibleOption implements Serializable{

	private static final long serialVersionUID = 1L;
	private String format;
	private String response;

	public PossibleOption(){

	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}




}
