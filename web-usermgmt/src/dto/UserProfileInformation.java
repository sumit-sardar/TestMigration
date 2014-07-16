package dto; 

import java.util.List;
import java.util.StringTokenizer;

import utils.PermissionsUtils;
import utils.StringUtils;

import com.ctb.bean.testAdmin.Address;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.Role;
import com.ctb.bean.testAdmin.User;
import com.ctb.util.web.sanitizer.SanitizedFormField;

/**
 * UserProfileInformation
 */
public class UserProfileInformation extends SanitizedFormField
{ 
    static final long serialVersionUID = 1L;

    private Integer userId = new Integer(0);
    private String userName = "";
    private String loginId = "";
    private String firstName = "";
    private String middleName = "";
    private String lastName = "";
    private String displayName = "";    
    private String role = "";
    private String roleId = "";
    private String email = "";
    private String timeZone = "";
    private String timeZoneDesc = "";
    private Node[] organizationNodes = null;
    private UserContactInformation userContact = new UserContactInformation();
    private PasswordInformation userPassword = new PasswordInformation();
    private Integer addressId = new Integer(0);
    private String actionPermission = PermissionsUtils.VIEW_ONLY_PERMISSION_TOKEN;
    /**
     * ext_pin1 is added for DEX CR
     */
    private String extPin1 = "";
    private String extSchoolId = "";
    
    /**
     * default constructor
     */
    public UserProfileInformation() {
    }


    /**
     * constructor
     */
    public UserProfileInformation(User user) {
        this.userName = user.getUserName();
        this.userId = user.getUserId();
        this.loginId = user.getUserName();
        this.firstName = user.getFirstName();
        this.middleName = user.getMiddleName();
        this.lastName = user.getLastName();
        this.displayName = user.getDisplayUserName();
        this.role = user.getRole().getRoleName();
        this.roleId = user.getRole().getRoleId().toString();
        this.extPin1 = user.getExtPin1(); //ext_pin1 is added for DEX CR
        this.email = user.getEmail();
        this.timeZone = user.getTimeZone();   
        this.timeZoneDesc = timeZone;
        this.organizationNodes = user.getOrganizationNodes();
        this.addressId = user.getAddressId();
        this.actionPermission = user.getEditable();
        this.userContact = new UserContactInformation(user.getAddressId(), user.getAddress());
        this.userPassword = new PasswordInformation();
        this.extSchoolId = user.getExtSchoolId(); // Ext_School_Id is added for LAUSD Customer
        if (user.getPasswordHintQuestionId() != null) {
            this.userPassword.setHintQuestionId(
                    user.getPasswordHintQuestionId().toString());
            this.userPassword.setHintAnswer(user.getPasswordHintAnswer());
        }
    }


    /**
     * createClone
     */
    public UserProfileInformation createClone() {
        UserProfileInformation copied = new UserProfileInformation();

        copied.setUserId(this.userId);
        copied.setLoginId(this.loginId);
        copied.setFirstName(this.firstName);
        copied.setMiddleName(this.middleName);
        copied.setLastName(this.lastName);
        copied.setDisplayName(this.displayName);
        copied.setRole(this.role);
        copied.setRoleId(this.roleId);
        copied.setEmail(this.email);
        copied.setExtPin1(this.extPin1); //ext_pin1 is added for DEX CR
        copied.setTimeZone(this.timeZone);
        copied.setTimeZoneDesc(this.timeZoneDesc);        
        copied.setOrganizationNodes(this.organizationNodes);
        copied.setUserContact(this.userContact);
        copied.setActionPermission(this.actionPermission);
        
        return copied;       
    }

    /**
     * makeCopy
     */
   public User makeCopy(String userName, List selectedOrgNodes) 
   {
        User copied = new User();
        Role role = new Role();        
        
        //User Information
        copied.setUserId(this.userId);
        copied.setUserName(userName);
        copied.setFirstName( StringUtils.upperCaseFirstLetter(this.firstName) );
        copied.setMiddleName( StringUtils.upperCaseFirstLetter(this.middleName) );
        copied.setLastName( StringUtils.upperCaseFirstLetter(this.lastName) );
        copied.setEmail(this.email);  
        copied.setTimeZone(this.timeZone);        
        role.setRoleName(this.role);
        copied.setRole(role);
        copied.setExtPin1(this.extPin1); //ext_pin1 is added for DEX CR
        copied.setExtSchoolId(this.extSchoolId); // LAUSD Story of External School Id
        if (!"".equals(this.roleId)) {
            role.setRoleId(Integer.valueOf(this.roleId));
            copied.getRole().setRoleId(Integer.valueOf(this.roleId));
        }
        
        if (this.userPassword != null) {
            if (!"".equals(this.userPassword.getHintQuestionId())) {
                copied.setPasswordHintQuestionId(
                        new Integer(this.userPassword.getHintQuestionId()));
            }
            copied.setPasswordHintAnswer(this.userPassword.getHintAnswer());
            copied.setPassword(this.userPassword.getOldPassword());
            copied.setNewPassword(this.userPassword.getNewPassword());
        }
        
        //Contact Information
        this.userContact.setPrimaryPhoneFromOthers();
        this.userContact.setSecondaryPhoneFromOthers();
        this.userContact.setFaxFromOthers();
        
        if ((this.addressId != null && this.addressId.intValue() != 0) 
                    || !userContact.isEmpty()) {
            Address address = new Address();
            copied.setAddressId(this.addressId);   
            address.setAddressId(this.addressId);
            address.setAddressLine1(this.userContact.getAddressLine1());
            address.setAddressLine2(this.userContact.getAddressLine2());
            address.setCity(this.userContact.getCity());
            address.setStatePr(this.userContact.getState());
            address.setZipCode(this.userContact.getZipCode1());
            address.setZipCodeExt(this.userContact.getZipCode2());
            address.setPrimaryPhone(this.userContact.getPrimaryPhone());
            address.setSecondaryPhone(this.userContact.getSecondaryPhone());
            address.setFaxNumber(this.userContact.getFaxNumber());
            copied.setAddress(address);        
        }
        
        
        //User Organization Information
        Node[] orgNodes = new Node[ selectedOrgNodes.size() ];        
        for (int i=0 ; i<selectedOrgNodes.size() ; i++) {
            PathNode node = (PathNode)selectedOrgNodes.get(i);
            Node orgNode = new Node();
            orgNode.setOrgNodeId(node.getId());
            orgNodes[i] = orgNode;
        }
        copied.setOrganizationNodes(orgNodes);    

        return copied;       
    }
    
   	/**
	 * @return Returns the userId.
	 */
	public Integer getUserId() {
		return this.userId != null ? this.userId : new Integer(0);
	}
    
    /**
	 * @param userId The userId to set.
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * @return Returns the displayName.
	 */

	public String getUserName() {
		return this.userName != null ? this.userName : "";
	}
	/**
	 * @param displayName 
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDisplayName() {
		return this.displayName != null ? this.displayName : "";
	}
	/**
	 * @param displayName 
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return this.firstName != null ? this.firstName : "";
	}
	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
        this.firstName = firstName;
	}
	/**
	 * @return Returns the loginId.
	 */
	public String getLoginId() {
        return this.loginId;
	}
	/**
	 * @param gender The gender to set.
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return this.lastName != null ? this.lastName : "";
	}
	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
        this.lastName = lastName;
	}
	/**
	 * @return Returns the middleName.
	 */
	public String getMiddleName() {
		return this.middleName != null ? this.middleName : "";
	}
	/**
	 * @param middleName The middleName to set.
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return Returns the timeZone.
	 */
	public String getTimeZone() {
		return this.timeZone;
	}
	/**
	 * @param timeZone 
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
    /**
	 * @return Returns the timeZoneDesc.
	 */
	public String getTimeZoneDesc() {
		return this.timeZoneDesc;
	}
	/**
	 * @param timeZoneDesc 
	 */
	public void setTimeZoneDesc(String timeZoneDesc) {
		this.timeZoneDesc = timeZoneDesc;
	}     
    /**
	 * @return Returns the viewOrganizationNodes.
	 */
	public Node[] getOrganizationNodes() {
		return this.organizationNodes;
	}
	/**
	 * @param viewOrganizationNodes The organizationNodes to set.
	 */
	public void setOrganizationNodes(Node[] viewOrganizationNodes) {
		this.organizationNodes = viewOrganizationNodes;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return this.email != null ? this.email : "";
	}
	/**
	 * @param email 
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the role.
	 */
	public String getRole() {
		return this.role != null ? this.role : "";
	}
	/**
	 * @param role 
	 */
	public void setRole(String role) {
		this.role = role;
	}
    
    /**
     * returns the role in InitCap
     */
    public String getRoleInitCap() {
        if (role == null || "".equals(role)) return "";
        StringTokenizer st = new StringTokenizer(role, " ");
        String initRole = "";
        while (st.hasMoreTokens()) {
            String word = st.nextToken();
            word = Character.toString((Character.toUpperCase(word.charAt(0)))) 
                        + word.substring(1).toLowerCase();
            initRole += word + " ";
        }
        return initRole;
    }
    
    /**
	 * @return Returns the roleId.
	 */
	public String getRoleId() {
		return this.roleId != null ? this.roleId : "";
	}
	/**
	 * @param roleId 
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
    /**
     * ext_pin1 is added for DEX CR
     * @return Returns the extPin1.
    */
    public String getExtPin1() {
        return extPin1;
    }
    /**
     * ext_pin1 is added for DEX CR
    * @param extPin1 The extPin1 to set.
    */
    public void setExtPin1(String extPin1) {
        this.extPin1 = extPin1;
    }
    
   /**
	* @return Returns the userContact.
	*/  
    public UserContactInformation getUserContact() {
		return this.userContact != null ? this.userContact : new UserContactInformation();
	}
    /**
	 * @param userContact The userContact to set.
	 */
	public void setUserContact(UserContactInformation userContact) {
		this.userContact = userContact;
	}
    
     /**
	 * @return Returns the addressId.
	 */
	public Integer getAddressId() {
		return this.addressId ;
	}
	/**
	 * @param addressId The addressId to set.
	 */
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	/**
	 * @return Returns the actionPermission.
	 */
	public String getActionPermission() {
		return this.actionPermission != null ? this.actionPermission : PermissionsUtils.VIEW_ONLY_PERMISSION_TOKEN;
	}
	/**
	 * @param actionPermission 
	 */
	public void setActionPermission(String actionPermission) {
		this.actionPermission = actionPermission;
	}

    /**
     * getUserPassword
     */
    public PasswordInformation getUserPassword() {
		return this.userPassword != null ? this.userPassword : new PasswordInformation();
	}

    /**
     * setUserPassword
     */
	public void setUserPassword(PasswordInformation userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * @return the extSchoolId
	 */
	public String getExtSchoolId() {
		return extSchoolId;
	}
	
	/**
	 * @param extSchoolId the extSchoolId to set
	 */
	public void setExtSchoolId(String extSchoolId) {
		this.extSchoolId = extSchoolId;
	}
	
	/**
	 * @return Returns the getOrgNodeNamesString.
	 */
	public String getOrgNodeNamesString() {
        String str = "";
        if (this.organizationNodes != null) {
            for (int i=0 ; i<this.organizationNodes.length ; i++) {
                Node node = this.organizationNodes[i];
                str = str + node.getOrgNodeName();
                if (i < (this.organizationNodes.length - 1))
                    str = str + ", " + "<br/>";
            }
        }
        return str;
	}    

    
} 