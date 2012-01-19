package sessionOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import util.BroadcastUtils;
import util.MessageResourceBundle;
import util.RequestUtil;
import viewmonitorstatus.ViewMonitorStatusController.ViewMonitorStatusForm;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.request.FilterParams.FilterType;
import com.ctb.bean.testAdmin.BroadcastMessage;
import com.ctb.bean.testAdmin.BroadcastMessageData;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.EditCopyStatus;
import com.ctb.bean.testAdmin.OrgNodeCategory;
import com.ctb.bean.testAdmin.PasswordHintQuestion;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.SessionStudentData;
import com.ctb.bean.testAdmin.StudentManifest;
import com.ctb.bean.testAdmin.StudentManifestData;
import com.ctb.bean.testAdmin.StudentNodeData;
import com.ctb.bean.testAdmin.StudentSessionStatus;
import com.ctb.bean.testAdmin.StudentSessionStatusData;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestElementData;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.bean.testAdmin.TestProductData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserData;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.testAdmin.InsufficientLicenseQuantityException;
import com.ctb.exception.testAdmin.TransactionTimeoutException;
import com.ctb.exception.validation.ValidationException;
import com.ctb.testSessionInfo.data.SubtestVO;
import com.ctb.testSessionInfo.data.TestVO;
import com.ctb.testSessionInfo.dto.Message;
import com.ctb.testSessionInfo.dto.MessageInfo;
import com.ctb.testSessionInfo.dto.PasswordInformation;
import com.ctb.testSessionInfo.dto.SubtestDetail;
import com.ctb.testSessionInfo.dto.TestRosterFilter;
import com.ctb.testSessionInfo.dto.TestRosterVO;
import com.ctb.testSessionInfo.dto.TestSessionVO;
import com.ctb.testSessionInfo.dto.UserProfileInformation;
import com.ctb.testSessionInfo.utils.Base;
import com.ctb.testSessionInfo.utils.BaseTree;
import com.ctb.testSessionInfo.utils.DateUtils;
import com.ctb.testSessionInfo.utils.FilterSortPageUtils;
import com.ctb.testSessionInfo.utils.Organization;
import com.ctb.testSessionInfo.utils.OrgnizationComparator;
import com.ctb.testSessionInfo.utils.PermissionsUtils;
import com.ctb.testSessionInfo.utils.Row;
import com.ctb.testSessionInfo.utils.ScheduleTestVo;
import com.ctb.testSessionInfo.utils.ScheduledSavedTestVo;
import com.ctb.testSessionInfo.utils.TestSessionUtils;
import com.ctb.testSessionInfo.utils.TreeData;
import com.ctb.testSessionInfo.utils.UserOrgHierarchyUtils;
import com.ctb.testSessionInfo.utils.UserPasswordUtils;
import com.ctb.testSessionInfo.utils.WebUtils;
import com.ctb.util.OperationStatus;
import com.ctb.util.SuccessInfo;
import com.ctb.util.ValidationFailedInfo;
import com.ctb.util.testAdmin.TestAdminStatusComputer;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.ColumnSortEntry;
import com.google.gson.Gson;




@Jpf.Controller()
public class SessionOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;

    /**
     * @common:control
     */
	 
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;

    @Control()
    private com.ctb.control.userManagement.UserManagement userManagement;
    
    @Control()
    private com.ctb.control.licensing.Licensing licensing;
    
    @Control()
    private com.ctb.control.testAdmin.ScheduleTest scheduleTest;
    
    @Control()
    private com.ctb.control.db.ItemSet itemSet;
    
    @Control()
    private com.ctb.control.db.BroadcastMessageLog message;
    
    //Added for view/monitor test status
   
    protected void onCreate() {
	}

	/**
	 * Callback that is invoked when this controller instance is destroyed.
	 */
	@Override
	protected void onDestroy(HttpSession session) {
	}
	
	private boolean sessionDetailsShowScores = false;
	private boolean subtestValidationAllowed = false;
	private List studentStatusSubtests = null; 
	private boolean showStudentReportButton = false;
	private String genFile = null;
	private List TABETestElements = null;    
	//goto json
	private TestRosterFilter  testRosterFilter = null;
	private ArrayList selectedRosterIds = null;
	public CustomerConfiguration[] customerConfigurations = null;
	private CustomerConfigurationValue[] customerConfigurationsValue = null;
	//private String userName = (String)getSession().getAttribute("userName");
	private Integer sessionId = null;
	private String[] testStatusOptions = {FilterSortPageUtils.FILTERTYPE_SHOWALL, FilterSortPageUtils.FILTERTYPE_COMPLETED, FilterSortPageUtils.FILTERTYPE_INCOMPLETE, 
            FilterSortPageUtils.FILTERTYPE_INPROGRESS, FilterSortPageUtils.FILTERTYPE_NOTTAKEN, FilterSortPageUtils.FILTERTYPE_SCHEDULED, 
            FilterSortPageUtils.FILTERTYPE_STUDENTSTOP, FilterSortPageUtils.FILTERTYPE_SYSTEMSTOP, FilterSortPageUtils.FILTERTYPE_TESTLOCKED,
            FilterSortPageUtils.FILTERTYPE_TESTABANDONED, FilterSortPageUtils.FILTERTYPE_STUDENTPAUSE};
	private String setCustomerFlagToogleButton="false";
	private String[] validationStatusOptions = {FilterSortPageUtils.FILTERTYPE_SHOWALL, FilterSortPageUtils.FILTERTYPE_INVALID, FilterSortPageUtils.FILTERTYPE_VALID};
	
	private String fileName = null;
	private String fileType = null;
	private String userEmail = null;
	private List fileTypeOptions = null;
	public boolean isLasLinkCustomer = false;

	
	//Added for view/monitor test status
    
	private String userName = null;
	private Integer customerId = null;
    private User user = null;
    private List<TestSessionVO> sessionListCUFU = new ArrayList<TestSessionVO>(); 
    private List<TestSessionVO> sessionListPA = new ArrayList<TestSessionVO>(); 
    private boolean hasLicenseConfig = false; 
    public static final String CONTENT_TYPE_JSON = "application/json";
    public LinkedHashMap<String, String> hintQuestionOptions = null;
    public UserProfileInformation userProfile = null; 
	private TestProduct [] tps;
	private static final String ACTION_INIT = "init";
	boolean isPopulatedSuccessfully = false;
	ScheduleTestVo vo = new ScheduleTestVo();
	
	Map<Integer, String> topNodesMap = new LinkedHashMap<Integer, String>();
	
	private List<String> studentGradesForCustomer;
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="organizations.do"
	 */
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "resetPassword", path = "resetPassword.do"), 
	        @Jpf.Forward(name = "editTimeZone", path = "setTimeZone.do"),
			@Jpf.Forward(name = "currentUI", path = "gotoCurrentUI.do"),
			@Jpf.Forward(name = "legacyUI", path = "gotoLegacyUI.do")
	})
	protected Forward begin()
	{
		String forwardName = "currentUI";
		getLoggedInUserPrincipal();		
		getUserDetails();

    	CustomerConfiguration [] customerConfigs = getCustomerConfigurations(this.customerId);
		if (accessNewUI(customerConfigs)) {
			// direct to revised UI
			setupUserPermission(customerConfigs);
	        if (isUserPasswordExpired()|| "T".equals(this.user.getResetPassword())) {
	        	forwardName = "resetPassword";
	        }
	        else if (this.user.getTimeZone() == null) {
	        	forwardName = "editTimeZone";
	        }
	        
		}
		else {
			forwardName = "legacyUI";	
		}
		
		return new Forward(forwardName);
	} 
	
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "assessments_sessions.do") 
        }) 
    protected Forward gotoCurrentUI()
    {
		List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
        this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
		    	
        return new Forward("success");
    }
	
    @Jpf.Action()
    protected Forward gotoLegacyUI()
    {
        try
        {
            String url = "/TestSessionInfoWeb/homepage/HomePageController.jpf";
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
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "reset_password.jsp")
    })
    protected Forward resetPassword()
    {              
        initHintQuestionOptions();
    	
		try {
			this.user = userManagement.getUser(this.userName, this.userName);
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
        this.userProfile = new UserProfileInformation(this.user);   
        
        String title = "Change Password: " + this.userProfile.getFirstName() + " " + this.userProfile.getLastName();
        this.getRequest().setAttribute("pageTitle", title);
        
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="gotoCurrentUI.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "gotoCurrentUI.do"),
        @Jpf.Forward(name = "error", path = "reset_password.jsp") 
    })
    protected Forward savePassword()
    {
    	String forwardName = "success";
    	
		 String message = "";
		 String requiredFields = null;
		 boolean revalidate = true;
		 boolean validationPassed = true;
		 MessageInfo messageInfo = new MessageInfo();
		 
		 String newPassword = this.userProfile.getUserPassword().getNewPassword();
		 String confirmPassword = this.userProfile.getUserPassword().getConfirmPassword();		 
		 String oldPassword = this.userProfile.getUserPassword().getOldPassword();
		 String hintQuestionId = this.userProfile.getUserPassword().getHintQuestionId();
		 String hintAnswer = this.userProfile.getUserPassword().getHintAnswer();
		 
		 
		 PasswordInformation passwordinfo = new PasswordInformation();
		 passwordinfo.setOldPassword(oldPassword);
		 passwordinfo.setNewPassword(newPassword);
		 passwordinfo.setConfirmPassword(confirmPassword);
		 passwordinfo.setHintQuestionId(hintQuestionId);
		 passwordinfo.setHintAnswer(hintAnswer);
		 
		 requiredFields = UserPasswordUtils.getRequiredPasswordField(passwordinfo);
		 if( requiredFields != null) {
			 revalidate = false;
			 validationPassed = false;
				if ( requiredFields.indexOf(",") > 0){
					message = requiredFields + (" <br/> " + Message.REQUIRED_TEXT_MULTIPLE);
					messageInfo = createMessageInfo(messageInfo, Message.MISSING_REQUIRED_FIELDS, message, Message.ERROR, true, false );
				}
				else {
					message = requiredFields + (" <br/> " + Message.REQUIRED_TEXT);
					messageInfo = createMessageInfo(messageInfo, Message.MISSING_REQUIRED_FIELD, message, Message.ERROR, true, false );
	
				}
		 }
		 else 
		 if (UserPasswordUtils.isPasswordDifferent(this.user.getPassword(), oldPassword)) {
			 validationPassed = false;
		 	 messageInfo = createMessageInfo(messageInfo, Message.CHANGE_PASSWORD_TITLE, Message.WRONG_PASSWORD, Message.ERROR, true, false );
		 }
		 else {
			 	String invalidCharFields = UserPasswordUtils.verifyPasswordInfo(passwordinfo);
			 	String invalidString = "";
	
				if (invalidCharFields != null && invalidCharFields.length() > 0) {
					 
					 if ( invalidCharFields.indexOf(",") > 0){
						 
						 invalidString = invalidCharFields + ("<br/>" + Message.INVALID_DEX_PASSWORD);
						 
					 } else {
						 
						 invalidString = invalidCharFields + ("<br/>" + Message.INVALID_DEX_PASSWORD_SINGLE_LINE);
						 
					 }
	
						
				}
	
				if (invalidString != null && invalidString.length() > 0) {
					 revalidate = false;
					 validationPassed = false;
					 messageInfo = createMessageInfo(messageInfo, Message.INVALID_CHARS_TITLE, invalidString, Message.ERROR, true, false );
				 }
				 
				if (revalidate) {
					 boolean isNewAndConfirmPasswordDifferent = UserPasswordUtils.isNewAndConfirmPasswordDifferent(passwordinfo);
					 
					 if(isNewAndConfirmPasswordDifferent) {
						 validationPassed = false;
					 	 messageInfo = createMessageInfo(messageInfo, Message.CHANGE_PASSWORD_TITLE, Message.PASSWORD_MISMATCH, Message.ERROR, true, false );
					 }
				 }
		 }		 
		 		
		 boolean passwordSaved = false;
		 
		 if (validationPassed) {
			 this.user.setResetPassword("F");
			 this.user.setPasswordHintQuestion(hintQuestionId);
			 this.user.setPasswordHintAnswer(hintAnswer);
			 this.user.setPassword(oldPassword);
			 this.user.setNewPassword(newPassword);
			 passwordSaved = true;
			 
			 try {
				 this.userManagement.updateUser(this.user.getUserName(),this.user);
			 } catch (CTBBusinessException be) {
				 be.printStackTrace();
	             String msg = MessageResourceBundle.getMessage(be.getMessage());
			 	 messageInfo = createMessageInfo(messageInfo, Message.CHANGE_PASSWORD_TITLE, msg, Message.ERROR, true, false );
				 passwordSaved = false;
			 }
			 
		 }
		 
		 if (! passwordSaved) {
			 String title = "Change Password: " + this.userProfile.getFirstName() + " " + this.userProfile.getLastName();
			 this.getRequest().setAttribute("pageTitle", title);

			 this.getRequest().setAttribute("errorMsg", messageInfo.getContent());
        
			 forwardName = "error";
		 }
		 
        return new Forward(forwardName);
    }    
    
	private MessageInfo createMessageInfo(MessageInfo messageInfo, String messageTitle, String content, String type, boolean errorflag, boolean successFlag){
		messageInfo.setTitle(messageTitle);
		messageInfo.setContent(content);
		messageInfo.setType(type);
		messageInfo.setErrorFlag(errorflag);
		messageInfo.setSuccessFlag(successFlag);
		return messageInfo;
	}
    
    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward setTimeZone()
    {               
        try
        {
            String url = "/UserManagementWeb/manageUser/beginEditMyProfile.do?isSetTimeZone=true";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
    
    @Jpf.Action()
    protected Forward selectTest(SessionOperationForm form)
    {
    	initialize();
    	String jsonData = "";
    	HttpServletResponse resp = getResponse();
    	resp.setCharacterEncoding("UTF-8"); 
    	OutputStream stream = null;
        String currentAction = this.getRequest().getParameter("currentAction");
        String selectedProductId =  this.getRequest().getParameter("productId");
        if(currentAction==null)
        {
        	currentAction=ACTION_INIT;
        } 
        
          try
        {
            if (!isPopulatedSuccessfully){
            	TestProductData testProductData  = this.getTestProductDataForUser();
            	tps = testProductData.getTestProducts();
            	 if( tps!=null ) {
            		vo.populate(userName, tps, itemSet, scheduleTest);
                 	vo.populateTopOrgnode(this.topNodesMap);
            	 }
            	 isPopulatedSuccessfully = true;
            }
        	           
            if(selectedProductId== null || selectedProductId.trim().length()==0)
            {
                if (tps.length > 0 && tps[0] != null)
                {
                     selectedProductId = tps[0].getProductId().toString();
                     vo.populateAccessCode(scheduleTest);
                     vo.populateDefaultDateAndTime(this.user.getTimeZone());
                }
           } 
            if(tps.length<=0) {
            	
            	vo.setNoTestExists(true);
            }else {
            	 vo.setNoTestExists(false);
            	 vo.setSelectedProductId(selectedProductId);
                 vo.setUserTimeZone(DateUtils.getUITimeZone(this.user.getTimeZone()));
                 
                 int selectedProductIndex = getProductIndexByID(selectedProductId);
           
                            
                 //this.condition.setOffGradeTestingDisabled(Boolean.FALSE);
                
                 
                 String acknowledgmentsURL =  tps[selectedProductIndex].getAcknowledgmentsURL();
                 if (acknowledgmentsURL != null)
                 {
                     acknowledgmentsURL = acknowledgmentsURL.trim();
                     if (!"".equals(acknowledgmentsURL))
                         this.getRequest().setAttribute("acknowledgmentsURL", acknowledgmentsURL);
                 }
            	
            }
    
            Gson gson = new Gson();
        	jsonData = gson.toJson(vo);
        	//System.out.println(jsonData);
        	try {

    			resp.setContentType(CONTENT_TYPE_JSON);
    			//resp.flushBuffer();
    			stream = resp.getOutputStream();
    			stream.write(jsonData.getBytes("UTF-8"));
    			resp.flushBuffer();
    		} catch (IOException e) {
    			
    			e.printStackTrace();
    		} 
    	
        } catch (Exception e) {
        	resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        	try {
				resp.flushBuffer();
			} catch (Exception e1) {
			}
			e.printStackTrace();
		} finally{
			if (stream!=null){
				try {
					stream.close();
				} catch (Exception e) {
				}
			}
		}
    	
    	
		return null;
    	
		
        
    }
    
    	@Jpf.Action()
        protected Forward saveTest(SessionOperationForm form)
        {
    		
    		//  form.validateValues(); NOT TO DO
    		Integer studentCountAfterSave = 0;
    		Integer testAdminId =null;
    		ValidationFailedInfo validationFailedInfo = new ValidationFailedInfo();
    		SuccessInfo successInfo = new SuccessInfo();
            boolean isAddOperation = true;
            int studentCountBeforeSave =0;
            boolean isValidationFailed = false;
            String jsonData = "";
            OperationStatus status = new OperationStatus();
            HttpServletResponse resp = getResponse();
        	resp.setCharacterEncoding("UTF-8"); 
        	OutputStream stream = null;
        	String testAdminIdString = (RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.TEST_ADMIN_ID, false, null));
        	String currentAction = RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.ACTION, true, "");
        	String isStudentListUpdated = RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.IS_STUDENT_LIST_UPDATED, true, "T");
        	
        	if(currentAction.equalsIgnoreCase("EDIT")){
        		isAddOperation = false;
        	} else if (currentAction.equalsIgnoreCase("ADD")){
        		isAddOperation = true;
        	} else if (testAdminIdString == null) {
        		isAddOperation = false;
        	} else {
        		try{
        			testAdminId = Integer.valueOf(testAdminIdString.trim());
        			isAddOperation = true;
        		} catch (Exception ne){
        			isAddOperation = false;
        		}
        	}
        	
        	if(isAddOperation || isStudentListUpdated.equalsIgnoreCase("T")){
        		String studentsBeforeSave =  RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.STUDENTS, true, "");
        		if (studentsBeforeSave!=null && studentsBeforeSave.trim().length()>1)
                    studentCountBeforeSave = studentsBeforeSave.trim().split(",").length;
        	} else {
        		//TODO
        	}
        	
            try
            {
                testAdminId = createSaveTest(this.getRequest(), validationFailedInfo, isAddOperation);
                if(!validationFailedInfo.isValidationFailed()) {
                	isValidationFailed = false;
                	RosterElementData red = this.testSessionStatus.getRosterForTestSession(this.userName,
                    		testAdminId, null, null, null);
                    studentCountAfterSave = red.getTotalCount().intValue(); 
                } else {
                	isValidationFailed = true;
                }
                
                                      
                                
            }   
            catch (InsufficientLicenseQuantityException e)
            {
                e.printStackTrace();
                String errorMessageHeader =  MessageResourceBundle.getMessage("SelectSettings.InsufficentLicenseQuantity.E001.Header");
                String errorMessageBody =  MessageResourceBundle.getMessage("SelectSettings.InsufficentLicenseQuantity.E001.Body");
                
                validationFailedInfo.setKey("SelectSettings.InsufficentLicenseQuantity.E001");
                validationFailedInfo.setMessageHeader(errorMessageHeader);
                validationFailedInfo.updateMessage(errorMessageBody);
                isValidationFailed = true;
        
            } 
            //START- Changed for deferred defect 64446
            catch (TransactionTimeoutException e)
            {
                e.printStackTrace();
                String errorMessageHeader = MessageResourceBundle.getMessage("SelectSettings.FailedToSaveTestSessionTransactionTimeOut.Header");
                String errorMessageBody =  MessageResourceBundle.getMessage("SelectSettings.FailedToSaveTestSessionTransactionTimeOut.Body");
                                
                
                validationFailedInfo.setKey("SelectSettings.FailedToSaveTestSessionTransactionTimeOut");
                validationFailedInfo.setMessageHeader(errorMessageHeader);
                validationFailedInfo.updateMessage(errorMessageBody);
                isValidationFailed = true;
            }
            catch (CTBBusinessException e)
            {
                e.printStackTrace();
                isValidationFailed = true;
                
                if(e instanceof ValidationException){
                	String errorMessageHeader =MessageResourceBundle.getMessage("FailedToSaveTestSession.ValidationException.Header");
                	String errorMessageBody =MessageResourceBundle.getMessage("FailedToSaveTestSession.ValidationException.Body");
                	validationFailedInfo.setKey("SYSTEM_EXCEPTION");
                    validationFailedInfo.setMessageHeader(errorMessageHeader);
                    validationFailedInfo.updateMessage(errorMessageBody);
                	
                } else  {
                	 String errorMessageHeader =MessageResourceBundle.getMessage("FailedToSaveTestSession");
                	 String errorMessageBody = MessageResourceBundle.getMessage("FailedToSaveTestSession.Body", e.getMessage());
                     validationFailedInfo.setKey("SYSTEM_EXCEPTION");
                     validationFailedInfo.setMessageHeader(errorMessageHeader);
                     validationFailedInfo.updateMessage(errorMessageBody);
                }

            } 
           if (!isValidationFailed && studentCountBeforeSave <= studentCountAfterSave) {
        	   
           		String messageHeader = "";
           		if(isAddOperation) {
           			messageHeader = MessageResourceBundle.getMessage("SelectSettings.TestSessionSaved.Header");
           		} else {
           			messageHeader = MessageResourceBundle.getMessage("SelectSettings.TestSessionEdited.Header");
           		}
           		//String messageBody = MessageResourceBundle.getMessage("SelectSettings.TestSessionSaved.Body");
           		successInfo.setKey("TEST_SESSION_SAVED");
           		successInfo.setMessageHeader(messageHeader);
           		//successInfo.updateMessage(messageBody);
        	   	status.setSuccess(true); 
        	   	status.setSuccessInfo(successInfo);
        	   	//idToStudentMap.clear(); // clear map
        	   	
           } else if (!isValidationFailed)
            {
                int removedCount = studentCountBeforeSave - studentCountAfterSave;
                String messageHeader = "";
           		if(isAddOperation) {
           			messageHeader = MessageResourceBundle.getMessage("SelectSettings.TestSessionSaved.Header");
           		} else {
           			messageHeader = MessageResourceBundle.getMessage("SelectSettings.TestSessionEdited.Header");
           		}
           		String messageBody = MessageResourceBundle.getMessage("RestrictedStudentsNotSaved", "" +removedCount);
           		successInfo.setKey("TEST_SESSION_SAVED_RES_STD");
           		successInfo.setMessageHeader(messageHeader);
           		successInfo.updateMessage(messageBody);
                status.setSuccess(true);
                status.setSuccessInfo(successInfo);
            	//idToStudentMap.clear(); // clear map
            } else {
            	status.setSuccess(false);
            	if("SYSTEM_EXCEPTION".equalsIgnoreCase(validationFailedInfo.getKey())){
            		status.setSystemError(true);
            		//idToStudentMap.clear(); // clear map
            	} else {
            		status.setSystemError(false);
            	}
            	status.setValidationFailedInfo(validationFailedInfo);
            }
            
           Gson gson = new Gson();
	       jsonData = gson.toJson(status);
	     //  System.out.println(jsonData);
	       	try {
	   			resp.setContentType(CONTENT_TYPE_JSON);
 	   			stream = resp.getOutputStream();
	   			stream.write(jsonData.getBytes("UTF-8"));
	   			resp.flushBuffer();
	   		} catch (IOException e) {
	   			e.printStackTrace();
   		} 
            return null;
           // return new Forward("success", form);
        }
    
    	@Jpf.Action()
        protected Forward getUserProductsDetails(SessionOperationForm form) {
    		
    		String jsonData = "";
    		OutputStream stream = null;
    		String selectedProductId = "";
    		HttpServletResponse resp = getResponse();
    	    resp.setCharacterEncoding("UTF-8"); 
    	    //String testAdminIdString = RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.TEST_ADMIN_ID, false, null);
    	    ScheduledSavedTestVo vo = new ScheduledSavedTestVo();
    	    OperationStatus status = new OperationStatus();
    	    vo.setOperationStatus(status) ;
    	    
    	    try {

    	    	if (!isPopulatedSuccessfully){
                	TestProductData testProductData  = this.getTestProductDataForUser();
                	tps = testProductData.getTestProducts();
                	 if( tps!=null ) {
                		 this.vo.populate(userName, tps, itemSet, scheduleTest);
                		 this.vo.populateTopOrgnode(this.topNodesMap);
                	 }
                	 isPopulatedSuccessfully = true;
                }
            	           
              
                 if (tps.length > 0 && tps[0] != null)
                  {
                	 //productName = tps[0].getProductName();
                     selectedProductId = tps[0].getProductId().toString();
                     this.vo.populateAccessCode(scheduleTest);
                     this.vo.populateDefaultDateAndTime(this.user.getTimeZone());
                    }
           
                if(tps.length<=0) {
                	
                	this.vo.setNoTestExists(true);
                }else {
                	this.vo.setNoTestExists(false);
                	this.vo.setSelectedProductId(selectedProductId);
                	this.vo.setUserTimeZone(DateUtils.getUITimeZone(this.user.getTimeZone()));
                     
                     int selectedProductIndex = getProductIndexByID(selectedProductId);
                    String acknowledgmentsURL =  tps[selectedProductIndex].getAcknowledgmentsURL();
                     if (acknowledgmentsURL != null)
                     {
                         acknowledgmentsURL = acknowledgmentsURL.trim();
                         if (!"".equals(acknowledgmentsURL))
                             this.getRequest().setAttribute("acknowledgmentsURL", acknowledgmentsURL);
                     }
                	
                }
                
                vo.setUserProductsDetails(this.vo);

    	    } catch(CTBBusinessException e){
    	    	 e.printStackTrace(); 
    	    	 status.setSystemError(true);
    	    	 ValidationFailedInfo validationFailedInfo = new ValidationFailedInfo();
    	    	 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SelectSettings.FailedToLoadTestSession"));
    	    	 if(e.getMessage()!=null && e.getMessage().length()>0){
    	    		 validationFailedInfo.updateMessage(e.getMessage()); 
    	    	 }
    			 status.setValidationFailedInfo(validationFailedInfo);
    	    	
    	    } catch(Exception e) {
    	    	e.printStackTrace(); 
    	    	status.setSystemError(true);
    	    	 ValidationFailedInfo validationFailedInfo = new ValidationFailedInfo();
    	    	 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("System.Exception.Header"));
    			 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("System.Exception.Body"));
    			 status.setValidationFailedInfo(validationFailedInfo);
    	    }
    		
			Gson gson = new Gson();
	
			jsonData = gson.toJson(vo);
			//System.out.println(jsonData);
			try {
				resp.setContentType(CONTENT_TYPE_JSON);
				stream = resp.getOutputStream();
				stream.write(jsonData.getBytes("UTF-8"));
				resp.flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			return null;
	   }
    	
    	@Jpf.Action()
        protected Forward getTestDetails(SessionOperationForm form) {
    		
    		String jsonData = "";
    		OutputStream stream = null;
    		HttpServletResponse resp = getResponse();
    	    resp.setCharacterEncoding("UTF-8"); 
    	    String testAdminIdString = RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.TEST_ADMIN_ID, false, null);
    	    ScheduledSavedTestVo vo = new ScheduledSavedTestVo();
    	    OperationStatus status = new OperationStatus();
    	    vo.setOperationStatus(status) ;
    	    try {
    	    	Integer testAdminId = Integer.valueOf(testAdminIdString);
    	    	ScheduledSession scheduledSession = this.scheduleTest.getScheduledSessionDetails(this.userName, testAdminId);
    	    	vo.setSavedTestDetails(scheduledSession);
    	    	vo.setProductType(TestSessionUtils.getProductType(scheduledSession.getTestSession().getProductType()));
    	    	Date now = new Date(System.currentTimeMillis());
    	    	Date today = com.ctb.util.DateUtils.getAdjustedDate(now, TimeZone.getDefault().getID(), this.user.getTimeZone(), now);
    	    	TestElement selectedTest = this.scheduleTest.getTestElementMinInfoById(scheduledSession.getTestSession().getItemSetId()); 
    	    	Date ovLoginStart = selectedTest.getOverrideLoginStartDate();
    	    	Date ovLoginEnd = selectedTest.getOverrideLoginEndDate();
    	    	if (ovLoginStart != null && !(DateUtils.isBeforeToday(ovLoginStart , this.user.getTimeZone() ))) {
    	    		vo.setMinLoginStartDate(DateUtils.formatDateToDateString(ovLoginStart));
	        	} else {
	        		vo.setMinLoginStartDate(DateUtils.formatDateToDateString(today));
	        	}
	        	
	        	if(ovLoginEnd!= null ) {
	        		vo.setMinLoginEndDate(DateUtils.formatDateToDateString(ovLoginEnd));
	        		
	        	} 
    	    	

    	    	if (this.user == null || topNodesMap.size() ==0 ){
    	    		initialize();
    	    	}
    	    	vo.setUserRole(this.user.getRole().getRoleName());
    	    	TestSession testSession = scheduledSession.getTestSession();
    	    	//String schedulerName = testSession.getCreatedBy();
                //User scheduler = this.scheduleTest.getUserDetails(this.userName, schedulerName);
                
                
                if( testSession.getTestAdminStatus().equals("PA")){
                	vo.setTestSessionExpired(Boolean.TRUE);
                } else {
                	vo.setTestSessionExpired(Boolean.FALSE);
                }
                vo.populateTopOrgnode(topNodesMap);
                vo.populateTimeZone();
                
                status.setSuccess(true);
                
                String timeZone = testSession.getTimeZone();
                testSession.setTimeZone(DateUtils.getUITimeZone(timeZone));
                testSession.setLoginStartDateString(DateUtils.formatDateToDateString(testSession.getLoginStartDate()));
                testSession.setLoginEndDateString(DateUtils.formatDateToDateString(testSession.getLoginEndDate()));
                testSession.setDailyLoginStartTimeString(DateUtils.formatDateToTimeString(testSession.getDailyLoginStartTime()));
                testSession.setDailyLoginEndTimeString(DateUtils.formatDateToTimeString(testSession.getDailyLoginEndTime()));
                
    	    	
    	    } catch(CTBBusinessException e){
    	    	 e.printStackTrace(); 
    	    	 status.setSystemError(true);
    	    	 ValidationFailedInfo validationFailedInfo = new ValidationFailedInfo();
    	    	 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SelectSettings.FailedToLoadTestSession"));
    	    	 if(e.getMessage()!=null && e.getMessage().length()>0){
    	    		 validationFailedInfo.updateMessage(e.getMessage()); 
    	    	 }
    			 status.setValidationFailedInfo(validationFailedInfo);
    	    	
    	    } catch(Exception e) {
    	    	e.printStackTrace(); 
    	    	status.setSystemError(true);
    	    	 ValidationFailedInfo validationFailedInfo = new ValidationFailedInfo();
    	    	 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("System.Exception.Header"));
    			 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("System.Exception.Body"));
    			 status.setValidationFailedInfo(validationFailedInfo);
    	    }
    		
			Gson gson = new Gson();
	
			jsonData = gson.toJson(vo);
			//System.out.println(jsonData);
			try {
				resp.setContentType(CONTENT_TYPE_JSON);
				stream = resp.getOutputStream();
				stream.write(jsonData.getBytes("UTF-8"));
				resp.flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			return null;
	   }
    	
    	@Jpf.Action()
        protected Forward getScheduledStudents(SessionOperationForm form) {
    		
    		String jsonData = "";
    		OutputStream stream = null;
    		HttpServletResponse resp = getResponse();
    	    resp.setCharacterEncoding("UTF-8"); 
    	    String testAdminIdString = RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.TEST_ADMIN_ID, false, null);
    	    ScheduledSavedTestVo vo = new ScheduledSavedTestVo();
    	    OperationStatus status = new OperationStatus();
    	    vo.setOperationStatus(status) ;
    	    try {
    	    	Integer testAdminId = Integer.valueOf(testAdminIdString);
    	    	ScheduledSession scheduledSession = this.scheduleTest.getScheduledStudentsMinimalInfoDetails(this.userName, testAdminId);
    	    	SessionStudent[] students =  scheduledSession.getStudents();
    	    	List<SessionStudent> studentsList = buildStudentList(students);
    	    	vo.setSavedStudentsDetails(studentsList);
                status.setSuccess(true);
               
                
    	    	
    	    } catch(CTBBusinessException e){
    	    	 e.printStackTrace(); 
    	    	 status.setSystemError(true);
    	    	 ValidationFailedInfo validationFailedInfo = new ValidationFailedInfo();
    	    	 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SelectSettings.FailedToLoadTestSession"));
    	    	 if(e.getMessage()!=null && e.getMessage().length()>0){
    	    		 validationFailedInfo.updateMessage(e.getMessage()); 
    	    	 }
    			 status.setValidationFailedInfo(validationFailedInfo);
    	    	
    	    } catch(Exception e) {
    	    	e.printStackTrace(); 
    	    	status.setSystemError(true);
    	    	 ValidationFailedInfo validationFailedInfo = new ValidationFailedInfo();
    	    	 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("System.Exception.Header"));
    			 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("System.Exception.Body"));
    			 status.setValidationFailedInfo(validationFailedInfo);
    	    }
    		
			Gson gson = new Gson();
			jsonData = gson.toJson(vo);
			//System.out.println(jsonData);
			try {
				resp.setContentType(CONTENT_TYPE_JSON);
				stream = resp.getOutputStream();
				stream.write(jsonData.getBytes("UTF-8"));
				resp.flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			return null;
	  }
    	
    @Jpf.Action()
    protected Forward getScheduleProctor(SessionOperationForm form) {
		
		String jsonData = "";
		OutputStream stream = null;
		HttpServletResponse resp = getResponse();
	    resp.setCharacterEncoding("UTF-8"); 
	    String testAdminIdString = RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.TEST_ADMIN_ID, false, null);
	    ScheduledSavedTestVo vo = new ScheduledSavedTestVo();
	    OperationStatus status = new OperationStatus(); 
	    vo.setOperationStatus(status) ;
	    try {
	    	Integer testAdminId = Integer.valueOf(testAdminIdString);
	    	ScheduledSession scheduledSession = this.scheduleTest.getScheduledProctorsMinimalInfoDetails(this.userName, testAdminId);
	    	 List<UserProfileInformation> proctors= buildProctorList(scheduledSession.getProctors());
	    	 vo.setSavedProctorsDetails(proctors);
            status.setSuccess(true);
           
            
	    	
	    } catch(CTBBusinessException e){
	    	 e.printStackTrace(); 
	    	 status.setSystemError(true);
	    	 ValidationFailedInfo validationFailedInfo = new ValidationFailedInfo();
	    	 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SelectSettings.FailedToLoadTestSession"));
	    	 if(e.getMessage()!=null && e.getMessage().length()>0){
	    		 validationFailedInfo.updateMessage(e.getMessage()); 
	    	 }
			 status.setValidationFailedInfo(validationFailedInfo);
	    	
	    } catch(Exception e) {
	    	e.printStackTrace(); 
	    	status.setSystemError(true);
	    	 ValidationFailedInfo validationFailedInfo = new ValidationFailedInfo();
	    	 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("System.Exception.Header"));
			 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("System.Exception.Body"));
			 status.setValidationFailedInfo(validationFailedInfo);
	    }
		
		Gson gson = new Gson();
		jsonData = gson.toJson(vo);
		//System.out.println(jsonData);
		try {
			resp.setContentType(CONTENT_TYPE_JSON);
			stream = resp.getOutputStream();
			stream.write(jsonData.getBytes("UTF-8"));
			resp.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
  }
    	
   
    	 private Integer createSaveTest(HttpServletRequest httpServletRequest, ValidationFailedInfo validationFailedInfo, boolean isAddOperation) throws CTBBusinessException
    	    {  
    		 Integer newTestAdminId = null;
    		 ScheduledSession scheduledSession = new ScheduledSession();
    		 populateTestSession(scheduledSession, httpServletRequest, validationFailedInfo , isAddOperation);
    		 if(!validationFailedInfo.isValidationFailed()) {
    			 populateScheduledUnits(scheduledSession, httpServletRequest, validationFailedInfo, isAddOperation ); 
    		 }
    		 if(!validationFailedInfo.isValidationFailed()) {
    			 populateSessionStudent(scheduledSession, httpServletRequest, validationFailedInfo, isAddOperation );
    		 }
    		 
    		 if(!validationFailedInfo.isValidationFailed()) {
    			 populateProctor(scheduledSession, httpServletRequest , validationFailedInfo, isAddOperation);
    		 }
    		 
    		 if(!validationFailedInfo.isValidationFailed()) {
    			 if(scheduledSession.getTestSession().getTestAdminId()!=null){
    				 newTestAdminId = this.scheduleTest.updateTestSession(this.userName, scheduledSession);  
    			 } else {
    				 newTestAdminId = this.scheduleTest.createNewTestSession(this.userName, scheduledSession);  
    			 }
    			 
    		 }    		 
    	        return newTestAdminId;
    }
    
     private void populateScheduledUnits(ScheduledSession scheduledSession,
				HttpServletRequest request, ValidationFailedInfo validationFailedInfo, boolean isAddOperation) {
    	/* List subtestList = null;*/
	     //boolean sessionHasLocator = false;
    	 try{
    		 String productType				= RequestUtil.getValueFromRequest(request, RequestUtil.PRODUCT_TYPE, true, "");
        	 Integer itemSetId        		= Integer.valueOf(RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_ITEM_SET_ID, false, null));
        	 String hasBreakValue     		= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_HAS_BREAK, false, null);
        	 String hasBreak          		= (hasBreakValue == null || !(hasBreakValue.trim().equals("T") || hasBreakValue.trim().equals("F"))) ? "F" :  hasBreakValue.trim();
        	 boolean hasBreakBoolean        = (hasBreak.equals("T")) ? true : false;
        	 String[] itemSetIdTDs          = RequestUtil.getValuesFromRequest(request, RequestUtil.TEST_ITEM_SET_ID_TD, true ,  new String [0]);
        	 String[] accesscodes           = RequestUtil.getValuesFromRequest(request, RequestUtil.TEST_ITEM_IND_ACCESS_CODE, true ,  new String [itemSetIdTDs.length]);
        	 String[] itemSetForms          = RequestUtil.getValuesFromRequest(request, RequestUtil.TEST_ITEM_SET_FORM, true ,  new String [itemSetIdTDs.length]);
        	 String[] itemSetisDefault      = RequestUtil.getValuesFromRequest(request, RequestUtil.TEST_ITEM_IS_SESSION_DEFAULT, true ,  new String [itemSetIdTDs.length]);
        	 
        	 
        	 //List<SubtestVO>  subtestList   = idToTestMap.get(itemSetId).getSubtests();
        	 List<SubtestVO>  subtestList   = new ArrayList<SubtestVO>();
        	 for(int ii =0, jj =itemSetIdTDs.length; ii<jj; ii++ ){
        		 SubtestVO subtest = new SubtestVO();
        		 subtest.setId(Integer.valueOf(itemSetIdTDs[ii].trim()));
        		 subtest.setTestAccessCode(accesscodes[ii]);
        		 subtest.setSessionDefault(itemSetisDefault[ii]);
        		 if(itemSetForms[ii] != null && itemSetForms[ii].trim().length()>0){
        			 subtest.setLevel(itemSetForms[ii]);
        		 }
        		 subtestList.add(subtest);
        		 
        	 }
        	                     
    	        if (productType!=null && TestSessionUtils.isTabeProduct(productType).booleanValue())
    	        {
    	            // for tabe test
    	        	/*   if (TestSessionUtils.isTabeBatterySurveyProduct(this.productType).booleanValue())
    	            {
    	                
    	                subtestList = TestSessionUtils.setupSessionSubtests(this.sessionSubtests, this.defaultSubtests); 
    	                
    	                String autoLocator = form.getAutoLocator();
    	                if ((autoLocator != null) && autoLocator.equals("true"))
    	                {            
    	                    TestSessionUtils.restoreLocatorSubtest(subtestList, this.locatorSubtest);
    	                    sessionHasLocator = true;
    	                }
    	                else
    	                {
    	                    TestSessionUtils.setDefaultLevels(subtestList, "E");  // make sure set level = 'E' if null
    	                }
    	            } 
    	            else
    	            {
    	                // tabe locator test
    	                subtestList = TestSessionUtils.cloneSubtests(this.defaultSubtests);
    	                TestSessionUtils.setDefaultLevels(subtestList, "1");  // make sure set level = '1' for test locator
    	            }    */   
    	            
    	        }
    	        else
    	        {
    	            // for non-tabe test
    	            subtestList = TestSessionUtils.cloneSubtests(subtestList);
    	        }
    	        
    	        
    	        TestElement [] newTEs = new TestElement[subtestList.size()];
    	        
    	        for (int i=0; i < subtestList.size(); i++)
    	        {
    	            SubtestVO subVO= (SubtestVO)subtestList.get(i);
    	            TestElement te = new TestElement();
    	        
    	            te.setItemSetId(subVO.getId());
    	            
    	            if (TestSessionUtils.isTabeProduct(productType).booleanValue())
    	            {                
    	                String level = subVO.getLevel();
    	                te.setItemSetForm(level);
    	            }
    	            
    	            if (!hasBreakBoolean ) {
    	            	//String accessCode = RequestUtil.getValueFromRequest(request, RequestUtil.ACCESS_CODE, true, "");
    	            	String accessCode = scheduledSession.getTestSession().getAccessCode();
    	            	te.setAccessCode(accessCode);
    	            } else {
    	            	//String accessCode = RequestUtil.getValueFromRequest(request, RequestUtil.ACCESS_CODEB+i, true, "");
    	            	//te.setAccessCode(accessCode);
    	            	te.setAccessCode(subVO.getTestAccessCode());
    	            }
    	               
    	            
    	            te.setSessionDefault(subVO.getSessionDefault());
    	            
    	            newTEs[i] = te;
    	        }
    	        
    	        scheduledSession.setScheduledUnits(newTEs);
    	        validateScheduledUnits(scheduledSession, hasBreakBoolean, validationFailedInfo, isAddOperation);
            
    	       
    	 } catch (Exception e) {
    		 e.printStackTrace();
    		 validationFailedInfo.setKey("SYSTEM_EXCEPTION");
			 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("System.Exception.Header"));
			 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("System.Exception.Body"));
    	 }
    	 
	        
	        
	        
	        
	        
			
		}

	private void populateProctor(ScheduledSession scheduledSession,
			HttpServletRequest request,
			ValidationFailedInfo validationFailedInfo, boolean isAddOperation) {
		

		try {
			boolean isProcListUpdated = true;
			if(!isAddOperation){
				String isStudentUpdated = RequestUtil.getValueFromRequest(request, RequestUtil.IS_PROCTOR_LIST_UPDATED, true, "true");
				if(isStudentUpdated.equalsIgnoreCase("false"))
					isProcListUpdated = false;
			}
			
			if(isAddOperation || isProcListUpdated) {
				String proctorsData = RequestUtil.getValueFromRequest(request, RequestUtil.PROCTORS, true, "");
				int proctorCount = 0;
				if (proctorsData != null
						&& proctorsData.trim().length() > 1) {
					proctorCount = proctorsData.split(",").length;
				}
				if (proctorCount > 0) {
					ArrayList<User> proctorList = new ArrayList<User>(proctorCount);
					String[] procs = proctorsData.split(",");
					for (String procrec : procs) {
						StringTokenizer st = new StringTokenizer(procrec, ":");
						User us = new User();
						while (st.hasMoreTokens()) {
							StringTokenizer keyVal = new StringTokenizer(st
									.nextToken(), "=");
	
							String key = keyVal.nextToken();
							String val = null;
							if (keyVal.countTokens() > 0) {
								val = keyVal.nextToken();
							}
	
							if (key.equalsIgnoreCase("userId")) {
								us.setUserId(Integer.valueOf(val));
							} else if (key.equalsIgnoreCase("userName")) {
								us.setUserName(val);
							} else if (key.equalsIgnoreCase("copyable")) {
								us.setCopyable(val);
							} 
						}
	
						proctorList.add(us);
					}
	
					scheduledSession.setProctors(proctorList.toArray(new User[proctorList.size()]));
				} else {
					User[] proctorArray = new User[1];
					proctorArray[0]= this.user;
					scheduledSession.setProctors(proctorArray);
				}
			} else {
				ScheduledSession schSession = this.scheduleTest.getScheduledProctorsMinimalInfoDetails(this.userName, scheduledSession.getTestSession().getTestAdminId());
				scheduledSession.setProctors(schSession.getProctors());
			}
		} catch (Exception e) {
			e.printStackTrace();
			validationFailedInfo.setKey("SYSTEM_EXCEPTION");
			validationFailedInfo.setMessageHeader(MessageResourceBundle
					.getMessage("System.Exception.Header"));
			validationFailedInfo.updateMessage(MessageResourceBundle
					.getMessage("System.Exception.Body"));
		}

	}

	private void populateSessionStudent(ScheduledSession scheduledSession,
				HttpServletRequest httpServletRequest,
			ValidationFailedInfo validationFailedInfo, boolean isAddOperation) {

		try {
			
			boolean isStudentListUpdated = true;
			if(!isAddOperation){
				String isStudentUpdated = RequestUtil.getValueFromRequest(httpServletRequest, RequestUtil.IS_STUDENT_LIST_UPDATED, true, "true");
				if(isStudentUpdated.equalsIgnoreCase("false"))
					isStudentListUpdated = false;
			}
			
			if(isAddOperation || isStudentListUpdated){
	
				String studentsBeforeSave = RequestUtil.getValueFromRequest(httpServletRequest, RequestUtil.STUDENTS, true, "");
				int studentCountBeforeSave = 0;
				if (studentsBeforeSave != null
						&& studentsBeforeSave.trim().length() > 1) {
					studentCountBeforeSave = studentsBeforeSave.split(",").length;
				}
				ArrayList<SessionStudent> sessionStudents = new ArrayList<SessionStudent>(studentCountBeforeSave);
				if (studentCountBeforeSave > 0) {
					String[] studs = studentsBeforeSave.split(",");
					for (String std : studs) {
						StringTokenizer st = new StringTokenizer(std, ":");
						SessionStudent ss = new SessionStudent();
						while (st.hasMoreTokens()) {
							StringTokenizer keyVal = new StringTokenizer(st.nextToken(), "=");
							
							String key = keyVal.nextToken();
							String val = null;
							if(keyVal.countTokens()>0) {
								val= keyVal.nextToken();
							}
	
							if (key.equalsIgnoreCase("studentId")) {
								ss.setStudentId(Integer.valueOf(val));
							} else if (key.equalsIgnoreCase("orgNodeId")) {
								ss.setOrgNodeId(Integer.valueOf(val));
							} else if (key.equalsIgnoreCase("extendedTimeAccom")) {
								ss.setExtendedTimeAccom(val);
							} else if (key.equalsIgnoreCase("statusCopyable")) {
								EditCopyStatus status = new EditCopyStatus();
								status.setCopyable(val);
								ss.setStatus(status);
							} else if (key.equalsIgnoreCase("itemSetForm")) {
								ss.setItemSetForm(val);
							}
						}
	
						sessionStudents.add(ss);
	
					}
				
			}
				scheduledSession.setStudents(sessionStudents
						.toArray(new SessionStudent[sessionStudents.size()]));
		
		} else {
			ScheduledSession schSession = this.scheduleTest.getScheduledStudentsMinimalInfoDetails(this.userName, scheduledSession.getTestSession().getTestAdminId());
	    	scheduledSession.setStudents(schSession.getStudents());
		}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			validationFailedInfo.setKey("SYSTEM_EXCEPTION");
			validationFailedInfo.setMessageHeader(MessageResourceBundle
					.getMessage("System.Exception.Header"));
			validationFailedInfo.updateMessage(MessageResourceBundle
					.getMessage("System.Exception.Body"));
		}

	}
             
	

	private void populateTestSession(ScheduledSession scheduledSession, HttpServletRequest request, ValidationFailedInfo validationFailedInfo, boolean isAddOperation) {
		
		 try{
			 TestSession testSession = new TestSession();
			 Set<Integer> keySet            = this.topNodesMap.keySet();
			 Integer[] topnodeids= (keySet).toArray(new Integer[keySet.size()]);
			 Integer creatorOrgNod    		= topnodeids[0];
			 Integer itemSetId        		= Integer.valueOf(RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_ITEM_SET_ID, false, null));
			 
			 //TestVO selectedTest = idToTestMap.get(itemSetId);
			 
			 Integer productId        			= Integer.valueOf(RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_PRODUCT_ID, true, "-1"));
			 String dailyLoginEndTimeString		=RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_END_TIME, false, null);
			 String dailyLoginStartTimeString	= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_START_TIME, false, null);
			 String dailyLoginEndDateString		= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_END_DATE, false, null);
			 String dailyLoginStartDateString	= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_START_DATE, false, null);

			 Date dailyLoginEndTime   		= DateUtils.getDateFromTimeString(dailyLoginEndTimeString);
			 Date dailyLoginStartTime 		= DateUtils.getDateFromTimeString(dailyLoginStartTimeString);
			 Date dailyLoginEndDate   		= DateUtils.getDateFromDateString(dailyLoginEndDateString);
			 Date dailyLoginStartDate 		= DateUtils.getDateFromDateString(dailyLoginStartDateString);
			 String location          		= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_LOCATION, false, null);
			 String hasBreakValue     		= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_HAS_BREAK, false, null);
			 String hasBreak          		= (hasBreakValue == null || !(hasBreakValue.trim().equals("T") || hasBreakValue.trim().equals("F"))) ? "F" :  hasBreakValue.trim();
			 boolean hasBreakBoolean        = (hasBreak.equals("T")) ? true : false;
			 String isRandomize       		= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_RANDOMIZE, true, "");
			 String timeZone          		= DateUtils.getDBTimeZone( RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_TIME_ZONE, false, null));
			 System.out.println("timeZone" + timeZone);
			 String sessionName		  		= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_TEST_NAME, false, null);
			 //String sessionName       		= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_SESSION_NAME, false, null);
			 String showStdFeedbackVal   	= RequestUtil.getValueFromRequest(request, RequestUtil.SHOW_STUDENT_FEEDBACK, true, "false");
			 String showStdFeedback         = (showStdFeedbackVal==null || !(showStdFeedbackVal.trim().equals("true") || showStdFeedbackVal.trim().equals("false")) )? "F" :(showStdFeedbackVal.trim().equals("true")? "T" : "F");  
			 String productType				= RequestUtil.getValueFromRequest(request, RequestUtil.PRODUCT_TYPE, true, "");
			 String isEndTestSession 		= RequestUtil.getValueFromRequest(request, RequestUtil.TEST_ADMIN_STATUS, true, "");
			 //String formOperand       		= RequestUtil.getValueFromRequest(request, RequestUtil.FORM_OPERAND, true, TestSession.FormAssignment.ROUND_ROBIN);
			 //String overrideFormAssignment 	= RequestUtil.getValueFromRequest(request, RequestUtil.OVERRIDE_FORM_ASSIGNMENT, false, null);
			 //String overrideLoginStartDate    = RequestUtil.getValueFromRequest(request, RequestUtil.OVERRIDE_LOGIN_START_DATE, false, null);
			 /*Date overrideLoginSDate        = null ;
			 if(overrideLoginStartDate!=null)
				 overrideLoginSDate = DateUtils.getDateFromDateString(overrideLoginStartDate);*/
			 //String formAssigned			= RequestUtil.getValueFromRequest(request, RequestUtil.FORM_ASSIGNED, true, "");
			 
			 String testAdminIdString = (RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.TEST_ADMIN_ID, false, null));
			 Integer testAdminId = null;
			 if(!isAddOperation ){
				 testAdminId = Integer.valueOf(testAdminIdString.trim());
			 }
			 String formOperand       		=  TestSession.FormAssignment.ROUND_ROBIN;
			 TestElement selectedTest = scheduleTest.getTestElementMinInfoById(itemSetId); 
			 if(selectedTest.getOverrideFormAssignmentMethod() != null) {
				 formOperand = selectedTest.getOverrideFormAssignmentMethod();
	           }else if (selectedTest.getForms()!= null && selectedTest.getForms().length > 0 ) {
	        	   formOperand = TestSession.FormAssignment.ROUND_ROBIN;
	            } else {
	            	formOperand = TestSession.FormAssignment.ROUND_ROBIN;
	           }

			 String overrideFormAssignment 	=  selectedTest.getOverrideFormAssignmentMethod();
			 Date overrideLoginSDate  		=  selectedTest.getOverrideLoginStartDate();
			 String formAssigned			=  (selectedTest.getForms() ==null || selectedTest.getForms().length==0)? null: selectedTest.getForms()[0]; 
			 String testName       		    = 	selectedTest.getItemSetName(); 
			 Date overrideLoginEDate  		=  selectedTest.getOverrideLoginEndDate();
			 
			 TimeZone defaultTimeZone = TimeZone.getDefault();
			 Date now = new Date(System.currentTimeMillis());
	         now = com.ctb.util.DateUtils.getAdjustedDate(now, defaultTimeZone.getID(), timeZone, now);
    		 String timeStr = DateUtils.formatDateToTimeString(now);
		     String dateStr = DateUtils.formatDateToDateString(now);
			 // setting default value
			 testSession.setTestAdminId(testAdminId);			 
			 testSession.setTestAdminStatus("CU");
			 testSession.setLoginEndDate(dailyLoginEndDate);
			  testSession.setDailyLoginEndTime(dailyLoginEndTime);
			 if(testAdminId != null && "true".equalsIgnoreCase(isEndTestSession)){
				 testSession.setTestAdminStatus("PA");
				 testSession.setLoginEndDate(now);
				 testSession.setDailyLoginEndTime(now);
			 }
	         testSession.setTestAdminType("SE");
	         testSession.setActivationStatus("AC");
	         testSession.setEnforceTimeLimit("T");
	         testSession.setCreatedBy(this.userName);

	         
	         testSession.setCreatorOrgNodeId(creatorOrgNod);
	         testSession.setShowStudentFeedback(showStdFeedback);
	         testSession.setProductId(productId);	    
	         testSession.setDailyLoginStartTime(dailyLoginStartTime);
	         testSession.setLocation(location);
	         testSession.setEnforceBreak(hasBreak);
	         testSession.setIsRandomize(isRandomize);	         	       
	         testSession.setLoginStartDate(dailyLoginStartDate);
	         testSession.setTimeZone(timeZone);
	         testSession.setTestName(testName);
	         testSession.setTestAdminName(sessionName);

	         if (formOperand.equals(TestSession.FormAssignment.MANUAL))
	             testSession.setFormAssignmentMethod(TestSession.FormAssignment.MANUAL);
	         else if (formOperand.equals(TestSession.FormAssignment.ALL_SAME))
	             testSession.setFormAssignmentMethod(TestSession.FormAssignment.ALL_SAME);
	         else 
	             testSession.setFormAssignmentMethod(TestSession.FormAssignment.ROUND_ROBIN);
	         
	        testSession.setPreferredForm(formAssigned);      
	         
	         testSession.setOverrideFormAssignmentMethod(overrideFormAssignment);
	         testSession.setOverrideLoginStartDate(overrideLoginSDate);
	         testSession.setOverrideLoginEndDate(overrideLoginEDate);
	         
	         testSession.setItemSetId(itemSetId);
	         
	         if (productType!=null && TestSessionUtils.isTabeProduct(productType).booleanValue())
	         {
	             testSession.setFormAssignmentMethod(TestSession.FormAssignment.MANUAL);
	         }

	         if (hasBreakBoolean)
	         {
	        	String accessCode = RequestUtil.getValuesFromRequest(request, RequestUtil.TEST_ITEM_IND_ACCESS_CODE, true, new String [0])[0];
	         	testSession.setAccessCode(accessCode);    
	         }
	         else
	         {
	        	 String accessCode = RequestUtil.getValueFromRequest(request, RequestUtil.ACCESS_CODE, true, "");
	        	 testSession.setAccessCode(accessCode); 
	         }
	         
	         validateTestSession(testSession, validationFailedInfo);
	         if(!validationFailedInfo.isValidationFailed()) {
	        	validateTestSessionDate(dailyLoginEndDateString,dailyLoginStartDateString, dailyLoginEndTimeString, dailyLoginStartTimeString, timeZone, overrideLoginSDate,overrideLoginEDate, validationFailedInfo, isAddOperation); 
	         }
	         
	         scheduledSession.setTestSession(testSession);
			 
		 } catch (Exception e) {
			 e.printStackTrace();
			 validationFailedInfo.setKey("SYSTEM_EXCEPTION");
			 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("System.Exception.Header"));
			 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("System.Exception.Body"));
			 
		 }
		 // retrieving data from request
		 
		 
			
		}

     private void validateTestSessionDate(String dailyLoginEndDateString,
			String dailyLoginStartDateString, String dailyLoginEndTimeString,
			String dailyLoginStartTimeString, String timeZonep,
			Date overrideLoginSDate,Date overrideLoginEDate, ValidationFailedInfo validationFailedInfo, boolean isAddOperation) {
    	 if ((DateUtils.validateDateString(dailyLoginStartDateString) == DateUtils.DATE_INVALID) ||( DateUtils.validateDateString(dailyLoginEndDateString)== DateUtils.DATE_INVALID)){
    		 validationFailedInfo.setKey("SaveTest.InvalidDate");
 			 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SaveTest.InvalidDate.Header"));
 			 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("SaveTest.InvalidDate.Body"));
    		 
    	 } else{
    		 Date dateStarted = DateUtils.getDateFromDateString(dailyLoginStartDateString);
             Date dateEnded = DateUtils.getDateFromDateString(dailyLoginEndDateString);
             Date timeStarted = DateUtils.getDateFromTimeString(dailyLoginStartTimeString);
             Date timeEnded = DateUtils.getDateFromTimeString(dailyLoginEndTimeString);
             
             String strDateTime = "";
             if (dailyLoginEndDateString != null && dailyLoginEndTimeString != null)
                 strDateTime = dailyLoginEndDateString + " " + dailyLoginEndTimeString;
             Date datetimeEnded = DateUtils.getDateFromDateTimeString(strDateTime);
             String timeZone = timeZonep;
             
             
    		 if( overrideLoginSDate != null && dateStarted.compareTo(overrideLoginSDate ) < 0){
    			 validationFailedInfo.setKey("SaveTest.StartDateBeforeOverrideStartDate");
     			 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SaveTest.StartDateBeforeOverrideStartDate.Header","" +DateUtils.formatDateToDateString(overrideLoginSDate)));
    		 } else if( overrideLoginEDate != null && dateEnded.compareTo(overrideLoginEDate ) > 0){
    			 validationFailedInfo.setKey("SaveTest.StartDateAfterOverrideEndDate");
     			 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SaveTest.StartDateAfterOverrideEndDate.Header","" +DateUtils.formatDateToDateString(overrideLoginEDate)));
    		 } else if ( isAddOperation && DateUtils.isBeforeToday(dateStarted, timeZone) ){
    			 validationFailedInfo.setKey("SaveTest.StartDateBeforeOverrideStartDate");
     			 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SaveTest.StartDateBeforeToday.Header"));
     			 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("SaveTest.StartDateBeforeToday.Body"));
    		 } else if ( isAddOperation && DateUtils.isBeforeNow(datetimeEnded, timeZone) ) {
    			 validationFailedInfo.setKey("SaveTest.EndDateTimeBeforeNow");
     			 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SaveTest.EndDateTimeBeforeNow.Header"));
     			 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("SaveTest.EndDateTimeBeforeNow.Body"));
    		 } else if ( dateStarted.compareTo(dateEnded)>0 ) {
    			 validationFailedInfo.setKey("SaveTest.EndDateBeforeStartDate");
     			 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SaveTest.EndDateBeforeStartDate"));
    		 } else if( timeStarted.compareTo(timeEnded)>=0 ) {
    			 validationFailedInfo.setKey("SaveTest.EndTimeBeforeStartTime");
     			 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SaveTest.EndTimeBeforeStartTime"));
    		 } 
    		 
    		 
    	 }
            
		
	}

     private void validateTestSession(TestSession testSession,	ValidationFailedInfo validationFailedInfo) throws Exception {
		String[] TACs = new String[1];
		TACs[0] = testSession.getAccessCode();
		if( testSession.getTestAdminName() == null || testSession.getTestAdminName().trim().length()==0 ) {
			validationFailedInfo.setKey("SaveTest.TestSessionNameRequired");
			validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SaveTest.TestSessionNameRequired.Header"));
			validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("SaveTest.TestSessionNameRequired.Body"));
		}else if (!WebUtils.validString(testSession.getTestAdminName())) {
			validationFailedInfo.setKey("SelectSettings.TestSessionName.InvalidCharacters");
			validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SelectSettings.TestSessionName.InvalidCharacters.Header"));
			validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("SelectSettings.TestSessionName.InvalidCharacters.Body"));
		} else if (!WebUtils.validString(testSession.getLocation())) {
			validationFailedInfo.setKey("SelectSettings.TestLocation.InvalidCharacters");
			validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("SelectSettings.TestLocation.InvalidCharacters.Header"));
			validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("SelectSettings.TestLocation.InvalidCharacters.Body"));
		} else if (hasEmptyTAC(TACs)) {
			if (testSession.getEnforceBreak().equals("T")) {
				validationFailedInfo.setKey("TAC.MissingTestAccessCodes");
	 			validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("TAC.MissingTestAccessCodes.Header"));
	 			validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("TAC.MissingTestAccessCodes.Body1"));
	 			validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("TAC.MissingTestAccessCodes.Body2"));
			} else {
				validationFailedInfo.setKey("TAC.MissingTestAccessCode");
				validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("TAC.MissingTestAccessCode.Header"));
				validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("TAC.MissingTestAccessCode.Body"));
			}
		} else if (hasSpecialCharInTAC(TACs)) {
			validationFailedInfo.setKey( "TAC.SpecialCharNotAllowed");
 			validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage( "TAC.SpecialCharNotAllowed.Header"));
 			validationFailedInfo.updateMessage(MessageResourceBundle.getMessage( "TAC.SpecialCharNotAllowed.Body"));
		} else if (hasInvalidateTACLength(TACs)) {
			validationFailedInfo.setKey("TAC.SixChars");
 			validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("TAC.SixChars"));
		}  

	}
     private void validateScheduledUnits(ScheduledSession scheduledSession,	boolean hasBreakBoolean, ValidationFailedInfo validationFailedInfo, boolean isAddOperation) {
    	try{
    		TestElement[] newTEs = scheduledSession.getScheduledUnits();
    		//boolean hasAL = ((form.getAutoLocator() != null) && form.getAutoLocator().equals("true"));
            //if (hasAL)
             //   TACs = new String[this.defaultSubtests.size() + 1];
           // else
       	 
       	 	String[] TACs = null;
       	 	if(!hasBreakBoolean){
	       		 TACs = new String[1];
	             TACs[0] = scheduledSession.getTestSession().getAccessCode();
       	 	} else {
       		 TACs = new String[newTEs.length];
       		 for(int i=0; i<newTEs.length; i++) {
       			 TACs[i] = newTEs[i].getAccessCode();
       		 }
       	 }
       	 
       	 	if (hasBreakBoolean && hasEmptyTAC(TACs)) {
    			validationFailedInfo.setKey("TAC.MissingTestAccessCodes");
    			validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("TAC.MissingTestAccessCodes.Header"));
    			validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("TAC.MissingTestAccessCodes.Body1"));
    			validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("TAC.MissingTestAccessCodes.Body2"));
    			
    		} else if (hasBreakBoolean && hasSpecialCharInTAC(TACs)) {
    			validationFailedInfo.setKey( "TAC.SpecialCharNotAllowed");
    			validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage( "TAC.SpecialCharNotAllowed.Header"));
    			validationFailedInfo.updateMessage(MessageResourceBundle.getMessage( "TAC.SpecialCharNotAllowed.Body"));
    		} else if (hasBreakBoolean && hasInvalidateTACLength(TACs)) {
    			validationFailedInfo.setKey("TAC.SixChars");
    			validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("TAC.SixChars"));
    		} else if (hasBreakBoolean && hasDuplicateTAC(TACs)) {
    			validationFailedInfo.setKey("TAC.IdenticalTestAccessCodes");
    			validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("TAC.IdenticalTestAccessCodes.Header"));
    			validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("TAC.IdenticalTestAccessCodes.Body1"));
    			validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("TAC.IdenticalTestAccessCodes.Body2"));
    		}else if (isValidTAC(scheduledSession, TACs,validationFailedInfo)){
    			// do nothing validationFailedInfo is populated
    		}
    		
    	}catch (Exception e) {
   		 e.printStackTrace();
		 validationFailedInfo.setKey("SYSTEM_EXCEPTION");
		 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("System.Exception.Header"));
		 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("System.Exception.Body"));
	 }
    	 
    	 
    	
    	 
    	 
 		
 	}
     
     private boolean isValidTAC(ScheduledSession scheduledSession, String[] TACs, ValidationFailedInfo validationFailedInfo) {

         String [] validateResults=null;
         boolean found = false;
         try
         {
             validateResults = this.scheduleTest.validateAccessCodes(this.userName, TACs,scheduledSession.getTestSession().getTestAdminId());
         }
         catch (CTBBusinessException e)
         {
             e.printStackTrace();    
         }
         
         if (validateResults != null)
         {
             Vector<String> tacsInuse = new Vector<String>();
             for (int i=0; i < validateResults.length; i++)
             {
                 if (validateResults[i] != null && validateResults[i].indexOf("exists") >= 0)
                 {
                	 found = true;
                     tacsInuse.add(TACs[i]);
                 }
                 
             }
             if (tacsInuse.size() > 1)
             {
            	 validationFailedInfo.setKey("TAC.InvalidTestAccessCode.Header2");
            	 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.Header2"));
            	 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.InUse2", getTACsInString(tacsInuse)));
             }                    
             else if (tacsInuse.size() == 1)
             {
            	 validationFailedInfo.setKey("TAC.InvalidTestAccessCode.Header");
            	 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.Header"));
            	 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.InUse", getTACsInString(tacsInuse)));
             }
             
             /*if (scheduledSession.getTestSession().getEnforceBreak().equals("T")){
            	 validationFailedInfo.setKey("TAC.InvalidTestAccessCode.Footer.WithBreak");
            	 validationFailedInfo.setMessageHeader("TAC.InvalidTestAccessCode.Footer.WithBreak");
                 //validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.Footer.WithBreak"));
             } else {
            	 validationFailedInfo.setKey("TAC.InvalidTestAccessCode.Footer.NoBreak");
            	 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.Footer.NoBreak"));
            	 //validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.Footer.NoBreak"));
             }*/
            	 
         }
         
         return found;
     
	
	} 
     
	private boolean hasDuplicateTAC(String[] TACs) {
		boolean found = false;
		if (TACs.length <= 1)
			return false;
		for (int i = 0; i < TACs.length && !found; i++) {
			for (int j = i + 1; j < TACs.length && !found; j++) {
				if (TACs[i] != null && TACs[i].equalsIgnoreCase((TACs[j]))) {
					found = true;
					break;
				}

			}
			if (found) {
				break;
			}
		}
		return found;
	}


	private boolean hasInvalidateTACLength(String[] TACs) {
		boolean found = false;
		for (int i = 0; i < TACs.length && !found; i++) {
			if (TACs[i] != null && TACs[i].length() < 6) {
				found = true;
				break;
			}
				
		}
		return found;
	}


	private boolean hasSpecialCharInTAC(String[] TACs) {
		boolean found = false;
		for (int i = 0; i < TACs.length && !found; i++) {
			for (int j = 0; j < TACs[i].length() && !found; j++) {
				char currentChar = TACs[i].charAt(j);
				if (!(currentChar >= 'A' && currentChar <= 'Z'
						|| currentChar >= 'a' && currentChar <= 'z'
						|| currentChar >= '0' && currentChar <= '9' || currentChar == '_')){
					found = true;
				    break;
				}
					
			}
			if(found)
				 break;
		}
		return found;
	}


	private boolean hasEmptyTAC(String[] TACs) {
		boolean found = false;
		for (int i = 0; i < TACs.length && !found; i++) {
			if ("".equals(TACs[i])) {
				found = true;
				break;
			}
				
		}
		return found;
	}


	private void initialize() {
		java.security.Principal principal = getRequest().getUserPrincipal();
		this.userName = principal.toString();

		getSession().setAttribute("userName", this.userName);
		UserNodeData und = null;

		try {
			if(this.user == null || this.topNodesMap == null || (this.topNodesMap!=null && this.topNodesMap.size()==0 ) ) {
				if(this.user ==null)
					this.user =  userManagement.getUser(this.userName, this.userName);
				und = this.scheduleTest.getTopUserNodesForUser(this.userName, null,
						null, null, null);
				UserNode[] nodes = und.getUserNodes();
				for (int i = 0; i < nodes.length; i++) {
					UserNode node = (UserNode) nodes[i];
					if (node != null) {
						this.topNodesMap.put(node.getOrgNodeId(), node
								.getOrgNodeName());
					}

				}
			}
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
	}

	private int getProductIndexByID(String selectedProductId) {
    	 int productIndex = -1;
    	 int counter = 0;
    	 if (selectedProductId == null)
             return -1;
    	 int val = 0;
    	 try {
    		 val = Integer.valueOf(selectedProductId);
    	 } catch (NumberFormatException ne) {
    		 return -1;
    	 }
    	 
         for (TestProduct tp :tps) {
        	 
        	 if(tp.getProductId().intValue() == val) {
        		 productIndex =  counter;
        		 break;
        	 }
        		
        	 counter = counter+1;
         }
    	 
    	 return productIndex;
	}
     
     
    

	private TestProductData getTestProductDataForUser() throws CTBBusinessException
    {
        TestProductData tpd = null;                
        SortParams sortParams = FilterSortPageUtils.buildSortParams("ProductName", ColumnSortEntry.ASCENDING, null, null);            
        tpd = this.scheduleTest.getTestProductsForUser(this.userName,null,null,sortParams);
        return tpd;
    }
    
    private boolean isUserPasswordExpired()
    {
    	boolean pwdExpiredStatus = false;    	
    	Date passwordExpirationDate = this.user.getPasswordExpirationDate();
    	Date CurrentDate = new Date();
    	if (CurrentDate.compareTo(passwordExpirationDate)> 0 ){
    		pwdExpiredStatus = true;
    	}
    	return pwdExpiredStatus;
    } 
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// BEGIN OF NEW NAVIGATION ACTIONS ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
    
    /**
     * ASSESSMENTS actions
     */    
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "sessionsLink", path = "assessments_sessions.do"),
            @Jpf.Forward(name = "studentScoringLink", path = "assessments_studentScoring.do"),
            @Jpf.Forward(name = "programStatusLink", path = "assessments_programStatus.do")
        }) 
    protected Forward assessments()
    {
    	String menuId = (String)this.getRequest().getParameter("menuId");    	
    	String forwardName = (menuId != null) ? menuId : "sessionsLink";
    	
        return new Forward(forwardName);
    }

    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "assessments_sessions.jsp") 
        }) 
    protected Forward assessments_sessions()
    {
        return new Forward("success");
    }
    
    @Jpf.Action(forwards={
    		@Jpf.Forward(name = "success", 
					path ="assessments_sessions.jsp")
	})
    protected Forward getSessionForUserHomeGrid(SessionOperationForm form){

		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		try {
			//System.out.println ("db process time Start:"+new Date());
			
			// get licenses
			CustomerLicense[] customerLicenses =  null;
			if(this.hasLicenseConfig) {
			//Temporary change to hide register student button
			//	customerLicenses = getCustomerLicenses(); 
			}
			/* if ((customerLicenses != null) && (customerLicenses.length > 0))
	        {
	            this.getRequest().setAttribute("customerLicenses", getLicenseQuantitiesByOrg());
	           // this.getSession().setAttribute("hasLicenseConfig", new Boolean(true));
	        }*/
			if(this.userName == null ) {
				getLoggedInUserPrincipal();		
				getUserDetails();
			}
			OrgNodeCategory orgNodeCategory = UserOrgHierarchyUtils.getCustomerLeafNodeDetail(this.userName,this.customerId,this.userManagement );
	     	
	        // retrieve information for user test sessions
			FilterParams sessionFilter = null;
	        PageParams sessionPage = null;
	        SortParams sessionSort = null;
	        sessionSort = FilterSortPageUtils.buildSortParams(FilterSortPageUtils.TESTSESSION_DEFAULT_SORT, FilterSortPageUtils.ASCENDING);
	        TestSessionData tsd = getTestSessionsForUserHome(sessionFilter, sessionPage, sessionSort);
	        //System.out.println ("db process time End:"+new Date());
	        Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
			if ((tsd != null) && (tsd.getFilteredCount().intValue() > 0))
			{
				System.out.println ("List process time Start:"+new Date());
				base = buildTestSessionList(customerLicenses, tsd, base); 
				System.out.println ("List process time End:"+new Date());
			} else {
				this.setSessionListCUFU(new ArrayList<TestSessionVO>());
		        this.setSessionListPA(new ArrayList<TestSessionVO>());
		        base.setTestSessionCUFU(sessionListCUFU);
		        base.setTestSessionPA(sessionListPA);
			}
			base.setOrgNodeCategory(orgNodeCategory);
			
			
			//System.out.println("just b4 gson");	
			Gson gson = new Gson();
			//System.out.println ("Json process time Start:"+new Date());
			
			json = gson.toJson(base);
			//System.out.println ("Json process time End:"+new Date() +".."+json);


			
			try{
				resp.setContentType("application/json");
				stream = resp.getOutputStream();
				resp.flushBuffer();
				stream.write(json.getBytes("UTF-8"));

			}

			finally{
				if (stream!=null){
					stream.close();
				}
			}



		} catch (Exception e) {
			System.err.println("Exception while processing CR response.");
			e.printStackTrace();
		}

		return null;

	}
    
    @Jpf.Action(forwards={
    		@Jpf.Forward(name = "success", 
					path ="assessments_sessions.jsp")
	})
    protected Forward getCompletedSessionForGrid(SessionOperationForm form){
    	System.out.println("completed");
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		try {
			Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
			base.setTestSessionCUFU(this.sessionListCUFU);
			base.setTestSessionPA(this.sessionListPA);
			Gson gson = new Gson();
			System.out.println ("Json process time Start:"+new Date());
			json = gson.toJson(base);
			System.out.println ("Json process time End:"+new Date() +".."+json);
			try{
				resp.setContentType("application/json");
				stream = resp.getOutputStream();
				resp.flushBuffer();
				stream.write(json.getBytes("UTF-8"));
			}
			finally{
				if (stream!=null){
					stream.close();
				}
			}
		} catch (Exception e) {
			System.err.println("Exception while processing CR response.");
			e.printStackTrace();
		}

		return null;

	}
    
    
    @Jpf.Action(forwards={
    		@Jpf.Forward(name = "success", 
					path ="assessments_sessions.jsp")
	})
    protected Forward getStudentForList(SessionOperationForm form){
    	
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		
		String testId = getRequest().getParameter("selectedTestId");
		String treeOrgNodeId = getRequest().getParameter("stuForOrgNodeId");
		String blockOffGrade = getRequest().getParameter("blockOffGradeTesting");
		String selectedLevel = getRequest().getParameter("selectedLevel");
		String testAdminIdString = getRequest().getParameter("testAdminId");
		Integer selectedOrgNodeId = null;
		Integer selectedTestId = null;
		Integer testAdminId = null;
		if(treeOrgNodeId != null)
			selectedOrgNodeId = Integer.parseInt(treeOrgNodeId);
		if(testId != null)
			selectedTestId = Integer.parseInt(testId);
		try{
			if(testAdminIdString != null){
				testAdminId = Integer.valueOf(testAdminIdString.trim());
			}
		} catch (Exception e){	}
		
		try {
			FilterParams studentFilter = null;
			if(blockOffGrade != null && blockOffGrade.equalsIgnoreCase("true")) { //Changes for block off grade testing
				studentFilter = generateFilterParams(selectedLevel);				
			}
	        PageParams studentPage = null;
	        SortParams studentSort = null;
	        //studentSort = FilterSortPageUtils.buildSortParams(FilterSortPageUtils.STUDENT_DEFAULT_SORT, FilterSortPageUtils.ASCENDING);
	        // get students - getSessionStudents
	        SessionStudentData ssd = getSessionStudents(selectedOrgNodeId, testAdminId, selectedTestId, studentFilter, studentPage, studentSort);
	        List<SessionStudent> studentNodes = buildStudentList(ssd.getSessionStudents());
			Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
			base.setStudentNode(studentNodes);
			base.setGradeList(this.studentGradesForCustomer);
			
			Gson gson = new Gson();
			System.out.println ("Json process time Start:"+new Date());
			json = gson.toJson(base);
			System.out.println ("Json process time End:"+new Date() +".."+json);
			try{
				resp.setContentType("application/json");
				stream = resp.getOutputStream();
				resp.flushBuffer();
				stream.write(json.getBytes("UTF-8"));
			}
			finally{
				if (stream!=null){
					stream.close();
				}
			}
		} catch (Exception e) {
			System.err.println("Exception while processing CR response.");
			e.printStackTrace();
		}

		return null;

	}
    	
    @Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="find_user_hierarchy.jsp")
	})
	protected Forward userOrgNodeHierarchyList(SessionOperationForm form){

		String jsonTree = "";
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		//String contentType = CONTENT_TYPE_JSON;
		try {
			BaseTree baseTree = new BaseTree ();

			ArrayList<Organization> completeOrgNodeList = new ArrayList<Organization>();
			UserNodeData associateNode = UserOrgHierarchyUtils.populateAssociateNode(this.userName,this.userManagement);
			ArrayList<Organization> selectedList  = UserOrgHierarchyUtils.buildassoOrgNodehierarchyList(associateNode);
			Collections.sort(selectedList, new OrgnizationComparator());
			ArrayList <Integer> orgIDList = new ArrayList <Integer>();
			ArrayList<TreeData> data = new ArrayList<TreeData>();

			UserNodeData und = UserOrgHierarchyUtils.OrgNodehierarchy(this.userName, 
					this.userManagement, selectedList.get(0).getOrgNodeId()); 
			ArrayList<Organization> orgNodesList = UserOrgHierarchyUtils.buildOrgNodehierarchyList(und, orgIDList,completeOrgNodeList);	

			//jsonTree = generateTree(orgNodesList,selectedList);

			for (int i= 0; i < selectedList.size(); i++) {

				if (i == 0) {

					preTreeProcess (data,orgNodesList,selectedList);

				} else {

					Integer nodeId = selectedList.get (i).getOrgNodeId();
					if (orgIDList.contains(nodeId)) {
						continue;
					} else if (!selectedList.get (i).getIsAssociate()) {
						
						continue;
						
					} else {

						orgIDList = new ArrayList <Integer>();
						UserNodeData undloop = UserOrgHierarchyUtils.OrgNodehierarchy(this.userName, 
								this.userManagement,nodeId);   
						ArrayList<Organization> orgNodesListloop = UserOrgHierarchyUtils.buildOrgNodehierarchyList(undloop, orgIDList, completeOrgNodeList);	
						preTreeProcess (data,orgNodesListloop,selectedList);
					}
				}


			}

			Gson gson = new Gson();
			baseTree.setData(data);
			Collections.sort(baseTree.getData(), new Comparator<TreeData>(){

				public int compare(TreeData t1, TreeData t2) {
					return (t1.getData().toUpperCase().compareTo(t2.getData().toUpperCase()));
				}
					
			});
			jsonTree = gson.toJson(baseTree);
			String pattern = ",\"children\":[]";
			jsonTree = jsonTree.replace(pattern, "");
			//System.out.println(jsonTree);
			try {

				resp.setContentType(CONTENT_TYPE_JSON);
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(jsonTree.getBytes("UTF-8"));
			} finally{
				if (stream!=null){
					stream.close();
				}
			}
		} catch (Exception e) {
			System.err.println("Exception while processing userOrgNodeHierarchyList.");
			e.printStackTrace();
		}

		return null;

	}
    
    
    @Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="find_user_hierarchy.jsp")
	})
	protected Forward userTreeOrgNodeHierarchyList(SessionOperationForm form){

		String jsonTree = "";
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		//String contentType = CONTENT_TYPE_JSON;
		Integer testAdminId = Integer.valueOf(this.getRequest().getParameter("testAdminId"));
		Integer orgNodeId = Integer.valueOf(this.getRequest().getParameter("orgNodeId"));
		
		int studentCount = getRosterForTestSession(testAdminId);
		
		try {
			BaseTree baseTree = new BaseTree ();
			ArrayList<TreeData> data = new ArrayList<TreeData>();
			if(studentCount > 0){
				baseTree.setIsStudentExist("true");		
			
				ArrayList<Organization> completeOrgNodeList = new ArrayList<Organization>();
				ArrayList <Integer> orgIDList = new ArrayList <Integer>();
				
				StudentNodeData snd = this.scheduleTest.getTestTicketNodesForParent(this.userName, orgNodeId, testAdminId, null, null, null);
				ArrayList<Organization> selectedList = new ArrayList<Organization>();

				ArrayList<Organization> orgNodesList = UserOrgHierarchyUtils.buildOrgNodeAncestorHierarchyList(snd, orgIDList,completeOrgNodeList);	
	
				
				preTreeProcess (data,orgNodesList,selectedList);
	
				
			}else{
				baseTree.setIsStudentExist("false");
			}

			Gson gson = new Gson();
			baseTree.setData(data);
			Collections.sort(baseTree.getData(), new Comparator<TreeData>(){

				public int compare(TreeData t1, TreeData t2) {
					return (t1.getData().toUpperCase().compareTo(t2.getData().toUpperCase()));
				}
					
			});
			jsonTree = gson.toJson(baseTree);
			String pattern = ",\"children\":[]";
			jsonTree = jsonTree.replace(pattern, "");
			//System.out.println(jsonTree);
			try {

				resp.setContentType(CONTENT_TYPE_JSON);
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(jsonTree.getBytes("UTF-8"));
			} finally{
				if (stream!=null){
					stream.close();
				}
			}
		} catch (Exception e) {
			System.err.println("Exception while processing userTreeOrgNodeHierarchyList.");
			e.printStackTrace();
		}

		return null;

	}
    
    
    @Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="assessments_sessions.jsp")
	})
    protected Forward getSessionForSelectedOrgNodeGrid(SessionOperationForm form){
    	System.out.println("selected");
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String treeOrgNodeId = getRequest().getParameter("treeOrgNodeId");
		Integer selectedOrgNodeId = null;
		if(treeOrgNodeId != null)
			selectedOrgNodeId = Integer.parseInt(treeOrgNodeId);
		String json = "";
		try {
			System.out.println ("db process time Start:"+new Date());
			CustomerLicense[] customerLicenses =  null;
			if(this.hasLicenseConfig) {
			 	//Temporary change to hide register student button
				// customerLicenses = getCustomerLicenses();  
			}
	    	// retrieve information for user test sessions
	        //  FilterParams sessionFilter = FilterSortPageUtils.buildFilterParams(FilterSortPageUtils.TESTSESSION_DEFAULT_FILTER_COLUMN, "CU");
	    	FilterParams sessionFilter = null;
	        PageParams sessionPage = null;
	        SortParams sessionSort = null;
	        sessionSort = FilterSortPageUtils.buildSortParams(FilterSortPageUtils.TESTSESSION_DEFAULT_SORT, FilterSortPageUtils.ASCENDING);
	        TestSessionData tsd = getTestSessionsForOrgNode(selectedOrgNodeId, sessionFilter, sessionPage, sessionSort, this.user.getUserId());
	        System.out.println ("db process time End:"+new Date());
	        Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
			if ((tsd != null) && (tsd.getFilteredCount().intValue() > 0))
			{
				System.out.println ("List process time Start:"+new Date());
				base = buildTestSessionList(customerLicenses, tsd, base); 
				//String userOrgCategoryName = getTestSessionOrgCategoryName(sessionList);
				System.out.println ("List process time End:"+new Date());
			} else {
				this.setSessionListCUFU(new ArrayList<TestSessionVO>());
		        this.setSessionListPA(new ArrayList<TestSessionVO>());
		        base.setTestSessionCUFU(sessionListCUFU);
		        base.setTestSessionPA(sessionListPA);
			}
			
			
			//System.out.println("just b4 gson");	
			Gson gson = new Gson();
			System.out.println ("Json process time Start:"+new Date());
			
			json = gson.toJson(base);
			//System.out.println ("Json process time End:"+new Date() +".."+json);


			
			try{
				resp.setContentType("application/json");
				stream = resp.getOutputStream();
				resp.flushBuffer();
				stream.write(json.getBytes("UTF-8"));

			}

			finally{
				if (stream!=null){
					stream.close();
				}
			}



		} catch (Exception e) {
			System.err.println("Exception while processing CR response.");
			e.printStackTrace();
		}

		return null;

	}
    
    @Jpf.Action(forwards={
    		@Jpf.Forward(name = "success", path ="student_scoring.jsp")
	})
    protected Forward assessments_studentScoring()
    {
    	/*
        try
        {
            String url = "/ScoringWeb/scoringOperation/assessments_studentScoring.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
        */
        return new Forward("success");
    	
    }

    @Jpf.Action()
    protected Forward assessments_programStatus()
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
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "reports.jsp"), 
            @Jpf.Forward(name = "viewReports", path = "viewReports.do") 
            
        }) 
    protected Forward reports()
    {
    	getLoggedInUserPrincipal();
		
		getUserDetails();
		CustomerConfiguration [] customerConfigs = getCustomerConfigurations(this.customerId);
		setupUserPermission(customerConfigs);
    	    	
        return new Forward("success");
        //return new Forward("viewReports");
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
	            String url = "/SessionWeb/softwareOperation/begin.do";
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
	
    @Jpf.Action()
	protected Forward services_uploadData()
	{
        try
        {
            String url = "/OrganizationWeb/uploadOperation/services_uploadData.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
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
    
    
	@Jpf.Action()
    protected Forward broadcastMessage()
    {
        HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		
		if (this.userName == null) {
			getLoggedInUserPrincipal();
			this.userName = (String)getSession().getAttribute("userName");
		}
		
		List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
        String bcmString = BroadcastUtils.buildBroadcastMessages(broadcastMessages);
		
		try{
    		resp.setContentType(CONTENT_TYPE_JSON);
			try {
				stream = resp.getOutputStream();
	    		resp.flushBuffer();
	    		stream.write(bcmString.getBytes());
			} 
			finally {
				if (stream!=null){
					stream.close();
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
        
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
        //String userTimeZone = "GMT";
             	
        try
        {
            this.user = this.testSessionStatus.getUserDetails(this.userName, this.userName);
            Customer customer = this.user.getCustomer();
            this.customerId = customer.getCustomerId();
            getSession().setAttribute("customerId", customerId);
            String hideAccommodations = customer.getHideAccommodations();
	        if ((hideAccommodations != null) && hideAccommodations.equalsIgnoreCase("T"))
	        {
	            supportAccommodations = Boolean.FALSE;
	        }
	        UserNodeData associateNode = UserOrgHierarchyUtils.populateAssociateNode(this.userName,this.userManagement);
	        ArrayList<Organization> selectedList  = UserOrgHierarchyUtils.buildassoOrgNodehierarchyList(associateNode);
	        getSession().setAttribute("supportAccommodations", supportAccommodations); 
	        getSession().setAttribute("schedulerFirstName", this.user.getFirstName());
	        getSession().setAttribute("schedulerLastName", this.user.getLastName());
	        getSession().setAttribute("schedulerUserId", this.user.getUserId().toString());
	        getSession().setAttribute("schedulerUserName", this.user.getUserName());
	        //System.out.println("supportAccommodations==>"+supportAccommodations);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
    }
    
    /**
     * getCustomerLicenses
     */
    @SuppressWarnings("unused")
	private CustomerLicense[] getCustomerLicenses()
    {
        CustomerLicense[] cls = null;

        try
        {
            cls = this.licensing.getCustomerOrgNodeLicenseData(this.userName, null);
        }    
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
     
        return cls;
    }
    
   
	private void setupUserPermission(CustomerConfiguration [] customerConfigs)
	{
        boolean adminUser = isAdminUser();
        //boolean TABECustomer = isTABECustomer(customerConfigs);
        boolean laslinkCustomer = isLaslinkCustomer(customerConfigs);
        
        this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));

        this.getSession().setAttribute("hasUploadDownloadConfigured", 
        		new Boolean( hasUploadDownloadConfig().booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasProgramStatusConfigured", 
        		new Boolean( hasProgramStatusConfig(customerConfigs).booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasScoringConfigured", 
        		new Boolean( customerHasScoring(customerConfigs).booleanValue() && adminUser));
        
        this.getSession().setAttribute("isBulkAccommodationConfigured",customerHasBulkAccommodation(customerConfigs));
    	
        //this.getSession().setAttribute("canRegisterStudent", canRegisterStudent(customerConfigs));
        this.getSession().setAttribute("canRegisterStudent", false);//Temporary change to hide register student button
        this.hasLicenseConfig = hasLicenseConfiguration(customerConfigs).booleanValue();
     	this.getSession().setAttribute("hasLicenseConfigured", this.hasLicenseConfig && adminUser);
     	
     	this.getSession().setAttribute("adminUser", new Boolean(adminUser));     

		this.getSession().setAttribute("isBulkMoveConfigured",customerHasBulkMove(customerConfigs));
		
     	this.getSession().setAttribute("userScheduleAndFindSessionPermission", userScheduleAndFindSessionPermission());   
     	
     	this.getSession().setAttribute("isDeleteSessionEnable", isDeleteSessionEnable());
     	
     	getConfigStudentLabel(customerConfigs);
     	
     	getStudentGrades(customerConfigs);
     	
   }
		
	
	private void getConfigStudentLabel(CustomerConfiguration[] customerConfigurations) 
	{     
		//boolean isStudentIdConfigurable = false;
		Integer configId=0;
		String []valueForStudentId = new String[8] ;
		valueForStudentId[0] = "Student ID";
		for (int i=0; i < customerConfigurations.length; i++)
		{
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID") && cc.getDefaultValue().equalsIgnoreCase("T"))
			{
				//isStudentIdConfigurable = true; 
				configId = cc.getId();
				CustomerConfigurationValue[] customerConfigurationsValue = customerConfigurationValues(configId);
				//By default there should be 3 entries for customer configurations
				valueForStudentId = new String[8];
				for(int j=0; j<customerConfigurationsValue.length; j++){
					int sortOrder = customerConfigurationsValue[j].getSortOrder();
					valueForStudentId[sortOrder-1] = customerConfigurationsValue[j].getCustomerConfigurationValue();
				}	
				valueForStudentId[0] = valueForStudentId[0]!= null ? valueForStudentId[0] : "Student ID" ;

			}

		}
		this.getSession().setAttribute("studentIdLabelName",valueForStudentId[0]);
		
	}
	
	 /**
     * get value for enabling RegisterStudent button.
     */
    private void registerStudentEnable(CustomerLicense[] customerLicenses, TestSessionVO testSessionVo)
    {    
    	if (customerLicenses == null  || (!this.hasLicenseConfig)) {
    		testSessionVo.setIsRegisterStudentEnable("T");  
    		return;
    	}
    	if (customerLicenses != null && customerLicenses.length<=0 && this.hasLicenseConfig) {
    		 testSessionVo.setIsRegisterStudentEnable("F");   
    		 return;
    	}
    	
             boolean flag = false;
            
            if (testSessionVo.getLicenseEnabled().equals("T"))
            {
            
                for (int j=0; j < customerLicenses.length; j++)
                { 
                            
                    if (customerLicenses[j].getProductId().intValue() == testSessionVo.getProductId().intValue() || customerLicenses[j].getProductId().intValue() == testSessionVo.
                        getParentProductId().intValue())
                    {
                        flag = true;      
                       if(customerLicenses[j].isLicenseAvailable()){
                        	testSessionVo.setIsRegisterStudentEnable("T");
                        } else {
                        	 testSessionVo.setIsRegisterStudentEnable("F");
                        }
                      
                      break;
                  }
                }
            } 
            if (!flag) {
                
                testSessionVo.setIsRegisterStudentEnable("T");   
                
            }
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
    
    @SuppressWarnings("unused")
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
        //Integer customerId = this.user.getCustomer().getCustomerId();
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

    @SuppressWarnings("unused")
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
    
    /*
	 * 
	 * this method retrieve CustomerConfigurationsValue for provided customer configuration Id.
	 */
	private CustomerConfigurationValue[] customerConfigurationValues(Integer configId)
	{	
		CustomerConfigurationValue[] customerConfigurationsValue = null;
		try {
			customerConfigurationsValue = this.testSessionStatus.getCustomerConfigurationsValue(configId);

		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		return customerConfigurationsValue;
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

    private Boolean hasProgramStatusConfig(CustomerConfiguration[] customerConfigurations)
    {	    	
    	Boolean hasProgramStatusConfig = Boolean.FALSE;
    	if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subtest_Invalidation") && 
						cc.getDefaultValue().equals("T")) {
					hasProgramStatusConfig = true; 
					break;
				}
			}
		}
		return new Boolean(hasProgramStatusConfig);   
    }
    
    @SuppressWarnings("unused")
	private TestSessionData getTestSessionsForUser(FilterParams filter, PageParams page, SortParams sort) 
    {
        TestSessionData tsd = new TestSessionData();                
        try
        {      
            tsd = this.testSessionStatus.getTestSessionsForUser(this.userName, filter, page, sort);            
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return tsd;
    }
    
    private TestSessionData getTestSessionsForUserHome(FilterParams filter, PageParams page, SortParams sort) 
    {
        TestSessionData tsd = new TestSessionData();                
        try
        {      
            tsd = this.testSessionStatus.getTestSessionsForUserHome(this.userName, filter, page, sort);            
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return tsd;
    }
    
    private SessionStudentData getSessionStudents(Integer orgNodeId, Integer testAdminId,Integer selectedTestId, FilterParams filter, PageParams page, SortParams sort)
    {    
        SessionStudentData sd = null;
        try {      
            sd = this.scheduleTest.getSessionStudentsMinimalInfoForOrgNode(this.userName, orgNodeId, testAdminId, selectedTestId, filter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return sd;
    }

    private Base buildTestSessionList(CustomerLicense[] customerLicenses, TestSessionData tsd, Base base) 
    {
        List<TestSessionVO> sessionListCUFU = new ArrayList<TestSessionVO>(); 
        List<TestSessionVO> sessionListPA = new ArrayList<TestSessionVO>();        
        TestSession[] testsessions = tsd.getTestSessions();            
        for (int i=0; i < testsessions.length; i++)
        {
            TestSession ts = testsessions[i];
            if (ts != null)
            {	if (ts.getTestAdminStatus().equals("CU") ||ts.getTestAdminStatus().equals("FU") ){
            		TestSessionVO vo = new TestSessionVO(ts);
            		registerStudentEnable(customerLicenses, vo);
            		sessionListCUFU.add(vo);
            	} else {
            		TestSessionVO vo = new TestSessionVO(ts);
            		registerStudentEnable(customerLicenses, vo);
            		sessionListPA.add(vo);
            	}
         
                
            }
        }
        this.setSessionListCUFU(sessionListCUFU);
        this.setSessionListPA(sessionListPA);
        base.setTestSessionCUFU(sessionListCUFU);
        base.setTestSessionPA(sessionListPA);
        return base;
    }
    
    
    @SuppressWarnings("unused")
	private String getTestSessionOrgCategoryName(List<TestSessionVO> testSessionList)
    {
        String categoryName = "Organization";        
        if (testSessionList.size() > 0)
        {
            TestSessionVO vo = (TestSessionVO)testSessionList.get(0);
            categoryName = vo.getCreatorOrgNodeCategoryName();
            for (int i=1; i < testSessionList.size(); i++)
            {
                vo = (TestSessionVO)testSessionList.get(i);
                if (! vo.getCreatorOrgNodeCategoryName().equals(categoryName))
                {
                    categoryName = "Organization";
                    break;
                }
            }
        }
        return categoryName;        
    }
    
    
    private static void preTreeProcess (ArrayList<TreeData> data,ArrayList<Organization> orgList,ArrayList<Organization> selectedList) {

    	Organization org = orgList.get(0);
		Integer rootCategoryLevel = 0;
		TreeData td = new TreeData ();
		td.setData(org.getOrgName());
		td.getAttr().setId(org.getOrgNodeId().toString());
		td.getAttr().setCid(org.getOrgCategoryLevel().toString());
		rootCategoryLevel = org.getOrgCategoryLevel();
		td.getAttr().setTcl("1");
		org.setTreeLevel(1);
		Map<Integer, Organization> orgMap = new HashMap<Integer, Organization>();
		orgMap.put(org.getOrgNodeId(), org);
		treeProcess (org, orgList, td, selectedList, rootCategoryLevel, orgMap);
		data.add(td);
	}
	
    private static void treeProcess (Organization org,List<Organization> list,TreeData td, 
    		ArrayList<Organization> selectedList, Integer rootCategoryLevel, 
    		Map<Integer, Organization> orgMap) {

		Integer treeLevel = 0;
		Organization parentOrg = null;
		for (Organization tempOrg : list) {
			if (org.getOrgNodeId().equals(tempOrg.getOrgParentNodeId())) {
				
				if (selectedList.contains(tempOrg)) {
					
					int index = selectedList.indexOf(tempOrg);
					if (index != -1) {
						
						Organization selectedOrg = selectedList.get(index);
						selectedOrg.setIsAssociate(false);
					}
					
				}
				TreeData tempData = new TreeData ();
				tempData.setData(tempOrg.getOrgName());
				tempData.getAttr().setId(tempOrg.getOrgNodeId().toString());
				tempData.getAttr().setCid(tempOrg.getOrgCategoryLevel().toString());
				parentOrg = orgMap.get(tempOrg.getOrgParentNodeId());
				treeLevel = parentOrg.getTreeLevel() + 1;
				tempOrg.setTreeLevel(treeLevel);
				tempData.getAttr().setTcl(treeLevel.toString());
				td.getChildren().add(tempData);
				orgMap.put(tempOrg.getOrgNodeId(), tempOrg);
				treeProcess (tempOrg, list, tempData, selectedList, rootCategoryLevel, orgMap);
			}
		}
	}
	
	private boolean userScheduleAndFindSessionPermission() 
    {               
        String roleName = this.user.getRole().getRoleName();        
        return (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) ||
                roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR) ||
                roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_COORDINATOR));
    }
	
	 private TestSessionData getTestSessionsForOrgNode(Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort,Integer userId) 
	    {
	        TestSessionData tsd = new TestSessionData();                        
	        try
	        {      
	            tsd = this.testSessionStatus.getTestSessionsForOrgNode(userName, orgNodeId, filter, page, sort, userId);
	        }
	        catch (CTBBusinessException be)
	        {
	            be.printStackTrace();
	        }
	        return tsd;
	    }
	    
	private boolean accessNewUI(CustomerConfiguration [] customerConfigs)
    {            
        boolean accessNewUI = false;
        
        for (int i=0; i < customerConfigs.length; i++)
        {
        	CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("TAS_Revised_UI")) {
            	accessNewUI = true;
            }
        }
        return accessNewUI;
    }

	/**
     * initHintQuestionOptions
     */
    private void initHintQuestionOptions()
    {                 
        try {
            PasswordHintQuestion[] options = 
                    this.userManagement.getHintQuestions();
            
            this.hintQuestionOptions = new LinkedHashMap<String, String>();
            
            this.hintQuestionOptions.put("", "Select a hint question");
            
            if (options != null) {
                for (int i=0 ; i<options.length ; i++) {
                    this.hintQuestionOptions.put(
                            ((PasswordHintQuestion) options[i])
                            .getPasswordHintQuestionId(),
                            ((PasswordHintQuestion)options[i])
                            .getPasswordHintQuestion());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private List<SessionStudent> buildStudentList(SessionStudent [] sessionStudents) 
    {
        List<SessionStudent> studentList = new ArrayList<SessionStudent>();
        for (int i=0 ; i<sessionStudents.length; i++) {
            SessionStudent ss = (SessionStudent)sessionStudents[i];
            if(ss.getStatus()!=null) {
            	ss.setStatusEditable(ss.getStatus().getEditable());
            	 ss.setStatusCopyable(ss.getStatus().getCopyable());
            } else {
            	ss.setStatusEditable("T");
            	ss.setStatusCopyable("T");
            }

           
            
            if (ss != null) {                
                StringBuffer buf = new StringBuffer();
                buf.append(ss.getFirstName()).append(" ").append(ss.getLastName()).append(": ");
                if ("T".equals(ss.getCalculator())) {
                    if ("true".equals(ss.getHasColorFontAccommodations()) ||
                        "T".equals(ss.getScreenReader()) ||
                        "T".equals(ss.getTestPause()) ||
                        "T".equals(ss.getUntimedTest()))
                        buf.append("Calculator, ");
                    else
                        buf.append("Calculator");
                }
                if ("true".equals(ss.getHasColorFontAccommodations())) {
                    if ("T".equals(ss.getScreenReader()) ||
                        "T".equals(ss.getTestPause()) ||
                        "T".equals(ss.getUntimedTest()))
                        buf.append("Color/Font, ");
                    else
                        buf.append("Color/Font");
                }
                if ("T".equals(ss.getScreenReader())) {
                    if ("T".equals(ss.getTestPause()) ||
                        "T".equals(ss.getUntimedTest()))
                        buf.append("ScreenReader, ");
                    else
                        buf.append("ScreenReader");
                }
                if ("T".equals(ss.getTestPause())) {
                    if ("T".equals(ss.getUntimedTest()))
                        buf.append("TestPause, ");
                    else
                        buf.append("TestPause");
                }
                if ("T".equals(ss.getUntimedTest())) {
                    buf.append("UntimedTest");
                }
                buf.append(".");
                ss.setExtPin3(escape(buf.toString()));
                ss.setHasColorFontAccommodations(getHasColorFontAccommodations(ss));
                ss.setHasAccommodations(studentHasAccommodation(ss));
                 if(ss.getMiddleName() != null && !ss.getMiddleName().equals(""))
                	ss.setMiddleName( ss.getMiddleName().substring(0,1));
                 
                 if (ss.getStatus().getCode() == null || ss.getStatus().getCode().equals(""))
                     ss.getStatus().setCode("&nbsp;");
                 if ("Ses".equals(ss.getStatus().getCode()))
                 {
                     StringBuffer buf1 = new StringBuffer();
                     TestSession ts = ss.getStatus().getPriorSession();
                     if (ts != null)
                     {
//                         String timeZone = ts.getTimeZone();
                         TestAdminStatusComputer.adjustSessionTimesToLocalTimeZone(ts);
                         String testAdminName = ts.getTestAdminName();
                         testAdminName = testAdminName.replaceAll("\"", "&quot;");
                         buf1.append("Session Name: ").append(testAdminName);
                         buf1.append("<br/>Start Date: ").append(DateUtils.formatDateToDateString(ts.getLoginStartDate()));
                         buf1.append("<br/>End Date: ").append(DateUtils.formatDateToDateString(ts.getLoginEndDate()));
//                         buf.append("<br/>Start Date: ").append(DateUtils.formatDateToDateString(com.ctb.util.DateUtils.getAdjustedDate(ts.getLoginStartDate(), TimeZone.getDefault().getID(), timeZone, ts.getDailyLoginStartTime())));
//                         buf.append("<br/>End Date: ").append(DateUtils.formatDateToDateString(com.ctb.util.DateUtils.getAdjustedDate(ts.getLoginEndDate(), TimeZone.getDefault().getID(), timeZone, ts.getDailyLoginEndTime())));
                     }
                     ss.setExtPin2(buf1.toString());
                 }
                 
                 
                studentList.add(ss);
                //idToStudentMap.put(ss.getStudentId()+":"+ss.getOrgNodeId(), ss);

            }
        }
        return studentList;
    }
    
    
    public String getHasColorFontAccommodations(SessionStudent ss) {
        String result = "F";
        if( ss.getQuestionBackgroundColor() != null ||
        	ss.getQuestionFontColor() != null ||
        	ss.getQuestionFontSize() != null ||
        	ss.getAnswerBackgroundColor() != null ||
        	ss.getAnswerFontColor() != null ||
        	ss.getAnswerFontSize() != null)
            result = "T";
        return result;
    }
    
    
    public String studentHasAccommodation(SessionStudent  sa){
		 String hasAccommodations = "No";
	        if( "T".equals(sa.getScreenMagnifier()) ||
	            "T".equals(sa.getScreenReader()) ||
	            "T".equals(sa.getCalculator()) ||
	            "T".equals(sa.getTestPause()) ||
	            "T".equals(sa.getUntimedTest()) ||
	            "T".equals(sa.getHighLighter()) ||
	            "T".equals(sa.getExtendedTimeAccom()) ||
	           // (sa.getMaskingRuler() != null && !sa.getMaskingRuler().equals("") && !sa.getMaskingRuler().equals("F"))||
	            (sa.getExtendedTimeAccom() != null && !sa.getExtendedTimeAccom().equals("") && !sa.getExtendedTimeAccom().equals("F")) || 
	           // (sa.getAuditoryCalming() != null && !sa.getAuditoryCalming().equals("") && !sa.getAuditoryCalming().equals("F")) || 
	            //(sa.getMagnifyingGlass() != null && !sa.getMagnifyingGlass().equals("") && !sa.getMagnifyingGlass().equals("F")) || 
	           // (sa.getMaskingTool() != null && !sa.getMaskingTool().equals("") && !sa.getMaskingTool().equals("F")) || 
	            sa.getQuestionBackgroundColor() != null ||
	            sa.getQuestionFontColor() != null ||
	            sa.getQuestionFontSize() != null ||
	            sa.getAnswerBackgroundColor() != null ||
	            sa.getAnswerFontColor() != null ||
	            sa.getAnswerFontSize() != null)
	        	hasAccommodations = "Yes";
	   return hasAccommodations;
	}
    
    
    
    private String escape(String str)
    {
        int len = str.length ();

        StringBuffer safe = new StringBuffer (len);

        for (int i = 0; i < len; i++)
        {
            char cur = str.charAt (i);
            if (cur == '\'')
            {
             safe.append ('\\');
             safe.append (cur);
            }
            else
                safe.append (cur);
        }
        return new String (safe);
    }
	
    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// END OF SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
    /**
	 * FormData get and set methods may be overwritten by the Form Bean editor.
	 */
	public static class SessionOperationForm extends SanitizedFormData
	{

	}
	/**
	 * @return the sessionListCUFU
	 */
	public List<TestSessionVO> getSessionListCUFU() {
		return sessionListCUFU;
	}

	/**
	 * @param sessionListCUFU the sessionListCUFU to set
	 */
	public void setSessionListCUFU(List<TestSessionVO> sessionListCUFU) {
		this.sessionListCUFU = sessionListCUFU;
	}

	/**
	 * @return the sessionListPA
	 */
	public List<TestSessionVO> getSessionListPA() {
		return sessionListPA;
	}

	/**
	 * @param sessionListPA the sessionListPA to set
	 */
	public void setSessionListPA(List<TestSessionVO> sessionListPA) {
		this.sessionListPA = sessionListPA;
	}

	public LinkedHashMap<String, String> getHintQuestionOptions() {
		return hintQuestionOptions;
	}

	public void setHintQuestionOptions(LinkedHashMap<String, String> hintQuestionOptions) {
		this.hintQuestionOptions = hintQuestionOptions;
	}

	public UserProfileInformation getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfileInformation userProfile) {
		this.userProfile = userProfile;
	}	
	
	// Added for Proctor : Start
	
	@Jpf.Action(forwards={
    		@Jpf.Forward(name = "success", 
					path ="assessments_sessions.jsp")
	})
    protected Forward getProctorList(SessionOperationForm form){
    	
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		//final String PROCTOR_DEFAULT_SORT = "LastName";
		
		
		String json = "";
		
		//String testId = getRequest().getParameter("selectedTestId");
		String proctorOrgNodeId = getRequest().getParameter("proctorOrgNodeId");
		Integer selectedOrgNodeId = null;
		if(proctorOrgNodeId != null)
			selectedOrgNodeId = Integer.parseInt(proctorOrgNodeId);
		//if(testId != null)
			//selectedTestId = Integer.parseInt(testId);
		try {
			FilterParams proctorFilter = null;
	        PageParams proctorPage = null;
	        SortParams proctorSort = null;
	        // proctorSort = FilterSortPageUtils.buildSortParams(PROCTOR_DEFAULT_SORT, FilterSortPageUtils.ASCENDING);
	        List<UserProfileInformation> proctorNodes = null;

	        // Get the list of proctors
	        UserData ud = getProctors(selectedOrgNodeId, proctorFilter, proctorPage, proctorSort);
	        if( ud != null) {
	        	proctorNodes = buildProctorList(ud.getUsers());
	        }
			Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
			base.setUserProfileInformation(proctorNodes);
			
			Gson gson = new Gson();
			System.out.println ("Json process time Start:"+new Date());
			json = gson.toJson(base);
			System.out.println ("Json process time End:"+new Date() +".."+json);
			try{
				resp.setContentType("application/json");
				stream = resp.getOutputStream();
				resp.flushBuffer();
				stream.write(json.getBytes("UTF-8"));
			}
			finally{
				if (stream!=null){
					stream.close();
				}
			}
		} catch (Exception e) {
			System.err.println("Exception while processing CR response.");
			e.printStackTrace();
		}

		return null;

	}
	
	
	private UserData getProctors(Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) {    
        UserData ud = null;
        try {      
            ud = this.scheduleTest.getUsersMinimalInfoForOrgNode(this.userName, orgNodeId, filter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return ud;
    }

	
	public List<UserProfileInformation> buildProctorList(User[] users) {
        ArrayList<UserProfileInformation> userList = new ArrayList<UserProfileInformation>();
        if (users != null) {
            //User[] users = uData.getUsers();
            if(users != null){
                for (int i=0 ; i<users.length ; i++) {
                    User user = users[i];
                    if (user != null && user.getUserName() != null) {
                        UserProfileInformation userDetail = new UserProfileInformation(user);
                        if(this.user.getUserId().equals(user.getUserId())){
                        	userDetail.setDefaultScheduler("T");
                        	
                        }else {
                        	userDetail.setDefaultScheduler("F");
                        }
                        userList.add(userDetail);
                    }
                }
            }
        }
        return userList;
    }
    
    // Added for Proctor : End
	 private String getTACsInString(Vector<String> vec) 
	    {
	        Iterator<String> it = vec.iterator();
	        StringBuffer buf = new StringBuffer();
	        while (it.hasNext()) {
	            buf.append((String)it.next());
	            if (it.hasNext())
	                buf.append(", ");
	        }
	        return buf.toString();
	    }
	 
	 private String getMessageResourceBundle(CTBBusinessException e, String msgId) 
	    {
	        String errorMessage = "";
            errorMessage = MessageResourceBundle.getMessage(msgId, e.getMessage());
	        return errorMessage; 
	    }
	 
	 //Added for block off grade
	 private FilterParams generateFilterParams(String selectedLevel) {
		 
		 FilterParams studentFilter = new FilterParams();		 
		String [] arg = new String[1];
		arg[0] = selectedLevel;
		studentFilter = new FilterParams();
		ArrayList<FilterParam> filters = new ArrayList<FilterParam>();
		if(selectedLevel.contains("-")) {
			String [] grades = selectedLevel.split("-");
			int initVal = Integer.parseInt(grades[0]);
			int finalVal = Integer.parseInt(grades[1]);
			for(int i = initVal; i <= finalVal; i++) {
				String [] args = new String[1];
				args[0] = String.valueOf(i);
				filters.add(new FilterParam("StudentGrade", args, FilterType.EQUALS));
			}
		} else if(selectedLevel.contains("/")) {
			String [] grades = selectedLevel.split("/");
			String [] args = new String[1];
			args[0] = grades[0];
			filters.add(new FilterParam("StudentGrade", args, FilterType.EQUALS));
			args[0] = grades[1];
			filters.add(new FilterParam("StudentGrade", args, FilterType.EQUALS));
		} else {
			filters.add(new FilterParam("StudentGrade", arg, FilterType.EQUALS));
		}
		studentFilter.setFilterParams((FilterParam[])filters.toArray(new FilterParam[0]));
		 
		 return studentFilter;
		 
	 }
	 
	 private void getStudentGrades(CustomerConfiguration[] customerConfigurations) 
		{     
		 this.studentGradesForCustomer = new ArrayList<String>();
			Integer configId=0;
			for (int i=0; i < customerConfigurations.length; i++)
			{
				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Grade") && cc.getDefaultValue().equalsIgnoreCase("T"))
				{
					configId = cc.getId();
					CustomerConfigurationValue[] customerConfigurationsValue = customerConfigurationValues(configId);
					for(int j=0; j<customerConfigurationsValue.length; j++){
						this.studentGradesForCustomer.add(customerConfigurationsValue[j].getCustomerConfigurationValue());
					}	

				}

			}			
		}
	 
	 private int getRosterForTestSession(Integer testAdminId){
		 int studentCount = 0;
		 //String errorMessage = "";
		 try
	        {
	            
	            RosterElementData red = this.testSessionStatus.getRosterForTestSession(this.userName,
	                            testAdminId, null, null, null);
	            studentCount = red.getTotalCount().intValue(); 
	                 
	        } 
	        //START- Changed for deferred defect 64446 
	        catch (TransactionTimeoutException e)
	        {
	            e.printStackTrace();
	            String errorMessage =MessageResourceBundle.getMessage("SelectSettings.FailedToSaveTestSessionTransactionTimeOut"); 
	            //System.out.println("errorMessage in TransactionTimeoutException");
	            this.getRequest().setAttribute("errorMessage", errorMessage); 
	            return 0;            
	        } 
	        //END- Changed for deferred defect 64446
	        catch (CTBBusinessException e)
	        {
	            e.printStackTrace();
	            String errorMessage = getMessageResourceBundle(e, "SelectSettings.FailedToSaveTestSession"); 
	            this.getRequest().setAttribute("errorMessage", errorMessage); 
	            return 0;            
	        }  
	        return studentCount;
	 }
	 
	//Added for view/monitor test status: Start
	 
	 private void initializeTestSession () {
			
			retrieveInfoFromSession();                        
	        this.testRosterFilter = new TestRosterFilter();            
	        getCustomerConfigurations();  
	        this.sessionDetailsShowScores = isSessionDetailsShowScores();
	        this.subtestValidationAllowed = isSubtestValidationAllowed();
	        this.studentStatusSubtests = new ArrayList();
	        this.showStudentReportButton = showStudentReportButton();
	        this.selectedRosterIds = new ArrayList();
	        genFile = getRequest().getParameter("genFile");
	        if ("generate_report_file".equals(genFile)) {   
	        	initGenerateReportFile();
	        }
	    }
			
	    
	    /**
	     * @jpf:action
	     * @jpf:forward name="done" path="from_view_subtests_detail.do"
	     * @jpf:forward name="success" path="validate_subtests_detail.jsp"
	     */ 
	    @Jpf.Action(forwards = { 
	         
	        @Jpf.Forward(name = "success",
	                     path = "validate_subtests_detail.jsp")
	    })
	    protected Forward to_validate_subtests_detail()
	    {
	        //String forwardName = handleValidateAction(form);
	    	Integer testRosterID = Integer.valueOf(getRequest().getParameter("testRosterID"));
	    	String[] selectedItemSetIds = getRequest().getParameterValues("selectedItemSetIds");
	        prepareSubtestsDetailInformation(testRosterID, selectedItemSetIds, true);
	        
	        return new Forward("success");
	    }
	    
	    
	    /**
	     * @jpf:action
	     * @jpf:forward name="report" path="/homepage/turnleaf_reports.jsp"
	     * @jpf:forward name="error" path="/error.jsp"
	     */
		@Jpf.Action(
			forwards = { 
				@Jpf.Forward(name = "report", path = "/homepage/turnleaf_reports.jsp"), 
				@Jpf.Forward(name = "error", path = "/error.jsp")
			}
		)
	    protected Forward viewIndividualReport()
	    {
			
	        try {
	        	// Defect 60476 
	        	if (this.userName == null) {
	        		
	        		 java.security.Principal principal = getRequest().getUserPrincipal();
	        	        if (principal != null) 
	        	            this.userName = principal.toString();  
	        	}
	        	Integer testRosterID = Integer.valueOf(getRequest().getParameter("testRosterID"));
	            String reportUrl = this.testSessionStatus.getIndividualReportUrl(this.userName, testRosterID);           
	            this.getRequest().setAttribute("reportUrl", reportUrl);
	            this.getRequest().setAttribute("testAdminId", String.valueOf(this.sessionId));
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new Forward("error");
	        }
	                    
	        return new Forward("report");
	    }
	    
	    
	    
	    private void prepareValidateButtons(String[] itemSetIds)
	    {            
	        if ((itemSetIds != null) && (itemSetIds.length > 0) && (itemSetIds[0] != null))
	            this.getRequest().setAttribute("disableToogleButton", "false");
	        else 
	            this.getRequest().setAttribute("disableToogleButton", "true");
	    }
	    
	    private String getTestLevel(List subtestList)
	    {
	        String level = null;
	        for (int i=0; i < subtestList.size(); i++)
	        {
	            SubtestDetail sd = (SubtestDetail)subtestList.get(i);
	            if ((sd.getLevel() != null) && (sd.getLevel() != ""))
	            {
	                return sd.getLevel();    
	            }
	        }
	        return level;
	    }
	    
	    private String getTestGrade(List subtestList)
	    {
	        String grade = null;
	        for (int i=0; i < subtestList.size(); i++)
	        {
	            SubtestDetail sd = (SubtestDetail)subtestList.get(i);
	            if ((sd.getGrade() != null) && (sd.getGrade() != ""))
	            {
	                return sd.getGrade();    
	            }
	        }
	        return grade;
	    }
	    
	    private void addTABESubtest(TestElement te) 
	    {
	        if (this.TABETestElements == null)
	            this.TABETestElements = new ArrayList();
	            
	        boolean found = false;
	        for (int i=0 ; i<this.TABETestElements.size() ; i++) {
	            TestElement tte = (TestElement)this.TABETestElements.get(i);
	            if (tte.getItemSetId().intValue() == te.getItemSetId().intValue()) {
	                found = true;
	                break;
	            }
	        }
	        if (! found) {
	            this.TABETestElements.add(te);
	        }
	    }
	    
	    private TestElement[] getTestElementsForParent(Integer parentItemSetId, String itemSetType) 
	    {
	        TestElement[] tes = null;
	        try
	        {      
	            TestElementData ted = this.testSessionStatus.getTestElementsForParent(this.userName, parentItemSetId, itemSetType, null, null, null);
	            tes = ted.getTestElements();            
	        }
	        catch (CTBBusinessException be)
	        {
	            be.printStackTrace();
	        }
	        return tes;
	    }
	    
	    private TestElement []  orderedSubtestList(TestElement[] subtestelements,Integer studentId,Integer testAdminId)
	    {
	        TestElement [] orderedSubtestElements = new TestElement[subtestelements.length];
	        StudentManifest [] sms = getStudentManifests(studentId,testAdminId);
	        HashMap smHM = new HashMap();
	        for(int i=0;i<sms.length;i++){
	           StudentManifest  sm = sms[i];
	           smHM.put(sm.getItemSetId(),new Integer(i));
	        }  
	        for(int j=0;j<subtestelements.length;j++){
	            TestElement te = subtestelements[j];
	            if(smHM.containsKey(te.getItemSetId())){
	                orderedSubtestElements[((Integer)smHM.get(te.getItemSetId())).intValue()] = te;
	            }
	        }
	        return orderedSubtestElements;
	    }
	    
	    private StudentManifest [] getStudentManifests(Integer studentId,Integer testAdminId)
	    {
	        StudentManifest [] sm = null;
	        try {  
	                StudentManifestData  smd =  this.scheduleTest.getManifestForRoster(this.userName,studentId,testAdminId,null,null,null);
	                sm = smd.getStudentManifests();
	        }catch (CTBBusinessException be) {
	            be.printStackTrace();
	        }   
	        return sm;
	    }
	    
	    private StudentSessionStatus[] getStudentItemSetStatusesForRoster(Integer studentId, Integer testAdminId) 
	    {
	        StudentSessionStatus[] ssss = null;
	        try
	        {
	            SortParams sort = FilterSortPageUtils.buildSortParams("ItemSetOrder", ColumnSortEntry.ASCENDING);
	            StudentSessionStatusData sssData = testSessionStatus.getStudentItemSetStatusesForRoster(this.userName, studentId, testAdminId, null, null, sort);
	            ssss = sssData.getStudentSessionStatuses();            
	        }
	        catch (CTBBusinessException be)
	        {
	            be.printStackTrace();
	        }
	        return ssss;
	    }
	    
	    private boolean isTestSessionCompleted(TestSessionVO testSession)
	    {
	        boolean completed = false;
	        if (testSession.getTestAdminStatus().equalsIgnoreCase("PA")) {
	            completed = true;
	        }
	        return completed;
	    }
	    
	    private void isLasLinkCustomer()
	    {               
	        
	        boolean isLasLinkCustomer = false;

	            
				 for (int i=0; i < this.customerConfigurations.length; i++)
	            {
	            	 CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
	            	//isLasLink customer
	                if (cc.getCustomerConfigurationName().equalsIgnoreCase("LASLINK_Customer") && cc.getDefaultValue().equals("T")	)
	                {
	                	isLasLinkCustomer = true;
	                    break;
	                } 
	            }
	       
	        this.isLasLinkCustomer = isLasLinkCustomer;
	       
	    }
	    
	    private boolean isTabeLocatorSession(String productType)
	    {
	        if (productType.equalsIgnoreCase("TL"))
	            return true;   
	        else             
	            return false;
	    }
	    
	    private boolean isTabeSession(String productType)
	    {
	        if (productType.equalsIgnoreCase("TB") || productType.equalsIgnoreCase("TL"))
	            return true;   
	        else             
	            return false;
	    }
	    
	    private TestProduct getProductForTestAdmin(Integer testAdminId)
	    {
	        TestProduct tp = null;
	        try {      
	            tp = this.testSessionStatus.getProductForTestAdmin(this.userName, testAdminId);
	        }
	        catch (CTBBusinessException be) {
	            be.printStackTrace();
	        }   
	        return tp;
	    }
	    
	    private RosterElement getTestRosterDetails(Integer testRosterId) 
	    {
	        RosterElement re = null;      
	        try
	        {
	            re = this.testSessionStatus.getRoster(testRosterId);
	        }
	        catch (CTBBusinessException be)
	        {
	            be.printStackTrace();
	        }    
	        return re;
	    }  
		
		 private TestSessionVO getTestSessionDetails(Integer sessionId) 
		    {
		        TestSessionVO testSession = null;
		        try
		        {      
		            TestSessionData tsd = this.testSessionStatus.getTestSessionDetails(this.userName, sessionId);
		            TestSession[] testsessions = tsd.getTestSessions();            
		            TestSession ts = testsessions[0];
		            testSession = new TestSessionVO(ts);            
		        }
		        catch (CTBBusinessException be)
		        {
		            be.printStackTrace();
		        }
		        return testSession;
		    }

			
			private boolean isSameAccessCode(List subtestList)
		    {
		        if (subtestList.size() <= 1)
		            return true;
		            
		        boolean sameAccessCode = true;
		        SubtestDetail sd = (SubtestDetail)subtestList.get(0);
		        String accessCode = sd.getAccessCode();
		        for (int i=1; i < subtestList.size(); i++)
		        {
		            sd = (SubtestDetail)subtestList.get(i);
		            if (! sd.getAccessCode().equals(accessCode))
		                sameAccessCode = false;    
		        }
		        return sameAccessCode;
		    }
	    
		 private TestElementData getTestElementsForTestSession(Integer sessionId) 
		    {
		        TestElementData ted = null;
		        try
		        {      
		            ted = this.testSessionStatus.getTestElementsForTestSession(this.userName, sessionId, null, null, null);
		        }
		        catch (CTBBusinessException be)
		        {
		            be.printStackTrace();
		        }
		        return ted;
		    }
		
		private RosterElementData getRosterForViewTestSession(Integer sessionId) 
	    {
	        if (this.testRosterFilter == null)
	            this.testRosterFilter = new TestRosterFilter();            

	       // FilterParams filter = FilterSortPageUtils.buildTestRosterFilterParams(this.testRosterFilter);
	        FilterParams filter = null;
	        PageParams page = null;
	        SortParams sort = FilterSortPageUtils.buildSortParams(FilterSortPageUtils.TESTROSTER_DEFAULT_SORT, FilterSortPageUtils.ASCENDING);
	        
	        RosterElementData red = null;
	        try
	        {      
	            red = this.testSessionStatus.getRosterForTestSession(this.userName, sessionId, filter, page, sort);
	        }
	        catch (CTBBusinessException be)
	        {
	            be.printStackTrace();
	        }        
	        return red;
	    }
		
		private List buildSubtestList(TestElementData ted)
	    {
	        List subtestList = new ArrayList();        
	        TestElement[] subtestelements = ted.getTestElements();  
	        int sequence = 1;
	        for (int i=0; i < subtestelements.length; i++)
	        {
	            TestElement te = subtestelements[i];
	            if (te != null && "T".equals(te.getSessionDefault()))
	            {
	                SubtestDetail sd = new SubtestDetail(te, sequence);
	                subtestList.add(sd);
	                sequence++;
	            }
	        }        
	                
	        return subtestList;
	    }
		
		 private List buildRosterList(RosterElementData red)
		    {
		        List rosterList = new ArrayList();    
		        if (red != null){
			        RosterElement[] rosterElements = red.getRosterElements();
			        for (int i=0; i < rosterElements.length; i++)
			        {
			            RosterElement rosterElt = rosterElements[i];
			            if (rosterElt != null)
			            {
			                TestRosterVO vo = new TestRosterVO(rosterElt);   
			                rosterList.add(vo);
			            }
			        }   
		        }
		        return rosterList;
		    }
		 private void createGson(Base  base){
			 	OutputStream stream = null;
				HttpServletRequest req = getRequest();
				HttpServletResponse resp = getResponse();
				try {
					try {
						Gson gson = new Gson();
						String json = gson.toJson(base);
						System.out.println("*********************************************************************");
						System.out.println(json);
						resp.setContentType("application/json");
						resp.flushBuffer();
						stream = resp.getOutputStream();
						stream.write(json.getBytes());

					} finally{
						if (stream!=null){
							stream.close();
						}
					}
					
				}
				catch (Exception e) {
					System.err.println("Exception while retrieving optionList.");
					e.printStackTrace();
				}
			}
		 
		public List getStudentStatusSubtests() {
			return studentStatusSubtests;
		}

		public void setStudentStatusSubtests(List studentStatusSubtests) {
			this.studentStatusSubtests = studentStatusSubtests;
		}

		public boolean isShowStudentReportButton() {
			return showStudentReportButton;
		}

		public void setShowStudentReportButton(boolean showStudentReportButton) {
			this.showStudentReportButton = showStudentReportButton;
		}

		public String getGenFile() {
			return genFile;
		}

		public void setGenFile(String genFile) {
			this.genFile = genFile;
		}

		public TestRosterFilter getTestRosterFilter() {
			return testRosterFilter;
		}

		public void setTestRosterFilter(TestRosterFilter testRosterFilter) {
			this.testRosterFilter = testRosterFilter;
		}

		public ArrayList getSelectedRosterIds() {
			return selectedRosterIds;
		}

		public void setSelectedRosterIds(ArrayList selectedRosterIds) {
			this.selectedRosterIds = selectedRosterIds;
		}

		public CustomerConfigurationValue[] getCustomerConfigurationsValue() {
			return customerConfigurationsValue;
		}

		public void setCustomerConfigurationsValue(
				CustomerConfigurationValue[] customerConfigurationsValue) {
			this.customerConfigurationsValue = customerConfigurationsValue;
		}

		/*public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}*/

		public Integer getSessionId() {
			return sessionId;
		}

		public void setSessionId(Integer sessionId) {
			this.sessionId = sessionId;
		}

		public String[] getTestStatusOptions() {
			return testStatusOptions;
		}

		public void setTestStatusOptions(String[] testStatusOptions) {
			this.testStatusOptions = testStatusOptions;
		}

		public String getSetCustomerFlagToogleButton() {
			return setCustomerFlagToogleButton;
		}

		public void setSetCustomerFlagToogleButton(String setCustomerFlagToogleButton) {
			this.setCustomerFlagToogleButton = setCustomerFlagToogleButton;
		}

		public String[] getValidationStatusOptions() {
			return validationStatusOptions;
		}

		public void setValidationStatusOptions(String[] validationStatusOptions) {
			this.validationStatusOptions = validationStatusOptions;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFileType() {
			return fileType;
		}

		public void setFileType(String fileType) {
			this.fileType = fileType;
		}

		public String getUserEmail() {
			return userEmail;
		}

		public void setUserEmail(String userEmail) {
			this.userEmail = userEmail;
		}

		public List getFileTypeOptions() {
			return fileTypeOptions;
		}

		public void setFileTypeOptions(List fileTypeOptions) {
			this.fileTypeOptions = fileTypeOptions;
		}

		public void setSessionDetailsShowScores(boolean sessionDetailsShowScores) {
			this.sessionDetailsShowScores = sessionDetailsShowScores;
		}

		public void setSubtestValidationAllowed(boolean subtestValidationAllowed) {
			this.subtestValidationAllowed = subtestValidationAllowed;
		}

		public void setCustomerConfigurations(
				CustomerConfiguration[] customerConfigurations) {
			this.customerConfigurations = customerConfigurations;
		}

		/**
		 * New method added for CR - GA2011CR001
		 * getCustomerConfigurations
		 */
		private void getCustomerConfigurations()
		{
			try {
					User user = this.testSessionStatus.getUserDetails(this.userName, this.userName);
					Customer customer = user.getCustomer();
					Integer customerId = customer.getCustomerId();
					this.customerConfigurations = this.testSessionStatus.getCustomerConfigurations(this.userName, customerId);
			}
			catch (CTBBusinessException be) {
				be.printStackTrace();
			}
		}
		
		private boolean retrieveInfoFromSession()
	    {
	        boolean success = true;
	        this.userName = (String)getSession().getAttribute("userName");
	        if (this.userName == null)
	        {
	            success = false;
	        }
	        
	                    
	        if (getSession().getAttribute("sessionId") != null)
	            this.sessionId = new Integer((String)getSession().getAttribute("sessionId")); 
	        else if (getRequest().getParameter("sessionId") != null)
	            this.sessionId = new Integer((String)getRequest().getParameter("sessionId")); 
	        else
	            success = false;

	        String sessionFilterTab = "CU";            
	        if (getSession().getAttribute("sessionFilterTab") != null)
	            sessionFilterTab = (String)getSession().getAttribute("sessionFilterTab"); 
	        setTestStatusOptions(sessionFilterTab); 
	                
	        return success;
	    }
		
		private void setTestStatusOptions(String sessionFilterTab)
	    {
	        if (sessionFilterTab.equals("CU")) {    // current
	            testStatusOptions = new String[7];
	            testStatusOptions[0] = FilterSortPageUtils.FILTERTYPE_SHOWALL;
	            testStatusOptions[1] = FilterSortPageUtils.FILTERTYPE_COMPLETED;
	            testStatusOptions[2] = FilterSortPageUtils.FILTERTYPE_INPROGRESS; 
	            testStatusOptions[3] = FilterSortPageUtils.FILTERTYPE_SCHEDULED; 
	            testStatusOptions[4] = FilterSortPageUtils.FILTERTYPE_STUDENTPAUSE;
	            testStatusOptions[5] = FilterSortPageUtils.FILTERTYPE_STUDENTSTOP; 
	            testStatusOptions[6] = FilterSortPageUtils.FILTERTYPE_SYSTEMSTOP; 
	        }
	        else 
	        if (sessionFilterTab.equals("FU")) {    // future
	            testStatusOptions = new String[2];
	            testStatusOptions[0] = FilterSortPageUtils.FILTERTYPE_SHOWALL;
	            testStatusOptions[1] = FilterSortPageUtils.FILTERTYPE_SCHEDULED; 
	        }
	        else {                                  // completed
	            testStatusOptions = new String[6];
	            testStatusOptions[0] = FilterSortPageUtils.FILTERTYPE_SHOWALL;
	            testStatusOptions[1] = FilterSortPageUtils.FILTERTYPE_COMPLETED;
	            testStatusOptions[2] = FilterSortPageUtils.FILTERTYPE_INPROGRESS; 
	            testStatusOptions[3] = FilterSortPageUtils.FILTERTYPE_INCOMPLETE; 
	            testStatusOptions[4] = FilterSortPageUtils.FILTERTYPE_NOTTAKEN; 
	            testStatusOptions[5] = FilterSortPageUtils.FILTERTYPE_STUDENTPAUSE;
	        }
	    }
		
		private boolean isSessionDetailsShowScores() 
	    {               
	        boolean showScores = false; 

	       	for (int i=0; i < this.customerConfigurations.length; i++)
	            {
	                CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
	                if (cc.getCustomerConfigurationName().equalsIgnoreCase("Session_Details_Show_Scores") && cc.getDefaultValue().equalsIgnoreCase("T"))
	                {
	                    showScores = true; 
	                }
	                if (cc.getCustomerConfigurationName().equalsIgnoreCase("Roster_Status_Flag"))
	                {
	                    this.setCustomerFlagToogleButton = "true";
	                     
	                }
	            }
	            this.getRequest().setAttribute("setCustomerFlagToogleButton", setCustomerFlagToogleButton);  
	                
	      
	        return showScores;
	    }
		
		 private boolean isSubtestValidationAllowed()
		    {
		        boolean isValidationAllowed = false; 
		        try
		        {    
		            isValidationAllowed = this.testSessionStatus.allowSubtestInvalidation(this.userName).booleanValue();
		        }
		        catch (CTBBusinessException be)
		        {
		            be.printStackTrace();
		        }   

		        if (isValidationAllowed)
		        {
		            List options = new ArrayList();
		            options.add(FilterSortPageUtils.FILTERTYPE_SHOWALL);
		            options.add(FilterSortPageUtils.FILTERTYPE_INVALID);
		            options.add(FilterSortPageUtils.FILTERTYPE_PARTIALLY_INVALID);
		            options.add(FilterSortPageUtils.FILTERTYPE_VALID);
		            this.validationStatusOptions = (String[])options.toArray(new String[0]);
		        }

		        return isValidationAllowed;
		    }
		 private boolean showStudentReportButton() 
		    {               
		        boolean showButton = false; 

		        for (int i=0; i < this.customerConfigurations.length; i++)
		            {
		                CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
		                if (cc.getCustomerConfigurationName().equalsIgnoreCase("Session_Status_Student_Reports") && cc.getDefaultValue().equalsIgnoreCase("T"))
		                {
		                    showButton = true; 
		                }
		            }     
		      
		        return showButton;
		    }
		 private boolean isDonotScoreAllowed()  {               
	        for (int i=0; i < this.customerConfigurations.length; i++)  {
                CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
                if (cc.getCustomerConfigurationName().equalsIgnoreCase("Do_Not_Score") && cc.getDefaultValue().equalsIgnoreCase("T")) {
                    return true;
                }
            }     
	        return false;
	     }
		 private void initGenerateReportFile() {
		    	
				try {
					User user = this.testSessionStatus.getUserDetails(this.userName, this.userName);
					Date currentDate = new Date();
					String strDate = DateUtils.formatDateToDateString(currentDate);
					strDate = strDate.replace('/', '_');
					String fileName = user.getUserName() + "_" + strDate + ".zip";
			    	setFileName(fileName);
			    	setUserEmail(user.getEmail());
				} catch (CTBBusinessException e) {
					e.printStackTrace();
				}
		    	
		        this.fileTypeOptions = new ArrayList();
		        this.fileTypeOptions.add("One file for all students");
		        this.fileTypeOptions.add("One file per student");
		    	setFileType((String)this.fileTypeOptions.get(0));
		 }
		 @Jpf.Action(forwards = { 
		        @Jpf.Forward(name = "success",
		                     path = "view_test_session.jsp")
		    })
		    protected Forward getRosterDetails()
		    {	
				List rosterList = null;
		        String testAdminId = getRequest().getParameter("testAdminId");
		        initializeTestSession();
		        
		        if (testAdminId != null)
		            this.sessionId = Integer.valueOf(testAdminId);
		        RosterElementData red = getRosterForViewTestSession(this.sessionId);
		        rosterList = buildRosterList(red);   
		       
		        
		        Base base = new Base();
				base.setPage("1");
				base.setRecords("10");
				base.setTotal("2");
				List <Row> rows = new ArrayList<Row>();
				base.setRosterElement(rosterList);
				base.setSubtestValidationAllowed(this.subtestValidationAllowed);
				base.setDonotScoreAllowed(isDonotScoreAllowed());
				/*Integer breakCount = ted.getBreakCount();
		        if ((breakCount != null) && (breakCount.intValue() > 0)) {
		            if (isSameAccessCode(subtestList)) 
		            	base.setHasBreak("singleAccesscode");
		            else
		            	base.setHasBreak("multiAccesscodes");
		        } else {
		        	base.setHasBreak("false");
		        }
*/
		        TestSessionVO testSession = getTestSessionDetails(this.sessionId);
		        base.setTestSession(testSession);
		        createGson(base);
		        return null;
		    }
			
			/**
		     * @jpf:action
		     * @jpf:forward name="success" path="view_subtests_detail.jsp"
		     */ 
		    @Jpf.Action(forwards = { 
		        @Jpf.Forward(name = "success",
		                     path = "view_subtests_detail.jsp")
		    })
		    protected Forward getSubtestDetails() {
		    	Integer testRosterID = Integer.valueOf(getRequest().getParameter("testRosterId"));
		    	String[] selectedItemSetIds = getRequest().getParameterValues("selectedItemSetIds");
		    	Base base = prepareSubtestsDetailInformation(testRosterID, selectedItemSetIds, this.subtestValidationAllowed);
		    	base.setSubtestValidationAllowed(this.subtestValidationAllowed);
		    	createGson(base);
		        return null;
		    }
		    
		    protected Base prepareSubtestsDetailInformation(Integer testRosterID, String[] selectedItemSetIds, boolean validation)
		    {
		    	Base base = new Base();
		        RosterElement re = getTestRosterDetails(testRosterID);
		        TestSessionVO testSession = getTestSessionDetails(this.sessionId);
		        TestProduct testProduct = getProductForTestAdmin(this.sessionId);
		        boolean isTabeSession = isTabeSession(testProduct.getProductType());
		        boolean isTabeLocatorSession = isTabeLocatorSession(testProduct.getProductType());
		       
		        // START- Added for LLO-109 
		        isLasLinkCustomer();
		        boolean isLaslinkSession  = this.isLasLinkCustomer;
		        boolean testSessionCompleted = isTestSessionCompleted(testSession);
		        this.studentStatusSubtests = buildStudentStatusSubtests(re.getStudentId(), this.sessionId, testSessionCompleted, isTabeSession, isTabeLocatorSession, isLaslinkSession);       
		       
		        String testGrade = getTestGrade(this.studentStatusSubtests);
		        String testLevel = getTestLevel(this.studentStatusSubtests);
		        
		        base.setStudentName(re.getFirstName() + " " + re.getLastName());
		        base.setLoginName(re.getUserName());
		        base.setPassword(re.getPassword());
		        base.setTestSession(testSession);
		        base.setTestStatus(FilterSortPageUtils.testStatus_CodeToString(re.getTestCompletionStatus()));
		        base.setTestElement(this.studentStatusSubtests);
		        if (!isTabeSession) {
		            if (!testGrade.equals("--")) {
		            	base.setTestGrade(testGrade);
		            }
		            if (!testLevel.equals("--")) {
		            	base.setTestLevel(testLevel);
		            }
		        }
		        boolean showStudentFeedback = false;
		        if ((testSession.getShowStudentFeedback() != null) 
		        		&& (testSession.getShowStudentFeedback().equalsIgnoreCase("T"))) {
		            showStudentFeedback = true;
		        }
		        boolean isShowScores = this.sessionDetailsShowScores;
		        base.setShowScores(isShowScores);
		        
		        int numberColumn = 4;
		        if (isTabeSession)
		            numberColumn += 1;
		        if (isShowScores)
		            numberColumn += 3;
		        if (isLaslinkSession)
		        	numberColumn += 2;
		        	
		       // END- Added for LLO-109 
		        if (validation) {
		            numberColumn += 1;
		            if (this.setCustomerFlagToogleButton.equals("true"))
		                numberColumn += 1;
		            prepareValidateButtons(selectedItemSetIds);
		        }
		        base.setNumberColumn(numberColumn);
		        base.setSubtestValidationAllowed(this.subtestValidationAllowed);
		        base.setTabeSession(isTabeSession);
		        base.setLaslinkSession(isLaslinkSession);
		        return base;
		    }
		    
		    private List buildStudentStatusSubtests(Integer studentId, Integer testAdminId, boolean testSessionCompleted, boolean isTabeSession, boolean isTabeLocatorSession,boolean isLaslinkSession)
		    {
		    	   
		        String userTimeZone = this.user.getTimeZone();//(String)getSession().getAttribute("userTimeZone"); 
		        List subtestList = new ArrayList();        
		        TestElementData ted = getTestElementsForTestSession(testAdminId); 
		        StudentSessionStatus[] ssss = getStudentItemSetStatusesForRoster(studentId, testAdminId);                 
		        TestElement[] subtestelements = ted.getTestElements(); 
		        HashMap recLevelHM = new HashMap();
		        if (isTabeSession) {
		            subtestelements = orderedSubtestList(subtestelements, studentId, testAdminId);
		        }                
		        boolean isLocatorTD = false;
		        for (int i=0; i < subtestelements.length; i++)
		        {
		            TestElement te = subtestelements[i];          
		              
		            if (te != null)
		            {
		                SubtestDetail sd_TS = new SubtestDetail(te, i + 1);
		          
		                TestElement[] tes = getTestElementsForParent(sd_TS.getItemSetId(), "TD"); 
		                boolean addTS = true;               
		                HashMap subTestHM = new HashMap();
		                for (int j=0; j < ssss.length; j++)
		                {
		                    StudentSessionStatus sss = ssss[j];
		                    
		                    for (int k=0; k < tes.length; k++)
		                    {
		                        TestElement te_TD = tes[k];

		                        if (isTabeSession)
		                        {
		                            addTABESubtest(te_TD);
		                        }
		                        
		                        if (sss.getItemSetId().intValue() == te_TD.getItemSetId().intValue())
		                        {
		                            
		                            SubtestDetail sd_TD = new SubtestDetail(te_TD, -1);
		                            
		                            sd_TD.setValidationStatus(FilterSortPageUtils.validationStatus_CodeToString(sss.getValidationStatus()));
		                            sd_TD.setCustomStatus(FilterSortPageUtils.customStatus_ToString(sss.getCustomerFlagStatus()));
		                            
		                            if (addTS)
		                            {
		                                if (sd_TD.getSubtestName().indexOf("Locator") < 0)
		                                {                                    
		                                    subtestList.add(sd_TS); 
		                                    addTS = false;                                                         
		                                }
		                                else
		                                {  
		                                    isLocatorTD = true;                                      
		                                }                                
		                            }
		                            
		                            if (isTabeSession)
		                            {
		                                String level = te_TD.getItemSetForm();
		                                if ((level == null) || level.equals("1"))
		                                    level = "";
		                                sd_TD.setLevel(level);
		                            }
		                            
		                            
		                            String status = FilterSortPageUtils.testStatus_CodeToString(sss.getCompletionStatus());
		                            if (testSessionCompleted)
		                            {
		                                if (status.equals(FilterSortPageUtils.FILTERTYPE_SCHEDULED))
		                                {
		                                    status = FilterSortPageUtils.FILTERTYPE_NOTTAKEN; 
		                                }
		                                if (status.equals(FilterSortPageUtils.FILTERTYPE_SYSTEMSTOP) || status.equals(FilterSortPageUtils.FILTERTYPE_STUDENTSTOP) || status.equals(FilterSortPageUtils.FILTERTYPE_INPROGRESS))
		                                {
		                                    status = FilterSortPageUtils.FILTERTYPE_INCOMPLETE; 
		                                }
		                            }
		                            sd_TD.setCompletionStatus(status);
		                                                        
		                            if (sss.getStartDateTime() != null)
		                            {                                
		                                Date adjStartDate = com.ctb.util.DateUtils.getAdjustedDate(sss.getStartDateTime(), "GMT", userTimeZone, sss.getStartDateTime());
		                                String startDate = DateUtils.formatDateToDateString(adjStartDate);
		                                String startTime = DateUtils.formatDateToTimeString(adjStartDate);                                
		                                sd_TD.setStartDate(startDate + " " + startTime);
		                            }
		                            
		                            if (sss.getCompletionDateTime() != null)
		                            {
		                                Date adjEndDate = com.ctb.util.DateUtils.getAdjustedDate(sss.getCompletionDateTime(), "GMT", userTimeZone, sss.getCompletionDateTime());
		                                String endDate = DateUtils.formatDateToDateString(adjEndDate);
		                                String endTime = DateUtils.formatDateToTimeString(adjEndDate);                                
		                                sd_TD.setEndDate(endDate + " " + endTime);
		                            }
		                            
		                            sd_TD.setMaxScore(sss.getMaxScore());
		                            sd_TD.setRawScore(sss.getRawScore());
		                            sd_TD.setUnScored(sss.getUnscored());
		                            String tdSubtestName = sd_TD.getSubtestName();
		                            String sn = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
		                                        tdSubtestName;
		                            sd_TD.setSubtestName(sn);
		                               
		                            // START- Added for LLO-109 
		                            if(isLaslinkSession)
		                            {
		                               	sd_TD.setTestExemptions(sss.getTestExemptions());
		                            	sd_TD.setAbsent(sss.getAbsent());
		                             }
		                             // END- Added for LLO-109 
		                             
		                            if (!isLocatorTD)
		                            {                                
		                                subtestList.add(sd_TD);
		                            }
		                            else
		                            {
		                                if (!subTestHM.containsValue(tdSubtestName))
		                                {
		                                    if (addTS)
		                                    {                                    
		                                        subtestList.add(sd_TS); 
		                                        addTS = false;
		                                    }
		                                    if (status.equals(FilterSortPageUtils.FILTERTYPE_COMPLETED))
		                                    {
		                                        if (sss.getRecommendedLevel() != null)
		                                        {   
		                                            if (tdSubtestName.indexOf("Reading") > 0)                                         
		                                                recLevelHM.put("Reading", sss.getRecommendedLevel());
		                                            else if (tdSubtestName.indexOf("Mathematics Computation") > 0)
		                                                recLevelHM.put("Mathematics Computation", sss.getRecommendedLevel());
		                                            else if (tdSubtestName.indexOf("Applied Mathematics") > 0)
		                                                recLevelHM.put("Applied Mathematics", sss.getRecommendedLevel());
		                                            else if (tdSubtestName.indexOf("Language") > 0)
		                                                recLevelHM.put("Language", sss.getRecommendedLevel());
		                                        }
		                                    }
		                                    else
		                                        sd_TD.setLevel("");
		                                    
		                                    String subtestName = tdSubtestName.substring(5, tdSubtestName.length()).trim();
		                                    if (subtestName.indexOf("Sample") > 0)
		                                    {
		                                        int indexOfSample = subtestName.indexOf("Sample");
		                                        subtestName = subtestName.substring(0, indexOfSample).trim();
		                                    }
		                                    if (recLevelHM.size() > 0)
		                                    {                                      
		                                        if (recLevelHM.containsKey(subtestName))
		                                        {
		                                            if (subtestName.indexOf("Mathematics Computation") >= 0 || subtestName.indexOf("Applied Mathematics") >= 0)
		                                            {                                        
		                                                if (recLevelHM.containsKey("Mathematics Computation") && recLevelHM.containsKey("Applied Mathematics"))
		                                                {
		                                                    sd_TD.setLevel(recLevelHM.get(subtestName).toString());
		                                                }                                               
		                                            }
		                                            else
		                                            {
		                                                sd_TD.setLevel(recLevelHM.get(subtestName).toString());
		                                            }
		                                        }
		                                        if (subtestName.indexOf("Vocabulary") >= 0 && recLevelHM.containsKey("Reading"))
		                                            sd_TD.setLevel(recLevelHM.get("Reading").toString());
		                                        if (recLevelHM.containsKey("Language"))
		                                        {
		                                            if (subtestName.indexOf("Language Mechanics") >= 0)
		                                                sd_TD.setLevel(recLevelHM.get("Language").toString());
		                                            else if (subtestName.indexOf("Spelling") >= 0)
		                                                sd_TD.setLevel(recLevelHM.get("Language").toString());
		                                        }                                   
		                                    }
		                                        
		                                    subtestList.add(sd_TD);  
		                                    subTestHM.put(sd_TD, tdSubtestName);
		                                }                              
		                            }
		                            break;
		                            
		                        }
		                    }
		                }
		            }
		        }                                        
		        return subtestList;
		    }
		    
		 @Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success",
	                     path = "view_subtest_details.jsp")
	     })
		 protected Forward toggleValidationStatus(ViewMonitorStatusForm form) {
	        Integer testRosterId = Integer.parseInt(getRequest().getParameter("testRosterId"));
			try {      
	            this.testSessionStatus.toggleRosterValidationStatus(this.userName, testRosterId);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
		}
			
		@Jpf.Action(forwards = { 
		    @Jpf.Forward(name = "success",
	                     path = "view_subtest_details.jsp")
	    })
		 protected Forward toggleSubtestValidationStatus(ViewMonitorStatusForm form)  {       
			String strItemSetIds = null;
			String[] itemSetIdsList = null;
			Base base = new Base();
            TestProduct testProduct = getProductForTestAdmin(this.sessionId);
            boolean isTabeSession = isTabeSession(testProduct.getProductType());
            base.setTabeSession(isTabeSession);
            Integer testRosterId = Integer.parseInt(getRequest().getParameter("testRosterId"));
	        if(getRequest().getParameter("itemSetIds") != null){
	        	strItemSetIds = getRequest().getParameter("itemSetIds");
	        	itemSetIdsList = strItemSetIds.split("\\|");
	        }
	        Integer[] itemSetIds = new Integer[itemSetIdsList.length];
	        for(int i=0; i<itemSetIdsList.length; i++){
	        	itemSetIds[i] = Integer.valueOf(itemSetIdsList[i]);
	        }
	        try {
	        	this.testSessionStatus.toggleSubtestValidationStatus(this.userName, testRosterId, itemSetIds, "ValidationStatus" );
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	        createGson(base);
 		    return null;
		}

	//Added for view/monitor test status: End
		
		@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success",
	                     path = "view_subtest_details.jsp")
	     })
		 protected Forward toggleDonotScoreStatus() {
	        Integer testRosterId = Integer.parseInt(getRequest().getParameter("testRosterId"));
	        String dnsStatus = getRequest().getParameter("dnsStatus");
	        try {      
	            this.testSessionStatus.updateDonotScore(testRosterId, dnsStatus, this.user.getUserId());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
		}
		
	    private boolean isDeleteSessionEnable() 
	    {               
	        String roleName = this.user.getRole().getRoleName();        
	        return (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) ||
	                roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR) ||
	                roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_COORDINATOR));
	    }
	    
		@Jpf.Action
	    protected Forward deleteTest(SessionOperationForm form)
	    {
			String jsonData = "";
    		OutputStream stream = null;
    		HttpServletResponse resp = getResponse();
    	    resp.setCharacterEncoding("UTF-8"); 
			Integer testAdminId = Integer.valueOf(getRequest().getParameter("testAdminId"));
			boolean hasStudentLoggedIn = false;
			OperationStatus status = new OperationStatus();
			try {
				ScheduledSession scheduledSession = this.scheduleTest.getScheduledSessionDetails(this.userName, testAdminId);
				int studentsLoggedIn = scheduledSession.getStudentsLoggedIn() == null ? 0 : scheduledSession.getStudentsLoggedIn().intValue();
				if(studentsLoggedIn > 0){
					hasStudentLoggedIn = true;
				}
				if(!hasStudentLoggedIn){
					this.scheduleTest.deleteTestSession(this.userName, testAdminId);
	            	status.setSuccess(true);
				}
	            else{
					status.setSuccess(false);
	            }
	        }
	        catch (CTBBusinessException e) {
	            e.printStackTrace();
	            //this.getRequest().setAttribute("errorMessage",MessageResourceBundle.getMessage("SelectSettings.FailedToDeleteTestSession", e.getMessage()));               
	        }
			Gson gson = new Gson();
			jsonData = gson.toJson(status);
//			System.out.println(jsonData);
			try {
				resp.setContentType(CONTENT_TYPE_JSON);
				stream = resp.getOutputStream();
				stream.write(jsonData.getBytes("UTF-8"));
				resp.flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return null;
	    }
}