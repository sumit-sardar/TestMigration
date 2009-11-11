package com.ctb.lexington.data;


/*
 * TestDetailVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author  rmariott
 * @version 
 */
public class TestDetailVO extends TestVO implements java.io.Serializable {

    public static final String VO_LABEL = "com.ctb.lexington.valueobject.TestDetailVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.valueobject.TestDetailVO.array";
    
    private List categoryValuesList = new ArrayList();
    private String categoryName = null;
    private Integer numOfQuestions;
    private Map formsMap = null;
    private Map levelsMap = null;
    private boolean isShared = false;
    private boolean isShareable = false;
    private Integer productId = null;
    private String productType = null;
    private boolean needsToBeRepublished = false;

    /** Creates new TestDetailVO */
    public TestDetailVO() {
        
    }
    
    public boolean getIsShared()
    {
        return this.isShared;
    }
    
    public void setNeedsToBeRepublished( boolean needsToBeRepublished_ )
    {
        this.needsToBeRepublished = needsToBeRepublished_;
    }
    
    public boolean getNeedsToBeRepublished()
    {
        return this.needsToBeRepublished;
    }
    
    public void setIsShareable( boolean shared_ )
    {
        this.isShareable = shared_;
    }
    
        public boolean getIsShareable()
    {
        return this.isShareable;
    }
    
    public void setIsShared( boolean shared_ )
    {
        this.isShared = shared_;
    }

    
    //-- Get/Set Methods --//
    
    /**
     * Get this property from this bean instance.
     *
     * @return List of Strings representing all the strands that this test's 
     * questions cover.
     */
    public Map getFormsMap()
    {
        return this.formsMap;
    }
    
    /**
     * Set the value of this property.
     *
     * @param strandsList_ List of Strings representing all the strands that 
     * this test's questions cover.
     *
     * @return void
     */
    public void setFormsMap(Map formsMap_)
    {
        this.formsMap = formsMap_;
    }
    
    public Map getLevelsMap()
    {
        return this.levelsMap;
    }
    
    public void setLevelsMap(Map levelsMap_)
    {
        this.levelsMap = levelsMap_;
    }
    
    
    /**
     * Get this property from this bean instance.
     *
     * @return List of Strings representing all the category values 
     * that this test's questions cover. This is the category at the 
     * scoring/reporting level.
     */
    public List getCategoryValuesList()
    {
        return this.categoryValuesList;
    }
    
    /**
     * Set the value of this property.
     *
     * @param strandsList_ List of Strings representing all the strands that 
     * this test's questions cover.
     *
     * @return void
     */
    public void setCategoryValuesList(List categoryValuesList_)
    {
        this.categoryValuesList = categoryValuesList_;
    }
    
    
    /**
     * Get this property from this bean instance.
     *
     * @return String representing the name of the category whose values are
     * in the categoryValuesList.
     */
    public String getCategoryName()
    {
        return this.categoryName;
    }
    
    /**
     * Set the value of this property.
     *
     * @param categoryName_ name of the category whose valuesList is in this VO.
     *
     * @return void
     */
    public void setCategoryName(String categoryName_)
    {
        this.categoryName = categoryName_;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public Integer getNumOfQuestions()
    {
        return this.numOfQuestions;
    }
    
    /**
     * Set the value of this property.
     *
     * @param numOfQuestions_ The value to set the property to.
     * @return void
     */
    public void setProductId(Integer productId_)
    {
        this.productId = productId_;
    }   
    
        /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public Integer getProductId()
    {
        return this.productId;
    }
    
    /**
     * Set the value of this property.
     *
     * @param numOfQuestions_ The value to set the property to.
     * @return void
     */
    public void setNumOfQuestions(Integer numOfQuestions_)
    {
        this.numOfQuestions = numOfQuestions_;
    }   

    public String getProductType()
    {
        return this.productType;
    }
   
    public void setProductType( String productType_ )
    {
        this.productType = productType_;
    }
}
