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
     
    private Integer rosterId = null;
    private Integer studentId = null;
    private String loginName = null;
    private String firstName = null;
    private String lastName = null;
    private String studentKey = null;
    private ContentArea[] contentArea = null; 
     
    public Roster() {
    	this.rosterId = null;
    	this.studentId = null;
    	this.loginName = null;
    	this.firstName = null;
    	this.lastName = null;
    	this.studentKey = null;
    	this.contentArea = null;    	
    }

    public Roster(Integer rosterId, Integer studentId, String loginName, 
    					String firstName, String lastName, String studentKey, ContentArea[] contentArea) {
    	this.rosterId = rosterId;
    	this.studentId = studentId;
    	this.loginName = loginName;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.studentKey = studentKey;
    	this.contentArea = contentArea;
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

	public ContentArea[] getContentArea() {
		return contentArea;
	}

	public void setContentArea(ContentArea[] contentArea) {
		this.contentArea = contentArea;
	}



    
} 
