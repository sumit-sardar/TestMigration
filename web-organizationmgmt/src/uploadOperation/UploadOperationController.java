package uploadOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import manageUpload.UploadDownloadManagementServiceControl;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import utils.BroadcastUtils;
import utils.FilterSortPageUtils;
import utils.MessageResourceBundle;
import utils.PathFinderUtils;
import utils.PermissionsUtils;
import utils.Row;
import utils.UploadDownload;
import utils.UploadDownloadFormUtils;
import utils.UploadHistoryUtils;

import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.DataFileAudit;
import com.ctb.bean.testAdmin.DataFileAuditData;
import com.ctb.bean.testAdmin.StudentFile;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserFile;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.CTBConstants;
import com.ctb.util.web.sanitizer.JavaScriptSanitizer;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.PagerSummary;
import com.google.gson.Gson;

import dto.AuditFileHistory;
import dto.Message;
import dto.UploadFileInformation;


@Jpf.Controller()
public class UploadOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;
	
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.userManagement.UserManagement userManagement;
    

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.uploadDownloadManagement.UploadDownloadManagement uploadDownloadManagement;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.organizationManagement.OrganizationManagement organizationManagement;
    
    @Control()
    private com.ctb.control.db.Users users;
    
    @Control()
    private com.ctb.control.db.BroadcastMessageLog message;
    
    
    // LLO- 118 - Change for Ematrix UI
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;
    
    private final int MAXBUFFER = 100 * 1048576; 	
    
    private String userName = null;
    public String strFileName = null;
    
    public FormFile theFile;
    private Integer uploadDataFileId = new Integer(0);
    private String uploadMessage = null;
    
    private static final String ACTION_DELETE_FILE = "deleteFile";
    private static final String ACTION_EXPORT_ERROR_FILE = "exportErrorFile";
    
    //Temporary list to store searched/added uploaded file
    public List fileList = new ArrayList(); 
    
    
    private static final String ACTION_DEFAULT = "defaultAction";
    private static final String ACTION_DOWNLOAD_TEMPLATE = "DownloadTemplate";
    private static final String ACTION_UPLOAD_FILE = "UploadFile";
    private static final String ACTION_VIEW_UPLOADS = "ViewUploads";
    
    private static final String MODULE_DOWNLOAD_TEMPLATE = "moduleDownloadTemplate";
    private static final String MODULE_UPLOAD_FILE = "moduleUploadFile";
    private static final String MODULE_VIEW_UPLOADS = "moduleViewUploads";
    private static final String ACTION_APPLY_SEARCH   = "applySearch";

    private static final String TEMPLATE_ORGANIZATION = "organizationTemplate";
    private static final String TEMPLATE_USER = "userTemplate";
    private static final String TEMPLATE_STUDENT = "studentTemplate";
    
    private static final String RETURN_SUCCESS = "TRUE";
    
    private String saveFileName = null;
    public Integer fileMaxPage = null;
    private boolean searchApplied = false;
    private boolean isEmptyProfileSearch = false;
    
    private User user = null;
	private Integer customerId = null;


	@Control
	private UploadDownloadManagementServiceControl uploadDownloadManagementServiceControl;

	 
    // LLO- 118 - Change for Ematrix UI
	private boolean isTopLevelUser = false;
	private boolean islaslinkCustomer = false;
    
    
    public List getFileList() {
		return fileList;
	}

	public void setFileList(List fileList) {
		this.fileList = fileList;
	}

	public FormFile getTheFile() {
		return theFile;
	}

	public void setTheFile(FormFile theFile) {
		this.theFile = theFile;
	}

	public String getStrFileName() {
		return strFileName;
	}
	
	/**
	 * @return the islaslinkCustomer
	 */
	public boolean isIslaslinkCustomer() {
		return islaslinkCustomer;
	}

	/**
	 * @param islaslinkCustomer the islaslinkCustomer to set
	 */
	public void setIslaslinkCustomer(boolean islaslinkCustomer) {
		this.islaslinkCustomer = islaslinkCustomer;
	}

    /**
     * @jpf:action
     * @jpf:forward name="success" path="manageUpload.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "manageUpload.do")
    })
    protected Forward begin()
    {    	
		getLoggedInUserPrincipal();
		
		getUserDetails();

		setupUserPermission();

        List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);		
        this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
        
   		return new Forward("success");
    }
	
	
    /**
     * @jpf:action
     * @jpf:forward name="success" path="manage_upload.jsp"
     * @jpf:forward name="viewUploads" path="viewUploads.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "uploadData.jsp")
    })
    protected Forward manageUpload()
    {       
    	String showViewUpload = this.getRequest().getParameter("showViewUpload");
    	if (showViewUpload != null)
    		this.getRequest().setAttribute("showViewUpload", "true");   

        return new Forward("success");
    }
    
    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward downloadTemplate()
    {         
        HttpServletResponse resp = this.getResponse();     
        byte[] data = null;   
        String fileName = "UserTemplate.xls";
        String fileContent = "";

        String downloadFile = (String)this.getRequest().getParameter("downloadFile");
        if ((downloadFile != null) && downloadFile.equals("studentFile"))
            fileName = "StudentTemplate.xls";

        System.out.println(fileName);
        
        String bodypart = "attachment; filename=\"" + fileName + "\" ";

        prepareContentHeader(resp, bodypart);

        data = getTemplate(fileName);
            
        try
        {
            
            if (data != null)
            {
                resp.flushBuffer();
                OutputStream stream = resp.getOutputStream();
                stream.write(data);
                stream.close();
            }
                            
        }
        catch (IOException e)
        {
                
            e.printStackTrace();
            
        }                
        return null;
    }
    
    
	@Jpf.Action()
    protected Forward populateDownloadTemplateListGrid()
    {
        HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		
		Row row1 = new Row("1");		
		String[] atts1 = new String[2];
		atts1[0] = "User Template";
		atts1[1] = "Format for user profiles to upload.";
		row1.setCell(atts1);

		Row row2 = new Row("2");		
		String[] atts2 = new String[2];
		atts2[0] = "Student Template";
		atts2[1] = "Format for student profiles to upload.";
		row2.setCell(atts2);
		
		ArrayList rows = new ArrayList();
		rows.add(row1);
		rows.add(row2);
		
		UploadDownload base = new UploadDownload();
		base.setRows(rows);
		
        Gson gson = new Gson();
        String jsonData = gson.toJson(base);
		
        System.out.println(jsonData);
        
		try{
	       	try {
	   			resp.setContentType("application/json");
 	   			stream = resp.getOutputStream();
	   			stream.write(jsonData.getBytes("UTF-8"));
	   			resp.flushBuffer();
	   		} catch (IOException e) {
	   			e.printStackTrace();
	   		} 
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
        return null;
    }
    

	@Jpf.Action()
    protected Forward populateUploadListGrid()
    {
        HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		ArrayList rows = new ArrayList();
		
		
		DataFileAuditData dataFileAuditData = findFile(); 
        this.fileList = UploadHistoryUtils.buildAuditFileList(dataFileAuditData);
		
		String[] atts = new String[5];
		
        for (int i=0 ; i<this.fileList.size() ; i++) {
        	AuditFileHistory afh = (AuditFileHistory)this.fileList.get(i);
        	
        	Integer dataFileAuditId = afh.getDataFileAuditId();
        	
        	String rowId = dataFileAuditId.intValue() + "_" + afh.getStatusCode();
    		Row row = new Row(rowId);
    		
    		atts = new String[5];
    		atts[0] = afh.getCreatedDateTime();
    		atts[1] = afh.getUploadFileName();
    		atts[2] = afh.getUploadFileRecordCount();
    		atts[3] = afh.getFailedRecordCount();
    		atts[4] = afh.getStatus();
    		
    		row.setCell(atts);

    		rows.add(row);
        }
        
		UploadDownload base = new UploadDownload();
		base.setPage("1");
		base.setRows(rows);
		
        Gson gson = new Gson();
        String jsonData = gson.toJson(base);
		
        System.out.println(jsonData);
        
		try{
	       	try {
	   			resp.setContentType("application/json");
 	   			stream = resp.getOutputStream();
	   			stream.write(jsonData.getBytes("UTF-8"));
	   			resp.flushBuffer();
	   		} catch (IOException e) {
	   			e.printStackTrace();
	   		} 
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
        
        return null;
    }

    /**
     * findByCustomerProfile
     */
    private DataFileAuditData findFile() {
        
        DataFileAuditData dataFileAuditData = null;
        SortParams sort = FilterSortPageUtils.buildFileSortParams(
        		FilterSortPageUtils.FILE_DATE_DEFAULT_SORT_COLUMN, 
        		FilterSortPageUtils.DESCENDING);
       
        try {
        	validateUser();
        	
            dataFileAuditData = this.uploadDownloadManagement.getUploadHistory(
                                        this.userName, null, sort);   
                                           
            
        } catch(CTBBusinessException be){        
                be.printStackTrace();
        } 
    
        return dataFileAuditData;
    }
	
    /**
     * @jpf:action
     * @jpf:forward name="success" path="manageUpload.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "manageUpload.do")
    })
    protected Forward uploadData()
    {       
    	
        String isSuccess = readFileContent();
        
        if (!RETURN_SUCCESS.equals(isSuccess))
        {
        	System.out.println("isSuccess=" + isSuccess);
            
            if ("1".equals(isSuccess))
            {
                this.getRequest().setAttribute("uploadMsg", "Please select a valid path and .xls data file to continue.");
                return new Forward("success");
            }
            else
            {
                this.getRequest().setAttribute("uploadMsg", "Failed to upload <b>" + this.strFileName + "</b> file. Please try again.");
                return new Forward("success");
            }
        }
        else
        {
            
            boolean isSuccessful = writeFileContent();
        	System.out.println("isSuccessful=" + isSuccessful);
        	
            if (isSuccessful) 
            	this.getRequest().setAttribute("uploadMsg", "File <b>" + this.strFileName + "</b> was uploaded successfully.");
            else
                this.getRequest().setAttribute("uploadMsg", this.uploadMessage);
        }
    	
        System.out.println("***** Upload App: returning control to user");
        return new Forward("success");
    }
	
    /**
     * @jpf:action
     * @jpf:forward name="success" path="manageUpload.do"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "manageUpload.do")
		}
	)
    protected Forward deleteErrorDataFile()
    {
        try {
            String selectedId = (String)this.getRequest().getParameter("selectedId");
            if (selectedId != null) {
            	String[] strSplit = selectedId.split("_");
            	Integer selectedAuditId = new Integer(strSplit[0]);            	
            	this.uploadDownloadManagement.deleteErrorDataFile(selectedAuditId);
            }
            
        } catch (Exception e) {            
            e.printStackTrace();
        }
        
        this.getRequest().setAttribute("showViewUpload", "true");   
        
        return new Forward("success");
    }
    
	
	 /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward getErrorDataFile()
    {
        HttpServletResponse resp = this.getResponse();   
        byte []errorFile = null;
        Integer selectedAuditId = new Integer(0);       
        
        String selectedId = (String)this.getRequest().getParameter("selectedId");
        if (selectedId != null) {
        	String[] strSplit = selectedId.split("_");
        	selectedAuditId = new Integer(strSplit[0]);            	
        }
        
        try
        {
        	validateUser();
        	
            errorFile = this.uploadDownloadManagement.getErrorDataFile(
                        this.userName, selectedAuditId);   
         
            String fileName = UploadHistoryUtils.getFileName(selectedAuditId, this.fileList);
                                        
            String errorFileName = getErrorFileName(fileName);
            String bodypart = "attachment; filename=\"" + errorFileName + "\" ";

            prepareContentHeader(resp, bodypart);
            
            resp.flushBuffer();
            OutputStream stream = resp.getOutputStream();
            stream.write(errorFile);
            stream.close();
        
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
	
    private class UploadThread extends Thread {
        private String userName;
        private String fullFilePath;
        private String instanceURL;
        private Integer uploadFileId;
        
        public UploadThread(String userName, String fullFilePath, Integer uploadDataFileId, String instanceURL) {
            this.userName = userName;
            this.fullFilePath = fullFilePath;
            this.instanceURL = instanceURL;
            this.uploadFileId = uploadDataFileId;
        }
        
        public void run() {
            invokeService(this.userName, this.fullFilePath, this.uploadFileId, this.instanceURL, 1);
        }
    }
    
    private void invokeService(String userName, String fullFilePath, Integer uploadFileId, String instanceURL, int trycount) {
    	try {
            System.out.println("***** Upload App: invoking process service: " + this.userName + " : " + saveFileName);
            String endpoint = instanceURL + "/platform-webservices/UploadDownloadManagement";
            uploadDownloadManagementServiceControl.setEndPoint(new URL(endpoint));
            System.out.println("***** Upload App: using service endpoint: " + endpoint);
            uploadDownloadManagementServiceControl.uploadFile(this.userName, fullFilePath, uploadFileId);
        } catch (com.ctb.webservices.CTBBusinessException e) {
            DataFileAudit dataFileAudit = new DataFileAudit();
            dataFileAudit.setStatus("FL");
            try{
                uploadDownloadManagement.updateAuditFileStatus(uploadFileId);
            } catch (Exception se) {
                se.printStackTrace();
            }
        } catch (Exception e) {
            if(trycount < 5 && "getMethodName".equals(e.getStackTrace()[0].getMethodName())) {
            	System.out.println("***** Service invocation failed, trying again - " + trycount);
            	invokeService(userName, fullFilePath, uploadFileId, instanceURL, trycount++);
            } else {
            	System.out.println("****************** start EXCEPTION in invokeService ***************** ");
            	System.out.println("getMethodName = " + e.getStackTrace()[0].getMethodName());
            	System.out.println(e.getMessage());
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
            	System.out.println("****************** end EXCEPTION in invokeService ***************** ");
            	throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * writeFileContent
     * @param form
     * @returns boolean
     */
    private boolean writeFileContent() 
    {
        String fullFilePath = CTBConstants.SERVER_FOLDER_NAME + "/" + this.saveFileName;
 
        try{
            Integer uploadDataFileId = uploadDownloadManagement.addErrorDataFile(this.userName, fullFilePath, this.uploadDataFileId);
                     
            ResourceBundle rb = ResourceBundle.getBundle("security");
            String processURL = rb.getString("processURL");
            
                                    
            final Thread uploadThread = new UploadThread(this.userName, fullFilePath, uploadDataFileId, processURL);
            uploadThread.start();
            
            /*PathFinderUtils.saveFileToDB(fullFilePath , 
                                         this.uploadDownloadManagement, 
                                         this.userName,uploadDataFileId);
            */
            
            return true;         
        } catch(Exception be ) {
            be.printStackTrace();
            this.uploadMessage = MessageResourceBundle.getMessage(be.getMessage());
            return false;
        }

    }
    
    /*
     * getTemplate
     * It returns the content of the template
     * @param fileName
     * @returns String
    */
    private byte [] getTemplate(String fileName) {
        
        String fileContent = "";
        byte[] data = null;
        
        try {
        
            if ( fileName.equals("UserTemplate.xls") ) {
        
                UserFile userFile = uploadDownloadManagement.
                                    getUserFileTemplate(this.userName);
                data = UploadDownloadFormUtils.createTemplateFile(userFile, this.userName, userManagement);      
                //fileContent = UploadDownloadFormUtils.createTemplate(userFile);                                               
        
            } else {
        
                StudentFile studentFile = uploadDownloadManagement.
                                    getStudentFileTemplate(this.userName);
                                    
                data = UploadDownloadFormUtils.createStudentTemplateFile(studentFile,
                        this.userName, userManagement);                    
                //fileContent = UploadDownloadFormUtils.
                                    //createStudentTemplate(studentFile);
        
            }
        
        } catch (CTBBusinessException be) {

            be.printStackTrace();
 
        }
        
        return data; 
    }
    
    private static String getErrorFileName(String fileName) {
		
		int position = fileName.lastIndexOf(".");
		fileName = fileName.substring(0,position);
	/*	position = fileName.lastIndexOf("_");
		fileName = fileName.substring(0,position);
		position = fileName.lastIndexOf("_");
		fileName = fileName.substring(0,position);
		position = fileName.indexOf("/");
		fileName = fileName.substring(position + 1,fileName.length());*/
		fileName = fileName + "_Error" + ".xls";
		System.out.println(fileName);
		return fileName;
		
	}
    
    
    /**
     * prepareContentHeader
     */
    private void prepareContentHeader(HttpServletResponse resp, String bodypart)
    {         
        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", bodypart);      
        resp.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");        
        resp.setHeader("Cache-Control", "cache");
        resp.setHeader("Pragma", "public");        
    }
        
    
    
    /**
     * 
     */
    private String readFileContent()
    {
		this.strFileName = this.theFile.getFileName();
        
        if ( !UploadDownloadFormUtils.verifyFileExtension(this.strFileName) ) {
        
            return "1";
        
        }
        
        int filesize = (this.theFile.getFileSize());
        
        if ( (filesize == 0) || (this.strFileName.length() == 0) ) {
            
            return "2";
        
        }
            
        try {
            this.saveFileName = PathFinderUtils.getSaveFileName(
                                          this.userName,  
                                          this.strFileName,
                                          this.userManagement);
                                          
            this.uploadDataFileId = PathFinderUtils.saveFileToDBTemp(saveFileName, this.theFile, this.uploadDownloadManagement);
                      
            return RETURN_SUCCESS;
        
        } catch (Exception e) {
        
            e.printStackTrace();    
        
        }
        
        return "False";
    }
    
    
	/////////////////////////////////////////////////////////////////////////////////////////////    
	///////////////////////////// BEGIN OF NEW NAVIGATION ACTIONS ///////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////    
	
    /**
	 * ASSESSMENTS actions
	 */    
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "sessionsLink", path = "assessments_sessionsLink.do"),
			@Jpf.Forward(name = "programStatusLink", path = "assessments_programStatusLink.do"),
			@Jpf.Forward(name = "studentRegistrationLink", path = "assessments_studentRegistrationLink.do")
	})   
	protected Forward assessments()
	{
        
		String menuId = (String)this.getRequest().getParameter("menuId");    	
		String forwardName = (menuId != null) ? menuId : "sessionsLink";
		
	    return new Forward(forwardName);	    
	}
	

    @Jpf.Action()
	protected Forward assessments_sessionsLink()
	{
        try
        {
            String url = "/SessionWeb/sessionOperation/assessments_sessions.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
    
    @Jpf.Action()
	protected Forward assessments_programStatusLink()
	{
        try
        {
            String url = "/SessionWeb/programOperation/assessments_programStatus.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
    
    /**
     * STUDENT REGISTRATION actions
     */
    @Jpf.Action()
    protected Forward assessments_studentRegistrationLink()
    {
        try
        {
        	String url = "/RegistrationWeb/registrationOperation/beginStudentRegistration.do";
        	getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
			
	
    /**
	  * ORGANIZATIONS actions
	  */    
	 @Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "studentsLink", path = "organizations_manageStudents.do"),
			 @Jpf.Forward(name = "usersLink", path = "organizations_manageUsers.do"),
			 @Jpf.Forward(name = "organizationsLink", path = "organizations_manageOrganizations.do"),
			 @Jpf.Forward(name = "bulkAccomLink", path = "organizations_manageBulkAccommodation.do"),
		        @Jpf.Forward(name = "bulkMoveLink", path = "organizations_manageBulkMove.do"),
		        @Jpf.Forward(name = "OOSLink", path = "organizations_manageOutOfSchool.do")
	 }) 
	 protected Forward organizations()
	 {
		 String menuId = (String)this.getRequest().getParameter("menuId");    	
		 String forwardName = (menuId != null) ? menuId : "studentsLink";

		 return new Forward(forwardName);
	 }
    

	 @Jpf.Action()
	 protected Forward organizations_manageOrganizations()
	 {
		 try
		 {
			 String url = "/OrganizationWeb/orgOperation/organizations_manageOrganizations.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;
	 }

	 @Jpf.Action()
	 protected Forward organizations_manageStudents()
	 {
		 try
		 {
			 String url = "/StudentWeb/studentOperation/organizations_manageStudents.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;
	 }

	 @Jpf.Action()
	 protected Forward organizations_manageBulkAccommodation()
	 {
		 try
		 {
			 String url = "/StudentWeb/bulkOperation/organizations_manageBulkAccommodation.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;
	 }

	 @Jpf.Action() 
	 protected Forward organizations_manageUsers()
	 {
		 try
		 {
			 String url = "/UserWeb/userOperation/organizations_manageUsers.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;

	 }

	 @Jpf.Action()
	 protected Forward organizations_manageBulkMove()
	 {
		 try
		 {
			 String url = "/StudentWeb/bulkMoveOperation/organizations_manageBulkMove.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;
	 }
	 
	 @Jpf.Action()
		protected Forward organizations_manageOutOfSchool()
		{
	        try
	        {
	            String url = "/StudentWeb/outOfSchoolOperation/organizations_manageOutOfSchool.do";
	            getResponse().sendRedirect(url);
	        } 
	        catch (IOException ioe)
	        {
	            System.err.print(ioe.getStackTrace());
	        }
	        return null;
		}


	 /**
	  * REPORTS actions
	  */    
	 @Jpf.Action()
	 protected Forward reports()
	 {
		 try
		 {
			 String url = "/SessionWeb/sessionOperation/reports.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;
	 }


	 /**
	  * SERVICES actions
	  */    
	 @Jpf.Action(forwards = { 
	         @Jpf.Forward(name = "resetTestSessionLink", path = "services_resetTestSession.do"),
			 @Jpf.Forward(name = "manageLicensesLink", path = "services_manageLicenses.do"),
			 @Jpf.Forward(name = "installSoftwareLink", path = "services_installSoftware.do"),
			 @Jpf.Forward(name = "downloadTestLink", path = "services_downloadTest.do"),
			 @Jpf.Forward(name = "uploadDataLink", path = "services_uploadData.do"),
			 @Jpf.Forward(name = "downloadDataLink", path = "services_downloadData.do"),
			 @Jpf.Forward(name = "exportDataLink", path = "services_dataExport.do"),
			 @Jpf.Forward(name = "viewStatusLink", path = "services_viewStatus.do"),
			 @Jpf.Forward(name = "showAccountFileDownloadLink", path = "eMetric_user_accounts_detail.do")
	 }) 
	 protected Forward services()
	 {
		 String menuId = (String)this.getRequest().getParameter("menuId");    	
		 String forwardName = (menuId != null) ? menuId : "installSoftwareLink";

		 return new Forward(forwardName);
	 }
	 @Jpf.Action()
		protected Forward eMetric_user_accounts_detail()
		{
	        try
	        {
	            String url = "/SessionWeb/userAccountFileOperation/accountFiles.do";
	            getResponse().sendRedirect(url);
	        } 
	        catch (IOException ioe)
	        {
	            System.err.print(ioe.getStackTrace());
	        }
	        return null;
		} 
	 @Jpf.Action()
	    protected Forward services_dataExport()
	    {
	    	try
	    	{
	    		String url = "/ExportWeb/dataExportOperation/services_dataExport.do";
	    		getResponse().sendRedirect(url);
	    	}
	    	catch (IOException ioe)
	        {
	            System.err.print(ioe.getStackTrace());
	        }
	    	return null;
	    }
	 


	    @Jpf.Action()
		protected Forward services_viewStatus()
		{
	        try
	        {
	            String url = "/ExportWeb/dataExportOperation/beginViewStatus.do";
	            getResponse().sendRedirect(url);
	        } 
	        catch (IOException ioe)
	        {
	            System.err.print(ioe.getStackTrace());
	        }
	        return null;
		}

	    @Jpf.Action()
	    protected Forward services_resetTestSession()
	    {
	        try
	        {
	            String url = "/OrganizationWeb/resetOperation/services_resetTestSession.do";
	            getResponse().sendRedirect(url);
	        } 
	        catch (IOException ioe)
	        {
	            System.err.print(ioe.getStackTrace());
	        }
	        return null;
	    }
	 
	 @Jpf.Action()
	 protected Forward services_manageLicenses()
	 {
		 try
		 {
			 String url = "/OrganizationWeb/licenseOperation/services_manageLicenses.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;
	 }

	 @Jpf.Action()
	 protected Forward services_installSoftware()
	 {
		 try
		 {
			 String url = "/SessionWeb/softwareOperation/services_installSoftware.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;
	 }

	 @Jpf.Action()
	 protected Forward services_downloadTest()
	 {
		 try
		 {
			 String url = "/SessionWeb/testContentOperation/services_downloadTest.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;
	 }

	 @Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "success", path = "begin.do") 
	 }) 
	 protected Forward services_uploadData()
	 {
		 return new Forward("success");
	 }

	 @Jpf.Action()
	 protected Forward services_downloadData()
	 {
		 try
		 {
			 String url = "/OrganizationWeb/downloadOperation/services_downloadData.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;
	 }    
    
	/**
	 * @jpf:action
	 */
	@Jpf.Action()
	protected Forward broadcastMessage()
	{
	    return null;
	}
	
	
	@Jpf.Action()
	protected Forward myProfile()
	{
	    return null;
	}
	
	/**
     * STUDENT SCORING actions
     */    
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "studentScoringLink", path = "scoring_studentScoring.do")
        }) 
    protected Forward studentScoring()
    {
    	String menuId = (String)this.getRequest().getParameter("menuId");    	
    	String forwardName = (menuId != null) ? menuId : "studentScoringLink";
    	
        return new Forward(forwardName);
    }
    
    @Jpf.Action()
	protected Forward scoring_studentScoring()
	{
        try
        {
            String url = "/ScoringWeb/studentScoringOperation/beginStudentScoring.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}


    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
    private void getLoggedInUserPrincipal()
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
        if (principal != null) {
            this.userName = principal.toString();
        }        
        getSession().setAttribute("userName", this.userName);
    }
    
    private void validateUser()
    {
    	if (this.userName == null) {
    		java.security.Principal principal = getRequest().getUserPrincipal();
    		if (principal != null) {
    			this.userName = principal.toString();
    		}
    		else {
    			this.userName = (String)getSession().getAttribute("userName");
    		}
    	}
    }
    
    private void getUserDetails()
    {
        Boolean supportAccommodations = Boolean.TRUE;
        String userTimeZone = "GMT";
             	
        try
        {
            this.user = this.testSessionStatus.getUserDetails(this.userName, this.userName);
            Customer customer = this.user.getCustomer();
            this.customerId = customer.getCustomerId();
            getSession().setAttribute("customerId", customerId); 
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
    }
    
	private void setupUserPermission()
	{
        CustomerConfiguration [] customerConfigs = getCustomerConfigurations(this.customerId);
        boolean adminUser = isAdminUser();
        boolean TABECustomer = isTABECustomer(customerConfigs);
        boolean laslinkCustomer = isLaslinkCustomer(customerConfigs);
        boolean adminCoordinatorUser = isAdminCoordinatotUser(); //For Student Registration
    	boolean hasResetTestSession = false;
    	boolean hasResetTestSessionForAdmin = false;
    	boolean isOKCustomer = false;
    	boolean isGACustomer = false;
    	boolean isTopLevelAdmin = new Boolean(isTopLevelUser() && isAdminUser());
    	boolean hasUploadConfig = false;
    	boolean hasDownloadConfig = false;
    	boolean hasUploadDownloadConfig = false;
    	        
        this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));

        this.getSession().setAttribute("isBulkAccommodationConfigured",customerHasBulkAccommodation(customerConfigs));    	
    	
        this.getSession().setAttribute("hasProgramStatusConfigured", 
        		new Boolean( hasProgramStatusConfig().booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasScoringConfigured", 
        		new Boolean( customerHasScoring(customerConfigs).booleanValue()));
        
        this.getSession().setAttribute("canRegisterStudent", canRegisterStudent(customerConfigs));
        
     	this.getSession().setAttribute("hasLicenseConfigured", hasLicenseConfiguration(customerConfigs).booleanValue() && adminUser);

		this.getSession().setAttribute("isBulkMoveConfigured",customerHasBulkMove(customerConfigs));
     	
     	this.getSession().setAttribute("adminUser", new Boolean(adminUser));
     	
     	this.getSession().setAttribute("isOOSConfigured",customerHasOOS(customerConfigs));	// Changes for Out Of School
     	this.getSession().setAttribute("hasRapidRagistrationConfigured", new Boolean(TABECustomer && (adminUser || adminCoordinatorUser) ));//For Student Registration

		for (int i=0; i < customerConfigs.length; i++) {
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Reopen_Subtest") && 
            		cc.getDefaultValue().equals("T")	) {
				hasResetTestSession = true;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Reopen_Subtest_For_Admin") && 
            		cc.getDefaultValue().equals("T")	) {
				hasResetTestSessionForAdmin = true;
				continue;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("OK_Customer")
					&& cc.getDefaultValue().equals("T")) {
            	isOKCustomer = true;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("GA_Customer") 
					&& cc.getDefaultValue().equalsIgnoreCase("T")) {
				isGACustomer = true;
				continue;
			}
			// For Upload Download
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Upload")
					&& cc.getDefaultValue().equals("T")) {
				hasUploadConfig = true;
				continue;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Download")
					&& cc.getDefaultValue().equals("T")) {
				hasDownloadConfig = true;
				continue;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Upload_Download")
					&& cc.getDefaultValue().equals("T")) {
				hasUploadDownloadConfig = true;
				continue;
            }
		}        
		
		if (hasUploadConfig && hasDownloadConfig) {
			hasUploadDownloadConfig = true;
		}
		if (hasUploadDownloadConfig) {
			hasUploadConfig = false;
			hasDownloadConfig = false;
		}
		
		this.getSession().setAttribute("hasUploadConfigured",new Boolean(hasUploadConfig && adminUser));
		this.getSession().setAttribute("hasDownloadConfigured",new Boolean(hasDownloadConfig && adminUser));
		this.getSession().setAttribute("hasUploadDownloadConfigured",new Boolean(hasUploadDownloadConfig && adminUser));
		
		this.getSession().setAttribute("hasResetTestSession", new Boolean((hasResetTestSession && hasResetTestSessionForAdmin) && ((isOKCustomer && isTopLevelAdmin)||(laslinkCustomer && isTopLevelAdmin)||(isGACustomer && adminUser))));
		this.getSession().setAttribute("showDataExportTab",laslinkCustomer);
		//show Account file download link      	
     	this.getSession().setAttribute("isAccountFileDownloadVisible", new Boolean(laslinkCustomer && isTopLevelAdmin));
	}
	
	private boolean isTopLevelUser(){
		boolean isUserTopLevel = false;
		try {
			isUserTopLevel = orgnode.checkTopOrgNodeUser(this.userName);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUserTopLevel;
	}
	
	private boolean isAdminCoordinatotUser() //For Student Registration
    {               
        String roleName = this.user.getRole().getRoleName();        
        return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR); 
    }
	
    private Boolean userHasReports() 
    {
        boolean hasReports = false;
        try
        {      
            Customer customer = this.user.getCustomer();
            Integer customerId = customer.getCustomerId();   
            hasReports = this.testSessionStatus.userHasReports(this.userName, customerId);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }        
        return new Boolean(hasReports);           
    }

    private boolean isAdminUser() 
    {               
        String roleName = this.user.getRole().getRoleName();        
        return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR); 
    }
    
    private Boolean canRegisterStudent(CustomerConfiguration [] customerConfigs) 
    {               
        String roleName = this.user.getRole().getRoleName();        
        boolean validCustomer = false; 

        for (int i=0; i < customerConfigs.length; i++)
        {
            CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer"))
            {
                validCustomer = true; 
            }               
        }
        
        boolean validUser = (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || 
        		roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
        
        return new Boolean(validCustomer && validUser);
    }
    
    private Boolean hasLicenseConfiguration(CustomerConfiguration [] customerConfigs)
    {               
    	 boolean hasLicenseConfiguration = false;

        for (int i=0; i < customerConfigs.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subscription") && 
            		cc.getDefaultValue().equals("T")	) {
            	hasLicenseConfiguration = true;
                break;
            } 
        }
       
        return new Boolean(hasLicenseConfiguration);
    }
    
    private Boolean customerHasScoring(CustomerConfiguration [] customerConfigs)
    {               
        Integer customerId = this.user.getCustomer().getCustomerId();
        boolean hasScoringConfigurable = false;
        
        for (int i=0; i < customerConfigs.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
            		cc.getDefaultValue().equals("T")	) {
            	hasScoringConfigurable = true;
            } 
        }
        return new Boolean(hasScoringConfigurable);
    }

    private boolean isLaslinkCustomer(CustomerConfiguration [] customerConfigs)
    {               
        boolean laslinkCustomer = false;
        
        for (int i=0; i < customerConfigs.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")
					&& cc.getDefaultValue().equals("T")) {
            	laslinkCustomer = true;
            }
        }
        return laslinkCustomer;
    }

    private boolean isTABECustomer(CustomerConfiguration [] customerConfigs)
    {               
        boolean TABECustomer = false;
        
        for (int i=0; i < customerConfigs.length; i++)
        {
        	CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer")) {
            	TABECustomer = true;
            }
        }
        return TABECustomer;
    }
    
    private CustomerConfiguration [] getCustomerConfigurations(Integer customerId)
    {               
        CustomerConfiguration [] ccArray = null;
        try
        {      
            ccArray = this.testSessionStatus.getCustomerConfigurations(this.userName, customerId);       
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }        
        return ccArray;
    }
    
    private Boolean hasUploadDownloadConfig()
    {
        Boolean hasUploadDownloadConfig = Boolean.FALSE;
        try {   
            hasUploadDownloadConfig = this.testSessionStatus.hasUploadDownloadConfig(this.userName);
        } 
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return hasUploadDownloadConfig;
    }

    private Boolean hasProgramStatusConfig()
    {	
        Boolean hasProgramStatusConfig = Boolean.FALSE;
        try {   
            hasProgramStatusConfig = this.testSessionStatus.hasProgramStatusConfig(this.userName);
        } 
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return hasProgramStatusConfig;
    }
    
    /**
	 * Bulk Accommodation
	 */
	private Boolean customerHasBulkAccommodation(CustomerConfiguration[] customerConfigurations) 
	{
		boolean hasBulkStudentConfigurable = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentConfigurable = true; 
					break;
				}
			}
		}
		return new Boolean(hasBulkStudentConfigurable);           
	}
	
	/**
	 * Bulk Move
	 */
	private Boolean customerHasBulkMove(CustomerConfiguration[] customerConfigurations) 
	{
		boolean hasBulkStudentConfigurable = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Bulk_Move_Students") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentConfigurable = true; 
					break;
				}
			}
		}
		return new Boolean(hasBulkStudentConfigurable);           
	}
	
	// Changes for Out Of School
	/**
	 * Out Of School
	 */
	private Boolean customerHasOOS(CustomerConfiguration[] customerConfigurations) 
	{
		boolean hasOOSConfigurable = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("OOS_Configurable") && 
						cc.getDefaultValue().equals("T")) {
					hasOOSConfigurable = true; 
					break;
				}
			}
		}
		return new Boolean(hasOOSConfigurable);           
	}

    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// END OF SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    

}