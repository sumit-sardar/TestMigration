package homepage;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.BroadcastMessage;
import com.ctb.bean.testAdmin.BroadcastMessageData;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.CustomerReport;
import com.ctb.bean.testAdmin.CustomerReportData;
import com.ctb.bean.testAdmin.ProgramData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.testSessionInfo.dto.ReportManager;
import com.ctb.widgets.bean.PagerSummary;
import com.ctb.testSessionInfo.dto.TestSessionVO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.ctb.testSessionInfo.utils.FilterSortPageUtils;
import com.ctb.testSessionInfo.utils.PermissionsUtils;
import com.ctb.util.web.sanitizer.JavaScriptSanitizer;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import javax.servlet.http.HttpServletRequest;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
      
/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class HomePageController extends PageFlowController
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
    private com.ctb.control.licensing.Licensing licensing;
   
    
    private String userName = null;
    private User user = null;
    private UserNodeData userTopNodes = null;
    private ProgramData userPrograms = null;

    private CustomerReportData customerReportData = null;                

    public ReportManager reportManager = null;
    
    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="home_page.do"
     * @jpf:forward name="resetPassword" path="resetPassword.do"
     * @jpf:forward name="editTimeZone" path="setTimeZone.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "home_page.do"), 
        @Jpf.Forward(name = "resetPassword",
                     path = "resetPassword.do"), 
        @Jpf.Forward(name = "editTimeZone",
                     path = "setTimeZone.do")
    })
    protected Forward begin()
    {        
    	getLoggedInUserPrincipal();   
        getUserDetails();
                         
        HomePageForm form = new HomePageForm();
        form.init();
        
        if ("T".equals(this.user.getResetPassword()))
        {
            return new Forward("resetPassword", form);
        }
        
        /*Changed for DEx defect # 57562 & 57563*/ 
        else if (this.user.getTimeZone() == null)
        {
            return new Forward("editTimeZone", form);
            
        }
        else
        {
            return new Forward("success", form);
        }
    }

    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward resetPassword(HomePageForm form)
    {               
        try
        {
            String url = "/UserManagementWeb/manageUser/resetPassword.do";
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
    protected Forward setTimeZone(HomePageForm form)
    {               
        try
        {
            String url = "/UserManagementWeb/manageUser/beginEditMyProfile.do?isSetTimeZone=true";
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
     * @jpf:forward name="success" path="home_page.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "home_page.do")
    })
    protected Forward returnToHomePage()
    {               
        HomePageForm form = new HomePageForm();
        form.init();

        return new Forward("success", form);
    }
        
    /**
     * @jpf:action
     * @jpf:forward name="success" path="home_page.jsp"
     * @jpf:forward name="accountManagerHomePage" path="account_manager_home_page.jsp"
     * @jpf:validation-error-forward name="failure" path="logout.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "home_page.jsp"), 
        @Jpf.Forward(name = "accountManagerHomePage",
                     path = "account_manager_home_page.jsp")
    }, 
        validationErrorForward = @Jpf.Forward(name = "failure",
                                              path = "logout.do"))
    protected Forward home_page(HomePageForm form)
    {
    	
        if (this.user.getRole().getRoleName().equals("ACCOUNT MANAGER"))
        {


            // get CTB broadcast messages
            List broadcastMessages = getBroadcastMessages(true);
            this.getRequest().setAttribute("broadcastMessages", broadcastMessages);
    
            // Flag that we are the homepage, so set the navigation link to "current"
            this.getRequest().setAttribute("isHomePage", Boolean.TRUE);
            
            form.setActionElement("none");   
            
            return new Forward("accountManagerHomePage", form);
        }
        
        
        form.resetValuesForAction();        
        form.validateValues();
        
        // retrieve information for user test sessions
        FilterParams sessionFilter = FilterSortPageUtils.buildFilterParams(FilterSortPageUtils.TESTSESSION_DEFAULT_FILTER_COLUMN, form.getUserSessionFilterTab());
        PageParams sessionPage = FilterSortPageUtils.buildPageParams(form.getUserSessionPageRequested(), FilterSortPageUtils.PAGESIZE_5);
        SortParams sessionSort = null;
        if (form.getUserSessionSortColumn() != null)
            sessionSort = FilterSortPageUtils.buildSortParams(form.getUserSessionSortColumn(), form.getUserSessionSortOrderBy());
        TestSessionData tsd = getTestSessionsForUser(sessionFilter, sessionPage, sessionSort);
        List sessionList = buildTestSessionList(tsd); 
        String userOrgCategoryName = getTestSessionOrgCategoryName(sessionList);
        
        prepareSessionSelection(sessionList, form, "userSessionDisableButton");
        form.setUserMaxPage(tsd.getFilteredPages());
        
        // build user test sessions request information for jsp         
        PagerSummary sessionPagerSummary = buildSessionPagerSummary(tsd, form.getUserSessionPageRequested()); 
        
        this.getRequest().setAttribute("userSessions", sessionList);
        this.getRequest().setAttribute("userSessionPagerSummary", sessionPagerSummary);
        this.getRequest().setAttribute("hasTestSessions", sessionList.size() > 0 ? "true" : null);
        this.getRequest().setAttribute("userOrgCategoryName", userOrgCategoryName);        

        // retrieve information for proctor assignment test sessions
        sessionFilter = FilterSortPageUtils.buildFilterParams(FilterSortPageUtils.TESTSESSION_DEFAULT_FILTER_COLUMN, form.getProctorSessionFilterTab());
        sessionPage = FilterSortPageUtils.buildPageParams(form.getProctorSessionPageRequested(), FilterSortPageUtils.PAGESIZE_5);
        sessionSort = null;
        if (form.getProctorSessionSortColumn() != null)
            sessionSort = FilterSortPageUtils.buildSortParams(form.getProctorSessionSortColumn(), form.getProctorSessionSortOrderBy());
        tsd = getProctorAssignmentsForUser(sessionFilter, sessionPage, sessionSort);
        List proctorSessionList = buildTestSessionList(tsd); 
        String proctorOrgCategoryName = getTestSessionOrgCategoryName(proctorSessionList);
        
        prepareSessionSelection(proctorSessionList, form, "proctorSessionDisableButton");
        form.setProctorMaxPage(tsd.getFilteredPages());
        
        // build user proctor assignment test sessions request information for jsp         
        PagerSummary proctorPagerSummary = buildSessionPagerSummary(tsd, form.getProctorSessionPageRequested()); 
        this.getRequest().setAttribute("proctorAssignmentSessions", proctorSessionList);
        this.getRequest().setAttribute("proctorSessionPagerSummary", proctorPagerSummary);
        this.getRequest().setAttribute("proctorOrgCategoryName", proctorOrgCategoryName);        

        this.getSession().setAttribute("hasLicenseConfig", hasLicenseConfig());

        // get licenses
        CustomerLicense[] customerLicenses = getCustomerLicenses();
        if ((customerLicenses != null) && (customerLicenses.length > 0))
        {
            this.getRequest().setAttribute("customerLicenses", customerLicenses);
        }

        //check avaliable license count for Register Student
        registerStudentEnable(customerLicenses, sessionList);
       
        //check avaliable license count for Register Student
        registerStudentEnable(customerLicenses, proctorSessionList); 
        
        // get CTB broadcast messages
        List broadcastMessages = getBroadcastMessages(false);
        this.getRequest().setAttribute("broadcastMessages", broadcastMessages);

        // Flag that we are the homepage, so set the navigation link to "current"
        this.getRequest().setAttribute("isHomePage", Boolean.TRUE);
         
        this.getSession().setAttribute("userHasReports", userHasReports());

        this.getSession().setAttribute("canRegisterStudent", canRegisterStudent());
        
        String userSessionFilterTab = form.getUserSessionFilterTab();
        if (userSessionFilterTab.equalsIgnoreCase("PA"))
        {
            this.getRequest().setAttribute("enableUserRegisterStudent", Boolean.FALSE);
        }
        else
        {
            this.getRequest().setAttribute("enableUserRegisterStudent", Boolean.TRUE);
        }
        
        String proctorSessionFilterTab = form.getProctorSessionFilterTab();
        if (proctorSessionFilterTab.equalsIgnoreCase("PA"))
        {
            this.getRequest().setAttribute("enableProctorRegisterStudent", Boolean.FALSE);
        }
        else
        {
            this.getRequest().setAttribute("enableProctorRegisterStudent", Boolean.TRUE);
        }

        form.setActionElement("none");   
        return new Forward("success", form);
    }
  
    private void getLoggedInUserPrincipal()
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
        if (principal != null) 
            this.userName = principal.toString();
            
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
            Integer customerId = customer.getCustomerId();
            String hideAccommodations = customer.getHideAccommodations();
            if ((hideAccommodations != null) && hideAccommodations.equalsIgnoreCase("T"))
            {
                supportAccommodations = Boolean.FALSE;
            }
            userTimeZone = this.user.getTimeZone();
            getSession().setAttribute("customerId", customerId); 
            getSession().setAttribute("supportAccommodations", supportAccommodations); 
            getSession().setAttribute("userTimeZone", userTimeZone); 
            
            SortParams orgNodeNameSort = FilterSortPageUtils.buildSortParams("OrgNodeName", "asc");                        
            this.userTopNodes = this.testSessionStatus.getTopUserNodesForUser(this.userName, null, null, null, orgNodeNameSort);
                        
            String[] sortNames = new String[2];
            sortNames[0] = "ProductId"; 
            sortNames[1] = "ProgramStartDate"; 
            String[] sortOrderBys = new String[2];
            sortOrderBys[0] = "asc";
            sortOrderBys[1] = "desc";
            SortParams programNameSort = FilterSortPageUtils.buildSortParams(sortNames, sortOrderBys);    
                               
            this.userPrograms = testSessionStatus.getProgramsForUser(this.userName, null, null, programNameSort);
            
            this.reportManager = new ReportManager();
            this.reportManager.initPrograms(this.userPrograms);
            this.reportManager.initOrganizations(this.userTopNodes);    
        	
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
    }
      
    private void prepareSessionSelection(List sessionList, HomePageForm form, String sessionDisableType)
    {
        String sessionId = null;
        if (sessionDisableType.equals("userSessionDisableButton")) 
            sessionId = form.getUserSessionId();
        else 
            sessionId = form.getProctorSessionId();     
            
        boolean found = false;
        if ((sessionId != null) && (sessionList != null) && (sessionList.size() > 0))
        {        
            for (int i=0; i < sessionList.size(); i++)
            {
                TestSessionVO ts = (TestSessionVO)sessionList.get(i);
                if ((ts != null) && ts.getTestAdminId().equals(sessionId))
                {
                    found = true;
                }
            }
        }
        if (found)
        {
            this.getRequest().setAttribute(sessionDisableType, "false");
        }
        else
        {
            if (sessionDisableType.equals("userSessionDisableButton")) 
                form.setUserSessionId(null);
            else
                form.setProctorSessionId(null);
            this.getRequest().setAttribute(sessionDisableType, "true");
        }         
    }


    /**
     * getCustomerLicenses
     */
    private CustomerLicense[] getCustomerLicenses()
    {
        CustomerLicense[] cls = null;

        try
        {
            cls = this.licensing.getCustomerLicenseData(this.userName, null);
        }    
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
     
        return cls;
    }

    /**
     * hasLicenseConfig
     */
    private Boolean hasLicenseConfig()
    {
        Boolean hasLicenseConfig = Boolean.FALSE;       
      
        try
        {
            CustomerLicense[] cls = this.licensing.getCustomerLicenseData(this.userName, null);            
            hasLicenseConfig = new Boolean(cls.length > 0);
        }    
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }     
        
        return hasLicenseConfig;
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
    
    private CustomerReportData getCustomerReportData(Integer orgNodeId, Integer programId) 
    {
        if (orgNodeId == null)
        {
            orgNodeId = this.reportManager.setSelectedOrganization(null);
        }
        if (programId == null)
        {
            programId = this.reportManager.setSelectedProgram(null);
        }
        
        CustomerReportData crd = null;
        try
        {      
            SortParams sort = FilterSortPageUtils.buildSortParams("DisplayName", "asc");            
            crd = this.testSessionStatus.getCustomerReportData(this.userName, orgNodeId, programId, null, null, sort);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return crd;
    }

    private Boolean canRegisterStudent() 
    {               
        Integer customerId = this.user.getCustomer().getCustomerId();
        String roleName = this.user.getRole().getRoleName();        
        boolean validCustomer = false; 

        try
        {      
            CustomerConfiguration [] ccArray = this.testSessionStatus.getCustomerConfigurations(this.userName, customerId);       
            for (int i=0; i < ccArray.length; i++)
            {
                CustomerConfiguration cc = (CustomerConfiguration)ccArray[i];
                if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer"))
                {
                    validCustomer = true; 
                }
            }
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }        
        
        boolean validUser = (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
        
        return new Boolean(validCustomer && validUser);
    }
 
    private TestSessionData getTestSessionsForUser(FilterParams filter, PageParams page, SortParams sort) 
    {
        TestSessionData tsd = new TestSessionData();                
        try
        {      
            tsd = this.testSessionStatus.getTestSessionsForUser(this.userName, filter, page, sort);            
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return tsd;
    }

    private TestSessionData getProctorAssignmentsForUser(FilterParams filter, PageParams page, SortParams sort) 
    {
        TestSessionData tsd = new TestSessionData();                
        try
        {      
            tsd = this.testSessionStatus.getProctorAssignmentsForUser(this.userName, filter, page, sort);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return tsd;
    }

    private List getBroadcastMessages(boolean isAccountManager)
    {        
        BroadcastMessageData bmd = null;
        BroadcastMessage[] broadcastMessages = null;
        try
        { 
            if (isAccountManager)
            {
                bmd = this.testSessionStatus.getBroadcastMessagesForActManager(this.userName);
            }
            else
            {
                bmd = this.testSessionStatus.getBroadcastMessages(this.userName);
            }
            broadcastMessages = bmd.getBroadcastMessages();
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }

        List messages = null;
        if (broadcastMessages.length > 0)
        {
            messages = new ArrayList();
            for (int i=0; i < broadcastMessages.length; i++)
            {
                messages.add(broadcastMessages[i]);
            }
        }                
        return messages;    
    }
    
    private List buildTestSessionList(TestSessionData tsd) 
    {
        List sessionList = new ArrayList();                       
        TestSession[] testsessions = tsd.getTestSessions();            
        for (int i=0; i < testsessions.length; i++)
        {
            TestSession ts = testsessions[i];
            if (ts != null)
            {
                TestSessionVO vo = new TestSessionVO(ts);
                sessionList.add(vo);
            }
        }
        return sessionList;
    }
    
    private String getTestSessionOrgCategoryName(List testSessionList)
    {
        String categoryName = "Organization";        
        if (testSessionList.size() > 0)
        {
            TestSessionVO vo = (TestSessionVO)testSessionList.get(0);
            categoryName = vo.getCreatorOrgNodeCategoryName();
            for (int i=1; i < testSessionList.size(); i++)
            {
                vo = (TestSessionVO)testSessionList.get(i);
                if (! vo.getCreatorOrgNodeCategoryName().equals(categoryName))
                {
                    categoryName = "Organization";
                    break;
                }
            }
        }
        return categoryName;        
    }
    
    private PagerSummary buildSessionPagerSummary(TestSessionData tsd, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);
        pagerSummary.setTotalObjects(tsd.getFilteredCount());
        pagerSummary.setTotalPages(tsd.getFilteredPages());
        pagerSummary.setTotalFilteredObjects(null);        
        return pagerSummary;
    }
    
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/viewmonitorstatus/ViewMonitorStatusController.jpf"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "/viewmonitorstatus/ViewMonitorStatusController.jpf")
    })
    protected Forward goto_view_user_session_monitor_status(HomePageForm form)
    {
        String sessionId = form.getUserSessionId();
        getSession().setAttribute("sessionId", sessionId);
        getSession().setAttribute("callerId", "goto_homepage");
        String sessionFilterTab = form.getUserSessionFilterTab();
        getSession().setAttribute("sessionFilterTab", sessionFilterTab);
        
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/viewmonitorstatus/ViewMonitorStatusController.jpf"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "/viewmonitorstatus/ViewMonitorStatusController.jpf")
    })
    protected Forward goto_view_proctor_session_monitor_status(HomePageForm form)
    {
        String sessionId = form.getProctorSessionId();
        getSession().setAttribute("sessionId", sessionId);
        getSession().setAttribute("callerId", "goto_homepage");        
        String sessionFilterTab = form.getProctorSessionFilterTab();
        getSession().setAttribute("sessionFilterTab", sessionFilterTab);

        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/viewtestsessions/ViewTestSessionsController.jpf"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "/viewtestsessions/ViewTestSessionsController.jpf")
    })
    protected Forward goto_view_all_test_sessions(HomePageForm form)
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward goto_user_view_session_information(HomePageForm form)
    {
        try
        {
            String contextPath = "/TestAdministrationWeb/scheduleTestPageflow/selectSettings.do";
            String action = "action=view";
            String testAdminId = this.getRequest().getParameter("testAdminId");
            String url = contextPath + "?" + action + "&testAdminId=" + testAdminId;            
            getSession().setAttribute("sessionId", testAdminId);
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
    protected Forward goto_user_edit_session_information(HomePageForm form)
    {
        try
        {
            String contextPath = "/TestAdministrationWeb/scheduleTestPageflow/selectSettings.do";
            String action = "action=edit";
            String testAdminId = "testAdminId=" +
                                 form.getUserSessionId();
            String url = contextPath + "?" + action + "&" + testAdminId;            
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
    protected Forward goto_proctor_view_session_information(HomePageForm form)
    {
        try
        {
            String contextPath = "/TestAdministrationWeb/scheduleTestPageflow/selectSettings.do";
            String action = "action=view";            
            String testAdminId = this.getRequest().getParameter("testAdminId");
            String url = contextPath + "?" + action + "&testAdminId=" + testAdminId;            
            getSession().setAttribute("sessionId", testAdminId);
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
    protected Forward goto_proctor_edit_session_information(HomePageForm form)
    {
        try
        {
            String contextPath = "/TestAdministrationWeb/scheduleTestPageflow/selectSettings.do";
            String action = "action=edit";
            String testAdminId = "testAdminId=" +
                                 form.getProctorSessionId();
            String url = contextPath + "?" + action + "&" + testAdminId;            
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
     * @jpf:forward name="home" path="turnleaf_report_home.jsp"
     * @jpf:forward name="report" path="turnleaf_reports.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "home",
                     path = "turnleaf_report_home.jsp"), 
        @Jpf.Forward(name = "report",
                     path = "turnleaf_reports.jsp")
    })
    protected Forward viewReports()
    {
        if (this.reportManager == null)
        {
        	System.out.println("ViewReports: this.reportManager == null");
        	getLoggedInUserPrincipal();   
            getUserDetails();            
        }else{
        	System.out.println("this.reportManager.getSelectedProgramId: "+this.reportManager.getSelectedProgramId());
        	System.out.println("this.reportManager.getSelectedProgramName: "+this.reportManager.getSelectedProgramName());
        	System.out.println("this.reportManager.getSelectedOrganizationId: "+this.reportManager.getSelectedOrganizationId());
        	System.out.println("this.reportManager.getSelectedOrganizationName: "+this.reportManager.getSelectedOrganizationName());
        }
        
        String selectedReport = (String)this.getRequest().getParameter("report");
        System.out.println("selectedReport: "+selectedReport);
        
        Integer programId = this.reportManager.getSelectedProgramId();
        Integer orgNodeId = this.reportManager.getSelectedOrganizationId();

        List reportList = buildReportList(orgNodeId, programId);
        String reportUrl = buildReportUrl(selectedReport, reportList);        
        System.out.println("Report URL in controller: " + reportUrl);
        
        
        this.getRequest().setAttribute("reportList", reportList);
        this.getRequest().setAttribute("selectedReport", selectedReport);
        this.getRequest().setAttribute("reportUrl", reportUrl);

        this.getRequest().setAttribute("programList", this.reportManager.getProgramNames());
        this.getRequest().setAttribute("program", this.reportManager.getSelectedProgramName());

        this.getRequest().setAttribute("organizationList", this.reportManager.getOrganizationNames());
        this.getRequest().setAttribute("organization", this.reportManager.getSelectedOrganizationName());

        this.getRequest().setAttribute("multipleProgram", this.reportManager.isMultiplePrograms());
        this.getRequest().setAttribute("multipleOrganizations", this.reportManager.isMultipleOrganizations());
        this.getRequest().setAttribute("singleProgOrg", this.reportManager.isSingleProgramAndOrganization());

        if (selectedReport == null) 
            return new Forward("home");
        else 
            return new Forward("report");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="turnleaf_report_list.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "turnleaf_report_list.jsp")
    })
    protected Forward getReportList()
    {
        String programIndex = this.getRequest().getHeader("programIndex");        
        String organizationIndex = this.getRequest().getHeader("organizationIndex");
        
        Integer programId = this.reportManager.setSelectedProgram(programIndex);
        Integer orgNodeId = this.reportManager.setSelectedOrganization(organizationIndex);
        
System.out.println("getReportList:   " + "   programId = " + programId + "       orgNodeId = " + orgNodeId);

        List reportList = buildReportList(orgNodeId, programId);

        this.getRequest().setAttribute("reportList", reportList);

        return new Forward("success");
    }

    
    /**
     * buildReportList
     */
    private List buildReportList(Integer orgNodeId, Integer programId)
    {
System.out.println("buildReportList:   " + "   programId = " + programId + "       orgNodeId = " + orgNodeId);
    	
        this.customerReportData = getCustomerReportData(orgNodeId, programId);
        
        List reportList = new ArrayList();
        CustomerReport[] crs = this.customerReportData.getCustomerReports();
        
        for (int i=0; i < crs.length; i++)
        {                
            CustomerReport cr = crs[i];
            if (! cr.getReportName().equals("IndividualProfile"))
            {
            	System.out.println("ProgramName = " + cr.getProgramName() + "  ReportName = " + cr.getReportName() + "  ReportUrl = " + cr.getReportUrl());
            	
                reportList.add(cr);
            }
        }           
                         
        return reportList; 
    }

    /**
     * buildReportUrl
     */
    private String buildReportUrl(String reportName, List reportList)
    {
        String reportUrl = null;
        if (reportName != null)
        {        
            for (int i=0; i < reportList.size(); i++)
            {                
                CustomerReport cr = (CustomerReport)reportList.get(i);
                if (cr.getReportName().equals(reportName))
                {
                    reportUrl = cr.getReportUrl();
                }
            }
        }                    
        
        return reportUrl; 
    }
    
    /**
     * get value for enabling RegisterStudent button.
     */
    private void registerStudentEnable(CustomerLicense[] customerLicenses, List sessionList)
    {    
    	if (customerLicenses == null) {
    		return;
    	}
    	
        for (int i=0; i < sessionList.size(); i++)
        {
            
            TestSessionVO testSessionVo =(TestSessionVO)sessionList.get(i);
            boolean flag = false;
            
            if (testSessionVo.getLicenseEnabled().equals("T"))
            {
            
                for (int j=0; j < customerLicenses.length; j++)
                { 
                            
                    if (customerLicenses[j].getProductId().intValue() == testSessionVo.getProductId().intValue() || customerLicenses[j].getProductId().intValue() == testSessionVo.
                        getParentProductId().intValue())
                    {
                        
                        flag = true;      
                        
                        if (customerLicenses[j].getAvailable().intValue() > 0)
                        { 
                            
                            testSessionVo.setIsRegisterStudentEnable("T");
                          
                        }
                        else if (customerLicenses[j].getAvailable().intValue() <= 0) {
                            
                            testSessionVo.setIsRegisterStudentEnable("F");
                            
                      }
                      
                      break;
                  }
                }
            } 
            if (!flag) {
                
                testSessionVo.setIsRegisterStudentEnable("T");   
                
            }
                 
         }                
   
    }
    
    /**
     * @jpf:action
     */
	@Jpf.Action()
    protected Forward goto_user_register_student(HomePageForm form)
    {
        try {
            String contextPath = "/StudentRegistrationWeb/registration/begin.do";
            String testAdminId = "testAdminId=" + form.getUserSessionId();
            String url = contextPath + "?" + testAdminId;            
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
    protected Forward goto_protor_register_student(HomePageForm form)
    {
        try {
            String contextPath = "/StudentRegistrationWeb/registration/begin.do";
            String testAdminId = "testAdminId=" + form.getProctorSessionId();
            String url = contextPath + "?" + testAdminId;            
            getResponse().sendRedirect(url);
        } 
        catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }

	/**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class HomePageForm extends SanitizedFormData
    {
		private static final long serialVersionUID = 1L;
		
		private String userSessionFilterTab = null;
        private Integer userSessionPageRequested = null;
        private String userSessionSortColumn = null;
        private String userSessionSortOrderBy = null;
        private Integer userMaxPage = null;
                
        private String proctorSessionFilterTab = null;        
        private Integer proctorSessionPageRequested = null;
        private String proctorSessionSortColumn = null;
        private String proctorSessionSortOrderBy = null;
        private Integer proctorMaxPage = null;

        private String userSessionId = null;
        private String proctorSessionId = null;

        private String actionElement = null;

        private Boolean licenseVisible = Boolean.TRUE;

        public HomePageForm()
        {
        }
        public void init()
        {
            this.userSessionFilterTab = FilterSortPageUtils.TESTSESSION_DEFAULT_FILTER_VALUE;
            this.userSessionPageRequested = new Integer(1);
            this.userSessionSortColumn = FilterSortPageUtils.TESTSESSION_DEFAULT_SORT;
            this.userSessionSortOrderBy = FilterSortPageUtils.ASCENDING;
            this.userMaxPage = new Integer(1);      

            this.proctorSessionFilterTab = FilterSortPageUtils.TESTSESSION_DEFAULT_FILTER_VALUE;
            this.proctorSessionPageRequested = new Integer(1);
            this.proctorSessionSortColumn = FilterSortPageUtils.TESTSESSION_DEFAULT_SORT;
            this.proctorSessionSortOrderBy = FilterSortPageUtils.ASCENDING;
            this.proctorMaxPage = new Integer(1);      
            
            this.userSessionId = null;
            this.proctorSessionId = null;   
            
            this.actionElement = "none";   
            
            this.licenseVisible = Boolean.FALSE;
        }        
        public void resetValuesForAction() 
        {
            if (actionElement.indexOf("userSessionFilterTab") != -1) {
                this.userSessionPageRequested = new Integer(1);
                this.userSessionSortColumn = FilterSortPageUtils.TESTSESSION_DEFAULT_SORT;
                this.userSessionSortOrderBy = FilterSortPageUtils.ASCENDING;
            }
            if ((actionElement.indexOf("userSessionSortColumn") != -1) ||
                (actionElement.indexOf("userSessionSortOrderBy") != -1)) {
                this.userSessionPageRequested = new Integer(1);
            }            
            if (actionElement.indexOf("proctorSessionFilterTab") != -1) {
                this.proctorSessionPageRequested = new Integer(1);
                this.proctorSessionSortColumn = FilterSortPageUtils.TESTSESSION_DEFAULT_SORT;
                this.proctorSessionSortOrderBy = FilterSortPageUtils.ASCENDING;
            }
            if ((actionElement.indexOf("proctorSessionSortColumn") != -1) ||
                (actionElement.indexOf("proctorSessionSortOrderBy") != -1)) {
                this.proctorSessionPageRequested = new Integer(1);
            }            
        }
        public void validateValues() 
        {
            if (this.userSessionFilterTab == null)
                this.userSessionFilterTab = FilterSortPageUtils.TESTSESSION_DEFAULT_FILTER_VALUE;
            
            if (this.userSessionSortColumn == null)
                this.userSessionSortColumn = FilterSortPageUtils.TESTSESSION_DEFAULT_SORT;
            if (this.userSessionSortOrderBy == null)
                this.userSessionSortOrderBy = FilterSortPageUtils.ASCENDING;
            
            if ((this.userMaxPage == null) || (this.userMaxPage.intValue() <= 0))
                this.userMaxPage = new Integer(1);
            
            if (this.userSessionPageRequested == null)
                this.userSessionPageRequested = new Integer(1);
            if (this.userSessionPageRequested.intValue() <= 0)
                this.userSessionPageRequested = new Integer(1);
            if (this.userSessionPageRequested.intValue() > this.userMaxPage.intValue())
                this.userSessionPageRequested = new Integer(this.userMaxPage.intValue());

            if (this.proctorSessionFilterTab == null)
                this.proctorSessionFilterTab = FilterSortPageUtils.TESTSESSION_DEFAULT_FILTER_VALUE;

            if (this.proctorSessionSortColumn == null)
                this.proctorSessionSortColumn = FilterSortPageUtils.TESTSESSION_DEFAULT_SORT;
            if (this.proctorSessionSortOrderBy == null)
                this.proctorSessionSortOrderBy = FilterSortPageUtils.ASCENDING;

            if ((this.proctorMaxPage == null) || (this.proctorMaxPage.intValue() <= 0))
                this.proctorMaxPage = new Integer(1);

            if (this.proctorSessionPageRequested == null)
                this.proctorSessionPageRequested = new Integer(1);
            if (this.proctorSessionPageRequested.intValue() <= 0)
                this.proctorSessionPageRequested = new Integer(1);
            if (this.proctorSessionPageRequested.intValue() > this.proctorMaxPage.intValue())
                this.proctorSessionPageRequested = new Integer(this.proctorMaxPage.intValue());
        }
        
        public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
        {            
            return super.validate(mapping, request);
        }
        
        public void setActionElement(String actionElement)
        {
            this.actionElement = actionElement;
        }
        public String getActionElement()
        {
            return JavaScriptSanitizer.sanitizeInput(this.actionElement);
        }
        public void setUserSessionSortColumn(String userSessionSortColumn)
        {
            this.userSessionSortColumn = userSessionSortColumn;
        }
        public String getUserSessionSortColumn()
        {
            this.userSessionSortColumn = JavaScriptSanitizer.sanitizeInput(this.userSessionSortColumn);                        
            return this.userSessionSortColumn != null ? this.userSessionSortColumn : FilterSortPageUtils.TESTSESSION_DEFAULT_SORT;
        }        
        public void setUserSessionSortOrderBy(String userSessionSortOrderBy)
        {
            this.userSessionSortOrderBy = userSessionSortOrderBy;
        }
        public String getUserSessionSortOrderBy()
        {
            this.userSessionSortOrderBy = JavaScriptSanitizer.sanitizeInput(this.userSessionSortOrderBy);                        
            return this.userSessionSortOrderBy != null ? this.userSessionSortOrderBy : FilterSortPageUtils.ASCENDING;
        }        
        public void setProctorSessionSortColumn(String proctorSessionSortColumn)
        {
            this.proctorSessionSortColumn = proctorSessionSortColumn;
        }
        public String getProctorSessionSortColumn()
        {
            this.proctorSessionSortColumn = JavaScriptSanitizer.sanitizeInput(this.proctorSessionSortColumn);                        
            return this.proctorSessionSortColumn != null ? this.proctorSessionSortColumn : FilterSortPageUtils.TESTSESSION_DEFAULT_SORT;
        }        
        public void setProctorSessionSortOrderBy(String proctorSessionSortOrderBy)
        {
            this.proctorSessionSortOrderBy = proctorSessionSortOrderBy;
        }
        public String getProctorSessionSortOrderBy()
        {
            this.proctorSessionSortOrderBy = JavaScriptSanitizer.sanitizeInput(this.proctorSessionSortOrderBy);                        
            return this.proctorSessionSortOrderBy != null ? this.proctorSessionSortOrderBy : FilterSortPageUtils.ASCENDING;
        }        
        public void setUserSessionFilterTab(String userSessionFilterTab)
        {
            this.userSessionFilterTab = userSessionFilterTab;
        }
        public String getUserSessionFilterTab()
        {
            this.userSessionFilterTab = JavaScriptSanitizer.sanitizeInput(this.userSessionFilterTab);            
            return this.userSessionFilterTab != null ? this.userSessionFilterTab : "CU";
        }        
        public void setProctorSessionFilterTab(String proctorSessionFilterTab)
        {
            this.proctorSessionFilterTab = proctorSessionFilterTab;
        }
        public String getProctorSessionFilterTab()
        {
            this.proctorSessionFilterTab = JavaScriptSanitizer.sanitizeInput(this.proctorSessionFilterTab);            
            return this.proctorSessionFilterTab != null ? this.proctorSessionFilterTab : "CU";
        }        
        public void setUserSessionPageRequested(Integer userSessionPageRequested)
        {
            this.userSessionPageRequested = userSessionPageRequested;
        }
        public Integer getUserSessionPageRequested()
        {
            if (this.userSessionPageRequested == null)
                return new Integer(1);
            if (this.userSessionPageRequested.intValue() <= 0)
                return new Integer(1);
            return this.userSessionPageRequested;
        }
        public void setProctorSessionPageRequested(Integer proctorSessionPageRequested)
        {
            this.proctorSessionPageRequested = proctorSessionPageRequested;
        }
        public Integer getProctorSessionPageRequested()
        {
            return this.proctorSessionPageRequested != null ? this.proctorSessionPageRequested : new Integer(1);
        }
        public void setUserSessionId(String userSessionId)
        {
            this.userSessionId = userSessionId;
        }
        public String getUserSessionId()
        {
            return this.userSessionId;
        }
        public void setProctorSessionId(String proctorSessionId)
        {
            this.proctorSessionId = proctorSessionId;
        }
        public String getProctorSessionId()
        {
            return this.proctorSessionId;
        }
        public void setUserMaxPage(Integer userMaxPage)
        {
            this.userMaxPage = userMaxPage;
        }
        public Integer getUserMaxPage()
        {
            return this.userMaxPage;
        }
        public void setProctorMaxPage(Integer proctorMaxPage)
        {
            this.proctorMaxPage = proctorMaxPage;
        }
        public Integer getProctorMaxPage()
        {
            return this.proctorMaxPage;
        }
        public void reset(org.apache.struts.action.ActionMapping mapping,
                          javax.servlet.http.HttpServletRequest request)
        {
        }
        public void setLicenseVisible(Boolean licenseVisible)
        {
            this.licenseVisible = licenseVisible;
        }
        public Boolean getLicenseVisible()
        {
            return this.licenseVisible;
        }
    }

}
