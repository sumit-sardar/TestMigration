package uploadOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import manageUpload.UploadDownloadManagementServiceControl;
import manageUpload.ManageUploadController.ManageUploadForm;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import utils.FilterSortPageUtils;
import utils.MessageResourceBundle;
import utils.PathFinderUtils;
import utils.PermissionsUtils;
import utils.Row;
import utils.UploadDownload;
import utils.UploadDownloadFormUtils;
import utils.UploadHistoryUtils;

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
    
    
    // LLO- 118 - Change for Ematrix UI
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;
    
    private final int MAXBUFFER = 100 * 1048576; 	
    
    private String userName = null;
    private ManageUploadForm savedForm = null;
    public String strFileName = null;
    
    public String uploadStatus = null;

    
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
    
    private static final String ACTION_ELEMENT = "{actionForm.selectedTab}";


	@Control
	private UploadDownloadManagementServiceControl uploadDownloadManagementServiceControl;

	 
    // LLO- 118 - Change for Ematrix UI
	private boolean isTopLevelUser = false;
	private boolean islaslinkCustomer = false;
    
    
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

   		return new Forward("success");
    }
	
	
    /**
     * @jpf:action
     * @jpf:forward name="success" path="manage_upload.jsp"
     * @jpf:forward name="viewUploads" path="viewUploads.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "uploadData.jsp"), 
        @Jpf.Forward(name = "viewUploads",
                     path = "viewUploads.do")
    })
    protected Forward manageUpload(ManageUploadForm form)
    {         
    	/*
        String selectedTab = form.getSelectedTab();
        selectedTab = JavaScriptSanitizer.sanitizeString(selectedTab);

        this.getRequest().setAttribute("selectedModule", selectedTab);   
        this.uploadStatus = "uploadFile";   
        this.getRequest().setAttribute("uploadStatus", "uploadFile");
                
        if (selectedTab != null && MODULE_VIEW_UPLOADS.equals(selectedTab))
        {            
            form.setCurrentAction(ACTION_APPLY_SEARCH);
            return new Forward("viewUploads", form);        
        }
        setFormInfoOnRequest(form);
        */
    	
        return new Forward("success", form);
    }
    
    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward downloadTemplate(ManageUploadForm form)
    {         
        HttpServletResponse resp = this.getResponse();     
        byte[] data = null;   
        String fileName = form.getFileName();
        String fileContent = "";
        String bodypart = "attachment; filename=\"" +
                          fileName +
                          "\" ";

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
    
    private void setFormInfoOnRequest(ManageUploadForm form) {
    	this.getRequest().setAttribute("pageMessage", form.getMessage());
    }
    
	@Jpf.Action()
    protected Forward populateDownloadTemplateListGrid()
    {
        HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		
		Row row1 = new Row(1);		
		String[] atts1 = new String[2];
		atts1[0] = "User Template";
		atts1[1] = "Format for user profiles to upload.";
		row1.setCell(atts1);

		Row row2 = new Row(2);		
		String[] atts2 = new String[2];
		atts2[0] = "Student Template";
		atts2[1] = "Format for student profiles to upload.";
		row2.setCell(atts2);
		
		ArrayList rows = new ArrayList();
		rows.add(row1);
		rows.add(row2);
		
		UploadDownload base = new UploadDownload();
		//base.setPage("1");
		//base.setRecords("2");
		//base.setTotal("2");
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
     * @jpf:action
     * @jpf:forward name="success" path="manage_upload.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "manage_upload.jsp")
    })
    protected Forward uploadData(ManageUploadForm form)
    {           

        String isSuccess = readFileContent(form);
        
        this.getRequest().setAttribute("selectedModule", form.getSelectedTab());   

        if (!RETURN_SUCCESS.equals(isSuccess))
        {
            
            if ("1".equals(isSuccess))
            {
                this.getRequest().setAttribute("noFileSelected", "true");   
                this.uploadStatus = "uploadFile";   
                this.getRequest().setAttribute("uploadStatus", "uploadFile");
                return new Forward("success", form);
            }
            else
            {
                this.getRequest().setAttribute("failToUpload", "true");   
                this.uploadStatus = "uploadFile";   
                this.getRequest().setAttribute("uploadStatus", "uploadFile");
                return new Forward("success", form);
            }
        }
        else
        {
            
            boolean isSuccessful = writeFileContent(form);
                        
            if (isSuccessful)
            {
            
                this.uploadStatus = "uploadDone";  
                this.getRequest().setAttribute("uploadStatus", "uploadDone");
            
            }
            else
            {
            
            	this.uploadStatus = "uploadFile";
            	this.getRequest().setAttribute("uploadStatus", "uploadFile");
            
            }  
            
        }
  
        System.out.println("***** Upload App: returning control to user");
        setFormInfoOnRequest(form) ; //Added for defect 61360     
        return new Forward("success", form);
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
    }
    
    /**
     * writeFileContent
     * @param form
     * @returns boolean
     */
    private boolean writeFileContent(ManageUploadForm form) 
    {
        String fullFilePath = CTBConstants.SERVER_FOLDER_NAME + "/" + this.saveFileName;
 
        try{
            Integer uploadDataFileId = uploadDownloadManagement.addErrorDataFile(this.userName, fullFilePath, form.getAuditFileId());
                     
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
            String msg = MessageResourceBundle.getMessage(be.getMessage());
            form.setMessage(Message.UPLOAD_TITLE, msg, Message.ERROR);
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
    private String readFileContent(ManageUploadForm form)
    {
		this.strFileName = form.theFile.getFileName();
        
        if ( !UploadDownloadFormUtils.verifyFileExtension(this.strFileName) ) {
        
            return "1";
        
        }
        
        int filesize = (form.theFile.getFileSize());
        
        if ( (filesize == 0) || (this.strFileName.length() == 0) ) {
            
            return "2";
        
        }
            
        try {
            this.saveFileName = PathFinderUtils.getSaveFileName(
                                          this.userName,  
                                          this.strFileName,
                                          this.userManagement);
                                          

            //PathFinderUtils.saveFileToDBTemp(saveFileName, form, this.uploadDownloadManagement);
            
            System.out.println("***** Upload App: File written to data_file_temp: " + form.getAuditFileId());
                      
            return RETURN_SUCCESS;
        
        } catch (Exception e) {
        
            e.printStackTrace();    
        
        }
        
        return "False";
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="manage_upload.jsp"
     * @jpf:forward name="gotoNextAction" path="gotoNextAction.do" redirect="true"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "uploadData.jsp")
    })
    protected Forward viewUploads(ManageUploadForm form)
    {      
    	/*
        form.validateValues();
        form.setFilter("My uploads");
        DataFileAuditData dataFileAuditData = null;
        
        String currentAction = form.getCurrentAction();
        String actionElement = form.getActionElement();
        
        form.resetValuesForAction(actionElement, ACTION_VIEW_UPLOADS);
       
        if (currentAction.equals(ACTION_DELETE_FILE) || currentAction.equals(ACTION_EXPORT_ERROR_FILE))
        {
                    
            AuditFileHistory uploadInfo = 
                 UploadDownloadFormUtils.getFileFromList(form.getSelectedAuditId(), this.fileList);
                 
            form.setSelectedAuditId(uploadInfo.getDataFileAuditId());
            this.savedForm = form.createClone();

            return new Forward("gotoNextAction");
        }
        
        initPagingSorting(form);
        this.searchApplied = true;
        boolean applySearch = initSearch(form);
        
        if (applySearch)
        {
        
            dataFileAuditData = findFile(form); 
           
            if (dataFileAuditData != null && (dataFileAuditData.getFilteredCount().intValue() == 0))
            {
                
                this.getRequest().setAttribute("searchResultEmpty",
                                                Message.FIND_NO_FILE_RESULT);
                
                isEmptyProfileSearch = true; 
                fileList = new ArrayList();   
            }      
        
        }
            
        if (dataFileAuditData != null && (dataFileAuditData.getFilteredCount().intValue() > 0))
        {
            
            fileList = UploadHistoryUtils.buildAuditFileList(dataFileAuditData);
            PagerSummary filePagerSummary = 
                    UploadHistoryUtils.buildFilePagerSummary(dataFileAuditData, form.getFilePageRequested()); 
            form.setFileMaxPage(dataFileAuditData.getFilteredPages());
            fileMaxPage = dataFileAuditData.getFilteredPages();   
            this.getRequest().setAttribute("fileResult", "true");        
            this.getRequest().setAttribute("filePagerSummary", filePagerSummary);
           
            AuditFileHistory file = UploadHistoryUtils.findFile(fileList, form.getSelectedAuditId());
        
            if (file != null)
            {
        
                form.setActionPermission(file.getActionPermission());
                form.setSelectedAuditId(file.getDataFileAuditId());
                      
            } 
          
            if (form.getSelectedAuditId() != null)
            {
                
                PermissionsUtils.setPermissionRequestAttributeFile(this.getRequest(), form);
            
            }
            else
            {
            
                PermissionsUtils.setPermissionRequestAttributeFile(this.getRequest(), form);
                
            }
            
        }
        else
        {
            
            PermissionsUtils.setPermissionRequestAttributeFile(this.getRequest(), form);
        }
        */
        return new Forward("success", form);
    }
    
	/////////////////////////////////////////////////////////////////////////////////////////////    
	///////////////////////////// BEGIN OF NEW NAVIGATION ACTIONS ///////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////    
	
    /**
	 * ASSESSMENTS actions
	 */    
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "sessionsLink", path = "assessments_sessionsLink.do"),
			@Jpf.Forward(name = "studentScoringLink", path = "assessments_studentScoringLink.do"),
			@Jpf.Forward(name = "programStatusLink", path = "assessments_programStatusLink.do")
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
	protected Forward assessments_studentScoringLink()
	{
        try
        {
            String url = "/SessionWeb/sessionOperation/assessments_studentScoring.do";
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
	  * ORGANIZATIONS actions
	  */    
	 @Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "studentsLink", path = "organizations_manageStudents.do"),
			 @Jpf.Forward(name = "usersLink", path = "organizations_manageUsers.do"),
			 @Jpf.Forward(name = "organizationsLink", path = "organizations_manageOrganizations.do"),
			 @Jpf.Forward(name = "bulkAccomLink", path = "organizations_manageBulkAccommodation.do"),
		        @Jpf.Forward(name = "bulkMoveLink", path = "organizations_manageBulkMove.do")
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
			 @Jpf.Forward(name = "manageLicensesLink", path = "services_manageLicenses.do"),
			 @Jpf.Forward(name = "installSoftwareLink", path = "services_installSoftware.do"),
			 @Jpf.Forward(name = "downloadTestLink", path = "services_downloadTest.do"),
			 @Jpf.Forward(name = "uploadDataLink", path = "services_uploadData.do"),
			 @Jpf.Forward(name = "downloadDataLink", path = "services_downloadData.do")
	 }) 
	 protected Forward services()
	 {
		 String menuId = (String)this.getRequest().getParameter("menuId");    	
		 String forwardName = (menuId != null) ? menuId : "installSoftwareLink";

		 return new Forward(forwardName);
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
        
        this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));

        this.getSession().setAttribute("isBulkAccommodationConfigured",customerHasBulkAccommodation(customerConfigs));    	
    	
        this.getSession().setAttribute("hasUploadDownloadConfigured", 
        		new Boolean( hasUploadDownloadConfig().booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasProgramStatusConfigured", 
        		new Boolean( hasProgramStatusConfig().booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasScoringConfigured", 
        		new Boolean( customerHasScoring(customerConfigs).booleanValue() && adminUser));
        
        this.getSession().setAttribute("canRegisterStudent", canRegisterStudent(customerConfigs));
        
     	this.getSession().setAttribute("hasLicenseConfigured", hasLicenseConfiguration(customerConfigs).booleanValue() && adminUser);

		this.getSession().setAttribute("isBulkMoveConfigured",customerHasBulkMove(customerConfigs));
     	
     	this.getSession().setAttribute("adminUser", new Boolean(adminUser));     	
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

    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// END OF SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
	
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ManageUploadForm extends SanitizedFormData
    {
        private String actionPermission;

		private Integer selectedAuditId;

        private Integer fileMaxPage;

        private Integer filePageRequested;

        private String fileSortOrderBy;

        private String fileSortColumn;

        private Integer auditFileId;
        private String actionElement;
        private String currentAction;
        private String filter;
        
        private String selectedTab;
        private FormFile theFile;

        private String fileName;
        private String uploadFileName;
        
		private UploadFileInformation uploadFileInfo;
        private Message message;


        public ManageUploadForm()
        {
        }
        
        public void init()
        {
            this.actionElement = ACTION_DEFAULT;
            this.currentAction = ACTION_DEFAULT;
            this.selectedTab = MODULE_DOWNLOAD_TEMPLATE; 
           // clearSearch();
            this.selectedAuditId = null;
           // this.selectedCustomerName = null;
            this.fileSortColumn = FilterSortPageUtils.FILE_DATE_DEFAULT_SORT_COLUMN;
            this.fileSortOrderBy = FilterSortPageUtils.DESCENDING;      
            this.filePageRequested = new Integer(1);       
            this.fileMaxPage = new Integer(1);   
            this.message = new Message();   
        }
        
		public ManageUploadForm createClone() {   
            ManageUploadForm copied = new ManageUploadForm();
            copied.setActionElement(this.actionElement);
            copied.setCurrentAction(this.currentAction);
            copied.setSelectedAuditId(this.selectedAuditId);
            copied.setFileSortColumn(this.fileSortColumn);
            copied.setFileSortOrderBy(this.fileSortOrderBy);
            copied.setFilePageRequested(this.filePageRequested);
            copied.setFileMaxPage(this.fileMaxPage);
            copied.setMessage(this.message);
            
            return copied;
        }
     //   START- Added for defect-#51537
		 // validation
        public ActionErrors validate(ActionMapping mapping, 
                                HttpServletRequest request)
        {
            ActionErrors errs = super.validate(mapping, request);
            if (!errs.isEmpty()) {
                request.setAttribute("hasAlert", Boolean.TRUE);
            }
            return errs;
        }
	//	END-Added for defect-#51537
		
         // validate values
        public void validateValues() {
            if ( this.fileSortColumn == null ) {
                
                this.fileSortColumn = 
                                FilterSortPageUtils.CUSTOMER_DEFAULT_SORT_COLUMN;
            
            }

            if ( this.fileSortOrderBy == null ) {
                
                this.fileSortOrderBy = FilterSortPageUtils.DESCENDING;
            
            }

            if ( this.filePageRequested == null ) {
            
                this.filePageRequested = new Integer(1);
            
            }
                
            if ( this.filePageRequested.intValue() <= 0 ) {            
               
                this.filePageRequested = new Integer(1);
            
            }

            if ( this.fileMaxPage == null ) {
                
                this.fileMaxPage = new Integer(1);
            
            }

            if ( this.filePageRequested.intValue() > this.fileMaxPage.intValue() ) {
                
                this.filePageRequested = new Integer(this.fileMaxPage.intValue());                
                this.selectedAuditId = null;
                this.uploadFileName = null;
             
            }
        }  
        
           // reset values based on action
        public void resetValuesForAction(String actionElement, 
                                        String fromAction) {
            if ( actionElement.equals("{actionForm.fileSortOrderBy}") ) {
                
                this.filePageRequested = new Integer(1);
            
            }
            
            if ( actionElement.equals("ButtonGoInvoked_fileSearchResult") 
                    || actionElement.equals("EnterKeyInvoked_fileSearchResult") ) {
                
                this.selectedAuditId = null;
             
            }
            if ( actionElement.equals("ButtonGoInvoked_tablePathListAnchor") 
                    || actionElement.equals("EnterKeyInvoked_tablePathListAnchor") ) {
            
                if ( fromAction.equals(ACTION_VIEW_UPLOADS) ) {
                
                    this.selectedAuditId = null;
                    this.uploadFileName = null;
                 
                }
            }
        }  
        
        
          // clear message
        public void clearMessage() {   
            this.message = null;
        }

        public void setActionElement(String actionElement)
        {
            this.actionElement = actionElement;
        }        
        public String getActionElement()
        {
            return this.actionElement != null ? this.actionElement : ACTION_DEFAULT;
        }
        public void setCurrentAction(String currentAction)
        {
            this.currentAction = currentAction;
        }
        public String getCurrentAction()
        {
            return this.currentAction != null ? this.currentAction : ACTION_DEFAULT;
        }

        public void setSelectedTab(String selectedTab)
        {
            this.selectedTab = selectedTab;
        }         
        public String getSelectedTab()
        {
            return this.selectedTab != null ? this.selectedTab : MODULE_DOWNLOAD_TEMPLATE;
        }
        
        public void setTheFile(FormFile theFile)
        {
            this.theFile = theFile;
        }

        public FormFile getTheFile()
        {
            return this.theFile;
        }
        
        public void setFileName(String fileName)
        {
            this.fileName = fileName;
        }
        public String getFileName()
        {
            return this.fileName;
        }

        public void setFilter(String filter)
        {
            this.filter = filter;
        }        
        public String getFilter()
        {
            return this.filter;
        }
        
        
		public void setAuditFileId(Integer auditFileId)
        {
            this.auditFileId = auditFileId;
        }

        public Integer getAuditFileId()
        {
            return this.auditFileId;
        }
        
         public void setUploadFileInformation(UploadFileInformation uploadFileInfo)
        {
            this.uploadFileInfo = uploadFileInfo;
        }
        
        public UploadFileInformation getUploadFileInformation()
        {
            if (this.uploadFileInfo == null) {
                this.uploadFileInfo = new UploadFileInformation();
            }
            
            return this.uploadFileInfo;
        }

        public void setFileSortColumn(String fileSortColumn)
        {
            this.fileSortColumn = fileSortColumn;
        }

        public String getFileSortColumn()
        {
            return this.fileSortColumn != null 
                                ? this.fileSortColumn 
                                : FilterSortPageUtils.FILE_DATE_DEFAULT_SORT_COLUMN;
        }

        public void setFileSortOrderBy(String fileSortOrderBy)
        {
            this.fileSortOrderBy = fileSortOrderBy;
        }

        public String getFileSortOrderBy()
        {
            return this.fileSortOrderBy != null 
                                ? this.fileSortOrderBy 
                                : FilterSortPageUtils.DESCENDING;
        }

        public void setFilePageRequested(Integer filePageRequested)
        {
            this.filePageRequested = filePageRequested;
        }

        public Integer getFilePageRequested()
        {
            return this.filePageRequested != null 
                                ? this.filePageRequested 
                                : new Integer(1);
        }

        public void setFileMaxPage(Integer fileMaxPage)
        {
            this.fileMaxPage = fileMaxPage;
        }

        public Integer getFileMaxPage()
        {
            return this.fileMaxPage != null 
                                ? this.fileMaxPage : new Integer(1);
        }

        public void setSelectedAuditId(Integer selectedAuditId)
        {
            this.selectedAuditId = selectedAuditId;
        }

        public Integer getSelectedAuditId()
        {
            return this.selectedAuditId;
        }
        
        public Message getMessage()
        {
            return this.message != null ? this.message : new Message();
        }       
        
        public void setMessage(Message message)
        {
            this.message = message;
        }
        
        public void setMessage(String title, String content, String type)
        {
            this.message = new Message(title, content, type);
        }

        public void setActionPermission(String actionPermission)
        {
            this.actionPermission = actionPermission;
        }

        public String getActionPermission()
        {
            return this.actionPermission;
        }
        public void setUploadFileName(String uploadFileName)
        {
            this.uploadFileName = uploadFileName;
        }
        public String getUploadFileName()
        {
            return this.uploadFileName;
        }
    }

	
}