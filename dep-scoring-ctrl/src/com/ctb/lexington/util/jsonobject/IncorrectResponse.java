package com.ctb.lexington.util.jsonobject;

import java.io.Serializable;

public class IncorrectResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private float noofIncorrectResponse;
	private float scoreDeducted;
	
	public IncorrectResponse() {
		
	}

	public float getNoofIncorrectResponse() {
		return noofIncorrectResponse;
	}

	public void setNoofIncorrectResponse(float noofIncorrectResponse) {
		this.noofIncorrectResponse = noofIncorrectResponse;
	}

	public float getScoreDeducted() {
		return scoreDeducted;
	}

	public void setScoreDeducted(float scoreDeducted) {
		this.scoreDeducted = scoreDeducted;
	}

	@Override
	public String toString() {
		return "IncorrectResponse [noofIncorrectResponse="
				+ noofIncorrectResponse + ", scoreDeducted=" + scoreDeducted
				+ "]";
	}

	
	
}
