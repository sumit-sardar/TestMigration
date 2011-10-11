package userOperation;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.Base;
import utils.BaseTree;
import utils.FilterSortPageUtils;
import utils.Organization;
import utils.TreeData;
import utils.UserPathListUtils;
import utils.UserSearchUtils;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserData;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.google.gson.Gson;


@Jpf.Controller()
public class UserOperationController extends PageFlowController
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

    // LLO- 118 - Change for Ematrix UI
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;
    
    private static final String ACTION_DEFAULT        = "defaultAction";
    private static final String ACTION_FIND_USER      = "findUser";
    private static final String ACTION_CHANGE_PASSWORD = "changePassword";
    public static String CONTENT_TYPE_JSON = "application/json";
    
	private String userName = null;
	private Integer customerId = null;
	private User user = null;

    
	
	
	
    /**
     * this method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="defaultAction.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "beginFindUser.do")
    })
    protected Forward begin()
    {
        return new Forward("success");
    }


    /**
     * the default action, set to beginFindUser.do 
     * @jpf:action
     * @jpf:forward name="success" path="beginFindUser.do"
     * @jpf:forward name="newUser" path="beginChangeMyPassword.do" 
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "findUserHierarchy.do"), 
        @Jpf.Forward(name = "newUser",
                     path = "beginChangeMyPassword.do")
    })
    protected Forward beginFindUser()
    {
        initialize();
        return new Forward("success");
    }
    
    
    /**
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "user_hierarchy.jsp")
	}, 
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "logout.do"))
			protected Forward findUserHierarchy(userOperationForm form)
	{   
		this.getRequest().setAttribute("isFindUser", Boolean.TRUE);
		return new Forward("success");
	}
    
	/**
     * initialize
     * @throws SQLException 
     */
    private void initialize() 
    {        
        getUserDetails();
        userOperationForm form = new userOperationForm();
      	CustomerConfiguration[] customerConfigurations = getCustomerConfigurations();

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
    

	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="find_user_hierarchy.jsp")
	})
	protected Forward userOrgNodeHierarchyList(userOperationForm form){

		String jsonTree = "";
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String contentType = CONTENT_TYPE_JSON;
		try {
			BaseTree baseTree = new BaseTree ();

			ArrayList<Organization> completeOrgNodeList = new ArrayList<Organization>();
			UserNodeData associateNode = UserPathListUtils.populateAssociateNode(this.userName,this.userManagement);
			ArrayList<Organization> selectedList  = UserPathListUtils.buildassoOrgNodehierarchyList(associateNode);	
			Integer leafNodeCategoryId = UserPathListUtils.getLeafNodeCategoryId(this.userName,this.customerId, this.userManagement);
			ArrayList <Integer> orgIDList = new ArrayList <Integer>();
			ArrayList<TreeData> data = new ArrayList<TreeData>();

			UserNodeData und = UserPathListUtils.OrgNodehierarchy(this.userName, 
					this.userManagement, selectedList.get(0).getOrgNodeId()); 
			ArrayList<Organization> orgNodesList = UserPathListUtils.buildOrgNodehierarchyList(und, orgIDList,completeOrgNodeList);	

			jsonTree = generateTree(orgNodesList);

			for (int i= 0; i < selectedList.size(); i++) {

				if (i == 0) {

					preTreeProcess (data,orgNodesList);

				} else {

					Integer nodeId = selectedList.get (i).getOrgNodeId();
					if (orgIDList.contains(nodeId)) {
						continue;
					} else {

						orgIDList = new ArrayList <Integer>();
						UserNodeData undloop = UserPathListUtils.OrgNodehierarchy(this.userName, 
								this.userManagement,nodeId);   
						ArrayList<Organization> orgNodesListloop = UserPathListUtils.buildOrgNodehierarchyList(undloop, orgIDList, completeOrgNodeList);	
						preTreeProcess (data,orgNodesListloop);
					}
				}


			}

			Gson gson = new Gson();
			baseTree.setData(data);
			baseTree.setLeafNodeCategoryId(leafNodeCategoryId);
			jsonTree = gson.toJson(baseTree);
		

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
			System.err.println("Exception while processing CR response.");
			e.printStackTrace();
		}

		return null;

	}
	 @Jpf.Action(forwards={
				@Jpf.Forward(name = "success", 
						path ="find_user_hierarchy.jsp")
		})
		protected Forward userOrgNodeHierarchyGrid(userOperationForm form){
		
				
		 String jsonTree = "";
		 HttpServletRequest req = getRequest();
		 HttpServletResponse resp = getResponse();
		 String treeOrgNodeId = getRequest().getParameter("treeOrgNodeId");
		 List userList = new ArrayList(0);
		 OutputStream stream = null;
		 String contentType = CONTENT_TYPE_JSON;
		 String json = "";
		 ObjectOutput output = null;
			try {
				UserData uData = findUserByHierarchy();
				
				if ((uData != null) && (uData.getFilteredCount().intValue() > 0))
		        {
		            userList = UserSearchUtils.buildUserList(uData);
		        }
				Base base = new Base();
				base.setPage("1");
				base.setRecords("10");
				base.setTotal("2");
				Gson gson = new Gson();
				base.setUserProfileInformation(userList);
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
				System.err.println("Exception while processing CR response.");
				e.printStackTrace();
			}
			
			return null;
			
		}
	 
		
	 /**
	     * findByHierarchy
	     */
	    private UserData findUserByHierarchy() throws CTBBusinessException
	    {      
	       
	    	String treeOrgNodeId = getRequest().getParameter("treeOrgNodeId");
			Integer selectedOrgNodeId = null;
			if(treeOrgNodeId != null)
				selectedOrgNodeId = Integer.parseInt(treeOrgNodeId);
			UserData uData = null;

			FilterParams filter = null;
			PageParams page = null;
			SortParams sort = null;

			if (selectedOrgNodeId != null)
			{
				 sort = FilterSortPageUtils.buildUserSortParams(FilterSortPageUtils.USER_DEFAULT_SORT_COLUMN, FilterSortPageUtils.ASCENDING);
				 uData = UserSearchUtils.searchUsersByOrgNode(this.userManagement, this.userName, selectedOrgNodeId, filter, page, sort);
			}

			return uData;
		}
	        
	    
	    
	
	/////////////////////////////////////////////////////////////////////////////////////////////    
	///////////////////////////// BEGIN OF NEW NAVIGATION ACTIONS ///////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////    
	
	/**
	 * ASSESSMENTS actions
	 */    
    @Jpf.Action()
	protected Forward assessments()
	{
        try
        {
            String url = "/TestSessionInfoWeb/homepage/assessments.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
			
	/**
	 * ORGANIZATIONS actions
	 */    
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "organizationsLink", path = "organizations_manageOrganizations.do"),
	        @Jpf.Forward(name = "studentsLink", path = "organizations_manageStudents.do"),
	        @Jpf.Forward(name = "usersLink", path = "organizations_manageUsers.do")
	    }) 
	protected Forward organizations()
	{
		String menuId = (String)this.getRequest().getParameter("menuId");    	
		String forwardName = (menuId != null) ? menuId : "usersLink";
		System.out.println(forwardName);
		
	    return new Forward(forwardName);
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward organizations_manageOrganizations()
	{
		return new Forward("success");
	}
	
    @Jpf.Action()
	protected Forward organizations_manageStudents()
	{
        try
        {
            String url = "/StudentManagementWeb/studentOperation/organizations.do?menuId=studentsLink";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "beginFindUser.do") 
	    }) 
	protected Forward organizations_manageUsers()
	{
		return new Forward("success");
	}

    /**
     * REPORTS actions
     */    
    @Jpf.Action()
    protected Forward reports()
    {
        try
        {
            String url = "/TestSessionInfoWeb/homepage/reports.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
	
	/**
	 * SERVICES actions
	 */    
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "manageLicensesLink", path = "services_manageLicenses.do"),
	        @Jpf.Forward(name = "installSoftwareLink", path = "services_installSoftware.do"),
	        @Jpf.Forward(name = "downloadTestLink", path = "services_downloadTest.do"),
	        @Jpf.Forward(name = "uploadDataLink", path = "services_uploadData.do"),
	        @Jpf.Forward(name = "downloadDataLink", path = "services_downloadData.do")
	    }) 
	protected Forward services()
	{
		String menuId = (String)this.getRequest().getParameter("menuId");    	
		String forwardName = (menuId != null) ? menuId : "manageLicensesLink";
		System.out.println(forwardName);
		
	    return new Forward(forwardName);
	}
	
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "blankPage.jsp") 
        }) 
    protected Forward services_manageLicenses()
    {
        return new Forward("success");
    }
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward services_installSoftware()
	{
	    return new Forward("success");
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward services_downloadTest()
	{
	    return new Forward("success");
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward services_uploadData()
	{
	    return new Forward("success");
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward services_downloadData()
	{
	    return new Forward("success");
	}

	/**
	 * BROADCAST MESSAGE actions
	 */    
	/**
	 * @jpf:action
	 */
	@Jpf.Action()
	protected Forward broadcastMessage()
	{
	    return null;
	}
	
	
	/**
	 * MYPROFILE actions
	 */    
	@Jpf.Action()
	protected Forward myProfile()
	{
	    return null;
	}


    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// END OF NEW NAVIGATION ACTIONS ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
	

	private String generateTree (ArrayList<Organization> orgNodesList) throws Exception{	

		Organization org = orgNodesList.get(0);
		TreeData td = new TreeData ();
		td.setData(org.getOrgName());
		td.getAttr().setId(org.getOrgNodeId().toString());
		td.getAttr().setCategoryID(org.getOrgCategoryLevel().toString());
		treeProcess (org,orgNodesList,td);
		BaseTree baseTree = new BaseTree ();
		baseTree.getData().add(td);
		Gson gson = new Gson();

		String json = gson.toJson(baseTree);

		return json;
	}

	
	private static void preTreeProcess (ArrayList<TreeData> data,ArrayList<Organization> orgList) {

		Organization org = orgList.get(0);
		TreeData td = new TreeData ();
		td.setData(org.getOrgName());
		td.getAttr().setId(org.getOrgNodeId().toString());
		treeProcess (org,orgList,td);
		data.add(td);
	}
	
	private static void treeProcess (Organization org,List<Organization> list,TreeData td) {

		for (Organization tempOrg : list) {
			if (org.getOrgNodeId().equals(tempOrg.getOrgParentNodeId())) {
				TreeData tempData = new TreeData ();
				tempData.setData(tempOrg.getOrgName());
				tempData.getAttr().setId(tempOrg.getOrgNodeId().toString());
				tempData.getAttr().setCategoryID(tempOrg.getOrgCategoryLevel().toString());
				td.getChildren().add(tempData);
				treeProcess (tempOrg,list,tempData);
			}
		}
	}
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}


	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}


	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}


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
    
	
	
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** userOperationForm ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    
	/**
	 * FormData get and set methods may be overwritten by the Form Bean editor.
	 */
	public static class userOperationForm extends SanitizedFormData
	{

	}



	
}
