package selectStudentPageflow;
import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.request.FilterParams.FilterType;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.OrgNodeLicenseInfo;
import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.SchedulingStudent;
import com.ctb.bean.testAdmin.SchedulingStudentData;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.SessionStudentData;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.StudentData;
import com.ctb.bean.testAdmin.StudentNode;
import com.ctb.bean.testAdmin.StudentNodeData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserData;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.testAdmin.TestAdminStatusComputer;
import com.ctb.widgets.bean.PagerSummary;
import com.ctb.widgets.bean.ColumnSortEntry;
import data.DuplicateAssignedStudentInfo;
import data.Message;
import data.OrganizationVO;
import data.PathNode;
import data.TestVO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import scheduleTestPageflow.ScheduleTestController;
import java.util.TimeZone;
import utils.DateUtils;
import utils.FilterSortPageUtils;
import utils.PathListUtils;
import utils.MessageResourceBundle;


/**
 * @jpf:controller nested="true"
 *  */
@Jpf.Controller(nested = true)
public class SelectStudentPageflowController extends PageFlowController
{
    static final long serialVersionUID = 1L; 

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.ScheduleTest scheduleTest;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.licensing.Licensing license;
    
    @Control()
    private com.ctb.control.db.Users users;

    
    public static final String ACTION_DEFAULT = "defaultAction";
    public static final String ACTION_CHANGE_ACCOMMODATION = "changeAccommodation";
    public static final String ACTION_APPLY = "apply";
    public static final String ACTION_CLEAR = "clear";
    public static final String ACTION_UPDATE_TOTAL = "updateTotal";
    public static final String ACTION_ADD_ALL = "addAll";
    
    // change for MQC defect  55837 
    
    public static final String ACTION_SCHEDULE_TEST = "schedule";
    public static final String ACTION_VIEW_TEST = "view";
    public static final String ACTION_EDIT_TEST = "edit";
    
    public String action = ACTION_SCHEDULE_TEST;
    
    // End of change for MQC defect  55837 

    public String[] gradeOptions = {FilterSortPageUtils.FILTERTYPE_SHOWALL, "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "AD"};
    public String[] accommodationOperandOptions = {FilterSortPageUtils.STUDENTS_WITH_AND_WITHOUT_ACCOMMODATIONS, FilterSortPageUtils.STUDENTS_WITH_ACCOMMODATIONS, FilterSortPageUtils.STUDENTS_WITHOUT_ACCOMMODATIONS};
    public String[] selectedAccommodationsOptions = {"Calculator", "Color/Font", "TestPause", "ScreenReader", "UntimedTest"};

    public List dupStudentList = null;
    public List pageDuplicateStudentList = null;     
    public Boolean offGradeTestingDisabled = Boolean.FALSE;
    public Boolean blockOffGradeTesting = Boolean.FALSE;

    private String userName;    
    private List orgNodePath = null;
    private Integer orgNodeId = null;    
    private Integer testAdminId = null;
    private Integer selectedTestId = null;
    private String selectedGrade = null;
    private String selectedFormOperand = null;
    private String [] selectedAccommodations = null;

    private Integer selectedOrgNodeId = null;    
    private Hashtable studentsHashtable;
    private List selectedStudents = null;
    private List originalSelectedStudents = null;
    public String licenseBarColor = null; 
    private int totalDuplicateStudentsInForm = 0;
    private boolean commitSelection = false;
    private boolean hasLicenseConfig = false;
    private CustomerLicense[] customerLicenses = null;
    private User user = null; //change for defect 66353
    
    
    /**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="selectStudent.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "selectStudent.do")
    })
    protected Forward begin(ScheduleTestController.ScheduleTestForm form)
    {
        init(form);        
        return new Forward("success", form);
    }

    private void init(ScheduleTestController.ScheduleTestForm form) 
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
        this.userName = principal.toString();
        getSession().setAttribute("userName", this.userName);      
        ScheduleTestController parentPageFlow = (ScheduleTestController)PageFlowUtils.getNestingPageFlow(getRequest());
        this.testAdminId = parentPageFlow.getTestAdminId();
        this.selectedTestId = new Integer(Integer.parseInt(form.getSelectedTestId()));
        
        this.gradeOptions = getGradeOptionsForStudent();
        
        this.orgNodePath = new ArrayList();        
        form.setSelectedStudentIds(null); 
        form.setSelectedStudentCount(new Integer(0));
        form.setSelectedStudentWithAccommodationCount(new Integer(0));
        form.setShowAccommodations(Boolean.FALSE);        
        
        this.selectedOrgNodeId = new Integer(0);
                
        if ((form.getAccommodationOperand() == null) || form.getAccommodationOperand().equals(""))
            form.setAccommodationOperand(FilterSortPageUtils.STUDENTS_WITH_AND_WITHOUT_ACCOMMODATIONS);
        this.selectedFormOperand = form.getAccommodationOperand();  
            
        if ((form.getSelectedGrade() == null) || form.getSelectedGrade().equals(""))
            form.setSelectedGrade(FilterSortPageUtils.FILTERTYPE_SHOWALL);        
        this.selectedGrade = form.getSelectedGrade();
        
        String selectedAccommodationElements = form.getSelectedAccommodationElements();
        if (selectedAccommodationElements.length() > 0)
        {
            StringTokenizer st = new StringTokenizer(selectedAccommodationElements, ",");
            this.selectedAccommodations = new String[st.countTokens()];
            int index = 0;
            while (st.hasMoreTokens())
            {
                String value = st.nextToken().trim();
                this.selectedAccommodations[index] = value;                    
                index++;
            }
            form.setSelectedAccommodations(this.selectedAccommodations);
        }
        
        copySelectedStudents(form);
        
        form.setCurrentAction(ACTION_DEFAULT);
        form.setActionElement(ACTION_DEFAULT);        
        form.getStudentStatePathList().resetValues(ColumnSortEntry.ASCENDING, FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN);
        
        setupOffGradeTesting(form);
        
        this.action = ACTION_SCHEDULE_TEST; 
        this.hasLicenseConfig = hasLicenseConfiguration(); 
        this.customerLicenses = getCustomerLicenses();
        
    }
    
    /**
     * 
     */
    private void setupOffGradeTesting(ScheduleTestController.ScheduleTestForm form)
    {
        ScheduleTestController parentPageFlow = (ScheduleTestController)PageFlowUtils.getNestingPageFlow(getRequest());
        this.offGradeTestingDisabled = parentPageFlow.condition.getOffGradeTestingDisabled();
        this.blockOffGradeTesting = this.offGradeTestingDisabled;
        
        if (this.offGradeTestingDisabled.booleanValue())
        {
            this.selectedGrade = form.getSelectedGrade();   
            List gradeList = parentPageFlow.getGradeList();
            if (parentPageFlow.isLaslinkCustomer()) {
                gradeList = getCustomGrade(this.selectedGrade);
                this.offGradeTestingDisabled = Boolean.FALSE; 
            }
            this.gradeOptions = (String [])gradeList.toArray(new String[0]);
        }
        else
        {
            this.selectedGrade = FilterSortPageUtils.FILTERTYPE_SHOWALL;        
            form.setSelectedGrade(this.selectedGrade);
        }
        
    }
    
    private List getCustomGrade(String selectedGrade)
    {
    	ArrayList list = new ArrayList();
    	list.add(selectedGrade);

    	if (selectedGrade.indexOf("-") < 0) {
        	return list;
        }
        
		StringTokenizer st = new StringTokenizer(selectedGrade, "-"); 
        String start = st.nextToken();
        String end = st.nextToken();
        
        int startNum = new Integer(start).intValue();
        int endNum = new Integer(end).intValue();
        
        for (int i=startNum ; i<=endNum ; i++) {
        	list.add(new Integer(i).toString());
        }
        return list;
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="selectStudent.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "selectStudent.jsp")
    })
    protected Forward selectStudent(ScheduleTestController.ScheduleTestForm form)
    {
        form.validateValues();
        if (form.getSelectedGrade() == null)
        {                
            form.setSelectedGrade(this.selectedGrade);
        }    

        String actionElement = form.getActionElement();
        form.resetValuesForAction(actionElement);
        Integer orgNodeId = form.getSelectedOrgNodeId();
        if ((orgNodeId != null) && (this.selectedOrgNodeId != null) && (orgNodeId.intValue() != this.selectedOrgNodeId.intValue()))
        {
            form.resetValuesForAction("newNodeSelected");
        }
        
        String currentAction = ACTION_DEFAULT;
        if (actionElement != null && actionElement.equals("currentAction"))
        {
            currentAction = form.getCurrentAction();  
        }   
        
        String orgNodeName = form.getOrgNodeName();
        this.orgNodeId = form.getOrgNodeId();   

        if (currentAction.equals(ACTION_APPLY))
        {
            this.selectedGrade = form.getSelectedGrade();
            this.selectedFormOperand = form.getAccommodationOperand();
            this.selectedAccommodations = form.getSelectedAccommodations();
            if (this.selectedAccommodations != null)
            {
                String selectedAccommodationElements = new String("");
                for (int i=0; i < this.selectedAccommodations.length; i++)
                {
                    selectedAccommodationElements += (this.selectedAccommodations[i] + ",");                
                }
                form.setSelectedAccommodationElements(selectedAccommodationElements);
            }
        }
        if (currentAction.equals(ACTION_CLEAR))
        {
            if (! this.offGradeTestingDisabled.booleanValue())
            {            
                this.selectedGrade = FilterSortPageUtils.FILTERTYPE_SHOWALL;
                form.setSelectedGrade(this.selectedGrade);            
            }
            this.selectedFormOperand = FilterSortPageUtils.STUDENTS_WITH_AND_WITHOUT_ACCOMMODATIONS;
            form.setAccommodationOperand(this.selectedFormOperand);
            this.selectedAccommodations = null;
            form.setSelectedAccommodations(this.selectedAccommodations);            
        }
            
        if (isNeedCommitSelection(currentAction, actionElement, form))
        {
            commitSelection(form);
        }
        
        String accommodationOperand = form.getAccommodationOperand();
        if ((accommodationOperand != null) && (accommodationOperand.equals(FilterSortPageUtils.STUDENTS_WITH_ACCOMMODATIONS)))
        {
            form.setShowAccommodations(Boolean.TRUE);        
            this.selectedAccommodations = form.getSelectedAccommodations();            
            if ((this.selectedAccommodations != null) && (this.selectedAccommodations.length > 0))             
                this.getRequest().setAttribute("disableApply", "false");     
            else
                this.getRequest().setAttribute("disableApply", "true");     
        }               
        else
        {
            form.setShowAccommodations(Boolean.FALSE);        
            this.getRequest().setAttribute("disableApply", "false");     
        }               

        // get data for Pathlist - getChildrenOrgNodes
        FilterParams filter = new FilterParams();
        ArrayList filters = new ArrayList();
        if (this.selectedGrade != null && !this.selectedGrade.equals(FilterSortPageUtils.FILTERTYPE_SHOWALL))
        {
            String [] arg = new String[1];
            arg[0] = this.selectedGrade;
            filters.add(new FilterParam("StudentGrade", arg, FilterType.EQUALS));
        }
        if (this.selectedFormOperand != null && this.selectedFormOperand.equals(FilterSortPageUtils.STUDENTS_WITH_ACCOMMODATIONS))
        {
            if (this.selectedAccommodations != null)
            {
                for (int i=0; i < this.selectedAccommodations.length; i++)
                {
                    if (this.selectedAccommodations[i].indexOf("Color/Font") >= 0)
                    {
                        String [] arg = new String[1];
                        arg[0] = "T";
                        filters.add(new FilterParam("ColorFontAccommodation", arg, FilterType.EQUALS));
                    }
                    else
                    {
                        String [] arg = new String[1];
                        arg[0] = "T";
                        filters.add(new FilterParam(this.selectedAccommodations[i], arg, FilterType.EQUALS));
                    }
                }
            }
        }
        else if (this.selectedFormOperand != null && this.selectedFormOperand.equals(FilterSortPageUtils.STUDENTS_WITHOUT_ACCOMMODATIONS))
        {
            String [] arg = new String[1];
            arg[0] = "T";
            filters.add(new FilterParam("ScreenReader", arg, FilterType.NOTEQUAL));
            filters.add(new FilterParam("Calculator", arg, FilterType.NOTEQUAL));
            filters.add(new FilterParam("TestPause", arg, FilterType.NOTEQUAL));
            filters.add(new FilterParam("UntimedTest", arg, FilterType.NOTEQUAL));
            String [] arg2 = new String[1];
            arg2[0] = "F";
            filters.add(new FilterParam("ColorFontAccommodation", arg2, FilterType.EQUALS)); //HasColorFontAccommodations
        }
        filter.setFilterParams((FilterParam[])filters.toArray(new FilterParam[0]));

        PageParams page = FilterSortPageUtils.buildPageParams(form.getOrgStatePathList().getPageRequested(), FilterSortPageUtils.PAGESIZE_10);
        SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgStatePathList().getSortColumn(), form.getOrgStatePathList().getSortOrderBy(), null, null);
        
        // get org nodes - getChildrenOrgNodes                    
        StudentNodeData snd = getChildrenOrgNodes(this.orgNodeId, this.testAdminId, filter, page, sort);
        List orgNodes = buildOrgNodeList(snd);
        
        form.getOrgStatePathList().setMaxPageRequested(snd.getFilteredPages());
        
        String orgCategoryName = getOrgCategoryName(orgNodes);
        
        if (form.getOrgStatePathList().getPageRequested().intValue() > snd.getFilteredPages().intValue())
        {
            form.getOrgStatePathList().setPageRequested(snd.getFilteredPages());
        }
        
        PagerSummary orgPagerSummary = buildOrgNodePagerSummary(snd, form.getOrgStatePathList().getPageRequested());        
                
        this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
        this.getRequest().setAttribute("orgNodes", orgNodes);        
        this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
        this.getRequest().setAttribute("orgCategoryName", orgCategoryName);        

        boolean nodeChanged = PathListUtils.adjustOrgNodePath(this.orgNodePath, this.orgNodeId, orgNodeName);

        if (actionElement.equals("{actionForm.orgStatePathList.pageRequested}") || actionElement.equals("{actionForm.orgStatePathList.sortOrderBy}") || actionElement.equals("EnterKeyInvoked_pathListAnchor"))
        {                
            nodeChanged = true;
        }


        String selectedOrgNodeName = null;
        if (nodeChanged)
        {
            selectedOrgNodeName = initFirstOrgNode(orgNodes, form);
        }
        else
        {
            Integer id = form.getSelectedOrgNodeId();
            if (id != null)
            {
                for (int i=0; i < orgNodes.size(); i++)
                {
                    PathNode node = (PathNode)orgNodes.get(i);
                    if (node.getId().intValue() == id.intValue())
                    {
                        selectedOrgNodeName = node.getName();
                        break;
                    }
                }
            }
            if (selectedOrgNodeName == null)
            {
                selectedOrgNodeName = initFirstOrgNode(orgNodes, form);
            }
        }        
        this.getRequest().setAttribute("selectedOrgNodeName", selectedOrgNodeName);

        this.selectedOrgNodeId = form.getSelectedOrgNodeId();
                
        filters = new ArrayList();
        if (this.selectedGrade != null && !this.selectedGrade.equals(FilterSortPageUtils.FILTERTYPE_SHOWALL))
        {
            ScheduleTestController parentPageFlow = (ScheduleTestController)PageFlowUtils.getNestingPageFlow(getRequest());
            boolean laslinkCustomer = parentPageFlow.isLaslinkCustomer();
            if (laslinkCustomer && (this.selectedGrade.indexOf("-") > 0)) {
                List grades = getCustomGrade(this.selectedGrade);
                for (int i=1 ; i<grades.size() ; i++) {
    	            String [] arg = new String[1];
    	            arg[0] = (String)grades.get(i);
    	            filters.add(new FilterParam("StudentGrade", arg, FilterType.EQUALS));            	                	
                }
            }
            else {
	            String [] arg = new String[1];
	            arg[0] = this.selectedGrade;
	            filters.add(new FilterParam("StudentGrade", arg, FilterType.EQUALS));
            }            
        }
        if (this.selectedFormOperand != null && this.selectedFormOperand.equals(FilterSortPageUtils.STUDENTS_WITH_ACCOMMODATIONS))
        {
            if (this.selectedAccommodations != null)
            {
                for (int i=0; i < this.selectedAccommodations.length; i++)
                {
                    if (this.selectedAccommodations[i].indexOf("Color/Font") >= 0)
                    {
                        String [] arg = new String[1];
                        arg[0] = "true";
                        filters.add(new FilterParam("HasColorFontAccommodations", arg, FilterType.EQUALS));
                    }
                    else
                    {
                        String [] arg = new String[1];
                        arg[0] = "T";
                        filters.add(new FilterParam(this.selectedAccommodations[i], arg, FilterType.EQUALS));
                    }
                }
            }
        }
        else if (this.selectedFormOperand != null && this.selectedFormOperand.equals(FilterSortPageUtils.STUDENTS_WITHOUT_ACCOMMODATIONS))
        {
            String [] arg = new String[1];
            arg[0] = "T";
            filters.add(new FilterParam("ScreenReader", arg, FilterType.NOTEQUAL));
            filters.add(new FilterParam("Calculator", arg, FilterType.NOTEQUAL));
            filters.add(new FilterParam("TestPause", arg, FilterType.NOTEQUAL));
            filters.add(new FilterParam("UntimedTest", arg, FilterType.NOTEQUAL));
            String [] arg2 = new String[1];
            arg2[0] = "false";
            filters.add(new FilterParam("HasColorFontAccommodations", arg2, FilterType.EQUALS)); //HasColorFontAccommodations
        }
        filter.setFilterParams((FilterParam[])filters.toArray(new FilterParam[0]));
                 
        page = FilterSortPageUtils.buildPageParams(form.getStudentStatePathList().getPageRequested(), FilterSortPageUtils.PAGESIZE_10);
        sort = FilterSortPageUtils.buildSortParams(form.getStudentStatePathList().getSortColumn(), form.getStudentStatePathList().getSortOrderBy(), null, null);

        if (currentAction.equals(ACTION_ADD_ALL))
        {
            addAllStudents(filter, this.selectedOrgNodeId, this.testAdminId);    
        }

        // get students - getSessionStudents
        SessionStudentData ssd = getSessionStudents(this.selectedOrgNodeId, this.testAdminId, filter, page, sort);
        List studentNodes = buildStudentList(ssd);
        form.getStudentStatePathList().setMaxPageRequested(ssd.getFilteredPages());
        
        PagerSummary studentPagerSummary = buildStudentPagerSummary(ssd, form.getStudentStatePathList().getPageRequested());        
        this.getRequest().setAttribute("studentNodes", studentNodes);        
        this.getRequest().setAttribute("hasStudentNodes", studentNodes.size() > 0 ? "true" : null);
        
        this.getRequest().setAttribute("studentPagerSummary", studentPagerSummary);

        Boolean nodeContainsStudents = ssd.getTotalCount().intValue() > 0 ? Boolean.TRUE : null;
        this.getRequest().setAttribute("nodeContainsStudents", nodeContainsStudents);

        setSelectedStudentOrgListToForm(form);                    
        setSelectedStudentCountToForm(form);
        setSelectedStudentCountToOrgNode(orgNodes);

        this.getRequest().setAttribute("isCopyTest", form.getIsCopyTest());
        
        if (this.selectedGrade.equals(FilterSortPageUtils.FILTERTYPE_SHOWALL))
        {
            if (this.selectedFormOperand == null)
                this.getRequest().setAttribute("noFilterApplied", "true");
            else if (this.selectedFormOperand.equals(FilterSortPageUtils.FILTERTYPE_SHOWALL)) 
                this.getRequest().setAttribute("noFilterApplied", "true");
        }
            
        form.setActionElement(ACTION_DEFAULT);    
        
        if (form.getAction().equals(ACTION_SCHEDULE_TEST))
        {
            this.action = ACTION_SCHEDULE_TEST;
        }
        else if (form.getAction().equals(ACTION_EDIT_TEST))
        {
            this.action = ACTION_EDIT_TEST;
        }
        else if (form.getAction().equals(ACTION_VIEW_TEST))
        {
            this.action = ACTION_VIEW_TEST;
        }

        boolean licenseflag = false; 
        if (needHandleLicense() && (nodeContainsStudents != null)) {
        	OrgNodeLicenseInfo onli = getLicenseQuantitiesByOrg(this.selectedOrgNodeId);
        	licenseflag = setupLicenseInfo(this.selectedOrgNodeId, selectedOrgNodeName, onli, form, actionElement);
        }
         
        this.getSession().setAttribute("displayLicenseBar", new Boolean(licenseflag));
        
        setFormInfoOnRequest(form);
        
        this.commitSelection = false;
        
        return new Forward("success", form);
    }
    
    private boolean needHandleLicense() {
    	
        ScheduleTestController parentPageFlow = (ScheduleTestController)PageFlowUtils.getNestingPageFlow(getRequest());
        String productType = parentPageFlow.getProductType();
        boolean nonLicenseProduct = (productType.equals("tabeLocatorProductType") || productType.equals("genericProductType"));
    	return (this.hasLicenseConfig && (! nonLicenseProduct));
    }
    
 	private Boolean hasLicenseConfiguration()
    {    
		
		 try {
			 this.user = this.scheduleTest.getUserDetails(this.userName, this.userName);
	        }    
	        catch (CTBBusinessException be) {
	            be.printStackTrace();
	        }        
		Integer customerId = this.user.getCustomer().getCustomerId();
        boolean hasLicenseConfiguration = false;
        
        try
        {      
			CustomerConfiguration [] customerConfigurations = users.getCustomerConfigurations(customerId.intValue());
			if(customerConfigurations != null  && customerConfigurations.length > 0) {
		        for (int i=0; i < customerConfigurations.length; i++)
		        {
		        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
		            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subscription") && 
		            		cc.getDefaultValue().equals("T")	) {
		            	hasLicenseConfiguration = true;
		                break;
		            } 
		        }
			}
       }
        
        catch (SQLException se) {
        	se.printStackTrace();
		}
        return new Boolean(hasLicenseConfiguration);
    }
    
	
    
    
    
    
    
    private void setFormInfoOnRequest(ScheduleTestController.ScheduleTestForm form) {
    	this.getRequest().setAttribute("pageMessage", form.getMessage());
    }

    private String initFirstOrgNode(List orgNodes, ScheduleTestController.ScheduleTestForm form)
    {
        String selectedOrgNodeName = "";
        if (orgNodes.size() > 0)
        {
            PathNode node = (PathNode)orgNodes.get(0);
            form.setSelectedOrgNodeId(node.getId());
            form.setSelectedOrgNodeName(node.getName());
            this.orgNodeId = node.getId();
            selectedOrgNodeName = node.getName();
        }
        else
        {
            form.setSelectedOrgNodeId(new Integer(0));
            form.setSelectedOrgNodeName("");
            this.orgNodeId = new Integer(0);
            selectedOrgNodeName = "";
        }
        return selectedOrgNodeName;
    }
    
    private List buildStudentList(SessionStudentData ssd) 
    {
        List studentList = new ArrayList();
        this.studentsHashtable = new Hashtable();        
        SessionStudent [] sessionStudents = ssd.getSessionStudents();    
            
        for (int i=0; i < sessionStudents.length; i++)
        {
            SessionStudent ss = (SessionStudent)sessionStudents[i];
            if (ss != null)
            {
                String middleName = ss.getMiddleName();
                if ((middleName != null) && (middleName.length() > 0))
                    ss.setMiddleName(String.valueOf(middleName.charAt(0)));
                String key = encodeStudentOrgIds(ss);
                ss.setExtElmId(key);
                
                if (ss.getStatus().getCode() == null || ss.getStatus().getCode().equals(""))
                    ss.getStatus().setCode("&nbsp;");
                if ("Ses".equals(ss.getStatus().getCode()))
                {
                    StringBuffer buf = new StringBuffer();
                    TestSession ts = ss.getStatus().getPriorSession();
                    if (ts != null)
                    {
//                        String timeZone = ts.getTimeZone();
                        TestAdminStatusComputer.adjustSessionTimesToLocalTimeZone(ts);
                        String testAdminName = ts.getTestAdminName();
                        testAdminName = testAdminName.replaceAll("\"", "&quot;");
                        buf.append("Session Name: ").append(testAdminName);
                        buf.append("<br/>Start Date: ").append(DateUtils.formatDateToDateString(ts.getLoginStartDate()));
                        buf.append("<br/>End Date: ").append(DateUtils.formatDateToDateString(ts.getLoginEndDate()));
//                        buf.append("<br/>Start Date: ").append(DateUtils.formatDateToDateString(com.ctb.util.DateUtils.getAdjustedDate(ts.getLoginStartDate(), TimeZone.getDefault().getID(), timeZone, ts.getDailyLoginStartTime())));
//                        buf.append("<br/>End Date: ").append(DateUtils.formatDateToDateString(com.ctb.util.DateUtils.getAdjustedDate(ts.getLoginEndDate(), TimeZone.getDefault().getID(), timeZone, ts.getDailyLoginEndTime())));
                    }
                    ss.setExtPin2(buf.toString());
                }
                      
                studentList.add(ss);
                this.studentsHashtable.put(key, ss);
            }
        }
        
        return studentList;
    }
    
    private void collectSelectedStudents(String[] selectedStudentOrgList)
    {   
        if ((selectedStudentOrgList != null) && (this.selectedStudents != null))
        {
            
            List unselectedStudents = getUnselectedStudentsInPage(selectedStudentOrgList);
            for (int i=0; i < unselectedStudents.size(); i++)
            {
                SessionStudent unselected = (SessionStudent)unselectedStudents.get(i);
                for (int j=0; j < this.selectedStudents.size(); j++)
                {
                    SessionStudent ss = (SessionStudent)this.selectedStudents.get(j);
                    if (ss.getExtElmId().equals(unselected.getExtElmId()))
                    {
                        this.selectedStudents.remove(j);
                        break;
                    }
                }                
            }
                        
            for (int i=0; i < selectedStudentOrgList.length; i++)
            {   
                String studentOrg = selectedStudentOrgList[i];         
                if (studentOrg != null)
                {
                    SessionStudent ss = (SessionStudent)this.studentsHashtable.get(studentOrg);
                    if ((ss != null) && (!"F".equals(ss.getStatus().getEditable())))
                    {                    
                        addSelectedStudentToList(ss);
                    }
                }
            }
        }
    }

    private List getUnselectedStudentsInPage(String[] selectedStudentOrgList)
    {
        Iterator iter = null;
        ArrayList unselectedStudents = new ArrayList();       
        if (selectedStudentOrgList != null)
        {
            if ((selectedStudentOrgList.length == 1) && (selectedStudentOrgList[0] == null))
            {
                iter = this.studentsHashtable.values().iterator();
                while (iter.hasNext())
                {
                    SessionStudent ss = (SessionStudent)iter.next();
                    if ("T".equals(ss.getStatus().getEditable()))
                        unselectedStudents.add(ss);
                }
                return unselectedStudents;
            }
        }
         
        if ((this.studentsHashtable != null) && (selectedStudentOrgList != null))
        {
            iter = this.studentsHashtable.values().iterator();
            while (iter.hasNext())
            {
                SessionStudent ss = (SessionStudent)iter.next();
                boolean found = false;
                for (int i=0; i < selectedStudentOrgList.length; i++)
                {   
                    String studentOrg = selectedStudentOrgList[i];         
                    if ((studentOrg != null) && (studentOrg.equals(ss.getExtElmId())))
                    {
                        found = true;
                        break;
                    }
                }
                if (! found && "T".equals(ss.getStatus().getEditable()))
                {
                    unselectedStudents.add(ss);
                }
            }
        }
        return unselectedStudents;
    }
    
    private void addSelectedStudentToList(SessionStudent student)
    {
        if (!this.selectedStudents.contains(student))
            this.selectedStudents.add(student);
        
        /*String extElmId = student.getExtElmId();
        if (this.selectedStudents != null) {
            boolean found = false;
            for (int i=0 ; i<this.selectedStudents.size() ; i++) {
                SessionStudent ss = (SessionStudent) this.selectedStudents.get(i);
                if (ss.getExtElmId().equals(extElmId)) {
                    found = true;
                }
            }
            if (! found) {
                this.selectedStudents.add(student);
            }
        }*/
    }
    
    private void setSelectedStudentCountToForm(ScheduleTestController.ScheduleTestForm form)
    {
        if (this.selectedStudents != null)
        {
            int selectedStudentWithAccommoCount = 0;
            for (int i=0; i < this.selectedStudents.size(); i++)
            {   
                SessionStudent ss = (SessionStudent)this.selectedStudents.get(i);
                if ((ss != null) && (ss.getHasAccommodations() != null))
                {
                    if (ss.getHasAccommodations().equalsIgnoreCase("true"))
                        selectedStudentWithAccommoCount++;
                }
            } 
            form.setSelectedStudentCount(new Integer(this.selectedStudents.size()));
            form.setSelectedStudentWithAccommodationCount(new Integer(selectedStudentWithAccommoCount));        
        }
    }

    private void setSelectedStudentOrgListToForm(ScheduleTestController.ScheduleTestForm form)
    {
        if (this.selectedStudents != null)
        {
            int size = this.selectedStudents.size();
            int index = 0;
            String[] studentOrgList = new String[size];
            for (int i=0; i < size; i++)
            {   
                SessionStudent ss = (SessionStudent)this.selectedStudents.get(i);
                String extElmId = ss.getExtElmId();
                if ((ss != null) && (extElmId != null))
                {
                    if (this.studentsHashtable.containsKey(extElmId))
                    {
                        studentOrgList[index] = extElmId;
                        index++;
                    }    
                }
            }         
            form.setSelectedStudentOrgList(studentOrgList);
        }
    }

    private Integer getSelectedStudentCountAtOrgNode(Integer orgNodeId)
    {
        int count = 0;
        if ((this.selectedStudents != null) && (orgNodeId != null))
        {
            for (int i=0; i < this.selectedStudents.size(); i++)
            {   
                SessionStudent ss = (SessionStudent)this.selectedStudents.get(i);
                if ((ss != null) && (ss.getOrgNodeId() != null))
                {
                    if (ss.getOrgNodeId().intValue() == orgNodeId.intValue())
                        count++;
                }
            } 
        }
        return new Integer(count);
    }

    private boolean verifyDuplicateStudents()
    {
        boolean dup = false;
        
        //Change to avoid error in null pointer exception
        if (this.selectedStudents != null)
        {
            for (int i=0; i < this.selectedStudents.size() - 1; i++)
            {   
                SessionStudent ss = (SessionStudent)this.selectedStudents.get(i);
                if (ss != null)
                {
                    SessionStudent dupStudent = findStudentAtIndex(i + 1, ss.getStudentId());
                    if (dupStudent != null)
                    {
                        ss.setPotentialDuplicatedStudent("T");
                        dupStudent.setPotentialDuplicatedStudent("T");
                        dup = true;
                    }
                }
            } 
        }
        
        return dup;
    }

    private List getDuplicateStudents()
    {
        int i;
        DuplicateAssignedStudentInfo dupStudent = null;
        List orgNodeIds = null;
        List orgNodeNames = null;
        
        List dupList = new ArrayList();
        totalDuplicateStudentsInForm = 0;
        for (i = 0; i < this.selectedStudents.size(); i++)
        {   
            SessionStudent ss = (SessionStudent)this.selectedStudents.get(i);
            Integer studentId = ss.getStudentId();
            if (ss != null)
            {
                if ((ss.getPotentialDuplicatedStudent() != null) && ss.getPotentialDuplicatedStudent().equals("T"))
                {
                    totalDuplicateStudentsInForm = totalDuplicateStudentsInForm + 1;
                    dupStudent = findDuplicatedStudentInList(studentId, dupList);
                    if (dupStudent != null)
                    {
                        orgNodeIds = dupStudent.getOrgNodeIds();
                        orgNodeIds.add(ss.getOrgNodeId());
                        orgNodeNames = dupStudent.getOrgNodeNames();
                        orgNodeNames.add(ss.getOrgNodeName());
                        dupStudent.setSelectedOrgNodeName(ss.getOrgNodeName());
                    }
                    else
                    {
                        dupStudent = new DuplicateAssignedStudentInfo();
                        dupStudent.setId(studentId);
                        dupStudent.setFirstName(ss.getFirstName());
                        dupStudent.setLastName(ss.getLastName());
                        dupStudent.setMiddleName(ss.getMiddleName());
                        dupStudent.setSelectedOrgNodeName(ss.getOrgNodeName());
                        orgNodeIds = new ArrayList();
                        orgNodeIds.add(ss.getOrgNodeId());
                        orgNodeNames = new ArrayList();                    
                        orgNodeNames.add(ss.getOrgNodeName());
                        dupStudent.setOrgNodeIds(orgNodeIds);
                        dupStudent.setOrgNodeNames(orgNodeNames);
                        dupStudent.setCategoryName(ss.getOrgNodeCategoryName());
                        dupList.add(dupStudent);
                    }                    
                }
            }
        } 
                
        return dupList;
    }

    private DuplicateAssignedStudentInfo findDuplicatedStudentInList(Integer studentId, List students)
    {
        DuplicateAssignedStudentInfo dupstudent = null;
        for (int i=0; i < students.size(); i++)
        {   
            DuplicateAssignedStudentInfo student = (DuplicateAssignedStudentInfo)students.get(i);
            if (student != null)
            {
                if (student.getId().intValue() == studentId.intValue())
                {
                    dupstudent = student;
                    break;
                }
            }
        } 
        return dupstudent;
    }
    
    private List sortStudentList(List students, String sortName, String sortOrderBy)
    {
        int i, j;
        int size = students.size();
        SessionStudentData ssd = new SessionStudentData();
        SessionStudent[] sessionStudents = new SessionStudent[size];
        for (i = 0; i < size; i++)
        {   
            DuplicateAssignedStudentInfo student = (DuplicateAssignedStudentInfo)students.get(i);
            SessionStudent ss = new SessionStudent();
            ss.setStudentId(student.getId());
            ss.setFirstName(student.getFirstName());
            ss.setLastName(student.getLastName());
            ss.setMiddleName(student.getMiddleName());
            ss.setOrgNodeName(student.getSelectedOrgNodeName());            
            sessionStudents[i] = ss;
        } 
        
        ssd.setSessionStudents(sessionStudents, new Integer(size));
        SortParams sort = FilterSortPageUtils.buildSortParams(sortName, sortOrderBy, null, null);        
        try
        {      
            ssd.applySorting(sort);        
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        
        List resultList = new ArrayList();
        sessionStudents = ssd.getSessionStudents();        
        for (i = 0; i < sessionStudents.length; i++)
        {
            SessionStudent ss = (SessionStudent)sessionStudents[i];
            for (j = 0; j < size; j++)
            {   
                DuplicateAssignedStudentInfo student = (DuplicateAssignedStudentInfo)students.get(j);
                if (student.getId().intValue() == ss.getStudentId().intValue())
                {
                    resultList.add(student);
                    break;
                }
            }
        }
            
        return resultList;
    }

    private SessionStudent findStudentAtIndex(int index, Integer studentId)
    {
        SessionStudent student = null;
        for (int i=index; i < this.selectedStudents.size(); i++)
        {   
            SessionStudent ss = (SessionStudent)this.selectedStudents.get(i);
            if (ss != null)
            {
                if (ss.getStudentId().intValue() == studentId.intValue())
                {
                    student = ss;
                    break;
                }
            }
        } 
        return student;
    }

    private String encodeStudentOrgIds(SessionStudent ss)
    {
        Integer studentId = ss.getStudentId();
        if ((ss != null) && (studentId != null) && (ss.getOrgNodeId() != null))
            return studentId + "-" + ss.getOrgNodeId();
        else
            return "";
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="reviewAssignment.jsp"
     * @jpf:validation-error-forward name="failure" path="reviewAssignment.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "reviewAssignment.jsp")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "reviewAssignment.do"))
    protected Forward reviewAssignment(ScheduleTestController.ScheduleTestForm form)
    {
        String column = form.getStudentStatePathList().getSortColumn();
        String order = form.getStudentStatePathList().getSortOrderBy();
        Integer pageRequested = form.getStudentStatePathList().getPageRequested();

        String currentAction = ACTION_DEFAULT;
        String actionElement = form.getActionElement();
        if (actionElement != null && actionElement.equals("currentAction"))
        {
            currentAction = form.getCurrentAction();  
        }   
        if (actionElement != null && actionElement.equals("{actionForm.studentStatePathList.sortOrderBy}"))
        {
            pageRequested = new Integer(1);
        }
        
        if (this.dupStudentList == null)
        {        
            this.dupStudentList = getDuplicateStudents();        
        }
        this.dupStudentList = sortStudentList(this.dupStudentList, form.getStudentStatePathList().getSortColumn(), form.getStudentStatePathList().getSortOrderBy());

        int pageSize = FilterSortPageUtils.PAGESIZE_20;
        int size = this.dupStudentList.size();
        Integer totalPages;
        if ((size % pageSize) == 0)
            totalPages = new Integer(this.dupStudentList.size() / pageSize);
        else
            totalPages = new Integer(this.dupStudentList.size() / pageSize + 1);
        
        if (pageRequested.intValue() > totalPages.intValue())
        {
            pageRequested = new Integer(totalPages.intValue());
        }
        if (pageRequested.intValue() <= 0)
        {
            pageRequested = new Integer(1);
        }
        Integer totalCount = new Integer(size);
        
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);        
        pagerSummary.setTotalPages(totalPages);
        pagerSummary.setTotalObjects(totalCount);
        pagerSummary.setTotalFilteredObjects(null);        
        this.getRequest().setAttribute("studentPagerSummary", pagerSummary);
        
        String totalStudentSelected = String.valueOf(this.selectedStudents.size());
        String totalStudentDuplicated = String.valueOf(this.dupStudentList.size());
        
        this.getRequest().setAttribute("totalStudentSelected", totalStudentSelected);
        this.getRequest().setAttribute("totalStudentDuplicated", totalStudentDuplicated);
        
        this.pageDuplicateStudentList = getCurrentPage(pageRequested.intValue(), pageSize, this.dupStudentList);
        String orgCategoryName = getDuplicatedOrgCategoryName(this.selectedStudents);
        this.getRequest().setAttribute("orgCategoryName", orgCategoryName);        

        this.getRequest().setAttribute("isCopyTest", form.getIsCopyTest());
        
        return new Forward("success");
    }
     
    private String getDuplicatedOrgCategoryName(List srcList)
    {
        String categoryName = "Organization";        
        if (srcList.size() > 0)
        {
            SessionStudent ss = (SessionStudent)srcList.get(0);
            categoryName = ss.getOrgNodeCategoryName();            
            for (int i=1; i < srcList.size(); i++)
            {
                ss = (SessionStudent)srcList.get(i);
                if (! ss.getOrgNodeCategoryName().equals(categoryName))
                {
                    categoryName = "Organization";
                    break;
                }
            }
        }
        return categoryName;
    }
    
    private List getCurrentPage(int page, int pageSize, List srcList) 
    {
        List pageList = new ArrayList();
        int index = (page - 1) * pageSize;
        for (int i=index; (i < (index + pageSize) && (i < srcList.size())); i++)
        {
            DuplicateAssignedStudentInfo student = (DuplicateAssignedStudentInfo)srcList.get(i);
            pageList.add(student);
        }        
        return pageList;
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="done" return-action="selectStudentDone"
     * @jpf:forward name="reviewAssignment" path="reviewAssignment.do"
     * @jpf:forward name="error" path="selectStudent.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "done",
                     returnAction = "selectStudentDone"), 
        @Jpf.Forward(name = "reviewAssignment",
                     path = "reviewAssignment.do"), 
        @Jpf.Forward(name = "error",
                     path = "selectStudent.do")
    })
    public Forward selectStudentDone(ScheduleTestController.ScheduleTestForm form)
    {
        commitSelection(form);       
        
        this.commitSelection = true;
        
        form.setSelectedStudents(this.selectedStudents);
        resetFormOnReturn(form, "selectStudentDone");
                
        boolean hasDuplicate = verifyDuplicateStudents();
        boolean isError = false;
        int duplicateStudentsNum = 0;
               
        if (hasDuplicate)
        {
            int dupStudentSize = getDuplicateStudents().size();
            duplicateStudentsNum = totalDuplicateStudentsInForm - dupStudentSize;
        }
     
        if (needHandleLicense()) {
	        if (!verifyLicenseAvailability(form, duplicateStudentsNum)) {
	            isError = true;
	        }
        }
        
        if (hasDuplicate)
        {    
            form.setCurrentAction(ACTION_DEFAULT);
            form.setActionElement(ACTION_DEFAULT);      
            form.getStudentStatePathList().resetValues(ColumnSortEntry.ASCENDING, FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN);  
            if (isError)
            {
                
                return new Forward("error", form);
                
            }
            else
            {
               
                return new Forward("reviewAssignment", form);
                
            }
           
        }
        else
        {
            
            if (isError)
            {
               
                return new Forward("error", form);
                
            }
            else
            {
               
                return new Forward("done", form);
                
            }
           
        }
    }

    /**
     * @jpf:action
     * @jpf:forward name="done" return-action="selectStudentDone"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "done",
                     returnAction = "selectStudentDone")
    })
    public Forward applyAssignmentDone(ScheduleTestController.ScheduleTestForm form)
    {
        form.setSelectedStudents(this.selectedStudents);
        resetFormOnReturn(form, "selectStudentDone");        
        return new Forward("done", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="done" return-action="selectStudentDone"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "done",
                     returnAction = "selectStudentDone")
    })
    protected Forward selectStudentCancel(ScheduleTestController.ScheduleTestForm form)
    {        
        form.setSelectedStudents(this.originalSelectedStudents);
        resetFormOnReturn(form, "selectStudentCancel");
        return new Forward("done", form);
    }

    private StudentNodeData getChildrenOrgNodes(Integer orgNodeId, Integer testAdminId, FilterParams filter, PageParams page, SortParams sort)
    {    
        StudentNodeData snd = null;
        try
        {      
            if ((orgNodeId == null) || (orgNodeId.intValue() <= 0))
                snd = this.scheduleTest.getTopStudentNodesForUserAndAdmin(this.userName, testAdminId, filter, page, sort);
            else
                snd = this.scheduleTest.getStudentNodesForParentAndAdmin(this.userName, orgNodeId, testAdminId, filter, page, sort);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return snd;
    }

    private List buildOrgNodeList(StudentNodeData snd) 
    {
        ArrayList nodeList = new ArrayList();
        PathNode pathNode = null;
        StudentNode [] nodes = snd.getStudentNodes();        
        for (int i=0; i < nodes.length; i++)
        {
            StudentNode node = (StudentNode)nodes[i];
            if (node != null)
            {
                pathNode = new PathNode();
                pathNode.setName(node.getOrgNodeName());
                pathNode.setId(node.getOrgNodeId());   
                pathNode.setFilteredCount(node.getStudentCount());
                Integer selectedCount = getSelectedStudentCountAtOrgNode(node.getOrgNodeId());
                pathNode.setSelectedCount(selectedCount);
                pathNode.setChildNodeCount(node.getChildNodeCount());
                pathNode.setHasChildren((new Boolean(node.getChildNodeCount().intValue() > 0)).toString());                
                pathNode.setCategoryName(node.getOrgNodeCategoryName());                
                nodeList.add(pathNode);
            }
        }
        return nodeList;
    }

    private String getOrgCategoryName(List nodeList)
    {
        String categoryName = "Organization";        
        if (nodeList.size() > 0)
        {
            PathNode node = (PathNode)nodeList.get(0);
            categoryName = node.getCategoryName();            
            for (int i=1; i < nodeList.size(); i++)
            {
                node = (PathNode)nodeList.get(i);
                if (! node.getCategoryName().equals(categoryName))
                {
                    categoryName = "Organization";
                    break;
                }
            }
        }
        return categoryName;
    }

    private void setSelectedStudentCountToOrgNode(List orgNodes) 
    {
        if (orgNodes != null)
        {
            for (int i=0; i < orgNodes.size(); i++)
            {
                PathNode node = (PathNode)orgNodes.get(i);
                if (node != null)
                {
                    Integer selectedCount = getSelectedStudentCountAtOrgNode(node.getId());
                    node.setSelectedCount(selectedCount);
                }
            }
        }
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


    private SessionStudentData getSessionStudents(Integer orgNodeId, Integer testAdminId, FilterParams filter, PageParams page, SortParams sort)
    {    
        SessionStudentData sd = null;
        try {      
            sd = this.scheduleTest.getSessionStudentsForOrgNode(this.userName, orgNodeId, testAdminId, this.selectedTestId, filter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return sd;
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

    /**
     * @jpf:action
     * @jpf:forward name="success" path="selectStudent.do"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "selectStudent.do")
		}
	)
    protected Forward cancelAssignment(ScheduleTestController.ScheduleTestForm form)
    {
        resetFormOnReturn(form, "cancelAssignment");
        this.dupStudentList = null;
        this.pageDuplicateStudentList = null;

        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="applyAssignmentDone.do"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "applyAssignmentDone.do")
		}
	)
    protected Forward applyAssignment(ScheduleTestController.ScheduleTestForm form)
    {
        int i, j;
        DuplicateAssignedStudentInfo student = null;
        SessionStudent ss = null;
        Integer studentId1 = null;
        Integer studentId2 = null;
        
        resetFormOnReturn(form, "applyAssignment");
        
        applyPageStudentAssignment();

        if ((this.dupStudentList != null) && (this.selectedStudents != null)) {
            for (i=0 ; i<this.dupStudentList.size(); i++) {   
                student = (DuplicateAssignedStudentInfo)this.dupStudentList.get(i);        
                for (j=this.selectedStudents.size()-1 ; j>=0; j--) {   
                    ss = (SessionStudent) this.selectedStudents.get(j);
                    studentId1 = ss.getStudentId();
                    ss.setPotentialDuplicatedStudent("F");
                    if (studentId1.intValue() == student.getId().intValue()) {
                        if (! ss.getOrgNodeName().equals(student.getSelectedOrgNodeName())) {
                            this.selectedStudents.remove(j);
                        }
                    }                        
                }
            }
            for (i=0 ; i<this.selectedStudents.size() ; i++) {   
                ss = (SessionStudent) this.selectedStudents.get(i);
                for (j=i+1 ; j<this.selectedStudents.size() ; j++) {   
                    SessionStudent ss2 = (SessionStudent) this.selectedStudents.get(j);
                    studentId2 = ss2.getStudentId();
                    if (studentId1.intValue() == studentId2.intValue()) {
                        this.selectedStudents.remove(j);
                    }
                }
            }
        }
        this.dupStudentList = null;
        this.pageDuplicateStudentList = null;
        
        return new Forward("success", form);
    }
     
    private void applyPageStudentAssignment()
    {
        if ((this.dupStudentList != null) && (this.pageDuplicateStudentList != null)) {
            for (int i=0 ; i<this.pageDuplicateStudentList.size(); i++) {   
                DuplicateAssignedStudentInfo student1 = (DuplicateAssignedStudentInfo)this.pageDuplicateStudentList.get(i);        
                for (int j=0 ; j<this.dupStudentList.size(); j++) {   
                    DuplicateAssignedStudentInfo student2 = (DuplicateAssignedStudentInfo)this.dupStudentList.get(j);        
                    if ((student1 != null) && (student2 != null)) {
                        if (student1.getId().intValue() == student2.getId().intValue()) { 
                            student2.setSelectedOrgNodeName(student1.getSelectedOrgNodeName());
                        }                        
                    }
                }
            }
        }
    }
    
    private boolean isNeedCommitSelection(String currentAction, String actionElement, ScheduleTestController.ScheduleTestForm form)
    {
        boolean needCommit = false;
        if ((currentAction != null) && (currentAction.equals(ACTION_UPDATE_TOTAL)))
            needCommit = true;
        if ((actionElement != null) && actionElement.equals("{actionForm.studentStatePathList.pageRequested}"))
            needCommit = true;
        if ((actionElement != null) && actionElement.equals("{actionForm.orgNodeId}"))
            needCommit = true;
        if ((actionElement != null) && actionElement.equals("{actionForm.studentStatePathList.sortOrderBy}"))
            needCommit = true;
        //Added for Table pager go button start
        if ((actionElement != null) && actionElement.equals("EnterKeyInvoked_studentTableAnchor"))
            needCommit = true;
        if ((actionElement != null) && actionElement.equals("ButtonGoInvoked_studentTableAnchor"))
            needCommit = true;
      //Added for Table pager go button end
            
        Integer orgNodeId = form.getSelectedOrgNodeId();
        if ((orgNodeId != null) && (this.selectedOrgNodeId != null) && 
            (orgNodeId.intValue() != this.selectedOrgNodeId.intValue()))
            needCommit = true;
        
        return needCommit;                         
    }
    
    private void commitSelection(ScheduleTestController.ScheduleTestForm form)
    {
        String [] selectedStudentOrgList = form.getSelectedStudentOrgList();
        collectSelectedStudents(selectedStudentOrgList);
    }    
    
    private void addAllStudents(FilterParams filter, Integer orgNodeId, Integer testAdminId)
    {
        PageParams page = new PageParams(1, 10000, 1000);
        
        SessionStudentData ssd = getSessionStudents(orgNodeId, testAdminId, filter, page, null);
        List studentNodes = buildStudentList(ssd);
        
        for (int i=0; i <studentNodes.size(); i++) {   
            SessionStudent ss = (SessionStudent) studentNodes.get(i);         
            if ( (ss != null) && (!"F".equals(ss.getStatus().getEditable())) ) {   
                addSelectedStudentToList(ss);                 
            }
        }
    }

    private void copySelectedStudents(ScheduleTestController.ScheduleTestForm form)
    {
        this.selectedStudents = form.getSelectedStudents();    
        if (this.selectedStudents == null)
            this.selectedStudents = new ArrayList();  
            
        this.originalSelectedStudents = new ArrayList();  
            
        for (int i=0 ; i<this.selectedStudents.size() ; i++) {
            SessionStudent ss = (SessionStudent) this.selectedStudents.get(i);
            String key = encodeStudentOrgIds(ss);
            ss.setExtElmId(key);
            this.originalSelectedStudents.add(ss);
        }
    }

    private void resetFormOnReturn(ScheduleTestController.ScheduleTestForm form, String actionElement)
    {
        form.setCurrentAction(ACTION_DEFAULT);
        form.setActionElement(actionElement);       
        form.getStudentStatePathList().resetValues(ColumnSortEntry.ASCENDING, FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN) ;
    }

    private void resetSortingPaging(ScheduleTestController.ScheduleTestForm form)
    {
        form.getOrgStatePathList().resetValues(ColumnSortEntry.ASCENDING, FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN);
        form.getStudentStatePathList().resetValues(ColumnSortEntry.ASCENDING, FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN) ;
    }
    
    private String [] getGradeOptionsForStudent()
    {
        String[] grades = null;
        try {
            User user = this.scheduleTest.getUserDetails(this.userName, this.userName);
            Integer customerId = user.getCustomer().getCustomerId();
            grades =  this.scheduleTest.getGradesForCustomer(this.userName, customerId);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        
        List options = new ArrayList();
        options.add(FilterSortPageUtils.FILTERTYPE_SHOWALL);

        for (int i=0 ; i<grades.length ; i++) {        
            options.add(grades[i]);
        }
                        
        return (String [])options.toArray(new String[0]);        
    }
    
    
    /**
     * getLicenseQuantitiesByOrg
     */    
    private OrgNodeLicenseInfo getLicenseQuantitiesByOrg(Integer orgNodeId) {
    	
        OrgNodeLicenseInfo onli = null;    
        Integer productId = this.customerLicenses[0].getProductId();
        String subtestModel = this.customerLicenses[0].getSubtestModel();
        
        try {
        	
            onli = this.license.getLicenseQuantitiesByOrgNodeIdAndProductId(this.userName, 
										                    orgNodeId, 
										                    productId, 
										                    subtestModel);
										                    
        }    
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        
        return onli;
    }
    
    /**
     * getCustomerLicenses
     */
    private CustomerLicense[] getCustomerLicenses()
    {
        CustomerLicense[] cls = null;

        try
        {
            cls = this.license.getCustomerOrgNodeLicenseData(this.userName, null);
        }    
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
     
        return cls;
    }
    
    /**
     * setupLicenseInfo
     */
    private boolean setupLicenseInfo(Integer orgNodeId, String orgNodeName, OrgNodeLicenseInfo onli, 
    				ScheduleTestController.ScheduleTestForm form, String actionElement) {

        boolean showLicense = false;    	
                
        if ((onli != null) && (orgNodeId != null) && (orgNodeId.intValue() > 0)) {
            calculateAvailableLicenseByOrganization(orgNodeId, orgNodeName, onli, form, actionElement);
            showLicense = true;
        } 
        
    	return showLicense;
    }
    
    
    /**
     * deductLicenseFromOrgNode
     */
    private Integer deductLicenseFromOrgNode(Integer orgNodeId, Integer availableLicense) {
    	int available = availableLicense.intValue();
        
    	for (int i=0; i < this.selectedStudents.size(); i++) {
            SessionStudent ss = (SessionStudent)this.selectedStudents.get(i);
            if (ss.getOrgNodeId().intValue() == orgNodeId.intValue()) {
            	available--;
            }
        }
        
        return new Integer(available);
    }
    
    /**
     * calculateAvailableLicenseByOrganization
     */
    private void calculateAvailableLicenseByOrganization(Integer orgNodeId, String orgNodeName, OrgNodeLicenseInfo onli, 
    					ScheduleTestController.ScheduleTestForm form, String actionElement) {
        
    	Integer availableLicense = onli.getLicPurchased();
    	Integer usedLicenses = usedLicensesInNode(orgNodeId);
        double usedLicPercent = 101;
        
        if ((availableLicense.intValue() == 0) && (usedLicenses.intValue() == 0)) { 
        	usedLicPercent = 0;
        }
        if (availableLicense.intValue() > 0) { 
        	usedLicPercent = Math.round(((usedLicenses.doubleValue() * 100) / availableLicense.doubleValue() ));
        }
                
        if (usedLicenses.intValue() > availableLicense.intValue()) {
        	Integer licenseNeeded = new Integer(usedLicenses.intValue() - availableLicense.intValue());
		    setLicenseErrorMessage(form, orgNodeName, licenseNeeded, this.commitSelection);
		}
		
        String availableLicensePercent = null;
        
        if (usedLicPercent > 100) { 
        	availableLicensePercent = "Exceeds Licenses Available (over 100%)";
    		licenseBarColor = Message.LOW_LICENSE_COLOR;
        }
        else {
            Integer usedLicensePercentage = new Integer(new Double(usedLicPercent).intValue());
        	
        	availableLicensePercent = "Used " + usedLicenses + " out of " + availableLicense + 
												" licenses ("+ usedLicensePercentage.toString() + "%)";        	
	        if (usedLicensePercentage.intValue() >= 90 ) 
	    		licenseBarColor = Message.LOW_LICENSE_COLOR;
	        else 
	        if (usedLicensePercentage.intValue() >= 70) 
	           	licenseBarColor = Message.MEDIUM_LICENSE_COLOR;     
	        else 
	        	licenseBarColor = Message.HIGH_LICENSE_COLOR;
        }
        
        form.setLicenseAvailable(availableLicense);
        form.setLicenseModel(this.customerLicenses[0].getSubtestModel());
        form.setLicensePercentage(availableLicensePercent);
         
    }

    
    /**
     * getScheduledOrgNodes
     */
    private List getScheduledOrgNodes() {
    	ArrayList scheduledOrgNodes = new ArrayList();
        
    	for (int i=0; i<this.selectedStudents.size(); i++) {
            SessionStudent ss = (SessionStudent)this.selectedStudents.get(i);
            boolean found = false;
        	for (int j=0; j<scheduledOrgNodes.size(); j++) {
        		OrganizationVO orgNode = (OrganizationVO)scheduledOrgNodes.get(j);
                if (ss.getOrgNodeId().intValue() == orgNodeId.intValue()) {
                	found = true;
                }
        	}
        	if (! found) {
        		OrganizationVO orgNode = new OrganizationVO(ss.getOrgNodeId(), ss.getOrgNodeName(), null, null);
        		scheduledOrgNodes.add(orgNode);
        	}
        }
        
        return scheduledOrgNodes;
    }
    
    /**
     * verifyLicenseAvailability
     */
    private boolean verifyLicenseAvailability(ScheduleTestController.ScheduleTestForm form, int duplicateStudentsNum) {
        
        List scheduledOrgNodes = getScheduledOrgNodes();
    	for (int i=0; i<scheduledOrgNodes.size(); i++) {
    		OrganizationVO orgNode = (OrganizationVO)scheduledOrgNodes.get(i);    		
            OrgNodeLicenseInfo onli = getLicenseQuantitiesByOrg(orgNode.getId());
        	Integer availableLicense = onli.getLicPurchased();
        	Integer usedLicenses = usedLicensesInNode(orgNode.getId());
            if (usedLicenses.intValue() > availableLicense.intValue()) {
            	Integer licenseNeeded = new Integer(usedLicenses.intValue() - availableLicense.intValue());
    		    setLicenseErrorMessage(form, orgNode.getOrganizationName(), licenseNeeded, true);
    			return false;
    		}
    	}
    	
        return true;
                            
    }
    
    /**
     * usedLicensesInNode
     */
    private Integer usedLicensesInNode(Integer orgNodeId) {

    	List existingStudents = getExistingStudentsInSessionForOrgNode(orgNodeId);
    	
    	int licensePerSubtest = 1;
        String subtestModel = this.customerLicenses[0].getSubtestModel();
    	
    	if (subtestModel.equals("T")) {
            licensePerSubtest = getNumberOfSubtests();
    	}
    	
    	int usedLicenses = 0;
        
    	for (int i=0; i<this.selectedStudents.size(); i++) {
            SessionStudent ss = (SessionStudent)this.selectedStudents.get(i);
            if (ss.getOrgNodeId().intValue() == orgNodeId.intValue()) {
            	if (! isExistingStudent(ss.getStudentId(), existingStudents)) {
            		usedLicenses += licensePerSubtest;
            	}
            }
        }
        
        return new Integer(usedLicenses);
    }

    /**
     * getNumberOfSubtests
     */
    private int getNumberOfSubtests() {
    	
    	List result = new ArrayList();
        ScheduleTestController parentPageFlow = (ScheduleTestController)PageFlowUtils.getNestingPageFlow(getRequest());
        List subtests = parentPageFlow.defaultSubtests;
        return subtests.size();
    }
    
    /**
     * getExistingStudentsInSessionForOrgNode
     */
    private boolean isExistingStudent(Integer studentId, List existingStudents) {
		for (int i=0; i<existingStudents.size(); i++) {
	        SessionStudent ss = (SessionStudent)existingStudents.get(i);
	        if (ss.getStudentId().intValue() == studentId.intValue()) {
	        	return true;
	        }
	    }
		return false;
    }
    
    /**
     * getExistingStudentsInSessionForOrgNode
     */
    private List getExistingStudentsInSessionForOrgNode(Integer orgNodeId) {
    	
    	List result = new ArrayList();
        ScheduleTestController parentPageFlow = (ScheduleTestController)PageFlowUtils.getNestingPageFlow(getRequest());
        ScheduledSession scheduledSession = parentPageFlow.getScheduledSession();
        
        if ((scheduledSession != null) &&  (scheduledSession.getStudents()!= null)) {
        	SessionStudent[] students = scheduledSession.getStudents();
        	if ((students != null && students.length > 0)) {
        		for (int i=0 ;i<students.length ; i++) {
                    SessionStudent ss = (SessionStudent)students[i];
                    if (ss.getOrgNodeId().intValue() == orgNodeId.intValue()) {
                    	result.add(ss);
                    }       			
        		}
        	}
        }
        
        return result;
    }
    
    private void setLicenseErrorMessage(ScheduleTestController.ScheduleTestForm form, String orgNodeName, 
    									Integer licenseNeeded, boolean stopIcon) {
        String errMsg = "<b>" + orgNodeName + "</b>: " + MessageResourceBundle.getMessage("Licence.Insufficient") +
        				" You need <b>" + licenseNeeded.toString() + "</b> more licenses.";
        String type = stopIcon? Message.ERROR : Message.ALERT;
        form.setMessage(Message.LICENSE_TITLE, errMsg, type);       	
    }
    
	public Boolean getOffGradeTestingDisabled() {
		return offGradeTestingDisabled;
	}

	public Boolean getBlockOffGradeTesting() {
		return blockOffGradeTesting;
	}
	
	public String getLicenseBarColor() {
		return licenseBarColor;
	}

	public String[] getAccommodationOperandOptions() {
		return accommodationOperandOptions;
	}

	public String[] getSelectedAccommodationsOptions() {
		return selectedAccommodationsOptions;
	}

	public String getAction() {
		return action;
	}

	public String[] getGradeOptions() {
		return gradeOptions;
	}

	public List getDupStudentList() {
		return dupStudentList;
	}
  
}
