package registration;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;



import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.DateUtils;
import utils.FilterSortPageUtils;
import utils.JsonStudentUtils;
import utils.MessageResourceBundle;
import utils.StudentPathListUtils;
import utils.StudentSearchUtils;
import utils.TABESubtestValidation;
import utils.TestSessionUtils;
import utils.WebUtils;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.studentManagement.OrganizationNode;
import com.ctb.bean.studentManagement.OrganizationNodeData;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.StudentAccommodations;
import com.ctb.bean.testAdmin.StudentManifest;
import com.ctb.bean.testAdmin.StudentManifestData;
import com.ctb.bean.testAdmin.StudentSessionStatus;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.studentManagement.StudentDataCreationException;
import com.ctb.exception.testAdmin.InsufficientLicenseQuantityException;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.PagerSummary;

import data.CustomerLicenseInfo;
import data.SubtestVO;
import dto.LicenseSessionData;
import dto.Message;
import dto.PathNode;
import dto.StudentProfileInformation;

/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** RegistrationController ************* ///////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
  
/**
 * @jpf:controller
**/
@Jpf.Controller()
public class RegistrationController extends PageFlowController
{

    static final long serialVersionUID = 1L;

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
    private com.ctb.control.licensing.Licensing licensing;
    
    private static final String ACTION_DEFAULT          = "defaultAction";
    private static final String ACTION_FIND_STUDENT     = "findStudentAction";
    private static final String ACTION_ADD_STUDENT      = "addStudentAction";
  //START- FORM RECOMMENDATION
	private static final String ACTION_FORM_RECOMMENDATION_STUDENT       = "goto_recommended_find_test_sessions";
	private static final String ACTION_FORM_TO_MODIFY_TEST    			 = "toModifyTestFromFind";
	private static final String ACTION_FORM_RECOMMENDATION_STUDENT_NO    = "goto_recommended_find_test_sessions_on_no";
	//END- FORM RECOMMENDATION
	

    private static final String ACTION_APPLY_SEARCH     = "applySearch";
    private static final String ACTION_CLEAR_SEARCH     = "clearSearch";

    private static final String ACTION_SUBTEST_VALIDATION_FAILED    = "subtestValidationFailed";
    private static final String ACTION_AUTOLOCATOR                  = "autoLocator";

    private String userName = null;
    private Integer customerId = null;
    
    private User user = null;
    
    private Integer testAdminId = null;
    private Integer itemSetId = null;
    private OrganizationNode[] studentOrgNodes;
    private StudentProfileInformation student = null;
    private StudentProfileInformation searchInfo = null;
    private Integer studentOrgId = null;
    
    private String currentSelectedTab = null;
    private boolean returnFromCongratulation = false;
    
    public String[] gradeOptions = null;
    public String[] genderOptions = null;
    public String[] monthOptions = null;
    public String[] dayOptions = null;
    public String[] yearOptions = null;

    private List orgNodePath = null;
    public List orgNodeNames = null;
    public List orgNodeList = null;
    
    public List defaultSubtests = null;
    public List availableSubtests = null;
    public List selectedSubtests = null;
    public List levelOptions = null;
    public SubtestVO locatorSubtest = null;
    private boolean isLocatorTest = false;
    private boolean sessionHasDefaultLocatorSubtest = false;
    
    ScheduledSession scheduledSession = null;
    
    private RegistrationForm savedForm = null;

    public CustomerLicenseInfo licenseInfo = null;
    
    //License user stories Agile task
    public LicenseSessionData licenseSessionData = null;
 
    // customer configuration
    CustomerConfiguration[] customerConfigurations = null;
    
    //GACRCT2010CR007- Disable_Mandatory_Birth_Date according to customer cofiguration
    private boolean disableMandatoryBirthdate = false;
    ///START- FORM RECOMMENDATION
    private Boolean  requestFromFindStudent = true;
    private boolean isFormRecommended = false;
    private Integer preTestAdminId = new Integer(0);
    private Integer recommendedProductId = new Integer(0);
	private Integer productId = new Integer(0);
	//END- FORM RECOMMENDATION
  
    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="enterStudent.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "enterStudent.do"),
                     @Jpf.Forward(name = "toModifyTest",
                    		 path = "toModifyTestFromFind.do")
    })
    protected Forward begin()
    {
        getUserDetails();
     
                        
        this.gradeOptions = getGradeOptions(ACTION_FIND_STUDENT);
        this.genderOptions = getGenderOptions(ACTION_FIND_STUDENT);
        this.monthOptions = DateUtils.getMonthOptions();
        this.dayOptions = DateUtils.getDayOptions();
        this.yearOptions = DateUtils.getYearOptions();        
                
   //     this.testAdminId = new Integer(221223); // temporary to run locally for now for tai_tabe user - TABE Dedault For RR
        
        String testAdminStr = (String)this.getRequest().getParameter("testAdminId");
        if (testAdminStr != null)
        {
            this.testAdminId = new Integer(testAdminStr);
        }
                
        this.scheduledSession = TestSessionUtils.getTestSessionDataWithoutRoster(this.scheduleTest, this.userName, this.testAdminId);
        
        TestSession testSession = this.scheduledSession.getTestSession();
        this.itemSetId = testSession.getItemSetId();
        ///START- FORM RECOMMENDATION
        if(this.savedForm != null && this.savedForm.testAdminId != null)
        {preTestAdminId = this.savedForm.testAdminId;}
        this.savedForm = initData();
        this.savedForm.setTestAdminId(preTestAdminId);
        initTestStructure(this.savedForm);
       ///END- FORM RECOMMENDATION
        this.licenseInfo = new CustomerLicenseInfo(this.customerId, this.userName);

        this.getSession().setAttribute("userHasReports", userHasReports());
        
        //GACRCT2010CR007- retrieve value for Disable_Mandatory_Birth_Date 
        getCustomerConfigurations();
        //Bulk Accommodation
		customerHasBulkAccommodation();
		customerHasScoring();//For hand scoring changes
		//START- Form Recommendation
		String reqStudentId = (String)this.getRequest().getParameter("studentId");
        if (reqStudentId != null && testAdminStr != null)
        {
        	this.isFormRecommended = true;
        	return new Forward("toModifyTest", this.savedForm);
        } 
        this.isFormRecommended = false;
        //END- Form Recommendation 
        return new Forward("success", this.savedForm);
    }

    /**
     * initTestStructure
     */
    private void initTestStructure(RegistrationForm form)
    {
        List subtestsForTestAdmin = TestSessionUtils.getAllSubtestsForTestAdmin(this.scheduleTest, this.userName, this.testAdminId); 

        this.sessionHasDefaultLocatorSubtest = false;        
        this.locatorSubtest = TestSessionUtils.getLocatorSubtest(subtestsForTestAdmin); 

        if (this.locatorSubtest == null)
        {
            this.locatorSubtest = TestSessionUtils.getLocatorSubtest(this.scheduleTest, this.userName, this.itemSetId); 
        }
        else
        {
            if (this.locatorSubtest.getSessionDefault().equals("T"))
                this.sessionHasDefaultLocatorSubtest = true;        
        }

        this.isLocatorTest = TestSessionUtils.isLocatorTest(subtestsForTestAdmin);        
        
        if (this.isLocatorTest)
            this.defaultSubtests = TestSessionUtils.getDefaultSubtests(subtestsForTestAdmin);
        else
            this.defaultSubtests = TestSessionUtils.getDefaultSubtestsWithoutLocator(subtestsForTestAdmin);
                
        if (this.isLocatorTest || this.sessionHasDefaultLocatorSubtest)
            form.setAutoLocator("true");                     
        else
            form.setAutoLocator(null);                     
        
        this.selectedSubtests = TestSessionUtils.cloneSubtestsForRegistration(this.defaultSubtests); 
        
        this.availableSubtests = TestSessionUtils.getAvailableSubtests(this.defaultSubtests, this.selectedSubtests);                          

        this.levelOptions = TestSessionUtils.getLevelOptions();
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
     * initData
     */
    private RegistrationForm initData()
    {
        RegistrationForm form = new RegistrationForm();
        form.init();
        
        this.orgNodePath = new ArrayList();
        
        this.currentSelectedTab = ACTION_DEFAULT;
        this.student = null;   
        
        this.returnFromCongratulation = false;   
        
        return form;  
    }
    
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** ENTER STUDENT ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="enter_student.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "enter_student.jsp"),
                     @Jpf.Forward(name = "goto_recommended_find_test_sessions",
								path = "goto_recommended_find_test_sessions.do"), 
								@Jpf.Forward(name = "toModifyTestFromFind",
										path = "toModifyTestFromFind.do") 
    })
    protected Forward enterStudent(RegistrationForm form)
    {   
        ///START- FORM RECOMMENDATION
    	String currentAction = form.getCurrentAction();
		String actionElement = form.getActionElement();
		
		form.resetValuesForAction(actionElement, ACTION_DEFAULT); 
		if (currentAction.equals(ACTION_FORM_RECOMMENDATION_STUDENT)){
			return new Forward(currentAction, form);
		}
		if (currentAction.equals(ACTION_FORM_TO_MODIFY_TEST)){
			return new Forward(currentAction, form);
		}
		///END- FORM RECOMMENDATION
        String selectedTab = form.getSelectedTab();
        
        if (! selectedTab.equals(this.currentSelectedTab))
        {
            initTab(selectedTab, form);
        }
        
        if (selectedTab.equals(ACTION_FIND_STUDENT))
        {
            handleFindStudent(form); 
            Integer selectedStudentId = form.getSelectedStudentId();
            if (selectedStudentId == null) 
                this.getRequest().setAttribute("disableNextButton", "true");
            else
                this.getRequest().setAttribute("disableNextButton", "false");
        }
                        
        if (selectedTab.equals(ACTION_ADD_STUDENT))
        {
            handleAddStudent(form); 
        }

        this.getRequest().setAttribute("selectedTab", selectedTab);
        
        this.getRequest().setAttribute("testAdminName", this.scheduledSession.getTestSession().getTestAdminName());                        
        
        setFormInfoOnRequest(form);                
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="beginEnterMoreStudent.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "beginEnterMoreStudent.do"),
                      @Jpf.Forward(name = "goto_to_find_student",
                    		  path = "goto_to_find_student.do")
    })
    protected Forward enterMoreStudent(RegistrationForm form)
    {	//START- Form Recommendation
    	if(this.isFormRecommended){
    		
    		return new Forward("goto_to_find_student", form);
    		
    	}
    	//END- Form Recommendation
        return new Forward("success");
    }
        

    /**
     * @jpf:action
     * @jpf:forward name="success" path="enterStudent.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "enterStudent.do")
    })
    protected Forward beginEnterMoreStudent()
    {
        this.scheduledSession = TestSessionUtils.getTestSessionDataWithoutRoster(this.scheduleTest, this.userName, this.testAdminId);
        
        initTestStructure(this.savedForm);

        this.gradeOptions = getGradeOptions(this.currentSelectedTab);
        this.genderOptions = getGenderOptions(this.currentSelectedTab);     
                                
        if (this.currentSelectedTab.equalsIgnoreCase(ACTION_ADD_STUDENT))
        {
            StudentProfileInformation studentProfile = new StudentProfileInformation();
            
            studentProfile.setGrade(this.gradeOptions[0]);
            studentProfile.setGender(this.genderOptions[0]);               
            studentProfile.setMonth(this.monthOptions[0]);
            studentProfile.setDay(this.dayOptions[0]);
            studentProfile.setYear(this.yearOptions[0]);
            
            this.savedForm.setStudentProfile(studentProfile);
        }
        else
        {
            this.savedForm.setStudentProfile(this.searchInfo);
            this.savedForm.setCurrentAction(ACTION_APPLY_SEARCH);            
        }
        
        this.savedForm.setMessage(null, null, null);
        this.savedForm.setSelectedStudentId(null);      
        this.student = null;  
        this.returnFromCongratulation = false;
        //GACRCT2010CR007- retrieve value for Disable_Mandatory_Birth_Date 
        getCustomerConfigurations();
        return new Forward("success", this.savedForm);
    }


    /**
     * @jpf:action
     * @jpf:forward name="success" path="modifyTest.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "modifyTest.do")
    })
    protected Forward toModifyTestFromCongratulation(RegistrationForm form)
    {
        initTestStructure(form);
        
        this.returnFromCongratulation = true;
        
        Integer studentId = this.student.getStudentId();
        
        this.selectedSubtests = TestSessionUtils.getStudentSubtests(this.scheduleTest, this.userName, studentId, this.testAdminId);        

        SubtestVO locSubtest = TestSessionUtils.getLocatorSubtest(this.selectedSubtests); 
        if (locSubtest != null) 
            this.savedForm.setAutoLocator("true");
        else 
            this.savedForm.setAutoLocator(null);
              
        TestSessionUtils.copyTestAccessCode(this.defaultSubtests, this.selectedSubtests);

        TestSessionUtils.extractLocatorSubtest(this.selectedSubtests);

        this.availableSubtests = TestSessionUtils.getAvailableSubtests(this.defaultSubtests, this.selectedSubtests);                                

        this.defaultSubtests = TestSessionUtils.sortSubtestList(this.defaultSubtests, this.selectedSubtests);         

        List testAdminSubtests = TestSessionUtils.getAllSubtestsForTestAdmin(this.scheduleTest, this.userName, this.testAdminId); 

        TestSessionUtils.copySubtestLevel(testAdminSubtests, this.defaultSubtests);

        Integer itemSetId = this.scheduledSession.getTestSession().getItemSetId();
        Integer locatorItemSetId = null;
        if (this.locatorSubtest != null)
        {
            locatorItemSetId = this.locatorSubtest.getId();
        }

        TestSessionUtils.setRecommendedLevelForSession(this.scheduleTest, this.userName, studentId, itemSetId, locatorItemSetId, this.defaultSubtests);
        
        TestSessionUtils.setDefaultLevelsIfNull(this.defaultSubtests, this.selectedSubtests);
        
        this.savedForm.setMessage(null, null, null);       
        this.savedForm.setActionElement(ACTION_DEFAULT);
        this.savedForm.setCurrentAction(ACTION_DEFAULT);                 
        this.savedForm.setSelectedOrgNodeName(form.getSelectedOrgNodeName());
        
        return new Forward("success", this.savedForm);
    }

    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="modifyTest.do"
     * @jpf:forward name="error" path="enterStudent.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "modifyTest.do"), 
        @Jpf.Forward(name = "error",
                     path = "enterStudent.do")
    })
    protected Forward toModifyTestFromFind(RegistrationForm form)
    {	//START- Form Recommendation
    	 String reqStudentId = (String)this.getRequest().getParameter("studentId");
         if (reqStudentId != null)
         {
             form.setSelectedStudentId(new Integer(reqStudentId));
         }
         String requestFromFindStudent = (String)this.getRequest().getParameter("requestFromFindStudent");
         if (requestFromFindStudent != null && !requestFromFindStudent.equals(""))
         {
             this.setRequestFromFindStudent(new Boolean (requestFromFindStudent));
         }
         //END- Form Recommendation             
        Integer studentId = form.getSelectedStudentId();
        if (studentId == null)
        {
            form.setMessage(Message.MISSING_SELECTION, Message.NSS_CONTENT, Message.ERROR);            
            return new Forward("error", form);            
        }
        
        this.student = StudentSearchUtils.getStudentProfileInformation(this.studentManagement, this.userName, studentId);
        
        this.studentOrgNodes = this.student.getOrganizationNodes();        
        this.orgNodeNames = new ArrayList();
        for (int i=0; i < this.studentOrgNodes.length; i++)
        {
            OrganizationNode orgNode = this.studentOrgNodes[i];
            this.orgNodeNames.add(orgNode.getOrgNodeName()); 
        }
        String orgNodeName = (String)this.orgNodeNames.get(0);
        form.setSelectedOrgNodeName(orgNodeName);       
                
        this.searchInfo = form.getStudentProfile();
        this.savedForm = form.createClone();     
        this.savedForm.setTestAdminId(this.preTestAdminId);
        Integer itemSetId = this.scheduledSession.getTestSession().getItemSetId();
        Integer locatorItemSetId = null;
        if (this.locatorSubtest != null)
        {
            locatorItemSetId = this.locatorSubtest.getId();
        }
        
        TestSessionUtils.setRecommendedLevelForSession(this.scheduleTest, this.userName, studentId, itemSetId, locatorItemSetId, this.defaultSubtests);
                
        if (! TestSessionUtils.setRecommendedLevelForStudent(this.scheduleTest, this.userName, studentId, itemSetId, locatorItemSetId, this.selectedSubtests))
        {
            TestSessionUtils.setDefaultLevels(this.defaultSubtests, this.selectedSubtests);                                                                                
        }
        TestSessionUtils.setDefaultLevelsIfNull(this.defaultSubtests, this.selectedSubtests);                                                                                
        
        return new Forward("success", this.savedForm);
    }


    /**
     * @jpf:action
     * @jpf:forward name="success" path="modifyTest.do"
     * @jpf:forward name="error" path="enterStudent.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "modifyTest.do"), 
        @Jpf.Forward(name = "error",
                     path = "enterStudent.do")
    })
    protected Forward toModifyTestFromAdd(RegistrationForm form)
    {
        // check for required fields
        String requiredFields = "";
        int requiredFieldCount = 0;

        StudentProfileInformation studentProfile = form.getStudentProfile();
        Integer selectedOrgNodeId = form.getSelectedOrgNodeId();
        
        //GACRCT2010CR007- set value for disableMandatoryBirthdate in  form.
        form.setDisableMandatoryBirthdate(disableMandatoryBirthdate);
        boolean result = form.verifyStudentInformation(studentProfile, selectedOrgNodeId);
        if (! result)
        {           
            form.setActionElement(ACTION_DEFAULT);
            form.setCurrentAction(ACTION_DEFAULT);                 
            return new Forward("error", form);
        }        
        
        
        this.orgNodeNames = new ArrayList();
        this.studentOrgNodes = new OrganizationNode[1];
        OrganizationNode orgNode = new OrganizationNode();

        for (int i=0; i < this.orgNodeList.size(); i++)
        {
            PathNode pathNode = (PathNode)this.orgNodeList.get(i);
            if (pathNode.getId().intValue() == selectedOrgNodeId.intValue())
            {
                orgNode.setOrgNodeId(selectedOrgNodeId);
                orgNode.setOrgNodeName(pathNode.getName());
                break;
            }
        }
        this.studentOrgNodes[0] = orgNode;            
        this.orgNodeNames.add(orgNode.getOrgNodeName()); 
        String orgNodeName = (String)this.orgNodeNames.get(0);
        form.setSelectedOrgNodeName(orgNodeName);       
                
        this.student = studentProfile.createClone();
        this.student.setDisplayName(studentProfile.getLastName().trim() + ", " + studentProfile.getFirstName().trim());
               
        form.setSelectedStudentId(null);  // indicate = add new student
        
        this.savedForm = form.createClone();     
        
        TestSessionUtils.setDefaultLevels(this.defaultSubtests, this.selectedSubtests);                                                                                
        
        return new Forward("success", this.savedForm);
    }

  //START- FORM RECOMMENDATION
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="listOfItem.jsp")
	})
	protected Forward goto_student_registration_popup(RegistrationForm form){
			
		String jsonResponse = "";
		Integer  studentId = new Integer(0);
		if(getRequest().getParameter("studentId") != null && !getRequest().getParameter("studentId").equals("")){
			studentId = Integer.valueOf(getRequest().getParameter("studentId"));
		}
		else {
			studentId = this.savedForm.selectedStudentId;
		}
		
		StudentSessionStatus [] scr =  getStudentPopUpDetails(studentId);
		if(scr.length > 0) {
			if(scr[0].getRecommendedProductId() != null && !scr[0].getRecommendedProductId().equals("")
					&& scr[0].getProductId() != null && !scr[0].getProductId().equals("")) {
				this.recommendedProductId = scr[0].getRecommendedProductId();
				this.productId = scr[0].getProductId();
			}
		}
		try {
			jsonResponse = JsonStudentUtils.getJson(scr, "studentSessionData",scr.getClass());
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
     * @jpf:action
     */
	@Jpf.Action()
    protected Forward goto_recommended_find_test_sessions(RegistrationForm form)
    {
        try {
        	this.searchInfo = form.getStudentProfile();
            this.savedForm = form.createClone();   
            this.savedForm.setTestAdminId(this.testAdminId);
            String contextPath = "/TestSessionInfoWeb/viewtestsessions/goto_recommended_find_test_sessions.do";
			String selectedProduct = "NONE";
			selectedProduct = this.recommendedProductId.toString();
			String selectedProductId = "selectedProductId=" +
			selectedProduct;
            String studentId = form.getSelectedStudentId().toString();
            String selectedStudentId = "studentId=" +
            studentId;      
            String requestFromFindStudent = "requestFromFindStudent=false";            
            String url = contextPath + "?" + selectedStudentId + "&" + selectedProductId + "&" + requestFromFindStudent;         
            getResponse().sendRedirect(url);
        } 
        catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
	
	 /**
     *getRubricDetails() for rubricView
     */
    private StudentSessionStatus[] getStudentPopUpDetails(Integer studentId){

    	StudentSessionStatus [] StudentSessionStatus = null;
    	try {	
    		StudentSessionStatus = this.studentManagement.getStudentMostResentSessionDetail(studentId);
    	}
    	catch(CTBBusinessException be){
    		be.printStackTrace();
    	}
    	return StudentSessionStatus;
    }
	
	//END- FORM RECOMMENDATION
    private void handleFindStudent(RegistrationForm form) 
    {
        form.validateValues();
        
        String currentAction = form.getCurrentAction();                        
        if ((currentAction != null) && currentAction.equals(ACTION_APPLY_SEARCH))
        {
            
            ManageStudentData msData = findByStudentProfile(form);    

            if ((msData != null) && (msData.getFilteredCount().intValue() > 0))
            {
                            
                List studentList = StudentSearchUtils.buildStudentList(msData);
                
                boolean studentInSession = setStudentsInSession(studentList);
                if (studentInSession)
                {
                    this.getRequest().setAttribute("studentInSession", MessageResourceBundle.getMessage("StudentInSession"));        
                }
                
                PagerSummary studentPagerSummary = StudentSearchUtils.buildStudentPagerSummary(msData, form.getStudentPageRequested());        
                form.setStudentMaxPage(msData.getFilteredPages());
                                                           
                this.getRequest().setAttribute("studentList", studentList);        
                this.getRequest().setAttribute("studentPagerSummary", studentPagerSummary);
                
            }       
            else
            {
                this.getRequest().setAttribute("searchResultEmpty", MessageResourceBundle.getMessage("SearchResultEmpty"));        
            }     
        }
        
        if ((currentAction != null) && currentAction.equals(ACTION_CLEAR_SEARCH))
        {
            form.clearSearch();
        }
    }
    //START- Form Recommendation
    /**
     * @jpf:action
     */
	@Jpf.Action()
    protected Forward goto_to_find_student(RegistrationForm form)
    {
        try {
        	String contextPath = "/StudentManagementWeb/manageStudent/returnToFindStudent.do";
        	 String studentId = this.savedForm.getSelectedStudentId().toString();
            String selectedStudentId = "studentId=" +
            studentId;            
            String url = contextPath + "?" + selectedStudentId;         
            getResponse().sendRedirect(url);
        } 
        catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
	//END- Form Recommendation

    private boolean setStudentsInSession(List studentList) 
    {
        boolean inSession = false;
        
        SessionStudent[] sss = TestSessionUtils.getSessionStudents(this.scheduleTest, this.userName, this.testAdminId);
        
        for (int i=0; i < studentList.size(); i++)
        {
            StudentProfileInformation student = (StudentProfileInformation)studentList.get(i);
            for (int j=0; j < sss.length; j++)
            {
                SessionStudent ss = sss[j];    
                if (student.getStudentId().intValue() == ss.getStudentId().intValue())
                {
                    student.setSelectable("false");
                    inSession = true;
                }
            }
        }
        return inSession;
    }


    private void handleAddStudent(RegistrationForm form) 
    {
        form.validateValues();

        String actionElement = form.getActionElement();        
        form.resetValuesForAction(actionElement);        
                          
        String orgNodeName = form.getOrgNodeName();
        Integer orgNodeId = form.getOrgNodeId();   
        boolean nodeChanged = StudentPathListUtils.adjustOrgNodePath(this.orgNodePath, orgNodeId, orgNodeName);

        if (nodeChanged)
        {
            form.resetValuesForPathList();
        }
        
        FilterParams filter = null;
        PageParams page = FilterSortPageUtils.buildPageParams(form.getOrgPageRequested(), FilterSortPageUtils.PAGESIZE_5);
        SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgSortColumn(), form.getOrgSortOrderBy(), null, null);
        
        OrganizationNodeData ond = StudentPathListUtils.getOrganizationNodes(this.userName, this.studentManagement, orgNodeId, filter, page, sort);

        this.orgNodeList = StudentPathListUtils.buildOrgNodeList(ond);
        String orgCategoryName = StudentPathListUtils.getOrgCategoryName(this.orgNodeList);
        
        PagerSummary orgPagerSummary = StudentPathListUtils.buildOrgNodePagerSummary(ond, form.getOrgPageRequested());        
        form.setOrgMaxPage(ond.getFilteredPages());

        if (actionElement.equals("{actionForm.actionElement}"))
        {
            PathNode node = StudentPathListUtils.findOrgNode(this.orgNodeList, form.getSelectedOrgNodeId());
            if (node != null)
            {
                form.setSelectedOrgNodeId(node.getId());
                form.setSelectedOrgNodeName(node.getName());                
            }
        }
        

        this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
        this.getRequest().setAttribute("orgNodes", this.orgNodeList);        
        this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
        this.getRequest().setAttribute("orgCategoryName", orgCategoryName);   
        
        //GACRCT2010CR007- retrieve value for Disable_Mandatory_Birth_Date 
        isMandatoryBirthDate();
        
    }
    
    

            
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** MODIFY TEST ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="modify_test.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "modify_test.jsp")
    })
    protected Forward modifyTest(RegistrationForm form)
    {       
        Integer studentId = form.getSelectedStudentId();        
        String currentAction = form.getCurrentAction();
        
        if ((currentAction != null) && (currentAction.equals(ACTION_AUTOLOCATOR) || currentAction.equals(ACTION_SUBTEST_VALIDATION_FAILED)))
        {
            List tempList = TestSessionUtils.cloneSubtests(this.defaultSubtests);
            TestSessionUtils.copySubtestLevel(this.selectedSubtests, tempList); 
            this.selectedSubtests = TestSessionUtils.retrieveSelectedSubtestsFromRequest(this.getRequest(), tempList);        
            this.availableSubtests = TestSessionUtils.getAvailableSubtests(tempList, this.selectedSubtests);                                
            this.defaultSubtests = TestSessionUtils.sortSubtestList(tempList, this.selectedSubtests); 
        }
        
        if (this.orgNodeNames.size() > 1)
        {
            this.getRequest().setAttribute("showDropDown", "true");
        }
        
        
        this.getRequest().setAttribute("testAdminName", this.scheduledSession.getTestSession().getTestAdminName());
        this.getRequest().setAttribute("studentName", this.student.getDisplayName());
        this.getRequest().setAttribute("allSubtests", this.defaultSubtests);
        this.getRequest().setAttribute("availableSubtests", this.availableSubtests);
        this.getRequest().setAttribute("selectedSubtests", this.selectedSubtests);
        this.getRequest().setAttribute("levelOptions", this.levelOptions);

        Boolean autoLocatorChecked = Boolean.FALSE;        
        if ((form.getAutoLocator() != null) && form.getAutoLocator().equals("true"))
        {
            autoLocatorChecked = Boolean.TRUE;
        }
        this.getRequest().setAttribute("checked", autoLocatorChecked);
        
        if (! autoLocatorChecked.booleanValue())
        {            
            if (studentId != null)
            {
                Integer itemSetId = this.scheduledSession.getTestSession().getItemSetId();
                Integer locatorItemSetId = null;
                if (this.locatorSubtest != null)
                {
                    locatorItemSetId = this.locatorSubtest.getId();
                }
                if (! TestSessionUtils.setRecommendedLevelForStudent(this.scheduleTest, this.userName, studentId, itemSetId, locatorItemSetId, this.selectedSubtests))
                {
                    if (! this.returnFromCongratulation)
                    {
                        TestSessionUtils.setDefaultLevels(this.defaultSubtests, this.selectedSubtests);                                                                                
                    }
                }
            }
            else
            {
                TestSessionUtils.setDefaultLevels(this.defaultSubtests, this.selectedSubtests);                                                                                
            }            
        }
                    
        Boolean showLevel = autoLocatorChecked.booleanValue() ? Boolean.FALSE : Boolean.TRUE;      

        if (this.locatorSubtest != null)
        {
            this.getRequest().setAttribute("subtestName", this.locatorSubtest.getSubtestName());
            this.getRequest().setAttribute("levelName", this.locatorSubtest.getLevel());
            this.getRequest().setAttribute("hasLocatorSubtest", Boolean.TRUE);
        }
        else
        {
            showLevel = Boolean.FALSE;
        }
        
        this.getRequest().setAttribute("showLevel", showLevel);
       
        if (this.defaultSubtests.size() > 0)
        {
            SubtestVO subtest = (SubtestVO)this.defaultSubtests.get(0);
            if (!"1".equals(subtest.getLevel()))
                this.getRequest().setAttribute("multipleSubtest", Boolean.TRUE);
        }
        
        String locatorSessionInfo = "";
        if ((studentId != null) && showLevel.booleanValue() && (! this.isLocatorTest))
        {
            Integer itemSetId = this.scheduledSession.getTestSession().getItemSetId();
            Integer locatorItemSetId = null;
            if (this.locatorSubtest != null)
            {
                locatorItemSetId = this.locatorSubtest.getId();
            }
            locatorSessionInfo = TestSessionUtils.getLocatorSessionInfo(this.scheduleTest, this.userName, studentId, itemSetId, locatorItemSetId, this.selectedSubtests);                                                                                
        }
        if (locatorSessionInfo.length() > 0)
        {
            this.getRequest().setAttribute("locatorSessionInfo", locatorSessionInfo);
        }
        
        this.getRequest().setAttribute("hideBackButton", new Boolean(this.returnFromCongratulation));
       
        this.getRequest().setAttribute("isLocatorTest", new Boolean(this.isLocatorTest));
        
        setFormInfoOnRequest(form);
        return new Forward("success", form);
    }


    /**
     * @jpf:action
     * @jpf:forward name="success" path="enterStudent.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "enterStudent.do"),
                     @Jpf.Forward(name = "go_back_to_recommended_find_test_sessions",
                             path = "go_back_to_recommended_find_test_sessions.do")
    })
    protected Forward backToEnterStudent(RegistrationForm form)
    {
       //START- Form Recommendation
    	if(this.isFormRecommended){
    		
    		return new Forward("go_back_to_recommended_find_test_sessions", this.savedForm);
    		
    	}
    	//END- Form Recommendation
        initTestStructure(this.savedForm);
        
        this.savedForm.setMessage(null, null, null);
        this.savedForm.setCurrentAction(ACTION_APPLY_SEARCH);
        this.savedForm.setActionElement(ACTION_DEFAULT);
        
        return new Forward("success", this.savedForm);
    }
    
    ///START- FORM RECOMMENDATION
    /**
     * @jpf:action
     * @jpf:forward name="success" path="enterStudent.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "enterStudent.do")
    })
    protected Forward backToRegisterStudent(RegistrationForm form)
    {	
    	this.testAdminId = this.savedForm.testAdminId;
        this.scheduledSession = TestSessionUtils.getTestSessionDataWithoutRoster(this.scheduleTest, this.userName, this.testAdminId);
        TestSession testSession = this.scheduledSession.getTestSession();
        this.itemSetId = testSession.getItemSetId();
        initTestStructure(this.savedForm);
        this.currentSelectedTab = ACTION_FIND_STUDENT;
        this.savedForm.setMessage(null, null, null);
        this.savedForm.setCurrentAction(ACTION_APPLY_SEARCH);
        this.savedForm.setActionElement(ACTION_DEFAULT);
        
        return new Forward("success", this.savedForm);
    }
    ///END- FORM RECOMMENDATION
    //START- Form Recommendation
    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward go_back_to_recommended_find_test_sessions(RegistrationForm form)
    {
        
    	try {
    		String requestFromFindStudent = "requestFromFindStudent=" +
			this.requestFromFindStudent;
        	String contextPath = "/TestSessionInfoWeb/viewtestsessions/goto_recommended_find_test_sessions.do";
			String url = contextPath + "?" + requestFromFindStudent ;         
            getResponse().sendRedirect(url);
        } 
        catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
    //END- Form Recommendation

    /**
     * @jpf:action
     * @jpf:forward name="success" path="congratulations.do"
     * @jpf:forward name="error" path="modifyTest.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "congratulations.do"), 
        @Jpf.Forward(name = "error",
                     path = "modifyTest.do")
    })
    protected Forward nextToCongratulations(RegistrationForm form)
    {
        try
        {
            this.studentOrgId = this.studentOrgNodes[0].getOrgNodeId();
            String orgNodeName = form.getSelectedOrgNodeName();
            if ((orgNodeName != null) && (this.orgNodeNames.size() > 1))
            {
                for (int i=0; i < this.studentOrgNodes.length; i++)
                {
                    OrganizationNode node = this.studentOrgNodes[i];
                    if (node.getOrgNodeName().equals(orgNodeName))
                    {
                        this.studentOrgId = this.studentOrgNodes[i].getOrgNodeId();
                        break;
                    }                
                }
            }
           
            List tempList = TestSessionUtils.cloneSubtests(this.defaultSubtests);
            TestSessionUtils.copySubtestLevel(this.selectedSubtests, tempList); 
            this.selectedSubtests = TestSessionUtils.retrieveSelectedSubtestsFromRequest(this.getRequest(), tempList);        
            this.availableSubtests = TestSessionUtils.getAvailableSubtests(tempList, this.selectedSubtests); //change done for defect 63097
               
            String autoLocator = form.getAutoLocator();
            boolean autoLocatorChecked = ((autoLocator != null) && autoLocator.equals("true"));
               
            if (! this.isLocatorTest)
            {      
                        
                boolean validateLevels = !autoLocatorChecked;    
                boolean valid = TABESubtestValidation.validation(this.selectedSubtests, validateLevels);
                String message = TABESubtestValidation.currentMessage;
                        
                if (! valid)
                { 
                    this.savedForm.setMessage(MessageResourceBundle.getMessage("SubtestValidationFailed"), message, Message.ERROR);      
                    this.savedForm.setAutoLocator(form.getAutoLocator());     
                    this.savedForm.setCurrentAction(ACTION_SUBTEST_VALIDATION_FAILED);   
                    this.savedForm.setSelectedOrgNodeName(form.getSelectedOrgNodeName()); 
                    return new Forward("error", this.savedForm);            
                }
                else
                {
                    if (! message.equals(TABESubtestValidation.NO_ERROR_MSG))
                    {
                        form.setMessage(MessageResourceBundle.getMessage("SubtestValidationWarning"), message, Message.ALERT);            
                    }
                }              
            }
            
    	
            if (this.selectedSubtests.size() == 0)
            {
                this.selectedSubtests = TestSessionUtils.cloneSubtests(this.defaultSubtests);
            }
                            
            // add a new student if needed
            Integer studentId = this.savedForm.getSelectedStudentId();        
            if (studentId == null)
            {
                studentId = createNewStudent(this.savedForm.getStudentProfile(), this.studentOrgId); 
                this.student = StudentSearchUtils.getStudentProfileInformation(this.studentManagement, this.userName, studentId);
            }
            else
            {
                this.student = getStudent(studentId);
            }
            
            this.savedForm.setSelectedStudentId(studentId);
            form.setSelectedStudentId(studentId);
        
            List studentSubtests = TestSessionUtils.cloneSubtests(this.selectedSubtests);
              
            if (autoLocatorChecked)
            {            
                TestSessionUtils.restoreLocatorSubtest(studentSubtests, this.locatorSubtest);
            }
            else
            {
                if (this.isLocatorTest)
                    TestSessionUtils.setDefaultLevels(studentSubtests, "1");  // make sure set level = '1' for test locator
                else if (this.scheduledSession.getTestSession().getProductId() != null && this.scheduledSession.getTestSession().getProductId().intValue() == 4013)
                {
                    TestSessionUtils.setDefaultLevels(studentSubtests, "1");  // make sure set level = '1' for tutorial test
                }
                else
                    TestSessionUtils.setDefaultLevels(studentSubtests, "E");  // make sure set level = 'E' if null
            }
    
              
            // add this student into roster     
            TestSession testSession = this.scheduledSession.getTestSession();
            Integer testAdminId = testSession.getTestAdminId();    
            Integer itemSetId = testSession.getItemSetId();   
    
            int subtestSize = studentSubtests.size();                 
            StudentManifest [] manifestArray = new StudentManifest[subtestSize];
                
            for (int i=0; i < subtestSize; i++)
            {
                SubtestVO subtest = (SubtestVO)studentSubtests.get(i);
                            
                StudentManifest manifest = new StudentManifest();                
                manifest.setItemSetId(subtest.getId());
                manifest.setItemSetName(subtest.getSubtestName());
                manifest.setItemSetForm(subtest.getLevel());
                manifest.setItemSetOrder(new Integer(i + 1));
                manifest.setTestAccessCode(subtest.getTestAccessCode());
                
                manifestArray[i] = manifest;            
            }
    
            RosterElement roster = new RosterElement(); 
            
            List sstList = TestSessionUtils.getStudentSubtests(this.scheduleTest, this.userName, studentId, testAdminId);        
            if (sstList.size() > 0)
            {
                // student already in session, update student roster
                StudentManifestData manifestData = new StudentManifestData();
                manifestData.setStudentManifests(manifestArray, new Integer(manifestArray.length));
                roster = TestSessionUtils.updateManifestForRoster(this.scheduleTest, this.userName, studentId, this.studentOrgId, testAdminId, manifestData);
            }
            else
            {
                // student not in session, add student roster
                SessionStudent ss = new SessionStudent();
                ss.setStudentId(studentId);
                ss.setOrgNodeId(this.studentOrgId);
                ss.setStudentManifests(manifestArray);
                roster = TestSessionUtils.addStudentToSession(this.scheduleTest, this.userName, ss, testAdminId);
            }
            
            // set current into form before goto next step
            form.setStudentProfile(this.student);
            form.setPassword(roster.getPassword());
            
            form.setStudentSectionVisible(Boolean.TRUE);            
            form.setTestSectionVisible(Boolean.TRUE);            
            form.setOptionSectionVisible(Boolean.TRUE);            
            form.setTestStructureSectionVisible(Boolean.TRUE);            
            form.setProctorSectionVisible(Boolean.TRUE);            
            form.setReportSectionVisible(Boolean.TRUE); 
                       
           
            return new Forward("success", form);
        }
        catch (InsufficientLicenseQuantityException e)
        {
            e.printStackTrace();
            //START - Added for Deferred Defect 63097
            form.setMessage(MessageResourceBundle.getMessage("SelectSettings.InsufficentLicenseQuantity.E001"),Message.INSUFFICENT_LICENSE_QUANTITY, Message.ERROR);            
            //END - Added for Deferred Defect 63097
            String errorMessage = MessageResourceBundle.getMessage("SelectSettings.InsufficentLicenseQuantity", e.getMessage());
            this.getRequest().setAttribute("errorMessage", errorMessage); 
            return new Forward("error", form);            
        }
        catch (CTBBusinessException e)
        {
            e.printStackTrace();
            String errorMessage = MessageResourceBundle.getMessage("SelectSettings.FailedToSaveTestSession", e.getMessage());
            this.getRequest().setAttribute("errorMessage", errorMessage); 
            return new Forward("error", form);            
        }
    }
      
    /**
     * createNewStudent
     */
    private Integer createNewStudent(StudentProfileInformation studentProfile, Integer studentOrgId)
    {
        Integer studentId = new Integer(0);
        List orgNodes = new ArrayList();
        for (int i=0; i < this.orgNodeList.size(); i++)
        {
            PathNode pathNode = (PathNode)this.orgNodeList.get(i);
            if (pathNode.getId().intValue() == studentOrgId.intValue())
            {
                orgNodes.add(pathNode);
                break;
            }
        }
        
        ManageStudent student = studentProfile.makeCopy(studentId, orgNodes);
        
        try
        {                    
            studentId = this.studentManagement.createNewStudent(this.userName, student);
            
            if (studentId != null)
            {
                boolean result = saveStudentAccommodations(studentId);
            }
        }
        catch (StudentDataCreationException sde)
        {
            sde.printStackTrace();
        }        
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }                    
        return studentId;
    }

    /**
     * saveStudentAccommodation
     */
    private boolean saveStudentAccommodations(Integer studentId)
    {
        StudentAccommodations sa = new StudentAccommodations();
        
        sa = getStudentDefaultAccommodations(studentId);
        
        createStudentAccommodations(studentId, sa);
        return true;
    }
    
    /**
     * getStudentDefaultAccommodations
     */
    private StudentAccommodations getStudentDefaultAccommodations(Integer studentId)
    {
        getCustomerConfigurations();
        StudentAccommodations stuAcc = new StudentAccommodations();
        stuAcc.setStudentId(studentId);
        for (int i=0; i < this.customerConfigurations.length; i++)
        {
            CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
            String ccName = cc.getCustomerConfigurationName();
            String defaultValue = cc.getDefaultValue() != null ? cc.getDefaultValue() : "F";
           
            if (ccName.equalsIgnoreCase("screen_reader"))
            {
                if (defaultValue.equalsIgnoreCase("T"))
                {
                    stuAcc.setScreenReader("T");
                }
                else
                {
                    stuAcc.setScreenReader("F");
                }
            }
            if (ccName.equalsIgnoreCase("calculator"))
            {
                if (defaultValue.equalsIgnoreCase("T"))
                {
                    stuAcc.setCalculator("T");
                }
                else
                {
                    stuAcc.setCalculator("F");
                }
            }
            if (ccName.equalsIgnoreCase("test_pause"))
            {
                if (defaultValue.equalsIgnoreCase("T"))
                {
                    stuAcc.setTestPause("T");
                }
                else
                {
                    stuAcc.setTestPause("F");
                }
            }
            if (ccName.equalsIgnoreCase("untimed_test"))
            {
                if (defaultValue.equalsIgnoreCase("T"))
                {
                    stuAcc.setUntimedTest("T");
                }
                else
                {
                    stuAcc.setUntimedTest("F");
                }
            }
            if (ccName.equalsIgnoreCase("highlighter"))
            {
                if (defaultValue.equalsIgnoreCase("T"))
                {
                    stuAcc.setHighlighter("T");
                }
                else
                {
                    stuAcc.setHighlighter("F");
                }
            }
        }
        return stuAcc;
    }
   
    /**
     * getCustomerConfigurations
     */
    private void getCustomerConfigurations()
    {
        try
        {
            //if (this.customerConfigurations == null)
            //{
                this.customerConfigurations = this.studentManagement.getCustomerConfigurations(this.userName, this.customerId);
            //}
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
    }
    
    /*
	 * GACRCT2010CR007- retrieve value for disableMandatoryBirthdate set  Value in request. 
	 */
    private void isMandatoryBirthDate() 
    {     
    	boolean disableMandatoryBirthdateValue = false;
        for (int i=0; i < this.customerConfigurations.length; i++)
         {
             CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
             if (cc.getCustomerConfigurationName().equalsIgnoreCase("Disable_Mandatory_Birth_Date") && cc.getDefaultValue().equalsIgnoreCase("T"))
             {
            	 
             	disableMandatoryBirthdateValue = true; 
             }
          }
        disableMandatoryBirthdate = disableMandatoryBirthdateValue;
        System.out.println("Student registration disableMandatoryBirthdate==>"+disableMandatoryBirthdate);
        this.getRequest().setAttribute("isMandatoryBirthDate", disableMandatoryBirthdate);
                
     }
    /**
     * createStudentAccommodations
     */
    private void createStudentAccommodations(Integer studentId, StudentAccommodations sa)
    {
           
        if ((studentId != null) && (studentId.intValue() > 0))
        {
                
            try
            {    
                this.studentManagement.createStudentAccommodations(this.userName, sa);
            }
            catch (CTBBusinessException be)
            {
                be.printStackTrace();
            }        
        }
    }

    /**
     * getStudent
     */
    private StudentProfileInformation getStudent(Integer studentId)
    {
        StudentProfileInformation studentProfileInfo = new StudentProfileInformation();                 
        try
        {                    
            ManageStudent student = this.studentManagement.getManageStudent(this.userName, studentId);
            studentProfileInfo = new StudentProfileInformation(student);   
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }                    
        return studentProfileInfo;
    }

          
        
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** CONGRATULATIONS ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="congratulations.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "congratulations.jsp")
    })
    protected Forward congratulations(RegistrationForm form)
    {
        TestSession testSession = this.scheduledSession.getTestSession();
        TestElement [] testElements = this.scheduledSession.getScheduledUnits();
        String testAccessCode = testElements[0].getAccessCode();
        
        Integer studentId = this.student.getStudentId();
        String studentName = this.student.getDisplayName();
        String loginName = this.student.getUserName();
                
        String password = form.getPassword();
        String testAdminId = testSession.getTestAdminId().toString();
        String orgNodeId = testSession.getCreatorOrgNodeId().toString();
        String testAdminName = testSession.getTestAdminName();
        String testName = testSession.getTestName();
        String sessionNumber = testSession.getSessionNumber();
        String creatorOrgNodeName = testSession.getCreatorOrgNodeName();
        
        String startDate = DateUtils.formatDateToDateString(testSession.getLoginStartDate());
        String endDate = DateUtils.formatDateToDateString(testSession.getLoginEndDate());
        String startTime = DateUtils.formatDateToTimeString(testSession.getDailyLoginStartTime());
        String endTime = DateUtils.formatDateToTimeString(testSession.getDailyLoginEndTime());
        
        String enforceBreak = testSession.getEnforceBreak();
        String location = testSession.getLocation();
        
        User [] proctors = this.scheduledSession.getProctors();
        List selectedProctors = new ArrayList();
        boolean foundScheduler = false;
        for (int i=0; i < proctors.length; i++)
        {
            User user = proctors[i];
            selectedProctors.add(user);
        }

        this.getRequest().setAttribute("studentId", studentId.toString());
        this.getRequest().setAttribute("studentName", studentName);
        this.getRequest().setAttribute("loginName", loginName);

        this.getRequest().setAttribute("testAdminId", testAdminId);
        this.getRequest().setAttribute("orgNodeId", this.studentOrgId.toString());
        this.getRequest().setAttribute("password", password);
        
        this.getRequest().setAttribute("testName", testName);
        this.getRequest().setAttribute("testAdminName", testAdminName);
        this.getRequest().setAttribute("testAccessCode", testAccessCode);
        this.getRequest().setAttribute("sessionNumber", sessionNumber);
        this.getRequest().setAttribute("creatorOrgNodeName", creatorOrgNodeName);

        this.getRequest().setAttribute("startDate", startDate);
        this.getRequest().setAttribute("endDate", endDate);
        this.getRequest().setAttribute("startTime", startTime);
        this.getRequest().setAttribute("endTime", endTime);
        this.getRequest().setAttribute("showAccessCode", customerHasAccessCode()); // Added for TABE BAUM - 028
        
        if (this.locatorSubtest != null) {            
            this.getRequest().setAttribute("hasLocatorSubtest", Boolean.TRUE);
        }

        this.getRequest().setAttribute("isLocatorTest", new Boolean(this.isLocatorTest));

        if (this.isLocatorTest && (this.orgNodeNames.size() == 1)) {
            this.getRequest().setAttribute("hideModifyTestButton", "true");
        }
        
        if ((enforceBreak != null) && (enforceBreak.equalsIgnoreCase("T"))) { 
            this.getRequest().setAttribute("enforceBreak", "Yes");
        }
        else {
            this.getRequest().setAttribute("enforceBreak", "No");
        }
        
        this.getRequest().setAttribute("selectedProctors", selectedProctors);
                        
        this.getRequest().setAttribute("selectedSubtestList", this.selectedSubtests);
        
        //To remove netui-compat
        
        setFormInfoOnRequest(form);
        
        return new Forward("success", form);
    }

    
    /**
     * @jpf:action
     */
	@Jpf.Action()
    protected Forward gotoHomePage(RegistrationForm form)
    {
        try {
            getResponse().sendRedirect("/TestSessionInfoWeb/homepage/HomePageController.jpf");
        } catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }
        
        return null;
    }
    
        
    /**
     * initTab
     */
    private void initTab(String selectedTab, RegistrationForm form)
    {
        this.currentSelectedTab = selectedTab;
        
        form.initTab();    
        
        this.orgNodePath = new ArrayList();
        
        form.setCurrentAction(ACTION_DEFAULT);    
        
        this.gradeOptions = getGradeOptions(selectedTab);
        this.genderOptions = getGenderOptions(selectedTab);     
        
        StudentProfileInformation studentProfile = form.getStudentProfile();
        studentProfile.setGrade(this.gradeOptions[0]);
        studentProfile.setGender(this.genderOptions[0]);       
        studentProfile.setMonth(this.monthOptions[0]);
        studentProfile.setDay(this.dayOptions[0]);
        studentProfile.setYear(this.yearOptions[0]);                
    }
        
    
    /**
     * getGenderOptions
     */
    private String [] getGenderOptions(String action)
    {
        List options = new ArrayList();
        if ( action.equals(ACTION_FIND_STUDENT) )
            options.add(FilterSortPageUtils.FILTERTYPE_ANY_GENDER);
        if ( action.equals(ACTION_ADD_STUDENT) || action.equals(ACTION_ADD_STUDENT) )
            options.add(FilterSortPageUtils.FILTERTYPE_SELECT_A_GENDER);
        
        options.add("Male");
        options.add("Female");
        options.add("Unknown");
                
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
        if ( action.equals(ACTION_ADD_STUDENT) || action.equals(ACTION_ADD_STUDENT) )
            options.add(FilterSortPageUtils.FILTERTYPE_SELECT_A_GRADE);

        for (int i=0 ; i<grades.length ; i++) {        
            options.add(grades[i]);
        }
                        
        return (String [])options.toArray(new String[0]);        
    }
    

    /**
     * findByStudentProfile
     */
    private ManageStudentData findByStudentProfile(RegistrationForm form)
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

        if (! gender.equals(FilterSortPageUtils.FILTERTYPE_ANY_GENDER)) {
            if (gender.equals("Male")) gender = "M";
            else
            if (gender.equals("Female")) gender = "F";
            else
                gender = "U";
        }

        String invalidCharFields = WebUtils.verifyFindStudentInfo(firstName, lastName, middleName, studentNumber, loginId);                

        if (invalidCharFields.length() > 0) {
            invalidCharFields += ("<br/>" + Message.INVALID_CHARS);
            form.setMessage(MessageResourceBundle.getMessage("InvalidCharacters"), invalidCharFields, Message.ERROR);
            return null;
        }

        PageParams page = FilterSortPageUtils.buildPageParams(form.getStudentPageRequested(), FilterSortPageUtils.PAGESIZE_10);
        SortParams sort = FilterSortPageUtils.buildStudentSortParams(form.getStudentSortColumn(), form.getStudentSortOrderBy());
        FilterParams filter = FilterSortPageUtils.buildFilterParams(firstName, middleName, lastName,
                                                                    loginId, studentNumber, grade, gender);
        

        ManageStudentData msData = null;
        
        if (filter == null) {
            msData = StudentSearchUtils.searchAllStudentsAtAndBelow(this.userName, this.studentManagement, page, sort);   
        }
        else {
            msData = StudentSearchUtils.searchStudentsByProfile(this.userName, this.studentManagement, filter, page, sort);   
        }
        
        return msData;
    }
    
    /*
	 * set form Value in request
	 */
	private void setFormInfoOnRequest(RegistrationForm form ) {


		this.getRequest().setAttribute("pageMessage", form.getMessage());
	}
    
    
    
    

    
 
    
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** REGISTRATIONFORM ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class RegistrationForm extends SanitizedFormData
    {
        private String actionElement;
        private String currentAction;
        private String selectedTab;
        
        // student profile
        private StudentProfileInformation studentProfile;
        private Integer selectedStudentId;
        private String password;
        
        
        // student pager
        private String studentSortColumn;
        private String studentSortOrderBy;
        private Integer studentPageRequested;
        private Integer studentMaxPage;
        
        // org pager
        private String orgSortColumn;
        private String orgSortOrderBy;
        private Integer orgPageRequested;
        private Integer orgMaxPage;
        
        // find all students
        private String orgNodeName;
        private Integer orgNodeId;
        private Integer selectedOrgNodeId;
        private String selectedOrgNodeName;
        
        private Message message;
        
        private Boolean studentSectionVisible;
        private Boolean testSectionVisible;
        private Boolean optionSectionVisible;
        private Boolean testStructureSectionVisible;
        private Boolean proctorSectionVisible;
        private Boolean reportSectionVisible;
        private Integer testAdminId; 
        private String autoLocator;
        private  boolean disableMandatoryBirthdate = false; //GACRCT2010CR007 - Disable Mandatory Birth Date
        
        public RegistrationForm()
        {
        }
        
        public void init()
        {
            this.actionElement = ACTION_DEFAULT;
            this.currentAction = ACTION_FIND_STUDENT;
            this.selectedTab = ACTION_FIND_STUDENT;            
            
            this.autoLocator = null;
            
            initTab();            
        }
        
        public void initTab()
        {
            // init add tab            
            this.orgNodeName = "Top";
            this.orgNodeId = new Integer(0);
            this.selectedOrgNodeId = null;
            this.selectedOrgNodeName = null;
            
            this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
            this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.orgPageRequested = new Integer(1);                                 
            this.orgMaxPage = new Integer(1);      
            
            // init find tab            
            this.studentSortColumn = FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN;
            this.studentSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.studentPageRequested = new Integer(1);      
            this.studentMaxPage = new Integer(1);      
            
            clearSearch(); 
            
            this.selectedStudentId = null;       
            
            this.studentSectionVisible = Boolean.TRUE;
            this.testSectionVisible = Boolean.TRUE;
            this.optionSectionVisible = Boolean.TRUE;
            this.testStructureSectionVisible = Boolean.TRUE;
            this.proctorSectionVisible = Boolean.TRUE;
            this.reportSectionVisible = Boolean.TRUE;
            
        }

        public void clearSearch()
        {   
            this.studentProfile = new StudentProfileInformation();
            this.studentProfile.setGrade(FilterSortPageUtils.FILTERTYPE_ANY_GRADE);
            this.studentProfile.setGender(FilterSortPageUtils.FILTERTYPE_ANY_GENDER);                        
            this.selectedStudentId = null;       
        }
        
        public void validateValues()
        {
            if (this.orgSortColumn == null)
                this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;

            if (this.orgSortOrderBy == null)
                this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;

            if (this.orgPageRequested == null)
                this.orgPageRequested = new Integer(1);
                
            if (this.orgPageRequested.intValue() <= 0) {
                this.orgPageRequested = new Integer(1);
                this.selectedOrgNodeId = null;
            }
            
            if (this.orgMaxPage == null)
                this.orgMaxPage = new Integer(1);

            if (this.orgPageRequested.intValue() > this.orgMaxPage.intValue()) {
                this.orgPageRequested = new Integer(this.orgMaxPage.intValue());
                this.selectedOrgNodeId = null;
            }
            
            if (this.studentSortColumn == null)
                this.studentSortColumn = FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN;

            if (this.studentSortOrderBy == null)
                this.studentSortOrderBy = FilterSortPageUtils.ASCENDING;

            if (this.studentPageRequested == null)
                this.studentPageRequested = new Integer(1);
                
            if (this.studentPageRequested.intValue() <= 0)
                this.studentPageRequested = new Integer(1);
                
            if (this.studentMaxPage == null)
                this.studentMaxPage = new Integer(1);

            if (this.studentPageRequested.intValue() > this.studentMaxPage.intValue()) {
                this.studentPageRequested = new Integer(this.studentMaxPage.intValue());                
                this.selectedStudentId = null;
            }                
        }    
         
         
        public RegistrationForm createClone()
        {
            RegistrationForm copied = new RegistrationForm();
            
            copied.setActionElement(this.actionElement);
            copied.setCurrentAction(this.currentAction);
            copied.setSelectedTab(this.selectedTab);
            
            copied.setStudentSortColumn(this.studentSortColumn);
            copied.setStudentSortOrderBy(this.studentSortOrderBy);
            copied.setStudentPageRequested(this.studentPageRequested);      
            copied.setStudentMaxPage(this.studentMaxPage);
                        
            copied.setStudentProfile(this.studentProfile);
            copied.setSelectedStudentId(this.selectedStudentId);
                        
            copied.setOrgSortColumn(this.orgSortColumn);
            copied.setOrgSortOrderBy(this.orgSortOrderBy);
            copied.setOrgPageRequested(this.orgPageRequested);
            copied.setOrgMaxPage(this.orgMaxPage);
                                    
            copied.setOrgNodeName(this.orgNodeName);
            copied.setOrgNodeId(this.orgNodeId);
            copied.setSelectedOrgNodeId(this.selectedOrgNodeId);
            copied.setSelectedOrgNodeName(this.selectedOrgNodeName);
            
            copied.setAutoLocator(this.autoLocator);
                                    
            return copied;                    
        }        
        public void resetValuesForPathList()
        {
            this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
            this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.orgPageRequested = new Integer(1);    
            this.orgMaxPage = new Integer(1);      

            this.studentSortColumn = FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN;
            this.studentSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.studentPageRequested = new Integer(1);    
            this.studentMaxPage = new Integer(1);      
        }     
        public void resetValuesForAction(String actionElement) 
        {
            if (actionElement.equals("{actionForm.orgSortOrderBy}")) {
                this.orgPageRequested = new Integer(1);
                this.selectedStudentId = null;
            }
            if (actionElement.equals("{actionForm.studentSortOrderBy}")) {
                this.studentPageRequested = new Integer(1);
                this.selectedStudentId = null;
            }
            if (actionElement.equals("{actionForm.studentPageRequested}")) {
                this.selectedStudentId = null;
            }
        }
        public void resetValuesForAction(String actionElement, String fromAction) 
        {
            if (actionElement.equals("{actionForm.orgSortOrderBy}")) {
                this.orgPageRequested = new Integer(1);
                this.selectedStudentId = null;
            }
            if (actionElement.equals("{actionForm.studentSortOrderBy}")) {
                this.studentPageRequested = new Integer(1);
                this.selectedStudentId = null;
            }
            if (actionElement.equals("{actionForm.studentPageRequested}")) {
                this.selectedStudentId = null;
            }
            if (actionElement.equals("ButtonGoInvoked_studentSearchResult") ||
                actionElement.equals("EnterKeyInvoked_studentSearchResult")) {
                this.selectedStudentId = null;
            }
            if (actionElement.equals("ButtonGoInvoked_tablePathListAnchor") ||
                actionElement.equals("EnterKeyInvoked_tablePathListAnchor")) {
                this.selectedOrgNodeId = null;
                if (fromAction.equals(ACTION_FIND_STUDENT))
                    this.selectedStudentId = null;
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
        
        public void setSelectedTab(String selectedTab)
        {
            this.selectedTab = selectedTab;
        }
        public String getSelectedTab()
        {
            return this.selectedTab != null ? this.selectedTab : "";
        }

        public void setStudentProfile(StudentProfileInformation studentProfile)
        {
            this.studentProfile = studentProfile;
        }
        public StudentProfileInformation getStudentProfile()
        {
            if (this.studentProfile == null) 
                this.studentProfile = new StudentProfileInformation();
            return this.studentProfile;
        }
        public void setSelectedStudentId(Integer selectedStudentId)
        {
            this.selectedStudentId = selectedStudentId;
        }
        public Integer getSelectedStudentId()
        {
            return this.selectedStudentId;
        }

        public void setStudentSortColumn(String studentSortColumn)
        {
            this.studentSortColumn = studentSortColumn;
        }
        public String getStudentSortColumn()
        {
            return this.studentSortColumn != null ? this.studentSortColumn : FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN;
        }       
        public void setStudentSortOrderBy(String studentSortOrderBy)
        {
            this.studentSortOrderBy = studentSortOrderBy;
        }
        public String getStudentSortOrderBy()
        {
            return this.studentSortOrderBy != null ? this.studentSortOrderBy : FilterSortPageUtils.ASCENDING;
        }       
        public void setStudentPageRequested(Integer studentPageRequested)
        {
            this.studentPageRequested = studentPageRequested;
        }
        public Integer getStudentPageRequested()
        {
            return this.studentPageRequested != null ? this.studentPageRequested : new Integer(1);
        }        
        public void setStudentMaxPage(Integer studentMaxPage)
        {
            this.studentMaxPage = studentMaxPage;
        }
        public Integer getStudentMaxPage()
        {
            return this.studentMaxPage != null ? this.studentMaxPage : new Integer(1);
        }        
             
        public void setOrgSortColumn(String orgSortColumn)
        {
            this.orgSortColumn = orgSortColumn;
        }
        public String getOrgSortColumn()
        {
            return this.orgSortColumn != null ? this.orgSortColumn : FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
        }       
        public void setOrgSortOrderBy(String orgSortOrderBy)
        {
            this.orgSortOrderBy = orgSortOrderBy;
        }
        public String getOrgSortOrderBy()
        {
            return this.orgSortOrderBy != null ? this.orgSortOrderBy : FilterSortPageUtils.ASCENDING;
        }       
        public void setOrgPageRequested(Integer orgPageRequested)
        {
            this.orgPageRequested = orgPageRequested;
        }
        public Integer getOrgPageRequested()
        {
            return this.orgPageRequested != null ? this.orgPageRequested : new Integer(1);
        }        
        public void setOrgMaxPage(Integer orgMaxPage)
        {
            this.orgMaxPage = orgMaxPage;
        }
        public Integer getOrgMaxPage()
        {
            return this.orgMaxPage != null ? this.orgMaxPage : new Integer(1);
        }        
        
        public void setOrgNodeName(String orgNodeName)
        {
            this.orgNodeName = orgNodeName;
        }
        public String getOrgNodeName()
        {
            return this.orgNodeName;
        }
        public void setOrgNodeId(Integer orgNodeId)
        {
            this.orgNodeId = orgNodeId;
        }
        public Integer getOrgNodeId()
        {
            return this.orgNodeId;
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
            return this.selectedOrgNodeName;
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
        
        public void setStudentSectionVisible(Boolean studentSectionVisible)
        {
            this.studentSectionVisible = studentSectionVisible;
        }
        public Boolean getStudentSectionVisible()
        {
            return this.studentSectionVisible;
        }        
        public void setTestSectionVisible(Boolean testSectionVisible)
        {
            this.testSectionVisible = testSectionVisible;
        }
        public Boolean getTestSectionVisible()
        {
            return this.testSectionVisible;
        }        
        public void setOptionSectionVisible(Boolean optionSectionVisible)
        {
            this.optionSectionVisible = optionSectionVisible;
        }
        public Boolean getOptionSectionVisible()
        {
            return this.optionSectionVisible;
        }        
        public void setTestStructureSectionVisible(Boolean testStructureSectionVisible)
        {
            this.testStructureSectionVisible = testStructureSectionVisible;
        }
        public Boolean getTestStructureSectionVisible()
        {
            return this.testStructureSectionVisible;
        }        
        public void setProctorSectionVisible(Boolean proctorSectionVisible)
        {
            this.proctorSectionVisible = proctorSectionVisible;
        }
        public Boolean getProctorSectionVisible()
        {
            return this.proctorSectionVisible;
        }        
        public void setReportSectionVisible(Boolean reportSectionVisible)
        {
            this.reportSectionVisible = reportSectionVisible;
        }
        public Boolean getReportSectionVisible()
        {
            return this.reportSectionVisible;
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
        public void setPassword(String password)
        {
            this.password = password;
        }
        public String getPassword()
        {
            return this.password;
        }
        /**
		 * @return the disableMandatoryBirthdate
		 */
		public boolean isDisableMandatoryBirthdate() {
			return disableMandatoryBirthdate;
		}

		/**
		 * @param disableMandatoryBirthdate the disableMandatoryBirthdate to set
		 */
		public void setDisableMandatoryBirthdate(boolean disableMandatoryBirthdate) {
			this.disableMandatoryBirthdate = disableMandatoryBirthdate;
		}  
        public boolean verifyStudentInformation(StudentProfileInformation studentProfile, Integer selectedOrgNodeId)
        {
            // check for required fields
            String requiredFields = "";
            int requiredFieldCount = 0;
            
            String firstName = studentProfile.getFirstName().trim();
            if ( firstName.length() == 0 ) {
                requiredFieldCount += 1;            
                requiredFields = Message.buildErrorString("First Name", requiredFieldCount, requiredFields);       
            }
                    
            String lastName = studentProfile.getLastName().trim();
            if ( lastName.length() == 0 ) {
                requiredFieldCount += 1;            
                requiredFields = Message.buildErrorString("Last Name", requiredFieldCount, requiredFields);       
            }
    
            String month = this.studentProfile.getMonth();
            String day = this.studentProfile.getDay();
            String year = this.studentProfile.getYear();
          //GACRCT2010CR007 - validate required date of birth  according to customer configuartion 
            System.out.println("isDisableMandatoryBirthdate==>"+isDisableMandatoryBirthdate());
			if(!isDisableMandatoryBirthdate()) {
	            if (! DateUtils.allSelected(month, day, year)) {
	                requiredFieldCount += 1;            
	                requiredFields = Message.buildErrorString("Date of Birth", requiredFieldCount, requiredFields);       
	            }
            }
            String studentGrade = studentProfile.getGrade();
            if ( studentGrade.equals(FilterSortPageUtils.FILTERTYPE_SELECT_A_GRADE)) {
                requiredFieldCount += 1;            
                requiredFields = Message.buildErrorString("Grade", requiredFieldCount, requiredFields);       
            }
            
            String studentGender = this.studentProfile.getGender();
            if ( studentGender.equals(FilterSortPageUtils.FILTERTYPE_SELECT_A_GENDER) ) {
                requiredFieldCount += 1;            
                requiredFields = Message.buildErrorString("Gender", requiredFieldCount, requiredFields);       
            }
            
            if ( selectedOrgNodeId == null ) {
                requiredFieldCount += 1;      
                requiredFields = Message.buildErrorString("Organization Assignment", requiredFieldCount, requiredFields);       
            }        
            
    
            if (requiredFieldCount > 0) {
                if (requiredFieldCount == 1) {
                    requiredFields += ("<br/>" + Message.REQUIRED_TEXT);
                    setMessage("Missing required field", requiredFields, Message.ERROR);
                }
                else {
                    requiredFields += ("<br/>" + Message.REQUIRED_TEXT_MULTIPLE);
                    setMessage("Missing required fields", requiredFields, Message.ERROR);
                }
                return false;
            }
            
            String middleName = studentProfile.getMiddleName().trim();
            String invalidCharFields = WebUtils.verifyCreateStudentName(firstName, lastName, middleName);                
            if (invalidCharFields.length() > 0) {
                invalidCharFields += ("<br/>" + Message.INVALID_NAME_CHARS);
                setMessage(MessageResourceBundle.getMessage("InvalidCharacters"), invalidCharFields, Message.ERROR);
                return false;
            }

            String studentNumber = studentProfile.getStudentNumber().trim();
            invalidCharFields = WebUtils.verifyCreateStudentNumber(studentNumber, null);                
            if (invalidCharFields.length() > 0) {
                invalidCharFields += ("<br/>" + Message.INVALID_NUMBER_CHARS);
                setMessage(MessageResourceBundle.getMessage("InvalidCharacters"), invalidCharFields, Message.ERROR);
                return false;
            }
            
			//GACRCT2010CR007 - validate  date of birth  when date value is provided.
            
            if(isDisableMandatoryBirthdate() && !DateUtils.allSelected(month, day, year)) {
				if (!DateUtils.noneSelected(month, day, year)) {
					invalidCharFields += Message.INVALID_DATE;
					setMessage("Invalid Date of Birth:", invalidCharFields, Message.ERROR);
	                return false;
					      
				}
			}
            
            if (DateUtils.allSelected(month, day, year)) {
	            int isDateValid = DateUtils.validateDateValues(year, month, day);
	            if (isDateValid != DateUtils.DATE_VALID) {
	                invalidCharFields += Message.INVALID_DATE;
	                setMessage("Invalid Date of Birth:", invalidCharFields, Message.ERROR);
	                return false;
	            }
            }       
            return true;
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
    
	public String[] getGradeOptions() {
		return gradeOptions;
	}

	public String[] getGenderOptions() {
		return genderOptions;
	}

	public LicenseSessionData getLicenseSessionData() {
		return licenseSessionData;
	}

	public String[] getMonthOptions() {
		return monthOptions;
	}

	public String[] getDayOptions() {
		return dayOptions;
	}

	public String[] getYearOptions() {
		return yearOptions;
	}

	public SubtestVO getLocatorSubtest() {
		return locatorSubtest;
	}

	public List getOrgNodeNames() {
		return orgNodeNames;
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
	
	

	//changes for scoring
		
		/**
		 * This method checks whether customer is configured to access the scoring feature or not.
		 * @return Return Boolean 
		 */
	

	private Boolean customerHasScoring()
    {               
        
        boolean hasScoringConfigurable = false;

        for (int i=0; i < customerConfigurations.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
            		cc.getDefaultValue().equals("T")	) {
            	hasScoringConfigurable = true;
                break;
            } 
        }
       
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
	    
	    boolean hasAccessCodeConfigurable = false;
	    String hasBreak = "T";
	    for (int i=0; i < customerConfigurations.length; i++)
	    {
	    	 CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
	        if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Print_Accesscode") && 
	        		cc.getDefaultValue().equals("T")	) {
	        	hasAccessCodeConfigurable = true;
	            break;
	        } 
	    }
	    
	    try {
	    	hasBreak = this.studentManagement.hasMultipleAccessCode(this.testAdminId);
	    	
	    	if(hasBreak.equals("F") && hasAccessCodeConfigurable)
	    		hasAccessCodeConfigurable = true;
	    	else
	    		hasAccessCodeConfigurable = false;
	    }
	    catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
	   
	    return new Boolean(hasAccessCodeConfigurable);
	}

		/**
		 * @return the isFormRecommended
		 */
		public boolean isFormRecommended() {
			return isFormRecommended;
		}

		/**
		 * @param isFormRecommended the isFormRecommended to set
		 */
		public void setFormRecommended(boolean isFormRecommended) {
			this.isFormRecommended = isFormRecommended;
		}

		

		/**
		 * @return the recommendedProductId
		 */
		public Integer getRecommendedProductId() {
			return recommendedProductId;
		}

		/**
		 * @param recommendedProductId the recommendedProductId to set
		 */
		public void setRecommendedProductId(Integer recommendedProductId) {
			this.recommendedProductId = recommendedProductId;
		}

		/**
		 * @return the productId
		 */
		public Integer getProductId() {
			return productId;
		}

		/**
		 * @param productId the productId to set
		 */
		public void setProductId(Integer productId) {
			this.productId = productId;
		}

		/**
		 * @return the requestFromFindStudent
		 */
		public Boolean isRequestFromFindStudent() {
			return requestFromFindStudent;
		}

		/**
		 * @param requestFromFindStudent the requestFromFindStudent to set
		 */
		public void setRequestFromFindStudent(Boolean requestFromFindStudent) {
			this.requestFromFindStudent = requestFromFindStudent;
		}

		/**
		 * @return the preTestAdminId
		 */
		public Integer getPreTestAdminId() {
			return preTestAdminId;
		}

		/**
		 * @param preTestAdminId the preTestAdminId to set
		 */
		public void setPreTestAdminId(Integer preTestAdminId) {
			this.preTestAdminId = preTestAdminId;
		}
    
	
	
    
}
