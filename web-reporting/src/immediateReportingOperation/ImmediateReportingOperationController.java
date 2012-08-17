package immediateReportingOperation;



import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.Base;
import utils.BaseTree;
import utils.BroadcastUtils;
import utils.DateUtils;
import utils.GridDropLists;
import utils.Organization;
import utils.OrgnizationComparator;
import utils.PermissionsUtils;
import utils.StudentImmediateCSVReportUtils;
import utils.StudentImmediatePdfReportUtils;
import utils.StudentPathListUtils;
import utils.StudentProfileInformation;
import utils.StudentSearchUtils;
import utils.TreeData;

import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.studentManagement.StudentScoreReport;
import com.ctb.bean.testAdmin.BroadcastMessage;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.google.gson.Gson;




@Jpf.Controller()
public class ImmediateReportingOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;

	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.BroadcastMessageLog message;
	@Control()
	private com.ctb.control.studentManagement.StudentManagement studentManagement;
	@Control()
	private com.ctb.control.db.OrgNode orgnode;
	@Control()
	private com.ctb.control.testAdmin.ScheduleTest scheduleTest;

	private String userName = null;
	private Integer customerId = null;
	private User user = null;
	CustomerConfiguration[] customerConfigurations = null;
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
	
	

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "immediate_scoring_report_main.jsp") })
	protected Forward begin() {
		initialize();
		return new Forward("success");
	}
	
	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "/error.jsp") })
	protected Forward error() {
		initialize();
		return new Forward("success");
	}

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "immediate_scoring_report_main.jsp") })
	public Forward beginImmediateReporting() {
		Forward forward = new Forward("success");
		return forward;
	}


	
	@Jpf.Action()
	public Forward getOrgNodeHierarchy() {

		String jsonTree = "";
		HttpServletResponse resp = getResponse();
		resp.setCharacterEncoding("UTF-8"); 
		OutputStream stream = null;
		String contentType = CONTENT_TYPE_JSON;
		if (this.userName == null) {
			getLoggedInUserPrincipal();
			getUserDetails();
		}
		try {
			BaseTree baseTree = new BaseTree();

			ArrayList<Organization> completeOrgNodeList = new ArrayList<Organization>();
			UserNodeData associateNode = StudentPathListUtils.populateAssociateNode(this.userName, this.studentManagement);
			ArrayList<Organization> selectedList = StudentPathListUtils.buildassoOrgNodehierarchyList(associateNode);
			Collections.sort(selectedList, new OrgnizationComparator());
			Integer leafNodeCategoryId = StudentPathListUtils.getLeafNodeCategoryId(this.userName, this.customerId,	this.studentManagement);
			ArrayList<Integer> orgIDList = new ArrayList<Integer>();
			ArrayList<TreeData> data = new ArrayList<TreeData>();

			UserNodeData und = StudentPathListUtils.OrgNodehierarchy(this.userName, this.studentManagement, selectedList.get(0).getOrgNodeId());
			ArrayList<Organization> orgNodesList = StudentPathListUtils.buildOrgNodehierarchyList(und, orgIDList, completeOrgNodeList);

			for (int i = 0; i < selectedList.size(); i++) {
				if (i == 0) {
					preTreeProcess(data, orgNodesList, selectedList);
				} else {
					Integer nodeId = selectedList.get(i).getOrgNodeId();
					if (orgIDList.contains(nodeId)) {
						continue;
					} else if (!selectedList.get(i).getIsAssociate()) {
						continue;
					} else {
						orgIDList = new ArrayList<Integer>();
						UserNodeData undloop = StudentPathListUtils.OrgNodehierarchy(this.userName,	this.studentManagement, nodeId);
						ArrayList<Organization> orgNodesListloop = StudentPathListUtils.buildOrgNodehierarchyList(undloop, orgIDList,completeOrgNodeList);
						preTreeProcess(data, orgNodesListloop, selectedList);
					}
				}
			}

			Gson gson = new Gson();
			baseTree.setData(data);
			Collections.sort(baseTree.getData(), new Comparator<TreeData>() {

				public int compare(TreeData t1, TreeData t2) {
					return (t1.getData().toUpperCase().compareTo(t2.getData()
							.toUpperCase()));
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
				stream.write(jsonTree.getBytes("UTF-8"));
			} finally {
				if (stream != null) {
					stream.close();
				}
			}
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.err.println("Exception while retrieving organization hierarchy.");
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
		HttpServletResponse resp = getResponse();
		try {
			Integer treeOrgNodeId = Integer.parseInt(getRequest().getParameter("treeOrgNodeId"));
			Integer studentCount = 0;
			studentCount = StudentSearchUtils.getCompletedStudentCountForOrgNode(this.userName, this.studentManagement, treeOrgNodeId);
			try {
				Gson gson = new Gson();
				String json = gson.toJson(studentCount);
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
		catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.err.println("Exception while retrieving student Count.");
			e.printStackTrace();
		}
		return null;

	}
	
	
	@Jpf.Action(forwards={	@Jpf.Forward(name = "success",	path ="") })
	protected Forward getAllCompletedStudentForOrgNode(){

		OutputStream stream = null;
		HttpServletResponse resp = getResponse();
		resp.setCharacterEncoding("UTF-8"); 
		List<StudentProfileInformation> studentList = new ArrayList<StudentProfileInformation>(0);
		try {
			ManageStudentData msData = null;
			Integer treeOrgNodeId = Integer.parseInt(getRequest().getParameter("treeOrgNodeId"));
			msData = StudentSearchUtils.getAllCompletedStudentForOrgNode(this.userName, this.studentManagement, treeOrgNodeId);
			studentList = StudentSearchUtils.buildStudentList(msData);
			Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
			base.setStudentProfileInformation(studentList);
			try {
				Gson gson = new Gson();
				String json = gson.toJson(base);
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
		catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.err.println("Exception while retrieving all completed student.");
			e.printStackTrace();
		}
		return null;

	}
	
	
	
	@Jpf.Action(forwards={	@Jpf.Forward(name = "success",	path ="") })
	protected Forward getStudentScoreDetails(){

		OutputStream stream = null;
		HttpServletResponse resp = getResponse();
		resp.setCharacterEncoding("UTF-8"); 

		try {
			Integer testRosterId = Integer.valueOf(this.getRequest().getParameter("rosterId"));
			Integer testAdminId = Integer.valueOf(this.getRequest().getParameter("testAdminId"));
			StudentScoreReport stuReport = studentManagement.getStudentReport(testRosterId, testAdminId);
			stuReport.setTestAdminStartDateString(  DateUtils.formatDateToDateString(stuReport.getTestAdminStartDate(), DateUtils.DATE_FORMAT_DISPLAY));
			try {
				Gson gson = new Gson();
				String json = gson.toJson(stuReport);
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
		catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.err.println("Exception while retrieving all completed student.");
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
		resp.setCharacterEncoding("UTF-8"); 
		try {
			if(this.userName == null ) {
				getLoggedInUserPrincipal();		
				getUserDetails();
			}
			GridDropLists dropList = new GridDropLists();
			dropList.setGradeOptions(getGradeOptions());
			dropList.setTestCatalogOptions(getTestNameOptions());
			dropList.setContentAreaOptions(getContentAreaOptions());
			dropList.setFormOptions(getFormOptions());
			try{
				Gson gson = new Gson();
				json = gson.toJson(dropList);
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
			System.err.println("Exception while processing populateGridDropDowns.");
			e.printStackTrace();
		}

		return null;

	}
	
	@Jpf.Action()
    protected Forward studentsImmediateScoreReportInPDF()
    {
		
		try{
			Integer testRosterId = Integer.valueOf(this.getRequest().getParameter("rosterId"));
			Integer testAdminId = Integer.valueOf(this.getRequest().getParameter("testAdminId"));
			StudentScoreReport stuReport = studentManagement.getStudentReport(testRosterId, testAdminId);
			StudentImmediatePdfReportUtils utils = new StudentImmediatePdfReportUtils();
			String fileName = stuReport.getStudentFirstName()+"_"+stuReport.getStudentLastName()+"_"+testRosterId;
			getResponse().setContentType("application/pdf");
	        getResponse().setHeader("Content-Disposition","attachment; filename="+fileName+".pdf");
	        getResponse().setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
	        getResponse().setHeader("Pragma", "public"); 
			utils.setup(getResponse().getOutputStream(), stuReport,  DateUtils.formatDateToDateString(stuReport.getTestAdminStartDate(), DateUtils.DATE_FORMAT_DISPLAY) );
			utils.generateReport();
			
		} catch (CTBBusinessException ce){
			ce.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
    	
		  return null;
    }
	
	@Jpf.Action()
    protected Forward studentsImmediateScoreReportInCSV()
    {
		try{
			Integer testRosterId = Integer.valueOf(this.getRequest().getParameter("rosterId"));
			Integer testAdminId = Integer.valueOf(this.getRequest().getParameter("testAdminId"));
			StudentScoreReport stuReport = studentManagement.getStudentReport(testRosterId, testAdminId);
			StudentImmediateCSVReportUtils utilsCSV = new StudentImmediateCSVReportUtils();
			String fileName = stuReport.getStudentFirstName()+"_"+stuReport.getStudentLastName()+"_"+testRosterId;
			getResponse().setContentType("text/csv");
	        getResponse().setHeader("Content-Disposition","attachment; filename="+fileName+".csv");
	        getResponse().setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
	        getResponse().setHeader("Pragma", "public");
	        getResponse().setCharacterEncoding("UTF-8");
			OutputStream os = getResponse().getOutputStream();
			os.write(239);     
			os.write(187);     
			os.write(191);    
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, "UTF-8")); 
			utilsCSV.setupCSV(writer, stuReport,  DateUtils.formatDateToDateString(stuReport.getTestAdminStartDate(), DateUtils.DATE_FORMAT_DISPLAY) );
	        utilsCSV.generateReport();
			writer.flush();
			writer.close();
		} catch (CTBBusinessException ce){
			ce.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
		String forwardName = (menuId != null) ? menuId : "OOSLink";
		
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
       /* HttpServletRequest req = getRequest();*/
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		
		if (this.userName == null) {
			getLoggedInUserPrincipal();
			this.userName = (String)getSession().getAttribute("userName");
		}
		
		List<BroadcastMessage> broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
        String bcmString = BroadcastUtils.buildBroadcastMessages(broadcastMessages);
        this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
		
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

	
    /////////////////////////////////////////////////////////////////////////////////////////////    
	///////////////////////////// END OF NEW NAVIGATION ACTIONS ///////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////// 	
	
	
    
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
	
	
	
	
	
	private void initialize() {
		getLoggedInUserPrincipal();
		getUserDetails();
		setupUserPermission();
	}

	private void getLoggedInUserPrincipal() {
		java.security.Principal principal = getRequest().getUserPrincipal();
		if (principal != null) {
			this.userName = principal.toString();
		}
		getSession().setAttribute("userName", this.userName);
	}

	private void getUserDetails() {
		try {
			if (this.userName != null) {
				this.user = this.studentManagement.getUserDetails(
						this.userName, this.userName);
				this.customerId = user.getCustomer().getCustomerId();
				Customer customer = this.user.getCustomer();
				Boolean supportAccommodations = Boolean.TRUE;
				String hideAccommodations = customer.getHideAccommodations();
				if ((hideAccommodations != null)
						&& hideAccommodations.equalsIgnoreCase("T")) {
					supportAccommodations = Boolean.FALSE;
				}
				this.getRequest().setAttribute("supportAccommodations",
						supportAccommodations);

			}
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		getSession().setAttribute("userName", this.userName);
		getSession().setAttribute("createdBy", this.user.getUserId());
	}

	private void setupUserPermission() {
		customerConfigurations = getCustomerConfigurations();

		boolean laslinkCustomer = isLaslinkCustomer(customerConfigurations);

		this.getSession()
				.setAttribute(
						"showReportTab",
						new Boolean(userHasReports().booleanValue()
								|| laslinkCustomer));

		this.getRequest().setAttribute("isLasLinkCustomer", laslinkCustomer);

		this.getRequest().setAttribute("isTopLevelUser",
				isTopLevelUser(laslinkCustomer));

		getConfigStudentLabel(customerConfigurations);

		this.getRequest().setAttribute("customerConfigurations",
				customerConfigurations);

		setUpAllUserPermission(customerConfigurations);
	}

	private CustomerConfiguration[] getCustomerConfigurations() {
		CustomerConfiguration[] customerConfigurations = null;
		try {
			customerConfigurations = this.studentManagement
					.getCustomerConfigurations(this.userName, this.customerId);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		return customerConfigurations;
	}

	private boolean isLaslinkCustomer(CustomerConfiguration[] customerConfigs) {
		boolean laslinkCustomer = false;

		for (int i = 0; i < customerConfigs.length; i++) {
			CustomerConfiguration cc = (CustomerConfiguration) customerConfigs[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase(
					"Laslink_Customer")
					&& cc.getDefaultValue().equals("T")) {
				laslinkCustomer = true;
			}
		}
		return laslinkCustomer;
	}

	private Boolean userHasReports() {
		boolean hasReports = false;
		try {
			Customer customer = this.user.getCustomer();
			Integer customerId = customer.getCustomerId();
			hasReports = this.studentManagement.userHasReports(this.userName,
					customerId);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		return new Boolean(hasReports);
	}

	private boolean isTopLevelUser(boolean isLasLinkCustomerVal) {

		boolean isUserTopLevel = false;
		boolean isLaslinkUserTopLevel = false;
		boolean isLaslinkUser = false;
		isLaslinkUser = isLasLinkCustomerVal;
		try {
			if (isLaslinkUser) {
				isUserTopLevel = orgnode.checkTopOrgNodeUser(this.userName);
				if (isUserTopLevel) {
					isLaslinkUserTopLevel = true;
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return isLaslinkUserTopLevel;
	}

	private void getConfigStudentLabel(
			CustomerConfiguration[] customerConfigurations) {
		//boolean isStudentIdConfigurable = false;
		Integer configId = 0;
		String[] valueForStudentId = new String[8];
		valueForStudentId[0] = "Student ID";
		for (int i = 0; i < customerConfigurations.length; i++) {
			CustomerConfiguration cc = (CustomerConfiguration) customerConfigurations[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase(
					"Configurable_Student_ID")
					&& cc.getDefaultValue().equalsIgnoreCase("T")) {
				//isStudentIdConfigurable = true;
				configId = cc.getId();
				CustomerConfigurationValue[] customerConfigurationsValue = customerConfigurationValues(configId);
				// By default there should be 3 entries for customer
				// configurations
				valueForStudentId = new String[8];
				for (int j = 0; j < customerConfigurationsValue.length; j++) {
					int sortOrder = customerConfigurationsValue[j]
							.getSortOrder();
					valueForStudentId[sortOrder - 1] = customerConfigurationsValue[j]
							.getCustomerConfigurationValue();
				}
				valueForStudentId[0] = valueForStudentId[0] != null ? valueForStudentId[0]
						: "Student ID";

			}

		}
		this.getRequest().setAttribute("studentIdLabelName",
				valueForStudentId[0]);

	}

	private void setUpAllUserPermission(
			CustomerConfiguration[] customerConfigurations) {

		boolean hasBulkStudentConfigurable = false;
		boolean hasBulkStudentMoveConfigurable = false;
		boolean hasOOSConfigurable = false;
		boolean adminUser = isAdminUser();
		boolean hasUploadDownloadConfig = false;
		boolean hasProgramStatusConfig = false;
		boolean hasScoringConfigurable = false;
		boolean hasLicenseConfiguration = false;
		boolean TABECustomer = false;
		String roleName = this.user.getRole().getRoleName();
		boolean adminCoordinatorUser = isAdminCoordinatotUser(); //For Student Registration

		if (customerConfigurations != null) {
			for (int i = 0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration) customerConfigurations[i];
				// For Bulk Accommodation
				if (cc.getCustomerConfigurationName().equalsIgnoreCase(
						"Configurable_Bulk_Accommodation")
						&& cc.getDefaultValue().equals("T")) {
					hasBulkStudentConfigurable = true;
					continue;
				}
				// For Bulk Student Move
				if (cc.getCustomerConfigurationName().equalsIgnoreCase(
						"Bulk_Move_Students")
						&& cc.getDefaultValue().equals("T")) {
					hasBulkStudentMoveConfigurable = true;
					continue;
				}
				// For Out Of School Student
				if (cc.getCustomerConfigurationName().equalsIgnoreCase(
						"OOS_Configurable")
						&& cc.getDefaultValue().equals("T")) {
					hasOOSConfigurable = true;
					continue;
				}
				// For Upload Download
				if (cc.getCustomerConfigurationName().equalsIgnoreCase(
						"Allow_Upload_Download")
						&& cc.getDefaultValue().equals("T")) {
					hasUploadDownloadConfig = true;
					continue;
				}
				// For Program Status
				if (cc.getCustomerConfigurationName().equalsIgnoreCase(
						"Program_Status")
						&& cc.getDefaultValue().equals("T")) {
					hasProgramStatusConfig = true;
					continue;
				}
				// For Hand Scoring
				if (cc.getCustomerConfigurationName().equalsIgnoreCase(
						"Configurable_Hand_Scoring")
						&& cc.getDefaultValue().equals("T")) {
					hasScoringConfigurable = true;
					continue;
				}
				// For License
				if (cc.getCustomerConfigurationName().equalsIgnoreCase(
						"Allow_Subscription")
						&& cc.getDefaultValue().equals("T")) {
					hasLicenseConfiguration = true;
					continue;
				}
				// For TABE Customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase(
						"TABE_Customer")) {
					TABECustomer = true;
					continue;
				}
			}

		}
		this.getSession().setAttribute("isBulkAccommodationConfigured",
				new Boolean(hasBulkStudentConfigurable));
		this.getSession().setAttribute("isBulkMoveConfigured",
				new Boolean(hasBulkStudentMoveConfigurable));
		this.getSession().setAttribute("isOOSConfigured",
				new Boolean(hasOOSConfigurable));
		this.getSession().setAttribute("hasUploadDownloadConfigured",
				new Boolean(hasUploadDownloadConfig && adminUser));
		this.getSession().setAttribute("hasProgramStatusConfigured",
				new Boolean(hasProgramStatusConfig && adminUser));
		this.getSession().setAttribute("hasScoringConfigured",
				new Boolean(hasScoringConfigurable));
		this.getSession().setAttribute("hasLicenseConfigured",
				new Boolean(hasLicenseConfiguration && adminUser));
		this.getSession().setAttribute("adminUser", new Boolean(adminUser));
		boolean validUser = (roleName
				.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || roleName
				.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
		this.getSession().setAttribute("canRegisterStudent",
				new Boolean(TABECustomer && validUser));
		this.getSession().setAttribute("hasRapidRagistrationConfigured", 
				new Boolean(TABECustomer && (adminUser || adminCoordinatorUser) ));//For Student Registration
	}
	

	private boolean isAdminCoordinatotUser() //For Student Registration
	{               
		String roleName = this.user.getRole().getRoleName();        
		return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR); 
	}

	private CustomerConfigurationValue[] customerConfigurationValues(
			Integer configId) {
		CustomerConfigurationValue[] customerConfigurationsValue = null;
		try {
			customerConfigurationsValue = this.studentManagement
					.getCustomerConfigurationsValue(configId);

		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		return customerConfigurationsValue;
	}

	private boolean isAdminUser() {
		String roleName = this.user.getRole().getRoleName();
		return roleName
				.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR);
	}
	
	private String[] getGradeOptions() throws Exception {
		String[] grades = null;
		grades = this.studentManagement.getGradesForCustomer(this.userName,
					this.customerId);
		List<String> options = new ArrayList<String>();
		for (int i = 0; i < grades.length; i++) {
			options.add(grades[i]);
		}
		return (String[]) options.toArray(new String[0]);
	}
	
	private String[] getTestNameOptions() throws Exception {
		String[] testNameOptions = null;
		testNameOptions = this.scheduleTest
					.getTestCatalogForUserForScoring(this.userName);

		return testNameOptions;

	}
	
	private String[] getContentAreaOptions() throws Exception {
		String[] testNameOptions = null;
		testNameOptions = this.scheduleTest.getAllContentAreaOptionsForUser(this.userName);
		return testNameOptions;
	}
	
	private String[] getFormOptions() throws Exception {
		String[] testNameOptions = null;
		testNameOptions = this.scheduleTest.getAllFormOptionsForUser(this.userName);
		return testNameOptions;
	}
	
	public static class ImmediateReportingForm extends SanitizedFormData{
		private static final long serialVersionUID = 1L;
		
	}
}