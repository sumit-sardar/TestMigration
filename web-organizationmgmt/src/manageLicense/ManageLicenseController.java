package manageLicense;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.OrgNodeCategory;
import com.ctb.bean.testAdmin.OrgNodeLicenseInfo;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.CTBConstants;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.PagerSummary;
import com.ctb.widgets.bean.PathListEntry;
import dto.LicenseNode;
import dto.Message;
import dto.NavigationPath;
import dto.Organization;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import utils.FilterSortPageUtils;
import utils.MessageResourceBundle;
import utils.OrgFormUtils;
import utils.OrgNodeUtils;
import utils.OrgPathListUtils;
import utils.PermissionsUtils;


/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class ManageLicenseController extends PageFlowController
{
    static final long serialVersionUID = 1L;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.organizationManagement.OrganizationManagement organizationManagement;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.licensing.Licensing licensing;

    @Control()
    private com.ctb.control.db.Users users;
    
    @Control()
    private com.ctb.control.userManagement.UserManagement userManagement;
    

    private String userName = null;
    private CustomerLicense[] customerLicenses = null;
    private List orgNodePath = null;

    // navigation
    private static final String ACTION_CURRENT = "{actionForm.currentAction}";
    private static final String ACTION_ORG_NODE_ID = "{actionForm.orgNodeId}";
    private static final String ACTION_ORG_SORT_ORDER_BY ="{actionForm.orgSortOrderBy}";
    private static final String ACTION_ORG_PAGE_REQUESTED = "{actionForm.orgPageRequested}";
    private static final String ACTION_ELEMENT = "{actionForm.actionElement}";
    private static final String BUTTON_GO_INVOKED = "ButtonGoInvoked_tablePathListAnchor";
    private static final String SETUP_ORG_NODE_PATH = "setupOrgNodePath";
    private static final String ACTION_ORG_LIST_SORT_ORDER_BY ="{actionForm.orgListSortOrderBy}";
    
    private User user = null;
    
    public String[] productNameOptions = null;
    public String productName = null;
    

    /**
     * @jpf:action
     * @jpf:forward name="success" path="manageLicense.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "manageLicense.do")
    })
    protected Forward begin()
    {                
        getUserDetails();
        //Bulk Accommodation Changes
        customerHasBulkAccommodation();
        
        customerHasScoring();//For hand scoring changes
        ManageLicenseForm form = initialize();
        
        this.customerLicenses = getCustomerLicenses();
                
        initHierarchy(form);           
             
        return new Forward("success", form);        
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="manage_license.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "manage_license.jsp")
		}
	)
    protected Forward manageLicense(ManageLicenseForm form)
    {        
        handleOrganizationControl(form);
        
        return new Forward("success", form);
    }

	 
/////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////// private methods//////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * handleOrganizationControl
     */
    private void handleOrganizationControl(ManageLicenseForm form) {
        String actionElement = form.getActionElement();
        String currentAction = form.getCurrentAction();
        
        String orgNodeName = form.getOrgNodeName();
        Integer orgNodeId = form.getOrgNodeId(); 
        
        form.resetValuesForAction(actionElement); 
                        
        boolean nodeChanged = OrgPathListUtils.adjustOrgNodePath(this.orgNodePath, orgNodeId, orgNodeName);

        if (nodeChanged) {             
            form.resetValuesForPathList();
        }
        
        if (actionElement.equals("setupOrgNodePath")) {
            String tempOrgNodeId = form.getCurrentAction();  
            
            if (tempOrgNodeId != null) {
                
                this.orgNodePath = setupHierarchy(new Integer(tempOrgNodeId), form);      
                orgNodeName = form.getOrgNodeName();
                orgNodeId = form.getOrgNodeId();   
                
            }
        }
        
        int pageSize = FilterSortPageUtils.PAGESIZE_5;
                      
        FilterParams filter = null;
        SortParams sort = null;
        PageParams page = null;
        
        sort = FilterSortPageUtils.buildSortParams(form.getOrgSortColumn(), 
                                form.getOrgSortOrderBy(), null, null);
        page = FilterSortPageUtils.buildPageParams(form.getOrgPageRequested(),
                                pageSize);
           
        NodeData und = OrgPathListUtils.getOrganizationNodes(this.userName, 
                                this.organizationManagement, orgNodeId, filter, page, sort);         
        if( form.getOrgPageRequested() == null ||  und.getFilteredPages() == null) {
            
            form.setOrgPageRequested(new Integer(1));
            
        }
        else if ( form.getOrgPageRequested().intValue() > und.getFilteredPages().intValue() ) {
            
            form.setOrgPageRequested(und.getFilteredPages());
        
        }
                
        List orgNodes = buildOrgNodeList(und);
        String orgCategoryName = getOrgCategoryName(orgNodes);
        
        PagerSummary orgPagerSummary = OrgPathListUtils.buildOrgNodePagerSummary(
                                            und, form.getOrgPageRequested());        
        form.setOrgMaxPage(und.getFilteredPages());

        this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
        this.getRequest().setAttribute("orgNodes", orgNodes);        
        this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
        this.getRequest().setAttribute("orgCategoryName", orgCategoryName);   

        this.getRequest().setAttribute("multipleProducts", new Boolean(this.customerLicenses.length > 1));                  
    }

    /**
     * buildOrgNodeList
     */    
    private List buildOrgNodeList(NodeData und) {
        ArrayList nodeList = new ArrayList();
        if ( und != null ) {  
                              
            LicenseNode licenseNode = null;
            Node[] nodes = und.getNodes();     
            
            for (int i = 0 ; i < nodes.length ; i++) {
                
                Node node = nodes[i];
                if ( node != null ) {
                    
                    OrgNodeLicenseInfo onli = getLicenseQuantitiesByOrg(node.getOrgNodeId());
                    
                    if (onli != null) {                              
                        licenseNode = new LicenseNode();
                        licenseNode.setName(node.getOrgNodeName());
                        licenseNode.setId(node.getOrgNodeId());   
                        licenseNode.setCategoryName(node.getOrgNodeCategoryName());
                        licenseNode.setChildrenNodeCount(node.getChildNodeCount());

                        licenseNode.setReserved(licenseNode.ConvertIntegerToString(onli.getLicReserved()));
                        licenseNode.setConsumed(licenseNode.ConvertIntegerToString(onli.getLicUsed()));
                        licenseNode.setAvailable(licenseNode.ConvertIntegerToString(onli.getLicPurchased()));
                                            
                        nodeList.add(licenseNode);
                    }
                }
            }
        }
        return nodeList;
    }

    /**
     * getLicenseQuantitiesByOrg
     */    
    private OrgNodeLicenseInfo getLicenseQuantitiesByOrg(Integer orgNodeId) {
        OrgNodeLicenseInfo onli = null;
        
        CustomerLicense cl = getCustomerLicenseByProduct(this.productName);
        
        try {
            onli = this.licensing.getLicenseQuantitiesByOrg(this.userName, 
                                                            orgNodeId, 
                                                            cl.getProductId(), 
                                                            cl.getSubtestModel());
        }    
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return onli;
    }

    /**
     * getOrgCategoryName
     */
    public static String getOrgCategoryName(List nodeList) {
        String categoryName = "Organization";        
        if (nodeList.size() > 0) {
            LicenseNode node = (LicenseNode)nodeList.get(0);
            categoryName = node.getCategoryName();            
            for (int i=1 ; i<nodeList.size() ; i++) {
                node = (LicenseNode)nodeList.get(i);
                if (! node.getCategoryName().equals(categoryName)) {
                    categoryName = "Organization";
                    break;
                }
            }
        }
        return categoryName;
    }


    /**
     * initHierarchy
     */
    private void initHierarchy(ManageLicenseForm form)
    {   
        this.orgNodePath = new ArrayList();
        form.setOrgNodeName("Top");
        form.setOrgNodeId(new Integer(0));
    }

    /**
     * setupHierarchy
     */
    private List setupHierarchy(Integer orgNodeId, ManageLicenseForm form)
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
                
                LicenseNode node = new LicenseNode();
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
            
            LicenseNode node = new LicenseNode();
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
     * initialize
     */
    private ManageLicenseForm initialize()
    {                
        this.orgNodePath = new ArrayList();
        ManageLicenseForm form = new ManageLicenseForm();
        form.init();
        form.setCurrentAction(ACTION_CURRENT);
        this.getSession().setAttribute("userHasReports", userHasReports());
        
        return form;
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
     * getCustomerLicenses
     */
    private CustomerLicense[] getCustomerLicenses()
    {
        CustomerLicense[] cls = null;
        CustomerLicense cl = null;
        
        try {
            cls = this.licensing.getCustomerLicenseData(this.userName, null);
            
            if ((cls != null) && (cls.length > 0)) {
                
                this.productNameOptions = new String[cls.length + 1];
        
                for (int i=(cls.length-1) ; i>=0 ; i--) {
                    cl = (CustomerLicense)cls[i];
                    this.productName = cl.getProductName();
                    this.productNameOptions[i] = cl.getProductName();
                }
            }            
        }    
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return cls;        
    }

    /**
     * getCustomerLicenseByProduct
     */
    private CustomerLicense getCustomerLicenseByProduct(String productName)
    {
        for (int i=0 ; i<this.customerLicenses.length ; i++) {
            CustomerLicense license = (CustomerLicense)this.customerLicenses[i];
            if (license.getProductName().equals(productName)) {
                return license;
            }
        }
        return this.customerLicenses[0];        
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
    
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** ManageLicenseForm ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ManageLicenseForm extends SanitizedFormData
    {
        private String actionElement;
        private String currentAction;
        
        private String orgNodeName;
        private Integer orgNodeId;
        
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

        
        public ManageLicenseForm()
        {
        }
        
        public void init()
        {
            this.actionElement = global.Global.ACTION_DEFAULT;
            this.currentAction = global.Global.ACTION_DEFAULT;
            this.orgNodeName = "Top";
            this.orgNodeId = new Integer(0);
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

        public ManageLicenseForm createClone()
        {   
            ManageLicenseForm copied = new ManageLicenseForm();

            copied.setActionElement(this.actionElement);
            copied.setCurrentAction(this.currentAction);
            copied.setOrgNodeName(this.orgNodeName);
            copied.setOrgNodeId(this.orgNodeId);
            copied.setOrgSortColumn(this.orgSortColumn);
            copied.setOrgSortOrderBy(this.orgSortOrderBy);
            copied.setOrgPageRequested(this.orgPageRequested);
            copied.setOrgMaxPage(this.orgMaxPage);
            copied.setOrgListSortColumn(this.orgListSortColumn);
            copied.setOrgListSortOrderBy(this.orgListSortOrderBy);
            copied.setOrgListPageRequested(this.orgListPageRequested);
            copied.setOrgListMaxPage(this.orgListMaxPage);
            
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
            }
        
            if (this.orgMaxPage == null) {
                
                this.orgMaxPage = new Integer(1);
            
            }

            if (this.orgPageRequested.intValue() > this.orgMaxPage.intValue()) {
                
                this.orgPageRequested = new Integer(this.orgMaxPage.intValue());
            }

        }     
        
        
        public void resetValuesForAction(String actionElement) 
        {
            if (actionElement.equals("{actionForm.orgSortOrderBy}")) {
                
                this.orgPageRequested = new Integer(1);
                
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
        
    }

	public String[] getProductNameOptions() {
		return productNameOptions;
	}
  
	/*
     * Bulk accommodation
     */
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
	

//changes for scoring
	
	/**
	 * This method checks whether customer is configured to access the scoring feature or not.
	 * @return Return Boolean 
	 */
	private Boolean customerHasScoring()
    {               
		Integer customerId = this.user.getCustomer().getCustomerId();
        boolean hasScoringConfigurable = false;
        
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
                break;
            } 
        }
       }
        
        catch (SQLException se) {
        	se.printStackTrace();
		}
       
        return new Boolean(hasScoringConfigurable);
    }
    
       
}
