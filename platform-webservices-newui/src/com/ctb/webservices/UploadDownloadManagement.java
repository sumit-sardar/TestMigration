package com.ctb.webservices;

import javax.jws.*;
import org.apache.beehive.controls.api.bean.Control;

@WebService
public class UploadDownloadManagement {

	@Control
	private com.ctb.control.uploadDownloadManagement.UploadDownloadManagement uploadDownloadManagement;

	@WebMethod
	public void uploadFile(java.lang.String userName,
			java.lang.String serverFilePath, java.lang.Integer uploadDataFileId)
			throws com.ctb.exception.CTBBusinessException {
		uploadDownloadManagement.uploadFile(userName, serverFilePath,
				uploadDataFileId);
	}
}