package dataExportOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.DataExportSearchUtils;
import utils.FilterSortPageUtils;
import utils.MessageResourceBundle;
import utils.PermissionsUtils;
import utils.BroadcastUtils;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.ManageTestSession;
import com.ctb.bean.testAdmin.ManageTestSessionData;
import com.ctb.bean.testAdmin.ScheduleElementData;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.StudentData;
import com.ctb.bean.testAdmin.StudentSessionStatusData;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.SuccessInfo;
import com.ctb.widgets.bean.PagerSummary;
import com.google.gson.Gson;
import dto.DataExportVO;





@Jpf.Controller
public class DataExportOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;
	private static final String ACTION_DEFAULT = "defaultAction";
	private static final String ACTION_FIND_STUDENT = "findStudent";
	private static final String ACTION_VIEW_STATUS = "getExportStatus";
	private CustomerLicense[] customerLicenses = null;
	CustomerConfiguration[] customerConfigurations = null;
	CustomerConfigurationValue[] customerConfigurationsValue = null;
	private String userName = null;
	private Integer customerId = null;
    private User user = null;
    private String userTimeZone = null;
    
    public static String CONTENT_TYPE_JSON = "application/json";

	/**
	 * Callback that is invoked when this controller instance is created.
	 */
	@Override
	protected void onCreate() {
	}

	/**
	 * Callback that is invoked when this controller instance is destroyed.
	 */
	@Override
	protected void onDestroy(HttpSession session) {
	}
	/**
	 * @common:control
	 */
	@Control()
	private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;
	/**
	 * @common:control
	 */
	@Control()
	private com.ctb.control.dataExportManagement.DataExportManagement dataexportManagement;
	
	@Control()
    private com.ctb.control.licensing.Licensing licensing;
	
	@org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.BroadcastMessageLog message;
	
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;
	
	
	
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success", path = "services.do")
	})
	protected Forward begin()
	{
		return new Forward("success");
	}
	
	
	@Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "exportDataLink", path = "services_dataExport.do"),
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
		 String forwardName = (menuId != null) ? menuId : "exportDataLink";

		 return new Forward(forwardName);
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
   
	@Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "success", path = "index.jsp") 
	 }) 
	 protected Forward services_dataExport()
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
	public Forward getStudentForExport() {

		HttpServletResponse resp = getResponse();
		resp.setCharacterEncoding("UTF-8"); 
		OutputStream stream = null;
		ManageTestSessionData mtsData = null;
		DataExportVO vo = new DataExportVO(); 
		if (this.userName == null) {
			getLoggedInUserPrincipal();
			getUserDetails();
		}
		try
		{
			mtsData = DataExportSearchUtils.getTestSessionsWithUnexportedStudents(customerId);
			if ((mtsData != null) && (mtsData.getFilteredCount().intValue() > 0)) {
				List<ManageTestSession> testSessionList = DataExportSearchUtils.buildTestSessionsWithStudentToBeExportedList(mtsData);
				vo.setTestSessionList(testSessionList);
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
		
		}catch(Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.err.println("Exception while findSessionStudentToBeExport");
			e.printStackTrace();
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
    	boolean isOKCustomer = false;
    	boolean isGACustomer = false;
    	boolean isTopLevelAdmin = new Boolean(isTopLevelUser() && isAdminUser());
    	        
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
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("OK_Customer")
					&& cc.getDefaultValue().equals("T")) {
            	isOKCustomer = true;
            }
			if ((cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID") 
					&& cc.getDefaultValue().equalsIgnoreCase("T"))	|| 
					(cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID_2") 
							&& cc.getDefaultValue().equalsIgnoreCase("T"))){
				isGACustomer = true;
			}
		}        
		this.getSession().setAttribute("hasResetTestSession", new Boolean(hasResetTestSession && ((isOKCustomer && isTopLevelAdmin)||(laslinkCustomer && isTopLevelAdmin)||(isGACustomer && adminUser))));
		this.getSession().setAttribute("hasAuditingResetTestSession", new Boolean(hasResetTestSession && (laslinkCustomer && isTopLevelAdmin)));
		this.getSession().setAttribute("hasDataExportConfigured", new Boolean(laslinkCustomer)); // add for Data Export
		getConfigStudentLabel(customerConfigs);
		
		
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
	
}
