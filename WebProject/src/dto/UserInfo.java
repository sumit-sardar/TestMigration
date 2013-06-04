package dto;

/**
 * Populates userId, userName if authenticating successfully otherwise set to null
 * status stores error message otherwise set to 'OK'
 *  
 * @author Tai_Truong
 */
public class UserInfo implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    private Integer userId = null;
    private String userName = null;		
    private String status = null;		
	
	public UserInfo() {
		this.userId = null;
		this.userName = null;
		this.status = null;
	}
	public UserInfo(Integer userId, String userName) {
		this.userId = userId;
		this.userName = userName;
		this.status = "OK";
	}

	public UserInfo(String error) {
		this.status = error;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
