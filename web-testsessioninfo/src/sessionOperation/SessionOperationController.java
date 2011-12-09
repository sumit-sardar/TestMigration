package sessionOperation;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import util.MessageResourceBundle;
import util.RequestUtil;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.request.FilterParams.FilterType;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.OrgNodeCategory;
import com.ctb.bean.testAdmin.PasswordHintQuestion;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.SessionStudentData;
import com.ctb.bean.testAdmin.TestElement;
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
import com.ctb.exception.testAdmin.SessionCreationException;
import com.ctb.exception.testAdmin.TransactionTimeoutException;

import com.ctb.exception.validation.ValidationException;

import com.ctb.testSessionInfo.data.SubtestVO;
import com.ctb.testSessionInfo.data.TestVO;
import com.ctb.testSessionInfo.dto.Message;
import com.ctb.testSessionInfo.dto.MessageInfo;
import com.ctb.testSessionInfo.dto.PasswordInformation;
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
import com.ctb.testSessionInfo.utils.TestSessionUtils;
import com.ctb.testSessionInfo.utils.TreeData;
import com.ctb.testSessionInfo.utils.UserOrgHierarchyUtils;
import com.ctb.testSessionInfo.utils.UserPasswordUtils;
import com.ctb.testSessionInfo.utils.WebUtils;
import com.ctb.util.OperationStatus;
import com.ctb.util.SuccessInfo;
import com.ctb.util.ValidationFailedInfo;
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
    
	private String userName = null;
	private Integer customerId = null;
    private User user = null;
    private List sessionListCUFU = new ArrayList(); 
    private List sessionListPA = new ArrayList(); 
    private boolean hasLicenseConfig = false; 
    //private List productNameList = null;
    //private Hashtable productNameToIndexHash = null;
    public static String CONTENT_TYPE_JSON = "application/json";

    public LinkedHashMap hintQuestionOptions = null;
    public UserProfileInformation userProfile = null; 
	private TestProductData testProductData = null;  
	private TestProduct [] tps;
	//private String productType = TestSessionUtils.GENERIC_PRODUCT_TYPE;
	private static final String ACTION_INIT = "init";
	//private static final int RETURN_TYPE_INT = 1;
	boolean isPopulatedSuccessfully = false;
	ScheduleTestVo vo = new ScheduleTestVo();

	//public Condition condition = new Condition();
	
	Map<Integer, String> topNodesMap = new LinkedHashMap<Integer, String>();
	Map<Integer, TestVO> idToTestMap = new LinkedHashMap<Integer, TestVO>();
    
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
        String productName = "";
        String currentAction = this.getRequest().getParameter("currentAction");
        String selectedProductId =  this.getRequest().getParameter("productId");
        if(currentAction==null)
        {
        	currentAction=ACTION_INIT;
        } 
        
          try
        {
            if (this.testProductData == null)
            { // first time here 
                this.testProductData = this.getTestProductDataForUser();
                 tps = this.testProductData.getTestProducts();//changes for performance tuning
                 if( tps!=null ) {
                	 vo.populate(userName,tps, itemSet, scheduleTest);
                	 vo.populateTopOrgnode(this.topNodesMap);
                	 vo.populateTestIdToTestMap(idToTestMap);
                	 
                 }
                 isPopulatedSuccessfully = true;
            } else if (!isPopulatedSuccessfully){
            	vo.populate(userName, tps, itemSet, scheduleTest);
            	vo.populateTopOrgnode(this.topNodesMap);
            	vo.populateTestIdToTestMap(idToTestMap);
            }
        	           
            if(selectedProductId== null || selectedProductId.trim().length()==0)
            {
                if (tps.length > 0 && tps[0] != null)
                {
                     productName = tps[0].getProductName();
                     selectedProductId = tps[0].getProductId().toString();
                     vo.populateAccessCode(scheduleTest);
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
        	System.out.println(jsonData);
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
        	resp.setStatus(resp.SC_INTERNAL_SERVER_ERROR );
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
            String[] studentsBeforeSave =  RequestUtil.getValuesFromRequest(this.getRequest(),"student");;
            int studentCountBeforeSave =0;
            boolean isValidationFailed = false;
            String jsonData = "";
            OperationStatus status = new OperationStatus();
            HttpServletResponse resp = getResponse();
        	resp.setCharacterEncoding("UTF-8"); 
        	OutputStream stream = null;
        	
        	
            if ( studentsBeforeSave != null )
                studentCountBeforeSave = studentsBeforeSave.length;
            try
            {
                testAdminId = createSaveTest(this.getRequest(), validationFailedInfo);
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
                	validationFailedInfo.setKey("SYATEM_EXCEPTION");
                    validationFailedInfo.setMessageHeader(errorMessageHeader);
                    validationFailedInfo.updateMessage(errorMessageBody);
                	
                } else  {
                	 String errorMessageHeader =MessageResourceBundle.getMessage("FailedToSaveTestSession");
                	 String errorMessageBody = MessageResourceBundle.getMessage("FailedToSaveTestSession", e.getMessage());
                     validationFailedInfo.setKey("SYATEM_EXCEPTION");
                     validationFailedInfo.setMessageHeader(errorMessageHeader);
                     validationFailedInfo.updateMessage(errorMessageBody);
                }

            } 
           if (!isValidationFailed && studentCountBeforeSave == studentCountAfterSave) {
        	   
           		String messageHeader = MessageResourceBundle.getMessage("SelectSettings.TestSessionSaved.Header");
           		String messageBody = MessageResourceBundle.getMessage("SelectSettings.TestSessionSaved.Body");
           		successInfo.setKey("TEST_SESSION_SAVED");
           		successInfo.setMessageHeader(messageHeader);
           		successInfo.updateMessage(messageBody);
        	   	status.setSuccess(true); 
        	   	status.setSuccessInfo(successInfo);
        	   	
           } else if (!isValidationFailed)
            {
                int removedCount = studentCountBeforeSave - studentCountAfterSave;
                String messageHeader = MessageResourceBundle.getMessage("SelectSettings.TestSessionSaved.Header");
           		String messageBody = MessageResourceBundle.getMessage("SelectSettings.TestSessionSaved.Body")+ MessageResourceBundle.getMessage("RestrictedStudentsNotSaved", "" +removedCount);
           		successInfo.setKey("TEST_SESSION_SAVED_RES_STD");
           		successInfo.setMessageHeader(messageHeader);
           		successInfo.updateMessage(messageBody);
                status.setSuccess(true);
                status.setSuccessInfo(successInfo);
            } else {
            	status.setSuccess(false);
            	if("SYATEM_EXCEPTION".equalsIgnoreCase(validationFailedInfo.getKey())){
            		status.setSystemError(true);
            	} else {
            		status.setSystemError(false);
            	}
            	status.setValidationFailedInfo(validationFailedInfo);
            }
            
           Gson gson = new Gson();
	       jsonData = gson.toJson(status);
	       System.out.println(jsonData);
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
    
    
    	
   
    	 private Integer createSaveTest(HttpServletRequest httpServletRequest, ValidationFailedInfo validationFailedInfo) throws CTBBusinessException
    	    {  
    		 Integer newTestAdminId = null;
    		 ScheduledSession scheduledSession = new ScheduledSession();
    		 populateTestSession(scheduledSession, httpServletRequest, validationFailedInfo );
    		 if(!validationFailedInfo.isValidationFailed()) {
    			 populateScheduledUnits(scheduledSession, httpServletRequest, validationFailedInfo ); 
    		 }
    		 if(!validationFailedInfo.isValidationFailed()) {
    			 populateSessionStudent(scheduledSession, httpServletRequest, validationFailedInfo );
    		 }
    		 if(!validationFailedInfo.isValidationFailed()) {
    			 populateSessionStudent(scheduledSession, httpServletRequest, validationFailedInfo ); 
    		 }
    		 if(!validationFailedInfo.isValidationFailed()) {
    			 populateProctor(scheduledSession, httpServletRequest , validationFailedInfo);
    		 }
    		 
    		 if(!validationFailedInfo.isValidationFailed()) {
    			 newTestAdminId = this.scheduleTest.createNewTestSession(this.userName, scheduledSession);  
    		 }    		 
    	        return newTestAdminId;
    }
    
     private void populateScheduledUnits(ScheduledSession scheduledSession,
				HttpServletRequest request, ValidationFailedInfo validationFailedInfo) {
    	/* List subtestList = null;*/
	     //boolean sessionHasLocator = false;
    	 try{
    		 String productType				= RequestUtil.getValueFromRequest(request, RequestUtil.PRODUCT_TYPE, true, "");
        	 Integer itemSetId        		= Integer.valueOf(RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_ITEM_SET_ID, false, null));
        	 String hasBreakValue     		= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_HAS_BREAK, false, null);
        	 String hasBreak          		= (hasBreakValue == null || !(hasBreakValue.trim().equals("T") || hasBreakValue.trim().equals("F"))) ? "F" :  hasBreakValue.trim();
        	 boolean hasBreakBoolean        = (hasBreak.equals("T")) ? true : false;
        	 
        	 
        	 List<SubtestVO>  subtestList   = idToTestMap.get(itemSetId).getSubtests();
        	                     
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
    	                /*String level = subVO.getLevel();
    	                te.setItemSetForm(level);*/
    	            }
    	            
    	            if (!hasBreakBoolean ) {
    	            	//String accessCode = RequestUtil.getValueFromRequest(request, RequestUtil.ACCESS_CODE, true, "");
    	            	String accessCode = scheduledSession.getTestSession().getAccessCode();
    	            	te.setAccessCode(accessCode);
    	            } else {
    	            	String accessCode = RequestUtil.getValueFromRequest(request, RequestUtil.ACCESS_CODEB+i, true, "");
    	            	te.setAccessCode(accessCode);
    	            }
    	               
    	            
    	            te.setSessionDefault(subVO.getSessionDefault());
    	            
    	            newTEs[i] = te;
    	        }
    	        
    	        scheduledSession.setScheduledUnits(newTEs);
    	        validateScheduledUnits(scheduledSession, hasBreakBoolean, validationFailedInfo);
            
    	       
    	 } catch (Exception e) {
    		 e.printStackTrace();
    		 validationFailedInfo.setKey("SYATEM_EXCEPTION");
			 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("Syatem.Exception.Header"));
			 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("Syatem.Exception.Body"));
    	 }
    	 
	        
	        
	        
	        
	        
			
		}

	private void populateProctor(ScheduledSession scheduledSession,
				HttpServletRequest request, ValidationFailedInfo validationFailedInfo) {
		User[] proctorArray = new User[1];
		proctorArray[0]= this.user;
		scheduledSession.setProctors(proctorArray);
			
		}

	private void populateSessionStudent(ScheduledSession scheduledSession,
				HttpServletRequest httpServletRequest, ValidationFailedInfo validationFailedInfo) {
			// TODO Auto-generated method stub
			
		}

	private void populateTestSession(ScheduledSession scheduledSession, HttpServletRequest request, ValidationFailedInfo validationFailedInfo) {
		
		 try{
			 TestSession testSession = new TestSession();
			 Set<Integer> keySet            = this.topNodesMap.keySet();
			 Integer[] topnodeids= (keySet).toArray(new Integer[keySet.size()]);
			 Integer creatorOrgNod    		= topnodeids[0];
			 Integer itemSetId        		= Integer.valueOf(RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_ITEM_SET_ID, false, null));
			 
			 TestVO selectedTest = idToTestMap.get(itemSetId);
			 Integer productId        		= Integer.valueOf(RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_PRODUCT_ID, true, "-1"));
			 Date dailyLoginEndTime   		= DateUtils.getDateFromTimeString(RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_END_TIME, false, null));
			 Date dailyLoginStartTime 		= DateUtils.getDateFromTimeString(RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_START_TIME, false, null));
			 Date dailyLoginEndDate   		= DateUtils.getDateFromDateString(RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_END_DATE, false, null));
			 Date dailyLoginStartDate 		= DateUtils.getDateFromDateString(RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_START_DATE, false, null));
			 String location          		= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_LOCATION, false, null);
			 String hasBreakValue     		= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_HAS_BREAK, false, null);
			 String hasBreak          		= (hasBreakValue == null || !(hasBreakValue.trim().equals("T") || hasBreakValue.trim().equals("F"))) ? "F" :  hasBreakValue.trim();
			 boolean hasBreakBoolean        = (hasBreak.equals("T")) ? true : false;
			 String isRandomize       		= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_RANDOMIZE, true, "");
			 String timeZone          		= DateUtils.getDBTimeZone( RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_TIME_ZONE, false, null));
			 String sessionName		  		= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_TEST_NAME, false, null);
			 //String sessionName       		= RequestUtil.getValueFromRequest(request, RequestUtil.SESSION_SESSION_NAME, false, null);
			 String showStdFeedbackVal   	= RequestUtil.getValueFromRequest(request, RequestUtil.SHOW_STUDENT_FEEDBACK, true, "false");
			 String showStdFeedback         = (showStdFeedbackVal==null || !(showStdFeedbackVal.trim().equals("true") || showStdFeedbackVal.trim().equals("false")) )? "F" :(showStdFeedbackVal.trim().equals("true")? "T" : "F");  
			 String productType				= RequestUtil.getValueFromRequest(request, RequestUtil.PRODUCT_TYPE, true, "");
			 //String formOperand       		= RequestUtil.getValueFromRequest(request, RequestUtil.FORM_OPERAND, true, TestSession.FormAssignment.ROUND_ROBIN);
			 //String overrideFormAssignment 	= RequestUtil.getValueFromRequest(request, RequestUtil.OVERRIDE_FORM_ASSIGNMENT, false, null);
			 //String overrideLoginStartDate    = RequestUtil.getValueFromRequest(request, RequestUtil.OVERRIDE_LOGIN_START_DATE, false, null);
			 /*Date overrideLoginSDate        = null ;
			 if(overrideLoginStartDate!=null)
				 overrideLoginSDate = DateUtils.getDateFromDateString(overrideLoginStartDate);*/
			 //String formAssigned			= RequestUtil.getValueFromRequest(request, RequestUtil.FORM_ASSIGNED, true, "");
			 
			 
			 String formOperand       		=  selectedTest.getFormOperand();
			 String overrideFormAssignment 	=  selectedTest.getOverrideFormAssignment();
			 Date overrideLoginSDate  		=  selectedTest.getOverrideLoginStartDate();
			 String formAssigned			=  (selectedTest.getForms() ==null || selectedTest.getForms().length==0)? null: selectedTest.getForms()[0]; 
			 String testName       		    = 	selectedTest.getTestName(); 
			 // setting default value
			 testSession.setTestAdminStatus("CU");
	         testSession.setTestAdminType("SE");
	         testSession.setActivationStatus("AC");
	         testSession.setEnforceTimeLimit("T");
	         testSession.setCreatedBy(this.userName);

	         
	         testSession.setCreatorOrgNodeId(creatorOrgNod);
	         testSession.setShowStudentFeedback(showStdFeedback);
	         testSession.setProductId(productId);
	         testSession.setDailyLoginEndTime(dailyLoginEndTime);
	         testSession.setDailyLoginStartTime(dailyLoginStartTime);
	         testSession.setLocation(location);
	         testSession.setEnforceBreak(hasBreak);
	         testSession.setIsRandomize(isRandomize);
	         testSession.setLoginEndDate(dailyLoginEndDate);
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
	         
	         testSession.setItemSetId(itemSetId);
	         
	         if (productType!=null && TestSessionUtils.isTabeProduct(productType).booleanValue())
	         {
	             testSession.setFormAssignmentMethod(TestSession.FormAssignment.MANUAL);
	         }

	         if (hasBreakBoolean)
	         {
	        	String accessCode = RequestUtil.getValueFromRequest(request, RequestUtil.ACCESS_CODEB+0, true, "");
	         	testSession.setAccessCode(accessCode);    
	         }
	         else
	         {
	        	 String accessCode = RequestUtil.getValueFromRequest(request, RequestUtil.ACCESS_CODE, true, "");
	        	 testSession.setAccessCode(accessCode); 
	         }
	         
	         validateTestSession(testSession, validationFailedInfo);

	         
	         scheduledSession.setTestSession(testSession);
			 
		 } catch (Exception e) {
			 e.printStackTrace();
			 validationFailedInfo.setKey("SYATEM_EXCEPTION");
			 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("Syatem.Exception.Header"));
			 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("Syatem.Exception.Body"));
			 
		 }
		 // retrieving data from request
		 
		 
			
		}

     private void validateTestSession(TestSession testSession,	ValidationFailedInfo validationFailedInfo) throws Exception {
		String[] TACs = new String[1];
		TACs[0] = testSession.getAccessCode();
		if (!WebUtils.validString(testSession.getTestAdminName())) {
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
     private void validateScheduledUnits(ScheduledSession scheduledSession,	boolean hasBreakBoolean, ValidationFailedInfo validationFailedInfo) {
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
		 validationFailedInfo.setKey("SYATEM_EXCEPTION");
		 validationFailedInfo.setMessageHeader(MessageResourceBundle.getMessage("Syatem.Exception.Header"));
		 validationFailedInfo.updateMessage(MessageResourceBundle.getMessage("Syatem.Exception.Body"));
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
			this.user = this.scheduleTest.getUserDetails(this.userName,
					this.userName);
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

		String jsonTree = "";
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String contentType = CONTENT_TYPE_JSON;
		String studentArray = "";
		String json = "";
		ObjectOutput output = null;
		try {
			System.out.println ("db process time Start:"+new Date());
			
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
	        System.out.println ("db process time End:"+new Date());
	        Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
			List <Row> rows = new ArrayList<Row>();
			if ((tsd != null) && (tsd.getFilteredCount().intValue() > 0))
			{
				System.out.println ("List process time Start:"+new Date());
				base = buildTestSessionList(customerLicenses, tsd, base); 
				System.out.println ("List process time End:"+new Date());
			} else {
				this.setSessionListCUFU(new ArrayList());
		        this.setSessionListPA(new ArrayList());
		        base.setTestSessionCUFU(sessionListCUFU);
		        base.setTestSessionPA(sessionListPA);
			}
			base.setOrgNodeCategory(orgNodeCategory);
			
			
			System.out.println("just b4 gson");	
			Gson gson = new Gson();
			System.out.println ("Json process time Start:"+new Date());
			
			json = gson.toJson(base);
			System.out.println ("Json process time End:"+new Date() +".."+json);


			
			try{
				resp.setContentType("application/json");
				stream = resp.getOutputStream();
				resp.flushBuffer();
				stream.write(json.getBytes());

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
		String jsonTree = "";
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String contentType = CONTENT_TYPE_JSON;
		List sessionList = new ArrayList(0);
		String studentArray = "";
		String json = "";
		ObjectOutput output = null;
		try {
			Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
			List <Row> rows = new ArrayList<Row>();
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
				stream.write(json.getBytes());
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
    	
		String jsonTree = "";
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String contentType = CONTENT_TYPE_JSON;
		List sessionList = new ArrayList(0);
		String studentArray = "";
		String json = "";
		ObjectOutput output = null;
		
		String testId = getRequest().getParameter("selectedTestId");
		String treeOrgNodeId = getRequest().getParameter("stuForOrgNodeId");
		String blockOffGrade = getRequest().getParameter("blockOffGradeTesting");
		String selectedLevel = getRequest().getParameter("selectedLevel");
		Integer selectedOrgNodeId = null;
		Integer selectedTestId = null;
		Integer testAdminId = null;
		if(treeOrgNodeId != null)
			selectedOrgNodeId = Integer.parseInt(treeOrgNodeId);
		if(testId != null)
			selectedTestId = Integer.parseInt(testId);
		try {
			FilterParams studentFilter = null;
			if(blockOffGrade.equalsIgnoreCase("true")) { //Changes for block off grade testing
				studentFilter = generateFilterParams(selectedLevel);				
			}
	        PageParams studentPage = null;
	        SortParams studentSort = null;
	        studentSort = FilterSortPageUtils.buildSortParams(FilterSortPageUtils.STUDENT_DEFAULT_SORT, FilterSortPageUtils.ASCENDING);
	        // get students - getSessionStudents
	        SessionStudentData ssd = getSessionStudents(selectedOrgNodeId, testAdminId, selectedTestId, studentFilter, studentPage, studentSort);
	        List studentNodes = buildStudentList(ssd);
			Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
			List <Row> rows = new ArrayList<Row>();
			base.setStudentNode(studentNodes);
			
			Gson gson = new Gson();
			System.out.println ("Json process time Start:"+new Date());
			json = gson.toJson(base);
			System.out.println ("Json process time End:"+new Date() +".."+json);
			try{
				resp.setContentType("application/json");
				stream = resp.getOutputStream();
				resp.flushBuffer();
				stream.write(json.getBytes());
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
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String contentType = CONTENT_TYPE_JSON;
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

				resp.setContentType(contentType);
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(jsonTree.getBytes());
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
					path ="assessments_sessions.jsp")
	})
    protected Forward getSessionForSelectedOrgNodeGrid(SessionOperationForm form){
    	System.out.println("selected");
		String jsonTree = "";
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String treeOrgNodeId = getRequest().getParameter("treeOrgNodeId");
		Integer selectedOrgNodeId = null;
		if(treeOrgNodeId != null)
			selectedOrgNodeId = Integer.parseInt(treeOrgNodeId);
		String contentType = CONTENT_TYPE_JSON;
		List sessionList = new ArrayList(0);
		String studentArray = "";
		String json = "";
		ObjectOutput output = null;
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
			List <Row> rows = new ArrayList<Row>();
			if ((tsd != null) && (tsd.getFilteredCount().intValue() > 0))
			{
				System.out.println ("List process time Start:"+new Date());
				base = buildTestSessionList(customerLicenses, tsd, base); 
				String userOrgCategoryName = getTestSessionOrgCategoryName(sessionList);
				System.out.println ("List process time End:"+new Date());
			} else {
				this.setSessionListCUFU(new ArrayList());
		        this.setSessionListPA(new ArrayList());
		        base.setTestSessionCUFU(sessionListCUFU);
		        base.setTestSessionPA(sessionListPA);
			}
			
			
			System.out.println("just b4 gson");	
			Gson gson = new Gson();
			System.out.println ("Json process time Start:"+new Date());
			
			json = gson.toJson(base);
			//System.out.println ("Json process time End:"+new Date() +".."+json);


			
			try{
				resp.setContentType("application/json");
				stream = resp.getOutputStream();
				resp.flushBuffer();
				stream.write(json.getBytes());

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
    @Jpf.Action()
    protected Forward organizations()
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
    
    
    /**
     * REPORTS actions
     */    
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "reports.jsp"), 
            @Jpf.Forward(name = "viewReports", path = "viewReports.do") 
            
        }) 
    protected Forward reports()
    {
        return new Forward("success");
        //return new Forward("viewReports");
    }

    
    /**
     * SERVICES actions
     */    
	@Jpf.Action()
    protected Forward services()
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
            String hideAccommodations = customer.getHideAccommodations();
	        if ((hideAccommodations != null) && hideAccommodations.equalsIgnoreCase("T"))
	        {
	            supportAccommodations = Boolean.FALSE;
	        }
	        getSession().setAttribute("supportAccommodations", supportAccommodations); 
	        getSession().setAttribute("schedulerFirstName", this.user.getFirstName());
	        getSession().setAttribute("schedulerLastName", this.user.getLastName());
	        getSession().setAttribute("schedulerUserId", this.user.getUserId().toString());
	        System.out.println("supportAccommodations==>"+supportAccommodations);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
    }
    
    /**
     * getCustomerLicenses
     */
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
        boolean TABECustomer = isTABECustomer(customerConfigs);
        boolean laslinkCustomer = isLaslinkCustomer(customerConfigs);
        
        this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));

        this.getSession().setAttribute("hasUploadDownloadConfigured", 
        		new Boolean( hasUploadDownloadConfig().booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasProgramStatusConfigured", 
        		new Boolean( hasProgramStatusConfig().booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasScoringConfigured", 
        		new Boolean( customerHasScoring(customerConfigs).booleanValue() && adminUser));
        
        //this.getSession().setAttribute("canRegisterStudent", canRegisterStudent(customerConfigs));
        this.getSession().setAttribute("canRegisterStudent", false);//Temporary change to hide register student button
        this.hasLicenseConfig = hasLicenseConfiguration(customerConfigs).booleanValue();
     	this.getSession().setAttribute("hasLicenseConfigured", this.hasLicenseConfig && adminUser);
     	
     	this.getSession().setAttribute("adminUser", new Boolean(adminUser));     
     	
     	this.getSession().setAttribute("userScheduleAndFindSessionPermission", userScheduleAndFindSessionPermission());   
     	
     	getConfigStudentLabel(customerConfigs);
     	
   }
		
	
	private void getConfigStudentLabel(CustomerConfiguration[] customerConfigurations) 
	{     
		boolean isStudentIdConfigurable = false;
		Integer configId=0;
		String []valueForStudentId = new String[8] ;
		valueForStudentId[0] = "Student ID";
		for (int i=0; i < customerConfigurations.length; i++)
		{
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID") && cc.getDefaultValue().equalsIgnoreCase("T"))
			{
				isStudentIdConfigurable = true; 
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
            sd = this.scheduleTest.getSessionStudentsForOrgNode(this.userName, orgNodeId, testAdminId, selectedTestId, filter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return sd;
    }

    private Base buildTestSessionList(CustomerLicense[] customerLicenses, TestSessionData tsd, Base base) 
    {
        List sessionListCUFU = new ArrayList(); 
        List sessionListPA = new ArrayList();        
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
    
    
    private String getTestSessionOrgCategoryName(List testSessionList)
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
		TreeData td = new TreeData ();
		td.setData(org.getOrgName());
		td.getAttr().setId(org.getOrgNodeId().toString());
		td.getAttr().setCategoryID(org.getOrgCategoryLevel().toString());
		//td.getAttr().setCustomerId(org.getCustomerId().toString());
		treeProcess (org,orgList,td,selectedList);
		data.add(td);
	}
	
	private static void treeProcess (Organization org,List<Organization> list,TreeData td,ArrayList<Organization> selectedList) {

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
				tempData.getAttr().setCategoryID(tempOrg.getOrgCategoryLevel().toString());
				//tempData.getAttr().setCustomerId(tempOrg.getCustomerId().toString());
				td.getChildren().add(tempData);
				treeProcess (tempOrg,list,tempData,selectedList);
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
            
            this.hintQuestionOptions = new LinkedHashMap();
            
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
    
    private List buildStudentList(SessionStudentData ssd) 
    {
        List studentList = new ArrayList();
        SessionStudent [] sessionStudents = ssd.getSessionStudents();   
        for (int i=0 ; i<sessionStudents.length; i++) {
            SessionStudent ss = (SessionStudent)sessionStudents[i];
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
                studentList.add(ss);
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
	public List getSessionListCUFU() {
		return sessionListCUFU;
	}

	/**
	 * @param sessionListCUFU the sessionListCUFU to set
	 */
	public void setSessionListCUFU(List sessionListCUFU) {
		this.sessionListCUFU = sessionListCUFU;
	}

	/**
	 * @return the sessionListPA
	 */
	public List getSessionListPA() {
		return sessionListPA;
	}

	/**
	 * @param sessionListPA the sessionListPA to set
	 */
	public void setSessionListPA(List sessionListPA) {
		this.sessionListPA = sessionListPA;
	}

	public LinkedHashMap getHintQuestionOptions() {
		return hintQuestionOptions;
	}

	public void setHintQuestionOptions(LinkedHashMap hintQuestionOptions) {
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
    	
		String jsonTree = "";
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String contentType = CONTENT_TYPE_JSON;
		final String PROCTOR_DEFAULT_SORT = "LastName";
		User testScheduler = null;
		
		
		List sessionList = new ArrayList(0);
		String studentArray = "";
		String json = "";
		ObjectOutput output = null;
		
		//String testId = getRequest().getParameter("selectedTestId");
		String proctorOrgNodeId = getRequest().getParameter("proctorOrgNodeId");
		Integer selectedOrgNodeId = null;
		Integer selectedTestId = null;
		Integer testAdminId = null;
		if(proctorOrgNodeId != null)
			selectedOrgNodeId = Integer.parseInt(proctorOrgNodeId);
		//if(testId != null)
			//selectedTestId = Integer.parseInt(testId);
		try {
			FilterParams proctorFilter = null;
	        PageParams proctorPage = null;
	        SortParams proctorSort = FilterSortPageUtils.buildSortParams(PROCTOR_DEFAULT_SORT, FilterSortPageUtils.ASCENDING);
	        FilterParams filter = null;
	        List proctorNodes = null;

	        // Get the list of proctors
	        UserData ud = getProctors(selectedOrgNodeId, proctorFilter, proctorPage, proctorSort);
	        if( ud != null) {
	        	proctorNodes = buildProctorList(ud);
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
				stream.write(json.getBytes());
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
            ud = this.scheduleTest.getUsersForOrgNode(this.userName, orgNodeId, filter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return ud;
    }

	
	public List buildProctorList(UserData uData) {
        ArrayList userList = new ArrayList();
        if (uData != null) {
            User[] users = uData.getUsers();
            if(users != null){
                for (int i=0 ; i<users.length ; i++) {
                    User user = users[i];
                    if (user != null && user.getUserName() != null) {
                        UserProfileInformation userDetail = new UserProfileInformation(user);
                        userDetail.setDefaultScheduler("F");
                        userList.add(userDetail);
                    }
                }
            }
        }
        return userList;
    }
    
    // Added for Proctor : End
	 private String getTACsInString(Vector vec) 
	    {
	        Iterator it = vec.iterator();
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
	        /*if (e instanceof ValidationException) {
	            String msgException = e.getMessage();
	            if (msgException != null) {
	                if (msgException.equals("SelectSettings.TestSessionName.InvalidCharacters") ||
	                    msgException.equals("SelectSettings.TestLocation.InvalidCharacters")) {
	                    errorMessage = MessageResourceBundle.getMessage(msgException);
	                }
	                else {
	                    msgId += ".ValidationException";
	                    errorMessage = MessageResourceBundle.getMessage(msgId);
	                }            
	            }
	            else {
	                msgId += ".ValidationException";
	                errorMessage = MessageResourceBundle.getMessage(msgId);
	            }
	        }*/
	        /*else {*/
	            errorMessage = MessageResourceBundle.getMessage(msgId, e.getMessage());
	        /*}*/
	            
	        return errorMessage; 
	    }
	 
	 //Added for block off grade
	 private FilterParams generateFilterParams(String selectedLevel) {
		 
		 FilterParams studentFilter = new FilterParams();		 
		String [] arg = new String[1];
		arg[0] = selectedLevel;
		studentFilter = new FilterParams();
		ArrayList filters = new ArrayList();
		if(selectedLevel.contains("-")) {
			String [] grades = selectedLevel.split("-");
			int initVal = Integer.parseInt(grades[0]);
			int finalVal = Integer.parseInt(grades[1]);
			for(int i = initVal; i <= finalVal; i++) {
				arg[0] = String.valueOf(i);
				filters.add(new FilterParam("StudentGrade", arg, FilterType.EQUALS));
			}
		} else if(selectedLevel.contains("/")) {
			String [] grades = selectedLevel.split("/");
			arg[0] = grades[0];
			filters.add(new FilterParam("StudentGrade", arg, FilterType.EQUALS));
			arg[0] = grades[1];
			filters.add(new FilterParam("StudentGrade", arg, FilterType.EQUALS));
		} else {
			filters.add(new FilterParam("StudentGrade", arg, FilterType.EQUALS));
		}
		studentFilter.setFilterParams((FilterParam[])filters.toArray(new FilterParam[0]));
		 
		 return studentFilter;
		 
	 }
}