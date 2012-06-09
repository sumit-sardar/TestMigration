package com.ctb.bean.studentManagement;

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the contents of the OAS.MUSIC_FILE_LIST table
 * 
 * @author TCS
 */

public class MusicFiles extends CTBBean{
	
	static final long serialVersionUID = 1L;
	 private Integer fileId;
	 private String audioFileName;
	
	 
	 public Integer getFileId() {
		return fileId;
	}
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	public String getAudioFileName() {
		return audioFileName;
	}
	public void setAudioFileName(String audioFileName) {
		this.audioFileName = audioFileName;
	}
	
	 
}
