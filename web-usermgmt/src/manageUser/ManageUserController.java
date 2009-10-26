package manageUser;

import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerReport;
import com.ctb.bean.testAdmin.CustomerReportData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.PasswordHintQuestion;
import com.ctb.bean.testAdmin.Role;
import com.ctb.bean.testAdmin.TimeZones;
import com.ctb.bean.testAdmin.USState;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserData;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.control.userManagement.UserManagement;
import com.ctb.control.userManagement.OrgNodeHierarchy;
import com.ctb.bean.testAdmin.PasswordHistory;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNode;	  
import com.ctb.bean.testAdmin.PasswordDetails;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.userManagement.UserDataCreationException;
import com.ctb.exception.userManagement.UserPasswordUpdateException;
import com.ctb.util.userManagement.CTBConstants;
//import com.ctb.util.userManagement.DeleteUserStatus;
import com.ctb.util.web.sanitizer.JavaScriptSanitizer;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.PagerSummary;
import dto.Message;
import dto.NavigationPath;
import dto.PasswordInformation;
import dto.PathNode;
import dto.UserProfileInformation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import utils.FilterSortPageUtils;
import utils.MessageResourceBundle;
import utils.UserFormUtils;
import utils.OrgNodeUtils;
import utils.PermissionsUtils;
import utils.UserPathListUtils;
import utils.UserSearchUtils;
import utils.WebUtils;



/**
 * @jpf:controller
 *  */


/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** ManageUserController ************* ///////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////

@Jpf.Controller()
public class ManageUserController extends PageFlowController
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
    private com.ctb.control.userManagement.OrgNodeHierarchy orgNodeHierarchy;
    

    private static final String ACTION_DEFAULT        = "defaultAction";
    private static final String ACTION_FIND_USER      = "findUser";
    private static final String ACTION_VIEW_USER      = "viewUser";
    private static final String ACTION_EDIT_USER      = "editUser";
    private static final String ACTION_ADD_USER       = "addUser";
    private static final String ACTION_DELETE_USER    = "deleteUser";
    private static final String ACTION_MY_PROFILE     = "myProfile";
    private static final String ACTION_EDIT_MY_PROFILE = "editMyProfile";
    private static final String ACTION_CHANGE_PASSWORD = "changePassword";

    private static final String ACTION_ADD_ADMINISTRATOR = "addAdministrator";
    
    private static final String MODULE_HIERARCHY      = "moduleHierarchy";
    private static final String MODULE_USER_PROFILE   = "moduleUserProfile";
    private static final String MODULE_NONE           = "moduleNone";

    private static final String ACTION_APPLY_SEARCH   = "applySearch";
    private static final String ACTION_CLEAR_SEARCH   = "clearSearch";
    
    private static final String ACTION_FORM_ELEMENT   = "{actionForm.actionElement}";
    private static final String ACTION_CURRENT_ELEMENT   = "{actionForm.currentAction}";


    private String userName = null;
    private Integer customerId = null;
    private User user = null;
    
    private String selectedModuleFind = null;
    private boolean searchApplied = false;
    private boolean viewUserFromSearch = false;

    private List orgNodePath = null;
    private Integer selectedOrgNodeId = null;
    private List tmpOrgNodePath = null;
    
    private ManageUserForm savedForm = null;
    private UserProfileInformation userSearch = null;

    private HashMap currentOrgNodesInPathList = null;
    public List selectedOrgNodes = null;
    public Integer[] currentOrgNodeIds = null;
    
    // option list
    public LinkedHashMap roleOptions = null;
    public LinkedHashMap hintQuestionOptions = null;
    public LinkedHashMap timeZoneOptions = null;	 
    public LinkedHashMap stateOptions = null;
    
    //Temporary list to store searched/added userProfiles
    public List userList = new ArrayList(0);
    
    // title, messages
    public String pageTitle = null;
    public String webTitle = null;
    public String pageMessage = null;
    public boolean clearCurrentMessage = true;
    
    private boolean isEmptyProfileSearch = false;

    // navigation
    private NavigationPath navPath = null;

    // help
    public String helpLink = null;
    
    public Integer currentOrgPageRequested = null;
    public Integer currentUserPageRequested = null;
    public Integer userMaxPage = null;
   
    public boolean isAddAdministrator = false;
    private String customerOrgNodeName = null;
    
    private boolean isLoginFirstTime = false;
    
    /*Changed for DEx defect # 57562 & 57563*/
    private boolean isLoginWithoutTimezone = false;
      
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** begin controller ************* //////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * this method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="defaultAction.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "defaultAction.do")
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
                     path = "beginFindUser.do"), 
        @Jpf.Forward(name = "newUser",
                     path = "beginChangeMyPassword.do")
    })
    protected Forward defaultAction(ManageUserForm form)
    {
        initialize(ACTION_FIND_USER);
      /*   if(this.displayNewMessage.equalsIgnoreCase(CTBConstants.TRUE)){
              form.setSelectedUserName(this.userName);
              initTimeZoneOptions(ACTION_DEFAULT);
              return new Forward("newUser",form);
        }*/
        return new Forward("success");
    }
    
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** FIND USER ************* /////////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * begin of Find User module
     * @jpf:action
     * @jpf:forward name="success" path="findUser.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "findUser.do")
    })
    protected Forward beginFindUser()
    {
        ManageUserForm form = initialize(ACTION_FIND_USER);
       
        form.setSelectedUserId(null); 
        form.setSelectedUserName(null);
        form.setSelectedTab(MODULE_USER_PROFILE);        
        clearMessage(form);
            
        this.searchApplied = false;
            
        initRoleOptions(ACTION_FIND_USER);
        initTimeZoneOptions(ACTION_FIND_USER);
        initStateOptions(ACTION_FIND_USER);		 
        resetUserList();
            
        this.navPath.reset(ACTION_FIND_USER);
        
                    
        return new Forward("success", form);                      
    }
    
    /**
     * handle find users by profile and hierarchy
     * @jpf:action
     * @jpf:forward name="success" path="find_user.jsp"
     * @jpf:forward name="gotoNextAction" path="gotoNextAction.do" redirect="true"
     * 
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "find_user.jsp"), 
        @Jpf.Forward(name = "gotoNextAction", 
                     path = "gotoNextAction.do", 
                     redirect = true)
    })
    protected Forward findUser(ManageUserForm form)
    {        
        form.validateValues();
        
        String currentAction = form.getCurrentAction();
        String actionElement = form.getActionElement();
            
        form.resetValuesForAction(actionElement, ACTION_FIND_USER);
                
        if (currentAction.equals(ACTION_VIEW_USER) || currentAction.equals(ACTION_EDIT_USER) || currentAction.equals(ACTION_DELETE_USER) || currentAction.equals(ACTION_CHANGE_PASSWORD))
        {
                    
            UserProfileInformation userProfile = UserSearchUtils.getUserNameFromList(form.getSelectedUserId(), this.userList);
            form.setSelectedUserName(userProfile.getUserName());
            this.viewUserFromSearch = true;
            this.savedForm = form.createClone();
            this.userSearch = form.getUserProfile().createClone();
            this.tmpOrgNodePath = this.orgNodePath;
            return new Forward("gotoNextAction");
        }
                        
        String selectedTab = form.getSelectedTab();
        selectedTab = JavaScriptSanitizer.sanitizeString(selectedTab);
        if (! selectedTab.equals(this.selectedModuleFind))
        {
            initFindUser(selectedTab, form);                
        }
                        
        initPagingSorting(form);
        
        boolean applySearch = initSearch(form);
        
        UserData uData = null;
        
        if (this.selectedModuleFind.equals(MODULE_USER_PROFILE))
        {
            if (applySearch)
            {
                try
                {
                    
                    uData = findByUserProfile(form);
                    
                }
                catch (CTBBusinessException be)
                {
                    be.printStackTrace();
                    String msg = MessageResourceBundle.getMessage(be.getMessage());
                    form.setMessage(Message.FIND_TITLE, msg, Message.ERROR);
                }
                    
                if ((uData != null) && (uData.getFilteredCount().intValue() == 0))
                {
                    this.getRequest().setAttribute("searchResultEmpty", 
                                                Message.FIND_NO_RESULT);
                    isEmptyProfileSearch = true;                            
                }
            }
        }
        
        if (this.selectedModuleFind.equals(MODULE_HIERARCHY))
        {
           
            try
            {
                
                uData = findByHierarchy(form);
                
            }
            catch (CTBBusinessException be)
            {
                be.printStackTrace();
                String msg = MessageResourceBundle.getMessage(be.getMessage());
                form.setMessage(Message.FIND_TITLE, msg, Message.ERROR);
            }
                            
        }
                       
                                        
        this.searchApplied = false;
        
        if (isEmptyProfileSearch)
        {
            this.searchApplied = true;
            isEmptyProfileSearch = false;        
        }
        
        if ((uData != null) && (uData.getFilteredCount().intValue() > 0))
        {
            
            this.searchApplied = true;        
            this.userList = UserSearchUtils.buildUserList(uData);
            PagerSummary userPagerSummary = 
                    UserSearchUtils.buildUserPagerSummary(uData, form.getUserPageRequested());        
            form.setUserMaxPage(uData.getFilteredPages());
            userMaxPage = uData.getFilteredPages();
                                                       
            this.getRequest().setAttribute("userResult", "true");        
            this.getRequest().setAttribute("userPagerSummary", userPagerSummary);
           
            if (form.getSelectedUserId() != null)
            {    
                UserProfileInformation userProfile =
                     UserSearchUtils.getUserProfileInformation(form.getSelectedUserId(), userList);
               
                PermissionsUtils.setPermissionRequestAttribute(this.getRequest(), userProfile);
            }
            else
            {
                PermissionsUtils.setPermissionRequestAttribute(this.getRequest(), null);
            }
        }

        form.setCurrentAction(ACTION_DEFAULT);
        this.getRequest().setAttribute("isFindUser", Boolean.TRUE);
        this.getRequest().setAttribute("selectedModule", 
                                            this.selectedModuleFind);
        
        this.pageTitle = buildPageTitle(ACTION_FIND_USER, form);

        this.navPath.addCurrentAction(ACTION_FIND_USER);
        
        return new Forward("success");
    }

    /**
     * initSearch
     */
    private boolean initSearch(ManageUserForm form)
    {
        boolean applySearch = false;
        String currentAction = form.getCurrentAction();
        
        if ((currentAction != null) && currentAction.equals(ACTION_APPLY_SEARCH))
        {
            applySearch = true;
            this.searchApplied = false;
            form.setUserSortColumn(FilterSortPageUtils.USER_DEFAULT_SORT_COLUMN);
            form.setUserSortOrderBy(FilterSortPageUtils.ASCENDING);      
            form.setUserPageRequested(new Integer(1));    
            form.setUserMaxPage(new Integer(1));                  
        }
        
        if ((currentAction != null) && currentAction.equals(ACTION_CLEAR_SEARCH))
        {
            applySearch = false;
            this.searchApplied = false;
            form.clearSearch();
        }

        if (this.searchApplied)
        {
            applySearch = true;
        }
        else
        {
            form.setSelectedUserId(null);
            form.setSelectedUserName(null);
        }
                
        return applySearch;
    }

    /**
     * findByUserProfile
     */
    private UserData findByUserProfile(ManageUserForm form) throws CTBBusinessException
    {
        String actionElement = form.getActionElement();
        form.resetValuesForAction(actionElement, ACTION_FIND_USER);        
        
        String firstName = form.getUserProfile().getFirstName().trim();
        String lastName = form.getUserProfile().getLastName().trim();
        String loginId = form.getUserProfile().getLoginId().trim();
        String email = form.getUserProfile().getEmail().trim();
        String role = form.getUserProfile().getRole().trim();
        String invalidEmail = ""; 
        String invalidString = "";            
        String invalidCharFields = UserFormUtils.verifyFindUserInfo(firstName, lastName);

        if (invalidCharFields.length() > 0)
        {
            invalidString = invalidCharFields + ("<br/>" +
                                                 Message.INVALID_NAME_CHARS);
        }

        boolean validLoginId = WebUtils.validLoginID(loginId);                
        if (!validLoginId)
        {
            if (invalidString != null && invalidString.length() > 0)
            {
                invalidString += ("<br/>");
            }
            invalidString += "Login ID" +
                             ("<br/>" +
                              Message.INVALID_LOGIN);
        } 
        
        boolean validEmail = WebUtils.validEmail(email);                
        if (! validEmail)
        {
            if (invalidString != null && invalidString.length() > 0)
            {
                invalidString += ("<br/>");
            }
            invalidString += "Email" +
                             ("<br/>" +
                              Message.INVALID_EMAIL);
        } 
            
        if (invalidString != null && invalidString.length() > 0)
        {    
            form.setMessage(Message.INVALID_CHARS_TITLE, invalidString, Message.ERROR);
            return null;
        }    
            
        PageParams page = FilterSortPageUtils.buildPageParams(form.getUserPageRequested(), FilterSortPageUtils.PAGESIZE_10);
        SortParams sort = FilterSortPageUtils.buildUserSortParams(form.getUserSortColumn(), form.getUserSortOrderBy());
        FilterParams filter = FilterSortPageUtils.buildFilterParams(firstName, lastName, loginId, email, role);
        
        UserData uData = null;

        if (filter == null)
        {
            uData = UserSearchUtils.searchAllUsersAtAndBelow(this.userManagement, this.userName, page, sort);   
            this.pageMessage = Message.FIND_FOUND_AT_AND_BELOW;
        }
        else
        {
            uData = UserSearchUtils.searchUsersByProfile(this.userManagement, this.userName, filter, page, sort);   
            this.pageMessage = Message.FIND_FOUND_WITH_CRITERIA;                                    
        }
    
        return uData;
    }


    /**
     * findByHierarchy
     */
    private UserData findByHierarchy(ManageUserForm form) throws CTBBusinessException
    {      
        form.validateValues();
        
        UserData uData = null;
        String actionElement = form.getActionElement();
        String currentAction = form.getCurrentAction();
        
        form.resetValuesForAction(actionElement, ACTION_FIND_USER);        
                      
        String orgNodeName = form.getOrgNodeName();
        Integer orgNodeId = form.getOrgNodeId();   
        boolean nodeChanged = UserPathListUtils.adjustOrgNodePath(this.orgNodePath, orgNodeId, orgNodeName);

        if (nodeChanged)
        {
            form.resetValuesForPathList();
            form.setSelectedOrgNodeId(null);            
        }
        
        FilterParams filter = null;
        PageParams page = FilterSortPageUtils.buildPageParams(form.getOrgPageRequested(), FilterSortPageUtils.PAGESIZE_5);
        SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgSortColumn(), form.getOrgSortOrderBy(), null, null);
        
        UserNodeData und = UserPathListUtils.getOrganizationNodes(this.userName, this.userManagement, orgNodeId, filter, page, sort);         

        if (form.getOrgPageRequested().intValue() > und.getFilteredPages().intValue())
        {
            form.setOrgPageRequested(und.getFilteredPages());
        }
        List orgNodes = UserPathListUtils.buildOrgNodeList(und);
        String orgCategoryName = UserPathListUtils.getOrgCategoryName(orgNodes);
        
        PagerSummary orgPagerSummary = 
                UserPathListUtils.buildOrgNodePagerSummary(und, form.getOrgPageRequested());
        form.setOrgMaxPage(und.getFilteredPages());

        if (actionElement.equals(ACTION_FORM_ELEMENT))
        {
            PathNode node = UserPathListUtils.findOrgNode(orgNodes, form.getSelectedOrgNodeId());
            if (node != null)
            {
                form.setSelectedOrgNodeId(node.getId());
                form.setSelectedOrgNodeName(node.getName());                
                form.setSelectedUserId(null);
                form.setSelectedUserName(null);
            }
        }
        this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
        this.getRequest().setAttribute("orgNodes", orgNodes);        
        this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
        this.getRequest().setAttribute("orgCategoryName", orgCategoryName);        

        this.pageMessage = "";
        Integer selectedOrgNodeId = form.getSelectedOrgNodeId();
        if (selectedOrgNodeId != null)
        {
            filter = null;
            page = FilterSortPageUtils.buildPageParams(form.getUserPageRequested(), FilterSortPageUtils.PAGESIZE_10);
            sort = FilterSortPageUtils.buildUserSortParams(form.getUserSortColumn(), form.getUserSortOrderBy());
            
            uData = UserSearchUtils.searchUsersByOrgNode(this.userManagement, this.userName, selectedOrgNodeId, filter, page, sort);
            if ((uData != null) && (uData.getFilteredCount().intValue() > 0))
            {
                this.pageMessage = Message.getFindMessageForOrgNode(form.getSelectedOrgNodeName(), true);
            }
            else
            {
                String searchMsg = Message.getFindMessageForOrgNode(form.getSelectedOrgNodeName(), false);
                this.getRequest().setAttribute("searchResultEmpty", searchMsg);
            }
        }
        
        return uData;
    }




////////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** CHANGE PASSWORD ************* //////////////////////////////////    
////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * begin change my password
     * @jpf:action
     * @jpf:forward name="success" path="changePassword.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "changePassword.do")
    })
    protected Forward beginChangeMyPassword(ManageUserForm form)
    {   
        initHintQuestionOptions(true);
        try
        {
            UserProfileInformation userProfile = setUserProfileToForm(form);
        }
        catch (CTBBusinessException businessException)
        {
            businessException.printStackTrace();
            form.setMessage(Message.CHANGE_PASSWORD_TITLE, Message.CHAANGE_PASSWORD_EXCEPTION, Message.ERROR);
        } 
        clearMessage(form);
                
        return new Forward("success", form);
    }

    /**
     * begin change password
     * @jpf:action
     * @jpf:forward name="success" path="changePassword.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "changePassword.do")
    })
    protected Forward beginChangePassword(ManageUserForm form)
    {   
        initHintQuestionOptions(false);
        clearMessage(form);
        if (this.navPath.findAction(ACTION_VIEW_USER))
        {
            this.savedForm.setByUserProfileVisible(form.getByUserProfileVisible());
            this.savedForm.setByUserContactVisible(form.getByUserContactVisible());
        }
        
        try
        {
            UserProfileInformation userProfile = setUserProfileToForm(form);
        }
        catch (CTBBusinessException businessException)
        {
            businessException.printStackTrace();
            form.setMessage(Message.CHANGE_PASSWORD_TITLE, Message.CHAANGE_PASSWORD_EXCEPTION, Message.ERROR);
        }         
       
        return new Forward("success", form);
    }

    /**
     * handle change password
     * @jpf:action
     * @jpf:forward name="changeMyPassword" path="my_profile_change_password.jsp"
     * @jpf:forward name="changeUserPassword" path="change_password.jsp"
     * @jpf:forward name="resetPassword" path="reset_password.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "changeMyPassword",
                     path = "my_profile_change_password.jsp"), 
        @Jpf.Forward(name = "changeUserPassword",
                     path = "change_password.jsp"), 
        @Jpf.Forward(name = "resetPassword",
                     path = "reset_password.jsp")
    })
    protected Forward changePassword(ManageUserForm form)
    {        
        this.navPath.addCurrentAction(ACTION_CHANGE_PASSWORD);        
        
        this.pageTitle = buildPageTitle(ACTION_CHANGE_PASSWORD, form);
        
        if (this.isLoginFirstTime)
            return new Forward("resetPassword", form);
        else if (isLoggedInUser(form.getSelectedUserName())) 
            return new Forward("changeMyPassword", form);
        else 
            return new Forward("changeUserPassword", form);
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="changePassword.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "changePassword.do")
    })
    protected Forward resetPassword()
    {        
        this.isLoginFirstTime = true;
        
        ManageUserForm form = initialize(ACTION_CHANGE_PASSWORD);
        initTimeZoneOptions(ACTION_CHANGE_PASSWORD);
        initHintQuestionOptions(false);
        
        form.setSelectedUserName(this.userName);
        try
        {
            UserProfileInformation userProfile = setUserProfileToForm(form);
        }  
        catch (CTBBusinessException be)
        {
        }

        return new Forward("success", form);
    }

    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward returnToHomePage(ManageUserForm form)
    {               
        try
        {
            String url = "/TestSessionInfoWeb/homepage/returnToHomePage.do";
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
     * @jpf:forward name="success" path="returnToPreviousAction.do"
     * @jpf:forward name="returnToHomePage" path="returnToHomePage.do"
     * @jpf:forward name="error" path="changePassword.do"
     * @jpf:forward name="editMyProfile" path="beginEditMyProfile.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "returnToPreviousAction.do"), 
        @Jpf.Forward(name = "returnToHomePage",
                     path = "returnToHomePage.do"), 
        @Jpf.Forward(name = "error",
                     path = "changePassword.do"), 
        @Jpf.Forward(name = "editMyProfile",
                     path = "beginEditMyProfile.do")
    })
    protected Forward savePassword(ManageUserForm form)
    {
        String forwardName = "error";
        String selectedUserName = form.getSelectedUserName(); 
        PasswordInformation passwordinfo = form.getUserProfile().getUserPassword();
        UserProfileInformation userProfile = this.savedForm.getUserProfile();
         
        User user = userProfile.makeCopy(selectedUserName, selectedOrgNodes);
        if (passwordinfo.getHintQuestionId() != null && !"".equals(passwordinfo.getHintQuestionId()))
        {
            user.setPasswordHintQuestionId(new Integer(passwordinfo.getHintQuestionId()));
            user.setPasswordHintAnswer(passwordinfo.getHintAnswer());
        }
        user.setPassword(passwordinfo.getOldPassword());
        user.setNewPassword(passwordinfo.getNewPassword());
        
        PasswordDetails  passwordDetails = new PasswordDetails();

        try
        {
              /* Changed/Added for DEx Phase 2 on 22-Apr-09 by TCS -- Start*/
                      
            if (UserFormUtils.verifyUserPassword(form, selectedUserName, form.getUserProfile().getUserPassword(), userManagement, isLoggedInUser(selectedUserName)))
            {
             /* Changed/Added for DEx Phase 2 on 22-Apr-09 by TCS -- End*/
                if (isLoggedInUser(selectedUserName))
                {
                    passwordDetails.setResetPassword(CTBConstants.FALSE);
                    passwordDetails.setPasswordHintAnswer(form.getUserProfile().getUserPassword().getHintAnswer());
                    passwordDetails.setPasswordHintQuestionId(Integer.valueOf(form.getUserProfile().getUserPassword().getHintQuestionId()));
                } 
                else
                {
                    passwordDetails.setResetPassword(CTBConstants.TRUE);
                }
                this.userManagement.updateUser(this.userName,user);
                this.savedForm.setMessage(Message.CHANGE_PASSWORD_TITLE, Message.CHANGE_PASSWORD_SUCCESSFUL,Message.INFORMATION);
                if (this.isLoginFirstTime)
                {
                    this.isLoginFirstTime = false;
                    forwardName = "returnToHomePage";
                }
                
                else
                    forwardName = "success";
                    
                /*Changed for DEx defect # 57562 & 57563*/
                
                if (user.getTimeZone() == null)
                {
                    forwardName = "editMyProfile";
                    this.getRequest().setAttribute("isSetTimeZone","true");
                }
                /*End of Change for DEx defect # 57562 & 57563*/       
                
            } 
            else
            {
                if (!isLoggedInUser(selectedUserName))
                {
                    userProfile.setFirstName(form.userProfile.getFirstName());
                    userProfile.setLastName(form.userProfile.getLastName());
                }
                
                form.getUserProfile().setFirstName(userProfile.getFirstName());
                form.getUserProfile().setLastName(userProfile.getLastName());                
            }                 
        }  
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
            String msg = MessageResourceBundle.getMessage(be.getMessage());
            form.getUserProfile().setFirstName(userProfile.getFirstName());
            form.getUserProfile().setLastName(userProfile.getLastName());
            form.setMessage(Message.CHANGE_PASSWORD_TITLE, msg, Message.ERROR);
        }
         
        return new Forward(forwardName, form);
    }    

////////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** ADD / EDIT USER ************* //////////////////////////////////    
////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * begin add user
     * @jpf:action
     * @jpf:forward name="success" path="addEditUser.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "addEditUser.do")
    })
    protected Forward beginAddAdministrator()
    {        
        ManageUserForm form = initializeAddUser();
        clearMessage(form);
        resetUserList();

        this.helpLink = "/help/index.html#adding_new_users.htm";
        
        String orgNodeId = this.getRequest().getParameter("orgNodeId");
        this.customerOrgNodeName = this.getRequest().getParameter("orgNodeName");
       
        this.selectedOrgNodes = new ArrayList();
        PathNode node = new PathNode();
        if (orgNodeId != null) 
            node.setId(new Integer(orgNodeId));
        node.setName(this.customerOrgNodeName);
        this.selectedOrgNodes.add(node);

        this.isAddAdministrator = true;
        
       
        form.getUserProfile().setRole(CTBConstants.DEFAULT_ROLE); 
        
        try
        {
            Role[] roles = this.userManagement.getRoles();
            if (roles != null)
            {
                for (int i = 0; i < roles.length; i++)
                {
                    if (roles[i].getRoleName().equals(CTBConstants.DEFAULT_ROLE))
                    {
                        form.getUserProfile().setRoleId(roles[i].getRoleId().toString());
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
          
          
        return new Forward("success", form);
    }

    /**
     * begin add user
     * @jpf:action
     * @jpf:forward name="success" path="addEditUser.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "addEditUser.do")
    })
    protected Forward beginAddUser()
    {
        ManageUserForm form = initializeAddUser();
        clearMessage(form);
        resetUserList();

        this.helpLink = "/help/index.html#adding_new_users.htm";
        
        this.isAddAdministrator = false;
        
        return new Forward("success", form);
    }

    /**
     * begin edit user
     * @jpf:action
     * @jpf:forward name="success" path="addEditUser.do"
     * @jpf:forward name="editMyProfile" path="beginEditMyProfile.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "addEditUser.do"), 
        @Jpf.Forward(name = "editMyProfile",
                     path = "beginEditMyProfile.do")
    })
    protected Forward beginEditUser(ManageUserForm form)
    {      
        String selectedUserName = form.getSelectedUserName();
        
        if (isLoggedInUser(selectedUserName))
        {
            return new Forward("editMyProfile");
        }
        
        UserProfileInformation userProfile = null;
        try
        {
            userProfile = setUserProfileToForm(form); 
            if ((userProfile.getFirstName() != null && userProfile.getFirstName().length() > 0) && (userProfile.getLastName() != null && userProfile.getLastName().length() > 0))
            {
                this.getRequest().getSession().setAttribute("UserFirstName", userProfile.getFirstName()); 
                this.getRequest().getSession().setAttribute("UserLastName", userProfile.getLastName()); 
            }
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
            String msg = MessageResourceBundle.getMessage(be.getMessage());                                        
            form.setMessage(Message.EDIT_TITLE, msg, Message.ERROR);
        } 

        initRoleOptions(ACTION_EDIT_USER);
        initTimeZoneOptions(ACTION_EDIT_USER);
        initStateOptions(ACTION_EDIT_USER);
        clearMessage(form);
        
        String selectedTab = form.getSelectedTab();
        selectedTab = JavaScriptSanitizer.sanitizeString(selectedTab);
        if ((selectedTab != null) && (selectedTab.length() > 0))
        {        
            form.setCurrentAction(MODULE_USER_PROFILE);
            form.clearSectionVisibility();
            form.setByUserProfileVisible(Boolean.TRUE);
        }
        else
        {
            form.setCurrentAction(ACTION_DEFAULT);
        }
        
        this.currentOrgNodesInPathList = new HashMap();
        this.currentOrgNodeIds = new Integer[0];
        
        this.selectedOrgNodes = UserPathListUtils.getOrgNodeAssignment(userProfile);

        // initialize hierarchy control
        Integer orgNodeId = null;
        String orgNodeName = null;       
                 
        PathNode node = (PathNode)this.selectedOrgNodes.get(0);   // get first node
        orgNodeId = node.getId();
        orgNodeName = node.getName();
        this.orgNodePath = initHierarchyControl(orgNodeId, form);

        this.navPath.addCurrentAction(ACTION_EDIT_USER);
        this.helpLink = "/help/index.html#editing_or_deleting_users.htm";

        return new Forward("success", form);
    }

    /**
     * forward to add or edit user module
     * @jpf:action
     * @jpf:forward name="addUser" path="addUser.do"
     * @jpf:forward name="editUser" path="editUser.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "addUser",
                     path = "addUser.do"), 
        @Jpf.Forward(name = "editUser",
                     path = "editUser.do")
    })
    protected Forward addEditUser(ManageUserForm form)
    {      
        String stringAction = form.getStringAction();
        return new Forward(stringAction, form);
    }

    /**
     * handle add user
     * @jpf:action
     * @jpf:forward name="success" path="add_edit_user.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "add_edit_user.jsp")
    })
    protected Forward addUser(ManageUserForm form)
    {      
        handleAddEdit(form);
        
        this.getRequest().setAttribute("isAddUser", Boolean.TRUE);
        this.getRequest().setAttribute("isAddAdministrator", Boolean.valueOf(this.isAddAdministrator));

        if (this.isAddAdministrator)
            this.pageTitle = buildPageTitle(ACTION_ADD_ADMINISTRATOR, form);
        else
            this.pageTitle = buildPageTitle(ACTION_ADD_USER, form);

        return new Forward("success");
    }

    /**
     * handle edit user
     * @jpf:action
     * @jpf:forward name="success" path="add_edit_user.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "add_edit_user.jsp")
    })
    protected Forward editUser(ManageUserForm form)
    {      
        handleAddEdit(form);

        this.getRequest().setAttribute("isEditUser", Boolean.TRUE);
        
        this.pageTitle = buildPageTitle(ACTION_EDIT_USER, form);
        
        return new Forward("success");
    }

    /**
     * return to find user module
     * @jpf:action
     * @jpf:forward name="success" path="findUser.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "findUser.do")
    })
    protected Forward returnToFindUser(ManageUserForm form)
    {
        this.savedForm.setUserProfile(this.userSearch);
        this.savedForm.setSelectedTab(this.selectedModuleFind);
        this.savedForm.setCurrentAction(ACTION_DEFAULT);
        this.savedForm.setSelectedOrgNodeName(form.getSelectedOrgNodeName());                        
        this.savedForm.setMessage(form.getMessage());
        this.orgNodePath = this.tmpOrgNodePath;

        return new Forward("success", this.savedForm);
    }

    /**
     * handleAddOrEdit
     */
    protected void handleAddEdit(ManageUserForm form)
    {        
        addEditUserProfile(form);    

        form.setCurrentAction(ACTION_DEFAULT);
        form.setActionElement(ACTION_FORM_ELEMENT);
    }


    /**
     * addEditUserProfile
     */
    protected void addEditUserProfile(ManageUserForm form)
    {
        form.validateValues();
        
        String actionElement = form.getActionElement();
        String currentAction = form.getCurrentAction();
        form.resetValuesForAction(actionElement, ACTION_EDIT_USER);      
        // editUser
        String actionvalue = form.getStringAction();             
        String orgNodeName = form.getOrgNodeName();
        Integer orgNodeId = form.getOrgNodeId();   
        boolean nodeChanged = UserPathListUtils.adjustOrgNodePath(this.orgNodePath, orgNodeId, orgNodeName);

        if (nodeChanged)
        {
            form.resetValuesForPathList();
        }
        
        if (actionElement.equals("setupOrgNodePath"))
        {
            String tempOrgNodeId = form.getCurrentAction();  
            if (tempOrgNodeId != null)
            {
                orgNodeId = new Integer(tempOrgNodeId);
                this.orgNodePath = initHierarchyControl(orgNodeId, form);      
                orgNodeName = form.getOrgNodeName();
                orgNodeId = form.getOrgNodeId();   
            }
        }
        
        FilterParams filter = null;
        PageParams page = FilterSortPageUtils.buildPageParams(form.getOrgPageRequested(), FilterSortPageUtils.PAGESIZE_8);
        SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgSortColumn(), form.getOrgSortOrderBy(), null, null);
        
        form.setUserMaxPage(userMaxPage);                    
        
        NodeData nodeData = UserPathListUtils.getOrganizationNodes(this.userName, this.userName, this.orgNodeHierarchy, orgNodeId, filter, page, sort);	   
        
        
        
        List orgNodes = UserPathListUtils.buildOrgNodeList(nodeData, actionvalue);
        String orgCategoryName = UserPathListUtils.getOrgCategoryName(orgNodes);
                        
        PagerSummary orgPagerSummary = 
                UserPathListUtils.buildOrgNodePagerSummary(nodeData, form.getOrgPageRequested());
               
        form.setOrgMaxPage(nodeData.getFilteredPages());

        if (actionElement.equals(ACTION_FORM_ELEMENT))
        {
            PathNode node = UserPathListUtils.findOrgNode(orgNodes, form.getSelectedOrgNodeId());
            if (node != null)
            {
                form.setSelectedOrgNodeId(node.getId());
                form.setSelectedOrgNodeName(node.getName());                
            }
        }
        
        // compute selected orgnodes from pathlist
        this.selectedOrgNodes = UserPathListUtils.buildSelectedOrgNodes(this.currentOrgNodesInPathList, this.currentOrgNodeIds, this.selectedOrgNodes);

        this.currentOrgNodeIds = UserPathListUtils.retrieveCurrentOrgNodeIds(this.selectedOrgNodes);
        this.currentOrgNodesInPathList = UserPathListUtils.buildOrgNodeHashMap(orgNodes);

        List selectableOrgNodes = UserPathListUtils.buildSelectableOrgNodes(this.currentOrgNodesInPathList, this.selectedOrgNodes);

        List orgNodesForSelector = buildOrgNodesForSelector(this.selectedOrgNodes, selectableOrgNodes, form.getOrgSortOrderBy());
        
        this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
        this.getRequest().setAttribute("orgNodes", orgNodes);        
        this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
        this.getRequest().setAttribute("orgCategoryName", orgCategoryName);      
        
        this.getRequest().setAttribute("selectedOrgNodes", 
                this.selectedOrgNodes);
        this.getRequest().setAttribute("orgNodesForSelector", orgNodesForSelector);
    }

    /**
     * create a new user or edit an existing user
     * verify user information from input, return an error if any
     * otherwise return to previous page
     * @jpf:action
     * @jpf:forward name="success" path="returnToPreviousAction.do"
     * @jpf:forward name="toManageOrganization" path="returnToManageOrganization.do"
     * @jpf:forward name="error" path="addEditUser.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "returnToPreviousAction.do"), 
        @Jpf.Forward(name = "toManageOrganization",
                     path = "returnToManageOrganization.do"), 
        @Jpf.Forward(name = "error",
                     path = "addEditUser.do")
    })
    protected Forward saveAddEditUser(ManageUserForm form)
    {
        String userName = form.getSelectedUserName();
        
        boolean isCreateNew = (userName == null || "".equals(userName)) ? true : false;
        Node[] loginUserNode = this.user.getOrganizationNodes();
       
        this.selectedOrgNodes = UserPathListUtils.buildSelectedOrgNodes(this.currentOrgNodesInPathList, this.currentOrgNodeIds, this.selectedOrgNodes);
        if (this.isAddAdministrator)
        {
            form.getUserProfile().setRole(CTBConstants.ROLE_NAME_ADMINISTRATOR);   
            
        }
        boolean validInfo = UserFormUtils.verifyUserInformation(form, this.selectedOrgNodes, isLoggedInUser(form.getSelectedUserName()), userName, this.user);	   
        
        if (!validInfo)
        {           
            form.setActionElement(ACTION_DEFAULT);
            form.setCurrentAction(ACTION_DEFAULT);   
            if (this.isAddAdministrator)
            {
                form.getUserProfile().setRole(CTBConstants.DEFAULT_ROLE);   
                
            }
                          
            return new Forward("error", form);
        }        
        
        userName = saveUserProfileInformation(isCreateNew, form, userName, this.selectedOrgNodes);
        
        if (userName == null)
        {
            return new Forward("error", form);
        }
        
        form.setSelectedUserName(userName);
        UserProfileInformation userProfile = null;
        try
        {
            userProfile = setUserProfileToForm(form); 
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
            String msg = MessageResourceBundle.getMessage(be.getMessage());
            String title = Message.EDIT_TITLE;
            if (isCreateNew)
            {
                title = Message.ADD_TITLE;  
            }                           
            form.setMessage(title, msg, Message.ERROR);
        }        
                
        this.viewUserFromSearch = false;             

        if (isCreateNew)
        {
            UserSearchUtils.addUserInUserList(userProfile, this.userList);
            if (userName != null)
            {
                if (userProfile.getEmail().length() == 0)
                    form.setMessage(Message.ADD_TITLE, Message.ADD_SUCCESSFUL_NO_EMAIL, Message.INFORMATION);
                else
                    form.setMessage(Message.ADD_TITLE, Message.ADD_SUCCESSFUL, Message.INFORMATION);
            }
            else 
                form.setMessage(Message.ADD_TITLE, Message.ADD_ERROR, Message.INFORMATION);
        }
        else
        {
            if (userName != null) 
                form.setMessage(Message.EDIT_TITLE, Message.EDIT_SUCCESSFUL, Message.INFORMATION);
            else 
                form.setMessage(Message.EDIT_TITLE, Message.EDIT_ERROR, Message.INFORMATION);
        }
        
        this.savedForm.setMessage(form.getMessage());
        this.savedForm.setUserProfile(form.getUserProfile());
        this.savedForm.setSelectedUserName(form.getSelectedUserName());
        this.savedForm.setSelectedUserId(form.getSelectedUserId());

        if (isCreateNew)
            this.navPath.setReturnActions(ACTION_VIEW_USER, ACTION_ADD_USER);
        else
            this.navPath.setReturnActions(ACTION_VIEW_USER, ACTION_FIND_USER);
        
        if (this.isAddAdministrator)
            return new Forward("toManageOrganization");
                                  
        return new Forward("success");
    }


    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward returnToManageOrganization()
    {
        this.isAddAdministrator = false;
        
        try
        {
            String message = "SuccessfullyAdded";
            String messageParam = "SuccessfullyAdded=" +
                                  message ;
           
            String contextPath = "/OrganizationManagementWeb/manageCustomer/returnToFindCustomer.do";
            String url = contextPath + "?" + messageParam ;
            this.isAddAdministrator = false;                          
            
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;        
    }


/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** MY PROFILE ************* ////////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * begin my profile
     * @jpf:action
     * @jpf:forward name="success" path="myProfile.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "myProfile.do")
    })
    protected Forward beginMyProfile()
    {   
        
        Message message = null;
        if (navPath != null && navPath.findAction(ACTION_EDIT_MY_PROFILE))
        {
            message = this.savedForm.getMessage();
        }
        
        ManageUserForm form = initialize(ACTION_MY_PROFILE);
        form.setSelectedUserId(this.user.getUserId());
        form.setSelectedUserName(this.user.getUserName());
        form.setCurrentAction(MODULE_USER_PROFILE);
        form.clearSectionVisibility();
        form.setByUserProfileVisible(Boolean.TRUE);
        clearMessage(form);
        initTimeZoneOptions(ACTION_MY_PROFILE);

        UserProfileInformation userProfile = null;
        try
        {
            userProfile = setUserProfileToForm(form); 
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
            String msg = MessageResourceBundle.getMessage(be.getMessage());                                        
            form.setMessage(Message.EDIT_TITLE, msg, Message.ERROR);
        } 
        
        form.setMessage(message);
        if (this.navPath != null)
        {
            this.navPath.reset(ACTION_MY_PROFILE);
        }
        this.savedForm = form.createClone();
                        
        return new Forward("success", form);
    }

    /**
     * setup to redirect to my profile
     * @jpf:action
     * @jpf:forward name="success" path="myProfile.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "myProfile.do")
    })
    protected Forward toMyProfile()
    {   
        this.savedForm.setCurrentAction(MODULE_USER_PROFILE);
        this.savedForm.clearSectionVisibility();
        this.savedForm.setByUserProfileVisible(Boolean.TRUE);

        UserProfileInformation userProfile = null;
        try
        {
            userProfile = setUserProfileToForm(this.savedForm); 
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
            String msg = MessageResourceBundle.getMessage(be.getMessage());                                        
            this.savedForm.setMessage(Message.EDIT_TITLE, msg, Message.ERROR);
        } 
        this.navPath.reset(ACTION_FIND_USER);
        this.navPath.addCurrentAction(ACTION_MY_PROFILE);
                        
        return new Forward("success", this.savedForm);
    }

    /**
     * view my profile
     * @jpf:action
     * @jpf:forward name="success" path="my_profile.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "my_profile.jsp")
    })
    protected Forward myProfile(ManageUserForm form)
    {   
        if (this.navPath.findAction(ACTION_FIND_USER))
        {
            this.getRequest().setAttribute("fromFindUsers", new Boolean(true));
        }
        this.getRequest().setAttribute("isMyProfile", Boolean.TRUE);
       
        return new Forward("success", form);                                                                                                                                                                                                    
    }

    /**
     * begin edit my profile
     * @jpf:action
     * @jpf:forward name="success" path="editMyProfile.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "editMyProfile.do")
    })
    protected Forward beginEditMyProfile(ManageUserForm form)
    {   
        /*Changed for DEx defect # 57562 & 57563*/
        String isSetTimeZoneParam = this.getRequest().getParameter("isSetTimeZone");
        
        String isSetTimeZoneAttr = (String)this.getRequest().getAttribute("isSetTimeZone");
        
        if ((isSetTimeZoneParam != null && isSetTimeZoneParam.equals("true")) || (isSetTimeZoneAttr != null && isSetTimeZoneAttr.equals("true")))
        {
      
            isLoginWithoutTimezone = true;
            form = initialize(ACTION_MY_PROFILE);
            form.setSelectedUserName((String)getSession().getAttribute("userName"));
            form.setActionElement(ACTION_DEFAULT);
            form.setByUserContactVisible(Boolean.FALSE);
            form.setByUserProfileVisible(Boolean.TRUE);
            form.setCurrentAction(MODULE_USER_PROFILE);
            form.setSelectedTab(MODULE_USER_PROFILE);
                        
            this.navPath.reset(ACTION_MY_PROFILE);
                  
        }
        /*End of Change for DEx defect # 57562 & 57563*/     
        initTimeZoneOptions(ACTION_MY_PROFILE);
        initStateOptions(ACTION_MY_PROFILE);	
        
        form.setCurrentAction(MODULE_USER_PROFILE);
        form.clearSectionVisibility();
        form.setByUserProfileVisible(Boolean.TRUE);
        clearMessage(form);
        UserProfileInformation userProfile = null;
        try
        {
            userProfile = setUserProfileToForm(form); 
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
            String msg = MessageResourceBundle.getMessage(be.getMessage());                                        
            form.setMessage(Message.EDIT_TITLE, msg, Message.ERROR);
        }  
                
        this.navPath.addCurrentAction(ACTION_EDIT_MY_PROFILE);    

        return new Forward("success", form);
    }

    /**
     * edit my profile
     * @jpf:action
     * @jpf:forward name="success" path="edit_my_profile.jsp"
     * @jpf:forward name="setTimezone" path="edit_my_profile_timezone.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "edit_my_profile.jsp"), 
        @Jpf.Forward(name = "setTimezone",
                     path = "edit_my_profile_timezone.jsp")
    })
    protected Forward editMyProfile(ManageUserForm form)
    {
        UserProfileInformation userProfile = this.savedForm.getUserProfile();
        
        if (userProfile.getOrganizationNodes() != null)
        { 
            form.getUserProfile().setOrganizationNodes(
                    userProfile.getOrganizationNodes());
        }
        form.validateValues();
        
        String actionElement = form.getActionElement();
        String currentAction = form.getCurrentAction();
        
        form.resetValuesForAction(actionElement, ACTION_EDIT_USER);        
        this.getRequest().setAttribute("isMyProfile", Boolean.TRUE);
        
        /*Changed for DEx defect # 57562 & 57563*/
        if (this.isLoginWithoutTimezone)
        {
            return new Forward("setTimezone", form);
        }
                      
        return new Forward("success", form);
    }

    /**
     * save my profile
     * @jpf:action
     * @jpf:forward name="success" path="beginMyProfile.do"
     * @jpf:forward name="error" path="editMyProfile.do"
     * @jpf:forward name="returnToHomePage" path="returnToHomePage.do"
     * @jpf:forward name="toMyProfile" path="toMyProfile.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "beginMyProfile.do"), 
        @Jpf.Forward(name = "error",
                     path = "editMyProfile.do"), 
        @Jpf.Forward(name = "returnToHomePage",
                     path = "returnToHomePage.do"), 
        @Jpf.Forward(name = "toMyProfile",
                     path = "toMyProfile.do")
    })
    protected Forward saveMyProfile(ManageUserForm form)
    {
               
        boolean result = UserFormUtils.verifyUserInformation(form, this.selectedOrgNodes, true, this.userName, this.user);	   
        if (!result)
        {
            form.setActionElement(ACTION_DEFAULT);
            form.setCurrentAction(ACTION_DEFAULT);                 
            return new Forward("error", form);
        }     
        
        form.getUserProfile().setUserId(this.user.getUserId());
        userName = saveUserProfileInformation(false, form, this.userName, this.selectedOrgNodes);
        if (userName == null)
        {
            return new Forward("error", form);
        }
        
        /*Changed for DEx defect # 57562 & 57563*/
        if (this.isLoginWithoutTimezone)
        {
            this.isLoginWithoutTimezone = false;
            return new Forward("returnToHomePage");
        }
        
        this.savedForm.setMessage(Message.EDIT_TITLE, 
                        Message.EDIT_SUCCESSFUL, Message.INFORMATION);
        
        if (this.navPath.findAction(ACTION_FIND_USER))
        {
            this.savedForm.setUserProfile(form.getUserProfile());
            return new Forward("toMyProfile");
        } 
                                 
        return new Forward("success");
    }



/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** VIEW USER ************* ////////////////////////////////// //   
/////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * begin view user
     * @jpf:action
     * @jpf:forward name="success" path="viewUser.do"
     * @jpf:forward name="toMyProfile" path="toMyProfile.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "viewUser.do"), 
        @Jpf.Forward(name = "toMyProfile",
                     path = "toMyProfile.do")
    })
    protected Forward beginViewUser(ManageUserForm form)
    {    
        clearMessage(form);

        if (isLoggedInUser(form.getSelectedUserName()))
        {
            this.savedForm = form.createClone();             
            return new Forward("toMyProfile");
        }
        
        form.setCurrentAction(MODULE_USER_PROFILE);
        form.clearSectionVisibility();
        form.setByUserProfileVisible(Boolean.TRUE);
        
        this.navPath.addCurrentAction(ACTION_VIEW_USER);
        
        return new Forward("success", form);
    }

    /**
     * view user
     * @jpf:action
     * @jpf:forward name="success" path="view_user.jsp"
     * @jpf:validation-error-forward name="failure" path="viewUser.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "view_user.jsp")
    }, 
                validationErrorForward = @Jpf.Forward(name = "failure",
                                                      path = "viewUser.do"))
    protected Forward viewUser(ManageUserForm form)
    {   
        UserProfileInformation userProfile = null;
        try
        {
            userProfile = setUserProfileToForm(form); 
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
            String msg = MessageResourceBundle.getMessage(be.getMessage());                                        
            form.setMessage(Message.VIEW_TITLE, msg, Message.ERROR);
        }             
         
        PermissionsUtils.setPermissionRequestAttribute(this.getRequest(), userProfile);

        form.setCurrentAction(ACTION_DEFAULT);

        this.getRequest().setAttribute("selectedModule", MODULE_USER_PROFILE);
        this.getRequest().setAttribute("organizationNodes", 
                userProfile.getOrganizationNodes());
                
        this.getRequest().setAttribute("viewOnly", Boolean.TRUE);       
        
        this.pageTitle = buildPageTitle(ACTION_VIEW_USER, form); 
        
        return new Forward("success", form);                                                                                                                                                                                                    
    }

    /**
     * returnToViewUser
     * @jpf:action
     * @jpf:forward name="success" path="viewUser.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "viewUser.do")
    })
    protected Forward returnToViewUser()
    {   
        return new Forward("success", this.savedForm);                                                                                                                                                                                                    
    }

/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** DELETE USER ************* ///////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * begin delete user
     * @jpf:action
     * @jpf:forward name="success" path="deleteUser.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "deleteUser.do")
    })
    protected Forward beginDeleteUser(ManageUserForm form)
    {        
        clearMessage(form);
        
        this.navPath.addCurrentAction(ACTION_DELETE_USER);
        
        return new Forward("success", form);
    }

    /**
     * handle delete user
     * @jpf:action
     * @jpf:forward name="success" path="returnToPreviousAction.do"
     * @jpf:forward name="error" path="returnToFindUser.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "returnToPreviousAction.do"), 
        @Jpf.Forward(name = "error",
                     path = "returnToFindUser.do")
    })
    protected Forward deleteUser(ManageUserForm form) 
    {        
        String selectedUserName = form.getSelectedUserName();  
        Integer selectedUserid = form.getSelectedUserId();
        
        boolean isDeleteException = false;    

        if (selectedUserName == null)
        {
            return new Forward("error", this.savedForm);
        }
        
        UserProfileInformation userProfile = 
                UserSearchUtils.getUserProfileInformation(selectedUserid, this.userList);
        User user = userProfile.makeCopy(selectedUserName, new ArrayList());
        
        try
        {                    
            this.userManagement.deleteUser(this.user.getUserName(), user);
            this.savedForm.setMessage(Message.DELETE_TITLE, 
                    Message.DELETE_SUCCESSFUL, Message.INFORMATION);
            this.savedForm.setSelectedUserId(null);
            this.savedForm.setSelectedUserName(null);
            
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace(); 
            String msg = MessageResourceBundle.getMessage(be.getMessage());
            this.savedForm.setMessage(Message.DELETE_TITLE, msg, Message.ERROR);
            this.savedForm.setByUserProfileVisible(form.getByUserProfileVisible());
            this.savedForm.setByUserContactVisible(form.getByUserContactVisible());
            isDeleteException = true;
        } 
        catch (Exception e)
        {
            e.printStackTrace();
            this.savedForm.setMessage(Message.DELETE_TITLE, 
                    Message.DELETE_ERROR, Message.ERROR);
            this.savedForm.setByUserProfileVisible(form.getByUserProfileVisible());
            this.savedForm.setByUserContactVisible(form.getByUserContactVisible());                    
            isDeleteException = true;        
        }   
        if (!isDeleteException)
        {
            this.navPath.setReturnActions(ACTION_FIND_USER, ACTION_FIND_USER);      //always return to findUser if deleted successfully
        }                    
        
        this.clearCurrentMessage = false;

        return new Forward("success", this.savedForm);
    }
     
    /**
     * forward to the action that stored in the saved form
     * @jpf:action
     * @jpf:forward name="viewUser" path="beginViewUser.do"
     * @jpf:forward name="editUser" path="beginEditUser.do"
     * @jpf:forward name="deleteUser" path="beginDeleteUser.do"
     * @jpf:forward name="changePassword" path="beginChangePassword.do"
     * @jpf:forward name="myProfile" path="beginMyProfile.do"
     * @jpf:forward name="defaultAction" path="defaultAction.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "viewUser",
                     path = "beginViewUser.do"), 
        @Jpf.Forward(name = "editUser",
                     path = "beginEditUser.do"), 
        @Jpf.Forward(name = "deleteUser",
                     path = "beginDeleteUser.do"), 
        @Jpf.Forward(name = "changePassword",
                     path = "beginChangePassword.do"), 
        @Jpf.Forward(name = "myProfile",
                     path = "beginMyProfile.do"), 
        @Jpf.Forward(name = "defaultAction",
                     path = "defaultAction.do")
    })
    protected Forward gotoNextAction()
    {
        String currentAction = this.savedForm.getCurrentAction();
        if (currentAction == null)
            currentAction = ACTION_DEFAULT;
        currentOrgPageRequested = this.savedForm.getOrgPageRequested(); 
        currentUserPageRequested = this.savedForm.getUserPageRequested();           
         
        return new Forward(currentAction, this.savedForm.createClone());
    }


    /**
     * forward to the last action
     * @jpf:action
     * @jpf:forward name="addUser" path="addUser.do"
     * @jpf:forward name="findUser" path="returnToFindUser.do"
     * @jpf:forward name="viewUser" path="returnToViewUser.do" redirect="true"
     * @jpf:forward name="editUser" path="beginEditUser.do"
     * @jpf:forward name="myProfile" path="myProfile.do"
     * @jpf:forward name="defaultAction" path="defaultAction.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "addUser",
                     path = "addUser.do"), 
        @Jpf.Forward(name = "findUser",
                     path = "returnToFindUser.do"), 
        @Jpf.Forward(name = "viewUser", 
                     path = "returnToViewUser.do", 
                     redirect = true), 
        @Jpf.Forward(name = "editUser",
                     path = "beginEditUser.do"), 
        @Jpf.Forward(name = "myProfile",
                     path = "myProfile.do"), 
        @Jpf.Forward(name = "defaultAction",
                     path = "defaultAction.do")
    })
    protected Forward returnToPreviousAction(ManageUserForm form)
    {
        String action = this.navPath.resetToPreviousAction();
        
        if (action.equals(ACTION_ADD_USER))
        {
            this.savedForm = initializeAddUser();     
        }
        else if (action.equals(ACTION_EDIT_USER))
        {
            this.savedForm = form;
        }
        else if (action.equals(ACTION_FIND_USER))
        {
            initRoleOptions(ACTION_FIND_USER);
            this.orgNodePath = this.tmpOrgNodePath;
        }
        else if (action.equals(ACTION_VIEW_USER))
        {
            if (form.getByUserProfileVisible() != null)
            {
                this.savedForm.setByUserProfileVisible(form.getByUserProfileVisible());
                this.savedForm.setByUserContactVisible(form.getByUserContactVisible());
            }
        }
    
        this.savedForm.setCurrentAction(ACTION_DEFAULT);
       
       
        if (currentOrgPageRequested != null)
        {
            this.savedForm.setOrgPageRequested(currentOrgPageRequested);
        }
        if (currentUserPageRequested != null)
        {
            this.savedForm.setUserPageRequested(currentUserPageRequested);
        
        }

        if (action.equals(ACTION_VIEW_USER))
        {                            
            return new Forward(action);
        }
        
        return new Forward(action, this.savedForm);
    }


    /**
     * setup and return to the last action
     * @jpf:action
     * @jpf:forward name="success" path="returnToPreviousAction.do"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "returnToPreviousAction.do")
		}
	)
    protected Forward cancelCurrentAction(ManageUserForm form)
    {
        this.clearCurrentMessage = true;
        clearMessage(form);
        if(this.isAddAdministrator) {
           try{
                String message = "Canceled";
                String messageParam = "Canceled=" + message ;
                
                String contextPath = "/OrganizationManagementWeb/manageCustomer/returnToFindCustomer.do";
                String url = contextPath + "?" + messageParam;
                
                this.isAddAdministrator = false;                        
                getResponse().sendRedirect(url);
             } 
          catch( IOException ioe ) {
            System.err.print(ioe.getStackTrace());
            }
        }
        
        this.savedForm.setActionElement(ACTION_CURRENT_ELEMENT);
        return new Forward("success", form);
    }

    /**
     * setup and return to the last action
     * @jpf:action
     * @jpf:forward name="success" path="returnToPreviousAction.do"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "returnToPreviousAction.do")
		}
	)
    protected Forward handleBackButton(ManageUserForm form)
    {
        this.clearCurrentMessage = true;
        clearMessage(form);
       
        this.savedForm.setActionElement(ACTION_CURRENT_ELEMENT);
       
        return new Forward("success", form);
    }
            

/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** Private methods ************* ///////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////   
    /**
     * initialize
     */
    private ManageUserForm initialize(String action)
    {        
        getUserDetails();
        
        this.orgNodePath = new ArrayList();
        this.currentOrgNodesInPathList = new HashMap();
        this.currentOrgNodeIds = new Integer[0];
        this.selectedOrgNodes = new ArrayList();

        this.savedForm = new ManageUserForm();
        this.savedForm.init(action);
        
        this.getSession().setAttribute("userHasReports", userHasReports());
        
        this.navPath = new NavigationPath();
        
        return this.savedForm;
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
    
    /**
     * initializeAddUser
     */
    private ManageUserForm initializeAddUser()
    {        
        ManageUserForm form = initialize(ACTION_ADD_USER);     
        form.setSelectedUserId(null);
        form.setSelectedUserName(null);
        
        form.setCurrentAction(MODULE_USER_PROFILE);
        form.clearSectionVisibility();
        form.setByUserProfileVisible(Boolean.TRUE);
        initRoleOptions(ACTION_ADD_USER);
        initTimeZoneOptions(ACTION_ADD_USER);
        initStateOptions(ACTION_ADD_USER);
                                       
        this.searchApplied = false;
        
        this.navPath.reset(ACTION_ADD_USER);
        
        return form;
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
       //     this.displayNewMessage = user.getDisplayNewMessage();                                                
        }
        catch (Exception e) {
            e.printStackTrace();
        }        
        getSession().setAttribute("userName", this.userName);
        
    }
    
    /**
     * initTimeZoneOptions	
    */
    private void initTimeZoneOptions(String action)
    {        
        this.timeZoneOptions = new LinkedHashMap();
              
        if (action.equals(ACTION_ADD_USER)) {
            this.timeZoneOptions.put("", Message.SELECT_TIME_ZONE);
        }     
        
        /*Changed for DEx defect # 57562 & 57563*/
        else if (isLoginWithoutTimezone){
            this.timeZoneOptions.put("", Message.SELECT_TIME_ZONE);
        }
          
        try {
            TimeZones[] timeZones = this.userManagement.getTimeZones();
            if (timeZones != null) {
               for (int i = 0 ; i < timeZones.length ; i++) {
                    this.timeZoneOptions.put(timeZones[i].getTimeZone(), 
                            timeZones[i].getTimeZoneDesc());
                } 
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     /**
     * initStateOptions
     */
    private void initStateOptions(String action)
    {        
        this.stateOptions = new LinkedHashMap();
        
        TreeMap territoriesOptions = new TreeMap();
         

        if (action.equals(ACTION_ADD_USER)) {
            this.stateOptions.put("", Message.SELECT_STATE);
        }
        if (action.equals(ACTION_EDIT_USER) || action.equals(ACTION_MY_PROFILE)) {
            this.stateOptions.put("", Message.SELECT_STATE);
        }
       
        try {
            USState[] state = this.userManagement.getStates();
            if (state != null) {
                for (int i = 0 ; i < state.length ; i++) {
                    if (!isContains (state[i].getStatePrDesc())) {
                        
                        this.stateOptions.put(state[i].getStatePr(), 
                                            state[i].getStatePrDesc());
                    } else {
                        
                        territoriesOptions.put(state[i].getStatePrDesc(),state[i]);
                        
                    }
                    
                }
            }
            orderedStateTerritories(territoriesOptions); 
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean isContains (String stateDesc) {
        
        String []USterritories = {"Virgin Islands","Puerto Rico","Palau","North Mariana Islands","Marshall Islands","Guam","F.S. of Micronesia","American Samoa"};
        
        for (int i = 0; i < USterritories.length; i++) {
            
            if (USterritories[i].equals(stateDesc)) {
                return true;
            } 
        }
        return false;
        
    } 
    
    private void orderedStateTerritories (TreeMap territoriesOptions) {
        
        Collection territories = territoriesOptions.values();
        
        Iterator iterate = territories.iterator();
        while (iterate.hasNext()) {
            USState state = (USState) iterate.next();
            this.stateOptions.put(state.getStatePr(), 
                                            state.getStatePrDesc());
        }
    }
    
    /**
     * initRoleOptions
     */
    private void initRoleOptions(String action)
    {        
        this.roleOptions = new LinkedHashMap();
        
        if (action.equals(ACTION_FIND_USER)) {
            this.roleOptions.put(Message.ANY_ROLE, Message.ANY_ROLE);
        }
        if (action.equals(ACTION_ADD_USER)) {	
            this.roleOptions.put("", Message.SELECT_ROLE);
        }
        
        try {
            Role[] roles = this.userManagement.getRoles();
            if (roles != null) {
                for (int i = 0; i < roles.length ; i++) {
                    this.roleOptions.put(roles[i].getRoleId(), 
                            roles[i].getRoleName());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void resetUserList() {
        this.userList.clear();
    }    

   /**
     * initHintQuestionOptions
     */
    private void initHintQuestionOptions(boolean myProfile)
    {                 
        try {
            PasswordHintQuestion[] options = 
                    this.userManagement.getHintQuestions();
            
            this.hintQuestionOptions = new LinkedHashMap();
            
            if (! myProfile) {
                this.hintQuestionOptions.put("", 
                        Message.SELECT_HINT_QUESTION);
            }
            
            if (options != null) {
                for (int i=0 ; i<options.length ; i++) {
                    this.hintQuestionOptions.put(
                            ((PasswordHintQuestion) options[i])
                            .getPasswordHintQuestionId(),
                            ((PasswordHintQuestion)options[i])
                            .getPasswordHintQuestion());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * buildPageTitle
     */
    private String buildPageTitle(String action, ManageUserForm form)
    {
        String title = "";
        UserProfileInformation userProfile = null;
        
        if (action.equals(ACTION_FIND_USER)) {
            title = Message.FIND_TITLE;
        }
        else
        if (action.equals(ACTION_ADD_USER)) {
            title = Message.ADD_TITLE;
            webTitle = Message.ADD_TITLE_WEB;
        }
        else
        if (action.equals(ACTION_ADD_ADMINISTRATOR)) {
            title = Message.ADD_ADMINISTRATOR_TITLE;
            webTitle = Message.ADD_ADMINISTRATOR_TITLE_WEB;
        }
        else
        if (action.equals(ACTION_VIEW_USER)) {
            title = Message.VIEW_TITLE + ": ";
            userProfile = form.getUserProfile();            
        }
        else
        if (action.equals(ACTION_EDIT_USER)) {
            title = Message.EDIT_TITLE + ": ";
            webTitle = Message.EDIT_TITLE_WEB;
            String oldFirstName = (String)this.getRequest().getSession().getAttribute("UserFirstName");
            String oldLastName = (String)this.getRequest().getSession().getAttribute("UserLastName");
            if ((oldFirstName!=null && oldFirstName.length()>0) 
                    && (oldLastName!=null && oldLastName.length()>0)){
                 title =  Message.buildPageTitle(title, oldFirstName, oldLastName);
            }
            else{
                userProfile = form.getUserProfile();
            }
        }
        if (action.equals(ACTION_CHANGE_PASSWORD)) {
            title = Message.CHANGE_PASSWORD_TITLE + ": ";
            userProfile = form.getUserProfile();
        }
         
        if (userProfile != null) {
            title =  Message.buildPageTitle(title, userProfile.getFirstName(), userProfile.getLastName());
        }
        
        return title;            
    }    
 
    /**
     * setUserProfileToForm
     */
    private UserProfileInformation setUserProfileToForm(ManageUserForm form) throws CTBBusinessException
    {
        String selectedUserName = form.getSelectedUserName();
        if (selectedUserName == null) {
            return null;
        }
            
        UserProfileInformation userProfile = 
                UserSearchUtils.getUserProfileInformation(
                this.userManagement, this.userName, selectedUserName);
                                
        userProfile.setTimeZoneDesc((String) 
                this.timeZoneOptions.get(userProfile.getTimeZone()));
		
        form.setUserProfile(userProfile);
        form.setSelectedUserId(userProfile.getUserId());
                
        return userProfile;
    }
    
    /**
     * isLoggedInUser
     */
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

    /**
     * getOrganizationNodes
     */
    private Integer[] getOrganizationNodes(Node[] ons)
    {      
        Integer[] result = null;        
                 
        if (ons != null) { 
            result = new Integer[ons.length];                  
            for (int i = 0 ; i < ons.length ; i++) {
                result[i] = ons[i].getOrgNodeId();
            }
        }        
        return result;
    }

    /**
     * initFindUser
     */
    private void initFindUser(String selectedTab, ManageUserForm form)
    {
        this.selectedModuleFind = selectedTab;
        
        if (this.selectedModuleFind.equals(MODULE_USER_PROFILE)) {
            clearUserProfileSearch(form);    
        }

        if (this.selectedModuleFind.equals(MODULE_HIERARCHY)) {
            clearHierarchySearch(form);    
        }
    }

    /**
     * clearUserProfileSearch
     */
    private void clearUserProfileSearch(ManageUserForm form)
    {   
        this.searchApplied = false;
        
        form.clearSearch();    

        form.setSelectedUserId(null);
        form.setSelectedUserName(null);
        form.setSelectedOrgNodeId(null);
    }
        
    /**
     * clearHierarchySearch
     */
    private void clearHierarchySearch(ManageUserForm form)
    {   
        this.searchApplied = false;
        this.orgNodePath = new ArrayList();
        
        form.setOrgNodeName("Top");
        form.setOrgNodeId(new Integer(0));

        form.setSelectedUserId(null);
        form.setSelectedUserName(null);
        form.setSelectedOrgNodeId(null);
    }

    /**
     * initPagingSorting
     */
    private void initPagingSorting(ManageUserForm form)
    {
        String actionElement = form.getActionElement();
        
        if ((actionElement.indexOf("orgPageRequested") > 0) 
                    || (actionElement.indexOf("orgSortOrderBy") > 0)) {
            this.searchApplied = false;
            form.setSelectedUserId(null);
            form.setSelectedUserName(null);
            form.setSelectedOrgNodeId(null);
        }
        if ((actionElement.indexOf("userPageRequested") > 0) 
                    || (actionElement.indexOf("userSortOrderBy") > 0)) {
            form.setSelectedUserId(null);
            form.setSelectedUserName(null);
        }
    }
        

    /**
     * saveUserProfileInformation
     */
    private String saveUserProfileInformation(boolean isCreateNew, 
                                    ManageUserForm form, 
                                    String userName, 
                                    List selectedOrgNodes)
    {        
        UserProfileInformation userProfile = form.getUserProfile();
        User user = userProfile.makeCopy(userName, selectedOrgNodes);
        String title = null;
        String username = user.getUserName();
        try {                    
            if (isCreateNew) {
                title = Message.ADD_TITLE;
                username = this.userManagement.createUser(this.userName, user);
            } else {
                title = Message.EDIT_TITLE;
                this.userManagement.updateUser(this.userName, user);
            }
        } 
        catch (CTBBusinessException be) {
            be.printStackTrace();
            String msg = MessageResourceBundle.getMessage(be.getMessage());
            form.setMessage(title, msg, Message.ERROR);
            username = null;
        }            
        catch (Exception e) {
            e.printStackTrace();
            form.setMessage(title, 
                    Message.EDIT_ERROR, Message.ERROR);
            username = null;
        }
                
        return username;
    }

    /**
     * initHierarchyControl
     */
    private List initHierarchyControl(Integer orgNodeId, 
                                            ManageUserForm form)
    {
        List orgNodePath = new ArrayList();
        
        List nodeAncestors = new ArrayList();
        boolean nodeIdPopulated = false;
            
        UserProfileInformation userProfile = form.getUserProfile();
        String selectedUserName = userProfile.getLoginId();
        UserNode[] orgNodes = UserPathListUtils
                .getAncestorOrganizationNodesForOrgNode(
                this.userName, orgNodeId, this.userManagement);
            
        for (int i = 0 ; i < (orgNodes.length - 1) ; i++) {
            Node orgNode = (Node)orgNodes[i];
            Integer orgId = orgNode.getOrgNodeId();
            String orgName = orgNode.getOrgNodeName();                
            if (orgId.intValue() >= 2) {    // ignore Root
                PathNode node = new PathNode();
                node.setId(orgId);
                node.setName(orgName);
                nodeAncestors.add(node);                
                form.setOrgNodeId(orgId);
                form.setOrgNodeName(orgName);
                form.resetValuesForPathList();
                nodeIdPopulated = true;
            }
        }    
        
        if (!nodeIdPopulated) {
            form.setOrgNodeId(null);
            form.setOrgNodeName(null);
            form.resetValuesForPathList();
        }

        orgNodePath = UserPathListUtils.setupOrgNodePath(nodeAncestors);       
        
        return orgNodePath;
    }

    /**
     * setFullPathNodeName
     */
    private void setFullPathNodeName(List orgNodes)    
    {
        for (int i = 0 ; i < orgNodes.size() ; i++) {
            PathNode node = (PathNode)orgNodes.get(i);
            Integer orgNodeId = node.getId();
            String fullPathNodeName = UserPathListUtils
                    .getFullPathNodeName(this.userName, 
                    orgNodeId, this.userManagement);
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
     * clearMessage
     */
    private void clearMessage(ManageUserForm form)
    {        
        if (this.clearCurrentMessage) {        
            form.clearMessage();
            this.savedForm.clearMessage();
        }
        this.clearCurrentMessage = true;
    }
    
        
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** ManageUserForm ************* ////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    

    /**
     * common form for all action / jsp in this page flow
     */
    public static class ManageUserForm extends SanitizedFormData
    {
        private String actionElement;
        private String currentAction;
        
        private String selectedTab;

        private String selectedOrgLevel;        
        private Integer selectedUserId;
        private String selectedUserName;
 

        // collapsible sections        
        private Boolean byUserProfileVisible;
        private Boolean byUserContactVisible;
        
        // user profile
        private UserProfileInformation userProfile;
        
        // find all users
        private String orgNodeName;
        private Integer orgNodeId;
        private Integer selectedOrgNodeId;
        private String selectedOrgNodeName;
        
        // org pager
        private String orgSortColumn;
        private String orgSortOrderBy;
        private Integer orgPageRequested;
        private Integer orgMaxPage;

        // user pager
        private String userSortColumn;
        private String userSortOrderBy;
        private Integer userPageRequested;
        private Integer userMaxPage;

        // messages
        private Message message;
        
        
        
        // constructor
        public ManageUserForm()
        {
        }
        
        // initilize method
        public void init(String action)
        {
            this.actionElement = ACTION_DEFAULT;
            this.currentAction = ACTION_DEFAULT;
            
            this.selectedTab = MODULE_USER_PROFILE;
            
            clearSearch();

            this.selectedUserId = null;
            this.selectedUserName=null;
            
            this.orgNodeName = "Top";
            this.orgNodeId = new Integer(0);
            this.selectedOrgNodeId = null;
            this.selectedOrgNodeName = null;
            
            this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
            this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.orgPageRequested = new Integer(1);                
            this.orgMaxPage = new Integer(1);      

            this.userSortColumn = FilterSortPageUtils.USER_DEFAULT_SORT_COLUMN;
            this.userSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.userPageRequested = new Integer(1);       
            this.userMaxPage = new Integer(1);      

            this.userProfile = new UserProfileInformation();
            this.message = new Message();            
            
            if (action.equals(ACTION_FIND_USER)) {
                this.userProfile.setRole(Message.ANY_ROLE);            
            }
            else {
                this.userProfile.setRole(Message.SELECT_ROLE);
                this.userProfile.setTimeZoneDesc(Message.SELECT_TIME_ZONE);
            }
        }   
        
        // validation
        public ActionErrors validate(ActionMapping mapping, 
                                HttpServletRequest request)
        {
            ActionErrors errs = super.validate(mapping, request);
                        
            if (!errs.isEmpty()) {
                request.setAttribute("hasAlert", Boolean.TRUE);
            }
            return errs;
        }
        
        // validate values
        public void validateValues()
        {
            if (this.orgSortColumn == null)
                this.orgSortColumn = 
                                FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;

            if (this.orgSortOrderBy == null)
                this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;

            if (this.orgPageRequested == null)
                this.orgPageRequested = new Integer(1);
                
            if (this.orgPageRequested.intValue() <= 0) {
                this.orgPageRequested = new Integer(1);
                this.selectedOrgNodeId = null;
            }
        
            if (this.orgMaxPage == null)
                this.orgMaxPage = new Integer(1);

            if (this.orgPageRequested.intValue() 
                                > this.orgMaxPage.intValue()) {
                this.orgPageRequested = 
                                new Integer(this.orgMaxPage.intValue());
                this.selectedOrgNodeId = null;
            }

            if (this.userSortColumn == null)
                this.userSortColumn = 
                                FilterSortPageUtils.USER_DEFAULT_SORT_COLUMN;

            if (this.userSortOrderBy == null)
                this.userSortOrderBy = FilterSortPageUtils.ASCENDING;

            if (this.userPageRequested == null) {
                this.userPageRequested = new Integer(1);
            }
                
            if (this.userPageRequested.intValue() <= 0)            
                this.userPageRequested = new Integer(1);

            if (this.userMaxPage == null)
                this.userMaxPage = new Integer(1);

            if (this.userPageRequested.intValue() > this.userMaxPage.intValue()) {
                this.userPageRequested = new Integer(this.userMaxPage.intValue());                
                this.selectedUserId = null;
                this.selectedUserName=null;
            }
        }     
        
        
        // reset values based on action
        public void resetValuesForAction(String actionElement, 
                                        String fromAction) 
        {
            if (actionElement.equals("{actionForm.orgSortOrderBy}")) {
                this.orgPageRequested = new Integer(1);
            }
            if (actionElement.equals("{actionForm.userSortOrderBy}")) {
                this.userPageRequested = new Integer(1);
            }
            if (actionElement.equals("ButtonGoInvoked_userSearchResult") ||
                actionElement.equals("EnterKeyInvoked_userSearchResult")) {
                this.selectedUserId = null;
                this.selectedUserName = null;
            }
            if (actionElement.equals("ButtonGoInvoked_tablePathListAnchor") ||
                actionElement.equals("EnterKeyInvoked_tablePathListAnchor")) {
                this.selectedOrgNodeId = null;
                if (fromAction.equals(ACTION_FIND_USER)) {
                    this.selectedUserId = null;
                    this.selectedUserName = null;
                }
            }
        }
                                                                                     
        // reset values for path list
        public void resetValuesForPathList()
        {
            this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
            this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.orgPageRequested = new Integer(1);    
            this.orgMaxPage = new Integer(1);      

            this.userSortColumn = FilterSortPageUtils.USER_DEFAULT_SORT_COLUMN;
            this.userSortOrderBy = FilterSortPageUtils.ASCENDING;      
            this.userPageRequested = new Integer(1);    
            this.userMaxPage = new Integer(1);      
        }     
        
        
        // clear search 
        public void clearSearch()
        {   
            this.userProfile = new UserProfileInformation();
            this.userProfile.setRole(Message.ANY_ROLE);
        }
        
        // clear message
        public void clearMessage()
        {   
            this.message = null;
        }
        
        // create a cloned object 
        public ManageUserForm createClone()
        {
            ManageUserForm copied = new ManageUserForm();
            
            copied.setActionElement(this.actionElement);
            copied.setCurrentAction(this.currentAction);
            
            copied.setSelectedTab(this.selectedTab);

            copied.setSelectedOrgLevel(this.selectedOrgLevel);            
            copied.setSelectedUserId(this.selectedUserId);
            copied.setSelectedUserName(this.selectedUserName);

            copied.setByUserProfileVisible(this.byUserProfileVisible);
            copied.setByUserContactVisible(this.byUserContactVisible);

            copied.setOrgNodeName(this.orgNodeName);
            copied.setOrgNodeId(this.orgNodeId);
            copied.setSelectedOrgNodeId(this.selectedOrgNodeId);
            copied.setSelectedOrgNodeName(this.selectedOrgNodeName);
            
            copied.setOrgSortColumn(this.orgSortColumn);
            copied.setOrgSortOrderBy(this.orgSortOrderBy);
            copied.setOrgPageRequested(this.orgPageRequested);
            copied.setOrgMaxPage(this.orgMaxPage);
            
            copied.setUserSortColumn(this.userSortColumn);
            copied.setUserSortOrderBy(this.userSortOrderBy);
            copied.setUserPageRequested(this.userPageRequested);      
            copied.setUserMaxPage(this.userMaxPage);
                        
            copied.setUserProfile(this.userProfile);

            copied.setMessage(this.message);
                                                
            return copied;                    
        } 
        
        // get and set methods       
        public void setActionElement(String actionElement)
        {
            this.actionElement = actionElement;
        }        
        public String getActionElement()
        {
            return this.actionElement != null ? this.actionElement : ACTION_DEFAULT;
        }
        public void setCurrentAction(String currentAction)
        {
            this.currentAction = currentAction;
        }
        public String getCurrentAction()
        {
            return this.currentAction != null ? this.currentAction : ACTION_DEFAULT;
        }
        public void setSelectedTab(String selectedTab)
        {
         	if ((selectedTab == null) 
                    || selectedTab.equals(MODULE_USER_PROFILE) 
                    || selectedTab.equals(MODULE_HIERARCHY))
            	this.selectedTab = selectedTab;
            else
            	throw new RuntimeException("XSS attack detected!");
        }         
        public String getSelectedTab()
        {
            return this.selectedTab != null ? 
                                this.selectedTab : MODULE_USER_PROFILE;
        }
        public void setSelectedUserId(Integer selectedUserId)
        {
            this.selectedUserId = selectedUserId;
        }
        
        public Integer getSelectedUserId()
        {
            return this.selectedUserId;
        }
        
        public String getSelectedUserName()
        {
            return this.selectedUserName;
        }
        public void setSelectedUserName(String selectedUserName)
        {
            this.selectedUserName = selectedUserName;
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
        public void setSelectedOrgNodeName(String selectedOrgNodeName)
        {
            this.selectedOrgNodeName = selectedOrgNodeName;
        }
        public String getSelectedOrgNodeName()
        {
            return this.selectedOrgNodeName;
        }        
        public void setOrgSortColumn(String orgSortColumn)
        {
            this.orgSortColumn = orgSortColumn;
        }
        public String getOrgSortColumn()
        {
            return this.orgSortColumn != null ? 
                    this.orgSortColumn 
                    : FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
        }       
        public void setOrgSortOrderBy(String orgSortOrderBy)
        {
            this.orgSortOrderBy = orgSortOrderBy;
        }
        public String getOrgSortOrderBy()
        {
            return this.orgSortOrderBy != null ? 
                    this.orgSortOrderBy 
                    : FilterSortPageUtils.ASCENDING;
        }       
        public void setOrgPageRequested(Integer orgPageRequested)
        {
            this.orgPageRequested = orgPageRequested;
        }
        public Integer getOrgPageRequested()
        {
            return this.orgPageRequested != null 
                    ? this.orgPageRequested : new Integer(1);
        }        
        public void setOrgMaxPage(Integer orgMaxPage)
        {
            this.orgMaxPage = orgMaxPage;
        }
        public Integer getOrgMaxPage()
        {
            return this.orgMaxPage != null 
                    ? this.orgMaxPage : new Integer(1);
        }        
        public void setUserSortColumn(String userSortColumn)
        {
            this.userSortColumn = userSortColumn;
        }
        public String getUserSortColumn()
        {
            return this.userSortColumn != null 
                    ? this.userSortColumn 
                    : FilterSortPageUtils.USER_DEFAULT_SORT_COLUMN;
        }       
        public void setUserSortOrderBy(String userSortOrderBy)
        {
            this.userSortOrderBy = userSortOrderBy;
        }
        public String getUserSortOrderBy()
        {
            return this.userSortOrderBy != null 
                            ? this.userSortOrderBy 
                            : FilterSortPageUtils.ASCENDING;
        }       
        public void setUserPageRequested(Integer userPageRequested)
        {
            this.userPageRequested = userPageRequested;
        }
        public Integer getUserPageRequested()
        {
            return this.userPageRequested != null 
                    ? this.userPageRequested 
                    : new Integer(1);
        }        
        public void setUserMaxPage(Integer userMaxPage)
        {
            this.userMaxPage = userMaxPage;
        }
        public Integer getUserMaxPage()
        {
            return this.userMaxPage != null 
                    ? this.userMaxPage : new Integer(1);
        }        
        // user profile
        public void setUserProfile(UserProfileInformation userProfile)
        {
            this.userProfile = userProfile;
        }
        public UserProfileInformation getUserProfile()
        {
            if (this.userProfile == null) 
                this.userProfile = new UserProfileInformation();
            return this.userProfile;
        }

        public String getStringAction()
        {
            String action = ACTION_ADD_USER;
            if ((this.selectedUserName != null) 
                    && (!this.selectedUserName.equals("")))
                    action = ACTION_EDIT_USER;
                return action; 
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

        public void setSelectedOrgLevel(String selectedOrgLevel)
        {
            this.selectedOrgLevel = selectedOrgLevel;
        }
        public String getSelectedOrgLevel()
        {
            return this.selectedOrgLevel;
        }       


        public void setByUserProfileVisible(Boolean byUserProfileVisible)
        {
            this.byUserProfileVisible = byUserProfileVisible;
        }
        public Boolean getByUserProfileVisible()
        {
            return this.byUserProfileVisible;
        }        
        public void setByUserContactVisible(Boolean byUserContactVisible)
        {
            this.byUserContactVisible = byUserContactVisible;
        }
        public Boolean getByUserContactVisible()
        {
            return this.byUserContactVisible;
        }        
        public void clearSectionVisibility()
        {
            this.byUserProfileVisible = Boolean.FALSE;
            this.byUserContactVisible = Boolean.FALSE;
        }
      
    }

   // Added getter method for pageflow attributes for Weblogic 10
	public String getPageTitle() {
		return pageTitle;
	}

	public LinkedHashMap getRoleOptions() {
		return roleOptions;
	}

	public String getWebTitle() {
		return webTitle;
	}

	public LinkedHashMap getTimeZoneOptions() {
		return timeZoneOptions;
	}

	public String getPageMessage() {
		return pageMessage;
	}

	public String getHelpLink() {
		return helpLink;
	}

	public List getSelectedOrgNodes() {
		return selectedOrgNodes;
	}

	public LinkedHashMap getHintQuestionOptions() {
		return hintQuestionOptions;
	}

	public LinkedHashMap getStateOptions() {
		return stateOptions;
	}
    

}
