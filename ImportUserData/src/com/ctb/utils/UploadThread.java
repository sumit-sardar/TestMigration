package com.ctb.utils;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ctb.bean.DataFileAudit;
import com.ctb.bean.UploadMoveData;
import com.ctb.dao.UploadFileDao;
import com.ctb.dao.UploadFileDaoImpl;

/**
 * Thread for Uploading UserData for each uploaded File
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
			System.out.println("\n");
			logger.info("Actual Process Start Time for file :"
					+ this.inFile.getName() + "-->"
					+ new Date(System.currentTimeMillis()));

			UploadUserFile userUpload = new UploadUserFile(customerId, inFile,
					uploadFileId, this.uploadMoveData.getOrgNodeCategory(),
					this.uploadMoveData.getUserFileRowHeader(),
					this.uploadMoveData.getNoOfUserColumn());

			userUpload.startProcessing();

			logger.info("Import process is completed for file :"
					+ this.inFile.getName() + "-->"
					+ new Date(System.currentTimeMillis()));
		} catch (Exception e) {
			logger.error("Exception in run() of UploadThread.. Thread Error.");
			logger.info("Import process is not completed for file :"
					+ this.inFile.getName() + "-->"
					+ new Date(System.currentTimeMillis()));
			DataFileAudit dataFileAudit = new DataFileAudit();
			dataFileAudit.setDataFileAuditId(uploadFileId);
			dataFileAudit.setStatus("FL");
			try {
				UploadFileDao dao = new UploadFileDaoImpl();
				dao.upDateAuditTable(dataFileAudit);
			} catch (Exception se) {
				se.printStackTrace();
			}
		}

	}

}
