package com.ctb.utils;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ctb.bean.UploadMoveData;

/**
 * Thread for Uploading StudentData for each uploaded File
 * 
 * @author TCS
 * 
 */
public class UploadThread implements Runnable {
	private Integer customerId;
	private File inFile;
	private Integer uploadFileId;
	private UploadMoveData uploadMoveData;
	private static Logger logger = Logger.getLogger(UploadThread.class
			.getName());

	/**
	 * Constructor
	 * 
	 * @param customerId
	 * @param inFile
	 * @param uploadFileId
	 * @param uploadMoveData
	 */
	public UploadThread(Integer customerId, File inFile, Integer uploadFileId,
			UploadMoveData uploadMoveData) {
		super();
		this.customerId = customerId;
		this.inFile = inFile;
		this.uploadFileId = uploadFileId;
		this.uploadMoveData = uploadMoveData;
	}

	public void run() {
		try {
			logger.info("Actual Process Start Time for file :"
					+ this.inFile.getName() + "-->"
					+ new Date(System.currentTimeMillis()));

			UploadStudentFile studentUpload = new UploadStudentFile(customerId,
					inFile, uploadFileId,
					this.uploadMoveData.getOrgNodeCategory(),
					this.uploadMoveData.getStudentFileRowHeader(),
					this.uploadMoveData.getNoOfUserColumn(),
					this.uploadMoveData.getValueForStudentId(),
					this.uploadMoveData.getValueForStudentId2(),
					this.uploadMoveData.isStudentIdUnique());

			studentUpload.startProcessing();
			
		} catch (Exception e) {
			logger.error("Exception in run() of UploadThread.. Thread Error.");
			System.exit(1);
		}

	}

}
