package utils; 

import com.ctb.bean.testAdmin.Role;
import com.ctb.bean.testAdmin.User;
import com.ctb.control.userManagement.UserManagement;
import dto.UserProfileInformation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class PermissionsUtils 
{ 
	// the role names
    public static final String ROLE_NAME_CTB_ROOT                           = "CTB ROOT";
	public static final String ROLE_NAME_ACCOUNT_MANAGER                    = "ACCOUNT MANAGER";
	public static final String ROLE_NAME_ADMINISTRATOR                      = "ADMINISTRATOR";
	public static final String ROLE_NAME_ACCOMMODATIONS_COORDINATOR         = "ADMINISTRATIVE COORDINATOR";
	public static final String ROLE_NAME_COORDINATOR                        = "COORDINATOR";
	public static final String ROLE_NAME_PROCTOR                            = "PROCTOR";

	public static final String ACTION_VIEW                                  = "View";
	public static final String ACTION_EDIT                                  = "Edit";
	public static final String ACTION_DELETE                                = "Delete";
	public static final String ACTION_CHANGEPASSWORD                        = "Change Password";

	public static final String DEFAULT_PERMISSION_TOKEN                     = "FFFF"; // (View, Edit, Delete, ChangePassword) in this order
	public static final String ENABLE_ALL_PERMISSION_TOKEN                  = "TTTT";
	public static final String VIEW_ONLY_PERMISSION_TOKEN                   = "TFFF";
	public static final String NON_DELETE_PERMISSION_TOKEN                  = "TTFT";


    /**
     * setPermissionRequestAttribute
     */    
    public static void setPermissionRequestAttribute(HttpServletRequest request, UserProfileInformation userProfile)
    {
        String actionPermission = DEFAULT_PERMISSION_TOKEN;      
        if (userProfile != null) {
            actionPermission = userProfile.getActionPermission();
        }
        
        request.setAttribute("disableViewButton", isViewDisabled(actionPermission));
        request.setAttribute("disableEditButton", isEditDisabled(actionPermission));
        request.setAttribute("disableDeleteButton", isDeleteDisabled(actionPermission));
        request.setAttribute("disableChangePasswordButton", isChangePasswordDisabled(actionPermission));
    }
    
    /**
     * getPermission
     */    
    public static String getPermission(Integer loginUserId, String loginRoleName, Integer[] userOrgNodeIds, 
                                       Integer selectedUserId, String selectedRoleName, Integer[] selectedUserOrgNodeIds)
    {   
        String permToken = DEFAULT_PERMISSION_TOKEN;   
        
        try {            
            if (loginUserId.intValue() == selectedUserId.intValue()) {
                permToken = NON_DELETE_PERMISSION_TOKEN;
            }                                   
            else if (loginRoleName.equalsIgnoreCase(ROLE_NAME_CTB_ROOT)) {
                permToken = ENABLE_ALL_PERMISSION_TOKEN;
            }             
            else if (loginRoleName.trim().equalsIgnoreCase(ROLE_NAME_ACCOUNT_MANAGER)) {
                 permToken = ENABLE_ALL_PERMISSION_TOKEN;
            }             
            else if (loginRoleName.trim().equalsIgnoreCase(ROLE_NAME_ADMINISTRATOR)) {
                 
                  // for any lower role admin has all the permissions
                  if(selectedRoleName.equalsIgnoreCase(ROLE_NAME_PROCTOR) 
                        || selectedRoleName.equalsIgnoreCase(ROLE_NAME_COORDINATOR)
                        || selectedRoleName.equalsIgnoreCase(ROLE_NAME_ACCOMMODATIONS_COORDINATOR)){    
                    permToken = ENABLE_ALL_PERMISSION_TOKEN;
                 }
                 // for same role
                 else if(selectedRoleName.equalsIgnoreCase(ROLE_NAME_ADMINISTRATOR)){    
                    
                    // in my node
                    if(isSamelevelNode(userOrgNodeIds, selectedUserOrgNodeIds)){
                        permToken = VIEW_ONLY_PERMISSION_TOKEN;
                    }
                    //in lower node
                    else{
                        permToken = ENABLE_ALL_PERMISSION_TOKEN;
                    }
                 }     
            }            
            else if (loginRoleName.trim().equalsIgnoreCase(ROLE_NAME_ACCOMMODATIONS_COORDINATOR)
                    || loginRoleName.trim().equalsIgnoreCase(ROLE_NAME_COORDINATOR)) {
                        
                  if(selectedRoleName.equalsIgnoreCase(ROLE_NAME_PROCTOR) 
                        || selectedRoleName.equalsIgnoreCase(ROLE_NAME_COORDINATOR)
                        || selectedRoleName.equalsIgnoreCase(ROLE_NAME_ACCOMMODATIONS_COORDINATOR)
                        || selectedRoleName.equals(ROLE_NAME_ADMINISTRATOR)){   
                             
                      if(!(isSamelevelNode(userOrgNodeIds, selectedUserOrgNodeIds))){  
                          permToken = VIEW_ONLY_PERMISSION_TOKEN;
                      }
                 }
            } 
        }
        catch (Exception be) {
            be.printStackTrace();
        } 
        
        return permToken;
    }
    
    
    private static boolean isSamelevelNode(Integer[] loginOrgNodeIds, Integer[] selectedOrgNodeIds) 
                                        { 
        boolean isSameNode = false; 
        try {
            
            for (int i=0; i<loginOrgNodeIds.length; i++) {
               Integer loginOrgNodeId = (loginOrgNodeIds[i]);
               for (int j=0; j<selectedOrgNodeIds.length; j++) {
                   Integer selectedOrgNodeId = (selectedOrgNodeIds[j]); 
                   if(loginOrgNodeId!=null && selectedOrgNodeId!=null 
                        && loginOrgNodeId.toString().equals(selectedOrgNodeId.toString())){
                       isSameNode = true;
                   }
               }
            } 
            
        }
        catch (Exception be) {
            be.printStackTrace();
        }  
        return isSameNode;
    }

    public static boolean canViewUser(String permToken) 
    {
        return getToken(permToken, ACTION_VIEW).equalsIgnoreCase("T");
    }
    
    public static boolean canEditUser(String permToken) 
    {
        return getToken(permToken, ACTION_EDIT).equalsIgnoreCase("T");
    }

    public static boolean canDeleteUser(String permToken) 
    {
        return getToken(permToken, ACTION_DELETE).equalsIgnoreCase("T");
    }

    public static boolean canChangeUserPassword(String permToken) 
    {
        return getToken(permToken, ACTION_CHANGEPASSWORD).equalsIgnoreCase("T");
    }

    public static Boolean isViewDisabled(String permToken) 
    {
        return new Boolean(! canViewUser(permToken));
    }

    public static Boolean isEditDisabled(String permToken) 
    {
        return new Boolean(! canEditUser(permToken));
    }

    public static Boolean isDeleteDisabled(String permToken) 
    {
        return new Boolean(! canDeleteUser(permToken));
    }

    public static Boolean isChangePasswordDisabled(String permToken) 
    {
        return new Boolean(! canChangeUserPassword(permToken));
    }

    public static String getToken(String permToken, String action) 
    {
        if (ACTION_VIEW.equals(action))
            return permToken.substring(0, 1);
        if (ACTION_EDIT.equals(action))
            return permToken.substring(1, 2);
        if (ACTION_DELETE.equals(action))
            return permToken.substring(2, 3);
        if (ACTION_CHANGEPASSWORD.equals(action))
            return permToken.substring(3, 4);
        return "F";
    }
    
} 
