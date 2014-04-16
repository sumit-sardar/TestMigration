package studentScoringOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore.Entry;
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
import utils.JsonUtils;
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
import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.QuestionAnswerData;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.RubricViewData;
import com.ctb.bean.testAdmin.ScorableCRAnswerContent;
import com.ctb.bean.testAdmin.ScorableItem;
import com.ctb.bean.testAdmin.ScorableItemData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.control.crscoring.TestScoring;
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
	
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;
	
		
    @Control()
    private com.ctb.control.testAdmin.ScheduleTest scheduleTest;
    
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;
    
    @Control()
    private com.ctb.control.crscoring.TestScoring testScoring;
    
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.CRScoring scoring;
    
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
		try {
			explicitlyInitializeAllControls();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	protected Forward getStudentCountForOrgNode(StudentSessionScoringForm form) throws IOException, ClassNotFoundException{
		explicitlyInitializeAllControls();
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
	protected Forward getStudentForScoringGrid(StudentSessionScoringForm form) throws IOException, ClassNotFoundException{
		explicitlyInitializeAllControls();
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
    protected Forward getSessionForScoringGrid(StudentSessionScoringForm form) throws IOException, ClassNotFoundException{
		explicitlyInitializeAllControls();
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
	protected Forward findItemDetail(StudentSessionScoringForm form) throws IOException, ClassNotFoundException{
		explicitlyInitializeAllControls();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		resp.setCharacterEncoding("UTF-8");
		Base base = new Base();
		Integer testAdminId = Integer.parseInt(getRequest().getParameter("testAdminId"));
		ScorableItemData siData = null;
		siData = ItemScoringUtils.getItemsByTestSession(testScoring, null, null, null, testAdminId);
		
		List<ScorableItem> itemList = ItemScoringUtils.buildItemList(siData);
		
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
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward populateGridDropDowns(StudentSessionScoringForm form) throws IOException, ClassNotFoundException{
		explicitlyInitializeAllControls();
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
	protected Forward beginDisplayStudItemList(StudentSessionScoringForm form) throws IOException, ClassNotFoundException
	{ 
		explicitlyInitializeAllControls();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		Integer rosterId = Integer.parseInt(getRequest().getParameter("rosterId"));
		Integer itemSetIdTC = Integer.parseInt(getRequest().getParameter("itemSetIdTC"));
		HashMap<Integer,ScorableItem> incompleteItemSetIdMap = new HashMap<Integer, ScorableItem>();
		HashMap<Integer,ScorableItem> totalItemSetIdMap = new HashMap<Integer, ScorableItem>();

		try {

			//can be used for immediate report button
			ScorableItemData sid = getTestItemForStudent(rosterId, itemSetIdTC, null, null);
			List<ScorableItem> itemList = buildItemList(sid);
			String status = "T";
			for (ScorableItem si : itemList){

				if(si.getScoreStatus().equalsIgnoreCase("incomplete") && si.getAnswered().equalsIgnoreCase("answered")){
					incompleteItemSetIdMap.put(si.getItemSetId(),si);
				}
				totalItemSetIdMap.put(si.getItemSetId(),si);
				
				//System.out.println("Parent product id :> " + si.getParentProductId());
			}

			for (Integer id : totalItemSetIdMap.keySet()){
				if(incompleteItemSetIdMap.get(id) == null){
					System.out.println("status true");
					status = "T";
					break;
				}else{
					status ="F";
					System.out.println("status false");
				}

			}


			
			try{
				
				String completionStatus = scoring.getScoringStatus(rosterId,itemSetIdTC);
				Boolean scoringButton = false;
				if(completionStatus.equals("CO")){
					 scoringButton = true;
				}else{
					 scoringButton = false;
				}
				Base base = new Base();
				base.setScorableItems(itemList);
				
				base.setPage("1");
				base.setRecords("10");
				base.setTotal("2");
				base.setProcessScoreBtn(scoringButton.toString());
				
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
	protected Forward getStudentListForScoreByStudent(StudentSessionScoringForm form) throws IOException, ClassNotFoundException{
		explicitlyInitializeAllControls();
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
	
	
	@Jpf.Action(forwards={
    		@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward getStudentListForItem(StudentSessionScoringForm form) throws IOException, ClassNotFoundException{
		explicitlyInitializeAllControls();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		resp.setCharacterEncoding("UTF-8"); 
		Base base = new Base();
		RosterElementData reData = null;
		Integer testAdminId = Integer.parseInt(getRequest().getParameter("testAdminId"));
		Integer itemSetId = Integer.parseInt(getRequest().getParameter("itemSetId"));
		String itemId = getRequest().getParameter("itemId");
		reData = ItemScoringUtils.getStudentsByItem(testScoring, null, null, null, testAdminId, itemSetId, itemId);
		List<RosterElement> studentList = buildStudentList(reData);
		
		base.setPage("1");
		base.setRecords("10");
		base.setTotal("2");
		base.setScoreByStudentList(studentList);
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
	
	 /**
	 * Included for rubric View
	 */
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward showQuestionAnswer(StudentSessionScoringForm form) throws IOException, ClassNotFoundException{
		explicitlyInitializeAllControls();	
		String jsonResponse = "";
		String itemId = getRequest().getParameter("itemId");
		Integer testRosterId = new Integer(getRequest().getParameter("testRosterId"));
		Integer itemSetId = new Integer(getRequest().getParameter("itemSetId"));
		String itemType = getRequest().getParameter("itemType");

		System.out.println("item id" + itemId);
		System.out.println("testRosterId id" + testRosterId);
		System.out.println("itemSetId id" + itemSetId);
		System.out.println("itemType id" + itemType);

	//	itemId = "0155662";//Had to make it static, since only 2 items are present in database now
		RubricViewData[] scr =  getRubricDetails(itemId);
		ScorableCRAnswerContent scrArea = getIndividualCRResponse(testScoring,
				userName, testRosterId, itemSetId, itemId, itemType);
		QuestionAnswerData qad = new QuestionAnswerData();
		qad.setRubricData(scr);
		qad.setScrContent(scrArea);
		
		try {
			jsonResponse = JsonUtils.getJson(qad, "questionAnswer",qad.getClass());

			System.out.println("jsonresponse" + jsonResponse);
		   HttpServletResponse resp = this.getResponse();     
		   resp.setContentType("application/json");
           resp.flushBuffer();
	        OutputStream stream = resp.getOutputStream();
	        stream.write(jsonResponse.getBytes());
	        stream.close();
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
		
	}
	
	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "") })
	protected Forward saveDetails(StudentSessionScoringForm form) throws IOException, ClassNotFoundException{
		System.out.println("Save details");
		explicitlyInitializeAllControls();
		String jsonMessageResponse = "";
		if (user == null) {
			getLoggedInUserPrincipal();
			getUserDetails();
		}
		String itemId = getRequest().getParameter("itemId");
		Integer itemSetId = Integer.valueOf(getRequest().getParameter(
		"itemSetId"));
		Integer testRosterId = Integer.valueOf(getRequest().getParameter(
		"rosterId"));
		Integer score = Integer.valueOf(getRequest().getParameter("score"));
		Integer itemSetIdTC = Integer.valueOf(getRequest().getParameter("itemSetIdTC"));
		try {
			String completeTD = null;
			Boolean isSuccess = this.testScoring.saveOrUpdateScore(user
					.getUserId(), itemId, itemSetId, testRosterId, score);
			// start- added for  Process Scores   button changes
			String completionStatus = scoring.getScoringStatus(testRosterId,itemSetIdTC);
			if (completionStatus.equals("CO")) {
				this.testSessionStatus.rescoreStudent(testRosterId);
			} else { // Change for immediate reporting requirements
				String completionStatusRosterAndTD = scoring.getStatusForRosterAndTD(testRosterId,itemSetId);
				if (completionStatusRosterAndTD.equals("CO")) {
					this.testSessionStatus.rescoreStudent(testRosterId);
					completeTD = "CO";
				} else {
					completeTD = "IN";
				}
			}


			ManageStudent ms = new ManageStudent();
			ms.setIsSuccess(isSuccess);
			ms.setCompletionStatus(completionStatus);
			ms.setCompletionStatusTD(completeTD);
			jsonMessageResponse = JsonUtils.getJson(ms, "SaveStatus",ms.getClass());
			// end - added for  Process Scores   button changes

			HttpServletResponse resp = this.getResponse();
			resp.setContentType("application/json");
			resp.flushBuffer();
			OutputStream stream = resp.getOutputStream();
			stream.write(jsonMessageResponse.getBytes());
			stream.close();

		} catch (CTBBusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "")
	})
public Forward rescoreStudent(StudentSessionScoringForm form) throws IOException, ClassNotFoundException{
		explicitlyInitializeAllControls();
		String jsonMessageResponse = "";
		if (user == null) {
			getUserDetails();
		}
		Integer testRosterId = Integer.valueOf(getRequest().getParameter(
		"rosterId"));

		System.out.println("rescore Student" + testRosterId);
        try {    
            this.testSessionStatus.rescoreStudent(testRosterId);
        	
        	ManageStudent ms = new ManageStudent();
			ms.setIsSuccess(true);
			jsonMessageResponse = JsonUtils.getJson(ms, "SaveStatus",ms.getClass());
            HttpServletResponse resp = this.getResponse();
			resp.setContentType("application/json");
			resp.flushBuffer();
			OutputStream stream = resp.getOutputStream();
			stream.write(jsonMessageResponse.getBytes());
			stream.close();
        }
        catch (Exception e) {
            e.printStackTrace();

			try {
	        	ManageStudent ms = new ManageStudent();
				ms.setIsSuccess(false);
				jsonMessageResponse = JsonUtils.getJson(ms, "SaveStatus",ms.getClass());
	            HttpServletResponse resp = this.getResponse();
				resp.setContentType("application/json");
				resp.flushBuffer();
				OutputStream stream = resp.getOutputStream();
				stream.write(jsonMessageResponse.getBytes());
				stream.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
        }                
        return null;
}
	 /**
     *getRubricDetails() for rubricView
     */
    private RubricViewData[] getRubricDetails(String itemId){

    	RubricViewData[] rubricDetailsData = null;
    	
    	try {	
			rubricDetailsData =  this.studentManagement.getRubricDetailsData(itemId);			 
    		System.out.println("rubricdetails data");
    	}
    	catch(CTBBusinessException be){
    		be.printStackTrace();
    	}
    	return rubricDetailsData;
    }
       
	private static ScorableCRAnswerContent getIndividualCRResponse(
			TestScoring testScoring, String userName, Integer testRosterId,
			Integer deliverableItemSetId, String itemId, String itemType) {
		ScorableCRAnswerContent answerArea = new ScorableCRAnswerContent();
		try {
			answerArea = testScoring.getCRItemResponseForScoring(userName,
						testRosterId, deliverableItemSetId, itemId, itemType);	
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		return answerArea;
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
            for (int i=0 ; i<testItemDetails.length ; i++){
            	ScorableItem itemDetail = (ScorableItem)testItemDetails[i];
                if (itemDetail != null) {
                	if("A".equals(itemDetail.getAnswered())){
                		itemDetail.setAnswered("Answered");
                		if("Incomplete".equalsIgnoreCase(itemDetail.getScoreStatus())) {
                			itemDetail.setScorePoint("-");
                		}
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
		
		List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
        this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
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
	protected Forward scoringOrgNodeHierarchyList() throws IOException, ClassNotFoundException{
		explicitlyInitializeAllControls();
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
    	boolean hasUploadConfig = false;
    	boolean hasDownloadConfig = false;
    	boolean hasUploadDownloadConfig = false;
    	boolean hasProgramStatusConfig = false;
    	boolean hasScoringConfigurable = false;
    	boolean hasLicenseConfiguration= false;
    	boolean TABECustomer = false;
    	boolean adminCoordinatorUser = isAdminCoordinatotUser(); //For Student Registration
    	String roleName = this.user.getRole().getRoleName();
    	boolean hasResetTestSession = false;
    	boolean hasResetTestSessionForAdmin = false;
    	boolean isOKCustomer = false;
    	boolean isGACustomer = false;
    	boolean isTopLevelAdmin = new Boolean(isTopLevelUser() && isAdminUser());
    	boolean laslinkCustomer = false;
    	boolean hasDataExportVisibilityConfig = false;
    	Integer dataExportVisibilityLevel = 1;
    	
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
					continue;
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
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")) {
	            	laslinkCustomer = true;
	            	continue;
	            }
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Data_Export_Visibility")) {
					hasDataExportVisibilityConfig = true;
					dataExportVisibilityLevel = Integer.parseInt(cc.getDefaultValue());
					continue;
	            }
			}
			
		}
		
		if (hasUploadConfig && hasDownloadConfig) {
			hasUploadDownloadConfig = true;
		}
		if (hasUploadDownloadConfig) {
			hasUploadConfig = false;
			hasDownloadConfig = false;
		}
		
		this.getSession().setAttribute("isBulkAccommodationConfigured",new Boolean(hasBulkStudentConfigurable));
		this.getSession().setAttribute("isBulkMoveConfigured",new Boolean(hasBulkStudentMoveConfigurable));
		this.getSession().setAttribute("isOOSConfigured",new Boolean(hasOOSConfigurable));
		this.getSession().setAttribute("hasUploadConfigured",new Boolean(hasUploadConfig && adminUser));
		this.getSession().setAttribute("hasDownloadConfigured",new Boolean(hasDownloadConfig && adminUser));
		this.getSession().setAttribute("hasUploadDownloadConfigured",new Boolean(hasUploadDownloadConfig && adminUser));
		this.getSession().setAttribute("hasProgramStatusConfigured",new Boolean(hasProgramStatusConfig && adminUser));
		this.getSession().setAttribute("hasScoringConfigured",new Boolean(hasScoringConfigurable));
		this.getSession().setAttribute("hasLicenseConfigured",new Boolean(hasLicenseConfiguration && adminUser));
		this.getSession().setAttribute("adminUser", new Boolean(adminUser));
		boolean validUser = (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || 
        		roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
		this.getSession().setAttribute("canRegisterStudent", new Boolean(TABECustomer && validUser));
		this.getSession().setAttribute("hasRapidRagistrationConfigured", new Boolean(TABECustomer&&(adminUser || adminCoordinatorUser) )); //For Student Registration
		this.getSession().setAttribute("hasResetTestSession", new Boolean((hasResetTestSession && hasResetTestSessionForAdmin) && ((isOKCustomer && isTopLevelAdmin)||(laslinkCustomer && (adminUser||adminCoordinatorUser))||(isGACustomer && adminUser))));
		//this.getSession().setAttribute("showDataExportTab",laslinkCustomer);
		this.getSession().setAttribute("showDataExportTab", new Boolean((laslinkCustomer && isTopLevelUser()) || (hasDataExportVisibilityConfig && checkUserLevel(dataExportVisibilityLevel))));
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
	
	private boolean isAdminCoordinatotUser() //For Student Registration
	{               
		String roleName = this.user.getRole().getRoleName();        
		return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR); 
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
		protected Forward eMetric_user_accounts_detail()
		{
	        try
	        {
	            String url = "/SessionWeb/userAccountFileOperation/accountFiles.do";
	            getResponse().sendRedirect(url);
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
	        @Jpf.Forward(name = "resetTestSessionLink", path = "services_resetTestSession.do"),
	        @Jpf.Forward(name = "manageLicensesLink", path = "services_manageLicenses.do"),
	        @Jpf.Forward(name = "installSoftwareLink", path = "services_installSoftware.do"),
	        @Jpf.Forward(name = "downloadTestLink", path = "services_downloadTest.do"),
	        @Jpf.Forward(name = "uploadDataLink", path = "services_uploadData.do"),
	        @Jpf.Forward(name = "downloadDataLink", path = "services_downloadData.do"),
	        @Jpf.Forward(name = "exportDataLink", path = "services_dataExport.do"),
			@Jpf.Forward(name = "viewStatusLink", path="services_viewStatus.do"),
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
    protected Forward broadcastMessage() throws IOException, ClassNotFoundException
    {	
        explicitlyInitializeAllControls();
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
    
    private void explicitlyInitializeAllControls() throws IOException, ClassNotFoundException{
    	ClassLoader cl = this.getClass().getClassLoader();
    	if(this.studentManagement == null){
    		this.studentManagement = (com.ctb.control.studentManagement.StudentManagementBean) java.beans.Beans
			.instantiate(cl,
					"com.ctb.control.studentManagement.StudentManagementBean");
    	}
    	if(this.testScoring == null){    		
    		this.testScoring = (com.ctb.control.crscoring.TestScoringBean) java.beans.Beans
					.instantiate(cl,
							"com.ctb.control.crscoring.TestScoringBean");
    	}
    	if(this.testSessionStatus == null){
    		this.testSessionStatus = (com.ctb.control.testAdmin.TestSessionStatusBean) java.beans.Beans
					.instantiate(cl,
							"com.ctb.control.testAdmin.TestSessionStatusBean");
    	}
    	if(this.scheduleTest == null){
    		this.scheduleTest = (com.ctb.control.testAdmin.ScheduleTestBean) java.beans.Beans
					.instantiate(cl,
						"com.ctb.control.testAdmin.ScheduleTestBean");
    	}    
    	if(this.scoring == null){
    		this.scoring = (com.ctb.control.db.CRScoring) java.beans.Beans
			.instantiate(cl,
					"com.ctb.control.db.CRScoringBean");
    	}
		if(this.orgnode == null){
    		this.orgnode = (com.ctb.control.db.OrgNode) java.beans.Beans
			.instantiate(cl,
					"com.ctb.control.db.OrgNodeBean");
    	}
		if(this.message == null){
    		this.message = (com.ctb.control.db.BroadcastMessageLog) java.beans.Beans
			.instantiate(cl,
					"com.ctb.control.db.BroadcastMessageLogBean");
    	}
		
    	
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
}