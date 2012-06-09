package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * Data bean representing the contents of the OAS.TEST_ADMIN_USER_ROLE table
 * 
 * @author Nate_Cohen
 */
public class ProctorAssignment extends CTBBean
{
    static final long serialVersionUID = 1L;
    private Integer testAdminId;
    private Integer userId;
    private Integer roleId;
    private Integer createdBy;
    private Date createdDateTime;
    
    
	/**
	 * @return Returns the createdBy.
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return Returns the createdDateTime.
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime The createdDateTime to set.
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	/**
	 * @return Returns the roleId.
	 */
	public Integer getRoleId() {
		return roleId;
	}
	/**
	 * @param roleId The roleId to set.
	 */
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	/**
	 * @return Returns the testAdminId.
	 */
	public Integer getTestAdminId() {
		return testAdminId;
	}
	/**
	 * @param testAdminId The testAdminId to set.
	 */
	public void setTestAdminId(Integer testAdminId) {
		this.testAdminId = testAdminId;
	}
	/**
	 * @return Returns the userId.
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
} 
