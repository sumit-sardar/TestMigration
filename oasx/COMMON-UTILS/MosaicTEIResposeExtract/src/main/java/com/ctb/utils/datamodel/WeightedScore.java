package com.ctb.utils.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WeightedScore implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<CorrectResponse> correctResponse;
	private List<IncorrectResponse> incorrectResponse;
	
	public WeightedScore() {
		
	}

	public List<CorrectResponse> getCorrectResponse() {
		return correctResponse;
	}

	public void setCorrectResponse(List<CorrectResponse> correctResponse) {
		this.correctResponse = correctResponse;
	}

	public List<IncorrectResponse> getIncorrectResponse() {
		return incorrectResponse;
	}

	public void setIncorrectResponse(List<IncorrectResponse> incorrectResponse) {
		this.incorrectResponse = incorrectResponse;
	}
	
	public void addCorrectResponse(CorrectResponse correctResponse) {
	
		if(this.correctResponse == null)
			this.correctResponse = new ArrayList<CorrectResponse>();
		this.correctResponse.add(correctResponse);
		
	}
	
	public void addIncorrectResponse(IncorrectResponse incorrectResponse) {
		
		if(this.incorrectResponse == null)
			this.incorrectResponse = new ArrayList<IncorrectResponse>();
		this.incorrectResponse.add(incorrectResponse);
		
	}

	@Override
	public String toString() {
		return "WeightedScore [correctResponse=" + correctResponse
				+ ", incorrectResponse=" + incorrectResponse + "]";
	}
}
