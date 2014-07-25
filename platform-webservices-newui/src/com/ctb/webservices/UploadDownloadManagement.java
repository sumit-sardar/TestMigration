package com.ctb.webservices;

import java.io.IOException;

import javax.jws.*;
import org.apache.beehive.controls.api.bean.Control;

import com.ctb.exception.CTBBusinessException;

@WebService
public class UploadDownloadManagement {

	@Control
	private com.ctb.control.uploadDownloadManagement.UploadDownloadManagement uploadDownloadManagement;

	@WebMethod
	public void uploadFile(java.lang.String userName,
			java.lang.String serverFilePath, java.lang.Integer uploadDataFileId)
			throws com.ctb.exception.CTBBusinessException {
		System.out.println("*****------ uploadDownloadManagement.uploadFile(): Started --------********");
		explicitlyInitializeAllControls();
		uploadDownloadManagement.uploadFile(userName, serverFilePath,
				uploadDataFileId);
		System.out.println("*****------ uploadDownloadManagement.uploadFile(): Completed --------********");
	}
	
	 public void explicitlyInitializeAllControls() throws CTBBusinessException{
		 try{
	    	ClassLoader cl = this.getClass().getClassLoader();
	    	if(this.uploadDownloadManagement == null){
	    		this.uploadDownloadManagement = (com.ctb.control.uploadDownloadManagement.UploadDownloadManagementBean) java.beans.Beans
				.instantiate(cl,
						"com.ctb.control.uploadDownloadManagement.UploadDownloadManagementBean");
	    	}
	    	 
	    }
		 catch(Exception e){
			 e.printStackTrace();
			 throw new CTBBusinessException(e.getMessage());
		 }
	 }
}