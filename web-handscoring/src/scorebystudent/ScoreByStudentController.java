package scorebystudent;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.FilterSortPageUtils;
import utils.JsonUtils;
import utils.MessageResourceBundle;
import utils.ScoringPopupUtil;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.SortParams.SortParam;
import com.ctb.bean.request.SortParams.SortType;
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.RubricViewData;
import com.ctb.bean.testAdmin.ScorableCRAnswerContent;
import com.ctb.bean.testAdmin.ScorableItem;
import com.ctb.bean.testAdmin.ScorableItemData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.ColumnSortEntry;
import com.ctb.widgets.bean.PagerSummary;

import utils.Message;

@Jpf.Controller
public class ScoreByStudentController extends PageFlowController {
	private static final long serialVersionUID = 1L;
	@Control()
	private com.ctb.control.studentManagement.StudentManagement studentManagement;

	@Control()
	private com.ctb.control.crscoring.TestScoring testScoring;


    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;
    
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.CRScoring scoring;
	
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;
	
	private static final String ACTION_DEFAULT = "defaultAction";
	private static final String ACTION_ITEM_LIST = "getItemByStudent";
	private static final String ACTION_FIND_STUDENT = "findStudent";
	 public static final String STUDENT_DEFAULT_SORT_COLUMN = "UserName";
	 public static final String LOGIN_NAME_SORT = "UserName";

	private String userName = null;
	private User user = null;
	private Integer customerId = null;
	public String pageTitle = null;
	public String pageMessage = null;
	private Integer testAdminId = null;
	private String loginName = null;
	private Integer testRosterId = null;
	private Integer itemSetIdTC = null;
	private boolean studentIdConfigurable = false;
	private String studentIdLabelName = "Student ID";
	private ScoreByStudentForm savedForm = null;

	// customer configuration
	CustomerConfiguration[] customerConfigurations = null;
	CustomerConfigurationValue[] customerConfigurationsValue = null;
	private boolean islaslinkCustomer = false;

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
	 * 
	 * @jpf:action
	 * @jpf:forward name="success" path="beginIndivStudentScoring.do"
	 */
	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "beginIndivStudentScoring.do") })
	protected Forward begin() {
		return new Forward("success");
	}

	/**
	 * This method represents the point of entry into the pageflow
	 * 
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudent.do"
	 */
	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "findStudent.do") }, validationErrorForward = @Jpf.Forward(name = "failure", path = "logout.do"))
	protected Forward beginIndivStudentScoring() {

		ScoreByStudentForm form = initialize(ACTION_FIND_STUDENT);
		isGeorgiaCustomer(form);
		customerHasBulkAccommodation();
		customerHasScoring();
		isTopLevelUser();

		return new Forward("success", form);
	}

	/**
	 * This method represents the pageflow for find student for a test session.
	 * 
	 * @jpf:action
	 * @jpf:forward name="success" path="find_student_list.jsp"
	 * @jpf:validation-error-forward name="failure" path="logout.do"
	 */
	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "find_student_list.jsp") }, validationErrorForward = @Jpf.Forward(name = "failure", path = "logout.do"))
	protected Forward findStudent(ScoreByStudentForm form) {

		form.validateValues();

		String currentAction = form.getCurrentAction();
		String actionElement = form.getActionElement();
		Integer testAdminId = 0;
		TestSession ts = null;
		form.resetValuesForAction(actionElement, ACTION_FIND_STUDENT);

		if (currentAction.equals(ACTION_DEFAULT)) {
			initFindStudent(form);
			form.setUserName((String) getSession().getAttribute("userName"));
			String reqTestAdminId = getRequest().getParameter("testAdminId");
			if (reqTestAdminId != null) {
				testAdminId = Integer.valueOf(reqTestAdminId);
				form.setTestAdminId(testAdminId);
			}
			actionElement = ACTION_FIND_STUDENT;
			form.setActionElement(ACTION_FIND_STUDENT);
			form.setCurrentAction(ACTION_FIND_STUDENT);
		}

		form.setUserName((String) getSession().getAttribute("userName"));
		initPagingSorting(form);
		RosterElementData reData = null;
		reData = findStudentForTestSession(form);
		if (reData != null) {
			List<RosterElement> studentList = buildStudentList(reData);
			if (form.getTestAccessCode() == null)
				try {
					ts = scoring.getTestAdminDetails(form.getTestAdminId());
					form.setTestAccessCode(ts.getAccessCode());
					form.setTestSessionName(ts.getTestAdminName());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			PagerSummary studentPagerSummary = buildStudentPagerSummary(reData,
					form.getStudentPageRequested());
			form.setStudentMaxPage(reData.getFilteredPages());

			this.getRequest().setAttribute("studentList", studentList);
			this.getRequest().setAttribute("studentPagerSummary",
					studentPagerSummary);
			this.pageTitle = "Scoring: Find Student";
			if (studentList.isEmpty()) {
				this.getRequest().setAttribute("resultEmpty",
						MessageResourceBundle.getMessage("resultEmpty"));
				return new Forward("success");
			}
		}

		this.savedForm = form.createClone();
		return new Forward("success", form);
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="listOfItem.jsp"
	 * @jpf:validation-error-forward name="failure" path="logout.do"
	 */
	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "listOfItem.jsp") }, validationErrorForward = @Jpf.Forward(name = "failure", path = "logout.do"))
	protected Forward beginDisplayStudItemList(ScoreByStudentForm form) {
		form.validateValues();
		String currentAction = form.getCurrentAction();
		String actionElement = form.getActionElement();
		if (currentAction.equals(ACTION_DEFAULT)) {
			getUserDetails();
			isGeorgiaCustomer(form);
			customerHasBulkAccommodation();
			customerHasScoring();
			isTopLevelUser();
			form.setUserName((String) getSession().getAttribute("userName"));
			form.setTestAdminId(Integer.valueOf(this.getRequest().getParameter(
					"testAdminId")));
			form.setItemSetIdTC(Integer.valueOf(this.getRequest().getParameter(
					"itemSetIdTC")));
			form.setLoginName(this.getRequest().getParameter("loginName"));
			form.setTestRosterId(Integer.valueOf(this.getRequest()
					.getParameter("testRosterId")));
			form.setIsDataExportFlow(this.getRequest().
					getParameter("dataExport"));
			
		}
		if (form.getActionElement().equals(ACTION_DEFAULT)) {
			form.setCurrentAction(ACTION_ITEM_LIST);
		}
		actionElement = form.getActionElement();
		form.resetValuesForAction(actionElement, ACTION_ITEM_LIST);
		ScorableItemData sid = null;
		TestSession ts = null;
		PageParams page = FilterSortPageUtils.buildPageParams(form
				.getItemPageRequested(), FilterSortPageUtils.PAGESIZE_10);
		SortParams sort = FilterSortPageUtils.buildItemSortParams(form
				.getItemSortColumn(), form.getItemSortOrderBy());
		try {
			
			sid = testScoring.getAllScorableCRItemsForTestRoster(
					form.testRosterId, form.itemSetIdTC, page, sort);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		List<ScorableItem> itemList = buildItemList(sid);
		PagerSummary itemPagerSummary = buildItemPagerSummary(sid, form
				.getItemPageRequested());
		form.setItemMaxPage(sid.getFilteredPages());
		try {
			ts = scoring.getTestAdminDetails(form.getTestAdminId());
			// start- added for  Process Scores   button changes
			String completionStatus = scoring.getScoringStatus(form.getTestRosterId(),form.getItemSetIdTC());
			Boolean scoringButton = false;
			if(completionStatus.equals("CO")){
				 scoringButton = false;
			}else{
				 scoringButton = true;
			}
   		    this.getRequest().setAttribute("scoringButton", scoringButton);
			// end- added for  Process Scores  button changes
			form.setTestAccessCode(ts.getAccessCode());
			form.setTestSessionName(ts.getTestAdminName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.getRequest().setAttribute("itemList", itemList);
		this.getRequest().setAttribute("itemPagerSummary", itemPagerSummary);

		if (itemList.isEmpty()) {
			this.getRequest().setAttribute("itemSearchResultEmpty",
					MessageResourceBundle.getMessage("itemSearchResultEmpty"));
			return new Forward("success");
		}
		return new Forward("success", form);
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudent.do"
	 */
	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "findStudent.do"),
			@Jpf.Forward(name = "successExport", path = "goto_export_student_list.do")})
	protected Forward returnToFindStudent(ScoreByStudentForm form) {
		Forward forward;
		
		if(form.getIsDataExportFlow().equalsIgnoreCase("T"))
		{
			
			forward = new Forward("successExport");
		}
		else
		{
			savedForm.setCurrentAction(ACTION_FIND_STUDENT);
			savedForm.setActionElement(ACTION_FIND_STUDENT);
			savedForm.setTestAdminId(form.getTestAdminId());
			forward = new Forward("success", this.savedForm);
		}
		return forward;
	}
	
	/**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward goto_export_student_list()
    {
        try
        {
            String contextPath = "/DataExportWeb/dataExportPageFlow/returnToFindStudent.do";
           /* String loginName = this.getRequest().getParameter("loginName");
            String testAdminId = this.getRequest().getParameter("testAdminId");
            String testRosterId = this.getRequest().getParameter("testRosterId");
            String itemSetIdTC = this.getRequest().getParameter("itemSetIdTC");*/
            
            String url = contextPath + "?itemBack=T";
            
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    } 

	/**
	 * findStudentForTestSession
	 */
	private RosterElementData findStudentForTestSession(ScoreByStudentForm form) {
		String actionElement = form.getActionElement();
		form.resetValuesForAction(actionElement, ACTION_FIND_STUDENT);

		PageParams page = FilterSortPageUtils.buildPageParams(form
				.getStudentPageRequested(), FilterSortPageUtils.PAGESIZE_10);
		SortParams sort = buildStudentSortParams(form
			.getStudentSortColumn(), form.getStudentSortOrderBy());

		FilterParams filter = null;

		RosterElementData reData = null;
		try {
			reData = testScoring.getAllStudentForTestSession(
					form.getTestAdminId(), form.getUserName(), filter, page, sort);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}

		this.pageMessage = MessageResourceBundle
				.getMessage("searchResultFound");

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
					studentList.add(student);
				}
			}
		}
		return studentList;
	}

	private PagerSummary buildStudentPagerSummary(RosterElementData reData,
			Integer pageRequested) {
		PagerSummary pagerSummary = new PagerSummary();
		pagerSummary.setCurrentPage(pageRequested);
		pagerSummary.setTotalObjects(reData.getFilteredCount());
		pagerSummary.setTotalPages(reData.getFilteredPages());
		return pagerSummary;
	}

	private List<ScorableItem> buildItemList(ScorableItemData tid) {
		ArrayList<ScorableItem> itemList = new ArrayList<ScorableItem>();
		if (tid != null) {
			ScorableItem[] testItemDetails = tid.getScorableItems();
			for (ScorableItem itemDetail : testItemDetails) {
				if (itemDetail != null) {
					itemList.add(itemDetail);
				}
			}
		}
		return itemList;
	}
	 public static SortParams buildStudentSortParams(String sortName, String sortOrderBy)
	    {
	        SortParams sort = new SortParams();
	                
	        SortType sortType = sortOrderBy.equals(ColumnSortEntry.ASCENDING) ? SortType.ALPHAASC : SortType.ALPHADESC;
	        SortParam[] sortParams = new SortParam[2];
	        
	        sortParams[0] = new SortParam(sortName, sortType);
	        
	        String secondarySortName = STUDENT_DEFAULT_SORT_COLUMN;
	        if (sortName.equals(STUDENT_DEFAULT_SORT_COLUMN))
	             secondarySortName = LOGIN_NAME_SORT;
	             
	        sortParams[1] = new SortParam(secondarySortName, sortType);
	        sort.setSortParams(sortParams);
	        
	        return sort;
	    }
	/**
	 * Bulk Accommodation
	 */
	private Boolean customerHasBulkAccommodation() {
		boolean hasBulkStudentConfigurable = false;
		for (CustomerConfiguration cc : customerConfigurations) {
			if (cc.getCustomerConfigurationName().equalsIgnoreCase(
					"Configurable_Bulk_Accommodation")
					&& cc.getDefaultValue().equals("T")) {
				hasBulkStudentConfigurable = true;
				break;
			}
		}

		getSession().setAttribute("isBulkAccommodationConfigured",
				hasBulkStudentConfigurable);

		return new Boolean(hasBulkStudentConfigurable);
	}

	private Boolean customerHasScoring() {

		boolean hasScoringConfigurable = false;
		boolean isLaslinkCustomer = false;
		for (CustomerConfiguration cc : customerConfigurations) {
			if (cc.getCustomerConfigurationName().equalsIgnoreCase(
					"Configurable_Hand_Scoring")
					&& cc.getDefaultValue().equals("T")) {
				hasScoringConfigurable = true;
				break;
			}
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")
					&& cc.getDefaultValue().equals("T")) {
				isLaslinkCustomer = true;
				break;
            }

		}
		this.setIslaslinkCustomer(isLaslinkCustomer);
		getSession()
				.setAttribute("isScoringConfigured", hasScoringConfigurable);
		return new Boolean(hasScoringConfigurable);
	}
	
	 //LLO- 118 - Change for Ematrix UI
    private void isTopLevelUser(){
		
		boolean isUserTopLevel = false;
		boolean isLaslinkUserTopLevel = false;
		boolean isLaslinkUser = false;
		isLaslinkUser = this.islaslinkCustomer;
		try {
			if(isLaslinkUser) {
				isUserTopLevel = orgnode.checkTopOrgNodeUser(this.userName);	
				if(isUserTopLevel){
					isLaslinkUserTopLevel = true;				
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getSession().setAttribute("isTopLevelUser",isLaslinkUserTopLevel);	
	}

	/**
	 * initialize
	 */
	private ScoreByStudentForm initialize(String action) {
		getUserDetails();
		this.savedForm = new ScoreByStudentForm();
		this.savedForm.init();
		this.getSession().setAttribute("userHasReports", userHasReports());

		return this.savedForm;
	}

	/**
	 * userHasReports
	 */
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

	/**
	 * initFindStudent
	 */
	private void initFindStudent(ScoreByStudentForm form) {
		form.setSelectedStudentId(null);
	}

	/**
	 * initPagingSorting
	 */
	private void initPagingSorting(ScoreByStudentForm form) {
		String actionElement = form.getActionElement();

		if ((actionElement.indexOf("studentPageRequested") > 0)
				|| (actionElement.indexOf("studentSortOrderBy") > 0)) {
			form.setSelectedStudentId(null);
		}
		if ((actionElement.indexOf("itemPageRequested") > 0)
				|| (actionElement.indexOf("itemSortOrderBy") > 0)) {
			form.setSelectedStudentId(null);
		}
	}

	private void isGeorgiaCustomer(ScoreByStudentForm form) {
		boolean isStudentIdConfigurable = false;
		Integer configId = 0;

		String[] valueForStudentId = new String[8];

		for (int i = 0; i < this.customerConfigurations.length; i++) {
			CustomerConfiguration cc = (CustomerConfiguration) this.customerConfigurations[i];

			if (cc.getCustomerConfigurationName().equalsIgnoreCase(
					"Configurable_Student_ID")
					&& cc.getDefaultValue().equalsIgnoreCase("T")) {
				isStudentIdConfigurable = true;
				configId = cc.getId();
				customerConfigurationValues(configId);
				valueForStudentId = new String[8];
				for (int j = 0; j < this.customerConfigurationsValue.length; j++) {
					int sortOrder = this.customerConfigurationsValue[j]
							.getSortOrder();
					valueForStudentId[sortOrder - 1] = this.customerConfigurationsValue[j]
							.getCustomerConfigurationValue();
				}

				this.studentIdConfigurable = isStudentIdConfigurable;
				form.setStudentIdConfigurable(this.studentIdConfigurable);

				valueForStudentId = getDefaultValue(valueForStudentId,
						"Student ID", form);

			}

		}

		this.getRequest().setAttribute("studentIdArrValue", valueForStudentId);
		this.getRequest().setAttribute("isStudentIdConfigurable",
				isStudentIdConfigurable);

	}

	/*
	 * this method retrieve CustomerConfigurationsValue for provided customer
	 * configuration Id.
	 */
	private void customerConfigurationValues(Integer configId) {
		try {
			this.customerConfigurationsValue = this.studentManagement
					.getCustomerConfigurationsValue(configId);

		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}
	}

	/**
	 * getUserDetails
	 */
	private void getUserDetails() {
		java.security.Principal principal = getRequest().getUserPrincipal();
		if (principal != null)
			this.userName = principal.toString();
		else
			this.userName = (String) getSession().getAttribute("userName");

		try {
			this.user = this.studentManagement.getUserDetails(this.userName,
					this.userName);
			this.customerId = user.getCustomer().getCustomerId();
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		getSession().setAttribute("userName", this.userName);

		getCustomerConfigurations();
	}

	/**
	 * getCustomerConfigurations
	 */
	private void getCustomerConfigurations() {
		try {
			this.customerConfigurations = this.studentManagement
					.getCustomerConfigurations(this.userName, this.customerId);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}
	}

	/*
	 * CustomerConfigurationsValue for provided customer configuration Id.
	 */
	private String[] getDefaultValue(String[] arrValue, String labelName,
			ScoreByStudentForm form) {
		arrValue[0] = arrValue[0] != null ? arrValue[0] : labelName;
		arrValue[1] = arrValue[1] != null ? arrValue[1] : "32";

		if (labelName.equals("Student ID")) {
			arrValue[2] = arrValue[2] != null ? arrValue[2] : "F";
			if (!arrValue[2].equals("T") && !arrValue[2].equals("F")) {
				arrValue[2] = "F";
			}
			this.studentIdLabelName = arrValue[0];
			form.setStudentIdLabelName(this.studentIdLabelName);

		}
		return arrValue;
	}

	private PagerSummary buildItemPagerSummary(ScorableItemData sid,
			Integer pageRequested) {
		PagerSummary pagerSummary = new PagerSummary();
		pagerSummary.setCurrentPage(pageRequested);
		pagerSummary.setTotalObjects(sid.getFilteredCount());
		pagerSummary.setTotalPages(sid.getFilteredPages());
		pagerSummary.setTotalFilteredObjects(null);
		return pagerSummary;
	}

	/**
	 * Included for rubric View
	 * @jpf:action
	 * @jpf:forward name="success" path="listOfItem.jsp"
	 */
		
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="listOfItem.jsp")
	})
	protected Forward rubricViewDisplay(ScoreByStudentForm form){
			
		String jsonResponse = "";
		String itemId = getRequest().getParameter("itemId");
		
		RubricViewData[] scr =  getRubricDetails(itemId);
		
		try {
			jsonResponse = JsonUtils.getJson(scr, "rubricData",scr.getClass());

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

	/**
	 * getRubricDetails() for rubricView
	 */
	private RubricViewData[] getRubricDetails(String itemId) {

		RubricViewData[] rubricDetailsData = null;
		try {
			rubricDetailsData = this.testScoring.getRubricDetailsData(itemId);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		return rubricDetailsData;
	}

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "listOfItem.jsp") })
	protected Forward beginCRResponseDisplay(ScoreByStudentForm form) {
		
		try {
			ScoringPopupUtil.processCRResponseDisplay(getRequest(), getResponse(), this.userName, 
					this.testScoring, ScoringPopupUtil.CONTENT_TYPE_JSON) ;
		} catch (Exception e) {
			System.err.println("Exception while processing CR response.");
			e.printStackTrace();
		}

		return null;

	}

	private ScorableCRAnswerContent getIndividualCRResponse(String userName,
			Integer testRosterId, Integer deliverableItemSetId, String itemId,
			String itemType) {
		ScorableCRAnswerContent answerArea = new ScorableCRAnswerContent();
		try {

			answerArea = this.testScoring.getCRItemResponseForScoring(userName,
					testRosterId, deliverableItemSetId, itemId, itemType);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}

		return answerArea;
	}

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "/itemPlayer/ItemPlayerController.jpf") }, validationErrorForward = @Jpf.Forward(name = "failure", path = "logout.do"))
	protected Forward viewQuestionWindow()
	{      
		String param = getRequest().getParameter("param");
		String itemSortNumber = getRequest().getParameter("itemSortNumber");
		getSession().setAttribute("param", param);
		getSession().setAttribute("itemSortNumber", itemSortNumber);

		return new Forward("success");
	}

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "beginDisplayStudItemList.do") })
	protected Forward saveDetails(ScoreByStudentForm form) {
		String jsonMessageResponse = "";
		if (user == null) {
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
			Boolean isSuccess = this.testScoring.saveOrUpdateScore(user
					.getUserId(), itemId, itemSetId, testRosterId, score);
			// start- added for  Process Scores   button changes
			
			 String completionStatus = scoring.getScoringStatus(testRosterId,itemSetIdTC);
			 ManageStudent ms = new ManageStudent();
			 ms.setIsSuccess(isSuccess);
			 ms.setCompletionStatus(completionStatus);
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

	/**
	 * @jpf:action
	 */
	@Jpf.Action()
	protected Forward goToHomePage(ScoreByStudentForm form) {
		try {
		
			getResponse().sendRedirect(
					"/TestSessionInfoWeb/homepage/HomePageController.jpf");
		} catch (IOException ioe) {
			System.err.print(ioe.getStackTrace());
		}

		return null;
	}
	
// start-added for  Process Scores  button changes
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "beginDisplayStudItemList.do")
	})
public Forward rescoreStudent(ScoreByStudentForm form) {
	 Integer testRosterId = form.getTestRosterId();

        try {    
            this.testSessionStatus.rescoreStudent(testRosterId);
            //Change for LL0-136 Hand-entered score conformation
	        this.getRequest().setAttribute("processScoreResult", MessageResourceBundle.getMessage("processScoreResult"));     
        }
        catch (Exception e) {
            e.printStackTrace();
        }                
        return new Forward("success");
}
// end- added for  Process Scores  button changes
	public static class ScoreByStudentForm extends SanitizedFormData {

		private static final long serialVersionUID = 1L;
		private Integer selectedStudentId;
		private Integer testRosterId;
		private Message message;
		private String actionElement;
		private String currentAction;

		// student pager
		private String studentSortColumn;
		private String studentSortOrderBy;
		private Integer studentPageRequested;
		private Integer studentMaxPage;

		// item pager
		private String itemSortColumn;
		private String itemSortOrderBy;
		private Integer itemPageRequested;
		private Integer itemMaxPage;

		private Integer rosterId = null;
		private Integer itemSetIdTC = null;
		private String accessCode = null;
		private String userName = null;
		private Integer testAdminId = null;
		private String testAccessCode;
		private String testSessionName;

		private Integer scorePoints = null;
		private String studentIdLabelName = "Student ID";
		private boolean studentIdConfigurable = false;
		//
		private Integer itemSetId;
		// student profile
		private String loginName;
		private String isDataExportFlow;

		/**
		 * @return the itemSetId
		 */
		public Integer getItemSetId() {
			return itemSetId;
		}

		/**
		 * @param itemSetId
		 *            the itemSetId to set
		 */
		public void setItemSetId(Integer itemSetId) {
			this.itemSetId = itemSetId;
		}

		/**
		 * @return the loginName
		 */
		public String getLoginName() {
			return loginName;
		}

		/**
		 * @param loginName
		 *            the loginName to set
		 */
		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}

		/**
		 * @return the studentIdConfigurable
		 */
		public boolean isStudentIdConfigurable() {
			return studentIdConfigurable;
		}

		/**
		 * @param studentIdConfigurable
		 *            the studentIdConfigurable to set
		 */
		public void setStudentIdConfigurable(boolean studentIdConfigurable) {
			this.studentIdConfigurable = studentIdConfigurable;
		}

		public ScoreByStudentForm() {
		}

		public void init() {
			this.actionElement = ACTION_DEFAULT;
			this.currentAction = ACTION_DEFAULT;
			// clearSearch();
			this.selectedStudentId = null;
			this.studentSortColumn = FilterSortPageUtils.SCORE_STUDENT_DEFAULT_SORT_COLUMN;
			this.studentSortOrderBy = FilterSortPageUtils.ASCENDING;
			this.studentPageRequested = new Integer(1);
			this.studentMaxPage = new Integer(1);

			this.itemSortColumn = FilterSortPageUtils.TESTITEM_DEFAULT_SORT_COLUMN;
			this.itemSortOrderBy = FilterSortPageUtils.ASCENDING;
			this.itemPageRequested = new Integer(1);
			this.itemMaxPage = new Integer(1);

		}

		public void validateValues() {
			if (this.studentSortColumn == null)
				this.studentSortColumn = FilterSortPageUtils.SCORE_STUDENT_DEFAULT_SORT_COLUMN;

			if (this.studentSortOrderBy == null)
				this.studentSortOrderBy = FilterSortPageUtils.ASCENDING;

			if (this.studentPageRequested == null) {
				this.studentPageRequested = new Integer(1);
			}

			if (this.studentPageRequested.intValue() <= 0)
				this.studentPageRequested = new Integer(1);

			if (this.studentMaxPage == null)
				this.studentMaxPage = new Integer(1);
			if (this.studentPageRequested.intValue() > this.studentMaxPage
					.intValue()) {
				this.studentPageRequested = new Integer(this.studentMaxPage
						.intValue());

			}

			if (this.itemSortColumn == null)
				this.itemSortColumn = FilterSortPageUtils.TESTITEM_DEFAULT_SORT_COLUMN;

			if (this.itemSortOrderBy == null)
				this.itemSortOrderBy = FilterSortPageUtils.ASCENDING;

			if (this.itemPageRequested == null)
				this.itemPageRequested = new Integer(1);

			if (this.itemPageRequested.intValue() <= 0)
				this.itemPageRequested = new Integer(1);

			if (this.itemMaxPage == null)
				this.itemMaxPage = new Integer(1);
			if (this.itemPageRequested.intValue() > this.itemMaxPage.intValue()) {
				this.itemPageRequested = new Integer(this.itemMaxPage
						.intValue());

			}
			
			if(this.isDataExportFlow == null){				
				this.setIsDataExportFlow("F");
			}

		}

		public ScoreByStudentForm createClone() {
			ScoreByStudentForm copied = new ScoreByStudentForm();

			copied.setActionElement(this.actionElement);
			copied.setCurrentAction(this.currentAction);

			copied.setStudentSortColumn(this.studentSortColumn);
			copied.setStudentSortOrderBy(this.studentSortOrderBy);
			copied.setStudentPageRequested(this.studentPageRequested);
			copied.setStudentMaxPage(this.studentMaxPage);

			return copied;
		}

		public void setActionElement(String actionElement) {
			this.actionElement = actionElement;
		}

		public String getActionElement() {
			return this.actionElement != null ? this.actionElement
					: ACTION_DEFAULT;
		}

		public void setCurrentAction(String currentAction) {
			this.currentAction = currentAction;
		}

		public String getCurrentAction() {
			return this.currentAction != null ? this.currentAction
					: ACTION_DEFAULT;
		}

		public Message getMessage() {
			return this.message != null ? this.message : new Message();
		}

		public void setMessage(Message message) {
			this.message = message;
		}

		public void setMessage(String title, String content, String type) {
			this.message = new Message(title, content, type);
		}

		/**
		 * @return the testAccessCode
		 */
		public String getTestAccessCode() {
			return testAccessCode;
		}

		/**
		 * @param testAccessCode
		 *            the testAccessCode to set
		 */
		public void setTestAccessCode(String testAccessCode) {
			this.testAccessCode = testAccessCode;
		}

		/**
		 * @return the testSessionName
		 */
		public String getTestSessionName() {
			return testSessionName;
		}

		/**
		 * @param testSessionName
		 *            the testSessionName to set
		 */
		public void setTestSessionName(String testSessionName) {
			this.testSessionName = testSessionName;
		}

		public void resetValuesForAction(String actionElement, String fromAction) {

			if (actionElement.equals("{actionForm.studentSortOrderBy}")) {
				this.studentPageRequested = new Integer(1);
			}
			if (actionElement.equals("{actionForm.itemSortOrderBy}")) {
				this.itemPageRequested = new Integer(1);
			}

		}

		public String getStudentSortColumn() {
			return studentSortColumn;
		}

		public void setStudentSortColumn(String studentSortColumn) {
			this.studentSortColumn = studentSortColumn;
		}

		public String getStudentSortOrderBy() {
			return studentSortOrderBy;
		}

		public void setStudentSortOrderBy(String studentSortOrderBy) {
			this.studentSortOrderBy = studentSortOrderBy;
		}

		public Integer getStudentPageRequested() {
			return studentPageRequested;
		}

		public void setStudentPageRequested(Integer studentPageRequested) {
			this.studentPageRequested = studentPageRequested;
		}

		public Integer getStudentMaxPage() {
			return studentMaxPage;
		}

		public void setStudentMaxPage(Integer studentMaxPage) {
			this.studentMaxPage = studentMaxPage;
		}

		public Integer getSelectedStudentId() {
			return selectedStudentId;
		}

		public void setSelectedStudentId(Integer selectedStudentId) {
			this.selectedStudentId = selectedStudentId;
		}

		public String getStudentIdLabelName() {
			return studentIdLabelName;
		}

		public void setStudentIdLabelName(String studentIdLabelName) {
			this.studentIdLabelName = studentIdLabelName;
		}

		public void setItemPageRequested(Integer itemPageRequested) {
			this.itemPageRequested = itemPageRequested;
		}

		public Integer getItemPageRequested() {
			return this.itemPageRequested != null ? this.itemPageRequested
					: new Integer(1);
		}

		public void setItemMaxPage(Integer itemMaxPage) {
			this.itemMaxPage = itemMaxPage;
		}

		public Integer getItemMaxPage() {
			return this.itemMaxPage;
		}

		public void setItemSortColumn(String itemSortColumn) {
			this.itemSortColumn = itemSortColumn;
		}

		public String getItemSortColumn() {
			return this.itemSortColumn != null ? this.itemSortColumn
					: FilterSortPageUtils.TESTITEM_DEFAULT_SORT_COLUMN;
		}

		public void setItemSortOrderBy(String itemSortOrderBy) {
			this.itemSortOrderBy = itemSortOrderBy;
		}

		public String getItemSortOrderBy() {
			return this.itemSortOrderBy != null ? this.itemSortOrderBy
					: FilterSortPageUtils.ASCENDING;
		}

		/**
		 * @return the rosterId
		 */
		public Integer getRosterId() {
			return rosterId;
		}

		/**
		 * @param rosterId
		 *            the rosterId to set
		 */
		public void setRosterId(Integer rosterId) {
			this.rosterId = rosterId;
		}

		/**
		 * @return the itemSetIdTC
		 */
		public Integer getItemSetIdTC() {
			return itemSetIdTC;
		}

		/**
		 * @param itemSetIdTC
		 *            the itemSetIdTC to set
		 */
		public void setItemSetIdTC(Integer itemSetIdTC) {
			this.itemSetIdTC = itemSetIdTC;
		}

		/**
		 * @return the accessCode
		 */
		public String getAccessCode() {
			return accessCode;
		}

		/**
		 * @param accessCode
		 *            the accessCode to set
		 */
		public void setAccessCode(String accessCode) {
			this.accessCode = accessCode;
		}

		/**
		 * @return the userName
		 */
		public String getUserName() {
			return userName;
		}

		/**
		 * @param me
		 *            the userName to set
		 */
		public void setUserName(String me) {
			this.userName = me;
		}

		/**
		 * @return the testAdminId
		 */
		public Integer getTestAdminId() {
			return testAdminId;
		}

		/**
		 * @param testAdminId
		 *            the testAdminId to set
		 */
		public void setTestAdminId(Integer testAdminId) {
			this.testAdminId = testAdminId;
		}

		/**
		 * @return the scorePoints
		 */
		public Integer getScorePoints() {
			return scorePoints;
		}

		/**
		 * @param scorePoints
		 *            the scorePoints to set
		 */
		public void setScorePoints(Integer scorePoints) {
			this.scorePoints = scorePoints;
		}

		/**
		 * @return the testRosterId
		 */
		public Integer getTestRosterId() {
			return testRosterId;
		}

		/**
		 * @param testRosterId
		 *            the testRosterId to set
		 */
		public void setTestRosterId(Integer testRosterId) {
			this.testRosterId = testRosterId;
		}

		/**
		 * @return the isDataExportFlow
		 */
		public String getIsDataExportFlow() {
			return isDataExportFlow;
		}

		/**
		 * @param isDataExportFlow the isDataExportFlow to set
		 */
		public void setIsDataExportFlow(String isDataExportFlow) {
			this.isDataExportFlow = isDataExportFlow;
		}

		
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * @return the testAdminId
	 */
	public Integer getTestAdminId() {
		return testAdminId;
	}

	/**
	 * @param testAdminId
	 *            the testAdminId to set
	 */
	public void setTestAdminId(Integer testAdminId) {
		this.testAdminId = testAdminId;
	}

	/**
	 * @return the testAdminId
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param testAdminId
	 *            the testAdminId to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * @return the testAdminId
	 */
	public Integer getTestRosterId() {
		return testRosterId;
	}

	/**
	 * @param testAdminId
	 *            the testAdminId to set
	 */
	public void setTestRosterId(Integer testRosterId) {
		this.testRosterId = testRosterId;
	}

	/**
	 * @return the itemSetIdTC
	 */
	public Integer getItemSetIdTC() {
		return itemSetIdTC;
	}

	/**
	 * @param itemSetIdTC
	 *            the itemSetIdTC to set
	 */
	public void setItemSetIdTC(Integer itemSetIdTC) {
		this.itemSetIdTC = itemSetIdTC;
	}

	/**
	 * @return the savedForm
	 */
	public ScoreByStudentForm getSavedForm() {
		return savedForm;
	}

	/**
	 * @param savedForm
	 *            the savedForm to set
	 */
	public void setSavedForm(ScoreByStudentForm savedForm) {
		this.savedForm = savedForm;
	}

	/**
	 * @return the islaslinkCustomer
	 */
	public boolean isIslaslinkCustomer() {
		return islaslinkCustomer;
	}

	/**
	 * @param islaslinkCustomer the islaslinkCustomer to set
	 */
	public void setIslaslinkCustomer(boolean islaslinkCustomer) {
		this.islaslinkCustomer = islaslinkCustomer;
	}

}