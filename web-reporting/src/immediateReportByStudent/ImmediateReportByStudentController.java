package immediateReportByStudent;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

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
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.bean.testAdmin.TestProductData;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.web.sanitizer.JavaScriptSanitizer;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.ColumnSortEntry;
import com.ctb.widgets.bean.PagerSummary;

@Jpf.Controller
public class ImmediateReportByStudentController extends PageFlowController {
	private static final long serialVersionUID = 1L;
	
	@Control()
	private com.ctb.control.studentManagement.StudentManagement studentManagement;
	
	@Control()
    private com.ctb.control.testAdmin.ScheduleTest scheduleTest;
	
	
	
	private static final String ACTION_DEFAULT           = "defaultAction";
	private static final String ACTION_FIND_STUDENT      = "findStudent";
	private static final String ACTION_APPLY_SEARCH   = "applySearch";
	private static final String ACTION_CLEAR_SEARCH   = "clearSearch";
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
	
	// customer configuration
	CustomerConfiguration[] customerConfigurations = null;
	CustomerConfigurationValue[] customerConfigurationsValue = null;
	
	public String[] gradeOptions = null;
	public String[] genderOptions = null;
	public String pageTitle = null;
	public String pageMessage = null;
	public String[] scoringStatusOptions = null;
	public String[] testNameOptions = null;
	public String[] contentAreaNames = null;
	

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
		//isTopLevelUser(); //For defect #66662
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
		}
		
		this.getRequest().setAttribute("isFindStudent", Boolean.TRUE);
		
		this.pageTitle  = "Scoring: Find Student";
		
		//customerHasBulkAccommodation();
		//customerHasResetTestSessions();
		this.savedForm = form.createClone();    
		form.setCurrentAction(ACTION_DEFAULT);     
		this.studentSearch = form.getStudentProfile().createClone();    
		//setFormInfoOnRequest(form);
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


		PageParams page = FilterSortPageUtils.buildPageParams(form.getStudentPageRequested(), FilterSortPageUtils.PAGESIZE_10);
		SortParams sort = FilterSortPageUtils.buildStudentSortParams(form.getStudentSortColumn(), form.getStudentSortOrderBy());
	 	 
	    
		FilterParams filter = FilterSortPageUtils.buildFilterParams(firstName, middleName, lastName, loginId, studentNumber, grade, gender,scoringStatus);

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
		
		this.contentAreaNames = getContentAreaOptions( action );
		if (comContentArea != null)
			form.getStudentProfile().setCompletedContentArea(comContentArea);
		else
			form.getStudentProfile().setCompletedContentArea(this.contentAreaNames[0]);
	}
	
	/**
	 * getContentAreaOptions
	 */
	private String [] getContentAreaOptions(String action)
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
		/*	if (actionElement.equals("ButtonGoInvoked_studentSearchResult") ||
					actionElement.equals("EnterKeyInvoked_studentSearchResult")) {
			    	this.selectedStudentId = null;
			}
			if (actionElement.equals("ButtonGoInvoked_tablePathListAnchor") ||
					actionElement.equals("EnterKeyInvoked_tablePathListAnchor")) {
				
				if (fromAction.equals(ACTION_FIND_STUDENT)){
					this.selectedStudentId = null;
				}
			}*/
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

	public String[] getContentAreaNames() {
		return contentAreaNames;
	}

	public void setContentAreaNames(String[] contentAreaNames) {
		this.contentAreaNames = contentAreaNames;
	}
}