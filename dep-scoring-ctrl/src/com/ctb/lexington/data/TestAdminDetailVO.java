/*
 * TestAdminDetailVO.java
 *
 * Created on March 10, 2003, 3:27 PM
 */

package com.ctb.lexington.data;

/**
 *
 * @author  Wen-Jin_Chang
 */
import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class TestAdminDetailVO extends TestAdminVO implements Serializable
{
    private Calendar DateScored;
    private String ScoredBy;
    private boolean haveStudentsLoggedIn = false;
    private boolean haveStudentsCompletedTest = false;
    private boolean isReportable = false;
    private boolean tabularScores = false;
    private ItemSetVO itemSetVO = null;
    private String productName = null;
    private String brandingTypeCode = null;
    private List scoreTypeCodes;
	private String hasCommonStudents = null;
    
    //Constants
    public static final String VO_LABEL = "com.ctb.lexington.bean.TestAdminDetailVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    /** Creates a new instance of TestAdminDetailVO */
    public TestAdminDetailVO() {
    }

	public List getScoreTypeCodes()
	{
		return this.scoreTypeCodes;
	}

	public void setScoreTypeCodes( List scoreTypeCodes_ )
	{
		this.scoreTypeCodes = scoreTypeCodes_;
		if(scoreTypeCodes_ != null && scoreTypeCodes.size() > 0) {
			setTabularScores(true);
		}else {
			setTabularScores(false);
		}
	}

    public Calendar getDateScored()
    {
        return this.DateScored;
    }

    public void setDateScored( Calendar DateScored_ )
    {
        this.DateScored = DateScored_;
    }

    public String getScoredBy()
    {
        return this.ScoredBy;
    }

    public void setScoredBy( String ScoredBy_ )
    {
        this.ScoredBy = ScoredBy_;
    }

    /**
     * Gets the haveStudentsCompletedTest attribute of the
     * TestAdminVO object
     *
     * @return The haveStudentsCompletedTest value
     */
    public boolean getHaveStudentsCompletedTest()
    {
        return this.haveStudentsCompletedTest;
    }

    /**
     * Sets the haveStudentsCompletedTest attribute of the
     * TestAdminVO object
     *
     * @param haveStudentsCompletedTest_ The new haveStudentsCompletedTest value
     */
    public void setHaveStudentsCompletedTest(boolean haveStudentsCompletedTest_)
    {
        this.haveStudentsCompletedTest = haveStudentsCompletedTest_;
    }

    /**
     * sets the haveStudentsLoggedIn property
     *
     * @param haveStudentsLoggedIn
     */
    public void setHaveStudentsLoggedIn(boolean haveStudentsLoggedIn) {
        this.haveStudentsLoggedIn = haveStudentsLoggedIn;
    }

    /**
     * gets the haveStudentsLoggedIn property
     *
     * @return
     */
    public boolean getHaveStudentsLoggedIn() {
        return haveStudentsLoggedIn;
    }

    /**
     * sets the isReportable property
     *
     * @param isReportable
     */
    public void setIsReportable(boolean isReportable) {
        this.isReportable = isReportable;
    }

    /**
     * gets the isReportable property
     *
     * @return
     */
    public boolean getIsReportable() {
        return isReportable;
    }
	/**
	 * Returns the itemSetVO.
	 * @return ItemSetVO
	 */
	public ItemSetVO getItemSetVO()
	{
		return itemSetVO;
	}

	/**
	 * Sets the itemSetVO.
	 * @param itemSetVO The itemSetVO to set
	 */
	public void setItemSetVO(ItemSetVO itemSetVO)
	{
		this.itemSetVO = itemSetVO;
	}

	/**
	 * Returns the productName.
	 * @return String
	 */
	public String getProductName()
	{
		return productName;
	}

	/**
	 * Sets the productName.
	 * @param productName The productName to set
	 */
	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	
	/**
	 * Returns the brandingTypeCode.
	 * @return String
	 */
	public String getBrandingTypeCode()
	{
		return brandingTypeCode;
	}

	/**
	 * Sets the brandingTypeCode.
	 * @param brandingTypeCode The brandingTypeCode to set
	 */
	public void setBrandingTypeCode(String brandingTypeCode)
	{
		this.brandingTypeCode = brandingTypeCode;
	}
	
	/**
	 * Indicates whether this admin has completed
	 * students in common with another admin.
	 * Used by the AchievementSessionPager
	 * @return String
	 */
	public String getHasCommonStudents()
	{
		return hasCommonStudents;
	}

	/**
	 * Indicates whether this admin has completed
	 * students in common with another admin.
	 * Used by the AchievementSessionPager
	 * @param hasCommonStudents 
	 */
	public void setHasCommonStudents(String hasCommonStudents_)
	{
		this.hasCommonStudents = hasCommonStudents_;
	}

	/**
	 * @return Returns the hasTabularScores.
	 */
	public boolean getTabularScores() {
		return tabularScores;
	}

	/**
	 * @param hasTabularScores The hasTabularScores to set.
	 */
	public void setTabularScores(boolean hasTabularScores) {
		this.tabularScores = hasTabularScores;
	}

}
