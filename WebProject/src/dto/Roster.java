package dto; 

/**
* Roster information
* Subtests is the list of subtests in student manifest
* OAS populates the fields
*
* @author Tai_Truong
*/
public class Roster implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Integer rosterId;
    private Integer studentId;
    private String loginName;
    private String firstName;
    private String lastName;
    private String studentKey;
    private Subject[] subjects; 
    
    public Roster() {}

    public Roster(Integer rosterId, Integer studentId, String loginName, 
    			String firstName, String lastName, String studentKey, Subject[] subjects) {
    	this.rosterId = rosterId;
    	this.studentId = studentId;
    	this.loginName = loginName;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.studentKey = studentKey;
    	this.subjects = subjects;
    }

	public Integer getRosterId() {
		return rosterId;
	}

	public void setRosterId(Integer rosterId) {
		this.rosterId = rosterId;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStudentKey() {
		return studentKey;
	}

	public void setStudentKey(String studentKey) {
		this.studentKey = studentKey;
	}

	public Subject[] getSubjects() {
		return subjects;
	}

	public void setSubjects(Subject[] subjects) {
		this.subjects = subjects;
	}


    
} 
