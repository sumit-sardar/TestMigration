package selectProctorPageflow;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserData;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.widgets.bean.PagerSummary;
import com.ctb.widgets.bean.ColumnSortEntry;
import data.PathNode;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import scheduleTestPageflow.ScheduleTestController;
import utils.FilterSortPageUtils;
import utils.PathListUtils;

/**
 * @jpf:controller nested="true"
 *  */
@Jpf.Controller(nested = true)
public class SelectProctorPageflowController extends PageFlowController
{
    static final long serialVersionUID = 1L; 

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.ScheduleTest scheduleTest;

    public static final String ACTION_DEFAULT = "defaultAction";
    public static final String ACTION_UPDATE_TOTAL = "updateTotal";
    public static final String ACTION_ADD_ALL = "addAll";
    
    // change for MQC defect  55837 
    
    public static final String ACTION_SCHEDULE_TEST = "schedule";
    public static final String ACTION_VIEW_TEST = "view";
    public static final String ACTION_EDIT_TEST = "edit";
    
    public String action = ACTION_SCHEDULE_TEST;
    
    // End of  change for MQC defect  55837 
    
    private String userName;    
    private User scheduler;
    private Hashtable proctorsHashtable;
    private List orgNodePath = null;
    private Integer orgNodeId = null;    
    private Integer testAdminId = null;    
    private List selectedProctors = null;
    private List originalSelectedProctors = null;


    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="selectProctor.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "selectProctor.do")
    })
    protected Forward begin(ScheduleTestController.ScheduleTestForm form)
    {
//        PageFlowController parentPageFlow = PageFlowUtils.getNestingPageFlow(getRequest());
        init(form);
        return new Forward("success", form);
    }
    
    private void init(ScheduleTestController.ScheduleTestForm form) 
    {
        getUserPrincipalAdmin();   
        this.scheduler = (User)getSession().getAttribute("scheduler");
             
        ScheduleTestController parentPageFlow = (ScheduleTestController)PageFlowUtils.getNestingPageFlow(getRequest());
        this.testAdminId = parentPageFlow.getTestAdminId();
                     
        copySelectedProctors(form);
        this.orgNodePath = new ArrayList();
        form.setSelectedProctorIds(null); 
        form.setSelectedProctorCount(new Integer(1));
        this.proctorsHashtable = new Hashtable();
        this.action = ACTION_SCHEDULE_TEST; // change for MQC defect  55837 
    }
    

    /**
     * @jpf:action
     * @jpf:forward name="done" return-action="selectProctorPageflowDone"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "done",
                     returnAction = "selectProctorPageflowDone")
    })
    public Forward selectProctorDone(ScheduleTestController.ScheduleTestForm form)
    {
        commitSelection(form);
        form.setSelectedProctors(this.selectedProctors);
        form.resetValuesForAction("selectProctorDone");
        return new Forward("done", form);
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="done" return-action="selectProctorPageflowDone"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "done",
                     returnAction = "selectProctorPageflowDone")
    })
    protected Forward selectProctorCancel(ScheduleTestController.ScheduleTestForm form)
    {
        form.setSelectedProctors(this.originalSelectedProctors);
        form.resetValuesForAction("selectProctorDone");
        return new Forward("done", form);
    }
    

    /**
     * @jpf:action
     * @jpf:forward name="success" path="selectProctor.jsp"
     */
    @Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success", path = "selectProctor.jsp")
		}
	)
    protected Forward selectProctor(ScheduleTestController.ScheduleTestForm form)
    {
        form.validateValues();

        String actionElement = form.getActionElement();
        form.resetValuesForAction(actionElement);
        
        String currentAction = ACTION_DEFAULT;
        if (actionElement!= null && actionElement.equals("currentAction")) {
            currentAction = form.getCurrentAction();  
        } 
        
        commitSelection(form);
  
              
        String orgNodeName = form.getOrgNodeName();
        this.orgNodeId = form.getOrgNodeId();   
        
        FilterParams filter = null;
        PageParams page = FilterSortPageUtils.buildPageParams(form.getOrgStatePathList().getPageRequested(), FilterSortPageUtils.PAGESIZE_10);
        SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgStatePathList().getSortColumn(), form.getOrgStatePathList().getSortOrderBy(), null, null);
        
                     
        UserNodeData und = getChildrenOrgNodes(orgNodeId, testAdminId, filter, page, sort);
        List orgNodes = buildOrgNodeList(und);
        
        form.getOrgStatePathList().setMaxPageRequested(und.getFilteredPages());
        
        String orgCategoryName = getOrgCategoryName(orgNodes);
        
        PagerSummary orgPagerSummary = buildOrgNodePagerSummary(und, form.getOrgStatePathList().getPageRequested());        
        
        
        this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
        this.getRequest().setAttribute("orgNodes", orgNodes);        
        this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
        this.getRequest().setAttribute("orgCategoryName", orgCategoryName);        

        this.getRequest().setAttribute("nodeContainsProctors", und.getTotalCount().intValue() > 0 ? Boolean.TRUE : Boolean.FALSE);

        boolean nodeChanged = PathListUtils.adjustOrgNodePath(this.orgNodePath, this.orgNodeId, orgNodeName);
        
        if (actionElement.equals("{actionForm.orgStatePathList.pageRequested}") ||
            actionElement.equals("{actionForm.orgStatePathList.sortOrderBy}") ||
            actionElement.equals("EnterKeyInvoked_pathListAnchor") ||
            actionElement.equals("ButtonGoInvoked_pathListAnchor")) {                
            nodeChanged = true;
        }

        if (nodeChanged) {
            if (orgNodes.size() > 0) {
                PathNode node = (PathNode)orgNodes.get(0);
                form.setSelectedOrgNodeId(node.getId());
                form.setSelectedOrgNodeName(node.getName());
                this.orgNodeId = node.getId();
            }
            else {
                form.setSelectedOrgNodeId(new Integer(0));
                form.setSelectedOrgNodeName("****");
                this.orgNodeId = new Integer(0);
            }
        }
        else {
            Iterator it = orgNodes.iterator();
            boolean found = false;
            while (it.hasNext() && !found) {
                PathNode node = (PathNode) it.next();
                if (node.getId().equals(form.getSelectedOrgNodeId())) {
                    found = true;
                    form.setSelectedOrgNodeName(node.getName());
                }
            }
            
        }


        if (currentAction.equals(ACTION_ADD_ALL)) {
            addAllProctors(form.getSelectedOrgNodeId());    
        }


        page = FilterSortPageUtils.buildPageParams(form.getProctorStatePathList().getPageRequested(), FilterSortPageUtils.PAGESIZE_10);
        
        if(FilterSortPageUtils.PROCTOR_DEFAULT_SECONDARY_SORT_COLUMN.equals(form.getProctorStatePathList().getSortColumn())) {
            sort = FilterSortPageUtils.buildSortParams(form.getProctorStatePathList().getSortColumn(), form.getProctorStatePathList().getSortOrderBy(), FilterSortPageUtils.PROCTOR_DEFAULT_SORT_COLUMN, ColumnSortEntry.ASCENDING);
        } else {
            sort = FilterSortPageUtils.buildSortParams(form.getProctorStatePathList().getSortColumn(), form.getProctorStatePathList().getSortOrderBy(), FilterSortPageUtils.PROCTOR_DEFAULT_SECONDARY_SORT_COLUMN, ColumnSortEntry.ASCENDING);
        }
        
        List proctorNodes;
        if (form.getSelectedOrgNodeId()== null || form.getSelectedOrgNodeId().intValue() == 0) {
            proctorNodes = new ArrayList();
            this.proctorsHashtable = new Hashtable();
        }
        else { 
            UserData ud = this.getProctors(form.getSelectedOrgNodeId(), filter, page, sort);
            proctorNodes = this.buildProctorList(ud); 
            // START : 63093  Deferred defect fix.
            if(ud.getFilteredPages().intValue() == 0){
            	form.getProctorStatePathList().setMaxPageRequested(1);
            } else {
            	form.getProctorStatePathList().setMaxPageRequested(ud.getFilteredPages());
            }
            //END : 63093  Deferred defect fix.
            PagerSummary proctorPagerSummary = buildProctorPagerSummary(ud, form.getProctorStatePathList().getPageRequested());
            this.getRequest().setAttribute("proctorPagerSummary", proctorPagerSummary);
        }        
        this.getRequest().setAttribute("proctorNodes", proctorNodes);        
        
        // set the total selected counts
        Integer [] selectedProctorIds = form.getSelectedProctorIds();
        
        form.setSelectedProctorCount(new Integer(this.selectedProctors.size()));
        setSelectedProctorIdsToForm(form);
        
        form.setActionElement(ACTION_DEFAULT);  
        
        // change for MQC defect  55837 
        if (form.getAction().equals(ACTION_SCHEDULE_TEST)) {
             this.action = ACTION_SCHEDULE_TEST;
        } else if (form.getAction().equals(ACTION_EDIT_TEST))  {
             this.action = ACTION_EDIT_TEST;
        } else if (form.getAction().equals(ACTION_VIEW_TEST))  {
             this.action = ACTION_VIEW_TEST;
        }
        
        // End of change for MQC defect  55837 
                              
        return new Forward("success", form);
    }
   
    private UserNodeData getChildrenOrgNodes(Integer orgNodeId, Integer testAdminId, FilterParams filter, PageParams page, SortParams sort)
    {    
        UserNodeData und = null;
        try {      
            if ((orgNodeId == null) || (orgNodeId.intValue() <= 0))
                und = this.scheduleTest.getTopUserNodesForUser(this.userName, testAdminId, filter, page, sort);
            else
                und = this.scheduleTest.getUserNodesForParent(this.userName, orgNodeId, testAdminId, filter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return und;
    }
    
    private List buildOrgNodeList(UserNodeData und) 
    {
        ArrayList nodeList = new ArrayList();
        PathNode pathNode = null;
        UserNode [] nodes = und.getUserNodes();        
        for (int i=0 ; i<nodes.length ; i++) {
            UserNode node = (UserNode)nodes[i];
            if (node != null) {
                pathNode = new PathNode();
                pathNode.setName(node.getOrgNodeName());
                pathNode.setId(node.getOrgNodeId());   
                pathNode.setFilteredCount(node.getUserCount());
                pathNode.setSelectedCount(node.getProctorCount());
                pathNode.setChildNodeCount(node.getChildNodeCount());
                pathNode.setHasChildren((new Boolean(node.getChildNodeCount().intValue()>0)).toString());
                pathNode.setCategoryName(node.getOrgNodeCategoryName());                
                nodeList.add(pathNode);
            }
        }
        return nodeList;
    }
     
    private PagerSummary buildOrgNodePagerSummary(UserNodeData und, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);        
        pagerSummary.setTotalPages(und.getTotalPages());
        pagerSummary.setTotalObjects(und.getFilteredCount());
        pagerSummary.setTotalFilteredObjects(null);        
        return pagerSummary;
    }
    
    private UserData getProctors(Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort)
    {    
        UserData ud = null;
        try {      
            ud = this.scheduleTest.getUsersForOrgNode(this.userName, orgNodeId, filter, page, sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return ud;
    }


    
    private PagerSummary buildProctorPagerSummary(UserData ud, Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);        
        pagerSummary.setTotalPages(ud.getTotalPages());
        pagerSummary.setTotalObjects(ud.getFilteredCount());
        pagerSummary.setTotalFilteredObjects(null);        
        return pagerSummary;
    }
    

    private List buildProctorList(UserData ud) 
    {
        ArrayList nodeList = new ArrayList();
        this.proctorsHashtable = new Hashtable();
        
        User [] nodes = ud.getUsers();        
        for (int i=0 ; i<nodes.length && nodes[i] != null ; i++) {
            User node = (User)nodes[i];
            if (node != null) {
                if (node.getUserId().equals(this.scheduler.getUserId()))
                    node.setEditable("F");
                else
                    node.setEditable("T");
                
                nodeList.add(node);
            }
            this.proctorsHashtable.put(node.getUserId(), node);
        }
        return nodeList;
    }
    

    private void copySelectedProctors(ScheduleTestController.ScheduleTestForm form)
    {
        this.selectedProctors = form.getSelectedProctors();    
        if (this.selectedProctors == null)
            this.selectedProctors = new ArrayList();  
            
        this.originalSelectedProctors = new ArrayList();  
            
        for (int i=0 ; i<this.selectedProctors.size(); i++) {
            User user = (User) this.selectedProctors.get(i);
            this.originalSelectedProctors.add(user);
        }
    }

    private void getUserPrincipalAdmin()
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
        if (principal != null) 
            this.userName = principal.toString();
        getSession().setAttribute("userName", this.userName);
    }
    
    private void collectSelectedProctors(Integer[] selectedProctorIds)
    {   
        if (selectedProctorIds != null) {
            
            List unselectedProctors = getUnselectedProctorsInPage(selectedProctorIds);
            for (int i=0 ; i<unselectedProctors.size() ; i++) {
                User unselected = (User) unselectedProctors.get(i);
                for (int j=0 ; j<this.selectedProctors.size() ; j++) {
                    User user = (User) this.selectedProctors.get(j);
                    if (user.getUserId().equals(unselected.getUserId()) && (!"F".equals(user.getEditable()))) {
                        this.selectedProctors.remove(j);
                        break;
                    }
                }                
            }
                        
            for (int i=0; i<selectedProctorIds.length; i++) {   
                Integer proctorId = selectedProctorIds[i];         
                if (proctorId != null) {
                    User user = (User) this.proctorsHashtable.get(proctorId);
                    if ((user != null) && (!"F".equals(user.getEditable()))) {                    
                        addSelectedProctorToList(user);
                    }
                }
            }
        }
    }

    private List getUnselectedProctorsInPage(Integer[] selectedProctorIds)
    {
        ArrayList unselectedProctors = new ArrayList();       
        if (selectedProctorIds != null) {
            if ((selectedProctorIds.length == 1) && (selectedProctorIds[0] == null)) {
                Iterator iter = this.proctorsHashtable.values().iterator();
                while (iter.hasNext()) {
                    User user = (User)iter.next();
                    if ("T".equals(user.getEditable()))
                        unselectedProctors.add(user);
                }
                return unselectedProctors;
            }    
        }
         
        if ((this.proctorsHashtable != null) && (selectedProctorIds != null)) {
            Iterator iter = this.proctorsHashtable.values().iterator();
            while (iter.hasNext()) {
                User user = (User)iter.next();
                boolean found = false;
                for (int i=0; i<selectedProctorIds.length; i++) {   
                    Integer proctorId = selectedProctorIds[i];         
                    if ((proctorId != null) && (proctorId.equals(user.getUserId()))) {
                        found = true;
                        break;
                    }
                }
                if (! found) {
                    unselectedProctors.add(user);
                }
            }
        }
        return unselectedProctors;
    }
    
    private void addSelectedProctorToList(User newUser)
    {
        if (this.selectedProctors != null) {
            boolean found = false;
            for (int i=0 ; i<this.selectedProctors.size() ; i++) {
                User user = (User) this.selectedProctors.get(i);
                if (user.getUserId().equals(newUser.getUserId())) {
                    found = true;
                }
            }
            if (! found) {
                this.selectedProctors.add(newUser);
            }
        }
    }

    private void commitSelection(ScheduleTestController.ScheduleTestForm form)
    {
        Integer [] selectedProctorIds = form.getSelectedProctorIds();
        collectSelectedProctors(selectedProctorIds);
    }    

    private void setSelectedProctorIdsToForm(ScheduleTestController.ScheduleTestForm form)
    {
        if (this.selectedProctors != null) {
            int size = this.selectedProctors.size();
            int index = 0;
            Integer[] selectedProctorIds = new Integer[size];
            for (int i=0 ; i<size; i++) {   
                User user = (User) this.selectedProctors.get(i);
                if ((user != null) && (user.getUserId() != null)) {
                    if (this.proctorsHashtable.containsKey(user.getUserId())) {
                        selectedProctorIds[index] = user.getUserId();
                        index++;
                    }    
                }
            }         
            form.setSelectedProctorIds(selectedProctorIds);
        }
    }
    
    private void addAllProctors(Integer orgNodeId)
    {
        PageParams page = new PageParams(1, 10000, 1000);
        
        UserData ud = getProctors(orgNodeId, null, page, null);
        List proctorNodes = buildProctorList(ud);
        
        for (int i=0; i <proctorNodes.size(); i++) {   
            User user = (User) proctorNodes.get(i);         
            if ( (user != null) && (!"F".equals(user.getEditable())) ) {   
                addSelectedProctorToList(user);                 
            }
        }
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

	public String getAction() {
		return action;
	}
    
}
