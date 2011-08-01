package scheduleTestPageflow;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import utils.DateUtils;
import utils.FilterSortPageUtils;
import utils.MessageResourceBundle;
import utils.PathListUtils;
import utils.TABESubtestValidation;
import utils.TestRosterFilter;
import utils.TestSessionUtils;
import utils.WebUtils;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.SessionStudentData;
import com.ctb.bean.testAdmin.StudentManifest;
import com.ctb.bean.testAdmin.StudentManifestData;
import com.ctb.bean.testAdmin.StudentNode;
import com.ctb.bean.testAdmin.StudentNodeData;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestElementData;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.bean.testAdmin.TestProductData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserData;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.testAdmin.InsufficientLicenseQuantityException;
import com.ctb.exception.testAdmin.TransactionTimeoutException;
import com.ctb.exception.validation.ValidationException;
import com.ctb.forms.FormFieldValidator;
import com.ctb.util.web.sanitizer.JavaScriptSanitizer;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.ColumnSortEntry;
import com.ctb.widgets.bean.PagerSummary;

import data.Condition;
import data.Message;
import data.PathNode;
import data.StatePathList;
import data.StateScheduler;
import data.SubtestVO;
import data.TestAdminVO;
import data.TestRosterVO;
import data.TestSummaryVO;
import data.TestVO;


/**
 * This is the default controller for a blank web application.
 *
 * @jpf:message-resources resources="ErrorMessages"
 * @jpf:controller
 *  */
@Jpf.Controller(messageBundles = { 
    @Jpf.MessageBundle(bundlePath = "ErrorMessages")
})
public class ScheduleTestController extends PageFlowController
{
    static final long serialVersionUID = 1L; 

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.ScheduleTest scheduleTest;
    
    /**
     * @common:control
     */    
    @Control()
    private com.ctb.control.db.Users users;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.ItemSet itemSet;
    
    
    // LLO- 118 - Change for Ematrix UI
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;

    
    public static final String ACTION_SCHEDULE_TEST = "schedule";
    public static final String ACTION_VIEW_TEST = "view";
    public static final String ACTION_EDIT_TEST = "edit";
    public static final String ACTION_COPY_TEST = "copy";
    public static final String ACTION_DEFAULT = "defaultAction";
    public static final String ACTION_AUTOLOCATOR      = "autoLocator";
    public static final String ACTION_SUBTEST_VALIDATION_FAILED  = "subtestValidationFailed";
    
    public String [] nameOptions = {FilterSortPageUtils.FILTERTYPE_CONTAINS, FilterSortPageUtils.FILTERTYPE_BEGINSWITH, FilterSortPageUtils.FILTERTYPE_EQUALS};
    private String [] formOptions = {FilterSortPageUtils.FILTERTYPE_SHOWALL};
    public String [] accommodationTypeOptions = {FilterSortPageUtils.STUDENTS_WITH_AND_WITHOUT_ACCOMMODATIONS, FilterSortPageUtils.STUDENTS_WITH_ACCOMMODATIONS, FilterSortPageUtils.STUDENTS_WITHOUT_ACCOMMODATIONS};
    public String [] selectedAccommodationsOptions = {"Calculator", "Color/Font", "ScreenReader", "TestPause", "UntimedTest"};
    
    public String blankInputString = "";
    public List levelOptions = null;

    // states and conditions
    public Condition condition = new Condition();
    public StateScheduler stateScheduler = new StateScheduler();
    
    // scheduling
    private String action = ACTION_SCHEDULE_TEST;
    private List productNameList = null;    
    private List levelList = null;    
    private List gradeList = null;    
    private List testList = null;
    private List studentNodes = null;
    private String selectedProductName = null;
    private String selectedLevel = null;
    private String selectedTestId = null;
    private String testAccessCode = null;
    private List timeZoneList = DateUtils.getTimeZoneList();    
    private List timeList = DateUtils.getTimeList();    
    private String [] formList = null;    
    private Hashtable productNameToIndexHash = null;
    private List selectedStudents = null;    
    private List selectedProctors = null;    
    private Integer testAdminId = null;
    private Integer itemSetId = null;
    private Integer studentId = null;
    private String showLevelOrGrade = "level";
    private TestProduct [] tps;
  //  private boolean gradeFlag= false;//changes for performance enhancement

    // scheduler and product info
    private String userName = null;
    private User user = null;
    private User scheduler = null;    
    private User originalScheduler = null; 
    private TestProductData testProductData = null;    
    private String productType = TestSessionUtils.GENERIC_PRODUCT_TYPE;
    
    // print options page
    private Integer orgNodeId = null;    
    private List orgNodePath = null;
    private int addedStudentsCount=0;
    private int addedProctorsCount=0;    
    private String testSessionId = null;    
    private ScheduledSession scheduledSession = null;    
    private HashMap topNodesMap = null;
    private TestRosterFilter testRosterFilter= new TestRosterFilter();        
    private String currentSelectedTestId = null;
    private String overrideFormAssignment = null;
    private Date overrideLoginStartDate = null;
    private SessionStudent [] restrictedSessionStudents = null;
    private List testTicketTestList = null;
    
    // modify subtests
    public SubtestVO locatorSubtest = null; // locator subtest in battery and survey
    private List sessionSubtests = null;    // original subtests for this session, excluding locator subtest
    public List defaultSubtests = null;     // default subtests that use for editing and display in jsp
    private List allSubtests = null;        // all subtests that use for editing
    private List availableSubtests = null;  // available subtests that use for editing
    private List selectedSubtests = null;   // current selected subtests 
    
     //Randomized Distractor Constant Value
    private static final String RD_YES_OPTION = "Yes";
    private static final String RD_NO_OPTION = "No";
    private static final String RD_YES = "Y";
    private static final String RD_NO = "N";
    
    private boolean isTestSessionDataExported  = false;
    private String hasReport = null;
    private String bulkAcc = null;
    //Changes for defect in performance tuning
    private boolean gradeFlag = false;
    
    // LLO- 118 - Change for Ematrix UI
	private boolean isTopLevelUser = false;
	private boolean islaslinkCustomer = false;
    
    
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

	public String [] getFormOptions() {
		return formOptions;
	}

	public void setFormOptions(String [] formOptions) {
		this.formOptions = formOptions;
	}
    
	
    /**
     * @jpf:action
     * @jpf:forward name="success" path="selectTest.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "selectTest.do")
    })
    protected Forward begin(ScheduleTestForm form)
    {
    	this.hasReport = (String)getRequest().getParameter("hasReport");
    	this.bulkAcc = (String)getRequest().getParameter("bulkAcc");
    	   	
    	//customerHasScoring();//For hand scoring changes
    	//this.getSession().setAttribute("isBulkAccommodationConfigured",  customerHasScoring());    
    	   	
        init(form);
		
		// customerHasScoring(); //For hand scoring changes //Commented: TAS Scalability Part III
        
        this.action = ACTION_SCHEDULE_TEST;
        
        return new Forward("success", form);
    }
    
    private void init(ScheduleTestForm form)
    {
        form.init();
        
        java.security.Principal principal = getRequest().getUserPrincipal();
        this.userName = principal.toString();
        
        getSession().setAttribute("userName", this.userName);

        this.topNodesMap = new LinkedHashMap();
        UserNodeData und =null;

        try
        {
            this.user = this.scheduleTest.getUserDetails(this.userName, this.userName);
            
            Boolean supportAccommodations = Boolean.TRUE;            
            Customer customer = this.user.getCustomer();
            
            String hideAccommodations = customer.getHideAccommodations();
            if ((hideAccommodations != null) && hideAccommodations.equalsIgnoreCase("T"))
            {
                supportAccommodations = Boolean.FALSE;
            }
            getSession().setAttribute("supportAccommodations", supportAccommodations); 
            
            SortParams topNodesSort = FilterSortPageUtils.buildSortParams(FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN, ColumnSortEntry.ASCENDING, null, null);
            und = this.scheduleTest.getTopUserNodesForUser(this.userName, null, null, null, topNodesSort);
        }
        catch (CTBBusinessException e)
        {
            e.printStackTrace();
        }

        
        this.scheduler = this.user;
        getSession().setAttribute("scheduler", this.user);
        getSession().setAttribute("schedulerFirstLastName", this.user.getFirstName() + " " + this.user.getLastName());

            
        UserNode [] nodes = und.getUserNodes();        
        for (int i=0; i < nodes.length; i++)
        {
            UserNode node = (UserNode)nodes[i];
            if (node != null)
            {
            	this.topNodesMap.put(node.getOrgNodeId(), node.getOrgNodeName());
                if (i == 0) 
                    form.setCreatorOrgNodeId(node.getOrgNodeId());                
            }
            
        }
        
        this.scheduledSession = null;
               
        this.timeZoneList = DateUtils.getTimeZoneList();    
        this.timeList = DateUtils.getTimeList();  
        form.getTestAdmin().setTimeZone(DateUtils.getUITimeZone(this.user.getTimeZone()));
        
        this.selectedStudents = null;
        
        this.condition.setTestSessionExpired(Boolean.FALSE);
        
        this.testAdminId = null;
        
        this.selectedProctors = new ArrayList();
        this.user.setEditable("F"); //so that cannot remove user as a proctor
        this.selectedProctors.add(this.user);
        
        this.testProductData = null; //force re-fetch product list
        
        form.getTestAdmin().setSessionName(null);
        
        /* this block does nothing
        Date now = new Date(System.currentTimeMillis());
        if(this.user.getTimeZone() == null){
        	try
            {
        		User userNew = this.scheduleTest.getUserDetails(this.userName, this.userName);
        		this.user.setTimeZone(userNew.getTimeZone());
            }
            catch (CTBBusinessException e)
            {
                e.printStackTrace();
            }
        	
        }
        Date today = com.ctb.util.DateUtils.getAdjustedDate(now, TimeZone.getDefault().getID(), this.user.getTimeZone(), now);
        Date tomorrow = com.ctb.util.DateUtils.getAdjustedDate(new Date(now.getTime() + (24 * 60 * 60 * 1000)), TimeZone.getDefault().getID(), this.user.getTimeZone(), now);
        */
        
        if (this.hasReport != null)
        	this.getSession().setAttribute("userHasReports", new Boolean(this.hasReport));
        else
        	this.getSession().setAttribute("userHasReports", userHasReports());
        
        if (this.bulkAcc != null)
        	this.getSession().setAttribute("isBulkAccommodationConfigured", new Boolean(this.bulkAcc));        
        else
        	this.getSession().setAttribute("isBulkAccommodationConfigured", customerHasBulkAccommodation());        
        	
        this.testRosterFilter = new TestRosterFilter();

        this.addedStudentsCount = 0;
        this.addedProctorsCount = 0;
        
        this.orgNodePath = new ArrayList(); //clean up path
        
        this.currentSelectedTestId = "";
        
        this.showLevelOrGrade = "level";
        
        this.overrideFormAssignment = null;
    
        this.overrideLoginStartDate = null;
        
        this.condition = new Condition();
        
        this.stateScheduler = new StateScheduler();
        
        customerHasScoring();//For hand scoring changes for defect # 65988
        isTopLevelUser();
    }    


    /**
     * @jpf:action
     * @jpf:forward name="success" path="modifySubtests.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "modifySubtests.do")
    })
    protected Forward toModifySubtests(ScheduleTestForm form)
    {        
        this.selectedSubtests = TestSessionUtils.cloneSubtests(this.defaultSubtests); 
        
        this.allSubtests = TestSessionUtils.getAllSubtestsForTest(this.scheduleTest, this.userName, this.itemSetId);
        
        TestSessionUtils.extractLocatorSubtest(this.allSubtests);
        
        this.availableSubtests = TestSessionUtils.getAvailableSubtests(this.allSubtests, this.selectedSubtests);     
                   
        this.allSubtests = TestSessionUtils.sortSubtestList(this.allSubtests, this.selectedSubtests); 
        
        this.levelOptions = TestSessionUtils.getLevelOptions();
      
        form.setCurrentAction(ACTION_DEFAULT);
        
        return new Forward("success", form);
    }
    

    /**
     * @jpf:action
     * @jpf:forward name="error" path="modifySubtests.do"
     * @jpf:forward name="modifySubtests" path="modifySubtests.jsp"
     * @jpf:forward name="selectTest" path="selectTest.do"
     * @jpf:validation-error-forward name="failure" path="logout.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "error",
                     path = "modifySubtests.do"), 
        @Jpf.Forward(name = "modifySubtests",
                     path = "modifySubtests.jsp"), 
        @Jpf.Forward(name = "selectTest",
                     path = "selectTest.do")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "logout.do"))
    protected Forward modifySubtests(ScheduleTestForm form)
    {
        String currentAction = form.getCurrentAction();        

        if (currentAction.equals("cancelEditSubtests"))
        {
            form.setCurrentAction("");
            return new Forward("selectTest", form);            
        }
                
                
        String autoLocator = form.getAutoLocator();
        boolean autoLocatorChecked = ((autoLocator != null) && autoLocator.equals("true"));
        boolean validateLevels = !autoLocatorChecked;                                          
                
        if (currentAction.equals("doneEditSubtests") && this.getRequest().getParameter("index_1") != null)
        {
            form.setCurrentAction(ACTION_DEFAULT);           
            
            this.selectedSubtests = TestSessionUtils.retrieveSelectedSubtestsFromRequest(this.getRequest(), this.allSubtests);      
                    
            boolean valid = TABESubtestValidation.validation(this.selectedSubtests, validateLevels);
            
            String message = TABESubtestValidation.currentMessage;                
            form.setSubtestValidationMessage(null);
            
            if (! valid)
            {
                this.getRequest().setAttribute("errorMessage", message); 
                this.availableSubtests = TestSessionUtils.getAvailableSubtests(this.allSubtests, this.selectedSubtests);   
                this.allSubtests = TestSessionUtils.sortSubtestList(this.allSubtests, this.selectedSubtests);                              
                form.setCurrentAction(ACTION_DEFAULT);
                return new Forward("error", form);            
            }
            else
            {
                if (! message.equals(TABESubtestValidation.NO_ERROR_MSG))
                {
                    form.setSubtestValidationMessage(message);
                }
            }                

            this.defaultSubtests = TestSessionUtils.cloneSubtests(this.selectedSubtests);     
                               
            return new Forward("selectTest", form);                        
        }
        
                            
        this.getRequest().setAttribute("allSubtests", this.allSubtests);
        this.getRequest().setAttribute("availableSubtests", this.availableSubtests);
        this.getRequest().setAttribute("selectedSubtests", this.selectedSubtests);
        this.getRequest().setAttribute("levelOptions", this.levelOptions);
        
        this.getRequest().setAttribute("showLevel", new Boolean(validateLevels));

        return new Forward("modifySubtests", form);
    }

                
                

    /**
     * @jpf:action
     * @jpf:forward name="selectTest" path="selectTest.jsp"
     * @jpf:validation-error-forward name="failure" path="logout.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "selectTest",
                     path = "selectTest.jsp")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "logout.do"))
    protected Forward selectTest(ScheduleTestForm form)
    {
        boolean disableNextButton = false;
        boolean hideTestOptions = false;
        boolean hasMultipleSubtests = false;
        String actionElement = form.getActionElement();
        String currentAction = form.getCurrentAction();
      //change for performance tuning
        if(currentAction==null)
        {
        	currentAction="null";
        }
        
        try
        {
            if (this.testProductData == null)
            { // first time here 
                this.testProductData = this.getTestProductDataForUser();
                 tps = this.testProductData.getTestProducts();//changes for performance tuning
            }
        	           
                       
            
            if (form.getSelectedProductName() == null || form.getSelectedProductName().equals(""))
            {
                if (tps.length > 0 && tps[0] != null)
                {
                    String productName = tps[0].getProductName();
                    form.setSelectedProductName(productName);
                }
                else
                {
                    form.setSelectedProductName("");
                }
            }
            
            form.validateValues();
            //changes for performance tuning
            
            if(currentAction.equals("null")){
            	 selectedProductName = form.getSelectedProductName();
                 selectedLevel = form.getSelectedLevel();
            }
           if (!((currentAction.equals("selectTest"))||(currentAction.equals("null")))){
               selectedProductName = form.getSelectedProductName();
                selectedLevel = form.getSelectedLevel();
            }
    
            boolean newTestSelected = false;
            
            

            disableNextButton = false;
            hideTestOptions = false;
                          
            this.condition.setOffGradeTestingDisabled(Boolean.FALSE);
            
            if (actionElement != null && actionElement.equals("currentAction"))
            {
                if (currentAction.equals("selectTest"))
                {
                    if (!form.getSelectedTestId().equals(this.currentSelectedTestId))
                    {
                        newTestSelected = true;
                    }
                }
                else if (currentAction.equals("changeProduct"))
                {
                	gradeFlag = false; // Change for defects in performance tuning
                    selectedLevel = FilterSortPageUtils.FILTERTYPE_SHOWALL;
                    form.setSelectedLevel(selectedLevel);
                    form.getTestStatePathList().setPageRequested(new Integer(1));
                    //to hide to subtest options
                    form.setSelectedTestId(null);
                    this.currentSelectedTestId = null;
                    disableNextButton = true;
                    hideTestOptions = true;
					//Changes for random distractor special senario
                    form.setProductChanged(true);
                }
                else if (currentAction.equals("changeLevel"))
                {
                    form.setSelectedTestId(null);
                    this.currentSelectedTestId = null;
                    form.getTestStatePathList().setPageRequested(new Integer(1));
                }
                else if (currentAction.equals("toogleAutoLocator"))
                {
                    String autoLocator = form.getAutoLocator();
                    if ((autoLocator != null) && autoLocator.equals("true"))
                    {    
                        if (! this.blankInputString.equals(""))
                        {
                            this.locatorSubtest.setTestAccessCode(this.blankInputString);
                        }
                    }
                    if ((autoLocator != null) && autoLocator.equals("false"))
                    {  
                        this.blankInputString = "";  
                    }
                }
                
                //unset current action
                form.setCurrentAction("");
    
            }
            else if (actionElement != null && actionElement.equals("{actionForm.testStatePathList.pageRequested}"))
            {
                form.setSelectedTestId(this.currentSelectedTestId);
            }
            
            
            this.productNameList = createProductNameList(tps); 
            if (this.productNameList.size() <= 0)
            {
                throw new TestNotFoundException(MessageResourceBundle.getMessage("SelectTest.NoTest"));    
            }
            
            int selectedProductIndex = getProductListIndexByName(selectedProductName);
            
            // populate grade and level for selected product
            TestProduct prod = tps[selectedProductIndex];
            try {
				prod.setLevels(itemSet.getLevelsForProduct(prod.getProductId()));
	            prod.setGrades(itemSet.getGradesForProduct(prod.getProductId()));
	            
	            // get grades for LasLink
	            if (isLasLinkProduct(prod.getProductType())) {
		            prod.setGrades(itemSet.getLevelsForProduct(prod.getProductId()));
	            }
	            
			} catch (SQLException e) {
				e.printStackTrace();
			}
         //changes for performance tuning
            
            if(currentAction.equals("null")){
            	 this.levelList = createLevelList(tps[selectedProductIndex].getLevels());
                 this.gradeList = createGradeList(tps[selectedProductIndex].getGrades()); 
                 this.condition.setShowStudentFeedback(new Boolean(tps[selectedProductIndex].getShowStudentFeedback().equals("T")));
         
            }
           if (!((currentAction.equals("selectTest"))||(currentAction.equals("null")))){
        	   this.levelList = createLevelList(tps[selectedProductIndex].getLevels());
               this.gradeList = createGradeList(tps[selectedProductIndex].getGrades()); 
               this.condition.setShowStudentFeedback(new Boolean(tps[selectedProductIndex].getShowStudentFeedback().equals("T")));
       
            }
//            this.levelList = createLevelList(tps[selectedProductIndex].getLevels());
//            this.gradeList = createGradeList(tps[selectedProductIndex].getGrades()); 
//            this.condition.setShowStudentFeedback(new Boolean(tps[selectedProductIndex].getShowStudentFeedback().equals("T")));
//    
            if (this.scheduledSession != null)
            {
                TestSession testSession = this.scheduledSession.getTestSession();
                testSession.setShowStudentFeedback(this.condition.getShowStudentFeedback().booleanValue() ? "T" : "F");
            }
            
            String acknowledgmentsURL =  tps[selectedProductIndex].getAcknowledgmentsURL();
            if (acknowledgmentsURL != null)
            {
                acknowledgmentsURL = acknowledgmentsURL.trim();
                if (!"".equals(acknowledgmentsURL))
                    this.getRequest().setAttribute("acknowledgmentsURL", acknowledgmentsURL);
            }

            Integer productId = tps[selectedProductIndex].getProductId();
			
            this.productType = TestSessionUtils.getProductType(tps[selectedProductIndex].getProductType());
            
            //Changes for defect in performance tuning    
            if (this.levelList.size() > 0 && gradeFlag==false) {
                this.showLevelOrGrade = "level";
            }
            else if (this.gradeList.size() > 0)
            {
            	 //Changes for defect in performance tuning 
            	gradeFlag = true;
                this.showLevelOrGrade = "grade";
                this.levelList = this.gradeList;
            }
            else
                this.showLevelOrGrade = "none";
            
            if ("grade".equals(this.showLevelOrGrade) && "ItemSetLevel".equals(form.getTestStatePathList().getSortColumn()))
                form.getTestStatePathList().setSortColumn("Grade");
            else if ("level".equals(this.showLevelOrGrade) && "Grade".equals(form.getTestStatePathList().getSortColumn()))
                form.getTestStatePathList().setSortColumn("ItemSetLevel");    
            else if ("none".equals(this.showLevelOrGrade) && ("ItemSetLevel".equals(form.getTestStatePathList().getSortColumn()) || "Grade".equals(form.getTestStatePathList().getSortColumn())))
                form.getTestStatePathList().setSortColumn("ItemSetName");
    
    
            if (TestSessionUtils.isTabeProduct(this.productType).booleanValue())
            { 
                if (form.getTestStatePathList().getSortColumn().equals("Grade") || form.getTestStatePathList().getSortColumn().equals("ItemSetLevel"))
                {
                    form.getTestStatePathList().setSortColumn("ItemSetName");
                }
            }
            
                
            FilterParams testFilter = null;
            if (selectedLevel != null && !selectedLevel.equals(FilterSortPageUtils.FILTERTYPE_SHOWALL))
            {
                if (this.showLevelOrGrade.equals("level"))                
                    testFilter = FilterSortPageUtils.buildFilterParams("ItemSetLevel", selectedLevel);
                else if (this.showLevelOrGrade.equals("grade")) 
                    testFilter = FilterSortPageUtils.buildFilterParams("Grade", selectedLevel);
                
            }
            PageParams testPage = FilterSortPageUtils.buildPageParams(form.getTestStatePathList().getPageRequested(), FilterSortPageUtils.PAGESIZE_10);
            SortParams testSort = FilterSortPageUtils.buildSortParams(form.getTestStatePathList().getSortColumn(), form.getTestStatePathList().getSortOrderBy(), null, null);
                        
            TestElementData ted = this.getTestsForProductForUser(productId, testFilter, testPage, testSort);
                   
            int totalNumOfPages = ted.getTotalPages().intValue();                       
            //START- Added for Deferred Defect 59285
            form.testStatePathList.setMaxPageRequested(totalNumOfPages);
            //END- Added for Deferred Defect 59285
          //code for performance tuning
            if(currentAction.equals("null")){
            this.testList = buildTestList(ted);
            }
            
            if (!((currentAction.equals("selectTest"))||(currentAction.equals("null")))){
            	this.testList = buildTestList(ted);
         }
           
            if (form.getTestStatePathList().getPageRequested().intValue() > ted.getFilteredPages().intValue())
            {
                form.getTestStatePathList().setPageRequested(ted.getFilteredPages());
            }
            
    
            PagerSummary testPagerSummary = buildTestPagerSummary(ted, form.getTestStatePathList().getPageRequested());
            this.getRequest().setAttribute("testPagerSummary", testPagerSummary);
                      
                      
            if (newTestSelected)
            {
                Integer testId = new Integer(form.getSelectedTestId());
                if (isOffGradeTestingContainStudents(tps[selectedProductIndex], ted, testId))
                { 
                    form.setSelectedTestId(this.currentSelectedTestId);
                    newTestSelected = false;                    
                    this.getRequest().setAttribute("errorMessage", MessageResourceBundle.getMessage("Off_Grade_Testing_Error"));
                }
                else
                {
                    this.currentSelectedTestId = form.getSelectedTestId();
                }
            }
            
                
                      
            TestVO selectedTest = null;
            if (form.getSelectedTestId() != null && !form.getSelectedTestId().equals(""))
            {
                
                selectedTest = getTestById(form.getSelectedTestId());
                
                if (selectedTest != null)
                {
                    
                    this.itemSetId = selectedTest.getId();
                 
                    if (newTestSelected)
                    {
                       
                        this.sessionSubtests = selectedTest.getSubtests(); 
                                                
                        this.locatorSubtest = TestSessionUtils.getLocatorSubtest(this.sessionSubtests);   

                        if (TestSessionUtils.isTabeBatterySurveyProduct(this.productType).booleanValue())
                        {
                            this.defaultSubtests = TestSessionUtils.getDefaultSubtestsWithoutLocator(this.sessionSubtests); 
                        }
                        else
                        {
                            this.defaultSubtests = TestSessionUtils.cloneSubtests(this.sessionSubtests); 
                        }
                                                                        
                        form.getTestAdmin().setAccessCode(selectedTest.getAccessCode());

                        if (this.defaultSubtests.size() >= 1)
                        {
                            form.getTestAdmin().setAccessCode(((SubtestVO)this.defaultSubtests.get(0)).getTestAccessCode());
                        }
                        
                        if (this.locatorSubtest != null)
                        {
                            form.setAutoLocator("true");   
                        }
                    }

                    this.setFormList(selectedTest.getForms());
                    this.levelOptions = TestSessionUtils.getLevelOptions();

                    int numOfSubtests =0;
                    if (this.defaultSubtests != null) 
                        numOfSubtests = this.defaultSubtests.size();

                    hasMultipleSubtests = (numOfSubtests > 1);
                    
                    disableNextButton = false;
                    hideTestOptions = false;         
                    
                    this.condition.setOffGradeTestingDisabled(isOffGradeTestingDisabled(tps[selectedProductIndex], ted, selectedTest.getId(), form));
                               
                }
                else if (this.condition.getIsSearchTestList().booleanValue())
                {
                    boolean found = false;
                    for (int i=2; i <= totalNumOfPages && !found; i++)
                    {
                        form.getTestStatePathList().setPageRequested(new Integer(i));
                        testPage = FilterSortPageUtils.buildPageParams(form.getTestStatePathList().getPageRequested(), FilterSortPageUtils.PAGESIZE_10);
                        ted = this.getTestsForProductForUser(productId, testFilter, testPage, testSort);
                        this.testList = buildTestList(ted);
                        selectedTest = getTestById(form.getSelectedTestId());
                        if (selectedTest != null)
                            found = true;
                    }
                    this.condition.setIsSearchTestList(Boolean.FALSE);
                    //disableNextButton = false;
                   //Start Defect fixing 59285
                    if(!found) {
                    	disableNextButton = true;
                    } else {
                    	disableNextButton = false;
                    }
                  //End Defect fixing 59285
                    testPagerSummary = buildTestPagerSummary(ted, form.getTestStatePathList().getPageRequested());
                    this.getRequest().setAttribute("testPagerSummary", testPagerSummary);
                    
                }
                else
                {
                    disableNextButton = true;
                    hideTestOptions = true;                    
                }
            }
            else
            {
                disableNextButton = true;
                hideTestOptions = true;                    
            }
            
            if (selectedTest != null)
                this.getRequest().setAttribute("selectedTestName", selectedTest.getTestName());
    
            if ("changeHasBreakToYes".equals(currentAction) && this.testAdminId != null && this.selectedTestId.equals(form.getSelectedTestId()))
            {
                resetTACs(form.getSelectedTestId());                
            }
        
        ///////////////////
    
            boolean hideProductNameDropDown = this.productNameList.size() <= 1;
            this.getRequest().setAttribute("hideProductNameDropDown", new Boolean(hideProductNameDropDown));
            if (hideProductNameDropDown)
                form.setSelectedProductName((String)this.productNameList.get(0));
            
            boolean hideLevelDropDown = this.levelList.size() <= 1;
            this.getRequest().setAttribute("hideLevelDropDown", new Boolean(hideLevelDropDown));
            if (hideLevelDropDown)
            {
                if (this.levelList.size() > 0)
                {
                    form.setSelectedLevel((String)this.levelList.get(0));
                }
            }
                   
        }
        catch (TestNotFoundException e)
        {
            e.printStackTrace();
            this.getRequest().setAttribute("informationMessage", e.getMessage());
            disableNextButton = true;
        }
        catch (CTBBusinessException e)
        {
            e.printStackTrace();
            this.getRequest().setAttribute("errorMessage", e.getMessage());
            disableNextButton = true;
        }

        Boolean isTabeProduct = TestSessionUtils.isTabeProduct(this.productType);
        Boolean isTabeLocatorProduct = TestSessionUtils.isTabeLocatorProduct(this.productType);
        
        this.getRequest().setAttribute("productType", this.productType);

        this.getRequest().setAttribute("isTabeProduct", isTabeProduct);

        this.getRequest().setAttribute("isSelectTest", Boolean.TRUE);
        
        this.getRequest().setAttribute("hideTestOptions", new Boolean(hideTestOptions));
        
        this.getRequest().setAttribute("disableNextButton", new Boolean(disableNextButton));
        
        if (isTabeProduct.booleanValue() && (! isTabeLocatorProduct.booleanValue()))
        {
            hasMultipleSubtests = true;
        }
        
        this.getRequest().setAttribute("hasMultipleSubtests", new Boolean(hasMultipleSubtests));

        form.setActionElement(ACTION_DEFAULT); 
        isTopLevelUser(); //LLO- 118 - Change for Ematrix UI
        setFormInfoOnRequest(form);
        return new Forward("selectTest", form);
        
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
			e.printStackTrace();
		}
		getSession().setAttribute("isTopLevelUser",isLaslinkUserTopLevel);	
	}
    
    private void setFormInfoOnRequest(ScheduleTestForm form) {

        
    	this.getRequest().setAttribute("pageMessage", form.getMessage());
    	this.getRequest().setAttribute("scheduledTestSessionData", form.getTestAdmin());
    	this.getRequest().setAttribute("hasBreak", form.getHasBreak());  //Changes for defect 60393 
    	this.getRequest().setAttribute("formOperand", form.getFormOperand());  //Changes for defect 61543
    	this.getRequest().setAttribute("autoLocatorDisplay",form.getAutoLocatorDisplay());
    	this.getRequest().setAttribute("autoLocator",form.getAutoLocator());
    	this.getRequest().setAttribute("testRosterFilter",form.getTestRosterFilter());
    	this.getRequest().setAttribute("subtestValidationMessage",form.getSubtestValidationMessage());
    	this.getRequest().setAttribute("formData",form);
        
    }
    

    private Boolean isOffGradeTestingDisabled(TestProduct tp, TestElementData ted, Integer testId, ScheduleTestForm form) 
    {        
        if (! tp.getOffGradeTestingDisabled().equals("T"))
        {
            return Boolean.FALSE;
        }

        String[] grades = tp.getGrades();
        if (grades != null)
        {
            for (int i=0; i < grades.length; i++)
            {
                if (grades[i] != null)
                {
                    TestElement[] tes = ted.getTestElements();
                    for (int j=0; j < tes.length && tes[j] != null; j++)
                    {
                        TestElement te = tes[j];
                        if (te.getItemSetId().intValue() == testId.intValue())
                        {
                        	if (isLasLinkProduct(tp.getProductType())) {
	                            if (te.getItemSetLevel() != null) {
	                                form.setSelectedGrade(te.getItemSetLevel());
	                                return Boolean.TRUE;                                
	                            }
                        	}
                        	else {
	                            if (te.getGrade() != null) {
	                                form.setSelectedGrade(te.getGrade());
	                                return Boolean.TRUE;                                
	                            }
                        	}
                        }
                    }
                }
            }                        
        }        
                
        return Boolean.FALSE;
    }
    

    private boolean isLasLinkProduct(String productType) {
    	return "LL".equals(productType);
    }
    
    private boolean isOffGradeTestingContainStudents(TestProduct tp, TestElementData ted, Integer testId) 
    {
        if (tp.getOffGradeTestingDisabled().equals("T") && (this.selectedStudents != null) && (this.selectedStudents.size() > 0))
        {
            String[] grades = tp.getGrades();
            if (grades != null)
            {
                for (int i=0; i < grades.length; i++)
                {
                    if (grades[i] != null)
                    {
                        TestElement[] tes = ted.getTestElements();
                        for (int j=0; j < tes.length && tes[j] != null; j++)
                        {
                            TestElement te = tes[j];
                            if (te.getItemSetId().intValue() == testId.intValue())
                            {
                                if (te.getGrade() != null)
                                {
                                    return true;                                
                                }
                            }
                        }
                    }
                }            
            }
        }                
        return false;            
    }
        
    /**
     * @jpf:action
     * @jpf:forward name="showRestrictedStudents" path="showRestrictedStudents.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "showRestrictedStudents",
                     path = "showRestrictedStudents.jsp")
    })
    protected Forward showRestrictedStudents(ScheduleTestForm form)
    {        
        form.validateValues();
        
        if (this.restrictedSessionStudents != null)
        {
            SessionStudentData ssd = new SessionStudentData();
            ssd.setSessionStudents(this.restrictedSessionStudents, new Integer(FilterSortPageUtils.PAGESIZE_10));

            form.getStudentStatePathList().setMaxPageRequested(ssd.getFilteredPages());
            
            String sortColumn = form.getStudentStatePathList().getSortColumn();
            if (!sortColumn.equals("LastName") && !sortColumn.equals("FirstName") && !sortColumn.equals("MiddleName") && !sortColumn.equals("Grade"))
                form.getStudentStatePathList().setSortColumn("LastName");
                
            String actionElement = form.getActionElement();
            form.resetValuesForAction(actionElement);
            
            FilterParams filter = null;
            PageParams page = FilterSortPageUtils.buildPageParams(form.getStudentStatePathList().getPageRequested(), FilterSortPageUtils.PAGESIZE_10);
            SortParams sort = FilterSortPageUtils.buildOrgNameLastNameFirstNameSortParams(form.getStudentStatePathList().getSortColumn(), form.getStudentStatePathList().getSortOrderBy());
            
            try
            {
                if (filter != null)
                    ssd.applyFiltering(filter);
                if (sort != null)
                    ssd.applySorting(sort);
                if (page != null)
                    ssd.applyPaging(page);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    
            List studentNodes = buildStudentList(ssd);
            
            int totalCount = 0;
            if (this.selectedStudents != null)
                totalCount = this.selectedStudents.size();

            PagerSummary studentPagerSummary = buildRestrictedStudentPagerSummary(ssd, form.getStudentStatePathList().getPageRequested());
            this.getRequest().setAttribute("studentNodes", studentNodes);
            this.getRequest().setAttribute("studentPagerSummary", studentPagerSummary);
            this.getRequest().setAttribute("totalCount", "" +
                                                         totalCount);
            this.getRequest().setAttribute("restrictedCount", "" +
                                                              this.restrictedSessionStudents.length);
        }
        
        return new Forward("showRestrictedStudents", form);
    }
    

    /**
     * @jpf:action
     * @jpf:forward name="success" path="selectTestDone.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "selectTestDone.do")
    })
    protected Forward removeRestrictedStudents(ScheduleTestForm form)
    {
        HashMap hashMap = new HashMap();
        for (int i=0; i < this.restrictedSessionStudents.length; i++)
            hashMap.put(this.restrictedSessionStudents[i].getStudentId(), this.restrictedSessionStudents[i].getStudentId());

        Iterator it = this.selectedStudents.iterator();
        while (it.hasNext())
        {
            SessionStudent ss = (SessionStudent)it.next();
            if (hashMap.containsKey(ss.getStudentId()))
            {
                it.remove();
            }
        }          
        return new Forward("success", form);
    }


    /**
     * @jpf:action
     * @jpf:forward name="selectSettings" path="selectSettings.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "selectSettings",
                     path = "selectSettings.do")
    })
    public Forward backToSelectSettings(ScheduleTestController.ScheduleTestForm form)
    {
        form.setActionElement("backToSelectSettings");   
        form.resetStudentSortPage();
        
        TestRosterFilter trf = form.getTestRosterFilter();
        trf.copyValues(this.testRosterFilter);
             
        return new Forward("selectSettings", form);
    }
    

    /**
     * @jpf:action
     * @jpf:forward name="selectSettings" path="selectSettings.jsp"
     * @jpf:validation-error-forward name="failure" path="logout.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "selectSettings",
                     path = "selectSettings.jsp")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "logout.do"))
    protected Forward selectSettings(ScheduleTestForm form)
    {
    	String actionElement = form.getActionElement();
        form.resetValuesForAction(actionElement);
        FormFieldValidator.validateFilterForm(form, getRequest());

        String queryStr = this.getRequest().getQueryString();
        String integrationAction="";
       
        if (queryStr != null && !queryStr.equals(""))
        {
            int actionStartIndex = queryStr.indexOf("action=");
            int testAdminIdIndex = queryStr.indexOf("testAdminId=");
            int ampIndex;
            
            String testAdminIdValue;
            ampIndex = queryStr.indexOf("&", actionStartIndex);
            if (ampIndex < 0) 
                ampIndex = queryStr.length();
            integrationAction = JavaScriptSanitizer.sanitizeString(queryStr.substring(actionStartIndex + "action=".length(), ampIndex));
            
            ampIndex = queryStr.indexOf("&", testAdminIdIndex);
            if (ampIndex < 0) 
                ampIndex = queryStr.length();
            testAdminIdValue = JavaScriptSanitizer.sanitizeString(queryStr.substring(testAdminIdIndex + "testAdminId=".length(), ampIndex));

            // init since this is the first time to the page flow                
            init(form);
            // START- Added for defect #65862
            if( this.showLevelOrGrade.equals("level")){
            	gradeFlag = false;
            }
            // END- Added for defect #65862
            form.getStudentStatePathList().setSortColumn(FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN); // special for kitchen sink
            
            if (integrationAction.equals(ACTION_SCHEDULE_TEST) || integrationAction.equals(ACTION_VIEW_TEST) || integrationAction.equals(ACTION_EDIT_TEST)) 
                this.action = integrationAction;
            else if (integrationAction.equals(ACTION_COPY_TEST))
            {
                this.action = ACTION_SCHEDULE_TEST;
                this.getRequest().setAttribute("copyTest", "true"); 
                this.condition.setIsCopyTest(Boolean.TRUE);                    
            }
            else
                getRequest().setAttribute("errorMessage","Unknow action in URL=" +
                                                         integrationAction);
                
            this.testAdminId = new Integer(testAdminIdValue);                    
            this.condition.setReloadTestSession(Boolean.TRUE);
            this.locatorSubtest = null;
        }
                        
        form.validateValues();
        form.setAction(this.action);
        this.condition.setShowCancelOnFirstPage(Boolean.TRUE);

        handleAction(form);
        
        boolean isSessionCopyable = false;
      
        if (this.testAdminId != null && this.condition.getReloadTestSession().booleanValue())
        {
            try
            {
                this.scheduledSession = this.scheduleTest.getScheduledSession(this.userName, this.testAdminId);
                TestSession testSession = this.scheduledSession.getTestSession();
                this.itemSetId = testSession.getItemSetId();
                
                this.condition.setShowStudentFeedback(new Boolean(testSession.getShowStudentFeedback().equalsIgnoreCase("T")));
                
                String schedulerName = testSession.getCreatedBy();
                this.scheduler = this.scheduleTest.getUserDetails(this.userName, schedulerName);
                if (integrationAction.equals(ACTION_COPY_TEST))
                {
                    this.originalScheduler = this.scheduler;
                    this.scheduler = this.user;
                }
                getSession().setAttribute("scheduler", this.scheduler);
                getSession().setAttribute("schedulerFirstLastName", this.scheduler.getFirstName() + " " + this.scheduler.getLastName());

                if (!this.user.getUserId().equals(this.scheduler.getUserId()))
                {
                    this.topNodesMap = new LinkedHashMap();
                    SortParams topNodesSort = FilterSortPageUtils.buildSortParams(FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN, ColumnSortEntry.ASCENDING, null, null);
                    UserNodeData und = this.scheduleTest.getTopUserNodesForUser(this.scheduler.getUserName(), null, null, null, topNodesSort);
                    UserNode [] nodes = und.getUserNodes();        
                    for (int i=0; i < nodes.length; i++)
                    {
                        UserNode node = (UserNode)nodes[i];
                        if (node != null)
                        {
                        	this.topNodesMap.put(node.getOrgNodeId(), node.getOrgNodeName());
                            if (i == 0) 
                                form.setCreatorOrgNodeId(node.getOrgNodeId());             
                        }
                    }
                }        
    
                int studentsLoggedIn = this.scheduledSession.getStudentsLoggedIn() == null ? 0 : this.scheduledSession.getStudentsLoggedIn().intValue();
                if (!integrationAction.equals(ACTION_COPY_TEST) && studentsLoggedIn > 0)
                    this.condition.setHasStudentLoggedIn(Boolean.TRUE);
                else
                    this.condition.setHasStudentLoggedIn(Boolean.FALSE);
                    
                this.overrideFormAssignment = testSession.getOverrideFormAssignmentMethod();
                this.overrideLoginStartDate = testSession.getOverrideLoginStartDate();
                                         
                Integer productId = testSession.getProductId();  
                
                //set productId in form
                
                if (form.getTestAdmin() != null)
                {
                
                    form.getTestAdmin().setProductId(productId);
                    
                }
                this.testSessionId = testSession.getSessionNumber();
                
                if (this.testProductData == null)
                { // first time here 
                    this.testProductData = this.getTestProductDataForUser();
                }

                tps = this.testProductData.getTestProducts(); //Change for  change button defect appeared during performance tuning
                
                if (this.productNameList == null)
                    this.productNameList = createProductNameList(tps); 
    
                boolean foundProduct = false;
                String productName = null;
                for (int i=0; i < tps.length && !foundProduct; i++)
                {
                    if (productId.equals(tps[i].getProductId()))
                    {
                        productName = tps[i].getProductName();
                        //for license Management
                   
                        // populate grade and level for selected product
                        TestProduct prod = tps[i];
                        try {
            				prod.setLevels(itemSet.getLevelsForProduct(prod.getProductId()));
            	            prod.setGrades(itemSet.getGradesForProduct(prod.getProductId()));
            			} catch (SQLException e) {
            				e.printStackTrace();
            			}
                        
                        this.levelList = createLevelList(tps[i].getLevels());
                        this.gradeList = createGradeList(tps[i].getGrades());  
             
                        //Changes for defect in performance tuning
                        if (this.levelList.size() > 0 && gradeFlag==false) 
                            this.showLevelOrGrade = "level";
                        else if (this.gradeList.size() > 0)
                        {
                        	gradeFlag=true;
                            this.showLevelOrGrade = "grade";
                            this.levelList = this.gradeList;
                        }
                        else
                            this.showLevelOrGrade = "none";
                        foundProduct = true;
                    } 
                }
                         
                TestProduct tp = this.testSessionStatus.getProductForTestAdmin(this.userName, this.testAdminId);
                this.productType = TestSessionUtils.getProductType(tp.getProductType());
                          
                form.setSelectedProductName(productName);
                this.setSelectedProductName(productName);
                form.setSelectedTestId(this.itemSetId.toString());            
                this.setSelectedTestId(this.itemSetId.toString());
                
                
                
                TestElement [] testElements;
                if (integrationAction.equals(ACTION_COPY_TEST))
                {
                    TestElementData suTed = this.scheduleTest.getSchedulableUnitsForTest(this.userName, this.itemSetId, new Boolean(true), null, null, null);
                    testElements = suTed.getTestElements();
                }
                else
                {          
                    testElements = this.scheduledSession.getScheduledUnits();
                }
                
                form.getTestAdmin().setTestName(testSession.getTestName());
                if (testElements.length > 0 && testElements[0] != null)
                {
                    if (this.showLevelOrGrade.equals("grade"))
                        form.getTestAdmin().setLevel(testElements[0].getGrade());
                    else if (this.showLevelOrGrade.equals("level"))
                        form.getTestAdmin().setLevel(testElements[0].getItemSetLevel());
                    else
                        form.getTestAdmin().setLevel("");
                    form.getTestAdmin().setAccessCode(testElements[0].getAccessCode());
                    this.setFormList(testElements[0].getForms());
                }
                else
                {
                    form.getTestAdmin().setLevel("");
                    form.getTestAdmin().setAccessCode("");
                    this.setFormList(new String[0]);
                }
                form.getTestAdmin().setSessionName(testSession.getTestAdminName());
                
                String defaultTimeZone = TimeZone.getDefault().getID();
                String timeZone = testSession.getTimeZone();
                form.setStartDate(DateUtils.formatDateToDateString(testSession.getLoginStartDate()));
                form.setEndDate(DateUtils.formatDateToDateString(testSession.getLoginEndDate()));

                if (integrationAction.equals(ACTION_COPY_TEST))
                { // for copy test
                    Date now = new Date(System.currentTimeMillis());
                    Date today = com.ctb.util.DateUtils.getAdjustedDate(now, TimeZone.getDefault().getID(), timeZone, now);
                    Date tomorrow = com.ctb.util.DateUtils.getAdjustedDate(new Date(now.getTime() + (24 * 60 * 60 * 1000)), TimeZone.getDefault().getID(), timeZone, now);
                    form.setStartDate(DateUtils.formatDateToDateString(today));
                    form.setEndDate(DateUtils.formatDateToDateString(tomorrow));
                    
                    if (this.overrideLoginStartDate != null && !DateUtils.isBeforeToday(this.overrideLoginStartDate, this.user.getTimeZone()))
                    {
                        Date loginEndDate = (Date)this.overrideLoginStartDate.clone();
                        loginEndDate.setDate(loginEndDate.getDate() + 1);
                        form.setStartDate(DateUtils.formatDateToDateString(this.overrideLoginStartDate));
                        form.setEndDate(DateUtils.formatDateToDateString(loginEndDate));
                    }
                    
                }

                
                if (!integrationAction.equals(ACTION_COPY_TEST) && testSession.getTestAdminStatus().equals("PA"))
                    this.condition.setTestSessionExpired(Boolean.TRUE);
                else
                    this.condition.setTestSessionExpired(Boolean.FALSE);
                
                                
                form.setStartTime(DateUtils.formatDateToTimeString(testSession.getDailyLoginStartTime()));
                
                Date loginEndTime = testSession.getDailyLoginEndTime();
                if (!this.action.equals(this.ACTION_VIEW_TEST) && !this.condition.getTestSessionExpired().booleanValue())
                {
                    int minutes = loginEndTime.getMinutes();
                    minutes = (minutes + 14) / 15 * 15;
                    loginEndTime.setMinutes(minutes);
                }
                form.setEndTime(DateUtils.formatDateToTimeString(loginEndTime));
                if (integrationAction.equals(ACTION_COPY_TEST))
                    form.getTestAdmin().setTimeZone(DateUtils.getUITimeZone(this.user.getTimeZone()));
                else
                    form.getTestAdmin().setTimeZone(DateUtils.getUITimeZone(timeZone));
                
                form.getTestAdmin().setLocation(testSession.getLocation());
                form.setFormOperand(testSession.getFormAssignmentMethod());
                if (integrationAction.equals(ACTION_COPY_TEST))
                    form.setFormOperand(TestSession.FormAssignment.ROUND_ROBIN);
                else
                    form.setFormOperand(testSession.getFormAssignmentMethod());
                    
                if (this.overrideFormAssignment != null)
                {
                    form.setFormOperand(this.overrideFormAssignment);
                }    

                form.setFormAssigned(testSession.getPreferredForm());
                
   
                 //set distractor allows in test session
                   
                if (form.getTestAdmin().getIsRandomize() == null)
                {
                    
                    form.getTestAdmin().setIsRandomize( testSession.getIsRandomize());
                       
                }
                        

                if (!integrationAction.equals(ACTION_COPY_TEST))
                {
                    if (this.action.equals(this.ACTION_VIEW_TEST))
                    {
                        form.setCreatorOrgNodeId(testSession.getCreatorOrgNodeId());
                        form.setCreatorOrgNodeName(testSession.getCreatorOrgNodeName());
                        //to display value of isRandomize on View Test session Information page
                         
                        if (testSession.getIsRandomize() != null && testSession.getIsRandomize() != "false")
                        {

                            form.getTestAdmin().setIsRandomize(testSession.getIsRandomize());

                        }
						
                    }
                    else
                    {
                        if (this.topNodesMap.containsKey(testSession.getCreatorOrgNodeId()))
                        {
                            form.setCreatorOrgNodeId(testSession.getCreatorOrgNodeId());
                            form.setCreatorOrgNodeName(testSession.getCreatorOrgNodeName());
                        }
                    }
                }
                                
                form.setHasBreak(testSession.getEnforceBreak().equals("T") ? Boolean.TRUE : Boolean.FALSE);
          
                this.sessionSubtests = new ArrayList();                
                for (int i=0; i < testElements.length; i++)
                {
                    int durationMinutes = testElements[i].getTimeLimit().intValue() / 60;
                    String duration = (durationMinutes == 0) ? "Untimed" : durationMinutes + " minutes";
                    SubtestVO subtestVO = new SubtestVO(testElements[i].getItemSetId(), String.valueOf(i + 1), testElements[i].getItemSetName(), duration, testElements[i].getAccessCode(), testElements[i].getSessionDefault(), testElements[i].getItemSetForm(), true);
                    this.sessionSubtests.add(subtestVO);
                }
                
                
                this.locatorSubtest = TestSessionUtils.getLocatorSubtest(this.sessionSubtests);   
                
                if (TestSessionUtils.isTabeBatterySurveyProduct(this.productType).booleanValue())
                {
                    this.defaultSubtests = TestSessionUtils.getDefaultSubtestsWithoutLocator(this.sessionSubtests); 
                }
                else
                {
                    this.defaultSubtests = TestSessionUtils.cloneSubtests(this.sessionSubtests); 
                }
                
                if (this.locatorSubtest == null)
                {                   
                    this.locatorSubtest = TestSessionUtils.getLocatorSubtest(this.scheduleTest, this.userName, this.itemSetId); 
                    form.setAutoLocator(null);                     
                }
                else
                {
                    if (this.locatorSubtest.getSessionDefault().equals("T"))
                        form.setAutoLocator("true");                     
                    else
                        form.setAutoLocator(null);                     
                }
                                                
                int nonEitableStudentCount = 0;                
                SessionStudent [] sessionStudents = this.scheduledSession.getStudents();
                if (this.condition.getHasStudentLoggedIn().booleanValue() && sessionStudents.length == studentsLoggedIn) 
                    this.condition.setAllStudentLoggedIn(Boolean.TRUE);
                    
                this.selectedStudents = new ArrayList();
                for (int i=0; i < sessionStudents.length; i++)
                {
                    if ("F".equals(sessionStudents[i].getStatus().getEditable()))
                        nonEitableStudentCount++;
                        
                    if (integrationAction.equals(ACTION_COPY_TEST))
                    {
                        if ("T".equals(sessionStudents[i].getStatus().getCopyable()))
                        {
                            sessionStudents[i].getStatus().setEditable("T");
                            //copy when override form assignment is manual
                            if (!TestSession.FormAssignment.MANUAL.equals(this.overrideFormAssignment))
                                sessionStudents[i].setItemSetForm(null);
                            this.selectedStudents.add(sessionStudents[i]);
                        }
                    }
                    else
                        this.selectedStudents.add(sessionStudents[i]);
                }

                boolean foundNonCopyableProctor = false;
                
                User [] proctors = this.scheduledSession.getProctors();
                this.selectedProctors = new ArrayList();
                boolean foundScheduler = false;
                for (int i=0; i < proctors.length; i++)
                {
                    if ("F".equals(proctors[i].getCopyable()))
                        foundNonCopyableProctor = true;

                    if (integrationAction.equals(ACTION_COPY_TEST))
                    {
                        if ("T".equals(proctors[i].getCopyable()))
                        {
/*                            
                            if (this.originalScheduler.getUserId().equals(proctors[i].getUserId())
                             && !this.scheduler.getUserId().equals(this.originalScheduler.getUserId())) {
                                // do not add original scheduler if he's not current scheduler
                             }
                             else {
*/                                
                            proctors[i].setEditable("T");
                            this.selectedProctors.add(proctors[i]);
                            if (this.scheduler.getUserId().equals(proctors[i].getUserId()))
                                foundScheduler = true;
//                             }
                        }
                    }
                    else                        
                        this.selectedProctors.add(proctors[i]);
                }
                
                if (integrationAction.equals(ACTION_COPY_TEST) && !foundScheduler)
                    this.selectedProctors.add(this.scheduler);
                

                if (this.action.equals(this.ACTION_EDIT_TEST))
                {
                    boolean isProctorRole = this.getRequest().isUserInRole("Proctor");
                    boolean noPermission = isProctorRole || (!this.user.getUserId().equals(this.scheduler.getUserId()) && this.topNodesMap.size() > 1) || foundNonCopyableProctor || (nonEitableStudentCount > studentsLoggedIn);
                    StringBuffer msgBuf = new StringBuffer();
                    //added for GACRCT2010CR006-OAS Export Automate
                    this.isTestSessionDataExported  =  testSession.getIsTestSessionDataExported().equals("T")? true : false;
                    if (this.condition.getHasStudentLoggedIn().booleanValue() || this.condition.getTestSessionExpired().booleanValue() || noPermission)
                    {
                        String previousInfoMsg = (String)this.getRequest().getAttribute("informationMessage");

                        if (previousInfoMsg != null && previousInfoMsg.length() > 0)
                            msgBuf.append(previousInfoMsg).append("<br/>");
                        
                        msgBuf.append(MessageResourceBundle.getMessage("SelectSettings.CertainFieldsUnavailable.Header"));  
                        //changes for GACRCT2010CR006-OAS Export Automate
                        if (this.condition.getTestSessionExpired().booleanValue() && ! this.isTestSessionDataExported)
                        {
                            msgBuf.append(MessageResourceBundle.getMessage("SelectSettings.CertainFieldsUnavailable.TestSessionEnded"));
                        }
                        else if (this.condition.getTestSessionExpired().booleanValue() && this.isTestSessionDataExported)
                        {
                            msgBuf.append(MessageResourceBundle.getMessage("SelectSettings.CertainFieldsUnavailable.TestSessionExported"));
                        }
                        else if (this.condition.getHasStudentLoggedIn().booleanValue())
                        {
                            msgBuf.append(MessageResourceBundle.getMessage("SelectSettings.CertainFieldsUnavailable.StudentsLoggedIn"));
                        }
                        if (noPermission)
                        {
                            msgBuf.append(MessageResourceBundle.getMessage("SelectSettings.CertainFieldsUnavailable.NoPermission"));
                        }
                    }
                    if (msgBuf.length() > 0)
                        this.getRequest().setAttribute("informationMessage", msgBuf.toString());
					
                    if (this.condition.getTestSessionExpired().booleanValue() && this.isTestSessionDataExported){
                    	this.getSession().setAttribute("isTestSessionDataExported",true);
                    }
                    else{
                    	this.getSession().setAttribute("isTestSessionDataExported",false);
                    }
                }

                TestProduct testProduct = getTestProduct(tps, productId);
                TestElementData ted = this.getTestsForProductForUser(productId, null, null, null);                
                this.condition.setOffGradeTestingDisabled(isOffGradeTestingDisabled(testProduct, ted, this.itemSetId, form));
                
                this.condition.setReloadTestSession(Boolean.FALSE);
            
            }
            catch (CTBBusinessException e)
            {
                e.printStackTrace(); 
                this.getRequest().setAttribute("errorMessage",MessageResourceBundle.getMessage("SelectSettings.FailedToLoadTestSession", e.getMessage()));  
                return new Forward("selectSettings", form);
            }
            
        }
        else
        { // when schedule/edit test
        
            
            TestVO selectedTest = getTestById(this.getSelectedTestId());
            if (form.getTestAdmin().getSessionName() == null || form.getTestAdmin().getSessionName().trim().equals(""))
            {
                if (selectedTest != null)
                {
                    form.getTestAdmin().setSessionName(selectedTest.getTestName());
                }
            }
            if (selectedTest != null)
            {
                this.setFormList(selectedTest.getForms());
                if (form.getFormAssigned() == null || form.getFormAssigned().equals(""))
                {
                    if (this.formList != null && this.formList.length > 0)
                        form.setFormAssigned(this.formList[0]);
                }
            }
            
            if (this.formList!= null && this.formList.length <= 1)
                form.setFormOperand(TestSession.FormAssignment.ROUND_ROBIN);
                
            if (this.overrideFormAssignment != null)
            {
                form.setFormOperand(this.overrideFormAssignment);
            }    
                
            if (this.condition.getSelectTestChanged().booleanValue())
            {
                if (form.getFormOperand() == null)
                    form.setFormOperand(TestSession.FormAssignment.ROUND_ROBIN);
                String defaultForm = "";
                if (this.formList!= null && this.formList.length >= 1)
                    defaultForm = this.formList[0];
                form.setFormAssigned(defaultForm); 
                
                String studentForm = null;
                if (TestSession.FormAssignment.MANUAL.equals(form.getFormOperand()))
                    studentForm = defaultForm;
                
                if (this.selectedStudents != null)
                {
                    Iterator it = this.selectedStudents.iterator();
                    while (it.hasNext())
                    {
                        SessionStudent ss = (SessionStudent)it.next();
                        if (ss != null)
                            ss.setItemSetForm(studentForm);
                    }
                }             
                this.condition.setSelectTestChanged(Boolean.FALSE);    
            }   
            
                        
            if (form.getTestAdmin().getTimeZone() == null || form.getTestAdmin().getTimeZone().trim().equals(""))
            {
                form.getTestAdmin().setTimeZone(DateUtils.getUITimeZone(this.user.getTimeZone()));
            }
            
        }
        
        int numOfSubtests =0;
        if (this.defaultSubtests != null) 
            numOfSubtests = this.defaultSubtests.size();
            
        boolean hasMultipleSubtests = false;
        if (numOfSubtests > 1)
            hasMultipleSubtests = true;
        this.getRequest().setAttribute("hasMultipleSubtests", new Boolean(hasMultipleSubtests));
        
        // insert Show All to the beginning of form options in filter
        if (this.formList != null)
        {
            this.formOptions = new String[this.formList.length + 1];
            this.formOptions[0] = FilterSortPageUtils.FILTERTYPE_SHOWALL;
            for (int i=0; i < this.formList.length; i++)
            {
                this.formOptions[i + 1] = this.formList[i];
            }
        }
        else
        {
            this.formOptions = new String[1];
            this.formOptions[0] = FilterSortPageUtils.FILTERTYPE_SHOWALL;
        } 
    
        String formOperand = form.getFormOperand();
        if (formOperand.equals(TestSession.FormAssignment.ROUND_ROBIN))
        {
            this.getRequest().setAttribute("displayFormList", "false");     
            this.getRequest().setAttribute("isFormEditable", "false");     
        }
        else if (formOperand.equals(TestSession.FormAssignment.ALL_SAME))
        {
            this.getRequest().setAttribute("displayFormList", "true");     
            this.getRequest().setAttribute("isFormEditable", "false");     
        }
        else if (formOperand.equals(TestSession.FormAssignment.MANUAL))
        {
            this.getRequest().setAttribute("displayFormList", "false");
            if (this.formList != null && this.formList.length > 1 && !this.condition.getTestSessionExpired().booleanValue())    
                this.getRequest().setAttribute("isFormEditable", "true");     
            else
                this.getRequest().setAttribute("isFormEditable", "false");     
            
        }
        
        if (this.action.equals(ACTION_VIEW_TEST))
        {
            this.getRequest().setAttribute("isFormEditable", "false");     
        }
        
        String showFormAssignment = "hidden";
        if (this.formList != null && this.formList.length > 1)
        {
            if (this.condition.getTestSessionExpired().booleanValue())
                showFormAssignment = "noneditable";
            else
                showFormAssignment = "editable";
            
            if (this.overrideFormAssignment != null)
            {
                showFormAssignment = "noneditable";
                if (TestSession.FormAssignment.ALL_SAME.equals(this.overrideFormAssignment))
                    showFormAssignment = "noneditable_samesame";
            }

            if (this.action.equals(ACTION_VIEW_TEST))
                showFormAssignment = "noneditable";
                                
        }
        else
            showFormAssignment = "hidden";
             
        this.getRequest().setAttribute("showFormAssignment", showFormAssignment);     

        String showSelectOrganization = "hidden";
        if (this.action.equals(ACTION_SCHEDULE_TEST))
        { 
            if (this.topNodesMap.size() > 1)
                showSelectOrganization = "editable"; 
            else
                showSelectOrganization = "hidden"; 
        }
        else if (this.action.equals(ACTION_EDIT_TEST))
        {
            if (this.user.getUserId().equals(this.scheduler.getUserId()))
            {
                if (this.topNodesMap.size() > 1)
                {
                    if (this.condition.getTestSessionExpired().booleanValue())
                        showSelectOrganization = "noneditable";
                    else
                        showSelectOrganization = "editable";
                } 
                else
                    showSelectOrganization = "hidden"; 
            } 
            else
            {
                if (this.topNodesMap.size() > 1)
                    showSelectOrganization = "noneditable"; 
                else
                    showSelectOrganization = "hidden"; 
            }  
        }
        else
            showSelectOrganization = "hidden"; 

        this.getRequest().setAttribute("showSelectOrganization", showSelectOrganization); 
            
        
        //////////////////////////////////////////////
        ////Students test roster
        ///////////////////////////////////////////////
        if (this.selectedStudents != null && this.selectedStudents.size() > 0)
        {
            SessionStudentData ssd = new SessionStudentData();
            SessionStudent [] sessionStudents = new SessionStudent[this.selectedStudents.size()];
            Iterator it = this.selectedStudents.iterator();
            int studentWithAccommodationsCount=0;
            int i=0;
            while (it.hasNext())
            {
                SessionStudent ss = (SessionStudent)it.next();
                if (ss.getMiddleName() != null && ss.getMiddleName().length() > 0)
                    ss.setMiddleName(ss.getMiddleName().substring(0, 1));                
                if (ss.getHasAccommodations().equalsIgnoreCase("true"))
                    studentWithAccommodationsCount++;
/*
                if (TestSession.FormAssignment.ALL_SAME.equals(form.getFormOperand()) 
                    && (ss.getItemSetForm()==null || ss.getItemSetForm().trim().equals("")))  
                    ss.setItemSetForm(form.getFormAssigned());
*/
                sessionStudents[i++] = ss;
            }
            ssd.setSessionStudents(sessionStudents, new Integer(FilterSortPageUtils.PAGESIZE_10));
            form.getStudentStatePathList().setMaxPageRequested(ssd.getFilteredPages());

            FilterParams filter = FilterSortPageUtils.buildTestRosterFilterParams(this.testRosterFilter);
            PageParams page = FilterSortPageUtils.buildPageParams(form.getStudentStatePathList().getPageRequested(), FilterSortPageUtils.PAGESIZE_10);
            SortParams sort = FilterSortPageUtils.buildOrgNameLastNameFirstNameSortParams(form.getStudentStatePathList().getSortColumn(), form.getStudentStatePathList().getSortOrderBy());
            
            try
            {
                if (filter != null)
                    ssd.applyFiltering(filter);
                if (sort != null)
                    ssd.applySorting(sort);
                if (page != null)
                    ssd.applyPaging(page);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    
            this.studentNodes = buildStudentList(ssd);
            String orgCategoryName = getSessionStudentOrgCategoryName(this.studentNodes);

            PagerSummary studentPagerSummary = buildStudentPagerSummary(ssd, form.getStudentStatePathList().getPageRequested());
            this.getRequest().setAttribute("studentPagerSummary", studentPagerSummary);
            this.getRequest().setAttribute("studentCount", new Integer(this.selectedStudents.size()));
            this.getRequest().setAttribute("studentWithAccommodationsCount", new Integer(studentWithAccommodationsCount));
            this.getRequest().setAttribute("orgCategoryName", orgCategoryName);        
            
        }
        else
        {
            this.getRequest().setAttribute("studentCount", new Integer(0));
            this.getRequest().setAttribute("studentWithAccommodationsCount", new Integer(0));
        }
                
        if (actionElement != null)
        {
            if (actionElement.equals("{actionForm.studentStatePathList.sortOrderBy}") || actionElement.equals("{actionForm.studentStatePathList.pageRequested}") || actionElement.equals("ButtonGoInvoked_studentTableAnchor"))
            {
                form.setSelectedStudentIds(null);
            }
        }
                
        if (getSelectedIdCount(form.getSelectedStudentIds()) > 0)
            this.getRequest().setAttribute("disableRemoveSelectedStudents", Boolean.FALSE);
        else
            this.getRequest().setAttribute("disableRemoveSelectedStudents", Boolean.TRUE);
        

        //////////////////////////////////////////////
        ////proctor table
        ///////////////////////////////////////////////

        if (this.selectedProctors != null && this.selectedProctors.size() > 0)
        {
            
            UserData ud = new UserData();
            User [] users = new User[this.selectedProctors.size()];
            this.selectedProctors.toArray(users);
            ud.setUsers(users, new Integer(FilterSortPageUtils.PAGESIZE_10));
            form.getProctorStatePathList().setMaxPageRequested(ud.getFilteredPages());

            FilterParams filter = null;
            PageParams page = FilterSortPageUtils.buildPageParams(form.getProctorStatePathList().getPageRequested(), FilterSortPageUtils.PAGESIZE_10);
            
            SortParams sort = null;
            if (FilterSortPageUtils.PROCTOR_DEFAULT_SECONDARY_SORT_COLUMN.equals(form.getProctorStatePathList().getSortColumn()))
            {
                sort = FilterSortPageUtils.buildSortParams(form.getProctorStatePathList().getSortColumn(), form.getProctorStatePathList().getSortOrderBy(), FilterSortPageUtils.PROCTOR_DEFAULT_SORT_COLUMN, ColumnSortEntry.ASCENDING);
            }
            else
            {
                sort = FilterSortPageUtils.buildSortParams(form.getProctorStatePathList().getSortColumn(), form.getProctorStatePathList().getSortOrderBy(), FilterSortPageUtils.PROCTOR_DEFAULT_SECONDARY_SORT_COLUMN, ColumnSortEntry.ASCENDING);
            }
            
            try
            {
                if (filter != null)
                    ud.applyFiltering(filter);
                if (sort != null)
                    ud.applySorting(sort);
                if (page != null)
                    ud.applyPaging(page);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    
            List proctorNodes = buildProctorList(ud);

            PagerSummary proctorPagerSummary = buildProctorPagerSummary(ud, form.getProctorStatePathList().getPageRequested());
            this.getRequest().setAttribute("proctorNodes", proctorNodes);        
            this.getRequest().setAttribute("proctorPagerSummary", proctorPagerSummary);
            this.getRequest().setAttribute("proctorCount", new Integer(this.selectedProctors.size()));
        }
        
        if (getSelectedIdCount(form.getSelectedProctorIds()) > 0)
            this.getRequest().setAttribute("disableRemoveSelectedProctors", Boolean.FALSE);
        else
            this.getRequest().setAttribute("disableRemoveSelectedProctors", Boolean.TRUE);
        
        
        if (integrationAction.equals(ACTION_COPY_TEST))
        { // for copy test
            this.getRequest().setAttribute("informationMessage", 
                MessageResourceBundle.getMessage("SelectSettings.CopyTest", form.getTestAdmin().getSessionName().trim()));
            form.getTestAdmin().setSessionName("Copy of " +
                                               form.getTestAdmin().getSessionName().trim());
        
            this.testAdminId = null;
            this.scheduledSession = null;
        }
        
        if (this.action.equals(ACTION_VIEW_TEST))
        {
            if ("T".equals(this.scheduledSession.getCopyable()))
                this.getRequest().setAttribute("hideCopyButton", Boolean.FALSE);
            else
                this.getRequest().setAttribute("hideCopyButton", Boolean.TRUE);
            if (form.getTestAdmin().getLocation() != null && !form.getTestAdmin().getLocation().trim().equals(""))
                this.getRequest().setAttribute("hideTestLocation", Boolean.FALSE);
            else 
                this.getRequest().setAttribute("hideTestLocation", Boolean.TRUE);
        }
        else
        {
            this.getRequest().setAttribute("hideCopyButton", Boolean.TRUE);
            this.getRequest().setAttribute("hideTestLocation", Boolean.FALSE);
        }

        
        if (this.addedStudentsCount != 0)
        {
            StringBuffer buf = new StringBuffer();
            if (this.addedStudentsCount > 0)
                buf.append(MessageResourceBundle.getMessage("SelectSettings.Students.StudentsAdded", "" +
                                                                                                     Math.abs(this.addedStudentsCount)));
            else
                buf.append(MessageResourceBundle.getMessage("SelectSettings.Students.StudentsRemoved", "" +
                                                                                                       Math.abs(this.addedStudentsCount)));
            
            if (TestSession.FormAssignment.MANUAL.equals(form.getFormOperand()))
            {
                String defaultForm = "";
                if (this.formList != null && this.formList.length > 0) 
                    defaultForm = this.formList[0];

                buf.append(MessageResourceBundle.getMessage("SelectSettings.Students.DefaultFormApplied", defaultForm));
            }

            if (this.formList.length > 1)
            {
                if (! TestSessionUtils.isTabeProduct(this.productType).booleanValue())
                {
                    if (!TestSession.FormAssignment.ROUND_ROBIN.equals(this.overrideFormAssignment))
                        buf.append(MessageResourceBundle.getMessage("SelectSettings.Students.YouMayChangeForm"));
                }
            }
            else
            {
                if (TestSessionUtils.isTabeProduct(this.productType).booleanValue())
                    buf.append(MessageResourceBundle.getMessage("SelectSettings.Students.TABEClickSave"));
                else
                    buf.append(MessageResourceBundle.getMessage("SelectSettings.Students.ClickSave"));                    
            }
                           

            this.getRequest().setAttribute("studentInformationMessage", buf.toString());
            this.addedStudentsCount = 0;
        }

        if (this.addedProctorsCount != 0)
        {
            StringBuffer buf = new StringBuffer();
            if (this.addedProctorsCount > 0)
                buf.append(MessageResourceBundle.getMessage("SelectSettings.Proctors.ProctorsAdded", "" +
                                                                                                     Math.abs(this.addedProctorsCount)));
            else
                buf.append(MessageResourceBundle.getMessage("SelectSettings.Proctors.ProctorsRemoved", "" +
                                                                                                       Math.abs(this.addedProctorsCount)));

            this.getRequest().setAttribute("proctorInformationMessage", buf.toString());
            this.addedProctorsCount = 0;
        }
        
        this.getRequest().setAttribute("overrideStartDate", DateUtils.formatDateToDateString(this.overrideLoginStartDate));

        String reportable = getReportable(this.testAdminId);
        this.getRequest().setAttribute("reportable", reportable);                     

        String helpLink = getHelpLink(integrationAction);
        this.getRequest().setAttribute("helpLink", helpLink);

        this.getRequest().setAttribute("productType", this.productType);
        
        Boolean tabeProduct = TestSessionUtils.isTabeProduct(this.productType);        
        Boolean isTabeBatterySurveyProduct = TestSessionUtils.isTabeBatterySurveyProduct(this.productType);
        Boolean isTabeLocatorProduct = TestSessionUtils.isTabeLocatorProduct(this.productType);
        
        this.getRequest().setAttribute("isTabeProduct", tabeProduct);            
        this.getRequest().setAttribute("isTabeBatterySurveyProduct", isTabeBatterySurveyProduct);                    
        this.getRequest().setAttribute("isTabeLocatorProduct", isTabeLocatorProduct);                    
        
        if (tabeProduct.booleanValue())
        {
            this.getRequest().setAttribute("hideCopyButton", Boolean.TRUE);
        }

        String autoLocator = form.getAutoLocator();
        if ((autoLocator != null) && autoLocator.equals("true") && tabeProduct.booleanValue() && (this.locatorSubtest != null))
        {
            this.getRequest().setAttribute("showLocatorSubtest", Boolean.TRUE);
        }
        
        if (isTabeBatterySurveyProduct.booleanValue() && (! hasMultipleSubtests))
        {
            this.getRequest().setAttribute("tabeWithSingleSubtest", Boolean.TRUE);
        }
               
        form.setActionElement(ACTION_DEFAULT);  
        setFormInfoOnRequest(form);
        
        return new Forward("selectSettings", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="editSubtestLevels.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "editSubtestLevels.do")
    })
    protected Forward gotoEditSubtestLevels(ScheduleTestForm form)
    {
        this.studentId = form.getSelectedStudentId();

        TestSession testSession = this.scheduledSession.getTestSession();
        this.itemSetId = testSession.getItemSetId();

        if (this.locatorSubtest == null)
        {
            this.locatorSubtest = TestSessionUtils.getLocatorSubtest(this.scheduleTest, this.userName, this.itemSetId); 
        }
        
        this.levelOptions = TestSessionUtils.getLevelOptions();

        this.selectedSubtests = TestSessionUtils.getStudentSubtests(this.scheduleTest, this.userName, this.studentId, this.testAdminId);

        if (TestSessionUtils.locatorSubtestPresent(this.selectedSubtests))
            form.setAutoLocator("true");
        else 
            form.setAutoLocator(null);

        TestSessionUtils.extractLocatorSubtest(this.selectedSubtests);

        this.allSubtests = TestSessionUtils.getAllSubtestsForTest(this.scheduleTest, this.userName, this.itemSetId);
        
        TestSessionUtils.extractLocatorSubtest(this.allSubtests);

        this.allSubtests = TestSessionUtils.sortSubtestList(this.allSubtests, this.selectedSubtests); 

        List testAdminSubtests = TestSessionUtils.getAllSubtestsForTestAdmin(this.scheduleTest, this.userName, this.testAdminId);

        TestSessionUtils.copySubtestLevel(testAdminSubtests, this.allSubtests);

        Integer locatorItemSetId = this.locatorSubtest.getId();
                             
        TestSessionUtils.setRecommendedLevelForSession(this.scheduleTest, this.userName, this.studentId, this.itemSetId, locatorItemSetId, this.allSubtests);

        TestSessionUtils.copySubtestLevelIfNull(this.allSubtests, this.selectedSubtests);
        
        return new Forward("success", form);
    }
    
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="editSubtestLevels.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "editSubtestLevels.jsp")
    })
    protected Forward editSubtestLevels(ScheduleTestForm form)
    {
        Boolean checked = Boolean.FALSE;
        String autoLocator = form.getAutoLocator();
        if ((autoLocator != null) && autoLocator.equals("true"))
        {            
            checked = Boolean.TRUE;
        }
        Boolean showLevel = checked.booleanValue() ? Boolean.FALSE : Boolean.TRUE;      
          
        this.availableSubtests = TestSessionUtils.getAvailableSubtests(this.allSubtests, this.selectedSubtests);                
        
        this.getRequest().setAttribute("allSubtests", this.allSubtests);
        this.getRequest().setAttribute("availableSubtests", this.availableSubtests);
        this.getRequest().setAttribute("selectedSubtests", this.selectedSubtests);
        this.getRequest().setAttribute("levelOptions", this.levelOptions);                
        
        this.getRequest().setAttribute("subtestName", this.locatorSubtest.getSubtestName());
        this.getRequest().setAttribute("levelName", this.locatorSubtest.getLevel());
        this.getRequest().setAttribute("hasLocatorSubtest", Boolean.TRUE);        

        this.getRequest().setAttribute("checked", checked);
        this.getRequest().setAttribute("showLevel", showLevel);
        
        String studentName = TestSessionUtils.getStudentDisplayName(this.studentNodes, this.studentId);

        this.getRequest().setAttribute("studentName", studentName);
        this.getRequest().setAttribute("sessionName", form.getTestAdmin().getSessionName().trim());

        String locatorSessionInfo = "";
        if (showLevel.booleanValue())
        {
            Integer locatorItemSetId = this.locatorSubtest.getId();
            String currentAction = form.getCurrentAction();
            locatorSessionInfo = TestSessionUtils.getLocatorSessionInfo(this.scheduleTest, this.userName, this.studentId, this.itemSetId, locatorItemSetId, this.selectedSubtests);
        }
        if (locatorSessionInfo.length() > 0)
        {
            this.getRequest().setAttribute("locatorSessionInfo", locatorSessionInfo);
        }
        //START - Added for Deferred Defect 64306
        setFormInfoOnRequest(form);
        //END - Added for Deferred Defect 64306
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="printOptions" path="printOptions.do"
     * @jpf:forward name="editSubtestLevels" path="editSubtestLevels.do"
     * @jpf:forward name="error" path="editSubtestLevels.do"
     * @jpf:validation-error-forward name="failure" path="editSubtestLevels.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "printOptions",
                     path = "printOptions.do"), 
        @Jpf.Forward(name = "editSubtestLevels",
                     path = "editSubtestLevels.do"), 
        @Jpf.Forward(name = "error",
                     path = "editSubtestLevels.do")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "editSubtestLevels.do"))
    protected Forward returnFromEditSubtestLevels(ScheduleTestForm form)
    {
        String currentAction = form.getCurrentAction();
        String autoLocator = form.getAutoLocator();
        
        if (currentAction.equals(ACTION_AUTOLOCATOR))
        {
            this.selectedSubtests = TestSessionUtils.retrieveSelectedSubtestsFromRequest(this.getRequest(), this.allSubtests);
            this.allSubtests = TestSessionUtils.sortSubtestList(this.allSubtests, this.selectedSubtests);             
            return new Forward("editSubtestLevels", form);
        }

        if (currentAction.equals("doneEditSubtests"))
        {

            this.selectedSubtests = TestSessionUtils.retrieveSelectedSubtestsFromRequest(this.getRequest(), this.allSubtests);
            
            boolean autoLocatorChecked = ((autoLocator != null) && autoLocator.equals("true"));
            boolean validateLevels = !autoLocatorChecked;          
                 
            boolean valid = TABESubtestValidation.validation(this.selectedSubtests, validateLevels);
                                                   
            String message = TABESubtestValidation.currentMessage;        
            form.setSubtestValidationMessage(null);
            
            if (! valid)
            {
                form.setCurrentAction(ACTION_SUBTEST_VALIDATION_FAILED);  
                //START - Changed for Deferred Defect 64306
                form.setMessage(MessageResourceBundle.getMessage("SelectSettings.SubtestValidationTitle"),message, Message.ERROR);  
                //END - Changed for Deferred Defect 64306
                this.allSubtests = TestSessionUtils.sortSubtestList(this.allSubtests, this.selectedSubtests); 
                
                return new Forward("error", form);            
            }
            else
            {
                if (! message.equals(TABESubtestValidation.NO_ERROR_MSG))
                {
                    form.setSubtestValidationMessage(message);
                }
            }                

            if ((autoLocator != null) && autoLocator.equals("true"))
            {            
                TestSessionUtils.restoreLocatorSubtest(this.selectedSubtests, this.locatorSubtest);
            }
                        
            int subtestSize = this.selectedSubtests.size();

            StudentManifestData manifestData = new StudentManifestData();   
            StudentManifest [] manifestArray = new StudentManifest[subtestSize];
            
            for (int i=0; i < subtestSize; i++)
            {
                
                SubtestVO subtest = (SubtestVO)this.selectedSubtests.get(i);
                StudentManifest manifest = new StudentManifest();    
                manifest.setItemSetId(subtest.getId());
                manifest.setItemSetName(subtest.getSubtestName());                
                manifest.setItemSetForm(subtest.getLevel());            
                manifest.setItemSetOrder(new Integer(i + 1));
                manifest.setTestAccessCode(subtest.getTestAccessCode());
                manifestArray[i] = manifest;
            }
            try {
            manifestData.setStudentManifests(manifestArray, new Integer(manifestArray.length));
            TestSessionUtils.updateManifestForRoster(this.scheduleTest, this.userName, this.studentId, this.testAdminId, manifestData);  
            } 
            catch (CTBBusinessException e)
            {
                e.printStackTrace();
                form.setMessage(MessageResourceBundle.getMessage("ModifyTest.InsufficentLicenseQuantityTitle"),Message.INSUFFICENT_LICENSE_QUANTITY, Message.ERROR);   
                return new Forward("error", form);            
            } 
        }
        
        return new Forward("printOptions", form);
    }
    
    
    private String getHelpLink(String action) 
    {
        // this helpLink should be retrieved from helpResources.properties
        // hardcoded for now and continue to make it works that way.
        String helpLink = null;
        if (action.equals(ACTION_SCHEDULE_TEST))
            helpLink = "/help/index.html#selecting_test_settings.htm";
        else if (action.equals(ACTION_EDIT_TEST))
            helpLink = "/help/index.html#editing_a_test_session.htm";
        else if (action.equals(ACTION_VIEW_TEST))
            helpLink = "/help/index.html#viewing_test_session_information.htm";
        else if (action.equals(ACTION_COPY_TEST))
            helpLink = "/help/index.html#copying_a_test_session.htm";
        else
            helpLink = "/help/index.html#selecting_test_settings.htm";
        
        return helpLink;
    }
     
    private String getReportable(Integer testAdminId) 
    {
        String reportable = null;
        Boolean userHasReport = userHasReports();
        
        try
        {      
            if (userHasReport.booleanValue() && (this.userName != null) && (testAdminId != null) && (testAdminId.intValue() > 0))
            {
                TestSessionData tsd = this.testSessionStatus.getTestSessionDetails(this.userName, testAdminId);
                TestSession[] testsessions = tsd.getTestSessions();            
                TestSession ts = testsessions[0];
                if (ts.getReportable().equals("T"))
                    reportable = "true";
            }
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return reportable;
    }

    private Boolean userHasReports() 
    {
        boolean hasReports = false;
        try {      
            Customer customer = this.user.getCustomer();
            Integer customerId = customer.getCustomerId();   
            hasReports = this.testSessionStatus.userHasReports(this.userName, customerId);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return new Boolean(hasReports);           
    }

	private Boolean customerHasBulkAccommodation()
    {               
        Integer customerId = this.user.getCustomer().getCustomerId();
        boolean hasBulkStudentConfigurable = false;

        try
        {      
			CustomerConfiguration [] customerConfigurations = users.getCustomerConfigurations(customerId.intValue());
			if (customerConfigurations == null || customerConfigurations.length == 0) {
				customerConfigurations = users.getCustomerConfigurations(2);
			}
            
            for (int i=0; i < customerConfigurations.length; i++)
            {
            	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
                //Bulk Accommodation
                if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && cc.getDefaultValue().equals("T")	)
                {
                    hasBulkStudentConfigurable = true;
                    break;
                } 
            }
        }
        catch (SQLException se) {
        	se.printStackTrace();
		}
               
        return new Boolean(hasBulkStudentConfigurable);
    }
	
	//changes for scoring
	
	/**
	 * This method checks whether customer is configured to access the scoring feature or not.
	 * @return Return Boolean 
	 */
	
	
	private Boolean customerHasScoring()
    {               
		Integer customerId = this.user.getCustomer().getCustomerId();
        boolean hasScoringConfigurable = false;
        boolean isLaslinkCustomer = false;
        try
        {      
			CustomerConfiguration [] customerConfigurations = users.getCustomerConfigurations(customerId.intValue());
			if (customerConfigurations == null || customerConfigurations.length == 0) {
				customerConfigurations = users.getCustomerConfigurations(2);
			}
        

        for (int i=0; i < customerConfigurations.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
            		cc.getDefaultValue().equals("T")	) {
            	hasScoringConfigurable = true;
            	getSession().setAttribute("isScoringConfigured", hasScoringConfigurable);
                //break;
            } 
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")
    				&& cc.getDefaultValue().equals("T")) {
    			isLaslinkCustomer = true;
    			//break;
            }
        }
       }
        catch (SQLException se) {
        	se.printStackTrace();
		}
        this.setIslaslinkCustomer(isLaslinkCustomer);
        getSession().setAttribute("isScoringConfigured", hasScoringConfigurable);
        return new Boolean(hasScoringConfigurable);
    }


    
//changes for TABE BAUM - 028
	
	/**
	 * This method checks whether customer is configured to display access code in individual 
	 * and multiple testTicket or not.
	 * @return Return Boolean 
	 */
	
	
	private Boolean customerHasAccessCode()
    {               
		Integer customerId = this.user.getCustomer().getCustomerId();
        boolean hasAccessCodeConfigurable = false;
        String hasBreak = "T";
        try
        {  
        	hasBreak = users.hasMultipleAccessCode(this.getTestAdminId());
        	
			CustomerConfiguration [] customerConfigurations = users.getCustomerConfigurations(customerId.intValue());
			if (customerConfigurations == null || customerConfigurations.length == 0) {
				customerConfigurations = users.getCustomerConfigurations(2);
			}
        

        for (int i=0; i < customerConfigurations.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Print_Accesscode") && 
            		cc.getDefaultValue().equals("T")	) {
            	hasAccessCodeConfigurable = true;
                break;
            } 
        }
       }
        
        catch (SQLException se) {
        	se.printStackTrace();
		}
       if(hasBreak.equals("F") && hasAccessCodeConfigurable)
    	   return true;
       else
    	   return false;
    }
	

    /**
     * @jpf:action
     * @jpf:forward name="success" path="printOptions.jsp"
     * @jpf:validation-error-forward name="failure" path="logout.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "printOptions.jsp")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "logout.do"))
    protected Forward printOptions(ScheduleTestForm form)
    {        
        String actionElement = form.getActionElement();
        form.validateValues();
        form.resetValuesForPrintOptionsAction(actionElement);
        FormFieldValidator.validateFilterForm(form, getRequest());
        

        if (this.orgNodePath == null)
            this.orgNodePath = new ArrayList();
            
        String orgNodeName = form.getOrgNodeName();
        this.orgNodeId = form.getOrgNodeId();   
        
        FilterParams filter = null;
        PageParams page = FilterSortPageUtils.buildPageParams(form.getOrgStatePathList().getPageRequested(), FilterSortPageUtils.PAGESIZE_10);
        SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgStatePathList().getSortColumn(), form.getOrgStatePathList().getSortOrderBy(), null, null);
        
        StudentNodeData snd = null;
        
        snd = getTestTicketNodes(this.orgNodeId, this.testAdminId, filter, page, sort);           

        form.getOrgStatePathList().setMaxPageRequested(snd.getFilteredPages());
        if (form.getOrgStatePathList().getPageRequested().intValue() > snd.getFilteredPages().intValue())
            form.getOrgStatePathList().setPageRequested(snd.getFilteredPages());
            
        List orgNodes = buildOrgNodeList(snd);
        String orgCategoryName = getOrgCategoryName(orgNodes);
        PagerSummary orgPagerSummary = buildOrgNodePagerSummary(snd, form.getOrgStatePathList().getPageRequested());        
        
        this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
        this.getRequest().setAttribute("orgNodes", orgNodes);        
        this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
        this.getRequest().setAttribute("orgCategoryName", orgCategoryName); 
        this.getRequest().setAttribute("showTicketLink", new Boolean(orgNodes.size() > 0)); 
        this.getRequest().setAttribute("showAccessCode", customerHasAccessCode()); // Added for TABE BAUM - 028

        boolean nodeChanged = PathListUtils.adjustOrgNodePath(this.orgNodePath, this.orgNodeId, orgNodeName);

        if (nodeChanged)
        {
            if (orgNodes.size() > 0)
            {
                PathNode node = (PathNode)orgNodes.get(0);
                form.setSelectedOrgNodeId(node.getId());
                form.setSelectedOrgNodeName(node.getName());
                this.orgNodeId = node.getId();
            }
        }
        else
        {        
            actionElement = form.getActionElement();
            if (actionElement.equals("{actionForm.orgStatePathList.pageRequested}") || (actionElement.indexOf("EnterKeyInvoked") != -1) || (actionElement.indexOf("ButtonGoInvoked") != -1))
            {
                if (orgNodes.size() > 0)
                {
                    PathNode node = (PathNode)orgNodes.get(0);
                    form.setSelectedOrgNodeId(node.getId());
                    form.setSelectedOrgNodeName(node.getName());
                    this.orgNodeId = node.getId();
                }
            }
        }

        prepareStudentInRoster(form);
        
        setFormInfoOnRequest(form);
        return new Forward("success", form);
    }
    
    
    private void prepareStudentInRoster(ScheduleTestForm form) 
    {           
        if (this.selectedStudents != null)
        {             
            TestSession testSession = this.scheduledSession.getTestSession();
            boolean completedTest = testSession.getTestAdminStatus().equals("PA");

            SessionStudentData ssd = new SessionStudentData();
            SessionStudent [] sessionStudents = new SessionStudent[this.selectedStudents.size()];
            Iterator it = this.selectedStudents.iterator();
            int i=0;
            while (it.hasNext())
            {
                SessionStudent ss = (SessionStudent)it.next();
                if (completedTest)
                {
                    ss.getStatus().setEditable("F");
                }
                sessionStudents[i++] = ss;
            }
            ssd.setSessionStudents(sessionStudents, new Integer(FilterSortPageUtils.PAGESIZE_10));
            form.getStudentStatePathList().setMaxPageRequested(ssd.getFilteredPages());
    
            PageParams page = FilterSortPageUtils.buildPageParams(form.getStudentStatePathList().getPageRequested(), FilterSortPageUtils.PAGESIZE_10);
            SortParams sort = FilterSortPageUtils.buildOrgNameLastNameFirstNameSortParams(form.getStudentStatePathList().getSortColumn(), form.getStudentStatePathList().getSortOrderBy());
            
            try
            {
                if (sort != null)
                    ssd.applySorting(sort);
                if (page != null)
                    ssd.applyPaging(page);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    
            this.studentNodes = buildStudentList(ssd);
            String studentOrgCategoryName = getSessionStudentOrgCategoryName(this.studentNodes);
            this.getRequest().setAttribute("studentOrgCategoryName", studentOrgCategoryName);        
            
            PagerSummary studentPagerSummary = buildStudentPagerSummary(ssd, form.getStudentStatePathList().getPageRequested());
            this.getRequest().setAttribute("studentPagerSummary", studentPagerSummary);
            
            if (TestSessionUtils.isTabeLocatorProduct(this.productType).booleanValue())             
                this.getRequest().setAttribute("hasStudentSelected", Boolean.FALSE);
            else
                this.getRequest().setAttribute("hasStudentSelected", Boolean.TRUE);
        }
        else
        {
            this.getRequest().setAttribute("hasStudentSelected", Boolean.FALSE);
        }
        
        this.getRequest().setAttribute("productType", this.productType);
        this.getRequest().setAttribute("isTabeProduct", TestSessionUtils.isTabeProduct(this.productType));    
        
        String actionElement = form.getActionElement();
        if (actionElement != null)
        {        
            if (actionElement.equals("{actionForm.studentStatePathList.sortOrderBy}") || actionElement.equals("{actionForm.studentStatePathList.pageRequested}") || actionElement.equals("ButtonGoInvoked_studentTableAnchor"))
            {
                form.setSelectedStudentId(null);
            }
        }
                
        if (form.getSelectedStudentId() != null)
            this.getRequest().setAttribute("disableModifyTestButton", "false");
        else
            this.getRequest().setAttribute("disableModifyTestButton", "true");
    }
    
    
    
    private Collection buildRosterList(RosterElementData red, SessionStudent[] students)
    {
        HashMap map = new HashMap();
        RosterElement[] rosterElements = red.getRosterElements();
        for (int i=0; i < rosterElements.length; i++)
        {
            RosterElement rosterElt = rosterElements[i];
            if (rosterElt != null)
            {
                TestRosterVO vo = new TestRosterVO(rosterElt);
                map.put(vo.getStudentId(), vo);
            }
        }     
        if (students != null)
        {
            for (int i=0; i < students.length; i++)
            {
                SessionStudent student = students[i];
                TestRosterVO roster = (TestRosterVO)map.get(student.getStudentId());
                setAccommodations(roster, student);
            }
        }   
        return map.values();
    }

    private void setAccommodations(TestRosterVO roster, SessionStudent student)
    {
        List accommodationList = new ArrayList();
        if (!stringToBoolean(student.getHasAccommodations()))
        {
            accommodationList.add("---");
        }
        else
        {
            if (stringToBoolean(student.getCalculator()))
            {
                accommodationList.add("Calculator");
            }
            if (stringToBoolean(student.getHasColorFontAccommodations()))
            {
                accommodationList.add("Color/Font");
            }
            if (stringToBoolean(student.getTestPause()))
            {
                accommodationList.add("Pause");
            }
            if (stringToBoolean(student.getScreenReader()))
            {
                accommodationList.add("Screen Reader");
            }
            if (stringToBoolean(student.getUntimedTest()))
            {
                accommodationList.add("Untimed");
            }
        }
        String[] accommodations = new String[accommodationList.size()];
        for (int i=0; i < accommodationList.size(); i++)
        {
            accommodations[i] = (String)accommodationList.get(i);
        }
        roster.setAccommodations(accommodations);
    }
    
    private TestAdminVO buildIndividualTestAdminVO(TestSessionData tsd)
    {
        TestSession[] testSessions = tsd.getTestSessions();
        TestSession testSession = testSessions[0];
        String testName = testSession.getTestAdminName();
        String location = testSession.getLocation();
        String accessCode = testSession.getAccessCode();
        return new TestAdminVO(testName, location, accessCode);
    }
    
    private Integer getTestId(TestSessionData tsd)
    {
        TestSession[] testSessions = tsd.getTestSessions();
        TestSession testSession = testSessions[0];
        return testSession.getItemSetId();
    }
  
    private TestAdminVO buildTestAdminVO(TestSessionData tsd)
    {
        TestSession[] testSessions = tsd.getTestSessions();
        TestSession testSession = testSessions[0];
        String testName = testSession.getTestAdminName();
        String location = testSession.getLocation();
        String accessCode = testSession.getAccessCode();
        String sessionName = testSession.getTestAdminName();
        String level = null;
        Date startDate = testSession.getLoginStartDate();
        Date endDate = testSession.getLoginEndDate();
        Date startTime = testSession.getDailyLoginStartTime();
        Date endTime = testSession.getDailyLoginEndTime();
        String timeZone = testSession.getTimeZone();
        String timeLimit = testSession.getEnforceTimeLimit();
        String enforceBreaks = testSession.getEnforceBreak();
        String isRandomize = testSession.getIsRandomize();
        Integer productId = testSession.getProductId();
        String tutorial = null;
        List subtests = this.defaultSubtests;
        return new TestAdminVO(sessionName, testName, location, accessCode, level, startDate, endDate, startTime, endTime, timeZone, timeLimit, enforceBreaks, isRandomize, tutorial, subtests, productId);   
    }
       
    /**
     * @jpf:action
     * @jpf:forward name="success" path="selectSettings.do"
     * @jpf:forward name="error" path="selectSettings.do"
     * @jpf:validation-error-forward name="failure" path="selectSettings.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "selectSettings.do"), 
        @Jpf.Forward(name = "error",
                     path = "selectSettings.do")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "selectSettings.do"))
    protected Forward saveTest(ScheduleTestForm form)
    {
        form.validateValues();
        int studentCountAfterSave;
        int studentCountBeforeSave = 0;
        if (this.selectedStudents != null)
            studentCountBeforeSave = this.selectedStudents.size();
        try
        {
            this.testAdminId = createSaveTest(form);
            this.condition.setReloadTestSession(Boolean.TRUE); // force Kitchen sink to reload from DB
            
            RosterElementData red = this.testSessionStatus.getRosterForTestSession(this.userName,
                            this.testAdminId, null, null, null);
            studentCountAfterSave = red.getTotalCount().intValue();                            
                            
        }   
        catch (InsufficientLicenseQuantityException e)
        {
            e.printStackTrace();
            String errorMessage = MessageResourceBundle.getMessage("SelectSettings.InsufficentLicenseQuantity.E001");
            this.getRequest().setAttribute("errorMessage", errorMessage); 
            return new Forward("error", form);            
        } 
        //START- Changed for deferred defect 64446
        catch (TransactionTimeoutException e)
        {
            e.printStackTrace();
            String errorMessage =MessageResourceBundle.getMessage("SelectSettings.FailedToSaveTestSessionTransactionTimeOut"); 
            System.out.println("errorMessage in TransactionTimeoutException==> " + errorMessage);
            this.getRequest().setAttribute("errorMessage", errorMessage); 
            return new Forward("error", form);            
        }
        //END- Changed for deferred defect 64446 
        catch (CTBBusinessException e)
        {
            e.printStackTrace();
            String errorMessage = getMessageResourceBundle(e, "SelectSettings.FailedToSaveTestSession"); 
            this.getRequest().setAttribute("errorMessage", errorMessage); 
            return new Forward("error", form);            
        } 
        
        if (studentCountBeforeSave == studentCountAfterSave)          
            this.getRequest().setAttribute("informationMessage", MessageResourceBundle.getMessage("SelectSettings.TestSessionSaved")); 
        else
        {
            int removedCount = studentCountBeforeSave - studentCountAfterSave;
            this.getRequest().setAttribute("informationMessage", MessageResourceBundle.getMessage("SelectSettings.TestSessionSaved") + MessageResourceBundle.getMessage("RestrictedStudentsNotSaved", "" +
                                                                                                                                                                                                      removedCount)); 
        }
        
        return new Forward("success", form);
    }
    
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="goToHomePage.do"
     * @jpf:forward name="error" path="selectSettings.do"
     * @jpf:validation-error-forward name="failure" path="selectSettings.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "goToHomePage.do"), 
        @Jpf.Forward(name = "error",
                     path = "selectSettings.do")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "selectSettings.do"))
    protected Forward endTestSession(ScheduleTestForm form)
    {
        form.validateValues();
        
        TimeZone defaultTimeZone = TimeZone.getDefault();
        String timeZone = DateUtils.getDBTimeZone(form.getTestAdmin().getTimeZone());
        Date now = new Date(System.currentTimeMillis());
/*
        int minutes = now.getMinutes();
        minutes = minutes/15*15;
        now.setMinutes(minutes);
*/
        now = com.ctb.util.DateUtils.getAdjustedDate(now, defaultTimeZone.getID(), timeZone, now);
        String timeStr = DateUtils.formatDateToTimeString(now);
        String dateStr = DateUtils.formatDateToDateString(now);
        form.setEndDate(dateStr);
        form.setEndTime(timeStr);
        
        try
        {
            this.testAdminId = createSaveTest(form);
            this.condition.setReloadTestSession(Boolean.TRUE); // force Kitchen sink to reload from DB
        }   
        catch (CTBBusinessException e)
        {
            e.printStackTrace();
            String errorMessage = getMessageResourceBundle(e, "SelectSettings.FailedToEndTestSession"); 
            this.getRequest().setAttribute("errorMessage", errorMessage); 
            return new Forward("error", form);            
        
                    
        }            
        return new Forward("success");
    }
    

    /**
     * @jpf:action
     * @jpf:forward name="success" path="printOptions.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "printOptions.do")
    })
    protected Forward goToPrintOptionsFromView(ScheduleTestForm form)
    {
        form.validateValues();
        
        this.condition.setReloadTestSession(Boolean.TRUE); // force Kitchen sink to reload from DB
        this.orgNodePath = new ArrayList(); //clean up path
        return new Forward("success", form);
    }

    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="printOptions.do"
     * @jpf:forward name="error" path="selectSettings.do"
     * @jpf:validation-error-forward name="failure" path="selectSettings.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "printOptions.do"), 
        @Jpf.Forward(name = "error",
                     path = "selectSettings.do")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "selectSettings.do"))
    protected Forward goToPrintOptions(ScheduleTestForm form)
    {
    	form.validateValues();
        form.resetStudentSortPage();
        
        int studentCountAfterSave;
        int studentCountBeforeSave = 0;
                
        if (this.selectedStudents != null)
            studentCountBeforeSave = this.selectedStudents.size();
        try
        {
            if (!this.action.equals(ACTION_VIEW_TEST))
            {
                this.testAdminId = createSaveTest(form); 
            }
            this.condition.setReloadTestSession(Boolean.TRUE); // force Kitchen sink to reload from DB
            this.orgNodePath = new ArrayList(); //clean up path
            
            RosterElementData red = this.testSessionStatus.getRosterForTestSession(this.userName,
                            this.testAdminId, null, null, null);
            studentCountAfterSave = red.getTotalCount().intValue(); 
                 
        } 
        //START- Changed for deferred defect 64446 
        catch (TransactionTimeoutException e)
        {
            e.printStackTrace();
            String errorMessage =MessageResourceBundle.getMessage("SelectSettings.FailedToSaveTestSessionTransactionTimeOut"); 
            System.out.println("errorMessage in TransactionTimeoutException");
            this.getRequest().setAttribute("errorMessage", errorMessage); 
            return new Forward("error", form);            
        } 
        //END- Changed for deferred defect 64446
        catch (CTBBusinessException e)
        {
            e.printStackTrace();
            String errorMessage = getMessageResourceBundle(e, "SelectSettings.FailedToSaveTestSession"); 
            this.getRequest().setAttribute("errorMessage", errorMessage); 
            return new Forward("error", form);            
        }  
        if (studentCountBeforeSave != studentCountAfterSave)
        {      
            int removedCount = studentCountBeforeSave - studentCountAfterSave;
            this.getRequest().setAttribute("informationMessage", MessageResourceBundle.getMessage("RestrictedStudentsNotSaved", "" +
                                                                                                                                removedCount)); 
        }
                  
        return new Forward("success", form);
    }

    private Integer createSaveTest(ScheduleTestForm form) throws CTBBusinessException
    {   
        String sessionName = form.getTestAdmin().getSessionName().trim();
        if (! WebUtils.validString(sessionName))
        {
            throw new ValidationException("SelectSettings.TestSessionName.InvalidCharacters");
        }

        String location = form.getTestAdmin().getLocation().trim();
        if (! WebUtils.validString(location))
        {
            throw new ValidationException("SelectSettings.TestLocation.InvalidCharacters");
        }
                
        Integer newTestAdminId = null;
        boolean isNewSession = true;
        if (this.scheduledSession != null)
            isNewSession = false;
            
        if (isNewSession)
        {
            this.scheduledSession = new ScheduledSession();
        }
            
        
        TestSession testSession;
        if (isNewSession)
        {
            testSession = new TestSession();
            
            testSession.setTestAdminStatus("CU");
            testSession.setTestAdminType("SE");
            testSession.setActivationStatus("AC");
            testSession.setEnforceTimeLimit("T");
            testSession.setCreatedBy(this.userName);
            testSession.setCreatorOrgNodeId(form.getCreatorOrgNodeId());
            testSession.setShowStudentFeedback(this.condition.getShowStudentFeedback().booleanValue() ? "T" : "F");
            
        }
        else
        {
            testSession = this.scheduledSession.getTestSession();
            testSession.setUpdatedBy(this.userName);
        }
        
        if (this.action.equals(ACTION_SCHEDULE_TEST) || this.action.equals(ACTION_EDIT_TEST))
            testSession.setCreatorOrgNodeId(form.getCreatorOrgNodeId());
        
        // set values from form
        TestProduct [] tps = this.testProductData.getTestProducts();           
		//Change for Randomized Distractor saving copy test
        form.setSelectedProductName(this.selectedProductName);        
        this.selectedProductName = form.getSelectedProductName();
        
        int selectedProductIndex = getProductListIndexByName(this.selectedProductName);
        if (selectedProductIndex >= 0)
            testSession.setProductId(tps[selectedProductIndex].getProductId());
        testSession.setDailyLoginEndTime(DateUtils.getDateFromTimeString(form.getEndTime()));
        testSession.setDailyLoginStartTime(DateUtils.getDateFromTimeString(form.getStartTime()));
        testSession.setLocation(form.getTestAdmin().getLocation());
        testSession.setEnforceBreak(form.getHasBreak().booleanValue() ? "T" : "F");
        //set the value retrieved for isRandomize from form 
        testSession.setIsRandomize(form.getTestAdmin().getIsRandomize());
        testSession.setLoginEndDate(DateUtils.getDateFromDateString(form.getEndDate()));
        testSession.setLoginStartDate(DateUtils.getDateFromDateString(form.getStartDate()));
        testSession.setTimeZone(DateUtils.getDBTimeZone(form.getTestAdmin().getTimeZone()));
        testSession.setTestName(form.getTestAdmin().getTestName());
        testSession.setTestAdminName(sessionName);
        testSession.setOverrideLoginStartDate(this.overrideLoginStartDate);
        
        String formOperand = form.getFormOperand();
        if (formOperand.equals(TestSession.FormAssignment.MANUAL))
            testSession.setFormAssignmentMethod(TestSession.FormAssignment.MANUAL);
        else if (formOperand.equals(TestSession.FormAssignment.ALL_SAME))
            testSession.setFormAssignmentMethod(TestSession.FormAssignment.ALL_SAME);
        else 
            testSession.setFormAssignmentMethod(TestSession.FormAssignment.ROUND_ROBIN);
        testSession.setPreferredForm(form.getFormAssigned());      
        
        testSession.setOverrideFormAssignmentMethod(this.overrideFormAssignment);
        testSession.setOverrideLoginStartDate(this.overrideLoginStartDate);
          

        testSession.setItemSetId(new Integer(this.selectedTestId));

        boolean hasBreak = form.getHasBreak().booleanValue();
        
        // set as manual for TB products
        if (TestSessionUtils.isTabeProduct(this.productType).booleanValue())
        {
            testSession.setFormAssignmentMethod(TestSession.FormAssignment.MANUAL);
        }
                
        List subtestList = null;
        boolean sessionHasLocator = false;
                             
        if (TestSessionUtils.isTabeProduct(this.productType).booleanValue())
        {
            // for tabe test
            if (TestSessionUtils.isTabeBatterySurveyProduct(this.productType).booleanValue())
            {
                
                subtestList = TestSessionUtils.setupSessionSubtests(this.sessionSubtests, this.defaultSubtests); 
                
                String autoLocator = form.getAutoLocator();
                if ((autoLocator != null) && autoLocator.equals("true"))
                {            
                    TestSessionUtils.restoreLocatorSubtest(subtestList, this.locatorSubtest);
                    sessionHasLocator = true;
                }
                else
                {
                    TestSessionUtils.setDefaultLevels(subtestList, "E");  // make sure set level = 'E' if null
                }
            } 
            else
            {
                // tabe locator test
                subtestList = TestSessionUtils.cloneSubtests(this.defaultSubtests);
                TestSessionUtils.setDefaultLevels(subtestList, "1");  // make sure set level = '1' for test locator
            }       
            
        }
        else
        {
            // for non-tabe test
            subtestList = TestSessionUtils.cloneSubtests(this.defaultSubtests);
        }
        
        TestElement [] newTEs = new TestElement[subtestList.size()];
        
        for (int i=0; i < subtestList.size(); i++)
        {
            SubtestVO subVO= (SubtestVO)subtestList.get(i);
            TestElement te = new TestElement();
        
            te.setItemSetId(subVO.getId());
            
            if (TestSessionUtils.isTabeProduct(this.productType).booleanValue())
            {                
                String level = subVO.getLevel();
                te.setItemSetForm(level);
            }
            
            if (hasBreak)
                te.setAccessCode(subVO.getTestAccessCode());
            else 
                te.setAccessCode(form.getTestAdmin().getAccessCode());
            
            te.setSessionDefault(subVO.getSessionDefault());
            
            newTEs[i] = te;
        }
        
        if (hasBreak)
        {
            testSession.setAccessCode(newTEs[0].getAccessCode());    
        }
        else
        {
            testSession.setAccessCode(form.getTestAdmin().getAccessCode());
        }
        
        this.scheduledSession.setScheduledUnits(newTEs);
        this.scheduledSession.setTestSession(testSession);

        User [] sessionProctors = new User[this.selectedProctors.size()];
        this.selectedProctors.toArray(sessionProctors);
        this.scheduledSession.setProctors(sessionProctors);
        
        if (this.selectedStudents == null)
            this.scheduledSession.setStudents(null);
        else
        {
            SessionStudent [] sessionStudents = new SessionStudent[this.selectedStudents.size()];

            for (int i=0; i < this.selectedStudents.size(); i++)
            {
                SessionStudent sessionStudent = (SessionStudent)this.selectedStudents.get(i);
                             
                if (TestSessionUtils.isTabeProduct(this.productType).booleanValue())
                {   
                                                                     
                    // replicate student's manifest if this student has no individual manifest
                    StudentManifest [] studentManifests = sessionStudent.getStudentManifests();
                    if ((studentManifests == null) || (studentManifests.length == 0))
                    {
                        
                        List studentSubtestList = TestSessionUtils.getDefaultSubtests(newTEs);
                        
                        studentManifests = new StudentManifest[studentSubtestList.size()];
                        
                        for (int j=0; j < studentSubtestList.size(); j++)
                        {
                            
                            SubtestVO subtestVO = (SubtestVO)studentSubtestList.get(j);
                            
                            studentManifests[j] = new StudentManifest();
                            
                            studentManifests[j].setItemSetId(subtestVO.getId());
                            studentManifests[j].setItemSetName(subtestVO.getSubtestName());                            
                            studentManifests[j].setItemSetForm(subtestVO.getLevel());
                            studentManifests[j].setItemSetOrder(new Integer(j + 1));                            
                        }   
                        
                        // set recommended level for this student if there is no locator for this session
                        if (! sessionHasLocator)
                        {
                            Integer studentId = sessionStudent.getStudentId();
                            Integer itemSetId = testSession.getItemSetId();
                            SubtestVO locSubtest = this.locatorSubtest;
                            if (locSubtest == null)
                            {                   
                                locSubtest = TestSessionUtils.getLocatorSubtest(this.scheduleTest, this.userName, itemSetId); 
                            }
                            Integer locatorItemSetId = locSubtest.getId();
                             
                            TestSessionUtils.setRecommendedLevelForStudent(this.scheduleTest, this.userName, studentId, itemSetId, locatorItemSetId, studentManifests);
                        }
                                     
                        sessionStudent.setStudentManifests(studentManifests);
                    }                                                            
                }
                
                    
                sessionStudents[i] = sessionStudent;                
            }
        
            this.scheduledSession.setStudents(sessionStudents);
        }
        
        //Change for transaction rollback during exceed of license
        if (!isNewSession)
        {
            
            if (!"true".equals(this.scheduleTest.isTestAdminExists
                    (scheduledSession.getTestSession().getTestAdminId())))
            {
                
                isNewSession = true;
                this.scheduledSession.getTestSession().setTestAdminId(null);
            }
            
        } 
        if (isNewSession)
            newTestAdminId = this.scheduleTest.createNewTestSession(this.userName, this.scheduledSession);        
        else
            newTestAdminId = this.scheduleTest.updateTestSession(this.userName, this.scheduledSession);        
         
        return newTestAdminId;
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/selectStudentPageflow/SelectStudentPageflowController.jpf"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "/selectStudentPageflow/SelectStudentPageflowController.jpf")
    })
    protected Forward gotoSelectStudentPageflow(ScheduleTestForm form)
    {
    	this.addedStudentsCount = 0;
        if (this.selectedStudents != null)
            this.addedStudentsCount = this.selectedStudents.size();
        form.setSelectedStudents(this.selectedStudents);
        form.setIsCopyTest(this.condition.getIsCopyTest());
           
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/selectProctorPageflow/SelectProctorPageflowController.jpf"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "/selectProctorPageflow/SelectProctorPageflowController.jpf")
    })
    protected Forward gotoSelectProctorPageflow(ScheduleTestForm form)
    {
        this.addedProctorsCount = 0;
        if (this.selectedProctors != null)
            this.addedProctorsCount = this.selectedProctors.size();
        form.setSelectedProctors(this.selectedProctors);
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="selectSettings.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "selectSettings.do")
    })
    protected Forward selectProctorPageflowDone(ScheduleTestForm form)
    {
        int afterCount = 0;
        if (form.getSelectedProctors() != null)
            afterCount = form.getSelectedProctors().size();
        this.addedProctorsCount = afterCount - this.addedProctorsCount;
        this.setSelectedProctors(form.getSelectedProctors());
        form.setSelectedProctorIds(null);           
        form.getProctorStatePathList().setPageRequested(new Integer(1));
        
        TestRosterFilter trf = form.getTestRosterFilter();
        trf.copyValues(this.testRosterFilter);
        
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="selectSettings.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "selectSettings.do")
    })
    protected Forward selectStudentDone(ScheduleTestForm form)
    {
        int afterCount = 0;
        if (form.getSelectedStudents() != null)
            afterCount = form.getSelectedStudents().size();
        this.addedStudentsCount = afterCount - this.addedStudentsCount;
        this.setSelectedStudents(form.getSelectedStudents());
        if (form.getFormOperand().equals(TestSession.FormAssignment.MANUAL))
        {
            String defaultForm = "";
            if (this.formList != null && this.formList.length > 0) 
                defaultForm = this.formList[0];
            Iterator it = this.selectedStudents.iterator();
            while (it.hasNext())
            {
                SessionStudent ss = (SessionStudent)it.next();
                if (ss != null && (ss.getItemSetForm() == null || ss.getItemSetForm().trim().equals(""))) 
                    ss.setItemSetForm(defaultForm);
            }
            
        }
        form.setSelectedStudentIds(null);           
        form.getStudentStatePathList().setPageRequested(new Integer(1));
        
        TestRosterFilter trf = form.getTestRosterFilter();
        trf.copyValues(this.testRosterFilter);
        
        return new Forward("success", form);
    }



    /**
     * @jpf:action
     * @jpf:forward name="success" path="selectSettings.do"
     * @jpf:forward name="failure" path="selectTest.do"
     * @jpf:forward name="showRestrictedStudents" path="showRestrictedStudents.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "selectSettings.do"), 
        @Jpf.Forward(name = "failure",
                     path = "selectTest.do"), 
        @Jpf.Forward(name = "showRestrictedStudents",
                     path = "showRestrictedStudents.do")
    })
    protected Forward selectTestDone(ScheduleTestForm form)
    {
        
       
        if (form.getSelectedTestId() == null)
        {
            form.setSelectedTestId(this.currentSelectedTestId);
        }

        // validate subtests        
        Boolean isTabeProduct = TestSessionUtils.isTabeProduct(this.productType);   
        if (isTabeProduct.booleanValue())
        {     
            String autoLocator = form.getAutoLocator();
            boolean autoLocatorChecked = ((autoLocator != null) && autoLocator.equals("true"));
            if (! autoLocatorChecked)
            {                
                boolean valid = TABESubtestValidation.validation(this.defaultSubtests, true);
                if (! valid)
                {
                    String message = TABESubtestValidation.currentMessage; 
                    message += "<br />Click the <b>Modify Test</b> button to change the difficulty level.";               
                    this.getRequest().setAttribute("errorMessage", message); 
                    return new Forward("failure", form);            
                }
            }
        }
                
        
        //validate TACs
        boolean errorFound = false;
        boolean hasBreak = form.getHasBreak().booleanValue();
        StringBuffer alertBuf = new StringBuffer();
        String [] TACs;
        if (hasBreak)
        {
            
            boolean hasAL = ((form.getAutoLocator() != null) && form.getAutoLocator().equals("true"));
            if (hasAL)
                TACs = new String[this.defaultSubtests.size() + 1];
            else
                TACs = new String[this.defaultSubtests.size()];
            
            Iterator it = this.defaultSubtests.iterator();
            int i=0;
            while (it.hasNext())
            {
                SubtestVO subtest =(SubtestVO)it.next();
                subtest.setTestAccessCode(subtest.getTestAccessCode().trim());
                TACs[i++] = subtest.getTestAccessCode();  
            }

            if (hasAL && (this.locatorSubtest != null))
            {                
                TACs[i] = this.locatorSubtest.getTestAccessCode().trim();  
            }            
        }
        else
        {
            TACs = new String[1];
            TACs[0] = form.getTestAdmin().getAccessCode();       
        }

                    
        if (hasEmptyTAC(TACs))
        {
            errorFound = true;
            if (hasBreak)
                alertBuf.append(MessageResourceBundle.getMessage("TAC.MissingTestAccessCodes"));
            else
                alertBuf.append(MessageResourceBundle.getMessage("TAC.MissingTestAccessCode"));
        }
        else if (hasSpecialCharInTAC(TACs))
        {
            errorFound = true;
            alertBuf.append(MessageResourceBundle.getMessage("TAC.SpecialCharNotAllowed"));
        }
        else if (hasInvalidateTACLength(TACs))
        {
            errorFound = true;
            alertBuf.append(MessageResourceBundle.getMessage("TAC.SixChars"));
        }
        else if (hasDuplicateTAC(TACs))
        {
            errorFound = true;
            alertBuf.append(MessageResourceBundle.getMessage("TAC.IdenticalTestAccessCodes"));
        }
        
        if (! errorFound)
        {

            String [] validateResults=null;
            try
            {
                validateResults = this.scheduleTest.validateAccessCodes(this.userName, TACs, this.testAdminId);
            }
            catch (CTBBusinessException e)
            {
                e.printStackTrace();    
            }
            
            if (validateResults != null)
            {
                Vector tacsInuse = new Vector();
                for (int i=0; i < validateResults.length; i++)
                {
/*                    if (validateResults[i]!=null && validateResults[i].indexOf("short") >= 0) {
                        alertFound = true;
                        alertBuf.append(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.SixChars", TACs[i]));
                    }
                    if (validateResults[i]!=null && validateResults[i].indexOf("badchars") >= 0) {
                        alertFound = true;
                        alertBuf.append(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.InvalidChars", TACs[i]));
                    }
*/
                    if (validateResults[i] != null && validateResults[i].indexOf("exists") >= 0)
                    {
                        errorFound = true;
                        tacsInuse.add(TACs[i]);
                    }
                }
                if (tacsInuse.size() > 1)
                {
                    alertBuf.append(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.Header2"));
                    alertBuf.append(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.InUse2", getTACsInString(tacsInuse)));
                }                    
                else
                {
                    alertBuf.append(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.Header"));
                    alertBuf.append(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.InUse", getTACsInString(tacsInuse)));
                }
                
                if (hasBreak)
                    alertBuf.append(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.Footer.WithBreak"));
                else
                    alertBuf.append(MessageResourceBundle.getMessage("TAC.InvalidTestAccessCode.Footer.NoBreak"));
                
            }
        }
        
        if (errorFound)
        {
            this.getRequest().setAttribute("errorMessage", alertBuf.toString()); 
            return new Forward("failure", form);            
        }
        
        if (this.selectedStudents != null && this.selectedStudents.size() > 0)
        {
            SessionStudentData ssd = new SessionStudentData();
            ssd.setBeansWithList(this.selectedStudents, null);
            
            SessionStudentData restrictedSSD = null;
            
            try
            {
            
                restrictedSSD = this.scheduleTest.getRestrictedStudentsForTest(this.userName, ssd.getSessionStudents(), 
                                                new Integer(form.getSelectedTestId()), this.testAdminId,
                                                null, null, null);
            }
            catch (CTBBusinessException e)
            {
                e.printStackTrace();
            }
            
            SessionStudent [] studentNodes = restrictedSSD.getSessionStudents();

            if (studentNodes.length > 0)
            {
                
                this.restrictedSessionStudents = studentNodes;
                return new Forward("showRestrictedStudents", form);            
                
            }
            
        }

        
        if (!form.getSelectedTestId().equals(this.selectedTestId)) 
            this.condition.setSelectTestChanged(Boolean.TRUE);
                        
        this.setSelectedProductName(form.getSelectedProductName());
        this.setSelectedLevel(form.getSelectedLevel());
        this.setSelectedTestId(form.getSelectedTestId()); 
        this.testAccessCode = form.getTestAdmin().getAccessCode();       
        this.condition.setHasBreak(form.getHasBreak());  
        TestVO selectedTest = getTestById(form.getSelectedTestId());
        if (selectedTest != null)
        {
            form.getTestAdmin().setTestName(selectedTest.getTestName());
            form.getTestAdmin().setLevel(selectedTest.getLevel());
            this.overrideFormAssignment = selectedTest.getOverrideFormAssignment();
            this.overrideLoginStartDate = selectedTest.getOverrideLoginStartDate();
            Date loginStartDate = selectedTest.getOverrideLoginStartDate();
            if (! this.condition.getShowCancelOnFirstPage().booleanValue() || this.condition.getSelectTestChanged().booleanValue())
            {
                String timeZone = this.user.getTimeZone();
                Date now = new Date(System.currentTimeMillis());
                Date today = com.ctb.util.DateUtils.getAdjustedDate(now, TimeZone.getDefault().getID(), timeZone, now);
                Date tomorrow = com.ctb.util.DateUtils.getAdjustedDate(new Date(now.getTime() + (24 * 60 * 60 * 1000)), TimeZone.getDefault().getID(), timeZone, now);
                if (form.getStartDate() == null || "".equals(form.getStartDate().trim()))                
                    form.setStartDate(DateUtils.formatDateToDateString(today));
                if (form.getEndDate() == null || "".equals(form.getEndDate().trim()))                
                    form.setEndDate(DateUtils.formatDateToDateString(tomorrow));
                
                if (loginStartDate != null && !DateUtils.isBeforeToday(loginStartDate, timeZone))
                {
                    Date loginEndDate = (Date)loginStartDate.clone();
                    loginEndDate.setDate(loginEndDate.getDate() + 1);
                    form.setStartDate(DateUtils.formatDateToDateString(loginStartDate));
                    form.setEndDate(DateUtils.formatDateToDateString(loginEndDate));
                }                
            }
        }
        else
        {
            form.getTestAdmin().setTestName("");
            form.getTestAdmin().setLevel("");
        }
        
        TestRosterFilter trf = form.getTestRosterFilter();
        trf.copyValues(this.testRosterFilter);
        
        if (this.condition.getSelectTestChanged().booleanValue())
        {
            if (this.scheduledSession != null)
            {
                SessionStudent[] sts = this.scheduledSession.getStudents();
                for (int i=0; i < sts.length; i++)
                {
                    SessionStudent ss = sts[i];
                    ss.setStudentManifests(null);
                }
            }
        }
        
        return new Forward("success", form);
        
    }

    private boolean hasEmptyTAC(String [] TACs)
    {
        boolean found = false;
        for (int i=0; i < TACs.length && !found; i++)
        {
            if ("".equals(TACs[i]))
                found = true;
        }            
        return found;
    }

    
    private boolean hasDuplicateTAC(String [] TACs)
    {
        boolean found = false;
        if (TACs.length <= 1)
            return false;
        for (int i=0; i < TACs.length && !found; i++)
        {
            for (int j=i + 1; j < TACs.length && !found; j++)
            {
                if (TACs[i] != null && TACs[i].equalsIgnoreCase((TACs[j])))
                    found = true;
            }     
        }            
        return found;
    }
    
    private boolean hasInvalidateTACLength(String [] TACs)
    {
        boolean found = false;
        for (int i=0; i < TACs.length && !found; i++)
        {
            if (TACs[i] != null && TACs[i].length() < 6)
                found = true;
        }            
        return found;
    }
    

    /**
     * @jpf:action
     * @jpf:forward name="success" path="selectSettings.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "selectSettings.do")
    })
    protected Forward selectTestCancel(ScheduleTestForm form)
    {
        form.setSelectedProductName(this.getSelectedProductName());
        form.setSelectedLevel(this.getSelectedLevel());
        form.setSelectedTestId(this.getSelectedTestId()); 
        form.getTestAdmin().setAccessCode(this.testAccessCode);    
        // START- Added for defect #65862
        this.showLevelOrGrade = this.stateScheduler.getShowLevelOrGrade(); 
        this.gradeFlag = this.stateScheduler.isGradeFlag();
        // END- Added for defect #65862     
        form.setHasBreak(this.condition.getHasBreak());   
        this.defaultSubtests = this.stateScheduler.getSubtests();
        this.testList = this.stateScheduler.getTestList();
        if (this.locatorSubtest != null)        
            this.locatorSubtest.setTestAccessCode(this.stateScheduler.getLocatorTAC());

        TestRosterFilter trf = form.getTestRosterFilter();
        trf.copyValues(this.testRosterFilter);
        
        this.productType = this.stateScheduler.getProductType();
        form.getTestAdmin().setTestName(this.stateScheduler.getTestName());
        form.setAutoLocator(this.stateScheduler.getAutoLocator());
        
        // Changed for Randomized Distractor
        form.getTestAdmin().setIsRandomize(this.stateScheduler.getIsRandomize());
        //Added for license which is in use during select test cancel
        form.getTestAdmin().setProductId(this.stateScheduler.getProductId());
        
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="selectTest.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "selectTest.do")
		}
	)
    protected Forward goToSelectTest(ScheduleTestForm form)
    {
        // save in case of cancel
        this.setSelectedProductName(form.getSelectedProductName());
        this.setSelectedLevel(form.getSelectedLevel());
        this.setSelectedTestId(form.getSelectedTestId()); 
        this.testAccessCode = form.getTestAdmin().getAccessCode(); 
        // START- Added for defect #65862
        if(this.showLevelOrGrade != null){
        	this.stateScheduler.setShowLevelOrGrade(this.showLevelOrGrade);
        	this.stateScheduler.setGradeFlag(this.gradeFlag);
        }
        // END- Added for defect #65862
        if (this.locatorSubtest != null)        
            this.stateScheduler.setLocatorTAC(this.locatorSubtest.getTestAccessCode());
        
        this.condition.setHasBreak(form.getHasBreak());  
        this.stateScheduler.setSubtests(TestSessionUtils.cloneSubtests(this.defaultSubtests));
        
        List copyOfTestList = null;
        if (this.testList != null) {
            copyOfTestList = new ArrayList();
            Iterator it = this.testList.iterator();
            while (it.hasNext()) {
                copyOfTestList.add(new TestVO((TestVO)it.next()));  
            }
        }
        this.stateScheduler.setTestList(copyOfTestList);
            
        this.condition.setIsSearchTestList(Boolean.TRUE);
        this.currentSelectedTestId = form.getSelectedTestId();
        
        this.stateScheduler.setProductType(this.productType);
        this.stateScheduler.setTestName(form.getTestAdmin().getTestName());
        this.stateScheduler.setAutoLocator(form.getAutoLocator());
        
        //Added for license which is in use during select test cancel
        this.stateScheduler.setProductId(form.getTestAdmin().getProductId());
        
		//Chage For Randomization Distractor after Clicking Change buttom
        //value retrive from test_admin
        boolean hasTestAdminSession =  form.getTestAdmin().getIsRandomize()!= null && 
                !form.getTestAdmin().getIsRandomize().trim().equals("") ? 
                    true : false;
        form.setHasSessionRandomizedDistractor(hasTestAdminSession);
        if (!hasTestAdminSession) {
            
            form.getTestAdmin().setIsRandomize(null);
            
        }
        
        this.stateScheduler.setIsRandomize(form.getTestAdmin().getIsRandomize());
        //check for random distractor special scnario
        form.setProductChanged(false);
        
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="goToHomePage.do"
     * @jpf:forward name="failure" path="selectSettings.do"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "goToHomePage.do"), 
			@Jpf.Forward(name = "failure", path = "selectSettings.do")
		}
	)
    protected Forward deleteTest(ScheduleTestForm form)
    {
        try {
            this.scheduleTest.deleteTestSession(this.userName, this.testAdminId);
        }
        catch (CTBBusinessException e) {
            e.printStackTrace();
            this.getRequest().setAttribute("errorMessage",MessageResourceBundle.getMessage("SelectSettings.FailedToDeleteTestSession", e.getMessage()));
            return new Forward("failure", form);
                
        }
        return new Forward("success");
    }


    /**
     * @jpf:action
     */
	@Jpf.Action()
    protected Forward goToEditTest(ScheduleTestForm form)
    {
        try {
            getResponse().sendRedirect("/TestAdministrationWeb/scheduleTestPageflow/selectSettings.do?action=edit&testAdminId="+this.testAdminId);
        } catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }
        
        return null;
    }
    
    /**
     * @jpf:action
     */
	@Jpf.Action()
    protected Forward goToCopyTest(ScheduleTestForm form)
    {
        try {
            getResponse().sendRedirect("/TestAdministrationWeb/scheduleTestPageflow/selectSettings.do?action=copy&testAdminId="+this.testAdminId);
        } catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }
        
        return null;
    }
    

    /**
     * @jpf:action
     */
	@Jpf.Action()
    protected Forward goToHomePage(ScheduleTestForm form)
    {
        try {
            getResponse().sendRedirect("/TestSessionInfoWeb/homepage/HomePageController.jpf");
        } catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }
        
        return null;
    }

    /**
     * @jpf:action
     */
	@Jpf.Action()
    protected Forward gotoViewReport()
    {
        try {
            String contextPath = "/iknow/reportWizard/chooseReportType";
            String rptTestAdmin = "rptTestAdmin=" + this.testAdminId.toString();
            String chooseReportTab = "chooseReportTab=IKNOW";
            String url = contextPath + "?" + rptTestAdmin + "&" + chooseReportTab;            
            getResponse().sendRedirect(url);
        } 
        catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }

    /**
     * @jpf:action
     */
	@Jpf.Action()
    protected Forward gotoViewStatus()
    {
        try {
            String contextPath = "/TestSessionInfoWeb/viewmonitorstatus/ViewMonitorStatusController.jpf";
            String sessionId = "sessionId=" + this.testAdminId.toString();
            String url = contextPath + "?" + sessionId;            
            getResponse().sendRedirect(url);
            
        } catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }        
        return null;
    }
    
    


    public static class TestNotFoundException extends CTBBusinessException
    {
        public TestNotFoundException(String msg) {
            super(msg);
        }
    }

    
    private String getMessageResourceBundle(CTBBusinessException e, String msgId) 
    {
        String errorMessage = "";
        if (e instanceof ValidationException) {
            String msgException = e.getMessage();
            if (msgException != null) {
                if (msgException.equals("SelectSettings.TestSessionName.InvalidCharacters") ||
                    msgException.equals("SelectSettings.TestLocation.InvalidCharacters")) {
                    errorMessage = MessageResourceBundle.getMessage(msgException);
                }
                else {
                    msgId += ".ValidationException";
                    errorMessage = MessageResourceBundle.getMessage(msgId);
                }            
            }
            else {
                msgId += ".ValidationException";
                errorMessage = MessageResourceBundle.getMessage(msgId);
            }
        }
        else {
            errorMessage = MessageResourceBundle.getMessage(msgId, e.getMessage());
        }
            
        return errorMessage; 
    }
    
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ScheduleTestForm extends SanitizedFormData
    {
        private TestAdminVO testAdmin;
        private String endTime="05:00 PM";
        private String startTime="08:00 AM";
        private String endDate;
        private String startDate;
        private String creatorOrgNodeName;
        private String action=ACTION_SCHEDULE_TEST;
        private Integer creatorOrgNodeId;
        private TestRosterFilter testRosterFilter;
        private Boolean filterVisible = Boolean.FALSE;
        private String[] selectedStudentOrgList;
        private List selectedProctors;
        private Integer selectedProctorCount;
        private Integer[] selectedProctorIds;
        private Integer selectedStudentWithAccommodationCount;
        private Integer selectedStudentCount;
        private String currentAction;
        private Boolean showAccommodations;
        private Integer[] selectedStudentIds;
        private Integer selectedStudentId;
        private String selectedOrgNodeName;
        private Integer selectedOrgNodeId;
        private String actionElement;
        private String orgNodeName=PathListUtils.TOP;
        private StatePathList orgStatePathList;        
        private StatePathList proctorStatePathList;        
        private StatePathList studentStatePathList;        
        private StatePathList testStatePathList;        
        private String orgNodePath=PathListUtils.TOP;
        private Integer orgNodeId = new Integer(0);
        private String selectedOrg;
        private String formAssigned;
        private String formOperand = TestSession.FormAssignment.ROUND_ROBIN;
        private String accommodationOperand;        
        private List gradeList = null;    
        private List selectedStudents;
        private String selectedOrganization;
        private String[] selectedAccommodations;
        private String selectedAccommodationElements;
        private String selectedGrade;
        
        private Boolean hasBreak = Boolean.FALSE;
        private String selectedLevel = FilterSortPageUtils.FILTERTYPE_SHOWALL;
        private String selectedProductName;        
        private String selectedTestId;       
        private Boolean isCopyTest = Boolean.FALSE;        
        private String autoLocator;
        private String subtestValidationMessage;
        //Change for Random destructor
        private boolean hasSessionRandomizedDistractor = true;
        //change for random distractor special senario
        private boolean isProductChanged = true;
       
        //Added for License
        private Integer licenseAvailable = null;
        private String licenseModel = null;    
        private String licensePercentage = null;
		
       
        // messages
        private Message message;
        
        public void init() 
        {
            action=ACTION_SCHEDULE_TEST;
            testRosterFilter = new TestRosterFilter();
            filterVisible = Boolean.FALSE;
            orgNodeName=PathListUtils.TOP;
            orgNodePath=PathListUtils.TOP;
            orgNodeId = new Integer(0);
            formOperand = TestSession.FormAssignment.ROUND_ROBIN;
            endTime="05:00 PM";
            startTime="08:00 AM";
            hasBreak=Boolean.FALSE;
            selectedLevel = FilterSortPageUtils.FILTERTYPE_SHOWALL;
            autoLocator = "true";
            subtestValidationMessage = null;
            
            this.orgStatePathList = new StatePathList(ColumnSortEntry.ASCENDING, FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN);
            this.studentStatePathList = new StatePathList(ColumnSortEntry.ASCENDING, FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN);
            this.proctorStatePathList = new StatePathList(ColumnSortEntry.ASCENDING, FilterSortPageUtils.PROCTOR_DEFAULT_SORT_COLUMN);
            this.testStatePathList = new StatePathList(ColumnSortEntry.ASCENDING, FilterSortPageUtils.TEST_DEFAULT_SORT_COLUMN);
            
            this.testAdmin = new TestAdminVO();
        }

        public void validateValues() 
        {
            this.getOrgStatePathList().validateValues(ColumnSortEntry.ASCENDING, FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN);
            this.getStudentStatePathList().validateValues(ColumnSortEntry.ASCENDING, FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN);            
            this.getProctorStatePathList().validateValues(ColumnSortEntry.ASCENDING, FilterSortPageUtils.PROCTOR_DEFAULT_SORT_COLUMN);
            this.getTestStatePathList().validateValues(ColumnSortEntry.ASCENDING, FilterSortPageUtils.TEST_DEFAULT_SORT_COLUMN);
                
            if (this.orgNodeName == null)
                this.orgNodeName= PathListUtils.TOP;
                
            if (this.orgNodePath == null)
                this.orgNodePath= PathListUtils.TOP;

        }

        public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
        {
            ActionErrors errs = new ActionErrors();
			
			String path = mapping.getPath();
            boolean doTestValidation = false;
            
            if ((path.indexOf("returnFromEditSubtestLevels") > 0) ||
                (path.indexOf("saveTest") > 0) ||
                (path.indexOf("endTestSession") > 0) ||
                (path.indexOf("goToPrintOptions") > 0)) {
                doTestValidation = true;
            }
            
            if (! doTestValidation) {
                errs = super.validate(mapping, request);
                return errs;
            }
            
            String sessionName = this.getTestAdmin().getSessionName();
            String location = this.getTestAdmin().getLocation();
            
            if (WebUtils.invalidSessionName(sessionName) || WebUtils.invalidSessionName(location)) {
                return errs; // do not let super class throw exception, return error message instead
            }

            errs = super.validate(mapping, request);
            
            String validateTest = (String)request.getParameter("validateTest");
            if ((validateTest != null) && validateTest.equals("noValidateTest")) {
                return errs;
            }

            ScheduleTestController controller = (ScheduleTestController) PageFlowUtils.getCurrentPageFlow(request); 
                        
            if ( (sessionName != null) && sessionName.equals("") )
            {
                errs.add( "Save Test", new ActionError( "SaveTest.TestSessionNameRequired" ) );
            }
            
            boolean startDateError = false;
            boolean endDateError = false;
            
            int validateResult = DateUtils.validateDateString(this.startDate);
            if (validateResult != DateUtils.DATE_VALID)
                startDateError = true;
            validateResult = DateUtils.validateDateString(this.endDate);
            if (validateResult != DateUtils.DATE_VALID)
                endDateError= true;

            if (startDateError || endDateError)
                errs.add( "Save Test", new ActionError( "SaveTest.InvalidDate" ) );

            Date dateStarted = DateUtils.getDateFromDateString(this.startDate);
            Date dateEnded = DateUtils.getDateFromDateString(this.endDate);
        
            String strDateTime = "";
            if (this.endDate != null && this.endTime != null)
                strDateTime = this.endDate + " " + this.endTime;
                
            Date datetimeEnded = DateUtils.getDateFromDateTimeString(strDateTime);
            String timeZone = DateUtils.getDBTimeZone(this.getTestAdmin().getTimeZone());


            Date overrideLoginStartDate = controller.overrideLoginStartDate;
            if (overrideLoginStartDate != null && !startDateError) {
                if (dateStarted.compareTo(overrideLoginStartDate) < 0) {
                    startDateError = true;
                    errs.add( "Save Test", new ActionError( "SaveTest.StartDateBeforeOverrideStartDate", DateUtils.formatDateToDateString(overrideLoginStartDate)) );
                }
            }

            if (this.action.equals(ACTION_SCHEDULE_TEST)) {
                if (!startDateError && dateStarted != null && DateUtils.isBeforeToday(dateStarted, timeZone)) {
                    startDateError = true;
                    errs.add( "Save Test", new ActionError( "SaveTest.StartDateBeforeToday" ) );
                }
                if (!endDateError && datetimeEnded != null && DateUtils.isBeforeNow(datetimeEnded, timeZone)) {
                    endDateError = true;
                    errs.add( "Save Test", new ActionError( "SaveTest.EndDateTimeBeforeNow" ) );
                }
            }
            
            
            Date timeStarted = DateUtils.getDateFromTimeString(this.startTime);
            Date timeEnded = DateUtils.getDateFromTimeString(this.endTime);
            if (timeStarted != null && timeEnded != null && timeStarted.compareTo(timeEnded)>=0) 
            {
                errs.add( "Save Test", new ActionError( "SaveTest.EndTimeBeforeStartTime" ) );
            }

            if (!startDateError && !endDateError && dateStarted != null && dateEnded != null && dateStarted.compareTo(dateEnded)>0) 
            {
                errs.add( "Save Test", new ActionError("SaveTest.EndDateBeforeStartDate") );
            }
            
            if (!errs.isEmpty()) {
                request.setAttribute("hasAlert", Boolean.TRUE);
            }
            return errs;
        }
        
        
        public void resetStudentSortPage() 
        {
            this.getStudentStatePathList().resetValues(FilterSortPageUtils.ASCENDING, FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN);
        }
        
        public void resetValuesForAction(String actionElement) 
        {
            if (actionElement == null)
                return;
                
            if (actionElement.equals("{actionForm.orgStatePathList.sortOrderBy}")) {
                this.getOrgStatePathList().setPageRequested(new Integer(1));                  
            }
            if (actionElement.equals("{actionForm.studentStatePathList.sortOrderBy}")) {
                this.getStudentStatePathList().setPageRequested(new Integer(1));                  
            }
            if (actionElement.equals("{actionForm.proctorStatePathList.sortOrderBy}")) {
                this.getProctorStatePathList().setPageRequested(new Integer(1));                  
            }
            if (actionElement.equals("{actionForm.orgStatePathList.pageRequested}")) {
                this.getStudentStatePathList().setPageRequested(new Integer(1));                  
                this.getProctorStatePathList().setPageRequested(new Integer(1));                  
            }
            
            if (actionElement.equals("{actionForm.orgNodeId}")) {
                this.getOrgStatePathList().resetValues(FilterSortPageUtils.ASCENDING, FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN);
                this.getStudentStatePathList().resetValues(FilterSortPageUtils.ASCENDING, FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN);
                this.getProctorStatePathList().resetValues(FilterSortPageUtils.ASCENDING, FilterSortPageUtils.PROCTOR_DEFAULT_SORT_COLUMN);
            }
            if (actionElement.equals("newNodeSelected")) {
                this.getStudentStatePathList().resetValues(FilterSortPageUtils.ASCENDING, FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN);
                this.getProctorStatePathList().resetValues(FilterSortPageUtils.ASCENDING, FilterSortPageUtils.PROCTOR_DEFAULT_SORT_COLUMN);
            }
            if (actionElement.equals("selectStudentDone") || 
                actionElement.equals("selectStudentCancel") ||
                actionElement.equals("backToSelectSettings")) {   
                this.getOrgStatePathList().resetValues(FilterSortPageUtils.ASCENDING, FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN);
                this.getStudentStatePathList().resetValues(FilterSortPageUtils.ASCENDING, FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN);
            }
            if (actionElement.equals("selectProctorDone")) {
                this.getOrgStatePathList().resetValues(FilterSortPageUtils.ASCENDING, FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN);
                this.getProctorStatePathList().resetValues(FilterSortPageUtils.ASCENDING, FilterSortPageUtils.PROCTOR_DEFAULT_SORT_COLUMN);
            }
        }       

        public void resetValuesForPrintOptionsAction(String actionElement) 
        {
            if (actionElement == null)
                return;
                
            if (actionElement.equals("{actionForm.orgStatePathList.sortOrderBy}")) {
                this.getOrgStatePathList().setPageRequested(new Integer(1));                  
            }
            if (actionElement.equals("{actionForm.studentStatePathList.sortOrderBy}")) {
                this.getStudentStatePathList().setPageRequested(new Integer(1));                  
            }
            if (actionElement.equals("{actionForm.orgStatePathList.pageRequested}")) {
                this.getStudentStatePathList().setPageRequested(new Integer(1));                  
                this.getProctorStatePathList().setPageRequested(new Integer(1));                  
            }
            
            if (actionElement.equals("{actionForm.orgNodeId}")) {
                this.getOrgStatePathList().resetValues(FilterSortPageUtils.ASCENDING, FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN);
            }
        }       
 
        public void setSelectedProductName(String selectedProductName)
        {   
            if (! isSanitized(selectedProductName)) {
                selectedProductName = JavaScriptSanitizer.sanitizeString( selectedProductName );
            }
            this.selectedProductName = selectedProductName;
        }
        
        //Change for Random Distractor
        
        /**
         * @return the hasSessionRandomizedDistractor
         */
        public boolean getHasSessionRandomizedDistractor() {
            return hasSessionRandomizedDistractor;
        }

        /**
         * @param hasSessionRandomizedDistractor the hasSessionRandomizedDistractor to set
         */
        public void setHasSessionRandomizedDistractor(
                boolean hasSessionRandomizedDistractor) {
            this.hasSessionRandomizedDistractor = hasSessionRandomizedDistractor;
        }
		//change for random distractor special senario
        
        /**
         * @return the isProductChanged
         */
        public boolean isProductChanged() {
            return isProductChanged;
        }

        /**
         * @param isProductChanged the isProductChanged to set
         */
        public void setProductChanged(boolean isProductChanged) {
            this.isProductChanged = isProductChanged;
        }

        public String getSelectedProductName() {   
            
            return this.selectedProductName;
        }

        public void setSelectedLevel(String selectedLevel)
        {   
            this.selectedLevel = selectedLevel;
        }

        public String getSelectedLevel()
        {
            return JavaScriptSanitizer.sanitizeInput(this.selectedLevel);
        }

        public void setHasBreak(Boolean hasBreak)
        {
            this.hasBreak = hasBreak;
        }

        public Boolean getHasBreak()
        {
            return this.hasBreak;
        }
        public void setStartDate(String startDate)
        {
            this.startDate = startDate;
        }

        public String getStartDate()
        {
            return JavaScriptSanitizer.sanitizeInput(this.startDate);
        }

        public void setEndDate(String endDate)
        {
            this.endDate = endDate;
        }

        public String getEndDate()
        {
            return JavaScriptSanitizer.sanitizeInput(this.endDate);
        }

        public void setStartTime(String startTime)
        {
            this.startTime = startTime;
        }

        public String getStartTime()
        {
            return JavaScriptSanitizer.sanitizeInput(this.startTime);
        }

        public void setEndTime(String endTime)
        {
            this.endTime = endTime;
        }

        public String getEndTime()
        {
            return JavaScriptSanitizer.sanitizeInput(this.endTime);
        }

        public void setSelectedGrade(String selectedGrade)
        {
            this.selectedGrade = selectedGrade;
        }

        public String getSelectedGrade()
        {
            return JavaScriptSanitizer.sanitizeInput(this.selectedGrade);
        }

        public void setSelectedAccommodations(String[] selectedAccommodations)
        {
            this.selectedAccommodations = selectedAccommodations;
        }

        public String[] getSelectedAccommodations()
        {
            return this.selectedAccommodations;
        }

        public void setSelectedAccommodationElements(String selectedAccommodationElements)
        {
            this.selectedAccommodationElements = selectedAccommodationElements;
        }

        public String getSelectedAccommodationElements()
        {
            return JavaScriptSanitizer.sanitizeInput(this.selectedAccommodationElements);
        }

        public void setSelectedOrganization(String selectedOrganization)
        {
            this.selectedOrganization = selectedOrganization;
        }

        public String getSelectedOrganization()
        {
            return JavaScriptSanitizer.sanitizeInput(this.selectedOrganization);
        }

        public void setSelectedStudents(List selectedStudents)
        {
            this.selectedStudents = selectedStudents;
        }

        public List getSelectedStudents()
        {
            return this.selectedStudents;
        }

        public void setAccommodationOperand(String accommodationOperand)
        {
            this.accommodationOperand = accommodationOperand;
        }

        public String getAccommodationOperand()
        {
            return JavaScriptSanitizer.sanitizeInput(this.accommodationOperand);
        }

        public void setFormOperand(String formOperand)
        {
            this.formOperand = formOperand;
        }

        public String getFormOperand()
        {
            return JavaScriptSanitizer.sanitizeInput(this.formOperand);
        }

        public void setFormAssigned(String formAssigned)
        {
            this.formAssigned = formAssigned;
        }

        public String getFormAssigned()
        {
            return JavaScriptSanitizer.sanitizeInput(this.formAssigned);
        }

        public void setSelectedOrg(String selectedOrg)
        {
            this.selectedOrg = selectedOrg;
        }

        public String getSelectedOrg()
        {
            return JavaScriptSanitizer.sanitizeInput(this.selectedOrg);
        }

        public void setOrgNodeId(Integer orgNodeId)
        {
            this.orgNodeId = orgNodeId;
        }

        public Integer getOrgNodeId()
        {
            return this.orgNodeId;
        }

        public void setOrgNodePath(String orgNodePath)
        {
            this.orgNodePath = orgNodePath;
        }

        public String getOrgNodePath()
        {
            return JavaScriptSanitizer.sanitizeInput(this.orgNodePath);
        }


        public void setSelectedTestId(String selectedTestId)
        {
            this.selectedTestId = selectedTestId;
        }
    
        public String getSelectedTestId()
        {
            return JavaScriptSanitizer.sanitizeInput(this.selectedTestId);
        }
        

        public void setOrgNodeName(String orgNodeName)
        {
            this.orgNodeName = orgNodeName;
        }

        public String getOrgNodeName()
        {
            return JavaScriptSanitizer.sanitizeInput(this.orgNodeName);
        }

        public void setActionElement(String actionElement)
        {
            this.actionElement = actionElement;
        }

        public String getActionElement()
        {
            return JavaScriptSanitizer.sanitizeInput(this.actionElement);
        }

        public void setSelectedOrgNodeId(Integer selectedOrgNodeId)
        {
            this.selectedOrgNodeId = selectedOrgNodeId;
        }

        public Integer getSelectedOrgNodeId()
        {
            return this.selectedOrgNodeId;
        }

        public void setSelectedOrgNodeName(String selectedOrgNodeName)
        {
            this.selectedOrgNodeName = selectedOrgNodeName;
        }

        public String getSelectedOrgNodeName()
        {
            return JavaScriptSanitizer.sanitizeInput(this.selectedOrgNodeName);
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

        public void setSelectedStudentId(Integer selectedStudentId)
        {
            this.selectedStudentId = selectedStudentId;
        }

        public Integer getSelectedStudentId()
        {
            return this.selectedStudentId;
        }

        public void setShowAccommodations(Boolean showAccommodations)
        {
            this.showAccommodations = showAccommodations;
        }

        public Boolean getShowAccommodations()
        {
            return this.showAccommodations;
        }

        public void setCurrentAction(String currentAction)
        {
            this.currentAction = currentAction;
        }

        public String getCurrentAction()
        {
            return this.currentAction;
        }

        public void setSelectedStudentCount(Integer selectedStudentCount)
        {
            this.selectedStudentCount = selectedStudentCount;
        }

        public Integer getSelectedStudentCount()
        {
            return this.selectedStudentCount;
        }

        public void setSelectedStudentWithAccommodationCount(Integer selectedStudentWithAccommodationCount)
        {
            this.selectedStudentWithAccommodationCount = selectedStudentWithAccommodationCount;
        }

        public Integer getSelectedStudentWithAccommodationCount()
        {
            return this.selectedStudentWithAccommodationCount;
        }

        public void setSelectedProctorIds(Integer[] selectedProctorIds)
        {
            this.selectedProctorIds = selectedProctorIds;
        }

        public Integer[] getSelectedProctorIds()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if(this.selectedProctorIds == null || this.selectedProctorIds.length == 0)
            {
                this.selectedProctorIds = new Integer[1];
            }

            return this.selectedProctorIds;
        }

        public void setSelectedProctorCount(Integer selectedProctorCount)
        {
            this.selectedProctorCount = selectedProctorCount;
        }

        public Integer getSelectedProctorCount()
        {
            return this.selectedProctorCount;
        }

        public void setSelectedProctors(List selectedProctors)
        {
            this.selectedProctors = selectedProctors;
        }

        public List getSelectedProctors()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if (this.selectedProctors == null) {
                this.selectedProctors = new ArrayList();
            }
            return this.selectedProctors;
        }

        public void setSelectedStudentOrgList(String[] selectedStudentOrgList)
        {
            this.selectedStudentOrgList = selectedStudentOrgList;
        }

        public String[] getSelectedStudentOrgList()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if (this.selectedStudentOrgList == null || this.selectedStudentOrgList.length == 0) {
                this.selectedStudentOrgList = new String[1];
            }
            return this.selectedStudentOrgList;
        }

        public void setFilterVisible(Boolean filterVisible)
        {
            this.filterVisible = filterVisible;
        }

        public Boolean getFilterVisible()
        {
            return this.filterVisible;
        }

        public void setTestRosterFilter(TestRosterFilter testRosterFilter)
        {
            this.testRosterFilter = testRosterFilter;
        }

        public TestRosterFilter getTestRosterFilter()
        {
            if(this.testRosterFilter == null)
            {
                this.testRosterFilter = new TestRosterFilter();
            }

            return this.testRosterFilter;
        }

        public void setCreatorOrgNodeId(Integer creatorOrgNodeId)
        {
            this.creatorOrgNodeId = creatorOrgNodeId;
        }

        public Integer getCreatorOrgNodeId()
        {
            return this.creatorOrgNodeId;
        }
        
        public void setIsCopyTest(Boolean isCopyTest)
        {
            this.isCopyTest = isCopyTest;
        }

        public Boolean getIsCopyTest()
        {
            return this.isCopyTest;
        }        


        public void setAction(String action)
        {
            this.action = action;
        }

        public String getAction()
        {
            return this.action;
        }
        

        public void setCreatorOrgNodeName(String creatorOrgNodeName)
        {
            this.creatorOrgNodeName = creatorOrgNodeName;
        }

        public String getCreatorOrgNodeName()
        {
            return this.creatorOrgNodeName;
        }
        
        public void setAutoLocator(String autoLocator)
        {
            this.autoLocator = autoLocator;
        }
        public String getAutoLocator()
        {
            return this.autoLocator;
        }
        public String getAutoLocatorDisplay()
        {
            if ((this.autoLocator != null) && this.autoLocator.equals("true"))
                return "Yes";
            else
                return "No";
        }

        public void setSubtestValidationMessage(String message)
        {
            this.subtestValidationMessage = message;
        }
        public String getSubtestValidationMessage()
        {
            return this.subtestValidationMessage;
        }

        private boolean isSanitized(String content)
        {
            return (content.indexOf("&amp;") > 0);
        }
        
        //Added for License LM10
        
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
        public StatePathList getOrgStatePathList()
        {
            if (this.orgStatePathList == null) 
                this.orgStatePathList = new StatePathList(ColumnSortEntry.ASCENDING, FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN);
            return this.orgStatePathList;
        }
        public StatePathList getProctorStatePathList()
        {
            if (this.proctorStatePathList == null) 
                this.proctorStatePathList = new StatePathList(ColumnSortEntry.ASCENDING, FilterSortPageUtils.PROCTOR_DEFAULT_SORT_COLUMN);
            return this.proctorStatePathList;
        }
        public StatePathList getStudentStatePathList()
        {
            if (this.studentStatePathList == null) 
                this.studentStatePathList = new StatePathList(ColumnSortEntry.ASCENDING, FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN);
            return this.studentStatePathList;
        }
        public StatePathList getTestStatePathList()
        {
            if (this.testStatePathList == null) 
                this.testStatePathList = new StatePathList(ColumnSortEntry.ASCENDING, FilterSortPageUtils.TEST_DEFAULT_SORT_COLUMN);
            return this.testStatePathList;
        }
        
        public TestAdminVO getTestAdmin()
        {
            if (this.testAdmin == null)
                this.testAdmin = new TestAdminVO();
            return this.testAdmin;
        }
        
       /**
         * @return the licenseAvailable
         */
        public Integer getLicenseAvailable() {
            return licenseAvailable;
        }
        /**
         * @param licenseAvailable the licenseAvailable to set
         */
        public void setLicenseAvailable(Integer licenseAvailable) {
            this.licenseAvailable = licenseAvailable;
        }
        /**
         * @return the licensePercentage
         */
        public String getLicensePercentage() {
            return licensePercentage;
        }
        /**
         * @param licensePercentage the licensePercentage to set
         */
        public void setLicensePercentage(String licensePercentage) {
            this.licensePercentage = licensePercentage;
        }
        /**
         * @return the licenseModel
         */
        public String getLicenseModel() {
        	if (this.licenseModel != null) {
	        	if (this.licenseModel.equals("T"))
	        		return "Subtest";
	        	else
	        		return "Session";
        	}
        	return null; 
        }
        /**
         * @param licenseModel the licenseModel to set
         */
        public void setLicenseModel(String licenseModel) {
            this.licenseModel = licenseModel;
        }
		
    }

////////////////////////////////////////////////////////////////////////////////////////////////
//// END OF ScheduleTestForm
////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////
////   METHODS FOR ScheduleTest
////////////////////////////////////////////////////////////////////////////////////////////////

    public List getProductNameList() 
    {
        return this.productNameList;
    }
    

    public void setProductNameList(List productNameList)
    {
        this.productNameList = productNameList;
    }


    public List getLevelList()
    {
        return levelList;
    }

    public void setLevelList(List levelList)
    {
        this.levelList = levelList;
    }
    
     public List getGradeList()
    {
        return gradeList;
    }

    public void setGradeList(List gradeList)
    {
        this.gradeList = gradeList;
    }
   
    
    public List getTestList()
    {
        return testList;
    }

    public void setTestList(List testList)
    {
        this.testList = testList;
    }
    
	//For LM10 : Returns selected subtests within a test.
    public List getSelectedSubtests() {
        return this.selectedSubtests;    
    }
    
    //Change for LM10
    public ScheduledSession getScheduledSession() {
        
        return this.scheduledSession;
    }
    public void setStudentNodes(List studentNodes)
    {
        this.studentNodes = studentNodes;
    }

    public List getStudentNodes()
    {
        // For data binding to be able to post data back, complex types and
        // arrays must be initialized to be non-null.
        if(this.studentNodes == null)
        {
            this.studentNodes = new ArrayList();
        }

        return this.studentNodes;
    }
    

    public void setSelectedProductName(String selectedProductName)
    {
        this.selectedProductName = selectedProductName;
    }

    public String getSelectedProductName()
    {
        return this.selectedProductName;
    }

    public void setSelectedLevel(String selectedLevel)
    {
        this.selectedLevel = selectedLevel;
    }

    public String getSelectedLevel()
    {
        return this.selectedLevel;
    }
    
    public void setTimeZoneList(List timeZoneList)
    {
        this.timeZoneList = timeZoneList;
    }

    public List getTimeZoneList()
    {
        return this.timeZoneList;
    }
    
    public void setTimeList(List timeList)
    {
        this.timeList = timeList;
    }

    public List getTimeList()
    {
        return this.timeList;
    }
    
    public void setFormList(String [] formList)
    {
        this.formList = formList;
    }

    public String [] getFormList()
    {
        return this.formList;
    }

    public void setSelectedTestId(String selectedTestId)
    {
        this.selectedTestId = selectedTestId;
    }

    public String getSelectedTestId()
    {
        return this.selectedTestId;
    }
    
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return this.userName;
    }


    public void setSelectedStudents(List selectedStudents)
    {
        this.selectedStudents = selectedStudents;
    }

    public List getSelectedStudents()
    {
        return this.selectedStudents;
    }

    public void setSelectedProctors(List selectedProctors)
    {
        this.selectedProctors = selectedProctors;
    }

    public List getSelectedProctors()
    {
        return this.selectedProctors;
    }

    public Integer getTestAdminId() {
        return this.testAdminId;
    }

    public void setTestAdminId(Integer testAdminId) {
        this.testAdminId=testAdminId;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getAction()
    {
        return this.action;
    }

    public void setTestSessionId(String testSessionId)
    {
        this.testSessionId = testSessionId;
    }

    public String getTestSessionId()
    {
        return this.testSessionId;
    }
 
    public void setTopNodesMap(HashMap topNodesMap)
    {
        this.topNodesMap = topNodesMap;
    }

    public HashMap getTopNodesMap()
    {
        return this.topNodesMap;
    }
    

    public void setShowLevelOrGrade(String showLevelOrGrade)
    {
        this.showLevelOrGrade = showLevelOrGrade;
    }

    public String getShowLevelOrGrade()
    {
        return this.showLevelOrGrade;
    }
    
    public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	////////////////////////////////////////////////////////////////////////////////////////
    /*
    * copySubtestForms
    */
    private void copySubtestForms(List subtests) 
    {
        TestElement[] tes = this.scheduledSession.getScheduledUnits();

        for (int i=0 ; i<subtests.size() ; i++) {
            SubtestVO subtest = (SubtestVO)subtests.get(i);
            subtest.setLevel("E");
            for (int j=0 ; j<tes.length ; j++) {
                TestElement te = tes[j];            
                if (subtest.getId().intValue() == te.getItemSetId().intValue()) {
                    subtest.setLevel(te.getItemSetForm());
                }
            }
        }
    } 

    /*
    * resetTACs
    */
    private void resetTACs(String testId) 
    {
        TestVO selectedTest = getTestById(testId);
        if (selectedTest != null) {
            List subtestList = selectedTest.getSubtests();
            for (int i=0 ; i<subtestList.size() ; i++) {
                SubtestVO original = (SubtestVO)subtestList.get(i);
                for (int j=0 ; j<this.defaultSubtests.size() ; j++) {
                    SubtestVO subtest = (SubtestVO)this.defaultSubtests.get(j);
                    if (subtest.getId().intValue() == original.getId().intValue()) {
                        subtest.setTestAccessCode(original.getTestAccessCode());
                        break;
                    }
                }
            }        
        }
    } 

    //todo: rewrite to put in a Hashtable
    private TestVO getTestById(String testId) 
    {
        if (this.testList == null)
            return null;
        Iterator it = this.testList.iterator();
        while (it.hasNext())
        {
            TestVO testVO= (TestVO)it.next();
            if (testVO.getId().equals(new Integer(testId)))
                return testVO;
        }
        return null;
    } 
    
    //todo: rewrite to put in a Hashtable
    private TestVO getTestTicketTestById(Integer testId) 
    {
        if (this.testTicketTestList == null)
            return null;
        Iterator it = this.testTicketTestList.iterator();
        while (it.hasNext())
        {
            TestVO testVO= (TestVO)it.next();
            if (testVO.getId().equals(testId))
                return testVO;
        }
        return null;
    } 
    
    private TestSummaryVO getTestSessionSummary(ScheduledSession scheduledSession)
    {
        int total = 0;
        int accommodated = 0;
        int calculator = 0;
        int screenReader = 0;
        int colorFont = 0;
        int pause = 0;
        int untimed = 0;
		/* 51931 Deferred Defect For HighLighter*/
		int highLigther = 0;
		int extendedTime = 0;// added for student pacing
        SessionStudent[] students = scheduledSession.getStudents();
        total = students.length;
        for(int i=0; i<students.length; i++){
            SessionStudent student = students[i];
            if(stringToBoolean(student.getHasAccommodations())){
                accommodated++;
            }
            if(stringToBoolean(student.getCalculator())){
                calculator++;
            }
            if(stringToBoolean(student.getScreenReader())){
                screenReader++;
            }
            if(stringToBoolean(student.getHasColorFontAccommodations())){
                colorFont++;
            }
            if(stringToBoolean(student.getTestPause())){
                pause++;
            }
            if(stringToBoolean(student.getUntimedTest())){
                untimed++;
            }
 		/* 51931 Deferred Defect For HighLighter*/
           if(stringToBoolean(student.getHighLighter())){
                highLigther++;
            }
           //Start- added for student pacing
           if(stringToBoolean(student.getExtendedTimeAccom())){
        	   extendedTime++;
           }
           //end- added for student pacing
        }
        return new TestSummaryVO(new Integer(total),
                                 new Integer (accommodated),
                                 new Integer (calculator),
                                 new Integer (screenReader),
                                 new Integer (colorFont),
                                 new Integer (pause),
                                 new Integer (untimed),
								/* 51931 Deferred Defect For HighLighter*/
								 new Integer (highLigther),
								 new Integer (extendedTime)); 
     } 
    
    private boolean stringToBoolean(String in)
    {
        boolean result = true;
        if(in == null ||
           in.toLowerCase().equals("false") ||
           in.toLowerCase().equals("F")){
            result = false;
        }
        return result;
    }
    
    private TestProduct getTestProduct(TestProduct [] tps, Integer productId)
    {
        for (int i=0 ; i<tps.length ; i++) {
            TestProduct tp = tps[i];
            if ((tp != null) && (tp.getProductId().intValue() == productId.intValue())) {
                return tp;                
            }
        }
        return new TestProduct();
    }
    
   private TestProductData getTestProductDataForUser() throws CTBBusinessException
    {
        TestProductData tpd = null;                
        SortParams sortParams = FilterSortPageUtils.buildSortParams("ProductName", ColumnSortEntry.ASCENDING, null, null);            
        tpd = this.scheduleTest.getTestProductsForUser(this.userName,null,null,sortParams);
        return tpd;
    }
    
    private List createProductNameList(TestProduct [] tps)
    {
        List result = new ArrayList();   
        this.productNameToIndexHash = new Hashtable();
        for (int i=0; i< tps.length; i++) {
            String productName = tps[i].getProductName();
            productName = JavaScriptSanitizer.sanitizeString(productName);            
            result.add(productName);
            this.productNameToIndexHash.put(productName, new Integer(i));
        }
        
        return result;
        
    }    
    
    private HashMap createProductIdNameHash(TestProduct [] tps)
    {
        HashMap result = new HashMap();   
        for (int i=0; i< tps.length; i++) {
            result.put(tps[i].getProductId().toString(),tps[i].getProductName());
        }
        return result;
        
    }      
    
    private List createLevelList(String [] levels)
    {
        List result = new ArrayList();
        for (int i=0; i<levels.length; i++)
        {
            if (levels[i] != null)
                result.add(levels[i]);
        }
        Collections.sort(result);
        if (levels.length > 1)
            result.add(0, FilterSortPageUtils.FILTERTYPE_SHOWALL);
        return result;
    }

    private List createGradeList(String [] grades)
    {
        List result = new ArrayList();
        for (int i=0; i<grades.length; i++)
        {
            if (grades[i] != null)
                result.add(grades[i]);
        }
        Collections.sort(result);
        if (grades.length > 1)
            result.add(0, FilterSortPageUtils.FILTERTYPE_SHOWALL);
        return result;
    }

    
    private TestElementData getTestsForProductForUser(Integer productId, FilterParams filter, PageParams page, SortParams sort)
        throws CTBBusinessException
    {
        TestElementData ted = this.scheduleTest.getTestsForProduct(this.userName, productId, filter, page, sort);    
        return ted;
    }
    
    private List buildSummaryTicketsTestList(Integer testAdminId) throws CTBBusinessException
    {
        List result = new ArrayList();
        if (testAdminId == null)
            return result;
        
        TestElementData suTed = this.testSessionStatus.getTestElementsForTestSession(this.userName, testAdminId, null, null, null);
        TestElement [] usTes = suTed.getTestElements();
        for(int i=0; i< usTes.length; i++){
            TestElement te = usTes[i];
            int durationMinutes = te.getTimeLimit().intValue()/60;
            String duration;
            if (durationMinutes == 0) 
                duration = "Untimed";
            else 
                duration = durationMinutes +" minutes";
            SubtestVO subtestVO = new SubtestVO(te.getItemSetId(),  String.valueOf(i+1), te.getItemSetName(), 
                                                duration, te.getAccessCode(), te.getSessionDefault());
            result.add(subtestVO);
        }
        return result;
        
    }
    
    private List buildTestList(TestElementData ted) throws CTBBusinessException
    {
        
        List result = new ArrayList();
        //Add variable for rd_allowable
        String hasRdAllowable = null;
        if (ted == null)
            return result;
        
        TestElement [] tes = ted.getTestElements();
        
        //todo: ask Nate to fix bug here.
        for (int i=0; i<tes.length && tes[i]!=null ; i++)
        {
            String accessCode = tes[i].getAccessCode();
            
            List subtestList = new ArrayList();

            Integer itemSetId = tes[i].getItemSetId();
            TestElementData suTed = this.scheduleTest.getSchedulableUnitsForTest(this.userName, itemSetId, new Boolean(true), null, null, null);
            TestElement [] usTes = suTed.getTestElements();
            for (int j=0; j<usTes.length; j++)
            {
                int durationMinutes = usTes[j].getTimeLimit().intValue()/60;
                String duration = (durationMinutes == 0) ? "Untimed" : durationMinutes + " minutes";
                SubtestVO subtestVO = new SubtestVO(usTes[j].getItemSetId(),
                                                    String.valueOf(j+1), 
                                                    usTes[j].getItemSetName(), 
                                                    duration, 
                                                    usTes[j].getAccessCode(),
                                                    usTes[j].getSessionDefault(),
                                                    usTes[j].getItemSetForm(),
                                                    false);
                subtestList.add(subtestVO);
            }
    
            int durationMinutes = tes[i].getTimeLimit().intValue()/60;
            String duration = (durationMinutes == 0) ? "Untimed" : durationMinutes + " minutes";
    
    
            String level = "";
            if (this.showLevelOrGrade.equals("level")) 
                level = tes[i].getItemSetLevel();
            else if (this.showLevelOrGrade.equals("grade"))
                level = tes[i].getGrade();
            
            
            
            TestVO testVO = new TestVO(tes[i].getItemSetId(), tes[i].getItemSetName(), 
                    level, duration, subtestList);
                    
            testVO.setAccessCode(accessCode);
            testVO.setForms(tes[i].getForms());
            
            
            
            //  check whether Randomized Distractor is allowable  of the test    
            hasRdAllowable = tes[i].getIsRandomize();
           
            if ( hasRdAllowable != null) {
                
                
             // set isRandomize valuee in the testVO   
                if ( hasRdAllowable .equals("T")) {
        
                            testVO.setIsRandomize(RD_YES);
        
                    } else {
        
                            testVO.setIsRandomize(RD_NO);
                    }
        
                } 
        
            testVO.setOverrideFormAssignment(tes[i].getOverrideFormAssignmentMethod());
            testVO.setOverrideLoginStartDate(tes[i].getOverrideLoginStartDate());
                    
            result.add(testVO);
        }
        return result;
        
    }
    
    private int getProductListIndexByName(String productName) {
        if (productName == null)
            return -1;
        Integer index = (Integer) this.productNameToIndexHash.get(productName);
        if (index == null)
            return -1;
        else
            return index.intValue();        

    }
    
    private PagerSummary buildTestPagerSummary(TestElementData ted, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);
        pagerSummary.setTotalPages(ted.getFilteredPages());
        pagerSummary.setTotalObjects(ted.getFilteredCount());
        pagerSummary.setTotalFilteredObjects(null);        
        
        return pagerSummary;
    }

    private StudentNodeData getChildrenOrgNodes(Integer orgNodeId, Integer testAdminId, FilterParams filter, PageParams page, SortParams sort)
    {    
        StudentNodeData snd = null;
        try {      
            if ((orgNodeId == null) || (orgNodeId.intValue() <= 0))
                snd = this.scheduleTest.getTopRosterNodesForUserAndAdmin(this.userName, testAdminId, filter, page, sort);
            else
                snd = this.scheduleTest.getRosterNodesForParentAndAdmin(this.userName, orgNodeId, testAdminId, filter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return snd;
    }
    
    private StudentNodeData getTestTicketNodes(Integer orgNodeId, Integer testAdminId, FilterParams filter, PageParams page, SortParams sort)
    {    
        StudentNodeData snd = null;
        try {      
            if ((orgNodeId == null) || (orgNodeId.intValue() <= 0))
                snd = this.scheduleTest.getTopTestTicketNodes(this.userName, testAdminId, filter, page, sort);
            else
                snd = this.scheduleTest.getTestTicketNodesForParent(this.userName, orgNodeId, testAdminId, filter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return snd;
    }
    
    private List buildOrgNodeList(StudentNodeData snd) 
    {
        ArrayList nodeList = new ArrayList();
        PathNode pathNode = null;
        StudentNode [] nodes = snd.getStudentNodes();        
        for (int i=0 ; i<nodes.length ; i++) {
            StudentNode node = (StudentNode)nodes[i];
            if (node != null) { 
                pathNode = new PathNode();
                pathNode.setName(node.getOrgNodeName());
                pathNode.setId(node.getOrgNodeId());   
                pathNode.setFilteredCount(node.getStudentCount());
                pathNode.setSelectedCount(node.getRosterCount());
                pathNode.setChildNodeCount(node.getChildNodeCount());
                pathNode.setHasChildren((new Boolean(node.getChildNodeCount().intValue()>0)).toString());
                pathNode.setCategoryName(node.getOrgNodeCategoryName());                
                nodeList.add(pathNode);
            }
        }
        return nodeList;
    }
     
    private PagerSummary buildOrgNodePagerSummary(StudentNodeData snd, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);        
        pagerSummary.setTotalPages(snd.getFilteredPages());
        pagerSummary.setTotalObjects(snd.getFilteredCount());
        pagerSummary.setTotalFilteredObjects(null);        
        return pagerSummary;
    }
  
    private PagerSummary buildStudentPagerSummary(SessionStudentData sd, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);        
        pagerSummary.setTotalPages(sd.getFilteredPages());
        pagerSummary.setTotalObjects(sd.getTotalCount());
        pagerSummary.setTotalFilteredObjects(sd.getFilteredCount());        
        return pagerSummary;
    }

    private PagerSummary buildRestrictedStudentPagerSummary(SessionStudentData sd, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);        
        pagerSummary.setTotalPages(sd.getFilteredPages());
        pagerSummary.setTotalObjects(sd.getTotalCount());
        pagerSummary.setTotalFilteredObjects(null);        
        return pagerSummary;
    }

 
    private List buildStudentList(SessionStudentData ssd) 
    {
        List studentList = new ArrayList();
        SessionStudent [] sessionStudents = ssd.getSessionStudents();   
        for (int i=0 ; i<sessionStudents.length; i++) {
            SessionStudent ss = (SessionStudent)sessionStudents[i];
            if (ss != null) {                
                StringBuffer buf = new StringBuffer();
                buf.append(ss.getFirstName()).append(" ").append(ss.getLastName()).append(": ");
                if ("T".equals(ss.getCalculator())) {
                    if ("true".equals(ss.getHasColorFontAccommodations()) ||
                        "T".equals(ss.getScreenReader()) ||
                        "T".equals(ss.getTestPause()) ||
                        "T".equals(ss.getUntimedTest()))
                        buf.append("Calculator, ");
                    else
                        buf.append("Calculator");
                }
                if ("true".equals(ss.getHasColorFontAccommodations())) {
                    if ("T".equals(ss.getScreenReader()) ||
                        "T".equals(ss.getTestPause()) ||
                        "T".equals(ss.getUntimedTest()))
                        buf.append("Color/Font, ");
                    else
                        buf.append("Color/Font");
                }
                if ("T".equals(ss.getScreenReader())) {
                    if ("T".equals(ss.getTestPause()) ||
                        "T".equals(ss.getUntimedTest()))
                        buf.append("ScreenReader, ");
                    else
                        buf.append("ScreenReader");
                }
                if ("T".equals(ss.getTestPause())) {
                    if ("T".equals(ss.getUntimedTest()))
                        buf.append("TestPause, ");
                    else
                        buf.append("TestPause");
                }
                if ("T".equals(ss.getUntimedTest())) {
                    buf.append("UntimedTest");
                }
                buf.append(".");
                ss.setExtPin3(escape(buf.toString()));
                studentList.add(ss);
            }
        }
        return studentList;
    }
    
    private String escape(String str)
    {
        int len = str.length ();

        StringBuffer safe = new StringBuffer (len);

        for (int i = 0; i < len; i++)
        {
            char cur = str.charAt (i);
            if (cur == '\'')
            {
             safe.append ('\\');
             safe.append (cur);
            }
            else
                safe.append (cur);
        }
        return new String (safe);
    }
    

    private PagerSummary buildProctorPagerSummary(UserData ud, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);        
        pagerSummary.setTotalPages(ud.getFilteredPages());
        pagerSummary.setTotalObjects(ud.getFilteredCount());
        pagerSummary.setTotalFilteredObjects(null);        
        return pagerSummary;
    }
    

    private List buildProctorList(UserData ud) 
    {
        ArrayList nodeList = new ArrayList();
        
        User [] nodes = ud.getUsers();        
        for (int i=0 ; i<nodes.length; i++) {
            User node = (User)nodes[i];
            if (node != null) {
                if (node.getUserId().equals(this.scheduler.getUserId()))
                    node.setEditable("F");
                else if (node.getUserId().equals(this.user.getUserId()))  
                    node.setEditable("F");
                else if (node.getEditable() == null || node.getEditable().equals(""))
                    node.setEditable("T");
            
                nodeList.add(node);
            }
        }
        return nodeList;
    }
    

    private void handleAction(ScheduleTestForm form)
    {
        String actionElement = form.getActionElement();
        String currentAction = form.getCurrentAction();
        
        String studentInformationMessage=null;
        if (this.condition.getAllStudentLoggedIn().booleanValue())
            studentInformationMessage = MessageResourceBundle.getMessage("SelectSettings.Students.ChangeFormInfo1");
        else
            studentInformationMessage = MessageResourceBundle.getMessage("SelectSettings.Students.ChangeFormInfo2");
        
        if (actionElement != null && actionElement.equals("currentAction")) {
            if (currentAction.equals("applyFilters")) {
                applyFilters(form);
            }
            else if (currentAction.equals("clearFilters")) {
                clearFilters(form);
                applyFilters(form);
            }
            else if (currentAction.equals("changeAccommodation")) {
                changeAccommodation(form);
            }
            else if (currentAction.equals("removeSelectedStudents")) {
                removeSelectedStudents(form);
            }
            else if (currentAction.equals("removeAllStudents")) {
                removeAllStudents(form);
            }
            else if (currentAction.equals(TestSession.FormAssignment.ROUND_ROBIN)) {
                this.getRequest().setAttribute("displayFormList", "false");     
                this.getRequest().setAttribute("isFormEditable", "false");     
                if (this.condition.getHasStudentLoggedIn().booleanValue()) 
                    this.getRequest().setAttribute("studentInformationMessage", studentInformationMessage);     
            }
            else if (currentAction.equals(TestSession.FormAssignment.ALL_SAME)) {
                this.getRequest().setAttribute("displayFormList", "true");     
                this.getRequest().setAttribute("isFormEditable", "false");     
                if (this.condition.getHasStudentLoggedIn().booleanValue()) 
                    this.getRequest().setAttribute("studentInformationMessage", studentInformationMessage);     
            }
            else if (currentAction.equals(TestSession.FormAssignment.MANUAL)) {
                if (this.selectedStudents != null) {
                    String defaultForm = "";
                    if (this.formList != null && this.formList.length > 0) 
                        defaultForm = this.formList[0];
                    Iterator it = this.selectedStudents.iterator();
                    while (it.hasNext()) {
                        SessionStudent ss = (SessionStudent) it.next();
                        if (ss!=null && (ss.getItemSetForm()==null || ss.getItemSetForm().trim().equals(""))) 
                            ss.setItemSetForm(defaultForm);
                    }
                }                
                this.getRequest().setAttribute("displayFormList", "false");     
                this.getRequest().setAttribute("isFormEditable", "true");     
                if (this.condition.getHasStudentLoggedIn().booleanValue()) 
                    this.getRequest().setAttribute("studentInformationMessage", studentInformationMessage);     
            }
            else if (currentAction.equals("removeSelectedProctors")) {
                removeSelectedProctors(form);
            }
            else if (currentAction.equals("removeAllProctors")) {
                removeAllProctors(form);
            }
         }
         else if (actionElement != null && actionElement.equals("{actionForm.studentStatePathList.pageRequested}")) {
            form.setSelectedStudentIds(new Integer[0]);
         }
         else if (actionElement != null && actionElement.equals("{actionForm.proctorStatePathList.pageRequested}")) {
            form.setSelectedProctorIds(new Integer[0]);
         }
    }
    
    private void applyFilters(ScheduleTestForm form) 
    {
        TestRosterFilter trf = form.getTestRosterFilter();
        this.testRosterFilter.copyValues(trf);
        form.resetStudentSortPage();
    }

    private void clearFilters(ScheduleTestForm form) 
    {
        TestRosterFilter trf = new TestRosterFilter();
        form.setTestRosterFilter(trf);
        getRequest().setAttribute("formIsClean", null);        
    }

    private void changeAccommodation(ScheduleTestForm form) 
    {
        TestRosterFilter trf = form.getTestRosterFilter();
        String accommodationOperand = trf.getAccommodationFilterType();
        if ((accommodationOperand != null) && (accommodationOperand.equals(FilterSortPageUtils.STUDENTS_WITH_ACCOMMODATIONS))) {
            this.getRequest().setAttribute("disableApply", "true");     
        }               
        else {
            this.getRequest().setAttribute("disableApply", "false");     
        }               
    }
    
    private void removeSelectedStudents(ScheduleTestForm form)
    {
        Integer [] selectedStudentIds = form.getSelectedStudentIds();
        
        int count=0;
        for (int i=this.selectedStudents.size()-1 ; i>=0 ; i--) {
            SessionStudent ss = (SessionStudent)this.selectedStudents.get(i);
            for (int j=0; j<selectedStudentIds.length; j++) {
                Integer studentId = selectedStudentIds[j];
                if ((studentId != null) && (studentId.intValue() > 0) &&
                    (studentId.intValue() == ss.getStudentId().intValue())) {
                    this.selectedStudents.remove(i);
                    count++;
                    break;
                }
            }             
        }
        
        if (count > 0) {
            this.getRequest().setAttribute("removeStudentMessage", MessageResourceBundle.getMessage("SelectSettings.Students.StudentsRemoved", ""+count));
        }
        
        form.setSelectedStudentIds(null);                    
    }


    private void removeAllStudents(ScheduleTestForm form)
    {
        
        if (this.selectedStudents != null && this.selectedStudents.size() >0) {
            SessionStudentData ssd = new SessionStudentData();
            SessionStudent [] sessionStudents = new SessionStudent[this.selectedStudents.size()];
            this.selectedStudents.toArray(sessionStudents);
            ssd.setSessionStudents(sessionStudents, new Integer(FilterSortPageUtils.MAX_RECORDS));

            FilterParams filter = FilterSortPageUtils.buildTestRosterFilterParams(this.testRosterFilter);
            PageParams page = new PageParams(1, 10000, 1000);
            SortParams sort = null;
            
            try {
                if (filter != null)
                    ssd.applyFiltering(filter);
                if (sort != null)
                    ssd.applySorting(sort);
                if (page != null)
                    ssd.applyPaging(page);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            
            SessionStudent [] selectedStudents = ssd.getSessionStudents();        
            HashMap hashmap = new HashMap();
            
            for (int i=0; i<selectedStudents.length; i++) {
                if (selectedStudents[i] != null && selectedStudents[i].getStatus().getEditable().equals("T"))
                    hashmap.put(selectedStudents[i].getStudentId(), selectedStudents[i].getStudentId());
            }

            int count=0;
            Iterator it = this.selectedStudents.iterator();
            while (it.hasNext()) {
                SessionStudent ss = (SessionStudent) it.next();
                if (hashmap.containsKey(ss.getStudentId())) {
                    it.remove();
                    count++;
                }
            }
            
            if (this.selectedStudents.size() == 0)
                this.getRequest().setAttribute("studentInformationMessage", MessageResourceBundle.getMessage("SelectSettings.Students.AllStudentsRemoved"));
            else if (count >= 1)
                this.getRequest().setAttribute("studentInformationMessage", MessageResourceBundle.getMessage("SelectSettings.Students.StudentsRemoved", ""+count));
        }
    }
    
    private void removeSelectedProctors(ScheduleTestForm form)
    {
        Integer [] selectedProctorIds = form.getSelectedProctorIds();
        HashMap hashmap = new HashMap();
        
        for (int i=0; i<selectedProctorIds.length; i++) {
            hashmap.put(selectedProctorIds[i], selectedProctorIds[i]);
        }

        int count=0;
        Iterator it = this.selectedProctors.iterator();
        while (it.hasNext()) {
            User user = (User) it.next();
            if (hashmap.containsKey(user.getUserId())){
                it.remove();
                count++;
            }
        }
        
        form.setSelectedProctorIds(null);
        if (count >= 1)
            this.getRequest().setAttribute("proctorInformationMessage", MessageResourceBundle.getMessage("SelectSettings.Proctors.ProctorsRemoved", ""+count));
        
    }
    
    private void removeAllProctors(ScheduleTestForm form)
    {
        int count=0;
        Iterator it = this.selectedProctors.iterator();
        while (it.hasNext()) {
            User user = (User) it.next();
            if ("T".equals(user.getEditable())){
                it.remove();
                count++;
            }
        }
        if (count >= 1)
            this.getRequest().setAttribute("proctorInformationMessage", MessageResourceBundle.getMessage("SelectSettings.Proctors.ProctorsRemoved.SchedulerKept", ""+count));
        
    }

    private String getSessionStudentOrgCategoryName(List studentNodes) {
        String categoryName = "Organization";        
        if (studentNodes.size() > 0) {
            SessionStudent ss = (SessionStudent)studentNodes.get(0);
            categoryName = ss.getOrgNodeCategoryName();
            for (int i=1 ; i<studentNodes.size() ; i++) {
                ss = (SessionStudent)studentNodes.get(i);
                if (! ss.getOrgNodeCategoryName().equals(categoryName)) {
                    categoryName = "Organization";
                    break;
                }
            }
        }
        return categoryName;        
    }

    private String getOrgCategoryName(List nodeList) {
        String categoryName = "Organization";        
        if (nodeList.size() > 0) {
            PathNode node = (PathNode)nodeList.get(0);
            categoryName = node.getCategoryName();            
            for (int i=1 ; i<nodeList.size() ; i++) {
                node = (PathNode)nodeList.get(i);
                if (! node.getCategoryName().equals(categoryName)) {
                    categoryName = "Organization";
                    break;
                }
            }
        }
        return categoryName;
    }

    private int getSelectedIdCount(Integer [] selectedIds) {
        if (selectedIds == null)
            return 0;
        int count = 0;    
        for (int i=0; i < selectedIds.length; i++) {
            if (selectedIds[i] !=null)
                count++;
        }
        return count;
    }
    
    private boolean hasSpecialCharInTAC(String [] TACs) 
    {
        boolean found = false;
        for (int i=0; i<TACs.length && !found; i++) {
            for (int j=0; j < TACs[i].length() && !found; j++) {
                char currentChar = TACs[i].charAt(j);
                if (!(currentChar >= 'A' && currentChar <= 'Z' || currentChar >= 'a' && currentChar <= 'z' || currentChar >= '0' && currentChar <= '9' || currentChar == '_'))
                    found = true;                
            }                
        }            
        return found;
    }
    
    private String getTACsInString(Vector vec) 
    {
        Iterator it = vec.iterator();
        StringBuffer buf = new StringBuffer();
        while (it.hasNext()) {
            buf.append((String)it.next());
            if (it.hasNext())
                buf.append(", ");
        }
        return buf.toString();
    }

	public SubtestVO getLocatorSubtest() {
		return locatorSubtest;
	}

	public Condition getCondition() {
		return condition;
	}
	

	public String[] getNameOptions() {
		return nameOptions;
	}

	public String[] getAccommodationTypeOptions() {
		return accommodationTypeOptions;
	}

	public String[] getSelectedAccommodationsOptions() {
		return selectedAccommodationsOptions;
	}
	
	
}