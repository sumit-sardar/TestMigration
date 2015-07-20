package com.ctb.lexington.util.mosaicobject;

import java.io.Serializable;

public class MSSRequestResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	MosaicScoringRequest MosaicScoringRequest;
	
	public MSSRequestResponse(MosaicScoringRequest mosaicScoringRequest) {
		MosaicScoringRequest = mosaicScoringRequest;
	}

	/**
	 * @return the mosaicScoringRequest
	 */
	public MosaicScoringRequest getMosaicScoringRequest() {
		return MosaicScoringRequest;
	}

	/**
	 * @param mosaicScoringRequest the mosaicScoringRequest to set
	 */
	public void setMosaicScoringRequest(MosaicScoringRequest mosaicScoringRequest) {
		MosaicScoringRequest = mosaicScoringRequest;
	}
	
}
