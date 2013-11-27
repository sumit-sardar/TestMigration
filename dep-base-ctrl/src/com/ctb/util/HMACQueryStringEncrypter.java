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
	private String IP 								= null;//"127.0.0.1";
	private String SECRET_KEY 						= "";//"WPZguVF49hXaRuZfe9L29ItsC2I";//encryptionKey
	private int SIGNATURE_VALIDITY_SECONDS 			= 30;
	private Integer selectedOrgNodeId				= 0;
	private String appName 							= "OAS";
	
	User user = null;
	HMACQueryStringBuilder queryStringBuilder = null;

	int CustomerId=0;
	String OrgNodeCode=null;
	int HeirarchyLevel=0;
	boolean WebServiceSSO = false;
	//public HMACQueryStringEncoder() {}

	/**
	 * @param _user	User object for the currently logged in user
	 * @param sharedKey	encryption key supplied by PRISM
	 * @param _selectedOrgNodeId	orgNodeId for current user
	 */
	public HMACQueryStringEncrypter(User _user, String sharedKey, Integer _selectedOrgNodeId) {
		
		if (_user == null) throw new IllegalArgumentException("User object null.");
		if (sharedKey == null || sharedKey.length()==0) throw new IllegalArgumentException("sharedKey null or empty.");
		if (_selectedOrgNodeId == null || _selectedOrgNodeId==0) throw new IllegalArgumentException("_selectedOrgNodeId null or empty.");
		user = _user;
		SECRET_KEY = sharedKey;		
		selectedOrgNodeId = _selectedOrgNodeId;
		queryStringBuilder = new HMACQueryStringBuilder(SECRET_KEY, SIGNATURE_VALIDITY_SECONDS, ENCODING_ALGORITHM);
		//timeZone = java.util.TimeZone.getTimeZone(user.getTimeZone()).getDisplayName(false,java.util.TimeZone.SHORT);//"PST";    	
		queryStringBuilder.setTimeZone(timeZone);
		queryStringBuilder.setENCODING_ALGORITHM(ENCODING_ALGORITHM);
		queryStringBuilder.setURL_ENCODING(URL_ENCODING);
	}
	
	/**
	 * @param sharedKey	encryption key supplied by PRISM
	 * @param _user	User object for the currently logged in user
	 */
	public HMACQueryStringEncrypter(String sharedKey, int _customerId, String _orgNodeCode, int _heirarchyLevel) {
		
		if (sharedKey == null || sharedKey.length()==0) throw new IllegalArgumentException("sharedKey null or empty.");
		//if (_selectedOrgNodeId == null || _selectedOrgNodeId==0) throw new IllegalArgumentException("_selectedOrgNodeId null or empty.");
		WebServiceSSO = true;
		SECRET_KEY = sharedKey;
		CustomerId = _customerId;
		OrgNodeCode = _orgNodeCode;
		HeirarchyLevel = _heirarchyLevel;
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
			if (!WebServiceSSO)
			{
				String orgNodeCode = "";
				Integer hierarchyLevel = 0;
				Node[] organizationNodes = user.getOrganizationNodes();    		     
		        if (organizationNodes != null) {
		            for (int i=0 ; i<organizationNodes.length ; i++) {
		                Node node = organizationNodes[i];
		                if (selectedOrgNodeId.compareTo(node.getOrgNodeId())==0)
		                {
			                String leafNodePath = node.getLeafNodePath();
			                if (leafNodePath != null && leafNodePath.length()>0)
			                {
			                	hierarchyLevel = leafNodePath.split(",").length;
			                	orgNodeCode = (node.getOrgNodeCode() == null?"":node.getOrgNodeCode());
			                }
		                }
		            }
		        }
				Integer customerId = this.user.getCustomer().getCustomerId();			
				String userRole = (isAdminUser()||isAdminCoordinatorUser())?"Admin":"User";
				String userName = this.user.getUserName();
				String datetimeStamp = "";//in GMT
				
				requestParam = queryStringBuilder.buildAuthenticatedQueryString(customerId, orgNodeCode, hierarchyLevel, appName, SECRET_KEY, userRole, userName, IP, datetimeStamp);
				System.out.println("PRISM Report Link SSO: "+requestParam);
			}
			else
			{
				String userRole = "Admin";
				String userName = "oas_user";
				String datetimeStamp = "";//in GMT
				requestParam = queryStringBuilder.buildAuthenticatedQueryString(CustomerId, OrgNodeCode, HeirarchyLevel, appName, SECRET_KEY, userRole, userName, IP, datetimeStamp);
				System.out.println("Web Service SSO: "+requestParam);
			}			
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
