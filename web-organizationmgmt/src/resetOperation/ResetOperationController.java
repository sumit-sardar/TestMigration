package resetOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.BroadcastUtils;
import utils.CustomerServiceSearchUtils;
import utils.PermissionsUtils;

import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfig;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.ScheduleElementData;
import com.ctb.bean.testAdmin.StudentSessionStatusData;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.OperationStatus;
import com.ctb.util.SuccessInfo;
import com.google.gson.Gson;

import dto.Message;
import dto.RestTestVO;
import dto.ScheduleElementVO;
import dto.StudentSessionStatusVO;

@Jpf.Controller
public class ResetOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;
	
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;

    @Control()
    private com.ctb.control.licensing.Licensing licensing;
    
    @Control()
    private com.ctb.control.userManagement.UserManagement userManagement;
    
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.BroadcastMessageLog message;
    
    @Control()
    private com.ctb.control.organizationManagement.OrganizationManagement organizationManagement;
    
    @Control()
	private com.ctb.control.customerServiceManagement.CustomerServiceManagement customerServiceManagement;
    
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.studentManagement.StudentManagement studentManagement ;
    
	private String userName = null;
	private Integer customerId = null;
    private User user = null;
    private String userTimeZone = null;
 
    
    private CustomerLicense[] customerLicenses = null;
    private Map<String, StudentSessionStatusVO> studentDetailsMap = null;
    
    
    public static String CONTENT_TYPE_JSON = "application/json";
    
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
	 * @jpf:forward name="success" path="services.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success", path = "services.do")
	})
	protected Forward begin()
	{
		return new Forward("success");
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
			 @Jpf.Forward(name = "downloadDataLink", path = "services_downloadData.do")
	 }) 
	 protected Forward services()
	 {
		 String menuId = (String)this.getRequest().getParameter("menuId");    	
		 String forwardName = (menuId != null) ? menuId : "hasResetTestSessionLink";

		 return new Forward(forwardName);
	 }

	 @Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "success", path = "reset_test_session.jsp") 
	 }) 
	 protected Forward services_resetTestSession()
	 {
		 getLoggedInUserPrincipal();

		 getUserDetails();

		 this.customerLicenses = getCustomerLicenses();
		 
		 setupUserPermission();

		 List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
		 this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
		 
		 
		 return new Forward("success");
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

    @Jpf.Action()
	protected Forward findSubtestListBySession()
	{
		HttpServletResponse resp = getResponse();
		resp.setCharacterEncoding("UTF-8"); 
		OutputStream stream = null;
		ScheduleElementData scheduleElementData = null;
		StudentSessionStatusData sstData = null;
		RestTestVO vo = new RestTestVO();
		List<StudentSessionStatusVO> studentDetailsList = new ArrayList<StudentSessionStatusVO>();
		if (this.userName == null) {
			getLoggedInUserPrincipal();
			getUserDetails();
		}
		try {
			
			String testAccessCode = getRequest().getParameter("testAccessCode");
			scheduleElementData = CustomerServiceSearchUtils.getTestDeliveryDataInTestSession(
					customerServiceManagement,this.userName,testAccessCode);
			if (scheduleElementData != null && scheduleElementData.getFilteredCount().intValue() > 0) {
				List<ScheduleElementVO> deliverableItemSetList = CustomerServiceSearchUtils.buildTestDeliveritemList(scheduleElementData);
				Integer defTestAdminId = deliverableItemSetList.get(0).getTestAdminId();
				Integer defItemSetId = scheduleElementData.getElements()[0].getItemSetId();
				sstData = CustomerServiceSearchUtils.getStudentListForSubTest(
						customerServiceManagement, defTestAdminId, defItemSetId , null, null, null);
				if((sstData != null) && (sstData.getFilteredCount().intValue() == 0)){
					 studentDetailsMap = new TreeMap<String, StudentSessionStatusVO>();
					 vo.getStatus().setSuccessInfo(new SuccessInfo());
					 vo.getStatus().getSuccessInfo().setKey("FIND_NO_SUBTEST_DATA_RESULT");
					 vo.getStatus().getSuccessInfo().setMessageHeader(Message.FIND_NO_SUBTEST_DATA_RESULT);
				 } else {
					 studentDetailsList = CustomerServiceSearchUtils.buildSubtestList(sstData, this.userTimeZone);
					 prepareStudentDetailsMap(studentDetailsList);
				 }
				 				 
				 vo.setDeliverableItemSetList(deliverableItemSetList);
				 vo.setStudentDetailsList(studentDetailsList);
				 vo.setSelectedTestAdmin(defTestAdminId);
				 vo.setSelectedItemSetId(defItemSetId);
				 vo.getStatus().setSuccess(true);
				 
			}else{
				studentDetailsMap = new TreeMap<String, StudentSessionStatusVO>();
				 vo.getStatus().setSuccess(true);
				 vo.getStatus().setSuccessInfo(new SuccessInfo());
				 vo.getStatus().getSuccessInfo().setKey("FIND_NO_TESTDATA_RESULT");
				 vo.getStatus().getSuccessInfo().setMessageHeader(Message.FIND_NO_TESTDATA_RESULT);
			}
			try {
				Gson gson = new Gson();
				String json = gson.toJson(vo);
				resp.setContentType(CONTENT_TYPE_JSON);
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(json.getBytes("UTF-8"));

			} finally{
				if (stream!=null){
					stream.close();
				}
			}
			
		}catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.err.println("Exception while findSubtestListBySession.");
			e.printStackTrace();
		}
    	return null;
	}
    
    
    private void prepareStudentDetailsMap(
			List<StudentSessionStatusVO> studentDetailsList) {
		studentDetailsMap = new TreeMap<String, StudentSessionStatusVO>();
		if(studentDetailsList == null)
			return;
		for (StudentSessionStatusVO studentSessionStatusVO : studentDetailsList) {
			studentDetailsMap.put(studentSessionStatusVO.getStudentItemId(), studentSessionStatusVO);
		}
		
	}

	@Jpf.Action()
	protected Forward findSubtestListBySessionTD()
	{
		HttpServletResponse resp = getResponse();
		resp.setCharacterEncoding("UTF-8"); 
		OutputStream stream = null;
		StudentSessionStatusData sstData = null;
		RestTestVO vo = new RestTestVO();
		List<StudentSessionStatusVO> studentDetailsList = new ArrayList<StudentSessionStatusVO>();
	
		if (this.userName == null) {
			getLoggedInUserPrincipal();
			getUserDetails();
		}
		try {
			
			/*String testAccessCode = getRequest().getParameter("testAccessCode");*/
			Integer itemSetId = Integer.parseInt(getRequest().getParameter("itemSetId"));
			Integer testAdminId = Integer.parseInt(getRequest().getParameter("testAdminId"));
			sstData = CustomerServiceSearchUtils.getStudentListForSubTest(
					customerServiceManagement, testAdminId, itemSetId , null, null, null);
			 if((sstData != null) && (sstData.getFilteredCount().intValue() == 0)){
				 studentDetailsMap = new TreeMap<String, StudentSessionStatusVO>();
				 vo.getStatus().setSuccessInfo(new SuccessInfo());
				 vo.getStatus().getSuccessInfo().setKey("FIND_NO_SUBTEST_DATA_RESULT");
				 vo.getStatus().getSuccessInfo().setMessageHeader(Message.FIND_NO_SUBTEST_DATA_RESULT);
			 } else {
				 studentDetailsList = CustomerServiceSearchUtils.buildSubtestList(sstData, this.userTimeZone);
				 prepareStudentDetailsMap(studentDetailsList);
			 }
			 prepareStudentDetailsMap(studentDetailsList);
			 vo.setStudentDetailsList(studentDetailsList);
			
			 vo.setSelectedTestAdmin(testAdminId);
			 vo.setSelectedItemSetId(itemSetId);
			try {
				Gson gson = new Gson();
				String json = gson.toJson(vo);
				resp.setContentType(CONTENT_TYPE_JSON);
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(json.getBytes("UTF-8"));

			} finally{
				if (stream!=null){
					stream.close();
				}
			}
			
		}catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.err.println("Exception while findSubtestListBySessionTD.");
			e.printStackTrace();
		}
    	return null;
	}
    
	@Jpf.Action()
	protected Forward resetSubtestForStudents(){
		
		HttpServletResponse resp = getResponse();
		resp.setCharacterEncoding("UTF-8"); 
		OutputStream stream = null;
		StudentSessionStatusData sstData = null;
		RestTestVO vo = new RestTestVO();
		try {
			Integer itemSetId = Integer.parseInt(getRequest().getParameter("itemSetId"));
			Integer testAdminId = Integer.parseInt(getRequest().getParameter("testAdminId"));
			String requestDescription = getRequest().getParameter("requestDescription");
			String serviceRequestor = getRequest().getParameter("serviceRequestor");
			String ticketId = getRequest().getParameter("ticketId");
			
			Integer customerID = Integer.parseInt(getRequest().getParameter("customerID"));
			Integer creatorOrgId = Integer.parseInt(getRequest().getParameter("creatorOrgId"));
			List<StudentSessionStatusVO> resetStudentDataList = prepareResetStudentDataList(getRequest().getParameter("resetStudentDataList"));
			
			CustomerServiceSearchUtils.reOpenSubtest( 
					this.customerServiceManagement ,
					this.user,
					requestDescription,
					serviceRequestor,
					ticketId,
					testAdminId,
					customerID,
					resetStudentDataList,
					itemSetId,
					creatorOrgId,
					null);
			
			sstData = CustomerServiceSearchUtils.getStudentListForSubTest(
					customerServiceManagement, testAdminId, itemSetId , null, null, null);
			 List<StudentSessionStatusVO> studentDetailsList = CustomerServiceSearchUtils.buildSubtestList(sstData, this.userTimeZone);
			 prepareStudentDetailsMap(studentDetailsList);
			 vo.setStudentDetailsList(studentDetailsList);
			
			 vo.setSelectedTestAdmin(testAdminId);
			 vo.setSelectedItemSetId(itemSetId);
			 try {
					Gson gson = new Gson();
					String json = gson.toJson(vo);
					resp.setContentType(CONTENT_TYPE_JSON);
					resp.flushBuffer();
					stream = resp.getOutputStream();
					stream.write(json.getBytes("UTF-8"));

				} finally{
					if (stream!=null){
						stream.close();
					}
				}
			
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.err.println("Exception while findSubtestListBySessionTD.");
			e.printStackTrace();
		}
		
		
		return null;
	}

    private List<StudentSessionStatusVO> prepareResetStudentDataList(	String parameter) {
    	List<StudentSessionStatusVO> resetStudentDataList = new ArrayList<StudentSessionStatusVO>();
    	if(parameter !=null && parameter.trim().length()>0){
    		parameter = parameter.trim();
    		String[] values = parameter.split(",");
    		for (String value : values) {
    			if(studentDetailsMap.get(value)!=null){
    				resetStudentDataList.add(studentDetailsMap.get(value));
    			}
				
			}
    		
    	}

		return resetStudentDataList;
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
            userTimeZone = this.user.getTimeZone();
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        this.userTimeZone = userTimeZone;
    }
    
	private void setupUserPermission()
	{
        CustomerConfiguration [] customerConfigs = getCustomerConfigurations(this.customerId);
        boolean adminUser = isAdminUser();
        boolean adminCoordinatorUser = isAdminCoordinatotUser();
        boolean TABECustomer = isTABECustomer(customerConfigs);
        boolean laslinkCustomer = isLaslinkCustomer(customerConfigs);
    	boolean hasResetTestSession = false;
        
        this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));

        this.getSession().setAttribute("isBulkAccommodationConfigured",customerHasBulkAccommodation(customerConfigs));    	
    	
        this.getSession().setAttribute("hasUploadDownloadConfigured", 
        		new Boolean( hasUploadDownloadConfig().booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasProgramStatusConfigured", 
        		new Boolean( hasProgramStatusConfig().booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasScoringConfigured", 
        		new Boolean( customerHasScoring(customerConfigs).booleanValue()));
        
        this.getSession().setAttribute("canRegisterStudent", canRegisterStudent(customerConfigs));
        
     	this.getSession().setAttribute("hasLicenseConfigured", hasLicenseConfiguration() && adminUser);

		this.getSession().setAttribute("isBulkMoveConfigured",customerHasBulkMove(customerConfigs));
		
     	this.getSession().setAttribute("adminUser", new Boolean(adminUser));
     	
     	this.getSession().setAttribute("isOOSConfigured",customerHasOOS(customerConfigs));	// Changes for Out Of School
     	
     	this.getSession().setAttribute("hasRapidRagistrationConfigured", new Boolean(TABECustomer && (adminUser || adminCoordinatorUser) ));
     	
		for (int i=0; i < customerConfigs.length; i++) {
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Reopen_Subtest") && 
            		cc.getDefaultValue().equals("T")	) {
				hasResetTestSession = true;
            }
		}        
		this.getSession().setAttribute("hasResetTestSession", new Boolean(hasResetTestSession));  
		getConfigStudentLabel(customerConfigs);
		
		
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
    
    private boolean isAdminCoordinatotUser() //For Student Registration
	{               
		String roleName = this.user.getRole().getRoleName();        
		return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR); 
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
    
    private Boolean hasLicenseConfiguration()
    {               
    	this.customerLicenses =  getCustomerLicenses();
		return new Boolean(this.customerLicenses.length > 0);
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
				CustomerConfigurationValue[] customerConfigurationsValue = null;
				if(cc.getCustomerConfigurationValues()== null) {
					customerConfigurationsValue = customerConfigurationValues(configId);
				} else {
					customerConfigurationsValue = cc.getCustomerConfigurationValues();
				}
				
				//By default there should be 3 entries for customer configurations
				valueForStudentId = new String[8];
				for(int j=0; j<customerConfigurationsValue.length; j++){
					int sortOrder = customerConfigurationsValue[j].getSortOrder();
					valueForStudentId[sortOrder-1] = customerConfigurationsValue[j].getCustomerConfigurationValue();
				}	
				valueForStudentId[0] = valueForStudentId[0]!= null ? valueForStudentId[0] : "Student ID" ;

			}

		}
		this.getRequest().setAttribute("studentIdLabelName",valueForStudentId[0]);
		
	}
	
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
			
			try {
				Gson gson = new Gson();
				String json = gson.toJson("");
				resp.setContentType(CONTENT_TYPE_JSON);
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(json.getBytes("UTF-8"));

			} finally{
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
    
	 @Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "success", path = "reset_test_session.jsp") 
	 }) 
	 protected Forward resetTestSession()
	 {
		 return new Forward("success");
	 }
	 
	 @Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "/error.jsp") })
		protected Forward error() {
			initialize();
			return new Forward("success");
		}
	 
	 private void initialize()
		{     
			getLoggedInUserPrincipal();
			getUserDetails();
			setupUserPermission();
		}
	
}