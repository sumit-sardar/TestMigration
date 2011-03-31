package studentScoringPageFlow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.FilterSortPageUtils;
import utils.MessageResourceBundle;
import utils.StudentProfileInformation;
import utils.StudentSearchUtils;
import utils.WebUtils;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.ScorableItem;
import com.ctb.bean.testAdmin.ScorableItemData;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.bean.testAdmin.TestProductData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.web.sanitizer.JavaScriptSanitizer;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.ColumnSortEntry;
import com.ctb.widgets.bean.PagerSummary;

import utils.Message;



@Jpf.Controller 
public class StudentScoringController extends PageFlowController {
	private static final long serialVersionUID = 1L;
	
	/**
     * @common:control
     */
	@Control()
	private com.ctb.control.studentManagement.StudentManagement studentManagement;
	
	 /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.ScheduleTest scheduleTest;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.crscoring.TestScoring testScoring;
        
    @org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.CRScoring scoring;
	
	private static final String ACTION_DEFAULT           = "defaultAction";
	private static final String ACTION_FIND_STUDENT      = "findStudent";
	private static final String ACTION_APPLY_SEARCH   = "applySearch";
	private static final String ACTION_CLEAR_SEARCH   = "clearSearch";
	private static final String ACTION_DISPLAY_ITEMLIST   = "displayItemList";
	
	private boolean searchApplied = false;
	
	private User user = null;
	private String userName = null;
	private Integer customerId = null;
    private TestProductData testProductData = null;
    private TestProduct [] tps; 
    private Hashtable productNameToIndexHash = null;
    private Hashtable productIdToProductName = null;
    public String[] gradeOptions = null;
	public String[] genderOptions = null;
	public String[] scoringStatusOptions = null;
	public String[] testNameOptions = null;
	public String pageTitle = null;
	public String pageMessage = null;
	private Message message;
	
	
	private Integer rosterId = null;
    private Integer itemSetIdTC = null;
    private String accessCode = null;
    private String stuUserName = null;
    private Integer testAdminId = null;
	
	private StudentScoringForm savedForm = null;
	
	// customer configuration
	CustomerConfiguration[] customerConfigurations = null;
	CustomerConfigurationValue[] customerConfigurationsValue = null;
	
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
		
		StudentScoringForm form = initialize(ACTION_FIND_STUDENT);
		this.searchApplied = false;
		initGradeGenderStatusTestNameOptions(ACTION_FIND_STUDENT, form, null, null ,null, null);
		
		return new Forward("success", form);
	}
	
	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="find_student_Scoring.jsp"
	 * @jpf:validation-error-forward name="failure" path="logout.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "find_student_Scoring.jsp")},
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "logout.do"))
			protected Forward findStudent(StudentScoringForm form)
	{    
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
			System.out.println("form.getStudentPageRequested()==>"+form.getStudentPageRequested());
			PagerSummary studentPagerSummary = StudentSearchUtils.buildStudentPagerSummary(msData, form.getStudentPageRequested());        
			form.setStudentMaxPage(msData.getFilteredPages());
			 
			this.getRequest().setAttribute("studentList", studentList);        
			this.getRequest().setAttribute("studentPagerSummary", studentPagerSummary);
		}

		form.setCurrentAction(ACTION_DEFAULT);
		this.getRequest().setAttribute("isFindStudent", Boolean.TRUE);
		
		this.pageTitle  = "Scoring: Find Student";
		
		customerHasBulkAccommodation();
		
		//setFormInfoOnRequest(form);
		return new Forward("success");
	}
	
	
	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="listOfItem.jsp"
	 * @jpf:validation-error-forward name="failure" path="logout.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path ="listOfItem.jsp")}, 
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "logout.do"))
			protected Forward beginDisplayStudItemList(StudentScoringForm form)
	{ 
			form.validateValues();
			System.out.println(form.getActionElement());
			if(form.getActionElement().equals(ACTION_DEFAULT)) {
				form.setActionElement(ACTION_DISPLAY_ITEMLIST);
				form.setCurrentAction(ACTION_DISPLAY_ITEMLIST);
			}
			
			String currentAction = form.getCurrentAction();
			String actionElement = form.getActionElement();
			
			
		// System.out.println("form data:" +  form.getStudentPageRequested());
		 if(actionElement.equals(ACTION_DISPLAY_ITEMLIST))
		 {
		 this.setRosterId(Integer.valueOf(this.getRequest().getParameter("rosterId")));
		 this.setItemSetIdTC(Integer.valueOf(this.getRequest().getParameter("itemSetIdTC")));
		 this.setAccessCode(this.getRequest().getParameter("accessCode"));
		 this.setStuUserName(this.getRequest().getParameter("userName"));
		 this.setTestAdminId(Integer.valueOf(this.getRequest().getParameter("testAdminId")));
		 }
		 
		 PageParams page =  FilterSortPageUtils.buildPageParams(form.getItemPageRequested(), FilterSortPageUtils.PAGESIZE_10);
		 SortParams sort = FilterSortPageUtils.buildItemSortParams(form.getItemSortColumn(), form.getItemSortOrderBy());
		 ScorableItemData sid = getTestItemForStudent(form.getRosterId(), form.getItemSetIdTC(), page, sort);//add productId
		 //ScorableItemData sid = getTestItemForStudent(1829973, 27775, page, sort);//add productId
         List itemList = buildItemList(sid); 
         PagerSummary itemPagerSummary = buildItemPagerSummary(sid, form.getItemPageRequested()); 
         form.setItemMaxPage(sid.getFilteredPages());
         try{
    		 TestSession testSession = scoring.getTestAdminDetails(this.getTestAdminId());
    		 this.getRequest().setAttribute("testSessionName", testSession.getTestAdminName());
    	}catch(Exception e){
    			 e.printStackTrace();
    	}
        
         this.getRequest().setAttribute("itemList", itemList);
         this.getRequest().setAttribute("itemPagerSummary", itemPagerSummary);
         this.getRequest().setAttribute("accessCode", this.getAccessCode());
         this.getRequest().setAttribute("userName", this.getStuUserName());
         if(itemList.isEmpty())
         {	
        	 this.getRequest().setAttribute("itemSearchResultEmpty", MessageResourceBundle.getMessage("itemSearchResultEmpty"));        
        	 return new Forward("success");
         }
       return new Forward("success");
	}
	
	
	/**
	 * initialize
	 */
	private StudentScoringForm initialize(String action)
	{        
		getUserDetails();
		this.savedForm = new StudentScoringForm();
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
	
	/**
	 * Bulk Accommodation
	 */
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
	 * getTestNameOptions
	 */
	private String [] getTestNameOptions(String action)
	{
		List testNameOption = null;
			 try
		        {
		            if (this.testProductData == null)
		            { // first time here 
		                this.testProductData = this.getTestProductDataForUser();
		                
		                
		            }
		            tps = this.testProductData.getTestProducts();
		            
		            if (testNameOption == null)
	                    testNameOption = createProductNameList(tps); 
	    
				
		}
			 
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		 String[] testNameOptions = (String[]) testNameOption.toArray(new String[0]);

		return testNameOptions;
		         
	  }
	       
	        
	   
	
	/**
	 * getTestProductDataForUser
	 */
		 private TestProductData getTestProductDataForUser() throws CTBBusinessException
		    {
		        TestProductData tpd = null;                
		        SortParams sortParams = FilterSortPageUtils.buildSortParams("ProductName", ColumnSortEntry.ASCENDING, null, null);            
		        tpd = this.scheduleTest.getTestProductsForUser(this.userName,null,null,sortParams);
		        
		        return tpd;
		        
		    }
		 
	/**
	 * createProductNameList
	 */
		 private List createProductNameList(TestProduct [] tps)
		    {
		        List result = new ArrayList();   
		        this.productNameToIndexHash = new Hashtable();
		        this.productIdToProductName = new Hashtable();
		        for (int i=0; i< tps.length; i++) {
		            String productName = tps[i].getProductName();
		            Integer productId   = tps[i].getProductId();
		            productName = JavaScriptSanitizer.sanitizeString(productName);            
		            result.add(productName);
		            this.productNameToIndexHash.put(productName, new Integer(i));
		            this.productIdToProductName.put(productName, productId);
		        }
		        
		        return result;
		        
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
	 * initGradeGenderstatusTestNameOptions
	 */
	private void initGradeGenderStatusTestNameOptions(String action, StudentScoringForm form, String grade, String gender ,String scoringStatus ,String testName)
	{        
		this.gradeOptions = getGradeOptions(action);
		if (grade != null)
			form.getStudentProfile().setGrade(grade);
		else
			form.getStudentProfile().setGrade(this.gradeOptions[0]);
		
		this.testNameOptions = getTestNameOptions(action);
		if (testName != null)
			form.getStudentProfile().setProductNameList(testName);
		else
			form.getStudentProfile().setProductNameList(this.testNameOptions[0]);

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
	}
	
	
	/**
	 * initFindStudent
	 */
	private void initFindStudent(StudentScoringForm form)
	{
			clearStudentProfileSearch(form);    
	}
	/**
	 * clearStudentProfileSearch
	 */
	private void clearStudentProfileSearch(StudentScoringForm form)
	{   
		this.searchApplied = false;
		form.clearSearch();    

		form.setSelectedStudentId(null);
	}
	
	/**
	 * initPagingSorting
	 */
	private void initPagingSorting(StudentScoringForm form)
	{
		String actionElement = form.getActionElement();

	
		if ((actionElement.indexOf("studentPageRequested") > 0) || (actionElement.indexOf("studentSortOrderBy") > 0))
		{
			form.setSelectedStudentId(null);
		}
		if ((actionElement.indexOf("itemPageRequested") > 0) || (actionElement.indexOf("itemSortOrderBy") > 0))
		{
			form.setSelectedStudentId(null);
		}
	}
	
	/**
	 * initSearch
	 */
	private boolean initSearch(StudentScoringForm form)
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
	 
	private List buildItemList(ScorableItemData tid) 
    {
        ArrayList itemList = new ArrayList();
        if (tid != null) {
        	 ScorableItem[] testItemDetails = tid.getScorableItems();    
            for (int i=0 ; i<testItemDetails.length ; i++) {
            	ScorableItem itemDetail = (ScorableItem)testItemDetails[i];
                if (itemDetail != null) {
                   
                    itemList.add(itemDetail);
                }
            }
        }
        return itemList;
    }
    
    private PagerSummary buildItemPagerSummary(ScorableItemData sid, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);              
        pagerSummary.setTotalObjects(sid.getFilteredCount());
        pagerSummary.setTotalPages(sid.getFilteredPages());
        pagerSummary.setTotalFilteredObjects(null);        
        return pagerSummary;
    }
    
	
	/**
	 * findByStudentProfile
	 */
	private ManageStudentData findByStudentProfile(StudentScoringForm form)
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
		if (! gender.equals(FilterSortPageUtils.FILTERTYPE_ANY_SCORING_STATUS))
		{
			if(scoringStatus.equals("Complete"))
				scoringStatus = "C";
			else if(scoringStatus.equals("InComplete"))
				scoringStatus = "I";
			
		}
		if (productName.equals(FilterSortPageUtils.FILTERTYPE_ANY_TESTNAME))
		{
			String requiredFields = "";
			 requiredFields += (Message.REQUIRED_TEXT);
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
	
	
	public static class StudentScoringForm extends SanitizedFormData
	{
		private Integer selectedStudentId;
		private Integer testRosterId;
		private Integer itemSetId;
		private String studentIdLabelName = "Student ID";
		private Message message;
		 private String selectedProductName;  
		
		// student profile
		private StudentProfileInformation studentProfile;
		private String actionElement;
		private String currentAction;
		
		// student pager
	    private String studentSortColumn;
		private String studentSortOrderBy;
		private Integer studentPageRequested;
		private Integer studentMaxPage;
		
		//item pager
		private String itemSortColumn ;
	    private String itemSortOrderBy;
	    private Integer itemPageRequested;
	    private Integer itemMaxPage;
	    
	    private Integer rosterId = null;
	    private Integer itemSetIdTC = null;
	    private String accessCode = null;
	    private String userName = null;
	    private Integer testAdminId = null;
		 

		public StudentScoringForm()
		{
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
			
			this.itemSortColumn = FilterSortPageUtils.TESTITEM_DEFAULT_SORT_COLUMN;
			this.itemSortOrderBy = FilterSortPageUtils.ASCENDING;      
			this.itemPageRequested = new Integer(1);    
            this.itemMaxPage = new Integer(1);      
			
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
				this.itemPageRequested = new Integer(this.itemMaxPage.intValue());                
				
			}
			
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

		
		public void resetValuesForAction(String actionElement, String fromAction) 
		{
			
			if (actionElement.equals("{actionForm.studentSortOrderBy}")) {
				this.studentPageRequested = new Integer(1);
			}
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
		public void setItemPageRequested(Integer itemPageRequested)
	    {
	        this.itemPageRequested = itemPageRequested;
	    }
	    public Integer getItemPageRequested()
	    {
	        return this.itemPageRequested != null ? this.itemPageRequested : new Integer(1);
	    } 
	    public void setItemMaxPage(Integer itemMaxPage)
	    {
	        this.itemMaxPage = itemMaxPage;
	    }
	    public Integer getItemMaxPage()
	    {
	        return this.itemMaxPage;
	    }
	   
                              
        public void setItemSortColumn(String itemSortColumn)
        {
            this.itemSortColumn = itemSortColumn;
        }
        public String getItemSortColumn()
        {
            return this.itemSortColumn != null ? this.itemSortColumn : FilterSortPageUtils.TESTITEM_DEFAULT_SORT_COLUMN;
        }       
        public void setItemSortOrderBy(String itemSortOrderBy)
        {
            this.itemSortOrderBy = itemSortOrderBy;
        }
        public String getItemSortOrderBy()
        {
            return this.itemSortOrderBy != null ? this.itemSortOrderBy : FilterSortPageUtils.ASCENDING;
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
		 * @return the itemSetIdTC
		 */
		public Integer getItemSetIdTC() {
			return itemSetIdTC;
		}

		/**
		 * @param itemSetIdTC the itemSetIdTC to set
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
		 * @param accessCode the accessCode to set
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
		 * @param userName the userName to set
		 */
		public void setUserName(String userName) {
			this.userName = userName;
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

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
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


	public CustomerConfigurationValue[] getCustomerConfigurationsValue() {
		return customerConfigurationsValue;
	}

	public void setCustomerConfigurationsValue(
			CustomerConfigurationValue[] customerConfigurationsValue) {
		this.customerConfigurationsValue = customerConfigurationsValue;
	}

	public void setCustomerConfigurations(
			CustomerConfiguration[] customerConfigurations) {
		this.customerConfigurations = customerConfigurations;
	}

	public String getPageMessage() {
		return pageMessage;
	}

	public void setPageMessage(String pageMessage) {
		this.pageMessage = pageMessage;
	}

	public String[] getTestNameOptions() {
		return testNameOptions;
	}

	public void setTestNameOptions(String[] testNameOptions) {
		this.testNameOptions = testNameOptions;
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
	 * @return the itemSetIdTC
	 */
	public Integer getItemSetIdTC() {
		return itemSetIdTC;
	}

	/**
	 * @param itemSetIdTC the itemSetIdTC to set
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
	 * @param accessCode the accessCode to set
	 */
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	/**
	 * @return the stuUserName
	 */
	public String getStuUserName() {
		return stuUserName;
	}

	/**
	 * @param stuUserName the stuUserName to set
	 */
	public void setStuUserName(String stuUserName) {
		this.stuUserName = stuUserName;
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