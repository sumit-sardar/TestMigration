package com.ctb.importData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ctb.bean.DataFileAudit;
import com.ctb.bean.OrgNodeCategory;
import com.ctb.bean.UploadMoveData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.FileHeaderException;
import com.ctb.exception.FileNotUploadedException;
import com.ctb.utils.Configuration;
import com.ctb.utils.ExtractUtil;
import com.ctb.utils.FtpSftpUtil;
import com.ctb.utils.UploadFormUtils;
import com.ctb.utils.UploadThread;
import com.jcraft.jsch.Session;

/**
 * This Class is entry point to this USER UPLOAD Utility
 * 
 * @author TCS
 * 
 */
public class ImportDataProcessor {

	static Logger logger = Logger
			.getLogger(ImportDataProcessor.class.getName());

	/**
	 * Properties File name should be mentioned as 1st Argument and Properties
	 * File location should be mentioned as 2nd Argument
	 */
	static String sourceDir, targetDir, archiveDir = "";
	static Integer customerId = new Integer(0);
	UploadMoveData uploadMoveData = null;

	/**
	 * Accepts single Parameter denoting the Properties file Name
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("\t******Utility Fresh Start*******");
		Long startTime = System.currentTimeMillis();
		logger.info("StartTime:" + new Date(System.currentTimeMillis()));
		String envName = getPropFileFromCommandLine(args);
		ExtractUtil.loadExternalPropetiesFile(envName, args[1]);
		sourceDir = Configuration.getFtpFilePath();
		targetDir = Configuration.getLocalFilePath();
		archiveDir = Configuration.getArchivePath();
		customerId = Integer.valueOf(Configuration.getCustomerId());
		logger.info("Import Process started..."
				+ new Date(System.currentTimeMillis()));
		Session session = null;
		try {
			logger.info("Temp Directory CleanUp Started..."
					+ new Date(System.currentTimeMillis()));
			deleteFiles(targetDir);
			logger.info("Temp Directory CleanUp Completed..."
					+ new Date(System.currentTimeMillis()));
			logger.info(" DownloadFiles Start Time:"
					+ new Date(System.currentTimeMillis()));
			session = FtpSftpUtil.getSFTPSession();
			FtpSftpUtil.downloadFiles(session, sourceDir, targetDir);
			logger.info("DownloadFiles End Time:"
					+ new Date(System.currentTimeMillis()));

			/**
			 * Processing the files from Temp Location
			 **/
			ImportDataProcessor importProcessor = new ImportDataProcessor();
			importProcessor.processImportedFiles();

			/**
			 * End of Processing the files from Temp Location
			 **/

			logger.info("Import Process Is Completed...  total time taken -> "
					+ (System.currentTimeMillis() - startTime) + "ms");
		} catch (Exception e) {
			logger.info("Runtime Exception Occurred..");
			logger.info(e.getMessage());
		} finally {
			System.exit(1);
		}
	}

	private static String getPropFileFromCommandLine(String[] args) {
		String envName = "";
		String usage = "Usage:\n 	java -jar ImportStudentData.jar <properties file name>";
		if (args.length < 1) {
			logger.info("Cannot parse command line. No command specified.");
			logger.info(usage);
			System.exit(1);
		} else {
			envName = args[0].toUpperCase();
		}
		return envName;
	}

	/**
	 * All files that are downloaded in the targetDir are processed one by one.
	 * 
	 * @throws Exception
	 */
	private void processImportedFiles() throws Exception {
		File folder = new File(targetDir);

		File[] listOfFiles = folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".csv");
			}
		});

		int uploadDataFileId = 0;
		if (listOfFiles != null && listOfFiles.length > 0) {
			int length = listOfFiles.length;
			for (int j = 0; j < length; j++) {
				File inFile = listOfFiles[j];
				logger.info("File Process Started for-> " + inFile.getName()
						+ " \t Customer-id used : "
						+ ImportDataProcessor.customerId);

				if (inFile.isFile()) {
					logger.info("ReadFileContent Start Time:"
							+ new Date(System.currentTimeMillis()));

					uploadDataFileId = readFileContent(inFile).intValue();

					if (uploadDataFileId != 0) {
						addErrorDataFile(inFile, new Integer(uploadDataFileId));

						logger.info("ReadFileContent End Time:"
								+ new Date(System.currentTimeMillis()));
						try {
							if (null != uploadMoveData) {
								UploadThread uploadThread = new UploadThread(
										customerId, inFile, new Integer(
												uploadDataFileId),
										uploadMoveData);
								Thread t = new Thread(uploadThread);
								t.start();
								t.join();
							}
						} catch (Exception e) {
							logger.error("Thread invoking Error.. ");
							throw e;
						}
					} else {
						logger.info("Upload Process Failed.. ");
					}
				}
			}
		} else {
			logger.info("No Input Files to be processed..");
		}
	}

	/**
	 * Deletes all files present in targetDir folder location
	 * 
	 * @param targetDir
	 * @throws Exception
	 */
	private static void deleteFiles(String targetDir) throws Exception {
		File dir = new File(targetDir);
		try {
			if (dir.isDirectory()) {
				if (dir.listFiles().length > 0) {
					File[] inFile = dir.listFiles();
					for (int i = 0; i < inFile.length; i++) {
						inFile[i].delete();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * File is saved in Database after basic extension and size check.
	 * 
	 * @param inFile
	 * @return Integer
	 */
	private Integer readFileContent(File inFile) {
		UploadFormUtils uploadFormUtils = new UploadFormUtils();
		String strFileName = inFile.getName();
		Integer uploadDataFileId = new Integer(0);
		if (!UploadFormUtils.verifyFileExtension(strFileName)) {
			logger.error("Upload File Extension must be .csv");
			return new Integer(0);
		}
		long filesize = (inFile.getTotalSpace());
		if ((filesize == 0) || (strFileName.length() == 0)) {
			logger.error("Upload File Cannot be empty..");
			return new Integer(0);
		}
		try {
			uploadDataFileId = uploadFormUtils.saveFileToDBTemp(inFile);
			logger.info("File Data saved in data_file_temp table..");
			return uploadDataFileId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Integer(0);

	}

	/**
	 * Basic File Validation is checked After Configuration check
	 * 
	 * @param inFile
	 * @param uploadDataFileId
	 * @return Integer
	 * @throws CTBBusinessException
	 */
	public Integer addErrorDataFile(File inFile, Integer uploadDataFileId)
			throws CTBBusinessException {
		int noOfUserColumn = 0;
		// Used to read the file type
		UploadFormUtils uploadFormUtils = new UploadFormUtils();
		try {
			DataFileAudit dataFileAudit = new DataFileAudit();
			Integer customerId = ImportDataProcessor.customerId;
			boolean isEngradeCustomer = uploadFormUtils
					.checkCustomerConfigurationEntries(customerId,
							"ENGRADE_Customer");
			if (!isEngradeCustomer) {
				logger.info("ENGRADE_Customer Configuration not present..");
				throw new CTBBusinessException("Uploaded.Failed");
			}
			OrgNodeCategory[] orgNodeCategory = uploadFormUtils
					.getOrgNodeCategories(customerId);
			noOfUserColumn = (orgNodeCategory.length) * 3 + 1;
			// Validating the excel sheet
			String fileType = uploadFormUtils.getUploadFileCheck(inFile,
					noOfUserColumn, customerId, orgNodeCategory);
			if (fileType == "") {
				throw new CTBBusinessException("Uploaded.Failed");
			}

			dataFileAudit.setDataFileAuditId(uploadDataFileId);
			dataFileAudit.setUploadFileName(inFile.getName());
			dataFileAudit.setCreatedDateTime(new Date());
			dataFileAudit.setStatus("IN");
			dataFileAudit.setUserId(new Integer(1));
			dataFileAudit.setCustomerId(customerId);
			dataFileAudit.setCreatedBy(new Integer(1));
			uploadFormUtils.createDataFileAudit(dataFileAudit);

			uploadMoveData = uploadFormUtils.getUploadMoveData();

		} catch (FileNotFoundException fn) {
			FileNotUploadedException fileNotUploadedException = new FileNotUploadedException(
					"FileNotFound.Failed");
			fileNotUploadedException.setStackTrace(fn.getStackTrace());
			throw fileNotUploadedException;
		} catch (FileHeaderException fh) {
			FileHeaderException fileHeaderException = new FileHeaderException(
					"FileHeader.Failed");
			fileHeaderException.setStackTrace(fh.getStackTrace());
			throw fileHeaderException;
		} catch (CTBBusinessException e) {
			FileHeaderException fileHeaderException = new FileHeaderException(
					"Uploaded.Failed");
			fileHeaderException.setStackTrace(e.getStackTrace());
			throw fileHeaderException;

		} catch (SQLException e) {
			FileNotUploadedException fileNotUploadedException = new FileNotUploadedException(
					"Uploaded.Failed");
			fileNotUploadedException.setStackTrace(e.getStackTrace());
			throw fileNotUploadedException;
		} catch (Exception e) {
			FileNotUploadedException fileNotUploadedException = new FileNotUploadedException(
					"Uploaded.Failed");
			fileNotUploadedException.setStackTrace(e.getStackTrace());
			throw fileNotUploadedException;
		}

		return uploadDataFileId;
	}

}
