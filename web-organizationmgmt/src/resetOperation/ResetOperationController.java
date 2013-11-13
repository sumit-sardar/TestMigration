package resetOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.BroadcastUtils;
import utils.CustomerServiceSearchUtils;
import utils.PermissionsUtils;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;

import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.ScheduleElementData;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.StudentData;
import com.ctb.bean.testAdmin.StudentSessionStatusData;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.SuccessInfo;
import com.google.gson.Gson;

import dto.Message;
import dto.RestTestVO;
import dto.ScheduleElementVO;
import dto.StudentProfileInformation;
import dto.StudentSessionStatusVO;
import dto.TestSessionVO;

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
    
	@Control()
	private com.ctb.control.db.OrgNode orgnode;
    
	private String userName = null;
	private Integer customerId = null;
    private User user = null;
    private String userTimeZone = null;
 
    
    private CustomerLicense[] customerLicenses = null;
    private Map<String, StudentSessionStatusVO> studentDetailsMap = null;
    
    
    public static String CONTENT_TYPE_JSON = "application/json";
    
    /* Changes for DEX Story - Add intermediate screen : Start */
    private boolean isEOIUser = false;
	private boolean isMappedWith3_8User = false;
	private boolean is3to8Selected = false;
	private boolean isEOISelected = false;
	private boolean isUserLinkSelected = false;
   /* Changes for DEX Story - Add intermediate screen : End */
	
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
		if(getSession().getAttribute("is3to8Selected") == null)
			this.is3to8Selected = (getRequest().getParameter("is3to8Selected") != null && "true".equalsIgnoreCase(getRequest().getParameter("is3to8Selected").toString()))? true: false; 
    	if(getSession().getAttribute("isEOISelected") == null)
    		this.isEOISelected = (getRequest().getParameter("isEOISelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isEOISelected").toString()))? true: false;
    	if(getSession().getAttribute("isUserLinkSelected") == null)
    		this.isUserLinkSelected = (getRequest().getParameter("isUserLinkSelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isUserLinkSelected").toString()))? true: false;

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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/RegistrationWeb/registrationOperation/beginStudentRegistration.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/RegistrationWeb/registrationOperation/beginStudentRegistration.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/RegistrationWeb/registrationOperation/beginStudentRegistration.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	        	String url = "/RegistrationWeb/registrationOperation/beginStudentRegistration.do";
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
	protected Forward assessments_sessionsLink()
	{
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
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
	protected Forward organizations_manageStudents()
	{
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/StudentWeb/studentOperation/organizations_manageStudents.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/StudentWeb/studentOperation/organizations_manageStudents.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/StudentWeb/studentOperation/organizations_manageStudents.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
				 String url = "/StudentWeb/studentOperation/organizations_manageStudents.do";
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
	 * REPORTS actions
	 */    
	@Jpf.Action()
	protected Forward reports()
	{
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/SessionWeb/sessionOperation/reports.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/SessionWeb/sessionOperation/reports.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/SessionWeb/sessionOperation/reports.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/SessionWeb/sessionOperation/reports.do";
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
		 String forwardName = (menuId != null) ? menuId : "hasResetTestSessionLink";

		 return new Forward(forwardName);
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
	    protected Forward services_dataExport()
	    {
		 	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
	    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
	    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
			try
	        {	
				if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
		        	String url = "/ExportWeb/dataExportOperation/services_dataExport.do?is3to8Selected="+this.is3to8Selected;
		        	getResponse().sendRedirect(url);
		        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
		    		String url = "/ExportWeb/dataExportOperation/services_dataExport.do?isEOISelected="+this.isEOISelected;
		    		getResponse().sendRedirect(url);
		    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
		    		String url = "/ExportWeb/dataExportOperation/services_dataExport.do?isUserLinkSelected="+this.isUserLinkSelected;
		    		getResponse().sendRedirect(url);
		    	}else{
		    		String url = "/ExportWeb/dataExportOperation/services_dataExport.do";
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
		protected Forward services_viewStatus()
		{
	    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
	    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
	    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
			try
	        {	
				if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
		        	String url = "/ExportWeb/dataExportOperation/beginViewStatus.do?is3to8Selected="+this.is3to8Selected;
		        	getResponse().sendRedirect(url);
		        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
		    		String url = "/ExportWeb/dataExportOperation/beginViewStatus.do?isEOISelected="+this.isEOISelected;
		    		getResponse().sendRedirect(url);
		    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
		    		String url = "/ExportWeb/dataExportOperation/beginViewStatus.do?isUserLinkSelected="+this.isUserLinkSelected;
		    		getResponse().sendRedirect(url);
		    	}else{
		            String url = "/ExportWeb/dataExportOperation/beginViewStatus.do";
		            getResponse().sendRedirect(url);
		    	}
	        } 
	        catch (IOException ioe)
	        {
	            System.err.print(ioe.getStackTrace());
	        }
	        return null;
		}
	    

	 @Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "success", path = "reset_test_session.jsp") 
	 }) 
	 protected Forward services_resetTestSession()
	 {
		if(getSession().getAttribute("is3to8Selected") == null)
			this.is3to8Selected = (getRequest().getParameter("is3to8Selected") != null && "true".equalsIgnoreCase(getRequest().getParameter("is3to8Selected").toString()))? true: false; 
    	if(getSession().getAttribute("isEOISelected") == null)
    		this.isEOISelected = (getRequest().getParameter("isEOISelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isEOISelected").toString()))? true: false;
    	if(getSession().getAttribute("isUserLinkSelected") == null)
    		this.isUserLinkSelected = (getRequest().getParameter("isUserLinkSelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isUserLinkSelected").toString()))? true: false;
    	
    	/* Changes for DEX Story - Add intermediate screen : Start */
    	//System.out.println("userName from session in user module >> "+getSession().getAttribute("userName"));
    	//System.out.println("isDexLogin from session [user module] >> "+getSession().getAttribute("isDexLogin"));
    	try {
			this.isEOIUser = this.userManagement.isOKEOIUser(getRequest().getUserPrincipal().toString()); //need to check and populate this flag
			this.isMappedWith3_8User = this.userManagement.isMappedWith3_8User(getRequest().getUserPrincipal().toString()); //need to check and populate this flag
			//this.is3to8Selected = (getRequest().getParameter("is3to8Selected") != null && "true".equalsIgnoreCase(getRequest().getParameter("is3to8Selected").toString()))? true: false; 
			//this.isEOISelected = (getRequest().getParameter("isEOISelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isEOISelected").toString()))? true: false;
			//this.isUserLinkSelected = (getRequest().getParameter("isUserLinkSelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isUserLinkSelected").toString()))? true: false;
			if(this.is3to8Selected)
				getSession().setAttribute("is3to8Selected", this.is3to8Selected);
			else if(this.isEOISelected)
				getSession().setAttribute("isEOISelected", this.isEOISelected);
			else if(this.isUserLinkSelected)
				getSession().setAttribute("isUserLinkSelected", this.isUserLinkSelected);
		} catch (CTBBusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Changes for DEX Story - Add intermediate screen : End */
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
		 	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
	    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
	    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
			try
	        {	
				if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
		        	String url = "/OrganizationWeb/licenseOperation/services_manageLicenses.do?is3to8Selected="+this.is3to8Selected;
		        	getResponse().sendRedirect(url);
		        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
		    		String url = "/OrganizationWeb/licenseOperation/services_manageLicenses.do?isEOISelected="+this.isEOISelected;
		    		getResponse().sendRedirect(url);
		    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
		    		String url = "/OrganizationWeb/licenseOperation/services_manageLicenses.do?isUserLinkSelected="+this.isUserLinkSelected;
		    		getResponse().sendRedirect(url);
		    	}else{
		            String url = "/OrganizationWeb/licenseOperation/services_manageLicenses.do";
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
	 protected Forward services_installSoftware()
	 {
		 	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
	    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
	    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
			try
	        {	
				if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
		        	String url = "/SessionWeb/softwareOperation/services_installSoftware.do?is3to8Selected="+this.is3to8Selected;
		        	getResponse().sendRedirect(url);
		        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
		    		String url = "/SessionWeb/softwareOperation/services_installSoftware.do?isEOISelected="+this.isEOISelected;
		    		getResponse().sendRedirect(url);
		    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
		    		String url = "/SessionWeb/softwareOperation/services_installSoftware.do?isUserLinkSelected="+this.isUserLinkSelected;
		    		getResponse().sendRedirect(url);
		    	}else{
					 String url = "/SessionWeb/softwareOperation/services_installSoftware.do";
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
	 protected Forward services_downloadTest()
	 {
		 	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
	    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
	    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
			try
	        {	
				if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
		        	String url = "/SessionWeb/testContentOperation/services_downloadTest.do?is3to8Selected="+this.is3to8Selected;
		        	getResponse().sendRedirect(url);
		        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
		    		String url = "/SessionWeb/testContentOperation/services_downloadTest.do?isEOISelected="+this.isEOISelected;
		    		getResponse().sendRedirect(url);
		    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
		    		String url = "/SessionWeb/testContentOperation/services_downloadTest.do?isUserLinkSelected="+this.isUserLinkSelected;
		    		getResponse().sendRedirect(url);
		    	}else{
		            String url = "/SessionWeb/testContentOperation/services_downloadTest.do";
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
	 protected Forward services_uploadData()
	 {
		 	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
	    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
	    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
			try
	        {	
				if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
		        	String url = "/OrganizationWeb/uploadOperation/services_uploadData.do?is3to8Selected="+this.is3to8Selected;
		        	getResponse().sendRedirect(url);
		        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
		    		String url = "/OrganizationWeb/uploadOperation/services_uploadData.do?isEOISelected="+this.isEOISelected;
		    		getResponse().sendRedirect(url);
		    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
		    		String url = "/OrganizationWeb/uploadOperation/services_uploadData.do?isUserLinkSelected="+this.isUserLinkSelected;
		    		getResponse().sendRedirect(url);
		    	}else{
		            String url = "/OrganizationWeb/uploadOperation/services_uploadData.do";
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
	 protected Forward services_downloadData()
	 {
		 	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
	    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
	    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
			try
	        {	
				if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
		        	String url = "/OrganizationWeb/downloadOperation/services_downloadData.do?is3to8Selected="+this.is3to8Selected;
		        	getResponse().sendRedirect(url);
		        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
		    		String url = "/OrganizationWeb/downloadOperation/services_downloadData.do?isEOISelected="+this.isEOISelected;
		    		getResponse().sendRedirect(url);
		    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
		    		String url = "/OrganizationWeb/downloadOperation/services_downloadData.do?isUserLinkSelected="+this.isUserLinkSelected;
		    		getResponse().sendRedirect(url);
		    	}else{
		            String url = "/OrganizationWeb/downloadOperation/services_downloadData.do";
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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {
        	if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/ScoringWeb/studentScoringOperation/beginStudentScoring.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/ScoringWeb/studentScoringOperation/beginStudentScoring.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/ScoringWeb/studentScoringOperation/beginStudentScoring.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/ScoringWeb/studentScoringOperation/beginStudentScoring.do";
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
	protected Forward findSubtestListBySession() throws CTBBusinessException
	{
		HttpServletResponse resp = getResponse();
		resp.setCharacterEncoding("UTF-8"); 
		OutputStream stream = null;
		ScheduleElementData scheduleElementData = null;
		StudentSessionStatusData sstData = null;
		RestTestVO vo = new RestTestVO();
		List<StudentSessionStatusVO> studentDetailsList = new ArrayList<StudentSessionStatusVO>();
		if(getSession().getAttribute("isEOIUser") != null)
			this.isEOIUser = new Boolean(getSession().getAttribute("isEOIUser").toString()).booleanValue();
		else
			this.isEOIUser = this.userManagement.isOKEOIUser(getRequest().getUserPrincipal().toString()); //need to check and populate this flag

		if(getSession().getAttribute("isMappedWith3_8User") != null)
			this.isMappedWith3_8User = new Boolean(getSession().getAttribute("isMappedWith3_8User").toString()).booleanValue();
		else
			this.isMappedWith3_8User = this.userManagement.isMappedWith3_8User(getRequest().getUserPrincipal().toString()); //need to check and populate this flag
		
		if (this.userName == null || (this.isEOIUser && this.isMappedWith3_8User)) {
			getLoggedInUserPrincipal();
			getUserDetails();
		}
		try {
			
			String testAccessCode = getRequest().getParameter("testAccessCode");
			try {
				scheduleElementData = CustomerServiceSearchUtils.getTestDeliveryDataInTestSession(
						customerServiceManagement,this.userName,testAccessCode);
			} catch (CTBBusinessException cbe) {
				cbe.printStackTrace();
			}
			if (scheduleElementData != null && scheduleElementData.getFilteredCount().intValue() > 0) {
				List<ScheduleElementVO> deliverableItemSetList = CustomerServiceSearchUtils.buildTestDeliveritemList(scheduleElementData);
				Integer defTestAdminId = deliverableItemSetList.get(0).getTestAdminId();
				Integer defItemSetId = scheduleElementData.getElements()[0].getItemSetId();
				try {
					sstData = CustomerServiceSearchUtils.getStudentListForSubTest(
							customerServiceManagement, defTestAdminId, defItemSetId , null, null, null);
				}catch (CTBBusinessException cbe) {
					cbe.printStackTrace();
				}
				if((sstData != null) && (sstData.getFilteredCount().intValue() == 0)){
					 studentDetailsMap = new TreeMap<String, StudentSessionStatusVO>();
				 } else {
					 studentDetailsList = CustomerServiceSearchUtils.buildSubtestList(sstData, this.userTimeZone);
					 prepareStudentDetailsMapForSessionFlow(studentDetailsList);
				 }
				 				 
				 vo.setDeliverableItemSetList(deliverableItemSetList);
				 vo.setStudentDetailsList(studentDetailsList);
				 vo.setSelectedTestAdmin(defTestAdminId);
				 vo.setSelectedItemSetId(defItemSetId);
				 vo.getStatus().setSuccess(true);
				 
			}else{
				studentDetailsMap = new TreeMap<String, StudentSessionStatusVO>();
				 vo.getStatus().setSuccess(true);
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
    
    
    private void prepareStudentDetailsMapForSessionFlow(
			List<StudentSessionStatusVO> studentDetailsList) {
		studentDetailsMap = new TreeMap<String, StudentSessionStatusVO>();
		if(studentDetailsList == null)
			return;
		for (StudentSessionStatusVO studentSessionStatusVO : studentDetailsList) {
			studentDetailsMap.put(studentSessionStatusVO.getStudentItemId(), studentSessionStatusVO);
		}
		
	}
    
    private void prepareStudentDetailsMapForStudentFlow(List<StudentSessionStatusVO> studentDetailsList) {
		studentDetailsMap = new TreeMap<String, StudentSessionStatusVO>();
		if(studentDetailsList == null)
			return;
		for (StudentSessionStatusVO studentSessionStatusVO : studentDetailsList) {
			studentDetailsMap.put(studentSessionStatusVO.getItemSetId().toString(), studentSessionStatusVO);
		}
		
	}

	@Jpf.Action()
	protected Forward findSubtestListBySessionTD() throws CTBBusinessException
	{
		HttpServletResponse resp = getResponse();
		resp.setCharacterEncoding("UTF-8"); 
		OutputStream stream = null;
		StudentSessionStatusData sstData = null;
		RestTestVO vo = new RestTestVO();
		List<StudentSessionStatusVO> studentDetailsList = new ArrayList<StudentSessionStatusVO>();
	
		if(getSession().getAttribute("isEOIUser") != null)
			this.isEOIUser = new Boolean(getSession().getAttribute("isEOIUser").toString()).booleanValue();
		else
			this.isEOIUser = this.userManagement.isOKEOIUser(getRequest().getUserPrincipal().toString()); //need to check and populate this flag

		if(getSession().getAttribute("isMappedWith3_8User") != null)
			this.isMappedWith3_8User = new Boolean(getSession().getAttribute("isMappedWith3_8User").toString()).booleanValue();
		else
			this.isMappedWith3_8User = this.userManagement.isMappedWith3_8User(getRequest().getUserPrincipal().toString()); //need to check and populate this flag
		
		if (this.userName == null || (this.isEOIUser && this.isMappedWith3_8User)) {
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
				 prepareStudentDetailsMapForSessionFlow(studentDetailsList);
			 }
			 prepareStudentDetailsMapForSessionFlow(studentDetailsList);
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
			Boolean isWipeOut = new Boolean (getRequest().getParameter("isWipeOut"));
			
			if(isWipeOut){
				CustomerServiceSearchUtils.wipeOutSubtest ( 
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
			} else {
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
			}
			
			sstData = CustomerServiceSearchUtils.getStudentListForSubTest(
					customerServiceManagement, testAdminId, itemSetId , null, null, null);
			 List<StudentSessionStatusVO> studentDetailsList = CustomerServiceSearchUtils.buildSubtestList(sstData, this.userTimeZone);
			 prepareStudentDetailsMapForSessionFlow(studentDetailsList);
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
	
	
	@Jpf.Action()
	protected Forward resetSubtestForAStudent(){
		
		HttpServletResponse resp = getResponse();
		resp.setCharacterEncoding("UTF-8"); 
		OutputStream stream = null;
		StudentSessionStatusData sstData = null;
		RestTestVO vo = new RestTestVO();
		SortParams sort = null;
		FilterParams filter = null;
		PageParams page = null;
		List<StudentSessionStatusVO> subtestList = new ArrayList<StudentSessionStatusVO>();
		
		try {
			Integer itemSetId = Integer.parseInt(getRequest().getParameter("itemSetId"));
			Integer testAdminId = Integer.parseInt(getRequest().getParameter("testAdminId"));
			Integer studentId = Integer.parseInt(getRequest().getParameter("studentId"));
			String requestDescription = getRequest().getParameter("requestDescription");
			String serviceRequestor = getRequest().getParameter("serviceRequestor");
			String ticketId = getRequest().getParameter("ticketId");
			Integer customerID = Integer.parseInt(getRequest().getParameter("customerID"));
			Integer creatorOrgId = Integer.parseInt(getRequest().getParameter("creatorOrgId"));
			
			Integer testRosterId = Integer.parseInt(getRequest().getParameter("testRosterId"));
			String testAccessCode = getRequest().getParameter("testAccessCode");
			Boolean isWipeOut = new Boolean (getRequest().getParameter("isWipeOut"));
			
			
			List<StudentSessionStatusVO> studentTestStatusDetailsList = new ArrayList<StudentSessionStatusVO>();
			if(studentDetailsMap.get(itemSetId.toString()) != null){
				studentTestStatusDetailsList.add(studentDetailsMap.get(itemSetId.toString()));
			} 
			
			if(isWipeOut){
				
				CustomerServiceSearchUtils.wipeOutSubtest (
						this.customerServiceManagement, 
						this.user,
						requestDescription,
						serviceRequestor,
						ticketId,
						testAdminId,
						customerID, 
						studentTestStatusDetailsList,
						itemSetId,
						creatorOrgId,
						studentId);
			} else {
				CustomerServiceSearchUtils.reOpenSubtest (
						this.customerServiceManagement, 
						this.user,
						requestDescription,
						serviceRequestor,
						ticketId,
						testAdminId,
						customerID, 
						studentTestStatusDetailsList,
						itemSetId,
						creatorOrgId,
						studentId);
			}
			
			sstData = CustomerServiceSearchUtils.getSubtestListForStudent(
					customerServiceManagement, testRosterId, testAccessCode,
						filter, page, sort);
			if (sstData != null && sstData.getFilteredCount().intValue() > 0) {
				subtestList = CustomerServiceSearchUtils.buildSubtestList(sstData,this.userTimeZone);
				prepareStudentDetailsMapForStudentFlow(subtestList);
			} else {
				studentDetailsMap = new TreeMap<String, StudentSessionStatusVO>();
			}
				vo.setStudentDetailsList(subtestList);
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
    //	step 1 of STUDENT tab: find student search criteria
    @Jpf.Action()
	protected Forward findTestSessionListByStudent()
	{

    	HttpServletResponse resp = getResponse();
		resp.setCharacterEncoding("UTF-8"); 
		OutputStream stream = null;
		Student sData = null;
		StudentData studentData = null;
		TestSessionData tsData = null;
		SortParams sort = null;
		FilterParams filter = null;
		PageParams page = null;
		List<TestSessionVO> testSessionList = new ArrayList<TestSessionVO>();
		List<StudentProfileInformation> studentList =  new ArrayList<StudentProfileInformation>();
		RestTestVO vo = new RestTestVO();
		try {
			String studentLoginId = getRequest().getParameter("studentLoginId");
			String testAccessCode = getRequest().getParameter("testAccessCode");
			if(getSession().getAttribute("isEOIUser") != null)
				this.isEOIUser = new Boolean(getSession().getAttribute("isEOIUser").toString()).booleanValue();
			else
				this.isEOIUser = this.userManagement.isOKEOIUser(getRequest().getUserPrincipal().toString()); //need to check and populate this flag

			if(getSession().getAttribute("isMappedWith3_8User") != null)
				this.isMappedWith3_8User = new Boolean(getSession().getAttribute("isMappedWith3_8User").toString()).booleanValue();
			else
				this.isMappedWith3_8User = this.userManagement.isMappedWith3_8User(getRequest().getUserPrincipal().toString()); //need to check and populate this flag
			
			if (this.userName == null || (this.isEOIUser && this.isMappedWith3_8User)) {
				getLoggedInUserPrincipal();
				getUserDetails();
			}
			try {
				sData = CustomerServiceSearchUtils.searchStudentData(customerServiceManagement,
							this.userName, studentLoginId);
				if (sData != null) {
					Student[] students = new Student[1];
					students[0] = sData;
					studentData = new StudentData();
					studentData.setStudents(students,1);
					studentList = CustomerServiceSearchUtils.buildStudentList(studentData);
					tsData = CustomerServiceSearchUtils.getStudentTestSessionData(customerServiceManagement,
								this.userName, sData.getStudentId(),sData.getCustomerId(),testAccessCode ,filter,page,sort);

				}
			} catch (CTBBusinessException cbe){
				cbe.printStackTrace();
			}
			
			if (tsData != null && tsData.getFilteredCount().intValue() > 0) {
				testSessionList = CustomerServiceSearchUtils.buildTestSessionList(tsData);
			} 
			
			vo.setStudentList(studentList);
			vo.setTestSessionList(testSessionList);
			
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
			System.err.println("Exception while findTestSessionListByStudent.");
			e.printStackTrace();
		}
		
		
    	return null;
	}
    
    //	step 2 of STUDENT tab: find subtest search criteria
    @Jpf.Action()
    protected Forward findSubtestByTestSessionId(){
    	

    	HttpServletResponse resp = getResponse();
		resp.setCharacterEncoding("UTF-8"); 
		OutputStream stream = null;
		RestTestVO vo = new RestTestVO();
		StudentSessionStatusData sstData = null;
		SortParams sort = null;
		FilterParams filter = null;
		PageParams page = null;
		List<StudentSessionStatusVO> subtestList = new ArrayList<StudentSessionStatusVO>();
		try{
			//Integer testAdminId = Integer.parseInt(getRequest().getParameter("testAdminId"));
			Integer testRosterId = Integer.parseInt(getRequest().getParameter("testRosterId"));
			String testAccessCode = getRequest().getParameter("testAccessCode");
			try{
				sstData = CustomerServiceSearchUtils.getSubtestListForStudent(
						customerServiceManagement, testRosterId, testAccessCode,
							filter, page, sort);
			}catch (CTBBusinessException cbe){
				cbe.printStackTrace();
			}
			
			if (sstData != null && sstData.getFilteredCount().intValue() > 0) {
				subtestList = CustomerServiceSearchUtils.buildSubtestList(sstData,this.userTimeZone);	
				prepareStudentDetailsMapForStudentFlow(subtestList);
			} else {
				studentDetailsMap = new TreeMap<String, StudentSessionStatusVO>();
			}
				vo.setStudentDetailsList(subtestList);
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
			System.err.println("Exception while findSubtestByTestSessionId.");
			e.printStackTrace();
		}
		
		
		
		
		return null;
    }
    

	/////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
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
    	boolean hasResetTestSessionForAdmin = false;
    	boolean isOKCustomer = false;
    	boolean isGACustomer = false;
    	boolean isTopLevelAdmin = new Boolean(isTopLevelUser() && isAdminUser());
    	boolean hasUploadConfig = false;
    	boolean hasDownloadConfig = false;
    	boolean hasUploadDownloadConfig = false;
    	boolean hasDataExportVisibilityConfig = false;
    	Integer dataExportVisibilityLevel = 1; 
    	        
        this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));

        this.getSession().setAttribute("isBulkAccommodationConfigured",customerHasBulkAccommodation(customerConfigs));    	
    	
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
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Data_Export_Visibility")) {
				hasDataExportVisibilityConfig = true;
				dataExportVisibilityLevel = Integer.parseInt(cc.getDefaultValue());
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
		this.getSession().setAttribute("hasAuditingResetTestSession", new Boolean(hasResetTestSession && (laslinkCustomer && isTopLevelAdmin)));
		getConfigStudentLabel(customerConfigs);
		//this.getSession().setAttribute("showDataExportTab",laslinkCustomer);
		this.getSession().setAttribute("showDataExportTab",new Boolean((isTopLevelUser() && laslinkCustomer) || (hasDataExportVisibilityConfig && checkUserLevel(dataExportVisibilityLevel))));
		//show Account file download link      	
     	this.getSession().setAttribute("isAccountFileDownloadVisible", new Boolean(laslinkCustomer && isTopLevelAdmin));
		
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
    protected Forward broadcastMessage() throws CTBBusinessException
    {
        HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		
		if(getSession().getAttribute("isEOIUser") != null)
			this.isEOIUser = new Boolean(getSession().getAttribute("isEOIUser").toString()).booleanValue();
		else
			this.isEOIUser = this.userManagement.isOKEOIUser(getRequest().getUserPrincipal().toString()); //need to check and populate this flag

		if(getSession().getAttribute("isMappedWith3_8User") != null)
			this.isMappedWith3_8User = new Boolean(getSession().getAttribute("isMappedWith3_8User").toString()).booleanValue();
		else
			this.isMappedWith3_8User = this.userManagement.isMappedWith3_8User(getRequest().getUserPrincipal().toString()); //need to check and populate this flag
		
		if (this.userName == null || (this.isEOIUser && this.isMappedWith3_8User)) {
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
	 
	 @Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "/errorPage.jsp") })
		protected Forward error() {
			initialize();
			return new Forward("success");
		}
	 
	 private void initialize()
		{    
		 	if(getSession().getAttribute("is3to8Selected") == null)
				this.is3to8Selected = (getRequest().getParameter("is3to8Selected") != null && "true".equalsIgnoreCase(getRequest().getParameter("is3to8Selected").toString()))? true: false; 
	    	if(getSession().getAttribute("isEOISelected") == null)
	    		this.isEOISelected = (getRequest().getParameter("isEOISelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isEOISelected").toString()))? true: false;
	    	if(getSession().getAttribute("isUserLinkSelected") == null)
	    		this.isUserLinkSelected = (getRequest().getParameter("isUserLinkSelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isUserLinkSelected").toString()))? true: false;
	    	
	    	/* Changes for DEX Story - Add intermediate screen : Start */
	    	//System.out.println("userName from session in user module >> "+getSession().getAttribute("userName"));
	    	//System.out.println("isDexLogin from session [user module] >> "+getSession().getAttribute("isDexLogin"));
	    	try {
				this.isEOIUser = this.userManagement.isOKEOIUser(getRequest().getUserPrincipal().toString()); //need to check and populate this flag
				this.isMappedWith3_8User = this.userManagement.isMappedWith3_8User(getRequest().getUserPrincipal().toString()); //need to check and populate this flag
				//this.is3to8Selected = (getRequest().getParameter("is3to8Selected") != null && "true".equalsIgnoreCase(getRequest().getParameter("is3to8Selected").toString()))? true: false; 
				//this.isEOISelected = (getRequest().getParameter("isEOISelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isEOISelected").toString()))? true: false;
				//this.isUserLinkSelected = (getRequest().getParameter("isUserLinkSelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isUserLinkSelected").toString()))? true: false;
				if(this.is3to8Selected)
					getSession().setAttribute("is3to8Selected", this.is3to8Selected);
				else if(this.isEOISelected)
					getSession().setAttribute("isEOISelected", this.isEOISelected);
				else if(this.isUserLinkSelected)
					getSession().setAttribute("isUserLinkSelected", this.isUserLinkSelected);
			} catch (CTBBusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/* Changes for DEX Story - Add intermediate screen : End */
			getLoggedInUserPrincipal();
			getUserDetails();
			setupUserPermission();
		}
	
}