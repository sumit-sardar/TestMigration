package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;
import java.sql.Blob;

public class DataFileTemp extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private  Integer dataFileAuditId;
    private  byte[] dataFile;
    
	/**
	 * @return Returns the dataFileAuditId.
	 */
	public Integer getDataFileAuditId() {
		return dataFileAuditId;
	}
	/**
	 * @param dataFileAuditId The dataFileAuditId to set.
	 */
	public void setDataFileAuditId(Integer dataFileAuditId) {
		this.dataFileAuditId = dataFileAuditId;
	}
	/**
	 * @return Returns the dataFile.
	 */
	public byte[] getDataFile() {
		return dataFile;
	}
	/**
	 * @param dataFile The dataFile to set.
	 */
	public void setDataFile(byte[] dataFile) {
		this.dataFile = dataFile;
	}
	
} 
