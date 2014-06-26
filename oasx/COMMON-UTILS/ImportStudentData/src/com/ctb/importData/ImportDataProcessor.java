package com.ctb.importData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

public class ImportDataProcessor {
	
	static Logger logger = Logger.getLogger(ImportDataProcessor.class.getName());
    static String propertiesFilePathLocation = "/export/home/oasdev/operations/operation-tools/java/ImportDataFromEngradeToOAS/PropertiesFiles/";
	//static String propertiesFilePathLocation = "C:\\Documents and Settings\\545946\\Desktop\\Sprint 75\\ImportDataFromEngradeToOAS\\";
	static String sourceDir , targetDir ,archiveDir = "";
	static Integer customerId = new Integer(0);	
	UploadMoveData uploadMoveData = null;

	
	public static void main(String[] args) {
		System.out.println("StartTime:" + new Date(System.currentTimeMillis()));
		//PropertyConfigurator.configure("conf/log4j.properties");
		String envName = getPropFileFromCommandLine(args);
		ExtractUtil.loadExternalPropetiesFile(envName, propertiesFilePathLocation);
		sourceDir = Configuration.getFtpFilePath();
		targetDir = Configuration.getLocalFilePath();
		archiveDir = Configuration.getArchivePath();
		customerId = Integer.valueOf(Configuration.getCustomerId());
		System.out.println("Import Process started..." + new Date(System.currentTimeMillis()));
		logger.info("Import Process started..." + new Date(System.currentTimeMillis()));	
		Session session = null;
		try{
			System.out.println("Temp Directory CleanUp Started..." + new Date(System.currentTimeMillis()));
			deleteFiles(targetDir);
			System.out.println("Temp Directory CleanUp Completed..." + new Date(System.currentTimeMillis()));
			System.out.println(" DownloadFiles Start Time:" + new Date(System.currentTimeMillis()));
			session = FtpSftpUtil.getSFTPSession();			
			FtpSftpUtil.downloadFiles(session, sourceDir, targetDir);	
			System.out.println("DownloadFiles End Time:" + new Date(System.currentTimeMillis()));
			 /** Processing the files from Temp Location
			 **/ 
			ImportDataProcessor importProcessor = new ImportDataProcessor();
			importProcessor.processImportedFiles();		
			
			/** End of Processing the files from Temp Location
			 * 
			*/
			
			System.out.println("Import Process Is in Progress..." + new Date(System.currentTimeMillis()));
			logger.info("Import Process Is in Progress..." + new Date(System.currentTimeMillis()));				 
		}catch(Exception e){
			logger.info("Runtime Exception Occurred..",e);
			System.out.println(e.getMessage());
		}
	}
	
	private static String getPropFileFromCommandLine(String[] args){
		String envName = "";
		String usage = "Usage:\n 	java -jar ImportStudentData.jar <properties file name>";
		if (args.length < 1){
			System.out.println("Cannot parse command line. No command specified.");
			System.out.println(usage);
			System.exit(1);
		}
		else{
			envName = args[0].toUpperCase();
		}
		return envName;
	}
	
	private void processImportedFiles() throws Exception{
		File folder = new File(targetDir);
		File[] listOfFiles = folder.listFiles();
		int uploadDataFileId = 0; 
		if(listOfFiles != null && listOfFiles.length > 0){
			for (int j = 0; j < listOfFiles.length; j++) {				
				File inFile = listOfFiles[j];
				System.out.println("File Process Started for-> "+inFile.getName());
				if(inFile.isFile()){
					System.out.println("ReadFileContent Start Time:" + new Date(System.currentTimeMillis()));
					uploadDataFileId = readFileContent(inFile).intValue();	
					if(uploadDataFileId != 0){
						addErrorDataFile( inFile , new Integer(uploadDataFileId));
						System.out.println("ReadFileContent End Time:" + new Date(System.currentTimeMillis()));
						try{
							if (null != uploadMoveData){
								UploadThread uploadThread = new UploadThread(customerId ,inFile,new Integer(uploadDataFileId) ,uploadMoveData);
								new Thread(uploadThread).start();
							}
						}catch(Exception e){
							System.out.println("Thread invoking Error.. ");
							logger.error("Thread invoking Error..");
							throw e;
						}
					}else{
						System.out.println("Upload Process Failed.. ");
						logger.error("Upload Process Failed..");
					}
				}
			}
		}		
	}//processImported End..
	
	
	private static void deleteFiles(String targetDir) throws Exception{
		File dir = new File(targetDir);
		try{
			if(dir.isDirectory()){
				if(dir.listFiles().length > 0){
					File[] inFile = dir.listFiles();
					for (int i = 0; i < inFile.length; i++) {						
						inFile[i].delete();
					}
				}				
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	private Integer readFileContent(File inFile){
		UploadFormUtils  uploadFormUtils = new UploadFormUtils();
		String strFileName = inFile.getName(); 
		Integer uploadDataFileId = new Integer(0);
        if (!UploadFormUtils.verifyFileExtension(strFileName)){ 
        	logger.error("Upload File Extension must be .xls");
        	System.out.println("Upload File Extension must be .xls");
            return new Integer(0);
         }        
        long filesize = (inFile.getTotalSpace());        
        if ( (filesize == 0) || (strFileName.length() == 0)){  
        	logger.error("Upload File Cannot be empty..");
        	System.out.println("Upload File Cannot be empty..");
            return new Integer(0);       
        }            
        try {
            //String saveFileName = getSaveFileName(strFileName);            
            uploadDataFileId = uploadFormUtils.saveFileToDBTemp(inFile);
            System.out.println("File saved in data_file_temp table..");
            return uploadDataFileId;        
        } catch (Exception e) {        	
            e.printStackTrace();       
        }       
        return new Integer(0);
    
	}
	

	public Integer addErrorDataFile(File inFile , Integer uploadDataFileId)throws CTBBusinessException {
		int noOfUserColumn = 0;		
		// Used to read the file type
		InputStream fileInputStrean = null;	
		UploadFormUtils  uploadFormUtils = new UploadFormUtils();
		try {
			DataFileAudit dataFileAudit = new DataFileAudit();			
			Integer customerId = ImportDataProcessor.customerId;			
			boolean isLaslinksCustomer = uploadFormUtils.checkCustomerConfigurationEntries(customerId,"ENGRADE_Customer");			
			/*if ( serverFilePath.indexOf(String.valueOf(customerId.intValue())) <=  -1  ) {			
				CTBBusinessException be = new CTBBusinessException("Uploaded.Failed");
				 throw be;
			}*/				
			if ( !isLaslinksCustomer) {	
				System.out.println("Configuration not present..");
				throw  new CTBBusinessException("Uploaded.Failed") ;				
			}	
			OrgNodeCategory[] orgNodeCategory = uploadFormUtils.getOrgNodeCategories(customerId);
			noOfUserColumn = (orgNodeCategory.length) * 3 + 1;		
			fileInputStrean = new FileInputStream(inFile);
			//Validating the excel sheet			
			String fileType = uploadFormUtils.getUploadFileCheck(fileInputStrean,noOfUserColumn,customerId,orgNodeCategory);    
			if ( fileType == "") {				
				throw  new CTBBusinessException("Uploaded.Failed") ;				
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
			
		}catch (FileNotFoundException fn) {		
			FileNotUploadedException fileNotUploadedException = 
			                new FileNotUploadedException
			                        ("FileNotFound.Failed");
			fileNotUploadedException.setStackTrace(fn.getStackTrace());
			throw fileNotUploadedException;		
		} catch (FileHeaderException fh) {
			FileHeaderException fileHeaderException = 
			                new FileHeaderException
			                        ("FileHeader.Failed");
			fileHeaderException.setStackTrace(fh.getStackTrace());
			throw fileHeaderException;
		} catch (CTBBusinessException e) {		
			FileHeaderException fileHeaderException = 
			                new FileHeaderException
			                        ("Uploaded.Failed");
			fileHeaderException.setStackTrace(e.getStackTrace());
			throw fileHeaderException;
			
		}catch (SQLException e) {		
			FileNotUploadedException fileNotUploadedException = 
			                new FileNotUploadedException
			                        ("Uploaded.Failed");
			fileNotUploadedException.setStackTrace(e.getStackTrace());
			throw fileNotUploadedException;			
		} catch (Exception e ) {		
			FileNotUploadedException fileNotUploadedException = 
			                new FileNotUploadedException
			                        ("Uploaded.Failed");
			fileNotUploadedException.setStackTrace(e.getStackTrace());
			throw fileNotUploadedException;		
		}
		
		return uploadDataFileId;
	}

}
