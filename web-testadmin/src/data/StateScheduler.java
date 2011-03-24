package data; 

import java.util.List;

public class StateScheduler implements java.io.Serializable  
{ 
    static final long serialVersionUID = 1L;

    private String productType;
    private String testName;
    private String autoLocator;
    private String locatorTAC;
    private List testList;
    private List subtests; 
    // START- Added for defect #65862
    private String showLevelOrGrade;
    private boolean gradeFlag ;
    // END- Added for defect #65862
    //Added for license which is in use during select test cancel
    private Integer productId; 
    
     
    /*Added for Random Distractor 
      which will use in cancel action
    */ 
    private String isRandomize = null; 
   

    public StateScheduler() 
    {
        this.productType = "genericProductType";
        this.testName = "";
        this.autoLocator = "";
        this.locatorTAC = "";
        this.testList = null;
        this.subtests = null;
        // START- Added for defect #65862
        this.showLevelOrGrade = "";
        this.gradeFlag = false;
        // END- Added for defect #65862
    }    


    public String getProductType() {
        return this.productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getTestName() {
        return this.testName;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getAutoLocator() {
        return this.autoLocator;
    }
    public void setAutoLocator(String autoLocator) {
        this.autoLocator = autoLocator;
    }

    public String getLocatorTAC() {
        return this.locatorTAC;
    }
    public void setLocatorTAC(String locatorTAC) {
        this.locatorTAC = locatorTAC;
    }

    public List getTestList() {
        return this.testList;
    }
    public void setTestList(List testList) {
        this.testList = testList;
    }

    public List getSubtests() {
        return this.subtests;
    }
    public void setSubtests(List subtests) {
        this.subtests = subtests;
    }
    
    /**
	 * @return Returns the IsRandomize.
     * which will use in cancel action
	 */
    
    public String getIsRandomize() {
		return isRandomize;
	}
    
    /**
	 * @param IsRandomize The IsRandomize to set.
     * which will use in cancel action
	 */
	public void setIsRandomize(String isRandomize) {
           this.isRandomize = isRandomize;
    }
    
     /**
	 * @return Returns the productId.
     * which will use in test cancel action
	 */
    
    public Integer getProductId() {
		return productId;
	}
    
    /**
	 * @param IsRandomize The productId to set.
     * which will use in test cancel action
	 */
	public void setProductId(Integer productId) {
           this.productId = productId;
    }


	public String getShowLevelOrGrade() {
		return showLevelOrGrade;
	}


	public void setShowLevelOrGrade(String showLevelOrGrade) {
		this.showLevelOrGrade = showLevelOrGrade;
	}


	public boolean isGradeFlag() {
		return gradeFlag;
	}


	public void setGradeFlag(boolean gradeFlag) {
		this.gradeFlag = gradeFlag;
	}
    
    
} 
