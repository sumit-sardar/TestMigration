package dto; 

/**
* All rosters associated under this assignment 
* status stores error message otherwise set to 'OK'
* 
* @author Tai_Truong
*/
public class RosterList implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;

    private Integer sessionId = null;
    Roster[] rosters = null;
    private String status = null;		

    public RosterList(Integer sessionId, Roster[] rosters) {
    	this.sessionId = sessionId;
    	this.rosters = rosters;
		this.status = "OK";
    }
    public RosterList(String error) {
		this.status = error;
    }
    
	public Integer getSessionId() {
		return sessionId;
	}
	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}
	public Roster[] getRosters() {
		return rosters;
	}
	public void setRosters(Roster[] rosters) {
		this.rosters = rosters;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
       
} 
