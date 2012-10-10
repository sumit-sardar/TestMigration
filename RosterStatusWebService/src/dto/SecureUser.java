package dto;

/**
 * This is the first object which passed into web service to perform authentication for security purpose 
 * This object contains information for user
 * The values which required from ACUITY through input are: 
 * 				userName, password, userType
 * 
 * @author Tai_Truong
 */
public class SecureUser implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    private Integer userId = null;
    private String userName = null;		// 32 chars
    private String password = null;		// 32 chars
    private String userType = null;		// 32 chars
	
	public SecureUser() {
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
