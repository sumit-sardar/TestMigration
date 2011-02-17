package com.ctb.testSessionInfo.dto; 

public class FileInfo implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;

    private String size = null;
    private String displayName = null;
    private String fileName = null;

    public FileInfo() {
    }

    public FileInfo(String displayName, String fileName, String size) {
    	this.size = size;
    	this.displayName = displayName;
    	this.fileName = fileName;
    }

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

    
} 
