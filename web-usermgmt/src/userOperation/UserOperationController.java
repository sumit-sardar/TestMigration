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
import utils.MessageInfo;
import utils.OptionList;
import utils.Organization;
import utils.TreeData;
import utils.UserPathListUtils;
import utils.UserSearchUtils;
import utils.WebUtils;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.OrganizationNode;
import com.ctb.bean.testAdmin.Role;
import com.ctb.bean.testAdmin.TimeZones;
import com.ctb.bean.testAdmin.USState;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserData;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.userManagement.CTBConstants;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.google.gson.Gson;

import dto.Message;
import dto.PathNode;
import dto.UserProfileInformation;


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
    private static final String ACTION_ADD_USER       = "addUser";
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
				System.err.println("Exception while processing userOrgNodeHierarchyGrid");
				e.printStackTrace();
			}
			
			return null;
			
		}
	 
	 
	 @Jpf.Action(forwards={
				@Jpf.Forward(name = "success", 
						path ="find_user_by_hierarchy.jsp")
		})
		protected Forward getOptionList(userOperationForm form){
			String jsonResponse = "";
			OutputStream stream = null;
			Boolean isLasLinkCustomer = new Boolean(getRequest().getParameter("isLasLinkCustomer"));
			HttpServletRequest req = getRequest();
			HttpServletResponse resp = getResponse();
			try {
				
				OptionList optionList = new OptionList();
				optionList.setRoleOptions(getRoleOptions(ACTION_ADD_USER));
				optionList.setTimeZoneOptions(getTimeZoneOptions(ACTION_ADD_USER));
				optionList.setStateOptions(getStateOptions(ACTION_ADD_USER));
				
				
				try {
					Gson gson = new Gson();
					String json = gson.toJson(optionList);
					resp.setContentType("application/json");
					resp.flushBuffer();
					stream = resp.getOutputStream();
					stream.write(json.getBytes());

				} finally{
					if (stream!=null){
						stream.close();
					}
				}
			}
			catch (Exception e) {
				System.err.println("Exception while retrieving optionList.");
				e.printStackTrace();
			}
			return null;
		}

	 
	 
	 @Jpf.Action(forwards={
				@Jpf.Forward(name = "success", 
						path ="find_user_by_hierarchy.jsp")
		})
		protected Forward saveAddEditUser(userOperationForm form)
		{   
			String jsonResponse = "";
			OutputStream stream = null;
			boolean validateAgain = true;
			boolean validInfo = false;
			String message = "";
			String requiredFields = null;
			HttpServletRequest req = getRequest();
			HttpServletResponse resp = getResponse();
			MessageInfo messageInfo = new MessageInfo();
			String userName = null;
			UserProfileInformation userProfile = null;
			userProfile = new UserProfileInformation();
			userProfile.setFirstName(getRequest().getParameter("userFirstName"));
			userProfile.setMiddleName(getRequest().getParameter("userMiddleName"));
			userProfile.setLastName(getRequest().getParameter("userLastName"));
			userProfile.setEmail(getRequest().getParameter("userEmail"));
			userProfile.setTimeZone(getRequest().getParameter("timeZoneOptions"));
			userProfile.setRoleId(getRequest().getParameter("roleOptions"));
			userProfile.setExtPin1(getRequest().getParameter("userExternalId"));
			userProfile.getUserContact().setAddressLine1(getRequest().getParameter("addressLine1"));
			userProfile.getUserContact().setAddressLine2(getRequest().getParameter("addressLine2"));
			userProfile.getUserContact().setCity(getRequest().getParameter("city"));
			userProfile.getUserContact().setState(getRequest().getParameter("stateOptions"));
			userProfile.getUserContact().setZipCode1(getRequest().getParameter("zipCode1"));
			userProfile.getUserContact().setZipCode2(getRequest().getParameter("zipCode2"));
			userProfile.getUserContact().setPrimaryPhone1(getRequest().getParameter("primaryPhone1"));
			userProfile.getUserContact().setPrimaryPhone2(getRequest().getParameter("primaryPhone2"));
			userProfile.getUserContact().setPrimaryPhone3(getRequest().getParameter("primaryPhone3"));
			userProfile.getUserContact().setPrimaryPhone4(getRequest().getParameter("primaryPhone4"));
			userProfile.getUserContact().setSecondaryPhone1(getRequest().getParameter("secondaryPhone1"));
			userProfile.getUserContact().setSecondaryPhone2(getRequest().getParameter("secondaryPhone2"));
			userProfile.getUserContact().setSecondaryPhone3(getRequest().getParameter("secondaryPhone3"));
			userProfile.getUserContact().setSecondaryPhone4(getRequest().getParameter("secondaryPhone4"));
			userProfile.getUserContact().setFaxNumber1(getRequest().getParameter("faxNumber1"));
			userProfile.getUserContact().setFaxNumber2(getRequest().getParameter("faxNumber2"));
			userProfile.getUserContact().setFaxNumber3(getRequest().getParameter("faxNumber3"));
			
			String assignedOrgNodeIds = getRequest().getParameter("assignedOrgNodeIds");
			String[] assignedOrgNodeId = assignedOrgNodeIds.split(",");
			ArrayList<OrganizationNode> selectedOrgNodes = new ArrayList<OrganizationNode>();
			try {
			for (int i = assignedOrgNodeId.length - 1; i >= 0; i--) {
				 String [] values = assignedOrgNodeId[i].split("\\|");
				 OrganizationNode orgNode = new OrganizationNode();
				 orgNode.setOrgNodeId(Integer.parseInt(values[0].trim()));
				 orgNode.setCustomerId(Integer.parseInt(values[1].trim()));
				 selectedOrgNodes.add(orgNode);
				
			}
			} catch (NumberFormatException ne ) {
				ne.printStackTrace();
				validateAgain = false;
				requiredFields = "Organization Assignment";
				message = requiredFields + (Message.REQUIRED_TEXT);
				messageInfo = createMessageInfo(messageInfo, Message.MISSING_REQUIRED_FIELDS, message, Message.ERROR, true, false );
			}
			//Check if logged in user is an AddAdministrator
			boolean isAdmin = isAddAdministrator();
			if (isAdmin){
				userProfile.setRole(CTBConstants.ROLE_NAME_ADMINISTRATOR);
			}
			
			// Future use
			boolean isLoginUser = false;
			//String selectedUserName = "";
			//boolean isLoginUser = isLoggedInUser(selectedUserName);
			
			requiredFields = requiredfieldMissing(userProfile, selectedOrgNodes, isLoginUser, userName );
			
			if( requiredFields != null){
				validateAgain = false;
				if ( requiredFields.indexOf(",") > 0){
					message = requiredFields + (" <br/> " + Message.REQUIRED_TEXT_MULTIPLE);
					messageInfo = createMessageInfo(messageInfo, Message.MISSING_REQUIRED_FIELDS, message, Message.ERROR, true, false );
				}
				else {
					message = requiredFields + (" <br/> " + Message.REQUIRED_TEXT);
					messageInfo = createMessageInfo(messageInfo, Message.MISSING_REQUIRED_FIELD, message, Message.ERROR, true, false );

				}
			}
			
			if (validateAgain){
				String isInvalidUserInfo = isInvalidUserInfo(userProfile);
				if( isInvalidUserInfo != null ) {
					validateAgain = false;
					message = isInvalidUserInfo + (" <br/> " + Message.REQUIRED_TEXT);
					messageInfo = createMessageInfo(messageInfo, Message.INVALID_FORMAT_TITLE, message, Message.ERROR, true, false );
				}
			}
			
			if(validateAgain){
				 validInfo = verifyUserInformation(userProfile, selectedOrgNodes, isLoginUser, userName, this.user );
				 if(!validInfo){
					 if (requiredFields == null ) {
						 requiredFields = "";
					 }
						requiredFields += (Message.ADMIN_CREATION_ERROR);
						messageInfo = createMessageInfo(messageInfo, Message.ADMIN_CREATION_TITLE, requiredFields, Message.ERROR, true, false );
					}
					
					if (validInfo) {
			            validInfo = verifyUserCreationPermission( selectedOrgNodes);
			            if(!validInfo){
			            	 if (requiredFields == null ) {
								 requiredFields = "";
							 }
							requiredFields += (Message.USER_CREATION_ERROR);
							messageInfo = createMessageInfo(messageInfo, Message.USER_CREATION_TITLE, requiredFields, Message.ERROR, true, false );
							
			            }
			         } 
			}
			
						
			
			
			//selectedOrgNodes.add(118641);
			
			
			boolean isCreateNew = userName == null ? true : false;

			boolean result = true;
			try {
				
				
				if(validInfo) {
					CustomerConfiguration[]  customerConfigurations = this.users.getCustomerConfigurations(this.customerId);
					userName = saveUserProfileInformation(isCreateNew, userProfile, userName, selectedOrgNodes);
				       
					
					if (isCreateNew)
					{
						if (userName != null)  {
							
							messageInfo = createMessageInfo(messageInfo, Message.ADD_TITLE, Message.ADD_SUCCESSFUL, Message.INFORMATION, false, true );
							//form.setMessage(Message.ADD_TITLE, Message.ADD_SUCCESSFUL, Message.INFORMATION);
						}
						else  {
							
							messageInfo = createMessageInfo(messageInfo, Message.ADD_TITLE, Message.ADD_ERROR, Message.INFORMATION, true, false );
							//form.setMessage(Message.ADD_TITLE, Message.ADD_ERROR, Message.INFORMATION);
						}
						
						
					}
					else
					{
						if (userName != null) {
							messageInfo = createMessageInfo(messageInfo, Message.EDIT_TITLE, Message.EDIT_SUCCESSFUL, Message.INFORMATION, false, true );
							//form.setMessage(Message.EDIT_TITLE, Message.EDIT_SUCCESSFUL, Message.INFORMATION);
						}
						else  {
							messageInfo = createMessageInfo(messageInfo, Message.EDIT_TITLE, Message.EDIT_ERROR, Message.INFORMATION, true, false );
							
						}
					}
			}
		}
			catch (SQLException be) {
				be.printStackTrace();
			}	
				creatGson( req, resp, stream, messageInfo );
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
	        
	    /**
	     * saveUserProfileInformation
	     */
	    private String saveUserProfileInformation(boolean isCreateNew, 
	    								UserProfileInformation userProfile, 
	                                    String userName, 
	                                    List selectedOrgNodes)
	    {        
	        
	        User user = userProfile.makeCopy(userName, selectedOrgNodes);
	        String title = null;
	        String username = user.getUserName();
	        try {                    
	            if (isCreateNew) {
	                username = this.userManagement.createUser(this.userName, user);
	            } 
	        } 
	        catch (CTBBusinessException be) {
	            be.printStackTrace();
	            username = null;
	        }            
	        catch (Exception e) {
	            e.printStackTrace();
	            username = null;
	        }
	                
	        return username;
	    }
	    

		private MessageInfo createMessageInfo(MessageInfo messageInfo, String messageTitle, String content, String type, boolean errorflag, boolean successFlag){
			messageInfo.setTitle(messageTitle);
			messageInfo.setContent(content);
			messageInfo.setType(type);
			messageInfo.setErrorFlag(errorflag);
			messageInfo.setSuccessFlag(successFlag);
			return messageInfo;
		}
		
		
		private void creatGson(HttpServletRequest req, HttpServletResponse resp, OutputStream stream, MessageInfo messageInfo ){
			
			try {
				try {
					Gson gson = new Gson();
					String json = gson.toJson(messageInfo);
					resp.setContentType("application/json");
					resp.flushBuffer();
					stream = resp.getOutputStream();
					stream.write(json.getBytes());

				} finally{
					if (stream!=null){
						stream.close();
					}
				}
				
			}
			catch (Exception e) {
				System.err.println("Exception while retrieving optionList.");
				e.printStackTrace();
			}
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
            String url = "/TestSessionInfoWeb/sessionOperation/assessments.do";
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
		
	    return new Forward(forwardName);
	}
	
    @Jpf.Action()
	protected Forward organizations_manageOrganizations()
	{
        try
        {
            String url = "/OrganizationManagementWeb/orgOperation/organizations.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
	
    @Jpf.Action()
	protected Forward organizations_manageStudents()
	{
        try
        {
            String url = "/StudentManagementWeb/studentOperation/organizations.do";
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
            String url = "/TestSessionInfoWeb/sessionOperation/reports.do";
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
	@Jpf.Action()
    protected Forward services()
    {
        try
        {
            String url = "/OrganizationManagementWeb/orgOperation/services.do";
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
	protected Forward broadcastMessage()
	{
	    return null;
	}
	
	
	/**
	 * @jpf:action
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
		td.getAttr().setCustomerId(org.getCustomerId().toString());
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
				tempData.getAttr().setCustomerId(tempOrg.getCustomerId().toString());
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
	
	// Added on Oct-13
	public boolean isAddAdministrator(){

		if(this.user == null){
			try {
				this.user = this.users.getUserDetails(this.userName);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return this.user.getRole().getRoleName().equalsIgnoreCase(CTBConstants.ROLE_NAME_ADMINISTRATOR);
	}

	public boolean verifyUserCreationPermission (List<OrganizationNode> selectedOrgNodes) {

		for (int i = 0; i < selectedOrgNodes.size(); i++) {
			OrganizationNode selectedPathNode = (OrganizationNode)selectedOrgNodes.get(i);

			for (int j = 0; j < selectedOrgNodes.size() && i!=j; j++) {
				
				OrganizationNode pathNode = (OrganizationNode)selectedOrgNodes.get(j);
				if (pathNode != null ) {

					if ( selectedPathNode.getCustomerId().intValue() != pathNode.getCustomerId().intValue() ) {
						//form.setMessage(Message.USER_CREATION_TITLE, requiredFields, Message.ERROR);
						return false;
					}
				}
			}
		}
		
		
		return true;
	}
	
	private String [] getRoleOptions(String action)
    {        
		Role[] roles = null;
		
		try {
			roles =  this.userManagement.getRoles();
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		
		
		List<String> roleOptions = new ArrayList<String>();
        /*if (action.equals(ACTION_FIND_USER)) {
            roleOptions.add(Message.ANY_ROLE);
        }*/
        if (action.equals(ACTION_ADD_USER)) {	
            roleOptions.add(-1 + "|"+Message.SELECT_ROLE);
        }
        
        try {
            if (roles != null) {
                for (int i = 0; i < roles.length ; i++) {
                    roleOptions.add(roles[i].getRoleId()+"|"+roles[i].getRoleName());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return roleOptions.toArray( new String[roleOptions.size()]);
    }
	
	private String [] getTimeZoneOptions(String action)
    {        
		TimeZones[] timeZones = null;
		
		try {
			timeZones =  this.userManagement.getTimeZones();
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		
		
		List<String> timeZoneOptions = new ArrayList<String>();
        /*if (action.equals(ACTION_FIND_USER)) {
            roleOptions.add(Message.ANY_ROLE);
        }*/
        if (action.equals(ACTION_ADD_USER)) {	
        	timeZoneOptions.add(-1 + "|"+Message.SELECT_TIME_ZONE);
        }
        
        try {
            if (timeZones != null) {
                for (int i = 0; i < timeZones.length ; i++) {
                	timeZoneOptions.add(timeZones[i].getTimeZone()+"|"+timeZones[i].getTimeZoneDesc());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return timeZoneOptions.toArray( new String[timeZoneOptions.size()]);
    }
	
	private String [] getStateOptions(String action)
    {        
		USState[] usStates = null;
		
		try {
			usStates =  this.userManagement.getStates();
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		
		
		List<String> stateOptions = new ArrayList<String>();
        /*if (action.equals(ACTION_FIND_USER)) {
            roleOptions.add(Message.ANY_ROLE);
        }*/
        if (action.equals(ACTION_ADD_USER)) {	
        	stateOptions.add(-1 + "|"+Message.SELECT_STATE);
        }
        
        try {
            if (stateOptions != null) {
                for (int i = 0; i < usStates.length ; i++) {
                	stateOptions.add(usStates[i].getStatePr()+"|"+usStates[i].getStatePrDesc());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return stateOptions.toArray( new String[stateOptions.size()]);
    }
	
	 public static boolean verifyUserInformation(UserProfileInformation form, List<OrganizationNode> selectedOrgNodes, boolean isLoginUser, String userName, User user)	    
	    {                    
	        
	      /*  if ( isRequiredfieldMissing(form, selectedOrgNodes, isLoginUser, userName) ) {
	            return false;
	        }
	                
	        if (isInvalidUserInfo(form)){
	            return false;
	        } */          									   
	        
	        if ( user.getRole().getRoleId().intValue() == Integer.parseInt(form.getRoleId()) &&
	        		user.getUserId().intValue() != form.getUserId().intValue()){
	        	if ( !verifyAdminCreationPermission
	                    (form, user.getOrganizationNodes(), selectedOrgNodes) ) {
	                return false;
	            }  
	        }
	          
	        /*if (isInvalidUserContact(form)){
	            return false;
	        }*/
	        return true;
	    }
	 
	 public static boolean verifyAdminCreationPermission (UserProfileInformation form, Node []loginUserNodes, List<OrganizationNode> selectedOrgNodes)
	    {
	        String requiredFields = "";
	        for (int i = 0; i < loginUserNodes.length; i++) {
	            
	            Integer loginUserNodeId = loginUserNodes[i].getOrgNodeId();
	            
	            for (int j = 0; j < selectedOrgNodes.size(); j++) {
	                
	            	OrganizationNode pathNode = (OrganizationNode)selectedOrgNodes.get(j);
	                Integer selectedUserNodeId = pathNode.getOrgNodeId();
	                
	                if (loginUserNodeId.intValue() == selectedUserNodeId.intValue()) {
	                    //requiredFields += ("<br/>" + Message.ADMIN_CREATION_ERROR);
	                    //form.setMessage(Message.ADMIN_CREATION_TITLE, requiredFields, Message.ERROR);
	                    return false;
	                }
	                
	            }
	           
	        }
	        return true;
	    }
	 
	 private boolean isLoggedInUser(String selectedUserName)
	    {
	        String loggedInUserName = this.user.getUserName(); 
	                      
	        if ((loggedInUserName != null) 
	                && (selectedUserName != null) 
	                && loggedInUserName.equals(selectedUserName)) {
	            return true;
	        }
	        return false;
	    }
	 
	 public static String requiredfieldMissing(UserProfileInformation form, List<OrganizationNode> selectedOrgNodes, boolean isLoginUser, String userName)
	    {
	        
	        // check for required fields
	        String requiredFields = null;
	        int requiredFieldCount = 0;
	        
	        String firstName = form.getFirstName().trim();
	        if ( firstName.length() == 0 ) {
	            requiredFieldCount += 1;            
	            requiredFields = Message.buildErrorString(Message.FIELD_FIRST_NAME, requiredFieldCount, requiredFields);       
	        }
	                
	        String lastName = form.getLastName().trim();
	        if ( lastName.length() == 0 ) {
	            requiredFieldCount += 1;            
	            requiredFields = Message.buildErrorString(Message.FIELD_LAST_NAME, requiredFieldCount, requiredFields);       
	        }
	        
	        //Time Zone is mandetory
	        String timeZone = form.getTimeZone().trim();
	        if ( timeZone== null || timeZone.length() == 0 ) {
	            requiredFieldCount += 1;            
	            requiredFields = Message.buildErrorString(Message.FIELD_TIME_ZONE, requiredFieldCount, requiredFields);       
	        }
	        
	        // for add user
	        if(userName == null || "".equals(userName)){
	            
	            //Role is mandetory
	            String role = form.getRoleId().trim();
	            if ( role==null || role.length() == 0 ) {
	                requiredFieldCount += 1;            
	                requiredFields = Message.buildErrorString(Message.FIELD_ROLE, requiredFieldCount, requiredFields);       
	            }
	        }
	        
	        if(!isLoginUser){							 
	             if ( selectedOrgNodes.size() == 0 ) {
	                requiredFieldCount += 1;      
	                requiredFields = Message.buildErrorString(Message.FIELD_ORG_ASSIGNMENT, requiredFieldCount, requiredFields);       
	            }   
	        }
	        
	        return requiredFields;
	    }
	 
	 
	 public static String isInvalidUserInfo(UserProfileInformation form)
	    {

		    String invalidString = null;        
	        String invalidCharFields = verifyUserInfo(form);
	                       
	        if (invalidCharFields != null && invalidCharFields.length() > 0) {
	            invalidString = invalidCharFields + ("<br/>" + Message.INVALID_NAME_CHARS);
	        }			
		 
	        String email = form.getEmail(); 
	        boolean validEmail = WebUtils.validEmail(email);  
	       
	        if (!validEmail) {
	            if(invalidString!=null && invalidString.length()>0){
	               invalidString += ("<br/>");
	            } else {
	            	invalidString = "";
	            }
	            invalidString += Message.FIELD_EMAIL + ("<br/>" + Message.INVALID_EMAIL);
	        } 
	            
	        return invalidString;   
	    }
	 
	 public static String verifyUserInfo(UserProfileInformation userProfile)
	    {
	        String invalidCharFields = null;
	        int invalidCharFieldCount = 0;

	        if (! WebUtils.validNameString(userProfile.getFirstName()) ) {
	            invalidCharFieldCount += 1;            
	            invalidCharFields = Message.buildErrorString(Message.FIELD_FIRST_NAME, invalidCharFieldCount, invalidCharFields);       
	        }
	        
	        if (! WebUtils.validNameString(userProfile.getMiddleName()) ) {
	            invalidCharFieldCount += 1;            
	            invalidCharFields = Message.buildErrorString(Message.FIELD_MIDDLE_NAME, invalidCharFieldCount, invalidCharFields);       
	        }
	        
	        if (! WebUtils.validNameString(userProfile.getLastName()) ) {
	            invalidCharFieldCount += 1;            
	            invalidCharFields = Message.buildErrorString(Message.FIELD_LAST_NAME, invalidCharFieldCount, invalidCharFields);       
	        }
	            
	        return invalidCharFields;
	    }
}
