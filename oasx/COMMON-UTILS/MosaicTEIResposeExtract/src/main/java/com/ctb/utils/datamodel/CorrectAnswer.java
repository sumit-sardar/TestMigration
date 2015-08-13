package com.ctb.utils.datamodel;

import java.io.Serializable;
import java.util.List;

public class CorrectAnswer implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private String format;
	private String response;
	private List<CorrectAnswerDragArea> answer;
	
	public CorrectAnswer(){
		
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

	public List<CorrectAnswerDragArea> getAnswer() {
		return answer;
	}

	public void setAnswer(List<CorrectAnswerDragArea> answer) {
		this.answer = answer;
	}
	
}
