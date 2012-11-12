package programOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import util.BroadcastUtils;

import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.Program;
import com.ctb.bean.testAdmin.ProgramData;
import com.ctb.bean.testAdmin.ProgramStatusSession;
import com.ctb.bean.testAdmin.ProgramStatusSessionData;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestElementData;
import com.ctb.bean.testAdmin.TestStatus;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.control.userManagement.UserManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.testSessionInfo.dto.ProgramStatusInfo;
import com.ctb.testSessionInfo.dto.ProgramStatusReportVO;
import com.ctb.testSessionInfo.dto.SubtestStatus;
import com.ctb.testSessionInfo.dto.TestStatusVO;
import com.ctb.testSessionInfo.utils.BaseTree;
import com.ctb.testSessionInfo.utils.FilterSortPageUtils;
import com.ctb.testSessionInfo.utils.Organization;
import com.ctb.testSessionInfo.utils.OrgnizationComparator;
import com.ctb.testSessionInfo.utils.PermissionsUtils;
import com.ctb.testSessionInfo.utils.ProgramStatusReportUtils;
import com.ctb.testSessionInfo.utils.TreeData;
import com.ctb.testSessionInfo.utils.UserOrgHierarchyUtils;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.google.gson.Gson;

@Jpf.Controller()
public class ProgramOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;

    /**
     * @common:control
     */
	
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;
	
    @Control()
    private com.ctb.control.userManagement.UserManagement userManagement;
    
    @Control()
    private com.ctb.control.db.BroadcastMessageLog message;
    
    @Control()
    private com.ctb.control.testAdmin.ProgramStatus programStatus;
    
    @Control()
	private com.ctb.control.db.OrgNode orgnode;
    
	private String userName = null;
	private Integer customerId = null;
    private User user = null;
    ///program status changes
    private String customerName = null;
    public static final String CONTENT_TYPE_JSON = "application/json";
    private static final int MAX_EXPORT = 65000;
    private List subtestList = null;
    private List statusList = null;
    public LinkedHashMap testList = null;
    public LinkedHashMap programList = null;
    private static final String SHOW_STATUS = "showStatus";
    private static final String STATUS_PAGE_REQUESTED = "{actionForm.statusPageRequested}";
    private static final String STATUS_SORT_REQUESTED = "{actionForm.statusSortOrderBy}";
    private static final String STATUS_PAGE_GO_BUTTON = "ButtonGoInvoked_unknown";
    private static final String STATUS_ENTER_KEY = "EnterKeyInvoked_unknown";
    private static final String APPLY_FILTERS = "applyFilters";
    private static final String CLEAR_FILTERS = "clearFilters";
    private static final String CHANGE_FILTER = "changeFilter";
    private static final String ACTION_FORM_CURRENT_ACTION = "{actionForm.currentAction}";
    private static final String ON_TEST_CHANGE = "onTestChange";
    private static final String ON_PROGRAM_CHANGE = "onProgramChange";
    private static final String EXPORT_TO_EXCEL = "exportToExcel";
    ///
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
	 * FormData get and set methods may be overwritten by the Form Bean editor.
	 */
	public static class ProgramOperationForm extends SanitizedFormData
	{

	}
	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="manageProgram.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success", path = "manageProgram.do")
	})
	protected Forward begin()
	{
		return new Forward("success");
	}
	
	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="programStatus.jsp"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success", path = "programStatus.jsp")
	}, 
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "logout.do"))
	protected Forward manageProgram()
	{
		getLoggedInUserPrincipal();
		
		getUserDetails();

		setupUserPermission();
		
		List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
        this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
		
		return new Forward("success");
	}

///program status changes:Start
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="programStatus_hierarchy.jsp")
	})
	protected Forward programStatusOrgNodeHierarchyList(ProgramOperationForm form){

		String jsonTree = "";
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		//String contentType = CONTENT_TYPE_JSON;
		try {
			BaseTree baseTree = new BaseTree ();
			Integer leafNodeCategoryId = getLeafNodeCategoryId(this.userName,this.customerId, this.userManagement);
			baseTree.setLeafNodeCategoryId(leafNodeCategoryId);
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
			System.out.println(jsonTree);
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
			System.err.println("Exception while processing programStatusOrgNodeHierarchyList.");
			e.printStackTrace();
		}

		return null;

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
    
    @Jpf.Action
    protected Forward getProgramStatusDetailsForOrgNode() {
    	Integer selectedProgramId = null; 
    	Integer selectedTestId = null;
    	String programIdStr = getRequest().getParameter("selectedProgramId");
    	String testIdStr = getRequest().getParameter("selectedTestId");
    	if(programIdStr!=null && !"null".equals(programIdStr) && !"".equals(programIdStr))
    		selectedProgramId = Integer.valueOf((String)getRequest().getParameter("selectedProgramId"));
    	if(testIdStr!=null	&& !"null".equals(testIdStr) && !"".equals(testIdStr))
    		selectedTestId= Integer.valueOf((String)getRequest().getParameter("selectedTestId"));
    	
    	Integer selectedOrgNodeId = Integer.valueOf((String)getRequest().getParameter("selectedOrgNodeId"));  		
    	String selectedOrgNodeName = getRequest().getParameter("selectedOrgNodeName");
    	boolean isBottomTwoLevels = getRequest().getParameter("isBottomTwoLevels").equalsIgnoreCase("true");
    	String subtestStatusAction = getRequest().getParameter("subtestStatusAction");
    	
    	
    	ProgramStatusInfo programStatusInfo = new ProgramStatusInfo();
    	
    	try{
    		programStatusInfo = buildProgramList(selectedProgramId,programStatusInfo);
	        
	        if (this.programList.size() > 0)
	        {
	            if(selectedProgramId == null){
	            	selectedProgramId = (Integer)programList.keySet().iterator().next();
	            }
                this.getRequest().setAttribute("selectedProgramId",selectedProgramId);
                programStatusInfo.setSelectedProgramId(selectedProgramId);
                programStatusInfo = buildTestList(selectedProgramId,selectedTestId,programStatusInfo);
	            
	            if (this.testList.size() > 0)
	            {
	            	 if(selectedTestId == null || (String)testList.get(selectedTestId) == null){
	                     selectedTestId = (Integer)testList.keySet().iterator().next();
	                 }
	            	 programStatusInfo.setSelectedTestId(selectedTestId);
	                 programStatusInfo = buildSubtestList(selectedProgramId,selectedOrgNodeId,selectedTestId,selectedOrgNodeName,isBottomTwoLevels,programStatusInfo);
	            }
	        }
    	}
    	catch(CTBBusinessException e){
    		e.printStackTrace();
    	}
		
    	createGson(programStatusInfo);
    	
    	return null;
	}
    private ProgramStatusInfo buildProgramList(Integer programId,ProgramStatusInfo programStatusInfo) throws CTBBusinessException{
    	this.programList = new LinkedHashMap();
    	String programName = null;
        ProgramData pd = this.programStatus.getActiveProgramsForUser(this.userName, null, null, null);
        Program[] programs = pd.getPrograms();
        for(int i=0; i<programs.length; i++){
        	this.programList.put(programs[i].getProgramId(), programs[i].getProgramName());
        }
        
        if(this.programList.size() > 0){
            // select first program if none selected
            Integer selectedProgramId = programId;
            if(selectedProgramId == null){
                selectedProgramId = (Integer)this.programList.keySet().iterator().next();
            }
            programStatusInfo.setMultiplePrograms(new Boolean(this.programList.size() > 1));
            programName = (String)this.programList.get(selectedProgramId);
            programStatusInfo.setProgramName(programName);
            
        }
        programStatusInfo.setNoPrograms(new Boolean(this.programList.size() == 0));
        
        return programStatusInfo;
    }
    
    private ProgramStatusInfo buildTestList(Integer programId, Integer selectedTestId, ProgramStatusInfo programStatusInfo) throws CTBBusinessException{
    	String testName = null;
    	this.testList = new LinkedHashMap();
    	SortParams sort = FilterSortPageUtils.buildSortParams(FilterSortPageUtils.TEST_DEFAULT_SORT, FilterSortPageUtils.ASCENDING);

        TestElementData tests = this.programStatus.getTestsForProgram(this.userName, programId, null, null, sort);
        TestElement[] testElements = tests.getTestElements();
        for(int i=0; i<testElements.length; i++){
        	this.testList.put(testElements[i].getItemSetId(), testElements[i].getItemSetName());
        }
        
        if(this.testList.size() > 0){
            // select first test if no test is selected or selected test is not there
            if(selectedTestId == null || (String)this.testList.get(selectedTestId) == null){
                selectedTestId = (Integer)this.testList.keySet().iterator().next();
             }
            testName =(String)this.testList.get(selectedTestId);
            programStatusInfo.setTestName(testName);
            programStatusInfo.setTestList(testElements);
        }
        programStatusInfo.setNoTest(new Boolean(this.testList.size() == 0));
        programStatusInfo.setSingleTest(new Boolean(this.testList.size() == 1));
        programStatusInfo.setMultipleTests(new Boolean(this.testList.size() > 1));
        
        return programStatusInfo;
    }
    
    private ProgramStatusInfo buildSubtestList(Integer programId,Integer selectedOrgNodeId,Integer selectedTestId, String selectedOrgNodeName, boolean isBottomTwoLevels,ProgramStatusInfo programStatusInfo) throws CTBBusinessException{

        TestStatus[] subtestStatuses = this.programStatus.getSubtestStatusForProgram(this.userName, programId, selectedOrgNodeId, selectedTestId);
        TestStatus testStatusBean = this.programStatus.getTestStatusForProgram(this.userName, programId, selectedOrgNodeId, selectedTestId);
        SubtestStatus testStatus = new SubtestStatus(testStatusBean, Boolean.FALSE);
        this.subtestList = new ArrayList();
        SubtestStatus ss = null;
        Boolean hasClickableSubtest = Boolean.FALSE;
        for(int i=0; i<subtestStatuses.length; i++){
            ss = new SubtestStatus(subtestStatuses[i], isBottomTwoLevels);
            if(ss.getHasAttemptedLink().booleanValue() ||
               ss.getHasCompletedLink().booleanValue() ||
               ss.getHasScheduledLink().booleanValue()){
                hasClickableSubtest = Boolean.TRUE;
            }
            this.subtestList.add(ss);
        }
        String title = this.getWebResource("manageProgram.testStatus.title") + " " + selectedOrgNodeName;
        programStatusInfo.setTestStatusTitle(title);
        programStatusInfo.setSubtestList(this.subtestList);
        programStatusInfo.setTestStatus(testStatus);
        programStatusInfo.setHasClickableSubtest(hasClickableSubtest);
        
        return programStatusInfo;
    }
    
    private boolean showSubtestStatus(String subtestStatusAction){
       
         return subtestStatusAction.equals(SHOW_STATUS) ||
         		subtestStatusAction.equals(APPLY_FILTERS) ||
         		subtestStatusAction.equals(CLEAR_FILTERS) ||
         		subtestStatusAction.equals(EXPORT_TO_EXCEL) ||
         		subtestStatusAction.equals(CHANGE_FILTER) ||
         		subtestStatusAction.equals(STATUS_PAGE_REQUESTED) ||
         		subtestStatusAction.equals(STATUS_PAGE_GO_BUTTON) ||
         		subtestStatusAction.equals(STATUS_SORT_REQUESTED) ||
         		subtestStatusAction.equals(STATUS_ENTER_KEY);
    }
    
    private ProgramStatusInfo buildSubtestStatus(Integer programId,Integer orgNodeId, Integer testId,Integer subtestId,String status,ProgramStatusInfo programStatusInfo) throws CTBBusinessException{

        String subtestName = getSubtestName(subtestId);
        programStatusInfo.setViewSubtestStatus(subtestName);
        
        String title = status + " ";
        title += this.getWebResource("manageProgram.studentsFor.title") + ": ";
        title += subtestName;
        programStatusInfo.setSessionsForTitle(title);
        
        // build status list
        this.statusList = new ArrayList();
        ProgramStatusSessionData data = 
          this.programStatus.getProgramStatusSessions(this.userName, 
                                                      programId, 
                                                      orgNodeId, 
                                                      testId, 
                                                      subtestId,
                                                      status, 
                                                      null, 
                                                      null, 
                                                      null);

            ProgramStatusSession[] statuses = data.getProgramStatusSessions();
            for(int i=0; i<statuses.length; i++){
                if(statuses[i] != null){
                    this.statusList.add(new TestStatusVO(statuses[i]));
                }
            }
        programStatusInfo.setAllowExport(new Boolean(statusList.size() < MAX_EXPORT));
        programStatusInfo.setStatusList(this.statusList);
        
        return programStatusInfo;
    }
    
    private String getWebResource(String key){
        ResourceBundle rb = ResourceBundle.getBundle("webResources");
        return rb.getString(key);
    }
    
    private String getSubtestName(Integer subtestId)
    {
        for (int i=0 ; i<this.subtestList.size() ; i++) {
            SubtestStatus ss = (SubtestStatus)this.subtestList.get(i);
            if (ss.getId().equals(subtestId.toString())) {
                return ss.getName();
            }
        }
        return null;
    }
    
    @Jpf.Action
        protected Forward viewSubtestStatus(ProgramOperationForm form) throws CTBBusinessException
        {   
	    	Integer selectedProgramId = Integer.valueOf((String)getRequest().getParameter("selectedProgramId"));
	    	Integer selectedTestId = Integer.valueOf((String)getRequest().getParameter("selectedTestId"));
	    	Integer selectedOrgNodeId = Integer.valueOf((String)getRequest().getParameter("selectedOrgNodeId"));
	    	Integer subtestId = Integer.valueOf(getRequest().getParameter("subtestId"));
	    	String status = getRequest().getParameter("status");
	    	ProgramStatusInfo programStatusInfo = new ProgramStatusInfo();

            programStatusInfo = buildSubtestStatus(selectedProgramId,selectedOrgNodeId,selectedTestId,subtestId,status,programStatusInfo);
            programStatusInfo.setPage("1");
            programStatusInfo.setRecords("10");
            programStatusInfo.setTotal("2");
            createGson(programStatusInfo);
            
            return null;
        }
    
    @Jpf.Action
    protected Forward getSubtestDetailsByTestId() throws CTBBusinessException
    {   
    	Integer selectedProgramId = Integer.valueOf((String)getRequest().getParameter("selectedProgramId"));
    	Integer selectedOrgNodeId = Integer.valueOf((String)getRequest().getParameter("selectedOrgNodeId"));
    	Integer selectedTestId = Integer.valueOf((String)getRequest().getParameter("selectedTestId"));
    	String selectedOrgNodeName = getRequest().getParameter("selectedOrgNodeName");
    	boolean isBottomTwoLevels = getRequest().getParameter("isBottomTwoLevels").equalsIgnoreCase("true");
    	ProgramStatusInfo programStatusInfo = new ProgramStatusInfo();

        programStatusInfo = buildSubtestList(selectedProgramId,selectedOrgNodeId,selectedTestId,selectedOrgNodeName,isBottomTwoLevels,programStatusInfo);
        createGson(programStatusInfo);
        
        return null;
    }
    
	 private void createGson(ProgramStatusInfo  programStatusInfo){
		 	OutputStream stream = null;
			HttpServletRequest req = getRequest();
			HttpServletResponse resp = getResponse();
			try {
				try {
					Gson gson = new Gson();
					String json = gson.toJson(programStatusInfo);
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
				System.err.println("Exception while retrieving program status details.");
				e.printStackTrace();
			}
		} 
	 
	 @Jpf.Action(
				validationErrorForward = @Jpf.Forward(name = "failure", path = "logout.do")
			)
		    protected Forward exportToExcel() throws CTBBusinessException
		    {
		        HttpServletResponse resp = this.getResponse();        
		        String bodypart = "attachment; filename=\"ProgramStatus.xls\" ";
		        
		        resp.setContentType("application/vnd.ms-excel");
		        resp.setHeader("Content-Disposition", bodypart);      
		        resp.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");        
		        resp.setHeader("Cache-Control", "cache");
		        resp.setHeader("Pragma", "public");      
		          
		        Integer subtestId = Integer.valueOf(getRequest().getParameter("subtestId"));
		        String status = getRequest().getParameter("status");
		        String subtestName = getSubtestName(subtestId);
		        List exportList = new ArrayList();
		        Integer programId = Integer.valueOf((String)getRequest().getParameter("selectedProgramId"));;
		        Integer orgNodeId = Integer.valueOf((String)getRequest().getParameter("selectedOrgNodeId"));
		        Integer testId = Integer.valueOf((String)getRequest().getParameter("selectedTestId"));
		        String selectedProgramName = getRequest().getParameter("selectedProgramName");
		        String selectedOrgNodeName = getRequest().getParameter("selectedOrgNodeName");
		        String selectedTestName = getRequest().getParameter("selectedTestName");
		        
		        ProgramStatusSessionData data = 
		          this.programStatus.getProgramStatusSessions(this.userName, 
		                                                      programId, 
		                                                      orgNodeId, 
		                                                      testId, 
		                                                      subtestId,
		                                                      status, 
		                                                      null, 
		                                                      null, 
		                                                      null);
		            ProgramStatusSession[] statuses = data.getProgramStatusSessions();
		            for(int i=0; i<statuses.length; i++){
		                if(statuses[i] != null){
		                    exportList.add(new TestStatusVO(statuses[i]));
		                }
		            }
		        
		        try {
		            resp.flushBuffer();
		            ProgramStatusReportVO vo = new ProgramStatusReportVO();
		            vo.setCustomer(this.customerName);
		            vo.setProgram(selectedProgramName);
		            vo.setOrganization(selectedOrgNodeName);
		            vo.setTest(selectedTestName);
		            vo.setSubtest(subtestName);
		            vo.setSubtestStatus(status);
//		            vo.setSessionNameFilter(form.testStatusFilter.getSessionNameFilterType());
		            //vo.setSessionNameValue(form.testStatusFilter.getSessionName());
//		            vo.setSessionNumberFilter(form.testStatusFilter.getSessionNumberFilterType());
		           // vo.setSessionNumberValue(form.testStatusFilter.getSessionNumber());
//		            vo.setLoginIdFilter(form.testStatusFilter.getLoginIdFilterType());
		           // vo.setLoginIdValue(form.testStatusFilter.getLoginId());
//		            vo.setPasswordFilter(form.testStatusFilter.getPasswordFilterType());
		           // vo.setPasswordValue(form.testStatusFilter.getPassword());
//		            vo.setAccessCodeFilter(form.testStatusFilter.getAccessCodeFilterType());
		           // vo.setAccessCodeValue(form.testStatusFilter.getAccessCode());
		            vo.setSubtestStatusList(exportList);
		            ProgramStatusReportUtils util = new ProgramStatusReportUtils(vo, resp.getOutputStream());
		            util.generateReport();
		        }
		        catch(Exception e){
		            e.printStackTrace();
		        }
		        
		        return null;
		    }
  ///program status changes:End  
    
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
	    
	    @Jpf.Action(forwards={
	    		@Jpf.Forward(name = "success", path ="manageProgram.do")
		})
	    protected Forward assessments_programStatus()
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
            this.customerName = user.getCustomer().getCustomerName();
            this.customerId = customer.getCustomerId();
            getSession().setAttribute("customerId", customerId); 
            getSession().setAttribute("customerName", customerName);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
    }
    
private void setUpAllUserPermission(CustomerConfiguration [] customerConfigurations) {
    	
    	boolean hasBulkStudentConfigurable = false;
    	boolean hasBulkStudentMoveConfigurable = false;
    	boolean hasOOSConfigurable = false;
    	boolean laslinkCustomer = false;
    	boolean adminUser = isAdminUser();
    	boolean hasUploadDownloadConfig = false;
    	boolean hasProgramStatusConfig = false;
    	boolean hasScoringConfigurable = false;
    	boolean hasLicenseConfiguration= false;
    	boolean TABECustomer = false;
    	String roleName = this.user.getRole().getRoleName();
    	boolean adminCoordinatorUser = isAdminCoordinatotUser();
    	boolean hasResetTestSession = false;
    	boolean hasResetTestSessionForAdmin = false;
    	boolean isOKCustomer = false;
    	boolean isGACustomer = false;
    	boolean isTopLevelAdmin = new Boolean(isTopLevelUser() && isAdminUser());
    	
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
				// For LasLink Customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")
						&& cc.getDefaultValue().equals("T")) {
	            	laslinkCustomer = true;
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
				if ((cc.getCustomerConfigurationName().equalsIgnoreCase("GA_Customer") 
						&& cc.getDefaultValue().equalsIgnoreCase("T")) && 
						((cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID") 
						&& cc.getDefaultValue().equalsIgnoreCase("T"))	|| 
						(cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID_2") 
								&& cc.getDefaultValue().equalsIgnoreCase("T")))){
					isGACustomer = true;
					continue;
				}
			}
			
		}
		this.getSession().setAttribute("showReportTab", new Boolean(userHasReports().booleanValue() || laslinkCustomer));
		this.getSession().setAttribute("isBulkAccommodationConfigured",new Boolean(hasBulkStudentConfigurable));
		this.getSession().setAttribute("isBulkMoveConfigured",new Boolean(hasBulkStudentMoveConfigurable));
		this.getSession().setAttribute("isOOSConfigured",new Boolean(hasOOSConfigurable));
		this.getSession().setAttribute("hasUploadDownloadConfigured",new Boolean(hasUploadDownloadConfig && adminUser));
		this.getSession().setAttribute("hasProgramStatusConfigured",new Boolean(hasProgramStatusConfig && adminUser));
		this.getSession().setAttribute("hasScoringConfigured",new Boolean(hasScoringConfigurable));
		this.getSession().setAttribute("hasLicenseConfigured",new Boolean(hasLicenseConfiguration && adminUser));
		this.getSession().setAttribute("adminUser", new Boolean(adminUser));
		boolean validUser = (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || 
        		roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
		this.getSession().setAttribute("canRegisterStudent", new Boolean(TABECustomer && validUser));
		this.getRequest().setAttribute("isLasLinkCustomer", laslinkCustomer);
		this.getSession().setAttribute("hasRapidRagistrationConfigured", new Boolean(TABECustomer&&(adminUser || adminCoordinatorUser) ));
		this.getSession().setAttribute("hasResetTestSession", new Boolean((hasResetTestSession && hasResetTestSessionForAdmin) && ((isOKCustomer && isTopLevelAdmin)||(laslinkCustomer && isTopLevelAdmin)||(isGACustomer && adminUser))));
	 
     	this.getSession().setAttribute("showDataExportTab",laslinkCustomer);
		

}
    
	private void setupUserPermission()
	{
        CustomerConfiguration [] customerConfigs = getCustomerConfigurations(this.customerId);
        
        setUpAllUserPermission(customerConfigs);
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
    
    private boolean isAdminCoordinatotUser() 
    {               
        String roleName = this.user.getRole().getRoleName();        
        return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR); 
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
    
	 /**
     * getOrganizationNodes
     */    
    public static Integer getLeafNodeCategoryId(String userName, Integer customerId, UserManagement userManagement) throws CTBBusinessException {    
    	Integer leafNodeCategoryId = new Integer(0);
       
    	leafNodeCategoryId = userManagement.getLeafNodeCategoryId(userName, customerId);
    	
      
        return leafNodeCategoryId;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// END OF SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
	
}