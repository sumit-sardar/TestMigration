package com.ctb.testSessionInfo.utils; 

public class PermissionsUtils 
{ 
	// the role names
	public static final String ROLE_NAME_ACCOUNT_MANAGER                     = "ACCOUNT MANAGER";
	public static final String ROLE_NAME_ADMINISTRATOR                       = "ADMINISTRATOR";
	public static final String ROLE_NAME_ACCOMMODATIONS_COORDINATOR          = "ADMINISTRATIVE COORDINATOR";
	public static final String ROLE_NAME_COORDINATOR                         = "COORDINATOR";
	public static final String ROLE_NAME_PROCTOR                             = "PROCTOR";

    
    public static boolean canViewStudent(String role) 
    {
        return true;
    }
    
    public static boolean canEditStudent(String role) 
    {
        if (role != null) {
            if (role.equals(ROLE_NAME_ACCOUNT_MANAGER))
                return true;
            if (role.equals(ROLE_NAME_ADMINISTRATOR))
                return true;
            if (role.equals(ROLE_NAME_ACCOMMODATIONS_COORDINATOR))
                return true;
        }                    
        return false;
    }

    public static boolean canDeleteStudent(String role) 
    {
        if (role != null) {
            if (role.equals(ROLE_NAME_ACCOUNT_MANAGER))
                return true;
            if (role.equals(ROLE_NAME_ADMINISTRATOR))
                return true;
            if (role.equals(ROLE_NAME_ACCOMMODATIONS_COORDINATOR))
                return true;
        }
        return false;
    }
   
    public static String showEditButton(String role) 
    {
        if (canEditStudent(role)) 
            return "true";
        else 
            return "false";
    }
        
    public static String showDeleteButton(String role) 
    {
        if (canDeleteStudent(role)) 
            return "true";
        else 
            return "false";
    }
} 
