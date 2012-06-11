package dto;

/**
 * This is the first object which passed into scheduling web service to perform authentication for security purpose 
 * This object contains information for user
 * The values which required from ACUITY through input are: 
 * 				name, password, userType
 * 
 * @author Tai_Truong
 */
public class SecureUser implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    private Integer id = null;
    private String name = null;			// 32 chars
    private String password = null;		// 32 chars
    private String userType = null;		// 32 chars
	
	public SecureUser() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}
