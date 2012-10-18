package registrationOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.MessageResourceBundle;
import utils.RequestUtil;
import utils.Base;
import utils.BaseTree;
import utils.BroadcastUtils;
import utils.DateUtils;
import utils.FilterSortPageUtils;
import utils.JsonStudentUtils;
import utils.MessageInfo;
import utils.ModifyManifestVo;
import utils.OptionList;
import utils.Organization;
import utils.OrgnizationComparator;
import utils.PermissionsUtils;
import utils.Row;
import utils.StudentPathListUtils;
import utils.StudentSearchUtils;
import utils.TestSessionUtils;
import utils.TreeData;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.studentManagement.MusicFiles;
import com.ctb.bean.studentManagement.StudentDemographic;
import com.ctb.bean.studentManagement.StudentDemographicValue;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.OrgNodeLicenseInfo;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.StudentAccommodations;
import com.ctb.bean.testAdmin.StudentManifest;
import com.ctb.bean.testAdmin.StudentManifestData;
import com.ctb.bean.testAdmin.StudentSessionStatus;
import com.ctb.bean.testAdmin.TABERecommendedLevel;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestElementData;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.control.db.OrgNode;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.studentManagement.StudentDataCreationException;
import com.ctb.exception.testAdmin.InsufficientLicenseQuantityException;
import com.ctb.util.OperationStatus;
import com.ctb.util.ValidationFailedInfo;
import com.google.gson.Gson;

import dto.GridDropLists;
import dto.Message;
import dto.RapidRegistrationVO;
import dto.StudentAccommodationsDetail;
import dto.StudentProfileInformation;
import dto.TestSessionVO;


@Jpf.Controller()
public class RegistrationOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;

	@org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.BroadcastMessageLog message;
	
	
	@Control()
	private com.ctb.control.studentManagement.StudentManagement studentManagement;
	
	@Control()
	private com.ctb.control.db.OrgNode orgnode;
	
		
    @Control()
    private com.ctb.control.testAdmin.ScheduleTest scheduleTest;
    
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;
    
    @Control()
    private com.ctb.control.db.ItemSet itemSet;
    
    @Control()
    private com.ctb.control.db.TestAdmin admins;
    
    @Control()
    private com.ctb.control.licensing.Licensing licensing;
    
    @Control
	private OrgNode orgNode; 
    

	private String userName = null;
	private Integer customerId = null;
	private User user = null;
	List demographics = null;
	public StudentAccommodationsDetail accommodations = null;

	CustomerConfiguration[] customerConfigurations = null;
	public static String CONTENT_TYPE_JSON = "application/json";
	private static final String ACTION_FIND_STUDENT      = "findStudent";
	private static final String ACTION_ADD_STUDENT       = "addStudent";
	private boolean hasLicenseConfiguration = false;
	private String selectedProductType = "ST";
	
	
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
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="beginStudentRegistration.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "beginStudentRegistration.do")
	})
	protected Forward begin()
	{
		return new Forward("success");
	}
	
	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudentHierarchy.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "student_session_list.jsp")
	})
	protected Forward beginStudentRegistration()
	{
		initialize();
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
            @Jpf.Forward(name = "programStatusLink", path = "assessments_programStatus.do"),
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
	        @Jpf.Forward(name = "viewStatusLink", path = "services_viewStatus.do")
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
	 * @jpf:action
	 */
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
	
	
	/**
	 * @jpf:action
	 */    
	@Jpf.Action()
	protected Forward myProfile()
	{
	    return null;
	}
	
    
    
    @Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward getOrgNodeHierarchyList(){

		String jsonTree = "";
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String contentType = CONTENT_TYPE_JSON;
		if(this.userName == null ) {
			getLoggedInUserPrincipal();		
			getUserDetails();
		}
		try {
			BaseTree baseTree = new BaseTree ();

			ArrayList<Organization> completeOrgNodeList = new ArrayList<Organization>();
			UserNodeData associateNode = StudentPathListUtils.populateAssociateNode(this.userName,this.studentManagement);
			ArrayList<Organization> selectedList  = StudentPathListUtils.buildassoOrgNodehierarchyList(associateNode);
			Collections.sort(selectedList, new OrgnizationComparator());
			Integer leafNodeCategoryId = StudentPathListUtils.getLeafNodeCategoryId(this.userName,this.customerId, this.studentManagement);
			ArrayList <Integer> orgIDList = new ArrayList <Integer>();
			ArrayList<TreeData> data = new ArrayList<TreeData>();

			UserNodeData und = StudentPathListUtils.OrgNodehierarchy(this.userName, 
					this.studentManagement, selectedList.get(0).getOrgNodeId()); 
			ArrayList<Organization> orgNodesList = StudentPathListUtils.buildOrgNodehierarchyList(und, orgIDList,completeOrgNodeList);

			for (int i= 0; i < selectedList.size(); i++) {

				if (i == 0) {

					preTreeProcess (data, orgNodesList, selectedList);

				} else {

					Integer nodeId = selectedList.get (i).getOrgNodeId();
					if (orgIDList.contains(nodeId)) {
						continue;
					} else if (!selectedList.get (i).getIsAssociate()) {
						
						continue;
						
					} else {

						orgIDList = new ArrayList <Integer>();
						UserNodeData undloop = StudentPathListUtils.OrgNodehierarchy(this.userName, 
								this.studentManagement,nodeId);   
						ArrayList<Organization> orgNodesListloop = StudentPathListUtils.buildOrgNodehierarchyList(undloop, orgIDList, completeOrgNodeList);	
						preTreeProcess (data, orgNodesListloop, selectedList);
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
			baseTree.setLeafNodeCategoryId(leafNodeCategoryId);
			jsonTree = gson.toJson(baseTree);
			String pattern = ",\"children\":[]";
			jsonTree = jsonTree.replace(pattern, "");

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
			System.err.println("Exception while processing CR response.");
			e.printStackTrace();
		}

		return null;

	}
    
   
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward populateGridDropDowns(){

		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		if(this.userName == null ) {
			getLoggedInUserPrincipal();		
			getUserDetails();
		}
		
		try {
			
			GridDropLists dropList = new GridDropLists();
			dropList.setGradeOptions(getGradeOptions());
			dropList.setTestCatalogOptions(getTestNameOptions());
			dropList.setUseRole(this.user.getRole().getRoleName().toUpperCase());
			
			try{

				Gson gson = new Gson();
				json = gson.toJson(dropList);
				resp.setContentType("application/json");
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(json.getBytes());

			}

			finally{
				if (stream!=null){
					stream.close();
				}
			}
		} catch (Exception e) {
			System.err.println("Exception while processing populateGridDropDowns.");
			e.printStackTrace();
		}

		return null;

	}
	
	
	
	@Jpf.Action(forwards={
    		@Jpf.Forward(name = "success", 
					path ="")
	})
    protected Forward getSessionForReportingGrid(){ 
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		Integer treeOrgNodeId = Integer.parseInt(getRequest().getParameter("treeOrgNodeId"));
		try {
			
			if(this.userName == null ) {
				getLoggedInUserPrincipal();		
				getUserDetails();
			}
			TestSessionData tsd = new TestSessionData();
			tsd = this.testSessionStatus.getCurrentFutureTestAdminsForOrgNode(this.userName, this.user.getUserId(), treeOrgNodeId);
			
	        Base base = new Base();
			
			if ((tsd != null) && (tsd.getFilteredCount().intValue() > 0)) {
				base = buildTestSessionListForCUPA(tsd, base);
			}
			
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
				
			Gson gson = new Gson();
			json = gson.toJson(base);
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
			System.err.println("Exception while processing getSessionForReportingGrid.");
			e.printStackTrace();
		}
		return null;

	}
	
	@Jpf.Action(forwards={
    		@Jpf.Forward(name = "success", 
					path ="")
	})
    protected Forward getSessionForOrgNodeWithStudentStatus(){ 
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		Integer treeOrgNodeId = Integer.parseInt(getRequest().getParameter("treeOrgNodeId"));
		Integer studentId = Integer.parseInt(getRequest().getParameter("studentId"));
		try {
			
			if(this.userName == null ) {
				getLoggedInUserPrincipal();		
				getUserDetails();
			}
			TestSessionData tsd = new TestSessionData();
			tsd = this.testSessionStatus.getCurrentFutureTestAdminsForOrgNodeWithStudentStatus(this.userName, this.user.getUserId(), treeOrgNodeId, studentId);
			
	        Base base = new Base();
			
			if ((tsd != null) && (tsd.getFilteredCount().intValue() > 0)) {
				base = buildTestSessionList(tsd, base);
			}
			
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
				
			Gson gson = new Gson();
			json = gson.toJson(base);
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
			System.err.println("Exception while processing getSessionForReportingGrid.");
			e.printStackTrace();
		}
		return null;

	}
	
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward getStudentForReportingGrid(){

		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		List<StudentProfileInformation> studentList = new ArrayList<StudentProfileInformation>(0);
		String json = "";
		
		try {
			ManageStudentData msData = findAllStudentsAtAndBelowByHierarchy();
			
			if ((msData != null) && (msData.getFilteredCount().intValue() > 0))
			{
				studentList = StudentSearchUtils.buildStudentListForForReportingGrid(msData);
			}
			Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
	
			Gson gson = new Gson();
			
			base.setStudentProfileInformation(studentList);
			json = gson.toJson(base);
			
			try{

				resp.setContentType(CONTENT_TYPE_JSON);
	    		stream = resp.getOutputStream();

	    		String acceptEncoding = req.getHeader("Accept-Encoding");

	    		if (acceptEncoding != null && acceptEncoding.contains("gzip")) {
	    		    resp.setHeader("Content-Encoding", "gzip");
	    		    stream = new GZIPOutputStream(stream);
	    		}
	    		
				resp.flushBuffer();
	    		stream.write(json.getBytes());

			}

			finally{
				if (stream!=null){
					stream.close();
				}
			}



		} catch (Exception e) {
			System.err.println("Exception while processing getStudentForReportingGrid.");
			e.printStackTrace();
		}

		return null;

	}
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward getStudentCountForOrgNode(){

		OutputStream stream = null;
		Integer treeOrgNodeId = Integer.parseInt(getRequest().getParameter("treeOrgNodeId"));
		HttpServletResponse resp = getResponse();
		try {
			Integer studentCount = 0;
			studentCount = StudentSearchUtils.getStudentsCountForOrgNode(this.userName, this.studentManagement, treeOrgNodeId);
			try {
				Gson gson = new Gson();
				String json = gson.toJson(studentCount);
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
			System.err.println("Exception while retrieving student Count.");
			e.printStackTrace();
		}
		return null;

	}
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward getStudentForSelectedOrgNodeGrid(){
		OutputStream stream = null;
		Integer orgNodeId = Integer.parseInt(getRequest().getParameter("treeOrgNodeId"));
		Integer testAdminId = Integer.parseInt(getRequest().getParameter("testAdminId"));
		HttpServletResponse resp = getResponse();
		try {
			List<StudentProfileInformation> studentlist = getStudentsForSelectedOrgNode(orgNodeId, testAdminId);
			Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
			base.setStudentProfileInformation(studentlist);
			try {
				Gson gson = new Gson();
				String json = gson.toJson(base);
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
			System.err.println("Exception while retrieving student Count.");
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward gotoStudentRegistrationPopup(){

		
		String jsonResponse = "";
		Integer  studentId = new Integer(0);
		studentId = Integer.valueOf(getRequest().getParameter("studentId"));

		
		
		StudentSessionStatus [] scr =  getStudentPopUpDetails(studentId);
		if(scr.length > 0) {
			if(scr[0].getRecommendedProductId() != null && !scr[0].getRecommendedProductId().equals("")
					&& scr[0].getProductId() != null && !scr[0].getProductId().equals("")) {
				//this.recommendedProductId = scr[0].getRecommendedProductId();
				//this.productId = scr[0].getProductId();
			}
		}
		try {
			jsonResponse = JsonStudentUtils.getJson(scr, "studentSessionData",scr.getClass());
			HttpServletResponse resp = this.getResponse();     
			resp.setContentType("application/json");
			resp.flushBuffer();
	        OutputStream stream = resp.getOutputStream();
	        stream.write(jsonResponse.getBytes());
	        stream.close();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;		
	
		
	}

	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward getStudentManifestDetail(){
		 ModifyManifestVo vo = new ModifyManifestVo();
		 HttpServletResponse resp = getResponse();
		 OutputStream stream = null;
		 String locatorSessionInfo = "";
		 Map<Integer, String> allRecomendedLevel =  new HashMap<Integer, String>();
		 try {
			 Integer testAdminId = Integer.parseInt(getRequest().getParameter("testAdminId"));
			 Integer itemSetIdTc = Integer.parseInt(getRequest().getParameter("itemSetIdTc"));
			 Integer studentId = Integer.parseInt(getRequest().getParameter("studentId"));
			 String selectedStudentOrgNodeid = getRequest().getParameter("selectedStudentOrgNodeid");
			 String [] studentOrgNodeidList = selectedStudentOrgNodeid.split("\\|");
			 			 
			 TestSession session = admins.getTestAdminDetails(testAdminId);
			 //Fetching License Information
			 this.selectedProductType = session.getProductType();
			 boolean isLicenseProduct = (this.selectedProductType.equals("TL") || this.selectedProductType.equals("PT")) ? false : true;
			 if(this.hasLicenseConfiguration && isLicenseProduct){
				 List<Row> licenseDataList = new ArrayList<Row>();
				 int index = 0;
				 for (String orgNodeId : studentOrgNodeidList) {
						CustomerLicense[] customerLicenses = getCustomerLicenses(); 
						if ((customerLicenses != null) && (customerLicenses.length > 0)) {
							CustomerLicense cl = customerLicenses[0];
						    OrgNodeLicenseInfo onli = getLicenseQuantitiesByOrg(Integer.parseInt(orgNodeId), cl.getProductId(), cl.getSubtestModel());
						    Node n = this.orgNode.getOrgNodeById(Integer.parseInt(orgNodeId));
						    Integer available = (onli.getLicPurchased() != null) ? onli.getLicPurchased() : new Integer(0);
							Row row = new Row(index);
							String[] cells = new String[5];
							cells[0] = orgNodeId;
							cells[1] = n.getOrgNodeName();
							cells[2] = cl.getSubtestModel();
							cells[3] = String.valueOf(new Integer(0));	
							cells[4] = available.toString();
							row.setCell(cells);			
							licenseDataList.add(row);
							System.out.println("orgNodeId : "+orgNodeId + ", License data: "+licenseDataList.get(index).getCell()[4]);
						}
					index++;				
				 }
				 vo.setLicenseData(licenseDataList);
			 }
			 //TestElement[] allSubtest = this.itemSet.getTestElementByTestAdmin(testAdminId);
			 TestElement [] allSubtest = itemSet.getTestElementsForSession(testAdminId);
			 //ScheduledSession scheduledSession = TestSessionUtils.getTestSessionDataWithoutRoster(scheduleTest, userName, testAdminId);
			 TestElement locatorSubtest = TestSessionUtils.getLocatorSubtest(allSubtest);
			 

			 if(locatorSubtest == null){
				 TestElementData suTed = scheduleTest.getSchedulableUnitsForTest(userName, itemSetIdTc, new Boolean(true), null, null, null);
				 locatorSubtest = TestSessionUtils.getLocatorSubtest(suTed.getTestElements());
			 } 
			 
			 String productType = TestSessionUtils.getProductType(session.getProductType());
			 if(locatorSubtest!=null && !TestSessionUtils.isTabeAdaptiveProduct(productType)){
				 TABERecommendedLevel[] trls = scheduleTest.getTABERecommendedLevelForStudent(userName, studentId, session.getItemSetId(), locatorSubtest.getItemSetId());
				 TestSessionUtils.setRecommendedLevelForSession(allSubtest, trls);
				 for (TestElement testElement : allSubtest) {
					 allRecomendedLevel.put(testElement.getItemSetId(), testElement.getItemSetForm());
				}
				 locatorSessionInfo = TestSessionUtils.getLocatorSessionInfo( allSubtest, trls);
			 }
			 			 
			 vo.populateDefaultTestSession(allSubtest);
			 
			 if(vo.getTestSession().getLocatorSubtest()==null && locatorSubtest!=null){
				 vo.populateLocatorSubtest(locatorSubtest);
			 }

			 vo.populateLevelOptions();
			 vo.populateDefaultManifest(allSubtest);
			 vo.setLocatorSessionInfo(locatorSessionInfo);
			 vo.setRecomendedLevel(allRecomendedLevel);
			 
			 
		 } catch (Exception e) {
			 e.printStackTrace();
		}
		 
	    writeToOutPutStream(resp, vo);
		
		return null;
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
    
    /**
     * getLicenseQuantitiesByOrg
     */    
    private OrgNodeLicenseInfo getLicenseQuantitiesByOrg(Integer orgNodeId, Integer productId, String subtestModel) {
        OrgNodeLicenseInfo onli = null;
        try {
            onli = this.licensing.getLicenseQuantitiesByOrgNodeIdAndProductId(this.userName, 
										                    orgNodeId, 
										                    productId, 
										                    subtestModel);
        }    
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return onli;
    }
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="find_user_by_hierarchy.jsp")
	})
	protected Forward getStudentOptionList(){
		String jsonResponse = "";
		OutputStream stream = null;
		//Boolean isLasLinkCustomer = new Boolean(getRequest().getParameter("isLasLinkCustomer"));
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			/*if(isLasLinkCustomer) {
				getTestPurposeOptions(ACTION_ADD_STUDENT);
		     }*/
			OptionList optionList = new OptionList();
			optionList.setGradeOptions(getGradeOptions(ACTION_ADD_STUDENT));
			optionList.setGenderOptions(getGenderOptions(ACTION_ADD_STUDENT));
			optionList.setMonthOptions( DateUtils.getMonthOptions());
			optionList.setDayOptions(DateUtils.getDayOptions());
			optionList.setYearOptions(DateUtils.getYearOptions());
			/*optionList.setTestPurposeOptions(getTestPurposeOptions(ACTION_ADD_STUDENT));*/
			optionList.setProfileEditable(true);
			
			try {
				Gson gson = new Gson();
				String json = gson.toJson(optionList);
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
		return null;
	}
	
	@Jpf.Action(forwards={
    		@Jpf.Forward(name = "success", 
					path ="")
	})
    protected Forward getRecommendedSessionForReportingGrid(){ 
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		Integer treeOrgNodeId = Integer.parseInt(getRequest().getParameter("treeOrgNodeId"));
		Integer studentId = Integer.parseInt((String)this.getRequest().getParameter("studentId"));
		Integer selectedProductId = Integer.parseInt( getRequest().getParameter("selectedProductId"));

		try {
			
			if(this.userName == null ) {
				getLoggedInUserPrincipal();		
				getUserDetails();
			}
			TestSessionData tsd = TestSessionUtils.getRecommendedTestSessionsForOrgNodeWithStudentStatus(this.userName, this.user.getUserId(), this.testSessionStatus, treeOrgNodeId, selectedProductId, studentId);
	        Base base = new Base();
			
			if ((tsd != null) && (tsd.getFilteredCount().intValue() > 0)) {
				base = buildTestSessionList(tsd, base);
			}
			
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
				
			Gson gson = new Gson();
			json = gson.toJson(base);
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
			System.err.println("Exception while processing getSessionForReportingGrid.");
			e.printStackTrace();
		}
		return null;

	}
	
	
	@Jpf.Action(forwards={
    		@Jpf.Forward(name = "success", 
					path ="")
	})
    protected Forward registerStudent(){

		String jsonData = "";
		OutputStream stream = null;
		HttpServletResponse resp = getResponse();
	    resp.setCharacterEncoding("UTF-8"); 
		OperationStatus status = new OperationStatus();
		RapidRegistrationVO vo = new RapidRegistrationVO();
		vo.setStatus(status);
		//boolean sessionHasDefaultLocatorSubtest = false;
		String orgNodeId = null;
    
		try{
			String testAdminIdString = RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.TEST_ADMIN_ID, true, "-1");
			String studentIdString   = RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.STUDENT_ID, true, "-1");
			String studentOrgNodeIdString   = RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.STUDENT_ORG_NODE_ID, true, "-1");
			Integer testAdminId      = Integer.valueOf(testAdminIdString);
			Integer studentId        = Integer.valueOf(studentIdString);	
			Integer studentOrgNodeId = Integer.valueOf(studentOrgNodeIdString);
			orgNodeId = studentOrgNodeId.toString();
			String[] itemSetIds   = RequestUtil.getValuesFromRequest(this.getRequest(), RequestUtil.TEST_ITEM_SET_ID_TD, true, new String[0]);
			String[] levels       = RequestUtil.getValuesFromRequest(this.getRequest(), RequestUtil.TEST_ITEM_SET_FORM, true, new String[itemSetIds.length]);
			String[] subtestNames = RequestUtil.getValuesFromRequest(this.getRequest(), RequestUtil.SUB_TEST_NAME, true, new String[itemSetIds.length]);
			String autoLocator	  =  RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.HAS_AUTOLOCATOR, true, "false");
			String assessmentHasLocator = RequestUtil.getValueFromRequest(this.getRequest(), "assessmentHasLocator", true, "false");
			vo.setAssessmentHasLocator(new Boolean(assessmentHasLocator));
			int subtestSize       = itemSetIds.length;
			int order             = 0;
			boolean hasAutoLocator = false;
			StudentManifest locatorManifest = null;
			TestProduct tp = scheduleTest.getProductForTestAdmin(this.userName, testAdminId);
			ScheduledSession scheduledSession = TestSessionUtils.getTestSessionDataWithoutRoster(this.scheduleTest, this.userName, testAdminId);
			
			String productType = TestSessionUtils.getProductType(tp.getProductType());
			this.selectedProductType = productType;
		    TestElement [] testElements = scheduledSession.getScheduledUnits();

		    
			if(autoLocator.equalsIgnoreCase("true") ){
				Integer locatorItemSetId = Integer.valueOf(RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.LOCATOR_TEST_ITEM_SET_ID_TD, false, null));
				String locatorItemSetName = RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.LOCATOR_SUB_TEST_NAME, false, null);
				locatorManifest = new StudentManifest(); 
				locatorManifest.setItemSetId(locatorItemSetId);
				locatorManifest.setItemSetOrder(0);
				locatorManifest.setItemSetName(locatorItemSetName);
				subtestSize = subtestSize+1;
				order = order+1;
				hasAutoLocator = true;
			}
			
			 StudentManifest [] manifestArray = new StudentManifest[subtestSize];
			 if(hasAutoLocator){
				 manifestArray[0] = locatorManifest;
			 }
			 
			 for(int ii=0; ii<itemSetIds.length; ii++ ,order++){
				 StudentManifest manifest = new StudentManifest(); 
				 manifest.setItemSetId(Integer.valueOf(itemSetIds[ii]));
				 if(!hasAutoLocator && TestSessionUtils.isTabeProduct(productType)){
					 manifest.setItemSetForm(levels[ii]); 
				 }
				 manifest.setItemSetOrder(order);
				 manifest.setItemSetName(subtestNames[ii]);
				 manifestArray[order]=manifest;
				 manifest.setTestAccessCode(getAccessCode(testElements, Integer.valueOf(itemSetIds[ii]) ));
			 }
			 
			 
			 
			// manifestData.setStudentManifests(manifestArray , new Integer(manifestArray.length));

			 boolean isTabeAdaptiveProduct =  TestSessionUtils.isTabeAdaptiveProduct(productType);
			 if(hasAutoLocator && !TestSessionUtils.isTabeLocatorProduct(productType)){
				 for (StudentManifest studentManifest : manifestArray) {
						studentManifest.setItemSetForm(null);
					}
			 } else  if(TestSessionUtils.isTabeLocatorProduct(productType) || tp.getProductId().intValue() == 4013){
				for (StudentManifest studentManifest : manifestArray) {
					studentManifest.setItemSetForm("1");
				}
			 } else if(isTabeAdaptiveProduct){
				 for (StudentManifest studentManifest : manifestArray) {
						studentManifest.setItemSetForm(null);
					}
			 } else { // tabe other product
				 for (StudentManifest studentManifest : manifestArray) {
					 if(studentManifest.getItemSetForm()==null)
						studentManifest.setItemSetForm("E");
					}
			 }

			 RosterElement roster = new RosterElement(); 
			 List sstList = TestSessionUtils.getStudentSubtests(this.scheduleTest, this.userName, studentId, testAdminId);
			 if (sstList.size() > 0)  {
				 // student already in session, update student roster
	             StudentManifestData smanifestData = new StudentManifestData();
	             smanifestData.setStudentManifests(manifestArray, new Integer(manifestArray.length));
	             roster = TestSessionUtils.updateManifestForRoster(this.scheduleTest, this.userName, studentId, studentOrgNodeId, testAdminId, smanifestData);
			 } else  {
				 // student not in session, add student roster
	             SessionStudent ss = new SessionStudent();
	             ss.setStudentId(studentId);
	             ss.setOrgNodeId(studentOrgNodeId);
	             ss.setStudentManifests(manifestArray);
	             roster = TestSessionUtils.addStudentToSession(this.scheduleTest, this.userName, ss, testAdminId);
	         }
			 status.setSuccess(true);
			 prepareResultVo(vo, roster, studentId, testAdminId, studentOrgNodeId, productType, hasAutoLocator);

		}
		catch (InsufficientLicenseQuantityException e)
        {
            e.printStackTrace();
            status.setSuccess(false);
            status.setLicenseError(true);  
			try {
				boolean isLicenseProduct = (this.selectedProductType.equals("TL") || this.selectedProductType.equals("PT")) ? false : true;
				if(this.hasLicenseConfiguration && isLicenseProduct){
					List<Row> licenseDataList = new ArrayList<Row>();
					int index = 0;
					CustomerLicense[] customerLicenses = getCustomerLicenses(); 
					if ((customerLicenses != null) && (customerLicenses.length > 0)) {
						CustomerLicense cl = customerLicenses[0];
					    OrgNodeLicenseInfo onli = getLicenseQuantitiesByOrg(Integer.parseInt(orgNodeId), cl.getProductId(), cl.getSubtestModel());
					    Node n = this.orgNode.getOrgNodeById(Integer.parseInt(orgNodeId));
					    Integer available = (onli.getLicPurchased() != null) ? onli.getLicPurchased() : new Integer(0);
						Row row = new Row(index);
						String[] cells = new String[5];
						cells[0] = orgNodeId;
						cells[1] = n.getOrgNodeName();
						cells[2] = cl.getSubtestModel();
						cells[3] = String.valueOf(new Integer(0));	
						cells[4] = available.toString();
						row.setCell(cells);			
						licenseDataList.add(row);
					}
					System.out.println("orgNodeId : "+orgNodeId + ", License available: "+licenseDataList.get(0).getCell()[4]);
					vo.setLicenseData(licenseDataList);
				}						
			} catch (Exception ex) {
				// TODO: handle exception
				 ex.printStackTrace();
			}			   
          
        }
		catch(Exception e) {
			 e.printStackTrace();
			 status.setSuccess(false);
			 status.setLicenseError(false);
		}
		
		Gson gson = new Gson();
		jsonData = gson.toJson(vo);
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
	
	
	
	private void prepareResultVo(RapidRegistrationVO vo, RosterElement roster, Integer studentId , Integer testAdminId,
			Integer studentOrgNodeId, String productType, boolean hasAutoLocator) throws CTBBusinessException {
		List  selectedSubtest = new ArrayList();
		ScheduledSession scheduledSession = TestSessionUtils.getTestSessionDataWithoutRoster(this.scheduleTest, this.userName, testAdminId);
		ManageStudent student = studentManagement.getManageStudent(userName, studentId);  
		StudentManifestData  smd =  this.scheduleTest.getManifestForRoster(this.userName,studentId,testAdminId,null,null,null);
		StudentManifest[] studentManifest= smd.getStudentManifests();
		
		TestSession testSession = scheduledSession.getTestSession();
		TestElement[] testElements = scheduledSession.getScheduledUnits();
		TestElement locatorSubtest = TestSessionUtils.getLocatorSubtest(testElements); 
		StudentManifest locatorManifest = null;
		
		 
		 for (StudentManifest manifest : studentManifest) {
			 for (TestElement testElement : testElements) {
				 if(manifest.getItemSetId().intValue() == testElement.getItemSetId().intValue()){
					 manifest.setTestAccessCode(testElement.getAccessCode());
					 break;
				 }
			}
			 if(locatorSubtest!=null && locatorSubtest.getItemSetId().intValue() == manifest.getItemSetId().intValue()){
				 locatorManifest = manifest;
			 } else {
				 selectedSubtest.add(manifest);
			 }
			 
		}
		
		if(hasAutoLocator){
			
		}
		
		 
		 String startDate = DateUtils.formatDateToDateString(testSession.getLoginStartDate());
	     String endDate = DateUtils.formatDateToDateString(testSession.getLoginEndDate());
	     String startTime = DateUtils.formatDateToTimeString(testSession.getDailyLoginStartTime());
	     String endTime = DateUtils.formatDateToTimeString(testSession.getDailyLoginEndTime());
	     String enforceBreak = testSession.getEnforceBreak(); 
	     List selectedProctors = new ArrayList();
	     User [] proctors = scheduledSession.getProctors();
	     for (int i=0; i < proctors.length; i++)  {
	            User user = proctors[i];
	            selectedProctors.add(user);
	        }

		 vo.setStudentId(studentId.toString());
		 vo.setStudentName(student.getStudentName());
		 vo.setLoginName(student.getLoginId());
		 vo.setTestAdminId(testSession.getTestAdminId().toString());
		 vo.setStudentOrgId(studentOrgNodeId.toString());
		 vo.setPassword(roster.getPassword());
		 vo.setTestName(testSession.getTestName());
		 vo.setTestAdminName(testSession.getTestAdminName());
		 vo.setTestAccessCode(testElements[0].getAccessCode());
		 vo.setSessionNumber(testSession.getSessionNumber());
		 vo.setCreatorOrgNodeName(testSession.getCreatorOrgNodeName());
		 vo.setStartDate(startDate);
		 vo.setEndDate(endDate);
		 vo.setStartTime(startTime);
		 vo.setEndTime(endTime);
		 vo.setShowAccessCode(customerHasAccessCode(testSession.getTestAdminId()));
		 vo.setLocatorTest(TestSessionUtils.isTabeLocatorProduct(productType));
		 if(TestSessionUtils.isTabeLocatorProduct(productType) || hasAutoLocator) {
			 vo.setAutoLocator(true);
			 vo.setAutoLocatorDisplay("Yes");
		 } else {
			 vo.setAutoLocator(false);
			 vo.setAutoLocatorDisplay("No");
		 }
		 if((enforceBreak != null) && (enforceBreak.equalsIgnoreCase("T"))){
			 vo.setEnforceBreak("Yes") ;
		 } else {
			 vo.setEnforceBreak("No") ;
		 }
		 
		
			 
		 vo.setSelectedProctors(selectedProctors);
		 vo.setLocatorSubtest(locatorSubtest);
		 vo.setSelectedSubtests(selectedSubtest);
		
	}

	private String getAccessCode(TestElement[] testElements, Integer valueOf) {
		String selectedAccessCode = "";
		for (TestElement testElement : testElements) {
			if(testElement.getItemSetId().intValue()== valueOf.intValue()){
				selectedAccessCode = testElement.getAccessCode();
				break;
			}
		}
		return selectedAccessCode;
	}

	private Boolean customerHasAccessCode(Integer testAdminId)
	{               
	    
	    boolean hasAccessCodeConfigurable = false;
	    String hasBreak = "T";
	    for (int i=0; i < customerConfigurations.length; i++)
	    {
	    	 CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
	        if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Print_Accesscode") && 
	        		cc.getDefaultValue().equals("T")	) {
	        	hasAccessCodeConfigurable = true;
	            break;
	        } 
	    }
	    
	    try {
	    	hasBreak = this.studentManagement.hasMultipleAccessCode(testAdminId);
	    	
	    	if(hasBreak.equals("F") && hasAccessCodeConfigurable)
	    		hasAccessCodeConfigurable = true;
	    	else
	    		hasAccessCodeConfigurable = false;
	    }
	    catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
	   
	    return new Boolean(hasAccessCodeConfigurable);
	}

	private void initialize()
	{     
		getLoggedInUserPrincipal();
		getUserDetails();
		setupUserPermission();
		/* rapid registration start */
		//StudentOperationForm  form = new StudentOperationForm();
		demographics = null;
		accommodations = null;
		addEditDemographics(new Integer(0), null);
		addEditAccommodations(customerConfigurations, null); 
		this.getRequest().setAttribute("viewOnly", Boolean.FALSE); 
		try{
			MusicFiles[] musicList = this.studentManagement.getMusicFiles();	
			this.getRequest().setAttribute("musicList", musicList);
		}catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		/* rapid registration end */
		
		//List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
        //this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
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
    /**
	 * getUserDetails
	 */
	private void getUserDetails()
	{
		try
		{	if(this.userName != null ) {
			this.user = this.studentManagement.getUserDetails(this.userName, this.userName);     
			this.customerId = user.getCustomer().getCustomerId();
			Customer customer = this.user.getCustomer();
			Boolean supportAccommodations = Boolean.TRUE;   
            String hideAccommodations = customer.getHideAccommodations();
            if ((hideAccommodations != null) && hideAccommodations.equalsIgnoreCase("T"))
            {
                supportAccommodations = Boolean.FALSE;
            }
            this.getRequest().setAttribute("supportAccommodations", supportAccommodations); 
            
            
		}
		}
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
		}        
		getSession().setAttribute("userName", this.userName);
		getSession().setAttribute("createdBy", this.user.getUserId());
	}
private void setUpAllUserPermission(CustomerConfiguration [] customerConfigurations) {
    	
    	boolean hasBulkStudentConfigurable = false;
    	boolean hasBulkStudentMoveConfigurable = false;
    	boolean hasOOSConfigurable = false;
    	boolean adminUser = isAdminUser();
    	boolean hasUploadDownloadConfig = false;
    	boolean hasProgramStatusConfig = false;
    	boolean hasScoringConfigurable = false;
    	//boolean hasLicenseConfiguration= false;
    	boolean TABECustomer = false;
    	boolean adminCoordinatorUser = isAdminCoordinatotUser(); //For Student Registration
    	boolean mandatoryBirthdateValue = true;
    	boolean multiOrgAssociationValid = true;
    	boolean hasResetTestSession = false;
    	boolean isOKCustomer = false;
    	boolean isGACustomer = false;
    	boolean isTopLevelAdmin = new Boolean(isTopLevelUser() && isAdminUser());
    	boolean laslinkCustomer = false;
    	    	
    	String roleName = this.user.getRole().getRoleName();
    	
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
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
				// For Upload Download
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
				//For License
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subscription") && 
	            		cc.getDefaultValue().equals("T")	) {
					this.hasLicenseConfiguration = true;
					continue;
	            }
				// For TABE Customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer")) {
	            	TABECustomer = true;
	            	continue;
	            }
		
				//For Date Of Birth
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Disable_Mandatory_Birth_Date") && 
						cc.getDefaultValue().equalsIgnoreCase("T")) {
					mandatoryBirthdateValue = false;
					continue;
				}
				//For multi org node association
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Class_Reassignment") && 
						cc.getDefaultValue().equalsIgnoreCase("T")) {
					multiOrgAssociationValid = false;
					continue;
				}
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
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")) {
	            	laslinkCustomer = true;
	            	continue;
	            }
			}
			
		}
		this.getSession().setAttribute("isBulkAccommodationConfigured",new Boolean(hasBulkStudentConfigurable));
		this.getSession().setAttribute("isBulkMoveConfigured",new Boolean(hasBulkStudentMoveConfigurable));
		this.getSession().setAttribute("isOOSConfigured",new Boolean(hasOOSConfigurable));
		this.getSession().setAttribute("hasUploadDownloadConfigured",new Boolean(hasUploadDownloadConfig && adminUser));
		this.getSession().setAttribute("hasProgramStatusConfigured",new Boolean(hasProgramStatusConfig && adminUser));
		this.getSession().setAttribute("hasScoringConfigured",new Boolean(hasScoringConfigurable));
		this.getSession().setAttribute("hasLicenseConfigured",new Boolean(this.hasLicenseConfiguration && adminUser));
		this.getSession().setAttribute("adminUser", new Boolean(adminUser));
		boolean validUser = (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || 
        		roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
		this.getSession().setAttribute("canRegisterStudent", new Boolean(TABECustomer && validUser));
		this.getSession().setAttribute("hasRapidRagistrationConfigured", new Boolean(TABECustomer && (adminUser || adminCoordinatorUser) ));//For Student Registration
		this.getRequest().setAttribute("isMandatoryBirthDate", mandatoryBirthdateValue);
		this.getSession().setAttribute("isClassReassignable",multiOrgAssociationValid);
		this.getSession().setAttribute("hasResetTestSession", new Boolean(hasResetTestSession && ((isOKCustomer && isTopLevelAdmin)||(laslinkCustomer && isTopLevelAdmin)||(isGACustomer && adminUser))));		
		this.getRequest().setAttribute("isLasLinkCustomer", laslinkCustomer);
     	this.getSession().setAttribute("showDataExportTab",laslinkCustomer);
		
    }

	private boolean isAdminCoordinatotUser() //For Student Registration
	{               
		String roleName = this.user.getRole().getRoleName();        
		return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR); 
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
	
	private void setupUserPermission()
	{
		customerConfigurations = getCustomerConfigurations();

        boolean laslinkCustomer = isLaslinkCustomer(customerConfigurations);
        
        this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));
        
        this.getRequest().setAttribute("isLasLinkCustomer", laslinkCustomer);  
    	
    	this.getRequest().setAttribute("isTopLevelUser",isTopLevelUser(laslinkCustomer));
    	
    	getConfigStudentLabel(customerConfigurations);
		
		this.getRequest().setAttribute("customerConfigurations", customerConfigurations);
		
		setUpAllUserPermission(customerConfigurations);
   }
	private boolean isTopLevelUser(boolean isLasLinkCustomerVal){

		boolean isUserTopLevel = false;
		boolean isLaslinkUserTopLevel = false;
		boolean isLaslinkUser = false;
		isLaslinkUser = isLasLinkCustomerVal;
		try {
			if(isLaslinkUser) {
				isUserTopLevel = orgnode.checkTopOrgNodeUser(this.userName);	
				if(isUserTopLevel){
					isLaslinkUserTopLevel = true;				
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return isLaslinkUserTopLevel;
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
	/**
	 * getCustomerConfigurations
	 */
	private CustomerConfiguration[] getCustomerConfigurations()
	{
		CustomerConfiguration[] customerConfigurations = null;
		try {
			customerConfigurations = this.studentManagement.getCustomerConfigurations(this.userName, this.customerId);
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		return customerConfigurations;
	}
	 private boolean isAdminUser() 
	    {               
	        String roleName = this.user.getRole().getRoleName();        
	        return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR); 
	    }
	 
	 /*
		 * 
		 * this method retrieve CustomerConfigurationsValue for provided customer configuration Id.
		 */
		private CustomerConfigurationValue[] customerConfigurationValues(Integer configId)
		{	
			CustomerConfigurationValue[] customerConfigurationsValue = null;
			try {
				customerConfigurationsValue = this.studentManagement.getCustomerConfigurationsValue(configId);

			}
			catch (CTBBusinessException be) {
				be.printStackTrace();
			}
			return customerConfigurationsValue;
		}
	
	/**
	 * userHasReports
	 */
	private Boolean userHasReports() 
	{
		boolean hasReports = false;
		try
		{      
			Customer customer = this.user.getCustomer();
			Integer customerId = customer.getCustomerId();   
			hasReports = this.studentManagement.userHasReports(this.userName, customerId);
		}
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
		}        
		return new Boolean(hasReports);           
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
		this.getRequest().setAttribute("studentIdLabelName",valueForStudentId[0]);
		
	}
	
	/**
	 * getGradeOptions
	 */
	private String[] getGradeOptions()
	{
		String[] grades = null;
		try {
			grades =  this.studentManagement.getGradesForCustomer(this.userName, this.customerId);
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}

		List<String> options = new ArrayList<String>();
		for (int i=0 ; i<grades.length ; i++) {        
			options.add(grades[i]);
		}

		return (String [])options.toArray(new String[0]);        
	}
	
	
	
	 private static void preTreeProcess (ArrayList<TreeData> data,ArrayList<Organization> orgList, ArrayList<Organization> selectedList) {

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
	
	/**
	 * getTestNameOptions
	 */
	private String [] getTestNameOptions()
	{
		String[] testNameOptions = null;
		try {
			 testNameOptions = this.scheduleTest.getTestCatalogForUserForScoring(this.userName);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}

		return testNameOptions;
		         
	  }
	
	private Base buildTestSessionListForCUPA(TestSessionData tsd, Base base) 
    {
        List<TestSessionVO> sessionListCUPA = new ArrayList<TestSessionVO>();        
        TestSession[] testsessions = tsd.getTestSessions();
        
        for (int i=0; i < testsessions.length; i++)
        {
            TestSession ts = testsessions[i];
            if (ts != null) {
	        	TestSessionVO vo = new TestSessionVO(ts);
            	sessionListCUPA.add(vo);
            	}
        }
        base.setTestSessionCUPA(sessionListCUPA);
        
        return base;
    }
	
	private Base buildTestSessionList(TestSessionData tsd, Base base) 
    {
        List<TestSessionVO> sessionList = new ArrayList<TestSessionVO>();        
        TestSession[] testsessions = tsd.getTestSessions();
        
        for (int i=0; i < testsessions.length; i++)
        {
            TestSession ts = testsessions[i];
            if (ts != null) {
	        	TestSessionVO vo = new TestSessionVO(ts);
            	sessionList.add(vo);
            	}
        }
        base.setTestSession(sessionList);
        
        return base;
    }
	
	/**
	 * findByHierarchy
	 */
	private ManageStudentData findAllStudentsAtAndBelowByHierarchy()
	{      
		String treeOrgNodeId = getRequest().getParameter("treeOrgNodeId");
		Integer selectedOrgNodeId = null;
		if(treeOrgNodeId != null)
			selectedOrgNodeId = Integer.parseInt(treeOrgNodeId);
		ManageStudentData msData = null;

		if (selectedOrgNodeId != null)
		{
			msData = StudentSearchUtils.searchAllStudentsAtAndBelowByOrgNode(this.userName, this.studentManagement, selectedOrgNodeId);

		}

		return msData;
	}
	
	private ArrayList<StudentProfileInformation> getStudentsForSelectedOrgNode(Integer orgNodeId, Integer testAdminId) {
		String treeOrgNodeId = getRequest().getParameter("treeOrgNodeId");
		Integer selectedOrgNodeId = null;
		if(treeOrgNodeId != null)
			selectedOrgNodeId = Integer.parseInt(treeOrgNodeId);
		ManageStudentData msData = null;
		ArrayList<StudentProfileInformation> studentList = new ArrayList<StudentProfileInformation>();

		FilterParams filter = null;
		PageParams page = null;
		SortParams sort = null;

		if (selectedOrgNodeId != null)
		{
			studentList = StudentSearchUtils.getStudentsForSelectedOrgNode(this.userName, this.studentManagement, orgNodeId,testAdminId );

		}

		return studentList;
	
	}
	
	private StudentSessionStatus[] getStudentPopUpDetails(Integer studentId){

    	StudentSessionStatus [] StudentSessionStatus = null;
    	try {	
    		StudentSessionStatus = this.studentManagement.getStudentMostResentSessionDetail(studentId);
    	}
    	catch(CTBBusinessException be){
    		be.printStackTrace();
    	}
    	return StudentSessionStatus;
    }
	
	
	/**
	 * getGradeOptions
	 */
	private String [] getGradeOptions(String action)
	{
		String[] grades = null;
		try {
			grades =  this.studentManagement.getGradesForCustomer(this.userName, this.customerId);
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}

		List options = new ArrayList();
		if ( action.equals(ACTION_FIND_STUDENT) )
			options.add(FilterSortPageUtils.FILTERTYPE_ANY_GRADE);
		if ( action.equals(ACTION_ADD_STUDENT))
			options.add(FilterSortPageUtils.FILTERTYPE_SELECT_A_GRADE);

		for (int i=0 ; i<grades.length ; i++) {        
			options.add(grades[i]);
		}

		return (String [])options.toArray(new String[0]);        
	}

	/**
	 * getGenderOptions
	 */
	private String [] getGenderOptions(String action)
	{
		List options = new ArrayList();
		if ( action.equals(ACTION_FIND_STUDENT) )
			options.add(FilterSortPageUtils.FILTERTYPE_ANY_GENDER);
		if ( action.equals(ACTION_ADD_STUDENT))
			options.add(FilterSortPageUtils.FILTERTYPE_SELECT_A_GENDER);

		options.add("Male");
		options.add("Female");
		options.add("Unknown");

		return (String [])options.toArray(new String[0]);        
	}
	
	/**
	 * addEditDemographics
	 */
	private List addEditDemographics(Integer studentId, Integer createBy)
	{	
		//List demographics = null;
		boolean studentImported = false;
		 if(createBy != null)
			 studentImported = (createBy.intValue() == 1);
		
		
		if ((this.demographics == null) && (studentId != null))
		{
			demographics = getStudentDemographics(studentId, demographics);
			prepareOnNullRule(demographics);            
		}
		else
		{
			if (studentImported)
			{        
				prepareStudentDemographicForCustomerConfiguration();
			}
			getStudentDemographicsFromRequest();
		}
		//this.demographics = demographics;
		this.getRequest().setAttribute("demographics", demographics);       
		this.getRequest().setAttribute("studentImported", new Boolean(studentImported));  
		
		return demographics;
	}
	
	/**
	 * getStudentDemographics
	 */
	private List getStudentDemographics(Integer studentId, List demographics)
	{
		demographics = new ArrayList();
		try
		{
			if ((studentId != null) && (studentId.intValue() == 0))
				studentId = null;

			StudentDemographic[] studentDemoList = this.studentManagement.getStudentDemographics(this.userName, this.customerId, studentId, false);

			if (studentDemoList != null)
			{
				for (int i=0; i < studentDemoList.length; i++)
				{
					StudentDemographic sd = studentDemoList[i];
					demographics.add(sd);                
				}                        
			}
		}
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
		}

		return demographics;
	}
	/**
	 * prepareOnNullRule
	 */
	private void prepareOnNullRule( List demographics) 
	{
		for (int i=0; i < demographics.size(); i++)
		{
			StudentDemographic sdd = (StudentDemographic)demographics.get(i);
			if (sdd.getImportEditable().equals("ON_NULL_RULE"))
			{
				StudentDemographicValue[] values = sdd.getStudentDemographicValues();		    
				boolean hasValue = false;
				for (int j=0; j < values.length; j++)
				{
					StudentDemographicValue value = values[j];
					if ((value.getSelectedFlag() != null) && value.getSelectedFlag().equals("true"))
						hasValue = true;
				}
				if (hasValue)
				{
					sdd.setImportEditable("UNEDITABLE_ON_NULL_RULE");
				}
			}
		}
	}
	
	/**
	 * prepareStudentDemographicForCustomerConfiguration
	 */
	private void prepareStudentDemographicForCustomerConfiguration() 
	{
		for (int i=0; i < this.demographics.size(); i++)
		{
			StudentDemographic sdd = (StudentDemographic)this.demographics.get(i);
			if (sdd.getImportEditable().equals("ON_NULL_RULE") || sdd.getImportEditable().equals("UNEDITABLE_ON_NULL_RULE") || sdd.getImportEditable().equals("F"))
			{            
				StudentDemographicValue[] values = sdd.getStudentDemographicValues();		    
				for (int j=0; j < values.length; j++)
				{
					StudentDemographicValue value = (StudentDemographicValue)values[j];
					if ((value.getSelectedFlag() != null) && value.getSelectedFlag().equals("true"))
					{
						value.setVisible("false");            
					}
				}
			}
		}
	}
	
	/**
	 * getStudentDemographicsFromRequest
	 */
	private void getStudentDemographicsFromRequest() 
	{
		String param = null, paramValue = null;
		if(this.demographics == null)
			addEditDemographics(new Integer(0), null);
		
		for (int i=0; i < this.demographics.size(); i++)
		{
			StudentDemographic sdd = (StudentDemographic)this.demographics.get(i);
			StudentDemographicValue[] values = sdd.getStudentDemographicValues();

			for (int j=0; j < values.length; j++)
			{
				StudentDemographicValue sdv = (StudentDemographicValue)values[j];

				// Look up the parameter based on checkbox vs radio/select
				if (sdd.getMultipleAllowedFlag().equals("true"))
				{
					if (! sdv.getVisible().equals("false"))
						sdv.setSelectedFlag("false");
					param = sdd.getLabelName() + "_" + sdv.getValueName();
					if (getRequest().getParameter(param) != null)
					{
						paramValue = getRequest().getParameter(param);
						sdv.setSelectedFlag("true");
					}
				} 
				else
				{
					if (values.length == 1)
					{
						if (! sdv.getVisible().equals("false"))
							sdv.setSelectedFlag("false");
						param = sdd.getLabelName() + "_" + sdv.getValueName();
						if (getRequest().getParameter(param) != null)
						{
							paramValue = getRequest().getParameter(param);
							sdv.setSelectedFlag("true");
						}
					}
					else
					{
						param = sdd.getLabelName();
						if (getRequest().getParameter(param) != null)
						{
							paramValue = getRequest().getParameter(param);

							for (int k=0; k < values.length; k++)
							{
								StudentDemographicValue sdv1 = (StudentDemographicValue)values[k];
								if (! sdv1.getVisible().equals("false"))
									sdv1.setSelectedFlag("false");
								if (!paramValue.equalsIgnoreCase("None") && !paramValue.equalsIgnoreCase("Please Select"))
								{
									if (paramValue.equals(sdv1.getValueName()))
									{
										sdv1.setSelectedFlag("true");
									}
								}
							}

							break;
						}
					}
				}
				sdv.setVisible("T");
			}
		}
	}
	
	/**
	 * addEditAccommodations
	 */
	private StudentAccommodationsDetail addEditAccommodations(CustomerConfiguration[] customerConfigurations, Integer createBy)
	{
		Integer studentId = 0;
		boolean studentImported  = false;
		 if(createBy != null)
			 studentImported = (createBy.intValue() == 1);
		 
		if (accommodations == null)
		{
			accommodations = getStudentAccommodations(studentId, customerConfigurations);
		}
		else
		{
			getStudentAccommodationsFromRequest(customerConfigurations, createBy);
		}
		this.getRequest().setAttribute("accommodations", this.accommodations);   
        
		
		return accommodations;
	}
	
	/**
	 * getStudentAccommodations
	 */
	private StudentAccommodationsDetail getStudentAccommodations(Integer studentId, CustomerConfiguration[] customerConfigurations)
	{
		StudentAccommodationsDetail accommodations = new StudentAccommodationsDetail();

		 if ((studentId != null) && (studentId.intValue() > 0))
		{
			try
			{    
				StudentAccommodations sa = this.studentManagement.getStudentAccommodations(this.userName, studentId);
				accommodations = new StudentAccommodationsDetail(sa);
			}
			catch (CTBBusinessException be)
			{
				be.printStackTrace();
			} 
		}
		else
		{
			setCustomerAccommodations(accommodations, true, customerConfigurations);
		}

	  accommodations.convertHexToText();


		return accommodations;
	}
	
	/**
	 * getStudentAccommodationsFromRequest
	 */
	private void getStudentAccommodationsFromRequest(CustomerConfiguration[]  customerConfigurations , Integer createBy) 
	{
		if(this.accommodations == null)
			addEditAccommodations(customerConfigurations, createBy);
		
		// first get values from request
		String screenReader = getRequest().getParameter("screenReader");
		String calculator = getRequest().getParameter("calculator");
		String highlighter = getRequest().getParameter("highlighter");
		String testPause = getRequest().getParameter("testPause");
		String untimedTest = getRequest().getParameter("untimedTest");
		String colorFont = getRequest().getParameter("colorFont");
		String maskingRuler = getRequest().getParameter("MaskingRuler"); //Added for Masking Ruler
		String auditoryCalming = getRequest().getParameter("AuditoryCalming"); //Added for Auditory Calming
		String magnifyingGlass = getRequest().getParameter("MagnifyingGlass"); //Added for Magnifying Glass
		String extendedTime = getRequest().getParameter("ExtendedTime"); //Added for Student Pacing
		String maskingTool = getRequest().getParameter("MaskingTool"); // Added for Masking Answers

		this.accommodations.setScreenReader(new Boolean(screenReader != null));
		this.accommodations.setCalculator(new Boolean(calculator != null));
		this.accommodations.setHighlighter(new Boolean(highlighter != null));
		this.accommodations.setTestPause(new Boolean(testPause != null));
		this.accommodations.setUntimedTest(new Boolean(untimedTest != null));
		this.accommodations.setColorFont(new Boolean(colorFont != null));
		this.accommodations.setAuditoryCalming(new Boolean(auditoryCalming != null));//Added for Auditory Calming
		this.accommodations.setMaskingRuler(new Boolean(maskingRuler != null));//Added for Masking Ruler
		this.accommodations.setMagnifyingGlass(new Boolean(magnifyingGlass != null));//Added for Magnifying Glass
		this.accommodations.setExtendedTime(new Boolean(extendedTime != null)); //Added for Student Pacing
		this.accommodations.setMaskingTool(new Boolean(maskingTool != null)); // Added for Masking Answers
		
		setCustomerAccommodations(this.accommodations, false, customerConfigurations);

		String question_bgrdColor = this.getRequest().getParameter("question_bgrdColor");
		if (question_bgrdColor != null)
		{
			this.accommodations.setQuestion_bgrdColor(question_bgrdColor);
		}

		String question_fontColor = this.getRequest().getParameter("question_fontColor");
		if (question_fontColor != null)
		{
			this.accommodations.setQuestion_fontColor(question_fontColor);
		}

		String answer_bgrdColor = this.getRequest().getParameter("answer_bgrdColor");
		if (answer_bgrdColor != null)
		{
			this.accommodations.setAnswer_bgrdColor(answer_bgrdColor);
		}

		String answer_fontColor = this.getRequest().getParameter("answer_fontColor");
		if (answer_fontColor != null)
		{
			this.accommodations.setAnswer_fontColor(answer_fontColor);
		}

		String fontSize = this.getRequest().getParameter("fontSize");
		if (fontSize != null)
		{
			this.accommodations.setFontSize(fontSize);
		}
		//Added for music files of Auditory Calming
		if(this.accommodations.getAuditoryCalming()){
			String selecteMusicFile = this.getRequest().getParameter("music_files");
			if(selecteMusicFile.equals("")) {
				selecteMusicFile = "1";
			}
			Integer musicFiles = Integer.parseInt(selecteMusicFile);
			if (musicFiles != null)
			{
				this.accommodations.setMusic_files(musicFiles);
			}
		}
	}
	
	/**
	 * setCustomerAccommodations
	 */
	private void setCustomerAccommodations(StudentAccommodationsDetail sad, boolean isSetDefaultValue,  CustomerConfiguration[] customerConfigurations) 
	{	   
		// set checked value if there is configuration for this customer
		  for (int i=0; i < customerConfigurations.length; i++)
		  {
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
			String ccName = cc.getCustomerConfigurationName();
			String defaultValue = cc.getDefaultValue() != null ? cc.getDefaultValue() : "F";
			String editable = cc.getEditable() != null ? cc.getEditable() : "F";

			if (isSetDefaultValue)
				editable = "F";

			if (defaultValue.equalsIgnoreCase("T") && editable.equalsIgnoreCase("F"))
			{

				if (ccName.equalsIgnoreCase("screen_reader"))
				{
					sad.setScreenReader(Boolean.TRUE);
				}

				if (ccName.equalsIgnoreCase("calculator"))
				{
					sad.setCalculator(Boolean.TRUE);
				}

				if (ccName.equalsIgnoreCase("test_pause"))
				{
					sad.setTestPause(Boolean.TRUE);
				}

				if (ccName.equalsIgnoreCase("untimed_test"))
				{
					sad.setUntimedTest(Boolean.TRUE);
				}

				if (ccName.equalsIgnoreCase("highlighter"))
				{
					sad.setHighlighter(Boolean.TRUE);
				}
				//Added for Masking Ruler
				if (ccName.equalsIgnoreCase("Masking_Ruler"))
				{
					sad.setMaskingRuler(Boolean.TRUE);
				}
				//Added for Auditory Calming
				if (ccName.equalsIgnoreCase("Auditory_Calming"))
				{
					sad.setAuditoryCalming(Boolean.TRUE);
				}
				//Added for Magnifying Glass
				if (ccName.equalsIgnoreCase("Magnifying_Glass"))
				{
					sad.setMagnifyingGlass(Boolean.TRUE);
				}
				//Added for student pacing
				if (ccName.equalsIgnoreCase("Extended_Time"))
				{
					sad.setExtendedTime(Boolean.TRUE);
				}
				//Added for Masking Answers
				if (ccName.equalsIgnoreCase("Masking_Tool"))
				{
					sad.setMaskingTool(Boolean.TRUE);
				}
			}
		}
}
	
		
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="find_user_by_hierarchy.jsp")
	})
	protected Forward saveAddEditStudent()
	{   
		String jsonResponse = "";
		OutputStream stream = null;
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		
		StudentProfileInformation studentProfile = null;
		studentProfile = new StudentProfileInformation();
		studentProfile.setFirstName(getRequest().getParameter("studentRegFirstName"));
		studentProfile.setMiddleName(getRequest().getParameter("studentRegMiddleName"));
		studentProfile.setLastName(getRequest().getParameter("studentRegLastName"));
		studentProfile.setUserName(getRequest().getParameter("loginId"));
		studentProfile.setMonth(getRequest().getParameter("monthOptions"));
		studentProfile.setDay(getRequest().getParameter("dayOptions"));
		studentProfile.setYear(getRequest().getParameter("yearOptions"));
		studentProfile.setGender(getRequest().getParameter("genderOptions"));
		studentProfile.setGrade(getRequest().getParameter("gradeOptions"));
		studentProfile.setTestPurpose("");
		studentProfile.setStudentNumber(getRequest().getParameter("studentRegExtPin1"));
		studentProfile.setStudentSecondNumber("");
		boolean studentIdConfigurable = new Boolean("");
		String studentIdLabelName = "";
		String assignedOrgNodeIds = getRequest().getParameter("assignedOrgNodeIds");
		String[] assignedOrgNodeId = assignedOrgNodeIds.split(",");
		List <Integer> selectedOrgNodes = new ArrayList <Integer>(assignedOrgNodeId.length);
		for (int i = assignedOrgNodeId.length - 1; i >= 0; i--) {
			selectedOrgNodes.add( new Integer(assignedOrgNodeId[i].trim()));
		}
		//selectedOrgNodes.add(118641);
		MessageInfo messageInfo = new MessageInfo();
		Integer studentId = new Integer(0);
		String studentIdVal = getRequest().getParameter("selectedStudentId");
		if(studentIdVal != "" && studentIdVal !=null)
			studentId = Integer.parseInt(studentIdVal);
		String isAddStudent = getRequest().getParameter("isAddStudent");
		boolean isCreateNew = (isAddStudent == null || isAddStudent.equals("") || isAddStudent.equals("true")) ? true : false;
		Integer createBy = new Integer(0);
		String createByVal = getRequest().getParameter("createBy");
		if(!createByVal.equals("") && createByVal != null)
			createBy = Integer.parseInt(createByVal);
		studentProfile.setCreateBy(createBy);
		
		boolean result = true;
		
			if (result) {
				if (isValidationForUniqueStudentIDRequired(studentProfile, customerConfigurations)) {
					result = validateUniqueStudentId(isCreateNew, null, studentProfile);
					if (!result) {
						String messageTitle = studentIdConfigurable ? Message.VALIDATE_STUDENT_ID_TITLE
								.replace("<#studentId#>", studentIdLabelName)
								: Message.VALIDATE_STUDENT_ID_TITLE.replace(
										"<#studentId#>",
										Message.DEFAULT_STUDENT_ID_LABEL);
						String content = studentIdConfigurable ? Message.STUDENT_ID_UNUNIQUE_ERROR
								.replace("<#studentId#>", studentIdLabelName)
								: Message.STUDENT_ID_UNUNIQUE_ERROR.replace(
										"<#studentId#>",
										Message.DEFAULT_STUDENT_ID_LABEL);
								messageInfo = createMessageInfo(messageInfo, messageTitle, content, Message.ERROR, true, false );
					}

				}
			}
			if (! result)
			{           
				creatGson( req, resp, stream, messageInfo );
				return null;
			}     
			Boolean isMultiOrgAssociationValid = isMultiOrgAssociationValid(customerConfigurations);
			if(result && !isMultiOrgAssociationValid){
				if ( selectedOrgNodes.size() > 1 ) {
					if (isCreateNew) {
						messageInfo = createMessageInfo(messageInfo, Message.ADD_TITLE, Message.STUDENT_ASSIGNMENT_ERROR, Message.ERROR, true, false );
						//form.setMessage(Message.ADD_TITLE, Message.STUDENT_ASSIGNMENT_ERROR, Message.ERROR);
					}
					else {
						messageInfo = createMessageInfo(messageInfo, Message.EDIT_TITLE, Message.STUDENT_ASSIGNMENT_ERROR, Message.ERROR, true, false );
						//form.setMessage(Message.EDIT_TITLE, Message.STUDENT_ASSIGNMENT_ERROR, Message.ERROR);
					}
					creatGson( req, resp, stream, messageInfo );
					return null;
				}  
			}
			if(result) {
				studentId = saveStudentProfileInformation(isCreateNew, studentProfile, studentId, selectedOrgNodes, messageInfo);
				
				System.out.println("studentId==>"+studentId + "studentProfile.setFirstName" + studentProfile.getFirstName());	
				String demographicVisible = this.user.getCustomer().getDemographicVisible();
				if ((studentId != null) && demographicVisible.equalsIgnoreCase("T"))
				{
					result = saveStudentDemographic(isCreateNew, studentProfile, studentId);
				}
	
				if (studentId != null)
				{
					result = saveStudentAccommodations(isCreateNew, studentProfile, studentId, customerConfigurations, messageInfo);
				}

			}


			
			if (isCreateNew)
			{
				if (studentId != null)  {
					
					messageInfo = createMessageInfo(messageInfo, Message.ADD_TITLE, Message.ADD_SUCCESSFUL, Message.INFORMATION, false, true );
					//form.setMessage(Message.ADD_TITLE, Message.ADD_SUCCESSFUL, Message.INFORMATION);
				}
				else  {
					
					messageInfo = createMessageInfo(messageInfo, Message.ADD_TITLE, Message.ADD_ERROR, Message.INFORMATION, true, false );
					//form.setMessage(Message.ADD_TITLE, Message.ADD_ERROR, Message.INFORMATION);
				}
				
				
			}
			else
			{
				if (studentId != null) {
					messageInfo = createMessageInfo(messageInfo, Message.EDIT_TITLE, Message.EDIT_SUCCESSFUL, Message.INFORMATION, false, true );
					//form.setMessage(Message.EDIT_TITLE, Message.EDIT_SUCCESSFUL, Message.INFORMATION);
				}
				else  {
					messageInfo = createMessageInfo(messageInfo, Message.EDIT_TITLE, Message.EDIT_ERROR, Message.INFORMATION, true, false );
					//form.setMessage(Message.EDIT_TITLE, Message.EDIT_ERROR, Message.INFORMATION);
				}
			}
		
			creatGson( req, resp, stream, messageInfo );
			return null;
		
	}
	
	private MessageInfo createMessageInfo(MessageInfo messageInfo, String messageTitle, String content, String type, boolean errorflag, boolean successFlag){
		messageInfo.setTitle(messageTitle);
		messageInfo.setContent(content);
		messageInfo.setType(type);
		messageInfo.setErrorFlag(errorflag);
		messageInfo.setSuccessFlag(successFlag);
		return messageInfo;
	}
	
	private void creatGson(HttpServletRequest req, HttpServletResponse resp, OutputStream stream, MessageInfo messageInfo ){
		
		try {
			try {
				Gson gson = new Gson();
				String json = gson.toJson(messageInfo);
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
	
	/* 
	 * This method checks unique student ID validation is required or not. 
	 * @param form RegistrationForm
	 * @return true if Student ID is unique
	 */
	private boolean isValidationForUniqueStudentIDRequired(StudentProfileInformation studentProfile, CustomerConfiguration[]  customerConfigurations ) {

		boolean validateUniqueStudentID = false;
		if (studentProfile==null || studentProfile.getStudentNumber() == null
				|| studentProfile.getStudentNumber().trim().length() == 0) {
			return false;
		}
		if (customerConfigurations != null) {
			for (CustomerConfiguration customerConfiguration : customerConfigurations) {
				if (customerConfiguration.getCustomerConfigurationName().trim()
						.equalsIgnoreCase("Unique_Student_ID")) {
					if (customerConfiguration.getDefaultValue() != null
							&& customerConfiguration.getDefaultValue().trim()
							.equalsIgnoreCase("T")) {
						validateUniqueStudentID = true;
						break;
					}
				}

			}
		}
		return validateUniqueStudentID;
	}
	
	/*
	 * This method validate unique student ID. 
	 * @param isCreateNew boolean new student
	 * @param form RegistrationForm
	 * @return boolean isStudentIdUnique
	 */
	private boolean validateUniqueStudentId(boolean isCreateNew,Integer selectedStudentId,StudentProfileInformation studentProfile) {

		boolean isStudentIdUnique = false;
		try {
			isStudentIdUnique = this.studentManagement.validateUniqueStudentId(
					isCreateNew, customerId, selectedStudentId , studentProfile.getStudentNumber());
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
		return isStudentIdUnique;
	}
	
	private boolean isMultiOrgAssociationValid(CustomerConfiguration[]  customerConfigurations) 
	{     
		boolean multiOrgAssociationValid = true;



		for (int i=0; i < customerConfigurations.length; i++)
		{
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Class_Reassignment") && cc.getDefaultValue().equalsIgnoreCase("T"))
			{
				multiOrgAssociationValid = false; 
				break;
			}
		}
		return multiOrgAssociationValid;
	}
	
	/**
	 * saveStudentProfileInformation
	 */
	private Integer saveStudentProfileInformation(boolean isCreateNew, StudentProfileInformation studentProfile, Integer studentId, List selectedOrgNodes, MessageInfo messageInfo)
	{

		ManageStudent student = studentProfile.makeCopy(studentId, selectedOrgNodes);

		try
		{                    
			if (isCreateNew)
			{
				Student studentdetail = this.studentManagement.createNewStudent(this.userName, student);
				studentId = studentdetail.getStudentId();
				messageInfo.setStudentLoginId(studentdetail.getUserName());
				messageInfo.setStudentId(studentId);
				
			}
			else
			{
				this.studentManagement.updateStudent(this.userName, student);
				messageInfo.setStudentLoginId(student.getLoginId());
				messageInfo.setStudentId(student.getId());
			}
		}
		catch (StudentDataCreationException sde)
		{
			sde.printStackTrace();
			studentId = null;
		}        
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
			studentId = null;
		}                    

		return studentId;
	}
	
	/**
	 * saveStudentDemographic
	 */
	private boolean saveStudentDemographic(boolean isCreateNew, StudentProfileInformation studentProfile, Integer studentId)
	{
		boolean studentImported = (studentProfile.getCreateBy().intValue() == 1);                
		
		getStudentDemographicsFromRequest();
		
		if (studentImported)
		{        
			prepareStudentDemographicForCustomerConfiguration();
		}        
		getStudentDemographicsFromRequest();        

		if (isCreateNew)
		{
			createStudentDemographics(studentId);
		}
		/*else
		{
			updateStudentDemographics(studentId);
		} */
		this.demographics = null;

		return true;
	}
	
	/**
	 * saveStudentAccommodation
	 */

	private boolean saveStudentAccommodations(boolean isCreateNew, StudentProfileInformation studentProfile, Integer studentId, CustomerConfiguration[]  customerConfigurations, MessageInfo messageInfo)
	{
			String hideAccommodations = this.user.getCustomer().getHideAccommodations();

		if (hideAccommodations.equalsIgnoreCase("T"))         
			getStudentDefaultAccommodations(customerConfigurations);
		else
			getStudentAccommodationsFromRequest(customerConfigurations, studentProfile.getCreateBy());

		StudentAccommodations sa = this.accommodations.makeCopy(studentId);
		messageInfo.setHasAccommodation(studentHasAccommodation(sa));
		if (isCreateNew)
		{
			if (sa != null)
			{
				createStudentAccommodations(studentId, sa);
			}
		}
	/*	else
		{
			if (sa != null)
				updateStudentAccommodations(studentId, sa);
			else
				deleteStudentAccommodations(studentId);
		} */
		this.accommodations = null;

		return true;
	}
	
	/**
	 * createStudentDemographics
	 */
	private void createStudentDemographics(Integer studentId)
	{
		if ((studentId != null) && (studentId.intValue() > 0) && (this.demographics != null))
		{
			try
			{    
				StudentDemographic[] studentDemoList = (StudentDemographic[])this.demographics.toArray( new StudentDemographic[0] );
				this.studentManagement.createStudentDemographics(this.userName, studentId, studentDemoList);
			}
			catch (CTBBusinessException be)
			{
				be.printStackTrace();
			} 
		}
	}
	
	/**
	 * createStudentAccommodations
	 */
	private void createStudentAccommodations(Integer studentId, StudentAccommodations sa)
	{
		if ((studentId != null) && (studentId.intValue() > 0))
		{
			try
			{    
				this.studentManagement.createStudentAccommodations(this.userName, sa);
			}
			catch (CTBBusinessException be)
			{
				be.printStackTrace();
			}        
		}
	}
	
	/**
	 * getStudentDefaultAccommodations
	 */
	private void getStudentDefaultAccommodations(CustomerConfiguration[]  customerConfigurations) 
	{
		this.accommodations.setScreenReader(Boolean.FALSE);
		this.accommodations.setCalculator(Boolean.FALSE);
		this.accommodations.setTestPause(Boolean.FALSE);
		this.accommodations.setUntimedTest(Boolean.FALSE);
		this.accommodations.setHighlighter(Boolean.TRUE);
		this.accommodations.setColorFont(Boolean.FALSE);
		this.accommodations.setAuditoryCalming(Boolean.FALSE);//Added for Auditory Calming
		this.accommodations.setMaskingRuler(Boolean.FALSE);//Added for Masking Ruler
		this.accommodations.setMagnifyingGlass(Boolean.FALSE);//Added for Magnifying Glass
		this.accommodations.setExtendedTime(Boolean.FALSE); //Added for Student Pacing
		this.accommodations.setMaskingTool(Boolean.FALSE); // Added for Masking Answers

		setCustomerAccommodations(this.accommodations, true, customerConfigurations);
	}
	
	public String studentHasAccommodation(StudentAccommodations sa){
		 String hasAccommodations = "No";
	        if( "T".equals(sa.getScreenMagnifier()) ||
	            "T".equals(sa.getScreenReader()) ||
	            "T".equals(sa.getCalculator()) ||
	            "T".equals(sa.getTestPause()) ||
	            "T".equals(sa.getUntimedTest()) ||
	            "T".equals(sa.getHighlighter()) ||
	            "T".equals(sa.getExtendedTime()) ||
	            (sa.getMaskingRuler() != null && !sa.getMaskingRuler().equals("") && !sa.getMaskingRuler().equals("F"))||
	            (sa.getExtendedTime() != null && !sa.getExtendedTime().equals("") && !sa.getExtendedTime().equals("F")) || 
	            (sa.getAuditoryCalming() != null && !sa.getAuditoryCalming().equals("") && !sa.getAuditoryCalming().equals("F")) || 
	            (sa.getMagnifyingGlass() != null && !sa.getMagnifyingGlass().equals("") && !sa.getMagnifyingGlass().equals("F")) || 
	            (sa.getMaskingTool() != null && !sa.getMaskingTool().equals("") && !sa.getMaskingTool().equals("F")) || 
	            sa.getQuestionBackgroundColor() != null ||
	            sa.getQuestionFontColor() != null ||
	            sa.getQuestionFontSize() != null ||
	            sa.getAnswerBackgroundColor() != null ||
	            sa.getAnswerFontColor() != null ||
	            sa.getAnswerFontSize() != null)
	        	hasAccommodations = "Yes";
	       
	       
	   return hasAccommodations;
	}
	
	
	private void writeToOutPutStream(HttpServletResponse resp, ModifyManifestVo vo) {
		OutputStream stream = null;
		try {
			Gson gson = new Gson();
			String json = gson.toJson(vo);
			resp.setContentType("application/json");
			resp.flushBuffer();
			stream = resp.getOutputStream();
			stream.write(json.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			close(stream);
		}
		
	}
	
	
	private void close(OutputStream stream){
		if (stream!=null){
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}