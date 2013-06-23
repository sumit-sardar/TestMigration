package dto; 

/**
* Session information
* Rosters is a list of students in this session
*
* @author Tai_Truong
*/
public class Assignment implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Integer sessionId = null;
    private String sessionName = null;
    private String accessCode = null;
    private String sessionNumber = null;
    private String startDate = null;
    private String endDate = null;
    private String enforceBreak = null;
    private String enforceTimeLimit = null;
    private Roster[] rosters = null;
    
    public Assignment() {
    	this.sessionId = null;
    	this.sessionName = null;
    	this.accessCode = null;
    	this.sessionNumber = null;
    	this.startDate = null;
    	this.endDate = null;
    	this.enforceBreak = null;
    	this.enforceTimeLimit = null;
    	this.rosters = null;
    }

    public Assignment(Integer sessionId, String sessionName, String accessCode, String sessionNumber, 
    		String startDate, String endDate, String enforceBreak, String enforceTimeLimit, Roster[] rosters) {
    	this.sessionId = sessionId;
    	this.sessionName = sessionName;
    	this.accessCode = accessCode;
    	this.sessionNumber = sessionNumber;
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.enforceBreak = enforceBreak;
    	this.enforceTimeLimit = enforceTimeLimit;
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

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getSessionNumber() {
		return sessionNumber;
	}

	public void setSessionNumber(String sessionNumber) {
		this.sessionNumber = sessionNumber;
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

	public String getEnforceBreak() {
		return enforceBreak;
	}

	public void setEnforceBreak(String enforceBreak) {
		this.enforceBreak = enforceBreak;
	}

	public String getEnforceTimeLimit() {
		return enforceTimeLimit;
	}

	public void setEnforceTimeLimit(String enforceTimeLimit) {
		this.enforceTimeLimit = enforceTimeLimit;
	}

	public Roster[] getRosters() {
		return rosters;
	}

	public void setRosters(Roster[] rosters) {
		this.rosters = rosters;
	}
    
    
} 
