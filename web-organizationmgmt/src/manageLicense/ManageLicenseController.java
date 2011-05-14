package manageLicense;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.FilterSortPageUtils;
import utils.OrgPathListUtils;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.LicenseNodeData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.OrgNodeLicenseInfo;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.PagerSummary;

import dto.LicenseNode;
import dto.Message;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
    
    private List licenseNodeList = null;
    private String availablePool = "0";
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
        initLicenseData();
        
        return new Forward("success", form);        
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="viewLicense.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "viewLicense.do")
    })
    protected Forward beginViewLicense()
    {                
        getUserDetails();
        //Bulk Accommodation Changes
        customerHasBulkAccommodation();
        
        customerHasScoring();//For hand scoring changes
        ManageLicenseForm form = initialize();
        
        this.customerLicenses = getCustomerLicenses();
                
        initHierarchy(form);           
        initLicenseData();
             
        return new Forward("success", form);        
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="manageLicense.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "manageLicense.do")
    })
    protected Forward beginManageLicense()
    {                
        getUserDetails();
        //Bulk Accommodation Changes
        customerHasBulkAccommodation();
        
        customerHasScoring();//For hand scoring changes
        ManageLicenseForm form = initialize();
        
        this.customerLicenses = getCustomerLicenses();
                
        initHierarchy(form);           
        initLicenseData();
        
        return new Forward("success", form);        
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="view_license.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "view_license.jsp")
		}
	)
    protected Forward viewLicense(ManageLicenseForm form)
    {        
        handleOrganizationControl(form);
        
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
        String currentAction = form.getCurrentAction();
        if (currentAction.equals("changeProduct")) {
            saveLicenses(form);       
            form = initialize();
            initHierarchy(form);           
            initLicenseData();
        }
        
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
        
        getLicenseValuesInForm(form);
        
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
        
        populateLicenseNodes(orgNodes);
        
        setLicenseValuesToForm(orgNodeId, orgNodes, form);

        String orgCategoryName = getOrgCategoryName(orgNodes);
        
        PagerSummary orgPagerSummary = OrgPathListUtils.buildOrgNodePagerSummary(
                                            und, form.getOrgPageRequested());        
        form.setOrgMaxPage(und.getFilteredPages());

        form.setTopNodeEditing(new Boolean((orgNodeId.intValue() == 0) && (orgNodes.size() > 1)));

        this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
        this.getRequest().setAttribute("orgNodes", orgNodes);        
        this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
        this.getRequest().setAttribute("orgCategoryName", orgCategoryName);   

        this.getRequest().setAttribute("multipleProducts", new Boolean(this.customerLicenses.length > 1));       
        
        this.getRequest().setAttribute("rootNode", new Boolean(orgNodeId.intValue() == 0));                  
        this.getRequest().setAttribute("singleRootNode", new Boolean(orgNodes.size() == 1));                  
        
    }

    /**
     * getLicenseValuesInForm
     */    
    private void getLicenseValuesInForm(ManageLicenseForm form) {
    	
    	Integer parentNodeId = form.getParentNodeId();
    	String parentNodeAvailable = form.getParentNodeAvailable();
    	
    	if ((parentNodeId != null) && (parentNodeAvailable != null)) {
    		if (parentNodeId.intValue() > 0) { 
				LicenseNode nodeInList = findLicenseNode(parentNodeId);
				if (nodeInList != null) {
					nodeInList.setAvailable(parentNodeAvailable);
				}
    		}
    		else {
    			this.availablePool = parentNodeAvailable;
    		}
    	}
    	
    	if (form.availableValues != null) {
	    	for (int i=0 ; i<form.availableValues.length ; i++) {
	    		String id = form.orgNodeIds[i];
	    		String available = form.availableValues[i];
	    		if ((id != null) && (available != null) && (id.length() > 0) && (available.length() > 0)) {
	    			LicenseNode nodeInList = findLicenseNode(new Integer(id));
	    			if (nodeInList != null) {
	    				nodeInList.setAvailable(available);
	    			}
	    		}
	    	}
    	}
    }

    /**
     * populateLicenseNodes
     */    
    private void populateLicenseNodes(List licenseNodes) {
    	CustomerLicense cl = getCustomerLicenseByProduct(this.productName);
        Integer customerId = this.user.getCustomer().getCustomerId();
        for (int i = 0 ; i < licenseNodes.size() ; i++) {           
        	LicenseNode node = (LicenseNode)licenseNodes.get(i);
            LicenseNode nodeInList = findLicenseNode(node.getId());
            if (nodeInList == null) {
            	node.setProductId(cl.getProductId());
            	node.setCustomerId(customerId);
            	nodeInList = new LicenseNode(node);
            	this.licenseNodeList.add(nodeInList);
            }
            else {
            	node.updateNode(nodeInList);            
            }
        }
        	
    }


    /**
     * setLicenseValuesToForm
     */    
    private void setLicenseValuesToForm(Integer orgNodeId, List licenseNodes, ManageLicenseForm form) {

    	if (licenseNodes.size() > 0) {
    		
	    	LicenseNode node = null;
	    	LicenseNode parentNode = null;
	    	
	    	if (orgNodeId.intValue() == 0) {
	    		if (licenseNodes.size() == 1) {
	    			node = (LicenseNode)licenseNodes.get(0);
	    			parentNode = new LicenseNode(node);
	    		}
	    		else { 
	    			int reserved = 0;
	    			int consumed = 0;	    			
	    	        for (int i = 0 ; i < licenseNodes.size() ; i++) {           
	    	        	node = (LicenseNode)licenseNodes.get(i);
	    	        	reserved += (new Integer(node.getReserved())).intValue();
	    	        	consumed += (new Integer(node.getConsumed())).intValue();
	    	        }
	    	        node = (LicenseNode)licenseNodes.get(0);
	    			parentNode = new LicenseNode(node);
	    			parentNode.setId(orgNodeId);
	    			parentNode.setReserved(new Integer(reserved).toString());
	    			parentNode.setConsumed(new Integer(consumed).toString());    			
	    			parentNode.setAvailable(this.availablePool);	   		
        		}
	    	}
	    	else {
		    	parentNode = findLicenseNode(orgNodeId);
	    	}

	    	form.setParentLicenseNode(parentNode);
	    	form.setParentNodeId(parentNode.getId());
	    	form.setParentNodeAvailable(parentNode.getAvailable());
	    	
	        for (int i = 0 ; i < licenseNodes.size() ; i++) {           
	        	node = (LicenseNode)licenseNodes.get(i);
	        	form.orgNodeIds[i] = node.getId().toString();
	        	form.availableValues[i] = node.getAvailable();
	        }
	    }
    }
    
     /**
     * findLicenseNode
     */    
    private LicenseNode findLicenseNode(Integer id) {

        for (int i = 0 ; i < this.licenseNodeList.size() ; i++) {           
        	LicenseNode licenseNode = (LicenseNode)this.licenseNodeList.get(i);
        	if (licenseNode.getId().intValue() == id.intValue()) {
        		return licenseNode;
        	}
        }
        return null;	
    }
    
    /**
     * buildOrgNodeList
     */    
    private List buildOrgNodeList(NodeData und) {
        ArrayList nodeList = new ArrayList();
        if ( und != null ) {  
                        
            CustomerLicense cl = getCustomerLicenseByProduct(this.productName);
        	
            LicenseNode licenseNode = null;
            Node[] nodes = und.getNodes();     
            
            for (int i = 0 ; i < nodes.length ; i++) {
                
                Node node = nodes[i];
                if ( node != null ) {
                    
                    OrgNodeLicenseInfo onli = getLicenseQuantitiesByOrg(node.getOrgNodeId(), cl.getProductId(), cl.getSubtestModel());
                    
                    if (onli != null) {                              
                        licenseNode = new LicenseNode();
                        licenseNode.setName(node.getOrgNodeName());
                        licenseNode.setId(node.getOrgNodeId());   
                        licenseNode.setCategoryName(node.getOrgNodeCategoryName());
                        licenseNode.setChildrenNodeCount(node.getChildNodeCount());
                        
                        licenseNode.setProductId(cl.getProductId());
                        licenseNode.setSubtestModel(cl.getSubtestModel());
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
    private OrgNodeLicenseInfo getLicenseQuantitiesByOrg(Integer orgNodeId, Integer productId, String subtestModel) {
        OrgNodeLicenseInfo onli = null;
        
        
        try {
            onli = this.licensing.getLicenseQuantitiesByOrgNodeIdAndProductId(this.userName, 
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
     * initLicenseData
     */
    private void initLicenseData()
    {   
        this.licenseNodeList = new ArrayList();
        this.availablePool = "0";
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
        	
        	 cls = this.licensing.getCustomerOrgNodeLicenseData(this.userName, null);
        	
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
    
    
    /**
     * @jpf:action
     */
	@Jpf.Action()
    protected Forward goToSystemAdministration(ManageLicenseForm form)
    {
        try {
            getResponse().sendRedirect("/OrganizationManagementWeb/administration/begin.do");
        } catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
        }
        
        return null;
    }

    /**
     * @jpf:action
     */
	@Jpf.Action()
    protected Forward goToSaveLicenses(ManageLicenseForm form)
    {
		saveLicenses(form);	
		return goToSystemAdministration(form);		
    }

    /**
     * saveLicenses
     */
    private boolean saveLicenses(ManageLicenseForm form)
    {
    	boolean result = false;
    	
        getLicenseValuesInForm(form);
		
		Integer customerId = this.user.getCustomer().getCustomerId();       
        int numberNodes = this.licenseNodeList.size();
        LicenseNodeData [] licenseNodeDataList = new LicenseNodeData[numberNodes];
        
        for (int i = 0 ; i < numberNodes ; i++) {           
        	LicenseNode licenseNode = (LicenseNode)this.licenseNodeList.get(i);
        	Integer orgNodeId = licenseNode.getId();
        	String name = licenseNode.getName();
        	String available = licenseNode.getAvailable();
        	Integer productId = licenseNode.getProductId();
        	String subtestModel = licenseNode.getSubtestModel();
        	
        	LicenseNodeData licenseNodeData = new LicenseNodeData();
        	licenseNodeData.setProductId(productId);
        	licenseNodeData.setAvailable(available);
        	licenseNodeData.setOrgNodeId(orgNodeId);
        	licenseNodeData.setSubtestModel(subtestModel);
        	licenseNodeData.setCustomerId(customerId);
        	licenseNodeDataList[i] = licenseNodeData;
      	
System.out.println("orgNodeId=" + orgNodeId + "    name=" + name + "    productId=" + productId + "    subtestModel=" + subtestModel + "    customerId=" + customerId + "    available=" + available);
        }

        try {
			result = this.licensing.saveOrUpdateOrgNodeLicenseDetail(licenseNodeDataList);
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
        
        return result;
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
        private String[] availableValues;
        private String[] orgNodeIds;
        private LicenseNode parentLicenseNode;
        private Integer parentNodeId;
        private String parentNodeAvailable;
        private Boolean topNodeEditing;
        
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
            this.availableValues = new String[5];
            this.orgNodeIds = new String[5];

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
            this.topNodeEditing = Boolean.FALSE;
        }   

        public ManageLicenseForm createClone()
        {   
            ManageLicenseForm copied = new ManageLicenseForm();

            copied.setActionElement(this.actionElement);
            copied.setCurrentAction(this.currentAction);
            copied.setOrgNodeName(this.orgNodeName);
            copied.setOrgNodeId(this.orgNodeId);
            copied.setAvailableValues(this.availableValues);
            copied.setOrgNodeIds(this.orgNodeIds);
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
        
        public void setAvailableValues(String[] availableValues)
        {
            this.availableValues = availableValues;
        }       
        public String[] getAvailableValues()
        {
            if (this.availableValues == null || this.availableValues.length == 0) {
                this.availableValues = new String[5];
            }       	
            return this.availableValues;
        }
        public void setOrgNodeIds(String[] orgNodeIds)
        {
            this.orgNodeIds = orgNodeIds;
        }       
        public String[] getOrgNodeIds()
        {
            if (this.orgNodeIds == null || this.orgNodeIds.length == 0) {
                this.orgNodeIds = new String[5];
            }       	
            return this.orgNodeIds;
        }
        
        public void setParentLicenseNode(LicenseNode node)
        {
            if (this.parentLicenseNode == null) {
                this.parentLicenseNode = new LicenseNode();
            }       	
            this.parentLicenseNode = node;
        }
        
        public Integer getParentNodeId() {
			return parentNodeId;
		}

		public void setParentNodeId(Integer parentNodeId) {
			this.parentNodeId = parentNodeId;
		}

		public String getParentNodeAvailable() {
			return parentNodeAvailable;
		}

		public void setParentNodeAvailable(String parentNodeAvailable) {
			this.parentNodeAvailable = parentNodeAvailable;
		}

		public LicenseNode getParentLicenseNode()
        {
            return this.parentLicenseNode;
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

		public Boolean getTopNodeEditing() {
			return topNodeEditing;
		}

		public void setTopNodeEditing(Boolean topNodeEditing) {
			this.topNodeEditing = topNodeEditing;
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
