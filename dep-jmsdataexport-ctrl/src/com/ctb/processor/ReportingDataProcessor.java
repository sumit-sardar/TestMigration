package com.ctb.processor;

import java.util.ArrayList;
import java.util.List;

import com.ctb.db.AuditTrailDAO;
import com.ctb.exception.CTBBusinessException;
import com.ctb.utils.CTBConstants;
public class ReportingDataProcessor {

	
	public void process(Integer customerId,  Integer userId, String userName, Integer jobId, List<Integer> rosterIdIn){
		List<String> fileNameList =new ArrayList<String>();
		int finalJobStatus =CTBConstants.JOB_STATUS_PROGRESS;
		int intirmJobStatus =CTBConstants.JOB_STATUS_PROGRESS;
		int invalidJobstattus = -1;
		String message  = "Job processing started.";
		List<String> formettedTestRoster = getFormettedTestRoster(rosterIdIn);
		
		boolean fileGenerationFailed = true;
		System.out.println("Received Job ID:"+jobId);
		System.out.println("Received TestRosterList:"+rosterIdIn);
		
		try{
			
			notifyUserByEmail(userName, jobId, "started");
			intirmJobStatus =CTBConstants.JOB_STATUS_FILE_GENERATION_STARTED;
			message  = "File generation started.";
			updateAuditTrail(true, finalJobStatus, intirmJobStatus , message, jobId);
			
			 /*********************
			 **job generate file**
			 *********************/
			
			generateFile(customerId, fileNameList, formettedTestRoster);
			
			/****************************
			 **process test roster file**
			 ****************************/
			//getFormettedTestRoster(testroster);
			updateTestRoster(formettedTestRoster);
			fileGenerationFailed = false;
						
			intirmJobStatus =CTBConstants.JOB_STATUS_FILE_GENERATION_COMPLETED;
			message  = "File generation completed.";
			updateAuditTrail(false,invalidJobstattus, intirmJobStatus, message, jobId);
			
						
			intirmJobStatus =CTBConstants.JOB_STATUS_FILE_TRANSFER_STARTED;
			message  = "File transfer started.";
			updateAuditTrail(false,invalidJobstattus , intirmJobStatus, message, jobId);
			
			/***********************
			 ** job transfer file **
			 ***********************/
			transferFile(fileNameList);
			
			intirmJobStatus =CTBConstants.JOB_STATUS_FILE_TRANSFER_COMPLETED;
			message  = "File transfer completed.";
			updateAuditTrail(false, invalidJobstattus, intirmJobStatus, message, jobId);
					
			finalJobStatus =CTBConstants.JOB_STATUS_COMPLETE;
			intirmJobStatus =CTBConstants.JOB_STATUS_FILE_TRANSFER_COMPLETED;
			message  = "Job processing completed.";
			updateAuditTrail(true,finalJobStatus, intirmJobStatus, message, jobId);
			
			notifyUserByEmail(userName, jobId, "completed");
			
		} catch (Exception e){
			System.err.print("Exception occurred for job id:"+jobId);
			e.printStackTrace();
			
			
			if(fileGenerationFailed){
				intirmJobStatus =CTBConstants.JOB_STATUS_FILE_GENERATION_FAILED;
				finalJobStatus =CTBConstants.JOB_STATUS_FAILED;
			} else {
				intirmJobStatus =CTBConstants.JOB_STATUS_FILE_TRANSFER_FAILED;
				finalJobStatus =CTBConstants.JOB_STATUS_PROGRESS;
			}
			
			message  = "Job processing failed:"+ e.getMessage();
			message  = message.substring(0, message.length()>999 ? 999:message.length() );
			updateAuditTrail(true,finalJobStatus, intirmJobStatus, message, jobId);
			notifyUserByEmail(userName, jobId, "failed");
			
			if(fileGenerationFailed){
				notifyUserByEmail(userName, jobId, "failed");
			} else {
				notifyManualFileTransferByEmail(jobId, fileNameList);
			}
			
			
		}
		
	}
	

	


	private void updateTestRoster(List<String> formettedTestRoster) throws CTBBusinessException {
		AuditTrailDAO dao = new AuditTrailDAO();
		dao.updateTestRoster(formettedTestRoster);
		
	}





	private List<String> getFormettedTestRoster(List<Integer> testroster) {
		//AuditTrailDAO dao = new AuditTrailDAO();
		int count = 0;
		StringBuilder rosterin = new StringBuilder();
		List<String> vallist = new ArrayList<String>();
		boolean isBlank = true;
	
		for (Integer roster : testroster) {
			if(!isBlank){
				rosterin.append(", ");	
			} else {
				isBlank = false;
			}
			rosterin.append(roster.toString());
			++count;
			if (count > 500) {
				vallist.add(rosterin.toString());
				rosterin = new StringBuilder();
				isBlank  = true;
				count= 0;
				
				
			} 
		}
		if(!isBlank && vallist.toString().trim().length()>0 ) {
			vallist.add(rosterin.toString());
		}
		return vallist;
	}


	private void updateAuditTrail(boolean isFinalStatus, int finalJobStatus, int intirmJobStatus ,String message , int jobId) {
		AuditTrailDAO dao = new AuditTrailDAO();
		if(isFinalStatus){
			dao.updateAuditTrail(finalJobStatus, intirmJobStatus, message, jobId);
		} else{
			dao.updateIntermideateAuditTrail(intirmJobStatus, message, jobId);
		}
		
		
	}


	private void generateFile(int customerId, List<String> fileNameList,  List<String> formettedTestRoster) throws CTBBusinessException{
		FileGenerator fileGenerator = new FileGenerator();
		fileGenerator.execute(customerId, fileNameList, formettedTestRoster);
	}
	
	
	private void transferFile(List<String> fileNameList) throws CTBBusinessException {
		FileTransporter trsp = FileTransporter.getInstance();
		trsp.transferFile(FileTransporter.TRANSPORT_TYPE_SFTP, fileNameList);
		
	}
	
	

	private void notifyUserByEmail(String userName, int jobid, String message) {
		EmailProcessor processor = EmailProcessor.getInstance();
		processor.processEmail( userName,  jobid,  message);
		
	}
	
	
	private void notifyManualFileTransferByEmail(Integer jobId, List<String> fileNameList) {
		EmailProcessor processor = EmailProcessor.getInstance();
		processor.processEmail(jobId, fileNameList);
		
	}
	
}
