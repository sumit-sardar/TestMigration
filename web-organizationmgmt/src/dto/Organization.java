package dto; 

/**
 *@author Tata Consultancy Services 
 * Organization deals with organization Deals
 */

import com.ctb.bean.testAdmin.Node;
import manageOrganization.ManageOrganizationController.ManageOrganizationForm;
import utils.PermissionsUtils;


public class Organization implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;

    private Integer orgId = new Integer(0);
    private String orgName = "";
    private String orgCode = "";
    private String orgType = "";
    private String orgTypeId = "";
    private String orgParent = "";
    private Integer orgParentId = new Integer (0);
    private String actionPermission = PermissionsUtils.VIEW_ADD_PERMISSION_TOKEN;
    
    public Organization() {
    }

    public Organization(Node node) {
        this.orgId = node.getOrgNodeId();
        this.orgName = node.getOrgNodeName();
        this.orgCode = node.getOrgNodeCode();
        this.orgType = node.getOrgNodeCategoryName();  
        this.orgTypeId = node.getOrgNodeCategoryId().toString();
        this.orgParent = node.getParentOrgNodeName(); 
        this.orgParentId = node.getParentOrgNodeId(); 
        this.actionPermission = node.getEditable();    
    }
    
    /**
     * createClone
     */
    
    public Organization createClone(Node node) {
        Organization copied = new Organization();
        
        copied.setOrgId(this.orgId);
        copied.setOrgName(this.orgName);
        copied.setOrgCode(this.orgCode);
        copied.setOrgType(this.orgType);  
        copied.setOrgTypeId(this.orgTypeId);
        copied.setOrgParent(this.orgParent); 
        copied.setOrgParentId(this.orgParentId);  
        
        return copied;   
    }
     
     /**
     * makeCopy From the Form
     */
    public static Node makeCopy(ManageOrganizationForm form) {
        Node copied = new Node();
               
        //User Information
        copied.setOrgNodeId(form.getSelectedOrgChildNodeId());
        copied.setOrgNodeName(form.getSelectedOrgName().trim());
        copied.setOrgNodeCode(form.getSelectedOrgNodeCode().trim());
        copied.setOrgNodeCategoryId(Integer.valueOf(form.getSelectedOrgNodeTypeId()));
        copied.setParentOrgNodeId(form.getSelectedOrgNodeId());
      
        return copied;       
    }
     /**
	 * @return Returns the orgId.
	 */
	public Integer getOrgId() {
		return this.orgId != null ? this.orgId : new Integer(0);
	}
    /**
	 * @param orgId 
	 */
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
     /**
	 * @return Returns the orgName.
	 */
	public String getOrgName() {
		return this.orgName != null ? this.orgName : "";
	}
    /**
	 * @param orgName 
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
     /**
	 * @return Returns the orgCode.
	 */
	public String getOrgCode() {
		return this.orgCode != null ? this.orgCode : "";
	}
    /**
	 * @param orgCode 
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
     /**
	 * @return Returns the orgType.
	 */
	public String getOrgType() {
		return this.orgType != null ? this.orgType : "";
	}
    /**
	 * @param orgType 
	 */
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
    /**
	 * @return Returns the orgParent.
	 */
	public String getOrgParent() {
		return this.orgParent != null ? this.orgParent : "";
	}
    /**
	 * @param orgParent 
	 */
	public void setOrgParent(String orgParent) {
		this.orgParent = orgParent;
	}
     /**
	 * @return Returns the orgTypeId.
	 */
	public String getOrgTypeId() {
		return this.orgTypeId != null ? this.orgTypeId : "";
	}
	/**
	 * @param orgTypeId 
	 */
	public void setOrgTypeId(String orgTypeId) {
		this.orgTypeId = orgTypeId;
	}
    
    /**
	 * @return Returns the orgParentId.
	 */
	public Integer getOrgParentId() {
		return this.orgParentId != null ? this.orgParentId : new Integer(0);
	}
	/**
	 * @param orgParentId 
	 */
	public void setOrgParentId(Integer orgParentId) {
		this.orgParentId = orgParentId;
	}
    
    /**
	 * @return Returns the actionPermission.
	 */
	public String getActionPermission() {
		return this.actionPermission != null ? 
                    this.actionPermission : PermissionsUtils.VIEW_ADD_PERMISSION_TOKEN;
	}
	/**
	 * @param actionPermission 
	 */
	public void setActionPermission(String actionPermission) {
		this.actionPermission = actionPermission;
	}
    
} 