package studentScoringOperation;

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

import utils.Base;
import utils.BaseTree;
import utils.BroadcastUtils;
import utils.ItemScoringUtils;
import utils.MessageResourceBundle;
import utils.Organization;
import utils.OrgnizationComparator;
import utils.PermissionsUtils;
import utils.StudentPathListUtils;
import utils.StudentSearchUtils;
import utils.TreeData;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.ScorableItem;
import com.ctb.bean.testAdmin.ScorableItemData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.google.gson.Gson;

import dto.GridDropLists;
import dto.StudentProfileInformation;
import dto.TestSessionVO;

@Jpf.Controller()
public class StudentScoringOperationController extends PageFlowController {
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
    private com.ctb.control.crscoring.TestScoring testScoring;
	
    
	private String userName = null;
	private Integer customerId = null;
	private User user = null;
	CustomerConfiguration[] customerConfigurations = null;
	public static String CONTENT_TYPE_JSON = "application/json";
    private Integer itemSetIdTC = null; 
    
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
	 * @jpf:forward name="success" path="beginStudentScoring.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "beginStudentScoring.do")
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
					path = "scoring_student_session_list.jsp")
	})
	protected Forward beginStudentScoring()
	{
		initialize();
		return new Forward("success");
	}
	
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward getStudentCountForOrgNode(StudentSessionScoringForm form){

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
	protected Forward getStudentForScoringGrid(StudentSessionScoringForm form){

		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		List<StudentProfileInformation> studentList = new ArrayList<StudentProfileInformation>(0);
		String json = "";
		
		try {
			ManageStudentData msData = findStudentByHierarchy();
			
			if ((msData != null) && (msData.getFilteredCount().intValue() > 0))
			{
				studentList = StudentSearchUtils.buildStudentListForScoring(msData);
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
			System.err.println("Exception while processing getStudentForScoringGrid.");
			e.printStackTrace();
		}

		return null;

	}
	
	@Jpf.Action(forwards={
    		@Jpf.Forward(name = "success", 
					path ="")
	})
    protected Forward getSessionForScoringGrid(StudentSessionScoringForm form){
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
			tsd = this.testSessionStatus.getTestSessionsForStudentScoring(this.userName, this.user.getUserId(), treeOrgNodeId);
			
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
			System.err.println("Exception while processing getSessionForScoringGrid.");
			e.printStackTrace();
		}
		return null;

	}
	
	
	//Added to populate Item List Grid 
	
	@Jpf.Action(forwards={
    		@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward findItemDetail(StudentSessionScoringForm form){
		
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		resp.setCharacterEncoding("UTF-8"); 
		TestSession ts = null;
		Base base = new Base();
//		String testAdminIdString = RequestUtil.getValueFromRequest(this.getRequest(), RequestUtil.TEST_ADMIN_ID, false, null);
		Integer itemId = Integer.parseInt(getRequest().getParameter("itemId"));
		ScorableItemData siData = null;
		siData = ItemScoringUtils.getItemsByTestSession(testScoring, null, null, null, itemId);
		if(siData != null){
			if(siData.getScorableItems()[0].getItemSetIdTC() != null)
				this.setItemSetIdTC( siData.getScorableItems()[0].getItemSetIdTC());
		}
		
		List<ScorableItem> itemList = ItemScoringUtils.buildItemList(siData);
		try {
			ts = testScoring.getTestAdminDetails(itemId);
		}  catch (CTBBusinessException be){
			be.printStackTrace();
		}
		customerHasBulkAccommodation();
		if(itemList.isEmpty())
		{	
			this.getRequest().setAttribute("itemSearchEmpty", MessageResourceBundle.getMessage("itemSearchEmpty"));        
			return new Forward("findItem");
		}
		base.setPage("1");
		base.setRecords("10");
		base.setTotal("2");
		base.setItemList(itemList);
		Gson gson = new Gson();
		json = gson.toJson(base);
		
		try {
			resp.setContentType(CONTENT_TYPE_JSON);
			stream = resp.getOutputStream();
			stream.write(json.getBytes("UTF-8"));
			resp.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private Boolean customerHasBulkAccommodation() 
	{
		boolean hasBulkStudentConfigurable = false;
		//Bulk Accommodation
		for (int i=0; i < this.customerConfigurations.length; i++) {

			CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && 
					cc.getDefaultValue().equals("T")) {
				hasBulkStudentConfigurable = true; 
				break;
			}
		}

		getSession().setAttribute("isBulkAccommodationConfigured", hasBulkStudentConfigurable);


		return new Boolean(hasBulkStudentConfigurable);           
	}
	
	
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward populateGridDropDowns(StudentSessionScoringForm form){

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
			protected Forward beginDisplayStudItemList(StudentSessionScoringForm form)
	{ 
			
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		Integer rosterId = Integer.parseInt(getRequest().getParameter("rosterId"));
		Integer itemSetIdTC = Integer.parseInt(getRequest().getParameter("itemSetIdTC"));
			
try {
			
	ScorableItemData sid = getTestItemForStudent(rosterId, itemSetIdTC, null, null);
	List<ScorableItem> itemList = buildItemList(sid);
			
			try{
				Base base = new Base();
				base.setScorableItems(itemList);
				
				base.setPage("1");
				base.setRecords("10");
				base.setTotal("2");
				
				Gson gson = new Gson();
				json = gson.toJson(base);

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
			System.err.println("Exception while processing populateGridDropDowns.");
			e.printStackTrace();
		}

		return null;
	}
	
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward getStudentListForScoreByStudent(StudentSessionScoringForm form) {
		
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		
		Integer testAdminId = 0;
		if(this.userName == null ) {
			getLoggedInUserPrincipal();		
			getUserDetails();
		}
		
		String reqTestAdminId = getRequest().getParameter("testAdminId");		
		if (reqTestAdminId != null) {
			testAdminId = Integer.valueOf(reqTestAdminId);
		}
		
		try {
			RosterElementData reData = null;
			reData = findStudentForTestSession(testAdminId, this.userName);
			List<RosterElement> studentList = buildStudentList(reData);
			try{
				Base base = new Base();
				base.setScoreByStudentList(studentList);
				
				base.setPage("1");
				base.setRecords("10");
				base.setTotal("2");
				
				Gson gson = new Gson();
				json = gson.toJson(base);

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
			System.err.println("Exception while processing getStudentListForScoreByStudent.");
			e.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * findStudentForTestSession
	 */
	private RosterElementData findStudentForTestSession(Integer testAdminId, String userName) {
		
		RosterElementData reData = null;
		try {
			reData = testScoring.getAllStudentForTestSession(
					testAdminId, userName, null, null, null);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}

		return reData;
	}

	/**
	 * buildStudentList
	 */
	public static List<RosterElement> buildStudentList(RosterElementData reData) {
		ArrayList<RosterElement> studentList = new ArrayList<RosterElement>();
		if (reData != null) {
			RosterElement[] students = reData.getRosterElements();
			for (RosterElement student : students) {
				if (student != null) {
					if(student.getScoringStatus().equals("CO")) {
						student.setScoringStatus("Complete");
					} else {
						student.setScoringStatus("Incomplete");
					}
					studentList.add(student);
				}
			}
		}
		return studentList;
	}
	
	
	
	private ScorableItemData getTestItemForStudent(Integer testRostorId,Integer itemSetId, PageParams page, SortParams sort) 
    {
	 ScorableItemData sid = new ScorableItemData();                        
        try
        {      
            sid = this.testScoring.getAllScorableCRItemsForTestRoster(testRostorId, itemSetId, page, sort);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return sid;
    } 
	
	
	private List<ScorableItem> buildItemList(ScorableItemData tid) 
    {
        ArrayList<ScorableItem> itemList = new ArrayList<ScorableItem>();
        if (tid != null) {
        	 ScorableItem[] testItemDetails = tid.getScorableItems();    
            for (int i=0 ; i<testItemDetails.length ; i++) {
            	ScorableItem itemDetail = (ScorableItem)testItemDetails[i];
                if (itemDetail != null) {
                	if(itemDetail.getAnswered().equals("A")) {
                		itemDetail.setAnswered("Answered");
                	} else {
                		itemDetail.setAnswered("Not Answered");
                	}
                    itemList.add(itemDetail);
                }
            }
        }
        return itemList;
    }
	
	
	private Base buildTestSessionList(TestSessionData tsd, Base base) 
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
	
	
	/**
	 * findByHierarchy
	 */
	private ManageStudentData findStudentByHierarchy()
	{      
		String treeOrgNodeId = getRequest().getParameter("treeOrgNodeId");
		Integer selectedOrgNodeId = null;
		if(treeOrgNodeId != null)
			selectedOrgNodeId = Integer.parseInt(treeOrgNodeId);
		ManageStudentData msData = null;

		FilterParams filter = null;
		PageParams page = null;
		SortParams sort = null;

		if (selectedOrgNodeId != null)
		{
			msData = StudentSearchUtils.getStudentsMinimalInfoForScoring(this.userName, this.studentManagement, selectedOrgNodeId, filter, page, sort);

		}

		return msData;
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
	
	
	
	private void initialize()
	{     
		getLoggedInUserPrincipal();
		getUserDetails();
		setupUserPermission();	
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
	
	//*****************************Code For Org Tree Population*************************************Start
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward scoringOrgNodeHierarchyList(){

		String jsonTree = "";
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String contentType = CONTENT_TYPE_JSON;
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
	//*****************************Code For Org Tree Population*************************************End
	
	
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
    
private void setUpAllUserPermission(CustomerConfiguration [] customerConfigurations) {
    	
    	boolean hasBulkStudentConfigurable = false;
    	boolean hasBulkStudentMoveConfigurable = false;
    	boolean hasOOSConfigurable = false;
    	boolean adminUser = isAdminUser();
    	boolean hasUploadDownloadConfig = false;
    	boolean hasProgramStatusConfig = false;
    	boolean hasScoringConfigurable = false;
    	boolean hasLicenseConfiguration= false;
    	boolean TABECustomer = false;
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
					hasLicenseConfiguration = true;
					continue;
	            }
				// For TABE Customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer")) {
	            	TABECustomer = true;
	            	continue;
	            }
			}
			
		}
		this.getSession().setAttribute("isBulkAccommodationConfigured",new Boolean(hasBulkStudentConfigurable));
		this.getSession().setAttribute("isBulkMoveConfigured",new Boolean(hasBulkStudentMoveConfigurable));
		this.getSession().setAttribute("isOOSConfigured",new Boolean(hasOOSConfigurable));
		this.getSession().setAttribute("hasUploadDownloadConfigured",new Boolean(hasUploadDownloadConfig && adminUser));
		this.getSession().setAttribute("hasProgramStatusConfigured",new Boolean(hasProgramStatusConfig && adminUser));
		this.getSession().setAttribute("hasScoringConfigured",new Boolean(hasScoringConfigurable && adminUser));
		this.getSession().setAttribute("hasLicenseConfigured",new Boolean(hasLicenseConfiguration && adminUser));
		this.getSession().setAttribute("adminUser", new Boolean(adminUser));
		boolean validUser = (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || 
        		roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
		this.getSession().setAttribute("canRegisterStudent", new Boolean(TABECustomer && validUser));
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
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////    
	///////////////////////////// BEGIN OF NEW NAVIGATION ACTIONS ///////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////    
	
	    /**
	     * ASSESSMENTS actions
	     */    
	    @Jpf.Action(forwards = { 
	    		@Jpf.Forward(name = "sessionsLink", path = "assessments_sessionsLink.do"),
	    		@Jpf.Forward(name = "studentScoringLink", path = "assessments_studentScoringLink.do"),
	    		@Jpf.Forward(name = "programStatusLink", path = "assessments_programStatusLink.do")
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
        HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		
		if (this.userName == null) {
			getLoggedInUserPrincipal();
			this.userName = (String)getSession().getAttribute("userName");
		}
		
		List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
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
    
    @Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "beginStudentScoring.do")
	})
	protected Forward scoring_studentScoring()
	{
    	return new Forward("success");
	}
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    /////// *********************** MANAGESTUDENTFORM ************* /////////////////////////////    
    /////////////////////////////////////////////////////////////////////////////////////////////    
	/**
	 * FormData get and set methods may be overwritten by the Form Bean editor.
	 */
	public static class StudentSessionScoringForm extends SanitizedFormData
	{

	}

	public Integer getItemSetIdTC() {
		return itemSetIdTC;
	}

	public void setItemSetIdTC(Integer itemSetIdTC) {
		this.itemSetIdTC = itemSetIdTC;
	}
}