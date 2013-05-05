package dto; 

/**
* Session information
* Rosters is all students in this session
*
* @author Tai_Truong
*/
public class Assignment implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Integer sessionId;
    private String sessionName;
    private String startDate;
    private String endDate;
    private Roster[] rosters;
    
    public Assignment() {}

    public Assignment(Integer sessionId, String sessionName, String startDate, String endDate, Roster[] rosters) {
    	this.sessionId = sessionId;
    	this.sessionName = sessionName;
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.rosters = rosters;
    }
    
	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Roster[] getRosters() {
		return rosters;
	}

	public void setRosters(Roster[] rosters) {
		this.rosters = rosters;
	}
    
} 
