package utils; 

import javax.servlet.http.HttpServletRequest;


public class PermissionsUtils 
{ 
	// the role names
	public static final String ROLE_NAME_ACCOUNT_MANAGER              = "ACCOUNT MANAGER";
	public static final String ROLE_NAME_ADMINISTRATOR                = "ADMINISTRATOR";
	public static final String ROLE_NAME_ACCOMMODATIONS_COORDINATOR   = "ADMINISTRATIVE COORDINATOR";
	public static final String ROLE_NAME_COORDINATOR                  = "COORDINATOR";
	public static final String ROLE_NAME_PROCTOR                      = "PROCTOR";
    public static final String DEFAULT_PERMISSION_TOKEN               = "FFFFF"; // (Edit Profile, Edit Framework, Create Admistrator, manageorganization) in this order
    public static final String ENABLE_ALL_PERMISSION_TOKEN            = "TTTTT";
    
    
    public static final String ACTION_VIEW                            = "View";
	public static final String ACTION_EDIT                            = "Edit";
	public static final String ACTION_DELETE                          = "Delete";
    public static final String ACTION_ADD                             = "Add";
    
    
    public static final String ACTION_EDIT_PROFILE                    = "Edit Profile";
	public static final String ACTION_EDIT_FRAMEWORK                  = "Edit Framework";
	public static final String ACTION_CREATE_ADMINISTRATOR            = "Create Administrator";
	public static final String ACTION_MANAGE_ORGANIZATION             = "Manage Organization";
    public static final String VIEW_ADD_PERMISSION_TOKEN              = "TFFT";
	public static final String DISABLE_ALL_PERMISSION_TOKEN           = "FFFF";  
    
    
    //file upload
    public static final String ACTION_DELETE_FILE                     = "deleteFile";
	public static final String ACTION_FAIL_RECORD                     = "getFailedRecords";
	public static final String ACTION_REFRESH                         = "refresh";
      
    public static final String DEFAULT_FILE_PERMISSION_TOKEN          = "FFF";
    public static final String REFRESH_PERMISSION_TOKEN               = "FFT";
    
    /**
     * setPermissionRequestAttribute
     */    
    public static void setPermissionRequestAttribute(HttpServletRequest request, 
                                                     Boolean isSelectedCustomer)
    {
        request.setAttribute("disableViewProfileButton", 
                                    isSelectedCustomer.toString());
        request.setAttribute("disableEditProfileButton", 
                                    isSelectedCustomer.toString());
        request.setAttribute("disableEditFrameworkButton",
                                    isSelectedCustomer.toString());
        request.setAttribute("disableCreateAdministratorButton", 
                                    isSelectedCustomer.toString());
        request.setAttribute("disableManageOrganizationButton", 
                                    isSelectedCustomer.toString());
        
    }
    
    public static void setLicensePermission(HttpServletRequest request, 
                                                     Boolean isLicenseEnable)
    {
       
        request.setAttribute("disableManageLicenseButton", 
                                    isLicenseEnable.toString());
    }
    
   
            
    
            
 
      
    
    
    /**
     * getPermission
     */    
    public static String getPermission(Integer selectedCustomerId)
    {   
        String permToken = DEFAULT_PERMISSION_TOKEN;   
        
        if( selectedCustomerId != null ) {
            
            permToken = ENABLE_ALL_PERMISSION_TOKEN;
            
        }
        
        return permToken;
    }
    
    
    private static boolean isSamelevelNode(Integer[] loginOrgNodeIds, 
                                           Integer[] selectedOrgNodeIds) 
                                        { 
        boolean isSameNode = false; 
        try {
            
            for ( int i = 0; i < loginOrgNodeIds.length; i++) {
                
               Integer loginOrgNodeId = (loginOrgNodeIds[i]);
               for ( int j = 0; j < selectedOrgNodeIds.length; j++) {
                
                   Integer selectedOrgNodeId = (selectedOrgNodeIds[j]); 
                   if( loginOrgNodeId!=null && selectedOrgNodeId!=null 
                        && loginOrgNodeId.toString().equals(selectedOrgNodeId.toString()) ){
                            
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

    public static boolean canEditProfile(String permToken) 
    {
        return getToken(permToken, ACTION_EDIT_PROFILE).equalsIgnoreCase("T");
    }
    
    public static boolean canEditFramework(String permToken) 
    {
        return getToken(permToken, ACTION_EDIT_FRAMEWORK).equalsIgnoreCase("T");
    }

    public static boolean canCreateAdministrator(String permToken) 
    {
        return getToken(permToken, ACTION_CREATE_ADMINISTRATOR).equalsIgnoreCase("T");
    }

    public static boolean canManageOrganization(String permToken) 
    {
        return getToken(permToken, ACTION_MANAGE_ORGANIZATION).equalsIgnoreCase("T");
    }

    public static Boolean isEditProfileDisabled(String permToken) 
    {
        return new Boolean(! canEditProfile(permToken));
    }

    public static Boolean isEditFrameworkDisabled(String permToken) 
    {
        return new Boolean(! canEditFramework(permToken));
    }

    public static Boolean isCreateAdministratorDisabled(String permToken) 
    {
        return new Boolean(! canCreateAdministrator(permToken));
    }

    public static Boolean isManageOrganizationDisabled(String permToken) 
    {
        return new Boolean(! canManageOrganization(permToken));
    }

    public static String getToken(String permToken, String action) 
    {
        if ( ACTION_EDIT_PROFILE.equals(action) ) {
            
            return permToken.substring(0, 1);
        
        }
        if ( ACTION_EDIT_FRAMEWORK.equals(action) ) {
            
            return permToken.substring(1, 2);
            
        }
        if ( ACTION_CREATE_ADMINISTRATOR.equals(action) ) {
            
            return permToken.substring(2, 3);
            
        }
        if ( ACTION_MANAGE_ORGANIZATION.equals(action) ) {
            
            return permToken.substring(3, 4);
            
        }
        return "F";
    }
    
    //Organization permission
    
    public static boolean canViewOrganization(String permToken) 
    {
        return getTokenValue(permToken, ACTION_VIEW).equalsIgnoreCase("T");
    }
    
    public static boolean canEditOrganization(String permToken) 
    {
        return getTokenValue(permToken, ACTION_EDIT).equalsIgnoreCase("T");
    }

    public static boolean canDeleteOrganization(String permToken) 
    {
        return getTokenValue(permToken, ACTION_DELETE).equalsIgnoreCase("T");
    }
    
    public static boolean canADDOrganization(String permToken) 
    {
        return getTokenValue(permToken, ACTION_ADD).equalsIgnoreCase("T");
    }

    public static Boolean isViewDisabled(String permToken) 
    {
        return new Boolean(! canViewOrganization(permToken));
    }

    public static Boolean isEditDisabled(String permToken) 
    {
        return new Boolean(! canEditOrganization(permToken));
    }

    public static Boolean isDeleteDisabled(String permToken) 
    {
        return new Boolean(! canDeleteOrganization(permToken));
    }
    
    public static Boolean isADDDisabled(String permToken) 
    {
        return new Boolean(! canADDOrganization(permToken));
    }

    

    public static String getTokenValue(String permToken, String action) 
    {
        if ( ACTION_VIEW.equals(action) ) {
            
            return permToken.substring(0, 1);
        
        }
        if ( ACTION_EDIT.equals(action) ) {
        
            return permToken.substring(1, 2);
            
        }
        if ( ACTION_DELETE.equals(action) ) {
            
            return permToken.substring(2, 3);
            
        }
        if ( ACTION_ADD.equals(action) ) {
            
            return permToken.substring(3, 4);
        }    
        return "F";
    }
    
    
     //File permission
    
    public static boolean canDeleteFile(String permToken) 
    {
        return getFileTokenValue(permToken, ACTION_DELETE_FILE).equalsIgnoreCase("T");
    }
    
    public static boolean canExportError(String permToken) 
    {
        return getFileTokenValue(permToken, ACTION_FAIL_RECORD).equalsIgnoreCase("T");
    }

    public static boolean canRefresh(String permToken) 
    {
        return getFileTokenValue(permToken, ACTION_REFRESH).equalsIgnoreCase("T");
    }
    
    public static Boolean isDeleteFileDisabled(String permToken) 
    {
        return new Boolean(! canDeleteFile(permToken));
    }

    public static Boolean isExportErrorDisabled(String permToken) 
    {
        return new Boolean(! canExportError(permToken));
    }

    public static Boolean isRefreshDisabled(String permToken) 
    {
        return new Boolean(! canRefresh(permToken));
    }
    
     

    public static String getFileTokenValue(String permToken, String action) 
    {
        if ( ACTION_DELETE_FILE.equals(action) ) {
            
            return permToken.substring(0, 1);
        
        }
        if ( ACTION_FAIL_RECORD.equals(action) ) {
        
            return permToken.substring(1, 2);
            
        }
        if ( ACTION_REFRESH.equals(action) ) {
            
            return permToken.substring(2, 3);
            
        }
           
        return "F";
    }
    
    
    
    
} 
