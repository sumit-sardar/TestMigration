package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;
import com.ctb.bean.testAdmin.Customer;

/**
 * Data bean representing the contents of the OAS.USERS table
 * 
 * @author Nate_Cohen, John_Wang
 */
public class User extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer userId;
    private String userName;
    private String displayUserName;
    private String password;
    private String newPassword;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredName;
    private String prefix;
    private String suffix;
    private String timeZone;
    private String email;
    private Integer passwordHintQuestionId;
    private String passwordHintQuestion;
    private Date passwordExpirationDate;
    private String passwordHintAnswer;
    private Integer addressId;
    private String activeSession;
    private String resetPassword;
    private Date lastLoginDateTime;
    private String extPin1;
    private String extPin2;
    private String extPin3;
    private String extSchoolId;
    private Integer createdBy;
    private Date CreatedDateTime;
    private Integer updatedBy;
    private Date updatedDateTime;
    private String activationStatus;
    private Integer dataImportHistoryId;
    private String displayNewMessage;
    private String editable;
    private String copyable;
    private Customer customer;
    private Role role;
    private Address address;
    private Node[] organizationNodes;
    
    public User() {
    }
    
    public User(FindUser findUser) {
        
        if (findUser == null) return;
        
        this.setUserId(findUser.getUserId());
        this.setUserName(findUser.getUserName());
        this.setDisplayUserName(findUser.getDisplayUserName());
        this.setPassword(findUser.getPassword());
        this.setFirstName(findUser.getFirstName());
        this.setMiddleName(findUser.getMiddleName());
        this.setLastName(findUser.getLastName());
        this.setPreferredName(findUser.getPreferredName());
        this.setPrefix(findUser.getPrefix());
        this.setSuffix(findUser.getSuffix());
        this.setTimeZone(findUser.getTimeZone());
        this.setEmail(findUser.getEmail());
        this.setPasswordHintQuestionId(findUser.getPasswordHintQuestionId());
        this.setPasswordExpirationDate(findUser.getPasswordExpirationDate());
        this.setPasswordHintAnswer(findUser.getPasswordHintAnswer());
        this.setAddressId(findUser.getAddressId());
        this.setActiveSession(findUser.getActiveSession());
        this.setResetPassword(findUser.getResetPassword());
        this.setLastLoginDateTime(findUser.getLastLoginDateTime());
        this.setExtPin1(findUser.getExtPin1());
        this.setExtPin2(findUser.getExtPin2());
        this.setExtPin3(findUser.getExtPin3());
        this.setExtSchoolId(findUser.getExtSchoolId());
        this.setCreatedBy(findUser.getCreatedBy());
        this.setCreatedDateTime(findUser.getCreatedDateTime());
        this.setUpdatedBy(findUser.getUpdatedBy());
        this.setUpdatedDateTime(findUser.getUpdatedDateTime());
        this.setActivationStatus(findUser.getActivationStatus());
        this.setDataImportHistoryId(findUser.getDataImportHistoryId());
        this.setDisplayNewMessage(findUser.getDisplayNewMessage());
        this.setEditable(findUser.getEditable());
        this.setCopyable(findUser.getCopyable());
        this.setRole(new Role());
        this.getRole().setRoleId(findUser.getRoleId());
        this.getRole().setRoleName(findUser.getRoleName());
    }
    
    /**
	 * @return Returns the customer.
	 */
	public Customer getCustomer() {
		return customer;
	}
	/**
	 * @param customer The customer to set.
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
    /**
	 * @return Returns the copyable.
	 */
	public String getCopyable() {
		return copyable;
	}
	/**
	 * @param copyable The copyable to set.
	 */
	public void setCopyable(String copyable) {
		this.copyable = copyable;
	}
    /**
	 * @return Returns the editable.
	 */
	public String getEditable() {
		return editable;
	}
	/**
	 * @param editable The editable to set.
	 */
	public void setEditable(String editable) {
		this.editable = editable;
	}
	/**
	 * @return Returns the activationStatus.
	 */
	public String getActivationStatus() {
		return activationStatus;
	}
	/**
	 * @param activationStatus The activationStatus to set.
	 */
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	/**
	 * @return Returns the activeSession.
	 */
	public String getActiveSession() {
		return activeSession;
	}
	/**
	 * @param activeSession The activeSession to set.
	 */
	public void setActiveSession(String activeSession) {
		this.activeSession = activeSession;
	}
	/**
	 * @return Returns the addressId.
	 */
	public Integer getAddressId() {
		return addressId;
	}
	/**
	 * @param addressId The addressId to set.
	 */
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
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
		return CreatedDateTime;
	}
	/**
	 * @param createdDateTime The createdDateTime to set.
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		CreatedDateTime = createdDateTime;
	}
	/**
	 * @return Returns the dataImportHistoryId.
	 */
	public Integer getDataImportHistoryId() {
		return dataImportHistoryId;
	}
	/**
	 * @param dataImportHistoryId The dataImportHistoryId to set.
	 */
	public void setDataImportHistoryId(Integer dataImportHistoryId) {
		this.dataImportHistoryId = dataImportHistoryId;
	}
	/**
	 * @return Returns the displayNewMessage.
	 */
	public String getDisplayNewMessage() {
		return displayNewMessage;
	}
	/**
	 * @param displayNewMessage The displayNewMessage to set.
	 */
	public void setDisplayNewMessage(String displayNewMessage) {
		this.displayNewMessage = displayNewMessage;
	}
	/**
	 * @return Returns the displayUserName.
	 */
	public String getDisplayUserName() {
		return displayUserName;
	}
	/**
	 * @param displayUserName The displayUserName to set.
	 */
	public void setDisplayUserName(String displayUserName) {
		this.displayUserName = displayUserName;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the extPin1.
	 */
	public String getExtPin1() {
		return extPin1;
	}
	/**
	 * @param extPin1 The extPin1 to set.
	 */
	public void setExtPin1(String extPin1) {
		this.extPin1 = extPin1;
	}
	/**
	 * @return Returns the extPin2.
	 */
	public String getExtPin2() {
		return extPin2;
	}
	/**
	 * @param extPin2 The extPin2 to set.
	 */
	public void setExtPin2(String extPin2) {
		this.extPin2 = extPin2;
	}
	/**
	 * @return Returns the extPin3.
	 */
	public String getExtPin3() {
		return extPin3;
	}
	/**
	 * @param extPin3 The extPin3 to set.
	 */
	public void setExtPin3(String extPin3) {
		this.extPin3 = extPin3;
	}
	/**
	 * @return Returns the extSchoolId.
	 */
	public String getExtSchoolId() {
		return extSchoolId;
	}
	/**
	 * @param extSchoolId The extSchoolId to set.
	 */
	public void setExtSchoolId(String extSchoolId) {
		this.extSchoolId = extSchoolId;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return Returns the lastLoginDateTime.
	 */
	public Date getLastLoginDateTime() {
		return lastLoginDateTime;
	}
	/**
	 * @param lastLoginDateTime The lastLoginDateTime to set.
	 */
	public void setLastLoginDateTime(Date lastLoginDateTime) {
		this.lastLoginDateTime = lastLoginDateTime;
	}
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return Returns the middleName.
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName The middleName to set.
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
    
    /**
	 * @return Returns the newpassword.
	 */
	public String getNewPassword() {
		return newPassword;
	}
	/**
	 * @param password The password to set.
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	/**
	 * @return Returns the passwordExpirationDate.
	 */
	public Date getPasswordExpirationDate() {
		return passwordExpirationDate;
	}
	/**
	 * @param passwordExpirationDate The passwordExpirationDate to set.
	 */
	public void setPasswordExpirationDate(Date passwordExpirationDate) {
		this.passwordExpirationDate = passwordExpirationDate;
	}
	/**
	 * @return Returns the passwordHintAnswer.
	 */
	public String getPasswordHintAnswer() {
		return passwordHintAnswer;
	}
	/**
	 * @param passwordHintAnswer The passwordHintAnswer to set.
	 */
	public void setPasswordHintAnswer(String passwordHintAnswer) {
		this.passwordHintAnswer = passwordHintAnswer;
	}
	/**
	 * @return Returns the passwordHintQuestionId.
	 */
	public Integer getPasswordHintQuestionId() {
		return passwordHintQuestionId;
	}
	/**
	 * @param passwordHintQuestionId The passwordHintQuestionId to set.
	 */
	public void setPasswordHintQuestionId(Integer passwordHintQuestionId) {
		this.passwordHintQuestionId = passwordHintQuestionId;
	}
    /**
	 * @return Returns the passwordHintQuestion.
	 */
	public String getPasswordHintQuestion() {
		return passwordHintQuestion;
	}
	/**
	 * @param passwordHintQuestion The passwordHintQuestion to set.
	 */
	public void setPasswordHintQuestion(String passwordHintQuestion) {
		this.passwordHintQuestion = passwordHintQuestion;
    }
	/**
	 * @return Returns the preferredName.
	 */
	public String getPreferredName() {
		return preferredName;
	}
	/**
	 * @param preferredName The preferredName to set.
	 */
	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}
	/**
	 * @return Returns the prefix.
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * @param prefix The prefix to set.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	/**
	 * @return Returns the resetPassword.
	 */
	public String getResetPassword() {
		return resetPassword;
	}
	/**
	 * @param resetPassword The resetPassword to set.
	 */
	public void setResetPassword(String resetPassword) {
		this.resetPassword = resetPassword;
	}
	/**
	 * @return Returns the suffix.
	 */
	public String getSuffix() {
		return suffix;
	}
	/**
	 * @param suffix The suffix to set.
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	/**
	 * @return Returns the timeZone.
	 */
	public String getTimeZone() {
		return timeZone;
	}
	/**
	 * @param timeZone The timeZone to set.
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	/**
	 * @return Returns the updatedBy.
	 */
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy The updatedBy to set.
	 */
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @return Returns the updatedDateTime.
	 */
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	/**
	 * @param updatedDateTime The updatedDateTime to set.
	 */
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
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
	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    public Node[] getOrganizationNodes() {
        return organizationNodes;
    }    
    
    public void setOrganizationNodes(Node[] organizationNodes) {
        this.organizationNodes = organizationNodes;
    }    

} 
