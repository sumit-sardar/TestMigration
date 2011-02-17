package manageOrganization;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.FilterSortPageUtils;
import utils.MessageResourceBundle;
import utils.OrgFormUtils;
import utils.OrgNodeUtils;
import utils.OrgPathListUtils;
import utils.PermissionsUtils;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.OrgNodeCategory;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.CTBConstants;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.PagerSummary;
import com.ctb.widgets.bean.PathListEntry;

import dto.Message;
import dto.NavigationPath;
import dto.Organization;
import dto.PathNode;


/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class ManageOrganizationController extends PageFlowController
{

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.db.Roles roles;

    /**
     * @common:control
     */    
    @Control()
    private com.ctb.control.db.Users users;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.userManagement.UserManagement userManagement;

    static final long serialVersionUID = 1L;
    
    private String userName = null;
    private ManageOrganizationForm savedForm = null;
    private List orgNodePath = null;
    private List previousOrgNodePath = null;

    private HashMap currentOrgNodesInPathList = null;
    public List selectedOrgNodes = null;
    public Integer[] currentOrgNodeIds = null;
    public String action = null;
    private Integer parentOrgNodeIdForAdd = null;
    public Integer currentSelectedOrgNodeId = null;
    public boolean isBack = false;
    public boolean isManageOrganization = false;
    public boolean isToFindOrg = false;
    public boolean isFindOrg = false;
    public boolean isAddFromSideBar = false;
    public boolean isCancelFromAdd = false;
    public Integer pageForParentSelected = new Integer(1);
    public boolean isFromAddEdit = false;
    public boolean isADD = false;
    // option list
    public LinkedHashMap orgLevelOptions = null;

    // navigation
    private static final String ACTION_CURRENT = "{actionForm.currentAction}";
    private static final String ACTION_ORG_NODE_ID = "{actionForm.orgNodeId}";
    private static final String ACTION_ORG_SORT_ORDER_BY ="{actionForm.orgSortOrderBy}";
    private static final String ACTION_ORG_PAGE_REQUESTED = "{actionForm.orgPageRequested}";
    private static final String ACTION_ELEMENT = "{actionForm.actionElement}";
    private static final String BUTTON_GO_INVOKED = "ButtonGoInvoked_tablePathListAnchor";
    private static final String SETUP_ORG_NODE_PATH = "setupOrgNodePath";
    private static final String ACTION_ORG_LIST_SORT_ORDER_BY ="{actionForm.orgListSortOrderBy}";
    
    public String pageMessage = null;
    public String webTitle = null;
    public String pageTitle = null;
    private User user = null;

    protected global.Global globalApp;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.organizationManagement.OrganizationManagement organizationManagement;


    /**
     * @jpf:action
     * @jpf:forward name="success" path="beginFindOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "beginFindOrganization.do")
    })
    protected Forward begin()
    {        
        return new Forward("success");
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path = "beginFindOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "beginFindOrganization.do")
    })
    protected Forward defaultAction(ManageOrganizationForm form)
    {   
        initialize(globalApp.ACTION_FIND_ORGANIZATION);
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="findOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "findOrganization.do")
    })
    protected Forward beginManageOrganization()
    {        
        getUserDetails();
        //For Bulk Accommodation
        customerHasBulkAccommodation();
        this.savedForm = initialize(globalApp.ACTION_DEFAULT);
        String orgNodeIdString = (String)this.getRequest().getAttribute("orgNodeId");
        String orgNodeName = (String)this.getRequest().getAttribute("orgNodeName");
        
        Integer orgNodeId = null;
        
        if (orgNodeIdString != null)
        {
            
            orgNodeId = new Integer(orgNodeIdString);
        
        }
        
        this.parentOrgNodeIdForAdd = orgNodeId;   
        //retrive parentOrganization for orgNodeId
        Node node = getParentOrgNode(orgNodeId, this.savedForm);
       
        //retrive actual pageRequest for orgNodeId
        SortParams sort = FilterSortPageUtils.buildSortParams(this.savedForm.getOrgSortColumn(), this.savedForm.getOrgSortOrderBy(), null, null);
        HashMap pageSummary = getPageSummary(node.getOrgNodeId(), orgNodeId, sort);
        
        setSelectedOrgNodes(orgNodeName, orgNodeId);
        setCurrentSelectedOrgNodeId(orgNodeId);
        this.savedForm.setOrgPageRequested((Integer)pageSummary.get("PageRequest"));
        this.savedForm.setOrgMaxPage((Integer)pageSummary.get("MaxPage"));
        this.savedForm.setOrgNodeId(node.getOrgNodeId());
        this.savedForm.setOrgNodeName(node.getOrgNodeName());
        this.orgNodePath = setupHierarchy(orgNodeId, null);
        this.isManageOrganization = true;
        return new Forward("success", this.savedForm);
    }


/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** FIND ORGANIZATION ************* //////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @jpf:action
     * @jpf:forward name="success" path="findOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "findOrganization.do")
    })
    protected Forward beginFindOrganization()
    {                
        getUserDetails();
        //For Bulk Accommodation
        customerHasBulkAccommodation();
        this.savedForm = initialize(globalApp.ACTION_FIND_ORGANIZATION);
        initHierarchy(this.savedForm);                
        this.globalApp.navPath.reset(globalApp.ACTION_FIND_ORGANIZATION);
        return new Forward("success", this.savedForm);
        
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="findOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "findOrganization.do")
    })
    protected Forward toFindOrganization(ManageOrganizationForm form)
    {
        this.savedForm = form.createClone();
        this.globalApp.navPath.addCurrentAction(globalApp.ACTION_FIND_ORGANIZATION, form);
        this.isToFindOrg = true;
        return new Forward("success", this.savedForm);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="find_organization.jsp"
     * @jpf:forward name="viewOrganization" path="toViewOrganization.do"
     * @jpf:forward name="addOrganization" path="toAddOrganization.do"
     * @jpf:forward name="editOrganization" path="toEditOrganization.do"
     * @jpf:forward name="deleteOrganization" path="toDeleteOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "find_organization.jsp"), 
        @Jpf.Forward(name = "viewOrganization",
                     path = "toViewOrganization.do"), 
        @Jpf.Forward(name = "addOrganization",
                     path = "toAddOrganization.do"), 
        @Jpf.Forward(name = "editOrganization",
                     path = "toEditOrganization.do"), 
        @Jpf.Forward(name = "deleteOrganization",
                     path = "toDeleteOrganization.do")
    })
    protected Forward findOrganization(ManageOrganizationForm form)
    {
        if (this.userName == null)
            checkUserState();
        Integer orgId = null;
        if (globalApp.ACTION_DELETE_ORGANIZATION.equals(globalApp.navPath.getPreviousAction()))
        {
            
            orgId = form.getSelectedOrgNodeId();
            
        }
        form.validateValues();
        
        if (globalApp.ACTION_DELETE_ORGANIZATION.equals(globalApp.navPath.getPreviousAction()))
        {
            
            form.setSelectedOrgNodeId(orgId);
            
        }
        
        String currentAction = form.getCurrentAction();

        this.globalApp.navPath.addCurrentAction(globalApp.ACTION_FIND_ORGANIZATION, form);
        
        if (currentAction.equals(globalApp.ACTION_VIEW_ORGANIZATION) || currentAction.equals(globalApp.ACTION_ADD_ORGANIZATION) || currentAction.equals(globalApp.ACTION_EDIT_ORGANIZATION) || currentAction.equals(globalApp.ACTION_DELETE_ORGANIZATION))
        {
        	
            return new Forward(currentAction, form);   
            
        }                        
        
        if (!this.isToFindOrg && !this.isManageOrganization)
        {
            
            if (currentAction.equals(globalApp.ACTION_DEFAULT))
            {
                
                this.isFindOrg = true;
                
            }
            
        }                     
        this.isToFindOrg = false;
        
      
        
        handleOrganizationControl(form, null);
        this.isAddFromSideBar = false;
        this.isCancelFromAdd = false;
        
        this.pageTitle = buildPageTitle(globalApp.ACTION_FIND_ORGANIZATION, form);
        form.setActionElement(globalApp.ACTION_DEFAULT);   
        this.getRequest().setAttribute("isFindOrganization",Boolean.TRUE);
        return new Forward("success", form);
    }

	 
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** VIEW ORGANIZATION ************* //////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @jpf:action
     * @jpf:forward name="success" path="viewOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "viewOrganization.do")
    })
    protected Forward toViewOrganization(ManageOrganizationForm form)
    {
        Integer orgId = null;
        if (globalApp.ACTION_EDIT_ORGANIZATION.equals(form.getPreviousAction()))
        {
            
            orgId = form.getSelectedOrgChildNodeId();
        
        }
        else if (globalApp.ACTION_ADD_ORGANIZATION.equals(form.getPreviousAction()))
        {
        
            orgId = form.getOrganizationDetail().getOrgId();
        
        }
        else
        {
        
            orgId = form.getSelectedOrgNodeId();    
        
        }
               
        this.savedForm = form.createClone();  
        setOrganizationToForm(orgId, this.savedForm, "VIEW");
        PermissionsUtils.setPermissionRequestAttributeORG(this.getRequest(), this.savedForm, false);
        this.globalApp.navPath.addCurrentAction(globalApp.ACTION_VIEW_ORGANIZATION);
        this.isAddFromSideBar = false;
        return new Forward("success", this.savedForm);
       
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="view_organization.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "view_organization.jsp")
    })
    protected Forward viewOrganization(ManageOrganizationForm form)
    {
        this.pageTitle = buildPageTitle(globalApp.ACTION_VIEW_ORGANIZATION, form);
        return new Forward("success", form);
    }


/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** DELETE ORGANIZATION ************* //////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @jpf:action
     * @jpf:forward name="success" path="deleteOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "deleteOrganization.do")
    })
    protected Forward toDeleteOrganization(ManageOrganizationForm form)
    {       
        this.isADD = false; 
        this.globalApp.navPath.addCurrentAction(globalApp.ACTION_DELETE_ORGANIZATION);
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="toFindOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "toFindOrganization.do")
    })
    protected Forward deleteOrganization(ManageOrganizationForm form)
    {
        
        boolean deleteOK = false; 
        this.isADD = false; 
        Node deleteNode = new Node();
        NodeData ond = null;    
        Integer orgNodeId = null;
        if (globalApp.ACTION_EDIT_ORGANIZATION.equals(this.globalApp.navPath.getPreviousAction()))
        {
            
            Integer deleteNodeId = form.getSelectedOrgChildNodeId();
            
            if (deleteNodeId == null)
            {
                
                deleteNodeId = form.getTempOrgChildNodeId();
            }
            
            deleteNode.setOrgNodeId(deleteNodeId);
            orgNodeId = form.getSelectedOrgNodeId();
            form.setOrgNodeId(orgNodeId);
            form.setOrgNodeName(form.getSelectedOrgNodeName());
            
        }
        else if (globalApp.ACTION_ADD_ORGANIZATION.equals(form.getPreviousAction()))
        {
                
            deleteNode.setOrgNodeId(form.getTempOrgChildNodeId());
            orgNodeId = form.getSelectedOrgNodeId();
            form.setOrgNodeId(orgNodeId);
            form.setOrgNodeName(form.getSelectedOrgNodeName());
                
        }
        else if (globalApp.ACTION_EDIT_ORGANIZATION.equals(form.getPreviousAction()))
        {
                
            deleteNode.setOrgNodeId(form.getSelectedOrgChildNodeId());
            orgNodeId = form.getSelectedOrgNodeId();
            form.setOrgNodeId(orgNodeId);
            form.setOrgNodeName(form.getSelectedOrgNodeName());
                
        }
        else if (globalApp.ACTION_VIEW_ORGANIZATION.equals(this.globalApp.navPath.getPreviousAction()))
        {
                
            deleteNode.setOrgNodeId(form.getSelectedOrgChildNodeId());   
            orgNodeId = form.getSelectedOrgNodeId();
                
        }
        else
        {
                
            deleteNode.setOrgNodeId(form.getSelectedOrgNodeId());
            orgNodeId = form.getOrgNodeId();
                
        }
        deleteOrganization(this.userName, deleteNode, form);
        deleteOK = true; 
        
       
        if (deleteOK)
        {
        
            form.setMessage(Message.DELETE_TITLE, Message.DELETE_SUCCESSFUL, Message.INFORMATION);
       
        }
        else
        {
        
            form.setMessage(Message.DELETE_TITLE, Message.DELETE_ERROR_TAS, Message.ERROR);
       
        }
        
        form.setActionElement(globalApp.ACTION_DEFAULT);   
        form.setCurrentAction(globalApp.ACTION_DEFAULT);
        form.setPreviousAction(globalApp.ACTION_DELETE_ORGANIZATION);
        form.setSelectedOrgNodeId(null);
        form.setSelectedOrgNodeName(null);
        
        return new Forward("success", form);
    }



/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** EDIT ORGANIZATION ************* //////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @jpf:action
     * @jpf:forward name="success" path="editOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "editOrganization.do")
    })
    protected Forward toEditOrganization(ManageOrganizationForm form)
    { 
        Integer orgId = null;
        boolean isEdit = true;
        boolean isPreviousAdd = false;
        String childSortName = "";
        String childSortOrderBy = "";
        
        if (form.currentAction.equals(globalApp.ACTION_VIEW_ORGANIZATION) && globalApp.ACTION_DEFAULT.equals(form.previousAction))
        { 
            
            orgId = form.getSelectedOrgChildNodeId();
            form.setPreviousAction(globalApp.ACTION_VIEW_ORGANIZATION);
       
        }
        else if (form.currentAction.equals(globalApp.ACTION_DEFAULT) && globalApp.ACTION_VIEW_ORGANIZATION.equals(form.previousAction))
        {
        
            orgId = form.getSelectedOrgChildNodeId();
            form.setPreviousAction(globalApp.ACTION_VIEW_ORGANIZATION);
            if (this.isADD)
            {
                
                isEdit = true;
                
            }
            else
            {
                
                isEdit = false;
                
            }
            isPreviousAdd = true;
       
        }
        else if (form.currentAction.equals(globalApp.ACTION_DEFAULT) && (globalApp.ACTION_EDIT_ORGANIZATION.equals(form.previousAction)))
        {
        
            orgId = form.getSelectedOrgChildNodeId();
            form.setPreviousAction(globalApp.ACTION_VIEW_ORGANIZATION);
            isEdit = false;
            
       
        }
        else if (form.currentAction.equals(globalApp.ACTION_DEFAULT) && (globalApp.ACTION_ADD_ORGANIZATION.equals(form.previousAction)))
        {
        
            orgId = form.getTempOrgChildNodeId();
            isEdit = false;
            isPreviousAdd = true;
       
        }
        else
        {
        
            orgId = form.getSelectedOrgNodeId(); 
       
        }

        String orgNodeName = form.getOrgNodeName();
        Integer orgNodeId = form.getOrgNodeId();
        setSelectedOrgNodes(orgNodeName, orgNodeId);        
        setCurrentSelectedOrgNodeId(orgNodeId);
        
        if (isPreviousAdd)
        {
        
            orgNodeName = form.getSelectedOrgNodeName();
            orgNodeId = form.getSelectedOrgNodeId();
            setSelectedOrgNodes(orgNodeName, orgNodeId);        
            setCurrentSelectedOrgNodeId(orgNodeId);
        }
        
        childSortName = this.savedForm.getOrgSortColumn();
        childSortOrderBy = this.savedForm.getOrgSortOrderBy();
              
        this.savedForm = form.createClone();
         
        this.globalApp.navPath.addCurrentAction(globalApp.ACTION_EDIT_ORGANIZATION);
        setOrganizationToForm(orgId, this.savedForm, "EDIT");
        if (this.savedForm.getSelectedOrgName() != null && this.savedForm.getSelectedOrgName().length() > 0)
        {
        
            this.getRequest().getSession().setAttribute("organizationName", 
                    this.savedForm.getSelectedOrgName());
        }
        
        this.savedForm.setOrgSortColumn(childSortName);
        this.savedForm.setOrgSortOrderBy(childSortOrderBy);
       
        if (isEdit)
        {
            if (this.orgNodePath.size() > 2)
            {
                Node orgNodeOfParent = getParentOrgNode(form.getOrgNodeId(), this.savedForm);
                if (orgNodeOfParent != null)
                {
                    
                    this.savedForm.setOrgNodeId(orgNodeOfParent.getOrgNodeId());
                    this.savedForm.setOrgNodeName(orgNodeOfParent.getOrgNodeName());
       
                } 
            }
            else
            {
               
                this.savedForm.setOrgNodeId(new Integer(0));
                this.savedForm.setOrgNodeName("Top");
         
            }  
        }
               
        this.previousOrgNodePath = createCloneList(this.orgNodePath);
        SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgSortColumn(), form.getOrgSortOrderBy(), null, null);
        HashMap pageSummary = getPageSummary(this.savedForm.getOrgNodeId(), this.savedForm.getSelectedOrgNodeId(), sort);
        
        this.savedForm.setOrgPageRequested((Integer)pageSummary.get("PageRequest"));
        this.savedForm.setOrgMaxPage((Integer)pageSummary.get("MaxPage"));
        this.savedForm.setOrgSortColumn(form.getOrgSortColumn());
        this.savedForm.setOrgSortOrderBy(form.getOrgSortOrderBy());
        
        if (!isPreviousAdd)
        {
        
            if (this.orgNodePath.size() < 2)
            {
               
                this.savedForm.setOrgMaxPage (new Integer(1));
        
            }
        }
        this.isADD = false;
        return new Forward("success", this.savedForm);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="edit_organization.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "edit_organization.jsp")
    })
    protected Forward editOrganization(ManageOrganizationForm form)
    {
        try
        {
            
            Integer orgNodeId = null;

            if ((globalApp.ACTION_ADD_ORGANIZATION.equals(form.previousAction)))
            {

                orgNodeId = form.getTempOrgChildNodeId();

            }
            else
            {

                orgNodeId = form.getSelectedOrgChildNodeId();

            }
            
            if (form.getSelectedOrgNodeId() == null)
            {
                
                form.setSelectedOrgNodeId(this.currentOrgNodeIds[0]);
                
                
            }
 
            
            if ((orgNodeId.intValue() == form.getSelectedOrgNodeId().intValue()) || isChild(orgNodeId))
            {
               
                this.orgLevelOptions = new LinkedHashMap();
        
            }
            else
            {
               
                if (!form.getActionElement().equals(ACTION_ORG_NODE_ID) && !form.getActionElement().equals(ACTION_ORG_SORT_ORDER_BY) && !form.getActionElement().equals(ACTION_ORG_PAGE_REQUESTED) && !form.getActionElement().equals(BUTTON_GO_INVOKED) && !form.getActionElement().equals(SETUP_ORG_NODE_PATH))
                {
                
                    initOrgLevelOption(globalApp.ACTION_EDIT_ORGANIZATION, orgNodeId, form.getSelectedOrgNodeId());
                    
                    this.isFromAddEdit = true; 
                
                }

            }

            if (form.getSelectedOrgNodeTypeId() != null && !form.getSelectedOrgNodeTypeId().equals("") && this.orgLevelOptions.containsKey(
                        Integer.valueOf(form.getSelectedOrgNodeTypeId())) && form.getActionElement().equals(ACTION_CURRENT))
            {
            
                this.savedForm.setSelectedOrgNodeTypeId(
                        form.getSelectedOrgNodeTypeId());    
                
            }
            
            if (form.getSelectedOrgNodeTypeId() != null && !form.getSelectedOrgNodeTypeId().equals("") && !this.orgLevelOptions.containsKey(Integer.valueOf(form.getSelectedOrgNodeTypeId())))
            {

                form.setSelectedOrgNodeTypeId("");

            }
            
            if (this.savedForm.getSelectedOrgNodeTypeId() != null && !this.savedForm.getSelectedOrgNodeTypeId().equals("") && this.orgLevelOptions.containsKey(
                        Integer.valueOf(this.savedForm.getSelectedOrgNodeTypeId())) && (!form.getActionElement().equals(ACTION_CURRENT) && !form.getActionElement().equals(ACTION_ORG_NODE_ID) && !form.getActionElement().equals(ACTION_ORG_LIST_SORT_ORDER_BY) && !form.getActionElement().equals(ACTION_ORG_PAGE_REQUESTED) && !form.getActionElement().equals(BUTTON_GO_INVOKED) && !form.getActionElement().equals(SETUP_ORG_NODE_PATH) && !form.getActionElement().equals(ACTION_ELEMENT)))
            {
            
                form.setSelectedOrgNodeTypeId(this.savedForm.getSelectedOrgNodeTypeId());    
                
            }
             

            handleAddEditOrganization(form, globalApp.ACTION_EDIT_ORGANIZATION);
            this.pageTitle = buildPageTitle(globalApp.ACTION_EDIT_ORGANIZATION, form);
               
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        adjustLayerSelection(form);
        setFormInfoOnRequest(form);
        return new Forward("success", form);
        
    }
    
    private boolean isChild(Integer orgNodeId)
    {
        
        int size = this.orgNodePath.size();
        
        for (int i = 0; i < size; i++)
        {
          
            PathListEntry node =(PathListEntry)this.orgNodePath.get(i);
            if (orgNodeId.intValue() == node.getValue().intValue())
            {
                
                return true;
                  
            }
            
        }
        
        return false;
        
    }
    
    private void adjustLayerSelection(ManageOrganizationForm form) 
    {
        String orgNodeTypeId = form.getSelectedOrgNodeTypeId();
        if ((orgNodeTypeId == null) || orgNodeTypeId.equals(""))
            orgNodeTypeId = "0";
        
        if (orgNodeTypeId.equals("0") || (! this.orgLevelOptions.containsKey(Integer.valueOf(orgNodeTypeId))))
        {
            this.orgLevelOptions.put("", Message.SELECT_ENTITY);
        }
        
        if (! this.orgLevelOptions.containsKey(Integer.valueOf(orgNodeTypeId)))
        {
            form.setSelectedOrgNodeTypeId(""); 
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** ADD ORGANIZATION ************* //////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @jpf:action
     * @jpf:forward name="success" path="addOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "addOrganization.do")
    })
    protected Forward beginAddOrganization()
    {
        getUserDetails();
        //For Bulk Accommodation
        customerHasBulkAccommodation();
        this.savedForm = initialize(globalApp.ACTION_ADD_ORGANIZATION);
        initHierarchy(this.savedForm);                
        this.globalApp.navPath.reset(globalApp.ACTION_ADD_ORGANIZATION);
        this.isAddFromSideBar = true;
        return new Forward("success", this.savedForm);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="addOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "addOrganization.do")
    })
    protected Forward toAddOrganization(ManageOrganizationForm form)
    {
        this.isADD = false;
        Integer orgId = null;
        boolean isPageRequestRequire = false;
        this.previousOrgNodePath = createCloneList(this.orgNodePath); 
        this.savedForm = form.createClone();
      
        if ((form.currentAction.equals(globalApp.ACTION_VIEW_ORGANIZATION) && globalApp.ACTION_DEFAULT.equals(form.previousAction)) || (form.currentAction.equals(globalApp.ACTION_DEFAULT) && globalApp.ACTION_VIEW_ORGANIZATION.equals(form.previousAction)))
        {
            
            orgId = form.getSelectedOrgChildNodeId();
            this.savedForm.setSelectedOrgNodeId(orgId);
            this.savedForm.setPreviousAction(globalApp.ACTION_VIEW_ORGANIZATION);
            this.savedForm.setPreviousParentId(orgId);
            this.savedForm.setOrgNodeId(form.getSelectedOrgNodeId());
            this.savedForm.setOrgNodeName(form.getSelectedOrgNodeName());
       
        }
        else if (form.currentAction.equals(globalApp.ACTION_DEFAULT) && (globalApp.ACTION_ADD_ORGANIZATION.equals(form.previousAction)))
        {
               
            orgId = form.getTempOrgChildNodeId();
            this.savedForm.setSelectedOrgNodeId (form.getTempOrgChildNodeId());
            this.savedForm.setPreviousParentId(form.getSelectedOrgNodeId());
            this.savedForm.setPreviousParentName(form.getSelectedOrgNodeName());
            this.savedForm.setOrgNodeId(form.getSelectedOrgNodeId());
            this.savedForm.setOrgNodeName(form.getSelectedOrgNodeName());
            isPageRequestRequire = true;
        
        }
        else if (form.currentAction.equals(globalApp.ACTION_DEFAULT) && (globalApp.ACTION_EDIT_ORGANIZATION.equals(form.previousAction)))
        {
              
            orgId = form.getSelectedOrgChildNodeId();
            this.savedForm.setPreviousParentId(orgId);
            this.savedForm.setPreviousParentName(form.getSelectedOrgNodeName());
            this.savedForm.setOrgNodeId(form.getSelectedOrgNodeId());
            this.savedForm.setOrgNodeName(form.getSelectedOrgNodeName());
            isPageRequestRequire = true;
        }
        else
        {
            orgId = form.getSelectedOrgNodeId();
            this.savedForm.setPreviousParentId(orgId);
        }
        
        // getPagerequest
        
        if (isPageRequestRequire)
        {
            
            SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgSortColumn(), form.getOrgSortOrderBy(), null, null);
            HashMap pageSummary = getPageSummary(form.getSelectedOrgNodeId(), orgId, sort);
        
            this.savedForm.setOrgPageRequested((Integer)pageSummary.get("PageRequest"));
            this.savedForm.setOrgMaxPage((Integer)pageSummary.get("MaxPage"));
        
        }

        Node nodeDetails = getOrganization(orgId);
        form.setSelectedOrgNodeName(nodeDetails.getOrgNodeName());
        String orgNodeName = form.getSelectedOrgNodeName();
        setSelectedOrgNodes(orgNodeName, orgId);                
        setCurrentSelectedOrgNodeId(orgId);
        initOrgLevelOption(globalApp.ACTION_ADD_ORGANIZATION, orgId, null);
        this.globalApp.navPath.addCurrentAction(globalApp.ACTION_ADD_ORGANIZATION);
                                                 
        return new Forward("success", this.savedForm);
    }

    private Node getOrganization(Integer orgId) 
    {
        Node nodeDetails = null;
        try
        {
            nodeDetails = organizationManagement.getOrganization(this.userName, orgId);
        } 
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return nodeDetails;
    }
        
    private HashMap getPageSummary(Integer parentNodeId, Integer selectedNodeId, SortParams sort) 
    {
        HashMap pageSummary = null;
        try
        {
            pageSummary = organizationManagement.getPageRequestForOrg(parentNodeId, selectedNodeId, sort);
        } 
        catch (CTBBusinessException e)
        {
            e.printStackTrace();
        }
        return pageSummary;
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="add_organization.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "add_organization.jsp")
    })
    protected Forward addOrganization(ManageOrganizationForm form)
    {
        if (form.getSelectedOrgNodeId() != null && form.getMessage().getType() == null && (!form.getActionElement().equals(ACTION_ORG_NODE_ID) && !form.getActionElement().equals(ACTION_ORG_SORT_ORDER_BY)) && !form.getActionElement().equals(ACTION_ORG_PAGE_REQUESTED) && !form.getActionElement().equals(BUTTON_GO_INVOKED) && !form.getActionElement().equals(SETUP_ORG_NODE_PATH))
        { 
            
            if (this.globalApp.navPath.getPreviousAction().equals(globalApp.ACTION_ADD_ORGANIZATION) || globalApp.ACTION_ADD_ORGANIZATION.equals(form.getPreviousAction()))
            { 
                if (form.getActionElement().equals("{actionForm.actionElement}"))
                { 
                    
                    initOrgLevelOption(globalApp.ACTION_ADD_ORGANIZATION, form.getSelectedOrgNodeId(), null); 
                    
                }
                else
                { 
                    
                    initOrgLevelOption(globalApp.ACTION_ADD_ORGANIZATION, form.getTempOrgChildNodeId(), null); 
                    
                }           
                
            }
            else if (this.globalApp.navPath.getPreviousAction().equals(globalApp.ACTION_EDIT_ORGANIZATION) || globalApp.ACTION_EDIT_ORGANIZATION.equals(form.getPreviousAction()) || this.globalApp.navPath.getPreviousAction().equals(globalApp.ACTION_VIEW_ORGANIZATION))
            { 
                            
                if (form.getActionElement().equals("{actionForm.actionElement}"))
                { 
                    
                    initOrgLevelOption(globalApp.ACTION_ADD_ORGANIZATION, form.getSelectedOrgNodeId(), null); 
                    
                }
                else
                { 
                    
                    initOrgLevelOption(globalApp.ACTION_ADD_ORGANIZATION, form.getSelectedOrgChildNodeId(), null); 
                    
                }               
                
                
                
            }
            else
            { 
                
                initOrgLevelOption(globalApp.ACTION_ADD_ORGANIZATION, form.getSelectedOrgNodeId(), null); 
                
            } 
           
            adjustLayerSelection(form);
            
            this.isFromAddEdit = true; 
        
        } 
       
        handleAddEditOrganization(form, globalApp.ACTION_ADD_ORGANIZATION); 
        this.pageTitle = buildPageTitle(globalApp.ACTION_ADD_ORGANIZATION, form); 
        this.getRequest().setAttribute("isAddOrganization",Boolean.TRUE); 
        setFormInfoOnRequest(form);
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="toViewOrganization.do"
     * @jpf:forward name="errorAdd" path="addOrganization.do"
     * @jpf:forward name="errorEdit" path="editOrganization.do"
     * @jpf:forward name = "viewOrganization" path ="viewOrganization.do" 
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "toViewOrganization.do"), 
        @Jpf.Forward(name = "errorAdd",
                     path = "addOrganization.do"), 
        @Jpf.Forward(name = "errorEdit",
                     path = "editOrganization.do"), 
        @Jpf.Forward(name = "viewOrganization",
                     path = "viewOrganization.do")
    })
    protected Forward saveOrganization(ManageOrganizationForm form)
    {
        boolean addOrganization = this.globalApp.navPath.getCurrentAction().equals(globalApp.ACTION_ADD_ORGANIZATION);
        
        if (this.isFromAddEdit)
        {
        
            form.setOrgPageRequested(pageForParentSelected);
           
        }
        
        this.isFromAddEdit = false;
        
        if (this.globalApp.navPath.getCurrentAction().equals(globalApp.ACTION_VIEW_ORGANIZATION))
        {
        
            return new Forward("viewOrganization", form);
        
        }
         
        if (globalApp.ACTION_ADD_ORGANIZATION.equals(form.getPreviousAction()) && !addOrganization)
        {
            form.setSelectedOrgChildNodeId(form.getTempOrgChildNodeId());
        }
        
        boolean validInfo = true;
        
        
        if ("".equals(form.getSelectedOrgName().trim()) || "".equals(form.getSelectedOrgNodeName().trim()) || "".equals(form.getSelectedOrgNodeTypeId()))
        {
        
            validInfo = OrgFormUtils.verifyOrgInformation(form);
            
        }
        else if (isInvalidParent())
        {
           
            form.setMessage(Message.EDIT_ERROR, Message.INVALID_PARENT, Message.ERROR) ;
          
            validInfo = false;
            
        }
        else
        {
        
            validInfo = OrgFormUtils.verifyOrgInformation(form);
            
        }
            
        
        if (!validInfo)
        {           
        
            if (addOrganization)
            {
                
                return new Forward("errorAdd", form);
        
            }
            else
            {
                
                this.pageTitle = buildPageTitle(globalApp.ACTION_EDIT_ORGANIZATION, form);
                return new Forward("errorEdit", form);
                
            }             
        }   
        
        form.setOrgSortColumn(this.savedForm.getOrgSortColumn());
        form.setOrgSortOrderBy(this.savedForm.getOrgSortOrderBy());
                
        if (form.getSelectedOrgNodeId() == null)
        {
            form.setSelectedOrgNodeId(this.currentOrgNodeIds[0]);
        }

        Node nodeDetail = Organization.makeCopy(form);
                           
        if (saveOrgInformation(addOrganization, nodeDetail, form))
        {
            // create or edit success
            if (addOrganization)
            {
                
                form.setMessage(Message.ADD_TITLE, Message.ADD_SUCCESSFUL, Message.INFORMATION);
                form.setPreviousAction(globalApp.ACTION_ADD_ORGANIZATION);
                Organization org = new Organization(nodeDetail);
                form.setOrganizationDetail(org);                
            
            }
            else
            {                
              
                form.setMessage(Message.EDIT_TITLE, Message.EDIT_SUCCESSFUL, Message.INFORMATION);
                form.setPreviousAction(globalApp.ACTION_EDIT_ORGANIZATION);
            
            }
        }
        else
        {
            // create or edit failure
            if (addOrganization)
            {
          
                form.setMessage(Message.ADD_TITLE, Message.ADD_ERROR, Message.ERROR);
                return new Forward("errorAdd", form);
          
            }
            else
            {
                return new Forward("errorEdit", form);
                
            }
        }

        form.setActionElement(globalApp.ACTION_DEFAULT);   
        form.setCurrentAction(globalApp.ACTION_DEFAULT);
        this.globalApp.navPath.reset(globalApp.ACTION_VIEW_ORGANIZATION);
        setFormInfoOnRequest(form);
        return new Forward("success", form);
    }

    /**
     * handleAddEditOrganization
     */
    protected void handleAddEditOrganization(ManageOrganizationForm form, String action)
    {       
        form.validateValues();
        handleOrganizationControl(form, action);
        form.setActionElement(globalApp.ACTION_DEFAULT); 
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="defaultAction" path="toFindOrganization.do"
     * @jpf:forward name="findOrganization" path="toFindOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "defaultAction",
                     path = "toFindOrganization.do"), 
        @Jpf.Forward(name = "findOrganization",
                     path = "toFindOrganization.do")
    })
    protected Forward backToPreviousAction(ManageOrganizationForm form)
    {
        this.isADD = false;
        String action = this.globalApp.navPath.resetToPreviousAction();
        String loginUserRole = OrgFormUtils.getLoginUserRole(this.userName, this.roles, form);
        
            
        if (form.getCurrentAction().equals(globalApp.ACTION_VIEW_ORGANIZATION) && globalApp.ACTION_DEFAULT.equals(form.getPreviousAction()))
        {
          
            this.savedForm.setOrgNodeId(form.getOrgNodeId());
            this.savedForm.setOrgNodeName(form.getOrgNodeName());
            this.savedForm.setSelectedOrgNodeId(form.getSelectedOrgChildNodeId());
        
        }
        else
        {
        
            if (form.getSelectedOrgChildNodeId() == null)
            {
        
                this.savedForm.setSelectedOrgNodeId(form.getTempOrgChildNodeId());
                this.savedForm.setOrgNodeName(form.getSelectedOrgNodeName());
                this.savedForm.setOrgNodeId(form.getSelectedOrgNodeId());
                
            }
            else
            {
        
                this.savedForm.setSelectedOrgNodeId(form.getSelectedOrgChildNodeId());
                if (loginUserRole != null && loginUserRole.
                        equalsIgnoreCase(CTBConstants.ROLE_NAME_ACCOUNT_MANAGER))
                {
                
                    this.savedForm.setOrgNodeName(form.getPreviousParentName());
                    this.savedForm.setOrgNodeId(form.getPreviousParentId());
                    
                }
                else
                {
                    
                    this.savedForm.setOrgNodeName(form.getSelectedOrgNodeName());
                    this.savedForm.setOrgNodeId(form.getSelectedOrgNodeId());
                    
                }
                
            }
            
            this.currentSelectedOrgNodeId = this.savedForm.getSelectedOrgNodeId();
            SortParams sort = FilterSortPageUtils.buildSortParams(this.savedForm.getOrgSortColumn(), this.savedForm.getOrgSortOrderBy(), null, null);
            HashMap pageSummary = getPageSummary(form.getSelectedOrgNodeId(), this.savedForm.getSelectedOrgNodeId(), sort);
        
            this.savedForm.setOrgPageRequested((Integer)pageSummary.get("PageRequest"));
            this.savedForm.setOrgMaxPage((Integer)pageSummary.get("MaxPage"));
            this.isBack = true;
        
        }
            
        
        
        this.savedForm.setCurrentAction(globalApp.ACTION_DEFAULT);
        this.savedForm.setMessage(null);

        this.globalApp.navPath.restoreOrgNavInfo(action, this.savedForm);
        
        return new Forward(action, this.savedForm);
    }

    /**
     * forward to the last action
     * @jpf:action
     * @jpf:forward name="findOrganization" path="toFindOrganization.do"
     * @jpf:forward name="viewOrganization" path="toViewOrganization.do"
     * @jpf:forward name="addOrganization" path="toAddOrganization.do"
     * @jpf:forward name="editOrganization" path="toEditOrganization.do"
     * @jpf:forward name="deleteOrganization" path="toDeleteOrganization.do"
     * @jpf:forward name="defaultAction" path="toFindOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "findOrganization",
                     path = "toFindOrganization.do"), 
        @Jpf.Forward(name = "viewOrganization",
                     path = "toViewOrganization.do"), 
        @Jpf.Forward(name = "addOrganization",
                     path = "toAddOrganization.do"), 
        @Jpf.Forward(name = "editOrganization",
                     path = "toEditOrganization.do"), 
        @Jpf.Forward(name = "deleteOrganization",
                     path = "toDeleteOrganization.do"), 
        @Jpf.Forward(name = "defaultAction",
                     path = "toFindOrganization.do")
    })
    protected Forward returnToPreviousAction(ManageOrganizationForm form)
    {
        String action = this.globalApp.navPath.resetToPreviousAction();
        form.setOrgSortColumn(this.savedForm.getOrgSortColumn());
        form.setOrgSortOrderBy(this.savedForm.getOrgSortOrderBy());
        if (form.getSelectedOrgChildNodeId() != null && form.getCurrentAction().equals(globalApp.ACTION_EDIT_ORGANIZATION))
        {
            
            this.savedForm.setSelectedOrgNodeId(form.getSelectedOrgChildNodeId());
            this.savedForm.setPreviousAction(form.getCurrentAction());
            this.savedForm.setOrgNodeId(form.getPreviousParentId());
            this.savedForm.setOrgNodeName(form.getPreviousParentName());
            if (previousOrgNodePath != null)
            {
                
                this.orgNodePath = createCloneList(previousOrgNodePath);
            
            }
        }
        
        if (form.getCurrentAction().equals(globalApp.ACTION_VIEW_ORGANIZATION))
        {
            
            if (form.getSelectedOrgChildNodeId() != null && (globalApp.ACTION_VIEW_ORGANIZATION.equals(form.getPreviousAction()) || globalApp.ACTION_DEFAULT.equals(form.getPreviousAction())))
            {
                
                this.savedForm.setSelectedOrgNodeId(form.getSelectedOrgChildNodeId());
                
            }
            else if (globalApp.ACTION_VIEW_ORGANIZATION.equals(form.getPreviousAction()))
            {
                
                this.savedForm.setSelectedOrgNodeId(form.getPreviousParentId());
                if (form.getSelectedOrgChildNodeId() == null)
                {
                    
                    this.isADD = true;
                    
                }
                
            } 
            else
            {
                
                this.savedForm.setSelectedOrgNodeId(form.getSelectedOrgNodeId());
                
            }
        }
        
        if ((form.getCurrentAction().equals(globalApp.ACTION_ADD_ORGANIZATION) && !(this.isAddFromSideBar)))
        {
            
            this.savedForm.setSelectedOrgNodeId(form.getPreviousParentId());
            this.savedForm.setPreviousAction(form.getCurrentAction());
            if (previousOrgNodePath != null)
            {
                
                this.orgNodePath = createCloneList(previousOrgNodePath);
            
            }
        
        }
        
        String loginUserRole = OrgFormUtils.getLoginUserRole(this.userName, this.roles, form);
       
        if (form.getCurrentAction().equals(globalApp.ACTION_DEFAULT))
        {
            
            if (form.getSelectedOrgChildNodeId() == null)
            {
            
                this.savedForm.setSelectedOrgNodeId(form.getPreviousParentId());
                this.isADD = true;
                if (loginUserRole != null && loginUserRole.
                        equalsIgnoreCase(CTBConstants.ROLE_NAME_ACCOUNT_MANAGER))
                {
                            
                    if (this.orgNodePath.size() >= 2)
                    {
                    
                        Node orgNodeOfParent = getParentOrgNode(form.getPreviousParentId(), form);
                        
                        if (orgNodeOfParent != null)
                        {
                            
                            this.savedForm.setOrgNodeId(orgNodeOfParent.getOrgNodeId());
                            this.savedForm.setOrgNodeName(orgNodeOfParent.getOrgNodeName());
                            
                        } 
                    }
                    else
                    {
                    
                        this.savedForm.setOrgNodeId(new Integer(0));
                        this.savedForm.setOrgNodeName("Top");
                        
                    }                            

                }
                else
                {
                    
                    if (this.orgNodePath.size() > 2)
                    {
                    
                        Node orgNodeOfParent = getParentOrgNode(form.getPreviousParentId(), form);
                        
                        if (orgNodeOfParent != null)
                        {
                            
                            this.savedForm.setOrgNodeId(orgNodeOfParent.getOrgNodeId());
                            this.savedForm.setOrgNodeName(orgNodeOfParent.getOrgNodeName());
                            
                        } 
                    }
                    else
                    {
                    
                        this.savedForm.setOrgNodeId(new Integer(0));
                        this.savedForm.setOrgNodeName("Top");
                        
                    } 
                    
                }
                
                if (globalApp.ACTION_VIEW_ORGANIZATION.equals(form.getPreviousAction()))
                {
                    
                    this.savedForm.setSelectedOrgNodeId(form.getPreviousParentId());
                    
                }
                if (globalApp.ACTION_ADD_ORGANIZATION.equals(form.getPreviousAction()))
                {
                    
                    Organization org = new Organization();
                    org.setOrgId(form.getTempOrgChildNodeId());
                    this.savedForm.setOrganizationDetail(org);
                    
                }
                
            }
            else
            {
              
                this.savedForm.setSelectedOrgNodeId (form.getSelectedOrgChildNodeId());
                
                this.savedForm.setOrgNodeId(form.getSelectedOrgNodeId());
                this.savedForm.setOrgNodeName (form.getSelectedOrgNodeName());
                
                
                
                if (globalApp.ACTION_EDIT_ORGANIZATION.equals(form.getPreviousAction()))
                {
                
                    SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgSortColumn(), form.getOrgSortOrderBy(), null, null);
                    HashMap pageSummary = getPageSummary(form.getSelectedOrgNodeId(), form.getSelectedOrgChildNodeId(), sort);
            
                    this.savedForm.setOrgPageRequested((Integer)pageSummary.get("PageRequest"));
                    this.savedForm.setOrgMaxPage((Integer)pageSummary.get("MaxPage"));
                     
                }
                
                if (globalApp.ACTION_VIEW_ORGANIZATION.equals(form.getPreviousAction()))
                {
                    
                    if (previousOrgNodePath != null)
                    {
                        
                        this.orgNodePath = createCloneList(previousOrgNodePath);
                        
                    }
                    
                     
                    
                    if (this.previousOrgNodePath.size() > 1)
                    {
               
                        Node orgNodeOfParent = getParentOrgNode(form.getPreviousParentId(), form);
                        this.savedForm.setOrgNodeId(orgNodeOfParent.getOrgNodeId());
                        this.savedForm.setOrgNodeName (orgNodeOfParent.getOrgNodeName());
                            
                  
                    }
                    else
                    {
                    
                   
                    
                        this.savedForm.setOrgNodeId(new Integer(0));
                        this.savedForm.setOrgNodeName("Top");
                        
               
                    }
                    
                    if (previousOrgNodePath != null)
                    {
                        
                        this.orgNodePath = createCloneList(previousOrgNodePath);
                        
                    }
                    
                }
                
                if (globalApp.ACTION_ADD_ORGANIZATION.equals(form.getPreviousAction()))
                {
                   
                    if (previousOrgNodePath != null)
                    {
                        
                        this.orgNodePath = createCloneList(previousOrgNodePath);
                        
                    }
                    
                    if (this.previousOrgNodePath.size() > 1)
                    {
               
                        Node orgNodeOfParent = getParentOrgNode(form.getPreviousParentId(), form);
                        this.savedForm.setOrgNodeId(orgNodeOfParent.getOrgNodeId());
                        this.savedForm.setOrgNodeName (orgNodeOfParent.getOrgNodeName());
               
                    }
                    else
                    {
               
                        this.savedForm.setOrgNodeId(new Integer(0));
                        this.savedForm.setOrgNodeName("Top");
               
                    }
                    
                    if (previousOrgNodePath != null)
                    {
                        
                        this.orgNodePath = createCloneList(previousOrgNodePath);
                        
                    }
                  }
                
            }
        }
        
        if ( this.isAddFromSideBar ) {
            
            this.isCancelFromAdd = true;
        
        }        
        
        this.savedForm.setCurrentAction(globalApp.ACTION_DEFAULT);
        this.savedForm.setMessage(null);
             
        this.globalApp.navPath.restoreOrgNavInfo(action, this.savedForm);
        this.isFromAddEdit = false;
        setFormInfoOnRequest(this.savedForm);            
        return new Forward(action, this.savedForm);
    }
    
    
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="addOrganization.do"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "addOrganization.do")
		}
	)
    protected Forward setOrgLevelOptions(ManageOrganizationForm form)
    {
        Integer orgId = form.getSelectedOrgNodeId();
        form.setCurrentAction(globalApp.ACTION_ADD_ORGANIZATION);
        initOrgLevelOption(form.getCurrentAction(),orgId,null);
        
        return new Forward("success",form);
    }
    
    
 /////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////// private methods//////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////
    
    private void checkUserState() {
        if(this.userName == null) getUserDetails();
    }

    /**
     * handleOrganizationControl
     */
    private void handleOrganizationControl(ManageOrganizationForm form, String thisAction)
    {
        if(this.userName == null) checkUserState();
        
        String actionElement = form.getActionElement();
        String currentAction = form.getCurrentAction();
        
        String orgNodeName = form.getOrgNodeName();
        Integer orgNodeId = form.getOrgNodeId(); 
        
        form.resetValuesForAction(actionElement); 
                        
        boolean nodeChanged = OrgPathListUtils.adjustOrgNodePath(
                                    this.orgNodePath, orgNodeId, orgNodeName);

        if (nodeChanged) {             

 			if (!(currentAction.equals(globalApp.ACTION_EDIT_ORGANIZATION)
                || currentAction.equals(globalApp.ACTION_ADD_ORGANIZATION) 
                || currentAction.equals(globalApp.ACTION_VIEW_ORGANIZATION)
                || globalApp.ACTION_EDIT_ORGANIZATION.equals(form.getPreviousAction()) 
                || globalApp.ACTION_ADD_ORGANIZATION.equals(form.getPreviousAction())
                || currentAction.equals(globalApp.ACTION_DEFAULT))) {
                    
                form.resetValuesForPathList();
                
            }
            
            if ((currentAction.equals(globalApp.ACTION_DEFAULT) && form.
                    getActionElement().equals(ACTION_ORG_NODE_ID)) 
                    || (currentAction.equals(globalApp.ACTION_EDIT_ORGANIZATION) && form.
                    getActionElement().equals(ACTION_ORG_NODE_ID)) 
                    || (currentAction.equals(globalApp.ACTION_ADD_ORGANIZATION) && form.
                    getActionElement().equals(ACTION_ORG_NODE_ID))) {
                        
                  form.resetValuesForPathList();  
                
            }
            if (!(currentAction.equals(globalApp.ACTION_EDIT_ORGANIZATION) 
                || currentAction.equals(globalApp.ACTION_VIEW_ORGANIZATION) 
                || globalApp.ACTION_ADD_ORGANIZATION.equals(form.getPreviousAction()))) {
                    
                form.setSelectedOrgNodeId(null);
                
            }
            
        }
        if (actionElement.equals("setupOrgNodePath")) {
            String tempOrgNodeId = form.getCurrentAction();  
            
            if (tempOrgNodeId != null) {
                
                this.orgNodePath = setupHierarchy(new Integer(tempOrgNodeId), form);      
                orgNodeName = form.getOrgNodeName();
                orgNodeId = form.getOrgNodeId();   
                
            }
            if(thisAction != null){
                form.setCurrentAction(thisAction);
            }
        }
        
        int pageSize = FilterSortPageUtils.PAGESIZE_5;
                      
        FilterParams filter = null;
        SortParams sort = null;
        PageParams page = null;
        
        if ( currentAction.equals(globalApp.ACTION_EDIT_ORGANIZATION) ) {
            
            sort = FilterSortPageUtils.buildSortParams(form.getOrgListSortColumn(), 
                                form.getOrgListSortOrderBy(), null, null);
            page = FilterSortPageUtils.buildPageParams(form.getOrgPageRequested(),
                                pageSize);
        
        } else {
        
            sort = FilterSortPageUtils.buildSortParams(form.getOrgSortColumn(), 
                                form.getOrgSortOrderBy(), null, null);
            page = FilterSortPageUtils.buildPageParams(form.getOrgPageRequested(),
                                pageSize);
       
        }    
       
        NodeData und = OrgPathListUtils.getOrganizationNodes(this.userName, 
                                this.organizationManagement, orgNodeId, filter, page, sort);         
        if( form.getOrgPageRequested() == null ||  und.getFilteredPages() == null) {
            
            form.setOrgPageRequested(new Integer(1));
            
        }
        else if ( form.getOrgPageRequested().intValue() > und.getFilteredPages().intValue() ) {
            
            form.setOrgPageRequested(und.getFilteredPages());
        
        }
                
        List orgNodes = OrgPathListUtils.buildOrgNodeList(und);
        String orgCategoryName = OrgPathListUtils.getOrgCategoryName(orgNodes);
        
        PagerSummary orgPagerSummary = OrgPathListUtils.buildOrgNodePagerSummary(
                                            und, form.getOrgPageRequested());        
        form.setOrgMaxPage(und.getFilteredPages());

        PathNode node = OrgPathListUtils.findOrgNode(orgNodes, form.getSelectedOrgNodeId());
        
        if (node != null) {
        
            form.setSelectedOrgNodeId(node.getId());
            form.setSelectedOrgNodeName(node.getName());                
        
        }
      
        Integer selectedOrgNodeId = form.getSelectedOrgNodeId();
       
        if (selectedOrgNodeId == null ) {
       
            if (this.currentOrgNodeIds.length > 0) {
                
                form.setSelectedOrgNodeId(this.currentOrgNodeIds[0]);
                
            }
        } else {
            
            setCurrentSelectedOrgNodeId(selectedOrgNodeId);
            
        }

        // only setup permission if action = find
        if (this.globalApp.navPath.findAction(globalApp.ACTION_FIND_ORGANIZATION)) {
			
			if (form.getCurrentAction().equals(globalApp.ACTION_DEFAULT) 
                    && globalApp.ACTION_DEFAULT.equals(form.getPreviousAction()) && this.isFindOrg) {
                
                form.setSelectedOrgNodeId(null);
                this.isFindOrg = false;
                
            } 
            
            setupPermission(orgNodes, form);
        }
        
        if (globalApp.ACTION_EDIT_ORGANIZATION.equals(this.globalApp.navPath.getCurrentAction())) {

            setupPermission(orgNodes, form);

        }
        
        // compute selected orgnodes from pathlist
        this.selectedOrgNodes = OrgPathListUtils.buildSelectedOrgNodes(
                                        this.currentOrgNodesInPathList, 
                                        this.currentOrgNodeIds, 
                                        this.selectedOrgNodes);

        this.currentOrgNodeIds = OrgPathListUtils.retrieveCurrentOrgNodeIds(
                                        this.selectedOrgNodes);
        this.currentOrgNodesInPathList = OrgPathListUtils.buildOrgNodeHashMap(orgNodes);
        List selectableOrgNodes = OrgPathListUtils.buildSelectableOrgNodes(
                                            this.currentOrgNodesInPathList, 
                                            this.selectedOrgNodes);

        List orgNodesForSelector = buildOrgNodesForSelector(
                this.selectedOrgNodes, selectableOrgNodes, 
                form.getOrgSortOrderBy());
        
        //for delete organization
        if (selectedOrgNodeId == null
                    && globalApp.ACTION_DELETE_ORGANIZATION.equals(form.getPreviousAction())) {
            
            form.setSelectedOrgNodeId(null);  
            setupPermission(orgNodes, form);          
                  
        }
        
        if (this.isManageOrganization) {
            
            setupPermission(orgNodes, form);
            this.isManageOrganization = false;
            
        }
        
        if (this.isBack) {
            
            if (form.getSelectedOrgNodeId() == null) {
            
                 form.setSelectedOrgNodeId(this.currentSelectedOrgNodeId);
            
            } 
            else {
                
                if (form.getSelectedOrgNodeId().intValue() != 
                        this.currentSelectedOrgNodeId.intValue()) {
                    
                    form.setSelectedOrgNodeId(this.currentSelectedOrgNodeId);
                    
                }
            }
            setupPermission(orgNodes, form);
            this.isBack = false;
        }
        
        if ( this.isAddFromSideBar && this.isCancelFromAdd) {
        
            form.setSelectedOrgNodeId(null);   
            setupPermission(orgNodes, form); 
            if( this.currentOrgNodeIds.length != 0 && !this.isCancelFromAdd ) 
                form.setSelectedOrgNodeId(this.currentOrgNodeIds[0]);               
        }
        
        this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
        this.getRequest().setAttribute("orgNodes", orgNodes);        
        this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
        this.getRequest().setAttribute("orgCategoryName", orgCategoryName);   
		this.getRequest().setAttribute("selectedOrgNodes", this.selectedOrgNodes);
        this.getRequest().setAttribute("orgNodesForSelector", orgNodesForSelector);
       
        if (selectedOrgNodeId != null) {
         
            this.getRequest().setAttribute("disableButtons", "false");   
        
        } else {
        
            this.getRequest().setAttribute("disableButtons", "true");   
        
        }
        setFormInfoOnRequest(form);
        
    }




/////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////// private methods//////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////
	
    
    private void setupPermission(List orgNodes, ManageOrganizationForm form)
    {
         
         if (globalApp.ACTION_EDIT_ORGANIZATION.equals(globalApp.navPath.getCurrentAction())) {
             
            PermissionsUtils.setPermissionRequestAttributeORG(this.getRequest(), form, true);
            
        } else if (form.getSelectedOrgNodeId() != null) {    
    
            PathNode organization = OrgPathListUtils.getOrganization(
                                            form.getSelectedOrgNodeId(), orgNodes);
            if (organization != null) {
                
                form.setActionPermission(organization.getActionPermission());
                PermissionsUtils.setPermissionRequestAttributeORG(this.getRequest(), form, false);
                
            }
        } else {
         
            PermissionsUtils.setPermissionRequestAttributeORG(this.getRequest(), form, false);
            
        }        
    }
    	
	private void setCurrentSelectedOrgNodeId(Integer orgNodeId)
    {
        this.currentOrgNodeIds = new Integer[1];
        this.currentOrgNodeIds[0] = orgNodeId;
        
        if (orgNodeId != null) {
        
            for (int i = this.selectedOrgNodes.size()-1 ; i >= 0 ; i--) {
                
                PathNode node = (PathNode)this.selectedOrgNodes.get(i);
                if (node.getId().intValue() != orgNodeId.intValue()) {
        
                    this.selectedOrgNodes.remove(i);
        
                }
            }
        }        
    }

	private void setSelectedOrgNodes(String orgNodeName, Integer orgNodeId)
    {
        this.selectedOrgNodes = new ArrayList();
        if (orgNodeName != null & orgNodeId != null) {
            
            PathNode node = new PathNode();
            node.setId(orgNodeId);
            node.setName(orgNodeName);
            this.selectedOrgNodes.add(node);
            
        }
    }
    
    /**
     * initOrgLevelOption
     */
    private void initOrgLevelOption(String action,Integer orgId,Integer selectedParentId)
    {        
        this.orgLevelOptions = new LinkedHashMap();
        OrgNodeCategory[] orgCatagories = null;
        boolean addFlag = true;
       
        if (action.equals(this.globalApp.ACTION_ADD_ORGANIZATION)) {	
           
            this.orgLevelOptions.put("", Message.SELECT_ENTITY);
            
        }
        
        if (action.equals(this.globalApp.ACTION_EDIT_ORGANIZATION)) {
            
            addFlag = false;
        }
        
        try {
            orgCatagories = organizationManagement.
                                    getFrameworkListForOrg(orgId, 
                                    selectedParentId, addFlag);            
            if (orgCatagories != null) {
                
                for (int i = 0; i < orgCatagories.length ; i++) {
                    
                    this.orgLevelOptions.put(orgCatagories[i].getOrgNodeCategoryId(), 
                            orgCatagories[i].getCategoryName());
                }
            }
        }
        catch (Exception be) {
           be.printStackTrace();
        }       
    }


    /**
     * setOrganizationToForm
     */
    private void setOrganizationToForm(Integer orgId, ManageOrganizationForm form, String action)
    {
        // Retrive Organization details by passing orgId
        try {
            Node node = organizationManagement.getOrganization(this.userName, orgId);
            Organization org = new Organization(node);
			form.setOrganizationDetail(org);
            String loginUserRole = OrgFormUtils.getLoginUserRole(this.userName, this.roles , form);
            
            // Changed for defect # 50858; View -> Add node under top of admin
            if(action.equals("VIEW")){
                if((loginUserRole.equals("Administrator") && org.getOrgParentId().intValue() <=2)
                        || (loginUserRole.equals("Account Manager") && org.getOrgParentId().intValue() <2)){
                    form.setSelectedOrgNodeId(new Integer(0));
                    form.setSelectedOrgNodeName("Top");
                }
                else{
                    form.setSelectedOrgNodeId(org.getOrgParentId());
                    form.setSelectedOrgNodeName(org.getOrgParent());
                }
            }
            else{
                form.setSelectedOrgNodeId(org.getOrgParentId());
                form.setSelectedOrgNodeName(org.getOrgParent());
            }
           
            
            form.setSelectedOrgName(org.getOrgName());
            form.setSelectedOrgNodeCode(org.getOrgCode());
            form.setSelectedOrgNodeType(org.getOrgType());
            form.setSelectedOrgNodeTypeId(org.getOrgTypeId());
            
            // end of change
            
            if (!globalApp.ACTION_ADD_ORGANIZATION.equals(form.getPreviousAction())) {
               
                form.setSelectedOrgChildNodeId(org.getOrgId());
            
            } else {
              
                form.setTempOrgChildNodeId(org.getOrgId());
            
            }
            form.setPreviousParentId(org.getOrgParentId());
            form.setPreviousParentName(org.getOrgParent());
            form.setActionPermission(org.getActionPermission());
        } 
        catch (Exception be) {
            be.printStackTrace();
            String msg = MessageResourceBundle.getMessage(be.getMessage());
            form.setMessage(Message.MANAGE_ORG_ERROR, msg, Message.ERROR);  
        }
    }
    
    
    
    
    /**
     * initHierarchy
     */
    private void initHierarchy(ManageOrganizationForm form)
    {   
        this.orgNodePath = new ArrayList();
        form.setOrgNodeName("Top");
        form.setOrgNodeId(new Integer(0));
        form.setSelectedOrgNodeId(null);
        form.setSelectedOrgNodeName(null);
    }

    /**
     * setupHierarchy
     */
    private List setupHierarchy(Integer orgNodeId, ManageOrganizationForm form)
    {
        List orgNodePath = new ArrayList();
        List nodeAncestors = new ArrayList();
        
        Node[] orgNodes = OrgPathListUtils.
                                    getAncestorOrganizationNodesForOrgNode(
                                    this.userName, orgNodeId, this.organizationManagement);

        for (int i = 0 ; i < (orgNodes.length - 1) ; i++) {
            
            Node orgNode = (Node)orgNodes[i];
            Integer orgId = orgNode.getOrgNodeId();
            String orgName = orgNode.getOrgNodeName();                
            if (orgId.intValue() >= 2) {    // ignore Root
                
                PathNode node = new PathNode();
                node.setId(orgId);
                node.setName(orgName);
                nodeAncestors.add(node);   
                if (form != null) {
                
                    form.setOrgNodeId(orgId);
                    form.setOrgNodeName(orgName);
                    form.resetValuesForPathList();
                
                }             
            }
        }    

        if (nodeAncestors.size() == 0) {
            
            PathNode node = new PathNode();
            node.setId(new Integer(0));
            node.setName("Top");
            nodeAncestors.add(node);   
            if (form != null) {
            
                form.setOrgNodeId(new Integer(0));
                form.setOrgNodeName("Top");
                form.resetValuesForPathList();
            
            }             
        }
        
        orgNodePath = OrgPathListUtils.setupOrgNodePath(nodeAncestors);       
        
        return orgNodePath;
    }


     /**
     * setFullPathNodeName
     */
    private void setFullPathNodeName(List orgNodes)    
    {
        for (int i=0 ; i<orgNodes.size() ; i++) {
            
            PathNode node = (PathNode)orgNodes.get(i);
            Integer orgNodeId = node.getId();
            String fullPathNodeName = OrgPathListUtils.
                                            getFullPathNodeName(this.userName, 
                                            orgNodeId, this.organizationManagement);	   
            node.setFullPathName(fullPathNodeName);            
        }    
    }

     /**
     * buildOrgNodesForSelector
     */
    private List buildOrgNodesForSelector(List selectedOrgNodes, 
                                          List selectableOrgNodes, 
                                          String sortOrderBy)    
    {
        
        List orgNodesForSelector = new ArrayList();

        for (int i=0 ; i<selectedOrgNodes.size() ; i++) {
            
            PathNode node = (PathNode)selectedOrgNodes.get(i);
            node.setSelectable("false");
            orgNodesForSelector.add(node);
            
        }
        
        for (int i=0 ; i<selectableOrgNodes.size() ; i++) {
            
            PathNode node = (PathNode)selectableOrgNodes.get(i);
            node.setSelectable("true");
            orgNodesForSelector.add(node);
            
        }
                
        setFullPathNodeName(orgNodesForSelector);    
        
        OrgNodeUtils.sortList(orgNodesForSelector, sortOrderBy); 
        
        return orgNodesForSelector; 
    }


     /**
     * initialize
     */
    private ManageOrganizationForm initialize(String action)
    {                
        this.orgNodePath = new ArrayList();
        this.currentOrgNodesInPathList = new HashMap();
        setSelectedOrgNodes(null, null);
        setCurrentSelectedOrgNodeId(null);
        this.savedForm = new ManageOrganizationForm();
        this.savedForm.init();
        this.savedForm.setCurrentAction(action);
        this.globalApp.navPath = new NavigationPath();
        this.globalApp.navPath.reset(globalApp.ACTION_FIND_ORGANIZATION);
        initOrgLevelOption(action,this.parentOrgNodeIdForAdd,null);
        this.getSession().setAttribute("userHasReports", userHasReports());
        
        return this.savedForm;
    }
    
    
     /**
     * userHasReports
     */
    private Boolean userHasReports() 
    {
        Boolean hasReports = Boolean.FALSE;
        try {   
            hasReports = this.organizationManagement.userHasReports(this.userName);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return hasReports;
    }

    /**
     * getUserDetails
     */
    private void getUserDetails()
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
        if (principal != null) {
        
            this.userName = principal.toString();
        
        } else {           
       
            this.userName = (String)getSession().getAttribute("userName");
      
        }
        try {
            this.user = this.userManagement.getUser(this.userName, 
                                                this.userName);
       //     this.displayNewMessage = user.getDisplayNewMessage();                                                
        }
        catch (Exception e) {
            e.printStackTrace();
        } 
        getSession().setAttribute("userName", this.userName);
    }
    
    /**
     * getParentOrgNode
     */
    private Node getParentOrgNode(Integer orgNodeId, ManageOrganizationForm form) {
        
        Node orgNodeOfParent = null;
        try {
           orgNodeOfParent = organizationManagement.getParentOrgNode(orgNodeId); 
        } 
        catch (CTBBusinessException be) {
            be.printStackTrace();
            String msg = MessageResourceBundle.getMessage(be.getMessage());
            form.setMessage(Message.MANAGE_ORG_ERROR, msg, Message.ERROR);  
        } 
        return orgNodeOfParent; 
        
    }
    
    
    /**
     * saveOrgInformation
     */
    private boolean saveOrgInformation(boolean isCreateNew, Node nodeDetail,
                                        ManageOrganizationForm form) {     
        String title = ""; 
        
        try {  
                           
            if (isCreateNew) {
                
                this.organizationManagement.createOrganization(this.userName, nodeDetail);
                
            } else { 
                
                this.organizationManagement.updateOrganization(this.userName, nodeDetail);
            }    
        } catch (Exception be) {
            
            String msg = MessageResourceBundle.getMessage(be.getMessage());  
            if ( isCreateNew ) {
                
                title = Message.ADD_ERROR;
            
            } else {
                
                 title = Message.EDIT_ERROR;
                 
            }                                     
            form.setMessage(title, msg, Message.ERROR);
            return false;
        }                       
        return true;
    }
    
    /**
     * this method holds the previousList
     */
    private List createCloneList (List orgNodeList) {
        List previousOrgNodeList = new ArrayList ();
        for (int i = 0; i < orgNodeList.size(); i++) {
            
            previousOrgNodeList.add((PathListEntry)orgNodeList.get(i));
            
        }
        return previousOrgNodeList;
    }
    
    /**
     * buildPageTitle
     */
    private String buildPageTitle(String action, ManageOrganizationForm form)
    {
        String title = "";
        Organization organization = null;
        
        if (action.equals(globalApp.ACTION_FIND_ORGANIZATION)) {
            
            title = Message.FIND_ORG_TITLE;
            
        } else if (action.equals(globalApp.ACTION_ADD_ORGANIZATION)) {
            
            title = Message.ADD_TITLE;
            webTitle = Message.ADD_TITLE_WEB;
            
        }
        else if (action.equals(globalApp.ACTION_VIEW_ORGANIZATION)) {
            
            title = Message.VIEW_ORG_TITLE;
            organization = form.getOrganizationDetail();   
            
            if (organization != null) {
           
                title =  Message.buildPageTitle(title, organization.getOrgName());
        
            }  
                  
        }
        else if (action.equals(globalApp.ACTION_EDIT_ORGANIZATION)) {
            
            title = Message.EDIT_TITLE;
            webTitle = Message.EDIT_TITLE_WEB;
            String oldOrgName = (String)this.getRequest().getSession().getAttribute("organizationName");
                                        
            if (oldOrgName!=null && oldOrgName.length()>0) {
                 
                 title =  Message.buildPageTitle(title, oldOrgName);
            
            }
            else {
            
                title =  Message.buildPageTitle(title, form.getSelectedOrgName());
            
            }
        }
       
        
        return title;            
    }    
    
    
    /**
     * deleteOrganization
     */
    
    private void deleteOrganization(String userName, Node deleteNode, 
                                    ManageOrganizationForm form) {
        try{
            this.organizationManagement.deleteOrganization(this.userName, deleteNode); 
        } catch (CTBBusinessException be) {
            be.printStackTrace();
            String msg = MessageResourceBundle.getMessage(be.getMessage());                                        
            form.setMessage(Message.DELETE_ERROR_UNKNOWN, msg, Message.ERROR);
        }
    } 
    

    /**
     * isInvalidParent
     */
    
    private boolean isInvalidParent() {
        boolean invalidParent = false;
        
        if(this.orgLevelOptions == null || this.orgLevelOptions.size()==0){
            invalidParent = true;
        }
        else if(this.orgLevelOptions.size()== 1 && this.orgLevelOptions.containsKey("")){
            invalidParent = true;
        }
         
        return invalidParent; 
    } 
    
    private void setFormInfoOnRequest(ManageOrganizationForm form) {
    	this.getRequest().setAttribute("pageMessage", form.getMessage());
    	this.getRequest().setAttribute("formData", form.getOrgPageRequested());
    }


/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** ManageOrganizationForm ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    

    

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ManageOrganizationForm extends SanitizedFormData
    {
        private String actionElement;
        private String currentAction;
        private String previousAction;
        
        private String orgNodeName;
        private Integer orgNodeId;
        
        // textbox
        private Integer selectedOrgNodeId;
        private Integer tempOrgChildNodeId;
        private Integer selectedOrgChildNodeId;
        private String selectedOrgNodeName;
		private String selectedOrgName;
        private String selectedOrgNodeCode;
        private String selectedOrgNodeType;
        private String selectedOrgNodeTypeId;
        private String selectedOrgNodeParent;
        private Integer previousParentId;
        private String previousParentName;
        
        // org pager
        private String orgSortColumn;
        private String orgSortOrderBy;
        private Integer orgPageRequested;
        private Integer orgMaxPage;


        // org list
        private String orgListSortColumn;
        private String orgListSortOrderBy;
        private Integer orgListPageRequested;
        private Integer orgListMaxPage;
        
        private Message message;
        // org list
        private Organization organizationDetail;
          
        //orgPermission
        
        private String actionPermission; 
        

        
        public ManageOrganizationForm()
        {
        }
        
        public void init()
        {
            this.actionElement = global.Global.ACTION_DEFAULT;
            this.currentAction = global.Global.ACTION_DEFAULT;
            this.previousAction = global.Global.ACTION_DEFAULT;
            this.orgNodeName = "Top";
            this.orgNodeId = new Integer(0);
            this.actionPermission =PermissionsUtils.VIEW_ADD_PERMISSION_TOKEN;
            this.selectedOrgNodeId = null;
            this.tempOrgChildNodeId = null;
            this.selectedOrgChildNodeId = null;
            this.previousParentId = null;
            this.selectedOrgNodeName = null;
			this.selectedOrgName = null;
            this.previousParentName = null;
            this.selectedOrgNodeCode = null;
            this.selectedOrgNodeType = null;
            this.selectedOrgNodeTypeId = null;
            this.selectedOrgNodeParent = null;
            
            this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
            this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.orgPageRequested = new Integer(1);                
            this.orgMaxPage = new Integer(1);      
            this.orgMaxPage = new Integer (1);
            this.orgListSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
            this.orgListSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.orgListPageRequested = new Integer(1);                
            this.orgListMaxPage = new Integer(1);    
            
            this.message = null;  
        }   

        public ManageOrganizationForm createClone()
        {   
            ManageOrganizationForm copied = new ManageOrganizationForm();

            copied.setActionElement(this.actionElement);
            copied.setCurrentAction(this.currentAction);
            copied.setPreviousAction(this.previousAction);
            copied.setOrgNodeName(this.orgNodeName);
            copied.setOrgNodeId(this.orgNodeId);
            
            copied.setSelectedOrgNodeId(this.selectedOrgNodeId);
            copied.setTempOrgChildNodeId(this.tempOrgChildNodeId);
            copied.setSelectedOrgChildNodeId(this.selectedOrgChildNodeId);
            copied.setSelectedOrgNodeName(this.selectedOrgNodeName);
			copied.setSelectedOrgName(this.selectedOrgName);
            copied.setSelectedOrgNodeCode(this.selectedOrgNodeCode);
            copied.setSelectedOrgNodeType(this.selectedOrgNodeType);
            copied.setSelectedOrgNodeParent(this.selectedOrgNodeParent);
            
            copied.setOrgSortColumn(this.orgSortColumn);
            copied.setOrgSortOrderBy(this.orgSortOrderBy);
            copied.setOrgPageRequested(this.orgPageRequested);
            copied.setOrgMaxPage(this.orgMaxPage);
            copied.setOrgListSortColumn(this.orgListSortColumn);
            copied.setOrgListSortOrderBy(this.orgListSortOrderBy);
            copied.setOrgListPageRequested(this.orgListPageRequested);
            copied.setOrgListMaxPage(this.orgListMaxPage);
            
            copied.setActionPermission(this.actionPermission);           
            
            copied.setMessage(this.message);
            
            return copied;
            
        }
        
        public void clearSearch()
        {   
        }
        
        public void validateValues()
        {
            if (this.orgSortColumn == null) {
             
                this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
            
            }

            if (this.orgSortOrderBy == null) {
                
                this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;
            
            }

            if (this.orgPageRequested == null) {
                
                this.orgPageRequested = new Integer(1);
                
            }
                
            if (this.orgPageRequested.intValue() <= 0) {
                
                this.orgPageRequested = new Integer(1);
                this.selectedOrgNodeId = null;
                
            }
        
            if (this.orgMaxPage == null) {
                
                this.orgMaxPage = new Integer(1);
            
            }

            if (this.orgPageRequested.intValue() > this.orgMaxPage.intValue()) {
                
                this.orgPageRequested = new Integer(this.orgMaxPage.intValue());
                this.selectedOrgNodeId = null;
                
            }

        }     
        
        
        public void resetValuesForAction(String actionElement) 
        {
            if (actionElement.equals("{actionForm.orgPageRequested}")) {
                
                this.selectedOrgNodeId = null;
                
            }
            if (actionElement.equals("{actionForm.orgSortOrderBy}")) {
                
                this.orgPageRequested = new Integer(1);
                this.selectedOrgNodeId = null;
                
            }
        }
        
        
        public void resetValuesForPathList()
        {
            this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
            this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.orgPageRequested = new Integer(1);    
            this.orgMaxPage = new Integer(1);      
        }     
        
        public void setActionElement(String actionElement)
        {
            this.actionElement = actionElement;
        } 
               
        public String getActionElement()
        {
            return this.actionElement != null ? this.actionElement : global.Global.ACTION_DEFAULT;
        }
        
        public void setCurrentAction(String currentAction)
        {
            this.currentAction = currentAction;
        }
        
        public String getCurrentAction()
        {
            return this.currentAction != null ? this.currentAction : global.Global.ACTION_DEFAULT;
        }
        /**
         * @return Returns the previousAction.
         */
        public String getPreviousAction() {
            return this.previousAction != null ? this.previousAction : global.Global.ACTION_DEFAULT;
        }

        /**
         * @param previousAction The previousAction to set.
         */
        public void setPreviousAction(String previousAction) {
            this.previousAction = previousAction;
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
        /**
         * @return Returns the tempOrgChildNodeId.
         */
        public Integer getTempOrgChildNodeId() {
            return tempOrgChildNodeId;
        }
    
        /**
         * @param tempOrgChildNodeId The tempOrgChildNodeId to set.
         */
        public void setTempOrgChildNodeId(Integer tempOrgChildNodeId) {
            this.tempOrgChildNodeId = tempOrgChildNodeId;
        }

        /**
        * @return Returns the selectedOrgChildNodeId.
        */
        public Integer getSelectedOrgChildNodeId() {
            return selectedOrgChildNodeId;
        }

        /**
         * @param selectedOrgChildNodeId The selectedOrgChildNodeId to set.
         */
        public void setSelectedOrgChildNodeId(Integer selectedOrgChildNodeId) {
            this.selectedOrgChildNodeId = selectedOrgChildNodeId;
        }
        
        /**
         * @return Returns the previousParentId.
         */
        public Integer getPreviousParentId() {
            return previousParentId;
        }
    
        /**
         * @param previousParentId The previousParentId to set.
         */
        public void setPreviousParentId(Integer previousParentId) {
            this.previousParentId = previousParentId;
        }
        
        public void setSelectedOrgNodeName(String selectedOrgNodeName)
        {
            this.selectedOrgNodeName = selectedOrgNodeName;
        }
        
        public String getSelectedOrgNodeName()
        {
            return this.selectedOrgNodeName;
        }        
        
		/**
         * @return Returns the selectedOrgName.
         */
        public String getSelectedOrgName() {
            return selectedOrgName;
        }
    
        /**
         * @param selectedOrgName The selectedOrgName to set.
         */
        public void setSelectedOrgName(String selectedOrgName) {
            this.selectedOrgName = selectedOrgName;
        }  
        
        /**
         * @return Returns the previousParentName.
         */
        public String getPreviousParentName() {
            return previousParentName;
        }
    
        /**
         * @param previousParentName The previousParentName to set.
         */
        public void setPreviousParentName(String previousParentName) {
            this.previousParentName = previousParentName;
        }
        
        public void setSelectedOrgNodeCode(String selectedOrgNodeCode)
        {
            this.selectedOrgNodeCode = selectedOrgNodeCode;
        }
        
        public String getSelectedOrgNodeCode()
        {
            return this.selectedOrgNodeCode;
        }
        
        public void setSelectedOrgNodeType(String selectedOrgNodeType)
        {
            this.selectedOrgNodeType = selectedOrgNodeType;
        }
        
        public String getSelectedOrgNodeType()
        {
            return this.selectedOrgNodeType;
        }
        
        public String getSelectedOrgNodeTypeId() {
            return selectedOrgNodeTypeId;
        }

        public void setSelectedOrgNodeTypeId(String selectedOrgNodeTypeId) {
            this.selectedOrgNodeTypeId = selectedOrgNodeTypeId;
        }
        
        public void setSelectedOrgNodeParent(String selectedOrgNodeParent)
        {
            this.selectedOrgNodeParent = selectedOrgNodeParent;
        }
        
        public String getSelectedOrgNodeParent()
        {
            return this.selectedOrgNodeParent;
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
        public void setOrgListSortColumn(String orgListSortColumn)
        {
            this.orgListSortColumn = orgListSortColumn;
        }
        
        public String getOrgListSortColumn()
        {
            return this.orgListSortColumn != null ? this.orgListSortColumn : FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
        }   
            
        public void setOrgListSortOrderBy(String orgListSortOrderBy)
        {
            this.orgListSortOrderBy = orgListSortOrderBy;
        }
        
        public String getOrgListSortOrderBy()
        {
            return this.orgListSortOrderBy != null ? this.orgListSortOrderBy : FilterSortPageUtils.ASCENDING;
        } 
              
        public void setOrgListPageRequested(Integer orgListPageRequested)
        {
            this.orgListPageRequested = orgListPageRequested;
        }
        
        public Integer getOrgListPageRequested()
        {
            return this.orgListPageRequested != null ? this.orgListPageRequested : new Integer(1);
        }   
             
        public void setOrgListMaxPage(Integer orgListMaxPage)
        {
            this.orgListMaxPage = orgListMaxPage;
        }
        
        public Integer getOrgListMaxPage()
        {
            return this.orgListMaxPage != null ? this.orgListMaxPage : new Integer(1);
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
        
         // Organization Detail
        public void setOrganizationDetail(Organization organizationDetail)
        {
            this.organizationDetail = organizationDetail;
        }
        
        public Organization getOrganizationDetail()
        {
            if (this.organizationDetail == null) {
                this.organizationDetail = new Organization();
            }
            return this.organizationDetail;
        }
        /**
         * @return Returns the actionPermission.
         */
        public String getActionPermission() {
            return actionPermission;
        }
    
        /**
         * @param actionPermission The actionPermission to set.
         */
        public void setActionPermission(String actionPermission) {
            this.actionPermission = actionPermission;
        }        
    }


	public String getPageTitle() {
		return pageTitle;
	}

	public LinkedHashMap getOrgLevelOptions() {
		return orgLevelOptions;
	}

	//Bulk Accommodation
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
                    this.getSession().setAttribute("isBulkAccommodationConfigured", true);
                    break;
                } 
            }
        }
        catch (SQLException se) {
        	se.printStackTrace();
		}
        
       
        return new Boolean(hasBulkStudentConfigurable);
    }
       
}
