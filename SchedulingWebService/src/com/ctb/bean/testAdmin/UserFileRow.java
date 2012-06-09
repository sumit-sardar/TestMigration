package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

public class UserFileRow extends CTBBean
{ 
        static final long serialVersionUID = 1L;
        private Integer userId;
	    private String email;
	    private String firstName;
	    private String middleName;
	    private String lastName;
	    private String prefix;
	    private String suffix;
	    private String userName;
	    private String displayUserName;
	    private Integer roleId;
	    private String roleName;
	    private String password;
	    private String preferredName;
	    private String timeZone;
	    private String timeZoneDese;
	    private Integer passwordHintQuestionId;
	    private Date passwordExpirationDate;
	    private String passwordHintAnswer;
	    private String activeSession;
	    private String resetPassword;
	    private Date lastLoginDateTime;
	    private String extPin1;
	    private String extPin2;
	    private String extPin3;
	    private String extSchoolId;
	    private Integer createdBy;
	    private Date createdDateTime;
	    private Integer updatedBy;
	    private Date updatedDateTime;
	    private String activationStatus;
	    private Integer dataImportHistoryId;
	    private String displayNewMessage;
	    private String editable;
	    private String copyable;
	    private Integer addressId;
	    private Integer orgNodeId;
        private String address1;
        private String address2;
        private String city;
        private String state;
        private String zip;
        private String primaryPhone;
        private String secondaryPhone;
        private String faxNumber;

	    private Node[] organizationNodes;
        private DataFileRowError[] dataFilerowError;
        
        private String primarySort;
        private String secondarySort;
        
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
        
        private String role;
        /**
         * @return Returns the role.
         */
        public String getRole() {
            return role;
        }
        /**
         * @param role The role to set.
         */
        public void setRole(String role) {
            this.role = role;
        }
        /**
         * @return Returns the address1.
         */
        public String getAddress1() {
            return address1;
        }
        /**
         * @param address1 The address1 to set.
         */
        public void setAddress1(String address1) {
            this.address1 = address1;
        }
        /**
         * @return Returns the address2.
         */
        public String getAddress2() {
            return address2;
        }
        /**
         * @param address2 The address2 to set.
         */
        public void setAddress2(String address2) {
            this.address2 = address2;
        }
        /**
         * @return Returns the city.
         */
        public String getCity() {
            return city;
        }
        /**
         * @param city The city to set.
         */
        public void setCity(String city) {
            this.city = city;
        }
        /**
         * @return Returns the faxNumber.
         */
        public String getFaxNumber() {
            return faxNumber;
        }
        /**
         * @param faxNumber The faxNumber to set.
         */
        public void setFaxNumber(String faxNumber) {
            this.faxNumber = faxNumber;
        }
        /**
         * @return Returns the primaryPhone.
         */
        public String getPrimaryPhone() {
            return primaryPhone;
        }
        /**
         * @param primaryPhone The primaryPhone to set.
         */
        public void setPrimaryPhone(String primaryPhone) {
            this.primaryPhone = primaryPhone;
        }
        /**
         * @return Returns the secondaryPhone.
         */
        public String getSecondaryPhone() {
            return secondaryPhone;
        }
        /**
         * @param secondaryPhone The secondaryPhone to set.
         */
        public void setSecondaryPhone(String secondaryPhone) {
            this.secondaryPhone = secondaryPhone;
        }
        /**
         * @return Returns the state.
         */
        public String getState() {
            return state;
        }
        /**
         * @param state The state to set.
         */
        public void setState(String state) {
            this.state = state;
        }
        /**
         * @return Returns the zip.
         */
        public String getZip() {
            return zip;
        }
        /**
         * @param zip The zip to set.
         */
        public void setZip(String zip) {
            this.zip = zip;
        }        
		/**
		 * @return Returns the organizationNodes.
		 */
		public Node[] getOrganizationNodes() {
			return organizationNodes;
		}
		/**
		 * @param organizationNodes The organizationNodes to set.
		 */
		public void setOrganizationNodes(Node[] organizationNodes) {
			this.organizationNodes = organizationNodes;
		}
		/**
		 * @return Returns the orgNodeId.
		 */
		public Integer getOrgNodeId() {
			return orgNodeId;
		}
		/**
		 * @param orgNodeId The orgNodeId to set.
		 */
		public void setOrgNodeId(Integer orgNodeId) {
			this.orgNodeId = orgNodeId;
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
		 * @return Returns the roleName.
		 */
		public String getRoleName() {
			return roleName;
		}
		/**
		 * @param roleName The roleName to set.
		 */
		public void setRoleName(String roleName) {
			this.roleName = roleName;
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
		 * @return Returns the timeZoneDese.
		 */
		public String getTimeZoneDese() {
			return timeZoneDese;
		}
		/**
		 * @param timeZoneDese The timeZoneDese to set.
		 */
		public void setTimeZoneDese(String timeZoneDese) {
			this.timeZoneDese = timeZoneDese;
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
		 * @return Returns the dataFilerowError.
		 */
        public DataFileRowError[] getDataFilerowError() {
			return dataFilerowError;
		}
        /**
		 * @param dataFilerowError The dataFilerowError to set.
		 */
		public void setDataFilerowError(DataFileRowError[] dataFilerowError) {
			this.dataFilerowError = dataFilerowError;
		}
        /* @return Returns the primarySort.
		 */
		public String getPrimarySort() {
			return primarySort;
		}
		/**
		 * @param primarySort The primarySort to set.
		 */
		public void setPrimarySort(String primarySort) {
			this.primarySort = primarySort;
		}
        /* @return Returns the secondarySort.
		 */
		public String getSecondarySort() {
			return secondarySort;
		}
		/**
		 * @param secondarySort The secondarySort to set.
		 */
		public void setSecondarySort(String secondarySort) {
			this.secondarySort = secondarySort;
		}
        
} 
