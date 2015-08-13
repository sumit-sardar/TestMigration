package com.ctb.utils.datamodel;

import java.io.Serializable;

public class CorrectResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private float noofCorrectResponse;
	private float scoreAdded;
	
	public CorrectResponse() {
		
	}

	public float getNoofCorrectResponse() {
		return noofCorrectResponse;
	}

	public void setNoofCorrectResponse(float noofCorrectResponse) {
		this.noofCorrectResponse = noofCorrectResponse;
	}

	public float getScoreAdded() {
		return scoreAdded;
	}

	public void setScoreAdded(float scoreAdded) {
		this.scoreAdded = scoreAdded;
	}

	@Override
	public String toString() {
		return "CorrectResponse [noofCorrectResponse=" + noofCorrectResponse
				+ ", scoreAdded=" + scoreAdded + "]";
	}
	
	
}
