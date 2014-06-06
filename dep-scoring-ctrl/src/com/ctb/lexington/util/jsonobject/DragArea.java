package com.ctb.lexington.util.jsonobject;

import java.io.Serializable;

public class DragArea implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private String format;
	private String response;
	
	public DragArea(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
