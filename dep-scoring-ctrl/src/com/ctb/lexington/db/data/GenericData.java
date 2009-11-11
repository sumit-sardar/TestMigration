package com.ctb.lexington.db.data;

public class GenericData {
	private String chunkSize;
	private String logLevel;
	private String fileName;

	public String getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(String chunkSize) {
		this.chunkSize = chunkSize;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

}
