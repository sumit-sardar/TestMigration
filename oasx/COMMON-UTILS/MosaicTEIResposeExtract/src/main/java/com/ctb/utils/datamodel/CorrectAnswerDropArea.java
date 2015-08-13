package com.ctb.utils.datamodel;

import java.io.Serializable;

public class CorrectAnswerDropArea implements Serializable{

	private static final long serialVersionUID = 1L;
	private String droparea;
	
	public CorrectAnswerDropArea(){
		
	}

	public String getDroparea() {
		return droparea;
	}

	public void setDroparea(String droparea) {
		this.droparea = droparea;
	}
	
	
}
