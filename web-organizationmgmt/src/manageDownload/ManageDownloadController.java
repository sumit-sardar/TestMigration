package manageDownload;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;

import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.StudentFile;
import com.ctb.bean.testAdmin.StudentFileRow;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserFile;
import com.ctb.bean.testAdmin.UserFileRow;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.CTBConstants;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import dto.Message;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import utils.MessageResourceBundle;
import utils.UploadDownloadFormUtils;

/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class ManageDownloadController extends PageFlowController
{
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.uploadDownloadManagement.UploadDownloadManagement downLoadManagement;

 // LLO- 118 - Change for Ematrix UI
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;
	
    static final long serialVersionUID = 1L;
    
    private String userName = null;
    private ManageDownloadForm savedForm = null;
    private User user = null;
    
    // LLO- 118 - Change for Ematrix UI
	private boolean isTopLevelUser = false;
	private boolean islaslinkCustomer = false;
  
    
    private static final String ACTION_DEFAULT = "defaultAction";

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.userManagement.UserManagement userManagement;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.organizationManagement.OrganizationManagement organizationManagement;

    @Control()
    private com.ctb.control.db.Users users;
    
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="beginManageDownload.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "beginManageDownload.do")
    })
    protected Forward begin()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="manageDownload.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "manageDownload.do")
    })
    protected Forward beginManageDownload()
    {
        getUserDetails();
        //Bulk Accommodation Changes
        customerHasBulkAccommodation();
        customerHasResetTestSessions();
        customerHasScoring();//For hand scoring changes
        isTopLevelUser();
        this.savedForm = initialize();
        
        return new Forward("success", this.savedForm);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="manage_download.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "manage_download.jsp")
    })
    protected Forward manageDownload(ManageDownloadForm form)
    {  
    	isTopLevelUser(); //LLO- 118 - Change for Ematrix UI
        return new Forward("success", form);
    }
    
  //LLO- 118 - Change for Ematrix UI
    private void isTopLevelUser(){
		
		boolean isUserTopLevel = false;
		boolean isLaslinkUserTopLevel = false;
		boolean isLaslinkUser = false;
		isLaslinkUser = this.islaslinkCustomer;
		try {
			if(isLaslinkUser) {
				isUserTopLevel = orgnode.checkTopOrgNodeUser(this.userName);	
				if(isUserTopLevel){
					isLaslinkUserTopLevel = true;				
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getSession().setAttribute("isTopLevelUser",isLaslinkUserTopLevel);	
	}
    /**
     * @jpf:action
     * @jpf:forward name="error" path="manage_download.jsp"
     */
    @Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "error", path = "manage_download.jsp")
		}
	)
    protected Forward downloadData(ManageDownloadForm form)
    {         
        form.clearMessage();
        
        byte []data = null;
        String fileContent = "";
        String fileName = form.getFileName();
        
        String userFileName = userName + "_User.xls"; 
        
        try {
            if ( fileName.equals(userFileName) ){
                
                UserFile userFile = downLoadManagement.getUserFile(this.userName);
                UserFileRow []userFileRow = userFile.getUserFileRows();
                
                if (userFileRow.length > CTBConstants.MAX_EXCEL_SIZE) {
                   
                    form.setMessage(Message.DOWNLOAD_TITLE, Message.DOWNLOAD_ERROR_MSG, Message.ERROR);
                    setFormInfoOnRequest(form);
                    return new Forward("error",form);
                    
                }
                
                //fileContent = UploadDownloadFormUtils.downLoadUserData(userFile);
                // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                data = UploadDownloadFormUtils.downLoadUserDataFile
                        (userFile, this.userName, userManagement, this.islaslinkCustomer);
                
            } else {
                
                StudentFile studentFile = downLoadManagement.getStudentFile(this.userName);
                StudentFileRow []studentFileRow = studentFile.getStudentFileRows();
                
                if (studentFileRow.length > CTBConstants.MAX_EXCEL_SIZE) {
                    
                    form.setMessage(Message.DOWNLOAD_TITLE, Message.DOWNLOAD_ERROR_MSG, Message.ERROR);
                    setFormInfoOnRequest(form);
                    return new Forward("error",form);
                    
                }
                //fileContent = UploadDownloadFormUtils.downLoadStudentData(studentFile);
                // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                data = UploadDownloadFormUtils.downLoadStudentDataFile
                        (studentFile, this.userName, userManagement, this.islaslinkCustomer);
                
            }
            
        setFormInfoOnRequest(form);
            
        HttpServletResponse resp = this.getResponse();        
        String bodypart = "attachment; filename=\"" + fileName + "\" ";

        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", bodypart);
        /*resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Pragma", "no-cache");*/
        
        resp.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        resp.setHeader("Cache-Control", "cache");
        resp.setHeader("Pragma", "public");
        resp.flushBuffer();
            /*PrintWriter pw = resp.getWriter();
            pw.write(fileContent);
            pw.close();*/
        OutputStream stream = resp.getOutputStream();
        stream.write(data);
        stream.close();
            
        } catch(Exception e) {
            e.printStackTrace();
            String msg = MessageResourceBundle.getMessage(e.getMessage());
            form.setMessage(Message.DOWNLOAD_TITLE, msg, Message.ERROR);
            //setFormInfoOnRequest(form);
        }
        
        return null;
    }
    
    private void setFormInfoOnRequest(ManageDownloadForm form) {
    	this.getRequest().setAttribute("pageMessage", form.getMessage());
    }

     /**
     * initialize
     */
    private ManageDownloadForm initialize()
    {                
        ManageDownloadForm form = new ManageDownloadForm();
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
        
        }
        catch (CTBBusinessException be) {
            
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

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////// *********************** ManageUploadForm ************* ////////////////////////////////    
    /////////////////////////////////////////////////////////////////////////////////////////////    
    
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ManageDownloadForm extends SanitizedFormData
    {
        private String userName;

        private String actionElement;
        private String currentAction;
        private String fileName;
        private Message message;
        

        public ManageDownloadForm()
        {
        }
        
        public void init()
        {
            this.actionElement = ACTION_DEFAULT;
            this.currentAction = ACTION_DEFAULT;
            this.message = new Message();   
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
        public void setFileName(String fileName)
        {
            this.fileName = fileName;
        }
        public String getFileName()
        {
            return this.fileName;
        }
        
          // clear message
        public void clearMessage() {   
            this.message = null;
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
	
	
	/**
	 * Reset Test Session
	 */
	private Boolean customerHasResetTestSessions() 
	{	
		Integer customerId = this.user.getCustomer().getCustomerId();
		boolean hasResetTestSessionsConfigurable = false;
		try
        {
			CustomerConfiguration [] customerConfigurations = users.getCustomerConfigurations(customerId.intValue());
			if (customerConfigurations == null || customerConfigurations.length == 0) {
				customerConfigurations = users.getCustomerConfigurations(2);
			}
			
			for (int i=0; i < customerConfigurations.length; i++) {
	
				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_User_Reset_Subtest") && 
						cc.getDefaultValue().equals("T")) {
					hasResetTestSessionsConfigurable = true; 
					break;
				}
			}
	
			getSession().setAttribute("isResetTestSessionsConfigured", hasResetTestSessionsConfigurable);
        }
        catch (SQLException se) {
        	se.printStackTrace();
		}

		return new Boolean(hasResetTestSessionsConfigurable);           
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
        boolean isLaslinkCustomer = false;
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
               // break;
            } 
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")
    				&& cc.getDefaultValue().equals("T")) {
    			isLaslinkCustomer = true;
    			// break;
            }
        }
        }catch (SQLException se) {
        	se.printStackTrace();
		}
        this.setIslaslinkCustomer(isLaslinkCustomer);
        getSession().setAttribute("isScoringConfigured", hasScoringConfigurable);
        return new Boolean(hasScoringConfigurable);
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
	

	
}
