package userOperation;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.Base;
import utils.BaseTree;
import utils.BroadcastUtils;
import utils.DateUtils;
import utils.FilterSortPageUtils;
import utils.MessageInfo;
import utils.MessageResourceBundle;
import utils.OptionList;
import utils.Organization;
import utils.OrgnizationComparator;
import utils.PermissionsUtils;
import utils.TreeData;
import utils.UserPasswordUtils;
import utils.UserPathListUtils;
import utils.UserSearchUtils;
import utils.WebUtils;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.BroadcastMessage;
import com.ctb.bean.testAdmin.BroadcastMessageData;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.OrganizationNode;
import com.ctb.bean.testAdmin.PasswordHintQuestion;
import com.ctb.bean.testAdmin.Role;
import com.ctb.bean.testAdmin.TimeZones;
import com.ctb.bean.testAdmin.USState;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserData;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.control.userManagement.UserManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.userManagement.UserPasswordUpdateException;
import com.ctb.util.SQLutils;
import com.ctb.util.userManagement.CTBConstants;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.google.gson.Gson;

import dto.Message;
import dto.PasswordInformation;
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

    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.BroadcastMessageLog message;

    
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
		getLoggedInUserPrincipal();
    	
        getUserDetails();
        
		setupUserPermission();
		
		List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
        this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
		
    }
    
	
    /**
     * getUserDetails
     */
    private void getUserDetails()
    {
        try {
            this.user = this.userManagement.getUser(this.userName, 
                                               this.userName);
            this.customerId = this.user.getCustomer().getCustomerId();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }        
        getSession().setAttribute("userName", this.userName);
        getSession().setAttribute("userRole", this.user.getRole().getRoleName());
        getSession().setAttribute("customerId", this.customerId);
        
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
			Collections.sort(selectedList, new OrgnizationComparator());
			ArrayList <Integer> orgIDList = new ArrayList <Integer>();
			ArrayList<TreeData> data = new ArrayList<TreeData>();

			UserNodeData und = UserPathListUtils.OrgNodehierarchy(this.userName, 
					this.userManagement, selectedList.get(0).getOrgNodeId()); 
			ArrayList<Organization> orgNodesList = UserPathListUtils.buildOrgNodehierarchyList(und, orgIDList,completeOrgNodeList);	

			//jsonTree = generateTree(orgNodesList,selectedList);

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
						UserNodeData undloop = UserPathListUtils.OrgNodehierarchy(this.userName, 
								this.userManagement,nodeId);   
						ArrayList<Organization> orgNodesListloop = UserPathListUtils.buildOrgNodehierarchyList(undloop, orgIDList, completeOrgNodeList);	
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
	 
	 /**
	  * This method is responsible for checking the selected user is editable or not.
	  * @param selectedUserOrgNodes
	  * @param loginUserOrgNodes
	  * @param selectedUserRoleId
	  * @param loginUserRoleId
	  * @return boolean
	  */
	 
	
	 private boolean isUserEditable (List<OrganizationNode> selectedUserOrgNodes, Node[] loginUserOrgNodes, 
			 Integer selectedUserRoleId, Integer loginUserRoleId) {
		 
		 if (selectedUserRoleId.intValue() == loginUserRoleId.intValue()) {
			 
			 if ( !verifyAdminCreationPermission
	                    (loginUserOrgNodes, selectedUserOrgNodes) ) {
	                return false;
	            }  
		 }
		 
		 return true;
	 }
	 
	/**
	 * This method is responsible to convert <code>Node[]</code> to <code>List<OrganizationNode></code>
	 * @param organizationNodes
	 * @return <code>List<OrganizationNode></code>
	 */
	 
	 private List<OrganizationNode> convertNodeArrToOrgList (Node[] organizationNodes) {
		 
		 List<OrganizationNode> selectedUserOrgNodes = new ArrayList <OrganizationNode>();
		 for (Node node : organizationNodes) {
			 OrganizationNode orgNode = new OrganizationNode();
			 orgNode.setOrgNodeId(node.getOrgNodeId());
			 orgNode.setCustomerId((Integer)getSession().getAttribute("customerId"));
			 selectedUserOrgNodes.add(orgNode);
			
		}
		 
		 return selectedUserOrgNodes;
	 }

	 /*Added on 24.10.2011
	  * for editUserDetails functionality
	  */
	 @Jpf.Action(forwards={
				@Jpf.Forward(name = "success", 
						path ="find_user_by_hierarchy.jsp")
		})
		protected Forward getUserDetailsForEdit(userOperationForm form){
			String jsonResponse = "";
			OutputStream stream = null;
			UserProfileInformation userProfileData = null;
			Boolean isLasLinkCustomer = new Boolean(getRequest().getParameter("isLasLinkCustomer"));
			String selectedUserName = getRequest().getParameter("selectedUserName");//added on 24.10.2011
			//String selectedUserName = "sumit_sardar";
			if(selectedUserName!=null){
				selectedUserName=selectedUserName.trim();
			}
			System.out.println("selectedUserName ::"+selectedUserName);
			HttpServletRequest req = getRequest();
			HttpServletResponse resp = getResponse();
			System.out.println("userName ::"+this.userName);
			try{
				userProfileData = UserSearchUtils.getUserProfileInformation(this.userManagement, this.userName, selectedUserName);
				if (!isUserEditable(convertNodeArrToOrgList(userProfileData.getOrganizationNodes()),
						this.user.getOrganizationNodes(),Integer.valueOf(userProfileData.getRoleId()),
						this.user.getRole().getRoleId())) {
					
					userProfileData.setViewMode(Boolean.TRUE);
				}
				
			}catch (CTBBusinessException e) {
				e.printStackTrace();
			}
			System.out.println("check1");
			try {
				
				OptionList optionList = new OptionList();
				optionList.setRoleOptions(getRoleOptions(ACTION_ADD_USER));
				optionList.setTimeZoneOptions(getTimeZoneOptions(ACTION_ADD_USER));
				optionList.setStateOptions(getStateOptions(ACTION_ADD_USER));
				
//				User selectedUserDetails = this.getSelectedUserDetails(userName);
				userProfileData.setOptionList(optionList);
				System.out.println("check2");
				if (userProfileData.getViewMode()) {
					if(userProfileData.getTimeZone() != null){
						userProfileData.setTimeZoneDesc(userProfileData.covertTimeCodeToTimeDesc
								(userProfileData.getTimeZone()));
					}
				}
				
				try {
					Gson gson = new Gson();
					String json = gson.toJson(userProfileData);
					System.out.println(json);
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
			userProfile.getUserContact().setPrimaryPhone1(getRequest().getParameter("primaryPhone1").trim());
			userProfile.getUserContact().setPrimaryPhone2(getRequest().getParameter("primaryPhone2").trim());
			userProfile.getUserContact().setPrimaryPhone3(getRequest().getParameter("primaryPhone3").trim());
			userProfile.getUserContact().setPrimaryPhone4(getRequest().getParameter("primaryPhone4").trim());
			userProfile.getUserContact().setSecondaryPhone1(getRequest().getParameter("secondaryPhone1").trim());
			userProfile.getUserContact().setSecondaryPhone2(getRequest().getParameter("secondaryPhone2").trim());
			userProfile.getUserContact().setSecondaryPhone3(getRequest().getParameter("secondaryPhone3").trim());
			userProfile.getUserContact().setSecondaryPhone4(getRequest().getParameter("secondaryPhone4").trim());
			userProfile.getUserContact().setFaxNumber1(getRequest().getParameter("faxNumber1"));
			userProfile.getUserContact().setFaxNumber2(getRequest().getParameter("faxNumber2"));
			userProfile.getUserContact().setFaxNumber3(getRequest().getParameter("faxNumber3"));
			
			int userId = 0;
			//int addressId = 0;
			String addressId = null;
			if(getRequest().getParameter("userId")!=null){
				userId = Integer.valueOf(getRequest().getParameter("userId"));
				userProfile.setUserId(userId);
			}
			if (userId != 0){
				addressId = this.userManagement.getAddressIdFromUserId(userId);
			}
			System.out.println("addressId::"+addressId);
			if(addressId != null) {
				userProfile.setAddressId(Integer.valueOf(addressId));
			}
			
			String assignedOrgNodeIds = getRequest().getParameter("assignedOrgNodeIds");
			String[] assignedOrgNodeId = assignedOrgNodeIds.split(",");
			ArrayList<OrganizationNode> selectedOrgNodes = new ArrayList<OrganizationNode>();
			
			try {
				
				for (int i = assignedOrgNodeId.length - 1; i >= 0; i--) {
					 String [] values = assignedOrgNodeId[i].split("\\|");
					 OrganizationNode orgNode = new OrganizationNode();
					 orgNode.setOrgNodeId(Integer.parseInt(values[0].trim()));
					 orgNode.setCustomerId((Integer)getSession().getAttribute("customerId"));
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
					message = isInvalidUserInfo ;
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
			
			userName = getRequest().getParameter("selectedUserName");
			
			boolean isCreateNew = (userName == null || "".equals(userName)) ? true : false;
			
			boolean result = true;
			User user = null;
			try {
				
				
				if(validInfo) {
					CustomerConfiguration[]  customerConfigurations = this.users.getCustomerConfigurations(this.customerId);
					userName = saveUserProfileInformation(isCreateNew, userProfile, userName, selectedOrgNodes);
				       
					
					
					if (isCreateNew)
					{
						if (userName != null)  {
							userProfile.setUserName(userName);
							messageInfo = createMessageInfo(messageInfo, Message.ADD_TITLE, Message.ADD_SUCCESSFUL, Message.INFORMATION, false, true );
							try {
								user = userManagement.getUser(this.userName, userName);
								if(user != null){
									userProfile.setRole((user.getRole().getRoleName()));
									userProfile.setUserId(user.getUserId());
								}
							} catch (CTBBusinessException e) {
								e.printStackTrace();
							}
							messageInfo.setUserProfile(userProfile);
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
							userProfile.setUserName(userName);
							messageInfo = createMessageInfo(messageInfo, Message.EDIT_TITLE, Message.EDIT_SUCCESSFUL, Message.INFORMATION, false, true );
							try {
								user = userManagement.getUser(this.userName, userName);
								if(user != null){
									userProfile.setRole((user.getRole().getRoleName()));
								}
							} catch (CTBBusinessException e) {
								e.printStackTrace();
							}
							messageInfo.setUserProfile(userProfile);
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
	 
	@Jpf.Action()
	protected Forward saveUserProfile(userOperationForm form)
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
		userProfile.setFirstName(getRequest().getParameter("profileFirstName"));
		userProfile.setMiddleName(getRequest().getParameter("profileMiddleName"));
		userProfile.setLastName(getRequest().getParameter("profileLastName"));
		userProfile.setEmail(getRequest().getParameter("profileEmail"));
		userProfile.setTimeZone(getRequest().getParameter("profileTimeZoneOptions"));
		userProfile.setRoleId(getRequest().getParameter("profileRoleOptions"));
		userProfile.setRole(getRequest().getParameter("profileRoleName"));
		userProfile.setExtPin1(getRequest().getParameter("profileExternalId"));
		userProfile.getUserContact().setAddressLine1(getRequest().getParameter("profileAddressLine1"));
		userProfile.getUserContact().setAddressLine2(getRequest().getParameter("profileAddressLine2"));
		userProfile.getUserContact().setCity(getRequest().getParameter("profileCity"));
		userProfile.getUserContact().setState(getRequest().getParameter("profileStateOptions"));
		userProfile.getUserContact().setZipCode1(getRequest().getParameter("profileZipCode1"));
		userProfile.getUserContact().setZipCode2(getRequest().getParameter("profileZipCode2"));
		userProfile.getUserContact().setPrimaryPhone1(getRequest().getParameter("profilePrimaryPhone1").trim());
		userProfile.getUserContact().setPrimaryPhone2(getRequest().getParameter("profilePrimaryPhone2").trim());
		userProfile.getUserContact().setPrimaryPhone3(getRequest().getParameter("profilePrimaryPhone3").trim());
		userProfile.getUserContact().setPrimaryPhone4(getRequest().getParameter("profilePrimaryPhone4").trim());
		userProfile.getUserContact().setSecondaryPhone1(getRequest().getParameter("profileSecondaryPhone1").trim());
		userProfile.getUserContact().setSecondaryPhone2(getRequest().getParameter("profileSecondaryPhone2").trim());
		userProfile.getUserContact().setSecondaryPhone3(getRequest().getParameter("profileSecondaryPhone3").trim());
		userProfile.getUserContact().setSecondaryPhone4(getRequest().getParameter("profileSecondaryPhone4").trim());
		userProfile.getUserContact().setFaxNumber1(getRequest().getParameter("profileFaxNumber1"));
		userProfile.getUserContact().setFaxNumber2(getRequest().getParameter("profileFaxNumber2"));
		userProfile.getUserContact().setFaxNumber3(getRequest().getParameter("profileFaxNumber3"));	
		userName = getRequest().getParameter("loginUserName");
		String oldPassword = getRequest().getParameter("profileOldPassword");
		String newPassword = getRequest().getParameter("profileNewPassword");
		String confirmPassword = getRequest().getParameter("profileConfirmPassword");	
		String hintQues = getRequest().getParameter("profileHintQues");
		String hintAns = getRequest().getParameter("profileHintAns");
		
		int userId = 0;		
		String addressId = null;
		if(getRequest().getParameter("userId")!=null){
			userId = Integer.valueOf(getRequest().getParameter("userId"));
			userProfile.setUserId(userId);
		}
		/*if(this.user.getUserId()!=null){
			userId = this.user.getUserId();
			userProfile.setUserId(userId);
		}*/
		if (userId != 0){
			addressId = this.userManagement.getAddressIdFromUserId(userId);
		}
		System.out.println("addressId::"+userId+"::"+this.userName+"::"+addressId);
		if(addressId != null) {
			userProfile.setAddressId(Integer.valueOf(addressId));
		}
		
		String assignedOrgNodeIds = getRequest().getParameter("assignedOrgNodeIds");
		String[] assignedOrgNodeId = assignedOrgNodeIds.split(",");
		ArrayList<OrganizationNode> selectedOrgNodes = new ArrayList<OrganizationNode>();
		
		try {
			
			for (int i = assignedOrgNodeId.length - 1; i >= 0; i--) {
				 String [] values = assignedOrgNodeId[i].split("\\|");
				 OrganizationNode orgNode = new OrganizationNode();
				 orgNode.setOrgNodeId(Integer.parseInt(values[0].trim()));
				 orgNode.setCustomerId((Integer)getSession().getAttribute("customerId"));
				 selectedOrgNodes.add(orgNode);
				
			}
		} catch (NumberFormatException ne ) {
			ne.printStackTrace();
			validateAgain = false;
			requiredFields = "Organization Assignment";
			message = requiredFields + (Message.REQUIRED_TEXT);
			messageInfo = createMessageInfo(messageInfo, Message.MISSING_REQUIRED_FIELDS, message, Message.ERROR, true, false );
		}
		
		/*System.out.println(getSession().getAttribute("userRole"));
		userProfile.setRole(this.user.getRole().getRoleName());*/
		
		boolean isLoginUser = true;		
		requiredFields = requiredfieldMissing(userProfile, selectedOrgNodes, isLoginUser, this.userName );
		
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
				message = isInvalidUserInfo ;
				messageInfo = createMessageInfo(messageInfo, Message.INVALID_FORMAT_TITLE, message, Message.ERROR, true, false );
			}
		}
		//userName = this.userName;		
		User loginUser = this.getLoginUserDetails(this.userManagement, userName);
		PasswordInformation passwordinfo = new PasswordInformation();
		passwordinfo.setOldPassword(oldPassword);
		passwordinfo.setNewPassword(newPassword);
		passwordinfo.setConfirmPassword(confirmPassword);
		passwordinfo.setHintQuestionId(hintQues);
		passwordinfo.setHintAnswer(hintAns);
		 
		//requiredFields = UserPasswordUtils.getRequiredPasswordField(passwordinfo);
		if(!passwordinfo.getOldPassword().trim().equals("") ||!passwordinfo.getNewPassword().trim().equals("") || !passwordinfo.getConfirmPassword().trim().equals("")){
			if (validateAgain) {
				 String invalidCharFields = UserPasswordUtils.verifyPasswordInfo(passwordinfo);
				 String invalidString = "";

				 if (invalidCharFields != null && invalidCharFields.length() > 0) {					 
					 if ( invalidCharFields.indexOf(",") > 0){						 
						 invalidString = invalidCharFields + ("<br/>" + Message.INVALID_DEX_PASSWORD);						 
					 } else {						 
						 invalidString = invalidCharFields + ("<br/>" + Message.INVALID_DEX_PASSWORD_SINGLE_LINE);						 
					 }						
				 }

				 if (invalidString != null && invalidString.length() > 0) {
					 validateAgain = false;
					 //validationPassed = false;
				 	 //message = invalidString + (" <br/> " + Message.PASSWORD_MISMATCH);
					 messageInfo = createMessageInfo(messageInfo, Message.INVALID_CHARS_TITLE, invalidString, Message.ERROR, true, false );
				 }
				 
				if (validateAgain) {
					 boolean isNewAndConfirmPasswordDifferent = UserPasswordUtils.isNewAndConfirmPasswordDifferent(passwordinfo);
					 if(isNewAndConfirmPasswordDifferent) {
						 validateAgain = false;
					 	 messageInfo = createMessageInfo(messageInfo, Message.INVALID_DATA_TITLE, Message.PASSWORD_MISMATCH, Message.ERROR, true, false );
					 }
				 }
			}
			
			if(validateAgain){				
				userProfile.setUserPassword(passwordinfo);
			}
		}
		
		
		boolean result = true;
		User user = null;
		if(validateAgain){
			try{
				userName = saveUserProfileDetails(userProfile, userName, selectedOrgNodes, messageInfo);
			}
			catch (CTBBusinessException be) {
				userName = null;
				if(be.getMessage() == "ChangePassword.InvalidOldPassword"){
					messageInfo = createMessageInfo(messageInfo, Message.INVALID_DATA_TITLE, Message.WRONG_PASSWORD, Message.INFORMATION, true, false );
				}else if(be.getMessage() == "ChangePassword.PasswordRepeated"){
					messageInfo = createMessageInfo(messageInfo, Message.INVALID_DATA_TITLE, Message.REPEATED_PASSWORD, Message.INFORMATION, true, false );
				}else{	    			
					messageInfo = createMessageInfo(messageInfo, Message.INVALID_DATA_TITLE, Message.PROFILE_EDIT_ERROR, Message.INFORMATION, true, false );
				}
			}            
			catch (Exception e) {
				e.printStackTrace();
				userName = null;
				messageInfo = createMessageInfo(messageInfo, Message.PROFILE_TITLE, Message.PROFILE_EDIT_ERROR, Message.INFORMATION, true, false );
			}

			if (userName != null) {
				userProfile.setUserName(userName);
				messageInfo = createMessageInfo(messageInfo, Message.PROFILE_TITLE, Message.PROFILE_EDIT_SUCCESSFUL, Message.INFORMATION, false, true );
				try {
					user = userManagement.getUser(userName, userName);
					if(user != null){
						userProfile.setRole((user.getRole().getRoleName()));
					}
				} catch (CTBBusinessException e) {
					e.printStackTrace();
				}
				messageInfo.setUserProfile(userProfile);
			}
			else  {
				//messageInfo = createMessageInfo(messageInfo, Message.INVALID_DATA_TITLE, Message.PROFILE_EDIT_ERROR, Message.INFORMATION, true, false );


			}
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
	    	User user = null;
        	user = userProfile.makeCopy(userName, selectedOrgNodes);
        	String title = null;
	        String username = user.getUserName();
	        System.out.println("username>>"+username);
	        try {                    
	            if (isCreateNew) {
	                username = this.userManagement.createUser(this.userName, user);
	            } else {
	            	this.userManagement.updateUser(this.userName, user);
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
	    
	    private String saveUserProfileDetails(
				UserProfileInformation userProfile, 
                String userName, 
                List selectedOrgNodes,
                MessageInfo messageInfo) throws CTBBusinessException
	    {       
	    	User user = null;
	    	user = userProfile.makeCopy(userName, selectedOrgNodes);
	    	String title = null;
	    	String username = user.getUserName();
	    	System.out.println("username>>"+username);
	    	try { 
	    		this.userManagement.updateUser(this.userName, user);
	    	} 
	    	catch (CTBBusinessException be) {
	    		be.printStackTrace();
	    		username = null;
	    		throw be;
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
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "sessionsLink", path = "assessments_sessionsLink.do"),
			@Jpf.Forward(name = "programStatusLink", path = "assessments_programStatusLink.do"),
			@Jpf.Forward(name = "studentRegistrationLink", path = "assessments_studentRegistrationLink.do")
	})   
	protected Forward assessments()
	{
        
		String menuId = (String)this.getRequest().getParameter("menuId");    	
		String forwardName = (menuId != null) ? menuId : "sessionsLink";
		
	    return new Forward(forwardName);	    
	}
	

    @Jpf.Action()
	protected Forward assessments_sessionsLink()
	{
        try
        {
            String url = "/SessionWeb/sessionOperation/assessments_sessions.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
    
    @Jpf.Action()
	protected Forward assessments_programStatusLink()
	{
        try
        {
            String url = "/SessionWeb/programOperation/assessments_programStatus.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
    /**
     * STUDENT REGISTRATION actions
     */
    @Jpf.Action()
    protected Forward assessments_studentRegistrationLink()
    {
        try
        {
        	String url = "/RegistrationWeb/registrationOperation/beginStudentRegistration.do";
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
	        @Jpf.Forward(name = "studentsLink", path = "organizations_manageStudents.do"),
	        @Jpf.Forward(name = "usersLink", path = "organizations_manageUsers.do"),
	        @Jpf.Forward(name = "organizationsLink", path = "organizations_manageOrganizations.do"),
	        @Jpf.Forward(name = "bulkAccomLink", path = "organizations_manageBulkAccommodation.do"),
	        @Jpf.Forward(name = "bulkMoveLink", path = "organizations_manageBulkMove.do"),
	        @Jpf.Forward(name = "OOSLink", path = "organizations_manageOutOfSchool.do")
	    }) 
	protected Forward organizations()
	{
		String menuId = (String)this.getRequest().getParameter("menuId");    	
		String forwardName = (menuId != null) ? menuId : "studentsLink";
		
	    return new Forward(forwardName);
	}
	
    @Jpf.Action()
	protected Forward organizations_manageOrganizations()
	{
        try
        {
            String url = "/OrganizationWeb/orgOperation/organizations_manageOrganizations.do";
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
            String url = "/StudentWeb/studentOperation/organizations_manageStudents.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
	
    @Jpf.Action()
	protected Forward organizations_manageBulkAccommodation()
	{
        try
        {
            String url = "/StudentWeb/bulkOperation/organizations_manageBulkAccommodation.do";
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
	

    @Jpf.Action()
	protected Forward organizations_manageBulkMove()
	{
        try
        {
            String url = "/StudentWeb/bulkMoveOperation/organizations_manageBulkMove.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
    
    @Jpf.Action()
	protected Forward organizations_manageOutOfSchool()
	{
        try
        {
            String url = "/StudentWeb/outOfSchoolOperation/organizations_manageOutOfSchool.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}


    /**
     * REPORTS actions
     */    
    @Jpf.Action()
    protected Forward reports()
    {
        try
        {
            String url = "/SessionWeb/sessionOperation/reports.do";
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
	        @Jpf.Forward(name = "resetTestSessionLink", path = "services_resetTestSession.do"),
	        @Jpf.Forward(name = "manageLicensesLink", path = "services_manageLicenses.do"),
	        @Jpf.Forward(name = "installSoftwareLink", path = "services_installSoftware.do"),
	        @Jpf.Forward(name = "downloadTestLink", path = "services_downloadTest.do"),
	        @Jpf.Forward(name = "uploadDataLink", path = "services_uploadData.do"),
	        @Jpf.Forward(name = "downloadDataLink", path = "services_downloadData.do"),
	        @Jpf.Forward(name = "exportDataLink", path = "services_dataExport.do"),
	        @Jpf.Forward(name = "viewStatusLink", path = "services_viewStatus.do")
	    }) 
	protected Forward services()
	{
		String menuId = (String)this.getRequest().getParameter("menuId");    	
		String forwardName = (menuId != null) ? menuId : "installSoftwareLink";
		
	    return new Forward(forwardName);
	}
	
	@Jpf.Action()
    protected Forward services_dataExport()
    {
    	try
    	{
    		String url = "/ExportWeb/dataExportOperation/services_dataExport.do";
    		getResponse().sendRedirect(url);
    	}
    	catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
    	return null;
    }
	
	@Jpf.Action()
    protected Forward services_resetTestSession()
    {
        try
        {
            String url = "/OrganizationWeb/resetOperation/services_resetTestSession.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
    
	
    @Jpf.Action()
    protected Forward services_manageLicenses()
    {
        try
        {
            String url = "/OrganizationWeb/licenseOperation/services_manageLicenses.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
    
    




       @Jpf.Action()
   	protected Forward services_viewStatus()
   	{
           try
           {
               String url = "/ExportWeb/dataExportOperation/beginViewStatus.do";
               getResponse().sendRedirect(url);
           } 
           catch (IOException ioe)
           {
               System.err.print(ioe.getStackTrace());
           }
           return null;
   	}


    
    
    
    
    
    
	
    @Jpf.Action()
	protected Forward services_installSoftware()
	{
        try
        {
            String url = "/SessionWeb/softwareOperation/services_installSoftware.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
	
    @Jpf.Action() 
	protected Forward services_downloadTest()
	{
		 try
	        {
	            String url = "/SessionWeb/testContentOperation/services_downloadTest.do";
	            getResponse().sendRedirect(url);
	        } 
	        catch (IOException ioe)
	        {
	            System.err.print(ioe.getStackTrace());
	        }
	        return null;
	}
	
    @Jpf.Action()
	protected Forward services_uploadData()
	{
        try
        {
            String url = "/OrganizationWeb/uploadOperation/services_uploadData.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
	
    @Jpf.Action()
	protected Forward services_downloadData()
	{
        try
        {
            String url = "/OrganizationWeb/downloadOperation/services_downloadData.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
    
	@Jpf.Action()
    protected Forward broadcastMessage()
    {
        HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		
		if (this.userName == null) {
			getLoggedInUserPrincipal();
			this.userName = (String)getSession().getAttribute("userName");
		}
		
		List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
        String bcmString = BroadcastUtils.buildBroadcastMessages(broadcastMessages);
		
		try{
    		resp.setContentType(CONTENT_TYPE_JSON);
			try {
				stream = resp.getOutputStream();
	    		resp.flushBuffer();
	    		stream.write(bcmString.getBytes());
			} 
			finally {
				if (stream!=null){
					stream.close();
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
        
        return null;
    }

    
	/**
	 * @jpf:action
	 */    
	@Jpf.Action()
	protected Forward myProfile()
	{
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		UserProfileInformation loginUserDetails = null;
		
		if (this.userName == null) {
			getLoggedInUserPrincipal();
			this.userName = (String)getSession().getAttribute("userName");
		}
		
		try{
			loginUserDetails = UserSearchUtils.getUserProfileInformation(this.userManagement, this.userName, this.userName);
			
		}catch (CTBBusinessException e) {
			e.printStackTrace();
		}
		try {
			
			OptionList optionList = new OptionList();
			optionList.setTimeZoneOptions(getTimeZoneOptions(ACTION_ADD_USER));
			optionList.setStateOptions(getStateOptions(ACTION_ADD_USER));
			optionList.setHintQuesOptions(getHintQuesOptions(ACTION_ADD_USER));
			
			loginUserDetails.setOptionList(optionList);
			//loginUserDetails.setTimeZoneDesc(loginUserDetails.covertTimeCodeToTimeDesc(loginUserDetails.getTimeZone()));
			
			try {
				Gson gson = new Gson();
				String json = gson.toJson(loginUserDetails);
				System.out.println(json);
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
		catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	/**
     * STUDENT SCORING actions
     */    
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "studentScoringLink", path = "scoring_studentScoring.do")
        }) 
    protected Forward studentScoring()
    {
    	String menuId = (String)this.getRequest().getParameter("menuId");    	
    	String forwardName = (menuId != null) ? menuId : "studentScoringLink";
    	
        return new Forward(forwardName);
    }
    
    @Jpf.Action()
	protected Forward scoring_studentScoring()
	{
        try
        {
            String url = "/ScoringWeb/studentScoringOperation/beginStudentScoring.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}


    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
    private void getLoggedInUserPrincipal()
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
        if (principal != null) {
            this.userName = principal.toString();
        }        
        getSession().setAttribute("userName", this.userName);
    }
    
private void setUpAllUserPermission(CustomerConfiguration [] customerConfigurations) {
    	
    	boolean hasBulkStudentConfigurable = false;
    	boolean hasBulkStudentMoveConfigurable = false;
    	boolean hasOOSConfigurable = false;
    	boolean adminUser = isAdminUser();
    	boolean hasUploadDownloadConfig = false;
    	boolean hasProgramStatusConfig = false;
    	boolean hasScoringConfigurable = false;
    	boolean hasLicenseConfiguration= false;
    	boolean TABECustomer = false;
    	boolean adminCoordinatorUser = isAdminCoordinatotUser(); //For Student Registration
    	String roleName = this.user.getRole().getRoleName();
    	boolean isOKCustomer = false;
    	boolean isGACustomer = false;
    	boolean isTopLevelAdmin = new Boolean(isTopLevelUser() && isAdminUser());
    	boolean laslinkCustomer = false;
    	boolean hasResetTestSession = false;
    	
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				// For Bulk Accommodation
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentConfigurable = true;
					continue;
				}
				// For Bulk Student Move
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Bulk_Move_Students") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentMoveConfigurable = true;
					continue;
				}
				// For Out Of School Student
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("OOS_Configurable") && 
						cc.getDefaultValue().equals("T")) {
					hasOOSConfigurable = true;
					continue;
				}
				// For Upload Download
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Upload_Download")
						&& cc.getDefaultValue().equals("T")) {
					hasUploadDownloadConfig = true;
					continue;
	            }
				// For Program Status
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Program_Status") && 
						cc.getDefaultValue().equals("T")) {
					hasProgramStatusConfig = true;
					continue;
				}
				// For Hand Scoring
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
	            		cc.getDefaultValue().equals("T")	) {
					hasScoringConfigurable = true;
					continue;
	            }
				//For License
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subscription") && 
	            		cc.getDefaultValue().equals("T")	) {
					hasLicenseConfiguration = true;
					continue;
	            }
				// For TABE Customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer")) {
	            	TABECustomer = true;
	            	continue;
	            }

				if (cc.getCustomerConfigurationName().equalsIgnoreCase("OK_Customer")
						&& cc.getDefaultValue().equals("T")) {
	            	isOKCustomer = true;
	            }
				if ((cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID") 
						&& cc.getDefaultValue().equalsIgnoreCase("T"))	|| 
						(cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID_2") 
								&& cc.getDefaultValue().equalsIgnoreCase("T"))){
					isGACustomer = true;
				}
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")) {
	            	laslinkCustomer = true;
	            	continue;
	            }
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Reopen_Subtest") && 
	            		cc.getDefaultValue().equals("T")	) {
					hasResetTestSession = true;
	            }
			}
			
		}
		this.getSession().setAttribute("isBulkAccommodationConfigured",new Boolean(hasBulkStudentConfigurable));
		this.getSession().setAttribute("isBulkMoveConfigured",new Boolean(hasBulkStudentMoveConfigurable));
		this.getSession().setAttribute("isOOSConfigured",new Boolean(hasOOSConfigurable));
		this.getSession().setAttribute("hasUploadDownloadConfigured",new Boolean(hasUploadDownloadConfig && adminUser));
		this.getSession().setAttribute("hasProgramStatusConfigured",new Boolean(hasProgramStatusConfig && adminUser));
		this.getSession().setAttribute("hasScoringConfigured",new Boolean(hasScoringConfigurable));
		this.getSession().setAttribute("hasLicenseConfigured",new Boolean(hasLicenseConfiguration && adminUser));
		this.getSession().setAttribute("adminUser", new Boolean(adminUser));
		boolean validUser = (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || 
        		roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
		this.getSession().setAttribute("canRegisterStudent", new Boolean(TABECustomer && validUser));
		this.getSession().setAttribute("hasRapidRagistrationConfigured", new Boolean(TABECustomer && (adminUser || adminCoordinatorUser) ));//For Student Registration
		this.getSession().setAttribute("hasResetTestSession", new Boolean(hasResetTestSession && ((isOKCustomer && isTopLevelAdmin)||(laslinkCustomer && isTopLevelAdmin)||(isGACustomer && adminUser))));
		this.getSession().setAttribute("hasAuditingResetTestSession", new Boolean(hasResetTestSession && (laslinkCustomer && isTopLevelAdmin)));
    }

	private boolean isAdminCoordinatotUser() //For Student Registration
	{               
		String roleName = this.user.getRole().getRoleName();        
		return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR); 
	}
	
	private boolean isTopLevelUser(){
		boolean isUserTopLevel = false;
		try {
			isUserTopLevel = orgnode.checkTopOrgNodeUser(this.userName);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUserTopLevel;
	}
    
	private void setupUserPermission()
	{
        CustomerConfiguration [] customerConfigs = getCustomerConfigurations(this.customerId);

        boolean laslinkCustomer = isLaslinkCustomer(customerConfigs);
        
        setUpAllUserPermission(customerConfigs);
        
        this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));   	
    	
    	this.getRequest().setAttribute("isLasLinkCustomer", laslinkCustomer);  
    	
    	this.getRequest().setAttribute("isTopLevelUser",isTopLevelUser(laslinkCustomer));
    	
    	this.getRequest().setAttribute("customerConfigurations", customerConfigs);
     	
     	this.getSession().setAttribute("isDeleteUserEnable", isDeleteUserEnable());
    
     	this.getSession().setAttribute("showDataExportTab",laslinkCustomer);
	}


    private Boolean userHasReports() 
    {
        boolean hasReports = false;
        try
        {      
            Customer customer = this.user.getCustomer();
            Integer customerId = customer.getCustomerId();   
            hasReports = this.userManagement.userHasReports(this.userName, customerId);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }        
        return new Boolean(hasReports);           
    }

    private boolean isAdminUser() 
    {               
        String roleName = this.user.getRole().getRoleName();        
        return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR); 
    }

    private boolean isLaslinkCustomer(CustomerConfiguration [] customerConfigs)
    {               
        boolean laslinkCustomer = false;
        
        for (int i=0; i < customerConfigs.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")
					&& cc.getDefaultValue().equals("T")) {
            	laslinkCustomer = true;
            }
        }
        return laslinkCustomer;
    }
    
    private CustomerConfiguration [] getCustomerConfigurations(Integer customerId)
    {               
        CustomerConfiguration [] ccArray = null;
        try
        {      
            ccArray = this.users.getCustomerConfigurations(customerId);       
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }        
        return ccArray;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// END OF SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
    
	/*private String generateTree (ArrayList<Organization> orgNodesList,ArrayList<Organization> selectedList) throws Exception{	

		Organization org = orgNodesList.get(0);
		TreeData td = new TreeData ();
		td.setData(org.getOrgName());
		td.getAttr().setId(org.getOrgNodeId().toString());
		td.getAttr().setCategoryID(org.getOrgCategoryLevel().toString());
		treeProcess (org,orgNodesList,td,selectedList);
		BaseTree baseTree = new BaseTree ();
		baseTree.getData().add(td);
		Gson gson = new Gson();

		String json = gson.toJson(baseTree);

		return json;
	}*/

	
    private static void preTreeProcess (ArrayList<TreeData> data,ArrayList<Organization> orgList, ArrayList<Organization> selectedList) {

		Organization org = orgList.get(0);
		Integer rootCategoryLevel = 0;
		TreeData td = new TreeData ();
		td.setData(org.getOrgName());
		td.getAttr().setId(org.getOrgNodeId().toString());
		td.getAttr().setCid(org.getOrgCategoryLevel().toString());
		rootCategoryLevel = org.getOrgCategoryLevel();
		td.getAttr().setTcl("1");
		org.setTreeLevel(1);
		Map<Integer, Organization> orgMap = new HashMap<Integer, Organization>();
		orgMap.put(org.getOrgNodeId(), org);
		treeProcess (org, orgList, td, selectedList, rootCategoryLevel, orgMap);
		data.add(td);
	}
	
    private static void treeProcess (Organization org,List<Organization> list,TreeData td, 
    		ArrayList<Organization> selectedList, Integer rootCategoryLevel, 
    		Map<Integer, Organization> orgMap) {

		Integer treeLevel = 0;
		Organization parentOrg = null;
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
				tempData.getAttr().setCid(tempOrg.getOrgCategoryLevel().toString());
				parentOrg = orgMap.get(tempOrg.getOrgParentNodeId());
				treeLevel = parentOrg.getTreeLevel() + 1;
				tempOrg.setTreeLevel(treeLevel);
				tempData.getAttr().setTcl(treeLevel.toString());
				td.getChildren().add(tempData);
				orgMap.put(tempOrg.getOrgNodeId(), tempOrg);
				treeProcess (tempOrg, list, tempData, selectedList, rootCategoryLevel, orgMap);
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
	
	private String [] getHintQuesOptions(String action)
    {        
		PasswordHintQuestion[] hintQuestions = null;
		
		try {
			hintQuestions =  this.userManagement.getHintQuestions();
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		
		
		List<String> hintQuesOptions = new ArrayList<String>();
       /*if (action.equals(ACTION_ADD_USER)) {	
        	hintQuesOptions.add(-1 + "|"+Message.SELECT_HINE_QUES);
        }
        */
        try {
            if (hintQuestions != null) {
                for (int i = 0; i < hintQuestions.length ; i++) {
                	hintQuesOptions.add(hintQuestions[i].getPasswordHintQuestionId()+"|"+hintQuestions[i].getPasswordHintQuestion());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return hintQuesOptions.toArray( new String[hintQuesOptions.size()]);
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
        	stateOptions.add("" + "|"+Message.SELECT_STATE);
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
	                    (user.getOrganizationNodes(), selectedOrgNodes) ) {
	                return false;
	            }  
	        }
	          
	        /*if (isInvalidUserContact(form)){
	            return false;
	        }*/
	        return true;
	    }
	 
	 public static boolean verifyAdminCreationPermission ( Node []loginUserNodes, List<OrganizationNode> selectedOrgNodes)
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
	 
	 @Jpf.Action(forwards={
				@Jpf.Forward(name = "success", 
						path ="find_user_by_hierarchy.jsp")
		})
		protected Forward saveUserPassword(String selectedUserName){
		 String message = "";
		 String requiredFields = null;
		 boolean revalidate = true;
		 boolean validationPassed = true;
		 MessageInfo messageInfo = new MessageInfo();
		 OutputStream stream = null;
		 HttpServletRequest req = getRequest();
		 HttpServletResponse resp = getResponse();
		 String userName = getRequest().getParameter("userName");
		 String newPassword = getRequest().getParameter("newPassword");
		 String confirmPassword = getRequest().getParameter("confirmPassword");
		 
		 //String userName = UserPasswordUtils.getSelectedUserName(userId);
		 //String userName = "01aa_01bb";
		 
		 User selectedUser = this.getSelectedUserDetails(userName);
		 String oldPassword = selectedUser.getPassword();
		 PasswordInformation passwordinfo = new PasswordInformation();
		 passwordinfo.setOldPassword(oldPassword);
		 passwordinfo.setNewPassword(newPassword);
		 passwordinfo.setConfirmPassword(confirmPassword);
		 
		 requiredFields = UserPasswordUtils.getRequiredPasswordField(passwordinfo);
		 if( requiredFields != null){
			 revalidate = false;
			 validationPassed = false;
				if ( requiredFields.indexOf(",") > 0){
					message = requiredFields + (" <br/> " + Message.REQUIRED_TEXT_MULTIPLE);
					messageInfo = createMessageInfo(messageInfo, Message.MISSING_REQUIRED_FIELDS, message, Message.ERROR, true, false );
				}
				else {
					message = requiredFields + (" <br/> " + Message.REQUIRED_TEXT);
					messageInfo = createMessageInfo(messageInfo, Message.MISSING_REQUIRED_FIELD, message, Message.ERROR, true, false );

				}
			}
		 else if (revalidate) {
			 String invalidCharFields = UserPasswordUtils.verifyPasswordInfo(passwordinfo);
			 String invalidString = "";

				 if (invalidCharFields != null && invalidCharFields.length() > 0) {
					 
					 if ( invalidCharFields.indexOf(",") > 0){
						 
						 invalidString = invalidCharFields + ("<br/>" + Message.INVALID_DEX_PASSWORD);
						 
					 } else {
						 
						 invalidString = invalidCharFields + ("<br/>" + Message.INVALID_DEX_PASSWORD_SINGLE_LINE);
						 
					 }
	
						
				 }

				 if (invalidString != null && invalidString.length() > 0) {
					 revalidate = false;
					 validationPassed = false;
				 	 //message = invalidString + (" <br/> " + Message.PASSWORD_MISMATCH);
					 messageInfo = createMessageInfo(messageInfo, Message.INVALID_CHARS_TITLE, invalidString, Message.ERROR, true, false );
				 }
				 
				if (revalidate) {
					 boolean isNewAndConfirmPasswordDifferent = UserPasswordUtils.isNewAndConfirmPasswordDifferent(passwordinfo);
					 
					 if(isNewAndConfirmPasswordDifferent) {
						 validationPassed = false;
					 	 messageInfo = createMessageInfo(messageInfo, Message.CHANGE_PASSWORD_TITLE, Message.PASSWORD_MISMATCH, Message.ERROR, true, false );
					 }
				 }
		 }		 
		 		 
		 if (validationPassed) {
			 try {
				selectedUser.setPassword(oldPassword);
				selectedUser.setNewPassword(newPassword);
				this.userManagement.updateUser(this.user.getUserName(),selectedUser);
				messageInfo = createMessageInfo(messageInfo, Message.CHANGE_PASSWORD_TITLE, Message.CHANGE_PASSWORD_SUCCESSFUL, Message.INFORMATION, false, true );
			} catch (CTBBusinessException e) {
				e.printStackTrace();
				message = MessageResourceBundle.getMessage(e.getMessage());
				messageInfo = createMessageInfo(messageInfo, Message.CHANGE_PASSWORD_TITLE, message, Message.ERROR, true, false );
			}
		 }
		 
		 creatGson( req, resp, stream, messageInfo );
			return null;
	 }
	 
	 public User getSelectedUserDetails(String selectedUserName){
		 User user = null;
		try {
			user = this.userManagement.getUser(this.user.getUserName(), selectedUserName);
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
		return user;
	 }
	 
	 public User getLoginUserDetails(UserManagement userManagement, String selectedUserName){
		 User user = null;
		try {
			user = userManagement.getUser(selectedUserName, selectedUserName);
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
		return user;
	 }
	 
	 private boolean isDeleteUserEnable(){
		 String roleName = this.user.getRole().getRoleName();
		 return (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR));
	 }
	 
	@Jpf.Action
    protected Forward deleteUser(userOperationForm form) 
    {        
        String selectedUserName = getRequest().getParameter("selectedUserName");  
        Integer selectedUserId = Integer.valueOf(getRequest().getParameter("selectedUserId"));
        HttpServletRequest req = getRequest();
        HttpServletResponse resp = getResponse();
        OutputStream stream = null;
        MessageInfo messageInfo = new MessageInfo();       
        User user = null;
		try 
		{
			user = userManagement.getUser(this.userName, selectedUserName);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
        
		if(user!=null){
		    try
		    {                    
		        this.userManagement.deleteUser(this.user.getUserName(), user);
		        messageInfo.setSuccessFlag(true);
		        messageInfo.setMessage(Message.DELETE_SUCCESSFUL);           
		    }
		    catch (CTBBusinessException be)
		    {
		        be.printStackTrace(); 
		        String errorMsg = MessageResourceBundle.getMessage(be.getMessage());
		        messageInfo.setMessage(errorMsg);
		    } 
		    catch (Exception e1)
		    {
		        e1.printStackTrace();
		        messageInfo.setMessage(Message.DELETE_ERROR);
		    }   
		    creatGson( req, resp, stream, messageInfo );
		}
        return null;
    }
 	 	 
}
