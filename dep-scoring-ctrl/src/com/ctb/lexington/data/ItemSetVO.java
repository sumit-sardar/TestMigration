/*
 * ItemSetVO.java
 *
 * Created on September 27, 2002, 2:23 PM
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 */

package com.ctb.lexington.data;


import java.io.Serializable;
import java.util.Date;

import com.ctb.lexington.db.record.Persistent;

/**
 *
 * @author  sprakash
 * @version
 */
public class ItemSetVO implements Persistent, Serializable
{
    // static constants
    /**
    * This beans's static label to be used for identification.
    */
    public static final String VO_LABEL = "com.ctb.lexington.data.ItemSetVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    public static final String ITEM_SET_TYPE_RE = "RE";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_TYPE_TD = "TD";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_TYPE_TC = "TC";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_TYPE_SC = "SC";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_TYPE_AT = "AT";

    /** DOCUMENT ME! */
    public static final String PUBLISH_STATUS_PUBLISHED = "PB";

    /** DOCUMENT ME! */
    public static final String PUBLISH_STATUS_UNPUBLISHED = "UP";

    /** DOCUMENT ME! */
    public static final String PUBLISH_STATUS_IN_PROGRESS = "IP";

    public static final String PUBLISH_STATUS_REPUBLISH_IN_PROGRESS = "RP";
    
    /** DOCUMENT ME! */
    public static final String PUBLISH_STATUS_ERROR_PUBLISHING = "EP";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_MEDIA_TABLE_NAME = "ITEM_SET_MEDIA";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_MEDIA_COLUMN_PKEY_NAME = "ITEM_SET_ID";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_MEDIA_TYPE_IBXML = "IBXML";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_MEDIA_TYPE_IBSWF = "IBSWF";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_MEDIA_TYPE_IBPDF = "IBPDF";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_MEDIA_TYPE_AKSWF = "AKSWF";
    public static final String ITEM_SET_MEDIA_TYPE_AKPDF = "AKPDF";
    public static final String ITEM_SET_MEDIA_TYPE_CRIBPDF = "CRIBPDF";
    public static final String ITEM_SET_MEDIA_TYPE_CRAKPDF = "CRAKPDF";



    /** DOCUMENT ME! */
    public static final String ITEM_SET_MEDIA_PATH_IBS = "/oastd-web/versions/ibs/1.0/";
    public static final String ITEM_SET_MEDIA_PATH_IBS_WITH_ONLINE_CR = "/oastd-web/versions/ibs/2.0/";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_MEDIA_MIME_TYPE_SWF =
        "application/x-shockwave-flash";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_MEDIA_MIME_TYPE_PDF = "application/pdf";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_MEDIA_MIME_TYPE_XML = "text/xml";

    /** DOCUMENT ME! */
    public static final String ITEM_SET_DEFAULT_DESCRIPTION =
        "Please enter a text description here.";


    //private members
    protected Integer itemSetId         = null;
    protected String  itemSetType       = null;
    protected Integer itemSetCategoryId = null;
    protected String  itemSetName       = null;
    protected String  itemSetForm       = null;

    protected String  itemSetLevel      = null;
    protected String  minGrade          = null;
    protected String  maxGrade          = null;
    protected String  sample            = null;
    protected Integer timeLimit         = null;

    protected Integer breakTime         = null;
    protected String  mediaPath         = null;
    protected String  itemSetDisplayName= null;
    protected String  itemSetDescription= null;
    protected Integer itemSetRuleId     = null;

    protected String  extEmsItemSetId   = null;
    protected String  extCmsItemSetId   = null;
    protected Integer createdBy         = null;
    protected Date    createdDateTime   = null;
    protected Integer updatedBy         = null;

    protected Date    updatedDateTime   = null;
    protected String  activationStatus  = null;
    protected Integer productId         = null;
    protected Integer ownerCustomerId   = null;
    protected String  grade             = null;

    protected String  publishStatus     = null;
    protected Integer originalCreatedBy = null;
    
    protected String recommendedLevel   = null;
    
    protected String validationStatus   = null;
    protected String exemptions   = null;
    protected String absent   = null;
    
    //Added for TABE ADAPTIVE
    protected Double abilityScore = null;
    protected Double semScore = null;
    protected String objectiveScore = null;

    public ItemSetVO() { }

    public ItemSetVO(final Integer itemSetId) {
        this.itemSetId = itemSetId;
    }

    public ItemSetVO(final Integer itemSetId, final Integer productId) {
        this(itemSetId);

        this.productId = productId;
    }

    /**
	 * @return Returns the recommendedLevel.
	 */
	public String getRecommendedLevel() {
		return recommendedLevel;
	}

	/**
	 * @param recommendedLevel The recommendedLevel to set.
	 */
	public void setRecommendedLevel(String recommendedLevel) {
		this.recommendedLevel = recommendedLevel;
	}

	/**
     * Return itemSetId property
     */
    public  Integer getItemSetId()
    {
        return this.itemSetId;
    }
    public  void setItemSetId(Integer arg1)
    {
        this.itemSetId = arg1;
    }

    /**
     * Get itemSetType property
     */

    public  String getItemSetType()
    {
        return this.itemSetType;
    }
    public  void setItemSetType(String arg1)
    {
        this.itemSetType = arg1;
    }

    /**
     * Get itemSetCategory property
     */
    public  Integer getItemSetCategoryId()
    {
        return this.itemSetCategoryId;
    }
    public  void setItemSetCategoryId(Integer arg1)
    {
        this.itemSetCategoryId = arg1;
    }


    /**
     * Get itemSetName property
     */
    public  String getItemSetName()
    {
        return this.itemSetName;
    }
    public  void setItemSetName(String arg1)
    {
        this.itemSetName = arg1;
    }

    /**
     * Return itemSetLevel property
     */
    public  String getItemSetLevel()
    {
        return this.itemSetLevel;
    }
    public  void setItemSetLevel(String arg1)
    {
        this.itemSetLevel = arg1;
    }

    /**
     * Return itemSetForm property
     */
    public String getItemSetForm()
    {
        return this.itemSetForm;
    }
    public void setItemSetForm(String arg1)
    {
        this.itemSetForm = arg1;
    }

    /**
     * Return minGrade property
     */
    public  String getMinGrade()
    {
        return this.minGrade;
    }

    /**
     * Set minGrade property
     */
    public  void setMinGrade(String arg1)
    {
        this.minGrade = arg1;
    }

    /**
     * Return maxGrade property
     */
    public  String getMaxGrade()
    {
        return this.maxGrade;
    }

    /**
     * Set maxGrade property
     */
    public  void setMaxGrade(String arg1)
    {
        this.maxGrade = arg1;
    }

    /**
     * Return sample property
     */
    public  String getSample()
    {
        return this.sample;
    }

    /**
     * Set sample property
     */
    public  void setSample(String arg1)
    {
        this.sample = arg1;
    }

    /**
     * Return timeLimit property
     */
    public  Integer getTimeLimit()
    {
        return this.timeLimit;
    }

    /**
     * Set timeLimit property
     */
    public  void setTimeLimit(Integer arg1)
    {
        this.timeLimit = arg1;
    }

    /**
     * Return breakTime property
     */
    public  Integer getBreakTime()
    {
        return this.breakTime;
    }

    /**
     * Set breakTime property
     */
    public  void setBreakTime(Integer arg1)
    {
        this.breakTime = arg1;
    }

    /**
     * Return mediaPath property
     */
    public  String getMediaPath()
    {
        return this.mediaPath;
    }

    /**
     * Set mediaPath property
     */
    public  void setMediaPath(String arg1)
    {
        this.mediaPath = arg1;
    }

    /**
     * Return itemSetDisplayName property
     */
    public  String getItemSetDisplayName()
    {
        return this.itemSetDisplayName;
    }

    /**
     * Set itemSetDisplayName property
     */
    public  void setItemSetDisplayName(String arg1)
    {
        this.itemSetDisplayName = arg1;
    }

    /**
     * Return itemSetDescription property
     */
    public  String getItemSetDescription()
    {
        return this.itemSetDescription;
    }

    /**
     * Set itemSetDescription property
     */
    public  void setItemSetDescription(String arg1)
    {
        this.itemSetDescription = arg1;
    }

    /**
     * Return itemSetRuleId property
     */
    public  Integer getItemSetRuleId()
    {
        return this.itemSetRuleId;
    }

    /**
     * Set itemSetRuleId property
     */
    public  void setItemSetRuleId(Integer arg1)
    {
        this.itemSetRuleId = arg1;
    }

    /**
     * Return extEmsItemSetId property
     */
    public  String getExtEmsItemSetId()
    {
        if ( this.extEmsItemSetId != null )
            return this.extEmsItemSetId;
        else
            return new String( "" );
    }

    /**
     * Set extEmsItemSetId property
     */
    public  void setExtEmsItemSetId(String arg1)
    {
        this.extEmsItemSetId = arg1;
    }

    /**
     * Return extCmsItemSetId property
     */
    public  String getExtCmsItemSetId()
    {
        if ( this.extCmsItemSetId != null )
            return this.extCmsItemSetId;
        else
            return new String( "" );
    }

    /**
     * Set extCmsItemSetId property
     */
    public  void setExtCmsItemSetId(String arg1)
    {
        this.extCmsItemSetId = arg1;
    }

    /**
     * Return createdBy property
     */
    public  Integer getCreatedBy()
    {
        return this.createdBy;
    }

    /**
     * Set createdBy property
     */
    public  void setCreatedBy(Integer arg1)
    {
        this.createdBy = arg1;
    }

    /**
     * Return createdBy property
     */
    public  Integer getOriginalCreatedBy()
    {
        return this.originalCreatedBy;
    }

    /**
     * Set originalCreatedBy property
     */
    public  void setOriginalCreatedBy(Integer arg1)
    {
        this.originalCreatedBy = arg1;
    }

    /**
     * Return createdDateTime property
     */
    public  Date getCreatedDateTime()
    {
        return this.createdDateTime;
    }

    /**
     * Set createdDateTime property
     */
    public  void setCreatedDateTime(Date arg1)
    {
        this.createdDateTime = arg1;
    }

    /**
     * Return updatedBy property
     */
    public  Integer getUpdatedBy()
    {
        return this.updatedBy;
    }

    /**
     * Set updatedBy property
     */
    public  void setUpdatedBy(Integer arg1)
    {
        this.updatedBy = arg1;
    }

    /**
     * Return updatedDateTime property
     */
    public  Date getUpdatedDateTime()
    {
        return this.updatedDateTime;
    }

    /**
     * Set updatedDateTime property
     */
    public  void setUpdatedDateTime(Date arg1)
    {
        this.updatedDateTime = arg1;
    }

    /**
     * Return activationStatus property
     */
    public  String getActivationStatus()
    {
        return this.activationStatus;
    }

    /**
     * Set activationStatus property
     */
    public  void setActivationStatus(String arg1)
    {
        this.activationStatus = arg1;
    }

    /**
     * Return publishStatus property
     */
    public  String getPublishStatus()
    {
        return this.publishStatus;
    }

    /**
     * Set publishStatus property
     */
    public  void setPublishStatus(String arg1)
    {
        this.publishStatus = arg1;
    }

    public void setProductId(Integer productId)
    {
        this.productId = productId;
    }
    public Integer getProductId()
    {
       return this.productId;
    }

    /* add when added to db
    public void setIsFinished(Boolean isFinished);
    public Boolean getIsFinished();
     **/

    public void setOwnerCustomerId(Integer ownerCustomerId)
    {
        this.ownerCustomerId = ownerCustomerId;
    }
    public Integer getOwnerCustomerId()
    {
        return this.ownerCustomerId;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }
    public String getGrade()
    {
        return this.grade;
    }

    //...................................................................helpers

    /**
     * to be used by callers having itemSetVO instantiated.
     * @return true if it is CTB Test; false otherwise.
     */
    public boolean isItemSetTypeCTBtest()
    {
        return( isItemSetTypeCTBtest( this.itemSetType ));
    }

    /**
     * to be used by callers NOT having itemSetVO instantiated.
     * @return true if it is CTB Test; false otherwise.
     */
    public static boolean isItemSetTypeCTBtest( String itemSetType )
    {
        if( itemSetType.equals( ITEM_SET_TYPE_TC )){
            return true;
        }else{
            return false;
        }
    }

    /**
     * @return isItemSetTypeCTBtest(); i.e. synonymous.
     */
    public boolean isItemSetTypeCABtest()
    {
        return( isItemSetTypeCTBtest() );
    }

    /**
     * @return isItemSetTypeCTBtest( String itemSetType ); i.e. synonymous.
     */
    public static boolean isItemSetTypeCABtest( String itemSetType )
    {
        return( isItemSetTypeCTBtest( itemSetType ));
    }


    /**
     * to be used by callers having itemSetVO instantiated.
     * @return true if it is MY Test (i.e. was assembled); false otherwise.
     */
    public boolean isItemSetTypeMYtest()
    {
        return( isItemSetTypeMYtest( this.itemSetType ));
    }

    /**
     * to be used by callers NOT having itemSetVO instantiated.
     * @return true if it is MY Test (i.e. was assembled); false otherwise.
     */
    public static boolean isItemSetTypeMYtest( String itemSetType )
    {
        if( itemSetType.equals( ITEM_SET_TYPE_AT )){
            return true;
        }else{
            return false;
        }
    }

    /**
     * @return isItemSetTypeMYtest(); i.e. synonymous.
     */
    public boolean isItemSetTypeAssembledTest()
    {
        return( isItemSetTypeCTBtest() );
    }

    /**
     * @return isItemSetTypeMYtest( String itemSetType ); i.e. synonymous.
     */
    public static boolean isItemSetTypeAssembledTest( String itemSetType )
    {
        return( isItemSetTypeCTBtest( itemSetType ));
    }

	public String getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(String validationStatus) {
		this.validationStatus = validationStatus;
	}

	public String getExemptions() {
		return exemptions;
	}

	public void setExemptions(String exemptions) {
		this.exemptions = exemptions;
	}

	public String getAbsent() {
		return absent;
	}

	public void setAbsent(String absent) {
		this.absent = absent;
	}

	public String getObjectiveScore() {
		return objectiveScore;
	}

	public void setObjectiveScore(String objectiveScore) {
		this.objectiveScore = objectiveScore;
	}

	public Double getAbilityScore() {
		return abilityScore;
	}

	public void setAbilityScore(Double abilityScore) {
		this.abilityScore = abilityScore;
	}

	public Double getSemScore() {
		return semScore;
	}

	public void setSemScore(Double semScore) {
		this.semScore = semScore;
	}

}