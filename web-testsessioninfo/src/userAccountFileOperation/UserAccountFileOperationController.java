package userAccountFileOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.CustomerReport;
import com.ctb.bean.testAdmin.CustomerReportData;
import com.ctb.bean.testAdmin.ManageTestSession;
import com.ctb.bean.testAdmin.ManageTestSessionData;
import com.ctb.bean.testAdmin.ProgramData;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.control.db.OrgNode;
import com.ctb.exception.CTBBusinessException;
import com.google.gson.Gson;

import com.ctb.testSessionInfo.dto.EmetricAccountFile;
import com.ctb.testSessionInfo.dto.EmetricAccountFileVO;
import com.ctb.testSessionInfo.dto.ReportManager;
import com.ctb.testSessionInfo.utils.FilterSortPageUtils;
import com.ctb.testSessionInfo.utils.PermissionsUtils;





@Jpf.Controller()
public class UserAccountFileOperationController extends PageFlowController{

	private static final long serialVersionUID = 1L;

	@Control()
	private com.ctb.control.userManagement.UserManagement userManagement;

	@Control
	private OrgNode orgNode; 

	@Control()
	private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;

	@Control()
	private com.ctb.control.licensing.Licensing licensing;

	@Control()
	private com.ctb.control.testAdmin.ScheduleTest scheduleTest;

	@Control()
	private com.ctb.control.db.ItemSet itemSet;

	@Control()
	private com.ctb.control.db.BroadcastMessageLog message;

	@Control()
	private com.ctb.control.db.Users users;
	
	@Control()
	private com.ctb.control.db.OrgNode orgnode;

	public static String CONTENT_TYPE_JSON = "application/json";

	private Integer customerId = null;
	private User user = null;
	private String userName = null;
	private boolean hasLicenseConfig = false; 
	public boolean isOKCustomer = false;
	public boolean isEngradeCustomer = false;
	private boolean forceTestBreak = false;
	public ReportManager reportManager = null;
	private UserNodeData userTopNodes = null;
	private ProgramData userPrograms = null;
	private List<String> studentGradesForCustomer;
	private CustomerReportData customerReportData = null;    
	
	/* Changes for DEX Story - Add intermediate screen : Start */
    private boolean isEOIUser = false;
	private boolean isMappedWith3_8User = false;
	private boolean is3to8Selected = false;
	private boolean isEOISelected = false;
	private boolean isUserLinkSelected = false;
   /* Changes for DEX Story - Add intermediate screen : End */
	
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success", path = "accountFiles.do")
	})
	protected Forward begin(){
		return new Forward("success");
	}

	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success", path = "emetric_account_files.jsp")
	})
	protected Forward accountFiles(){
	    if(getSession().getAttribute("is3to8Selected") == null)
			this.is3to8Selected = (getRequest().getParameter("is3to8Selected") != null && "true".equalsIgnoreCase(getRequest().getParameter("is3to8Selected").toString()))? true: false; 
		if(getSession().getAttribute("isEOISelected") == null)
			this.isEOISelected = (getRequest().getParameter("isEOISelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isEOISelected").toString()))? true: false;
		if(getSession().getAttribute("isUserLinkSelected") == null)
			this.isUserLinkSelected = (getRequest().getParameter("isUserLinkSelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isUserLinkSelected").toString()))? true: false;
	 
		if((getRequest().getParameter("is3to8Selected") != null && "true".equalsIgnoreCase(getRequest().getParameter("is3to8Selected").toString()))) {
			this.is3to8Selected = true;
			this.isEOISelected = false;
			this.isUserLinkSelected = false;
			
			getSession().setAttribute("is3to8Selected", this.is3to8Selected);
			getSession().setAttribute("isEOISelected", this.isEOISelected);
			getSession().setAttribute("isUserLinkSelected", this.isUserLinkSelected);
		}
		if((getRequest().getParameter("isEOISelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isEOISelected").toString()))) {
			this.is3to8Selected = false;
			this.isEOISelected = true;
			this.isUserLinkSelected = false;
			
			getSession().setAttribute("is3to8Selected", this.is3to8Selected);
			getSession().setAttribute("isEOISelected", this.isEOISelected);
			getSession().setAttribute("isUserLinkSelected", this.isUserLinkSelected);
		}
		if((getRequest().getParameter("isUserLinkSelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isUserLinkSelected").toString()))) {
			this.is3to8Selected = false;
			this.isEOISelected = false;
			this.isUserLinkSelected = true;
			
			getSession().setAttribute("is3to8Selected", this.is3to8Selected);
			getSession().setAttribute("isEOISelected", this.isEOISelected);
			getSession().setAttribute("isUserLinkSelected", this.isUserLinkSelected);
		}
		
		getLoggedInUserPrincipal();		
		getUserDetails();

		CustomerConfiguration [] customerConfigs = getCustomerConfigurations(this.customerId);

		setUpAllUserPermission(customerConfigs);
		return new Forward("success");
	}
	@Jpf.Action()
	public Forward getAccountFileList() {

		HttpServletResponse resp = getResponse();
		//resp.setCharacterEncoding("UTF-8"); 
		OutputStream stream = null;

		EmetricAccountFileVO vo = new  EmetricAccountFileVO();

		try
		{	
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date = new Date(); 

			try {

				vo.setAccountFileList(getDummyFiles());//dummy files
				Gson gson = new Gson();
				String json = gson.toJson(vo);
				System.out.println("Jason value"+ json );
				resp.setContentType(CONTENT_TYPE_JSON);
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(json.getBytes("UTF-8"));

			}catch(IOException ioe){
				ioe.printStackTrace();
			}
			finally{
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
	private void getLoggedInUserPrincipal()
	{
		/* Changes for DEX Story - Add intermediate screen : Start */
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	if(this.isEOIUser && this.isMappedWith3_8User){
    		//if(getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString())){
    		if(this.is3to8Selected){
    			try {
					this.userName = this.userManagement.fetchMapped3to8User(getRequest().getUserPrincipal().toString());
				} catch (CTBBusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}	
    		else
    			this.userName = getRequest().getUserPrincipal().toString();//principle object will always contain EOI user
    		
    	}else{
			java.security.Principal principal = getRequest().getUserPrincipal();
			if (principal != null) {
				this.userName = principal.toString();
			}   
    	}
		getSession().setAttribute("userName", this.userName);
	}

	private void getUserDetails()
	{
		Boolean supportAccommodations = Boolean.TRUE;
		//String userTimeZone = "GMT";

		try
		{
			if(this.user == null){
				this.user = userManagement.getUser(this.userName, this.userName);
			}
			// this.user = this.testSessionStatus.getUserDetails(this.userName, this.userName);
			Customer customer = this.user.getCustomer();
			this.customerId = customer.getCustomerId();
			getSession().setAttribute("customerId", customerId);
			String hideAccommodations = customer.getHideAccommodations();
			if ((hideAccommodations != null) && hideAccommodations.equalsIgnoreCase("T"))
			{
				supportAccommodations = Boolean.FALSE;
			}
			//UserNodeData associateNode = UserOrgHierarchyUtils.populateAssociateNode(this.userName,this.userManagement);
			//ArrayList<Organization> selectedList  = UserOrgHierarchyUtils.buildassoOrgNodehierarchyList(associateNode);
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
	private boolean isAdminUser() 
	{               
		String roleName = this.user.getRole().getRoleName();        
		return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR); 
	}

	private boolean isAdminCoordinatorUser() 
	{               
		String roleName = this.user.getRole().getRoleName();        
		return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR); 
	}
    private boolean isProctorUser() 
    {               
        String roleName = this.user.getRole().getRoleName();        
        return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_PROCTOR); 
    }
	private boolean isTopLevelUser(){
		boolean isUserTopLevel = false;
		try {
			isUserTopLevel = orgNode.checkTopOrgNodeUser(this.userName);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUserTopLevel;
	}
	private void setUpAllUserPermission(CustomerConfiguration [] customerConfigurations) {

		boolean hasBulkStudentConfigurable = false;
		boolean hasBulkStudentMoveConfigurable = false;
		boolean hasOOSConfigurable = false;
		boolean laslinkCustomer = false;
		boolean tabeCustomer = false;
		boolean TASCCustomer = false;
		boolean isTASCReadinessCustomer = false; //Added for story OAS-1542 TASC Readiness - Enable/Test 'Test Reset'
		boolean adminUser = isAdminUser();
		boolean adminCoordinatorUser = isAdminCoordinatorUser();
    	boolean hasUploadConfig = false;
    	boolean hasDownloadConfig = false;
		boolean hasUploadDownloadConfig = false;
		boolean hasProgramStatusConfig = false;
		boolean hasScoringConfigurable = false;
		boolean hasResetTestSession = false;
		//boolean isOKCustomer = false;
		boolean isGACustomer = false;
		boolean isTopLevelAdmin = new Boolean(isTopLevelUser() && isAdminUser());
		boolean hasDataExportVisibilityConfig = false;
    	Integer dataExportVisibilityLevel = 1;
    	boolean hasBlockUserManagement = false;
    	boolean hasSSOHideUserProfile = false;
    	boolean hasSSOBlockUserModifications = false;
    	boolean isWVCustomer = false;
    	
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName() == null) cc.setCustomerConfigurationName("");
				if (cc.getDefaultValue() == null) cc.setDefaultValue("");

				// For Bulk Accommodation
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentConfigurable = true;
					continue;
				}
				// For Bulk Student Move
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Bulk_Move_Students") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentMoveConfigurable = true;
					continue;
				}
				// For Out Of School Student
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("OOS_Configurable") && 
						cc.getDefaultValue().equals("T")) {
					hasOOSConfigurable = true;
					continue;
				}
				// For LasLink Customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")) {
					laslinkCustomer = true;
					continue;
				}
				// For TABE Customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer")) {
					tabeCustomer = true;
					continue;
				}
				// For TASC Customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("TASC_Customer")) {
					TASCCustomer = true;
	            	continue;
	            }
	 			//Added for story OAS-1542 TASC Readiness - Enable/Test 'Test Reset'
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("TASCReadiness_Customer")){
					isTASCReadinessCustomer = true;
					continue;
	            }
				// For Upload Download
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
				// For Program Status
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Program_Status") && 
						cc.getDefaultValue().equals("T")) {
					hasProgramStatusConfig = true;
					continue;
				}
				// For Hand Scoring
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
						cc.getDefaultValue().equals("T")	) {
					hasScoringConfigurable = true;
					continue;
				}
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subscription") && 
						cc.getDefaultValue().equals("T")	) {
					this.hasLicenseConfig = true;
				}
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Reopen_Subtest") && 
						cc.getDefaultValue().equals("T")	) {
					hasResetTestSession = true;
				}
				//Added for oklahoma customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("OK_Customer")
						&& cc.getDefaultValue().equals("T")) {
					this.isOKCustomer = true;
				}
				if ((cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID") 
						&& cc.getDefaultValue().equalsIgnoreCase("T"))	|| 
						(cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID_2") 
								&& cc.getDefaultValue().equalsIgnoreCase("T"))){
					isGACustomer = true;
				}
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Force_Test_Break") && 
						cc.getDefaultValue().equals("T")	) {
					this.forceTestBreak = true;
				}
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Data_Export_Visibility")) {
					hasDataExportVisibilityConfig = true;
					dataExportVisibilityLevel = Integer.parseInt(cc.getDefaultValue());
					continue;
	            }
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("WV_Customer")
						//[IAA]&& cc.getDefaultValue().equals("T")) {
	            		){
					isWVCustomer = true;
					continue;
	            }
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Block_User_Management_3to8") && 
	            		cc.getDefaultValue().equals("T")) {
	        		hasBlockUserManagement = Boolean.TRUE;
				}
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("SSO_Hide_User_Profile") && 
	            		cc.getDefaultValue().equals("T")) {
					hasSSOHideUserProfile = Boolean.TRUE;
	            }
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("SSO_Block_User_Modifications") && 
	            		cc.getDefaultValue().equals("T")) {
					hasSSOBlockUserModifications = Boolean.TRUE;
	            }
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("ENGRADE_Customer") && 
	            		cc.getDefaultValue().equals("T")) {
	        		this.isEngradeCustomer = true;
	        		continue;
	            }
			}

		}
		if (isWVCustomer)
		{
			if(!isWVCustomerTopLevelAdminAndAdminCO())
			{
			hasUploadConfig=false;
			hasUploadDownloadConfig=false;
			}
		}
		if (hasUploadConfig && hasDownloadConfig) {
			hasUploadDownloadConfig = true;
		}
		if (hasUploadDownloadConfig) {
			hasUploadConfig = false;
			hasDownloadConfig = false;
		}
		
		//**[IAA] Proctor users should not see PRISM reports
		boolean TASCProctor = false;
        if (TASCCustomer && isProctorUser())
        {
        	TASCProctor = true;
        }
        if(isWVCustomer)
		{
			this.getSession().setAttribute("hasUploadConfigured",new Boolean(hasUploadConfig));
			this.getSession().setAttribute("hasUploadDownloadConfigured",new Boolean(hasUploadDownloadConfig));
		}
		else
		{
			this.getSession().setAttribute("hasUploadConfigured",new Boolean(hasUploadConfig && adminUser));
			this.getSession().setAttribute("hasUploadDownloadConfigured",new Boolean(hasUploadDownloadConfig && adminUser));
		}
		//this.getSession().setAttribute("showModifyManifest", new Boolean(userScheduleAndFindSessionPermission() && (tabeCustomer || laslinkCustomer)));
		this.getSession().setAttribute("showModifyManifest", new Boolean(userScheduleAndFindSessionPermission() && tabeCustomer));
		this.getSession().setAttribute("showReportTab", new Boolean((userHasReports().booleanValue() && !TASCProctor) || laslinkCustomer));
		this.getSession().setAttribute("isBulkAccommodationConfigured",new Boolean(hasBulkStudentConfigurable));
		this.getSession().setAttribute("isBulkMoveConfigured",new Boolean(hasBulkStudentMoveConfigurable));
		this.getSession().setAttribute("isOOSConfigured",new Boolean(hasOOSConfigurable));
		this.getSession().setAttribute("hasDownloadConfigured",new Boolean(hasDownloadConfig && adminUser));
		this.getSession().setAttribute("hasProgramStatusConfigured",new Boolean(hasProgramStatusConfig && adminUser));
		this.getSession().setAttribute("hasScoringConfigured",new Boolean(hasScoringConfigurable));
		this.getSession().setAttribute("hasLicenseConfigured",new Boolean(this.hasLicenseConfig && adminUser));
		this.getSession().setAttribute("adminUser", new Boolean(adminUser));
		this.getSession().setAttribute("hasRapidRagistrationConfigured", new Boolean(tabeCustomer&&(adminUser || adminCoordinatorUser) ));
		this.getSession().setAttribute("hasResetTestSession", new Boolean(hasResetTestSession && ((isOKCustomer && isTopLevelAdmin)||(laslinkCustomer && (adminUser||adminCoordinatorUser))||(isGACustomer && adminUser)||(TASCCustomer && isTopLevelAdmin) || (isTASCReadinessCustomer && isTopLevelAdmin))));


		this.getSession().setAttribute("showDataExportTab",laslinkCustomer);
		//this.getSession().setAttribute("showDataExportTab",laslinkCustomer);
		this.getSession().setAttribute("showDataExportTab",new Boolean((isTopLevelUser() && laslinkCustomer) || (hasDataExportVisibilityConfig && checkUserLevel(dataExportVisibilityLevel))));
		
		//show Account file download link      	
		this.getSession().setAttribute("isAccountFileDownloadVisible", new Boolean(laslinkCustomer && isTopLevelAdmin));
		//Done for 3to8 customer to block user module
		this.getSession().setAttribute("hasBlockUserManagement", new Boolean(hasBlockUserManagement));
     	//Done for Engrade customer to block admin users from adding/editing/deleting users
     	this.getSession().setAttribute("hasSSOHideUserProfile", new Boolean(hasSSOHideUserProfile));
     	this.getSession().setAttribute("hasSSOBlockUserModifications", new Boolean(hasSSOBlockUserModifications));
     	this.getSession().setAttribute("isEngradeCustomer", new Boolean(this.isEngradeCustomer));
	}
	
	private boolean checkUserLevel(Integer defaultVisibilityLevel){
		boolean isUserLevelMatched = false;
		try {
			isUserLevelMatched = orgnode.matchUserLevelWithDefault(this.userName, defaultVisibilityLevel);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUserLevelMatched;
	}
	private boolean isWVCustomerTopLevelAdminAndAdminCO(){
		boolean isWVCustomerTopLevelAdminAndAdminCO = false;
		boolean isUserTopLevel =false;
		try {
			isUserTopLevel = orgnode.checkTopOrgNodeUser(this.userName);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (isUserTopLevel &&(isAdminUser() || isAdminCoordinatorUser()))
			isWVCustomerTopLevelAdminAndAdminCO = true;
		return isWVCustomerTopLevelAdminAndAdminCO;
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
	private boolean userScheduleAndFindSessionPermission() 
	{               
		String roleName = this.user.getRole().getRoleName();        
		return (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) ||
				roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR) ||
				roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_COORDINATOR));
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

	private List<EmetricAccountFile> getDummyFiles(){
		List<EmetricAccountFile> accountFileList = new ArrayList<EmetricAccountFile>();
		EmetricAccountFile file = null;

		for(int i =1; i < 32;i++){
			file = new EmetricAccountFile();
			file.setAdminEmail(user.getEmail());
			file.setOwnerEmail("File Number # "+i+"_EmailId");
			file.setFileName("File Number # "+i);
			file.setFileSize(new Integer(Math.abs(new Random().nextInt())).toString());
			file.setUpdateDate("2012-11-03");
			file.setDownloadPath("Path/to/download/"+file.getFileName());
			accountFileList.add(file);
		}
		return accountFileList;
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
	protected Forward eMetric_user_accounts_detail()
	{
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/SessionWeb/userAccountFileOperation/accountFiles.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/SessionWeb/userAccountFileOperation/accountFiles.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/SessionWeb/userAccountFileOperation/accountFiles.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
				String url = "/SessionWeb/userAccountFileOperation/accountFiles.do";
				getResponse().sendRedirect(url);
	    	}
		} 
		catch (IOException ioe)
		{
			System.err.print(ioe.getStackTrace());
		}
		return null;
	}   
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
	private void setupUserPermission(CustomerConfiguration [] customerConfigs)
	{
		setUpAllUserPermission(customerConfigs);

		this.getSession().setAttribute("showStudentReportButton", isTABECustomer(customerConfigs));

		this.getSession().setAttribute("userScheduleAndFindSessionPermission", userScheduleAndFindSessionPermission());   

		this.getSession().setAttribute("isDeleteSessionEnable", isDeleteSessionEnable());

		getConfigStudentLabel(customerConfigs);

		getStudentGrades(customerConfigs);     	

	}
	private boolean isDeleteSessionEnable() 
	{               
		String roleName = this.user.getRole().getRoleName();        
		return (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) ||
				roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR) ||
				roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_COORDINATOR));
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
	private void getStudentGrades(CustomerConfiguration[] customerConfigurations) 
	{     
		this.studentGradesForCustomer = new ArrayList<String>();
		Integer configId=0;
		for (int i=0; i < customerConfigurations.length; i++)
		{
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
			if (cc.getCustomerConfigurationName() == null) cc.setCustomerConfigurationName("");
			if (cc.getDefaultValue() == null) cc.setDefaultValue("");

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
	 * buildReportList
	 */
	private List buildReportList(Integer orgNodeId, Integer programId)
	{

		this.customerReportData = getCustomerReportData(orgNodeId, programId);

		List reportList = new ArrayList();
		CustomerReport[] crs = this.customerReportData.getCustomerReports();

		boolean isTABEAdaptive = false;
		CustomerReport ExportIndividualStudentResults = null;
		CustomerReport GroupList = null;
		CustomerReport IndividualPortfolio = null;

		for (int i=0; i < crs.length; i++)
		{                
			CustomerReport cr = crs[i];
			if (cr.getProductId().intValue() == 8000)
				isTABEAdaptive = true;

			String reportUrl = cr.getReportUrl();
			if (reportUrl.indexOf("http:") == 0) {
				reportUrl = reportUrl.replaceAll("http:", "https:");
				cr.setReportUrl(reportUrl);
			}
			if (! cr.getReportName().equals("IndividualProfile")) {
				reportList.add(cr);
			}
			if (cr.getReportName().equals("ExportIndividualStudentResults")) {
				ExportIndividualStudentResults = cr;
			}
			if (cr.getReportName().equals("GroupList")) {
				GroupList = cr;
			}
			if (cr.getReportName().equals("IndividualPortfolio")) {
				IndividualPortfolio = cr;
			}
		}           

		if (isTABEAdaptive) {
			reportList = new ArrayList();
			if (IndividualPortfolio != null)
				reportList.add(IndividualPortfolio);
			if (GroupList != null)
				reportList.add(GroupList);            
			if (ExportIndividualStudentResults != null)
				reportList.add(ExportIndividualStudentResults);
		}

		return reportList; 
	}

	/**
	 * buildReportUrl
	 */
	private String buildReportUrl(String reportName, List reportList)
	{
		String reportUrl = null;
		if (reportName != null)
		{        
			for (int i=0; i < reportList.size(); i++)
			{                
				CustomerReport cr = (CustomerReport)reportList.get(i);
				if (cr.getReportName().equals(reportName))
				{
					reportUrl = cr.getReportUrl();
				}
			}
		}                    

		return reportUrl; 
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="turnleaf_report_list.jsp"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "turnleaf_report_list.jsp")
	})
	protected Forward getReportList()
	{
		String programIndex = this.getRequest().getHeader("programIndex");        
		String organizationIndex = this.getRequest().getHeader("organizationIndex");

		Integer programId = this.reportManager.setSelectedProgram(programIndex);
		Integer orgNodeId = this.reportManager.setSelectedOrganization(organizationIndex);

		System.out.println("programId=" + programId + " - orgNodeId=" + orgNodeId);

		List reportList = buildReportList(orgNodeId, programId);

		this.getRequest().setAttribute("reportList", reportList);

		return new Forward("success");
	}

	private CustomerReportData getCustomerReportData(Integer orgNodeId, Integer programId) 
	{
		if (orgNodeId == null)
		{
			orgNodeId = this.reportManager.setSelectedOrganization(null);
		}
		if (programId == null)
		{
			programId = this.reportManager.setSelectedProgram(null);
		}

		CustomerReportData crd = null;
		try
		{      
			SortParams sort = FilterSortPageUtils.buildSortParams("DisplayName", "asc");            
			crd = this.testSessionStatus.getCustomerReportData(this.userName, orgNodeId, programId, null, null, sort);
		}
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
		}
		return crd;
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
	protected Forward organizations_manageStudents()
	{
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/StudentWeb/studentOperation/beginFindStudent.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/StudentWeb/studentOperation/beginFindStudent.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/StudentWeb/studentOperation/beginFindStudent.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/StudentWeb/studentOperation/beginFindStudent.do";
	            getResponse().sendRedirect(url);
	    	}
		} 
		catch (IOException ioe)
		{
			System.err.print(ioe.getStackTrace());
		}
		return null;
	}


	@Jpf.Action()
	protected Forward organizations_manageOrganizations()
	{
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/OrganizationWeb/orgOperation/organizations_manageOrganizations.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/OrganizationWeb/orgOperation/organizations_manageOrganizations.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/OrganizationWeb/orgOperation/organizations_manageOrganizations.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/OrganizationWeb/orgOperation/organizations_manageOrganizations.do";
	            getResponse().sendRedirect(url);
	    	}
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
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/StudentWeb/bulkOperation/organizations_manageBulkAccommodation.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/StudentWeb/bulkOperation/organizations_manageBulkAccommodation.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/StudentWeb/bulkOperation/organizations_manageBulkAccommodation.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
				String url = "/StudentWeb/bulkOperation/organizations_manageBulkAccommodation.do";
				getResponse().sendRedirect(url);
	    	}
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
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/UserWeb/userOperation/organizations_manageUsers.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/UserWeb/userOperation/organizations_manageUsers.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/UserWeb/userOperation/organizations_manageUsers.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
				String url = "/UserWeb/userOperation/organizations_manageUsers.do";
				getResponse().sendRedirect(url);
	    	}
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
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/StudentWeb/bulkMoveOperation/organizations_manageBulkMove.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/StudentWeb/bulkMoveOperation/organizations_manageBulkMove.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/StudentWeb/bulkMoveOperation/organizations_manageBulkMove.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
				String url = "/StudentWeb/bulkMoveOperation/organizations_manageBulkMove.do";
				getResponse().sendRedirect(url);
	    	}
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
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/StudentWeb/outOfSchoolOperation/organizations_manageOutOfSchool.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/StudentWeb/outOfSchoolOperation/organizations_manageOutOfSchool.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/StudentWeb/outOfSchoolOperation/organizations_manageOutOfSchool.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
				String url = "/StudentWeb/outOfSchoolOperation/organizations_manageOutOfSchool.do";
				getResponse().sendRedirect(url);
	    	}
		} 
		catch (IOException ioe)
		{
			System.err.print(ioe.getStackTrace());
		}
		return null;
	}
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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/SessionWeb/sessionOperation/assessments_sessions.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/SessionWeb/sessionOperation/assessments_sessions.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/SessionWeb/sessionOperation/assessments_sessions.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/SessionWeb/sessionOperation/assessments_sessions.do";
	            getResponse().sendRedirect(url);
	    	}
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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/SessionWeb/programOperation/assessments_programStatus.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/SessionWeb/programOperation/assessments_programStatus.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/SessionWeb/programOperation/assessments_programStatus.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/SessionWeb/programOperation/assessments_programStatus.do";
	            getResponse().sendRedirect(url);
	    	}
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}

}
