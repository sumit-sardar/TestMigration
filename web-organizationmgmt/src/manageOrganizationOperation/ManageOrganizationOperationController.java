package manageOrganizationOperation;

import java.io.ObjectOutput;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.Base;
import utils.BaseTree;
import utils.Organization;
import utils.OrgnizationComparator;
import utils.TreeData;
import utils.OrganizationPathListUtils;
import utils.OrgPathListUtils;


import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.OrgNodeCategory;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserData;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.CTBConstants;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.google.gson.Gson;



/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class ManageOrganizationOperationController extends PageFlowController
{
	static final long serialVersionUID = 1L;
	
	/**
     * @common:control
     */
    @Control()
    private com.ctb.control.userManagement.UserManagement userManagement;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.db.Users users;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.organizationManagement.OrganizationManagement organizationManagement;

    
    @org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;
	
    public static String CONTENT_TYPE_JSON = "application/json";
    
    private String userName = null;
	private Integer customerId = null;
	private User user = null;
	
	
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
        return new Forward("success");
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="findOrganizationHierarchy.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "findOrganizationHierarchy.do")
    })
    protected Forward beginFindOrganization()
    {                
        getUserDetails();
        CustomerConfiguration[] customerConfigurations = getCustomerConfigurations();

    	return new Forward("success");
        
    }
    
    /**
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "organization_hierarchy.jsp")
	}, 
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "logout.do"))
			protected Forward findOrganizationHierarchy(ManageOrganizationForm form)
	{   
		this.getRequest().setAttribute("isFindOrganization", Boolean.TRUE);
		return new Forward("success");
	}
	
	
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="find_user_hierarchy.jsp")
	})
	protected Forward organizationOrgNodeHierarchyList(ManageOrganizationForm form){

		String jsonTree = "";
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String contentType = CONTENT_TYPE_JSON;
		try {
			BaseTree baseTree = new BaseTree ();

			ArrayList<Organization> completeOrgNodeList = new ArrayList<Organization>();
			UserNodeData associateNode = OrganizationPathListUtils.populateAssociateNode(this.userName,this.userManagement);
			ArrayList<Organization> selectedList  = OrganizationPathListUtils.buildassoOrgNodehierarchyList(associateNode);
			Collections.sort(selectedList, new OrgnizationComparator());
			ArrayList <Integer> orgIDList = new ArrayList <Integer>();
			ArrayList<TreeData> data = new ArrayList<TreeData>();

			UserNodeData und = OrganizationPathListUtils.OrgNodehierarchy(this.userName, 
					this.userManagement, selectedList.get(0).getOrgNodeId()); 
			ArrayList<Organization> orgNodesList = OrganizationPathListUtils.buildOrgNodehierarchyList(und, orgIDList,completeOrgNodeList);	


			for (int i= 0; i < selectedList.size(); i++) {

				if (i == 0) {

					preTreeProcess (data,orgNodesList,selectedList);

				} else {

					Integer nodeId = selectedList.get (i).getOrgNodeId();
					if (orgIDList.contains(nodeId)) {
						continue;
					} else if (!selectedList.get (i).getIsAssociate()) {
						
						continue;
						
					} else {

						orgIDList = new ArrayList <Integer>();
						UserNodeData undloop = OrganizationPathListUtils.OrgNodehierarchy(this.userName, 
								this.userManagement,nodeId);   
						ArrayList<Organization> orgNodesListloop = OrganizationPathListUtils.buildOrgNodehierarchyList(undloop, orgIDList, completeOrgNodeList);	
						preTreeProcess (data,orgNodesListloop,selectedList);
					}
				}


			}

			Gson gson = new Gson();
			baseTree.setData(data);
			Collections.sort(baseTree.getData(), new Comparator<TreeData>(){

				public int compare(TreeData t1, TreeData t2) {
					return (t1.getData().toUpperCase().compareTo(t2.getData().toUpperCase()));
				}
					
			});
			jsonTree = gson.toJson(baseTree);
			String pattern = ",\"children\":[]";
			jsonTree = jsonTree.replace(pattern, "");

			try {

				resp.setContentType(contentType);
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(jsonTree.getBytes());
			} finally{
				if (stream!=null){
					stream.close();
				}
			}
		} catch (Exception e) {
			System.err.println("Exception while processing userOrgNodeHierarchyList.");
			e.printStackTrace();
		}

		return null;

	}
	
	
	 @Jpf.Action(forwards={
				@Jpf.Forward(name = "success", 
						path ="find_user_hierarchy.jsp")
		})
		protected Forward orgNodeHierarchyGrid(ManageOrganizationForm form){
						
		 HttpServletRequest req = getRequest();
		 HttpServletResponse resp = getResponse();
		 Integer treeOrgNodeId = Integer.parseInt(getRequest().getParameter("treeOrgNodeId"));
		 List organizationList = new ArrayList(0);
		 OutputStream stream = null;
		 String contentType = CONTENT_TYPE_JSON;
		 String json = "";
		 ObjectOutput output = null;
			try {
				NodeData subOrganizationList  = new NodeData();
				subOrganizationList = OrgPathListUtils.getOrganizationNodesForParentIncludingParentName(this.userName, this.organizationManagement, treeOrgNodeId, null, null, null);
				if (subOrganizationList != null)
		        {
		            organizationList = OrgPathListUtils.buildOrganizationList(subOrganizationList);
		        }
				Base base = new Base();
				base.setPage("1");
				base.setRecords("10");
				base.setTotal("2");
				Gson gson = new Gson();
				base.setOrganizationProfileInformation(organizationList);
				json = gson.toJson(base);
				
					try{
			    		resp.setContentType("application/json");
			    		stream = resp.getOutputStream();
			    		resp.flushBuffer();
			    		stream.write(json.getBytes());
				
			    		}
				
			    		 finally{
			 				if (stream!=null){
			 					stream.close();
			 				}
			 			}
					
					
				
			} catch (Exception e) {
				System.err.println("Exception while processing OrgNodeHierarchyGrid");
				e.printStackTrace();
			}
			
			return null;
			
		}
	 
	 @Jpf.Action(forwards={
				@Jpf.Forward(name = "success", 
						path ="find_user_hierarchy.jsp")
		})
		protected Forward getUserRole(ManageOrganizationForm form){
						
		 HttpServletRequest req = getRequest();
		 HttpServletResponse resp = getResponse();
		 OutputStream stream = null;
		 String contentType = CONTENT_TYPE_JSON;
		 String json = "";
			try {
				String userRoleValue = this.user.getRole().getRoleName(); 
				Gson gson = new Gson();
				json = gson.toJson(userRoleValue);
				System.out.println(json);
					try{
			    		resp.setContentType("application/json");
			    		stream = resp.getOutputStream();
			    		resp.flushBuffer();
			    		stream.write(json.getBytes());
				
			    		}
				
			    		 finally{
			 				if (stream!=null){
			 					stream.close();
			 				}
			 			}
					
					
				
			} catch (Exception e) {
				System.err.println("Exception while processing OrgNodeHierarchyGrid");
				e.printStackTrace();
			}
			
			return null;
			
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
            this.userName = (String) getSession().getAttribute("userName"); 
        
        try {
            this.user = this.userManagement.getUser(this.userName, 
                                               this.userName);
            this.customerId = this.user.getCustomer().getCustomerId();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }        
        getSession().setAttribute("userName", this.userName);
        
    }
	
	
    /**
	 * getCustomerConfigurations
	 */
	private CustomerConfiguration[] getCustomerConfigurations()
	{
		CustomerConfiguration[] customerConfigurations = null;
		try {
			customerConfigurations = this.users.getCustomerConfigurations(this.customerId);

			this.getRequest().setAttribute("isBulkAccommodationConfigured",customerHasBulkAccommodation(customerConfigurations));
			this.getRequest().setAttribute("isScoringConfigured", customerHasScoring(customerConfigurations));
			boolean isLasLinkCustomer = isLasLinkCustomer(customerConfigurations);
			this.getRequest().setAttribute("isLasLinkCustomer", isLasLinkCustomer);  
			this.getRequest().setAttribute("isTopLevelUser",isTopLevelUser(isLasLinkCustomer));
			this.getRequest().setAttribute("userHasReports", userHasReports());
			this.getRequest().setAttribute("customerConfigurations", customerConfigurations);    
		}
		catch (SQLException be) {
			be.printStackTrace();
		}
		return customerConfigurations;
	}
    
    
	 /**
	 * Bulk Accommodation
	 */
	private Boolean customerHasBulkAccommodation(CustomerConfiguration[] customerConfigurations) 
	{
		boolean hasBulkStudentConfigurable = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentConfigurable = true; 
					break;
				}
			}
		}
		return new Boolean(hasBulkStudentConfigurable);           
	}
	
	
	/**
	 * This method checks whether customer is configured to access the scoring feature or not.
	 * @return Return Boolean 
	 */
	
	private Boolean customerHasScoring(CustomerConfiguration[] customerConfigurations)
	{               
		boolean hasScoringConfigurable = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++)
			{
				CustomerConfiguration cc = (CustomerConfiguration) customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
						cc.getDefaultValue().equals("T")	) {
					hasScoringConfigurable = true;
					break;
				} 
			}
		}

		return new Boolean(hasScoringConfigurable);
	}
	
	private boolean isLasLinkCustomer(CustomerConfiguration[] customerConfigurations)
	{               
		boolean isLasLinkCustomer = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++)
			{
				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				//isLasLink customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("LASLINK_Customer") && cc.getDefaultValue().equals("T")	)
				{
					isLasLinkCustomer = true;
					break;
				} 
			}
		}
		return isLasLinkCustomer;
	}

	
	private boolean isTopLevelUser(boolean isLasLinkCustomerVal){

		boolean isUserTopLevel = false;
		boolean isLaslinkUserTopLevel = false;
		boolean isLaslinkUser = false;
		isLaslinkUser = isLasLinkCustomerVal;
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
		return isLaslinkUserTopLevel;
	}
	
	
	/**
     * userHasReports
     */
    private Boolean userHasReports() 
    {
        Boolean hasReports = Boolean.FALSE;
        try {   
            Customer customer = this.user.getCustomer();
            Integer customerId = customer.getCustomerId();     
            hasReports = this.userManagement.userHasReports(this.userName, customerId);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return hasReports;
    }
    
    private static void preTreeProcess (ArrayList<TreeData> data,ArrayList<Organization> orgList,ArrayList<Organization> selectedList) {

		Organization org = orgList.get(0);
		TreeData td = new TreeData ();
		td.setData(org.getOrgName());
		td.getAttr().setId(org.getOrgNodeId().toString());
		td.getAttr().setCustomerId(org.getCustomerId().toString());
		treeProcess (org,orgList,td,selectedList);
		data.add(td);
	}
    
    
    private static void treeProcess (Organization org,List<Organization> list,TreeData td,ArrayList<Organization> selectedList) {

		for (Organization tempOrg : list) {
			
			if (org.getOrgNodeId().equals(tempOrg.getOrgParentNodeId())) {
				
				if (selectedList.contains(tempOrg)) {
					
					int index = selectedList.indexOf(tempOrg);
					if (index != -1) {
						
						Organization selectedOrg = selectedList.get(index);
						selectedOrg.setIsAssociate(false);
					}
					
				}
				TreeData tempData = new TreeData ();
				tempData.setData(tempOrg.getOrgName());
				tempData.getAttr().setId(tempOrg.getOrgNodeId().toString());
				tempData.getAttr().setCategoryID(tempOrg.getOrgCategoryLevel().toString());
				tempData.getAttr().setCustomerId(tempOrg.getCustomerId().toString());
				td.getChildren().add(tempData);
				treeProcess (tempOrg,list,tempData,selectedList);
			}
		}
	}
    
    
	
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** ManageOrganizationForm ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    

  

  /**
   * FormData get and set methods may be overwritten by the Form Bean editor.
   */
  public static class ManageOrganizationForm extends SanitizedFormData
  {
	  
  }
	
}
