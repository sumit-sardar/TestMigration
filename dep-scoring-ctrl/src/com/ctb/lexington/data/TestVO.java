package com.ctb.lexington.data;


/*
 * TestVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */


import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.ctb.lexington.util.CTBConstants;

/**
 *
 * @author  rmariott
 * @version 
 */
public class TestVO extends ItemSetVO implements java.io.Serializable {

    public static final String VO_LABEL = "com.ctb.lexington.data.TestVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.data.TestVO.array";
    
    private boolean isTABE = false;
    private String creatorDisplayName;
    private String originalCreatorDisplayName;
    private Integer numOfForms;
    private Integer numOfLevels;
    private String testType;
    private boolean hasCRItems = false; 
    private boolean hasCRItemsOnly = false;
    private static final HashMap testTypes = new HashMap();
    private String hasOutOfDateItems = "FALSE";

    static
    {
        testTypes.put( CTBConstants.REPOSITORY_CTB_TESTS_LABEL, "TC");
        testTypes.put( CTBConstants.REPOSITORY_MY_TESTS_LABEL, "AT");
        testTypes.put( CTBConstants.REPOSITORY_SHARED_TESTS_LABEL, "AT");
        testTypes.put( CTBConstants.REPOSITORY_UNFINISHED_TESTS_LABEL, "AT");
    }

    /** Creates new TestVO */
    public TestVO() 
    {
        super();
    }
    
    
    //-- Get/Set Methods --//
    
    public void setHasOutOfDateItems(String hasOutOfDateItems_){
    	this.hasOutOfDateItems = hasOutOfDateItems_;
    }
    
    public String getHasOutOfDateItems(){
    	return this.hasOutOfDateItems;
    }
    
    public void setNeedsToBeRepublished( boolean needsToBeRepublished_ )
    {
        this.hasOutOfDateItems = needsToBeRepublished_ ? "TRUE" : "FALSE";
    }
    
    public boolean getNeedsToBeRepublished()
    {
        return this.hasOutOfDateItems.equals("TRUE");
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public Integer getTestId()
    {
        return getItemSetId();
    }
    
    /**
     * Set the value of this property.
     *
     * @param testId_ The value to set the property to.
     * @return void
     */
    public void setTestId(Integer testId_)
    {
        setItemSetId( testId_ );
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getGrade()
    {
        return super.getGrade();
    }
    
    /**
     * Set the value of this property.
     *
     * @param testId_ The value to set the property to.
     * @return void
     */
    public void setGrade(String grade_)
    {
        super.setGrade( grade_ );
    }
    
    /**
     * Return itemSetType property
     */
    public  String getTestType()
    {
        if ( testType != null )
            return testType;
        else
        {
            String type = "";
            Iterator it = testTypes.keySet().iterator();
            while ( it.hasNext() )
            {
                String keyName = (String) it.next();
                String value = (String) testTypes.get( keyName );
                if ( value.equals( getItemSetType() ) )
                    type = keyName;
            }
            return type;
        }
    }   

    /**
     * Set testType property.
     * input : String: should be one of the keys of the testTypes HashMap.
     */
    public  void setTestType(String arg1)
    {
        setItemSetType( (String) testTypes.get( arg1 ) );
        testType = arg1;
    }
    
    /**
     * Return publishStatus property
     */
    public  String getPublishStatus()
    {
        return super.getPublishStatus();
    }

    /**
     * Set publishStatus property
     */
    public  void setPublishStatus(String arg1)
    {
        super.setPublishStatus( arg1 );
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getLevel()
    {
        return getItemSetLevel();
    }
    
    /**
     * Set the value of this property.
     *
     * @param testId_ The value to set the property to.
     * @return void
     */
    public void setLevel(String level_)
    {
        setItemSetLevel( level_ );
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getTestName()
    {
        return getItemSetName();
    }
    
    /**
     * Set the value of this property.
     *
     * @param testName_ The value to set the property to.
     * @return void
     */
    public void setTestName(String testName_)
    {
        setItemSetName( testName_ );
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public Integer getCreatedBy()
    {
        return super.getCreatedBy();
    }
    
    /**
     * Set the value of this property.
     *
     * @param createdBy_ The value to set the property to.
     * @return void
     */
    public void setCreatedBy(Integer createdBy_)
    {
        super.setCreatedBy( createdBy_ );
    }
    
    
    /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public Integer getOriginalCreatedBy()
    {
        return super.getOriginalCreatedBy();
    }
    
    /**
     * Set the value of this property.
     *
     * @param originalCreatedBy_ The value to set the property to.
     * @return void
     */
    public void setOriginalCreatedBy(Integer originalCreatedBy_)
    {
        super.setOriginalCreatedBy( originalCreatedBy_ );
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return Date The value of the property.
     */
    public Date getCreatedDateTime()
    {
        return super.getCreatedDateTime();
    }
    
    /**
     * Set the value of this property.
     *
     * @param createdDateTime_ The value to set the property to.
     * @return void
     */
    public void setCreatedDateTime(Date createdDateTime_)
    {
        super.setCreatedDateTime( createdDateTime_ );
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getCreatorDisplayName()
    {
        return this.creatorDisplayName;
    }
    
    /**
     * Set the value of this property.
     *
     * @param creatorDisplayName_ The value to set the property to.
     * @return void
     */
    public void setCreatorDisplayName(String creatorDisplayName_)
    {
        this.creatorDisplayName = creatorDisplayName_;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getOriginalCreatorDisplayName()
    {
        return this.originalCreatorDisplayName;
    }
    
    /**
     * Set the value of this property.
     *
     * @param orignialCreatorDisplayName_ The value to set the property to.
     * @return void
     */
    public void setOriginalCreatorDisplayName(String originalCreatorDisplayName_)
    {
        this.originalCreatorDisplayName = originalCreatorDisplayName_;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public Integer getProductId()
    {
        return super.getProductId();
    }
    
    /**
     * Set the value of this property.
     *
     * @param productId_ The value to set the property to.
     * @return void
     */
    public void setProductId(Integer productId_)
    {
        super.setProductId( productId_ );
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getTestDescription()
    {
        return getItemSetDescription();
    }
    
    /**
     * Set the value of this property.
     *
     * @param testDescription_ The value to set the property to.
     * @return void
     */
    public void setTestDescription(String testDescription_)
    {
        setItemSetDescription( testDescription_ );
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getTestDisplayName()
    {
        return getItemSetDisplayName();
    }
    
    /**
     * Set the value of this property.
     *
     * @param testDisplayName_ The value to set the property to.
     * @return void
     */
    public void setTestDisplayName(String testDisplayName_)        
    {
        setItemSetDisplayName( testDisplayName_ ) ;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getForm()
    {
        return getItemSetForm();
    }
    
    /**
     * Set the value of this property.
     *
     * @param testDisplayName_ The value to set the property to.
     * @return void
     */
    public void setForm(String form_)        
    {
        setItemSetForm( form_ ) ;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public Integer getNumOfForms()
    {
        return this.numOfForms;
    }
    
    /**
     * Set the value of this property.
     *
     * @param numOfForms_ The value to set the property to.
     * @return void
     */
    public void setNumOfForms(Integer numOfForms_)
    {
        this.numOfForms = numOfForms_;
    }
    
    public Integer getNumOfLevels()
    {
        return this.numOfLevels;
    }
    public void setNumOfLevels(Integer numOfLevels_)
    {
        this.numOfLevels = numOfLevels_;
    }

    public boolean getHasCRItems()
    {
        return this.hasCRItems;
    }
    
    public void setHasCRItems( boolean hasCRItems_ )
    {
        this.hasCRItems = hasCRItems_;
    }
    
    public boolean getHasCRItemsOnly()
    {
        return this.hasCRItemsOnly;
    }
    
    public void setHasCRItemsOnly( boolean hasCRItemsOnly_ )
    {
        this.hasCRItemsOnly = hasCRItemsOnly_;
    }
       
	/**
	 * @return Returns the isTABE.
	 */
	public boolean isTABE() {
		return isTABE;
	}

	/**
	 * @param isTABE The isTABE to set.
	 */
	public void setTABE(boolean isTABE) {
		this.isTABE = isTABE;
	}

}
