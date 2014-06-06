package com.ctb.lexington.util.jsonobject;

import java.io.Serializable;

public class CorrectAnswerDragArea implements Serializable{

	private static final long serialVersionUID = 1L;
	private String format;
	private String response;
	
	public CorrectAnswerDragArea(){
		
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
