package manageUpload;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.DataFileAudit;
import com.ctb.bean.testAdmin.DataFileAuditData;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.CTBConstants;
import com.ctb.bean.testAdmin.StudentFile;
import com.ctb.bean.testAdmin.UserFile;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.util.web.sanitizer.JavaScriptSanitizer;
import dto.Message;
import dto.UploadFileInformation;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.DiskFile;
import org.apache.struts.upload.FormFile;
import utils.UploadHistoryUtils;
import utils.FilterSortPageUtils;
import utils.MessageResourceBundle;
import utils.PathFinderUtils;
import utils.UploadHistoryUtils;
import com.ctb.widgets.bean.PagerSummary;
import dto.AuditFileHistory;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnection;
import javax.jms.Session;
import utils.PermissionsUtils;
import utils.UploadDownloadFormUtils;


/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class ManageUploadController extends PageFlowController
{
	
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
    
   
    
    static final long serialVersionUID = 1L;
    
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
    
    private static final String ACTION_ELEMENT = "{actionForm.selectedTab}";


	@Control
	private UploadDownloadManagementServiceControl uploadDownloadManagementServiceControl;

    
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="beginManageUpload.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "beginManageUpload.do")
    })
    protected Forward begin()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="manageUpload.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "manageUpload.do")
    })
    protected Forward beginManageUpload()
    {
        getUserDetails();
        //Bulk Accommodation Changes
        customerHasBulkAccommodation();
        customerHasScoring();//For hand scoring changes
        this.savedForm = initialize();
        
        return new Forward("success", this.savedForm);
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="manage_upload.jsp"
     * @jpf:forward name="viewUploads" path="viewUploads.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "manage_upload.jsp"), 
        @Jpf.Forward(name = "viewUploads",
                     path = "viewUploads.do")
    })
    protected Forward manageUpload(ManageUploadForm form)
    {         
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


    /**
     * @jpf:action
     * @jpf:forward name="success" path="manageUpload.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "manageUpload.do")
    })
    protected Forward cancelUploadData()
    {       
        this.savedForm = initialize();
        this.savedForm.setSelectedTab(MODULE_UPLOAD_FILE);
        
        return new Forward("success", this.savedForm);
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

   
       
    /**
     * @jpf:action
     * @jpf:forward name="success" path="manage_upload.jsp"
     * @jpf:forward name="gotoNextAction" path="gotoNextAction.do" redirect="true"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "manage_upload.jsp"), 
        @Jpf.Forward(name = "gotoNextAction", 
                     path = "gotoNextAction.do", 
                     redirect = true)
    })
    protected Forward viewUploads(ManageUploadForm form)
    {               
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
        
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward getUploadFile(ManageUploadForm form)
    {       
        HttpServletResponse resp = this.getResponse();        
        String fileContent = new String("first\tsecond\tthird\tfourth");
        String fileName = "Error_" +
                          form.getFileName();
        String bodypart = "attachment; filename=\"" +
                          fileName +
                          "\" ";

        prepareContentHeader(resp, bodypart);

        try
        {
        
            resp.flushBuffer();
            PrintWriter pw = resp.getWriter();
            pw.write(fileContent);
            pw.close();
        
        }
        catch (Exception e)
        {
        
            e.printStackTrace();
            String msg = MessageResourceBundle.getMessage(e.getMessage());
            form.setMessage(Message.ERROR_FILE_TITLE, msg, Message.ERROR);
        
        }
        
        return null;
    }

    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward getErrorDataFile(ManageUploadForm form)
    {
        HttpServletResponse resp = this.getResponse();   
        byte []errorFile = null;
        
        try
        {
             
            errorFile = this.uploadDownloadManagement.getErrorDataFile(
                        this.userName,form.getSelectedAuditId());   
         
            form.setFileName(UploadHistoryUtils.getFileName(form.getSelectedAuditId(), this.fileList));
                                        
            String fileName = getErrorFileName(form.getFileName());
            String bodypart = "attachment; filename=\"" +
                              fileName +
                              "\" ";

            prepareContentHeader(resp, bodypart);
            
            resp.flushBuffer();
            OutputStream stream = resp.getOutputStream();
            stream.write(errorFile);
            stream.close();
        
        }
        catch (Exception e)
        {
        
            e.printStackTrace();
            String msg = MessageResourceBundle.getMessage(e.getMessage());
            form.setMessage(Message.ERROR_FILE_TITLE, msg, Message.ERROR);

        }
        
        return null;
    }
    
    /**
     * forward to the action that stored in the saved form
     * @jpf:action
     * @jpf:forward name="deleteFile" path="deleteErrorDataFile.do"
     * @jpf:forward name= "exportErrorFile" path="getErrorDataFile.do"
     * @jpf:forward name="uploadFile" path="getUploadFile.do"
     * @jpf:forward name="defaultAction" path="defaultAction.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "deleteFile",
                     path = "deleteErrorDataFile.do"), 
        @Jpf.Forward(name = "exportErrorFile",
                     path = "getErrorDataFile.do"), 
        @Jpf.Forward(name = "uploadFile",
                     path = "getUploadFile.do"), 
        @Jpf.Forward(name = "defaultAction",
                     path = "defaultAction.do")
    })
    protected Forward gotoNextAction(ManageUploadForm form)
    {
        String currentAction = this.savedForm.getCurrentAction();
        
        if (currentAction == null)
        {
         
            currentAction = ACTION_DEFAULT;
        
        }
               
        return new Forward(currentAction, this.savedForm.createClone());
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path = "getUploadFile.do"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "getUploadFile.do")
		}
	)
    protected Forward defaultAction(ManageUploadForm form)
    {   
        initialize();
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
    protected Forward deleteErrorDataFile(ManageUploadForm form)
    {
        try {
            
            this.uploadDownloadManagement.deleteErrorDataFile(form.selectedAuditId);
            form.setMessage(Message.DELETE_FILE_TITLE, 
                    Message.DELETE_FILE_SUCCESSFUL, Message.INFORMATION);
            form.setCurrentAction(ACTION_APPLY_SEARCH);
            
        } catch (Exception e) {
            
            e.printStackTrace();
            String msg = MessageResourceBundle.getMessage(e.getMessage());
            form.setMessage(Message.DELETE_FILE_TITLE, msg, Message.ERROR);
        }
        
        return new Forward("success");
    }
	//////////////////////////private methods/////////////////////////////////////////////

    
     /**
     * initialize
     */
    private ManageUploadForm initialize()
    {                
        ManageUploadForm form = new ManageUploadForm();
        form.init();
        this.getSession().setAttribute("userHasReports", userHasReports());
        return form;
    }
    
    /**
     * userHasReports
     */
    private Boolean userHasReports() 
    {
        Boolean hasReports = Boolean.FALSE;
        try {   
        
            hasReports = this.organizationManagement.userHasReports(this.userName);
        
        } catch (CTBBusinessException be) {
           
            be.printStackTrace();
        
        }
        return hasReports;
    }
    
    /**
     * getUserDetails
     */
    private void getUserDetails()
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
       
        if ( principal != null ) {
            
            this.userName = principal.toString();
        
        } else {            
        
            this.userName = (String)getSession().getAttribute("userName");
        
        }   
        try {
            this.user = this.userManagement.getUser(this.userName, 
                                                this.userName);
       //     this.displayNewMessage = user.getDisplayNewMessage();                                                
        }
        catch (Exception e) {
            e.printStackTrace();
        } 
        getSession().setAttribute("userName", this.userName);
    }
    
    
	/**
     * initSearch
     */
    private boolean initSearch(ManageUploadForm form)
    {
        boolean applySearch = false;
        String currentAction = form.getCurrentAction();
        String actionElement = form.getActionElement();
        if ( (currentAction != null)
                    && currentAction.equals(ACTION_APPLY_SEARCH)
                    && actionElement.equals(ACTION_ELEMENT)) {
        
            applySearch = true;
       
            form.setFileSortColumn(FilterSortPageUtils.FILE_DATE_DEFAULT_SORT_COLUMN);
            form.setFileSortOrderBy(FilterSortPageUtils.DESCENDING);      
            form.setFilePageRequested(new Integer(1));    
            form.setFileMaxPage(new Integer(1));   
            form.setCurrentAction(ACTION_DEFAULT);               
        
        }
        
        if ( this.searchApplied ) {
        
            applySearch = true;
        
        }
        else {
        
            form.setSelectedAuditId(null);
         
        }
                
        return applySearch;
    }
    
    
	/**
     * initPagingSorting
     */
    private void initPagingSorting(ManageUploadForm form)
    {
        String actionElement = form.getActionElement();
        
        if ( (actionElement.indexOf("filePageRequested") > 0) 
                    || (actionElement.indexOf("fileSortOrderBy") > 0) ) {
           
             form.setSelectedAuditId(null);
         
        }
    }
    
    
    /**
     * findByCustomerProfile
     */
    private DataFileAuditData findFile(ManageUploadForm form) {
        
        String actionElement = form.getActionElement();
                     
        PageParams page = FilterSortPageUtils.buildPageParams(
                form.getFilePageRequested(), 
                FilterSortPageUtils.PAGESIZE_20);
        SortParams sort = FilterSortPageUtils.buildFileSortParams(
                form.getFileSortColumn(), 
                form.getFileSortOrderBy());
                
        DataFileAuditData dataFileAuditData = null;
       
        try {
            dataFileAuditData = this.uploadDownloadManagement.getUploadHistory(
                                        this.userName, page, sort);   
                                           
            
        } catch(CTBBusinessException be){
        
                be.printStackTrace();
                String msg = MessageResourceBundle.getMessage(be.getMessage());
                form.setMessage(Message.FILE_HISTORY_ERROR, msg, Message.ERROR);
        } 
    
        return dataFileAuditData;
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
                                          

            PathFinderUtils.saveFileToDBTemp(saveFileName, form, this.uploadDownloadManagement);
            
            System.out.println("***** Upload App: File written to data_file_temp: " + form.getAuditFileId());
                      
            return RETURN_SUCCESS;
        
        } catch (Exception e) {
        
            e.printStackTrace();    
        
        }
        
        return "False";
    }
    
    private void setFormInfoOnRequest(ManageUploadForm form) {
    	this.getRequest().setAttribute("pageMessage", form.getMessage());
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
                DataFileAudit dataFileAudit = new DataFileAudit();
                dataFileAudit.setStatus("FL");
                try{
                    uploadDownloadManagement.updateAuditFileStatus(uploadFileId);
                } catch (Exception se) {
                    se.printStackTrace();
                }
            	e.printStackTrace();
            	throw new RuntimeException(e);
            }
        }
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
        
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    /////// *********************** ManageUploadForm ************* ////////////////////////////////    
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

	public String getStrFileName() {
		return strFileName;
	}

	public List getFileList() {
		return fileList;
	}

	/*
     * Bulk accommodation
     */
	private Boolean customerHasBulkAccommodation()
    {               
        Integer customerId = this.user.getCustomer().getCustomerId();
        boolean hasBulkStudentConfigurable = false;

        try
        {      
			CustomerConfiguration [] customerConfigurations = users.getCustomerConfigurations(customerId.intValue());
			if (customerConfigurations == null || customerConfigurations.length == 0) {
				customerConfigurations = users.getCustomerConfigurations(2);
			}
            
            for (int i=0; i < customerConfigurations.length; i++)
            {
            	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
                //Bulk Accommodation
                if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && cc.getDefaultValue().equals("T")	)
                {
                    this.getSession().setAttribute("isBulkAccommodationConfigured", true);
                    break;
                } 
            }
        }
        catch (SQLException se) {
        	se.printStackTrace();
		}
        
       
        return new Boolean(hasBulkStudentConfigurable);
    }
	
	//changes for scoring
	
	/**
	 * This method checks whether customer is configured to access the scoring feature or not.
	 * @return Return Boolean 
	 */

	
	private Boolean customerHasScoring()
    {               
		Integer customerId = this.user.getCustomer().getCustomerId();
        boolean hasScoringConfigurable = false;
        
        try
        {      
			CustomerConfiguration [] customerConfigurations = users.getCustomerConfigurations(customerId.intValue());
			if (customerConfigurations == null || customerConfigurations.length == 0) {
				customerConfigurations = users.getCustomerConfigurations(2);
			}
        

        for (int i=0; i < customerConfigurations.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
            		cc.getDefaultValue().equals("T")	) {
            	hasScoringConfigurable = true;
            	getSession().setAttribute("isScoringConfigured", hasScoringConfigurable);
                break;
            } 
        }
       }
        
        catch (SQLException se) {
        	se.printStackTrace();
		}
       
        return new Boolean(hasScoringConfigurable);
    }
    
	
    
}
