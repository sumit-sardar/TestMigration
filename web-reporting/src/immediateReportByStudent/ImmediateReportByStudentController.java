package immediateReportByStudent;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.DateUtils;
import utils.FilterSortPageUtils;
import utils.Message;
import utils.MessageResourceBundle;
import utils.StudentProfileInformation;
import utils.StudentSearchUtils;
import utils.WebUtils;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.studentManagement.StudentScoreReport;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.ScorableItem;
import com.ctb.bean.testAdmin.StudentReportIrsScore;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.bean.testAdmin.TestProductData;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.web.sanitizer.JavaScriptSanitizer;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.ColumnSortEntry;
import com.ctb.widgets.bean.PagerSummary;
import com.google.gson.Gson;

@Jpf.Controller
public class ImmediateReportByStudentController extends PageFlowController {
	private static final long serialVersionUID = 1L;
	
	@Control()
	private com.ctb.control.studentManagement.StudentManagement studentManagement;
	
	@Control()
    private com.ctb.control.testAdmin.ScheduleTest scheduleTest;
	
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;
	
	
	
	private static final String ACTION_DEFAULT           = "defaultAction";
	private static final String ACTION_FIND_STUDENT      = "findStudent";
	private static final String ACTION_APPLY_SEARCH   = "applySearch";
	private static final String ACTION_CLEAR_SEARCH   = "clearSearch";
	private static final String ACTION_GOTO_REPORTS   = "gotoReports";
	private boolean searchApplied = false;
	private User user = null;
	private String userName = null;
	private Integer customerId = null;
	private StudentImmediateReportForm savedForm = null;
	private StudentProfileInformation studentSearch = null;
	private String studentIdLabelName = "Student ID";
	private boolean studentIdConfigurable = false;
	private TestProductData testProductData = null;
    private TestProduct [] tps;
    private Hashtable productNameToIndexHash = null;
    private Hashtable productIdToProductName = null;
    private boolean islaslinkCustomer = false;
    
    private Integer rosterId = null;
    private Integer testAdminId = null;
	
	// customer configuration
	CustomerConfiguration[] customerConfigurations = null;
	CustomerConfigurationValue[] customerConfigurationsValue = null;
	
	public String[] gradeOptions = null;
	public String[] genderOptions = null;
	public String pageTitle = null;
	public String pageMessage = null;
	public String[] scoringStatusOptions = null;
	public String[] testNameOptions = null;
	public ScorableItem[] contentAreaNames = null;
	

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
	 * @jpf:forward name="success" path="beginIndivStudentScoring.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "beginIndivStudentScoring.do")
	})
	protected Forward begin()
	{
		return new Forward("success");
	}
	
	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "findStudent.do")
	})
	protected Forward beginIndivStudentScoring()
	{
		
		StudentImmediateReportForm form = initialize(ACTION_FIND_STUDENT);
		//this.searchApplied = false;
		initGradeGenderStatusTestNameOptions(ACTION_FIND_STUDENT, form, null, null ,null, null, null);
		isTopLevelUser(); //For defect #66662
		setupUserPermissions();
		this.pageTitle  = "Immediate Reporting: Find Student";
		return new Forward("success", form);
	}
	
	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="find_student_Scoring.jsp"
	 * @jpf:validation-error-forward name="failure" path="logout.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "find_student_immediate_score.jsp")},
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "logout.do"))
			protected Forward findStudent(StudentImmediateReportForm form)
	{   
		
		isGeorgiaCustomer(form);
		form.validateValues();
		
		String currentAction = form.getCurrentAction();
		String actionElement = form.getActionElement();
		form.resetValuesForAction(actionElement, ACTION_FIND_STUDENT);
		if(currentAction.equalsIgnoreCase("gotoReports")) {
			try
	        {
	            String url = "/TestSessionInfoWeb/homepage/viewReports.do";
	            getResponse().sendRedirect(url);
	        } 
	        catch (IOException ioe)
	        {
	            System.err.print(ioe.getStackTrace());
	        }
		}
		
		if(actionElement.equals(ACTION_DEFAULT)){
			initFindStudent(form);    
		}
		initPagingSorting(form);

		boolean applySearch = initSearch(form);
		ManageStudentData msData = null;
			if (applySearch)
			{
				msData = findByStudentProfile(form);    
				if ((msData != null) && (msData.getFilteredCount().intValue() == 0))
				{
					this.getRequest().setAttribute("searchResultEmpty", MessageResourceBundle.getMessage("searchResultEmpty"));        
				}
			}
		this.searchApplied = false;
		if ((msData != null) && (msData.getFilteredCount().intValue() > 0))
		{
			this.searchApplied = true;
			
			List studentList = StudentSearchUtils.buildStudentList(msData);
			//System.out.println("form.getStudentPageRequested()==>"+form.getStudentPageRequested());
			PagerSummary studentPagerSummary = StudentSearchUtils.buildStudentPagerSummary(msData, form.getStudentPageRequested());        
			form.setStudentMaxPage(msData.getFilteredPages());
			 
			this.getRequest().setAttribute("studentList", studentList);        
			this.getRequest().setAttribute("studentPagerSummary", studentPagerSummary);
			//this.getRequest().setAttribute("contentAreaList", this.contentAreaNames);
			HashMap valMap = new LinkedHashMap();
			valMap.put(FilterSortPageUtils.FILTERTYPE_ANY_CONTENT_AREA,FilterSortPageUtils.FILTERTYPE_ANY_CONTENT_AREA);
			for(ScorableItem si : this.contentAreaNames){
				valMap.put(si.getItemId().toString(), si.getItemSetName());
			}
			
			this.getRequest().setAttribute("contentAreaList", valMap);
		}
		
		this.getRequest().setAttribute("isFindStudent", Boolean.TRUE);
		
		this.pageTitle  = "Immediate Reporting: Find Student";
		
		//customerHasBulkAccommodation();
		//customerHasResetTestSessions();
		this.savedForm = form.createClone();    
		form.setCurrentAction(ACTION_DEFAULT);     
		this.studentSearch = form.getStudentProfile().createClone();    
		//setFormInfoOnRequest(form);
		return new Forward("success",form);
	}
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward getContentAreasForCatalog(StudentImmediateReportForm form){
					
	 HttpServletRequest req = getRequest();
	 HttpServletResponse resp = getResponse();
	 OutputStream stream = null;
	 String contentType = "application/json";
	 String json = "";
	 String catalogName = getRequest().getParameter("catalogName");
	 Integer catalogId = (Integer)this.productIdToProductName.get(catalogName);
	 ScorableItem[] contentAreas = null;
		
		try {
			if(catalogId != null)
				contentAreas =  this.studentManagement.getContentAreaForCatalog(this.userName, this.customerId, catalogId);
			
			List options = new ArrayList();
			//options.add(FilterSortPageUtils.FILTERTYPE_ANY_CONTENT_AREA);
			if(contentAreas != null) {
				for (int i=0 ; i<contentAreas.length ; i++) {        
					options.add(contentAreas[i]);
				}
			}
			this.contentAreaNames = (ScorableItem [])options.toArray( new ScorableItem [options.size()]);

			Gson gson = new Gson();
			json = gson.toJson(contentAreas);
				try{
					resp.setContentType("application/json");
		    		stream = resp.getOutputStream();
		    		resp.flushBuffer();
		    		stream.write(json.getBytes());
		    		
		    		//stream.write(json.getBytes());
			
		    		}
			
		    		 finally{
		 				if (stream!=null){
		 					stream.close();
		 				}
		 			}
				
				
			
		} catch (Exception e) {
			System.err.println("Exception while processing getContentAreasForCatalog");
			e.printStackTrace();
		}
		
		return null;
		
	}

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", action = "findStudentBySession") })
	public Forward beginImmediateStudentScoreByTestAdmin() {
		StudentImmediateReportForm form = initialize(ACTION_FIND_STUDENT);
		String reqTestAdminId = getRequest().getParameter("testAdminId");;
		if (reqTestAdminId != null) {
			form.setTestAdminId(Integer.valueOf(reqTestAdminId));
		}
		
		isTopLevelUser(); 
		setupUserPermissions();
		Forward forward = new Forward("success", form);
		return forward;
	}
	

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success",	path = "students_immediate_score_by_session.jsp")}, 
			validationErrorForward = @Jpf.Forward(name = "failure",	path = "logout.do"))
	public Forward findStudentBySession(StudentImmediateReportForm form) {
		isGeorgiaCustomer(form);
		form.validateValues();
		String currentAction = form.getCurrentAction();
		if(currentAction!=null && currentAction.equalsIgnoreCase("gotoHome")){
			try {
	            String url = "/TestSessionInfoWeb/homepage/HomePageController.jpf";
	            getResponse().sendRedirect(url);
	        } catch (IOException ioe) {
	            System.err.print(ioe.getStackTrace());
	        }
		}
		//form.setTestAdminId(234852); 
		setupSearchCriteria(form);
		ManageStudentData msData = findAllScoredStudentBySession(form); 
		if (msData != null) {
			List studentList = StudentSearchUtils.buildStudentList(msData);
			PagerSummary studentPagerSummary = StudentSearchUtils.buildStudentPagerSummary(msData, form.getStudentPageRequested());        
			form.setStudentMaxPage(msData.getFilteredPages());
			this.getRequest().setAttribute("studentList", studentList);        
			this.getRequest().setAttribute("studentPagerSummary", studentPagerSummary);
		}
		
		this.pageTitle  = "Immediate Reporting: Find Student";
		
		/*customerHasBulkAccommodation();
		customerHasResetTestSessions();*/
		this.savedForm = form.createClone();    
		/*form.setCurrentAction(ACTION_DEFAULT);     
		this.studentSearch = form.getStudentProfile().createClone();    
		setFormInfoOnRequest(form);*/
		return new Forward("success",form);
	}
	
	
	
	private ManageStudentData findAllScoredStudentBySession(StudentImmediateReportForm form) {
		
		PageParams page = FilterSortPageUtils.buildPageParams(form.getStudentPageRequested(), FilterSortPageUtils.PAGESIZE_10);
		SortParams sort = FilterSortPageUtils.buildStudentSortParams(form.getStudentSortColumn(), form.getStudentSortOrderBy());
		FilterParams filter = null;
		ManageStudentData msData = null;
		msData = StudentSearchUtils.findAllScoredStudentBySession(this.userName,this.studentManagement,form.getTestAdminId(), filter, page, sort);   
		this.pageMessage = MessageResourceBundle.getMessage("searchProfileFound");
		return msData;
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="listOfItem.jsp"
	 * @jpf:validation-error-forward name="failure" path="logout.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path ="find_student_reporting.jsp")}, 
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "logout.do"))
			protected Forward beginDisplayStudScoringReport(StudentImmediateReportForm form)
	{ 
			form.validateValues();
			this.pageTitle  = "Immediate Reporting: View Report";
			String currentAction = form.getCurrentAction();
			String actionElement = form.getActionElement();
			Integer testRosterId = Integer.valueOf(this.getRequest().getParameter("rosterId"));
			Integer testAdminId = Integer.valueOf(this.getRequest().getParameter("testAdminId"));
			Boolean isLaslinkCust = Boolean.valueOf(this.getRequest().getParameter("rosterId"));
			try {
				if(this.islaslinkCustomer) {
					StudentScoreReport stuReport = studentManagement.getStudentReport(testRosterId, testAdminId);
					form.setStudentNameRe(stuReport.getStudentName());
					form.setStudentExtPin1(stuReport.getStudentExtPin1());
					form.setTestAdminStartDate(stuReport.getTestAdminStartDate());
					form.setForm(stuReport.getForm());
					form.setGrade(stuReport.getGrade());
					form.setDistrict(stuReport.getDistrict());
					form.setSchool(stuReport.getSchool());
					form.setStudentReportIrsScoreVal(stuReport.getStudentReportIrsScore());
					this.getRequest().setAttribute("studentName", stuReport.getStudentName());
					this.getRequest().setAttribute("studentExtPin", stuReport.getStudentExtPin1());
					this.getRequest().setAttribute("startDate", form.getTestAdminStartDateStr());
					this.getRequest().setAttribute("formRe", stuReport.getForm());
					this.getRequest().setAttribute("grade", stuReport.getGrade());
					this.getRequest().setAttribute("district", stuReport.getDistrict());
					this.getRequest().setAttribute("school", stuReport.getSchool());
					this.getRequest().setAttribute("irsScores", stuReport.getStudentReportIrsScore());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			/* if(actionElement.equals(ACTION_DEFAULT))
			 {
				 form.setRosterId(Integer.valueOf(this.getRequest().getParameter("rosterId")));
				 form.setItemSetIdTC(Integer.valueOf(this.getRequest().getParameter("itemSetIdTC")));
				 form.setAccessCode(this.getRequest().getParameter("accessCode"));
				 form.setUserName(this.getRequest().getParameter("userName"));
				 form.setTestAdminId(Integer.valueOf(this.getRequest().getParameter("testAdminId")));
			 }
			if(form.getActionElement().equals(ACTION_DEFAULT)) {
				form.setActionElement(ACTION_DISPLAY_ITEMLIST);
				form.setCurrentAction(ACTION_DISPLAY_ITEMLIST);
			}
			
			currentAction = form.getCurrentAction();
			actionElement = form.getActionElement();
			form.resetValuesForAction(actionElement, ACTION_DISPLAY_ITEMLIST);
		
		 PageParams page =  FilterSortPageUtils.buildPageParams(form.getItemPageRequested(), FilterSortPageUtils.PAGESIZE_10);
		 SortParams sort = FilterSortPageUtils.buildItemSortParams(form.getItemSortColumn(), form.getItemSortOrderBy());
		 ScorableItemData sid = getTestItemForStudent(form.getRosterId(), form.getItemSetIdTC(), page, sort);//add productId
		 //ScorableItemData sid = getTestItemForStudent(1829973, 27775, page, sort);//add productId
         List itemList = buildItemList(sid); 
         PagerSummary itemPagerSummary = buildItemPagerSummary(sid, form.getItemPageRequested()); 
         form.setItemMaxPage(sid.getFilteredPages());
         try{
    		 TestSession testSession = scoring.getTestAdminDetails(form.getTestAdminId());
    		
         this.getRequest().setAttribute("itemList", itemList);
         this.getRequest().setAttribute("itemPagerSummary", itemPagerSummary);
         this.getRequest().setAttribute("accessCode", form.getAccessCode());
         this.getRequest().setAttribute("userName", form.getUserName());
         if(itemList.isEmpty())
         {	
        	 this.getRequest().setAttribute("itemSearchResultEmpty", MessageResourceBundle.getMessage("itemSearchResultEmpty"));        
        	 return new Forward("success");
         }*/
       return new Forward("success",form);
	}
	
	/**
	 * findByStudentProfile
	 */
	private ManageStudentData findByStudentProfile(StudentImmediateReportForm form)
	{
		String actionElement = form.getActionElement();
		form.resetValuesForAction(actionElement, ACTION_FIND_STUDENT);        

		String firstName = form.getStudentProfile().getFirstName().trim();
		String middleName = form.getStudentProfile().getMiddleName().trim();
		String lastName = form.getStudentProfile().getLastName().trim();
		String loginId = form.getStudentProfile().getUserName().trim();
		String studentNumber = form.getStudentProfile().getStudentNumber().trim();
		String grade = form.getStudentProfile().getGrade().trim();
		String gender = form.getStudentProfile().getGender().trim();
		String scoringStatus = form.getStudentProfile().getScoringStatus().trim();
        String productName = form.getStudentProfile().getProductNameList().trim();
        Integer productId = (Integer)this.productIdToProductName.get(productName);
        String contentAreaName = form.getStudentProfile().getCompletedContentArea().trim();
		if (! gender.equals(FilterSortPageUtils.FILTERTYPE_ANY_GENDER))
		{
			if (gender.equals("Male"))
				gender = "M";
			else if (gender.equals("Female"))
				gender = "F";
			else
				gender = "U";
		}
		if (! scoringStatus.equals(FilterSortPageUtils.FILTERTYPE_ANY_SCORING_STATUS))
		{
			if(scoringStatus.equals("Complete"))
				scoringStatus = "CO";
			else if(scoringStatus.equals("InComplete"))
				scoringStatus = "IN";
			
		}
		if (productName.equals(FilterSortPageUtils.FILTERTYPE_ANY_TESTNAME))
		{
			String requiredFields = "Test Name ";
			 requiredFields += ("<br/>" + Message.REQUIRED_TEXT);
			 form.setMessage("Missing required field", requiredFields, Message.ERROR);
			this.getRequest().setAttribute("pageMessage", form.getMessage());
			return null;
		}
		

		String invalidCharFields = WebUtils.verifyFindStudentInfo(firstName, lastName, middleName, studentNumber, loginId, form.studentIdLabelName);                

		if (invalidCharFields.length() > 0)
		{
			invalidCharFields += ("<br/>" +
					Message.INVALID_CHARS);
			form.setMessage(MessageResourceBundle.getMessage("invalid_char_message"), invalidCharFields, Message.ERROR);
			this.getRequest().setAttribute("pageMessage", form.getMessage());
			return null;
		}


		PageParams page = FilterSortPageUtils.buildPageParams(form.getStudentPageRequested(), FilterSortPageUtils.PAGESIZE_50);
		SortParams sort = FilterSortPageUtils.buildStudentSortParams(form.getStudentSortColumn(), form.getStudentSortOrderBy());
	 	 
	    
		FilterParams filter = FilterSortPageUtils.buildFilterParams(firstName, middleName, lastName, loginId, studentNumber, grade, gender,scoringStatus,contentAreaName);

		ManageStudentData msData = null;
        
		if (filter == null)
		{
			msData = StudentSearchUtils.searchAllStudentsAtAndBelow(this.userName, this.studentManagement,productId,page, sort);   
			this.pageMessage = MessageResourceBundle.getMessage("searchResultFound");
		}
		else
		{
			msData = StudentSearchUtils.searchStudentsByProfile(this.userName,this.studentManagement,productId, filter, page, sort);   
			this.pageMessage = MessageResourceBundle.getMessage("searchProfileFound");
		}

		return msData;
	}
	
	
	/**
	 * initialize
	 */
	private StudentImmediateReportForm initialize(String action)
	{        
		getUserDetails();
		this.savedForm = new StudentImmediateReportForm();
		this.savedForm.init();
		this.getSession().setAttribute("userHasReports", userHasReports());
		
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
			this.userName = (String)getSession().getAttribute("userName");

		try
		{
			this.user = this.studentManagement.getUserDetails(this.userName, this.userName);   
			this.customerId = user.getCustomer().getCustomerId();
		}
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
		}        
		getSession().setAttribute("userName", this.userName);

		getCustomerConfigurations();             
	}
	
	/**
	 * getCustomerConfigurations
	 */
	private void getCustomerConfigurations()
	{
		try {
				this.customerConfigurations = this.studentManagement.getCustomerConfigurations(this.userName, this.customerId);
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
	}
	
	/*
	 * this method retrieve CustomerConfigurationsValue for provided customer configuration Id.
	 */
	private void customerConfigurationValues(Integer configId)
	{
		try {
				this.customerConfigurationsValue = this.studentManagement.getCustomerConfigurationsValue(configId);
			
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
	}
	
	private void isTopLevelUser(){
		 
	    boolean isLaslinkCustomer = false;
		boolean isUserTopLevel = false;
		boolean isLaslinkUserTopLevel = false;
		boolean isLaslinkUser = false;
		try {
		for (CustomerConfiguration cc : customerConfigurations) {
			 if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")
						&& cc.getDefaultValue().equals("T")) {
					isLaslinkCustomer = true;
	            }
		}
		    this.setIslaslinkCustomer(isLaslinkCustomer);
		     isLaslinkUser = this.islaslinkCustomer;
		
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
	
	private void setupUserPermissions() {
		boolean hasScoringConfigurable = false;
		boolean hasResetTestSessionsConfigurable = false;
		boolean hasBulkStudentConfigurable = false;
		for (CustomerConfiguration cc : customerConfigurations) {
			if (cc.getCustomerConfigurationName().equalsIgnoreCase(
					"Configurable_Hand_Scoring")
					&& cc.getDefaultValue().equals("T")) {
				hasScoringConfigurable = true;
				continue;
			}
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_User_Reset_Subtest")
					&& cc.getDefaultValue().equals("T")) {
				hasResetTestSessionsConfigurable = true;
				continue;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation")
					&& cc.getDefaultValue().equals("T")) {
				hasBulkStudentConfigurable = true;
				continue;
            }

		}
		getSession().setAttribute("isScoringConfigured", hasScoringConfigurable);
		getSession().setAttribute("isResetTestSessionsConfigured", hasResetTestSessionsConfigurable);
		getSession().setAttribute("isBulkAccommodationConfigured", hasBulkStudentConfigurable);
		
	}
	
	private void isGeorgiaCustomer(StudentImmediateReportForm form) 
    {     
		 boolean isStudentIdConfigurable = false;
		 Integer configId=0;
		
		String []valueForStudentId = new String[8] ;
		
		for (int i=0; i < this.customerConfigurations.length; i++)
	        {
	            CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
	           
	            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID") && cc.getDefaultValue().equalsIgnoreCase("T"))
				{
					isStudentIdConfigurable = true; 
					configId = cc.getId();
					customerConfigurationValues(configId);
					//By default there should be 3 entries for customer configurations
				valueForStudentId = new String[8];
					for(int j=0; j<this.customerConfigurationsValue.length; j++){
						int sortOrder = this.customerConfigurationsValue[j].getSortOrder();
						valueForStudentId[sortOrder-1] = this.customerConfigurationsValue[j].getCustomerConfigurationValue();
					}	
				
				this.studentIdConfigurable = isStudentIdConfigurable;
				form.setStudentIdConfigurable(this.studentIdConfigurable);
				
					valueForStudentId = getDefaultValue(valueForStudentId,"Student ID", form);
					
				}
	            
	         }
		
		this.getRequest().setAttribute("studentIdArrValue",valueForStudentId);
        this.getRequest().setAttribute("isStudentIdConfigurable",isStudentIdConfigurable);
        
    }
	
	private String[] getDefaultValue(String [] arrValue, String labelName, StudentImmediateReportForm form)
	{
		arrValue[0] = arrValue[0] != null ? arrValue[0]   : labelName ;
		arrValue[1] = arrValue[1] != null ? arrValue[1]   : "32" ;
		
		
		if(labelName.equals("Student ID")){
			arrValue[2] = arrValue[2] != null ? arrValue[2]   : "F" ;
			if(!arrValue[2].equals("T") && !arrValue[2].equals("F"))
				{ 
					arrValue[2]  = "F";
				}
			this.studentIdLabelName = arrValue[0];
			form.setStudentIdLabelName(this.studentIdLabelName );
			
		}
		return arrValue;
	}
	
	
	/**
	 * initGradeGenderstatusTestNameOptions
	 */
	private void initGradeGenderStatusTestNameOptions(String action, StudentImmediateReportForm form, String grade, String gender, String scoringStatus, String testName, String comContentArea)
	{        
		this.gradeOptions = getGradeOptions(action);
		if (grade != null)
			form.getStudentProfile().setGrade(grade);
		else
			form.getStudentProfile().setGrade(this.gradeOptions[0]);
		
		this.testNameOptions = getTestNameOptions(action);
		if (testName != null){
			//System.out.println(testName+"..."+action);
			form.getStudentProfile().setProductNameList(testName);
		}
		else {
			//System.out.println(action+"..."+this.testNameOptions[0]);
			form.getStudentProfile().setProductNameList(this.testNameOptions[0]);
		}

		this.genderOptions = getGenderOptions( action );
		if (gender != null)
			form.getStudentProfile().setGender(gender);
		else
			form.getStudentProfile().setGender(this.genderOptions[0]);
		
		this.scoringStatusOptions = getScoringStatusOptions( action );
		if (scoringStatus != null)
			form.getStudentProfile().setScoringStatus(scoringStatus);
		else
			form.getStudentProfile().setScoringStatus(this.scoringStatusOptions[0]);
		
		//this.contentAreaNames = getContentAreaOptions( action );
		HashMap valMap = new LinkedHashMap();
		valMap.put(FilterSortPageUtils.FILTERTYPE_ANY_CONTENT_AREA,FilterSortPageUtils.FILTERTYPE_ANY_CONTENT_AREA);
		this.getRequest().setAttribute("contentAreaList", valMap);
				
		if (comContentArea != null)
			form.getStudentProfile().setCompletedContentArea(comContentArea);
		else
			form.getStudentProfile().setCompletedContentArea(FilterSortPageUtils.FILTERTYPE_ANY_CONTENT_AREA);
	}
	
	/**
	 * getContentAreaOptions
	 */
	/*private String [] getContentAreaOptions(String action)
	{
		String[] contentAreas = null;
		try {
			contentAreas =  this.studentManagement.getContentAreaForCustomer(this.userName, this.customerId);
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}

		List options = new ArrayList();
		if ( action.equals(ACTION_FIND_STUDENT) )
			options.add(FilterSortPageUtils.FILTERTYPE_ANY_CONTENT_AREA);
		

		for (int i=0 ; i<contentAreas.length ; i++) {        
			options.add(contentAreas[i]);
		}

		return (String [])options.toArray(new String[0]);        
	}*/
	
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
		

		options.add("Male");
		options.add("Female");
		options.add("Unknown");

		return (String [])options.toArray(new String[0]);        
	}
	
	/**
	 * getTestNameOptions
	 */
	private String [] getTestNameOptions(String action)
	{
		List testNameOption = null;
			 try
		        {
		            if (this.testProductData == null)
		            { // first time here 
		                this.testProductData = this.getTestCatalogDataForUser();
		                
		                
		            }
		            tps = this.testProductData.getTestProducts();
		            
		            if (testNameOption == null)
	                    testNameOption = createProductNameList(action,tps); 
	    
				
		}
			 
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		 String[] testNameOptions = (String[]) testNameOption.toArray(new String[0]);

		return testNameOptions;
		         
	  }
	
	/**
	 * getScoringStatus
	 */
	private String [] getScoringStatusOptions(String action)
	{
		List options = new ArrayList();
		if ( action.equals(ACTION_FIND_STUDENT) )
			options.add(FilterSortPageUtils.FILTERTYPE_ANY_SCORING_STATUS);
	    options.add("Complete");
		options.add("InComplete");

		return (String [])options.toArray(new String[0]);        
	}
	
	/**
	 * initPagingSorting
	 */
	private void initPagingSorting(StudentImmediateReportForm form)
	{
		String actionElement = form.getActionElement();

	
		if ((actionElement.indexOf("studentPageRequested") > 0) || (actionElement.indexOf("studentSortOrderBy") > 0))
		{
			form.setSelectedStudentId(null);
		}
		/*if ((actionElement.indexOf("itemPageRequested") > 0) || (actionElement.indexOf("itemSortOrderBy") > 0))
		{
			form.setSelectedStudentId(null);
		}*/
	}
	
	/**
	 * initSearch
	 */
	private boolean initSearch(StudentImmediateReportForm form)
	{
		boolean applySearch = false;
		String currentAction = form.getCurrentAction();

		if ((currentAction != null) && currentAction.equals(ACTION_APPLY_SEARCH))
		{
			applySearch = true;
			this.searchApplied = false;
			form.setStudentSortColumn(FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN);
			form.setStudentSortOrderBy(FilterSortPageUtils.ASCENDING);      
			form.setStudentPageRequested(new Integer(1));    
			form.setStudentMaxPage(new Integer(1));                  
		}

		if ((currentAction != null) && currentAction.equals(ACTION_CLEAR_SEARCH))
		{
			applySearch = false;
			this.searchApplied = false;
			form.clearSearch();
		}

		if (this.searchApplied)
		{
			applySearch = true;
		}
		else
		{
			form.setSelectedStudentId(null);
		}

		return applySearch;
	}
	
	private void setupSearchCriteria(StudentImmediateReportForm form)
	{
		
		
		if(form.getStudentSortColumn()==null || form.getStudentSortColumn().trim().length()==0 ) {
			form.setStudentSortColumn(FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN);
		}
		if(form.getStudentSortOrderBy()==null || form.getStudentSortOrderBy().trim().length()==0 ){
			form.setStudentSortOrderBy(FilterSortPageUtils.ASCENDING);      
		}
		if(form.getStudentPageRequested()==null || form.getStudentPageRequested()<1 ){
			form.setStudentPageRequested(new Integer(1));      
		}
		if(form.getStudentMaxPage()==null || form.getStudentMaxPage()<1 ){
			form.setStudentMaxPage(new Integer(1));      
		}

	}
	
	
	/**
	 * getTestCatalogDataForUser
	 */
		 private TestProductData getTestCatalogDataForUser() throws CTBBusinessException
		    {
		        TestProductData tpd = null;                
		        SortParams sortParams = FilterSortPageUtils.buildSortParams("TestCatalogName", ColumnSortEntry.ASCENDING, null, null);            
		       // tpd = this.scheduleTest.getTestProductsForUser(this.userName,null,null,sortParams);
		        tpd = this.scheduleTest.getTestCatalogForUser(this.userName,null,null,sortParams);
		        
		        return tpd;
		        
		    }
		 
		 /**
			 * createProductNameList
			 */
				 private List createProductNameList(String action,TestProduct [] tps)
				    {
				        List result = new ArrayList();   
				        this.productNameToIndexHash = new Hashtable();
				        this.productIdToProductName = new Hashtable();
				    	if ( action.equals(ACTION_FIND_STUDENT) )
				        result.add(FilterSortPageUtils.FILTERTYPE_ANY_TESTNAME);
				        for (int i=0; i< tps.length; i++) {
				            String catalogName = tps[i].getTestCatalogName();
				            Integer catalogId   = tps[i].getCatalogId();
				            catalogName = JavaScriptSanitizer.sanitizeString(catalogName);            
				            result.add(catalogName);
				            this.productNameToIndexHash.put(catalogName, new Integer(i));
				            this.productIdToProductName.put(catalogName, catalogId);
				        }
				        
				        return result;
				        
				    }
				 
				 /**
				  * initFindStudent
				  */
					private void initFindStudent(StudentImmediateReportForm form)
					{
							clearStudentProfileSearch(form);    
					}
					/**
					 * clearStudentProfileSearch
					 */
					private void clearStudentProfileSearch(StudentImmediateReportForm form)
					{   
						this.searchApplied = false;
						form.clearSearch();    

						form.setSelectedStudentId(null);
					}
	
	
	//---------------------------FORM---------------------------------------
	public static class StudentImmediateReportForm extends SanitizedFormData {
		
		private Integer selectedStudentId;
		private Message message;
		// student profile
		private StudentProfileInformation studentProfile;
		private String actionElement;
		private String currentAction;
		
		private String studentIdLabelName = "Student ID";
		private boolean studentIdConfigurable = false;
		
		// student pager
	    private String studentSortColumn;
		private String studentSortOrderBy;
		private Integer studentPageRequested;
		private Integer studentMaxPage;
		
		private Integer testAdminId = null;
		private Integer rosterId = null;
		private String studentNameRe = null;
		private Integer studentIdRe = null;
		private String studentExtPin1 = null;
		private Date testAdminStartDate = null;
		private String testAdminStartDateStr = null;
		private String form = null;
		private String district = null;
		private String school = null;
		private String grade = null;
		private StudentReportIrsScore[] studentReportIrsScoreVal;
		
		public StudentImmediateReportForm() {
			
		}
		
		public void init()
		{
			this.actionElement = ACTION_DEFAULT;
			this.currentAction = ACTION_DEFAULT;
			clearSearch();
			this.selectedStudentId = null;
			this.studentSortColumn = FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN;
			this.studentSortOrderBy = FilterSortPageUtils.ASCENDING;      
			this.studentPageRequested = new Integer(1);       
			this.studentMaxPage = new Integer(1);
			
			/*this.itemSortColumn = FilterSortPageUtils.TESTITEM_DEFAULT_SORT_COLUMN;
			this.itemSortOrderBy = FilterSortPageUtils.ASCENDING;      
			this.itemPageRequested = new Integer(1);    
            this.itemMaxPage = new Integer(1);      */
			
			this.studentProfile = new StudentProfileInformation();
		}
		
		public void validateValues()
		{
			if (this.studentSortColumn == null)
				this.studentSortColumn = FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN;

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
				
			}
			
			
			/*if (this.itemSortColumn == null)
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
				this.itemPageRequested = new Integer(this.itemMaxPage.intValue());                
				
			}*/
			
		}
		
		public StudentImmediateReportForm createClone()
		{
			StudentImmediateReportForm copied = new StudentImmediateReportForm();

			copied.setActionElement(this.actionElement);
			copied.setCurrentAction(this.currentAction);
			

			copied.setStudentSortColumn(this.studentSortColumn);
			copied.setStudentSortOrderBy(this.studentSortOrderBy);
			copied.setStudentPageRequested(this.studentPageRequested);      
			copied.setStudentMaxPage(this.studentMaxPage);

			copied.setStudentProfile(this.studentProfile);

			return copied;                    
		}
		
		public void resetValuesForAction(String actionElement, String fromAction) 
		{
			
			if (actionElement.equals("{actionForm.studentSortOrderBy}")) {
				this.studentPageRequested = new Integer(1);
			}
			/*if (actionElement.equals("{actionForm.itemSortOrderBy}")) {
				this.itemPageRequested = new Integer(1);
			}*/
			if (actionElement.equals("ButtonGoInvoked_studentSearchResult") ||
					actionElement.equals("EnterKeyInvoked_studentSearchResult")) {
			    	this.selectedStudentId = null;
			}
			if (actionElement.equals("ButtonGoInvoked_tablePathListAnchor") ||
					actionElement.equals("EnterKeyInvoked_tablePathListAnchor")) {
				
				if (fromAction.equals(ACTION_FIND_STUDENT)){
					this.selectedStudentId = null;
				}
			}
		}
		
		public void clearSearch()
		{   
			this.studentProfile = new StudentProfileInformation();
			this.studentProfile.setGrade(FilterSortPageUtils.FILTERTYPE_ANY_GRADE);
			this.studentProfile.setGender(FilterSortPageUtils.FILTERTYPE_ANY_GENDER); 
			this.studentProfile.setScoringStatus(FilterSortPageUtils.FILTERTYPE_ANY_SCORING_STATUS);
			this.studentProfile.setProductNameList(FilterSortPageUtils.FILTERTYPE_ANY_TESTNAME);
			this.studentProfile.setCompletedContentArea(FilterSortPageUtils.FILTERTYPE_ANY_CONTENT_AREA);
		}
		
		public void setActionElement(String actionElement)
		{
			this.actionElement = actionElement;
		}        
		public String getActionElement()
		{
			return this.actionElement != null ? this.actionElement : ACTION_DEFAULT;
		}
		public void setCurrentAction(String currentAction)
		{
			this.currentAction = currentAction;
		}
		public String getCurrentAction()
		{
			return this.currentAction != null ? this.currentAction : ACTION_DEFAULT;
		}
		// student profile
		public void setStudentProfile(StudentProfileInformation studentProfile)
		{
			this.studentProfile = studentProfile;
		}
		public StudentProfileInformation getStudentProfile()
		{
			if (this.studentProfile == null) this.studentProfile = new StudentProfileInformation();
	
			return this.studentProfile;
		}
		public String getStudentIdLabelName() {
			return studentIdLabelName;
		}

		public void setStudentIdLabelName(String studentIdLabelName) {
			this.studentIdLabelName = studentIdLabelName;
		}
		
		/**
		 * @return the studentIdConfigurable
		 */
		public boolean isStudentIdConfigurable() {
			return studentIdConfigurable;
		}

		/**
		 * @param studentIdConfigurable the studentIdConfigurable to set
		 */
		public void setStudentIdConfigurable(boolean studentIdConfigurable) {
			this.studentIdConfigurable = studentIdConfigurable;
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
		
		public Message getMessage()
		{
			return this.message != null ? this.message : new Message();
		}       
		public void setMessage(Message message)
		{
			this.message = message;
		}
		public void setMessage(String title, String content, String type)
		{
			this.message = new Message(title, content, type);
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
		 * @return the rosterId
		 */
		public Integer getRosterId() {
			return rosterId;
		}

		/**
		 * @param rosterId the rosterId to set
		 */
		public void setRosterId(Integer rosterId) {
			this.rosterId = rosterId;
		}

		/**
		 * @return the studentNameRe
		 */
		public String getStudentNameRe() {
			return studentNameRe;
		}

		/**
		 * @param studentNameRe the studentNameRe to set
		 */
		public void setStudentNameRe(String studentNameRe) {
			this.studentNameRe = studentNameRe;
		}

		/**
		 * @return the studentIdRe
		 */
		public Integer getStudentIdRe() {
			return studentIdRe;
		}

		/**
		 * @param studentIdRe the studentIdRe to set
		 */
		public void setStudentIdRe(Integer studentIdRe) {
			this.studentIdRe = studentIdRe;
		}

		/**
		 * @return the studentExtPin1
		 */
		public String getStudentExtPin1() {
			return studentExtPin1;
		}

		/**
		 * @param studentExtPin1 the studentExtPin1 to set
		 */
		public void setStudentExtPin1(String studentExtPin1) {
			this.studentExtPin1 = studentExtPin1;
		}

		/**
		 * @return the testAdminStartDate
		 */
		public Date getTestAdminStartDate() {
			return testAdminStartDate;
		}

		/**
		 * @param testAdminStartDate the testAdminStartDate to set
		 */
		public void setTestAdminStartDate(Date testAdminStartDate) {
			this.testAdminStartDate = testAdminStartDate;
		}

		/**
		 * @return the form
		 */
		public String getForm() {
			return form;
		}

		/**
		 * @param form the form to set
		 */
		public void setForm(String form) {
			this.form = form;
		}

		/**
		 * @return the district
		 */
		public String getDistrict() {
			return district;
		}

		/**
		 * @param district the district to set
		 */
		public void setDistrict(String district) {
			this.district = district;
		}

		/**
		 * @return the school
		 */
		public String getSchool() {
			return school;
		}

		/**
		 * @param school the school to set
		 */
		public void setSchool(String school) {
			this.school = school;
		}

		/**
		 * @return the grade
		 */
		public String getGrade() {
			return grade;
		}

		/**
		 * @param grade the grade to set
		 */
		public void setGrade(String grade) {
			this.grade = grade;
		}

		/**
		 * @return the testAdminStartDateStr
		 */
		public String getTestAdminStartDateStr() {
			if (this.testAdminStartDate != null) {
	            this.testAdminStartDateStr = DateUtils.formatDateToDateString(this.testAdminStartDate, DateUtils.DATE_FORMAT_CHAR);     
	            StringTokenizer tokenizer = new StringTokenizer(this.testAdminStartDateStr, "/");
	            this.testAdminStartDateStr = DateUtils.formatDateToDateString(this.testAdminStartDate, DateUtils.DATE_FORMAT_DISPLAY);     
	        }
			return testAdminStartDateStr;
		}

		/**
		 * @param testAdminStartDateStr the testAdminStartDateStr to set
		 */
		public void setTestAdminStartDateStr(String testAdminStartDateStr) {
			this.testAdminStartDateStr = testAdminStartDateStr;
		}

		/**
		 * @return the studentReportIrsScoreVal
		 */
		public StudentReportIrsScore[] getStudentReportIrsScoreVal() {
			return studentReportIrsScoreVal;
		}

		/**
		 * @param studentReportIrsScoreVal the studentReportIrsScoreVal to set
		 */
		public void setStudentReportIrsScoreVal(
				StudentReportIrsScore[] studentReportIrsScoreVal) {
			this.studentReportIrsScoreVal = studentReportIrsScoreVal;
		}
		
	}
	
	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	
	public String getPageMessage() {
		return pageMessage;
	}

	public void setPageMessage(String pageMessage) {
		this.pageMessage = pageMessage;
	}
	
	public String[] getGradeOptions() {
		return gradeOptions;
	}

	public void setGradeOptions(String[] gradeOptions) {
		this.gradeOptions = gradeOptions;
	}

	public String[] getGenderOptions() {
		return genderOptions;
	}

	public void setGenderOptions(String[] genderOptions) {
		this.genderOptions = genderOptions;
	}
	
	public String[] getScoringStatusOptions() {
		return scoringStatusOptions;
	}

	public void setScoringStatusOptions(String[] scoringStatusOptions) {
		this.scoringStatusOptions = scoringStatusOptions;
	}
	
	public String[] getTestNameOptions() {
		return testNameOptions;
	}

	public void setTestNameOptions(String[] testNameOptions) {
		this.testNameOptions = testNameOptions;
	}
	
	public boolean isSearchApplied() {
		return searchApplied;
	}

	public void setSearchApplied(boolean searchApplied) {
		this.searchApplied = searchApplied;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public ScorableItem[] getContentAreaNames() {
		return contentAreaNames;
	}

	public void setContentAreaNames(ScorableItem[] contentAreaNames) {
		this.contentAreaNames = contentAreaNames;
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

	/**
	 * @return the rosterId
	 */
	public Integer getRosterId() {
		return rosterId;
	}

	/**
	 * @param rosterId the rosterId to set
	 */
	public void setRosterId(Integer rosterId) {
		this.rosterId = rosterId;
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
}