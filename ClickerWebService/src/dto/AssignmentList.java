package dto; 

/**
* All assignments associated with this organization 
* status stores error message otherwise set to 'OK'
* 
* @author Tai_Truong
*/
public class AssignmentList implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;

    private Integer orgNodeId = null;
    Assignment[] assignments = null;
    private String status = null;		

    public AssignmentList(Integer orgNodeId, Assignment[] assignments) {
    	this.orgNodeId = orgNodeId;
    	this.assignments = assignments;
		this.status = "OK";
    }
    public AssignmentList(String error) {
		this.status = error;
    }
       
	public Integer getOrgNodeId() {
		return orgNodeId;
	}
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	public Assignment[] getAssignments() {
		return assignments;
	}
	public void setAssignments(Assignment[] assignments) {
		this.assignments = assignments;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
} 
