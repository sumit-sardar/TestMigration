package dto; 

/**
 *@author Tata Consultancy Services 
 */

import utils.FilterSortPageUtils;

public class TestRosterFilter implements java.io.Serializable  
{ 
    static final long serialVersionUID = 1L;

    private String lastName = null;
    private String lastNameFilterType = null;
    private String firstName = null;
    private String firstNameFilterType = null;
    private String loginName = null;
    private String loginNameFilterType = null;
    private String password = null;
    private String passwordFilterType = null;
    private String validationStatusFilterType = null;
    private String testStatusFilterType = null;

    public TestRosterFilter() {
        this.lastName = "";
        this.lastNameFilterType = FilterSortPageUtils.FILTERTYPE_CONTAINS;
        this.firstName = "";
        this.firstNameFilterType = FilterSortPageUtils.FILTERTYPE_CONTAINS;
        this.loginName = "";
        this.loginNameFilterType = FilterSortPageUtils.FILTERTYPE_CONTAINS;
        this.password = "";
        this.passwordFilterType = FilterSortPageUtils.FILTERTYPE_CONTAINS;
        this.validationStatusFilterType = FilterSortPageUtils.FILTERTYPE_SHOWALL;
        this.testStatusFilterType = FilterSortPageUtils.FILTERTYPE_SHOWALL;
    }

    public void copyValues(TestRosterFilter src) {
        this.lastName = src.getLastName();
        this.lastNameFilterType = src.getLastNameFilterType();
        this.firstName = src.getFirstName();
        this.firstNameFilterType = src.getFirstNameFilterType();
        this.loginName = src.getLoginName();
        this.loginNameFilterType = src.getLoginNameFilterType();
        this.password = src.getPassword();
        this.passwordFilterType = src.getPasswordFilterType();
        this.validationStatusFilterType = src.getValidationStatusFilterType();
        this.testStatusFilterType = src.getTestStatusFilterType();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getLastName() {
        return this.lastName;
    }    
    public void setLastNameFilterType(String lastNameFilterType) {
        this.lastNameFilterType = lastNameFilterType;
    }
    public String getLastNameFilterType() {
        return this.lastNameFilterType;
    }    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return this.firstName;
    }    
    public void setFirstNameFilterType(String firstNameFilterType) {
        this.firstNameFilterType = firstNameFilterType;
    }
    public String getFirstNameFilterType() {
        return this.firstNameFilterType;
    }    
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getLoginName() {
        return this.loginName;
    }    
    public void setLoginNameFilterType(String loginNameFilterType) {
        this.loginNameFilterType = loginNameFilterType;
    }
    public String getLoginNameFilterType() {
        return this.loginNameFilterType;
    }        
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return this.password;
    }    
    public void setPasswordFilterType(String passwordFilterType) {
        this.passwordFilterType = passwordFilterType;
    }
    public String getPasswordFilterType() {
        return this.passwordFilterType;
    }    
    public void setValidationStatusFilterType(String validationStatusFilterType) {
        this.validationStatusFilterType = validationStatusFilterType;
    }
    public String getValidationStatusFilterType() {
        return this.validationStatusFilterType;
    }        
    public void setTestStatusFilterType(String testStatusFilterType) {
        this.testStatusFilterType = testStatusFilterType;
    }
    public String getTestStatusFilterType() {
        return this.testStatusFilterType;
    }    
    
} 
