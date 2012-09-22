package com.ctb.testSessionInfo.dto; 

import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.testSessionInfo.utils.TestSessionUtils;

import java.util.Date;

public class TestSessionVO implements java.io.Serializable
{     
    static final long serialVersionUID = 1L;

    private Integer testAdminId = null;
    private String testAdminName = null;
    private String testName = null;
    private String sessionNumber = null;
    private Date loginStartDate = null;
    private Date loginEndDate = null;
    private String reportable = null;
    private String accessCode = null;
    private String creatorOrgNodeName = null;
    private String creatorOrgNodeCategoryName = null;
    private String testAdminStatus = null;
    private String showStudentFeedback = null;
    //Change for RD
    private String randomDistractorStatus = null;
    //Change for License Management
    private String isRegisterStudentEnable = null;
    private Integer productId = null;
    private Integer parentProductId = null;
    private String licenseEnabled = null;
    //START - form recommendation
    private Boolean isStudentInTestSession = false;
     //END - form recommendation
    private String AssignedRole;
    private Integer creatorOrgNodeId;
    //For completed Tests
    private String isTestSessionDataExported ;
    private String loginStartDateString;
    private String loginEndDateString;
    
    private String productType ;
    private boolean isSTabeProduct = false;
    private boolean isSTabeAdaptiveProduct = false;
    private String copyable;
    
    public String getCopyable() {
		return copyable;
	}
	public void setCopyable(String copyable) {
		this.copyable = copyable;
	}
	public String getLoginStartDateString() {
		return loginStartDateString;
	}
	public void setLoginStartDateString(String loginStartDateString) {
		this.loginStartDateString = loginStartDateString;
	}
	public String getLoginEndDateString() {
		return loginEndDateString;
	}
	public void setLoginEndDateString(String loginEndDateString) {
		this.loginEndDateString = loginEndDateString;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public boolean isSTabeProduct() {
		return isSTabeProduct;
	}
	public void setSTabeProduct(boolean isSTabeProduct) {
		this.isSTabeProduct = isSTabeProduct;
	}
	public boolean isSTabeAdaptiveProduct() {
		return isSTabeAdaptiveProduct;
	}
	public void setSTabeAdaptiveProduct(boolean isSTabeAdaptiveProduct) {
		this.isSTabeAdaptiveProduct = isSTabeAdaptiveProduct;
	}
	/**
	 * @return the assignedRole
	 */
	public String getAssignedRole() {
		return AssignedRole;
	}
	/**
	 * @param assignedRole the assignedRole to set
	 */
	public void setAssignedRole(String assignedRole) {
		AssignedRole = assignedRole;
	}
	public TestSessionVO() 
    {
    }    
    public TestSessionVO(TestSession ts) 
    {
        this.testAdminId =  ts.getTestAdminId();
        this.testAdminName = ts.getTestAdminName();
        this.testName = ts.getTestName();
        this.sessionNumber = ts.getSessionNumber();        
        this.loginStartDate = ts.getLoginStartDate();
        this.loginEndDate = ts.getLoginEndDate();
        this.reportable = ts.getReportable();   
        this.accessCode = ts.getAccessCode();
        this.creatorOrgNodeName = ts.getCreatorOrgNodeName();
        this.creatorOrgNodeId = ts.getCreatorOrgNodeId();
        this.creatorOrgNodeCategoryName = ts.getCreatorOrgNodeCategoryName();
        this.testAdminStatus = ts.getTestAdminStatus();
        this.showStudentFeedback = ts.getShowStudentFeedback();
        //Change for RD
        this.randomDistractorStatus = ts.getIsRandomize()!=null ? 
                ts.getIsRandomize().equals("Y") ? "Yes" : "No" : null;
                
        //Change for License Management
         this.productId = ts.getProductId();  
         this.parentProductId = ts.getParentProductId();
         this.licenseEnabled = ts.getLicenseEnabled();
         this.isStudentInTestSession = ts.getIsStudentInTestSession();   //START - form recommendation
         this.AssignedRole = ts.getAssignedRole();
         this.loginEndDateString = ts.getLoginEndDateString();
         this.loginStartDateString = ts.getLoginStartDateString();
         if(ts.getProductType()!=null){
        	 this.productType = TestSessionUtils.getProductType(ts.getProductType());
             this.isSTabeProduct         = TestSessionUtils.isTabeProduct(this.productType);
             this.isSTabeAdaptiveProduct = TestSessionUtils.isTabeAdaptiveProduct(this.productType);
         }
         this.copyable = ts.getCopyable();
    }     
    public String getAccessCode() {
        return this.accessCode;
    }
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
    public Date getLoginEndDate() {
        return this.loginEndDate;
    }
    public void setLoginEndDate(Date loginEndDate) {
        this.loginEndDate = loginEndDate;
    }
    public Integer getTestAdminId() {
        return this.testAdminId;
    }
    public void setId(Integer testAdminId) {
        this.testAdminId = testAdminId;
    }
    public String getSessionNumber() {
        return this.sessionNumber;
    }
    public void setSessionNumber(String sessionNumber) {
        this.sessionNumber = sessionNumber;
    }
    public String getTestAdminName() {
        return this.testAdminName;
    }
    public void setTestAdminName(String testAdminName) {
        this.testAdminName = testAdminName;
    }
    public String getTestAdminStatus() {
        return this.testAdminStatus;
    }
    public void setTestAdminStatus(String testAdminStatus) {
        this.testAdminStatus = testAdminStatus;
    }
    public Date getLoginStartDate() {
        return this.loginStartDate;
    }
    public void setLoginStartDate(Date loginStartDate) {
        this.loginStartDate = loginStartDate;
    }
    public String getTestName() {
        return this.testName;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }                
    public String getReportable() {
        if ((this.reportable != null) && this.reportable.equals("T"))
            return "true";
        return "false";
    }
    public void setReportable(String reportable) {
        this.reportable = reportable;
    }                
    public String getCreatorOrgNodeName() {
        return this.creatorOrgNodeName;
    }
    public void setCreatorOrgNodeName(String creatorOrgNodeName) {
        this.creatorOrgNodeName = creatorOrgNodeName;
    }                
    public String getCreatorOrgNodeCategoryName() {
        return this.creatorOrgNodeCategoryName;
    }
    public void setCreatorOrgNodeCategoryName(String creatorOrgNodeCategoryName) {
        this.creatorOrgNodeCategoryName = creatorOrgNodeCategoryName;
    }                
    public String getShowStudentFeedback() {
        return this.showStudentFeedback;
    }
    public void setShowStudentFeedback(String showStudentFeedback) {
        this.showStudentFeedback = showStudentFeedback;
    } 
    
    /**
     *  return randomDistractorStatus
     */
    public String getRandomDistractorStatus() {
        return this.randomDistractorStatus;
    }
    
    /**
     * set randomDistractorStatus
     */
    public void setRandomDistractorStatus(String randomDistractorStatus) {
        this.randomDistractorStatus = randomDistractorStatus;
    } 
    
    /**
     *  return isRegisterStudentEnable
     */
    public String getIsRegisterStudentEnable() {
		return isRegisterStudentEnable;
	}
    
    /**
     * set isRegisterStudentEnable
     */
	public void setIsRegisterStudentEnable(String isRegisterStudentEnable) {
		this.isRegisterStudentEnable = isRegisterStudentEnable;
	} 
    
    /**
     * @return Returns the productId.
     */
    public Integer getProductId() {
        return productId;
    }
    /**
     * @param productId The productId to set.
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }  
    
    /**
	 * @return Returns the ParentProductId.
	 */
    
    public Integer getParentProductId() {
		return parentProductId;
	}
    
    /**
	 * @param ParentProductId The ParentProductId to set.
	 */
	public void setParentProductId(Integer parentProductId) {
		this.parentProductId = parentProductId;
	}
    
    
    /**
	 * @return Returns the LicenseEnabled.
	 */
    
    public String getLicenseEnabled() {
		return licenseEnabled;
	}
    
    /**
	 * @param LicenseEnabled The LicenseEnabled to set.
	 */
	public void setLicenseEnabled(String licenseEnabled) {
		this.licenseEnabled = licenseEnabled;
	}
	/**
	 * @return the isStudentInTestSession
	 */
	public Boolean getIsStudentInTestSession() {
		return isStudentInTestSession;
	}
	/**
	 * @param isStudentInTestSession the isStudentInTestSession to set
	 */
	public void setIsStudentInTestSession(Boolean isStudentInTestSession) {
		this.isStudentInTestSession = isStudentInTestSession;
	}
	/**
	 * @return the creatorOrgNodeId
	 */
	public Integer getCreatorOrgNodeId() {
		return creatorOrgNodeId;
	}
	/**
	 * @param creatorOrgNodeId the creatorOrgNodeId to set
	 */
	public void setCreatorOrgNodeId(Integer creatorOrgNodeId) {
		this.creatorOrgNodeId = creatorOrgNodeId;
	}
	/**
	 * @return the isTestSessionDataExported
	 */
	public String getIsTestSessionDataExported() {
		return isTestSessionDataExported;
	}
	/**
	 * @param isTestSessionDataExported the isTestSessionDataExported to set
	 */
	public void setIsTestSessionDataExported(String isTestSessionDataExported) {
		this.isTestSessionDataExported = isTestSessionDataExported;
	}
   
               
} 
