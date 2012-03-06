package viewtestsessions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.OrganizationNode;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.SessionNode;
import com.ctb.bean.testAdmin.SessionNodeData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.testSessionInfo.dto.PathNode;
import com.ctb.testSessionInfo.dto.TestSessionVO;
import com.ctb.testSessionInfo.utils.FilterSortPageUtils;
import com.ctb.testSessionInfo.utils.PathListUtils;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.PagerSummary;


/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class ViewTestSessionsController extends PageFlowController
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
    
    // LLO- 118 - Change for Ematrix UI
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;

    
        
    private String userName = null;
    private List orgNodePath = null;
     //START - form recommendation
    private Integer selectedStudentId = new Integer(0);
    private Integer selectedProductId = new Integer(0);
    private boolean  requestFromFindStudent = true;
    private Integer selectedSessionId = new Integer(0);
    private Integer selectedOrgNodeId = new Integer(0);
     //END - form recommendation
 // customer configuration Change for HandScoring: score by student
	CustomerConfiguration[] customerConfigurations = null;
	CustomerConfigurationValue[] customerConfigurationsValue = null;
	 
    // LLO- 118 - Change for Ematrix UI
	private boolean isTopLevelUser = false;
	private boolean islaslinkCustomer = false;
	private boolean hasLicenseConfig = false;
    
    
         
    public static final String ACTION_DEFAULT = "defaultAction";

    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="view_test_sessions.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "view_test_sessions.do")
    })
    protected Forward begin()
    {

        retrieveInfoFromSession();            
        this.orgNodePath = new ArrayList();
        ViewTestSessionsForm form = new ViewTestSessionsForm();
        form.init();
                
        ViewTestSessionsForm formCopied = (ViewTestSessionsForm)this.getSession().getAttribute("ViewTestSessionsFormCopied");
        if ((formCopied != null) && (formCopied.getOrgNodeId().intValue() > 0))
        {
            this.orgNodePath = restoreFormSettings(formCopied, form);               
        }
     // change for handscoring
        customerHasScoring();
        customerHasImmediateScoreReport();
        isTopLevelUser();
        this.hasLicenseConfig = hasLicenseConfiguration(customerConfigurations); 
        this.getSession().setAttribute("orgNodePath", this.orgNodePath);
        
        return new Forward("success", form);
    }
  //Change for HandScoring: score by student
    /**
     * @jpf:action
     * @jpf:forward name="success" path="view_test_sessions.jsp"
     * @jpf:forward name="sessionViewStatus" path="goto_view_session_monitor_status.do"
     * @jpf:forward name="sessionEdit" path="goto_edit_session_information.do"
     * @jpf:forward name="registerStudent" path="goto_register_student.do"
     * @jpf:forward name="generateReportFile" path="goto_generate_report_file.do"
     * @jpf:forward name="scoringByItem" path="goto_score_by_item.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "view_test_sessions.jsp"),
        @Jpf.Forward(name = "sessionViewStatus",
                     path = "goto_view_session_monitor_status.do"),                    
        @Jpf.Forward(name = "sessionEdit",
                	 path = "goto_edit_session_information.do"),                     
        @Jpf.Forward(name = "registerStudent",
                   	 path = "goto_register_student.do"),
        @Jpf.Forward(name = "generateReportFile",
                   	 path = "goto_generate_report_file.do"),
        @Jpf.Forward(name = "scoringByItem",
                     path = "goto_score_by_item.do"),
        @Jpf.Forward(name = "scoringByStudent",
                     path = "goto_score_by_student.do"),
        @Jpf.Forward(name = "viewImmediateScore",
                             action = "goto_immediate_score")
    })
    protected Forward view_test_sessions(ViewTestSessionsForm form)
    {
    	retrieveInfoFromSession();
    	String actionElement = form.getActionElement();            
        String currentAction = form.getCurrentAction();            
        
        if (currentAction.equals("sessionViewStatus") ||
            currentAction.equals("sessionEdit") ||
            currentAction.equals("registerStudent") ||
            currentAction.equals("generateReportFile") ||
            currentAction.equals("scoringByItem") || 
            currentAction.equals("scoringByStudent") || 
            currentAction.equals("viewImmediateScore") ){
            return new Forward(currentAction, form);        	
        }

        form.resetValuesForAction();        
        form.validateValues();
        
        String orgNodeName = form.getOrgNodeName();
        Integer orgNodeId = form.getOrgNodeId();   

        if (this.orgNodePath == null) {
        	this.orgNodePath = (List)this.getSession().getAttribute("orgNodePath");
        	this.userName = (String)this.getSession().getAttribute("userName");	
        }

		boolean nodeChanged = PathListUtils.adjustOrgNodePath(this.orgNodePath, orgNodeId, orgNodeName);

        if (nodeChanged)
        {
            form.resetValuesForPathList();
        }
        
        FilterParams filter = null;
        PageParams page = FilterSortPageUtils.buildPageParams(form.getOrgPageRequested(), FilterSortPageUtils.PAGESIZE_5);
        SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgSortColumn(), form.getOrgSortOrderBy());
                     
        SessionNodeData snd = getChildrenSessionOrgNodes(orgNodeId, filter, page, sort);
        if (form.getOrgPageRequested().intValue() > snd.getFilteredPages().intValue())
        {
            form.setOrgPageRequested(snd.getFilteredPages());
        }
        List orgNodes = buildSessionNodeList(snd);
        String orgCategoryName = getOrgCategoryName(orgNodes);
        
        PagerSummary orgPagerSummary = buildSessionOrgNodePagerSummary(snd, form.getOrgPageRequested());        
        
        this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
        this.getRequest().setAttribute("orgNodes", orgNodes);        
        this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
        this.getRequest().setAttribute("orgCategoryName", orgCategoryName);        

        String selectedOrgNodeName = null;
        if (nodeChanged || actionElement.equals("{actionForm.orgPageRequested}") || actionElement.equals("EnterKeyInvoked_tablePathListAnchor") || actionElement.equals("ButtonGoInvoked_tablePathListAnchor"))
        {
            if (orgNodes.size() > 0)
            {
                PathNode node = (PathNode)orgNodes.get(0);
                form.setSelectedOrgNodeId(node.getId());
                form.setSelectedOrgNodeName(node.getName());
                selectedOrgNodeName = node.getName();
            }
        }
        else
        {
            if (actionElement.equals("{actionForm.sessionFilterTab}") || actionElement.equals("{actionForm.sessionSortColumn}") || actionElement.equals("{actionForm.sessionPageRequested}") || actionElement.equals("{actionForm.sessionSortOrderBy}") || actionElement.equals("EnterKeyInvoked_tableSessionAnchor") || actionElement.equals("ButtonGoInvoked_tableSessionAnchor"))
            {
                selectedOrgNodeName = form.getSelectedOrgNodeName();
                form.setSessionId(null);
            }
            if (actionElement.equals("{actionForm.actionElement}"))
            {
                PathNode node = PathListUtils.findOrgNode(orgNodes, form.getSelectedOrgNodeId());
                if (node != null)
                {
                    form.setSelectedOrgNodeId(node.getId());
                    form.setSelectedOrgNodeName(node.getName());
                    selectedOrgNodeName = node.getName();
                }
            }
        }
        if (selectedOrgNodeName == null)
        {
            if (orgNodes.size() > 0)
            {
                PathNode node = (PathNode)orgNodes.get(0);
                form.setSelectedOrgNodeId(node.getId());
                form.setSelectedOrgNodeName(node.getName());
            }
        }
        
        
        FilterParams sessionFilter = FilterSortPageUtils.buildFilterParams(FilterSortPageUtils.TESTSESSION_DEFAULT_FILTER_COLUMN, form.getSessionFilterTab());
        PageParams sessionPage = FilterSortPageUtils.buildPageParams(form.getSessionPageRequested(), FilterSortPageUtils.PAGESIZE_5);
        SortParams sessionSort = FilterSortPageUtils.buildSortParams(form.getSessionSortColumn(), form.getSessionSortOrderBy());
        TestSessionData tsd = getTestSessionsForOrgNode(form.getSelectedOrgNodeId(), sessionFilter, sessionPage, sessionSort);
        List sessionList = buildTestSessionList(tsd); 
        String testSessionOrgCategoryName = getTestSessionOrgCategoryName(sessionList);
        
        PagerSummary sessionPagerSummary = buildSessionPagerSummary(tsd, form.getSessionPageRequested()); 
        form.setSessionMaxPage(tsd.getFilteredPages());
        
        this.getRequest().setAttribute("sessionList", sessionList);
        this.getRequest().setAttribute("sessionPagerSummary", sessionPagerSummary);
        this.getRequest().setAttribute("testSessionOrgCategoryName", testSessionOrgCategoryName);        
        
        String sessionFilterTab = form.getSessionFilterTab();
        Boolean isScoringConfigured =  (Boolean) getSession().getAttribute("isScoringConfigured");
        Boolean canRegisterStudent =  (Boolean) getSession().getAttribute("canRegisterStudent");
        Boolean isImmediateScoreReportConfigured =  (Boolean) getSession().getAttribute("isImmediateScoreReportConfigured");
        
        if( (canRegisterStudent) && !(sessionFilterTab.equalsIgnoreCase("PA")))
        {
        	this.getRequest().setAttribute("visibleRegisterStudent", Boolean.TRUE);
        } 
        else {
        	this.getRequest().setAttribute("visibleRegisterStudent", Boolean.FALSE);
        }
        
        
        if( ((Boolean)isScoringConfigured) && !(sessionFilterTab.equalsIgnoreCase("FU")))
        {
        	this.getRequest().setAttribute("visiableScoreByStudent", Boolean.TRUE);
        } 
        else {
        	this.getRequest().setAttribute("visiableScoreByStudent", Boolean.FALSE);
        }

        if( ((Boolean)isImmediateScoreReportConfigured) && !(sessionFilterTab.equalsIgnoreCase("FU")))
        {
        	this.getRequest().setAttribute("visiableImmediateScoreReport", Boolean.TRUE);
        } 
        else {
        	this.getRequest().setAttribute("visiableImmediateScoreReport", Boolean.FALSE);
        }
        
        saveFormToSession(form);
        
        this.getSession().setAttribute("hasLicenseConfig", hasLicenseConfig());
        
		// get licenses
        CustomerLicense[] customerLicenses = getCustomerLicenses();
        if ((customerLicenses != null) && (customerLicenses.length > 0))
        {
            this.getRequest().setAttribute("customerLicenses", customerLicenses);
        }
        
        //check avaliable license count for Register Student
        registerStudentEnable(customerLicenses, sessionList);
        
        form.setActionElement(ACTION_DEFAULT);   
        
        this.getSession().setAttribute("orgNodePath", this.orgNodePath);
        
        if (canRegisterStudent.booleanValue() && (sessionFilterTab.equalsIgnoreCase("CU") || sessionFilterTab.equalsIgnoreCase("PA")))
        {
            // comment out generate PDF in 9.0 release
            // this.getRequest().setAttribute("showGenerateReportFile", Boolean.TRUE);
        }
        
        return new Forward("success", form);
    }
    
     //START - form recommendation
    /**
     * @jpf:action
     * @jpf:forward name="success" path="view_test_sessions.jsp"
     * @jpf:forward name="sessionViewStatus" path="goto_view_session_monitor_status.do"
     * @jpf:forward name="sessionEdit" path="goto_edit_session_information.do"
     * @jpf:forward name="registerStudent" path="goto_register_student.do"
     * @jpf:forward name="scoringByItem" path="goto_score_by_item.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "recommended_find_test_sessions.jsp")
    })
    protected Forward goto_recommended_find_test_sessions(ViewTestSessionsForm form)
    {
    	retrieveInfoFromSession();
    	 String studentId = (String)this.getRequest().getParameter("studentId");
    	 String selectedProductId = (String)this.getRequest().getParameter("selectedProductId");
    	 String requestFromFindStudent = (String)this.getRequest().getParameter("requestFromFindStudent");
    	 
    	 if(requestFromFindStudent != null && !requestFromFindStudent.equals("")){
    		 if(requestFromFindStudent.equals("false"))
    		 	 this.requestFromFindStudent = false;
    		 else
    			 this.requestFromFindStudent = true;
    	 }
    	
         if (studentId != null && selectedProductId != null 
        		 && !studentId.equals("") && !selectedProductId.equals(""))
         {	 if(!(selectedProductId.equals("NONE"))){
        		 this.setSelectedProductId(new Integer(selectedProductId));
         		}
         	else{
         		this.setSelectedProductId(new Integer(0));  //change for defect - 66361
         	}
             this.setSelectedStudentId(new Integer(studentId));
          	form.init();
          	this.orgNodePath = new ArrayList();
          	// change for handscoring
           customerHasScoring();
           customerHasImmediateScoreReport();
           isTopLevelUser();
           this.hasLicenseConfig = hasLicenseConfiguration(customerConfigurations); 
           this.getSession().setAttribute("orgNodePath", this.orgNodePath);
         }
         if (form.orgNodeId == null)
         {
        	 
        	 form.setOrgNodeId(this.selectedOrgNodeId);
        	 form.setSessionId(this.selectedSessionId);
         }
   
    	String actionElement = form.getActionElement();            
        String currentAction = form.getCurrentAction();  
        
        if(actionElement == null )
        	form.setActionElement("defaultAction");
        if(currentAction == null )
        	form.setCurrentAction("defaultAction");
        
        actionElement = form.getActionElement();            
        currentAction = form.getCurrentAction();  
        
        form.resetValuesForAction();        
        form.validateValues();
        
        String orgNodeName = form.getOrgNodeName();
        Integer orgNodeId = form.getOrgNodeId();   

        if (this.orgNodePath == null) {
        	this.orgNodePath = (List)this.getSession().getAttribute("orgNodePath");
        	this.userName = (String)this.getSession().getAttribute("userName");	
        }

		boolean nodeChanged = PathListUtils.adjustOrgNodePath(this.orgNodePath, orgNodeId, orgNodeName);

        if (nodeChanged)
        {
            form.resetValuesForPathList();
        }
        
        FilterParams filter = null;
        PageParams page = FilterSortPageUtils.buildPageParams(form.getOrgPageRequested(), FilterSortPageUtils.PAGESIZE_5);
        SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgSortColumn(), form.getOrgSortOrderBy());
                     
        SessionNodeData snd = getRecommendedChildrenSessionOrgNodes(orgNodeId, this.selectedProductId, filter, page, sort);
        if (form.getOrgPageRequested().intValue() > snd.getFilteredPages().intValue())
        {
            form.setOrgPageRequested(snd.getFilteredPages());
        }
        List orgNodes = buildSessionNodeList(snd);
        String orgCategoryName = getOrgCategoryName(orgNodes);
        
        PagerSummary orgPagerSummary = buildSessionOrgNodePagerSummary(snd, form.getOrgPageRequested());        
        
        this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
        this.getRequest().setAttribute("orgNodes", orgNodes);        
        this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
        this.getRequest().setAttribute("orgCategoryName", orgCategoryName);        

        String selectedOrgNodeName = null;
        if (nodeChanged || actionElement.equals("{actionForm.orgPageRequested}") || actionElement.equals("EnterKeyInvoked_tablePathListAnchor") || actionElement.equals("ButtonGoInvoked_tablePathListAnchor"))
        {
            if (orgNodes.size() > 0)
            {
                PathNode node = (PathNode)orgNodes.get(0);
                form.setSelectedOrgNodeId(node.getId());
                form.setSelectedOrgNodeName(node.getName());
                selectedOrgNodeName = node.getName();
            }
        }
        else
        {
            if (actionElement.equals("{actionForm.sessionFilterTab}") || actionElement.equals("{actionForm.sessionSortColumn}") || actionElement.equals("{actionForm.sessionPageRequested}") || actionElement.equals("{actionForm.sessionSortOrderBy}") || actionElement.equals("EnterKeyInvoked_tableSessionAnchor") || actionElement.equals("ButtonGoInvoked_tableSessionAnchor"))
            {
                selectedOrgNodeName = form.getSelectedOrgNodeName();
                form.setSessionId(null);
            }
            if (actionElement.equals("{actionForm.actionElement}"))
            {
                PathNode node = PathListUtils.findOrgNode(orgNodes, form.getSelectedOrgNodeId());
                if (node != null)
                {
                    form.setSelectedOrgNodeId(node.getId());
                    form.setSelectedOrgNodeName(node.getName());
                    selectedOrgNodeName = node.getName();
                }
            }
        }
        if (selectedOrgNodeName == null)
        {
            if (orgNodes.size() > 0)
            {
                PathNode node = (PathNode)orgNodes.get(0);
                form.setSelectedOrgNodeId(node.getId());
                form.setSelectedOrgNodeName(node.getName());
            }
        }
        
        
        //FilterParams sessionFilter = FilterSortPageUtils.buildFilterParams(FilterSortPageUtils.TESTSESSION_DEFAULT_FILTER_COLUMN, form.getSessionFilterTab());
        PageParams sessionPage = FilterSortPageUtils.buildPageParams(form.getSessionPageRequested(), FilterSortPageUtils.PAGESIZE_5);
        SortParams sessionSort = FilterSortPageUtils.buildSortParams(form.getSessionSortColumn(), form.getSessionSortOrderBy());
        
        TestSessionData tsd = getRecommendedTestSessionsForOrgNode(form.getSelectedOrgNodeId(),this.selectedProductId, null, sessionPage, sessionSort);
        
        RosterElement [] stl  =	getTestRosterForStudentIdAndOrgNode(this.selectedStudentId, form.getSelectedOrgNodeId());
        
        List sessionList = buildStudentTestSessionList(tsd, stl); 
        String testSessionOrgCategoryName = getTestSessionOrgCategoryName(sessionList);
        
        PagerSummary sessionPagerSummary = buildSessionPagerSummary(tsd, form.getSessionPageRequested()); 
        form.setSessionMaxPage(tsd.getFilteredPages());
        
        this.getRequest().setAttribute("sessionList", sessionList);
        this.getRequest().setAttribute("sessionPagerSummary", sessionPagerSummary);
        this.getRequest().setAttribute("testSessionOrgCategoryName", testSessionOrgCategoryName);        
        
        String sessionFilterTab = form.getSessionFilterTab();
        Boolean isScoringConfigured =  (Boolean) getSession().getAttribute("isScoringConfigured");
        Boolean canRegisterStudent =  (Boolean) getSession().getAttribute("canRegisterStudent");
        
        Boolean isImmediateScoreReportConfigured =  (Boolean) getSession().getAttribute("isImmediateScoreReportConfigured");
        
        if( (canRegisterStudent) && !(sessionFilterTab.equalsIgnoreCase("PA")))
        {
        	this.getRequest().setAttribute("visibleRegisterStudent", Boolean.TRUE);
        } 
        else {
        	this.getRequest().setAttribute("visibleRegisterStudent", Boolean.FALSE);
        }
        
        
        if( ((Boolean)isScoringConfigured) && !(sessionFilterTab.equalsIgnoreCase("FU")))
        {
        	this.getRequest().setAttribute("visiableScoreByStudent", Boolean.TRUE);
        } 
        else {
        	this.getRequest().setAttribute("visiableScoreByStudent", Boolean.FALSE);
        }
        
        if( ((Boolean)isImmediateScoreReportConfigured) && !(sessionFilterTab.equalsIgnoreCase("FU")))
        {
        	this.getRequest().setAttribute("visiableImmediateScoreReport", Boolean.TRUE);
        } 
        else {
        	this.getRequest().setAttribute("visiableImmediateScoreReport", Boolean.FALSE);
        }
        

        saveFormToSession(form);
        
        this.getSession().setAttribute("hasLicenseConfig", hasLicenseConfig());
        
		// get licenses
        CustomerLicense[] customerLicenses = getCustomerLicenses();
        if ((customerLicenses != null) && (customerLicenses.length > 0))
        {
            this.getRequest().setAttribute("customerLicenses", customerLicenses);
        }
        
        //check avaliable license count for Register Student
        registerStudentEnable(customerLicenses, sessionList);
        
        form.setActionElement(ACTION_DEFAULT);   
        Integer selectedSessionId =  form.getSessionId();
        if (selectedSessionId == null || sessionList.size()<= 0) 
            this.getRequest().setAttribute("disableNextButton", "true");
        else
            this.getRequest().setAttribute("disableNextButton", "false");
       
        
        this.getSession().setAttribute("orgNodePath", this.orgNodePath);
        
        return new Forward("success", form);
    }
     //END - form recommendation

    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward goto_register_student(ViewTestSessionsForm form)
    {
        saveFormToSession(form);
        
        String contextPath = "/StudentRegistrationWeb/registration/begin.do";
        String sessionId = form.getSessionId().toString();
        String testAdminId = "testAdminId=" +
                             sessionId;            
        String url = contextPath + "?" + testAdminId;            
            
        try
        {
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
     * @jpf:forward name="success" path="/viewmonitorstatus/ViewMonitorStatusController.jpf"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "/viewmonitorstatus/ViewMonitorStatusController.jpf")
    })
    protected Forward goto_generate_report_file(ViewTestSessionsForm form)
    {
        String sessionId = form.getSessionId().toString();
        getSession().setAttribute("sessionId", sessionId);
        getSession().setAttribute("callerId", "generate_report_file");
        
        return new Forward("success");
    }
    
    //Change for HandScoring: score by student
    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward goto_score_by_item(ViewTestSessionsForm form)
    {
        saveFormToSession(form);
        
        String contextPath = "/HandScoringWeb/itemScoringPageFlow/beginIndivItemScoring.do";
        String sessionId = form.getSessionId().toString();
        String testAdminId = "testAdminId=" +
                             sessionId;            
        String url = contextPath + "?" + testAdminId;            
            
        try
        {
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
    // Change for HandScoring: score by student
    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward goto_score_by_student(ViewTestSessionsForm form)
    {
        saveFormToSession(form);
        
        String contextPath = "/HandScoringWeb/scorebystudent/beginIndivStudentScoring.do";
        String sessionId = form.getSessionId().toString();
        String testAdminId = "testAdminId=" +
                             sessionId;            
        String url = contextPath + "?" + testAdminId;            
            
        try
        {
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
     //START - form recommendation
    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward goto_register_student_ModifyTest(ViewTestSessionsForm form)
    {
        this.selectedSessionId = form.getSessionId();
        this.selectedOrgNodeId = form.orgNodeId; 
        String contextPath = "/StudentRegistrationWeb/registration/begin.do";
        String studentId = this.getSelectedStudentId().toString();
        String selectedStudentId = "studentId=" +
        studentId;  
        String sessionId = form.getSessionId().toString();
        String testAdminId = "testAdminId=" +
                             sessionId;            
        String requestFromFindStudent = "requestFromFindStudent=" +
        this.requestFromFindStudent;            
        String url = contextPath + "?" + testAdminId + "&" + selectedStudentId + "&" + requestFromFindStudent;       
            
        try
        {
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
    protected Forward goto_to_find_student(ViewTestSessionsForm form)
    {
		String contextPath = "";
        try {
        	if(this.requestFromFindStudent) {
        		contextPath = "/StudentManagementWeb/manageStudent/returnToFindStudent.do";
        	}
        	else {
        		contextPath = "/StudentRegistrationWeb/registration/backToRegisterStudent.do";
        	}
        	String studentId = this.getSelectedStudentId().toString();
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
	
     //END - form recommendation
	@Jpf.Action()
    protected Forward goto_immediate_score(ViewTestSessionsForm form)
    {
        saveFormToSession(form);

        String contextPath = "/ImmediateReportWeb/immediateReportByStudent/beginImmediateStudentScoreByTestAdmin.do";
        String sessionId = form.getSessionId().toString();
        String testAdminId = "testAdminId=" +  sessionId;            
        String url = contextPath + "?" + testAdminId;            
            
        try
        {
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
    private void saveFormToSession(ViewTestSessionsForm form)
    {
        /*
         do not implement this until CR approved
        ViewTestSessionsForm formCopied = form.createClone();
        this.getSession().setAttribute("ViewTestSessionsFormCopied", formCopied);
        */
    }
    
    private boolean retrieveInfoFromSession()
    {
        boolean success = true;
        this.userName = (String)getSession().getAttribute("userName");
        if (this.userName == null) 
            success = false;        
        return success;
    }
    
    private SessionNodeData getChildrenSessionOrgNodes(Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort)
    {    
        SessionNodeData snd = null;
        if ((orgNodeId == null) || (orgNodeId.intValue() <= 0))
            snd = getTopSessionNodesForUser(filter, page, sort);
        else
            snd = getSessionNodesForParent(orgNodeId, filter, page, sort);
        return snd;
    }
    
    private SessionNodeData getRecommendedChildrenSessionOrgNodes(Integer orgNodeId,Integer selectedProductId, FilterParams filter, PageParams page, SortParams sort)
    {    
        SessionNodeData snd = null;
        if ((orgNodeId == null) || (orgNodeId.intValue() <= 0)) {
        	if ((selectedProductId == null) || (selectedProductId.intValue() <= 0))	{
        		snd = getTopRecommendedSessionNodesForUser(null, filter, page, sort);
        	}
        	else  {
        		snd = getTopRecommendedSessionNodesForUser(selectedProductId, filter, page, sort);
        	}
        }
        else {
        	if ((selectedProductId == null) || (selectedProductId.intValue() <= 0))	{
        		snd = getRecommendedSessionNodesForParent(orgNodeId, null, filter, page, sort);
        	}
        	else {
        		snd = getRecommendedSessionNodesForParent(orgNodeId, selectedProductId, filter, page, sort);
        	}
        }
        return snd;
    }

    private NodeData getChildrenOrgNodes(Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort)
    {    
        NodeData nd = null;
        if ((orgNodeId == null) || (orgNodeId.intValue() <= 0))
            nd = getTopNodesForUser(filter, page, sort);
        else
            nd = getOrgNodesForParent(orgNodeId, filter, page, sort);
        return nd;
    }
        
    private NodeData getTopNodesForUser(FilterParams filter, PageParams page, SortParams sort)
    {
        NodeData nd = null;
        try
        {      
            nd = this.testSessionStatus.getTopNodesForUser(this.userName, filter, page, sort);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return nd;
    }

    private NodeData getOrgNodesForParent(Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort)
    {    
        NodeData nd = null;    
        try
        {      
            nd = this.testSessionStatus.getOrgNodesForParent(this.userName, orgNodeId, filter, page, sort);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }       
        return nd;
    }

    private SessionNodeData getTopSessionNodesForUser(FilterParams filter, PageParams page, SortParams sort)
    {
        SessionNodeData snd = null;
        try
        {      
            snd = this.testSessionStatus.getTopSessionNodesForUser(this.userName, filter, page, sort);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return snd;
    }
    
    private SessionNodeData getTopRecommendedSessionNodesForUser(Integer productId,FilterParams filter, PageParams page, SortParams sort)
    {
        SessionNodeData snd = null;
        try
        {      
            snd = this.testSessionStatus.getTopRecommendedSessionNodesForUser(this.userName, productId, filter, page, sort);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return snd;
    }

    private SessionNodeData getSessionNodesForParent(Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort)
    {    
        SessionNodeData snd = null;    
        try
        {      
            snd = this.testSessionStatus.getSessionNodesForParent(this.userName, orgNodeId, filter, page, sort);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }       
        return snd;
    }
    
    private SessionNodeData getRecommendedSessionNodesForParent(Integer orgNodeId,Integer productId, FilterParams filter, PageParams page, SortParams sort)
    {    
        SessionNodeData snd = null;    
        try
        {      
            snd = this.testSessionStatus.getRecommendedSessionNodesForParent(this.userName, orgNodeId, productId, filter, page, sort);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }       
        return snd;
    }

    private TestSessionData getTestSessionsForOrgNode(Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) 
    {
        TestSessionData tsd = new TestSessionData();                        
        try
        {      
            tsd = this.testSessionStatus.getTestSessionsForOrgNode(userName, orgNodeId, filter, page, sort);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return tsd;
    }
     //START - form recommendation
    private TestSessionData getRecommendedTestSessionsForOrgNode(Integer orgNodeId, Integer selectedProductId, FilterParams filter, PageParams page, SortParams sort) 
    {
        TestSessionData tsd = new TestSessionData();                        
        try
        {     
        	if ((selectedProductId == null) || (selectedProductId.intValue() <= 0))
        		tsd = this.testSessionStatus.getRecommendedTestSessionsForOrgNode(userName, null, orgNodeId, filter, page, sort);
        	else
        		tsd = this.testSessionStatus.getRecommendedTestSessionsForOrgNode(userName, selectedProductId, orgNodeId, filter, page, sort);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return tsd;
    }
    
    
    private RosterElement[] getTestRosterForStudentIdAndOrgNode(Integer studentId, Integer orgNodeId) 
    {
    	RosterElement [] stl = null;                        
        try
        {      
        	stl = this.testSessionStatus.getTestRosterForStudentIdAndOrgNode(studentId, orgNodeId);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return stl;
    }
     //END - form recommendation

    /**
     * getAncestorOrganizationNodesForOrgNode
     */    
    public OrganizationNode[] getAncestorOrganizationNodesForOrgNode(String userName, Integer orgNodeId)
    {    
        OrganizationNode[] orgNodes = null;    
        try
        {      
            orgNodes = this.testSessionStatus.getAncestorOrganizationNodesForOrgNode(userName, orgNodeId);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }       
        return orgNodes;
    }
    
    private List buildSessionNodeList(SessionNodeData snd) 
    {
        ArrayList nodeList = new ArrayList();
        PathNode pathNode = null;
        SessionNode[] nodes = snd.getSessionNodes();        
        for (int i=0; i < nodes.length; i++)
        {
            SessionNode node = (SessionNode)nodes[i];
            if (node != null)
            {
                pathNode = new PathNode();
                pathNode.setName(node.getOrgNodeName());
                pathNode.setId(node.getOrgNodeId());   
                pathNode.setSessionCount(node.getSessionCount());
                pathNode.setChildrenNodeCount(node.getChildNodeCount());
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
    
    private String getTestSessionOrgCategoryName(List testSessionList)
    {
        String categoryName = "Organization";        
        if ((testSessionList != null) && (testSessionList.size() > 0))
        {
            TestSessionVO vo = (TestSessionVO)testSessionList.get(0);
            if (vo != null)
            {
                categoryName = vo.getCreatorOrgNodeCategoryName();
                for (int i=1; i < testSessionList.size(); i++)
                {
                    vo = (TestSessionVO)testSessionList.get(i);
                    if ((vo != null) && (vo.getCreatorOrgNodeCategoryName() != null) && (! vo.getCreatorOrgNodeCategoryName().equals(categoryName)))
                    {
                        categoryName = "Organization";
                        break;
                    }
                }
            }
        }
        return categoryName;        
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
     //START - form recommendation
    private List buildStudentTestSessionList(TestSessionData tsd, RosterElement [] stl) 
    {
        List sessionList = new ArrayList();                       
        TestSession[] testsessions = tsd.getTestSessions(); 
        for(int j=0; j < stl.length; j++){
        	
        	if (stl[j] != null) {
        	for (int i=0; i < testsessions.length; i++)
            {
                TestSession ts = testsessions[i];
                if (ts != null)
                {
                    if(stl[j].getTestAdminId().equals(testsessions[i].getTestAdminId())){
                    	testsessions[i].setIsStudentInTestSession(true);
                    }
                    
                }
            }
        }
        }
        
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
     //END - form recommendation
    private PagerSummary buildSessionPagerSummary(TestSessionData tsd, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);              
        pagerSummary.setTotalObjects(tsd.getFilteredCount());
        pagerSummary.setTotalPages(tsd.getFilteredPages());
        pagerSummary.setTotalFilteredObjects(null);        
        return pagerSummary;
    }
    
    private PagerSummary buildSessionOrgNodePagerSummary(SessionNodeData snd, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);        
        pagerSummary.setTotalObjects(snd.getFilteredCount());
        pagerSummary.setTotalPages(snd.getFilteredPages());
        pagerSummary.setTotalFilteredObjects(null);        
        return pagerSummary;
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
     * getCustomerLicenses
     */
    private CustomerLicense[] getCustomerLicenses()
    {
        CustomerLicense[] cls = null;
        
        try
        {
            cls = this.licensing.getCustomerOrgNodeLicenseData(this.userName, null);
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
            CustomerLicense[] cls = this.licensing.getCustomerOrgNodeLicenseData(this.userName, null);            
            hasLicenseConfig = new Boolean(cls.length > 0);
        }    
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        } 
             
        return hasLicenseConfig;
    }
    
    /**
     * get value for enabling RegisterStudent button.
     */
    private void registerStudentEnable(CustomerLicense[] customerLicenses, List sessionList)
    {    
        for (int i=0; i < sessionList.size(); i++)
        {
            
            TestSessionVO testSessionVo =(TestSessionVO)sessionList.get(i);
            boolean flag = false;
            if (customerLicenses == null  || (!this.hasLicenseConfig)) {
        		testSessionVo.setIsRegisterStudentEnable("T");  
        		continue;
        	}
        	if (customerLicenses != null && customerLicenses.length<=0 && this.hasLicenseConfig) {
        		 testSessionVo.setIsRegisterStudentEnable("F");   
        		 continue;
        	}
         
            if (testSessionVo.getLicenseEnabled().equals("T"))
            {
                for (int j=0; j < customerLicenses.length; j++)
                { 
                            
                    if (customerLicenses[j].getProductId().intValue() == testSessionVo.getProductId().intValue() || customerLicenses[j].getProductId().intValue() == testSessionVo.
                            getParentProductId().intValue())
                    {
                        
                        flag = true;      
                        
                        /*if (customerLicenses[j].getAvailable().intValue() > 0)
                        { 
                            
                            testSessionVo.setIsRegisterStudentEnable("T");
                          
                        }
                        else if (customerLicenses[j].getAvailable().intValue() <= 0)
                        {
                            
                            testSessionVo.setIsRegisterStudentEnable("F");
                              
                        }*/
                        // For MQC 66803 :Rapid registration
                        if(customerLicenses[j].isLicenseAvailable()){
                        	testSessionVo.setIsRegisterStudentEnable("T");
                        } else {
                        	 testSessionVo.setIsRegisterStudentEnable("F");
                        }
                        
                        break;
                    }
                }
            } 
            if (!flag)
            {
                
                testSessionVo.setIsRegisterStudentEnable("T");   
                
            }
                
        }                
   
    }
        
    /**
     * @jpf:action
     * @jpf:forward name="success" path="/viewmonitorstatus/ViewMonitorStatusController.jpf"
     */
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success",
                         path = "/viewmonitorstatus/ViewMonitorStatusController.jpf")
        })
    protected Forward goto_view_session_monitor_status(ViewTestSessionsForm form)
    {
        saveFormToSession(form);

        String sessionId = form.getSessionId().toString();
        getSession().setAttribute("sessionId", sessionId);
        getSession().setAttribute("callerId", "goto_viewtestsessions");
        
        return new Forward("success");
    }

    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward goto_view_session_information(ViewTestSessionsForm form)
    {
        saveFormToSession(form);

        String sessionId = this.getRequest().getParameter("testAdminId");
        getSession().setAttribute("sessionId", sessionId);
        String testAdminId = "testAdminId=" +
                             sessionId;
        String contextPath = "/TestAdministrationWeb/scheduleTestPageflow/selectSettings.do";
        String action = "action=view";
        String url = contextPath + "?" + action + "&" + testAdminId;            
            
        try {
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
    protected Forward goto_edit_session_information(ViewTestSessionsForm form)
    {
        saveFormToSession(form);
        
        String sessionId = form.getSessionId().toString();
        String contextPath = "/TestAdministrationWeb/scheduleTestPageflow/selectSettings.do";
        String action = "action=edit";
        String testAdminId = "testAdminId=" + sessionId;
        String url = contextPath + "?" + action + "&" + testAdminId;            
        
        try {
            getResponse().sendRedirect(url);
        } 
        catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }

	/**
     * restoreFormSettings
     */
    private ArrayList restoreFormSettings(ViewTestSessionsForm formCopied, ViewTestSessionsForm form)
    {
    	ArrayList orgNodePath = new ArrayList();
        
    	ArrayList<PathNode> nodeAncestors = new ArrayList<PathNode>();
        
        Integer orgNodeId = formCopied.getOrgNodeId();
        OrganizationNode[] orgNodes = getAncestorOrganizationNodesForOrgNode(this.userName, orgNodeId);
            
        for (int i=0 ; i<orgNodes.length ; i++) {
            OrganizationNode orgNode = (OrganizationNode)orgNodes[i];
            Integer orgId = orgNode.getOrgNodeId();
            String orgName = orgNode.getOrgNodeName();                
            if (orgId.intValue() >= 2) {    // ignore Root
                PathNode node = new PathNode();
                node.setId(orgId);
                node.setName(orgName);
                nodeAncestors.add(node);                
                form.setOrgNodeId(orgId);
                form.setOrgNodeName(orgName);
            }
        }    

        form.resetValuesForPathList();
        
        form.setSessionId(formCopied.getSessionId());
        
        form.setSelectedOrgNodeId(formCopied.getSelectedOrgNodeId());        
        form.setSelectedOrgNodeName(formCopied.getSelectedOrgNodeName());
            
        form.setOrgSortColumn(formCopied.getOrgSortColumn());
        form.setOrgSortOrderBy(formCopied.getOrgSortOrderBy());
        form.setOrgPageRequested(formCopied.getOrgPageRequested());
            
        form.setSessionFilterTab(formCopied.getSessionFilterTab());
        form.setSessionSortColumn(formCopied.getSessionSortColumn());
        form.setSessionSortOrderBy(formCopied.getSessionSortOrderBy());
        form.setSessionPageRequested(formCopied.getSessionPageRequested());
        form.setSessionMaxPage(formCopied.getSessionMaxPage());
            
        
        form.setOrgPageRequested(formCopied.getOrgPageRequested());
        form.setOrgSortColumn(formCopied.getOrgSortColumn());
        form.setOrgSortOrderBy(formCopied.getOrgSortOrderBy());

        form.setActionElement("{actionForm.actionElement}");
        
        orgNodePath = PathListUtils.setupOrgNodePath(nodeAncestors);       
        
        return orgNodePath;
    }
    
    // Change for HandScoring: score by student
     
    private Boolean customerHasScoring() {
    	getCustomerConfigurations();
		boolean hasScoringConfigurable = false;
		boolean isLaslinkCustomer = false;
		for (CustomerConfiguration cc : customerConfigurations) {
			if (cc.getCustomerConfigurationName().equalsIgnoreCase(
					"Configurable_Hand_Scoring")
					&& cc.getDefaultValue().equals("T")) {
				hasScoringConfigurable = true;
				//break;
			}
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")
    				&& cc.getDefaultValue().equals("T")) {
    			isLaslinkCustomer = true;
    			//break;
            }
        }
        this.setIslaslinkCustomer(isLaslinkCustomer);
        getSession().setAttribute("isScoringConfigured", hasScoringConfigurable);
        return new Boolean(hasScoringConfigurable);
    }
	
    private Boolean hasLicenseConfiguration(CustomerConfiguration [] customerConfigs)
    {               
    	 boolean hasLicenseConfiguration = false;

        for (int i=0; i < customerConfigs.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subscription") && 
            		cc.getDefaultValue().equals("T")	) {
            	hasLicenseConfiguration = true;
                break;
            } 
        }
       
        return new Boolean(hasLicenseConfiguration);
    }	

    private Boolean customerHasImmediateScoreReport() {
    	//getCustomerConfigurations();
		boolean isImmediateScoreReportConfigured = false;
		//boolean isLaslinkCustomer = false;
		for (CustomerConfiguration cc : customerConfigurations) {
			if (cc.getCustomerConfigurationName().equalsIgnoreCase(
					"Immediate_Score_Report")
					&& cc.getDefaultValue().equals("T")) {
				isImmediateScoreReportConfigured = true;
				break;
			}
			
        }
        getSession().setAttribute("isImmediateScoreReportConfigured", isImmediateScoreReportConfigured);
        return new Boolean(isImmediateScoreReportConfigured);
    }
    
    /**
     * Changes For cr hand scoring score by student
	 * getCustomerConfigurations
	 */
	private void getCustomerConfigurations() {
		try {
			User user = this.testSessionStatus.getUserDetails(this.userName, this.userName);
			Customer customer = user.getCustomer();
			Integer customerId = customer.getCustomerId();
			this.customerConfigurations = this.testSessionStatus.getCustomerConfigurations(this.userName, customerId);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}
	}
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ViewTestSessionsForm extends SanitizedFormData
    {
		private static final long serialVersionUID = 1L;
		
		private String orgSortColumn = null;
        private String orgSortOrderBy = null;
        private Integer orgPageRequested = null;

        private String sessionFilterTab = null;
        private String sessionSortColumn = null;
        private String sessionSortOrderBy = null;
        private Integer sessionPageRequested = null;
        private Integer sessionMaxPage = null;

        private Integer orgNodeId = null;
        private String orgNodeName = null;
        private Integer sessionId = null;

        private String selectedOrgNodeName = null;
        private Integer selectedOrgNodeId = null;
        
        private String actionElement = null;
        private String currentAction = null;
        
		//License widget in find TestSession Page
        private Boolean licenseVisible = Boolean.TRUE;
        
        
        public ViewTestSessionsForm()
        {
        }
        public void init()
        {
            this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT;
            this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.orgPageRequested = new Integer(1);    
            
            this.sessionSortColumn = FilterSortPageUtils.TESTSESSION_DEFAULT_SORT;
            this.sessionSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.sessionPageRequested = new Integer(1);    
            this.sessionMaxPage = new Integer(1);      
            
            this.sessionFilterTab = FilterSortPageUtils.TESTSESSION_DEFAULT_FILTER_VALUE;                          
            this.orgNodeId = new Integer(0);
            this.orgNodeName = "Top";
            this.sessionId = null;
            
            this.selectedOrgNodeName = null;            
            this.selectedOrgNodeId = null;            

            this.actionElement = ACTION_DEFAULT;    
            this.currentAction = ACTION_DEFAULT;
            
			this.licenseVisible = Boolean.FALSE;
        }
        public ViewTestSessionsForm createClone()
        {
            ViewTestSessionsForm copied = new ViewTestSessionsForm();
            
            copied.setOrgNodeId(this.orgNodeId);
            copied.setOrgNodeName(this.orgNodeName);
            copied.setSessionId(this.sessionId);

            copied.setSelectedOrgNodeId(this.selectedOrgNodeId);
            copied.setSelectedOrgNodeName(this.selectedOrgNodeName);
            
            copied.setOrgSortColumn(this.orgSortColumn);
            copied.setOrgSortOrderBy(this.orgSortOrderBy);
            copied.setOrgPageRequested(this.orgPageRequested);
            
            copied.setSessionFilterTab(this.sessionFilterTab);
            copied.setSessionSortColumn(this.sessionSortColumn);
            copied.setSessionSortOrderBy(this.sessionSortOrderBy);
            copied.setSessionPageRequested(this.sessionPageRequested);
            copied.setSessionMaxPage(this.sessionMaxPage);
            
            copied.setActionElement(this.actionElement);     
            this.setCurrentAction(this.currentAction);
            
            return copied;       
        }
        public void validateValues() 
        {
            if (this.orgSortColumn == null)
                this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT;

            if (this.orgSortOrderBy == null)
                this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;

            if (this.orgPageRequested == null)
                this.orgPageRequested = new Integer(1);
            if (this.orgPageRequested.intValue() <= 0)
                this.orgPageRequested = new Integer(1);

            if (this.sessionSortColumn == null)
                this.sessionSortColumn = FilterSortPageUtils.TESTSESSION_DEFAULT_SORT;

            if (this.sessionSortOrderBy == null)
                this.sessionSortOrderBy = FilterSortPageUtils.ASCENDING;

            if ((this.sessionMaxPage == null) || (this.sessionMaxPage.intValue() == 0))
                this.sessionMaxPage = new Integer(1);
            
            if (this.sessionPageRequested == null)
                this.sessionPageRequested = new Integer(1);
            if (this.sessionPageRequested.intValue() <= 0)
                this.sessionPageRequested = new Integer(1);
            if (this.sessionPageRequested.intValue() > this.sessionMaxPage.intValue())
                this.sessionPageRequested = new Integer(this.sessionMaxPage.intValue());
                
            if (this.sessionFilterTab == null)
                this.sessionFilterTab = FilterSortPageUtils.TESTSESSION_DEFAULT_FILTER_VALUE;

            if (this.orgNodeId == null)
                this.orgNodeId = new Integer(1);
                
            if (this.orgNodeName == null)
                this.orgNodeName = "Top";

            if (this.actionElement == null)
                this.actionElement = ACTION_DEFAULT;
            if (this.currentAction == null)
                this.currentAction = ACTION_DEFAULT;
        }

        public void resetValuesForAction() 
        {
            if (actionElement.indexOf("sessionFilterTab") != -1) {
                this.sessionPageRequested = new Integer(1);
                this.sessionSortColumn = FilterSortPageUtils.TESTSESSION_DEFAULT_SORT;
                this.sessionSortOrderBy = FilterSortPageUtils.ASCENDING;
            }
            if ((actionElement.indexOf("sessionSortColumn") != -1) ||
                (actionElement.indexOf("sessionSortOrderBy") != -1)) {
                this.sessionPageRequested = new Integer(1);
            }            
            if (actionElement.equals("{actionForm.orgSortOrderBy}")) {
                this.orgPageRequested = new Integer(1);
            }
            if (actionElement.equals("{actionForm.orgPageRequested}")) {
                this.sessionPageRequested = new Integer(1);
            }
        }
        
        public void resetValuesForPathList()
        {
            this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT;
            this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.orgPageRequested = new Integer(1);    
            
            this.sessionSortColumn = FilterSortPageUtils.TESTSESSION_DEFAULT_SORT;
            this.sessionSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.sessionPageRequested = new Integer(1);    
            this.sessionMaxPage = new Integer(1);      
            
            this.sessionFilterTab = FilterSortPageUtils.TESTSESSION_DEFAULT_FILTER_VALUE;                          
            this.actionElement = ACTION_DEFAULT;         
            this.currentAction = ACTION_DEFAULT;         
        }
        
        public void setCurrentAction(String currentAction)
        {
            this.currentAction = currentAction;
        }
        public String getCurrentAction()
        {
            return this.currentAction;
        }        
        public void setActionElement(String actionElement)
        {
            this.actionElement = actionElement;
        }
        public String getActionElement()
        {
            return this.actionElement;
        }
        public void setSessionId(Integer sessionId)
        {
            this.sessionId = sessionId;
        }
        public Integer getSessionId()
        {
            return this.sessionId;
        }
        public void setSessionFilterTab(String sessionFilterTab)
        {
            this.sessionFilterTab = sessionFilterTab;
        }
        public String getSessionFilterTab()
        {
            return this.sessionFilterTab != null ? this.sessionFilterTab : FilterSortPageUtils.TESTSESSION_DEFAULT_FILTER_VALUE;
        }                        
        public void setSessionSortColumn(String sessionSortColumn)
        {
            this.sessionSortColumn = sessionSortColumn;
        }
        public String getSessionSortColumn()
        {
            return this.sessionSortColumn != null ? this.sessionSortColumn : FilterSortPageUtils.TESTSESSION_DEFAULT_SORT;
        }       
        public void setSessionSortOrderBy(String sessionSortOrderBy)
        {
            this.sessionSortOrderBy = sessionSortOrderBy;
        }
        public String getSessionSortOrderBy()
        {
            return this.sessionSortOrderBy != null ? this.sessionSortOrderBy : FilterSortPageUtils.ASCENDING;
        }       
        public void setSessionPageRequested(Integer sessionPageRequested)
        {
            this.sessionPageRequested = sessionPageRequested;
        }
        public Integer getSessionPageRequested()
        {
            return this.sessionPageRequested != null ? this.sessionPageRequested : new Integer(1);
        } 
        public void setSessionMaxPage(Integer sessionMaxPage)
        {
            this.sessionMaxPage = sessionMaxPage;
        }
        public Integer getSessionMaxPage()
        {
            return this.sessionMaxPage;
        }

        public void setOrgSortColumn(String orgSortColumn)
        {
            this.orgSortColumn = orgSortColumn;
        }
        public String getOrgSortColumn()
        {
            return this.orgSortColumn != null ? this.orgSortColumn : FilterSortPageUtils.ORGNODE_DEFAULT_SORT;
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
        public void setOrgNodeId(Integer orgNodeId)
        {
            this.orgNodeId = orgNodeId;
        }
        public Integer getOrgNodeId()
        {
            return this.orgNodeId != null ? this.orgNodeId : new Integer(0);
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
        public void setOrgNodeName(String orgNodeName)
        {
            this.orgNodeName = orgNodeName;
        }
        public String getOrgNodeName()
        {
            return this.orgNodeName;
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


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List getOrgNodePath() {
		return orgNodePath;
	}

	public void setOrgNodePath(List orgNodePath) {
		this.orgNodePath = orgNodePath;
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
	 * @return the selectedProductId
	 */
	public Integer getSelectedProductId() {
		return selectedProductId;
	}
	/**
	 * @param selectedProductId the selectedProductId to set
	 */
	public void setSelectedProductId(Integer selectedProductId) {
		this.selectedProductId = selectedProductId;
	}
	/**
	 * @return the requestFromFindStudent
	 */
	public boolean isRequestFromFindStudent() {
		return requestFromFindStudent;
	}
	/**
	 * @param requestFromFindStudent the requestFromFindStudent to set
	 */
	public void setRequestFromFindStudent(boolean requestFromFindStudent) {
		this.requestFromFindStudent = requestFromFindStudent;
	}
	/**
	 * @return the selectedSessionId
	 */
	public Integer getSelectedSessionId() {
		return selectedSessionId;
	}
	/**
	 * @param selectedSessionId the selectedSessionId to set
	 */
	public void setSelectedSessionId(Integer selectedSessionId) {
		this.selectedSessionId = selectedSessionId;
	}
	/**
	 * @return the selectedOrgNodeId
	 */
	public Integer getSelectedOrgNodeId() {
		return selectedOrgNodeId;
	}
	/**
	 * @param selectedOrgNodeId the selectedOrgNodeId to set
	 */
	public void setSelectedOrgNodeId(Integer selectedOrgNodeId) {
		this.selectedOrgNodeId = selectedOrgNodeId;
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
