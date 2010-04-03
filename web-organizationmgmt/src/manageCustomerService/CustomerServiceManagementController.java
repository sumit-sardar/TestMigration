package manageCustomerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import utils.CustomerServiceFormUtils;
import utils.CustomerServiceSearchUtils;
import utils.FilterSortPageUtils;
import utils.MessageResourceBundle;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.ScheduleElement;
import com.ctb.bean.testAdmin.ScheduleElementData;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.StudentData;
import com.ctb.bean.testAdmin.StudentSessionStatusData;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.web.sanitizer.JavaScriptSanitizer;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.PagerSummary;

import dto.Message;
import dto.ScheduleElementVO;
import dto.StudentProfileInformation;
import dto.StudentSessionStatusVO;
import dto.TestSessionVO;




@Jpf.Controller
public class CustomerServiceManagementController extends PageFlowController {

	private static final long serialVersionUID = 1L;
	public String pageTitle = null;

	private static final String MODULE_STUDENT_TEST_SESSION   = "moduleStudentTestSession";
	private static final String MODULE_TEST_SESSION  = "moduleTestSession";
	private static final String MODULE_NONE           = "moduleNone";
	private static final String ACTION_DEFAULT        = "defaultAction";
	private static final String ACTION_FIND_TESTSESSION      = "findTestSession";
	private static final String ACTION_FORM_ELEMENT   = "{actionForm.actionElement}";
	private static final String ACTION_CURRENT_ELEMENT   = "{actionForm.currentAction}";

	private static final String ACTION_APPLY_SEARCH   = "applySearch";
	private static final String ACTION_CLEAR_SEARCH   = "clearSearch";
	private static final String ACTION_ADD_ALL = "addAll";
	private static final String ACTION_SHOW_DETAILS = "showDetails";


	private String userName = null;
	private User user = null;
	private CustomerServiceManagementForm savedForm = null;
	private boolean searchApplied = false;
	private String selectedModuleFind = null;

	private List<TestSessionVO> testSessionList = null; ;
	private List subtestList = null;
	private List<ScheduleElementVO> testDeliveryItemList = null;	
	private List<StudentSessionStatusVO> studentStatusDetailsList = null;
	private List studentList = null;
	private HashMap  subtestNameToIndexHash = null;
	//private List selectedStudents = null;
	private HashMap studentsOnPage   = null;
	private HashMap selectedStudents = null;
	private List studentTestStatusDetailsList = null;
	private List showStudentDeatilsList = null;
	private HashMap studentStatusMap = null;
	PagerSummary testSessionPagerSummary = null;
	PagerSummary  subtestPagerSummary = null;
	private String userTimeZone = null;

	private StudentProfileInformation studentProfileInformation;

	@Control()
	private com.ctb.control.userManagement.UserManagement userManagement;

	@Control()
	private com.ctb.control.customerServiceManagement.CustomerServiceManagement customerServiceManagement;

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

	protected global.Global globalApp;
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "defaultAction.do")
	})
	protected Forward begin()
	{   

		System.out.println("restest");
		return new Forward("success");
	}

	/**
	 * the default action, set to beginFindUser.do 
	 * @jpf:action
	 * @jpf:forward name="success" path="beginFindUser.do"
	 * @jpf:forward name="newUser" path="beginChangeMyPassword.do" 
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "beginFindTestSessionByStudent.do") 

	})
	protected Forward defaultAction(CustomerServiceManagementForm form)
	{
		initialize(ACTION_FIND_TESTSESSION);


		return new Forward("success");
	}



	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "findTestSessionByStudent.do")
	})
	protected Forward beginFindTestSessionByStudent(){

		CustomerServiceManagementForm form = initialize(ACTION_FIND_TESTSESSION);

		System.out.println("selected tab..."+ form.getSelectedTab());

		form.setSelectedStudentId(null); 
		form.setSelectedTab(MODULE_STUDENT_TEST_SESSION);
		form.setSelectedStudentLoginId(null);        
		clearMessage(form);

		this.searchApplied = false;

		return new Forward("success", form);
	}


	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "reopen_subtest.jsp"),
			@Jpf.Forward(name = "findSubtestByTestSessionId",
					path = "findSubtestByTestSessionId.do"),
			@Jpf.Forward(name="changeSubtest",
					path="changeSubtest.do") ,
			@Jpf.Forward(name = "showStudentTestStatusDetails",
					path = "showStudentTestStatusDetails.do"),
			@Jpf.Forward(name = "showDetails",
					path = "showDetails.do")

	})
	protected Forward findTestSessionByStudent(CustomerServiceManagementForm form)
	{        
		this.getRequest().setAttribute("isReopenTestSession", Boolean.TRUE);
		form.validateValues();
		String actionElement = form.getActionElement();            
		String currentAction = form.getCurrentAction();  
		form.resetValuesForAction(actionElement, currentAction);
		currentAction = form.getCurrentAction(); 

		String selectedTab = form.getSelectedTab();
		selectedTab = JavaScriptSanitizer.sanitizeString(selectedTab);
		if (! selectedTab.equals(this.selectedModuleFind))
		{
			initFindTestSessionByStudent(selectedTab, form);                
		}
		boolean applySearch = initSearch(form);

		if (form.getTestSessionPageRequested().intValue() 
				> form.getTestSessionMaxPage().intValue()) {

			this.subtestList = null;
		}

		if (currentAction.equals(globalApp.ACTION_APPLY_SEARCH)) {
			this.studentProfileInformation = form.getStudentProfile().createClone();
			initPagingSorting(form);
		}

		if (currentAction.equals("findSubtestByTestSessionId")) {
			return new Forward(currentAction, form);
		}  

		if (currentAction.equals("showStudentTestStatusDetails")) {
			return new Forward(currentAction, form);    
		}

		//form.validateValues();
		
		if (currentAction.equals("changeSubtest")){
			//this.savedForm.setSelectedSubtestName(form.getSelectedSubtestName());
			return new Forward(currentAction,form);
		}

		if (currentAction.equals("showDetails")){
			System.out.println("showDetails" );
			return new Forward(currentAction,form);
		}

		TestSessionData tsData = null;	
		Student studentData = null;
		StudentSessionStatusData studentSessionStatusData = null;
		ScheduleElementData scheduleElementData = null;

		if (this.selectedModuleFind.equals(MODULE_STUDENT_TEST_SESSION))
		{
			if (applySearch) {

				this.searchApplied = false;
				try {
					tsData = findTestSessionListByStudent(form);

				} catch (CTBBusinessException be) {
					be.printStackTrace();
					String msg = MessageResourceBundle.getMessage(be.getMessage());
					form.setMessage(Message.FIND_NO_TESTDATA_RESULT, msg, Message.INFORMATION);
				}

				if ((tsData != null) && (tsData.getFilteredCount().intValue() == 0)){
					this.getRequest().setAttribute("searchResultEmpty", 
							Message.FIND_NO_TESTDATA_RESULT);
				}
			}
		}


		if (this.selectedModuleFind.equals(MODULE_TEST_SESSION))
		{
			if (applySearch){

				this.searchApplied = false;

				try {

					scheduleElementData = findSubtestListBySession(form);

				} catch (CTBBusinessException be) {

					be.printStackTrace();
					String msg = MessageResourceBundle.getMessage(be.getMessage());
					form.setMessage(Message.FIND_TEST_SESSION_TITLE, msg, Message.INFORMATION);
				}

				if((studentSessionStatusData != null) && (studentSessionStatusData.getFilteredCount().intValue() == 0)){
					this.getRequest().setAttribute("searchResultEmpty",Message.FIND_NO_TESTDATA_RESULT);
				}
			}
		}



		setFormInfoOnRequest(form);

		//keep state of request attribute
		if (testSessionPagerSummary != null) {



			this.getRequest().setAttribute("testSessionResult", "true");        
			this.getRequest().setAttribute("testSessionPagerSummary", testSessionPagerSummary);
		}
		if (subtestPagerSummary != null) {


			this.getRequest().setAttribute("subtestResult", "true");        
			this.getRequest().setAttribute("subtestPagerSummary", subtestPagerSummary);
		}

		return new Forward("success");
	}


	//step 3 of STUDENT tab
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "reopen_subtest.jsp")
	})
	public Forward findSubtestByTestSessionId(
			manageCustomerService.CustomerServiceManagementController.CustomerServiceManagementForm form) {

		SortParams sort = null;
		FilterParams filter = null;
		PageParams page = null;
		StudentSessionStatusData sstData = null;
		form.validateValues(); 
		String actionElement = form.getActionElement();
		form.resetValuesForAction(actionElement, ACTION_FIND_TESTSESSION); 
		Integer testAdminId = form.getSelectedTestSessionId();
		Integer testRosterId = null;

		this.getRequest().setAttribute("isReopenTestSession", Boolean.TRUE);
		
		for (TestSessionVO testSessionVO : this.testSessionList) {

			if (testSessionVO.getTestAdminId().intValue() == testAdminId.intValue()) {
				testRosterId = testSessionVO.getTestRosterId();
				//form.setSelectedAccessCode(testSessionVO.getAccessCode());
				form.setSelectedTestAdminName(testSessionVO.getTestAdminName());
				form.setCustomerId(testSessionVO.getCustomerId());
				form.setCreatorOrgNodeId(testSessionVO.getCreatorOrgNodeId());
				form.setSelectedTestSessionId(testSessionVO.getTestAdminId());
				break;
			}
		}

		page = FilterSortPageUtils.buildPageParams(form.getSubtestPageRequested(), FilterSortPageUtils.PAGESIZE_20);
		sort = FilterSortPageUtils.buildSortParams(form.getSubtestSortColumn(), form.getSubtestSortOrderBy());

		try {
			sstData = CustomerServiceSearchUtils.getSubtestListForStudent(
					customerServiceManagement, testRosterId, filter, page, sort);

		} catch (CTBBusinessException be) {
			be.printStackTrace();
			String msg = MessageResourceBundle.getMessage(be.getMessage());
			form.setMessage(Message.FIND_NO_TESTDATA_RESULT, msg, Message.INFORMATION);
		}

		if (sstData != null && sstData.getFilteredCount().intValue() > 0) {

			this.searchApplied = true;
			this.subtestList = CustomerServiceSearchUtils.buildSubtestList(sstData,this.userTimeZone);
			subtestPagerSummary = 
				CustomerServiceSearchUtils.buildSubtestDataPagerSummary(sstData, form.getSubtestPageRequested()); 
			form.setSubtestMaxPage(sstData.getFilteredPages());  
			this.getRequest().setAttribute("subtestResult", "true");        
			this.getRequest().setAttribute("subtestPagerSummary", subtestPagerSummary);

		} else {
			this.subtestList = null;
			String searchMessage = Message.FIND_NO_TESTDATA_RESULT;
			this.getRequest().setAttribute("searchResultEmpty", searchMessage);
		}

		if( form.getSubtestPageRequested() == null ||  (
				sstData != null && sstData.getFilteredPages() == null)) {

			form.setSubtestPageRequested(new Integer(1));

		}
		else {
			if (sstData != null) {
				if ( form.getSubtestPageRequested().intValue() > sstData.getFilteredPages().intValue() ) {

					form.setSubtestPageRequested(sstData.getFilteredPages());

				}
			}
		}

		if(form.getSelectedItemSetId() != null) {
			showStudentTestStatusDetails(form.getSelectedItemSetId());
		}

		setFormInfoOnRequest(form);

		if (testSessionPagerSummary != null) {

			this.getRequest().setAttribute("testSessionResult", "true");        
			this.getRequest().setAttribute("testSessionPagerSummary", testSessionPagerSummary);
		}

		if (subtestPagerSummary != null) {

			this.getRequest().setAttribute("subtestResult", "true");        
			this.getRequest().setAttribute("subtestPagerSummary", subtestPagerSummary);
		}

		return new Forward("success",form);
	}  


	//step 3 of SESSION tab
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "reopen_subtest.jsp")
	})
	public Forward changeSubtest(
			manageCustomerService.CustomerServiceManagementController.CustomerServiceManagementForm form) {
		SortParams sort = null;
		FilterParams filter = null;
		PageParams page = null;
		StudentSessionStatusData sstData = null;
		String subtestName = form.getSelectedSubtestName();
		Integer testAdminId = this.testDeliveryItemList.get(0).getTestAdminId();

		this.getRequest().setAttribute("isReopenTestSession", Boolean.TRUE);
		
		page = FilterSortPageUtils.buildPageParams(form.getSubtestPageRequested(), FilterSortPageUtils.PAGESIZE_20);

		try {
			sstData = CustomerServiceSearchUtils.getStudentListForSubTest(
					customerServiceManagement, testAdminId, Integer.valueOf(subtestName) , filter, page, sort);

		} catch (CTBBusinessException be) {
			be.printStackTrace();
			String msg = MessageResourceBundle.getMessage(be.getMessage());
			form.setMessage(Message.FIND_NO_TESTDATA_RESULT, msg, Message.INFORMATION);
		}

		if (sstData != null && sstData.getFilteredCount().intValue() > 0) {

			this.searchApplied = true;
			this.studentStatusDetailsList = CustomerServiceSearchUtils.buildSubtestList(sstData,this.userTimeZone);
			PagerSummary studentPagerSummary = 
				CustomerServiceSearchUtils.buildSubtestDataPagerSummary(sstData, form.getStudentPageRequested()); 
			form.setSubtestMaxPage(sstData.getFilteredPages());  
			this.getRequest().setAttribute("studentSearchResult", "true");        
			this.getRequest().setAttribute("studentPagerSummary", studentPagerSummary);

		} else {
			resetPageFlowLists();
			String searchMessage = Message.FIND_NO_TESTDATA_RESULT;
			this.getRequest().setAttribute("searchResultEmpty", searchMessage);
		}

		if( form.getSubtestPageRequested() == null ||  (
				sstData != null && sstData.getFilteredPages() == null)) {

			form.setSubtestPageRequested(new Integer(1));

		}
		else 
			if (sstData != null)
				if ( form.getSubtestPageRequested().intValue() > sstData.getFilteredPages().intValue() ) {

					form.setSubtestPageRequested(sstData.getFilteredPages());

				}

		return new Forward("success",form);
	}

	// Step 4 of SESSION tab
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "reopen_subtest.jsp")})
	public Forward showDetails(manageCustomerService.CustomerServiceManagementController.CustomerServiceManagementForm form)
	{

		SortParams sort = null;
		FilterParams filter = null;
		PageParams page = null;
		StudentSessionStatusData sstData = null;

		this.getRequest().setAttribute("isReopenTestSession", Boolean.TRUE);
		//populate step 4
		showStudentDeatilsList = null;
		ArrayList selectedStudentIdFromPage = this.getArrayListFromStringArray(form.getSelectedStudentItemId());
		if (selectedStudentIdFromPage !=null && selectedStudentIdFromPage.size() > 0 ){
			showStudentDeatilsList = new ArrayList();
		}
		for (Iterator it=this.studentsOnPage.keySet().iterator(); it.hasNext(); )
		{
			String studentIdOnPage = (String)it.next();
			if (selectedStudentIdFromPage.contains(studentIdOnPage))
			{
				StudentSessionStatusVO  test = (StudentSessionStatusVO)this.studentsOnPage.get(studentIdOnPage);
				showStudentDeatilsList.add(test);
			}
		}
		System.out.println("Size of Show Details......................"+showStudentDeatilsList.size());
		PagerSummary subtestDetailsPagerSummary = 
			buildSubtestDataPagerSummary(form.getSubtestPageRequested()); 
		//Added studentStatusMap
		this.studentStatusMap = getAllStudentHashMapForArrayList(showStudentDeatilsList);
		this.updateSelectedStudentItemDeatilsInForm(form);
		this.updateSelectedStudentsFromForm(form);
		form.setSubtestMaxPage(this.studentsOnPage.size());
		this.getRequest().setAttribute("subtestDetailsSearchResult", "true");        
		this.getRequest().setAttribute("subtestDetailsPagerSummary", subtestDetailsPagerSummary);
		return new Forward("success",form);
	}   

	//step 4 of STUDENT tab
	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "reopen_subtest.jsp") })
	public Forward showStudentTestStatusDetails(CustomerServiceManagementForm form) 
	{
		this.getRequest().setAttribute("isReopenTestSession", Boolean.TRUE);
		Integer itemSetId = form.getSelectedItemSetId();
		showStudentTestStatusDetails(itemSetId);	

		setFormInfoOnRequest(form);

		if (testSessionPagerSummary != null) {

			this.getRequest().setAttribute("testSessionResult", "true");        
			this.getRequest().setAttribute("testSessionPagerSummary", testSessionPagerSummary);
		}
		if (subtestPagerSummary != null) {


			this.getRequest().setAttribute("subtestResult", "true");        
			this.getRequest().setAttribute("subtestPagerSummary", subtestPagerSummary);
		}

		Forward forward = new Forward("success", form);
		return forward;
	}


	@ Jpf. Action (forwards = { 
			@ Jpf. Forward (name = "success" ,
					path = "findSubtestByTestSessionId.do")
	})
	protected  Forward reOpenSubtest(CustomerServiceManagementForm form) 
	{

		try  {
			this.getRequest().setAttribute("isReopenTestSession", Boolean.TRUE);
			StudentProfileInformation sDetails = (StudentProfileInformation)this.studentList.get(0);
			Integer studentId = sDetails.getStudentId(); 	
			CustomerServiceSearchUtils.reOpenSubtest (
					this.customerServiceManagement, 
					this.user,
					form.getRequestDescription(),
					form.getServiceRequestor(),
					form.getTicketId(),
					form.getSelectedTestSessionId(),
					form.getCustomerId(), 
					this.studentTestStatusDetailsList,
					form.getSelectedItemSetId(),
					form.getCreatorOrgNodeId(),
					studentId);

		} catch  (CTBBusinessException be) {

			String msg = MessageResourceBundle. getMessage (be.getMessage());
			form.setMessage(Message.TEST_ROSTER_UPDATION_FAILED , msg, Message. ERROR );

			return   new  Forward( "success" );
		}
		finally {
			form.setMessage(Message.TEST_ROSTER_UPDATION_TITLE, 
					Message.TEST_ROSTER_UPDATION_SUCCESS, Message.INFORMATION);

		}

		return   new  Forward( "success" );
	}


	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "reopen_subtest.jsp")
	})
	protected Forward reOpenSubtestForStudents(CustomerServiceManagementForm form) {

		try {
			this.getRequest().setAttribute("isReopenTestSession", Boolean.TRUE);
			CustomerServiceSearchUtils.reOpenSubtest( 
					this.customerServiceManagement ,
					this.user,
					form.getRequestDescription(),
					form.getServiceRequestor(),
					form.getTicketId(),
					form.getTestAdminId(),
					form.getCustomerId(),
					this.showStudentDeatilsList,
					Integer.valueOf(form.getSelectedSubtestName()),
					this.testDeliveryItemList.get(0).getOrgNodeId(),
					null);

		} catch (CTBBusinessException be) {

			String msg = MessageResourceBundle.getMessage(be.getMessage());
			form.setMessage(Message.TEST_ROSTER_UPDATION_FAILED, msg, Message.ERROR);

		}

		return new Forward("success");

	}

	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "reopen_subtest.jsp")
	})
	protected Forward selectAllStudents(CustomerServiceManagementForm form)
	{
		this.getRequest().setAttribute("isReopenTestSession", Boolean.TRUE);
		selectAllStudents(form.getTestAdminId(), Integer.valueOf(form.getSelectedSubtestName()),form); 
		return new Forward("success", form);
	}

	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "reopen_subtest.jsp")
	})
	protected Forward deselectAllStudents(CustomerServiceManagementForm form)
	{  
		this.getRequest().setAttribute("isReopenTestSession", Boolean.TRUE);
		deSelectAllStudents(222911, Integer.valueOf(form.getSelectedSubtestName()),form); 
		return new Forward("success", form);
	}




//	step 1 of STUDENT tab: find student search criteria
	private TestSessionData findTestSessionListByStudent(CustomerServiceManagementForm form) throws CTBBusinessException{

		SortParams sort = null;
		FilterParams filter = null;
		TestSessionData tsData = null;
		PageParams page = null;
		Student sData = null;
		String actionElement = form.getActionElement();

		form.validateValues();     
		form.resetValuesForAction(actionElement, ACTION_FIND_TESTSESSION);    


		String studentLoginId = form.getStudentProfile().getStudentLoginId();
		Boolean validInfo = true;

		validInfo = CustomerServiceFormUtils.verifyFormInformation(form);

		if (validInfo) {

			page = FilterSortPageUtils.buildPageParams(form.getTestSessionPageRequested(), FilterSortPageUtils.PAGESIZE_20);
			sort = FilterSortPageUtils.buildSortParams(form.getTestSessionSortColumn(), form.getTestSessionSortOrderBy());
			sData = CustomerServiceSearchUtils.searchStudentData(customerServiceManagement, this.userName, studentLoginId);
			StudentData studentData = null;


			if (sData != null) {
				form.setStudentProfile(new StudentProfileInformation(sData));
				form.setSelectedStudentId(sData.getStudentId());
				form.setSelectedStudentLoginId(sData.getUserName());
				Student[] students = new Student[1];
				students[0] = sData;
				studentData = new StudentData();
				studentData.setStudents(students,1);
				studentList = CustomerServiceSearchUtils.buildStudentList(studentData);
				tsData = CustomerServiceSearchUtils.getStudentTestSessionData(customerServiceManagement,
						this.userName, sData.getStudentId(),sData.getCustomerId(),form.getTestAccessCode()
						,filter,page,sort);
			}

			if (tsData != null && tsData.getFilteredCount().intValue() > 0) {

				this.searchApplied = true;
				this.testSessionList = CustomerServiceSearchUtils.buildTestSessionList(tsData);
				testSessionPagerSummary = 
					CustomerServiceSearchUtils.buildTestDataPagerSummary(tsData, form.getTestSessionPageRequested()); 
				form.setTestSessionMaxPage(tsData.getFilteredPages());  
				this.getRequest().setAttribute("testSessionResult", "true");        
				this.getRequest().setAttribute("testSessionPagerSummary", testSessionPagerSummary);

			} else {
				//empty the lists to show blank page
				resetPageFlowLists();
				String searchMessage = Message.FIND_NO_TESTDATA_RESULT;
				this.getRequest().setAttribute("searchResultEmpty", searchMessage);
			}
		} 


		if( form.getTestSessionPageRequested() == null ||  (
				tsData != null && tsData.getFilteredPages() == null)) {

			form.setTestSessionPageRequested(new Integer(1));

		}
		else 
			if (tsData != null)
				if ( form.getTestSessionPageRequested().intValue() > tsData.getFilteredPages().intValue() ) {

					form.setTestSessionPageRequested(tsData.getFilteredPages());

				}
		this.savedForm = form.createClone();

		return tsData;
	}


//	step 1 search of SESSION tab
	private ScheduleElementData findSubtestListBySession(CustomerServiceManagementForm form) throws CTBBusinessException{


		String actionElement = form.getActionElement();
		form.resetValuesForAction(actionElement, MODULE_TEST_SESSION);
		String studentLoginId = form.getStudentProfile().getStudentLoginId();
		Boolean validInfo = true;

		validInfo = CustomerServiceFormUtils.verifyFormInformation(form);
		PageParams page = FilterSortPageUtils.buildPageParams(form.getTestSessionPageRequested(), FilterSortPageUtils.PAGESIZE_20);

		SortParams sort = null;
		FilterParams filter = null;
		TestSessionData tsData = null;
		ScheduleElementData scheduleElementData = null;
		if(validInfo) {

			scheduleElementData = CustomerServiceSearchUtils.getTestDeliveryDataInTestSession(customerServiceManagement,this.userName,form.getTestAccessCode());
			if (scheduleElementData != null && scheduleElementData.getFilteredCount().intValue() > 0) {

				form.setSelectedTestAdminId(scheduleElementData.getElements()[0].getTestAdminId());
				this.searchApplied = true;
				this.testDeliveryItemList = CustomerServiceSearchUtils.buildTestDeliveritemList(scheduleElementData);

				this.createSubtestNameList(scheduleElementData.getElements());
				this.getRequest().setAttribute("testSessionResult", "true"); 

				boolean hideProductNameDropDown = this.testDeliveryItemList.size() <= 1;

				this.getRequest().setAttribute("hideProductNameDropDown", new Boolean(hideProductNameDropDown));

				if (hideProductNameDropDown) {
					form.setSelectedSubtestName(this.testDeliveryItemList.get(0).getItemSetName());
				}

			} else {
				resetPageFlowLists();
				String searchMessage = Message.FIND_NO_TESTDATA_RESULT;
				this.getRequest().setAttribute("searchResultEmpty", searchMessage);
			}

		}
		return scheduleElementData;
	}

	//Hash
	private List createSubtestNameList(ScheduleElement [] scheduleElement)
	{
		List result = new ArrayList();   
		this.subtestNameToIndexHash = new HashMap();
		for (int i=0; i< scheduleElement.length; i++) {
			String itemSetName = scheduleElement[i].getItemSetName();
			itemSetName = JavaScriptSanitizer.sanitizeString(itemSetName);            
			result.add(itemSetName);
			this.subtestNameToIndexHash.put(scheduleElement[i].getItemSetId(),itemSetName);
		}

		return result;
	}





	private void setFormInfoOnRequest(CustomerServiceManagementForm form) {
		this.getRequest().setAttribute("pageMessage", form.getMessage());
		this.getRequest().setAttribute("selectedTestSessionName", form.getSelectedTestAdminName());
		this.getRequest().setAttribute("selectedTestSessionId", form.getSelectedTestSessionId());
	}


	/**
	 * initialize
	 */
	private CustomerServiceManagementForm initialize(String action)
	{        
		getUserDetails();
		this.studentsOnPage = new HashMap();
		resetPageFlowLists();
		this.savedForm = new CustomerServiceManagementForm();
		this.savedForm.init(action);

		return this.savedForm;
	}

	/**
	 * getUserDetails
	 */
	private void getUserDetails()
	{
		java.security.Principal principal = getRequest().getUserPrincipal();
		if (principal != null) 
			this.userName = principal.toString();
		else            
			this.userName = (String) getSession().getAttribute("userName"); 
		userTimeZone = "GMT";

		try {
			this.user = this.userManagement.getUser(this.userName, 
					this.userName);

		}
		catch (Exception e) {
			e.printStackTrace();
		}        
		getSession().setAttribute("userName", this.userName);

		//set user time zone
		userTimeZone = this.user.getTimeZone();

		getSession().setAttribute("userTimeZone", userTimeZone); 
	}

	/**
	 * clearMessage
	 */
	private void clearMessage(CustomerServiceManagementForm form)
	{        

		form.clearMessage();
		this.savedForm.clearMessage();


	}

	/**
	 * initFindTestSessionByStudent
	 */
	private void initFindTestSessionByStudent(String selectedTab, CustomerServiceManagementForm form)
	{
		this.selectedModuleFind = selectedTab;

		if (this.selectedModuleFind.equals(MODULE_STUDENT_TEST_SESSION)) {
			clearStudentTestSessionSearch(form);    
		}

		if (this.selectedModuleFind.equals(MODULE_TEST_SESSION)) {
			clearTestSessionSearch(form);    
		}
		resetPageFlowLists();
	}

	/**
	 * clearUserProfileSearch
	 */
	private void clearStudentTestSessionSearch(CustomerServiceManagementForm form)
	{   
		this.searchApplied = false;

		form.clearSearch();    
		form.setCurrentAction(ACTION_DEFAULT);
		form.setActionElement(ACTION_DEFAULT);
		form.setSelectedStudentId(null);
		form.setSelectedStudentLoginId(null);
		form.setSelectedAccessCode(null);
		resetPageFlowLists();
	}

	/**
	 * clearHierarchySearch
	 */
	private void clearTestSessionSearch(CustomerServiceManagementForm form)
	{   
		this.searchApplied = false;

		form.clearSearch();  
		form.setCurrentAction(ACTION_DEFAULT);
		form.setActionElement(ACTION_DEFAULT);
		form.setSelectedAccessCode(null);
		form.setTestAccessCode(null);
		resetPageFlowLists();
	}

	/**
	 * initSearch
	 */
	private boolean initSearch(CustomerServiceManagementForm form)
	{
		boolean applySearch = false;
		String currentAction = form.getCurrentAction();

		if ((currentAction != null) && currentAction.equals(ACTION_APPLY_SEARCH))
		{
			applySearch = true;
			this.searchApplied = false;
			form.setTestSessionSortColumn(FilterSortPageUtils.REOPEN_TESTSESSION_DEFAULT_SORT);
			form.setTestSessionSortOrderBy(FilterSortPageUtils.ASCENDING);      
			form.setTestSessionPageRequested(new Integer(1));    
			form.setTestSessionMaxPage(new Integer(1)); 
			resetPageFlowLists();
			this.subtestPagerSummary = null;
			this.testSessionPagerSummary = null;
		}

		if ((currentAction != null) && currentAction.equals(ACTION_CLEAR_SEARCH)) {
			applySearch = false;
			this.searchApplied = false;
			form.clearSearch();
		}

		if (this.searchApplied)	{
			applySearch = true;
		}
		else {
			form.setSelectedStudentLoginId(null);
			form.setSelectedAccessCode(null);
		}

		return applySearch;
	}

	/**
	 * initPagingSorting
	 */
	private void initPagingSorting(CustomerServiceManagementForm form)
	{
		String actionElement = form.getActionElement();

		if ( (actionElement.indexOf("testSessionPageRequested") > 0) 
				|| (actionElement.indexOf("testSessionSortOrderBy") > 0) ) {

			form.setSelectedTestSessionId(null);
		}
	}

	private void showStudentTestStatusDetails(Integer itemSetId){

		studentTestStatusDetailsList  = new  ArrayList();
		for  (int i = 0; i < subtestList.size(); i++){
			StudentSessionStatusVO studentSessionStatusVO= 
				(StudentSessionStatusVO) this . subtestList .get(i);
			if  (itemSetId.intValue() == studentSessionStatusVO.getItemSetId().intValue()) {


				StudentProfileInformation studentProfileInformation = (StudentProfileInformation)this.studentList.get(0);
				studentSessionStatusVO.setStudentLoginName(studentProfileInformation.getStudentLoginId());
				studentSessionStatusVO.setStudentName(studentProfileInformation.getStudentPreferredName());
				studentSessionStatusVO.setExternalStudentId(studentProfileInformation.getStudentExternalId());


				studentSessionStatusVO.setTestAccessCode(studentSessionStatusVO.getTestAccessCode());
				studentTestStatusDetailsList.add(studentSessionStatusVO);
				break ;
			}
		}
	}


	private PagerSummary buildSubtestDataPagerSummary(Integer pageRequested) 
	{
		int numTotalTests = this.showStudentDeatilsList.size();
		int maxPageNumber = numTotalTests;
		PagerSummary result = new PagerSummary();
		result.setCurrentPage(pageRequested);
		result.setTotalObjects(new Integer(numTotalTests));
		result.setTotalFilteredObjects(numTotalTests);
		result.setTotalPages(new Integer(numTotalTests));

		return result;
	}



	private void selectAllStudents(Integer testAdminId, Integer itemSetId,CustomerServiceManagementForm form) 
	{

		FilterParams filter = null;
		//PageParams page = new PageParams(1, 10000, 1000);
		StudentSessionStatusData sssd = null;
		try {
			sssd = CustomerServiceSearchUtils.getStudentListForSubTest(customerServiceManagement,
					testAdminId, itemSetId, null, null, null);
		} catch (CTBBusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List studentStatusDetails = CustomerServiceSearchUtils.buildSubtestList(sssd,this.userTimeZone);
		this.studentStatusMap = getAllStudentHashMapForArrayList(studentStatusDetails);
		PageParams studentPage =  FilterSortPageUtils.buildPageParams(form.getStudentPageRequested(), FilterSortPageUtils.PAGESIZE_20);
		SortParams testSort = FilterSortPageUtils.buildSortParams(form.getStudentSortColumn(), form.getStudentSortOrderBy());
		try {
			sssd.applyPaging(studentPage);
			sssd.applySorting(testSort);
		} catch (CTBBusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.updateSelectedStudentItemDeatilsInForm(form);
		List filteredStudentStatusDetails = CustomerServiceSearchUtils.buildSubtestList(sssd,this.userTimeZone);
		form.setStudentMaxPage(sssd.getFilteredPages());
		PagerSummary studentPagerSummary = 
			CustomerServiceSearchUtils.buildSubtestDataPagerSummary(sssd, form.getStudentPageRequested()); 
		// update page flow member vars
		//this.studentsOnPage = getStudentHashMapForArrayList(filteredStudentStatusDetails);
		this.getRequest().setAttribute("studentSearchResult", "true");        
		this.getRequest().setAttribute("studentPagerSummary", studentPagerSummary);
		//PagerSummary studentPagerSummary = CustomerServiceSearchUtils.buildsubtestDataPagerSummary(sssd, form.getStudentPageRequested()); 
		System.out.println("Size of students Details list............."+this.studentStatusDetailsList.size());

	}

	private void deSelectAllStudents(Integer testAdminId, Integer itemSetId,CustomerServiceManagementForm form) {

		this.studentStatusMap = new HashMap();
		StudentSessionStatusData sssd = null;
		PageParams studentPage =  FilterSortPageUtils.buildPageParams(form.getStudentPageRequested(), FilterSortPageUtils.PAGESIZE_20);
		SortParams testSort = FilterSortPageUtils.buildSortParams(form.getStudentSortColumn(), form.getStudentSortOrderBy());


		try {
			sssd = CustomerServiceSearchUtils.getStudentListForSubTest(customerServiceManagement,
					testAdminId, itemSetId, null, studentPage, testSort);
		} catch (CTBBusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//this.studentStatusMap = getStudentHashMapForArrayList(studentStatusDetails);

		List filteredStudentStatusDetails = CustomerServiceSearchUtils.buildSubtestList(sssd,this.userTimeZone);
		form.setStudentMaxPage(sssd.getFilteredPages());
		this.updateSelectedStudentItemDeatilsInForm(form);
		PagerSummary studentPagerSummary = 
			CustomerServiceSearchUtils.buildSubtestDataPagerSummary(sssd, form.getStudentPageRequested());

		// update page flow member vars
		//this.studentsOnPage = getStudentHashMapForArrayList(filteredStudentStatusDetails);
		this.getRequest().setAttribute("studentSearchResult", "true");        
		this.getRequest().setAttribute("studentPagerSummary", studentPagerSummary);



	}

	private HashMap getStudentHashMapForArrayList(List studentDetailsList){
		HashMap result = new HashMap();
		Iterator studentDataIterator = studentDetailsList.iterator();
		while (studentDataIterator.hasNext()) {
			StudentSessionStatusVO studentStatusVO = (StudentSessionStatusVO)studentDataIterator.next();


			result.put(studentStatusVO.getStudentId()+ "_" + studentStatusVO.getItemSetId(), studentStatusVO);

		}
		return result;
	}



	private void updateSelectedStudentItemDeatilsInForm(CustomerServiceManagementForm form){
		ArrayList formItemSetIds = new ArrayList();
		Set selectedStudentItemId = this.studentStatusMap.keySet();
		for(Iterator it=this.studentsOnPage.keySet().iterator(); it.hasNext();){
			String studentItemSetIdsOnPage = (String)it.next().toString();
			if(selectedStudentItemId.contains(studentItemSetIdsOnPage)){
				formItemSetIds.add(studentItemSetIdsOnPage);
			}
		}
		form.setSelectedStudentItemId(getStringArrayFromArraylist(formItemSetIds));
	}

	private String[] getStringArrayFromArraylist(ArrayList list){
		String[] result = new String[list.size()];
		int i=0;
		for(Iterator it=list.iterator(); it.hasNext();){
			result[i++] = (String)it.next();
		}
		return result;
	}
	private void updateSelectedStudentsFromForm(CustomerServiceManagementForm form){
		ArrayList selectedStudentIdFromPage = this.getArrayListFromStringArray(form.getSelectedStudentItemId());
		this.selectedStudents = new HashMap();
		for (Iterator it=this.studentsOnPage.keySet().iterator(); it.hasNext(); )
		{
			String studentIdOnPage = (String)it.next();
			if (selectedStudentIdFromPage.contains(studentIdOnPage))
			{
				StudentSessionStatusVO  test = (StudentSessionStatusVO)this.studentsOnPage.get(studentIdOnPage);
				this.selectedStudents.put(studentIdOnPage, test);
			}
			else
			{
				this.selectedStudents.remove(studentIdOnPage);
			}
		}
	}

	private ArrayList getArrayListFromStringArray(String[] in)
	{
		ArrayList result = new ArrayList();
		if (in != null)
		{
			for (int i=0; i < in.length; i++)
			{
				result.add(in[i]);
			}
		}
		return result;
	}
	private Integer[] getIntegerArrayFromArraylist(ArrayList list)
	{
		Integer[] result = new Integer[list.size()];
		int i=0;
		for (Iterator it=list.iterator(); it.hasNext(); )
		{
			result[i++] = (Integer)it.next();
		}
		return result;
	}


	private HashMap getAllStudentHashMapForArrayList(List  studentDetailsList)
	{
		HashMap result = new HashMap();
		Iterator studentDataIterator = studentDetailsList.iterator();
		while (studentDataIterator.hasNext()) {
			StudentSessionStatusVO studentStatusVO = (StudentSessionStatusVO)studentDataIterator.next();
			if (studentStatusVO.getCompletionStatus().equals("Completed") || 
					studentStatusVO.getCompletionStatus().equals("In Progress" )) {


				result.put(studentStatusVO.getStudentId()+ "_" + studentStatusVO.getItemSetId(), studentStatusVO);
			}
		}
		return result;

	}	
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** CustomerServiceManagementForm ************* ////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    

	/**
	 * common form for all action / jsp in this page flow
	 */
	public static class CustomerServiceManagementForm extends SanitizedFormData
	{
		private String actionElement;
		private String currentAction;

		private String selectedTab;

		private String selectedOrgLevel;        
		private Integer selectedStudentId;
		private String selectedStudentLoginId;
		private String selectedAccessCode;

		private Integer selectedTestSessionId;	 

		private Integer testAdminId;
		private String testAdminName;

		private Integer selectedTestAdminId;
		private String selectedTestAdminName;

		private Integer selectedDeliverableitemSetId;  
		private Integer selectedItemSetId;

		private String testAccessCode;

		private String selectedSubtestName;

		private Integer[] selectedStudentIds;

		// messages
		private Message message;

		// user profile
		private StudentProfileInformation studentProfile;

		// test session pager
		private String testSessionSortColumn;
		private String testSessionSortOrderBy;
		private Integer testSessionPageRequested;
		private Integer testSessionMaxPage;

		// subtest pager
		private String subtestSortColumn;
		private String subtestSortOrderBy;
		private Integer subtestPageRequested;
		private Integer subtestMaxPage;

		// student pager
		private String studentSortColumn;
		private String studentSortOrderBy;
		private Integer studentPageRequested;
		private Integer studentMaxPage;

		private String ticketId;
		private String requestDescription;
		private String serviceRequestor;
		private List selectedStudents;
		private Integer customerId;
		private Integer creatorOrgNodeId;
		private String[] selectedStudentItemId; 
		// constructor
		public CustomerServiceManagementForm() {
		}

		// initialize method
		public void init(String action)
		{
			this.actionElement = global.Global.ACTION_DEFAULT;
			this.currentAction = global.Global.ACTION_DEFAULT;

			this.selectedTab = MODULE_STUDENT_TEST_SESSION;
			this.studentProfile = new StudentProfileInformation();
			this.message = new Message();   
			//clearSearch();
		}

		public CustomerServiceManagementForm createClone()
		{
			CustomerServiceManagementForm copied = new CustomerServiceManagementForm();

			//set student paging variables
			copied.setStudentMaxPage(this.studentMaxPage);
			copied.setStudentPageRequested(this.studentPageRequested);
			copied.setStudentSortColumn(this.studentSortColumn);
			copied.setStudentSortOrderBy(this.studentSortOrderBy);

			//set test session paging variables
			copied.setTestSessionMaxPage(this.testSessionMaxPage);
			copied.setTestSessionPageRequested(this.testSessionPageRequested);
			copied.setTestSessionSortColumn(this.testSessionSortColumn);
			copied.setTestSessionSortOrderBy(this.testSessionSortOrderBy);

			//set subtest paginG variables
			copied.setSubtestMaxPage(this.subtestMaxPage);
			copied.setSubtestPageRequested(this.subtestPageRequested);
			copied.setSubtestSortColumn(this.subtestSortColumn);
			copied.setSubtestSortOrderBy(this.subtestSortOrderBy);

			//set selected testAdmin
			copied.setTestAdminId(this.testAdminId);
			copied.setTestAdminName(this.testAdminName);
			copied.setSelectedTestAdminId(this.selectedTestAdminId);

			//set selected accesscode
			copied.setSelectedAccessCode(this.selectedAccessCode);
			copied.setSelectedTestSessionId(this.selectedTestSessionId);
			copied.setSelectedTestAdminId(this.testAdminId);
			copied.setSelectedStudentLoginId(this.selectedStudentLoginId);
			copied.setSelectedStudentId(this.selectedStudentId);
			copied.setActionElement(this.actionElement);     
			this.setCurrentAction(this.currentAction);

			return copied;       
		}


		// validate values
		public void validateValues()
		{
			if (this.testSessionSortColumn == null)
				this.testSessionSortColumn = 
					FilterSortPageUtils.REOPEN_TESTSESSION_DEFAULT_SORT;

			if (this.testSessionSortOrderBy == null)
				this.testSessionSortOrderBy = FilterSortPageUtils.ASCENDING;

			if (this.testSessionPageRequested == null)
				this.testSessionPageRequested = new Integer(1);

			if (this.testSessionPageRequested.intValue() <= 0) {
				this.testSessionPageRequested = new Integer(1);

			}

			if (this.testSessionMaxPage == null)
				this.testSessionMaxPage = new Integer(1);

			if (this.testSessionPageRequested.intValue() 
					> this.testSessionMaxPage.intValue()) {
				this.testSessionPageRequested = 
					new Integer(this.testSessionMaxPage.intValue());
				this.setSelectedTestSessionId(null);
				this.setSelectedTestAdminId(null);
				this.setSelectedTestAdminName(null);

			}

			if (this.studentSortColumn == null)
				this.studentSortColumn = 
					FilterSortPageUtils.STUDENT_USER_NAME_DEFAULT_SORT;

			if (this.studentSortOrderBy == null)
				this.studentSortOrderBy = FilterSortPageUtils.ASCENDING;

			if (this.studentPageRequested == null) {
				this.studentPageRequested = new Integer(1);
			}

			if (this.studentPageRequested.intValue() <= 0)            
				this.studentPageRequested = new Integer(1);

			if (this.studentMaxPage == null)
				this.studentMaxPage = new Integer(1);

			if (this.studentPageRequested.intValue() > this.studentMaxPage.intValue()) {
				this.studentPageRequested = new Integer(this.studentMaxPage.intValue());                
				this.selectedStudentId = null;
				this.selectedStudentLoginId=null;
			}

			if (this.subtestSortColumn == null)
				this.subtestSortColumn = 
					FilterSortPageUtils.SUBTEST_ITEM_SET_ORDER_DEFAULT_SORT;

			if (this.subtestSortOrderBy == null)
				this.subtestSortOrderBy = FilterSortPageUtils.ASCENDING;

			if (this.subtestPageRequested == null) {
				this.subtestPageRequested = new Integer(1);
			}

			if (this.subtestPageRequested.intValue() <= 0)            
				this.subtestPageRequested = new Integer(1);

			if (this.subtestMaxPage == null)
				this.subtestMaxPage = new Integer(1);

			if (this.subtestPageRequested.intValue() > this.subtestMaxPage.intValue()) {
				this.subtestMaxPage = new Integer(this.subtestMaxPage.intValue());                
				this.selectedItemSetId = null;
				//this.selectedStudentLoginId=null;
			}
			if (this.actionElement == null)
				this.actionElement = ACTION_DEFAULT;
			if (this.currentAction == null)
				this.currentAction = ACTION_DEFAULT;
		}

		// clear search 
		public void clearSearch()
		{   
			this.studentProfile = new StudentProfileInformation();

		}

		// clear message
		public void clearMessage()
		{   
			this.message = null;
		}

		// validation
		public ActionErrors validate(ActionMapping mapping, 
				HttpServletRequest request)
		{
			ActionErrors errs = super.validate(mapping, request);

			if (!errs.isEmpty()) {
				request.setAttribute("hasAlert", Boolean.TRUE);
			}
			return errs;
		}


		// reset values based on action
		public void resetValuesForAction(String actionElement, 
				String fromAction) 
		{
			/*if (actionElement.equals("{actionForm.orgSortOrderBy}")) {
                this.orgPageRequested = new Integer(1);
            }*/
			/*if (actionElement.equals("{actionForm.testSessionPageRequested}")) {
				this.currentAction="defaultAction";
			}*/
			if (actionElement.equals("{actionForm.testSessionPageRequested}")) {
				//this.testSessionPageRequested = new Integer(1);
				this.currentAction="defaultAction";
			}
			if ((actionElement.indexOf("testSessionSortColumn") != -1) ||
					(actionElement.indexOf("testSessionSortOrderBy") != -1)) {
				this.testSessionPageRequested = new Integer(1);
				this.testSessionSortColumn = FilterSortPageUtils.REOPEN_TESTSESSION_DEFAULT_SORT;
				this.testSessionSortOrderBy = FilterSortPageUtils.ASCENDING;
			}
			if (actionElement.equals("{actionForm.userSortOrderBy}")) {
				this.testSessionPageRequested = new Integer(1);
			}
			if (actionElement.equals("ButtonGoInvoked_testSessionSearchResult") ||
					actionElement.equals("EnterKeyInvoked_testSessionSearchResult")) {
				//this.selectedStudentLoginId = null;
				//this.selectedAccessCode = null;
				this.selectedTestSessionId = null;
			}
			if (actionElement.equals("ButtonGoInvoked_tablePathListAnchor") ||
					actionElement.equals("EnterKeyInvoked_tablePathListAnchor")) {
				this.selectedTestSessionId = null;
				if (fromAction.equals(ACTION_FIND_TESTSESSION)) {
					this.selectedStudentLoginId = null;
					this.selectedAccessCode = null;
				}
			}
		}

		/**
		 * @return the selectedTab
		 */
		public String getSelectedTab() {
			return selectedTab;
		}

		/**
		 * @param selectedTab the selectedTab to set
		 */
		public void setSelectedTab(String selectedTab) {
			this.selectedTab = selectedTab;
		}

		/**
		 * @return the selectedStudentId
		 */
		public Integer getSelectedStudentId() {
			return selectedStudentId;
		}

		/**
		 * @param selectedStudentId the selectedStudentId to set
		 */
		public void setSelectedStudentId(Integer selectedStudentId) {
			this.selectedStudentId = selectedStudentId;
		}

		/**
		 * @return the selectedStudentLoginId
		 */
		public String getSelectedStudentLoginId() {
			return selectedStudentLoginId;
		}

		/**
		 * @param selectedStudentLoginId the selectedStudentLoginId to set
		 */
		public void setSelectedStudentLoginId(String selectedStudentLoginId) {
			this.selectedStudentLoginId = selectedStudentLoginId;
		}

		/**
		 * @return the studentProfile
		 */
		public StudentProfileInformation getStudentProfile() {

			if (this.studentProfile == null) 
				this.studentProfile = new StudentProfileInformation();
			return studentProfile;
		}

		/**
		 * @param studentProfile the studentProfile to set
		 */
		public void setStudentProfile(StudentProfileInformation studentProfile) {
			this.studentProfile = studentProfile;
		}


		public void setActionElement(String actionElement)
		{
			this.actionElement = actionElement;
		}   

		public String getActionElement()
		{
			return this.actionElement != null ? this.actionElement : global.Global.ACTION_DEFAULT;
		}

		public void setCurrentAction(String currentAction)
		{
			this.currentAction = currentAction;
		}

		public String getCurrentAction()
		{
			return this.currentAction != null ? this.currentAction : global.Global.ACTION_DEFAULT;
		}

		public String getSelectedAccessCode() {
			return selectedAccessCode;
		}

		public void setSelectedAccessCode(String selectedAccessCode) {
			this.selectedAccessCode = selectedAccessCode;
		}

		public Message getMessage() {
			return message;
		}

		public void setMessage(Message message) {
			this.message = message;
		}
		public void setMessage(String title, String content, String type)
		{
			this.message = new Message(title, content, type);
		}

		public Integer getTestSessionPageRequested() {
			return this.testSessionPageRequested != null ? this.testSessionPageRequested : new Integer(1);

		}

		public void setTestSessionPageRequested(Integer testSessionPageRequested) {
			this.testSessionPageRequested = testSessionPageRequested;
		}

		public Integer getStudentPageRequested() {
			return this.studentPageRequested != null ? this.studentPageRequested : new Integer(1);

		}

		public void setStudentPageRequested(Integer studentPageRequested) {
			this.studentPageRequested = studentPageRequested;
		}

		public Integer getTestSessionMaxPage() {
			return this.testSessionMaxPage != null 
			? this.testSessionMaxPage : new Integer(1);

		}

		public void setTestSessionMaxPage(Integer testSessionMaxPage) {
			this.testSessionMaxPage = testSessionMaxPage;
		}

		public Integer getSelectedTestSessionId() {
			return selectedTestSessionId;
		}

		public void setSelectedTestSessionId(Integer selectedTestSessionId) {
			this.selectedTestSessionId = selectedTestSessionId;
		}

		/**

		 * @return the testSessionSortColumn
		 */
		public String getTestSessionSortColumn() {
			return this.testSessionSortColumn != null ? this.testSessionSortColumn : FilterSortPageUtils.REOPEN_TESTSESSION_DEFAULT_SORT;

		}

		/**
		 * @param testSessionSortColumn the testSessionSortColumn to set
		 */
		public void setTestSessionSortColumn(String testSessionSortColumn) {
			this.testSessionSortColumn = testSessionSortColumn;
		}

		/**
		 * @return the testSessionSortOrderBy
		 */
		public String getTestSessionSortOrderBy() {
			return this.testSessionSortOrderBy != null ? this.testSessionSortOrderBy : FilterSortPageUtils.ASCENDING;

		}

		/**
		 * @param testSessionSortOrderBy the testSessionSortOrderBy to set
		 */
		public void setTestSessionSortOrderBy(String testSessionSortOrderBy) {
			this.testSessionSortOrderBy = testSessionSortOrderBy;
		}

		/**
		 * @return the studentSortColumn
		 */
		public String getStudentSortColumn() {
			return this.studentSortColumn != null ? this.studentSortColumn : FilterSortPageUtils.STUDENT_USER_NAME_DEFAULT_SORT;

		}

		/**
		 * @param studentSortColumn the studentSortColumn to set
		 */
		public void setStudentSortColumn(String studentSortColumn) {
			this.studentSortColumn = studentSortColumn;
		}

		/**
		 * @return the studentSortOrderBy
		 */
		public String getStudentSortOrderBy() {
			return this.studentSortOrderBy != null ? this.studentSortOrderBy : FilterSortPageUtils.ASCENDING;

		}

		/**
		 * @param studentSortOrderBy the studentSortOrderBy to set
		 */
		public void setStudentSortOrderBy(String studentSortOrderBy) {
			this.studentSortOrderBy = studentSortOrderBy;
		}

		/**
		 * @return the studentMaxPage
		 */
		public Integer getStudentMaxPage() {
			return studentMaxPage;
		}

		/**
		 * @param studentMaxPage the studentMaxPage to set
		 */
		public void setStudentMaxPage(Integer studentMaxPage) {
			this.studentMaxPage = studentMaxPage;
		}

		public Integer getSelectedTestAdminId() {
			return selectedTestAdminId;
		}

		public void setSelectedTestAdminId(Integer selectedTestAdminId) {
			this.selectedTestAdminId = selectedTestAdminId;
		}

		public String getSubtestSortColumn() {
			return this.subtestSortColumn != null ? this.subtestSortColumn : FilterSortPageUtils.SUBTEST_ITEM_SET_ORDER_DEFAULT_SORT;

		}

		public void setSubtestSortColumn(String subtestSortColumn) {
			this.subtestSortColumn = subtestSortColumn;
		}

		public String getSubtestSortOrderBy() {
			return this.subtestSortOrderBy != null ? this.subtestSortOrderBy : FilterSortPageUtils.ASCENDING;

		}

		public void setSubtestSortOrderBy(String subtestSortOrderBy) {
			this.subtestSortOrderBy = subtestSortOrderBy;
		}

		public Integer getSubtestPageRequested() {
			return this.subtestPageRequested != null ? this.subtestPageRequested : new Integer(1);

		}

		public void setSubtestPageRequested(Integer subtestPageRequested) {
			this.subtestPageRequested = subtestPageRequested;
		}

		public Integer getSubtestMaxPage() {
			return subtestMaxPage;
		}

		public void setSubtestMaxPage(Integer subtestMaxPage) {
			this.subtestMaxPage = subtestMaxPage;
		}

		/**
		 * @return the testAdminId
		 */
		public Integer getTestAdminId() {
			return testAdminId;
		}

		/**
		 * @param testAdminId the testAdminId to set
		 */
		public void setTestAdminId(Integer testAdminId) {
			this.testAdminId = testAdminId;
		}

		/**
		 * @return the testAdminName
		 */
		public String getTestAdminName() {
			return testAdminName;
		}

		/**
		 * @param testAdminName the testAdminName to set
		 */
		public void setTestAdminName(String testAdminName) {
			this.testAdminName = testAdminName;
		}

		/**
		 * @return the selectedOrgLevel
		 */
		public String getSelectedOrgLevel() {
			return selectedOrgLevel;
		}

		/**
		 * @param selectedOrgLevel the selectedOrgLevel to set
		 */
		public void setSelectedOrgLevel(String selectedOrgLevel) {
			this.selectedOrgLevel = selectedOrgLevel;
		}

		/**
		 * @return the selectedTestAdminName
		 */
		public String getSelectedTestAdminName() {
			return selectedTestAdminName;
		}

		/**
		 * @param selectedTestAdminName the selectedTestAdminName to set
		 */
		public void setSelectedTestAdminName(String selectedTestAdminName) {
			this.selectedTestAdminName = selectedTestAdminName;
		}

		/**
		 * @return the selectedDeliverableitemSetId
		 */
		public Integer getSelectedDeliverableitemSetId() {
			return selectedDeliverableitemSetId;
		}

		/**
		 * @param selectedDeliverableitemSetId the selectedDeliverableitemSetId to set
		 */
		public void setSelectedDeliverableitemSetId(Integer selectedDeliverableitemSetId) {
			this.selectedDeliverableitemSetId = selectedDeliverableitemSetId;
		}
		/**
		 * @return the selectedDeliverableitemSetId
		 */
		public Integer getSelectedItemSetId() {
			return selectedItemSetId;
		}

		/**
		 * @param selectedDeliverableitemSetId the selectedDeliverableitemSetId to set
		 */
		public void setSelectedItemSetId(Integer selectedItemSetId) {
			this.selectedItemSetId = selectedItemSetId;
		}

		/**
		 * @return the testAccesscode
		 */
		public String getTestAccessCode() {
			return testAccessCode;
		}

		/**
		 * @param testAccesscode the testAccesscode to set
		 */
		public void setTestAccessCode(String testAccessCode) {
			this.testAccessCode = testAccessCode;
		}

		/**
		 * @return the selectedSubtestName
		 */
		public String getSelectedSubtestName() {
			return selectedSubtestName;
		}

		/**
		 * @param selectedSubtestName the selectedSubtestName to set
		 */
		public void setSelectedSubtestName(String selectedSubtestName) {
			this.selectedSubtestName = selectedSubtestName;
		}

		public void setSelectedStudentIds(Integer[] selectedStudentIds)
		{
			this.selectedStudentIds = selectedStudentIds;
		}

		public Integer[] getSelectedStudentIds()
		{
			// For data binding to be able to post data back, complex types and
			// arrays must be initialized to be non-null.
			if(this.selectedStudentIds == null || this.selectedStudentIds.length == 0)
			{
				this.selectedStudentIds = new Integer[1];
			}

			return this.selectedStudentIds;
		}

		/**
		 * @return the ticketId
		 */
		public String getTicketId() {
			return ticketId;
		}

		/**
		 * @param ticketId the ticketId to set
		 */
		public void setTicketId(String ticketId) {
			this.ticketId = ticketId;
		}

		/**
		 * @return the requestDescription
		 */
		public String getRequestDescription() {
			return requestDescription;
		}

		/**
		 * @param requestDescription the requestDescription to set
		 */
		public void setRequestDescription(String requestDescription) {
			this.requestDescription = requestDescription;
		}

		/**
		 * @return the serviceRequestor
		 */
		public String getServiceRequestor() {
			return serviceRequestor;
		}

		/**
		 * @param serviceRequestor the serviceRequestor to set
		 */
		public void setServiceRequestor(String serviceRequestor) {
			this.serviceRequestor = serviceRequestor;
		}

		/**
		 * @return the selectedStudents
		 */
		public List getSelectedStudents() {
			return selectedStudents;
		}

		/**
		 * @param selectedStudents the selectedStudents to set
		 */
		public void setSelectedStudents(List selectedStudents) {
			this.selectedStudents = selectedStudents;
		}

		/**
		 * @return the selectedStudentItemId
		 */
		public String[] getSelectedStudentItemId() {
			return selectedStudentItemId;
		}

		/**
		 * @param selectedStudentItemId the selectedStudentItemId to set
		 */
		public void setSelectedStudentItemId(String[] selectedStudentItemId) {
			this.selectedStudentItemId = selectedStudentItemId;
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
		 * @return the creatorOrgNodeId
		 */
		public Integer getCreatorOrgNodeId() {
			return creatorOrgNodeId;
		}

		/**
		 * @param creatorOrgNodeId the creatorOrgNodeId to set
		 */
		public void setCreatorOrgNodeId(Integer creatorOrgNodeId) {
			this.creatorOrgNodeId = creatorOrgNodeId;
		}
	}

	public List getTestSessionList() {
		return testSessionList;
	}

	public void setTestSessionList(List testSessionList) {
		this.testSessionList = testSessionList;
	}	
	public List getSubtestList() {
		return subtestList;
	}

	public void setSubtestList(List subtestList) {
		this.subtestList = subtestList;
	}

	/**
	 * @return the studentList
	 */
	public List getStudentList() {
		return studentList;
	}

	/**
	 * @param studentList the studentList to set
	 */
	public void setStudentList(List studentList) {
		this.studentList = studentList;
	}

	/**
	 * @return the testDeliveryItemList
	 */
	public List getTestDeliveryItemList() {
		return testDeliveryItemList;
	}

	/**
	 * @param testDeliveryItemList the testDeliveryItemList to set
	 */
	public void setTestDeliveryItemList(List testDeliveryItemList) {
		this.testDeliveryItemList = testDeliveryItemList;
	}  

	public void resetTestSessionList() {
		testSessionList = null;
	}

	public void resetSubtestList() {
		subtestList = null;
	}

	public void resetStudentTestStatusDetailsList() {
		studentTestStatusDetailsList = null;
	}
	public void resetStudentStatusDetailsList() {

		this.studentStatusDetailsList = null;
	}

	public void resettestDeliveryItemList() {
		this.testDeliveryItemList = null;
	}

	public void resetShowStudentDeatilsList() {
		this.showStudentDeatilsList = null;
	}


	public void resetPageFlowLists() {
		resetTestSessionList();
		resetSubtestList();
		resetStudentTestStatusDetailsList();
		resetStudentStatusDetailsList();
		resettestDeliveryItemList();
		resetShowStudentDeatilsList();
	}

	public List getStudentTestStatusDetailsList() {
		return studentTestStatusDetailsList;
	}

	public void setStudentTestStatusDetailsList(List studentTestStatusDetailsList) {
		this.studentTestStatusDetailsList = studentTestStatusDetailsList;
	}

	/**
	 * @return the subtestNameToIndexHash
	 */
	public HashMap getSubtestNameToIndexHash() {
		return subtestNameToIndexHash;
	}

	/**
	 * @param subtestNameToIndexHash the subtestNameToIndexHash to set
	 */
	public void setSubtestNameToIndexHash(HashMap subtestNameToIndexHash) {
		this.subtestNameToIndexHash = subtestNameToIndexHash;
	}

	/**
	 * @return the studentStatusDetailsList
	 */
	public List<StudentSessionStatusVO> getStudentStatusDetailsList() {
		return studentStatusDetailsList;
	}

	/**
	 * @param studentStatusDetailsList the studentStatusDetailsList to set
	 */
	public void setStudentStatusDetailsList(
			List<StudentSessionStatusVO> studentStatusDetailsList) {
		this.studentStatusDetailsList = studentStatusDetailsList;
	}

	/**
	 * @return the selectedStudents
	 */
	public HashMap getSelectedStudents() {
		return selectedStudents;
	}

	/**
	 * @param selectedStudents the selectedStudents to set
	 */
	public void setSelectedStudents(HashMap selectedStudents) {
		this.selectedStudents = selectedStudents;
	}

	/**
	 * @return the showStudentDeatilsList
	 */
	public List getShowStudentDeatilsList() {
		return showStudentDeatilsList;
	}

	/**
	 * @param showStudentDeatilsList the showStudentDeatilsList to set
	 */
	public void setShowStudentDeatilsList(List showStudentDeatilsList) {
		this.showStudentDeatilsList = showStudentDeatilsList;
	}

	/**
	 * @return the studentStatusMap
	 */
	public HashMap getStudentStatusMap() {
		return studentStatusMap;
	}

	/**
	 * @param studentStatusMap the studentStatusMap to set
	 */
	public void setStudentStatusMap(HashMap studentStatusMap) {
		this.studentStatusMap = studentStatusMap;
	}

	
	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}


}