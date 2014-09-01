package com.ctb.bean; 

/**
 * Data bean representing the format of Uploaded User File
 * @author TCS
 *
 */

public class UserFileRow extends CTBBean
{ 
        static final long serialVersionUID = 1L;
        private Integer userId;
	    private String email;
	    private String firstName;
	    private String middleName;
	    private String lastName;
	    private String userName;
	    private Integer roleId;
	    private String password;
	    private String timeZone;
	    private String extSchoolId;
	    private Integer addressId;
	    private Integer orgNodeId;
	    
        private String address1;
        private String address2;
        private String city;
        private String state;
        private String zip;
        private String zipCodeExt;
        private String primaryPhone;
        private String primaryPhoneExt;
        private String secondaryPhone;
        private String faxNumber;
        
        private String roleName;
	    private Node[] organizationNodes;
	    private String key;
	    private String basicUserName;	    
	    private boolean isAddressPresent = false;
	    
		public Integer getUserId() {
			return userId;
		}
		public void setUserId(Integer userId) {
			this.userId = userId;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getMiddleName() {
			return middleName;
		}
		public void setMiddleName(String middleName) {
			this.middleName = middleName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public Integer getRoleId() {
			return roleId;
		}
		public void setRoleId(Integer roleId) {
			this.roleId = roleId;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getTimeZone() {
			return timeZone;
		}
		public void setTimeZone(String timeZone) {
			this.timeZone = timeZone;
		}
		public String getExtSchoolId() {
			return extSchoolId;
		}
		public void setExtSchoolId(String extSchoolId) {
			this.extSchoolId = extSchoolId;
		}
		public Integer getAddressId() {
			return addressId;
		}
		public void setAddressId(Integer addressId) {
			this.addressId = addressId;
		}
		public Integer getOrgNodeId() {
			return orgNodeId;
		}
		public void setOrgNodeId(Integer orgNodeId) {
			this.orgNodeId = orgNodeId;
		}
		public String getAddress1() {
			return address1;
		}
		public void setAddress1(String address1) {
			this.address1 = address1;
		}
		public String getAddress2() {
			return address2;
		}
		public void setAddress2(String address2) {
			this.address2 = address2;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getZip() {
			return zip;
		}
		public void setZip(String zip) {
			this.zip = zip;
		}
		public String getZipCodeExt() {
			return zipCodeExt;
		}
		public void setZipCodeExt(String zipCodeExt) {
			this.zipCodeExt = zipCodeExt;
		}
		public String getPrimaryPhone() {
			return primaryPhone;
		}
		public void setPrimaryPhone(String primaryPhone) {
			this.primaryPhone = primaryPhone;
		}
		public String getPrimaryPhoneExt() {
			return primaryPhoneExt;
		}
		public void setPrimaryPhoneExt(String primaryPhoneExt) {
			this.primaryPhoneExt = primaryPhoneExt;
		}
		public String getSecondaryPhone() {
			return secondaryPhone;
		}
		public void setSecondaryPhone(String secondaryPhone) {
			this.secondaryPhone = secondaryPhone;
		}
		public String getFaxNumber() {
			return faxNumber;
		}
		public void setFaxNumber(String faxNumber) {
			this.faxNumber = faxNumber;
		}
		public String getRoleName() {
			return roleName;
		}
		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}
		public Node[] getOrganizationNodes() {
			return organizationNodes;
		}
		public void setOrganizationNodes(Node[] organizationNodes) {
			this.organizationNodes = organizationNodes;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getBasicUserName() {
			return basicUserName;
		}
		public void setBasicUserName(String basicUserName) {
			this.basicUserName = basicUserName;
		}
		public boolean isAddressPresent() {
			return isAddressPresent;
		}
		public void setAddressPresent(boolean isAddressPresent) {
			this.isAddressPresent = isAddressPresent;
		}
	    
        
} 
