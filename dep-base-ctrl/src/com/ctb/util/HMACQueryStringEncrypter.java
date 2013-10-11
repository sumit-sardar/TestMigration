package com.ctb.util;

import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.User;


public class HMACQueryStringEncrypter {
	
	// the role names
	private String ROLE_NAME_ACCOUNT_MANAGER                     = "ACCOUNT MANAGER";
	private String ROLE_NAME_ADMINISTRATOR                       = "ADMINISTRATOR";
	private String ROLE_NAME_ACCOMMODATIONS_COORDINATOR          = "ADMINISTRATIVE COORDINATOR";
	private String ROLE_NAME_COORDINATOR                         = "COORDINATOR";
	private String ROLE_NAME_PROCTOR                             = "PROCTOR";
	
	private String URL_ENCODING 					= "UTF-8";
	private String ENCODING_ALGORITHM 				= "HmacSHA1";
	private String timeZone 						= "GMT";
	private String apykey 							= "217907";
	private String IP 								= "127.0.0.1";
	private String SECRET_KEY 						= "WPZguVF49hXaRuZfe9L29ItsC2I";//encryptionKey
	private int SIGNATURE_VALIDITY_SECONDS 			= 60;
	
	User user = null;
	HMACQueryStringBuilder queryStringBuilder = null;

	//public HMACQueryStringEncoder() {}

	/**
	 * @param _user	User object for the currently logged in user
	 * @param sharedKey	encryption key supplied by PRISM
	 */
	public HMACQueryStringEncrypter(User _user, String sharedKey) {
		
		if (_user == null) throw new IllegalArgumentException("User object null.");
		if (sharedKey == null || sharedKey.length()==0) throw new IllegalArgumentException("sharedKey null or empty.");
		user = _user;
		SECRET_KEY = sharedKey;		
		queryStringBuilder = new HMACQueryStringBuilder(SECRET_KEY, SIGNATURE_VALIDITY_SECONDS, ENCODING_ALGORITHM);
		//timeZone = java.util.TimeZone.getTimeZone(user.getTimeZone()).getDisplayName(false,java.util.TimeZone.SHORT);//"PST";    	
		queryStringBuilder.setTimeZone(timeZone);
		queryStringBuilder.setENCODING_ALGORITHM(ENCODING_ALGORITHM);
		queryStringBuilder.setURL_ENCODING(URL_ENCODING);
	}
	
	/**
     * Builds and encrypts all parameteres needed to be passed to PRISM
     * Params=customer_id=123&org_node_code=abc&hierarchy_level=1&application_name=OAS&time_stamp=&shared_key=&user_role=Admin
     * @return String
	 * @throws Exception
     */	
	public String encrypt ()
	{
		String requestParam = "";
		try
		{
			String orgNodeCode = "";
			Integer hierarchyLevel = 0;
			Node[] organizationNodes = user.getOrganizationNodes();
			//this.userProfile.getOrgNodeNamesString()    		     
	        if (organizationNodes != null) {
	            for (int i=0 ; i<organizationNodes.length ; i++) {
	            	hierarchyLevel++;
	                Node node = organizationNodes[i];
	                orgNodeCode = orgNodeCode + (node.getOrgNodeCode() == null?"":node.getOrgNodeCode());
	                if (i < (organizationNodes.length - 1))
	                	orgNodeCode = orgNodeCode + ", ";
	            }
	        }
			Integer customerId = this.user.getCustomer().getCustomerId();    			
			
			String appName = "OAS";
			String sharedKey = "WPZguVF49hXaRuZfe9L29ItsC2I";
			String userRole = (isAdminUser()||isAdminCoordinatorUser())?"Admin":"User";
			/*
			String userRole = this.user.getRole().getRoleName();
			if (userName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) ||
            userName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR))
				userName = "Admin";
			else
				userName = "User";
			*/
			String datetimeStamp = "";//in GMT
			
			//requestParam = queryStringBuilder.buildAuthenticatedQueryString(apykey, IP);
			requestParam = queryStringBuilder.buildAuthenticatedQueryString(customerId, orgNodeCode, hierarchyLevel, appName, sharedKey, userRole, IP, datetimeStamp);
			//System.out.println(REQUEST_URL + "?" + requestParam);
			System.out.println(requestParam);
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		return (requestParam);
	}

    private boolean isAdminUser() 
    {               
        String roleName = this.user.getRole().getRoleName();        
        return roleName.equalsIgnoreCase(this.ROLE_NAME_ADMINISTRATOR); 
    }
    
    private boolean isAdminCoordinatorUser() 
    {               
        String roleName = this.user.getRole().getRoleName();        
        return roleName.equalsIgnoreCase(this.ROLE_NAME_ACCOMMODATIONS_COORDINATOR); 
    }

}
