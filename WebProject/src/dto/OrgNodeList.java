package dto; 

/**
* Children nodes of an organization 
* status stores error message otherwise set to 'OK'
* 
* @author Tai_Truong
*/
public class OrgNodeList implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;

    private Integer parentOrgNodeId = null;
    OrgNode[] orgNodes = null;
    private String status = null;		

    public OrgNodeList() {
    	this.parentOrgNodeId = null;
    	this.orgNodes = null;
		this.status = null;
    }
    
    public OrgNodeList(Integer parentOrgNodeId, OrgNode[] orgNodeChildren) {
    	this.parentOrgNodeId = parentOrgNodeId;
    	this.orgNodes = orgNodeChildren;
		this.status = "OK";
    }
    public OrgNodeList(String error) {
		this.status = error;
    }
       
	public Integer getParentOrgNodeId() {
		return parentOrgNodeId;
	}
	public void setParentOrgNodeId(Integer parentOrgNodeId) {
		this.parentOrgNodeId = parentOrgNodeId;
	}	
	public OrgNode[] getOrgNodes() {
		return orgNodes;
	}
	public void setOrgNodes(OrgNode[] orgNodes) {
		this.orgNodes = orgNodes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
} 
