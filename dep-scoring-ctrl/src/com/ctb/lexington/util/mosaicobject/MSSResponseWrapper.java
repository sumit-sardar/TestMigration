package com.ctb.lexington.util.mosaicobject;

import java.io.Serializable;

public class MSSResponseWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private MosaicSuccessResponse MosaicScoringResponse;
	private String ResponseCode;
	private String Url;
	private String ErrorMessage="";
	private String MosaicErrorCode;
	private String MosaicErrorMessage;
	private String Timestamp;
	private String DeveloperNotes;

	public MosaicSuccessResponse getMosaicScoringResponse() {
		return MosaicScoringResponse;
	}

	public void setMosaicScoringResponse(
			MosaicSuccessResponse mosaicScoringResponse) {
		MosaicScoringResponse = mosaicScoringResponse;
	}

	public String getResponseCode() {
		return ResponseCode;
	}

	public void setResponseCode(String responseCode) {
		ResponseCode = responseCode;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getErrorMessage() {
		return ErrorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}

	public String getMosaicErrorCode() {
		return MosaicErrorCode;
	}

	public void setMosaicErrorCode(String mosaicErrorCode) {
		MosaicErrorCode = mosaicErrorCode;
	}

	public String getMosaicErrorMessage() {
		return MosaicErrorMessage;
	}

	public void setMosaicErrorMessage(String mosaicErrorMessage) {
		MosaicErrorMessage = mosaicErrorMessage;
	}

	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

	public String getDeveloperNotes() {
		return DeveloperNotes;
	}

	public void setDeveloperNotes(String developerNotes) {
		DeveloperNotes = developerNotes;
	}

	

}
