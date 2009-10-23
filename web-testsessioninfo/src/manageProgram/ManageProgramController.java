package manageProgram;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.OrganizationNode;
import com.ctb.bean.testAdmin.Program;
import com.ctb.bean.testAdmin.ProgramData;
import com.ctb.bean.testAdmin.ProgramStatusSession;
import com.ctb.bean.testAdmin.ProgramStatusSessionData;
import com.ctb.bean.testAdmin.SessionNode;
import com.ctb.bean.testAdmin.SessionNodeData;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestElementData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.TestStatus;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.forms.FormFieldValidator;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.PagerSummary;
import com.ctb.testSessionInfo.dto.PathNode;
import com.ctb.testSessionInfo.dto.ProgramStatusReportVO;
import com.ctb.testSessionInfo.dto.SubtestStatus;
import com.ctb.testSessionInfo.dto.TestStatusFilter;
import com.ctb.testSessionInfo.dto.TestSessionVO;
import com.ctb.testSessionInfo.dto.TestStatusVO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import com.ctb.testSessionInfo.utils.FilterSortPageUtils;
import com.ctb.testSessionInfo.utils.PathListUtils;
import com.ctb.testSessionInfo.utils.ProgramStatusReportUtils;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class ManageProgramController extends PageFlowController
{
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.ProgramStatus programStatus;

    
    static final long serialVersionUID = 1L;
    
    private String userName = null;
    private String customerName = null;
    private Integer customerId = null;
    
    private List orgNodePath = null;

    public LinkedHashMap testList = null;
    private List subtestList = null;
    private List statusList = null;
    private Integer numRecords = null;
    
    public LinkedHashMap programList = null;
    
    public String programName = null;
    public String testName = null;
    
    private Boolean isBottomTwoLevels = Boolean.FALSE;
    private TestStatusFilter testStatusFilter = null;
    
    private Integer[] bottomTwoLevels = null;
    
    private static final String SHOW_STATUS = "showStatus";
    private static final String STATUS_PAGE_REQUESTED = "{actionForm.statusPageRequested}";
    private static final String STATUS_SORT_REQUESTED = "{actionForm.statusSortOrderBy}";
    private static final String STATUS_PAGE_GO_BUTTON = "ButtonGoInvoked_unknown";
    private static final String STATUS_ENTER_KEY = "EnterKeyInvoked_unknown";
    private static final String APPLY_FILTERS = "applyFilters";
    private static final String CLEAR_FILTERS = "clearFilters";
    private static final String CHANGE_FILTER = "changeFilter";
    private static final String ACTION_FORM_CURRENT_ACTION = "{actionForm.currentAction}";
    private static final String ON_TEST_CHANGE = "onTestChange";
    private static final String ON_PROGRAM_CHANGE = "onProgramChange";
    private static final String EXPORT_TO_EXCEL = "exportToExcel";
    private static final int MAX_EXPORT = 65000;
    private static final String[] STATUS_SORT = {"SessionName", "SessionNumber", "LoginId", "Password", "AccessCode"};
    
    private static final String ACTION_DEFAULT = "defaultAction";

    public String[] nameOptions = {FilterSortPageUtils.FILTERTYPE_CONTAINS, FilterSortPageUtils.FILTERTYPE_BEGINSWITH, FilterSortPageUtils.FILTERTYPE_EQUALS};

    /**
     * @jpf:action
     * @jpf:forward name="success" path="beginManageProgram.do"
     * @jpf:validation-error-forward name="failure" path="logout.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "beginManageProgram.do")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "logout.do"))
    protected Forward begin()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="manageProgram.do"
     * @jpf:validation-error-forward name="failure" path="logout.do"
    */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "manageProgram.do")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "logout.do"))
    protected Forward beginManageProgram() throws CTBBusinessException
    {     
        ManageProgramForm form = initialize();
        this.orgNodePath = new ArrayList();
        this.getUserDetails();
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="manage_program.jsp"
     * @jpf:validation-error-forward name="failure" path="logout.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "manage_program.jsp")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "logout.do"))
    protected Forward manageProgram(ManageProgramForm form) throws CTBBusinessException
    {   
        form.resetValuesForAction();        
        form.validateValues();
        
        FormFieldValidator.validateFilterForm(form, getRequest());
        
        this.buildProgramList(form);
        
        if (this.programList.size() > 0)
        {
            this.navigateOrganizations(form);
            
            this.buildTestList(form);
            
            if (this.testList.size() > 0)
            {
                this.buildSubtestList(form);
                
                if (showSubtestStatus(form))
                {
                    this.buildSubtestStatus(form);
                }
            }
        }
        form.setActionElement(ACTION_DEFAULT);
        form.setCurrentAction(ACTION_DEFAULT);

        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="manageProgram.do"
     * @jpf:validation-error-forward name="failure" path="logout.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "manageProgram.do")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "logout.do"))
    protected Forward viewSubtestStatus(ManageProgramForm form) throws CTBBusinessException
    {   
        form.resetStatusSortPage();
        form.resetStatusFilter();
        form.setSubtestId(new Integer(form.getActionElement()));
        form.setStatus(form.getCurrentAction());
        form.setCurrentAction(SHOW_STATUS);
        form.setActionElement(ACTION_DEFAULT);
        
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:validation-error-forward name="failure" path="logout.do"
     */
    @Jpf.Action(validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "logout.do"))
    protected Forward goto_user_view_session_information(ManageProgramForm form)
    {
        try
        {
            String testAdminId = this.getRequest().getParameter("testAdminId");
            String url = "/TestAdministrationWeb/scheduleTestPageflow/selectSettings.do?action=view&testAdminId=" + testAdminId;
            getSession().setAttribute("sessionId", testAdminId);
            getResponse().sendRedirect(url);
        } 
        catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    } 
  
    /**
     * @jpf:action
     * @jpf:validation-error-forward name="failure" path="logout.do"
     */
	@Jpf.Action(
		validationErrorForward = @Jpf.Forward(name = "failure", path = "logout.do")
	)
    protected Forward exportToExcel(ManageProgramForm form) throws CTBBusinessException
    {
        HttpServletResponse resp = this.getResponse();        
        String bodypart = "attachment; filename=\"ProgramStatus.xls\" ";
        
        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", bodypart);      
        resp.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");        
        resp.setHeader("Cache-Control", "cache");
        resp.setHeader("Pragma", "public");      
          
        Integer subtestId = form.getSubtestId();
        String status = form.getStatus();
        String subtestName = getSubtestName(subtestId);
        List exportList = new ArrayList();
        Integer programId = form.getSelectedProgramId();
        Integer orgNodeId = form.getSelectedOrgNodeId();
        Integer testId = form.getSelectedTestId();
        FilterParams filter = FilterSortPageUtils.buildTestStatusFilterParams(form.getTestStatusFilter());
        PageParams page = FilterSortPageUtils.buildPageParams(new Integer(1), numRecords.intValue());
        SortParams sort = getStatusSortParams(form);
        ProgramStatusSessionData data = 
          this.programStatus.getProgramStatusSessions(this.userName, 
                                                      programId, 
                                                      orgNodeId, 
                                                      testId, 
                                                      subtestId,
                                                      status, 
                                                      filter, 
                                                      page, 
                                                      sort);
        if(data.getFilteredCount().intValue() > 0){
            ProgramStatusSession[] statuses = data.getProgramStatusSessions();
            for(int i=0; i<statuses.length; i++){
                if(statuses[i] != null){
                    exportList.add(new TestStatusVO(statuses[i]));
                }
            }
        }
        
        try {
            resp.flushBuffer();
            ProgramStatusReportVO vo = new ProgramStatusReportVO();
            vo.setCustomer(this.customerName);
            vo.setProgram(form.getSelectedProgramName());
            vo.setOrganization(form.getSelectedOrgNodeName());
            vo.setTest(form.getSelectedTestName());
            vo.setSubtest(subtestName);
            vo.setSubtestStatus(form.getStatus());
            vo.setSessionNameFilter(form.testStatusFilter.getSessionNameFilterType());
            vo.setSessionNameValue(form.testStatusFilter.getSessionName());
            vo.setSessionNumberFilter(form.testStatusFilter.getSessionNumberFilterType());
            vo.setSessionNumberValue(form.testStatusFilter.getSessionNumber());
            vo.setLoginIdFilter(form.testStatusFilter.getLoginIdFilterType());
            vo.setLoginIdValue(form.testStatusFilter.getLoginId());
            vo.setPasswordFilter(form.testStatusFilter.getPasswordFilterType());
            vo.setPasswordValue(form.testStatusFilter.getPassword());
            vo.setAccessCodeFilter(form.testStatusFilter.getAccessCodeFilterType());
            vo.setAccessCodeValue(form.testStatusFilter.getAccessCode());
            vo.setSubtestStatusList(exportList);
            ProgramStatusReportUtils util = new ProgramStatusReportUtils(vo, resp.getOutputStream());
            util.generateReport();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        return null;
    }

    private SortParams getStatusSortParams(ManageProgramForm form){
        String primarySort = form.getStatusSortColumn();
        String sortOrderBy = form.getStatusSortOrderBy();
        String[] sorts = new String[STATUS_SORT.length];
        String[] orders = new String[STATUS_SORT.length];
        int index = 0;
        sorts[index] = primarySort;
        orders[index++] = sortOrderBy;
        for(int i=0; i<STATUS_SORT.length; i++){
            String sort = STATUS_SORT[i];
            if(!sort.equals(primarySort)){
                sorts[index] = sort;
                orders[index++] = sortOrderBy;
            }
        }
        return FilterSortPageUtils.buildSortParams(sorts, orders);
    }
    
    private void navigateOrganizations(ManageProgramForm form) throws CTBBusinessException
    {
        String currentAction = form.getCurrentAction();        
        String actionElement = form.getActionElement();
        
        String orgNodeName = form.getOrgNodeName();
        Integer orgNodeId = form.getOrgNodeId();   
        
        boolean nodeChanged = PathListUtils.adjustOrgNodePath(this.orgNodePath, orgNodeId, orgNodeName);

        if (nodeChanged) {
            form.resetValuesForPathList();
        }
        
        FilterParams filter = null;
        PageParams page = FilterSortPageUtils.buildPageParams(form.getOrgPageRequested(), FilterSortPageUtils.PAGESIZE_5);
        SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgSortColumn(), form.getOrgSortOrderBy());
                     
        SessionNodeData snd = getChildrenSessionOrgNodes(orgNodeId, filter, page, sort);
        if (form.getOrgPageRequested().intValue() > snd.getFilteredPages().intValue()) {
            form.setOrgPageRequested(snd.getFilteredPages());
        }
        List orgNodes = buildSessionNodeList(snd);
        String orgCategoryName = getOrgCategoryName(orgNodes);
        PagerSummary orgPagerSummary = buildSessionOrgNodePagerSummary(snd, form.getOrgPageRequested());        
        
        PathNode node = null;
        if ((form.getSelectedOrgNodeId() != null) && (form.getSelectedOrgNodeId().intValue() > 0)) {
            node = PathListUtils.findOrgNode(orgNodes, form.getSelectedOrgNodeId());
        }        
        if (node == null) {
            node = (PathNode)orgNodes.get(0);
        }        
        
        this.setIsBottomTwoLevels(node);
        
        form.setSelectedOrgNodeId(node.getId());
        form.setSelectedOrgNodeName(node.getName());
        
        this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
        this.getRequest().setAttribute("orgNodes", orgNodes);        
        this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
        this.getRequest().setAttribute("orgCategoryName", orgCategoryName);        
    }

    private void buildProgramList(ManageProgramForm form) throws CTBBusinessException{
        this.programList = new LinkedHashMap();
        SortParams sort = FilterSortPageUtils.buildSortParams(FilterSortPageUtils.PROGRAM_DEFAULT_SORT, FilterSortPageUtils.ASCENDING);
        ProgramData pd = this.programStatus.getActiveProgramsForUser(this.userName, null, null, sort);
        Program[] programs = pd.getPrograms();
        for(int i=0; i<programs.length; i++){
            this.programList.put(programs[i].getProgramId(), programs[i].getProgramName());
        }
        
        if(this.programList.size() > 0){
            // select first program if none selected
            Integer selectedProgramId = form.getSelectedProgramId();
            if(selectedProgramId == null){
                selectedProgramId = (Integer)this.programList.keySet().iterator().next();
                form.setSelectedProgramId(selectedProgramId);
            }
            form.setSelectedProgramName((String)this.programList.get(selectedProgramId));
            this.getRequest().setAttribute("multiplePrograms", new Boolean(this.programList.size() > 1));
            this.programName = (String)this.programList.get(selectedProgramId);
        }
        this.getRequest().setAttribute("noPrograms", new Boolean(this.programList.size() == 0));
    }
    
    private void buildTestList(ManageProgramForm form) throws CTBBusinessException{
        if(form.getActionElement().equals(ACTION_FORM_CURRENT_ACTION)){
            if(form.getCurrentAction().equals(ON_PROGRAM_CHANGE)){
                form.setSelectedProgramName((String)programList.get(form.getSelectedProgramId()));
            }
        }
        
        this.testList = new LinkedHashMap();
        
        SortParams sort = FilterSortPageUtils.buildSortParams(FilterSortPageUtils.TEST_DEFAULT_SORT, FilterSortPageUtils.ASCENDING);
        TestElementData tests = this.programStatus.getTestsForProgram(this.userName, form.getSelectedProgramId(), null, null, sort);
        TestElement[] testElements = tests.getTestElements();
        for(int i=0; i<testElements.length; i++){
            this.testList.put(testElements[i].getItemSetId(), testElements[i].getItemSetName());
        }
        
        if(this.testList.size() > 0){
            Integer selectedTestId = form.getSelectedTestId();
            // select first test if no test is selected or selected test is not there
            if(selectedTestId == null || (String)this.testList.get(selectedTestId) == null){
                selectedTestId = (Integer)this.testList.keySet().iterator().next();
                form.setSelectedTestId(selectedTestId);
            }
            form.setSelectedTestName((String)this.testList.get(selectedTestId));
            this.testName =(String)this.testList.get(selectedTestId);
        }
        this.getRequest().setAttribute("noTest", new Boolean(this.testList.size() == 0));
        this.getRequest().setAttribute("singleTest", new Boolean(this.testList.size() == 1));
        this.getRequest().setAttribute("multipleTests", new Boolean(this.testList.size() > 1));
   }
    
    private void buildSubtestList(ManageProgramForm form) throws CTBBusinessException{
        if(form.getActionElement().equals(ACTION_FORM_CURRENT_ACTION)){
            if(form.getCurrentAction().equals(ON_TEST_CHANGE)){
                form.setSelectedTestName((String)testList.get(form.getSelectedTestId()));
            }
        }
        TestStatus[] subtestStatuses = this.programStatus.getSubtestStatusForProgram(this.userName, form.getSelectedProgramId(), form.getSelectedOrgNodeId(), form.getSelectedTestId());
        TestStatus testStatusBean = this.programStatus.getTestStatusForProgram(this.userName, form.getSelectedProgramId(), form.getSelectedOrgNodeId(), form.getSelectedTestId());
        SubtestStatus testStatus = new SubtestStatus(testStatusBean, Boolean.FALSE);
        this.subtestList = new ArrayList();
        SubtestStatus ss = null;
        Boolean hasClickableSubtest = Boolean.FALSE;
        for(int i=0; i<subtestStatuses.length; i++){
            ss = new SubtestStatus(subtestStatuses[i], this.isBottomTwoLevels);
            if(ss.getHasAttemptedLink().booleanValue() ||
               ss.getHasCompletedLink().booleanValue() ||
               ss.getHasScheduledLink().booleanValue()){
                hasClickableSubtest = Boolean.TRUE;
            }
            this.subtestList.add(ss);
        }
        String title = this.getWebResource("manageProgram.testStatus.title") + " " + form.getSelectedOrgNodeName();
        this.getRequest().setAttribute("testStatusTitle", title);
        this.getRequest().setAttribute("subtestList", this.subtestList);
        this.getRequest().setAttribute("testStatus", testStatus);
        this.getRequest().setAttribute("hasClickableSubtest", hasClickableSubtest);
    }
    
    private void buildSubtestStatus(ManageProgramForm form) throws CTBBusinessException{
        FormFieldValidator.validateFilterForm(form, getRequest());
        Integer subtestId = form.getSubtestId();
        String status = form.getStatus();
        String subtestName = getSubtestName(subtestId);
        
        this.getRequest().setAttribute("viewSubtestStatus", subtestName);
        
        String title = status + " ";
        title += this.getWebResource("manageProgram.studentsFor.title") + ": ";
        title += subtestName;
        
        this.getRequest().setAttribute("sessionsForTitle", title);
        
        // build status list
        this.statusList = new ArrayList();
        Integer programId = form.getSelectedProgramId();
        Integer orgNodeId = form.getSelectedOrgNodeId();
        Integer testId = form.getSelectedTestId();
        FilterParams filter = FilterSortPageUtils.buildTestStatusFilterParams(form.getTestStatusFilter());
        PageParams page = FilterSortPageUtils.buildPageParams(form.getStatusPageRequested(), FilterSortPageUtils.PAGESIZE_10);
        SortParams sort = getStatusSortParams(form);
        ProgramStatusSessionData data = 
          this.programStatus.getProgramStatusSessions(this.userName, 
                                                      programId, 
                                                      orgNodeId, 
                                                      testId, 
                                                      subtestId,
                                                      status, 
                                                      filter, 
                                                      page, 
                                                      sort);
        if(data.getFilteredCount().intValue() > 0){
            ProgramStatusSession[] statuses = data.getProgramStatusSessions();
            for(int i=0; i<statuses.length; i++){
                if(statuses[i] != null){
                    this.statusList.add(new TestStatusVO(statuses[i]));
                }
            }
        }
        this.numRecords = data.getTotalCount();
        form.setStatusMaxPage(data.getTotalPages());
        this.getRequest().setAttribute("statusList", this.statusList);
        PagerSummary statusPagerSummary = buildStatusPagerSummary(data, form.getStatusPageRequested());
        this.getRequest().setAttribute("statusPagerSummary", statusPagerSummary);
        this.getRequest().setAttribute("allowExport", new Boolean(statusList.size() < MAX_EXPORT));        
    }
    
    private boolean showSubtestStatus(ManageProgramForm form){
        String currentAction = form.getCurrentAction();
        String actionElement = form.getActionElement();
        
         return currentAction.equals(SHOW_STATUS) ||
                currentAction.equals(APPLY_FILTERS) ||
                currentAction.equals(CLEAR_FILTERS) ||
                currentAction.equals(EXPORT_TO_EXCEL) ||
                currentAction.equals(CHANGE_FILTER) ||
                actionElement.equals(STATUS_PAGE_REQUESTED) ||
                actionElement.equals(STATUS_PAGE_GO_BUTTON) ||
                actionElement.equals(STATUS_SORT_REQUESTED) ||
                actionElement.equals(STATUS_ENTER_KEY);
    }
    
    private void applyFilters(ManageProgramForm form) 
    {
        TestStatusFilter tsf = form.getTestStatusFilter();
        this.testStatusFilter.copyValues(tsf);
        form.resetStatusSortPage();
    }

    private void clearFilters(ManageProgramForm form) 
    {
        TestStatusFilter tsf = new TestStatusFilter();
        form.setTestStatusFilter(tsf);
        
        getRequest().setAttribute("formIsClean", null);        
    }

    private PagerSummary buildStatusPagerSummary(ProgramStatusSessionData data, Integer pageRequested)
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);
        pagerSummary.setTotalObjects(data.getTotalCount());
        pagerSummary.setTotalPages(data.getFilteredPages());
        pagerSummary.setTotalFilteredObjects(data.getFilteredCount());      
        
        return pagerSummary;
    }
    
    private void setIsBottomTwoLevels(PathNode node){
        Integer categoryId = node.getCategoryId();
        if(categoryId.equals(this.bottomTwoLevels[0]) ||
           categoryId.equals(this.bottomTwoLevels[1])){
            this.isBottomTwoLevels = Boolean.TRUE;
        }
        else{
            this.isBottomTwoLevels = Boolean.FALSE;
        }
    }
    
    private String getSubtestName(Integer subtestId)
    {
        for (int i=0 ; i<this.subtestList.size() ; i++) {
            SubtestStatus ss = (SubtestStatus)this.subtestList.get(i);
            if (ss.getId().equals(subtestId.toString())) {
                return ss.getName();
            }
        }
        return null;
    }
    
    private String getWebResource(String key){
        ResourceBundle rb = ResourceBundle.getBundle("webResources");
        return rb.getString(key);
    }
    
    private SessionNodeData getChildrenSessionOrgNodes(Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {    
        SessionNodeData snd = null;
        if ((orgNodeId == null) || (orgNodeId.intValue() <= 0))
            snd = getTopSessionNodesForUser(filter, page, sort);
        else
            snd = getSessionNodesForParent(orgNodeId, filter, page, sort);
        return snd;
    }

    private NodeData getChildrenOrgNodes(Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {    
        NodeData nd = null;        
        Node[] nds = nd.getNodes();
        if ((orgNodeId == null) || (orgNodeId.intValue() <= 0))
            nd = getTopNodesForUser(filter, page, sort);
        else
            nd = getOrgNodesForParent(orgNodeId, filter, page, sort);
        return nd;
    }
        
    private NodeData getTopNodesForUser(FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
        return this.testSessionStatus.getTopNodesForUser(this.userName, filter, page, sort);
    }

    private NodeData getOrgNodesForParent(Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {    
        return this.testSessionStatus.getOrgNodesForParent(this.userName, orgNodeId, filter, page, sort);
    }

    private SessionNodeData getTopSessionNodesForUser(FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
        return this.testSessionStatus.getTopSessionNodesForUser(this.userName, filter, page, sort);
    }

    private SessionNodeData getSessionNodesForParent(Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {    
        return this.testSessionStatus.getSessionNodesForParent(this.userName, orgNodeId, filter, page, sort);
    }

    private TestSessionData getTestSessionsForOrgNode(Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort)  throws CTBBusinessException
    {
       return this.testSessionStatus.getTestSessionsForOrgNode(userName, orgNodeId, filter, page, sort);
    }

    /**
     * getAncestorOrganizationNodesForOrgNode
     */    
    public OrganizationNode[] getAncestorOrganizationNodesForOrgNode(String userName, Integer orgNodeId) throws CTBBusinessException
    {    
        return this.testSessionStatus.getAncestorOrganizationNodesForOrgNode(userName, orgNodeId);
    }
    
    private List buildSessionNodeList(SessionNodeData snd) 
    {
        ArrayList nodeList = new ArrayList();
        PathNode pathNode = null;
        SessionNode[] nodes = snd.getSessionNodes();        
        for (int i=0 ; i<nodes.length ; i++) {
            SessionNode node = (SessionNode)nodes[i];
            if (node != null) {
                pathNode = new PathNode();
                pathNode.setName(node.getOrgNodeName());
                pathNode.setId(node.getOrgNodeId());   
                pathNode.setSessionCount(node.getSessionCount());
                pathNode.setChildrenNodeCount(node.getChildNodeCount());
                pathNode.setCategoryName(node.getOrgNodeCategoryName());
                pathNode.setOrgCode(node.getOrgNodeCode());
                pathNode.setCategoryId(node.getOrgNodeCategoryId());
                nodeList.add(pathNode);
            }
        }
        return nodeList;
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
    
    private String getTestSessionOrgCategoryName(List testSessionList) {
        String categoryName = "Organization";        
        if ((testSessionList != null) && (testSessionList.size() > 0)) {
            TestSessionVO vo = (TestSessionVO)testSessionList.get(0);
            if (vo != null) {
                categoryName = vo.getCreatorOrgNodeCategoryName();
                for (int i=1 ; i<testSessionList.size() ; i++) {
                    vo = (TestSessionVO)testSessionList.get(i);
                    if ((vo != null) && (vo.getCreatorOrgNodeCategoryName() != null) &&
                        (! vo.getCreatorOrgNodeCategoryName().equals(categoryName))) {
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
        for (int i=0 ; i<testsessions.length ; i++) {
            TestSession ts = testsessions[i];
            if (ts != null) {
                TestSessionVO vo = new TestSessionVO(ts);
                sessionList.add(vo);
            }
        }
        return sessionList;
    }
    
    private PagerSummary buildStatusPagerSummary(TestSessionData tsd, Integer pageRequested) 
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

     /**
     * initialize
     */
    private ManageProgramForm initialize()
    {                
        ManageProgramForm form = new ManageProgramForm();
        form.init();
        
        return form;
    }

    /**
     * getUserDetails
     */
    private void getUserDetails() throws CTBBusinessException
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
        if (principal != null) 
            this.userName = principal.toString();
        else            
            this.userName = (String)getSession().getAttribute("userName");
        
        getSession().setAttribute("userName", this.userName);
        User user = this.testSessionStatus.getUserDetails(this.userName, this.userName);
        this.customerName = user.getCustomer().getCustomerName();
        this.customerId = user.getCustomer().getCustomerId();
        this.bottomTwoLevels = this.programStatus.getBottomTwoOrgNodeCategoryIdsForCustomer(this.userName, this.customerId);
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    /////// *********************** ManageUploadForm ************* ////////////////////////////////    
    /////////////////////////////////////////////////////////////////////////////////////////////    
    
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ManageProgramForm extends SanitizedFormData
    {
        private String orgSortColumn = null;
        private String orgSortOrderBy = null;
        private Integer orgPageRequested = null;

        private String statusSortColumn = null;
        private String statusSortOrderBy = null;
        private Integer statusPageRequested = null;
        private Integer statusMaxPage = null;

        private Integer orgNodeId = null;
        private String orgNodeName = null;
        private String orgNodeCode = null;
        private Integer sessionId = null;

        private String selectedOrgNodeName = null;
        private Integer selectedOrgNodeId = null;
        private Integer selectedTestId = null;
        private String selectedTestName = null;
        private Integer selectedProgramId = null;
        private String selectedProgramName = null;
                
        private String currentAction = null;
        private String actionElement = null;
        
        private Boolean filterVisible = Boolean.FALSE;
        
        private TestStatusFilter testStatusFilter = null;
        private Integer subtestId = null;
        private String status = null;
        
        public ManageProgramForm()
        {
        }
        public void init()
        {
            this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT;
            this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.orgPageRequested = new Integer(1);    
            
            this.statusSortColumn = FilterSortPageUtils.STATUS_DEFAULT_SORT;
            this.statusSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.statusPageRequested = new Integer(1);    
            this.statusMaxPage = new Integer(1);      

            this.orgNodeId = new Integer(0);
            this.orgNodeName = "Top";
            this.orgNodeCode = "";
            this.sessionId = null;
            
            this.selectedOrgNodeName = null;            
            this.selectedOrgNodeId = null;            

            this.selectedTestName = null;            
            this.selectedTestId = null;            

            this.selectedProgramName = null;            
            this.selectedProgramId = null;            

            this.currentAction = ACTION_DEFAULT;         
            this.actionElement = ACTION_DEFAULT;       
            
            this.filterVisible = Boolean.FALSE;  
            this.testStatusFilter = new TestStatusFilter();   
        }
        
        public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
        {
            return super.validate(mapping, request);
        }
            

        public ManageProgramForm createClone()
        {
            ManageProgramForm copied = new ManageProgramForm();
            
            copied.setOrgNodeId(this.orgNodeId);
            copied.setOrgNodeName(this.orgNodeName);
            copied.setOrgNodeCode(this.orgNodeCode);
            copied.setSessionId(this.sessionId);

            copied.setSelectedOrgNodeId(this.selectedOrgNodeId);
            copied.setSelectedOrgNodeName(this.selectedOrgNodeName);

            copied.setSelectedTestId(this.selectedTestId);
            copied.setSelectedTestName(this.selectedTestName);
            
            copied.setSelectedProgramId(this.selectedProgramId);
            copied.setSelectedProgramName(this.selectedProgramName);
            
            copied.setOrgSortColumn(this.orgSortColumn);
            copied.setOrgSortOrderBy(this.orgSortOrderBy);
            copied.setOrgPageRequested(this.orgPageRequested);
            
            copied.setStatusSortColumn(this.statusSortColumn);
            copied.setStatusSortOrderBy(this.statusSortOrderBy);
            copied.setStatusPageRequested(this.statusPageRequested);
            copied.setStatusMaxPage(this.statusMaxPage);

            copied.setCurrentAction(this.currentAction);     
            copied.setActionElement(this.actionElement);     
            copied.setFilterVisible(this.filterVisible);     
            copied.setTestStatusFilter(this.testStatusFilter);
            copied.setStatus(this.status);
            copied.setSubtestId(this.subtestId);
            
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

            if (this.statusSortColumn == null)
                this.statusSortColumn = FilterSortPageUtils.STATUS_DEFAULT_SORT;
            if (this.statusSortOrderBy == null)
                this.statusSortOrderBy = FilterSortPageUtils.ASCENDING;
            if ((this.statusMaxPage == null) || (this.statusMaxPage.intValue() == 0))
                this.statusMaxPage = new Integer(1);
            
            if (this.statusPageRequested == null)
                this.statusPageRequested = new Integer(1);
            if (this.statusPageRequested.intValue() <= 0)
                this.statusPageRequested = new Integer(1);
            if (this.statusPageRequested.intValue() > this.statusMaxPage.intValue())
                this.statusPageRequested = new Integer(this.statusMaxPage.intValue());

            if (this.orgNodeId == null)
                this.orgNodeId = new Integer(1);
                
            if (this.orgNodeName == null)
                this.orgNodeName = "Top";
           
            if (this.orgNodeCode == null)
                this.orgNodeCode = "";

            if (this.currentAction == null)
                this.currentAction = ACTION_DEFAULT;
            if (this.actionElement == null)
                this.actionElement = ACTION_DEFAULT;
        }

        public void resetValuesForAction() 
        {
            if ((actionElement.indexOf("statusSortColumn") != -1) ||
                (actionElement.indexOf("statusSortOrderBy") != -1)) {
                this.statusPageRequested = new Integer(1);
            }            
            if (actionElement.equals("{actionForm.orgSortOrderBy}")) {
                this.orgPageRequested = new Integer(1);
                this.selectedOrgNodeId = null;
                this.selectedOrgNodeName = null;
            }
            if (actionElement.equals("{actionForm.orgPageRequested}")) {
                this.statusPageRequested = new Integer(1);
            }
            if(currentAction.equals(CLEAR_FILTERS)){
                this.testStatusFilter = new TestStatusFilter();
            }
            if(currentAction.equals(APPLY_FILTERS)){
                this.statusPageRequested = new Integer(1);
            }
        }
        
        public void resetValuesForPathList()
        {
            this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT;
            this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.orgPageRequested = new Integer(1);    
            
            this.statusSortColumn = FilterSortPageUtils.STATUS_DEFAULT_SORT;
            this.statusSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.statusPageRequested = new Integer(1);    
            this.statusMaxPage = new Integer(1);      

            this.currentAction = ACTION_DEFAULT;         
            this.actionElement = ACTION_DEFAULT;         
        }
        
        public void resetStatusSortPage() 
        {
            this.statusSortColumn = FilterSortPageUtils.STATUS_DEFAULT_SORT;
            this.statusSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.statusPageRequested = new Integer(1);   
            this.statusMaxPage = new Integer(1);               
        }
        
        public void resetStatusFilter(){
            this.testStatusFilter = new TestStatusFilter();
            this.filterVisible = Boolean.FALSE;
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
        public void setStatusSortColumn(String statusSortColumn)
        {
            this.statusSortColumn = statusSortColumn;
        }
        public String getStatusSortColumn()
        {
            return this.statusSortColumn != null ? this.statusSortColumn : FilterSortPageUtils.STATUS_DEFAULT_SORT;
        }       
        public void setStatusSortOrderBy(String statusSortOrderBy)
        {
            this.statusSortOrderBy = statusSortOrderBy;
        }
        public String getStatusSortOrderBy()
        {
            return this.statusSortOrderBy != null ? this.statusSortOrderBy : FilterSortPageUtils.ASCENDING;
        }       
        public void setStatusPageRequested(Integer statusPageRequested)
        {
            this.statusPageRequested = statusPageRequested;
        }
        public Integer getStatusPageRequested()
        {
            return this.statusPageRequested != null ? this.statusPageRequested : new Integer(1);
        } 
        public void setStatusMaxPage(Integer statusMaxPage)
        {
            this.statusMaxPage = statusMaxPage;
        }
        public Integer getStatusMaxPage()
        {
            return this.statusMaxPage;
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
        public void setOrgNodeCode(String orgNodeCode)
        {
            this.orgNodeCode = orgNodeCode;
        }
        public String getOrgNodeCode()
        {
            return this.orgNodeCode;
        } 
        public void setSelectedTestId(Integer selectedTestId)
        {
            this.selectedTestId = selectedTestId;
        }
        public Integer getSelectedTestId()
        {
            return this.selectedTestId;
        } 
        public void setSelectedTestName(String selectedTestName)
        {
            this.selectedTestName = selectedTestName;
        }
        public String getSelectedTestName()
        {
            return this.selectedTestName;
        } 
                                     
         public void setSelectedProgramId(Integer selectedProgramId)
        {
            this.selectedProgramId = selectedProgramId;
        }
        public Integer getSelectedProgramId()
        {
            return this.selectedProgramId;
        } 
        public void setSelectedProgramName(String selectedProgramName)
        {
            this.selectedProgramName = selectedProgramName;
        }
        public String getSelectedProgramName()
        {
            return this.selectedProgramName;
        } 
          public void reset(org.apache.struts.action.ActionMapping mapping,
                          javax.servlet.http.HttpServletRequest request)
        {
            this.testStatusFilter = new TestStatusFilter();
        }       
        
        public void setFilterVisible(Boolean filterVisible)
        {
            this.filterVisible = filterVisible;
        }
        public Boolean getFilterVisible()
        {
            return this.filterVisible != null ? this.filterVisible : Boolean.FALSE;
        } 
         
        public void setTestStatusFilter(TestStatusFilter testStatusFilter)
        {
            this.testStatusFilter = testStatusFilter;
        }
        public TestStatusFilter getTestStatusFilter()
        {
            return this.testStatusFilter;
        }    
        public void setSubtestId(Integer subtestId)
        {
            this.subtestId = subtestId;
        }
        public Integer getSubtestId()
        {
            return this.subtestId;
        }    
        public void setStatus(String status)
        {
            this.status = status;
        }
        public String getStatus()
        {
            return this.status;
        } 
    }

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer[] getBottomTwoLevels() {
		return bottomTwoLevels;
	}

	public void setBottomTwoLevels(Integer[] bottomTwoLevels) {
		this.bottomTwoLevels = bottomTwoLevels;
	}

	public Boolean getIsBottomTwoLevels() {
		return isBottomTwoLevels;
	}

	public void setIsBottomTwoLevels(Boolean isBottomTwoLevels) {
		this.isBottomTwoLevels = isBottomTwoLevels;
	}

	public String[] getNameOptions() {
		return nameOptions;
	}

	public void setNameOptions(String[] nameOptions) {
		this.nameOptions = nameOptions;
	}

	public Integer getNumRecords() {
		return numRecords;
	}

	public void setNumRecords(Integer numRecords) {
		this.numRecords = numRecords;
	}

	public List getOrgNodePath() {
		return orgNodePath;
	}

	public void setOrgNodePath(List orgNodePath) {
		this.orgNodePath = orgNodePath;
	}

	public LinkedHashMap getProgramList() {
		return programList;
	}

	public void setProgramList(LinkedHashMap programList) {
		this.programList = programList;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public LinkedHashMap getTestList() {
		return testList;
	}

	public void setTestList(LinkedHashMap testList) {
		this.testList = testList;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
    
}
