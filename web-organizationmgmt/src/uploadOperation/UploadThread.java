package uploadOperation;

import java.io.IOException;

import javax.jms.JMSException;
import javax.servlet.http.HttpSession;

import manageUpload.UploadDownloadManagementServiceControl;

import com.ctb.bean.testAdmin.DataFileAudit;

public class UploadThread implements Runnable {

	private String userName;
	private String fullFilePath;
	private String instanceURL;
	private Integer uploadFileId;
	private HttpSession session;

	private UploadDownloadManagementServiceControl uploadDownloadManagementServiceControl;
	private com.ctb.control.uploadDownloadManagement.UploadDownloadManagement uploadDownloadManagement;
	 
	public UploadThread(String userName, String fullFilePath, Integer uploadDataFileId, String instanceURL, HttpSession session) {
		this.userName = userName;
		this.fullFilePath = fullFilePath;
		this.instanceURL = instanceURL;
		this.uploadFileId = uploadDataFileId;
		this.session = session;
	}


	public void run()  {
		System.out.println("[iaa] t.1 run()"+" uploadDataFileId="+uploadFileId);
		try {
			explicitlyInitializeAllControls();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		invokeService(this.userName, this.fullFilePath, this.uploadFileId, this.instanceURL, 1,session);
		System.out.println("[iaa] t.2 run() Completed."+" uploadDataFileId="+uploadFileId);

	}

	private void invokeService(String userName, String fullFilePath, Integer uploadFileId, String instanceURL, int trycount,HttpSession session) {
		try {
			/*System.out.println("XXXXXXXXXXXXXXXXXXXXXXX Upload Thread XXXXXXXXXXXXXXXXXXX"+" uploadDataFileId="+uploadFileId);
			Enumeration<String> enumbjects = session.getAttributeNames();
			while(enumbjects.hasMoreElements()){
				String attrName = enumbjects.nextElement();
				
			}
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXX Upload Thread XXXXXXXXXXXXXXXXXXX"+" uploadDataFileId="+uploadFileId);*/

			System.out.println("***** Upload App: invoking process service: " + this.userName + " : " +" uploadDataFileId="+uploadFileId);
			String endpoint = instanceURL + "/platform-webservices-newui/UploadDownloadManagement";
			
			// uploadDownloadManagementServiceControl.setEndPoint(new URL(endpoint));
			uploadDownloadManagementServiceControl.setEndpointAddress(endpoint);
			System.out.println("***** Upload App: using service endpoint: " + endpoint+" uploadDataFileId="+uploadFileId);
			System.out.println("[iaa] uf.1 uploadDownloadManagementServiceControl.uploadFile()"+" uploadDataFileId="+uploadFileId);
			uploadDownloadManagementServiceControl.uploadFile(this.userName, fullFilePath, uploadFileId);
			System.out.println("[iaa] uf.2 uploadDownloadManagementServiceControl.uploadFile()"+" uploadDataFileId="+uploadFileId);
		} 
		catch (com.ctb.webservices.CTBBusinessException e) {
			System.out.println("[iaa] uf.x CTBBusinessException. "+e.toString());
			DataFileAudit dataFileAudit = new DataFileAudit();
			dataFileAudit.setStatus("FL");
			try{
				uploadDownloadManagement.updateAuditFileStatus(uploadFileId);
			} catch (Exception se) {
				System.out.println("[iaa] uf.x2 Exception. "+se.toString());
				se.printStackTrace();
			}
		}
		catch (Exception e) {
			if(e  instanceof java.rmi.MarshalException ||
					e instanceof  java.io.NotSerializableException){
				System.out.println("Exception class :: " + e.getClass().getName());

			}else{
				System.out.println("Exception class :: " + e.getClass().getName());
				if(trycount < 5 && "getMethodName".equals(e.getStackTrace()[0].getMethodName())) {
					System.out.println("***** Service invocation failed, trying again - " + trycount);
					invokeService(userName, fullFilePath, uploadFileId, instanceURL, ++trycount,session);
				} else {
					System.out.println("****************** start EXCEPTION in invokeService ***************** ");
					System.out.println("getMethodName = " + e.getStackTrace()[0].getMethodName());
					System.out.println("Message :" + e.getMessage());
					System.out.println("[iaa] uf.x3 Exception. "+e.toString());
					if (!"getConversationPhase".equals(e.getStackTrace()[0].getMethodName()) && (e.getMessage() != null) &&	
							(e.getClass().isInstance(new JMSException(""))) && (trycount >= 5)) {
						System.out.println("Set status to error");
						DataFileAudit dataFileAudit = new DataFileAudit();
						dataFileAudit.setStatus("FL");
						try{
							uploadDownloadManagement.updateAuditFileStatus(uploadFileId);
						} catch (Exception se) {
							se.printStackTrace();
						}
					}                
					e.printStackTrace();
					System.out.println("****************** end EXCEPTION in invokeService ***************** " + e.fillInStackTrace());
					throw new RuntimeException(e);
				}
			}
		}
		finally{
			uploadDownloadManagementServiceControl = null;
			uploadDownloadManagement = null;
		}
	}

	private void explicitlyInitializeAllControls() throws IOException, ClassNotFoundException{
		ClassLoader cl = this.getClass().getClassLoader();
		this.uploadDownloadManagementServiceControl = (manageUpload.UploadDownloadManagementServiceControlBean) java.beans.Beans
														.instantiate(cl,"manageUpload.UploadDownloadManagementServiceControlBean");
		this.uploadDownloadManagement = (com.ctb.control.uploadDownloadManagement.UploadDownloadManagementBean) java.beans.Beans
											.instantiate(cl,"com.ctb.control.uploadDownloadManagement.UploadDownloadManagementBean");
	}
}
